import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductCancellationsFormService, ProductCancellationsFormGroup } from './product-cancellations-form.service';
import { IProductCancellations } from '../product-cancellations.model';
import { ProductCancellationsService } from '../service/product-cancellations.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

@Component({
  selector: 'jhi-product-cancellations-update',
  templateUrl: './product-cancellations-update.component.html',
})
export class ProductCancellationsUpdateComponent implements OnInit {
  isSaving = false;
  productCancellations: IProductCancellations | null = null;

  orderItemsSharedCollection: IOrderItem[] = [];
  ordersSharedCollection: IOrder[] = [];

  editForm: ProductCancellationsFormGroup = this.productCancellationsFormService.createProductCancellationsFormGroup();

  constructor(
    protected productCancellationsService: ProductCancellationsService,
    protected productCancellationsFormService: ProductCancellationsFormService,
    protected orderItemService: OrderItemService,
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productCancellations }) => {
      this.productCancellations = productCancellations;
      if (productCancellations) {
        this.updateForm(productCancellations);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productCancellations = this.productCancellationsFormService.getProductCancellations(this.editForm);
    if (productCancellations.id !== null) {
      this.subscribeToSaveResponse(this.productCancellationsService.update(productCancellations));
    } else {
      this.subscribeToSaveResponse(this.productCancellationsService.create(productCancellations));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductCancellations>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productCancellations: IProductCancellations): void {
    this.productCancellations = productCancellations;
    this.productCancellationsFormService.resetForm(this.editForm, productCancellations);

    this.orderItemsSharedCollection = this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(
      this.orderItemsSharedCollection,
      productCancellations.orderItem
    );
    this.ordersSharedCollection = this.orderService.addOrderToCollectionIfMissing<IOrder>(
      this.ordersSharedCollection,
      productCancellations.order
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.productCancellations?.orderItem)
        )
      )
      .subscribe((orderItems: IOrderItem[]) => (this.orderItemsSharedCollection = orderItems));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.productCancellations?.order)))
      .subscribe((orders: IOrder[]) => (this.ordersSharedCollection = orders));
  }
}

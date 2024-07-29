import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PriceHistoryFormService, PriceHistoryFormGroup } from './price-history-form.service';
import { IPriceHistory } from '../price-history.model';
import { PriceHistoryService } from '../service/price-history.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-price-history-update',
  templateUrl: './price-history-update.component.html',
})
export class PriceHistoryUpdateComponent implements OnInit {
  isSaving = false;
  priceHistory: IPriceHistory | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: PriceHistoryFormGroup = this.priceHistoryFormService.createPriceHistoryFormGroup();

  constructor(
    protected priceHistoryService: PriceHistoryService,
    protected priceHistoryFormService: PriceHistoryFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceHistory }) => {
      this.priceHistory = priceHistory;
      if (priceHistory) {
        this.updateForm(priceHistory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priceHistory = this.priceHistoryFormService.getPriceHistory(this.editForm);
    if (priceHistory.id !== null) {
      this.subscribeToSaveResponse(this.priceHistoryService.update(priceHistory));
    } else {
      this.subscribeToSaveResponse(this.priceHistoryService.create(priceHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriceHistory>>): void {
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

  protected updateForm(priceHistory: IPriceHistory): void {
    this.priceHistory = priceHistory;
    this.priceHistoryFormService.resetForm(this.editForm, priceHistory);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      priceHistory.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.priceHistory?.product))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}

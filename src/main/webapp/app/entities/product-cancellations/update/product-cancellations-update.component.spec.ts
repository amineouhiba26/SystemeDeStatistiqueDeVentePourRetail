import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductCancellationsFormService } from './product-cancellations-form.service';
import { ProductCancellationsService } from '../service/product-cancellations.service';
import { IProductCancellations } from '../product-cancellations.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

import { ProductCancellationsUpdateComponent } from './product-cancellations-update.component';

describe('ProductCancellations Management Update Component', () => {
  let comp: ProductCancellationsUpdateComponent;
  let fixture: ComponentFixture<ProductCancellationsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productCancellationsFormService: ProductCancellationsFormService;
  let productCancellationsService: ProductCancellationsService;
  let orderItemService: OrderItemService;
  let orderService: OrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductCancellationsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductCancellationsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductCancellationsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productCancellationsFormService = TestBed.inject(ProductCancellationsFormService);
    productCancellationsService = TestBed.inject(ProductCancellationsService);
    orderItemService = TestBed.inject(OrderItemService);
    orderService = TestBed.inject(OrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OrderItem query and add missing value', () => {
      const productCancellations: IProductCancellations = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orderItem: IOrderItem = { id: '654a8e58-ea9f-4cb1-9cb3-d6e735d6ebd9' };
      productCancellations.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: '22b5b4b9-3f9c-4e99-9815-3e831c213d32' }];
      jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCancellations });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(expect.objectContaining)
      );
      expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Order query and add missing value', () => {
      const productCancellations: IProductCancellations = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const order: IOrder = { id: '58891773-d14e-40ee-9b7a-9db4db2deaf6' };
      productCancellations.order = order;

      const orderCollection: IOrder[] = [{ id: '8d10e4fb-6823-4d34-a43c-db9e7eb5c5d7' }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCancellations });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(expect.objectContaining)
      );
      expect(comp.ordersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productCancellations: IProductCancellations = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orderItem: IOrderItem = { id: '02218564-325c-4f1b-9000-77ad7cf7ffd1' };
      productCancellations.orderItem = orderItem;
      const order: IOrder = { id: '492e54ba-cfb5-4c3e-b312-4babc651d313' };
      productCancellations.order = order;

      activatedRoute.data = of({ productCancellations });
      comp.ngOnInit();

      expect(comp.orderItemsSharedCollection).toContain(orderItem);
      expect(comp.ordersSharedCollection).toContain(order);
      expect(comp.productCancellations).toEqual(productCancellations);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductCancellations>>();
      const productCancellations = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productCancellationsFormService, 'getProductCancellations').mockReturnValue(productCancellations);
      jest.spyOn(productCancellationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCancellations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productCancellations }));
      saveSubject.complete();

      // THEN
      expect(productCancellationsFormService.getProductCancellations).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productCancellationsService.update).toHaveBeenCalledWith(expect.objectContaining(productCancellations));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductCancellations>>();
      const productCancellations = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productCancellationsFormService, 'getProductCancellations').mockReturnValue({ id: null });
      jest.spyOn(productCancellationsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCancellations: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productCancellations }));
      saveSubject.complete();

      // THEN
      expect(productCancellationsFormService.getProductCancellations).toHaveBeenCalled();
      expect(productCancellationsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductCancellations>>();
      const productCancellations = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productCancellationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCancellations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productCancellationsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrderItem', () => {
      it('Should forward to orderItemService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(orderItemService, 'compareOrderItem');
        comp.compareOrderItem(entity, entity2);
        expect(orderItemService.compareOrderItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrder', () => {
      it('Should forward to orderService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

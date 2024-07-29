import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PriceHistoryFormService } from './price-history-form.service';
import { PriceHistoryService } from '../service/price-history.service';
import { IPriceHistory } from '../price-history.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { PriceHistoryUpdateComponent } from './price-history-update.component';

describe('PriceHistory Management Update Component', () => {
  let comp: PriceHistoryUpdateComponent;
  let fixture: ComponentFixture<PriceHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let priceHistoryFormService: PriceHistoryFormService;
  let priceHistoryService: PriceHistoryService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PriceHistoryUpdateComponent],
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
      .overrideTemplate(PriceHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriceHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    priceHistoryFormService = TestBed.inject(PriceHistoryFormService);
    priceHistoryService = TestBed.inject(PriceHistoryService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const priceHistory: IPriceHistory = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: '59def3ad-8da2-48a5-918e-cae8dccde036' };
      priceHistory.product = product;

      const productCollection: IProduct[] = [{ id: '9e9f7254-a8cc-4901-9cee-165a1c6491fb' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ priceHistory });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const priceHistory: IPriceHistory = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: '1a7fb9f4-49af-4aa6-88c9-5a87636c8942' };
      priceHistory.product = product;

      activatedRoute.data = of({ priceHistory });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.priceHistory).toEqual(priceHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceHistory>>();
      const priceHistory = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(priceHistoryFormService, 'getPriceHistory').mockReturnValue(priceHistory);
      jest.spyOn(priceHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceHistory }));
      saveSubject.complete();

      // THEN
      expect(priceHistoryFormService.getPriceHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(priceHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(priceHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceHistory>>();
      const priceHistory = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(priceHistoryFormService, 'getPriceHistory').mockReturnValue({ id: null });
      jest.spyOn(priceHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceHistory }));
      saveSubject.complete();

      // THEN
      expect(priceHistoryFormService.getPriceHistory).toHaveBeenCalled();
      expect(priceHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceHistory>>();
      const priceHistory = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(priceHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(priceHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

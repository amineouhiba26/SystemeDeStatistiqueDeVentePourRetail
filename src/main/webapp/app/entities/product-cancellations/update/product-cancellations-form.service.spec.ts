import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-cancellations.test-samples';

import { ProductCancellationsFormService } from './product-cancellations-form.service';

describe('ProductCancellations Form Service', () => {
  let service: ProductCancellationsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductCancellationsFormService);
  });

  describe('Service methods', () => {
    describe('createProductCancellationsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductCancellationsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reason: expect.any(Object),
            orderItem: expect.any(Object),
            order: expect.any(Object),
          })
        );
      });

      it('passing IProductCancellations should create a new form with FormGroup', () => {
        const formGroup = service.createProductCancellationsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reason: expect.any(Object),
            orderItem: expect.any(Object),
            order: expect.any(Object),
          })
        );
      });
    });

    describe('getProductCancellations', () => {
      it('should return NewProductCancellations for default ProductCancellations initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductCancellationsFormGroup(sampleWithNewData);

        const productCancellations = service.getProductCancellations(formGroup) as any;

        expect(productCancellations).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductCancellations for empty ProductCancellations initial value', () => {
        const formGroup = service.createProductCancellationsFormGroup();

        const productCancellations = service.getProductCancellations(formGroup) as any;

        expect(productCancellations).toMatchObject({});
      });

      it('should return IProductCancellations', () => {
        const formGroup = service.createProductCancellationsFormGroup(sampleWithRequiredData);

        const productCancellations = service.getProductCancellations(formGroup) as any;

        expect(productCancellations).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductCancellations should not enable id FormControl', () => {
        const formGroup = service.createProductCancellationsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductCancellations should disable id FormControl', () => {
        const formGroup = service.createProductCancellationsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

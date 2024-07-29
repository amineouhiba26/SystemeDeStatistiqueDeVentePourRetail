import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../price-history.test-samples';

import { PriceHistoryFormService } from './price-history-form.service';

describe('PriceHistory Form Service', () => {
  let service: PriceHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriceHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createPriceHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPriceHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            oldPrice: expect.any(Object),
            newPrice: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });

      it('passing IPriceHistory should create a new form with FormGroup', () => {
        const formGroup = service.createPriceHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            oldPrice: expect.any(Object),
            newPrice: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });
    });

    describe('getPriceHistory', () => {
      it('should return NewPriceHistory for default PriceHistory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPriceHistoryFormGroup(sampleWithNewData);

        const priceHistory = service.getPriceHistory(formGroup) as any;

        expect(priceHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewPriceHistory for empty PriceHistory initial value', () => {
        const formGroup = service.createPriceHistoryFormGroup();

        const priceHistory = service.getPriceHistory(formGroup) as any;

        expect(priceHistory).toMatchObject({});
      });

      it('should return IPriceHistory', () => {
        const formGroup = service.createPriceHistoryFormGroup(sampleWithRequiredData);

        const priceHistory = service.getPriceHistory(formGroup) as any;

        expect(priceHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPriceHistory should not enable id FormControl', () => {
        const formGroup = service.createPriceHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPriceHistory should disable id FormControl', () => {
        const formGroup = service.createPriceHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

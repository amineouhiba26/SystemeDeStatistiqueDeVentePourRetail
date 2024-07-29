import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPriceHistory, NewPriceHistory } from '../price-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPriceHistory for edit and NewPriceHistoryFormGroupInput for create.
 */
type PriceHistoryFormGroupInput = IPriceHistory | PartialWithRequiredKeyOf<NewPriceHistory>;

type PriceHistoryFormDefaults = Pick<NewPriceHistory, 'id'>;

type PriceHistoryFormGroupContent = {
  id: FormControl<IPriceHistory['id'] | NewPriceHistory['id']>;
  oldPrice: FormControl<IPriceHistory['oldPrice']>;
  newPrice: FormControl<IPriceHistory['newPrice']>;
  product: FormControl<IPriceHistory['product']>;
};

export type PriceHistoryFormGroup = FormGroup<PriceHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PriceHistoryFormService {
  createPriceHistoryFormGroup(priceHistory: PriceHistoryFormGroupInput = { id: null }): PriceHistoryFormGroup {
    const priceHistoryRawValue = {
      ...this.getFormDefaults(),
      ...priceHistory,
    };
    return new FormGroup<PriceHistoryFormGroupContent>({
      id: new FormControl(
        { value: priceHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      oldPrice: new FormControl(priceHistoryRawValue.oldPrice, {
        validators: [Validators.required],
      }),
      newPrice: new FormControl(priceHistoryRawValue.newPrice, {
        validators: [Validators.required],
      }),
      product: new FormControl(priceHistoryRawValue.product),
    });
  }

  getPriceHistory(form: PriceHistoryFormGroup): IPriceHistory | NewPriceHistory {
    return form.getRawValue() as IPriceHistory | NewPriceHistory;
  }

  resetForm(form: PriceHistoryFormGroup, priceHistory: PriceHistoryFormGroupInput): void {
    const priceHistoryRawValue = { ...this.getFormDefaults(), ...priceHistory };
    form.reset(
      {
        ...priceHistoryRawValue,
        id: { value: priceHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PriceHistoryFormDefaults {
    return {
      id: null,
    };
  }
}

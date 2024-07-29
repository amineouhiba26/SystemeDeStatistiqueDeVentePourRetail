import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductCancellations, NewProductCancellations } from '../product-cancellations.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductCancellations for edit and NewProductCancellationsFormGroupInput for create.
 */
type ProductCancellationsFormGroupInput = IProductCancellations | PartialWithRequiredKeyOf<NewProductCancellations>;

type ProductCancellationsFormDefaults = Pick<NewProductCancellations, 'id'>;

type ProductCancellationsFormGroupContent = {
  id: FormControl<IProductCancellations['id'] | NewProductCancellations['id']>;
  reason: FormControl<IProductCancellations['reason']>;
  orderItem: FormControl<IProductCancellations['orderItem']>;
  order: FormControl<IProductCancellations['order']>;
};

export type ProductCancellationsFormGroup = FormGroup<ProductCancellationsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductCancellationsFormService {
  createProductCancellationsFormGroup(
    productCancellations: ProductCancellationsFormGroupInput = { id: null }
  ): ProductCancellationsFormGroup {
    const productCancellationsRawValue = {
      ...this.getFormDefaults(),
      ...productCancellations,
    };
    return new FormGroup<ProductCancellationsFormGroupContent>({
      id: new FormControl(
        { value: productCancellationsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      reason: new FormControl(productCancellationsRawValue.reason),
      orderItem: new FormControl(productCancellationsRawValue.orderItem),
      order: new FormControl(productCancellationsRawValue.order),
    });
  }

  getProductCancellations(form: ProductCancellationsFormGroup): IProductCancellations | NewProductCancellations {
    return form.getRawValue() as IProductCancellations | NewProductCancellations;
  }

  resetForm(form: ProductCancellationsFormGroup, productCancellations: ProductCancellationsFormGroupInput): void {
    const productCancellationsRawValue = { ...this.getFormDefaults(), ...productCancellations };
    form.reset(
      {
        ...productCancellationsRawValue,
        id: { value: productCancellationsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductCancellationsFormDefaults {
    return {
      id: null,
    };
  }
}

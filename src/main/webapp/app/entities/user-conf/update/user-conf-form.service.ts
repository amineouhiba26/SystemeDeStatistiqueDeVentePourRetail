import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserConf, NewUserConf } from '../user-conf.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserConf for edit and NewUserConfFormGroupInput for create.
 */
type UserConfFormGroupInput = IUserConf | PartialWithRequiredKeyOf<NewUserConf>;

type UserConfFormDefaults = Pick<NewUserConf, 'id'>;

type UserConfFormGroupContent = {
  id: FormControl<IUserConf['id'] | NewUserConf['id']>;
  username: FormControl<IUserConf['username']>;
  password: FormControl<IUserConf['password']>;
  firstName: FormControl<IUserConf['firstName']>;
  lastName: FormControl<IUserConf['lastName']>;
  email: FormControl<IUserConf['email']>;
  phoneNumber: FormControl<IUserConf['phoneNumber']>;
  role: FormControl<IUserConf['role']>;
};

export type UserConfFormGroup = FormGroup<UserConfFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserConfFormService {
  createUserConfFormGroup(userConf: UserConfFormGroupInput = { id: null }): UserConfFormGroup {
    const userConfRawValue = {
      ...this.getFormDefaults(),
      ...userConf,
    };
    return new FormGroup<UserConfFormGroupContent>({
      id: new FormControl(
        { value: userConfRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      username: new FormControl(userConfRawValue.username, {
        validators: [Validators.required],
      }),
      password: new FormControl(userConfRawValue.password, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(userConfRawValue.firstName),
      lastName: new FormControl(userConfRawValue.lastName),
      email: new FormControl(userConfRawValue.email),
      phoneNumber: new FormControl(userConfRawValue.phoneNumber),
      role: new FormControl(userConfRawValue.role),
    });
  }

  getUserConf(form: UserConfFormGroup): IUserConf | NewUserConf {
    return form.getRawValue() as IUserConf | NewUserConf;
  }

  resetForm(form: UserConfFormGroup, userConf: UserConfFormGroupInput): void {
    const userConfRawValue = { ...this.getFormDefaults(), ...userConf };
    form.reset(
      {
        ...userConfRawValue,
        id: { value: userConfRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserConfFormDefaults {
    return {
      id: null,
    };
  }
}

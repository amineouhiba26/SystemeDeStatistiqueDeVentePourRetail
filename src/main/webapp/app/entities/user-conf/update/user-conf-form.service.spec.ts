import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-conf.test-samples';

import { UserConfFormService } from './user-conf-form.service';

describe('UserConf Form Service', () => {
  let service: UserConfFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserConfFormService);
  });

  describe('Service methods', () => {
    describe('createUserConfFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserConfFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            password: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            role: expect.any(Object),
          })
        );
      });

      it('passing IUserConf should create a new form with FormGroup', () => {
        const formGroup = service.createUserConfFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            password: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            role: expect.any(Object),
          })
        );
      });
    });

    describe('getUserConf', () => {
      it('should return NewUserConf for default UserConf initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserConfFormGroup(sampleWithNewData);

        const userConf = service.getUserConf(formGroup) as any;

        expect(userConf).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserConf for empty UserConf initial value', () => {
        const formGroup = service.createUserConfFormGroup();

        const userConf = service.getUserConf(formGroup) as any;

        expect(userConf).toMatchObject({});
      });

      it('should return IUserConf', () => {
        const formGroup = service.createUserConfFormGroup(sampleWithRequiredData);

        const userConf = service.getUserConf(formGroup) as any;

        expect(userConf).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserConf should not enable id FormControl', () => {
        const formGroup = service.createUserConfFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserConf should disable id FormControl', () => {
        const formGroup = service.createUserConfFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

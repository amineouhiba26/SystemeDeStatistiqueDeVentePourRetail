import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoleFormService } from './role-form.service';
import { RoleService } from '../service/role.service';
import { IRole } from '../role.model';
import { IPermission } from 'app/entities/permission/permission.model';
import { PermissionService } from 'app/entities/permission/service/permission.service';

import { RoleUpdateComponent } from './role-update.component';

describe('Role Management Update Component', () => {
  let comp: RoleUpdateComponent;
  let fixture: ComponentFixture<RoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roleFormService: RoleFormService;
  let roleService: RoleService;
  let permissionService: PermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoleUpdateComponent],
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
      .overrideTemplate(RoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roleFormService = TestBed.inject(RoleFormService);
    roleService = TestBed.inject(RoleService);
    permissionService = TestBed.inject(PermissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Permission query and add missing value', () => {
      const role: IRole = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const permissions: IPermission[] = [{ id: '22da7c73-73fb-45f1-b731-53936e7e7095' }];
      role.permissions = permissions;

      const permissionCollection: IPermission[] = [{ id: '4d02bedb-b864-4382-8536-9fc2f9dda521' }];
      jest.spyOn(permissionService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionCollection })));
      const additionalPermissions = [...permissions];
      const expectedCollection: IPermission[] = [...additionalPermissions, ...permissionCollection];
      jest.spyOn(permissionService, 'addPermissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(permissionService.query).toHaveBeenCalled();
      expect(permissionService.addPermissionToCollectionIfMissing).toHaveBeenCalledWith(
        permissionCollection,
        ...additionalPermissions.map(expect.objectContaining)
      );
      expect(comp.permissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const role: IRole = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const permission: IPermission = { id: 'cfc08ce3-bbe1-413a-afa3-2b598aee6268' };
      role.permissions = [permission];

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(comp.permissionsSharedCollection).toContain(permission);
      expect(comp.role).toEqual(role);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(roleFormService, 'getRole').mockReturnValue(role);
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roleService.update).toHaveBeenCalledWith(expect.objectContaining(role));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(roleFormService, 'getRole').mockReturnValue({ id: null });
      jest.spyOn(roleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRole).toHaveBeenCalled();
      expect(roleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePermission', () => {
      it('Should forward to permissionService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(permissionService, 'comparePermission');
        comp.comparePermission(entity, entity2);
        expect(permissionService.comparePermission).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserConfFormService } from './user-conf-form.service';
import { UserConfService } from '../service/user-conf.service';
import { IUserConf } from '../user-conf.model';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';

import { UserConfUpdateComponent } from './user-conf-update.component';

describe('UserConf Management Update Component', () => {
  let comp: UserConfUpdateComponent;
  let fixture: ComponentFixture<UserConfUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userConfFormService: UserConfFormService;
  let userConfService: UserConfService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserConfUpdateComponent],
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
      .overrideTemplate(UserConfUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserConfUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userConfFormService = TestBed.inject(UserConfFormService);
    userConfService = TestBed.inject(UserConfService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Role query and add missing value', () => {
      const userConf: IUserConf = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const role: IRole = { id: '278db07c-ec8d-4547-bd1e-8a94d76f3316' };
      userConf.role = role;

      const roleCollection: IRole[] = [{ id: 'd3671d48-24ec-4afb-a239-37e5b5bb82e0' }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [role];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userConf });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining)
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userConf: IUserConf = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const role: IRole = { id: '82311938-fba5-4ae1-9129-cc4a7a8f3c11' };
      userConf.role = role;

      activatedRoute.data = of({ userConf });
      comp.ngOnInit();

      expect(comp.rolesSharedCollection).toContain(role);
      expect(comp.userConf).toEqual(userConf);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConf>>();
      const userConf = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(userConfFormService, 'getUserConf').mockReturnValue(userConf);
      jest.spyOn(userConfService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userConf }));
      saveSubject.complete();

      // THEN
      expect(userConfFormService.getUserConf).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userConfService.update).toHaveBeenCalledWith(expect.objectContaining(userConf));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConf>>();
      const userConf = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(userConfFormService, 'getUserConf').mockReturnValue({ id: null });
      jest.spyOn(userConfService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConf: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userConf }));
      saveSubject.complete();

      // THEN
      expect(userConfFormService.getUserConf).toHaveBeenCalled();
      expect(userConfService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConf>>();
      const userConf = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(userConfService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userConfService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRole', () => {
      it('Should forward to roleService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(roleService, 'compareRole');
        comp.compareRole(entity, entity2);
        expect(roleService.compareRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

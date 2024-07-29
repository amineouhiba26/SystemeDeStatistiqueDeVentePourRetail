import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UserConfFormService, UserConfFormGroup } from './user-conf-form.service';
import { IUserConf } from '../user-conf.model';
import { UserConfService } from '../service/user-conf.service';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';

@Component({
  selector: 'jhi-user-conf-update',
  templateUrl: './user-conf-update.component.html',
})
export class UserConfUpdateComponent implements OnInit {
  isSaving = false;
  userConf: IUserConf | null = null;

  rolesSharedCollection: IRole[] = [];

  editForm: UserConfFormGroup = this.userConfFormService.createUserConfFormGroup();

  constructor(
    protected userConfService: UserConfService,
    protected userConfFormService: UserConfFormService,
    protected roleService: RoleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userConf }) => {
      this.userConf = userConf;
      if (userConf) {
        this.updateForm(userConf);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userConf = this.userConfFormService.getUserConf(this.editForm);
    if (userConf.id !== null) {
      this.subscribeToSaveResponse(this.userConfService.update(userConf));
    } else {
      this.subscribeToSaveResponse(this.userConfService.create(userConf));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserConf>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userConf: IUserConf): void {
    this.userConf = userConf;
    this.userConfFormService.resetForm(this.editForm, userConf);

    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(this.rolesSharedCollection, userConf.role);
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, this.userConf?.role)))
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));
  }
}

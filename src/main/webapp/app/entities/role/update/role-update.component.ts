import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoleFormService, RoleFormGroup } from './role-form.service';
import { IRole } from '../role.model';
import { RoleService } from '../service/role.service';
import { IPermission } from 'app/entities/permission/permission.model';
import { PermissionService } from 'app/entities/permission/service/permission.service';

@Component({
  selector: 'jhi-role-update',
  templateUrl: './role-update.component.html',
})
export class RoleUpdateComponent implements OnInit {
  isSaving = false;
  role: IRole | null = null;

  permissionsSharedCollection: IPermission[] = [];

  editForm: RoleFormGroup = this.roleFormService.createRoleFormGroup();

  constructor(
    protected roleService: RoleService,
    protected roleFormService: RoleFormService,
    protected permissionService: PermissionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePermission = (o1: IPermission | null, o2: IPermission | null): boolean => this.permissionService.comparePermission(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.role = role;
      if (role) {
        this.updateForm(role);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.roleFormService.getRole(this.editForm);
    if (role.id !== null) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRole>>): void {
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

  protected updateForm(role: IRole): void {
    this.role = role;
    this.roleFormService.resetForm(this.editForm, role);

    this.permissionsSharedCollection = this.permissionService.addPermissionToCollectionIfMissing<IPermission>(
      this.permissionsSharedCollection,
      ...(role.permissions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.permissionService
      .query()
      .pipe(map((res: HttpResponse<IPermission[]>) => res.body ?? []))
      .pipe(
        map((permissions: IPermission[]) =>
          this.permissionService.addPermissionToCollectionIfMissing<IPermission>(permissions, ...(this.role?.permissions ?? []))
        )
      )
      .subscribe((permissions: IPermission[]) => (this.permissionsSharedCollection = permissions));
  }
}

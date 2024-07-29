import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PermissionFormService, PermissionFormGroup } from './permission-form.service';
import { IPermission } from '../permission.model';
import { PermissionService } from '../service/permission.service';

@Component({
  selector: 'jhi-permission-update',
  templateUrl: './permission-update.component.html',
})
export class PermissionUpdateComponent implements OnInit {
  isSaving = false;
  permission: IPermission | null = null;

  editForm: PermissionFormGroup = this.permissionFormService.createPermissionFormGroup();

  constructor(
    protected permissionService: PermissionService,
    protected permissionFormService: PermissionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permission }) => {
      this.permission = permission;
      if (permission) {
        this.updateForm(permission);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permission = this.permissionFormService.getPermission(this.editForm);
    if (permission.id !== null) {
      this.subscribeToSaveResponse(this.permissionService.update(permission));
    } else {
      this.subscribeToSaveResponse(this.permissionService.create(permission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermission>>): void {
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

  protected updateForm(permission: IPermission): void {
    this.permission = permission;
    this.permissionFormService.resetForm(this.editForm, permission);
  }
}

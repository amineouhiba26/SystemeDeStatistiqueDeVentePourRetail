import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermission } from '../permission.model';
import { PermissionService } from '../service/permission.service';

@Injectable({ providedIn: 'root' })
export class PermissionRoutingResolveService implements Resolve<IPermission | null> {
  constructor(protected service: PermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermission | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permission: HttpResponse<IPermission>) => {
          if (permission.body) {
            return of(permission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

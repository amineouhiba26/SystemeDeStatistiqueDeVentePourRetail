import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserConf } from '../user-conf.model';
import { UserConfService } from '../service/user-conf.service';

@Injectable({ providedIn: 'root' })
export class UserConfRoutingResolveService implements Resolve<IUserConf | null> {
  constructor(protected service: UserConfService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserConf | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userConf: HttpResponse<IUserConf>) => {
          if (userConf.body) {
            return of(userConf.body);
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

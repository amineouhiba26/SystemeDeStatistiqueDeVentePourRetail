import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductCancellations } from '../product-cancellations.model';
import { ProductCancellationsService } from '../service/product-cancellations.service';

@Injectable({ providedIn: 'root' })
export class ProductCancellationsRoutingResolveService implements Resolve<IProductCancellations | null> {
  constructor(protected service: ProductCancellationsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductCancellations | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productCancellations: HttpResponse<IProductCancellations>) => {
          if (productCancellations.body) {
            return of(productCancellations.body);
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

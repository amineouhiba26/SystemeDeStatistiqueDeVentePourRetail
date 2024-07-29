import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPriceHistory } from '../price-history.model';
import { PriceHistoryService } from '../service/price-history.service';

@Injectable({ providedIn: 'root' })
export class PriceHistoryRoutingResolveService implements Resolve<IPriceHistory | null> {
  constructor(protected service: PriceHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPriceHistory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((priceHistory: HttpResponse<IPriceHistory>) => {
          if (priceHistory.body) {
            return of(priceHistory.body);
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

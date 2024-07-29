import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriceHistory, NewPriceHistory } from '../price-history.model';

export type PartialUpdatePriceHistory = Partial<IPriceHistory> & Pick<IPriceHistory, 'id'>;

export type EntityResponseType = HttpResponse<IPriceHistory>;
export type EntityArrayResponseType = HttpResponse<IPriceHistory[]>;

@Injectable({ providedIn: 'root' })
export class PriceHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/price-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(priceHistory: NewPriceHistory): Observable<EntityResponseType> {
    return this.http.post<IPriceHistory>(this.resourceUrl, priceHistory, { observe: 'response' });
  }

  update(priceHistory: IPriceHistory): Observable<EntityResponseType> {
    return this.http.put<IPriceHistory>(`${this.resourceUrl}/${this.getPriceHistoryIdentifier(priceHistory)}`, priceHistory, {
      observe: 'response',
    });
  }

  partialUpdate(priceHistory: PartialUpdatePriceHistory): Observable<EntityResponseType> {
    return this.http.patch<IPriceHistory>(`${this.resourceUrl}/${this.getPriceHistoryIdentifier(priceHistory)}`, priceHistory, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPriceHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriceHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPriceHistoryIdentifier(priceHistory: Pick<IPriceHistory, 'id'>): string {
    return priceHistory.id;
  }

  comparePriceHistory(o1: Pick<IPriceHistory, 'id'> | null, o2: Pick<IPriceHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getPriceHistoryIdentifier(o1) === this.getPriceHistoryIdentifier(o2) : o1 === o2;
  }

  addPriceHistoryToCollectionIfMissing<Type extends Pick<IPriceHistory, 'id'>>(
    priceHistoryCollection: Type[],
    ...priceHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const priceHistories: Type[] = priceHistoriesToCheck.filter(isPresent);
    if (priceHistories.length > 0) {
      const priceHistoryCollectionIdentifiers = priceHistoryCollection.map(
        priceHistoryItem => this.getPriceHistoryIdentifier(priceHistoryItem)!
      );
      const priceHistoriesToAdd = priceHistories.filter(priceHistoryItem => {
        const priceHistoryIdentifier = this.getPriceHistoryIdentifier(priceHistoryItem);
        if (priceHistoryCollectionIdentifiers.includes(priceHistoryIdentifier)) {
          return false;
        }
        priceHistoryCollectionIdentifiers.push(priceHistoryIdentifier);
        return true;
      });
      return [...priceHistoriesToAdd, ...priceHistoryCollection];
    }
    return priceHistoryCollection;
  }
}

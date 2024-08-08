import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriceHistory, NewPriceHistory } from '../price-history.model';
import { isPresent } from '../../../core/util/operators';

export type PartialUpdatePriceHistory = Partial<IPriceHistory> & Pick<IPriceHistory, 'id'>;
export type EntityResponseType = HttpResponse<IPriceHistory>;
export type EntityArrayResponseType = HttpResponse<IPriceHistory[]>;

@Injectable({ providedIn: 'root' })
export class PriceHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/price-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  // Ensure the service method returns Observable<IPriceHistory[]>
  getPriceHistoriesByProductId(productId: string): Observable<IPriceHistory[]> {
    return this.http.get<IPriceHistory[]>(`${this.resourceUrl}?productId=${productId}`).pipe(catchError(this.handleError));
  }

  // Create a new price history record
  create(priceHistory: NewPriceHistory): Observable<EntityResponseType> {
    return this.http.post<IPriceHistory>(this.resourceUrl, priceHistory, { observe: 'response' }).pipe(catchError(this.handleError));
  }

  // Update an existing price history record
  update(priceHistory: IPriceHistory): Observable<EntityResponseType> {
    return this.http
      .put<IPriceHistory>(`${this.resourceUrl}/${this.getPriceHistoryIdentifier(priceHistory)}`, priceHistory, {
        observe: 'response',
      })
      .pipe(catchError(this.handleError));
  }

  // Partially update a price history record
  partialUpdate(priceHistory: PartialUpdatePriceHistory): Observable<EntityResponseType> {
    return this.http
      .patch<IPriceHistory>(`${this.resourceUrl}/${this.getPriceHistoryIdentifier(priceHistory)}`, priceHistory, {
        observe: 'response',
      })
      .pipe(catchError(this.handleError));
  }

  // Find a specific price history record by ID
  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPriceHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(catchError(this.handleError));
  }

  // Query price history records with optional request parameters
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriceHistory[]>(this.resourceUrl, { params: options, observe: 'response' }).pipe(catchError(this.handleError));
  }

  // Delete a specific price history record by ID
  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(catchError(this.handleError));
  }

  // Extract the ID from a price history record
  getPriceHistoryIdentifier(priceHistory: Pick<IPriceHistory, 'id'>): string {
    return priceHistory.id;
  }

  // Compare two price history records based on their ID
  comparePriceHistory(o1: Pick<IPriceHistory, 'id'> | null, o2: Pick<IPriceHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getPriceHistoryIdentifier(o1) === this.getPriceHistoryIdentifier(o2) : o1 === o2;
  }

  // Add price histories to the collection if not already present
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

  // Error handling method
  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(() => new Error('Something went wrong. Please try again later.'));
  }
}

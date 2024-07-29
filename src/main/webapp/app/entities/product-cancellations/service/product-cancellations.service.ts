import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductCancellations, NewProductCancellations } from '../product-cancellations.model';

export type PartialUpdateProductCancellations = Partial<IProductCancellations> & Pick<IProductCancellations, 'id'>;

export type EntityResponseType = HttpResponse<IProductCancellations>;
export type EntityArrayResponseType = HttpResponse<IProductCancellations[]>;

@Injectable({ providedIn: 'root' })
export class ProductCancellationsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-cancellations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productCancellations: NewProductCancellations): Observable<EntityResponseType> {
    return this.http.post<IProductCancellations>(this.resourceUrl, productCancellations, { observe: 'response' });
  }

  update(productCancellations: IProductCancellations): Observable<EntityResponseType> {
    return this.http.put<IProductCancellations>(
      `${this.resourceUrl}/${this.getProductCancellationsIdentifier(productCancellations)}`,
      productCancellations,
      { observe: 'response' }
    );
  }

  partialUpdate(productCancellations: PartialUpdateProductCancellations): Observable<EntityResponseType> {
    return this.http.patch<IProductCancellations>(
      `${this.resourceUrl}/${this.getProductCancellationsIdentifier(productCancellations)}`,
      productCancellations,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IProductCancellations>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductCancellations[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductCancellationsIdentifier(productCancellations: Pick<IProductCancellations, 'id'>): string {
    return productCancellations.id;
  }

  compareProductCancellations(o1: Pick<IProductCancellations, 'id'> | null, o2: Pick<IProductCancellations, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductCancellationsIdentifier(o1) === this.getProductCancellationsIdentifier(o2) : o1 === o2;
  }

  addProductCancellationsToCollectionIfMissing<Type extends Pick<IProductCancellations, 'id'>>(
    productCancellationsCollection: Type[],
    ...productCancellationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productCancellations: Type[] = productCancellationsToCheck.filter(isPresent);
    if (productCancellations.length > 0) {
      const productCancellationsCollectionIdentifiers = productCancellationsCollection.map(
        productCancellationsItem => this.getProductCancellationsIdentifier(productCancellationsItem)!
      );
      const productCancellationsToAdd = productCancellations.filter(productCancellationsItem => {
        const productCancellationsIdentifier = this.getProductCancellationsIdentifier(productCancellationsItem);
        if (productCancellationsCollectionIdentifiers.includes(productCancellationsIdentifier)) {
          return false;
        }
        productCancellationsCollectionIdentifiers.push(productCancellationsIdentifier);
        return true;
      });
      return [...productCancellationsToAdd, ...productCancellationsCollection];
    }
    return productCancellationsCollection;
  }
}

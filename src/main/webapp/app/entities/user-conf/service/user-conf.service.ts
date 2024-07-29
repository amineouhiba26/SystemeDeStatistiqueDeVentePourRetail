import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserConf, NewUserConf } from '../user-conf.model';

export type PartialUpdateUserConf = Partial<IUserConf> & Pick<IUserConf, 'id'>;

export type EntityResponseType = HttpResponse<IUserConf>;
export type EntityArrayResponseType = HttpResponse<IUserConf[]>;

@Injectable({ providedIn: 'root' })
export class UserConfService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-confs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userConf: NewUserConf): Observable<EntityResponseType> {
    return this.http.post<IUserConf>(this.resourceUrl, userConf, { observe: 'response' });
  }

  update(userConf: IUserConf): Observable<EntityResponseType> {
    return this.http.put<IUserConf>(`${this.resourceUrl}/${this.getUserConfIdentifier(userConf)}`, userConf, { observe: 'response' });
  }

  partialUpdate(userConf: PartialUpdateUserConf): Observable<EntityResponseType> {
    return this.http.patch<IUserConf>(`${this.resourceUrl}/${this.getUserConfIdentifier(userConf)}`, userConf, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IUserConf>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserConf[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserConfIdentifier(userConf: Pick<IUserConf, 'id'>): string {
    return userConf.id;
  }

  compareUserConf(o1: Pick<IUserConf, 'id'> | null, o2: Pick<IUserConf, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserConfIdentifier(o1) === this.getUserConfIdentifier(o2) : o1 === o2;
  }

  addUserConfToCollectionIfMissing<Type extends Pick<IUserConf, 'id'>>(
    userConfCollection: Type[],
    ...userConfsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userConfs: Type[] = userConfsToCheck.filter(isPresent);
    if (userConfs.length > 0) {
      const userConfCollectionIdentifiers = userConfCollection.map(userConfItem => this.getUserConfIdentifier(userConfItem)!);
      const userConfsToAdd = userConfs.filter(userConfItem => {
        const userConfIdentifier = this.getUserConfIdentifier(userConfItem);
        if (userConfCollectionIdentifiers.includes(userConfIdentifier)) {
          return false;
        }
        userConfCollectionIdentifiers.push(userConfIdentifier);
        return true;
      });
      return [...userConfsToAdd, ...userConfCollection];
    }
    return userConfCollection;
  }
}

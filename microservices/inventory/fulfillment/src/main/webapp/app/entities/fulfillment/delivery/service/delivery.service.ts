import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDelivery, NewDelivery } from '../delivery.model';

export type PartialUpdateDelivery = Partial<IDelivery> & Pick<IDelivery, 'id'>;

type RestOf<T extends IDelivery | NewDelivery> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestDelivery = RestOf<IDelivery>;

export type NewRestDelivery = RestOf<NewDelivery>;

export type PartialUpdateRestDelivery = RestOf<PartialUpdateDelivery>;

export type EntityResponseType = HttpResponse<IDelivery>;
export type EntityArrayResponseType = HttpResponse<IDelivery[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deliveries', 'fulfillment');

  create(delivery: NewDelivery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delivery);
    return this.http
      .post<RestDelivery>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(delivery: IDelivery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delivery);
    return this.http
      .put<RestDelivery>(`${this.resourceUrl}/${this.getDeliveryIdentifier(delivery)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(delivery: PartialUpdateDelivery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delivery);
    return this.http
      .patch<RestDelivery>(`${this.resourceUrl}/${this.getDeliveryIdentifier(delivery)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDelivery>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDelivery[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeliveryIdentifier(delivery: Pick<IDelivery, 'id'>): number {
    return delivery.id;
  }

  compareDelivery(o1: Pick<IDelivery, 'id'> | null, o2: Pick<IDelivery, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeliveryIdentifier(o1) === this.getDeliveryIdentifier(o2) : o1 === o2;
  }

  addDeliveryToCollectionIfMissing<Type extends Pick<IDelivery, 'id'>>(
    deliveryCollection: Type[],
    ...deliveriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deliveries: Type[] = deliveriesToCheck.filter(isPresent);
    if (deliveries.length > 0) {
      const deliveryCollectionIdentifiers = deliveryCollection.map(deliveryItem => this.getDeliveryIdentifier(deliveryItem));
      const deliveriesToAdd = deliveries.filter(deliveryItem => {
        const deliveryIdentifier = this.getDeliveryIdentifier(deliveryItem);
        if (deliveryCollectionIdentifiers.includes(deliveryIdentifier)) {
          return false;
        }
        deliveryCollectionIdentifiers.push(deliveryIdentifier);
        return true;
      });
      return [...deliveriesToAdd, ...deliveryCollection];
    }
    return deliveryCollection;
  }

  protected convertDateFromClient<T extends IDelivery | NewDelivery | PartialUpdateDelivery>(delivery: T): RestOf<T> {
    return {
      ...delivery,
      createdAt: delivery.createdAt?.toJSON() ?? null,
      updatedAt: delivery.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDelivery: RestDelivery): IDelivery {
    return {
      ...restDelivery,
      createdAt: restDelivery.createdAt ? dayjs(restDelivery.createdAt) : undefined,
      updatedAt: restDelivery.updatedAt ? dayjs(restDelivery.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDelivery>): HttpResponse<IDelivery> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDelivery[]>): HttpResponse<IDelivery[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

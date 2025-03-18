import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrder, NewOrder } from '../order.model';

export type PartialUpdateOrder = Partial<IOrder> & Pick<IOrder, 'id'>;

type RestOf<T extends IOrder | NewOrder> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestOrder = RestOf<IOrder>;

export type NewRestOrder = RestOf<NewOrder>;

export type PartialUpdateRestOrder = RestOf<PartialUpdateOrder>;

export type EntityResponseType = HttpResponse<IOrder>;
export type EntityArrayResponseType = HttpResponse<IOrder[]>;

@Injectable({ providedIn: 'root' })
export class OrderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orders', 'orders');

  create(order: NewOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http.post<RestOrder>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(order: IOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http
      .put<RestOrder>(`${this.resourceUrl}/${this.getOrderIdentifier(order)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(order: PartialUpdateOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http
      .patch<RestOrder>(`${this.resourceUrl}/${this.getOrderIdentifier(order)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrderIdentifier(order: Pick<IOrder, 'id'>): number {
    return order.id;
  }

  compareOrder(o1: Pick<IOrder, 'id'> | null, o2: Pick<IOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderIdentifier(o1) === this.getOrderIdentifier(o2) : o1 === o2;
  }

  addOrderToCollectionIfMissing<Type extends Pick<IOrder, 'id'>>(
    orderCollection: Type[],
    ...ordersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orders: Type[] = ordersToCheck.filter(isPresent);
    if (orders.length > 0) {
      const orderCollectionIdentifiers = orderCollection.map(orderItem => this.getOrderIdentifier(orderItem));
      const ordersToAdd = orders.filter(orderItem => {
        const orderIdentifier = this.getOrderIdentifier(orderItem);
        if (orderCollectionIdentifiers.includes(orderIdentifier)) {
          return false;
        }
        orderCollectionIdentifiers.push(orderIdentifier);
        return true;
      });
      return [...ordersToAdd, ...orderCollection];
    }
    return orderCollection;
  }

  protected convertDateFromClient<T extends IOrder | NewOrder | PartialUpdateOrder>(order: T): RestOf<T> {
    return {
      ...order,
      createdAt: order.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOrder: RestOrder): IOrder {
    return {
      ...restOrder,
      createdAt: restOrder.createdAt ? dayjs(restOrder.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrder>): HttpResponse<IOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrder[]>): HttpResponse<IOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

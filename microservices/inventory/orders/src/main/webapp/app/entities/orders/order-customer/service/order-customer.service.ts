import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderCustomer, NewOrderCustomer } from '../order-customer.model';

export type PartialUpdateOrderCustomer = Partial<IOrderCustomer> & Pick<IOrderCustomer, 'id'>;

export type EntityResponseType = HttpResponse<IOrderCustomer>;
export type EntityArrayResponseType = HttpResponse<IOrderCustomer[]>;

@Injectable({ providedIn: 'root' })
export class OrderCustomerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-customers', 'orders');

  create(orderCustomer: NewOrderCustomer): Observable<EntityResponseType> {
    return this.http.post<IOrderCustomer>(this.resourceUrl, orderCustomer, { observe: 'response' });
  }

  update(orderCustomer: IOrderCustomer): Observable<EntityResponseType> {
    return this.http.put<IOrderCustomer>(`${this.resourceUrl}/${this.getOrderCustomerIdentifier(orderCustomer)}`, orderCustomer, {
      observe: 'response',
    });
  }

  partialUpdate(orderCustomer: PartialUpdateOrderCustomer): Observable<EntityResponseType> {
    return this.http.patch<IOrderCustomer>(`${this.resourceUrl}/${this.getOrderCustomerIdentifier(orderCustomer)}`, orderCustomer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderCustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderCustomer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrderCustomerIdentifier(orderCustomer: Pick<IOrderCustomer, 'id'>): number {
    return orderCustomer.id;
  }

  compareOrderCustomer(o1: Pick<IOrderCustomer, 'id'> | null, o2: Pick<IOrderCustomer, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderCustomerIdentifier(o1) === this.getOrderCustomerIdentifier(o2) : o1 === o2;
  }

  addOrderCustomerToCollectionIfMissing<Type extends Pick<IOrderCustomer, 'id'>>(
    orderCustomerCollection: Type[],
    ...orderCustomersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderCustomers: Type[] = orderCustomersToCheck.filter(isPresent);
    if (orderCustomers.length > 0) {
      const orderCustomerCollectionIdentifiers = orderCustomerCollection.map(orderCustomerItem =>
        this.getOrderCustomerIdentifier(orderCustomerItem),
      );
      const orderCustomersToAdd = orderCustomers.filter(orderCustomerItem => {
        const orderCustomerIdentifier = this.getOrderCustomerIdentifier(orderCustomerItem);
        if (orderCustomerCollectionIdentifiers.includes(orderCustomerIdentifier)) {
          return false;
        }
        orderCustomerCollectionIdentifiers.push(orderCustomerIdentifier);
        return true;
      });
      return [...orderCustomersToAdd, ...orderCustomerCollection];
    }
    return orderCustomerCollection;
  }
}

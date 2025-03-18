import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryItem, NewDeliveryItem } from '../delivery-item.model';

export type PartialUpdateDeliveryItem = Partial<IDeliveryItem> & Pick<IDeliveryItem, 'id'>;

export type EntityResponseType = HttpResponse<IDeliveryItem>;
export type EntityArrayResponseType = HttpResponse<IDeliveryItem[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryItemService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-items', 'fulfillment');

  create(deliveryItem: NewDeliveryItem): Observable<EntityResponseType> {
    return this.http.post<IDeliveryItem>(this.resourceUrl, deliveryItem, { observe: 'response' });
  }

  update(deliveryItem: IDeliveryItem): Observable<EntityResponseType> {
    return this.http.put<IDeliveryItem>(`${this.resourceUrl}/${this.getDeliveryItemIdentifier(deliveryItem)}`, deliveryItem, {
      observe: 'response',
    });
  }

  partialUpdate(deliveryItem: PartialUpdateDeliveryItem): Observable<EntityResponseType> {
    return this.http.patch<IDeliveryItem>(`${this.resourceUrl}/${this.getDeliveryItemIdentifier(deliveryItem)}`, deliveryItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliveryItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliveryItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeliveryItemIdentifier(deliveryItem: Pick<IDeliveryItem, 'id'>): number {
    return deliveryItem.id;
  }

  compareDeliveryItem(o1: Pick<IDeliveryItem, 'id'> | null, o2: Pick<IDeliveryItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeliveryItemIdentifier(o1) === this.getDeliveryItemIdentifier(o2) : o1 === o2;
  }

  addDeliveryItemToCollectionIfMissing<Type extends Pick<IDeliveryItem, 'id'>>(
    deliveryItemCollection: Type[],
    ...deliveryItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deliveryItems: Type[] = deliveryItemsToCheck.filter(isPresent);
    if (deliveryItems.length > 0) {
      const deliveryItemCollectionIdentifiers = deliveryItemCollection.map(deliveryItemItem =>
        this.getDeliveryItemIdentifier(deliveryItemItem),
      );
      const deliveryItemsToAdd = deliveryItems.filter(deliveryItemItem => {
        const deliveryItemIdentifier = this.getDeliveryItemIdentifier(deliveryItemItem);
        if (deliveryItemCollectionIdentifiers.includes(deliveryItemIdentifier)) {
          return false;
        }
        deliveryItemCollectionIdentifiers.push(deliveryItemIdentifier);
        return true;
      });
      return [...deliveryItemsToAdd, ...deliveryItemCollection];
    }
    return deliveryItemCollection;
  }
}

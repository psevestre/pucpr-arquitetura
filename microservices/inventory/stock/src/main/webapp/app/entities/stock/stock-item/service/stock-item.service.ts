import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockItem, NewStockItem } from '../stock-item.model';

export type PartialUpdateStockItem = Partial<IStockItem> & Pick<IStockItem, 'id'>;

export type EntityResponseType = HttpResponse<IStockItem>;
export type EntityArrayResponseType = HttpResponse<IStockItem[]>;

@Injectable({ providedIn: 'root' })
export class StockItemService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-items', 'stock');

  create(stockItem: NewStockItem): Observable<EntityResponseType> {
    return this.http.post<IStockItem>(this.resourceUrl, stockItem, { observe: 'response' });
  }

  update(stockItem: IStockItem): Observable<EntityResponseType> {
    return this.http.put<IStockItem>(`${this.resourceUrl}/${this.getStockItemIdentifier(stockItem)}`, stockItem, { observe: 'response' });
  }

  partialUpdate(stockItem: PartialUpdateStockItem): Observable<EntityResponseType> {
    return this.http.patch<IStockItem>(`${this.resourceUrl}/${this.getStockItemIdentifier(stockItem)}`, stockItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStockItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStockItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockItemIdentifier(stockItem: Pick<IStockItem, 'id'>): number {
    return stockItem.id;
  }

  compareStockItem(o1: Pick<IStockItem, 'id'> | null, o2: Pick<IStockItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockItemIdentifier(o1) === this.getStockItemIdentifier(o2) : o1 === o2;
  }

  addStockItemToCollectionIfMissing<Type extends Pick<IStockItem, 'id'>>(
    stockItemCollection: Type[],
    ...stockItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockItems: Type[] = stockItemsToCheck.filter(isPresent);
    if (stockItems.length > 0) {
      const stockItemCollectionIdentifiers = stockItemCollection.map(stockItemItem => this.getStockItemIdentifier(stockItemItem));
      const stockItemsToAdd = stockItems.filter(stockItemItem => {
        const stockItemIdentifier = this.getStockItemIdentifier(stockItemItem);
        if (stockItemCollectionIdentifiers.includes(stockItemIdentifier)) {
          return false;
        }
        stockItemCollectionIdentifiers.push(stockItemIdentifier);
        return true;
      });
      return [...stockItemsToAdd, ...stockItemCollection];
    }
    return stockItemCollection;
  }
}

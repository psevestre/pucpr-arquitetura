import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';

const orderItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderItem> => {
  const id = route.params.id;
  if (id) {
    return inject(OrderItemService)
      .find(id)
      .pipe(
        mergeMap((orderItem: HttpResponse<IOrderItem>) => {
          if (orderItem.body) {
            return of(orderItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default orderItemResolve;

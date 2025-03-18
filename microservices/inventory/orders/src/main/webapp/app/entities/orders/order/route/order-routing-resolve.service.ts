import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';

const orderResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrder> => {
  const id = route.params.id;
  if (id) {
    return inject(OrderService)
      .find(id)
      .pipe(
        mergeMap((order: HttpResponse<IOrder>) => {
          if (order.body) {
            return of(order.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default orderResolve;

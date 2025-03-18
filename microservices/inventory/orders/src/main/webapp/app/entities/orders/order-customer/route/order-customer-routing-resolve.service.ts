import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderCustomer } from '../order-customer.model';
import { OrderCustomerService } from '../service/order-customer.service';

const orderCustomerResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderCustomer> => {
  const id = route.params.id;
  if (id) {
    return inject(OrderCustomerService)
      .find(id)
      .pipe(
        mergeMap((orderCustomer: HttpResponse<IOrderCustomer>) => {
          if (orderCustomer.body) {
            return of(orderCustomer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default orderCustomerResolve;

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryItem } from '../delivery-item.model';
import { DeliveryItemService } from '../service/delivery-item.service';

const deliveryItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IDeliveryItem> => {
  const id = route.params.id;
  if (id) {
    return inject(DeliveryItemService)
      .find(id)
      .pipe(
        mergeMap((deliveryItem: HttpResponse<IDeliveryItem>) => {
          if (deliveryItem.body) {
            return of(deliveryItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default deliveryItemResolve;

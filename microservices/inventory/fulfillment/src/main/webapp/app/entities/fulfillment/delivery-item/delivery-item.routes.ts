import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DeliveryItemResolve from './route/delivery-item-routing-resolve.service';

const deliveryItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/delivery-item.component').then(m => m.DeliveryItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/delivery-item-detail.component').then(m => m.DeliveryItemDetailComponent),
    resolve: {
      deliveryItem: DeliveryItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/delivery-item-update.component').then(m => m.DeliveryItemUpdateComponent),
    resolve: {
      deliveryItem: DeliveryItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/delivery-item-update.component').then(m => m.DeliveryItemUpdateComponent),
    resolve: {
      deliveryItem: DeliveryItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default deliveryItemRoute;

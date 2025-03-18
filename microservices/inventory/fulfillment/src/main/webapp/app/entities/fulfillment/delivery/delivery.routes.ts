import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DeliveryResolve from './route/delivery-routing-resolve.service';

const deliveryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/delivery.component').then(m => m.DeliveryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/delivery-detail.component').then(m => m.DeliveryDetailComponent),
    resolve: {
      delivery: DeliveryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/delivery-update.component').then(m => m.DeliveryUpdateComponent),
    resolve: {
      delivery: DeliveryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/delivery-update.component').then(m => m.DeliveryUpdateComponent),
    resolve: {
      delivery: DeliveryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default deliveryRoute;

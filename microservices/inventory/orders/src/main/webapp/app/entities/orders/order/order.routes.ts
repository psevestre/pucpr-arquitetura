import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OrderResolve from './route/order-routing-resolve.service';

const orderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order.component').then(m => m.OrderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-detail.component').then(m => m.OrderDetailComponent),
    resolve: {
      order: OrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-update.component').then(m => m.OrderUpdateComponent),
    resolve: {
      order: OrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-update.component').then(m => m.OrderUpdateComponent),
    resolve: {
      order: OrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orderRoute;

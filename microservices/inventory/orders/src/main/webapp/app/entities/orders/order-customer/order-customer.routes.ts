import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OrderCustomerResolve from './route/order-customer-routing-resolve.service';

const orderCustomerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-customer.component').then(m => m.OrderCustomerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-customer-detail.component').then(m => m.OrderCustomerDetailComponent),
    resolve: {
      orderCustomer: OrderCustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-customer-update.component').then(m => m.OrderCustomerUpdateComponent),
    resolve: {
      orderCustomer: OrderCustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-customer-update.component').then(m => m.OrderCustomerUpdateComponent),
    resolve: {
      orderCustomer: OrderCustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orderCustomerRoute;

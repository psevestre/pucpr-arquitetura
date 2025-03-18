import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import StockItemResolve from './route/stock-item-routing-resolve.service';

const stockItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/stock-item.component').then(m => m.StockItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/stock-item-detail.component').then(m => m.StockItemDetailComponent),
    resolve: {
      stockItem: StockItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/stock-item-update.component').then(m => m.StockItemUpdateComponent),
    resolve: {
      stockItem: StockItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/stock-item-update.component').then(m => m.StockItemUpdateComponent),
    resolve: {
      stockItem: StockItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockItemRoute;

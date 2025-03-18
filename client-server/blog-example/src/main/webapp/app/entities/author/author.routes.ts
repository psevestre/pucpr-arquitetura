import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AuthorResolve from './route/author-routing-resolve.service';

const authorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/author.component').then(m => m.AuthorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/author-detail.component').then(m => m.AuthorDetailComponent),
    resolve: {
      author: AuthorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/author-update.component').then(m => m.AuthorUpdateComponent),
    resolve: {
      author: AuthorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/author-update.component').then(m => m.AuthorUpdateComponent),
    resolve: {
      author: AuthorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default authorRoute;

import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CommentResolve from './route/comment-routing-resolve.service';

const commentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/comment.component').then(m => m.CommentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/comment-detail.component').then(m => m.CommentDetailComponent),
    resolve: {
      comment: CommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/comment-update.component').then(m => m.CommentUpdateComponent),
    resolve: {
      comment: CommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/comment-update.component').then(m => m.CommentUpdateComponent),
    resolve: {
      comment: CommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default commentRoute;

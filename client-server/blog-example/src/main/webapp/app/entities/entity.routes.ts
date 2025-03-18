import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'blogApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'author',
    data: { pageTitle: 'blogApp.author.home.title' },
    loadChildren: () => import('./author/author.routes'),
  },
  {
    path: 'post',
    data: { pageTitle: 'blogApp.post.home.title' },
    loadChildren: () => import('./post/post.routes'),
  },
  {
    path: 'comment',
    data: { pageTitle: 'blogApp.comment.home.title' },
    loadChildren: () => import('./comment/comment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

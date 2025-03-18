import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'stock-item',
    data: { pageTitle: 'stockApp.stockStockItem.home.title' },
    loadChildren: () => import('./stock/stock-item/stock-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

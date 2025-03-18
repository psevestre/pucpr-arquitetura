import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'delivery',
    data: { pageTitle: 'fulfillmentApp.fulfillmentDelivery.home.title' },
    loadChildren: () => import('./fulfillment/delivery/delivery.routes'),
  },
  {
    path: 'delivery-item',
    data: { pageTitle: 'fulfillmentApp.fulfillmentDeliveryItem.home.title' },
    loadChildren: () => import('./fulfillment/delivery-item/delivery-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

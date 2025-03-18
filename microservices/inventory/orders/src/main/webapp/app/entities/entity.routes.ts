import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'order',
    data: { pageTitle: 'ordersApp.ordersOrder.home.title' },
    loadChildren: () => import('./orders/order/order.routes'),
  },
  {
    path: 'order-item',
    data: { pageTitle: 'ordersApp.ordersOrderItem.home.title' },
    loadChildren: () => import('./orders/order-item/order-item.routes'),
  },
  {
    path: 'order-customer',
    data: { pageTitle: 'ordersApp.ordersOrderCustomer.home.title' },
    loadChildren: () => import('./orders/order-customer/order-customer.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Order',
    route: '/orders/order',
    translationKey: 'global.menu.entities.ordersOrder',
  },
  {
    name: 'OrderItem',
    route: '/orders/order-item',
    translationKey: 'global.menu.entities.ordersOrderItem',
  },
  {
    name: 'OrderCustomer',
    route: '/orders/order-customer',
    translationKey: 'global.menu.entities.ordersOrderCustomer',
  },
];

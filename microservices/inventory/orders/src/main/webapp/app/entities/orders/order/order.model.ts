import dayjs from 'dayjs/esm';
import { IOrderCustomer } from 'app/entities/orders/order-customer/order-customer.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IOrder {
  id: number;
  orderId?: string | null;
  createdAt?: dayjs.Dayjs | null;
  status?: keyof typeof OrderStatus | null;
  customer?: IOrderCustomer | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };

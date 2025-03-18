import { IOrder } from 'app/entities/orders/order/order.model';

export interface IOrderItem {
  id: number;
  sku?: string | null;
  description?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  totalPrice?: number | null;
  order?: IOrder | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };

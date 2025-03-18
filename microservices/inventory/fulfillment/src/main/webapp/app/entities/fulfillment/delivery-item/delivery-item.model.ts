import { IDelivery } from 'app/entities/fulfillment/delivery/delivery.model';

export interface IDeliveryItem {
  id: number;
  sku?: string | null;
  description?: string | null;
  quantity?: number | null;
  weight?: number | null;
  volume?: number | null;
  delivery?: IDelivery | null;
}

export type NewDeliveryItem = Omit<IDeliveryItem, 'id'> & { id: null };

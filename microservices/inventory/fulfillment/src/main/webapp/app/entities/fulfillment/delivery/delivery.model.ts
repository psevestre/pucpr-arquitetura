import dayjs from 'dayjs/esm';
import { DeliveryStatus } from 'app/entities/enumerations/delivery-status.model';

export interface IDelivery {
  id: number;
  orderId?: string | null;
  status?: keyof typeof DeliveryStatus | null;
  customerId?: string | null;
  name?: string | null;
  email?: string | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  zipCode?: string | null;
  city?: string | null;
  country?: string | null;
  deliveryInstructions?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewDelivery = Omit<IDelivery, 'id'> & { id: null };

import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 26110,
  orderId: 'ick willfully',
  createdAt: dayjs('2025-03-17T07:47'),
  status: 'DELIVERED',
};

export const sampleWithPartialData: IOrder = {
  id: 19840,
  orderId: 'who given',
  createdAt: dayjs('2025-03-17T11:35'),
  status: 'CREATED',
};

export const sampleWithFullData: IOrder = {
  id: 27813,
  orderId: 'along against',
  createdAt: dayjs('2025-03-17T05:13'),
  status: 'DELIVERED',
};

export const sampleWithNewData: NewOrder = {
  orderId: 'boohoo dishonor',
  createdAt: dayjs('2025-03-17T21:35'),
  status: 'DELIVERED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

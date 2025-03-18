import dayjs from 'dayjs/esm';

import { IDelivery, NewDelivery } from './delivery.model';

export const sampleWithRequiredData: IDelivery = {
  id: 30085,
  orderId: 'sprinkles',
  status: 'IN_TRANSIT',
  customerId: 'pfft heartbeat countess',
  name: 'whereas bliss ashamed',
  email: 'MariaAlice24@yahoo.com',
  addressLine1: 'step boldly gadzooks',
  addressLine2: 'valiantly splosh',
  zipCode: '30056-205',
  createdAt: dayjs('2025-03-17T19:13'),
  updatedAt: dayjs('2025-03-18T00:20'),
};

export const sampleWithPartialData: IDelivery = {
  id: 14818,
  orderId: 'utterly um sarcastic',
  status: 'IN_TRANSIT',
  customerId: 'doodle abaft',
  name: 'ah',
  email: 'Lorena_Moraes@gmail.com',
  addressLine1: 'incidentally including',
  addressLine2: 'considering critical viciously',
  zipCode: '20770-503',
  city: 'Silva do Descoberto',
  createdAt: dayjs('2025-03-17T14:13'),
  updatedAt: dayjs('2025-03-17T17:09'),
};

export const sampleWithFullData: IDelivery = {
  id: 6503,
  orderId: 'liberalize numb reproachfully',
  status: 'DELIVERED',
  customerId: 'save sedately inscribe',
  name: 'fast unless ick',
  email: 'Yasmin24@live.com',
  addressLine1: 'yippee beneath',
  addressLine2: 'impish',
  zipCode: '29493-350',
  city: 'Núbia de Nossa Senhora',
  country: 'Reino Unido',
  deliveryInstructions: 'commonly',
  createdAt: dayjs('2025-03-17T21:51'),
  updatedAt: dayjs('2025-03-17T07:19'),
};

export const sampleWithNewData: NewDelivery = {
  orderId: 'frantically meh',
  status: 'IN_TRANSIT',
  customerId: 'phooey opposite repentant',
  name: 'gloss on',
  email: 'Sirineu.Moraes75@yahoo.com',
  addressLine1: 'if deer',
  addressLine2: 'wherever',
  zipCode: '35607-349',
  createdAt: dayjs('2025-03-17T22:29'),
  updatedAt: dayjs('2025-03-17T10:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

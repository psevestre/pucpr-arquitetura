import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: 16549,
  sku: 'exactly about wherever',
  quantity: 3386,
  unitPrice: 20727.12,
  totalPrice: 4830.48,
};

export const sampleWithPartialData: IOrderItem = {
  id: 27299,
  sku: 'openly governance obnoxiously',
  quantity: 27052,
  unitPrice: 2320.77,
  totalPrice: 28422.94,
};

export const sampleWithFullData: IOrderItem = {
  id: 6728,
  sku: 'carpool consequently',
  description: 'festival following',
  quantity: 17937,
  unitPrice: 26542.46,
  totalPrice: 16249.51,
};

export const sampleWithNewData: NewOrderItem = {
  sku: 'fooey modulo',
  quantity: 4842,
  unitPrice: 22049.33,
  totalPrice: 17372.32,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

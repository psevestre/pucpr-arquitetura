import { IStockItem, NewStockItem } from './stock-item.model';

export const sampleWithRequiredData: IStockItem = {
  id: 6854,
  sku: 'yet',
  description: 'duh',
  available: 25676,
  reserved: 27360,
  minStock: 28037,
};

export const sampleWithPartialData: IStockItem = {
  id: 20673,
  sku: 'dash midst',
  description: 'what merit',
  available: 13995,
  reserved: 28152,
  minStock: 196,
};

export const sampleWithFullData: IStockItem = {
  id: 20266,
  sku: 'tomatillo concerning',
  description: 'minty exhaust vivaciously',
  available: 27656,
  reserved: 5401,
  minStock: 17148,
};

export const sampleWithNewData: NewStockItem = {
  sku: 'because',
  description: 'retrospectivity',
  available: 25969,
  reserved: 29241,
  minStock: 20484,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

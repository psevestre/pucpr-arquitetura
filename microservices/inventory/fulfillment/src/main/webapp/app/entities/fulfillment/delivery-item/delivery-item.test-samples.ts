import { IDeliveryItem, NewDeliveryItem } from './delivery-item.model';

export const sampleWithRequiredData: IDeliveryItem = {
  id: 15838,
  sku: 'energetically',
  quantity: 20597,
  weight: 19116.87,
  volume: 6012.06,
};

export const sampleWithPartialData: IDeliveryItem = {
  id: 21222,
  sku: 'agreeable implode dispense',
  quantity: 8597,
  weight: 6367.04,
  volume: 6546.71,
};

export const sampleWithFullData: IDeliveryItem = {
  id: 26264,
  sku: 'moral yuck exalt',
  description: 'pants sway majestic',
  quantity: 15430,
  weight: 15165.43,
  volume: 22752.5,
};

export const sampleWithNewData: NewDeliveryItem = {
  sku: 'inculcate',
  quantity: 23837,
  weight: 1232.6,
  volume: 14429.05,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

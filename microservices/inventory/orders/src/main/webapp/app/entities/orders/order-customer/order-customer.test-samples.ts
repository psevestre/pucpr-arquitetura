import { IOrderCustomer, NewOrderCustomer } from './order-customer.model';

export const sampleWithRequiredData: IOrderCustomer = {
  id: 6289,
  customerId: 'consequently how',
  name: 'instead um designation',
  email: 'Joaquim3@gmail.com',
  addressLine1: 'friendly prime hmph',
  addressLine2: 'cardboard',
  zipCode: '82052-610',
};

export const sampleWithPartialData: IOrderCustomer = {
  id: 4286,
  customerId: 'gadzooks',
  name: 'victorious so',
  email: 'MariaCecilia.Santos@live.com',
  addressLine1: 'for ah',
  addressLine2: 'mechanically handful',
  zipCode: '67547-423',
  city: 'Fabiano do Sul',
  country: 'Guernesey',
};

export const sampleWithFullData: IOrderCustomer = {
  id: 20283,
  customerId: 'phew faithfully ack',
  name: 'bore boom eek',
  email: 'Ladislau52@bol.com.br',
  addressLine1: 'microchip shyly',
  addressLine2: 'who nor notwithstanding',
  zipCode: '45321-989',
  city: 'Pietro de Nossa Senhora',
  country: 'Dinamarca',
};

export const sampleWithNewData: NewOrderCustomer = {
  customerId: 'in a onto',
  name: 'phew pish after',
  email: 'Emanuelly_Reis@hotmail.com',
  addressLine1: 'fooey jump ornery',
  addressLine2: 'psst',
  zipCode: '10495-360',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

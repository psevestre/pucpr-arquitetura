import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 24433,
  name: 'meh hover',
  email: 'Yango.Saraiva@yahoo.com',
  active: true,
};

export const sampleWithPartialData: IAuthor = {
  id: 12726,
  name: 'gosh',
  email: 'Gubio.Franco@hotmail.com',
  active: true,
};

export const sampleWithFullData: IAuthor = {
  id: 16232,
  name: 'probe meh',
  email: 'Janaina85@bol.com.br',
  active: false,
};

export const sampleWithNewData: NewAuthor = {
  name: 'upbeat since bah',
  email: 'Ofelia56@gmail.com',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

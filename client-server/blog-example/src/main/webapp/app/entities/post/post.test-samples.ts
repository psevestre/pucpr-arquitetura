import dayjs from 'dayjs/esm';

import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 8730,
  title: 'lawful above unless',
  content: '../fake-data/blob/hipster.txt',
  creationDate: dayjs('2025-03-16T00:32'),
};

export const sampleWithPartialData: IPost = {
  id: 26014,
  title: 'boohoo',
  content: '../fake-data/blob/hipster.txt',
  creationDate: dayjs('2025-03-16T21:13'),
};

export const sampleWithFullData: IPost = {
  id: 9917,
  title: 'clearly darn icy',
  content: '../fake-data/blob/hipster.txt',
  creationDate: dayjs('2025-03-16T06:29'),
  publishDate: dayjs('2025-03-16T18:34'),
};

export const sampleWithNewData: NewPost = {
  title: 'willfully settler',
  content: '../fake-data/blob/hipster.txt',
  creationDate: dayjs('2025-03-16T00:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

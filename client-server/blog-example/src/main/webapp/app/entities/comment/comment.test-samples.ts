import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 20452,
  content: 'and better',
  creationDate: dayjs('2025-03-16T08:09'),
  userId: 'slink carpool',
};

export const sampleWithPartialData: IComment = {
  id: 6771,
  content: 'norm athwart',
  creationDate: dayjs('2025-03-16T18:06'),
  publishDate: dayjs('2025-03-16T03:39'),
  userId: 'key ascribe pfft',
};

export const sampleWithFullData: IComment = {
  id: 28427,
  content: 'testing ugh',
  creationDate: dayjs('2025-03-16T12:19'),
  publishDate: dayjs('2025-03-16T08:30'),
  userId: 'where next weekly',
};

export const sampleWithNewData: NewComment = {
  content: 'metabolise pish jubilant',
  creationDate: dayjs('2025-03-16T11:02'),
  userId: 'ha gray gnaw',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

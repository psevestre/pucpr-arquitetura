import dayjs from 'dayjs/esm';
import { IAuthor } from 'app/entities/author/author.model';

export interface IPost {
  id: number;
  title?: string | null;
  content?: string | null;
  creationDate?: dayjs.Dayjs | null;
  publishDate?: dayjs.Dayjs | null;
  author?: IAuthor | null;
}

export type NewPost = Omit<IPost, 'id'> & { id: null };

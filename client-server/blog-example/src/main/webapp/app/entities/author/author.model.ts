export interface IAuthor {
  id: number;
  name?: string | null;
  email?: string | null;
  active?: boolean | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };

export interface IStockItem {
  id: number;
  sku?: string | null;
  description?: string | null;
  available?: number | null;
  reserved?: number | null;
  minStock?: number | null;
}

export type NewStockItem = Omit<IStockItem, 'id'> & { id: null };

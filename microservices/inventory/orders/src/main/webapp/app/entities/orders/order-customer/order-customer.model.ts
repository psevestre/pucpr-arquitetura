export interface IOrderCustomer {
  id: number;
  customerId?: string | null;
  name?: string | null;
  email?: string | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  zipCode?: string | null;
  city?: string | null;
  country?: string | null;
}

export type NewOrderCustomer = Omit<IOrderCustomer, 'id'> & { id: null };

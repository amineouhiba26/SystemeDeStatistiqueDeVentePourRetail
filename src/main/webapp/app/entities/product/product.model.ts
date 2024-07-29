export interface IProduct {
  id: string;
  sku?: string | null;
  name?: string | null;
  price?: number | null;
  discountAmount?: number | null;
  category?: string | null;
  capacity?: string | null;
  brand?: string | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };

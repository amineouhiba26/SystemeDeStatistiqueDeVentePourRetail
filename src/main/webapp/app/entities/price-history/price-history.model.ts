import { IProduct } from 'app/entities/product/product.model';

export interface IPriceHistory {
  id: string;
  oldPrice?: number | null;
  newPrice?: number | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewPriceHistory = Omit<IPriceHistory, 'id'> & { id: null };

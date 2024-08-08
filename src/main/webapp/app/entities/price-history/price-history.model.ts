import { IProduct } from '../product/product.model';

export interface IPriceHistory {
  createdDate: Date;
  id: string;
  oldPrice?: number | null;
  newPrice?: number | null;
  product?: IProduct | null;
}

export type NewPriceHistory = Omit<IPriceHistory, 'id'> & { id: null };

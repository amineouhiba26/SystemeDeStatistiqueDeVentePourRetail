import { IProduct } from 'app/entities/product/product.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderItem {
  id: string;
  quantityOrdered?: number | null;
  quantityCancelled?: number | null;
  quantityInvoiced?: number | null;
  price?: number | null;
  product?: Pick<IProduct, 'id'> | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };

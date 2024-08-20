import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IProductCancellations {
  id: string;
  reason?: string | null;
  orderItem?: IOrderItem | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewProductCancellations = Omit<IProductCancellations, 'id'> & { id: null };

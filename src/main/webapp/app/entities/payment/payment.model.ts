import dayjs from 'dayjs/esm';
import { IOrder } from 'app/entities/order/order.model';

export interface IPayment {
  id: string;
  amount?: number | null;
  paymentMehodName?: string | null;
  paymentMehodeCode?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  status?: string | null;
  payment?: Pick<IOrder, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };

import dayjs from 'dayjs/esm';

export interface IOrder {
  id: string;
  incrementId?: string | null;
  orderDate?: dayjs.Dayjs | null;
  status?: string | null;
  grandTotal?: number | null;
  totalInvoiced?: number | null;
  totalDue?: number | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };

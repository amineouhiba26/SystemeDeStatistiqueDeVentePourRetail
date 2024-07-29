import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: '75b8b579-d1ae-490d-bf79-9f7f3c2b3616',
  incrementId: 'engine',
  orderDate: dayjs('2024-07-25T19:58'),
  status: 'bypassing',
  grandTotal: 25173,
};

export const sampleWithPartialData: IOrder = {
  id: 'bf9737f5-d32e-4397-bd52-2f50ce512924',
  incrementId: 'Wooden one-to-one',
  orderDate: dayjs('2024-07-25T17:29'),
  status: 'Lead',
  grandTotal: 55186,
};

export const sampleWithFullData: IOrder = {
  id: '101bfa9d-3dda-41a8-bdca-824ec1afd43f',
  incrementId: 'rich',
  orderDate: dayjs('2024-07-25T23:55'),
  status: 'GB Pizza',
  grandTotal: 97552,
  totalInvoiced: 13402,
  totalDue: 62062,
};

export const sampleWithNewData: NewOrder = {
  incrementId: 'Home',
  orderDate: dayjs('2024-07-25T17:29'),
  status: 'Wooden card driver',
  grandTotal: 29069,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

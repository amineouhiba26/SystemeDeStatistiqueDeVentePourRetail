import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: '7bb75e76-458a-47ee-97c2-272eb2852f49',
};

export const sampleWithPartialData: IPayment = {
  id: '9e89be33-7efa-46a0-9709-ce2dc746aa30',
  amount: 99767,
  paymentMehodName: 'encryption hack',
  paymentMehodeCode: 'Sleek',
  status: 'Books',
};

export const sampleWithFullData: IPayment = {
  id: 'a4c37e9b-a3db-4bb2-9e50-aa778d35e6b0',
  amount: 23675,
  paymentMehodName: 'Cotton Developer Dollar',
  paymentMehodeCode: 'Group',
  paymentDate: dayjs('2024-07-25T12:41'),
  status: 'plum Indiana',
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: '9db4928b-a829-49d0-a2c6-04ecf1208cb6',
  quantityOrdered: 29017,
  price: 19312,
};

export const sampleWithPartialData: IOrderItem = {
  id: '6edde6e6-5c5f-4873-835f-3360285631f9',
  quantityOrdered: 2771,
  quantityCancelled: 23070,
  price: 11392,
};

export const sampleWithFullData: IOrderItem = {
  id: '006e64e8-15a5-4558-a963-7158007fc36a',
  quantityOrdered: 31469,
  quantityCancelled: 29873,
  quantityInvoiced: 7340,
  price: 7099,
};

export const sampleWithNewData: NewOrderItem = {
  quantityOrdered: 65965,
  price: 16907,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

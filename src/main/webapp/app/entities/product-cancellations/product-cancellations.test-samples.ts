import { IProductCancellations, NewProductCancellations } from './product-cancellations.model';

export const sampleWithRequiredData: IProductCancellations = {
  id: '9e3006eb-316a-44ff-81bc-3698708034bd',
};

export const sampleWithPartialData: IProductCancellations = {
  id: 'effa7604-089c-4e5d-8239-684db279daa2',
};

export const sampleWithFullData: IProductCancellations = {
  id: '190bc568-2fcd-459a-833d-bc3093688e0f',
  reason: 'withdrawal',
};

export const sampleWithNewData: NewProductCancellations = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

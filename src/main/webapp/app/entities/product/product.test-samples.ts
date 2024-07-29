import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 'c442dfc5-f1e3-4a23-b568-16f6b07dcadc',
  sku: 'Account experiences Garden',
  name: 'Fresh',
  price: 76163,
  category: 'Plastic Incredible Agent',
};

export const sampleWithPartialData: IProduct = {
  id: 'b5084bec-cd55-42ab-a2c1-374ca3b776a4',
  sku: 'architectures enhance',
  name: 'bricks-and-clicks transmitter',
  price: 23843,
  discountAmount: 36917,
  category: 'context-sensitive Operations pixel',
};

export const sampleWithFullData: IProduct = {
  id: 'bcfc4d92-e5b6-4d07-a564-eb7f6530bf16',
  sku: 'Planner',
  name: 'Movies Rubber',
  price: 93874,
  discountAmount: 72698,
  category: 'collaboration',
  capacity: 'Dobra Canyon',
  brand: 'e-enable Jewelery',
};

export const sampleWithNewData: NewProduct = {
  sku: 'viral Circles',
  name: 'Account Car Green',
  price: 69801,
  category: 'plum Officer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

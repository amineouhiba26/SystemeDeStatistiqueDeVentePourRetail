import { IPriceHistory, NewPriceHistory } from './price-history.model';

export const sampleWithRequiredData: IPriceHistory = {
  id: '7317c43c-fee1-4384-a29c-a2d3b534b0a3',
  oldPrice: 37060,
  newPrice: 26423,
};

export const sampleWithPartialData: IPriceHistory = {
  id: 'f6e33833-5986-4e13-a730-17676755249f',
  oldPrice: 13259,
  newPrice: 86163,
};

export const sampleWithFullData: IPriceHistory = {
  id: '76601845-b34f-4c8c-96a2-cd66291a8c36',
  oldPrice: 55265,
  newPrice: 16874,
};

export const sampleWithNewData: NewPriceHistory = {
  oldPrice: 73854,
  newPrice: 86895,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

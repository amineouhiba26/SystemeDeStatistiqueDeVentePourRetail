import { IUserConf, NewUserConf } from './user-conf.model';

export const sampleWithRequiredData: IUserConf = {
  id: 'a598287e-4268-4a19-9e95-27b081938d42',
  username: 'platforms',
  password: 'Assistant Buckinghamshire SDD',
};

export const sampleWithPartialData: IUserConf = {
  id: '97e57f18-5b68-49ce-bdfe-f91fa070d8f2',
  username: 'Turks pink',
  password: 'indexing salmon',
  phoneNumber: 91830,
};

export const sampleWithFullData: IUserConf = {
  id: 'd41a2d85-489d-498d-9767-1c056c4bd38f',
  username: 'Books HTTP',
  password: 'Ouguiya HDD',
  firstName: 'Ashton',
  lastName: 'Klein',
  email: 'Ian_Schiller81@hotmail.com',
  phoneNumber: 25992,
};

export const sampleWithNewData: NewUserConf = {
  username: 'Louisiana',
  password: 'Electronics',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

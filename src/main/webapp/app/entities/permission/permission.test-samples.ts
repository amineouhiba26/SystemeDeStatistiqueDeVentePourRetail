import { IPermission, NewPermission } from './permission.model';

export const sampleWithRequiredData: IPermission = {
  id: '3cb4eb37-c372-4263-8e16-8904d370a6c6',
  name: 'repurpose Focused',
};

export const sampleWithPartialData: IPermission = {
  id: '7d3322c0-d2b5-4d7a-a1d8-dd7ec2eee730',
  name: 'Chicken',
  description: 'Account',
};

export const sampleWithFullData: IPermission = {
  id: '3a89f52b-8845-4b72-88c4-c470b472bb57',
  name: 'CFP',
  description: 'web-readiness robust',
};

export const sampleWithNewData: NewPermission = {
  name: 'Shoes',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

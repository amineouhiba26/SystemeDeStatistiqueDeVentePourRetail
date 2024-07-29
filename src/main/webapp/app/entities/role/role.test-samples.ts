import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: '9f76f9d6-657b-4d73-b487-0442d0cc8bd7',
  name: 'methodical',
};

export const sampleWithPartialData: IRole = {
  id: '2b1dde51-4edd-405a-89f0-2753427a117f',
  name: 'SMTP',
};

export const sampleWithFullData: IRole = {
  id: 'bf8a05ab-738b-429a-bb30-8110f2ee4d39',
  name: 'maroon Horizontal Underpass',
  description: 'Plastic moderator',
};

export const sampleWithNewData: NewRole = {
  name: 'payment Virginia Rustic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import { IRole } from 'app/entities/role/role.model';

export interface IPermission {
  id: string;
  name?: string | null;
  description?: string | null;
  roles?: Pick<IRole, 'id'>[] | null;
}

export type NewPermission = Omit<IPermission, 'id'> & { id: null };

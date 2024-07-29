import { IPermission } from 'app/entities/permission/permission.model';

export interface IRole {
  id: string;
  name?: string | null;
  description?: string | null;
  permissions?: Pick<IPermission, 'id'>[] | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };

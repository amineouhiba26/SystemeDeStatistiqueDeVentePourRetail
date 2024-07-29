import { IRole } from 'app/entities/role/role.model';

export interface IUserConf {
  id: string;
  username?: string | null;
  password?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: number | null;
  role?: Pick<IRole, 'id'> | null;
}

export type NewUserConf = Omit<IUserConf, 'id'> & { id: null };

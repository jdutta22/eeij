import { IUser } from 'app/entities/user/user.model';

export interface IUserDetails {
  id?: number;
  designation?: string | null;
  caste?: string | null;
  address?: string | null;
  department?: string | null;
  discipline?: string | null;
  state?: string | null;
  bankAccount?: number | null;
  ifscCode?: string | null;
  user?: IUser | null;
}

export class UserDetails implements IUserDetails {
  constructor(
    public id?: number,
    public designation?: string | null,
    public caste?: string | null,
    public address?: string | null,
    public department?: string | null,
    public discipline?: string | null,
    public state?: string | null,
    public bankAccount?: number | null,
    public ifscCode?: string | null,
    public user?: IUser | null
  ) {}
}

export function getUserDetailsIdentifier(userDetails: IUserDetails): number | undefined {
  return userDetails.id;
}

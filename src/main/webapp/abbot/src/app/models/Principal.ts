
export interface Authority
{
  authority: string;
}

export interface Principal
{
  authenticated: boolean;
  authorities: Authority[];
  name: string;
  prinicpal: string;
  isAdmin: boolean;
  password: string;
}

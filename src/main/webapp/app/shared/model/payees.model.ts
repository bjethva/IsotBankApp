export interface IPayees {
  id?: number;
  payeeID?: number;
  email?: string;
  firstName?: string;
  lastName?: string;
  telephone?: string;
  userTOPayeeLogin?: string;
  userTOPayeeId?: number;
}

export const defaultValue: Readonly<IPayees> = {};

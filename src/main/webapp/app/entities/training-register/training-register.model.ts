import { ITraining } from 'app/entities/training/training.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITrainingRegister {
  id?: number;
  userAttendance?: number | null;
  cerificate?: boolean | null;
  training?: ITraining | null;
  user?: IUser | null;
}

export class TrainingRegister implements ITrainingRegister {
  constructor(
    public id?: number,
    public userAttendance?: number | null,
    public cerificate?: boolean | null,
    public training?: ITraining | null,
    public user?: IUser | null
  ) {
    this.cerificate = this.cerificate ?? false;
  }
}

export function getTrainingRegisterIdentifier(trainingRegister: ITrainingRegister): number | undefined {
  return trainingRegister.id;
}

import * as dayjs from 'dayjs';

export interface ITraining {
  id?: number;
  trainingName?: string | null;
  trainingMode?: string | null;
  trainingType?: string | null;
  trainingStartDate?: dayjs.Dayjs | null;
  traingEndDate?: dayjs.Dayjs | null;
  trainingYear?: string | null;
  trainingRegistration?: boolean | null;
}

export class Training implements ITraining {
  constructor(
    public id?: number,
    public trainingName?: string | null,
    public trainingMode?: string | null,
    public trainingType?: string | null,
    public trainingStartDate?: dayjs.Dayjs | null,
    public traingEndDate?: dayjs.Dayjs | null,
    public trainingYear?: string | null,
    public trainingRegistration?: boolean | null
  ) {
    this.trainingRegistration = this.trainingRegistration ?? false;
  }
}

export function getTrainingIdentifier(training: ITraining): number | undefined {
  return training.id;
}

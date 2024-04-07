import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 329,
  login: 'lYxWir@4qIxfS\\i7\\`f5E\\k0PyNt',
};

export const sampleWithPartialData: IUser = {
  id: 18638,
  login: 'p22W@hNIOH\\SmOX6\\txX1\\[MrJT5\\dQ7',
};

export const sampleWithFullData: IUser = {
  id: 14905,
  login: '5E@XNlp\\ad',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

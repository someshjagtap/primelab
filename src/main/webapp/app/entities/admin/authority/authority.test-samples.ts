import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'edde7648-385f-4465-98ae-5920bfbf13ae',
};

export const sampleWithPartialData: IAuthority = {
  name: 'ad5e8076-ea58-4259-8799-de0c53821e8a',
};

export const sampleWithFullData: IAuthority = {
  name: '4b0be950-cd68-41bb-bb17-c27cce0e86d3',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

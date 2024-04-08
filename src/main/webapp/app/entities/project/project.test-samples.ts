import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 20726,
};

export const sampleWithPartialData: IProject = {
  id: 16399,
  reason: 'BUSSINESS',
  type: 'INTERNAL',
  division: 'COMPRESSER',
  category: 'QUALITY_B',
  priority: 'LOW',
  department: 'FINANCE',
  startDate: dayjs('2024-04-07T12:39'),
  location: 'hm sociable',
};

export const sampleWithFullData: IProject = {
  id: 28980,
  projectName: 'nearly garland',
  reason: 'DELERSHIP',
  type: 'EXTERNAL',
  division: 'FILTERS',
  category: 'QUALITY_B',
  priority: 'MEDIUM',
  department: 'STARTEGY',
  startDate: dayjs('2024-04-07T06:43'),
  endDate: dayjs('2024-04-07T17:43'),
  location: 'tremendous plastic equally',
  status: 'yawningly',
};

export const sampleWithNewData: NewProject = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

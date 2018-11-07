import { IProjectItem } from 'app/shared/model//project-item.model';

export interface IProject {
    id?: number;
    value?: string;
    items?: IProjectItem[];
}

export class Project implements IProject {
    constructor(public id?: number, public value?: string, public items?: IProjectItem[]) {}
}

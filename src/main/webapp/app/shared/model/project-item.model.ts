import { IItem } from 'app/shared/model//item.model';

export interface IProjectItem {
    id?: number;
    quantity?: number;
    items?: IItem[];
    projectId?: number;
}

export class ProjectItem implements IProjectItem {
    constructor(public id?: number, public quantity?: number, public items?: IItem[], public projectId?: number) {}
}

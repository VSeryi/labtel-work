export interface IProjectItem {
    id?: number;
    quantity?: number;
    itemValue?: string;
    itemId?: number;
    projectId?: number;
}

export class ProjectItem implements IProjectItem {
    constructor(
        public id?: number,
        public quantity?: number,
        public itemValue?: string,
        public itemId?: number,
        public projectId?: number
    ) {}
}

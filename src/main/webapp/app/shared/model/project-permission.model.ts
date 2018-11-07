export const enum TypePermission {
    READ = 'READ',
    READ_WRITE = 'READ_WRITE'
}

export interface IProjectPermission {
    id?: number;
    permission?: TypePermission;
    userLogin?: string;
    userId?: number;
    projectValue?: string;
    projectId?: number;
}

export class ProjectPermission implements IProjectPermission {
    constructor(
        public id?: number,
        public permission?: TypePermission,
        public userLogin?: string,
        public userId?: number,
        public projectValue?: string,
        public projectId?: number
    ) {}
}

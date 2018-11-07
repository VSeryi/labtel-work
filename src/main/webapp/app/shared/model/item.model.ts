import { IItem } from 'app/shared/model//item.model';

export const enum TypeItem {
    INDIVIDUAL = 'INDIVIDUAL',
    GROUP = 'GROUP'
}

export interface IItem {
    id?: number;
    value?: string;
    type?: TypeItem;
    categoryName?: string;
    categoryId?: number;
    elements?: IItem[];
}

export class Item implements IItem {
    constructor(
        public id?: number,
        public value?: string,
        public type?: TypeItem,
        public categoryName?: string,
        public categoryId?: number,
        public elements?: IItem[]
    ) {}
}

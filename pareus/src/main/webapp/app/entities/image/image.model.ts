import { BaseEntity } from './../../shared';

export class Image implements BaseEntity {
    constructor(
        public id?: number,
        public path?: string,
        public description?: string,
        public estateId?: number,
    ) {
    }
}

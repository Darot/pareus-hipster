import { BaseEntity } from './../../shared';

export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public street?: string,
        public houseNumber?: string,
        public zipCode?: string,
        public city?: string,
    ) {
    }
}

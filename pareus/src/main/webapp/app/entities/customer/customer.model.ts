import { BaseEntity } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public watchlistId?: number,
        public addressId?: number,
    ) {
    }
}

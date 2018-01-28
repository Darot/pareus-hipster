import { BaseEntity } from './../../shared';

export class Watchlist implements BaseEntity {
    constructor(
        public id?: number,
        public estates?: BaseEntity[],
        public customerId?: number,
    ) {
    }
}

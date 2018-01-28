import { BaseEntity } from './../../shared';

export class Estate implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public price?: number,
        public addressId?: number,
        public images?: BaseEntity[],
        public claimedById?: number,
        public watchlistId?: number,
    ) {
    }
}

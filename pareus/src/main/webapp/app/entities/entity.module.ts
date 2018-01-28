import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PareusEstateModule } from './estate/estate.module';
import { PareusImageModule } from './image/image.module';
import { PareusWatchlistModule } from './watchlist/watchlist.module';
import { PareusCustomerModule } from './customer/customer.module';
import { PareusEstateAgencyModule } from './estate-agency/estate-agency.module';
import { PareusAddressModule } from './address/address.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PareusEstateModule,
        PareusImageModule,
        PareusWatchlistModule,
        PareusCustomerModule,
        PareusEstateAgencyModule,
        PareusAddressModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PareusEntityModule {}

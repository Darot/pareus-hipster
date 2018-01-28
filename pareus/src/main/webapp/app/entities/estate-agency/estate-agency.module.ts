import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PareusSharedModule } from '../../shared';
import {
    EstateAgencyService,
    EstateAgencyPopupService,
    EstateAgencyComponent,
    EstateAgencyDetailComponent,
    EstateAgencyDialogComponent,
    EstateAgencyPopupComponent,
    EstateAgencyDeletePopupComponent,
    EstateAgencyDeleteDialogComponent,
    estateAgencyRoute,
    estateAgencyPopupRoute,
} from './';

const ENTITY_STATES = [
    ...estateAgencyRoute,
    ...estateAgencyPopupRoute,
];

@NgModule({
    imports: [
        PareusSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EstateAgencyComponent,
        EstateAgencyDetailComponent,
        EstateAgencyDialogComponent,
        EstateAgencyDeleteDialogComponent,
        EstateAgencyPopupComponent,
        EstateAgencyDeletePopupComponent,
    ],
    entryComponents: [
        EstateAgencyComponent,
        EstateAgencyDialogComponent,
        EstateAgencyPopupComponent,
        EstateAgencyDeleteDialogComponent,
        EstateAgencyDeletePopupComponent,
    ],
    providers: [
        EstateAgencyService,
        EstateAgencyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PareusEstateAgencyModule {}

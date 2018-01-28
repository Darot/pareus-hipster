import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PareusSharedModule } from '../../shared';
import {
    EstateService,
    EstatePopupService,
    EstateComponent,
    EstateDetailComponent,
    EstateDialogComponent,
    EstatePopupComponent,
    EstateDeletePopupComponent,
    EstateDeleteDialogComponent,
    estateRoute,
    estatePopupRoute,
} from './';

const ENTITY_STATES = [
    ...estateRoute,
    ...estatePopupRoute,
];

@NgModule({
    imports: [
        PareusSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EstateComponent,
        EstateDetailComponent,
        EstateDialogComponent,
        EstateDeleteDialogComponent,
        EstatePopupComponent,
        EstateDeletePopupComponent,
    ],
    entryComponents: [
        EstateComponent,
        EstateDialogComponent,
        EstatePopupComponent,
        EstateDeleteDialogComponent,
        EstateDeletePopupComponent,
    ],
    providers: [
        EstateService,
        EstatePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PareusEstateModule {}

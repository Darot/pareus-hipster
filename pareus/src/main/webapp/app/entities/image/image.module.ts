import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PareusSharedModule } from '../../shared';
import {
    ImageService,
    ImagePopupService,
    ImageComponent,
    ImageDetailComponent,
    ImageDialogComponent,
    ImagePopupComponent,
    ImageDeletePopupComponent,
    ImageDeleteDialogComponent,
    imageRoute,
    imagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...imageRoute,
    ...imagePopupRoute,
];

@NgModule({
    imports: [
        PareusSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ImageComponent,
        ImageDetailComponent,
        ImageDialogComponent,
        ImageDeleteDialogComponent,
        ImagePopupComponent,
        ImageDeletePopupComponent,
    ],
    entryComponents: [
        ImageComponent,
        ImageDialogComponent,
        ImagePopupComponent,
        ImageDeleteDialogComponent,
        ImageDeletePopupComponent,
    ],
    providers: [
        ImageService,
        ImagePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PareusImageModule {}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PareusSharedModule } from '../../shared';
import {
    WatchlistService,
    WatchlistPopupService,
    WatchlistComponent,
    WatchlistDetailComponent,
    WatchlistDialogComponent,
    WatchlistPopupComponent,
    WatchlistDeletePopupComponent,
    WatchlistDeleteDialogComponent,
    watchlistRoute,
    watchlistPopupRoute,
} from './';

const ENTITY_STATES = [
    ...watchlistRoute,
    ...watchlistPopupRoute,
];

@NgModule({
    imports: [
        PareusSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WatchlistComponent,
        WatchlistDetailComponent,
        WatchlistDialogComponent,
        WatchlistDeleteDialogComponent,
        WatchlistPopupComponent,
        WatchlistDeletePopupComponent,
    ],
    entryComponents: [
        WatchlistComponent,
        WatchlistDialogComponent,
        WatchlistPopupComponent,
        WatchlistDeleteDialogComponent,
        WatchlistDeletePopupComponent,
    ],
    providers: [
        WatchlistService,
        WatchlistPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PareusWatchlistModule {}

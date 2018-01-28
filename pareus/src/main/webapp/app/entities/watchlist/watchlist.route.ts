import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { WatchlistComponent } from './watchlist.component';
import { WatchlistDetailComponent } from './watchlist-detail.component';
import { WatchlistPopupComponent } from './watchlist-dialog.component';
import { WatchlistDeletePopupComponent } from './watchlist-delete-dialog.component';

export const watchlistRoute: Routes = [
    {
        path: 'watchlist',
        component: WatchlistComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.watchlist.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'watchlist/:id',
        component: WatchlistDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.watchlist.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const watchlistPopupRoute: Routes = [
    {
        path: 'watchlist-new',
        component: WatchlistPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.watchlist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'watchlist/:id/edit',
        component: WatchlistPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.watchlist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'watchlist/:id/delete',
        component: WatchlistDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.watchlist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

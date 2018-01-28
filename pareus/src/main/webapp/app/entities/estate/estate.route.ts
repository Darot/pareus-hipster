import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { EstateComponent } from './estate.component';
import { EstateDetailComponent } from './estate-detail.component';
import { EstatePopupComponent } from './estate-dialog.component';
import { EstateDeletePopupComponent } from './estate-delete-dialog.component';

export const estateRoute: Routes = [
    {
        path: 'estate',
        component: EstateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'estate/:id',
        component: EstateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const estatePopupRoute: Routes = [
    {
        path: 'estate-new',
        component: EstatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'estate/:id/edit',
        component: EstatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'estate/:id/delete',
        component: EstateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

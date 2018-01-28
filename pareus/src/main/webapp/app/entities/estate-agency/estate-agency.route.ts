import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { EstateAgencyComponent } from './estate-agency.component';
import { EstateAgencyDetailComponent } from './estate-agency-detail.component';
import { EstateAgencyPopupComponent } from './estate-agency-dialog.component';
import { EstateAgencyDeletePopupComponent } from './estate-agency-delete-dialog.component';

export const estateAgencyRoute: Routes = [
    {
        path: 'estate-agency',
        component: EstateAgencyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estateAgency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'estate-agency/:id',
        component: EstateAgencyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estateAgency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const estateAgencyPopupRoute: Routes = [
    {
        path: 'estate-agency-new',
        component: EstateAgencyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estateAgency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'estate-agency/:id/edit',
        component: EstateAgencyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estateAgency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'estate-agency/:id/delete',
        component: EstateAgencyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pareusApp.estateAgency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

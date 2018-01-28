import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Estate } from './estate.model';
import { EstatePopupService } from './estate-popup.service';
import { EstateService } from './estate.service';
import { Address, AddressService } from '../address';
import { EstateAgency, EstateAgencyService } from '../estate-agency';
import { Watchlist, WatchlistService } from '../watchlist';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-estate-dialog',
    templateUrl: './estate-dialog.component.html'
})
export class EstateDialogComponent implements OnInit {

    estate: Estate;
    isSaving: boolean;

    addresses: Address[];

    estateagencies: EstateAgency[];

    watchlists: Watchlist[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private estateService: EstateService,
        private addressService: AddressService,
        private estateAgencyService: EstateAgencyService,
        private watchlistService: WatchlistService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.addressService
            .query({filter: 'estate-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.estate.addressId) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.estate.addressId)
                        .subscribe((subRes: Address) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.estateAgencyService.query()
            .subscribe((res: ResponseWrapper) => { this.estateagencies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.watchlistService.query()
            .subscribe((res: ResponseWrapper) => { this.watchlists = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.estate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.estateService.update(this.estate));
        } else {
            this.subscribeToSaveResponse(
                this.estateService.create(this.estate));
        }
    }

    private subscribeToSaveResponse(result: Observable<Estate>) {
        result.subscribe((res: Estate) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Estate) {
        this.eventManager.broadcast({ name: 'estateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAddressById(index: number, item: Address) {
        return item.id;
    }

    trackEstateAgencyById(index: number, item: EstateAgency) {
        return item.id;
    }

    trackWatchlistById(index: number, item: Watchlist) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-estate-popup',
    template: ''
})
export class EstatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private estatePopupService: EstatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.estatePopupService
                    .open(EstateDialogComponent as Component, params['id']);
            } else {
                this.estatePopupService
                    .open(EstateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

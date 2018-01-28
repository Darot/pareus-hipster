import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EstateAgency } from './estate-agency.model';
import { EstateAgencyPopupService } from './estate-agency-popup.service';
import { EstateAgencyService } from './estate-agency.service';
import { Address, AddressService } from '../address';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-estate-agency-dialog',
    templateUrl: './estate-agency-dialog.component.html'
})
export class EstateAgencyDialogComponent implements OnInit {

    estateAgency: EstateAgency;
    isSaving: boolean;

    addresses: Address[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private estateAgencyService: EstateAgencyService,
        private addressService: AddressService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.addressService
            .query({filter: 'estateagency-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.estateAgency.addressId) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.estateAgency.addressId)
                        .subscribe((subRes: Address) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.estateAgency.id !== undefined) {
            this.subscribeToSaveResponse(
                this.estateAgencyService.update(this.estateAgency));
        } else {
            this.subscribeToSaveResponse(
                this.estateAgencyService.create(this.estateAgency));
        }
    }

    private subscribeToSaveResponse(result: Observable<EstateAgency>) {
        result.subscribe((res: EstateAgency) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: EstateAgency) {
        this.eventManager.broadcast({ name: 'estateAgencyListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-estate-agency-popup',
    template: ''
})
export class EstateAgencyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private estateAgencyPopupService: EstateAgencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.estateAgencyPopupService
                    .open(EstateAgencyDialogComponent as Component, params['id']);
            } else {
                this.estateAgencyPopupService
                    .open(EstateAgencyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Watchlist } from './watchlist.model';
import { WatchlistPopupService } from './watchlist-popup.service';
import { WatchlistService } from './watchlist.service';
import { Customer, CustomerService } from '../customer';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-watchlist-dialog',
    templateUrl: './watchlist-dialog.component.html'
})
export class WatchlistDialogComponent implements OnInit {

    watchlist: Watchlist;
    isSaving: boolean;

    customers: Customer[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private watchlistService: WatchlistService,
        private customerService: CustomerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customerService.query()
            .subscribe((res: ResponseWrapper) => { this.customers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.watchlist.id !== undefined) {
            this.subscribeToSaveResponse(
                this.watchlistService.update(this.watchlist));
        } else {
            this.subscribeToSaveResponse(
                this.watchlistService.create(this.watchlist));
        }
    }

    private subscribeToSaveResponse(result: Observable<Watchlist>) {
        result.subscribe((res: Watchlist) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Watchlist) {
        this.eventManager.broadcast({ name: 'watchlistListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-watchlist-popup',
    template: ''
})
export class WatchlistPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private watchlistPopupService: WatchlistPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.watchlistPopupService
                    .open(WatchlistDialogComponent as Component, params['id']);
            } else {
                this.watchlistPopupService
                    .open(WatchlistDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

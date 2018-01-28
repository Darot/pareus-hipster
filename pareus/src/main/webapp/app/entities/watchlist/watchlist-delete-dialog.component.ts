import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Watchlist } from './watchlist.model';
import { WatchlistPopupService } from './watchlist-popup.service';
import { WatchlistService } from './watchlist.service';

@Component({
    selector: 'jhi-watchlist-delete-dialog',
    templateUrl: './watchlist-delete-dialog.component.html'
})
export class WatchlistDeleteDialogComponent {

    watchlist: Watchlist;

    constructor(
        private watchlistService: WatchlistService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.watchlistService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'watchlistListModification',
                content: 'Deleted an watchlist'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-watchlist-delete-popup',
    template: ''
})
export class WatchlistDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private watchlistPopupService: WatchlistPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.watchlistPopupService
                .open(WatchlistDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription as RxSubscription} from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Watchlist } from './watchlist.model';
import { WatchlistService } from './watchlist.service';

@Component({
    selector: 'jhi-watchlist-detail',
    templateUrl: './watchlist-detail.component.html'
})
export class WatchlistDetailComponent implements OnInit, OnDestroy {

    watchlist: Watchlist;
    private rxSubscription: any;
    private eventSubscriber: RxSubscription;

    constructor(
        private eventManager: JhiEventManager,
        private watchlistService: WatchlistService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.rxSubscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWatchlists();
    }

    load(id) {
        this.watchlistService.find(id).subscribe((watchlist) => {
            this.watchlist = watchlist;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.rxSubscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWatchlists() {
        this.eventSubscriber = this.eventManager.subscribe(
            'watchlistListModification',
            (response) => this.load(this.watchlist.id)
        );
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription as RxSubscription} from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Estate } from './estate.model';
import { EstateService } from './estate.service';

@Component({
    selector: 'jhi-estate-detail',
    templateUrl: './estate-detail.component.html'
})
export class EstateDetailComponent implements OnInit, OnDestroy {

    estate: Estate;
    private rxSubscription: any;
    private eventSubscriber: RxSubscription;

    constructor(
        private eventManager: JhiEventManager,
        private estateService: EstateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.rxSubscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEstates();
    }

    load(id) {
        this.estateService.find(id).subscribe((estate) => {
            this.estate = estate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.rxSubscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEstates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'estateListModification',
            (response) => this.load(this.estate.id)
        );
    }
}

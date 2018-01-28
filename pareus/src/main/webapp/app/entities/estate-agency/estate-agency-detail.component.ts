import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription as RxSubscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { EstateAgency } from './estate-agency.model';
import { EstateAgencyService } from './estate-agency.service';

@Component({
    selector: 'jhi-estate-agency-detail',
    templateUrl: './estate-agency-detail.component.html'
})
export class EstateAgencyDetailComponent implements OnInit, OnDestroy {

    estateAgency: EstateAgency;
    private rxSubscription: any;
    private eventSubscriber: RxSubscription;

    constructor(
        private eventManager: JhiEventManager,
        private estateAgencyService: EstateAgencyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.rxSubscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEstateAgencies();
    }

    load(id) {
        this.estateAgencyService.find(id).subscribe((estateAgency) => {
            this.estateAgency = estateAgency;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.rxSubscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEstateAgencies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'estateAgencyListModification',
            (response) => this.load(this.estateAgency.id)
        );
    }
}

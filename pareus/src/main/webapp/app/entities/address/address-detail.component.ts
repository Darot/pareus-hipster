import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription as RxSubscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Address } from './address.model';
import { AddressService } from './address.service';

@Component({
    selector: 'jhi-address-detail',
    templateUrl: './address-detail.component.html'
})
export class AddressDetailComponent implements OnInit, OnDestroy {

    address: Address;
    private rxSubscription: any;
    private eventSubscriber: RxSubscription;

    constructor(
        private eventManager: JhiEventManager,
        private addressService: AddressService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.rxSubscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAddresses();
    }

    load(id) {
        this.addressService.find(id).subscribe((address) => {
            this.address = address;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.rxSubscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAddresses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'addressListModification',
            (response) => this.load(this.address.id)
        );
    }
}

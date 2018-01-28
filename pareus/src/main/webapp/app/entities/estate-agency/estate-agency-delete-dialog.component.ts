import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EstateAgency } from './estate-agency.model';
import { EstateAgencyPopupService } from './estate-agency-popup.service';
import { EstateAgencyService } from './estate-agency.service';

@Component({
    selector: 'jhi-estate-agency-delete-dialog',
    templateUrl: './estate-agency-delete-dialog.component.html'
})
export class EstateAgencyDeleteDialogComponent {

    estateAgency: EstateAgency;

    constructor(
        private estateAgencyService: EstateAgencyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.estateAgencyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'estateAgencyListModification',
                content: 'Deleted an estateAgency'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-estate-agency-delete-popup',
    template: ''
})
export class EstateAgencyDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private estateAgencyPopupService: EstateAgencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.estateAgencyPopupService
                .open(EstateAgencyDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

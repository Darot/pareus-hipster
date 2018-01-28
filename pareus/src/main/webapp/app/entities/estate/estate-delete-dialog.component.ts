import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Estate } from './estate.model';
import { EstatePopupService } from './estate-popup.service';
import { EstateService } from './estate.service';

@Component({
    selector: 'jhi-estate-delete-dialog',
    templateUrl: './estate-delete-dialog.component.html'
})
export class EstateDeleteDialogComponent {

    estate: Estate;

    constructor(
        private estateService: EstateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.estateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'estateListModification',
                content: 'Deleted an estate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-estate-delete-popup',
    template: ''
})
export class EstateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private estatePopupService: EstatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.estatePopupService
                .open(EstateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

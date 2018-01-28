/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PareusTestModule } from '../../../test.module';
import { EstateDialogComponent } from '../../../../../../main/webapp/app/entities/estate/estate-dialog.component';
import { EstateService } from '../../../../../../main/webapp/app/entities/estate/estate.service';
import { Estate } from '../../../../../../main/webapp/app/entities/estate/estate.model';
import { AddressService } from '../../../../../../main/webapp/app/entities/address';
import { EstateAgencyService } from '../../../../../../main/webapp/app/entities/estate-agency';
import { WatchlistService } from '../../../../../../main/webapp/app/entities/watchlist';

describe('Component Tests', () => {

    describe('Estate Management Dialog Component', () => {
        let comp: EstateDialogComponent;
        let fixture: ComponentFixture<EstateDialogComponent>;
        let service: EstateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [EstateDialogComponent],
                providers: [
                    AddressService,
                    EstateAgencyService,
                    WatchlistService,
                    EstateService
                ]
            })
            .overrideTemplate(EstateDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EstateDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstateService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Estate(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.estate = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'estateListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Estate();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.estate = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'estateListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PareusTestModule } from '../../../test.module';
import { EstateAgencyDialogComponent } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency-dialog.component';
import { EstateAgencyService } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.service';
import { EstateAgency } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.model';
import { AddressService } from '../../../../../../main/webapp/app/entities/address';

describe('Component Tests', () => {

    describe('EstateAgency Management Dialog Component', () => {
        let comp: EstateAgencyDialogComponent;
        let fixture: ComponentFixture<EstateAgencyDialogComponent>;
        let service: EstateAgencyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [EstateAgencyDialogComponent],
                providers: [
                    AddressService,
                    EstateAgencyService
                ]
            })
            .overrideTemplate(EstateAgencyDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EstateAgencyDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstateAgencyService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new EstateAgency(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.estateAgency = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'estateAgencyListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new EstateAgency();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.estateAgency = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'estateAgencyListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

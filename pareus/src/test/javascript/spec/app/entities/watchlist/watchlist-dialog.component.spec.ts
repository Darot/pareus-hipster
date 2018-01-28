/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PareusTestModule } from '../../../test.module';
import { WatchlistDialogComponent } from '../../../../../../main/webapp/app/entities/watchlist/watchlist-dialog.component';
import { WatchlistService } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.service';
import { Watchlist } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.model';
import { CustomerService } from '../../../../../../main/webapp/app/entities/customer';

describe('Component Tests', () => {

    describe('Watchlist Management Dialog Component', () => {
        let comp: WatchlistDialogComponent;
        let fixture: ComponentFixture<WatchlistDialogComponent>;
        let service: WatchlistService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [WatchlistDialogComponent],
                providers: [
                    CustomerService,
                    WatchlistService
                ]
            })
            .overrideTemplate(WatchlistDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WatchlistDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchlistService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Watchlist(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.watchlist = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'watchlistListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Watchlist();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.watchlist = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'watchlistListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

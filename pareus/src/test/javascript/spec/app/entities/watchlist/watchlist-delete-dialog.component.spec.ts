/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PareusTestModule } from '../../../test.module';
import { WatchlistDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/watchlist/watchlist-delete-dialog.component';
import { WatchlistService } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.service';

describe('Component Tests', () => {

    describe('Watchlist Management Delete Component', () => {
        let comp: WatchlistDeleteDialogComponent;
        let fixture: ComponentFixture<WatchlistDeleteDialogComponent>;
        let service: WatchlistService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [WatchlistDeleteDialogComponent],
                providers: [
                    WatchlistService
                ]
            })
            .overrideTemplate(WatchlistDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WatchlistDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchlistService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

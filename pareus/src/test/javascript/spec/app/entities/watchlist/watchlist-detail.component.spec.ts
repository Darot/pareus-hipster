/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { PareusTestModule } from '../../../test.module';
import { WatchlistDetailComponent } from '../../../../../../main/webapp/app/entities/watchlist/watchlist-detail.component';
import { WatchlistService } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.service';
import { Watchlist } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.model';

describe('Component Tests', () => {

    describe('Watchlist Management Detail Component', () => {
        let comp: WatchlistDetailComponent;
        let fixture: ComponentFixture<WatchlistDetailComponent>;
        let service: WatchlistService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [WatchlistDetailComponent],
                providers: [
                    WatchlistService
                ]
            })
            .overrideTemplate(WatchlistDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WatchlistDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchlistService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Watchlist(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.watchlist).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

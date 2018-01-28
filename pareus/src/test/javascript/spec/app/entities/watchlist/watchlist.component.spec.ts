/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { PareusTestModule } from '../../../test.module';
import { WatchlistComponent } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.component';
import { WatchlistService } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.service';
import { Watchlist } from '../../../../../../main/webapp/app/entities/watchlist/watchlist.model';

describe('Component Tests', () => {

    describe('Watchlist Management Component', () => {
        let comp: WatchlistComponent;
        let fixture: ComponentFixture<WatchlistComponent>;
        let service: WatchlistService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [WatchlistComponent],
                providers: [
                    WatchlistService
                ]
            })
            .overrideTemplate(WatchlistComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WatchlistComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WatchlistService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Watchlist(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.watchlists[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

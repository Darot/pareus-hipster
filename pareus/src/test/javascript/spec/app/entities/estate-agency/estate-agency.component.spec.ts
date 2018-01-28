/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { PareusTestModule } from '../../../test.module';
import { EstateAgencyComponent } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.component';
import { EstateAgencyService } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.service';
import { EstateAgency } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.model';

describe('Component Tests', () => {

    describe('EstateAgency Management Component', () => {
        let comp: EstateAgencyComponent;
        let fixture: ComponentFixture<EstateAgencyComponent>;
        let service: EstateAgencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [EstateAgencyComponent],
                providers: [
                    EstateAgencyService
                ]
            })
            .overrideTemplate(EstateAgencyComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EstateAgencyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstateAgencyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new EstateAgency(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.estateAgencies[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { PareusTestModule } from '../../../test.module';
import { EstateAgencyDetailComponent } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency-detail.component';
import { EstateAgencyService } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.service';
import { EstateAgency } from '../../../../../../main/webapp/app/entities/estate-agency/estate-agency.model';

describe('Component Tests', () => {

    describe('EstateAgency Management Detail Component', () => {
        let comp: EstateAgencyDetailComponent;
        let fixture: ComponentFixture<EstateAgencyDetailComponent>;
        let service: EstateAgencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [EstateAgencyDetailComponent],
                providers: [
                    EstateAgencyService
                ]
            })
            .overrideTemplate(EstateAgencyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EstateAgencyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstateAgencyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new EstateAgency(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.estateAgency).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

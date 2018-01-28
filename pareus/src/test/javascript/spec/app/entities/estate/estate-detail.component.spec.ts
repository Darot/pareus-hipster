/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { PareusTestModule } from '../../../test.module';
import { EstateDetailComponent } from '../../../../../../main/webapp/app/entities/estate/estate-detail.component';
import { EstateService } from '../../../../../../main/webapp/app/entities/estate/estate.service';
import { Estate } from '../../../../../../main/webapp/app/entities/estate/estate.model';

describe('Component Tests', () => {

    describe('Estate Management Detail Component', () => {
        let comp: EstateDetailComponent;
        let fixture: ComponentFixture<EstateDetailComponent>;
        let service: EstateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [EstateDetailComponent],
                providers: [
                    EstateService
                ]
            })
            .overrideTemplate(EstateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EstateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Estate(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.estate).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

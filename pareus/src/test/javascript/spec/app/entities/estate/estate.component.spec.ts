/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { PareusTestModule } from '../../../test.module';
import { EstateComponent } from '../../../../../../main/webapp/app/entities/estate/estate.component';
import { EstateService } from '../../../../../../main/webapp/app/entities/estate/estate.service';
import { Estate } from '../../../../../../main/webapp/app/entities/estate/estate.model';

describe('Component Tests', () => {

    describe('Estate Management Component', () => {
        let comp: EstateComponent;
        let fixture: ComponentFixture<EstateComponent>;
        let service: EstateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PareusTestModule],
                declarations: [EstateComponent],
                providers: [
                    EstateService
                ]
            })
            .overrideTemplate(EstateComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EstateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Estate(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.estates[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

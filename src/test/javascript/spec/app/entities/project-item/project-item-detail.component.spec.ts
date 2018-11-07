/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LabtelTestModule } from '../../../test.module';
import { ProjectItemDetailComponent } from 'app/entities/project-item/project-item-detail.component';
import { ProjectItem } from 'app/shared/model/project-item.model';

describe('Component Tests', () => {
    describe('ProjectItem Management Detail Component', () => {
        let comp: ProjectItemDetailComponent;
        let fixture: ComponentFixture<ProjectItemDetailComponent>;
        const route = ({ data: of({ projectItem: new ProjectItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LabtelTestModule],
                declarations: [ProjectItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProjectItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProjectItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.projectItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

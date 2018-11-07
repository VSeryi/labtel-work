/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LabtelTestModule } from '../../../test.module';
import { ProjectPermissionDetailComponent } from 'app/entities/project-permission/project-permission-detail.component';
import { ProjectPermission } from 'app/shared/model/project-permission.model';

describe('Component Tests', () => {
    describe('ProjectPermission Management Detail Component', () => {
        let comp: ProjectPermissionDetailComponent;
        let fixture: ComponentFixture<ProjectPermissionDetailComponent>;
        const route = ({ data: of({ projectPermission: new ProjectPermission(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LabtelTestModule],
                declarations: [ProjectPermissionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProjectPermissionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProjectPermissionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.projectPermission).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

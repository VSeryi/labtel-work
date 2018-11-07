/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LabtelTestModule } from '../../../test.module';
import { ProjectItemUpdateComponent } from 'app/entities/project-item/project-item-update.component';
import { ProjectItemService } from 'app/entities/project-item/project-item.service';
import { ProjectItem } from 'app/shared/model/project-item.model';

describe('Component Tests', () => {
    describe('ProjectItem Management Update Component', () => {
        let comp: ProjectItemUpdateComponent;
        let fixture: ComponentFixture<ProjectItemUpdateComponent>;
        let service: ProjectItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LabtelTestModule],
                declarations: [ProjectItemUpdateComponent]
            })
                .overrideTemplate(ProjectItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProjectItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectItemService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ProjectItem(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.projectItem = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ProjectItem();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.projectItem = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});

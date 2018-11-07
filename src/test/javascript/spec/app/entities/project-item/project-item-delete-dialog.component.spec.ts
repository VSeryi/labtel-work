/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LabtelTestModule } from '../../../test.module';
import { ProjectItemDeleteDialogComponent } from 'app/entities/project-item/project-item-delete-dialog.component';
import { ProjectItemService } from 'app/entities/project-item/project-item.service';

describe('Component Tests', () => {
    describe('ProjectItem Management Delete Component', () => {
        let comp: ProjectItemDeleteDialogComponent;
        let fixture: ComponentFixture<ProjectItemDeleteDialogComponent>;
        let service: ProjectItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LabtelTestModule],
                declarations: [ProjectItemDeleteDialogComponent]
            })
                .overrideTemplate(ProjectItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProjectItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectItemService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});

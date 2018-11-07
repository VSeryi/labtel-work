import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProjectPermission } from 'app/shared/model/project-permission.model';
import { ProjectPermissionService } from './project-permission.service';

@Component({
    selector: 'jhi-project-permission-delete-dialog',
    templateUrl: './project-permission-delete-dialog.component.html'
})
export class ProjectPermissionDeleteDialogComponent {
    projectPermission: IProjectPermission;

    constructor(
        private projectPermissionService: ProjectPermissionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectPermissionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'projectPermissionListModification',
                content: 'Deleted an projectPermission'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-permission-delete-popup',
    template: ''
})
export class ProjectPermissionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectPermission }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ProjectPermissionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.projectPermission = projectPermission;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}

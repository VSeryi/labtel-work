import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProjectPermission } from 'app/shared/model/project-permission.model';
import { ProjectPermissionService } from './project-permission.service';
import { IUser, UserService } from 'app/core';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
    selector: 'jhi-project-permission-update',
    templateUrl: './project-permission-update.component.html'
})
export class ProjectPermissionUpdateComponent implements OnInit {
    projectPermission: IProjectPermission;
    isSaving: boolean;

    users: IUser[];

    projects: IProject[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private projectPermissionService: ProjectPermissionService,
        private userService: UserService,
        private projectService: ProjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ projectPermission }) => {
            this.projectPermission = projectPermission;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectService.query({ filter: 'projectpermission-is-null' }).subscribe(
            (res: HttpResponse<IProject[]>) => {
                if (!this.projectPermission.projectId) {
                    this.projects = res.body;
                } else {
                    this.projectService.find(this.projectPermission.projectId).subscribe(
                        (subRes: HttpResponse<IProject>) => {
                            this.projects = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.projectPermission.id !== undefined) {
            this.subscribeToSaveResponse(this.projectPermissionService.update(this.projectPermission));
        } else {
            this.subscribeToSaveResponse(this.projectPermissionService.create(this.projectPermission));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProjectPermission>>) {
        result.subscribe((res: HttpResponse<IProjectPermission>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ProjectPermission } from 'app/shared/model/project-permission.model';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { ProjectPermissionService } from '../project-permission/project-permission.service';
import { UserService } from 'app/core/user/user.service';

@Component({
    selector: 'jhi-project-update',
    templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit {
    project: IProject;
    users: any;
    usersFiltered: any;
    permissions: any = [];
    permissionsToDelete: any = [];
    isSaving: boolean;
    newUser: any;
    newPermission: any;

    constructor(private projectService: ProjectService, private activatedRoute: ActivatedRoute,
        private userService: UserService, private projectPermissionService: ProjectPermissionService) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
            const promises = [this.userService.query().toPromise()];
            if (this.project.id) {
                promises.push(this.projectPermissionService.findByProject({projectId: this.project.id}).toPromise());
            }
            Promise.all(promises)
                .then(response => {
                    this.users = response[0].body;
                    this.usersFiltered = this.users;
                    if (response.length > 1) {
                        this.permissions = response[1].body;
                        this.filterUsers();
                    }
                });
        });

    }

    addPermission() {
        const permission = new ProjectPermission();
        permission.userId = this.newUser.id;
        permission.userLogin = this.newUser.login;
        permission.permission = this.newPermission;
        permission.projectId = this.project.id;
        this.permissions.push(permission);
        this.filterUsers();
    }

    deletePermission(permission) {
        this.permissionsToDelete.push(permission);
        this.permissions.pop(permission);
        this.filterUsers();
    }

    isNew(user, index, array) {
        for (const permission of this.permissions) {
            if (permission.userId === user.id) {
                return false;
            }
        }
        return true;
     }

    filterUsers() {
        this.usersFiltered = this.users.filter(this.isNew, this);
    }
    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;

        const promises = [];

        for (const element of this.permissionsToDelete) {
            if (element.id !== undefined) {
                promises.push(this.projectPermissionService.delete(element.id).toPromise());
            }
        }

        for (const element of this.permissions) {
            if (element.id === undefined) {
                promises.push(this.projectPermissionService.create(element).toPromise());
            }
        }

        Promise.all(promises).then(respond => {
            console.log(respond);
            if (this.project.id !== undefined) {
                this.subscribeToSaveResponse(this.projectService.update(this.project));
            } else {
                this.subscribeToSaveResponse(this.projectService.create(this.project));
            }
        });


    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
        result.subscribe((res: HttpResponse<IProject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

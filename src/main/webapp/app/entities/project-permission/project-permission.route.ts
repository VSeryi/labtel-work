import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ProjectPermission } from 'app/shared/model/project-permission.model';
import { ProjectPermissionService } from './project-permission.service';
import { ProjectPermissionComponent } from './project-permission.component';
import { ProjectPermissionDetailComponent } from './project-permission-detail.component';
import { ProjectPermissionUpdateComponent } from './project-permission-update.component';
import { ProjectPermissionDeletePopupComponent } from './project-permission-delete-dialog.component';
import { IProjectPermission } from 'app/shared/model/project-permission.model';

@Injectable({ providedIn: 'root' })
export class ProjectPermissionResolve implements Resolve<IProjectPermission> {
    constructor(private service: ProjectPermissionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ProjectPermission> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ProjectPermission>) => response.ok),
                map((projectPermission: HttpResponse<ProjectPermission>) => projectPermission.body)
            );
        }
        return of(new ProjectPermission());
    }
}

export const projectPermissionRoute: Routes = [
    {
        path: 'project-permission',
        component: ProjectPermissionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'labtelApp.projectPermission.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-permission/:id/view',
        component: ProjectPermissionDetailComponent,
        resolve: {
            projectPermission: ProjectPermissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectPermission.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-permission/new',
        component: ProjectPermissionUpdateComponent,
        resolve: {
            projectPermission: ProjectPermissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectPermission.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-permission/:id/edit',
        component: ProjectPermissionUpdateComponent,
        resolve: {
            projectPermission: ProjectPermissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectPermission.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectPermissionPopupRoute: Routes = [
    {
        path: 'project-permission/:id/delete',
        component: ProjectPermissionDeletePopupComponent,
        resolve: {
            projectPermission: ProjectPermissionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectPermission.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

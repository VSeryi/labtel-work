import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ProjectItem } from 'app/shared/model/project-item.model';
import { ProjectItemService } from './project-item.service';
import { ProjectItemComponent } from './project-item.component';
import { ProjectItemDetailComponent } from './project-item-detail.component';
import { ProjectItemUpdateComponent } from './project-item-update.component';
import { ProjectItemDeletePopupComponent } from './project-item-delete-dialog.component';
import { IProjectItem } from 'app/shared/model/project-item.model';

@Injectable({ providedIn: 'root' })
export class ProjectItemResolve implements Resolve<IProjectItem> {
    constructor(private service: ProjectItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ProjectItem> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ProjectItem>) => response.ok),
                map((projectItem: HttpResponse<ProjectItem>) => projectItem.body)
            );
        }
        return of(new ProjectItem());
    }
}

export const projectItemRoute: Routes = [
    {
        path: 'project-item',
        component: ProjectItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'labtelApp.projectItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-item/:id/view',
        component: ProjectItemDetailComponent,
        resolve: {
            projectItem: ProjectItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-item/new',
        component: ProjectItemUpdateComponent,
        resolve: {
            projectItem: ProjectItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-item/:id/edit',
        component: ProjectItemUpdateComponent,
        resolve: {
            projectItem: ProjectItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectItemPopupRoute: Routes = [
    {
        path: 'project-item/:id/delete',
        component: ProjectItemDeletePopupComponent,
        resolve: {
            projectItem: ProjectItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'labtelApp.projectItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

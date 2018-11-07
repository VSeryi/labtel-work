import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectPermission } from 'app/shared/model/project-permission.model';

@Component({
    selector: 'jhi-project-permission-detail',
    templateUrl: './project-permission-detail.component.html'
})
export class ProjectPermissionDetailComponent implements OnInit {
    projectPermission: IProjectPermission;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectPermission }) => {
            this.projectPermission = projectPermission;
        });
    }

    previousState() {
        window.history.back();
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectItem } from 'app/shared/model/project-item.model';

@Component({
    selector: 'jhi-project-item-detail',
    templateUrl: './project-item-detail.component.html'
})
export class ProjectItemDetailComponent implements OnInit {
    projectItem: IProjectItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectItem }) => {
            this.projectItem = projectItem;
        });
    }

    previousState() {
        window.history.back();
    }
}

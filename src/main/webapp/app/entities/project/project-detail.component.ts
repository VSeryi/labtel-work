import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

import { ProjectService } from './project.service';
import { IProject } from 'app/shared/model/project.model';

@Component({
    selector: 'jhi-project-detail',
    templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit, OnDestroy {
    project: IProject;
    eventSubscriber: Subscription;

    constructor(private activatedRoute: ActivatedRoute,
        private projectService: ProjectService,
        private eventManager: JhiEventManager) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
        });
        this.registerChangeInProjects();
    }

    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe('projectItemListModification', response => this.updateProject());
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    updateProject() {
        this.projectService.find(this.project.id).toPromise().then(
            response => {
                this.project = response.body;
            }
        );
    }
}

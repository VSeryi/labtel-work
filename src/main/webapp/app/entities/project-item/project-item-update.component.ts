import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProjectItem } from 'app/shared/model/project-item.model';
import { ProjectItemService } from './project-item.service';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
    selector: 'jhi-project-item-update',
    templateUrl: './project-item-update.component.html'
})
export class ProjectItemUpdateComponent implements OnInit {
    projectItem: IProjectItem;
    isSaving: boolean;

    items: IItem[];

    projects: IProject[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private projectItemService: ProjectItemService,
        private itemService: ItemService,
        private projectService: ProjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ projectItem }) => {
            this.projectItem = projectItem;
        });
        this.itemService.query().subscribe(
            (res: HttpResponse<IItem[]>) => {
                this.items = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.projectItem.id !== undefined) {
            this.subscribeToSaveResponse(this.projectItemService.update(this.projectItem));
        } else {
            this.subscribeToSaveResponse(this.projectItemService.create(this.projectItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProjectItem>>) {
        result.subscribe((res: HttpResponse<IProjectItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackItemById(index: number, item: IItem) {
        return item.id;
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }
}

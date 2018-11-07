import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItem } from 'app/shared/model/item.model';
import { loadInternal } from '@angular/core/src/render3/util';

@Component({
    selector: 'jhi-item-detail',
    templateUrl: './item-detail.component.html'
})
export class ItemDetailComponent implements OnInit {
    item: IItem;

    projectId: any;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ item }) => {
            this.item = item;
        });

        this.activatedRoute.params.subscribe(routeParams => {
            this.projectId = routeParams.projectId;
        });

    }

    previousState() {
        window.history.back();
    }
}

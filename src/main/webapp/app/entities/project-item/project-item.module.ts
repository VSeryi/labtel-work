import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LabtelSharedModule } from 'app/shared';
import {
    ProjectItemComponent,
    ProjectItemDetailComponent,
    ProjectItemUpdateComponent,
    ProjectItemDeletePopupComponent,
    ProjectItemDeleteDialogComponent,
    projectItemRoute,
    projectItemPopupRoute
} from './';

const ENTITY_STATES = [...projectItemRoute, ...projectItemPopupRoute];

@NgModule({
    imports: [LabtelSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProjectItemComponent,
        ProjectItemDetailComponent,
        ProjectItemUpdateComponent,
        ProjectItemDeleteDialogComponent,
        ProjectItemDeletePopupComponent
    ],
    entryComponents: [ProjectItemComponent, ProjectItemUpdateComponent, ProjectItemDeleteDialogComponent, ProjectItemDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LabtelProjectItemModule {}

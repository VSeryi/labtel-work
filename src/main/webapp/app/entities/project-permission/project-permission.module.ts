import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LabtelSharedModule } from 'app/shared';
import { LabtelAdminModule } from 'app/admin/admin.module';
import {
    ProjectPermissionComponent,
    ProjectPermissionDetailComponent,
    ProjectPermissionUpdateComponent,
    ProjectPermissionDeletePopupComponent,
    ProjectPermissionDeleteDialogComponent,
    projectPermissionRoute,
    projectPermissionPopupRoute
} from './';

const ENTITY_STATES = [...projectPermissionRoute, ...projectPermissionPopupRoute];

@NgModule({
    imports: [LabtelSharedModule, LabtelAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProjectPermissionComponent,
        ProjectPermissionDetailComponent,
        ProjectPermissionUpdateComponent,
        ProjectPermissionDeleteDialogComponent,
        ProjectPermissionDeletePopupComponent
    ],
    entryComponents: [
        ProjectPermissionComponent,
        ProjectPermissionUpdateComponent,
        ProjectPermissionDeleteDialogComponent,
        ProjectPermissionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LabtelProjectPermissionModule {}

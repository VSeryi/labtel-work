import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LabtelCategoryModule } from './category/category.module';
import { LabtelItemModule } from './item/item.module';
import { LabtelProjectItemModule } from './project-item/project-item.module';
import { LabtelProjectModule } from './project/project.module';
import { LabtelProjectPermissionModule } from './project-permission/project-permission.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        LabtelCategoryModule,
        LabtelItemModule,
        LabtelProjectItemModule,
        LabtelProjectModule,
        LabtelProjectPermissionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LabtelEntityModule {}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { LabtelSharedLibsModule, LabtelSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, HasWritePermissionDirective } from './';

@NgModule({
    imports: [LabtelSharedLibsModule, LabtelSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, HasWritePermissionDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [LabtelSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, HasWritePermissionDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LabtelSharedModule {}

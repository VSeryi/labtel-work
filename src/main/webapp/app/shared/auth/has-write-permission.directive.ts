import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from 'app/core/auth/principal.service';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the authorities passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *jhiHasAnyAuthority="'ROLE_ADMIN'">...</some-element>
 *
 *     <some-element *jhiHasAnyAuthority="['ROLE_ADMIN', 'ROLE_USER']">...</some-element>
 * ```
 */
@Directive({
    selector: '[jhiHasWritePermission]'
})
export class HasWritePermissionDirective {
    private projectId: number;

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

    @Input()
    set jhiHasWritePermission(value: number ) {
        this.projectId = value;
        this.updateView();
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe(identity => this.updateView());
    }

    private updateView(): void {
        this.principal.hasWritePermission(this.projectId).then(result => {
            this.viewContainerRef.clear();
            if (result) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
            }
        });
    }
}

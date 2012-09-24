package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;

import com.smartgwt.client.types.Visibility;

public class OrganisationSchemeMainFormLayout extends LifeCycleMainFormLayout {

    public OrganisationSchemeMainFormLayout() {
        common();
    }

    public OrganisationSchemeMainFormLayout(boolean canEdit) {
        common();
    }

    private void common() {
        announce.setVisibility(OrganisationsClientSecurityUtils.canAnnounceOrganisationScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    @Override
    protected void showSendToProductionValidation() {
        if (OrganisationsClientSecurityUtils.canSendOrganisationSchemeToProductionValidation()) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (OrganisationsClientSecurityUtils.canSendOrganisationSchemeToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (OrganisationsClientSecurityUtils.canRejectOrganisationSchemeValidation(status)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (OrganisationsClientSecurityUtils.canPublishOrganisationSchemeInternally()) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (OrganisationsClientSecurityUtils.canPublishOrganisationSchemeExternally()) {
            publishExternally.show();
        }
    }

    @Override
    protected void showVersioningButton() {
        if (OrganisationsClientSecurityUtils.canVersioningOrganisationScheme()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (OrganisationsClientSecurityUtils.canCancelOrganisationSchemeValidity()) {
            cancelValidity.show();
        }
    }

}

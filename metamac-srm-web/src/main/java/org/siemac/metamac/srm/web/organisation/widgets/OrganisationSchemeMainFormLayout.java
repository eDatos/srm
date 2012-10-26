package org.siemac.metamac.srm.web.organisation.widgets;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.smartgwt.client.types.Visibility;

public class OrganisationSchemeMainFormLayout extends LifeCycleMainFormLayout {

    private OrganisationSchemeTypeEnum organisationSchemeType;

    public OrganisationSchemeMainFormLayout() {
        common();
    }

    public OrganisationSchemeMainFormLayout(boolean canEdit) {
        common();
    }

    private void common() {
        announce.setVisibility(OrganisationsClientSecurityUtils.canAnnounceOrganisationScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    public void updatePublishSection(ProcStatusEnum status, Date validTo, OrganisationSchemeTypeEnum type) {
        this.organisationSchemeType = type;
        super.updatePublishSection(status, validTo);
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
        // Agency schemes cannot be versioned
        if (!OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeType) && OrganisationsClientSecurityUtils.canVersioningOrganisationScheme()) {
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

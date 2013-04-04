package org.siemac.metamac.srm.web.organisation.widgets;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.organisation.utils.CommonUtils;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationSchemeMainFormLayout extends LifeCycleMainFormLayout {

    private OrganisationSchemeTypeEnum organisationSchemeType;

    public OrganisationSchemeMainFormLayout() {
    }

    public OrganisationSchemeMainFormLayout(boolean canEdit) {
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
        // Agency schemes, data consumer schemes and data provider schemes can not be version
        if (!CommonUtils.isDataConsumerScheme(organisationSchemeType) && !CommonUtils.isDataProviderScheme(organisationSchemeType) & !CommonUtils.isAgencyScheme(organisationSchemeType)) {
            if (OrganisationsClientSecurityUtils.canVersioningOrganisationScheme()) {
                versioning.show();
            }
        }
    }

    @Override
    protected void showCancelValidityButton() {
        // Agency schemes, data consumer schemes and data provider schemes can not be canceled
        if (!CommonUtils.isDataConsumerScheme(organisationSchemeType) && !CommonUtils.isDataProviderScheme(organisationSchemeType) & !CommonUtils.isAgencyScheme(organisationSchemeType)) {
            if (OrganisationsClientSecurityUtils.canCancelOrganisationSchemeValidity()) {
                cancelValidity.show();
            }
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (OrganisationsClientSecurityUtils.canAnnounceOrganisationScheme()) {
    // announce.show();
    // }
    // }
}

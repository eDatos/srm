package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationSchemeMainFormLayout extends LifeCycleMainFormLayout {

    private OrganisationSchemeTypeEnum organisationSchemeType;

    public OrganisationSchemeMainFormLayout() {
    }

    public OrganisationSchemeMainFormLayout(boolean canEdit) {
    }

    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        super.updatePublishSection(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto);
        this.organisationSchemeType = organisationSchemeMetamacDto.getType();
        setCanEdit();
        setCanDelete();
    }

    private void setCanEdit() {
        super.setCanEdit(OrganisationsClientSecurityUtils.canUpdateOrganisationScheme(procStatus, organisationSchemeType));
    }

    private void setCanDelete() {
        boolean canDelete = false;
        canDelete = OrganisationsClientSecurityUtils.canDeleteOrganisationScheme(procStatus);
        super.setCanDelete(canDelete);
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
        if (OrganisationsClientSecurityUtils.canRejectOrganisationSchemeValidation(procStatus)) {
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
    protected void showCreateTemporalVersionButton() {
        if (OrganisationsClientSecurityUtils.canCreateOrganisationSchemeTemporalVersion()) {
            createTemporalVersion.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (OrganisationsClientSecurityUtils.canCancelOrganisationSchemeValidity(urn, maintainer, versionLogic, organisationSchemeType, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showConsolidateVersionButton() {
        if (OrganisationsClientSecurityUtils.canVersioningOrganisationScheme(urn, maintainer, versionLogic, organisationSchemeType)) {
            consolidateVersion.show();
        }
    }

    @Override
    protected void showExportButton() {
        if (OrganisationsClientSecurityUtils.canExportOrganisationScheme(versionLogic)) {
            export.show();
        }
    }

    @Override
    protected void showCopyButton() {
        if (OrganisationsClientSecurityUtils.canCopyOrganisationScheme(maintainer)) {
            copy.show();
        }
    }

    @Override
    protected void showCopyKeepingMaintainerButton() {
        if (OrganisationsClientSecurityUtils.canCopyOrganisationSchemeKeepingMaintainer(maintainer)) {
            copyKeepingMaintainer.show();
        }
    }

    @Override
    protected void showLifeCycleReSendStreamMessage() {
        if (OrganisationsClientSecurityUtils.canResendOrganisationSchemeStreamMessage()) {
            lifeCycleReSendStreamMessage.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (OrganisationsClientSecurityUtils.canAnnounceOrganisationScheme()) {
    // announce.show();
    // }
    // }
}

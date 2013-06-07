package org.siemac.metamac.srm.web.organisation.widgets;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.TasksClientSecurityUtils;
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
        boolean canEdit = false;
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            canEdit = OrganisationsClientSecurityUtils.canCreateOrganisationSchemeTemporalVersion();
        } else {
            canEdit = OrganisationsClientSecurityUtils.canUpdateOrganisationScheme(procStatus, organisationSchemeType);
        }
        super.setCanEdit(canEdit);
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
    protected void showVersioningButton() {
        if (canVersionOrganisationScheme()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (OrganisationsClientSecurityUtils.canCancelOrganisationSchemeValidity(urn, maintainer, versionLogic, organisationSchemeType, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showVersionSdmxResourceButton() {
        if (canVersionOrganisationScheme() && VersionUtil.isTemporalVersion(versionLogic)) {
            versionSdmxResource.show();
        }
    }

    @Override
    protected void showExportButton() {
        if (TasksClientSecurityUtils.canExportStructure(versionLogic)) {
            export.show();
        }
    }

    @Override
    protected void showCopyButton() {
        if (OrganisationsClientSecurityUtils.canCopyOrganisationScheme(maintainer)) {
            copy.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (OrganisationsClientSecurityUtils.canAnnounceOrganisationScheme()) {
    // announce.show();
    // }
    // }

    private boolean canVersionOrganisationScheme() {
        return OrganisationsClientSecurityUtils.canVersioningOrganisationScheme(urn, maintainer, organisationSchemeType);
    }
}

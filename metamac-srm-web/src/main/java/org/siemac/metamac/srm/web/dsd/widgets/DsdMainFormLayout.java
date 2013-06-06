package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.utils.TasksClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;

public class DsdMainFormLayout extends LifeCycleMainFormLayout {

    private String operationCode;

    public DsdMainFormLayout() {
    }

    public void setDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        super.updatePublishSection(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), dataStructureDefinitionMetamacDto);
        this.operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
        setCanEdit();
        setCanDelete();
    }

    private void setCanEdit() {
        boolean canEdit = false;
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            canEdit = DsdClientSecurityUtils.canCreateDsdTemporalVersion(operationCode);
        } else {
            canEdit = DsdClientSecurityUtils.canUpdateDsd(procStatus, operationCode);
        }
        super.setCanEdit(canEdit);
    }

    private void setCanDelete() {
        boolean canDelete = false;
        canDelete = DsdClientSecurityUtils.canDeleteDsd(procStatus, operationCode);
        super.setCanDelete(canDelete);
    }

    @Override
    protected void showSendToProductionValidation() {
        if (DsdClientSecurityUtils.canSendDsdToProductionValidation(operationCode)) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (DsdClientSecurityUtils.canSendDsdToDiffusionValidation(operationCode)) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (DsdClientSecurityUtils.canRejectDsdValidation(procStatus, operationCode)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (DsdClientSecurityUtils.canPublishDsdInternally(operationCode)) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (DsdClientSecurityUtils.canPublishDsdExternally(operationCode)) {
            publishExternally.show();
        }
    }

    @Override
    protected void showVersioningButton() {
        if (canVersionDsd()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (DsdClientSecurityUtils.canCancelDsdValidity(urn, operationCode, maintainer, versionLogic)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showVersionSdmxResourceButton() {
        if (canVersionDsd() && VersionUtil.isTemporalVersion(versionLogic)) {
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
        if (DsdClientSecurityUtils.canCopyDsd(maintainer)) {
            copy.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (DsdClientSecurityUtils.canAnnounceDsd(operationCode)) {
    // announce.show();
    // }
    // }

    private boolean canVersionDsd() {
        // Resources from other maintainers can not be version
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer) && DsdClientSecurityUtils.canVersioningDsd(operationCode);
    }
}

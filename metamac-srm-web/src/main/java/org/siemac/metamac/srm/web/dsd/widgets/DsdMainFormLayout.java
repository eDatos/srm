package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
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
        super.setCanEdit(DsdClientSecurityUtils.canUpdateDsd(procStatus, operationCode));
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
    protected void showCreateTemporalVersionButton() {
        if (DsdClientSecurityUtils.canCreateDsdTemporalVersion(operationCode)) {
            createTemporalVersion.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (DsdClientSecurityUtils.canCancelDsdValidity(urn, operationCode, maintainer, versionLogic, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showConsolidateVersionButton() {
        if (DsdClientSecurityUtils.canVersioningDsd(operationCode, maintainer, versionLogic)) {
            consolidateVersion.show();
        }
    }

    @Override
    protected void showExportButton() {
        if (DsdClientSecurityUtils.canExportDsd(versionLogic)) {
            export.show();
        }
    }

    @Override
    protected void showCopyButton() {
        if (DsdClientSecurityUtils.canCopyDsd(maintainer)) {
            copy.show();
        }
    }

    @Override
    protected void showCopyKeepingMaintainerButton() {
        if (DsdClientSecurityUtils.canCopyDsdKeepingMaintainer(maintainer)) {
            copyKeepingMaintainer.show();
        }
    }

    @Override
    protected void showLifeCycleReSendStreamMessage() {
        if (DsdClientSecurityUtils.canResendDsdStreamMessage(operationCode)) {
            lifeCycleReSendStreamMessage.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (DsdClientSecurityUtils.canAnnounceDsd(operationCode)) {
    // announce.show();
    // }
    // }
}

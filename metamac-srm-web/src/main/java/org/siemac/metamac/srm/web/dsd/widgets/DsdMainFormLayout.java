package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;

public class DsdMainFormLayout extends LifeCycleMainFormLayout {

    private String operationCode;

    public DsdMainFormLayout() {
    }

    public DsdMainFormLayout(boolean canEdit) {
    }

    public void updatePublishSection(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        super.updatePublishSection(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(), dataStructureDefinitionMetamacDto.getValidTo());
        this.operationCode = CommonUtils.getStatisticalOperationCodeFromDsd(dataStructureDefinitionMetamacDto);
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
        if (DsdClientSecurityUtils.canRejectDsdValidation(status, operationCode)) {
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
        if (DsdClientSecurityUtils.canVersioningDsd(operationCode)) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (DsdClientSecurityUtils.canCancelDsdValidity(operationCode)) {
            cancelValidity.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (DsdClientSecurityUtils.canAnnounceDsd(operationCode)) {
    // announce.show();
    // }
    // }
}

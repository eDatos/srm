package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.TasksClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CodelistMainFormLayout extends LifeCycleMainFormLayout {

    private Boolean              isTaskInBackground;

    private MainFormLayoutButton addCodelistToFamilyButton;

    public CodelistMainFormLayout() {
        // Add button to add the codelist to a family (when is published)
        addCodelistToFamilyButton = new MainFormLayoutButton(getConstants().codelistModifyCodelistFamily(), GlobalResources.RESOURCE.editListGrid().getURL());
        toolStrip.addButton(addCodelistToFamilyButton, 1);
    }

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        super.updatePublishSection(codelistMetamacDto.getLifeCycle().getProcStatus(), codelistMetamacDto);
        this.isTaskInBackground = codelistMetamacDto.getIsTaskInBackground();
        setCanEdit();
        setCanDelete();
    }

    private void setCanEdit() {
        super.setCanEdit(CodesClientSecurityUtils.canUpdateCodelist(procStatus, isTaskInBackground));
    }

    private void setCanDelete() {
        boolean canDelete = false;
        canDelete = CodesClientSecurityUtils.canDeleteCodelist(procStatus, isTaskInBackground);
        super.setCanDelete(canDelete);
    }

    @Override
    protected void hideAllLifeCycleButtons() {
        super.hideAllLifeCycleButtons();
        addCodelistToFamilyButton.hide();
    }

    @Override
    protected void updateVisibility() {
        super.updateVisibility();
        if (CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            showAddCodelistToFamilyButton();
        }
    }

    protected void showAddCodelistToFamilyButton() {
        if (CodesClientSecurityUtils.canAddCodelistToCodelistFamily(isTaskInBackground)) {
            addCodelistToFamilyButton.show();
        }
    }

    @Override
    protected void showSendToProductionValidation() {
        if (CodesClientSecurityUtils.canSendCodelistToProductionValidation(isTaskInBackground)) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (CodesClientSecurityUtils.canSendCodelistToDiffusionValidation(isTaskInBackground)) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (CodesClientSecurityUtils.canRejectCodelistValidation(procStatus, isTaskInBackground)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (CodesClientSecurityUtils.canPublishCodelistInternally(isTaskInBackground)) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (CodesClientSecurityUtils.canPublishCodelistExternally(isTaskInBackground)) {
            publishExternally.show();
        }
    }

    @Override
    protected void showCreateTemporalVersionButton() {
        if (CodesClientSecurityUtils.canCreateCodelistTemporalVersion(isTaskInBackground)) {
            createTemporalVersion.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (CodesClientSecurityUtils.canCancelCodelistValidity(urn, isTaskInBackground, maintainer, versionLogic, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showConsolidateVersionButton() {
        if (CodesClientSecurityUtils.canVersioningCodelist(maintainer, versionLogic, isTaskInBackground)) {
            consolidateVersion.show();
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
        if (CodesClientSecurityUtils.canCopyCodelist(maintainer, isTaskInBackground)) {
            copy.show();
        }
    }

    @Override
    protected void showCopyKeepingMaintainerButton() {
        if (CodesClientSecurityUtils.canCopyCodelistKeepingMaintainer(maintainer, isTaskInBackground)) {
            copyKeepingMaintainer.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (CodesClientSecurityUtils.canAnnounceCodelist()) {
    // announce.show();
    // }
    // }

    public HasClickHandlers getAddCodelistToFamily() {
        return addCodelistToFamilyButton;
    }
}

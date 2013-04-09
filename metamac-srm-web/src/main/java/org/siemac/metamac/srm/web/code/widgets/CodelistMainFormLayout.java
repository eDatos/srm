package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class CodelistMainFormLayout extends LifeCycleMainFormLayout {

    private MainFormLayoutButton addCodelistToFamilyButton;

    public CodelistMainFormLayout() {
        // Add button to add the codelist to a family (when is published)
        addCodelistToFamilyButton = new MainFormLayoutButton(getConstants().codelistModifyCodelistFamily(), GlobalResources.RESOURCE.editListGrid().getURL());
        toolStrip.addButton(addCodelistToFamilyButton, 1);
    }

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        super.updatePublishSection(codelistMetamacDto.getLifeCycle().getProcStatus(), codelistMetamacDto);
        setCanEdit();
    }

    private void setCanEdit() {
        boolean canEdit = false;
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(status)) {
            canEdit = CodesClientSecurityUtils.canCreateCodelistTemporalVersion();
        } else {
            canEdit = CodesClientSecurityUtils.canUpdateCodelist(status);
        }
        super.setCanEdit(canEdit);
    }

    @Override
    protected void hideAllLifeCycleButtons() {
        super.hideAllLifeCycleButtons();
        addCodelistToFamilyButton.hide();
    }

    @Override
    protected void updateVisibility() {
        super.updateVisibility();
        if (CommonUtils.isItemSchemePublished(status)) {
            showAddCodelistToFamilyButton();
        }
    }

    protected void showAddCodelistToFamilyButton() {
        if (CodesClientSecurityUtils.canAddCodelistToCodelistFamily()) {
            addCodelistToFamilyButton.show();
        }
    }

    @Override
    protected void showSendToProductionValidation() {
        if (CodesClientSecurityUtils.canSendCodelistToProductionValidation()) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (CodesClientSecurityUtils.canSendCodelistToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (CodesClientSecurityUtils.canRejectCodelistValidation(status)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (CodesClientSecurityUtils.canPublishCodelistInternally()) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (CodesClientSecurityUtils.canPublishCodelistExternally()) {
            publishExternally.show();
        }
    }

    @Override
    protected void showVersioningButton() {
        if (canVersionCodelist()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (CodesClientSecurityUtils.canCancelCodelistValidity()) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showVersionSdmxResourceButton() {
        if (canVersionCodelist() && VersionUtil.isTemporalVersion(versionLogic)) {
            versionSdmxResource.show();
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

    private boolean canVersionCodelist() {
        // Resources from other maintainers can not be version
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer) && CodesClientSecurityUtils.canVersioningCodelist();
    }
}

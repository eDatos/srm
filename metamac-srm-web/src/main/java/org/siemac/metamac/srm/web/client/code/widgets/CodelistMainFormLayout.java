package org.siemac.metamac.srm.web.client.code.widgets;

import org.siemac.metamac.srm.web.client.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;

import com.smartgwt.client.types.Visibility;

public class CodelistMainFormLayout extends LifeCycleMainFormLayout {

    public CodelistMainFormLayout() {
        common();
    }

    public CodelistMainFormLayout(boolean canEdit) {
        common();
    }

    private void common() {
        announce.setVisibility(CodesClientSecurityUtils.canAnnounceCodelist() ? Visibility.VISIBLE : Visibility.HIDDEN);
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
        if (CodesClientSecurityUtils.canVersioningCodelist()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (CodesClientSecurityUtils.canCancelCodelistValidity()) {
            cancelValidity.show();
        }
    }
}

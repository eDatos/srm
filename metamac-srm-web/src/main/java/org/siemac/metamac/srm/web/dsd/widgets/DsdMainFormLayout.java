package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;

import com.smartgwt.client.types.Visibility;

public class DsdMainFormLayout extends LifeCycleMainFormLayout {

    public DsdMainFormLayout() {
        common();
    }

    public DsdMainFormLayout(boolean canEdit) {
        common();
    }

    private void common() {
        announce.setVisibility(DsdClientSecurityUtils.canAnnounceDsd() ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    @Override
    protected void showSendToProductionValidation() {
        if (DsdClientSecurityUtils.canSendDsdToProductionValidation()) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (DsdClientSecurityUtils.canSendDsdToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (DsdClientSecurityUtils.canRejectDsdValidation(status)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (DsdClientSecurityUtils.canPublishDsdInternally()) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (DsdClientSecurityUtils.canPublishDsdExternally()) {
            publishExternally.show();
        }
    }

    @Override
    protected void showVersioningButton() {
        if (DsdClientSecurityUtils.canVersioningDsd()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (DsdClientSecurityUtils.canCancelDsdValidity()) {
            cancelValidity.show();
        }
    }

}

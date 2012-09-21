package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.AnnounceToolStripButton;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdMainFormLayout extends InternationalMainFormLayout {

    private MainFormLayoutButton            productionValidation;
    private MainFormLayoutButton            diffusionValidation;
    private MainFormLayoutButton            rejectValidation;
    private MainFormLayoutButton            publishInternally;
    private MainFormLayoutButton            publishExternally;
    private MainFormLayoutButton            versioning;
    private MainFormLayoutButton            cancelValidity;
    private AnnounceToolStripButton         announce;

    private ProcStatusEnum status;
    private Date                            validTo;

    public DsdMainFormLayout() {
        common();
    }

    public DsdMainFormLayout(boolean canEdit) {
        common();
    }

    private void common() {
        // Remove handler from edit button
        editHandlerRegistration.removeHandler();

        productionValidation = new MainFormLayoutButton(getConstants().lifeCycleSendToProductionValidation(), GlobalResources.RESOURCE.validateProduction().getURL());
        diffusionValidation = new MainFormLayoutButton(getConstants().lifeCycleSendToDiffusionValidation(), GlobalResources.RESOURCE.validateDiffusion().getURL());
        publishInternally = new MainFormLayoutButton(getConstants().lifeCyclePublishInternally(), GlobalResources.RESOURCE.internalPublish().getURL());
        publishExternally = new MainFormLayoutButton(getConstants().lifeCyclePublishExternally(), GlobalResources.RESOURCE.externalPublish().getURL());
        rejectValidation = new MainFormLayoutButton(getConstants().lifeCycleRejectValidation(), GlobalResources.RESOURCE.reject().getURL());
        versioning = new MainFormLayoutButton(getConstants().lifeCycleVersioning(), GlobalResources.RESOURCE.version().getURL());
        cancelValidity = new MainFormLayoutButton(getConstants().lifeCycleCancelValidity(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.disable().getURL());
        announce = new AnnounceToolStripButton(MetamacWebCommon.getConstants().announce(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.announce().getURL());
        announce.setVisibility(DsdClientSecurityUtils.canAnnounceDsd() ? Visibility.VISIBLE : Visibility.HIDDEN);

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publishInternally);
        toolStrip.addButton(publishExternally);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
        toolStrip.addButton(cancelValidity);
        toolStrip.addButton(announce);
    }

    public void updatePublishSection(ProcStatusEnum status, Date validTo) {
        this.status = status;
        this.validTo = validTo;
    }

    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (ProcStatusEnum.DRAFT.equals(status)) {
            showSendToProductionValidation();
        } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(status)) {
            showSendToProductionValidation();
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            showSendToDiffusionValidation();
            showRejectValidationButton();
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            showPublishInternallyButton();
            showRejectValidationButton();
        } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(status)) {
            showPublishExternallyButton();
            showVersioningButton();
        } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
            showVersioningButton();
            // Only cancel scheme validity if it has not been canceled previously
            if (validTo == null) {
                showCancelValidityButton();
            }
        }
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        hideAllPublishButtons();
    }

    public HasClickHandlers getSendToProductionValidation() {
        return productionValidation;
    }

    public HasClickHandlers getSendToDiffusionValidation() {
        return diffusionValidation;
    }

    public HasClickHandlers getRejectValidation() {
        return rejectValidation;
    }

    public HasClickHandlers getPublishInternally() {
        return publishInternally;
    }

    public HasClickHandlers getPublishExternally() {
        return publishExternally;
    }

    public HasClickHandlers getVersioning() {
        return versioning;
    }

    public HasClickHandlers getCancelValidity() {
        return cancelValidity;
    }

    public HasClickHandlers getAnnounce() {
        return announce;
    }

    private void hideAllPublishButtons() {
        productionValidation.hide();
        diffusionValidation.hide();
        rejectValidation.hide();
        publishInternally.hide();
        publishExternally.hide();
        versioning.hide();
        cancelValidity.hide();
    }

    private void showSendToProductionValidation() {
        if (DsdClientSecurityUtils.canSendDsdToProductionValidation()) {
            productionValidation.show();
        }
    }

    private void showSendToDiffusionValidation() {
        if (DsdClientSecurityUtils.canSendDsdToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    private void showRejectValidationButton() {
        if (DsdClientSecurityUtils.canRejectDsdValidation(status)) {
            rejectValidation.show();
        }
    }

    private void showPublishInternallyButton() {
        if (DsdClientSecurityUtils.canPublishDsdInternally()) {
            publishInternally.show();
        }
    }

    private void showPublishExternallyButton() {
        if (DsdClientSecurityUtils.canPublishDsdExternally()) {
            publishExternally.show();
        }
    }

    private void showVersioningButton() {
        if (DsdClientSecurityUtils.canVersioningDsd()) {
            versioning.show();
        }
    }

    private void showCancelValidityButton() {
        if (DsdClientSecurityUtils.canCancelDsdValidity()) {
            cancelValidity.show();
        }
    }

}

package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.concept.utils.ConceptClientSecurityUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.AnnounceToolStripButton;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class ConceptSchemeMainFormLayout extends InternationalMainFormLayout {

    private MainFormLayoutButton               productionValidation;
    private MainFormLayoutButton               diffusionValidation;
    private MainFormLayoutButton               rejectValidation;
    private MainFormLayoutButton               publishInternally;
    private MainFormLayoutButton               publishExternally;
    private MainFormLayoutButton               versioning;
    private MainFormLayoutButton               cancelValidity;
    private AnnounceToolStripButton            announce;

    private ItemSchemeMetamacProcStatusEnum status;

    public ConceptSchemeMainFormLayout() {
        super();
        common();
    }

    public ConceptSchemeMainFormLayout(boolean canEdit) {
        super(canEdit);
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
        announce.setVisibility(ConceptClientSecurityUtils.canAnnounceConceptScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publishInternally);
        toolStrip.addButton(publishExternally);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
        toolStrip.addButton(cancelValidity);
        toolStrip.addButton(announce);
    }

    public void updatePublishSection(ItemSchemeMetamacProcStatusEnum status) {
        this.status = status;
    }

    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (ItemSchemeMetamacProcStatusEnum.DRAFT.equals(status)) {
            showSendToProductionValidation();
        } else if (ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED.equals(status)) {
            showSendToProductionValidation();
        } else if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            showSendToDiffusionValidation();
            showRejectValidationButton();
        } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            showPublishInternallyButton();
            showRejectValidationButton();
        } else if (ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.equals(status)) {
            showPublishExternallyButton();
            showVersioningButton();
        } else if (ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
            showVersioningButton();
            showCancelValidityButton();
        } else if (ItemSchemeMetamacProcStatusEnum.EXTERNAL_PUBLICATION_FAILED.equals(status)) {
            showPublishExternallyButton();
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
        if (ConceptClientSecurityUtils.canSendConceptSchemeToProductionValidation()) {
            productionValidation.show();
        }
    }

    private void showSendToDiffusionValidation() {
        if (ConceptClientSecurityUtils.canSendConceptSchemeToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    private void showRejectValidationButton() {
        if (ConceptClientSecurityUtils.canRejectConceptSchemeValidation()) {
            rejectValidation.show();
        }
    }

    private void showPublishInternallyButton() {
        if (ConceptClientSecurityUtils.canPublishConceptSchemeInternally()) {
            publishInternally.show();
        }
    }

    private void showPublishExternallyButton() {
        if (ConceptClientSecurityUtils.canPublishConceptSchemeExternally()) {
            publishExternally.show();
        }
    }

    private void showVersioningButton() {
        if (ConceptClientSecurityUtils.canVersioningConceptScheme()) {
            versioning.show();
        }
    }

    private void showCancelValidityButton() {
        if (ConceptClientSecurityUtils.canCancelConceptSchemeValidity()) {
            cancelValidity.show();
        }
    }

}

package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.MaintainableArtefactDto;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public abstract class LifeCycleMainFormLayout extends InternationalMainFormLayout {

    protected MainFormLayoutButton productionValidation;
    protected MainFormLayoutButton diffusionValidation;
    protected MainFormLayoutButton rejectValidation;
    protected MainFormLayoutButton publishInternally;
    protected MainFormLayoutButton publishExternally;
    protected MainFormLayoutButton versioning;
    protected MainFormLayoutButton cancelValidity;
    protected MainFormLayoutButton versionSdmxResource;
    // protected AnnounceToolStripButton announce;

    protected ProcStatusEnum       status;
    protected Date                 validTo;
    protected RelatedResourceDto   maintainer;
    protected String               versionLogic;

    public LifeCycleMainFormLayout() {
        super();
        common();
    }

    public LifeCycleMainFormLayout(boolean canEdit) {
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
        versionSdmxResource = new MainFormLayoutButton(getConstants().lifeCycleVersionSdmxResource(), GlobalResources.RESOURCE.version().getURL());
        // announce = new AnnounceToolStripButton(MetamacWebCommon.getConstants().announce(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.announce().getURL());

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publishInternally);
        toolStrip.addButton(publishExternally);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
        toolStrip.addButton(cancelValidity);
        toolStrip.addButton(versionSdmxResource);
        // toolStrip.addButton(announce);
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        hideAllLifeCycleButtons();
    }

    public void updatePublishSection(ProcStatusEnum status, MaintainableArtefactDto maintainableArtefactDto) {
        this.status = status;
        this.validTo = maintainableArtefactDto.getValidTo();
        this.maintainer = maintainableArtefactDto.getMaintainer();
        this.versionLogic = maintainableArtefactDto.getVersionLogic();
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

    public HasClickHandlers getVersionSdmxResource() {
        return versionSdmxResource;
    }

    // public HasClickHandlers getAnnounce() {
    // return announce;
    // }

    protected void hideAllLifeCycleButtons() {
        productionValidation.hide();
        diffusionValidation.hide();
        rejectValidation.hide();
        publishInternally.hide();
        publishExternally.hide();
        versioning.hide();
        cancelValidity.hide();
        versionSdmxResource.hide();
        // announce.hide();
    }

    protected void updateVisibility() {
        // Hide all buttons
        hideAllLifeCycleButtons();
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
        // Version SDMX resource button: this button will be shown when the resource is in a temporal version. Take into account that resources with a temporal version can be in any procStatus,
        // excepting INTERNALLY_PUBLISHED and EXTERNALLY_PUBLISHED.
        showVersionSdmxResourceButton();

        // Announce button (does not depends on the procStatus)
        // showAnnounceButton();
    }

    protected abstract void showSendToProductionValidation();

    protected abstract void showSendToDiffusionValidation();

    protected abstract void showRejectValidationButton();

    protected abstract void showPublishInternallyButton();

    protected abstract void showPublishExternallyButton();

    protected abstract void showVersioningButton();

    protected abstract void showCancelValidityButton();

    protected abstract void showVersionSdmxResourceButton();

    // protected abstract void showAnnounceButton();
}

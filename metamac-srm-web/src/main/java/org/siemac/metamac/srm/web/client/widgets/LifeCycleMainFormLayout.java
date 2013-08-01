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
    protected MainFormLayoutButton createTemporalVersion;
    protected MainFormLayoutButton cancelValidity;
    protected MainFormLayoutButton consolidateVersion;
    protected MainFormLayoutButton export;
    protected MainFormLayoutButton copy;
    // protected AnnounceToolStripButton announce;

    protected String               urn;
    protected ProcStatusEnum       procStatus;
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
        createTemporalVersion = new MainFormLayoutButton(getConstants().lifeCycleCreateVersion(), GlobalResources.RESOURCE.version().getURL());
        consolidateVersion = new MainFormLayoutButton(getConstants().lifeCycleConsolidateVersion(), GlobalResources.RESOURCE.version().getURL());
        cancelValidity = new MainFormLayoutButton(getConstants().lifeCycleCancelValidity(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.disable().getURL());
        export = new MainFormLayoutButton(getConstants().actionExport(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        copy = new MainFormLayoutButton(getConstants().actionCopy(), GlobalResources.RESOURCE.copy().getURL());
        // announce = new AnnounceToolStripButton(MetamacWebCommon.getConstants().announce(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.announce().getURL());

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publishInternally);
        toolStrip.addButton(publishExternally);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(createTemporalVersion);
        toolStrip.addButton(consolidateVersion);
        toolStrip.addButton(cancelValidity);
        toolStrip.addButton(export);
        toolStrip.addButton(copy);
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
        this.urn = maintainableArtefactDto.getUrn();
        this.procStatus = status;
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

    public HasClickHandlers getCreateTemporalVersion() {
        return createTemporalVersion;
    }

    public HasClickHandlers getCancelValidity() {
        return cancelValidity;
    }

    public HasClickHandlers getConsolidateVersion() {
        return consolidateVersion;
    }

    public HasClickHandlers getExport() {
        return export;
    }

    public HasClickHandlers getCopy() {
        return copy;
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
        createTemporalVersion.hide();
        cancelValidity.hide();
        consolidateVersion.hide();
        export.hide();
        copy.hide();
        // announce.hide();
    }

    protected void updateVisibility() {
        // Hide all buttons
        hideAllLifeCycleButtons();
        // Show buttons depending on the status
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            showSendToProductionValidation();
        } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)) {
            showSendToProductionValidation();
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            showSendToDiffusionValidation();
            showRejectValidationButton();
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            showPublishInternallyButton();
            showRejectValidationButton();
        } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus)) {
            showPublishExternallyButton();
            showCreateTemporalVersionButton();
        } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            showCreateTemporalVersionButton();
            // Only cancel scheme validity if it has not been canceled previously
            if (validTo == null) {
                showCancelValidityButton();
            }
        }
        // Version SDMX resource button: this button will be shown when the resource is in a temporal version. Take into account that resources with a temporal version can be in any procStatus,
        // excepting INTERNALLY_PUBLISHED and EXTERNALLY_PUBLISHED.
        showConsolidateVersionButton();

        showExportButton();
        showCopyButton();

        // Announce button (does not depends on the procStatus)
        // showAnnounceButton();
    }

    protected abstract void showSendToProductionValidation();

    protected abstract void showSendToDiffusionValidation();

    protected abstract void showRejectValidationButton();

    protected abstract void showPublishInternallyButton();

    protected abstract void showPublishExternallyButton();

    protected abstract void showCreateTemporalVersionButton();

    protected abstract void showConsolidateVersionButton();

    protected abstract void showCancelValidityButton();

    protected abstract void showExportButton();

    protected abstract void showCopyButton();

    // protected abstract void showAnnounceButton();
}

package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.domain.srm.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.utils.ClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class ConceptSchemeMainFormLayout extends InternationalMainFormLayout {

    private PublishToolStripButton             pendingPublication;
    private PublishToolStripButton             rejectValidation;
    private PublishToolStripButton             publishInternally;
    private PublishToolStripButton             publishExternally;
    private PublishToolStripButton             versioning;

    private MaintainableArtefactProcStatusEnum status;

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

        pendingPublication = new PublishToolStripButton(getConstants().conceptSchemeSendToPendingPublication(), GlobalResources.RESOURCE.pendingPublication().getURL());
        publishInternally = new PublishToolStripButton(getConstants().conceptSchemePublishInternally(), GlobalResources.RESOURCE.internalPublish().getURL());
        publishExternally = new PublishToolStripButton(getConstants().conceptSchemePublishExternally(), GlobalResources.RESOURCE.externalPublish().getURL());
        rejectValidation = new PublishToolStripButton(getConstants().conceptSchemeRejectValidation(), GlobalResources.RESOURCE.reject().getURL());
        versioning = new PublishToolStripButton(getConstants().conceptSchemeVersioning(), GlobalResources.RESOURCE.version().getURL());

        toolStrip.addButton(pendingPublication);
        toolStrip.addButton(publishInternally);
        toolStrip.addButton(publishExternally);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
    }

    public void updatePublishSection(MaintainableArtefactProcStatusEnum status) {
        this.status = status;
    }

    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(status)) {
            showSendToPendingPublication();
        } else if (MaintainableArtefactProcStatusEnum.PENDING_PUBLICATION.equals(status)) {
            showPublishInternallyButton();
            showRejectValidationButton();
        } else if (MaintainableArtefactProcStatusEnum.VALIDATION_REJECTED.equals(status)) {
            showSendToPendingPublication();
        } else if (MaintainableArtefactProcStatusEnum.INTERNAL_PUBLICATION_FAILED.equals(status)) {
            showPublishInternallyButton();
        } else if (MaintainableArtefactProcStatusEnum.EXTERNAL_PUBLICATION_FAILED.equals(status)) {
            showPublishExternallyButton();
        } else if (MaintainableArtefactProcStatusEnum.INTERNALLY_PUBLISHED.equals(status)) {
            showPublishExternallyButton();
            showVersioningButton();
        } else if (MaintainableArtefactProcStatusEnum.EXTERNALLY_PUBLISHED.equals(status)) {
            showVersioningButton();
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

    public HasClickHandlers getSendToPendingPublication() {
        return pendingPublication;
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

    private void hideAllPublishButtons() {
        pendingPublication.hide();
        rejectValidation.hide();
        publishInternally.hide();
        publishExternally.hide();
        versioning.hide();
    }

    private void showSendToPendingPublication() {
        if (ClientSecurityUtils.canSendConceptSchemeToPendingPublication()) {
            pendingPublication.show();
        }
    }

    private void showRejectValidationButton() {
        if (MaintainableArtefactProcStatusEnum.PENDING_PUBLICATION.equals(status)) {
            if (ClientSecurityUtils.canRejectConceptSchemeValidation()) {
                rejectValidation.show();
            }
        }
    }

    private void showPublishInternallyButton() {
        if (ClientSecurityUtils.canPublishInternallyConceptScheme()) {
            publishInternally.show();
        }
    }

    private void showPublishExternallyButton() {
        if (ClientSecurityUtils.canPublishExternallyConceptScheme()) {
            publishExternally.show();
        }
    }

    private void showVersioningButton() {
        if (ClientSecurityUtils.canVersioningConceptScheme()) {
            versioning.show();
        }
    }

}

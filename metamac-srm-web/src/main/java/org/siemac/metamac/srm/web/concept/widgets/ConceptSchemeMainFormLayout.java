package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.concept.utils.ConceptClientSecurityUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.AnnounceToolStripButton;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class ConceptSchemeMainFormLayout extends InternationalMainFormLayout {

    private MainFormLayoutButton               pendingPublication;
    private MainFormLayoutButton               rejectValidation;
    private MainFormLayoutButton               publishInternally;
    private MainFormLayoutButton               publishExternally;
    private MainFormLayoutButton               versioning;
    private AnnounceToolStripButton            announce;

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

        pendingPublication = new MainFormLayoutButton(getConstants().conceptSchemeSendToPendingPublication(), GlobalResources.RESOURCE.pendingPublication().getURL());
        publishInternally = new MainFormLayoutButton(getConstants().conceptSchemePublishInternally(), GlobalResources.RESOURCE.internalPublish().getURL());
        publishExternally = new MainFormLayoutButton(getConstants().conceptSchemePublishExternally(), GlobalResources.RESOURCE.externalPublish().getURL());
        rejectValidation = new MainFormLayoutButton(getConstants().conceptSchemeRejectValidation(), GlobalResources.RESOURCE.reject().getURL());
        versioning = new MainFormLayoutButton(getConstants().conceptSchemeVersioning(), GlobalResources.RESOURCE.version().getURL());
        announce = new AnnounceToolStripButton(MetamacWebCommon.getConstants().announce(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.announce().getURL());
        announce.setVisibility(ConceptClientSecurityUtils.canAnnounceConceptScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);

        toolStrip.addButton(pendingPublication);
        toolStrip.addButton(publishInternally);
        toolStrip.addButton(publishExternally);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
        toolStrip.addButton(announce);
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
        if (ConceptClientSecurityUtils.canSendConceptSchemeToPendingPublication()) {
            pendingPublication.show();
        }
    }

    private void showRejectValidationButton() {
        if (MaintainableArtefactProcStatusEnum.PENDING_PUBLICATION.equals(status)) {
            if (ConceptClientSecurityUtils.canRejectConceptSchemeValidation()) {
                rejectValidation.show();
            }
        }
    }

    private void showPublishInternallyButton() {
        if (ConceptClientSecurityUtils.canPublishInternallyConceptScheme()) {
            publishInternally.show();
        }
    }

    private void showPublishExternallyButton() {
        if (ConceptClientSecurityUtils.canPublishExternallyConceptScheme()) {
            publishExternally.show();
        }
    }

    private void showVersioningButton() {
        if (ConceptClientSecurityUtils.canVersioningConceptScheme()) {
            versioning.show();
        }
    }

}

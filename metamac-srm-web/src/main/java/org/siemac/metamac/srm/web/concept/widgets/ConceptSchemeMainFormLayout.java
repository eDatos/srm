package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;

public class ConceptSchemeMainFormLayout extends LifeCycleMainFormLayout {

    private ConceptSchemeTypeEnum type;
    private String                relatedOperationCode;

    public ConceptSchemeMainFormLayout() {
        super();
    }

    public ConceptSchemeMainFormLayout(boolean canEdit) {
        super(canEdit);
    }

    public void updatePublishSection(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        super.updatePublishSection(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getValidTo());
        this.type = conceptSchemeMetamacDto.getType();
        this.relatedOperationCode = CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
    }

    protected void updateVisibility() {
        super.updateVisibility();
        showAnnounceButton();
    }

    @Override
    protected void showSendToProductionValidation() {
        if (ConceptsClientSecurityUtils.canSendConceptSchemeToProductionValidation(type, relatedOperationCode)) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (ConceptsClientSecurityUtils.canSendConceptSchemeToDiffusionValidation(type, relatedOperationCode)) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (ConceptsClientSecurityUtils.canRejectConceptSchemeValidation(status, type, relatedOperationCode)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (ConceptsClientSecurityUtils.canPublishConceptSchemeInternally(type, relatedOperationCode)) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (ConceptsClientSecurityUtils.canPublishConceptSchemeExternally(type, relatedOperationCode)) {
            publishExternally.show();
        }
    }

    @Override
    protected void showVersioningButton() {
        if (ConceptsClientSecurityUtils.canVersioningConceptScheme(type, relatedOperationCode)) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (ConceptsClientSecurityUtils.canCancelConceptSchemeValidity(type, relatedOperationCode)) {
            cancelValidity.show();
        }
    }

    private void showAnnounceButton() {
        if (ConceptsClientSecurityUtils.canAnnounceConceptScheme(type, relatedOperationCode)) {
            announce.show();
        }
    }

}

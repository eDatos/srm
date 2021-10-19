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

    public void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        super.updatePublishSection(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto);
        this.type = conceptSchemeMetamacDto.getType();
        this.relatedOperationCode = CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        setCanEdit();
        setCanDelete();
    }

    private void setCanEdit() {
        super.setCanEdit(ConceptsClientSecurityUtils.canUpdateConceptScheme(procStatus, type, relatedOperationCode));
    }

    private void setCanDelete() {
        boolean canDelete = false;
        canDelete = ConceptsClientSecurityUtils.canDeleteConceptScheme(procStatus, type, relatedOperationCode);
        super.setCanDelete(canDelete);
    }

    @Override
    protected void updateVisibility() {
        super.updateVisibility();
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
        if (ConceptsClientSecurityUtils.canRejectConceptSchemeValidation(procStatus, type, relatedOperationCode)) {
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
    protected void showCreateTemporalVersionButton() {
        if (ConceptsClientSecurityUtils.canCreateConceptSchemeTemporalVersion(type, relatedOperationCode)) {
            createTemporalVersion.show();
        }
    }

    @Override
    protected void showConsolidateVersionButton() {
        if (ConceptsClientSecurityUtils.canVersioningConceptScheme(type, relatedOperationCode, maintainer, versionLogic)) {
            consolidateVersion.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (ConceptsClientSecurityUtils.canCancelConceptSchemeValidity(urn, type, relatedOperationCode, maintainer, versionLogic, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showExportButton() {
        if (ConceptsClientSecurityUtils.canExportConceptScheme(versionLogic)) {
            export.show();
        }
    }

    @Override
    protected void showCopyButton() {
        if (ConceptsClientSecurityUtils.canCopyConceptScheme(type, relatedOperationCode, maintainer)) {
            copy.show();
        }
    }

    @Override
    protected void showCopyKeepingMaintainerButton() {
        if (ConceptsClientSecurityUtils.canCopyConceptSchemeKeepingMaintainer(type, relatedOperationCode, maintainer)) {
            copyKeepingMaintainer.show();
        }
    }

    @Override
    protected void showLifeCycleReSendStreamMessage() {
        if (ConceptsClientSecurityUtils.canResendConceptSchemeStreamMessage(type, relatedOperationCode)) {
            lifeCycleReSendStreamMessage.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (ConceptsClientSecurityUtils.canAnnounceConceptScheme(type, relatedOperationCode)) {
    // announce.show();
    // }
    // }
}

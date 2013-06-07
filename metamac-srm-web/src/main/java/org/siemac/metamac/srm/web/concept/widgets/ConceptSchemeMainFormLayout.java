package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.utils.TasksClientSecurityUtils;
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
        boolean canEdit = false;
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            canEdit = ConceptsClientSecurityUtils.canCreateConceptSchemeTemporalVersion(type, relatedOperationCode);
        } else {
            canEdit = ConceptsClientSecurityUtils.canUpdateConceptScheme(procStatus, type, relatedOperationCode);
        }
        super.setCanEdit(canEdit);
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
    protected void showVersioningButton() {
        if (canConceptSchemeBeVersion()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (ConceptsClientSecurityUtils.canCancelConceptSchemeValidity(urn, type, relatedOperationCode, maintainer, versionLogic, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showVersionSdmxResourceButton() {
        if (canConceptSchemeBeVersion() && VersionUtil.isTemporalVersion(versionLogic)) {
            versionSdmxResource.show();
        }
    }

    @Override
    protected void showExportButton() {
        if (TasksClientSecurityUtils.canExportStructure(versionLogic)) {
            export.show();
        }
    }

    @Override
    protected void showCopyButton() {
        if (ConceptsClientSecurityUtils.canCopyConceptScheme(type, relatedOperationCode, maintainer)) {
            copy.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (ConceptsClientSecurityUtils.canAnnounceConceptScheme(type, relatedOperationCode)) {
    // announce.show();
    // }
    // }

    private boolean canConceptSchemeBeVersion() {
        // Resources from other maintainers can not be version
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer) && ConceptsClientSecurityUtils.canVersioningConceptScheme(type, relatedOperationCode);
    }
}

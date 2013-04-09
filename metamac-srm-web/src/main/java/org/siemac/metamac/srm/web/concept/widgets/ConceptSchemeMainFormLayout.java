package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class ConceptSchemeMainFormLayout extends LifeCycleMainFormLayout {

    private ConceptSchemeTypeEnum type;
    private String                relatedOperationCode;
    private RelatedResourceDto    maintainer;

    public ConceptSchemeMainFormLayout() {
        super();
    }

    public void setConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        super.updatePublishSection(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getValidTo());
        this.type = conceptSchemeMetamacDto.getType();
        this.relatedOperationCode = CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        this.maintainer = conceptSchemeMetamacDto.getMaintainer();
        setCanEdit(conceptSchemeMetamacDto);
    }

    private void setCanEdit(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        boolean canEdit = false;
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(status)) {
            canEdit = ConceptsClientSecurityUtils.canCreateConceptSchemeTemporalVersion(type, relatedOperationCode);
        } else {
            canEdit = ConceptsClientSecurityUtils.canUpdateConceptScheme(status, type, relatedOperationCode);
        }
        super.setCanEdit(canEdit);
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
        // Resources from other maintainers can not be version
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer) && ConceptsClientSecurityUtils.canVersioningConceptScheme(type, relatedOperationCode)) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (ConceptsClientSecurityUtils.canCancelConceptSchemeValidity(type, relatedOperationCode)) {
            cancelValidity.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (ConceptsClientSecurityUtils.canAnnounceConceptScheme(type, relatedOperationCode)) {
    // announce.show();
    // }
    // }
}

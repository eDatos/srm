package org.siemac.metamac.srm.web.concept.utils;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedConceptsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.TasksClientSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class ConceptsClientSecurityUtils {

    // Schemes

    public static boolean canCreateConceptScheme() {
        // The method SharedConceptsSecurityUtils.canCreateConceptScheme is not called because web application only need to know if the create button can be shown.
        return SharedConceptsSecurityUtils.canCreateItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateConceptScheme(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        // This method is called with the current concept scheme type in the parameters "type" and "typeOld" (the same with the operationCode parameters). The web application only need to know if the
        // edit button can be shown (before doing any metadata change)
        return SharedConceptsSecurityUtils.canUpdateConceptScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode, type, operationCode);
    }

    public static boolean canDeleteConceptScheme(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            return false;
        }
        return SharedConceptsSecurityUtils.canDeleteConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToProductionValidation(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToDiffusionValidation(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canRejectConceptSchemeValidation(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canRejectConceptSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canPublishConceptSchemeInternally(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canPublishConceptSchemeInternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canPublishConceptSchemeExternally(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canPublishConceptSchemeExternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canVersioningConceptScheme(ConceptSchemeTypeEnum type, String operationCode, RelatedResourceDto maintainer, String versionLogic) {
        if (!org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer)) {
            return false;
        }
        if (!VersionUtil.isTemporalVersion(versionLogic)) {
            // The scheme can only be version when the temporal version has been previously created
            return false;
        }
        return SharedConceptsSecurityUtils.canVersioningConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCreateConceptSchemeTemporalVersion(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canCreateConceptSchemeTemporalVersion(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceConceptScheme(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canAnnounceConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelConceptSchemeValidity(ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto) {
        return canCancelConceptSchemeValidity(conceptSchemeMetamacDto.getUrn(), conceptSchemeMetamacDto.getType(),
                org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto), conceptSchemeMetamacDto.getMaintainer(),
                conceptSchemeMetamacDto.getVersionLogic(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getValidTo());
    }

    public static boolean canExportConceptScheme(String versionLogic) {
        return TasksClientSecurityUtils.canExportResource(versionLogic);
    }

    public static boolean canImportConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, String operationCode) {
        if (isTaskInBackground(conceptSchemeMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        // ConceptSchemeTypeEnum type, String operationCode
        return SharedConceptsSecurityUtils.canImportConceptsTsv(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                operationCode) && CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeMetamacDto);
    }

    public static boolean canCancelConceptSchemeValidity(String urn, ConceptSchemeTypeEnum type, String relatedOperationCode, RelatedResourceDto maintainer, String versionLogic,
            ProcStatusEnum procStatus, Date validTo) {

        // validity was already ended
        if (validTo != null) {
            return false;
        }

        // only externally published resources can be canceled
        if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return false;
        }

        return SharedConceptsSecurityUtils.canEndConceptSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, relatedOperationCode)
                && CommonUtils.canSdmxMetadataAndStructureBeModified(urn, maintainer, versionLogic);
    }

    public static boolean canCreateCategorisation(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteCategorisation(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode, CategorisationDto categorisationDto) {

        if (BooleanUtils.isTrue(categorisationDto.getFinalLogic())) {

            // if it is final, can NEVER be deleted
            return false;

        } else {

            if (CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {

                return SharedConceptsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);

            } else {

                // if it does not have the default maintainer, can NEVER be deleted
                return false;
            }
        }
    }

    public static boolean canCancelCategorisationValidity(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode, CategorisationDto categorisationDto) {

        if (categorisationDto.getValidTo() != null) { // The validity has been canceled previously
            return false;
        }

        // Only categorisations of default maintainer can be canceled

        if (CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {
            return SharedConceptsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
        } else {
            return false;
        }
    }

    public static boolean canExportCategorisation(CategorisationDto categorisationDto) {
        return TasksClientSecurityUtils.canExportResource(categorisationDto.getVersionLogic());
    }

    public static boolean canCopyConceptScheme(ConceptSchemeTypeEnum type, String operationCode, RelatedResourceDto maintainer) {
        // Only resources from other organisations can be copied
        return SharedConceptsSecurityUtils.canCopyConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode) && !CommonUtils.isDefaultMaintainer(maintainer);
    }

    public static boolean canCopyConceptSchemeKeepingMaintainer(ConceptSchemeTypeEnum type, String operationCode, RelatedResourceDto maintainer) {
        // Only resources from default organisations can be copied keeping maintainer
        return SharedConceptsSecurityUtils.canCopyConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode) && CommonUtils.isDefaultMaintainer(maintainer);
    }

    // Concepts

    public static boolean canCreateConcept(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        // Maintainer and temporal version are checked because the structure of an imported resource (or a resource in temporal version) can not be modified
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canCreateConcept(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(), operationCode)
                && CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeMetamacDto);
    }

    public static boolean canUpdateConcept(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConcept(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        // Maintainer and temporal version are checked because the structure of an imported resource (or a resource in temporal version) can not be modified
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canDeleteConcept(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(), operationCode)
                && CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeMetamacDto);
    }

    public static boolean canExportConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        if (isTaskInBackground(conceptSchemeMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedConceptsSecurityUtils.canExportConceptsTsv(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateConceptParent(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        if (isTaskInBackground(conceptSchemeMetamacDto.getIsTaskInBackground())) {
            return false;
        }

        // Maintainer and temporal version are checked because the structure of an imported resource (or a resource in temporal version) can not be modified
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(), operationCode)
                && CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeMetamacDto);
    }

    public static boolean canUpdateConceptOrderInLevel(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        if (isTaskInBackground(conceptSchemeMetamacDto.getIsTaskInBackground())) {
            return false;
        }

        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(), operationCode);
    }

    //
    // PRIVATE METHODS
    //

    private static boolean isTaskInBackground(Boolean isTaskInBackground) {
        return BooleanUtils.isTrue(isTaskInBackground);
    }
}

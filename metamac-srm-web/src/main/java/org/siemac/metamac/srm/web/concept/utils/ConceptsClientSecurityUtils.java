package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedConceptsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

public class ConceptsClientSecurityUtils {

    // Schemes

    public static boolean canCreateConceptScheme() {
        return true;
        // TODO return SharedConceptsSecurityUtils.canCreateConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canUpdateConceptScheme() {
        return true;
        // TODO return SharedConceptsSecurityUtils.canUpdateConceptScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConceptScheme(ConceptSchemeTypeEnum type, String operationCode) {
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

    public static boolean canVersioningConceptScheme(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canVersioningConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceConceptScheme(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canAnnounceConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelConceptSchemeValidity(ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canEndConceptSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCreateCategorisation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                operationCode);
    }

    public static boolean canDeleteCategorisation(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        // Maintainer is checked because the creation/deletion of a categorisation is not allowed when the resource is imported (i am not the maintainer)
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                operationCode) && CommonUtils.isDefaultMaintainer(conceptSchemeMetamacDto.getMaintainer());
    }

    // Concepts

    public static boolean canCreateConcept(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canCreateConcept(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(), operationCode)
                && CommonUtils.isDefaultMaintainer(conceptSchemeMetamacDto.getMaintainer());
    }

    public static boolean canUpdateConcept(ProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConcept(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        String operationCode = org.siemac.metamac.srm.web.concept.utils.CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto);
        return SharedConceptsSecurityUtils.canDeleteConcept(MetamacSrmWeb.getCurrentUser(), conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(), operationCode)
                && CommonUtils.isDefaultMaintainer(conceptSchemeMetamacDto.getMaintainer());
    }
}

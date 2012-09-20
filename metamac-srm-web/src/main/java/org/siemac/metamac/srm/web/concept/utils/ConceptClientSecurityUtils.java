package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedConceptsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class ConceptClientSecurityUtils {

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

    public static boolean canRejectConceptSchemeValidation(ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
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
        return SharedConceptsSecurityUtils.canCancelConceptSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    // Concepts

    public static boolean canCreateConcept(ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canCreateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canUpdateConcept(ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConcept(ItemSchemeMetamacProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        return SharedConceptsSecurityUtils.canDeleteConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

}

package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

public class ConceptClientSecurityUtils {

    // TODO Remove these attributes!!!
    private static ItemSchemeMetamacProcStatusEnum procStatus = ItemSchemeMetamacProcStatusEnum.DRAFT;
    private static ConceptSchemeTypeEnum           type       = ConceptSchemeTypeEnum.GLOSSARY;
    private static String                          operationCode;

    // Schemes

    public static boolean canCreateConceptScheme() {
        return true;
        // return SharedConceptsSecurityUtils.canCreateConceptScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateConceptScheme() {
        return true;
        // return SharedConceptsSecurityUtils.canUpdateConceptScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConceptScheme() {
        return true;
        // return SharedConceptsSecurityUtils.canDeleteConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToProductionValidation() {
        return true;
        // return SharedConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToDiffusionValidation() {
        return true;
        // return SharedConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canRejectConceptSchemeValidation() {
        return true;
        // return SharedConceptsSecurityUtils.canRejectConceptSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canPublishConceptSchemeInternally() {
        return true;
        // return SharedConceptsSecurityUtils.canPublishConceptSchemeInternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canPublishConceptSchemeExternally() {
        return true;
        // return SharedConceptsSecurityUtils.canPublishConceptSchemeExternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canVersioningConceptScheme() {
        return true;
        // return SharedConceptsSecurityUtils.canVersioningConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceConceptScheme() {
        return true;
        // return SharedConceptsSecurityUtils.canAnnounceConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelConceptSchemeValidity() {
        return true;
        // return SharedConceptsSecurityUtils.canCancelConceptSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    // Concepts

    public static boolean canCreateConcept() {
        return true;
        // return SharedConceptsSecurityUtils.canCreateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canEditConcept() {
        return true;
        // return SharedConceptsSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConcept() {
        return true;
        // return SharedConceptsSecurityUtils.canDeleteConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

}

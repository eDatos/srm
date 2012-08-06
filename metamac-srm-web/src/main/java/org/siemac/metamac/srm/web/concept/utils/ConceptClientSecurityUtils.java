package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedConceptSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.enume.concept.domain.ConceptSchemeTypeEnum;

public class ConceptClientSecurityUtils {

    // TODO Remove these attributes!!!
    private static MaintainableArtefactProcStatusEnum procStatus = MaintainableArtefactProcStatusEnum.DRAFT;
    private static ConceptSchemeTypeEnum              type       = ConceptSchemeTypeEnum.GLOSSARY;
    private static String                             operationCode;

    // Schemes

    public static boolean canCreateConceptScheme() {
        return SharedConceptSecurityUtils.canCreateConceptScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateConceptScheme() {
        return SharedConceptSecurityUtils.canUpdateConceptScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConceptScheme() {
        return SharedConceptSecurityUtils.canDeleteConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToProductionValidation() {
        return SharedConceptSecurityUtils.canSendConceptSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToDiffusionValidation() {
        return SharedConceptSecurityUtils.canSendConceptSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canRejectConceptSchemeValidation() {
        return SharedConceptSecurityUtils.canRejectConceptSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canPublishInternallyConceptScheme() {
        return SharedConceptSecurityUtils.canPublishInternallyConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canPublishExternallyConceptScheme() {
        return SharedConceptSecurityUtils.canPublishExternallyConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canVersioningConceptScheme() {
        return SharedConceptSecurityUtils.canVersioningConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceConceptScheme() {
        return SharedConceptSecurityUtils.canAnnounceConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelConceptSchemeValidity() {
        return SharedConceptSecurityUtils.canCancelConceptSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    // Concepts

    public static boolean canCreateConcept() {
        return SharedConceptSecurityUtils.canCreateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canEditConcept() {
        return SharedConceptSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConcept() {
        return SharedConceptSecurityUtils.canDeleteConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

}

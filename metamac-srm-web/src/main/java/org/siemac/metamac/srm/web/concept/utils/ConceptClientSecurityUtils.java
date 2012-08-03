package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.enume.concept.domain.ConceptSchemeTypeEnum;

public class ConceptClientSecurityUtils {

    // TODO Remove these attributes!!!
    private static MaintainableArtefactProcStatusEnum procStatus;
    private static ConceptSchemeTypeEnum              type;
    private static String                             operationCode;

    // Schemes

    public static boolean canCreateConceptScheme() {
        return SharedSecurityUtils.canCreateConceptScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateConceptScheme() {
        return SharedSecurityUtils.canUpdateConceptScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConceptScheme() {
        return SharedSecurityUtils.canDeleteConceptScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendConceptSchemeToProductionValidation() {
        return SharedSecurityUtils.canSendConceptSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendConceptSchemeToDiffusionValidation() {
        return SharedSecurityUtils.canSendConceptSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canRejectConceptSchemeValidation() {
        return SharedSecurityUtils.canRejectConceptSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canPublishInternallyConceptScheme() {
        return SharedSecurityUtils.canPublishInternallyConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canPublishExternallyConceptScheme() {
        return SharedSecurityUtils.canPublishExternallyConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canVersioningConceptScheme() {
        return SharedSecurityUtils.canVersioningConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceConceptScheme() {
        return SharedSecurityUtils.canAnnounceConceptScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelConceptSchemeValidity() {
        return SharedSecurityUtils.canCancelConceptSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    // Concepts

    public static boolean canCreateConcept() {
        return SharedSecurityUtils.canCreateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canEditConcept() {
        return SharedSecurityUtils.canUpdateConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteConcept() {
        return SharedSecurityUtils.canDeleteConcept(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

}

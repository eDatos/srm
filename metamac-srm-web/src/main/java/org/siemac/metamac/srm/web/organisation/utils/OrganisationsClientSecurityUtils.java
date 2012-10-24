package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class OrganisationsClientSecurityUtils {

    // ORGANISATION SCHEMES

    public static boolean canCreateOrganisationScheme() {
        return SharedItemsSecurityUtils.canCreateItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateOrganisationScheme(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canUpdateItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteOrganisationScheme() {
        return SharedItemsSecurityUtils.canDeleteItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendOrganisationSchemeToProductionValidation() {
        return SharedItemsSecurityUtils.canSendItemSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendOrganisationSchemeToDiffusionValidation() {
        return SharedItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectOrganisationSchemeValidation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canRejectItemSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishOrganisationSchemeInternally() {
        return SharedItemsSecurityUtils.canPublishItemSchemeInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishOrganisationSchemeExternally() {
        return SharedItemsSecurityUtils.canPublishItemSchemeExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningOrganisationScheme() {
        return SharedItemsSecurityUtils.canVersioningItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceOrganisationScheme() {
        return SharedItemsSecurityUtils.canAnnounceItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelOrganisationSchemeValidity() {
        return SharedItemsSecurityUtils.canEndItemSchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    // ORGANISATIONS

    public static boolean canCreateOrganisation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifiyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateOrganisation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifiyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteOrganisation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifiyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

}

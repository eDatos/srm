package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class OrganisationsClientSecurityUtils {

    // ORGANISATION SCHEMES

    public static boolean canCreateOrganisationScheme() {
        return true;
        // TODO return SharedOrganisationsSecurityUtils.canCreateOrganisationScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canUpdateOrganisationScheme() {
        return true;
        // TODO return SharedOrganisationsSecurityUtils.canUpdateOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteOrganisationScheme() {
        return true;
        // return SharedOrganisationsSecurityUtils.canDeleteOrganisationScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendOrganisationSchemeToProductionValidation() {
        return true;
        // return SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendOrganisationSchemeToDiffusionValidation() {
        return true;
        // return SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canRejectOrganisationSchemeValidation(ProcStatusEnum procStatus) {
        return true;
        // return SharedOrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canPublishOrganisationSchemeInternally() {
        return true;
        // return SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canPublishOrganisationSchemeExternally() {
        return true;
        // return SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canVersioningOrganisationScheme() {
        return true;
        // return SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceOrganisationScheme() {
        return true;
        // return SharedOrganisationsSecurityUtils.canAnnounceOrganisationScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelOrganisationSchemeValidity() {
        return true;
        // return SharedOrganisationsSecurityUtils.canCancelOrganisationSchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    // ORGANISATIONS

    public static boolean canCreateOrganisation(ProcStatusEnum procStatus) {
        return true;
        // return SharedOrganisationsSecurityUtils.canCreateOrganisation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canUpdateOrganisation(ProcStatusEnum procStatus) {
        return true;
        // return SharedOrganisationsSecurityUtils.canUpdateOrganisation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteOrganisation(ProcStatusEnum procStatus) {
        return true;
        // return SharedOrganisationsSecurityUtils.canDeleteOrganisation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

}

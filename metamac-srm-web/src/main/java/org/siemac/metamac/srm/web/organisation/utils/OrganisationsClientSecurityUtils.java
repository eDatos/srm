package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedOrganisationsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsClientSecurityUtils {

    // ORGANISATION SCHEMES

    public static boolean canCreateOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canCreateOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canUpdateOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canDeleteOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendOrganisationSchemeToProductionValidation() {
        return SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendOrganisationSchemeToDiffusionValidation() {
        return SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectOrganisationSchemeValidation(ProcStatusEnum procStatus) {
        return SharedOrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishOrganisationSchemeInternally() {
        return SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishOrganisationSchemeExternally() {
        return SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canAnnounceOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelOrganisationSchemeValidity() {
        return SharedOrganisationsSecurityUtils.canEndOrganisationSchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    // ORGANISATIONS

    public static boolean canCreateOrganisation(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifiyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canUpdateOrganisation(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifiyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteOrganisation(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifiyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

}

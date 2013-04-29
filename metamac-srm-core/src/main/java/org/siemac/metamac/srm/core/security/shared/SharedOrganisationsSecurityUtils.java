package org.siemac.metamac.srm.core.security.shared;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class SharedOrganisationsSecurityUtils extends SharedItemsSecurityUtils {

    //
    // ORGANISATION SCHEMES
    //

    public static boolean canRetrieveOrganisationSchemeByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindOrganisationSchemesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveOrganisationSchemeVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateOrganisationScheme(MetamacPrincipal metamacPrincipal) {
        return canCreateItemScheme(metamacPrincipal);
    }

    public static boolean canUpdateOrganisationScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return canUpdateItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canDeleteOrganisationScheme(MetamacPrincipal metamacPrincipal) {
        return canDeleteItemScheme(metamacPrincipal);
    }

    public static boolean canSendOrganisationSchemeToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToProductionValidation(metamacPrincipal);
    }

    public static boolean canSendOrganisationSchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToDiffusionValidation(metamacPrincipal);
    }

    public static boolean canRejectOrganisationSchemeValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canRejectItemSchemeValidation(metamacPrincipal, procStatus);
    }

    public static boolean canPublishOrganisationSchemeInternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeInternally(metamacPrincipal);
    }

    public static boolean canPublishOrganisationSchemeExternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeExternally(metamacPrincipal);
    }

    public static boolean canCopyOrganisationScheme(MetamacPrincipal metamacPrincipal) {
        return canCopyItemScheme(metamacPrincipal);
    }

    public static boolean canVersioningOrganisationScheme(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canCreateOrganisationSchemeTemporalVersion(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canAnnounceOrganisationScheme(MetamacPrincipal metamacPrincipal) {
        return canAnnounceItemScheme(metamacPrincipal);
    }

    public static boolean canEndOrganisationSchemeValidity(MetamacPrincipal metamacPrincipal) {
        return canEndItemSchemeValidity(metamacPrincipal);
    }

    //
    // ITEMS
    //

    public static boolean canRetrieveOrganisationByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveOrganisationsByOrganisationSchemeUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindOrganisationsByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    /**
     * Determines if items from an organisation scheme can be created, deleted or updated
     */
    public static boolean canModifyOrganisationFromOrganisationScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
    }

    /**
     * Determines if categorisations from an organisation scheme can be created or deleted
     */
    public static boolean canModifyCategorisationFromOrganisationScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return canPublishOrganisationSchemeExternally(metamacPrincipal);
        } else {
            return canUpdateOrganisationScheme(metamacPrincipal, procStatus, type);
        }
    }
}
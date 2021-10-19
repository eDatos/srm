package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedItemsSecurityUtils extends SharedSecurityUtils {

    //
    // ITEM SCHEMES
    //

    public static boolean canCreateItemScheme(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canUpdateItemScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        }
        return false;
    }

    public static boolean canDeleteItemScheme(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canSendItemSchemeToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
    }

    public static boolean canSendItemSchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
    }

    public static boolean canRejectItemSchemeValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        }
        return false;
    }

    public static boolean canPublishItemSchemeInternally(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canPublishItemSchemeExternally(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canResendItemSchemeStreamMessage(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canVersioningItemScheme(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canCopyItemScheme(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canAnnounceItemScheme(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
    }

    public static boolean canEndItemSchemeValidity(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    //
    // ITEMS
    //

    /**
     * Determines if items from an item scheme can be created, deleted or updated
     */
    public static boolean canModifyItemFromItemScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        }
        return false;
    }

    // OTHER

    public static boolean canModifyCategorisation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return canPublishItemSchemeExternally(metamacPrincipal);
        } else {
            return canUpdateItemScheme(metamacPrincipal, procStatus);
        }
    }
}
package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedDsdSecurityUtils extends SharedSecurityUtils {

    public static boolean canCreateDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canUpdateDsd(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canVersioningDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canUpdatePrimaryMeasure(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canUpdateDimensions(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canUpdateAttributes(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canUpdateGroupKeys(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canDeleteDsd(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus) || MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canImportDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canUpdateAnnotations(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canCancelDsdValidity(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
    }

    public static boolean canAnnounceDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canSendDsdToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_PRODUCCION, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canSendDsdToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canRejectDsdValidation(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus) {
        if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canPublishDsdInternally(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
    }

    public static boolean canPublishDsdExternally(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    /**
     * Checks if metamacPrincipal has any of the roles allowed in SRM module
     * 
     * @param metamacPrincipal
     * @return
     */
    private static boolean isAnyDsdRole(MetamacPrincipal metamacPrincipal) {
        return isAdministrador(metamacPrincipal) || isTecnicoApoyoProduccion(metamacPrincipal) || isTecnicoProduccion(metamacPrincipal) || isJefeProduccion(metamacPrincipal);
    }

}

package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedDsdSecurityUtils extends SharedSecurityUtils {

    /**
     * DSD
     */

    public static boolean canCreateDataStructureDefinition(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canUpdateDsd(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canDeleteDsd(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canFindDataStructureDefinitionByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveDataStructureDefinitionByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveDataStructureDefinitionVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canImportDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    /**
     * LIFECYCLE
     */

    public static boolean canSendDsdToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_PRODUCCION, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canSendDsdToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canRejectDsdValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
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

    public static boolean canVersioningDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canAnnounceDsd(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
    }

    public static boolean canEndDsdValidity(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
    }

    /**
     * ARTEFACTS IN DSD
     */

    public static boolean canUpdatePrimaryMeasure(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canUpdateDimensions(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canUpdateAttributes(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canUpdateGroupKeys(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canFindDescriptorsForDataStructureDefinition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canSaveDescriptorForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canDeleteDescriptorForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canSaveComponentForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus)) {
            return isAnyDsdRole(metamacPrincipal);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canDeleteComponentForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            return isSrmRoleAllowed(metamacPrincipal, JEFE_PRODUCCION);
        }
        return false;
    }

    public static boolean canModifyCategorisationForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return canPublishDsdExternally(metamacPrincipal);
        } else {
            return canUpdateDsd(metamacPrincipal, procStatus);
        }
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
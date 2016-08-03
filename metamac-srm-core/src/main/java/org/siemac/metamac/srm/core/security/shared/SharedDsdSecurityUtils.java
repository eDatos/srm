package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedDsdSecurityUtils extends SharedSecurityUtils {

    //
    // DSD
    //

    public static boolean canRetrieveDataStructureDefinitionByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveDataStructureDefinitionVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateDataStructureDefinition(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canUpdateDsd(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCodeOld, String operationCodeNew) {
        return canUpdateDataStructureDefinition(metamacPrincipal, procStatus, operationCodeOld) && canUpdateDataStructureDefinition(metamacPrincipal, procStatus, operationCodeNew);
    }

    public static boolean canCopyDataStructureDefinition(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    /**
     * Do not call this method from DataStructureDefinitionSecurityUtils or to validate the security in the facade (for application web use only).
     * 
     * @param metamacPrincipal
     * @param procStatus
     * @param operationCode
     * @return
     */
    public static boolean canUpdateDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_APOYO_PRODUCCION, JEFE_PRODUCCION};
            return isAnyDsdRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum[] roles = {JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canDeleteDsd(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus) || ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum roles[] = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum roles[] = {JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canFindDataStructureDefinitionByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    /**
     * LIFECYCLE
     */

    public static boolean canSendDsdToProductionValidation(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {TECNICO_APOYO_PRODUCCION, TECNICO_PRODUCCION, JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canSendDsdToDiffusionValidation(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canRejectDsdValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum[] roles = {JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canPublishDsdInternally(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canPublishDsdExternally(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canVersioningDsd(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canCreateDsdTemporalVersion(MetamacPrincipal metamacPrincipal, String operationCode) {
        return canVersioningDsd(metamacPrincipal, operationCode);
    }

    public static boolean canAnnounceDsd(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
    }

    public static boolean canEndDsdValidity(MetamacPrincipal metamacPrincipal, String operationCode) {
        SrmRoleEnum[] roles = {JEFE_PRODUCCION};
        return isSrmRoleAllowed(metamacPrincipal, roles);
    }

    //
    // ARTEFACTS IN DSD
    //

    public static boolean canUpdatePrimaryMeasure(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canUpdateDimensions(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canUpdateAttributes(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canUpdateGroupKeys(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canFindDescriptorsForDataStructureDefinition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canSaveDescriptorForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canDeleteDescriptorForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canSaveComponentForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    public static boolean canDeleteComponentForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        return canModifyComponentFromDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
    }

    // Categorisations

    public static boolean canModifyCategorisationForDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return canPublishDsdExternally(metamacPrincipal, operationCode);
        } else {
            return canUpdateDataStructureDefinition(metamacPrincipal, procStatus, operationCode);
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

    private static boolean canModifyComponentFromDataStructureDefinition(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus, String operationCode) {
        if (ProcStatusEnum.DRAFT.equals(procStatus) || ProcStatusEnum.VALIDATION_REJECTED.equals(procStatus)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_APOYO_PRODUCCION, JEFE_PRODUCCION};
            return isAnyDsdRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        } else if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            SrmRoleEnum[] roles = {JEFE_PRODUCCION};
            return isSrmRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }
}
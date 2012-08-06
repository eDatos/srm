package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.ADMINISTRADOR;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.ANY_ROLE_ALLOWED;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;

import com.arte.statistic.sdmx.v2_1.domain.enume.concept.domain.ConceptSchemeTypeEnum;

public class SharedSecurityUtils {

    // Schemes

    public static boolean canCreateConceptScheme(MetamacPrincipal metamacPrincipal) {
        return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canUpdateConceptScheme(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_PRODUCCION, JEFE_NORMALIZACION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canDeleteConceptScheme(MetamacPrincipal metamacPrincipal) {
        return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION, JEFE_PRODUCCION);
    }

    public static boolean canSendConceptSchemeToProductionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
        }
        return false;
    }

    public static boolean canSendConceptSchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canRejectConceptSchemeValidation(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canPublishInternallyConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum[] roles = {JEFE_NORMALIZACION};
            return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canPublishExternallyConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canVersioningConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION};
            return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canAnnounceConceptScheme(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    public static boolean canCancelConceptSchemeValidity(MetamacPrincipal metamacPrincipal, ConceptSchemeTypeEnum type, String operationCode) {
        if (isNonOperationConceptSchemeType(type)) {
            return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
        } else if (isOperationConceptSchemeType(type)) {
            SrmRoleEnum roles[] = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
            return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
        }
        return false;
    }

    // Concepts

    public static boolean canCreateConcept(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canUpdateConcept(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    public static boolean canDeleteConcept(MetamacPrincipal metamacPrincipal, MaintainableArtefactProcStatusEnum procStatus, ConceptSchemeTypeEnum type, String operationCode) {
        if (MaintainableArtefactProcStatusEnum.DRAFT.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                return isAnySrmRole(metamacPrincipal) && isOperationAllowed(metamacPrincipal, operationCode, ANY_ROLE_ALLOWED);
            }
        } else if (MaintainableArtefactProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {TECNICO_PRODUCCION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        } else if (MaintainableArtefactProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus)) {
            if (isNonOperationConceptSchemeType(type)) {
                return isRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
            } else if (isOperationConceptSchemeType(type)) {
                SrmRoleEnum[] roles = {JEFE_NORMALIZACION, JEFE_PRODUCCION};
                return isRoleAllowed(metamacPrincipal, roles) && isOperationAllowed(metamacPrincipal, operationCode, roles);
            }
        }
        return false;
    }

    /**
     * Checks if logged user has one of the allowed roles
     * 
     * @param roles
     * @return
     */
    private static boolean isRoleAllowed(MetamacPrincipal metamacPrincipal, SrmRoleEnum... roles) {
        // Administration has total control
        if (SharedSecurityUtils.isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks user has any role of requested
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                SrmRoleEnum role = roles[i];
                if (SharedSecurityUtils.isUserInRol(metamacPrincipal, role)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if logged user has access to a statistical operation with one of the selected roles
     * 
     * @param operationCode
     * @param roles
     * @return
     * @throws MetamacException
     */
    private static boolean isOperationAllowed(MetamacPrincipal metamacPrincipal, String operationCode, SrmRoleEnum... roles) {
        // Administrator has total control in all statistical operations
        if (isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks if the statistical operation is in any role
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                SrmRoleEnum role = roles[i];
                // If role is any of the normalization roles, do not check permissions related to the statistical operation (does not matter the operation associated)
                if (isAnyNormalizationRole(role)) {
                    if (isUserInRol(metamacPrincipal, role)) {
                        return true;
                    }
                } else if (haveAccessToOperationInRol(metamacPrincipal, role, operationCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks user has any role
     */
    private static boolean isUserInRol(MetamacPrincipal metamacPrincipal, SrmRoleEnum role) {
        if (ANY_ROLE_ALLOWED.equals(role)) {
            return isAnySrmRole(metamacPrincipal);
        } else {
            return isRoleInAccesses(metamacPrincipal, role);
        }
    }

    /**
     * Checks if user has access to an operation. To have access, any access must exists to specified role and operation, or has any access with
     * role and operation with 'null' value
     */
    public static boolean haveAccessToOperationInRol(MetamacPrincipal metamacPrincipal, SrmRoleEnum role, String operation) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (SrmConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                if (metamacPrincipalAccess.getOperation() == null || metamacPrincipalAccess.getOperation().equals(operation)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean isAdministrador(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, ADMINISTRADOR);
    }

    /**
     * Checks if user has access with role
     */
    private static Boolean isRoleInAccesses(MetamacPrincipal metamacPrincipal, SrmRoleEnum role) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (SrmConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean isAnySrmRole(MetamacPrincipal metamacPrincipal) {
        return isAdministrador(metamacPrincipal) || isTecnicoApoyoNormalizacion(metamacPrincipal) || isTecnicoNormalizacion(metamacPrincipal) || isJefeNormalizacion(metamacPrincipal)
                || isTecnicoApoyoProduccion(metamacPrincipal) || isTecnicoProduccion(metamacPrincipal) || isJefeProduccion(metamacPrincipal);
    }

    private static boolean isAnyNormalizationRole(SrmRoleEnum role) {
        return (SrmRoleEnum.TECNICO_APOYO_NORMALIZACION.equals(role) || SrmRoleEnum.TECNICO_NORMALIZACION.equals(role) || SrmRoleEnum.JEFE_NORMALIZACION.equals(role));
    }

    private static Boolean isTecnicoApoyoNormalizacion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_APOYO_NORMALIZACION);
    }

    private static Boolean isTecnicoNormalizacion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_NORMALIZACION);
    }

    private static Boolean isJefeNormalizacion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, JEFE_NORMALIZACION);
    }

    private static Boolean isTecnicoApoyoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_APOYO_PRODUCCION);
    }

    private static Boolean isTecnicoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_PRODUCCION);
    }

    private static Boolean isJefeProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, JEFE_PRODUCCION);
    }

    private static boolean isOperationConceptSchemeType(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type);
    }

    private static boolean isNonOperationConceptSchemeType(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.GLOSSARY.equals(type) || ConceptSchemeTypeEnum.ROLE.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type);
    }

}

package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.ADMINISTRADOR;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.ANY_ROLE_ALLOWED;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;

public class SharedSecurityUtils {

    /**
     * Determines if a retrieve or find operation can be executed.
     * 
     * @param metamacPrincipal
     * @return
     */
    public static boolean canRetrieveOrFindResource(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }

    /**
     * Checks if logged user has one of the allowed roles
     * 
     * @param roles
     * @return
     */
    protected static boolean isSrmRoleAllowed(MetamacPrincipal metamacPrincipal, SrmRoleEnum... roles) {
        // Administration has total control
        if (SharedSecurityUtils.isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks user has any role of requested
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                SrmRoleEnum role = roles[i];
                if (SharedSecurityUtils.isUserInSrmRol(metamacPrincipal, role)) {
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
     */
    protected static boolean isOperationAllowed(MetamacPrincipal metamacPrincipal, String operationCode, SrmRoleEnum... roles) {
        // Administrator has total control in all statistical operations
        if (isAdministrador(metamacPrincipal)) {
            return true;
        }
        // Checks if the statistical operation is in any role
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                SrmRoleEnum role = roles[i];
                if (haveAccessToOperationInRol(metamacPrincipal, role, operationCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks user has any role
     */
    protected static boolean isUserInSrmRol(MetamacPrincipal metamacPrincipal, SrmRoleEnum role) {
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
    protected static boolean haveAccessToOperationInRol(MetamacPrincipal metamacPrincipal, SrmRoleEnum role, String operation) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (SrmConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                if (metamacPrincipalAccess.getOperation() == null || metamacPrincipalAccess.getOperation().equals(operation)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean isAdministrador(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, ADMINISTRADOR);
    }

    /**
     * Checks if user has access with role
     */
    protected static boolean isRoleInAccesses(MetamacPrincipal metamacPrincipal, SrmRoleEnum role) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (SrmConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if metamacPrincipal has any of the roles allowed in SRM (except DSD module)
     * 
     * @param metamacPrincipal
     * @return
     */
    protected static boolean isAnySrmRole(MetamacPrincipal metamacPrincipal) {
        return isAdministrador(metamacPrincipal) || isTecnicoApoyoNormalizacion(metamacPrincipal) || isTecnicoNormalizacion(metamacPrincipal) || isJefeNormalizacion(metamacPrincipal)
                || isTecnicoApoyoProduccion(metamacPrincipal) || isTecnicoProduccion(metamacPrincipal) || isJefeProduccion(metamacPrincipal);
    }

    protected static boolean isAnyNormalizationRole(SrmRoleEnum role) {
        return (SrmRoleEnum.TECNICO_APOYO_NORMALIZACION.equals(role) || SrmRoleEnum.TECNICO_NORMALIZACION.equals(role) || SrmRoleEnum.JEFE_NORMALIZACION.equals(role));
    }

    protected static boolean isTecnicoApoyoNormalizacion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_APOYO_NORMALIZACION);
    }

    protected static boolean isTecnicoNormalizacion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_NORMALIZACION);
    }

    protected static boolean isJefeNormalizacion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, JEFE_NORMALIZACION);
    }

    protected static boolean isTecnicoApoyoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_APOYO_PRODUCCION);
    }

    protected static boolean isTecnicoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, TECNICO_PRODUCCION);
    }

    protected static boolean isJefeProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, JEFE_PRODUCCION);
    }

    protected static boolean isOperationConceptSchemeType(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type);
    }

    protected static boolean isNonOperationConceptSchemeType(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.GLOSSARY.equals(type) || ConceptSchemeTypeEnum.ROLE.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type);
    }

}

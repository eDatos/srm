package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;

import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCodesSecurityUtils extends SharedSecurityUtils {

    //
    // NOTE: Only to related entities. Security about codelists and codes is in ItemSecurityUtils
    //

    public static boolean canRetrieveOrFindCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION); // TODO pendiente confirmación seguridad
    }

    public static boolean canRetrieveOrFindVariableFamily(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION); // TODO pendiente confirmación seguridad
    }

    public static boolean canRetrieveOrFindVariable(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariable(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION); // TODO pendiente confirmación seguridad
    }

    public static boolean canRetrieveOrFindVariableElement(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableElement(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION); // TODO pendiente confirmación seguridad
    }
}
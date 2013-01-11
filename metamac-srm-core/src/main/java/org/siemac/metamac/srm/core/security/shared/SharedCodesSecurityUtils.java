package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;

import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCodesSecurityUtils extends SharedSecurityUtils {

    //
    // NOTE: Only to related entities. Security about codelists and codes is in ItemSecurityUtils
    //

    public static boolean canRetrieveOrFindCodelistFamilyByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariableFamilyByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariableByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariable(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }
}
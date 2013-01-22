package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCodesSecurityUtils extends SharedSecurityUtils {

    //
    // NOTE: Only to related entities. Security about codelists and codes is in ItemSecurityUtils
    //

    public static boolean canRetrieveOrFindCodelistOrderVisualisation(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCodelistOrderVisualisation(MetamacPrincipal metamacPrincipal, ProcStatusEnum codelistProcStatus) {
        return SharedItemsSecurityUtils.canUpdateItemScheme(metamacPrincipal, codelistProcStatus);
    }

    public static boolean canRetrieveOrFindCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCodelistFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariableFamily(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableFamily(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariable(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariable(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindVariableElement(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudVariableElement(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
    }
}
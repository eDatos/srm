package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_PRODUCCION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_PRODUCCION;

import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedTasksSecurityUtils extends SharedSecurityUtils {

    public static boolean canFindTasksByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    //
    // Structure artifacts
    //

    public static boolean canImportStructure(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_PRODUCCION, JEFE_PRODUCCION, JEFE_NORMALIZACION);
    }

    public static boolean canExportStructure(MetamacPrincipal metamacPrincipal) {
        return isAnySrmRole(metamacPrincipal);
    }
}
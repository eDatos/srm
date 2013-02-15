package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.core.security.shared.SharedImportSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class ImportationClientSecurityUtils {

    public static boolean canImportStructure() {
        return SharedImportSecurityUtils.canImportStructure(MetamacSrmWeb.getCurrentUser());
    }
}

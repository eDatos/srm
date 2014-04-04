package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.security.shared.SharedTasksSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class TasksClientSecurityUtils {

    public static boolean canImportStructure() {
        return SharedTasksSecurityUtils.canImportStructure(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canExportStructure(String versionLogic) {
        return SharedTasksSecurityUtils.canExportStructure(MetamacSrmWeb.getCurrentUser()) && !VersionUtil.isTemporalVersion(versionLogic);
    }
}

package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.security.shared.SharedImportSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class ImportationClientSecurityUtils {

    public static boolean canImportStructure() {
        return SharedImportSecurityUtils.canImportStructure(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canExportStructure(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        // Resources in temporal version CANNOT be exported
        return SharedImportSecurityUtils.canExportStructure(MetamacSrmWeb.getCurrentUser()) && !VersionUtil.isTemporalVersion(dataStructureDefinitionMetamacDto.getVersionLogic());
    }
}

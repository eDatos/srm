package org.siemac.metamac.srm.web.client.code.utils;

import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class CommonUtils {

    private static LinkedHashMap<String, String> accessTypeHashMap = null;

    public static LinkedHashMap<String, String> getAccessTypeHashMap() {
        if (accessTypeHashMap == null) {
            accessTypeHashMap = new LinkedHashMap<String, String>();
            accessTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
            for (AccessTypeEnum a : AccessTypeEnum.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().accessTypeEnum() + a.getName());
                accessTypeHashMap.put(a.name(), value);
            }
        }
        return accessTypeHashMap;
    }

    public static String getAccessTypeName(AccessTypeEnum accessTypeEnum) {
        return accessTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().accessTypeEnum() + accessTypeEnum.name()) : null;
    }
}

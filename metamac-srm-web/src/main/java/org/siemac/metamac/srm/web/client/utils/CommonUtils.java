package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

public class CommonUtils {

    private static LinkedHashMap<String, String> versionTypeHashMap        = null;
    private static LinkedHashMap<String, String> typeRepresentationHashMap = null;

    public static String getProcStatusName(ProcStatusEnum procStatus) {
        return procStatus != null ? getCoreMessages().getString(getCoreMessages().procStatusEnum() + procStatus.getName()) : null;
    }

    public static LinkedHashMap<String, String> getVersionTypeHashMap() {
        if (versionTypeHashMap == null) {
            versionTypeHashMap = new LinkedHashMap<String, String>();
            versionTypeHashMap.put(new String(), new String());
            for (VersionTypeEnum v : VersionTypeEnum.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().versionTypeEnum() + v.getName());
                versionTypeHashMap.put(v.toString(), value);
            }
        }
        return versionTypeHashMap;
    }

    public static LinkedHashMap<String, String> getTypeRepresentationEnumHashMap() {
        if (typeRepresentationHashMap == null) {
            typeRepresentationHashMap = new LinkedHashMap<String, String>();
            typeRepresentationHashMap.put(new String(), new String());
            for (TypeRepresentationEnum r : TypeRepresentationEnum.values()) {
                String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRepresentationEnum() + r.getName());
                typeRepresentationHashMap.put(r.toString(), value);
            }
        }
        return typeRepresentationHashMap;
    }

    public static String getTypeRepresentationName(TypeRepresentationEnum typeRepresentationEnum) {
        return typeRepresentationEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().typeRepresentationEnum() + typeRepresentationEnum.name()) : null;
    }
}

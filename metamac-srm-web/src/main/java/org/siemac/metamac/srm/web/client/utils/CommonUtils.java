package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.BooleanItemEnum;
import org.siemac.metamac.web.common.client.MetamacWebCommon;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;

public class CommonUtils {

    private static LinkedHashMap<String, String> versionTypeHashMap        = null;
    private static LinkedHashMap<String, String> typeRepresentationHashMap = null;
    private static LinkedHashMap<String, String> booleanItemHashMap        = null;

    public static String getProcStatusName(ProcStatusEnum procStatus) {
        return procStatus != null ? getCoreMessages().getString(getCoreMessages().procStatusEnum() + procStatus.getName()) : null;
    }

    public static LinkedHashMap<String, String> getVersionTypeHashMap() {
        if (versionTypeHashMap == null) {
            versionTypeHashMap = new LinkedHashMap<String, String>();
            versionTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
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
            typeRepresentationHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
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

    // BOOLEAN

    public static LinkedHashMap<String, String> getBooleanHashMap() {
        if (booleanItemHashMap == null) {
            booleanItemHashMap = new LinkedHashMap<String, String>();
            booleanItemHashMap.put(BooleanItemEnum.NULL_VALUE.toString(), StringUtils.EMPTY);
            booleanItemHashMap.put(BooleanItemEnum.TRUE_VALUE.toString(), MetamacWebCommon.getConstants().yes());
            booleanItemHashMap.put(BooleanItemEnum.FALSE_VALUE.toString(), MetamacWebCommon.getConstants().no());
        }
        return booleanItemHashMap;
    }

    public static Boolean getBooleanValue(String booleanItemEnumValue) {
        if (booleanItemEnumValue != null) {
            BooleanItemEnum booleanItemEnum = BooleanItemEnum.valueOf(booleanItemEnumValue);
            if (BooleanItemEnum.TRUE_VALUE.equals(booleanItemEnum)) {
                return true;
            } else if (BooleanItemEnum.FALSE_VALUE.equals(booleanItemEnum)) {
                return false;
            }
        }
        return null;
    }

    public static BooleanItemEnum getBooleanItemEnum(Boolean booleanValue) {
        if (BooleanUtils.isTrue(booleanValue)) {
            return BooleanItemEnum.TRUE_VALUE;
        } else if (BooleanUtils.isFalse(booleanValue)) {
            return BooleanItemEnum.FALSE_VALUE;
        } else {
            return BooleanItemEnum.NULL_VALUE;
        }
    }

    public static String getBooleanName(Boolean booleanValue) {
        if (BooleanUtils.isTrue(booleanValue)) {
            return MetamacWebCommon.getConstants().yes();
        } else if (BooleanUtils.isFalse(booleanValue)) {
            return MetamacWebCommon.getConstants().no();
        } else {
            return StringUtils.EMPTY;
        }
    }

    // VALIDATION UTILS

    public static boolean isItemSchemePublished(ProcStatusEnum procStatus) {
        return ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus);
    }

    public static boolean isInitialVersion(String versionLogic) {
        return SdmxVersionUtils.PATTERN_XX_YYY_INITIAL_VERSION.equals(versionLogic);
    }

    public static boolean canCodeBeEdited(ProcStatusEnum procStatus, String versionLogic) {
        return !org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(procStatus) && org.siemac.metamac.srm.web.client.utils.CommonUtils.isInitialVersion(versionLogic);
    }
}

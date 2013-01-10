package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;

public class CommonUtils {

    private static LinkedHashMap<String, String> versionTypeHashMap        = null;
    private static LinkedHashMap<String, String> typeRepresentationHashMap = null;

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

    // RELATED RESOURCE UTILS

    public static LinkedHashMap<String, String> getRelatedResourceHashMap(List<RelatedResourceDto> relatedResourceDtos) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            hashMap.put(relatedResourceDto.getUrn(), getRelatedResourceName(relatedResourceDto));
        }
        return hashMap;
    }

    public static String getRelatedResourceName(RelatedResourceDto relatedResourceDto) {
        if (relatedResourceDto != null) {
            return CommonWebUtils.getElementName(relatedResourceDto.getCode(), relatedResourceDto.getTitle());
        } else {
            return StringUtils.EMPTY;
        }
    }
}

package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.BooleanItemEnum;
import org.siemac.metamac.srm.web.shared.utils.SharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxSrmSharedUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.MaintainableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.NameableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class CommonUtils {

    // LIFE CYCLE

    public static LinkedHashMap<String, String> getProcStatusHashMap() {
        LinkedHashMap<String, String> procStatusHashMap = new LinkedHashMap<String, String>();
        procStatusHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (ProcStatusEnum p : ProcStatusEnum.values()) {
            procStatusHashMap.put(p.name(), getProcStatusName(p));
        }
        return procStatusHashMap;
    }

    public static String getProcStatusName(ProcStatusEnum procStatus) {
        return procStatus != null ? getCoreMessages().getString(getCoreMessages().procStatusEnum() + procStatus.getName()) : null;
    }

    public static LinkedHashMap<String, String> getVersionTypeHashMap() {
        LinkedHashMap<String, String> versionTypeHashMap = new LinkedHashMap<String, String>();
        versionTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (VersionTypeEnum v : VersionTypeEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().versionTypeEnum() + v.getName());
            versionTypeHashMap.put(v.toString(), value);
        }
        return versionTypeHashMap;
    }

    // REPRESENTATION

    public static LinkedHashMap<String, String> getTypeRepresentationEnumHashMap() {
        LinkedHashMap<String, String> typeRepresentationHashMap = new LinkedHashMap<String, String>();
        typeRepresentationHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (RepresentationTypeEnum r : RepresentationTypeEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().representationTypeEnum() + r.getName());
            typeRepresentationHashMap.put(r.toString(), value);
        }
        return typeRepresentationHashMap;
    }

    public static String getTypeRepresentationName(RepresentationTypeEnum typeRepresentationEnum) {
        return typeRepresentationEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().representationTypeEnum() + typeRepresentationEnum.name()) : null;
    }

    // BOOLEAN

    public static LinkedHashMap<String, String> getBooleanHashMap() {
        LinkedHashMap<String, String> booleanItemHashMap = new LinkedHashMap<String, String>();
        booleanItemHashMap.put(BooleanItemEnum.NULL_VALUE.toString(), StringUtils.EMPTY);
        booleanItemHashMap.put(BooleanItemEnum.TRUE_VALUE.toString(), MetamacWebCommon.getConstants().yes());
        booleanItemHashMap.put(BooleanItemEnum.FALSE_VALUE.toString(), MetamacWebCommon.getConstants().no());
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

    public static boolean isMaintainableArtefactPublished(ProcStatusEnum procStatus) {
        return ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus);
    }

    public static boolean isMaintainableArtefactInProductionValidationOrGreaterProcStatus(ProcStatusEnum procStatus) {
        return ProcStatusEnum.PRODUCTION_VALIDATION.equals(procStatus) || ProcStatusEnum.DIFFUSION_VALIDATION.equals(procStatus) || ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus)
                || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus);
    }

    public static boolean isInitialVersion(String versionLogic) {
        return SdmxVersionUtils.PATTERN_XX_YYY_INITIAL_VERSION.equals(versionLogic);
    }

    public static boolean canCodeBeEdited(ProcStatusEnum procStatus, String versionLogic) {
        return !org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(procStatus) && org.siemac.metamac.srm.web.client.utils.CommonUtils.isInitialVersion(versionLogic);
    }

    // FORMAT UTILS

    public static String getResourceTitle(NameableArtefactDto nameableArtefactDto) {
        String defaultLocalisedString = InternationalStringUtils.getLocalisedString(nameableArtefactDto.getName());
        return defaultLocalisedString != null ? defaultLocalisedString : StringUtils.EMPTY;
    }

    // MAINTAINER UTILS

    private static boolean isDefaultMaintainer(String maintainerUrn) {
        return StringUtils.equals(maintainerUrn, MetamacSrmWeb.getDefaultMaintainer().getUrn());
    }

    public static boolean isDefaultMaintainer(RelatedResourceDto maintainer) {
        if (maintainer != null) {
            return isDefaultMaintainer(maintainer.getUrn());
        }
        return false;
    }

    public static boolean hasDefaultMaintainerOrIsAgencySchemeSdmxResource(MaintainableArtefactDto maintainableArtefactDto) {
        return hasDefaultMaintainerOrIsAgencySchemeSdmxResource(maintainableArtefactDto.getUrn(), maintainableArtefactDto.getMaintainer());
    }

    public static boolean hasDefaultMaintainerOrIsAgencySchemeSdmxResource(String urn, RelatedResourceDto maintainer) {
        if (SdmxSrmSharedUtils.isAgencySchemeSdmx(urn)) {
            return true;
        }
        return isDefaultMaintainer(maintainer);
    }

    /**
     * Returns true if the artefactDto belongs to the default maintainer, and the version is not a temporal one
     * 
     * @param artefactDto
     * @return
     */
    public static boolean canSdmxMetadataAndStructureBeModified(MaintainableArtefactDto artefactDto) {
        return canSdmxMetadataAndStructureBeModified(artefactDto.getUrn(), artefactDto.getMaintainer(), artefactDto.getVersionLogic());
    }

    public static boolean canSdmxMetadataAndStructureBeModified(String urn, RelatedResourceDto maintainer, String versionLogic) {
        return hasDefaultMaintainerOrIsAgencySchemeSdmxResource(urn, maintainer) && !VersionUtil.isTemporalVersion(versionLogic);
    }

    // EXPORTATION UTILS

    public static void downloadFile(String fileName) {
        // TODO Is it better to use com.google.gwt.http.client.RequestBuilder to send the request?
        StringBuffer url = new StringBuffer();
        url.append(URL.encode(MetamacSrmWeb.getRelativeURL(SharedTokens.FILE_DOWNLOAD_DIR_PATH)));
        url.append("?").append(URL.encode(SharedTokens.PARAM_FILE_NAME)).append("=").append(URL.encode(fileName));
        Window.open(url.toString(), "_blank", "");
    }
}

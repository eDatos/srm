package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxSrmSharedUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.MaintainableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.NameableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.google.gwt.http.client.URL;

public class CommonUtils {

    // EXPORT

    public static LinkedHashMap<String, String> getStructureParameterDetailTypeHashMap() {
        LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
        typeMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (ExportDetailEnum type : ExportDetailEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().structureParameterDetailEnum() + type.name());
            typeMap.put(type.name(), value);
        }
        return typeMap;
    }

    public static LinkedHashMap<String, String> getStructureParameterReferencesTypeHashMap() {
        LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
        typeMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (ExportReferencesEnum type : ExportReferencesEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().structureParameterReferencesEnum() + type.name());
            typeMap.put(type.name(), value);
        }
        return typeMap;
    }

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
        StringBuffer url = new StringBuffer();
        url.append(URL.encode(MetamacSrmWeb.getRelativeURL(SrmSharedTokens.FILE_DOWNLOAD_DIR_PATH)));
        url.append("?").append(URL.encode(SrmSharedTokens.PARAM_FILE_NAME)).append("=").append(URL.encode(fileName));
        downloadUrl(url.toString());
    }

    private static native void downloadUrl(String url) /*-{
		$wnd.location = url;
    }-*/;

}

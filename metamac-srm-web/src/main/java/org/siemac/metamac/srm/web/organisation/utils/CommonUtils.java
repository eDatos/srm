package org.siemac.metamac.srm.web.organisation.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class CommonUtils {

    private static LinkedHashMap<String, String> organisationSchemeTypeHashMap = null;
    private static LinkedHashMap<String, String> organisationTypeHashMap       = null;

    public static LinkedHashMap<String, String> getOrganisationSchemeTypeHashMap() {
        if (organisationSchemeTypeHashMap == null) {
            organisationSchemeTypeHashMap = new LinkedHashMap<String, String>();
            for (OrganisationSchemeTypeEnum type : OrganisationSchemeTypeEnum.values()) {
                organisationSchemeTypeHashMap.put(type.name(), getOrganisationSchemeTypeName(type));
            }
        }
        return organisationSchemeTypeHashMap;
    }

    public static String getOrganisationSchemeTypeName(OrganisationSchemeTypeEnum type) {
        return type != null ? getCoreMessages().getString(getCoreMessages().organisationSchemeTypeEnum() + type.name()) : null;
    }

    public static LinkedHashMap<String, String> getOrganisationTypeHashMap() {
        if (organisationTypeHashMap == null) {
            organisationTypeHashMap = new LinkedHashMap<String, String>();
            for (OrganisationTypeEnum type : OrganisationTypeEnum.values()) {
                organisationTypeHashMap.put(type.name(), getOrganisationTypeName(type));
            }
        }
        return organisationTypeHashMap;
    }

    public static String getOrganisationTypeName(OrganisationTypeEnum type) {
        return type != null ? getCoreMessages().getString(getCoreMessages().organisationTypeEnum() + type.name()) : null;
    }

}

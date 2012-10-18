package org.siemac.metamac.srm.web.organisation.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.web.common.client.utils.UrnUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class CommonUtils {

    //
    // HASH MAPS
    //

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

    //
    // URNs
    //

    public static String generateOrganisationSchemeUrn(String identifier, OrganisationSchemeTypeEnum type) {
        String urn = null;
        switch (type) {
            case AGENCY_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_AGENCYSCHEME_PREFIX, identifier);
                break;
            case ORGANISATION_UNIT_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_ORGANISATIONUNITSCHEME_PREFIX, identifier);
                break;
            case DATA_PROVIDER_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATAPROVIDERSCHEME_PREFIX, identifier);
                break;
            case DATA_CONSUMER_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATACONSUMERSCHEME_PREFIX, identifier);
                break;
            default:
                break;
        }
        return urn;
    }

    public static String generateOrganisationUrn(String schemeIdentifier, OrganisationSchemeTypeEnum schemeType, String organisationCode) {
        String urn = null;
        switch (schemeType) {
            case AGENCY_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_AGENCY_PREFIX, schemeIdentifier, organisationCode);
                break;
            case ORGANISATION_UNIT_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_ORGANISATIONUNIT_PREFIX, schemeIdentifier, organisationCode);
                break;
            case DATA_PROVIDER_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATAPROVIDER_PREFIX, schemeIdentifier, organisationCode);
                break;
            case DATA_CONSUMER_SCHEME:
                urn = UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATACONSUMER_PREFIX, schemeIdentifier, organisationCode);
                break;
            default:
                break;
        }
        return urn;
    }

    public static OrganisationTypeEnum getOrganisationTypeEnum(OrganisationSchemeTypeEnum organisationSchemeType) {
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeType)) {
            return OrganisationTypeEnum.AGENCY;
        } else if (OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME.equals(organisationSchemeType)) {
            return OrganisationTypeEnum.DATA_CONSUMER;
        } else if (OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME.equals(organisationSchemeType)) {
            return OrganisationTypeEnum.DATA_PROVIDER;
        } else if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeType)) {
            return OrganisationTypeEnum.ORGANISATION_UNIT;
        }
        return null;
    }

    public static OrganisationSchemeTypeEnum getOrganisationSchemeTypeEnum(OrganisationTypeEnum organisationType) {
        if (OrganisationTypeEnum.AGENCY.equals(organisationType)) {
            return OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        } else if (OrganisationTypeEnum.DATA_CONSUMER.equals(organisationType)) {
            return OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME;
        } else if (OrganisationTypeEnum.DATA_PROVIDER.equals(organisationType)) {
            return OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;
        } else if (OrganisationTypeEnum.ORGANISATION_UNIT.equals(organisationType)) {
            return OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME;
        }
        return null;
    }

}

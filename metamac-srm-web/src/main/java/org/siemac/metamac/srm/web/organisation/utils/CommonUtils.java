package org.siemac.metamac.srm.web.organisation.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class CommonUtils {

    //
    // HASH MAPS
    //

    public static LinkedHashMap<String, String> getOrganisationSchemeTypeHashMap() {
        LinkedHashMap<String, String> organisationSchemeTypeHashMap = new LinkedHashMap<String, String>();
        for (OrganisationSchemeTypeEnum type : OrganisationSchemeTypeEnum.values()) {
            organisationSchemeTypeHashMap.put(type.name(), getOrganisationSchemeTypeName(type));
        }
        return organisationSchemeTypeHashMap;
    }

    public static String getOrganisationSchemeTypeName(OrganisationSchemeTypeEnum type) {
        return type != null ? getCoreMessages().getString(getCoreMessages().organisationSchemeTypeEnum() + type.name()) : null;
    }

    public static LinkedHashMap<String, String> getOrganisationTypeHashMap() {
        LinkedHashMap<String, String> organisationTypeHashMap = new LinkedHashMap<String, String>();
        organisationTypeHashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (OrganisationTypeEnum type : OrganisationTypeEnum.values()) {
            organisationTypeHashMap.put(type.name(), getOrganisationTypeName(type));
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

    public static boolean isDataConsumenScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        return isDataConsumerScheme(organisationSchemeMetamacDto.getType());
    }

    public static boolean isDataConsumerScheme(OrganisationSchemeTypeEnum type) {
        return OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME.equals(type);
    }

    public static boolean isDataProviderScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        return isDataProviderScheme(organisationSchemeMetamacDto.getType());
    }

    public static boolean isDataProviderScheme(OrganisationSchemeTypeEnum type) {
        return OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME.equals(type);
    }

    public static boolean isAgencyScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        return isAgencyScheme(organisationSchemeMetamacDto.getType());
    }

    public static boolean isAgencyScheme(OrganisationSchemeTypeEnum type) {
        return OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(type);
    }

    public static boolean isOrganisationUnitScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        return isOrganisationUnitScheme(organisationSchemeMetamacDto.getType());
    }

    public static boolean isOrganisationUnitScheme(OrganisationSchemeTypeEnum type) {
        return OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(type);
    }
}

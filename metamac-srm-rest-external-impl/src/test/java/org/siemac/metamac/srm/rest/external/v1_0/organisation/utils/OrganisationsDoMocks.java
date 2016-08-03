package org.siemac.metamac.srm.rest.external.v1_0.organisation.utils;

import static org.junit.Assert.fail;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsDoMocks {

    public static OrganisationSchemeVersionMetamac mockOrganisationScheme(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) throws MetamacException {
        return OrganisationsMetamacDoMocks.mockOrganisationSchemeFixedValues(agencyID, resourceID, version, type);
    }

    public static OrganisationMetamac mockOrganisation(String resourceID, OrganisationSchemeVersionMetamac organisationScheme, OrganisationMetamac parent, OrganisationTypeEnum type)
            throws MetamacException {
        Boolean organisationsCanHaveChildren = OrganisationTypeEnum.ORGANISATION_UNIT.equals(type);
        if (!organisationsCanHaveChildren && parent != null) {
            fail("parent must be null because this organisation type can not have children");
        }
        return OrganisationsMetamacDoMocks.mockOrganisationFixedValues(resourceID, organisationScheme, parent, type);
    }

    public static OrganisationSchemeVersionMetamac mockOrganisationSchemeWithOrganisations(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) throws MetamacException {

        OrganisationSchemeVersionMetamac organisationSchemeVersion = mockOrganisationScheme(agencyID, resourceID, version, type);

        // organisations
        Boolean organisationsCanHaveChildren = OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(type);
        OrganisationTypeEnum organisationType = guessOrganisationTypeEnum(type);
        OrganisationMetamac organisation1 = mockOrganisation("organisation1", organisationSchemeVersion, null, organisationType);
        OrganisationMetamac organisation2 = mockOrganisation("organisation2", organisationSchemeVersion, null, organisationType);

        // organisations hierarchy
        organisationSchemeVersion.addItem(organisation1);
        organisationSchemeVersion.addItemsFirstLevel(organisation1);
        organisationSchemeVersion.addItem(organisation2);
        organisationSchemeVersion.addItemsFirstLevel(organisation2);

        // children
        if (organisationsCanHaveChildren) {
            OrganisationMetamac organisation2A = mockOrganisation("organisation2A", organisationSchemeVersion, organisation2, organisationType);
            OrganisationMetamac organisation2B = mockOrganisation("organisation2B", organisationSchemeVersion, organisation2, organisationType);
            organisationSchemeVersion.addItem(organisation2A);
            organisationSchemeVersion.addItem(organisation2B);
            organisation2.addChildren(organisation2A);
            organisation2.addChildren(organisation2B);
        }

        return organisationSchemeVersion;
    }

    public static ItemResult mockOrganisationItemResult(String resourceID, ItemResult parent, OrganisationTypeEnum organisationType) {
        ItemResult itemResult = OrganisationsMetamacDoMocks.mockOrganisationResultFixedValues(resourceID, parent, organisationType);
        itemResult.setUriProvider(null);
        itemResult.setUrn(itemResult.getUrn().replace("urn:sdmx:org.sdmx.infomodel.xxx=", "urn:sdmx:org.sdmx.infomodel.base.Organisation=agency1:itemScheme1(01.000)."));
        itemResult.setUrnProvider(itemResult.getUrn());
        return itemResult;
    }

    private static OrganisationTypeEnum guessOrganisationTypeEnum(OrganisationSchemeTypeEnum schemeType) {
        switch (schemeType) {
            case AGENCY_SCHEME:
                return OrganisationTypeEnum.AGENCY;
            case ORGANISATION_UNIT_SCHEME:
                return OrganisationTypeEnum.ORGANISATION_UNIT;
            case DATA_CONSUMER_SCHEME:
                return OrganisationTypeEnum.DATA_CONSUMER;
            case DATA_PROVIDER_SCHEME:
                return OrganisationTypeEnum.DATA_PROVIDER;
            default:
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + schemeType);
        }
    }
}
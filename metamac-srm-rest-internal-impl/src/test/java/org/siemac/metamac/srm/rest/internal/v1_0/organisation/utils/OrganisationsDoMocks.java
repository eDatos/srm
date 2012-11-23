package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.DoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsDoMocks extends DoMocks {

    public static OrganisationSchemeVersionMetamac mockOrganisationScheme(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {
        OrganisationSchemeVersionMetamac target = OrganisationsMetamacDoMocks.mockOrganisationScheme(type, mockMaintainer(agencyID));
        OrganisationsMetamacDoMocks.fillOrganisationSchemeAutogeneratedMetadata(target);
        fillMaintainableArtefactWithInmutableValues(agencyID, resourceID, version, target.getMaintainableArtefact());
        return target;
    }

    public static OrganisationSchemeVersionMetamac mockOrganisationSchemeWithOrganisations(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {

        OrganisationSchemeVersionMetamac organisationSchemeVersion = mockOrganisationScheme(agencyID, resourceID, version, type);

        // organisations
        OrganisationTypeEnum organisationType = guessOrganisationTypeEnum(type);
        OrganisationMetamac organisation1 = mockOrganisation("organisation1", organisationSchemeVersion, null, organisationType);
        OrganisationMetamac organisation2 = mockOrganisation("organisation2", organisationSchemeVersion, null, organisationType);
        OrganisationMetamac organisation2A = mockOrganisation("organisation2A", organisationSchemeVersion, organisation2, organisationType);
        OrganisationMetamac organisation2B = mockOrganisation("organisation2B", organisationSchemeVersion, organisation2, organisationType);

        // organisations hierarchy
        organisationSchemeVersion.addItem(organisation1);
        organisationSchemeVersion.addItemsFirstLevel(organisation1);
        organisationSchemeVersion.addItem(organisation2);
        organisationSchemeVersion.addItemsFirstLevel(organisation2);
        organisationSchemeVersion.addItem(organisation2A);
        organisationSchemeVersion.addItem(organisation2B);
        organisation2.addChildren(organisation2A);
        organisation2.addChildren(organisation2B);

        return organisationSchemeVersion;
    }
    
    public static OrganisationMetamac mockOrganisation(String resourceID, ItemSchemeVersion itemSchemeVersion, OrganisationMetamac parent, OrganisationTypeEnum type) {

        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(type);
        OrganisationsMetamacDoMocks.fillOrganisationAutogeneratedMetadata(organisation);

        // relations
        organisation.setParent(parent);
        organisation.setItemSchemeVersion(itemSchemeVersion);

        // metadata
        fillNameableArtefactWithInmutableValues(resourceID, organisation.getNameableArtefact()); // immutable values to test xml streams
        return organisation;
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
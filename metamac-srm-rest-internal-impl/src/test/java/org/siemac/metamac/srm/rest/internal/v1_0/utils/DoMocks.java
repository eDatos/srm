package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class DoMocks {

    public static InternationalString mockInternationalString(String metadata, String subCode) {
        String subTitle = subCode != null ? metadata + "-" + subCode : metadata;
        return mockInternationalString("es", subTitle + " en Español", "en", subTitle + " in English");
    }

    public static OrganisationMetamac mockMaintainer(String agencyID) {
        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.AGENCY);
        organisation.getNameableArtefact().setCode(agencyID);
        OrganisationsMetamacDoMocks.fillOrganisationAutogeneratedMetadata(organisation);
        return organisation;
    }

    public static void fillMaintainableArtefactWithInmutableValues(String agencyID, String resourceID, String version, MaintainableArtefact maintainableArtefact) {
        maintainableArtefact.setCode(resourceID);
        maintainableArtefact.setUrn("urn:" + resourceID + ":" + version);
        maintainableArtefact.setUrnProvider(maintainableArtefact.getUrn());
        maintainableArtefact.setName(mockInternationalString("name", resourceID + "v" + version));
        maintainableArtefact.setDescription(mockInternationalString("description", resourceID + "v" + version));
        maintainableArtefact.setVersionLogic(version);
    }

    public static void fillNameableArtefactWithInmutableValues(String resourceID, NameableArtefact nameableArtefact) {
        nameableArtefact.setUrn("urn:" + resourceID);
        nameableArtefact.setUrnProvider(nameableArtefact.getUrn());
        nameableArtefact.setCode(resourceID);
        nameableArtefact.setName(mockInternationalString("name", resourceID));
        nameableArtefact.setDescription(mockInternationalString("description", resourceID));
    }

    private static InternationalString mockInternationalString(String locale1, String label1, String locale2, String label2) {

        InternationalString internationalString = new InternationalString();

        LocalisedString internationalStringLocale1 = new LocalisedString();
        internationalStringLocale1.setLocale(locale1);
        internationalStringLocale1.setLabel(label1);
        internationalString.addText(internationalStringLocale1);

        LocalisedString internationalStringLocale2 = new LocalisedString();
        internationalStringLocale2.setLocale(locale2);
        internationalStringLocale2.setLabel(label2);
        internationalString.addText(internationalStringLocale2);

        return internationalString;
    }
}
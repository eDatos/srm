package org.siemac.metamac.srm.core.organisation.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

public class OrganisationsDoCopyUtils {

    /**
     * Create a new OrganisationSchemeVersion copying values from a source
     */
    public static OrganisationSchemeVersionMetamac copyOrganisationSchemeVersionMetamac(OrganisationSchemeVersionMetamac source) {

        OrganisationSchemeVersionMetamac target = new OrganisationSchemeVersionMetamac(source.getOrganisationSchemeType());

        // Common metadata of OrganisationSchemeVersion
        com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsDoCopyUtils.copyOrganisationSchemeVersion(source, target);

        return target;
    }

    /**
     * Copy children hierarchy from a {@link OrganisationSchemeVersionMetamac}
     */
    public static List<OrganisationMetamac> copyOrganisationsMetamac(OrganisationSchemeVersionMetamac organisationSchemeVersion) {
        List<Item> sources = organisationSchemeVersion.getItemsFirstLevel();
        List<OrganisationMetamac> targets = new ArrayList<OrganisationMetamac>();
        for (Item source : sources) {
            OrganisationMetamac target = copyOrganisationMetamacWithChildren((OrganisationMetamac) source);
            targets.add(target);
        }
        return targets;
    }

    /**
     * Create a new {@link Organisation} copying values from a source with children
     */
    private static OrganisationMetamac copyOrganisationMetamacWithChildren(OrganisationMetamac source) {

        OrganisationMetamac target = copyOrganisationMetamac(source);

        // Children
        for (Item childrenSource : source.getChildren()) {
            OrganisationMetamac childrenTarget = copyOrganisationMetamacWithChildren((OrganisationMetamac) childrenSource);
            childrenTarget.setParent(target);
            target.addChildren(childrenTarget);
        }

        return target;
    }

    /**
     * Create a new {@link OrganisationMetamac} copying values from a source. Do not copy children
     */
    private static OrganisationMetamac copyOrganisationMetamac(OrganisationMetamac source) {
        OrganisationMetamac target = new OrganisationMetamac(source.getOrganisationType());

        // Common metadata of Organisation
        com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsDoCopyUtils.copyOrganisation(source, target);

        // Metamac metadata
        // nothing

        return target;
    }
}
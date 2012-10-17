package org.siemac.metamac.srm.core.organisation.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsDoCopyUtils.OrganisationCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("organisationCopyCallbackMetamac")
public class OrganisationCopyCallbackMetamacImpl implements OrganisationCopyCallback {

    @Override
    public OrganisationSchemeVersion copyOrganisationSchemeVersion(OrganisationSchemeVersion source) {
        return copyOrganisationSchemeVersion((OrganisationSchemeVersionMetamac) source);
    }

    private OrganisationSchemeVersionMetamac copyOrganisationSchemeVersion(OrganisationSchemeVersionMetamac source) {
        OrganisationSchemeVersionMetamac target = new OrganisationSchemeVersionMetamac(source.getOrganisationSchemeType());
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        return target;
    }

    @Override
    public Organisation copyOrganisation(Organisation source) {
        return copyOrganisation((OrganisationMetamac) source);
    }

    private OrganisationMetamac copyOrganisation(OrganisationMetamac source) {
        OrganisationMetamac target = new OrganisationMetamac(source.getOrganisationType());
        return target;
    }
}
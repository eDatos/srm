package org.siemac.metamac.srm.core.organisation.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.OrganisationsService;

/**
 * Implementation of OrganisationsMetamacService.
 */
@Service("organisationsMetamacService")
public class OrganisationsMetamacServiceImpl extends OrganisationsMetamacServiceImplBase {

    @Autowired
    private OrganisationsService organisationsService;

    public OrganisationsMetamacServiceImpl() {
    }

    public OrganisationSchemeVersionMetamac retrieveOrganisationSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (OrganisationSchemeVersionMetamac) organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
    }
}
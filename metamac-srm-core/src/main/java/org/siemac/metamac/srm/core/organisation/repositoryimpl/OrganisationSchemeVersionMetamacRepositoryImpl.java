package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for OrganisationSchemeVersionMetamac
 */
@Repository("organisationSchemeVersionMetamacRepository")
public class OrganisationSchemeVersionMetamacRepositoryImpl extends OrganisationSchemeVersionMetamacRepositoryBase {

    public OrganisationSchemeVersionMetamacRepositoryImpl() {
    }

    public OrganisationSchemeMetamac findByUrn(String urn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByUrn not implemented");

    }

    public OrganisationSchemeMetamac retrieveOrganisationSchemeByProcStatus(String urn, ProcStatusEnum[] procStatus) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("retrieveOrganisationSchemeByProcStatus not implemented");

    }
}

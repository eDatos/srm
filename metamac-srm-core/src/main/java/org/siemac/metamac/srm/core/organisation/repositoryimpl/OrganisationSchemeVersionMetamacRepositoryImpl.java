package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for OrganisationSchemeVersionMetamac
 */
@Repository("organisationSchemeVersionMetamacRepository")
public class OrganisationSchemeVersionMetamacRepositoryImpl
    extends OrganisationSchemeVersionMetamacRepositoryBase {
    public OrganisationSchemeVersionMetamacRepositoryImpl() {
    }

    public OrganisationSchemeVersionMetamac findByUrn(String urn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByUrn not implemented");

    }

    public OrganisationSchemeVersionMetamac retrieveOrganisationSchemeVersionByProcStatus(
        String urn, ProcStatusEnum[] procStatus) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "retrieveOrganisationSchemeVersionByProcStatus not implemented");

    }

    public OrganisationSchemeVersionMetamac findByOrganisation(String urn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findByOrganisation not implemented");

    }
}

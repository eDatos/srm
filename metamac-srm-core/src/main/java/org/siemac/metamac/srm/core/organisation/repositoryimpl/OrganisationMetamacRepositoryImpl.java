package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for OrganisationMetamac
 */
@Repository("organisationMetamacRepository")
public class OrganisationMetamacRepositoryImpl extends OrganisationMetamacRepositoryBase {

    public OrganisationMetamacRepositoryImpl() {
    }

    public OrganisationMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<OrganisationMetamac> result = findByQuery("from OrganisationMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

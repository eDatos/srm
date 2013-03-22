package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;

/**
 * Repository implementation for OrganisationMetamac
 */
@Repository("organisationMetamacRepository")
public class OrganisationMetamacRepositoryImpl extends OrganisationMetamacRepositoryBase {

    @Autowired
    private OrganisationRepository organisationRepository;

    public OrganisationMetamacRepositoryImpl() {
    }

    @Override
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

    @Override
    public void checkOrganisationTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        organisationRepository.checkOrganisationTranslations(itemSchemeVersionId, locale, exceptionItems);
        // no metadata specific in metamac
    }

}

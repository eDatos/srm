package org.siemac.metamac.srm.core.organisation.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;

/**
 * Repository implementation for OrganisationMetamac
 */
@Repository("organisationMetamacRepository")
public class OrganisationMetamacRepositoryImpl extends OrganisationMetamacRepositoryBase {

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private ItemRepository         itemRepository;

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
    public List<ItemResult> findOrganisationsByOrganisationSchemeUnordered(Long itemSchemeVersionId, ItemMetamacResultSelection resultSelection) {
        // Find items
        List<ItemResult> items = organisationRepository.findOrganisationsByOrganisationSchemeUnordered(itemSchemeVersionId, resultSelection);
        // no extension point
        return items;
    }

    @Override
    public List<ItemVisualisationResult> findOrganisationsByOrganisationSchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {
        return itemRepository.findItemsByItemSchemeUnorderedToVisualisation(itemSchemeVersionId, locale);
    }

    @Override
    public void checkOrganisationTranslations(Long itemSchemeVersionId, String locale, List<MetamacExceptionItem> exceptionItems) {
        organisationRepository.checkOrganisationTranslations(itemSchemeVersionId, locale, exceptionItems);
        // no metadata specific in metamac
    }

}
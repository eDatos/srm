package org.siemac.metamac.srm.core.concept.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for ConceptMetamac
 */
@Repository("conceptMetamacRepository")
public class ConceptMetamacRepositoryImpl extends ConceptMetamacRepositoryBase {

    public ConceptMetamacRepositoryImpl() {
    }

    @Override
    public ConceptMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptMetamac> result = findByQuery("from ConceptMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }
}
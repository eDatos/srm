package org.siemac.metamac.srm.core.concept.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for ConceptSchemeVersionMetamac
 */
@Repository("conceptSchemeVersionMetamacRepository")
public class ConceptSchemeVersionMetamacRepositoryImpl extends ConceptSchemeVersionMetamacRepositoryBase {

    public ConceptSchemeVersionMetamacRepositoryImpl() {
    }

    public ConceptSchemeVersionMetamac findByConcept(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptSchemeVersionMetamac> result = findByQuery("select cs from ConceptSchemeVersionMetamac cs join cs.items as i where i.nameableArtefact.urn = :urn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }
}

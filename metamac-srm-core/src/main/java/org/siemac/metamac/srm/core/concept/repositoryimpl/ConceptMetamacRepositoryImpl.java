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

    public ConceptMetamac findByCodeInConceptSchemeVersion(String conceptCode, String conceptSchemeUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("conceptCode", conceptCode);
        parameters.put("conceptSchemeUrn", conceptSchemeUrn);
        List<ConceptMetamac> result = findByQuery("from ConceptMetamac c where c.nameableArtefact.code = :conceptCode and c.itemSchemeVersion.maintainableArtefact.urn = :conceptSchemeUrn", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

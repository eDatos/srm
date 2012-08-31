package org.siemac.metamac.srm.core.concept.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.concept.domain.ConceptRelation;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for ConceptRelation
 */
@Repository("conceptRelationRepository")
public class ConceptRelationRepositoryImpl extends ConceptRelationRepositoryBase {

    public ConceptRelationRepositoryImpl() {
    }

    @Override
    public ConceptRelation find(String urn1, String urn2) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn1", urn1);
        parameters.put("urn2", urn2);
        List<ConceptRelation> result = findByQuery(
                "from ConceptRelation where (concept1.nameableArtefact.urn = :urn1 and concept2.nameableArtefact.urn = :urn2) or (concept1.nameableArtefact.urn = :urn2 and concept2.nameableArtefact.urn = :urn1)",
                parameters);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    public List<ConceptRelation> findByConcept(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptRelation> result = findByQuery("from ConceptRelation where concept1.nameableArtefact.urn = :urn or concept2.nameableArtefact.urn = :urn", parameters);
        return result;
    }

    public List<ConceptRelation> findByConceptSchemeVersion(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<ConceptRelation> result = findByQuery("from ConceptRelation where concept1.itemSchemeVersion.maintainableArtefact.urn= :urn", parameters); // note: itemScheme is the same to two concepts
        return result;
    }
}

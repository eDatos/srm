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
    
    @Override
    public ConceptMetamac findOneConceptByConceptExtends(Long idConceptExtends) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", idConceptExtends);
        List<ConceptMetamac> result = findByQuery("from ConceptMetamac where conceptExtends.id = :id", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public ConceptMetamac findOneConceptWithConceptExtendsOfConceptSchemeVersion(String conceptSchemeUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("conceptSchemeUrn", conceptSchemeUrn);
        List<ConceptMetamac> result = findByQuery("from ConceptMetamac where conceptExtends.itemSchemeVersion.maintainableArtefact.urn = :conceptSchemeUrn", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public ConceptMetamac findOneConceptExtendsConceptInConceptSchemeNotFinal(String conceptSchemeUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("conceptSchemeUrn", conceptSchemeUrn);
        List<ConceptMetamac> result = findByQuery(
                "from ConceptMetamac where itemSchemeVersion.maintainableArtefact.urn = :conceptSchemeUrn and conceptExtends.itemSchemeVersion.maintainableArtefact.finalLogic is false", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public ConceptMetamac findOneConceptExtendsConceptInConceptSchemeNotValid(String conceptSchemeUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("conceptSchemeUrn", conceptSchemeUrn);
        List<ConceptMetamac> result = findByQuery(
                "from ConceptMetamac where itemSchemeVersion.maintainableArtefact.urn = :conceptSchemeUrn and conceptExtends.itemSchemeVersion.maintainableArtefact.validFrom is null", parameters, 1);
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }
}

package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodelistOpennessVisualisation
 */
@Repository("codelistOpennessVisualisationRepository")
public class CodelistOpennessVisualisationRepositoryImpl extends CodelistOpennessVisualisationRepositoryBase {

    public CodelistOpennessVisualisationRepositoryImpl() {
    }

    @Override
    public CodelistOpennessVisualisation findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistOpennessVisualisation> result = findByQuery("from CodelistOpennessVisualisation where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

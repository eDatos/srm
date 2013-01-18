package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodelistOrderVisualisation
 */
@Repository("codelistOrderVisualisationRepository")
public class CodelistOrderVisualisationRepositoryImpl extends CodelistOrderVisualisationRepositoryBase {

    public CodelistOrderVisualisationRepositoryImpl() {
    }

    @Override
    public CodelistOrderVisualisation findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistOrderVisualisation> result = findByQuery("from CodelistOrderVisualisation where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

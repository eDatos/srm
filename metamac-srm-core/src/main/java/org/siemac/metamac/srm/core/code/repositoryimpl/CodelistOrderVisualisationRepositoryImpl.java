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
    public CodelistOrderVisualisation findByIdentifier(String codelistUrn, String identifier) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("identifier", identifier);
        parameters.put("codelistUrn", codelistUrn);
        List<CodelistOrderVisualisation> result = findByQuery("from CodelistOrderVisualisation where codelistVersion.maintainableArtefact.urn = :codelistUrn and identifier = :identifier", parameters,
                1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

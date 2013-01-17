package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.CodeOrderVisualisation;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodeOrderVisualisation
 */
@Repository("codeOrderVisualisationRepository")
public class CodeOrderVisualisationRepositoryImpl extends CodeOrderVisualisationRepositoryBase {

    public CodeOrderVisualisationRepositoryImpl() {
    }

    @Override
    public List<CodeOrderVisualisation> findByCodeUrn(String codeUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("codeUrn", codeUrn);
        List<CodeOrderVisualisation> result = findByQuery("from CodeOrderVisualisation where code.nameableArtefact.urn = :codeUrn", parameters);
        return result;
    }
}

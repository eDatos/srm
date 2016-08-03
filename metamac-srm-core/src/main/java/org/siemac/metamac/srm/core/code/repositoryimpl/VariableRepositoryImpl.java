package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.Variable;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for Variable
 */
@Repository("variableRepository")
public class VariableRepositoryImpl extends VariableRepositoryBase {

    public VariableRepositoryImpl() {
    }

    @Override
    public Variable findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<Variable> result = findByQuery("from Variable where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

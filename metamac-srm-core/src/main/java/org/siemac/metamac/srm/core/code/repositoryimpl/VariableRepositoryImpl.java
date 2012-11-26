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

    public Variable findByIdentifier(String identifier) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("identifier", identifier);
        List<Variable> result = findByQuery("from Variable where identifier = :identifier", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

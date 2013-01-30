package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for VariableElementOperation
 */
@Repository("variableElementOperationRepository")
public class VariableElementOperationRepositoryImpl extends VariableElementOperationRepositoryBase {

    public VariableElementOperationRepositoryImpl() {
    }

    @Override
    public VariableElementOperation findByCode(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", code);
        List<VariableElementOperation> result = findByQuery("from VariableElementOperation where code = :code", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<VariableElementOperation> findByVariableElementUrn(String variableElementUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("variableElementUrn", variableElementUrn);
        List<VariableElementOperation> result = findByQuery(
                "select distinct(op) from VariableElementOperation op join op.sources s join op.targets t where s.nameableArtefact.urn = :variableElementUrn or t.nameableArtefact.urn = :variableElementUrn",
                parameters);
        return result;
    }
}

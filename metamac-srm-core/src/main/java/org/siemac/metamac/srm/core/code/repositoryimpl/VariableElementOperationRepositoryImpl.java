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
                "select distinct(op) from VariableElementOperation op join op.sources s join op.targets t where s.identifiableArtefact.urn = :variableElementUrn or t.identifiableArtefact.urn = :variableElementUrn",
                parameters);
        return result;
    }

    @Override
    public VariableElementOperation findVariableElementWithVariableElementAsSource(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("variableElementUrn", urn);
        List<VariableElementOperation> result = findByQuery("select op from VariableElementOperation op join op.sources s where s.identifiableArtefact.urn = :variableElementUrn", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public VariableElementOperation findVariableElementWithVariableElementAsTarget(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("variableElementUrn", urn);
        List<VariableElementOperation> result = findByQuery("select op from VariableElementOperation op join op.targets t where t.identifiableArtefact.urn = :variableElementUrn", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

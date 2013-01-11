package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for VariableElement
 */
@Repository("variableElementRepository")
public class VariableElementRepositoryImpl extends VariableElementRepositoryBase {

    public VariableElementRepositoryImpl() {
    }

    @Override
    public VariableElement findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<VariableElement> result = findByQuery("from VariableElement where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

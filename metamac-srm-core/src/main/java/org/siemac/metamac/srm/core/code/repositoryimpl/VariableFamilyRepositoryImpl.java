package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for VariableFamily
 */
@Repository("variableFamilyRepository")
public class VariableFamilyRepositoryImpl extends VariableFamilyRepositoryBase {

    public VariableFamilyRepositoryImpl() {
    }

    @Override
    public VariableFamily findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<VariableFamily> result = findByQuery("from VariableFamily where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

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

    public VariableFamily findByIdentifier(String identifier) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("identifier", identifier);
        List<VariableFamily> result = findByQuery("from VariableFamily where identifier = :identifier", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

package org.siemac.metamac.srm.core.code.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CodelistFamily
 */
@Repository("codelistFamilyRepository")
public class CodelistFamilyRepositoryImpl extends CodelistFamilyRepositoryBase {

    public CodelistFamilyRepositoryImpl() {
    }

    @Override
    public CodelistFamily findByIdentifier(String identifier) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("identifier", identifier);
        List<CodelistFamily> result = findByQuery("from CodelistFamily where identifier = :identifier", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

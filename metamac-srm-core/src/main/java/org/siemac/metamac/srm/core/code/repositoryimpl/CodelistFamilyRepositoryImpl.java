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
    public CodelistFamily findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CodelistFamily> result = findByQuery("from CodelistFamily where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

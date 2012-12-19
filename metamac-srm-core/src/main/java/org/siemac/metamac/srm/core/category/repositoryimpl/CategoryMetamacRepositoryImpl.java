package org.siemac.metamac.srm.core.category.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CategoryMetamac
 */
@Repository("categoryMetamacRepository")
public class CategoryMetamacRepositoryImpl extends CategoryMetamacRepositoryBase {

    public CategoryMetamacRepositoryImpl() {
    }

    @Override
    public CategoryMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CategoryMetamac> result = findByQuery("from CategoryMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

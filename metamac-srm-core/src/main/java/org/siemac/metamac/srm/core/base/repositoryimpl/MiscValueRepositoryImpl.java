package org.siemac.metamac.srm.core.base.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.base.domain.MiscValue;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for MiscValue
 */
@Repository("miscValueRepository")
public class MiscValueRepositoryImpl extends MiscValueRepositoryBase {

    public MiscValueRepositoryImpl() {
    }

    @Override
    public MiscValue findByName(String name) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        List<MiscValue> result = findByQuery("from MiscValue where name = :name", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}

package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;

public interface CodesRest2DoMapper {

    public RestCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistCriteriaMapper();
    public RestCriteria2SculptorCriteria<CodeMetamac> getCodeCriteriaMapper();
}

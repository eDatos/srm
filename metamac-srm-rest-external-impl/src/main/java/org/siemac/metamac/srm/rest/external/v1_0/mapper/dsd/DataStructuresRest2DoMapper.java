package org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

public interface DataStructuresRest2DoMapper {

    public RestCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper();
}

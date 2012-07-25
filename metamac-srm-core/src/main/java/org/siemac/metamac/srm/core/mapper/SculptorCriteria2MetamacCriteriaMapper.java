package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;


public interface SculptorCriteria2MetamacCriteriaMapper {

    public MetamacCriteriaResult<DataStructureDefinitionDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinition> source, Integer pageSize);
}

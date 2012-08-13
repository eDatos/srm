package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinition;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;

public interface SculptorCriteria2MetamacCriteriaMapper {

    public MetamacCriteriaResult<DataStructureDefinitionDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinition> source, Integer pageSize);

    public MetamacCriteriaResult<ConceptSchemeMetamacDto> pageResultToMetamacCriteriaResultConceptSchemeVersion(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize);

}

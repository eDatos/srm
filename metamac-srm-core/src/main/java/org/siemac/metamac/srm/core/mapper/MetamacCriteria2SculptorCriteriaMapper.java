package org.siemac.metamac.srm.core.mapper;

import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinition;

public interface MetamacCriteria2SculptorCriteriaMapper {

    public MetamacCriteria2SculptorCriteria<DataStructureDefinition> getDataStructureDefinitionCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeMetamacCriteriaMapper();

}

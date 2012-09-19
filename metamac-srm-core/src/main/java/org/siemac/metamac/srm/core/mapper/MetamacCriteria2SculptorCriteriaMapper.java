package org.siemac.metamac.srm.core.mapper;

import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

public interface MetamacCriteria2SculptorCriteriaMapper {

    public MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<ConceptMetamac> getConceptMetamacCriteriaMapper();

}

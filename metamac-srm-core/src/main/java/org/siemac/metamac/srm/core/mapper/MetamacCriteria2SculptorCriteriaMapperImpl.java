package org.siemac.metamac.srm.core.mapper;

import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinition;

@Component
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    @Autowired
    @Qualifier("metamacCriteria2SculptorCriteriaMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.mapper.MetamacCriteria2SculptorCriteriaMapper metamacCriteria2SculptorCriteriaMapperSdmxSrm;

    @Override
    public MetamacCriteria2SculptorCriteria<DataStructureDefinition> getDataStructureDefinitionCriteriaMapper() {
        return metamacCriteria2SculptorCriteriaMapperSdmxSrm.getDataStructureDefinitionCriteriaMapper();
    }

}

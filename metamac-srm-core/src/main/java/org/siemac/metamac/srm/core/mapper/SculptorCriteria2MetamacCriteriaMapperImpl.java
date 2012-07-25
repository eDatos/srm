package org.siemac.metamac.srm.core.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private Do2DtoMapper do2DtoMapper;
    
    @Override
    public MetamacCriteriaResult<DataStructureDefinitionDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinition> source, Integer pageSize) {
        MetamacCriteriaResult<DataStructureDefinitionDto> target = new MetamacCriteriaResult<DataStructureDefinitionDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() !=  null) {
            target.setResults(new ArrayList<DataStructureDefinitionDto>());
            for (DataStructureDefinition dataStructureDefinition: source.getValues()) {
                target.getResults().add(do2DtoMapper.dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dataStructureDefinition));
            }
        }
        
        return target;
    }


}

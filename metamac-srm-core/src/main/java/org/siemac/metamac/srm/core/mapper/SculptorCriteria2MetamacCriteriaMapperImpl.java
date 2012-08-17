package org.siemac.metamac.srm.core.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private Do2DtoMapper                                                                   do2DtoMapper;

    @Autowired
    @Qualifier("sculptorCriteria2MetamacCriteriaMapper")
    private com.arte.statistic.sdmx.srm.core.mapper.SculptorCriteria2MetamacCriteriaMapper sculptorCriteria2MetamacCriteriaMapper;

    @Override
    public MetamacCriteriaResult<DataStructureDefinitionDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinitionVersion> source, Integer pageSize) {
        return sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDataStructureDefinition(source, pageSize);
    }

    @Override
    public MetamacCriteriaResult<ConceptSchemeMetamacDto> pageResultToMetamacCriteriaResultConceptSchemeVersion(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<ConceptSchemeMetamacDto> target = new MetamacCriteriaResult<ConceptSchemeMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ConceptSchemeMetamacDto>());
            for (ConceptSchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(do2DtoMapper.conceptSchemeMetamacDoToDto(scheme));
            }
        }
        return target;
    }

}

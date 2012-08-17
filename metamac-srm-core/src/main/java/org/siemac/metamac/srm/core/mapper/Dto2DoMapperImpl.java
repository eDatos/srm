package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;

@org.springframework.stereotype.Component("dto2DoMapper")
public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    @Qualifier("dto2DoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.mapper.Dto2DoMapper dto2DoMapperSdmxSrm;

    @Override
    public <U extends Component> U componentDtoToComponent(ServiceContext ctx, ComponentDto source) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentDtoToComponent(ctx, source);
    }

    @Override
    public <U extends ComponentList> U componentListDtoToComponentList(ServiceContext ctx, ComponentListDto componentListDto) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentListDtoToComponentList(ctx, componentListDto);
    }

    @Override
    public DataStructureDefinitionVersion dataStructureDefinitionDtoToDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {
        return dto2DoMapperSdmxSrm.dataStructureDefinitionDtoToDataStructureDefinition(ctx, dataStructureDefinitionDto);
    }

    @Override
    public ConceptSchemeVersionMetamac conceptSchemeDtoToDo(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeDto) throws MetamacException {
        return (ConceptSchemeVersionMetamac) dto2DoMapperSdmxSrm.conceptSchemeDtoToDo(ctx, conceptSchemeDto);
    }

}

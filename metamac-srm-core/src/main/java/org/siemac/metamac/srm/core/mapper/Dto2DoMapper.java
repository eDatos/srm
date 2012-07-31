package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinition;
import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;

public interface Dto2DoMapper {

    public <U extends Component> U componentDtoToComponent(ServiceContext ctx, ComponentDto source) throws MetamacException;

    public <U extends ComponentList> U componentListDtoToComponentList(ServiceContext ctx, ComponentListDto componentListDto) throws MetamacException;

    public DataStructureDefinition dataStructureDefinitionDtoToDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException;

    public ConceptSchemeVersion conceptSchemeDtoToDo(ServiceContext ctx, ConceptSchemeDto conceptSchemeDto) throws MetamacException;

}

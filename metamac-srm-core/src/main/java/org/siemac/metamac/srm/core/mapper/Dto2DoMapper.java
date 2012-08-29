package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;

public interface Dto2DoMapper {

    // TODO Cambiar a tipos de METAMAC
    public <U extends Component> U componentDtoToComponent(ServiceContext ctx, ComponentDto source) throws MetamacException;
    public <U extends ComponentList> U componentListDtoToComponentList(ServiceContext ctx, ComponentListDto componentListDto) throws MetamacException;
    public DataStructureDefinitionVersion dataStructureDefinitionDtoToDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException;

    public ConceptSchemeVersionMetamac conceptSchemeDtoToDo(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeDto) throws MetamacException;

    public ConceptMetamac conceptDtoToDo(ServiceContext ctx, ConceptMetamacDto conceptDto) throws MetamacException;

}

package org.siemac.metamac.srm.core.dsd.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;

public interface DataStructureDefinitionDto2DoMapper {

    // TODO Cambiar a tipos de METAMAC
    public <U extends Component> U componentDtoToComponent(ComponentDto source) throws MetamacException;
    public <U extends ComponentList> U componentListDtoToComponentList(ComponentListDto source) throws MetamacException;

    public DataStructureDefinitionVersionMetamac dataStructureDefinitionDtoToDataStructureDefinition(DataStructureDefinitionMetamacDto source) throws MetamacException;
}

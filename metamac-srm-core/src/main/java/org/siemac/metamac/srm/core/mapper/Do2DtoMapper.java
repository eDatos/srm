package org.siemac.metamac.srm.core.mapper;

import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

public interface Do2DtoMapper {

    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component);

    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList);

    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion);

    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion);

    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac conceptSchemeVersion);

}

package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

public interface DataStructureDefinitionDo2DtoMapper {

    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component);
    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList);

    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac)
            throws MetamacException;
    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException;
    public DataStructureDefinitionMetamacBasicDto dataStructureDefinitionMetamacDoToBasicDto(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException;
    public List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDoListToDtoList(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacs)
            throws MetamacException;
    public RelatedResourceDto dataStructureDefinitionMetamacDoToRelatedResourceDto(DataStructureDefinitionVersionMetamac source) throws MetamacException;

}

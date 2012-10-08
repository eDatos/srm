package org.siemac.metamac.srm.core.dsd.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.exception.DataStructureDefinitionVersionMetamacNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;

@org.springframework.stereotype.Component("dataStructureDefinitionDto2DoMapper")
public class DataStructureDefinitionDto2DoMapperImpl implements DataStructureDefinitionDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.structure.mapper.DataStructureDefinitionDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository                                       dataStructureDefinitionVersionMetamacRepository;

    // ------------------------------------------------------------
    // DATA STRUCTURE DEFINITIONS
    // ------------------------------------------------------------

    @Override
    public <U extends Component> U componentDtoToComponent(ComponentDto source) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentDtoToComponent(source);
    }

    @Override
    public <U extends ComponentList> U componentListDtoToComponentList(ComponentListDto componentListDto) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentListDtoToComponentList(componentListDto);
    }

    @Override
    public DataStructureDefinitionVersionMetamac dataStructureDefinitionDtoToDataStructureDefinition(DataStructureDefinitionMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        DataStructureDefinitionVersionMetamac target = null;
        if (source.getId() == null) {
            target = new DataStructureDefinitionVersionMetamac();
        } else {
            try {
                target = dataStructureDefinitionVersionMetamacRepository.findById(source.getId());
            } catch (DataStructureDefinitionVersionMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // Modifiable attributes
        dto2DoMapperSdmxSrm.dataStructureDefinitionDtoToDataStructureDefinition(source, target);

        return target;
    }
}
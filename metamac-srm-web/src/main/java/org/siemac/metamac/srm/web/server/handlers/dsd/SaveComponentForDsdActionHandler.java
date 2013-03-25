package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveComponentForDsdActionHandler extends SecurityActionHandler<SaveComponentForDsdAction, SaveComponentForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveComponentForDsdActionHandler() {
        super(SaveComponentForDsdAction.class);
    }

    @Override
    public SaveComponentForDsdResult executeSecurityAction(SaveComponentForDsdAction action) throws ActionException {
        try {
            // Create the corresponding descriptor (if has not been created yet)
            createDescriptorIfNotExists(action.getDsdUrn(), action.getTypeComponentList());
            // Save component
            ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), action.getComponentDto());
            // Load DSD
            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(ServiceContextHolder.getCurrentServiceContext(),
                    action.getDsdUrn());
            return new SaveComponentForDsdResult(componentDto, dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private void createDescriptorIfNotExists(String dsdUrn, TypeComponentList typeComponentList) throws MetamacException {
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsdUrn, typeComponentList);
        if (descriptorDtos == null || descriptorDtos.isEmpty()) {
            DescriptorDto descriptorDto = new DescriptorDto();
            descriptorDto.setTypeComponentList(typeComponentList);
            srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsdUrn, descriptorDto);
        }
    }
}

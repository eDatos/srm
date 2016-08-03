package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdAndDescriptorsActionHandler extends SecurityActionHandler<GetDsdAndDescriptorsAction, GetDsdAndDescriptorsResult> {

    private static Logger        logger = Logger.getLogger(GetDsdAndDescriptorsActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdAndDescriptorsActionHandler() {
        super(GetDsdAndDescriptorsAction.class);
    }

    @Override
    public GetDsdAndDescriptorsResult executeSecurityAction(GetDsdAndDescriptorsAction action) throws ActionException {

        // ----------------------------------------------------------------------------------------
        // DSD
        // ----------------------------------------------------------------------------------------

        DataStructureDefinitionMetamacDto dsd;
        try {
            dsd = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn());
        } catch (MetamacException e1) {
            logger.log(Level.SEVERE, "Error in retrieveDsdByUrn with dsd URN =  " + action.getDsdUrn());
            throw WebExceptionUtils.createMetamacWebException(e1);
        }

        // ----------------------------------------------------------------------------------------
        // Primary Measure
        // ----------------------------------------------------------------------------------------

        DescriptorDto primaryMeasure = null;
        if (action.getDescriptorsToRetrieve().contains(TypeComponentList.MEASURE_DESCRIPTOR)) {
            primaryMeasure = new DescriptorDto();
            primaryMeasure.setTypeComponentList(TypeComponentList.MEASURE_DESCRIPTOR);
            try {
                List<DescriptorDto> measureDescriptorDtos = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsd.getUrn(),
                        TypeComponentList.MEASURE_DESCRIPTOR);
                if (!measureDescriptorDtos.isEmpty()) {
                    primaryMeasure = measureDescriptorDtos.get(0);
                }
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + dsd.getId() + " and typeComponentList = " + TypeComponentList.MEASURE_DESCRIPTOR + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }

        // ----------------------------------------------------------------------------------------
        // Dimensions
        // ----------------------------------------------------------------------------------------

        DescriptorDto dimensions = null;
        if (action.getDescriptorsToRetrieve().contains(TypeComponentList.DIMENSION_DESCRIPTOR)) {
            dimensions = new DescriptorDto();
            dimensions.setTypeComponentList(TypeComponentList.DIMENSION_DESCRIPTOR);
            try {
                List<DescriptorDto> dimensionDescriptorDtos = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsd.getUrn(),
                        TypeComponentList.DIMENSION_DESCRIPTOR);
                if (!dimensionDescriptorDtos.isEmpty()) {
                    dimensions = dimensionDescriptorDtos.get(0);
                }
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + dsd.getId() + " and typeComponentList = " + TypeComponentList.DIMENSION_DESCRIPTOR + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }

        // ----------------------------------------------------------------------------------------
        // Attributes
        // ----------------------------------------------------------------------------------------

        DescriptorDto attributes = null;
        if (action.getDescriptorsToRetrieve().contains(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
            attributes = new DescriptorDto();
            attributes.setTypeComponentList(TypeComponentList.ATTRIBUTE_DESCRIPTOR);
            try {
                List<DescriptorDto> attributeDescriptorDtos = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsd.getUrn(),
                        TypeComponentList.ATTRIBUTE_DESCRIPTOR);
                if (!attributeDescriptorDtos.isEmpty()) {
                    attributes = attributeDescriptorDtos.get(0);
                }
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + dsd.getId() + " and typeComponentList = " + TypeComponentList.ATTRIBUTE_DESCRIPTOR + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }

        // ----------------------------------------------------------------------------------------
        // Group keys
        // ----------------------------------------------------------------------------------------

        List<DescriptorDto> groupKeys = null;
        if (action.getDescriptorsToRetrieve().contains(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
            try {
                groupKeys = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsd.getUrn(), TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
            } catch (MetamacException e) {
                logger.log(Level.SEVERE,
                        "Error in findDescriptorForDsd with idDsd =  " + dsd.getId() + " and typeComponentList = " + TypeComponentList.GROUP_DIMENSION_DESCRIPTOR + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }

        return new GetDsdAndDescriptorsResult(dsd, primaryMeasure, dimensions, attributes, groupKeys);
    }
}

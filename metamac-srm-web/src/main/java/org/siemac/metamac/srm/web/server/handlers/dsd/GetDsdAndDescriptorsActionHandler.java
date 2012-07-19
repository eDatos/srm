package org.siemac.metamac.srm.web.server.handlers.dsd;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties;
import org.siemac.metamac.srm.web.shared.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.GetDsdAndDescriptorsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
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
        DataStructureDefinitionDto dsd = null;
        DescriptorDto primaryMeasure = null;
        DescriptorDto dimensions = null;
        DescriptorDto attributes = null;
        List<DescriptorDto> groupKeys = null;

        // DSD
        dsd = new DataStructureDefinitionDto();
        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(DataStructureDefinitionProperties.id()).eq(action.getIdDsd()).build();
        try {
            List<DataStructureDefinitionDto> dsdList = srmCoreServiceFacade.findDsdByCondition(ServiceContextHolder.getCurrentServiceContext(), conditions, PagingParameter.pageAccess(10)).getValues();
            if (!dsdList.isEmpty()) {
                dsd = dsdList.get(0);
            }
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDsdByCondition with idDsd =  " + action.getIdDsd() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        // Primary Measure
        primaryMeasure = new DescriptorDto();
        primaryMeasure.setTypeComponentList(TypeComponentList.MEASURE_DESCRIPTOR);
        try {
            List<DescriptorDto> measureDescriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(),
                    TypeComponentList.MEASURE_DESCRIPTOR);
            if (!measureDescriptorDtos.isEmpty()) {
                primaryMeasure = measureDescriptorDtos.get(0);
            }
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + action.getIdDsd() + " and typeComponentList = " + TypeComponentList.MEASURE_DESCRIPTOR + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        // Dimensions
        dimensions = new DescriptorDto();
        dimensions.setTypeComponentList(TypeComponentList.DIMENSION_DESCRIPTOR);
        try {
            List<DescriptorDto> dimensionDescriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(),
                    TypeComponentList.DIMENSION_DESCRIPTOR);
            if (!dimensionDescriptorDtos.isEmpty()) {
                dimensions = dimensionDescriptorDtos.get(0);
            }
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + action.getIdDsd() + " and typeComponentList = " + TypeComponentList.DIMENSION_DESCRIPTOR + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        // Attributes
        attributes = new DescriptorDto();
        attributes.setTypeComponentList(TypeComponentList.ATTRIBUTE_DESCRIPTOR);
        try {
            List<DescriptorDto> attributeDescriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(),
                    TypeComponentList.ATTRIBUTE_DESCRIPTOR);
            if (!attributeDescriptorDtos.isEmpty()) {
                attributes = attributeDescriptorDtos.get(0);
            }
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + action.getIdDsd() + " and typeComponentList = " + TypeComponentList.ATTRIBUTE_DESCRIPTOR + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        // Group keys
        try {
            groupKeys = srmCoreServiceFacade.findDescriptorForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(), TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE,
                    "Error in findDescriptorForDsd with idDsd =  " + action.getIdDsd() + " and typeComponentList = " + TypeComponentList.GROUP_DIMENSION_DESCRIPTOR + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        return new GetDsdAndDescriptorsResult(dsd, primaryMeasure, dimensions, attributes, groupKeys);
    }

    @Override
    public void undo(GetDsdAndDescriptorsAction action, GetDsdAndDescriptorsResult result, ExecutionContext context) throws ActionException {

    }

}

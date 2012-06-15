package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.idLogic;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.maintainer;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.versionLogic;

import java.io.FileInputStream;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.GeneratorUriUtil;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.domain.trans.dto.StructureMsgDto;
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseService;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.service.dto.Dto2DoMapper;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.serviceapi.DataStructureDefinitionService;
import org.siemac.metamac.srm.rest.internal.v1_0.util.RestUtils;
import org.siemac.metamac.trans.facade.serviceapi.TransformationServiceFacade;
import org.siemac.metamac.trans.v2_1.message.Error;
import org.springframework.beans.factory.annotation.Autowired;


public class DatastructureRestServiceFacadeImpl implements DatastructureRestServiceFacade {

    @Autowired
    private Dto2DoMapper dto2DoMapper;
    
    @Autowired
    private SdmxBaseService sdmxBaseService;
    
    @Autowired
    private DataStructureDefinitionService dataStructureDefinitionService;
    
    @Autowired
    private TransformationServiceFacade transformationServiceFacade;
    
    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;
    
    public SrmCoreServiceFacade getSrmCoreServiceFacade() {
        return srmCoreServiceFacade;
    }
    
    public DataStructureDefinitionService getDataStructureDefinitionService() {
        return dataStructureDefinitionService;
    }
    
    protected TransformationServiceFacade getTransformationServiceFacade() {
        return transformationServiceFacade;
    }
    
    protected SdmxBaseService getSdmxBaseService() {
        return sdmxBaseService;
    }

    protected Dto2DoMapper getDto2DoMapper() {
        return dto2DoMapper;
    }
    
    @Override
    public Response maintainableArtefactQuery(String agencyID, String resourceID, String version, String detail, String references) {
        // Data for error responses
        Error sdmx21ErrorResponse = null;
        Status errorStatus = Response.Status.INTERNAL_SERVER_ERROR;
        
        try {
            
            // Transform
            // 0. Create ExternalItemBtDto for Maintainer
            ExternalItemBtDto agencyExItemBtDto = GeneratorUriUtil.createExternalItem(agencyID, agencyID, "1.0", null, null, TypeExternalArtefactsEnum.AGENCY); // Agencys always have 1.0 version
            
            // 1. To DO
            ExternalItemBt agencyExItemBt = getDto2DoMapper().externalItemBtDtoToExternalItemBt(agencyExItemBtDto, RestUtils.SERVICECONTEXT, getSdmxBaseService());
            
            List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(idLogic()).eq(resourceID).and().withProperty(versionLogic()).eq(version).withProperty(maintainer()).eq(agencyExItemBt).build();
            
            // Find
            PagedResult<DataStructureDefinition> dataStructureDefinitionPagedList = getDataStructureDefinitionService().findDsdByCondition(RestUtils.SERVICECONTEXT, conditions, PagingParameter.pageAccess(1));
            
            if (dataStructureDefinitionPagedList.getValues().isEmpty()) {
                errorStatus = Response.Status.NOT_FOUND;
                throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND).withLoggedLevel(ExceptionLevelEnum.INFO).withMessageParameters(DataStructureDefinition.class.getName()).build();
            }
            
            StructureMsgDto structureMsgDto = new StructureMsgDto();
            structureMsgDto.addDataStructureDefinitionDto(getSrmCoreServiceFacade().retrieveExtendedDsd(RestUtils.SERVICECONTEXT, dataStructureDefinitionPagedList.getValues().get(0).getId(), TypeDozerCopyMode.UPDATE));
            
//            // Transform Metamac Business Objects to JAXB Objects
//             Structure structure = getTransformationServiceFacade().transformStructureMessage(RestUtils.SERVICECONTEXT, structureMsgDto);
//            // Serialization in response
//              return Response.ok(structure).build(); NO CXF Marshallers.
            
            // Serialization & Response
            return Response.ok(new FileInputStream(getSrmCoreServiceFacade().exportSDMXStructureMsg(RestUtils.SERVICECONTEXT, structureMsgDto))).build();
        }
        catch (Exception e) {
            // Create error list 
            try {
                sdmx21ErrorResponse = getTransformationServiceFacade().transformErrorMessage(RestUtils.SERVICECONTEXT, e);
            } catch (MetamacException e1) {
                sdmx21ErrorResponse = new Error();
            }
        }
        return Response.status(errorStatus).entity(sdmx21ErrorResponse).build();
    }

}
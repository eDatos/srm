package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.ws.ServicesResolver;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDo2DtoMapper;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDto2DoMapper;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDo2DtoMapper;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDto2DoMapper;
import org.siemac.metamac.srm.core.mapper.MetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.srm.core.mapper.SculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapper;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapper;
import org.siemac.metamac.srm.core.security.ConceptsSecurityUtils;
import org.siemac.metamac.srm.core.security.DataStructureDefinitionSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.trans.StructureMsgDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.util.ContentInputDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.message.Structure;
import com.arte.statistic.sdmx.v2_1.transformation.error.MetamacTransExceptionType;

/**
 * Implementation of srmCoreServiceFacade.
 */
@Service("srmCoreServiceFacade")
public class SrmCoreServiceFacadeImpl extends SrmCoreServiceFacadeImplBase {

    public SrmCoreServiceFacadeImpl() {
    }

    @Autowired
    private DataStructureDefinitionDo2DtoMapper    dataStructureDefinitionDo2DtoMapper;

    @Autowired
    private DataStructureDefinitionDto2DoMapper    dataStructureDefinitionDto2DoMapperDto2DoMapper;

    @Autowired
    private ConceptsDo2DtoMapper                   conceptsDo2DtoMapper;

    @Autowired
    private ConceptsDto2DoMapper                   conceptsDto2DoMapper;

    @Autowired
    private OrganisationsDo2DtoMapper              organisationsDo2DtoMapper;

    @Autowired
    private OrganisationsDto2DoMapper              organisationsDto2DoMapper;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller                        marshallerWithValidation;

    @Autowired
    private MetamacCriteria2SculptorCriteriaMapper metamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private SculptorCriteria2MetamacCriteriaMapper sculptorCriteria2MetamacCriteriaMapper;

    public Jaxb2Marshaller getMarshallerWithValidation() {
        return marshallerWithValidation;
    }

    protected DataStructureDefinitionDo2DtoMapper getDo2DtoMapper() {
        return dataStructureDefinitionDo2DtoMapper;
    }

    protected DataStructureDefinitionDto2DoMapper getDto2DoMapper() {
        return dataStructureDefinitionDto2DoMapperDto2DoMapper;
    }

    public MetamacCriteria2SculptorCriteriaMapper getMetamacCriteria2SculptorCriteriaMapper() {
        return metamacCriteria2SculptorCriteriaMapper;
    }

    public SculptorCriteria2MetamacCriteriaMapper getSculptorCriteria2MetamacCriteriaMapper() {
        return sculptorCriteria2MetamacCriteriaMapper;
    }

    /**************************************************************************
     * DSDs
     **************************************************************************/
    @Override
    public DataStructureDefinitionMetamacDto createDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException {
        // Security and transform
        // DTOs to Entities
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(dataStructureDefinitionMetamacDto);
        DataStructureDefinitionSecurityUtils.canCreateDataStructureDefinition(ctx);

        // Create
        dataStructureDefinitionVersionMetamac = getDsdsMetamacService().createDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode.COPY_ALL_METADATA, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public DataStructureDefinitionMetamacDto updateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException {
        // Security and transform
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, dataStructureDefinitionMetamacDto.getUrn());
        DataStructureDefinitionSecurityUtils.canUpdateDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // DTOs to Entities
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(dataStructureDefinitionMetamacDto);

        // Update
        dataStructureDefinitionVersionMetamac = getDsdsMetamacService().updateDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode.COPY_ALL_METADATA, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public void deleteDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canDeleteDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Delete
        getDsdsMetamacService().deleteDataStructureDefinition(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<DataStructureDefinitionMetamacDto> findDataStructureDefinitionsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canFindDataStructureDefinitionByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getDataStructureDefinitionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<DataStructureDefinitionVersionMetamac> result = getDsdsMetamacService().findDataStructureDefinitionsByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDataStructureDefinition(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public DataStructureDefinitionExtendDto retrieveExtendedDataStructureDefinition(ServiceContext ctx, String urn, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canRetrieveDataStructureDefinitionByUrn(ctx);

        // Load Dsd
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public DataStructureDefinitionMetamacDto retrieveDataStructureDefinition(ServiceContext ctx, String urn, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canRetrieveDataStructureDefinitionByUrn(ctx);

        // Load Dsd
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(typeDozerCopyMode, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public DataStructureDefinitionMetamacDto retrieveDataStructureDefinitionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canRetrieveDataStructureDefinitionByUrn(ctx);

        // Search
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode.COPY_ALL_METADATA, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public List<DataStructureDefinitionMetamacDto> retrieveDataStructureDefinitionVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canDataStructureDefinitionVersions(ctx);

        // Retrieve
        List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacs = getDsdsMetamacService().retrieveDataStructureDefinitionVersions(ctx, urn);

        // Transform
        List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper
                .dataStructureDefinitionMetamacDoListToDtoList(dataStructureDefinitionVersionMetamacs);

        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto sendDataStructureDefinitionToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canSendDataStructureDefinitionToProductionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().sendDataStructureDefinitionToProductionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto sendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canSendDataStructureDefinitionToDiffusionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().sendDataStructureDefinitionToDiffusionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto rejectDataStructureDefinitionProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canRejectDataStructureDefinitionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().rejectDataStructureDefinitionProductionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto rejectDataStructureDefinitionDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canRejectDataStructureDefinitionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().rejectDataStructureDefinitionDiffusionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto publishDataStructureDefinitionInternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canPublishDataStructureDefinitionInternally(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().publishInternallyDataStructureDefinition(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto publishDataStructureDefinitionExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canPublishDataStructureDefinitionExternally(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().publishExternallyDataStructureDefinition(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);
        DataStructureDefinitionSecurityUtils.canVersioningDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().versioningDataStructureDefinition(ctx, urnToCopy, versionType);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto cancelDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canCancelDataStructureDefinitionValidity(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().cancelDataStructureDefinitionValidity(ctx, urn);

        // Transform
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    /**************************************************************************
     * Descriptors
     **************************************************************************/

    @Override
    public List<DescriptorDto> findDescriptorsForDataStructureDefinition(ServiceContext ctx, String urnDsd, TypeComponentList typeComponentList) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canFindDescriptorsForDataStructureDefinition(ctx);

        // Load Dsd
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd);

        // Check Type
        if (!typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR) && !typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)
                && !typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR) && !typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withLoggedLevel(ExceptionLevelEnum.INFO)
                    .withMessageParameters(ServiceExceptionParameters.COMPONENT_LIST).build();
        }

        // To DTOs
        List<DescriptorDto> descriptorDtos = new ArrayList<DescriptorDto>();
        for (ComponentList componentList : dataStructureDefinitionVersionMetamac.getGrouping()) {
            if ((componentList instanceof AttributeDescriptor) && typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.COPY_ALL_METADATA, componentList));
            } else if ((componentList instanceof DimensionDescriptor) && typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.COPY_ALL_METADATA, componentList));
            } else if ((componentList instanceof GroupDimensionDescriptor) && typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.COPY_ALL_METADATA, componentList));
            } else if ((componentList instanceof MeasureDescriptor) && typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.COPY_ALL_METADATA, componentList));
            }
        }

        return descriptorDtos;
    }

    @Override
    public List<DescriptorDto> findDescriptorsForDataStructureDefinition(ServiceContext ctx, String urnDsd) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canFindDescriptorsForDataStructureDefinition(ctx);

        return findDescriptorsForDsd(ctx, urnDsd, TypeDozerCopyMode.COPY_ALL_METADATA);
    }

    @Override
    public DescriptorDto saveDescriptorForDataStructureDefinition(ServiceContext ctx, String urnDsd, DescriptorDto descriptorDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(descriptorDto);

        // Save
        componentListDescriptor = getDsdsMetamacService().saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(),
                componentListDescriptor);

        // Entities to DTOs
        return getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.COPY_ALL_METADATA, componentListDescriptor);
    }

    @Override
    public void deleteDescriptorForDataStructureDefinition(ServiceContext ctx, String urnDsd, DescriptorDto descriptorDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(descriptorDto);

        // Delete descriptor for DSD
        getDsdsMetamacService().deleteDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), componentListDescriptor);
    }

    /**************************************************************************
     * Component
     **************************************************************************/

    @Override
    public ComponentDto saveComponentForDataStructureDefinition(ServiceContext ctx, String urnDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto);

        // Save component for DSD
        component = getDsdsMetamacService().saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), component, typeComponentList);

        // Entitys to DTOs
        return getDo2DtoMapper().componentToComponentDto(TypeDozerCopyMode.COPY_ALL_METADATA, component);
    }

    @Override
    public void deleteComponentForDataStructureDefinition(ServiceContext ctx, String urnDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canDeleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto);

        // Delete component for DSD
        getDsdsMetamacService().deleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), component, typeComponentList);
    }

    /**************************************************************************
     * Import/Export
     **************************************************************************/
    @Override
    public void importSDMXStructureMsg(ServiceContext ctx, ContentInputDto contentDto) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canImportDataStructureDefinition(ctx);

        // StructureMessage
        // CodeList -> Metamac not imported CodeList, only process
        // Concepts -> Metamac not imported Concepts, only process
        // DataStructures -> Import

        // 1. Extract Structure
        StructureMsgDto structureMsgDto = null;
        try {
            Structure structure = (Structure) getMarshallerWithValidation().unmarshal(new StreamSource(contentDto.getInput()));
            structureMsgDto = getTransformationServiceFacade().transformStructureMessage(ctx, structure);
        } catch (XmlMappingException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacTransExceptionType.MATAMAC_TRANS_JAXB_ERROR).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .withMessageParameters(e.getMessage()).build();
        }

        // Import DataStructures
        for (DataStructureDefinitionExtendDto dsdExtDto : structureMsgDto.getDataStructureDefinitionDtos()) {
            saveDsdGraph(ctx, dsdExtDto);
        }
    }

    @Override
    public String exportSDMXStructureMsg(ServiceContext ctx, StructureMsgDto structureMsgDto) throws MetamacException {
        // TODO añadir seguridad a exportar DSD

        OutputStream outputStream = null;
        File file = null;

        try {
            file = File.createTempFile("mt_dsd_", ".xml");
            outputStream = new FileOutputStream(file);

            // StreamResult resultWriter = new StreamResult(outputStream);

            // Output with writer to avoid bad indent in xml ouput
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            StreamResult result = new StreamResult(writer);

            // Marshall properties
            Map<String, Object> marshallProperties = new HashMap<String, Object>();
            marshallProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // Formatted output
            getMarshallerWithValidation().setMarshallerProperties(marshallProperties);

            // Transform Metamac Business Objects to JAXB Objects
            Structure structure = getTransformationServiceFacade().transformStructureMessage(ctx, structureMsgDto);

            getMarshallerWithValidation().marshal(structure, result);

        } catch (XmlMappingException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacTransExceptionType.MATAMAC_TRANS_JAXB_ERROR).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .withMessageParameters((e.getRootCause() != null) ? e.getRootCause().getMessage() : e.getMessage()).build();
        } catch (FileNotFoundException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.UNKNOWN).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .withMessageParameters(FileNotFoundException.class.getName()).build();
        } catch (IOException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.UNKNOWN).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .withMessageParameters(IOException.class.getName()).build();
        }

        return (file == null) ? StringUtils.EMPTY : file.getAbsolutePath();
    }

    /**************************************************************************
     * CODELIST
     **************************************************************************/
    @Override
    public List<ExternalItemDto> findCodelists(ServiceContext ctx, String uriConcept) throws MetamacException {
        // TODO devolver los codelist posibles para un concepto
        return ServicesResolver.findAllCodelists();
    }

    /**************************************************************************
     * ORGANISATION
     **************************************************************************/

    @Override
    public ExternalItemDto findOrganisation(ServiceContext ctx, String uriOrganisation) throws MetamacException {
        // TODO find organization
        return ServicesResolver.resolveOrganisation(uriOrganisation);
    }

    // ------------------------------------------------------------------------
    // ORGANISATION SCHEMES
    // ------------------------------------------------------------------------

    @Override
    public OrganisationSchemeMetamacDto createOrganisationScheme(ServiceContext ctx, OrganisationSchemeMetamacDto organisationSchemeDto) throws MetamacException {
        // TODO Security and transform
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsDto2DoMapper.organisationSchemeMetamacDtoToDo(organisationSchemeDto);
        // OrganisationsSecurityUtils.canCreateOrganisationScheme(ctx, organisationSchemeVersion);

        // Create
        OrganisationSchemeVersionMetamac organisationSchemeVersionCreated = getOrganisationsMetamacService().createOrganisationScheme(ctx, organisationSchemeVersion);

        // Transform to DTO
        organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionCreated);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto updateOrganisationScheme(ServiceContext ctx, OrganisationSchemeMetamacDto organisationSchemeDto) throws MetamacException {
        // Security
        // OrganisationsSecurityUtils.canUpdateOrganisationScheme();

        // Transform
        OrganisationSchemeVersionMetamac organisationSchemeVersionToUpdate = organisationsDto2DoMapper.organisationSchemeMetamacDtoToDo(organisationSchemeDto);

        // Update
        OrganisationSchemeVersionMetamac organisationSchemeVersionUpdated = getOrganisationsMetamacService().updateOrganisationScheme(ctx, organisationSchemeVersionToUpdate);

        // Transform to DTO
        organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionUpdated);
        return organisationSchemeDto;
    }

    @Override
    public void deleteOrganisationScheme(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canDeleteOrganisationScheme(ctx, organisationSchemeVersion);

        // Delete
        getOrganisationsMetamacService().deleteOrganisationScheme(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<OrganisationSchemeMetamacDto> findOrganisationSchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // TODO Security
        // OrganisationsSecurityUtils.canFindOrganisationSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getOrganisationSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<OrganisationSchemeVersionMetamac> result = getOrganisationsMetamacService().findOrganisationSchemesByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<OrganisationSchemeMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultOrganisationSchemeVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public OrganisationSchemeMetamacDto retrieveOrganisationSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationsSecurityUtils.canRetrieveOrganisationSchemeByUrn(ctx);

        // Retrieve
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);

        // Transform
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersion);

        return organisationSchemeMetamacDto;
    }

    @Override
    public List<OrganisationSchemeMetamacDto> retrieveOrganisationSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationsSecurityUtils.canRetrieveOrganisationSchemeVersions(ctx);

        // Retrieve
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersionMetamacs = getOrganisationsMetamacService().retrieveOrganisationSchemeVersions(ctx, urn);

        // Transform
        List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos = organisationsDo2DtoMapper.organisationSchemeMetamacDoListToDtoList(organisationSchemeVersionMetamacs);

        return organisationSchemeMetamacDtos;
    }

    @Override
    public OrganisationSchemeMetamacDto sendOrganisationSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(ctx, organisationSchemeVersion);

        // Send
        OrganisationSchemeVersionMetamac organisationSchemeVersionProductionValidation = getOrganisationsMetamacService().sendOrganisationSchemeToProductionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionProductionValidation);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto sendOrganisationSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(ctx, organisationSchemeVersion);

        // Send
        OrganisationSchemeVersionMetamac organisationSchemeVersionDiffusionValidation = getOrganisationsMetamacService().sendOrganisationSchemeToDiffusionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionDiffusionValidation);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto rejectOrganisationSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(ctx, organisationSchemeVersion);

        // Reject
        OrganisationSchemeVersionMetamac organisationSchemeVersionRejected = getOrganisationsMetamacService().rejectOrganisationSchemeProductionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionRejected);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto rejectOrganisationSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(ctx, organisationSchemeVersion);

        // Reject
        OrganisationSchemeVersionMetamac organisationSchemeVersionRejected = getOrganisationsMetamacService().rejectOrganisationSchemeDiffusionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionRejected);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto publishOrganisationSchemeInternally(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(ctx, organisationSchemeVersion);

        // Publish
        OrganisationSchemeVersionMetamac organisationSchemeVersionPublished = getOrganisationsMetamacService().publishInternallyOrganisationScheme(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionPublished);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto publishOrganisationSchemeExternally(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(ctx, organisationSchemeVersion);

        OrganisationSchemeVersionMetamac organisationSchemeVersionPublished = getOrganisationsMetamacService().publishExternallyOrganisationScheme(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionPublished);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto versioningOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urnToCopy);
        // OrganisationsSecurityUtils.canVersioningOrganisationScheme(ctx, organisationSchemeVersionToCopy);

        OrganisationSchemeVersionMetamac organisationSchemeVersioned = getOrganisationsMetamacService().versioningOrganisationScheme(ctx, urnToCopy, versionType);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersioned);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto cancelOrganisationSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        // OrganisationsSecurityUtils.canCancelOrganisationSchemeValidity(ctx, organisationSchemeVersion);

        OrganisationSchemeVersionMetamac organisationSchemeCanceled = getOrganisationsMetamacService().cancelOrganisationSchemeValidity(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeCanceled);

        return organisationSchemeDto;
    }

    // ------------------------------------------------------------------------
    // ORGANISATIONS
    // ------------------------------------------------------------------------

    @Override
    public OrganisationMetamacDto createOrganisation(ServiceContext ctx, OrganisationMetamacDto organisationMetamacDto) throws MetamacException {
        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, organisationMetamacDto.getItemSchemeVersionUrn());
        // OrganisationsSecurityUtils.canCreateOrganisation(ctx, organisationSchemeVersion);

        // Transform
        OrganisationMetamac organisationMetamac = organisationsDto2DoMapper.organisationMetamacDtoToDo(organisationMetamacDto);

        // Create
        OrganisationMetamac organisationCreated = getOrganisationsMetamacService().createOrganisation(ctx, organisationMetamacDto.getItemSchemeVersionUrn(), organisationMetamac);

        // Transform to DTO
        organisationMetamacDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationCreated);

        return organisationMetamacDto;
    }

    //
    // @Override
    // public OrganisationMetamacDto updateOrganisation(ServiceContext ctx, OrganisationMetamacDto organisationDto) throws MetamacException {
    //
    // TODO Security
    // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByOrganisationUrn(ctx, organisationDto.getUrn());
    // OrganisationsSecurityUtils.canUpdateOrganisation(ctx, organisationSchemeVersion);
    //
    // // Transform
    // OrganisationMetamac organisationMetamac = organisationsDto2DoMapper.organisationDtoToDo(organisationDto);
    //
    // // Update
    // OrganisationMetamac organisationUpdated = getOrganisationsMetamacService().updateOrganisation(ctx, organisationMetamac);
    //
    // // Transform to DTO
    // organisationDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationUpdated);
    // return organisationDto;
    // }

    @Override
    public OrganisationMetamacDto retrieveOrganisationByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // TODO Security
        // OrganisationsSecurityUtils.canRetrieveOrganisationByUrn(ctx);

        // Retrieve
        OrganisationMetamac organisationMetamac = getOrganisationsMetamacService().retrieveOrganisationByUrn(ctx, urn);

        // Transform
        OrganisationMetamacDto organisationMetamacDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationMetamac);

        return organisationMetamacDto;
    }

    @Override
    public void deleteOrganisation(ServiceContext ctx, String urn) throws MetamacException {

        // TODO Security
        // OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByOrganisationUrn(ctx, urn);
        // OrganisationsSecurityUtils.canDeleteOrganisation(ctx, organisationSchemeVersion);

        // Delete
        getOrganisationsMetamacService().deleteOrganisation(ctx, urn);
    }

    @Override
    public List<ItemHierarchyDto> retrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx, String organisationSchemeUrn) throws MetamacException {

        // TODO Security
        // OrganisationsSecurityUtils.canRetrieveOrganisationsByOrganisationSchemeUrn(ctx);

        // Retrieve
        List<OrganisationMetamac> organisations = getOrganisationsMetamacService().retrieveOrganisationsByOrganisationSchemeUrn(ctx, organisationSchemeUrn);

        // Transform
        List<ItemHierarchyDto> itemsHierarchyDto = organisationsDo2DtoMapper.organisationMetamacDoListToItemHierarchyDtoList(organisations);
        return itemsHierarchyDto;
    }

    // @Override
    // public MetamacCriteriaResult<OrganisationMetamacDto> findOrganisationsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
    // TODO Security
    // OrganisationsSecurityUtils.canFindOrganisationsByCondition(ctx);
    //
    // // Transform
    // SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getOrganisationMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);
    //
    // // Find
    // PagedResult<OrganisationMetamac> result = getOrganisationsMetamacService().findOrganisationsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    //
    // // Transform
    // MetamacCriteriaResult<OrganisationMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultOrganisation(result,
    // sculptorCriteria.getPageSize());
    //
    // return metamacCriteriaResult;
    // }

    /**************************************************************************
     * CONCEPTS
     *************************************************************************/

    // ------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ------------------------------------------------------------------------

    // TODO REMOVE when DSDs were related to ConceptDtos (not to ExternalItemDtos)
    @Override
    public List<ExternalItemDto> findConceptSchemeRefs(ServiceContext ctx) {
        return ServicesResolver.findAllConceptSchemes();
    }

    // TODO REMOVE when DSDs were related to ConceptDtos (not to ExternalItemDtos)
    @Override
    public List<ExternalItemDto> findConcepts(ServiceContext ctx, String uriConceptScheme) {
        return ServicesResolver.retrieveConceptScheme(uriConceptScheme);
    }

    @Override
    public ConceptSchemeMetamacDto retrieveConceptSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canRetrieveConceptSchemeByUrn(ctx);

        // Retrieve
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);

        // Transform
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);

        return conceptSchemeMetamacDto;
    }

    @Override
    public List<ConceptSchemeMetamacDto> retrieveConceptSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canRetrieveConceptSchemeVersions(ctx);

        // Retrieve
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionMetamacs = getConceptsMetamacService().retrieveConceptSchemeVersions(ctx, urn);

        // Transform
        List<ConceptSchemeMetamacDto> conceptSchemeMetamacDtos = conceptsDo2DtoMapper.conceptSchemeMetamacDoListToDtoList(conceptSchemeVersionMetamacs);

        return conceptSchemeMetamacDtos;
    }

    @Override
    public ConceptSchemeMetamacDto createConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeDto) throws MetamacException {
        // Security and transform
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsDto2DoMapper.conceptSchemeDtoToDo(conceptSchemeDto);
        ConceptsSecurityUtils.canCreateConceptScheme(ctx, conceptSchemeVersion);

        // Create
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = getConceptsMetamacService().createConceptScheme(ctx, conceptSchemeVersion);

        // Transform to DTO
        conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionCreated);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto updateConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeDto) throws MetamacException {
        // Security and transform
        ConceptSchemeVersionMetamac conceptSchemeVersionOld = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, conceptSchemeDto.getUrn());
        String operationNew = conceptSchemeDto.getRelatedOperation() != null ? conceptSchemeDto.getRelatedOperation().getCode() : null;
        ConceptsSecurityUtils.canUpdateConceptScheme(ctx, conceptSchemeVersionOld, conceptSchemeDto.getType(), operationNew);

        // Transform
        ConceptSchemeVersionMetamac conceptSchemeVersionToUpdate = conceptsDto2DoMapper.conceptSchemeDtoToDo(conceptSchemeDto);

        // Update
        ConceptSchemeVersionMetamac conceptSchemeVersionUpdated = getConceptsMetamacService().updateConceptScheme(ctx, conceptSchemeVersionToUpdate);

        // Transform to DTO
        conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionUpdated);
        return conceptSchemeDto;
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canDeleteConceptScheme(ctx, conceptSchemeVersion);

        // Delete
        getConceptsMetamacService().deleteConceptScheme(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<ConceptSchemeMetamacDto> findConceptSchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getConceptsMetamacService().findConceptSchemesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<ConceptSchemeMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultConceptSchemeVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public ConceptSchemeMetamacDto sendConceptSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(ctx, conceptSchemeVersion);

        // Send
        ConceptSchemeVersionMetamac conceptSchemeVersionProductionValidation = getConceptsMetamacService().sendConceptSchemeToProductionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionProductionValidation);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto sendConceptSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(ctx, conceptSchemeVersion);

        // Send
        ConceptSchemeVersionMetamac conceptSchemeVersionDiffusionValidation = getConceptsMetamacService().sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionDiffusionValidation);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto rejectConceptSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canRejectConceptSchemeValidation(ctx, conceptSchemeVersion);

        // Reject
        ConceptSchemeVersionMetamac conceptSchemeVersionRejected = getConceptsMetamacService().rejectConceptSchemeProductionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionRejected);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto rejectConceptSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canRejectConceptSchemeValidation(ctx, conceptSchemeVersion);

        // Reject
        ConceptSchemeVersionMetamac conceptSchemeVersionRejected = getConceptsMetamacService().rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionRejected);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto publishConceptSchemeInternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canPublishConceptSchemeInternally(ctx, conceptSchemeVersion);

        // Publish
        ConceptSchemeVersionMetamac conceptSchemeVersionPublished = getConceptsMetamacService().publishInternallyConceptScheme(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionPublished);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto publishConceptSchemeExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canPublishConceptSchemeExternally(ctx, conceptSchemeVersion);

        ConceptSchemeVersionMetamac conceptSchemeVersionPublished = getConceptsMetamacService().publishExternallyConceptScheme(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionPublished);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptsSecurityUtils.canVersioningConceptScheme(ctx, conceptSchemeVersionToCopy);

        ConceptSchemeVersionMetamac conceptSchemeVersioned = getConceptsMetamacService().versioningConceptScheme(ctx, urnToCopy, versionType);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersioned);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto cancelConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canCancelConceptSchemeValidity(ctx, conceptSchemeVersion);

        ConceptSchemeVersionMetamac conceptSchemeCanceled = getConceptsMetamacService().cancelConceptSchemeValidity(ctx, urn);

        // Transform
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeCanceled);

        return conceptSchemeDto;
    }

    // ------------------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------------------

    @Override
    public ConceptMetamacDto createConcept(ServiceContext ctx, ConceptMetamacDto conceptMetamacDto) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, conceptMetamacDto.getItemSchemeVersionUrn());
        ConceptsSecurityUtils.canCreateConcept(ctx, conceptSchemeVersion);

        // Transform
        ConceptMetamac conceptMetamac = conceptsDto2DoMapper.conceptDtoToDo(conceptMetamacDto);

        // Create
        ConceptMetamac conceptMetamacCreated = getConceptsMetamacService().createConcept(ctx, conceptMetamacDto.getItemSchemeVersionUrn(), conceptMetamac);

        // Transform to DTO
        conceptMetamacDto = conceptsDo2DtoMapper.conceptMetamacDoToDto(conceptMetamacCreated);

        return conceptMetamacDto;
    }

    @Override
    public ConceptMetamacDto updateConcept(ServiceContext ctx, ConceptMetamacDto conceptDto) throws MetamacException {

        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, conceptDto.getUrn());
        ConceptsSecurityUtils.canUpdateConcept(ctx, conceptSchemeVersion);

        // Transform
        ConceptMetamac conceptMetamac = conceptsDto2DoMapper.conceptDtoToDo(conceptDto);

        // Update
        ConceptMetamac conceptUpdated = getConceptsMetamacService().updateConcept(ctx, conceptMetamac);

        // Transform to DTO
        conceptDto = conceptsDo2DtoMapper.conceptMetamacDoToDto(conceptUpdated);
        return conceptDto;
    }

    @Override
    public ConceptMetamacDto retrieveConceptByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveConceptByUrn(ctx);

        // Retrieve
        ConceptMetamac conceptMetamac = getConceptsMetamacService().retrieveConceptByUrn(ctx, urn);

        // Transform
        ConceptMetamacDto conceptMetamacDto = conceptsDo2DtoMapper.conceptMetamacDoToDto(conceptMetamac);

        return conceptMetamacDto;
    }

    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn);
        ConceptsSecurityUtils.canDeleteConcept(ctx, conceptSchemeVersion);

        // Delete
        getConceptsMetamacService().deleteConcept(ctx, urn);
    }

    @Override
    public List<ItemHierarchyDto> retrieveConceptsByConceptSchemeUrn(ServiceContext ctx, String conceptSchemeUrn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveConceptsByConceptSchemeUrn(ctx);

        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveConceptsByConceptSchemeUrn(ctx, conceptSchemeUrn);

        // Transform
        List<ItemHierarchyDto> itemsHierarchyDto = conceptsDo2DtoMapper.conceptMetamacDoListToItemHierarchyDtoList(concepts);
        return itemsHierarchyDto;
    }

    @Override
    public MetamacCriteriaResult<ConceptMetamacDto> findConceptsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getConceptsMetamacService().findConceptsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<ConceptMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultConcept(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public void addConceptRelation(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn1); // concept scheme of urn1 is same that urn2
        ConceptsSecurityUtils.canAddConceptRelation(ctx, conceptSchemeVersion);

        // Add relation
        getConceptsMetamacService().addConceptRelation(ctx, urn1, urn2);
    }

    @Override
    public void deleteConceptRelation(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn1); // concept scheme of urn1 is same that urn2
        ConceptsSecurityUtils.canDeleteConceptRelation(ctx, conceptSchemeVersion);

        // Delete concept relation
        getConceptsMetamacService().deleteConceptRelation(ctx, urn1, urn2);
    }

    @Override
    public List<ConceptMetamacDto> retrieveRelatedConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveRelatedConcepts(ctx);

        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveRelatedConcepts(ctx, urn);

        // Transform
        List<ConceptMetamacDto> conceptsDto = conceptsDo2DtoMapper.conceptMetamacDoListToDtoList(concepts);
        return conceptsDto;
    }

    @Override
    public void addConceptRelationRoles(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {
        // TODO Security ¿sobre los dos esquemas de concepto?

        getConceptsMetamacService().addConceptRelationRoles(ctx, urn, conceptRoleUrn);
    }

    @Override
    public void deleteConceptRelationRoles(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {
        // TODO Security ¿sobre los dos esquemas de concepto?

        getConceptsMetamacService().deleteConceptRelationRoles(ctx, urn, conceptRoleUrn);
    }

    @Override
    public List<ConceptMetamacDto> retrieveRelatedConceptsRoles(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveRelatedConceptsRoles(ctx);

        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveRelatedConceptsRoles(ctx, urn);

        // Transform
        List<ConceptMetamacDto> conceptsDto = conceptsDo2DtoMapper.conceptMetamacDoListToDtoList(concepts);
        return conceptsDto;
    }

    @Override
    public List<ConceptTypeDto> findAllConceptTypes(ServiceContext ctx) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canFindAllConceptTypes(ctx);

        // Find
        List<ConceptType> conceptTypes = getConceptsMetamacService().findAllConceptTypes(ctx);

        // Transform
        List<ConceptTypeDto> conceptTypesDto = conceptsDo2DtoMapper.conceptTypeDoListToConceptTypeDtoList(conceptTypes);
        return conceptTypesDto;
    }

    @Override
    public ConceptTypeDto retrieveConceptTypeByIdentifier(ServiceContext ctx, String identifier) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveConceptTypeByIdentifier(ctx);

        // Retrieve
        ConceptType conceptType = getConceptsMetamacService().retrieveConceptTypeByIdentifier(ctx, identifier);

        // Transform
        ConceptTypeDto conceptTypeDto = conceptsDo2DtoMapper.conceptTypeDoToDto(conceptType);
        return conceptTypeDto;
    }

    /**************************************************************************
     * PRIVATE METHODS
     *************************************************************************/

    private DataStructureDefinitionDto saveDsdGraph(ServiceContext ctx, DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto) throws MetamacException {
        return null;
        /*
         * // Save DSD (without grouping)
         * DataStructureDefinitionDto dataStructureDefinitionDto = null;
         * if (dataStructureDefinitionExtendDto.getId() != null) {
         * dataStructureDefinitionDto = updateDataStructureDefinition(ctx, dataStructureDefinitionExtendDto);
         * }
         * else {
         * dataStructureDefinitionDto = createDataStructureDefinition(ctx, dataStructureDefinitionExtendDto);
         * }
         * // Save DimensionDescriptor
         * DescriptorDto dimensionDesDto = new DescriptorDto();
         * for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
         * if (componentListDto.getTypeComponentList().equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
         * dimensionDesDto = (DescriptorDto) saveDescriptorAndComponents(ctx, dataStructureDefinitionDto.getId(), componentListDto);
         * }
         * }
         * // Auxiliary structure for update references
         * HashMap<String, DimensionComponentDto> dimensionComponents = new HashMap<String, DimensionComponentDto>();
         * for (ComponentDto componentDto : dimensionDesDto.getComponents()) {
         * dimensionComponents.put(componentDto.getCode(), (DimensionComponentDto) componentDto);
         * }
         * // Save GroupDescriptor
         * HashMap<String, DescriptorDto> groupDescriptors = new HashMap<String, DescriptorDto>();
         * for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
         * if (componentListDto.getTypeComponentList().equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
         * // 1: Update References For GroupDimensionDescriptor->components
         * Set<ComponentDto> componentsUpdate = new HashSet<ComponentDto>();
         * for (ComponentDto componentDto : componentListDto.getComponents()) {
         * if (dimensionComponents.get(componentDto.getCode()) == null) {
         * throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_GROUP_DESCRIPTOR_UNABLE_UPDATE).build();
         * } else {
         * componentsUpdate.add(dimensionComponents.get(componentDto.getCode()));
         * }
         * }
         * componentListDto.getComponents().clear();
         * componentListDto.getComponents().addAll(componentsUpdate);
         * // 2: Save groupDescriptor
         * // Don't need save new components only descriptor
         * DescriptorDto groupDescriptorDto = saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), (DescriptorDto) componentListDto);
         * groupDescriptors.put(groupDescriptorDto.getCode(), groupDescriptorDto); // Auxiliary structure for update references
         * }
         * }
         * // Save AttributeDescriptor
         * for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
         * if (componentListDto.getTypeComponentList().equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
         * for (ComponentDto componentDto : componentListDto.getComponents()) {
         * // 1: Update references for AttributeRelationship
         * RelationshipDto relationshipDto = ((DataAttributeDto) componentDto).getRelateTo();
         * if (relationshipDto.getDimensionForDimensionRelationship() != null) {
         * Set<DimensionComponentDto> componentsUpdate = new HashSet<DimensionComponentDto>();
         * for (ComponentDto itemRefDto : relationshipDto.getDimensionForDimensionRelationship()) {
         * if (dimensionComponents.get(itemRefDto.getCode()) == null) {
         * throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE).build();
         * } else {
         * componentsUpdate.add(dimensionComponents.get(itemRefDto.getCode()));
         * }
         * }
         * relationshipDto.getDimensionForDimensionRelationship().clear();
         * relationshipDto.getDimensionForDimensionRelationship().addAll(componentsUpdate);
         * }
         * if (relationshipDto.getGroupKeyForDimensionRelationship() != null) {
         * Set<DescriptorDto> descriptorsDtoUpdate = new HashSet<DescriptorDto>();
         * for (DescriptorDto itemRefDto : relationshipDto.getGroupKeyForDimensionRelationship()) {
         * if (groupDescriptors.get(itemRefDto.getCode()) == null) {
         * throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE).build();
         * } else {
         * descriptorsDtoUpdate.add(groupDescriptors.get(itemRefDto.getCode()));
         * }
         * }
         * relationshipDto.getGroupKeyForDimensionRelationship().clear();
         * relationshipDto.getGroupKeyForDimensionRelationship().addAll(descriptorsDtoUpdate);
         * }
         * if (relationshipDto.getGroupKeyForGroupRelationship() != null) {
         * if (groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getCode()) == null) {
         * throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE).build();
         * } else {
         * relationshipDto.setGroupKeyForGroupRelationship(groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getCode()));
         * }
         * }
         * }
         * // 2: Save AttributeDescriptor
         * saveDescriptorAndComponents(ctx, dataStructureDefinitionDto.getId(), componentListDto);
         * }
         * }
         * // Save MeasureDescriptor
         * for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
         * if (componentListDto.getTypeComponentList().equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
         * saveDescriptorAndComponents(ctx, dataStructureDefinitionDto.getId(), componentListDto);
         * }
         * }
         * return dataStructureDefinitionDto;
         */
    }

    private List<DescriptorDto> findDescriptorsForDsd(ServiceContext ctx, String urnDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // 2 - Retrieve Descriptor
        List<DescriptorDto> descriptorDtos;
        // try {
        // 1 - Retrieve DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd);

        descriptorDtos = new ArrayList<DescriptorDto>();
        for (ComponentList componentList : dataStructureDefinitionVersion.getGrouping()) {
            descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(typeDozerCopyMode, componentList));
        }
        // } catch (DataStructureDefinitionVersionNotFoundException e) {
        // throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND)
        // .withMessageParameters(ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION).build();
        // }

        return descriptorDtos;
    }

    // private ComponentListDto saveDescriptorAndComponents(ServiceContext ctx, String urnDsd, ComponentListDto componentListDto) throws MetamacException {
    // Set<ComponentDto> componentBackupDtos = new HashSet<ComponentDto>();
    // for (ComponentDto componentDto : componentListDto.getComponents()) {
    // componentBackupDtos.add(componentDto);
    // }
    //
    // // Remove association with components (temporal)
    // componentListDto.removeAllComponents();
    //
    // // Save ComponentList
    // componentListDto = saveDescriptorForDataStructureDefinition(ctx, urnDsd, (DescriptorDto) componentListDto);
    //
    // // Save Components
    // for (ComponentDto componentDto : componentBackupDtos) {
    // componentDto = saveComponentForDataStructureDefinition(ctx, urnDsd, componentDto, componentListDto.getTypeComponentList());
    // componentListDto.getComponents().add(componentDto); // Recreate association with components
    // }
    //
    // return componentListDto;
    // }

}

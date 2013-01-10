package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.ws.ServicesResolver;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapper;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDto2DoMapper;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.mapper.CodesDo2DtoMapper;
import org.siemac.metamac.srm.core.code.mapper.CodesDto2DoMapper;
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
import org.siemac.metamac.srm.core.criteria.mapper.MetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.srm.core.criteria.mapper.SculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDo2DtoMapper;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDto2DoMapper;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapper;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapper;
import org.siemac.metamac.srm.core.security.CodesSecurityUtils;
import org.siemac.metamac.srm.core.security.ConceptsSecurityUtils;
import org.siemac.metamac.srm.core.security.DataStructureDefinitionSecurityUtils;
import org.siemac.metamac.srm.core.security.ItemsSecurityUtils;
import org.siemac.metamac.srm.core.security.OrganisationsSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.importation.ImportationJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.importation.ImportationService;
import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.trans.StructureMsgDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.util.ContentInputDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

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
    private CategoriesDo2DtoMapper                 categoriesDo2DtoMapper;

    @Autowired
    private CategoriesDto2DoMapper                 categoriesDto2DoMapper;

    @Autowired
    private CodesDo2DtoMapper                      codesDo2DtoMapper;

    @Autowired
    private CodesDto2DoMapper                      codesDto2DoMapper;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller                        marshallerWithValidation;

    @Autowired
    private MetamacCriteria2SculptorCriteriaMapper metamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private SculptorCriteria2MetamacCriteriaMapper sculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private ImportationService                     importationService;

    @Autowired
    @Qualifier("importationMetamacJaxb2DoCallback")
    private ImportationJaxb2DoCallback             importationJaxb2DoCallback;

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
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canDeleteDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

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
    public DataStructureDefinitionMetamacDto endDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canEndDataStructureDefinitionValidity(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDsdsMetamacService().endDataStructureDefinitionValidity(ctx, urn);

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
    public ComponentDto saveComponentForDataStructureDefinition(ServiceContext ctx, String urnDsd, ComponentDto componentDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto);

        // Save component for DSD
        component = getDsdsMetamacService().saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), component);

        // Entitys to DTOs
        return getDo2DtoMapper().componentToComponentDto(TypeDozerCopyMode.COPY_ALL_METADATA, component);
    }

    @Override
    public void deleteComponentForDataStructureDefinition(ServiceContext ctx, String urnDsd, ComponentDto componentDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canDeleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto);

        // Delete component for DSD
        getDsdsMetamacService().deleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), component);
    }

    /**************************************************************************
     * Import/Export
     **************************************************************************/
    @Override
    public void importSDMXStructureMsg(ServiceContext ctx, ContentInputDto contentDto) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canImportDataStructureDefinition(ctx);

        // Import
        importationService.importSDMXStructure(ctx, contentDto.getInput(), importationJaxb2DoCallback);
    }

    @Override
    public String exportSDMXStructureMsg(ServiceContext ctx, StructureMsgDto structureMsgDto) throws MetamacException {
        // TODO cambiar la interfaz de este método para que sean los que sean los elementos que se le pasan, sean URNs en vez de objetos
        // posiblemente se tenga que cambiar el structuremsgdto para que almacene conjuntos de urn agrupasad por tipo, es decir, urn de dsd, urn de codelist... y así

        // TODO Facade. Añadir seguridad a exportar DSD

        // OutputStream outputStream = null;
        File file = null;
        /*
         * try {
         * file = File.createTempFile("mt_dsd_", ".xml");
         * outputStream = new FileOutputStream(file);
         * // StreamResult resultWriter = new StreamResult(outputStream);
         * // Output with writer to avoid bad indent in xml ouput
         * OutputStreamWriter writer = new OutputStreamWriter(outputStream);
         * StreamResult result = new StreamResult(writer);
         * // Marshall properties
         * Map<String, Object> marshallProperties = new HashMap<String, Object>();
         * marshallProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // Formatted output
         * getMarshallerWithValidation().setMarshallerProperties(marshallProperties);
         * // Transform Metamac Business Objects to JAXB Objects
         * Structure structure = getTransformationServiceFacade().transformStructureMessage(ctx, structureMsgDto);
         * getMarshallerWithValidation().marshal(structure, result);
         * } catch (XmlMappingException e) {
         * throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacTransExceptionType.MATAMAC_TRANS_JAXB_ERROR).withLoggedLevel(ExceptionLevelEnum.ERROR)
         * .withMessageParameters((e.getRootCause() != null) ? e.getRootCause().getMessage() : e.getMessage()).build();
         * } catch (FileNotFoundException e) {
         * throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.UNKNOWN).withLoggedLevel(ExceptionLevelEnum.ERROR)
         * .withMessageParameters(FileNotFoundException.class.getName()).build();
         * } catch (IOException e) {
         * throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.UNKNOWN).withLoggedLevel(ExceptionLevelEnum.ERROR)
         * .withMessageParameters(IOException.class.getName()).build();
         * }
         */
        return (file == null) ? StringUtils.EMPTY : file.getAbsolutePath();
    }

    // ------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------

    @Override
    public CodelistMetamacDto retrieveCodelistByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveItemSchemeByUrn(ctx);

        // Retrieve
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, urn);

        // Transform
        CodelistMetamacDto codelistMetamacDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersion);

        return codelistMetamacDto;
    }

    @Override
    public CodelistMetamacDto createCodelist(ServiceContext ctx, CodelistMetamacDto codelistDto) throws MetamacException {
        // Security
        ItemsSecurityUtils.canCreateItemScheme(ctx);

        // Transform
        CodelistVersionMetamac codelistVersion = codesDto2DoMapper.codelistDtoToDo(codelistDto);

        // Create
        CodelistVersionMetamac codelistVersionCreated = getCodesMetamacService().createCodelist(ctx, codelistVersion);

        // Transform to DTO
        codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionCreated);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto updateCodelist(ServiceContext ctx, CodelistMetamacDto codelistDto) throws MetamacException {
        // Security and transform
        CodelistVersionMetamac codelistVersionOld = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistDto.getUrn());
        ItemsSecurityUtils.canUpdateItemScheme(ctx, codelistVersionOld.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodelistVersionMetamac codelistVersionToUpdate = codesDto2DoMapper.codelistDtoToDo(codelistDto);

        // Update
        CodelistVersionMetamac codelistVersionUpdated = getCodesMetamacService().updateCodelist(ctx, codelistVersionToUpdate);

        // Transform to DTO
        codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionUpdated);
        return codelistDto;
    }

    @Override
    public void deleteCodelist(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canDeleteItemScheme(ctx);

        // Delete
        getCodesMetamacService().deleteCodelist(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<CodelistMetamacDto> findCodelistsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ItemsSecurityUtils.canFindItemSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistVersionMetamac> result = getCodesMetamacService().findCodelistsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CodelistMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCodelistVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public List<CodelistMetamacDto> retrieveCodelistVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveItemSchemeVersions(ctx);

        // Retrieve
        List<CodelistVersionMetamac> codelistVersionMetamacs = getCodesMetamacService().retrieveCodelistVersions(ctx, urn);

        // Transform
        List<CodelistMetamacDto> codelistMetamacDtos = codesDo2DtoMapper.codelistMetamacDoListToDtoList(codelistVersionMetamacs);

        return codelistMetamacDtos;
    }

    @Override
    public CodelistMetamacDto sendCodelistToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canSendItemSchemeToProductionValidation(ctx);

        // Send
        CodelistVersionMetamac codelistVersionProductionValidation = getCodesMetamacService().sendCodelistToProductionValidation(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionProductionValidation);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto sendCodelistToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(ctx);

        // Send
        CodelistVersionMetamac codelistVersionDiffusionValidation = getCodesMetamacService().sendCodelistToDiffusionValidation(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionDiffusionValidation);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto rejectCodelistProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, urn);
        ItemsSecurityUtils.canRejectItemSchemeValidation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        CodelistVersionMetamac codelistVersionRejected = getCodesMetamacService().rejectCodelistProductionValidation(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionRejected);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto rejectCodelistDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, urn);
        ItemsSecurityUtils.canRejectItemSchemeValidation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        CodelistVersionMetamac codelistVersionRejected = getCodesMetamacService().rejectCodelistDiffusionValidation(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionRejected);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto publishCodelistInternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canPublishItemSchemeInternally(ctx);

        // Publish
        CodelistVersionMetamac codelistVersionPublished = getCodesMetamacService().publishInternallyCodelist(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionPublished);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto publishCodelistExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canPublishItemSchemeExternally(ctx);

        CodelistVersionMetamac codelistVersionPublished = getCodesMetamacService().publishExternallyCodelist(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionPublished);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto versioningCodelist(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        ItemsSecurityUtils.canVersioningItemScheme(ctx);

        CodelistVersionMetamac codelistVersioned = getCodesMetamacService().versioningCodelist(ctx, urnToCopy, versionType);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersioned);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto endCodelistValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canEndItemSchemeValidity(ctx);

        CodelistVersionMetamac codelistEnded = getCodesMetamacService().endCodelistValidity(ctx, urn);

        // Transform
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistEnded);

        return codelistDto;
    }

    // ------------------------------------------------------------------------
    // CODES
    // ------------------------------------------------------------------------

    @Override
    public CodeMetamacDto createCode(ServiceContext ctx, CodeMetamacDto codeMetamacDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codeMetamacDto.getItemSchemeVersionUrn());
        ItemsSecurityUtils.canCreateItem(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodeMetamac codeMetamac = codesDto2DoMapper.codeDtoToDo(codeMetamacDto);

        // Create
        CodeMetamac codeMetamacCreated = getCodesMetamacService().createCode(ctx, codeMetamacDto.getItemSchemeVersionUrn(), codeMetamac);

        // Transform to DTO
        codeMetamacDto = codesDo2DtoMapper.codeMetamacDoToDto(codeMetamacCreated);

        return codeMetamacDto;
    }

    @Override
    public CodeMetamacDto updateCode(ServiceContext ctx, CodeMetamacDto codeDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, codeDto.getUrn());
        ItemsSecurityUtils.canUpdateItem(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodeMetamac codeMetamac = codesDto2DoMapper.codeDtoToDo(codeDto);

        // Update
        CodeMetamac conceptUpdated = getCodesMetamacService().updateCode(ctx, codeMetamac);

        // Transform to DTO
        codeDto = codesDo2DtoMapper.codeMetamacDoToDto(conceptUpdated);
        return codeDto;
    }

    @Override
    public CodeMetamacDto retrieveCodeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Retrieve
        CodeMetamac codeMetamac = getCodesMetamacService().retrieveCodeByUrn(ctx, urn);

        // Transform
        CodeMetamacDto codeMetamacDto = codesDo2DtoMapper.codeMetamacDoToDto(codeMetamac);

        return codeMetamacDto;
    }

    @Override
    public MetamacCriteriaResult<CodeMetamacDto> findCodesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodeMetamac> result = getCodesMetamacService().findCodesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CodeMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCode(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public void deleteCode(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, urn);
        ItemsSecurityUtils.canDeleteItem(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Delete
        getCodesMetamacService().deleteCode(ctx, urn);
    }

    @Override
    public List<ItemHierarchyDto> retrieveCodesByCodelistUrn(ServiceContext ctx, String codelistUrn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Retrieve
        List<CodeMetamac> codes = getCodesMetamacService().retrieveCodesByCodelistUrn(ctx, codelistUrn);

        // Transform
        List<ItemHierarchyDto> itemsHierarchyDto = codesDo2DtoMapper.codeMetamacDoListToItemHierarchyDtoList(codes);
        return itemsHierarchyDto;
    }

    // ------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------

    @Override
    public CodelistFamilyDto retrieveCodelistFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveCodelistFamilyUrn(ctx);

        // Retrieve
        CodelistFamily codelistFamily = getCodesMetamacService().retrieveCodelistFamilyByUrn(ctx, urn);

        // Transform
        CodelistFamilyDto codelistFamilyDto = codesDo2DtoMapper.codelistFamilyDoToDto(codelistFamily);
        return codelistFamilyDto;
    }

    @Override
    public CodelistFamilyDto createCodelistFamily(ServiceContext ctx, CodelistFamilyDto codelistFamilyDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudCodelistFamilyUrn(ctx);

        // Transform
        CodelistFamily codelistFamily = codesDto2DoMapper.codelistFamilyDtoToDo(codelistFamilyDto);

        // Create
        CodelistFamily codelistFamilyCreated = getCodesMetamacService().createCodelistFamily(ctx, codelistFamily);

        // Transform to DTO
        codelistFamilyDto = codesDo2DtoMapper.codelistFamilyDoToDto(codelistFamilyCreated);
        return codelistFamilyDto;
    }

    @Override
    public CodelistFamilyDto updateCodelistFamily(ServiceContext ctx, CodelistFamilyDto codelistFamilyDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudCodelistFamilyUrn(ctx);

        // Transform
        CodelistFamily codelistFamilyToUpdate = codesDto2DoMapper.codelistFamilyDtoToDo(codelistFamilyDto);

        // Update
        CodelistFamily codelistFamilyUpdated = getCodesMetamacService().updateCodelistFamily(ctx, codelistFamilyToUpdate);

        // Transform to DTO
        codelistFamilyDto = codesDo2DtoMapper.codelistFamilyDoToDto(codelistFamilyUpdated);
        return codelistFamilyDto;
    }

    @Override
    public void deleteCodelistFamily(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudCodelistFamilyUrn(ctx); // TODO security CodelistFamily

        // Delete
        getCodesMetamacService().deleteCodelistFamily(ctx, urn);
    }

    @Override
    public void addCodelistsToCodelistFamily(ServiceContext ctx, List<String> codelistUrns, String codelistFamilyUrn) throws MetamacException {
        // Security // TODO security CodelistFamily

        // Add
        getCodesMetamacService().addCodelistsToCodelistFamily(ctx, codelistUrns, codelistFamilyUrn);
    }

    @Override
    public void removeCodelistFromCodelistFamily(ServiceContext ctx, String codelistUrn, String codelistFamilyUrn) throws MetamacException {
        // Security // TODO security CodelistFamily

        // Delete
        getCodesMetamacService().removeCodelistFromCodelistFamily(ctx, codelistUrn, codelistFamilyUrn);
    }

    @Override
    public MetamacCriteriaResult<CodelistFamilyDto> findCodelistFamiliesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveCodelistFamilyUrn(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistFamilyCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistFamily> result = getCodesMetamacService().findCodelistFamiliesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CodelistFamilyDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCodelistFamily(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    /**************************************************************************
     * ORGANISATION
     **************************************************************************/
    // ------------------------------------------------------------------------
    // ORGANISATION SCHEMES
    // ------------------------------------------------------------------------

    @Override
    public OrganisationSchemeMetamacDto createOrganisationScheme(ServiceContext ctx, OrganisationSchemeMetamacDto organisationSchemeDto) throws MetamacException {
        // Security and transform
        OrganisationsSecurityUtils.canCreateOrganisationScheme(ctx);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsDto2DoMapper.organisationSchemeMetamacDtoToDo(organisationSchemeDto);

        // Create
        OrganisationSchemeVersionMetamac organisationSchemeVersionCreated = getOrganisationsMetamacService().createOrganisationScheme(ctx, organisationSchemeVersion);

        // Transform to DTO
        organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionCreated);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto updateOrganisationScheme(ServiceContext ctx, OrganisationSchemeMetamacDto organisationSchemeDto) throws MetamacException {
        // Security
        OrganisationSchemeVersionMetamac organisationSchemeVersionOld = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, organisationSchemeDto.getUrn());
        OrganisationsSecurityUtils.canUpdateOrganisationScheme(ctx, organisationSchemeVersionOld.getLifeCycleMetadata().getProcStatus(), organisationSchemeVersionOld.getOrganisationSchemeType());

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
        // Security
        OrganisationsSecurityUtils.canDeleteOrganisationScheme(ctx);

        // Delete
        getOrganisationsMetamacService().deleteOrganisationScheme(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<OrganisationSchemeMetamacDto> findOrganisationSchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canFindOrganisationSchemesByCondition(ctx);

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
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationSchemeByUrn(ctx);

        // Retrieve
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);

        // Transform
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersion);

        return organisationSchemeMetamacDto;
    }

    @Override
    public List<OrganisationSchemeMetamacDto> retrieveOrganisationSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationSchemeVersions(ctx);

        // Retrieve
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersionMetamacs = getOrganisationsMetamacService().retrieveOrganisationSchemeVersions(ctx, urn);

        // Transform
        List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos = organisationsDo2DtoMapper.organisationSchemeMetamacDoListToDtoList(organisationSchemeVersionMetamacs);

        return organisationSchemeMetamacDtos;
    }

    @Override
    public OrganisationSchemeMetamacDto sendOrganisationSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(ctx);

        // Send
        OrganisationSchemeVersionMetamac organisationSchemeVersionProductionValidation = getOrganisationsMetamacService().sendOrganisationSchemeToProductionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionProductionValidation);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto sendOrganisationSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(ctx);

        // Send
        OrganisationSchemeVersionMetamac organisationSchemeVersionDiffusionValidation = getOrganisationsMetamacService().sendOrganisationSchemeToDiffusionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionDiffusionValidation);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto rejectOrganisationSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        OrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(ctx, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        OrganisationSchemeVersionMetamac organisationSchemeVersionRejected = getOrganisationsMetamacService().rejectOrganisationSchemeProductionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionRejected);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto rejectOrganisationSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, urn);
        OrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(ctx, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        OrganisationSchemeVersionMetamac organisationSchemeVersionRejected = getOrganisationsMetamacService().rejectOrganisationSchemeDiffusionValidation(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionRejected);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto publishOrganisationSchemeInternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(ctx);

        // Publish
        OrganisationSchemeVersionMetamac organisationSchemeVersionPublished = getOrganisationsMetamacService().publishInternallyOrganisationScheme(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionPublished);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto publishOrganisationSchemeExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(ctx);

        OrganisationSchemeVersionMetamac organisationSchemeVersionPublished = getOrganisationsMetamacService().publishExternallyOrganisationScheme(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersionPublished);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto versioningOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canVersioningOrganisationScheme(ctx);

        OrganisationSchemeVersionMetamac organisationSchemeVersioned = getOrganisationsMetamacService().versioningOrganisationScheme(ctx, urnToCopy, versionType);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeVersioned);
        return organisationSchemeDto;
    }

    @Override
    public OrganisationSchemeMetamacDto endOrganisationSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canEndOrganisationSchemeValidity(ctx);

        OrganisationSchemeVersionMetamac organisationSchemeEnded = getOrganisationsMetamacService().endOrganisationSchemeValidity(ctx, urn);

        // Transform to DTO
        OrganisationSchemeMetamacDto organisationSchemeDto = organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(organisationSchemeEnded);

        return organisationSchemeDto;
    }

    // ------------------------------------------------------------------------
    // ORGANISATIONS
    // ------------------------------------------------------------------------

    @Override
    public OrganisationMetamacDto createOrganisation(ServiceContext ctx, OrganisationMetamacDto organisationMetamacDto) throws MetamacException {
        // Security
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, organisationMetamacDto.getItemSchemeVersionUrn());
        OrganisationsSecurityUtils.canCreateOrganisation(ctx, organisationSchemeVersion);

        // Transform
        OrganisationMetamac organisationMetamac = organisationsDto2DoMapper.organisationMetamacDtoToDo(organisationMetamacDto);

        // Create
        OrganisationMetamac organisationCreated = getOrganisationsMetamacService().createOrganisation(ctx, organisationMetamacDto.getItemSchemeVersionUrn(), organisationMetamac);

        // Transform to DTO
        organisationMetamacDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationCreated);

        return organisationMetamacDto;
    }

    @Override
    public OrganisationMetamacDto updateOrganisation(ServiceContext ctx, OrganisationMetamacDto organisationDto) throws MetamacException {
        // Security
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByOrganisationUrn(ctx, organisationDto.getUrn());
        OrganisationsSecurityUtils.canUpdateOrganisation(ctx, organisationSchemeVersion);

        // Transform
        OrganisationMetamac organisationMetamac = organisationsDto2DoMapper.organisationMetamacDtoToDo(organisationDto);

        // Update
        OrganisationMetamac organisationUpdated = getOrganisationsMetamacService().updateOrganisation(ctx, organisationMetamac);

        // Transform to DTO
        organisationDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationUpdated);
        return organisationDto;
    }

    @Override
    public OrganisationMetamacDto retrieveOrganisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationByUrn(ctx);

        // Retrieve
        OrganisationMetamac organisationMetamac = getOrganisationsMetamacService().retrieveOrganisationByUrn(ctx, urn);

        // Transform
        OrganisationMetamacDto organisationMetamacDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationMetamac);

        return organisationMetamacDto;
    }

    @Override
    public void deleteOrganisation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByOrganisationUrn(ctx, urn);
        OrganisationsSecurityUtils.canDeleteOrganisation(ctx, organisationSchemeVersion);

        // Delete
        getOrganisationsMetamacService().deleteOrganisation(ctx, urn);
    }

    @Override
    public List<ItemHierarchyDto> retrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx, String organisationSchemeUrn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationsByOrganisationSchemeUrn(ctx);

        // Retrieve
        List<OrganisationMetamac> organisations = getOrganisationsMetamacService().retrieveOrganisationsByOrganisationSchemeUrn(ctx, organisationSchemeUrn);

        // Transform
        List<ItemHierarchyDto> itemsHierarchyDto = organisationsDo2DtoMapper.organisationMetamacDoListToItemHierarchyDtoList(organisations);
        return itemsHierarchyDto;
    }

    @Override
    public MetamacCriteriaResult<OrganisationMetamacDto> findOrganisationsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canFindOrganisationsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getOrganisationMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<OrganisationMetamac> result = getOrganisationsMetamacService().findOrganisationsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<OrganisationMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultOrganisation(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public OrganisationMetamacDto retrieveMaintainerDefault(ServiceContext ctx) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationByUrn(ctx);

        // Retrieve
        OrganisationMetamac organisationMetamac = getOrganisationsMetamacService().retrieveMaintainerDefault(ctx);

        // Transform
        OrganisationMetamacDto organisationMetamacDto = organisationsDo2DtoMapper.organisationMetamacDoToDto(organisationMetamac);

        return organisationMetamacDto;
    }

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
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesByConditionWithConceptsCanBeRole(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getConceptsMetamacService().findConceptSchemesByConditionWithConceptsCanBeRole(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesByConditionWithConceptsCanBeExtended(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getConceptsMetamacService().findConceptSchemesByConditionWithConceptsCanBeExtended(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
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
    public ConceptSchemeMetamacDto endConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canEndConceptSchemeValidity(ctx, conceptSchemeVersion);

        ConceptSchemeVersionMetamac conceptSchemeEnded = getConceptsMetamacService().endConceptSchemeValidity(ctx, urn);

        // Transform
        ConceptSchemeMetamacDto conceptSchemeDto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeEnded);

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
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeRoleByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getConceptsMetamacService().findConceptsCanBeRoleByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeExtendedByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getConceptsMetamacService().findConceptsCanBeExtendedByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public void addRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn1); // concept scheme of urn1 is same that urn2
        ConceptsSecurityUtils.canAddRelatedConcept(ctx, conceptSchemeVersion);

        // Add relation
        getConceptsMetamacService().addRelatedConcept(ctx, urn1, urn2);
    }

    @Override
    public void deleteRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn1); // concept scheme of urn1 is same that urn2
        ConceptsSecurityUtils.canDeleteRelatedConcept(ctx, conceptSchemeVersion);

        // Delete concept relation
        getConceptsMetamacService().deleteRelatedConcept(ctx, urn1, urn2);
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
    public void addRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Security in concept scheme where relation will be added
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn);
        ConceptsSecurityUtils.canAddRoleConcept(ctx, conceptSchemeVersion);

        // Add relation
        getConceptsMetamacService().addRoleConcept(ctx, urn, conceptRoleUrn);
    }

    @Override
    public void deleteRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Security in concept scheme where relation will be deleted
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByConceptUrn(ctx, urn);
        ConceptsSecurityUtils.canDeleteRoleConcept(ctx, conceptSchemeVersion);

        // Delete relation
        getConceptsMetamacService().deleteRoleConcept(ctx, urn, conceptRoleUrn);
    }

    @Override
    public List<ConceptMetamacDto> retrieveRoleConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveRoleConcepts(ctx);

        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveRoleConcepts(ctx, urn);

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

    // ------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // ------------------------------------------------------------------------

    @Override
    public CategorySchemeMetamacDto createCategoryScheme(ServiceContext ctx, CategorySchemeMetamacDto categorySchemeDto) throws MetamacException {
        // Security and transform
        ItemsSecurityUtils.canCreateItemScheme(ctx);
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesDto2DoMapper.categorySchemeMetamacDtoToDo(categorySchemeDto);

        // Create
        CategorySchemeVersionMetamac categorySchemeVersionCreated = getCategoriesMetamacService().createCategoryScheme(ctx, categorySchemeVersion);

        // Transform to DTO
        categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionCreated);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto updateCategoryScheme(ServiceContext ctx, CategorySchemeMetamacDto categorySchemeDto) throws MetamacException {
        // Security
        CategorySchemeVersionMetamac categorySchemeVersionOld = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, categorySchemeDto.getUrn());
        ItemsSecurityUtils.canUpdateItemScheme(ctx, categorySchemeVersionOld.getLifeCycleMetadata().getProcStatus());

        // Transform
        CategorySchemeVersionMetamac categorySchemeVersionToUpdate = categoriesDto2DoMapper.categorySchemeMetamacDtoToDo(categorySchemeDto);

        // Update
        CategorySchemeVersionMetamac categorySchemeVersionUpdated = getCategoriesMetamacService().updateCategoryScheme(ctx, categorySchemeVersionToUpdate);

        // Transform to DTO
        categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionUpdated);
        return categorySchemeDto;
    }

    @Override
    public void deleteCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canDeleteItemScheme(ctx);

        // Delete
        getCategoriesMetamacService().deleteCategoryScheme(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<CategorySchemeMetamacDto> findCategorySchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ItemsSecurityUtils.canFindItemSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCategorySchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CategorySchemeVersionMetamac> result = getCategoriesMetamacService().findCategorySchemesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CategorySchemeMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCategorySchemeVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public CategorySchemeMetamacDto retrieveCategorySchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveItemSchemeByUrn(ctx);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, urn);

        // Transform
        CategorySchemeMetamacDto categorySchemeMetamacDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersion);

        return categorySchemeMetamacDto;
    }

    @Override
    public List<CategorySchemeMetamacDto> retrieveCategorySchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveItemSchemeVersions(ctx);

        // Retrieve
        List<CategorySchemeVersionMetamac> categorySchemeVersionMetamacs = getCategoriesMetamacService().retrieveCategorySchemeVersions(ctx, urn);

        // Transform
        List<CategorySchemeMetamacDto> categorySchemeMetamacDtos = categoriesDo2DtoMapper.categorySchemeMetamacDoListToDtoList(categorySchemeVersionMetamacs);

        return categorySchemeMetamacDtos;
    }

    @Override
    public CategorySchemeMetamacDto sendCategorySchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canSendItemSchemeToProductionValidation(ctx);

        // Send
        CategorySchemeVersionMetamac categorySchemeVersionProductionValidation = getCategoriesMetamacService().sendCategorySchemeToProductionValidation(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionProductionValidation);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto sendCategorySchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(ctx);

        // Send
        CategorySchemeVersionMetamac categorySchemeVersionDiffusionValidation = getCategoriesMetamacService().sendCategorySchemeToDiffusionValidation(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionDiffusionValidation);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto rejectCategorySchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, urn);
        ItemsSecurityUtils.canRejectItemSchemeValidation(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        CategorySchemeVersionMetamac categorySchemeVersionRejected = getCategoriesMetamacService().rejectCategorySchemeProductionValidation(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionRejected);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto rejectCategorySchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, urn);
        ItemsSecurityUtils.canRejectItemSchemeValidation(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        CategorySchemeVersionMetamac categorySchemeVersionRejected = getCategoriesMetamacService().rejectCategorySchemeDiffusionValidation(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionRejected);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto publishCategorySchemeInternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canPublishItemSchemeInternally(ctx);

        // Publish
        CategorySchemeVersionMetamac categorySchemeVersionPublished = getCategoriesMetamacService().publishInternallyCategoryScheme(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionPublished);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto publishCategorySchemeExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canPublishItemSchemeExternally(ctx);

        CategorySchemeVersionMetamac categorySchemeVersionPublished = getCategoriesMetamacService().publishExternallyCategoryScheme(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionPublished);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto versioningCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        ItemsSecurityUtils.canVersioningItemScheme(ctx);

        CategorySchemeVersionMetamac categorySchemeVersioned = getCategoriesMetamacService().versioningCategoryScheme(ctx, urnToCopy, versionType);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersioned);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto endCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canEndItemSchemeValidity(ctx);

        CategorySchemeVersionMetamac categorySchemeEnded = getCategoriesMetamacService().endCategorySchemeValidity(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeEnded);

        return categorySchemeDto;
    }

    // ------------------------------------------------------------------------
    // CATEGORIES
    // ------------------------------------------------------------------------

    @Override
    public CategoryMetamacDto createCategory(ServiceContext ctx, CategoryMetamacDto categoryMetamacDto) throws MetamacException {
        // Security
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, categoryMetamacDto.getItemSchemeVersionUrn());
        ItemsSecurityUtils.canCreateItem(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CategoryMetamac categoryMetamac = categoriesDto2DoMapper.categoryMetamacDtoToDo(categoryMetamacDto);

        // Create
        CategoryMetamac categoryCreated = getCategoriesMetamacService().createCategory(ctx, categoryMetamacDto.getItemSchemeVersionUrn(), categoryMetamac);

        // Transform to DTO
        categoryMetamacDto = categoriesDo2DtoMapper.categoryMetamacDoToDto(categoryCreated);

        return categoryMetamacDto;
    }

    @Override
    public CategoryMetamacDto updateCategory(ServiceContext ctx, CategoryMetamacDto categoryDto) throws MetamacException {
        // Security
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByCategoryUrn(ctx, categoryDto.getUrn());
        ItemsSecurityUtils.canUpdateItem(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CategoryMetamac categoryMetamac = categoriesDto2DoMapper.categoryMetamacDtoToDo(categoryDto);

        // Update
        CategoryMetamac categoryUpdated = getCategoriesMetamacService().updateCategory(ctx, categoryMetamac);

        // Transform to DTO
        categoryDto = categoriesDo2DtoMapper.categoryMetamacDoToDto(categoryUpdated);
        return categoryDto;
    }

    @Override
    public CategoryMetamacDto retrieveCategoryByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Retrieve
        CategoryMetamac categoryMetamac = getCategoriesMetamacService().retrieveCategoryByUrn(ctx, urn);

        // Transform
        CategoryMetamacDto categoryMetamacDto = categoriesDo2DtoMapper.categoryMetamacDoToDto(categoryMetamac);

        return categoryMetamacDto;
    }

    @Override
    public void deleteCategory(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByCategoryUrn(ctx, urn);
        ItemsSecurityUtils.canDeleteItem(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Delete
        getCategoriesMetamacService().deleteCategory(ctx, urn);
    }

    @Override
    public List<ItemHierarchyDto> retrieveCategoriesByCategorySchemeUrn(ServiceContext ctx, String categorySchemeUrn) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Retrieve
        List<CategoryMetamac> categories = getCategoriesMetamacService().retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn);

        // Transform
        List<ItemHierarchyDto> itemsHierarchyDto = categoriesDo2DtoMapper.categoryMetamacDoListToItemHierarchyDtoList(categories);
        return itemsHierarchyDto;
    }

    @Override
    public MetamacCriteriaResult<CategoryMetamacDto> findCategoriesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCategoryMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CategoryMetamac> result = getCategoriesMetamacService().findCategoriesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CategoryMetamacDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCategory(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public CategorisationDto createCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {

        // Security
        canModifyCategorisation(ctx, artefactCategorisedUrn);

        // Create
        Categorisation categorisation = getCategoriesMetamacService().createCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn);

        // Transform
        CategorisationDto categorisationDto = categoriesDo2DtoMapper.categorisationDoToDto(categorisation);
        return categorisationDto;
    }

    @Override
    public CategorisationDto retrieveCategorisationByUrn(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Retrieve
        Categorisation categorisation = getCategoriesMetamacService().retrieveCategorisationByUrn(ctx, urn);

        // Transform
        CategorisationDto categorisationDto = categoriesDo2DtoMapper.categorisationDoToDto(categorisation);
        return categorisationDto;
    }

    @Override
    public void deleteCategorisation(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        Categorisation categorisation = getCategoriesMetamacService().retrieveCategorisationByUrn(ctx, urn);
        canModifyCategorisation(ctx, categorisation.getArtefactCategorised().getUrn());

        // Delete
        getCategoriesMetamacService().deleteCategorisation(ctx, urn);
    }

    @Override
    public List<CategorisationDto> retrieveCategorisationsByArtefact(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ItemsSecurityUtils.canRetrieveOrFindResource(ctx);

        // Retrieve
        List<Categorisation> categorisations = getCategoriesMetamacService().retrieveCategorisationsByArtefact(ctx, urn);

        // Transform
        List<CategorisationDto> categorisationsDto = categoriesDo2DtoMapper.categorisationDoListToDtoList(categorisations);
        return categorisationsDto;
    }

    /**************************************************************************
     * PRIVATE METHODS
     *************************************************************************/
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

    // Note: only check acess to artefact. Category and maintainer must be externally published, and everyone can access to them
    private void canModifyCategorisation(ServiceContext ctx, String artefactCategorisedUrn) throws MetamacException {

        if (artefactCategorisedUrn == null) {
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.URN);
        }
        // Concept schemes
        if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_CONCEPTSCHEME_PREFIX)) {
            ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, artefactCategorisedUrn);
            ConceptsSecurityUtils.canModifyCategorisation(ctx, conceptSchemeVersion);

            // Category schemes
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_CATEGORYSCHEME_PREFIX)) {
            CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, artefactCategorisedUrn);
            ItemsSecurityUtils.canModifyCategorisation(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

            // Organisation schemes
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_ORGANISATIONUNITSCHEME_PREFIX)
                || artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_DATAPROVIDERSCHEME_PREFIX) || artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_DATACONSUMERSCHEME_PREFIX)
                || artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_AGENCYSCHEME_PREFIX)) {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, artefactCategorisedUrn);
            OrganisationsSecurityUtils.canModifyCategorisation(ctx, organisationSchemeVersion);

            // Codelists
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_CODELIST_PREFIX)) {
            CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, artefactCategorisedUrn);
            ItemsSecurityUtils.canModifyCategorisation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

            // Dsd
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX)) {
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = getDsdsMetamacService().retrieveDataStructureDefinitionByUrn(ctx, artefactCategorisedUrn);
            DataStructureDefinitionSecurityUtils.canModifyCategorisation(ctx, dataStructureDefinitionVersion);
        } else {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }
}
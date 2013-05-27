package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapper;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDto2DoMapper;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementResult;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.code.mapper.CodesDo2DtoMapper;
import org.siemac.metamac.srm.core.code.mapper.CodesDto2DoMapper;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDo2DtoMapper;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDto2DoMapper;
import org.siemac.metamac.srm.core.criteria.mapper.MetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.srm.core.criteria.mapper.SculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDo2DtoMapper;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDto2DoMapper;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapper;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDto2DoMapper;
import org.siemac.metamac.srm.core.security.CategoriesSecurityUtils;
import org.siemac.metamac.srm.core.security.CodesSecurityUtils;
import org.siemac.metamac.srm.core.security.ConceptsSecurityUtils;
import org.siemac.metamac.srm.core.security.DataStructureDefinitionSecurityUtils;
import org.siemac.metamac.srm.core.security.OrganisationsSecurityUtils;
import org.siemac.metamac.srm.core.security.TasksSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.task.domain.Task;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.task.ContentInputDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.task.TaskDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.trans.StructureMsgDto;
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
    private DataStructureDefinitionDo2DtoMapper        dataStructureDefinitionDo2DtoMapper;

    @Autowired
    private DataStructureDefinitionDto2DoMapper        dataStructureDefinitionDto2DoMapperDto2DoMapper;

    @Autowired
    private ConceptsDo2DtoMapper                       conceptsDo2DtoMapper;

    @Autowired
    private ConceptsDto2DoMapper                       conceptsDto2DoMapper;

    @Autowired
    private OrganisationsDo2DtoMapper                  organisationsDo2DtoMapper;

    @Autowired
    private OrganisationsDto2DoMapper                  organisationsDto2DoMapper;

    @Autowired
    private CategoriesDo2DtoMapper                     categoriesDo2DtoMapper;

    @Autowired
    private CategoriesDto2DoMapper                     categoriesDto2DoMapper;

    @Autowired
    private CodesDo2DtoMapper                          codesDo2DtoMapper;

    @Autowired
    private CodesDto2DoMapper                          codesDto2DoMapper;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller                            marshallerWithValidation;

    @Autowired
    private MetamacCriteria2SculptorCriteriaMapper     metamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private SculptorCriteria2MetamacCriteriaMapper     sculptorCriteria2MetamacCriteriaMapper;

    private final static Comparator<ItemSchemeVersion> ITEM_SCHEME_CREATED_DATE_DESC_COMPARATOR = new ItemSchemeCreatedDateDescComparator();
    private final static Comparator<StructureVersion>  STRUCTURE_CREATED_DATE_DESC_COMPARATOR   = new StructureCreatedDateDescComparator();

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
        DataStructureDefinitionSecurityUtils.canCreateDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

        // Create
        dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().createDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode.COPY_ALL_METADATA, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public DataStructureDefinitionMetamacDto updateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException {
        // Security and transform
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx,
                dataStructureDefinitionMetamacDto.getUrn());
        String operationNew = dataStructureDefinitionMetamacDto.getStatisticalOperation() != null ? dataStructureDefinitionMetamacDto.getStatisticalOperation().getCode() : null;
        DataStructureDefinitionSecurityUtils.canUpdateDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld, operationNew);

        // DTOs to Entities
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(dataStructureDefinitionMetamacDto);

        // Update
        dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().updateDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode.COPY_ALL_METADATA, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public void deleteDataStructureDefinition(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canDeleteDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);

        // Delete
        getDataStructureDefinitionMetamacService().deleteDataStructureDefinition(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> findDataStructureDefinitionsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canFindDataStructureDefinitionByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getDataStructureDefinitionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<DataStructureDefinitionVersionMetamac> result = getDataStructureDefinitionMetamacService().findDataStructureDefinitionsByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDataStructureDefinition(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public DataStructureDefinitionMetamacDto retrieveDataStructureDefinition(ServiceContext ctx, String urn, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canRetrieveDataStructureDefinitionByUrn(ctx);

        // Load Dsd
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(typeDozerCopyMode, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public DataStructureDefinitionMetamacDto retrieveDataStructureDefinitionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canRetrieveDataStructureDefinitionByUrn(ctx);

        // Search
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode.COPY_ALL_METADATA, dataStructureDefinitionVersionMetamac);
    }

    @Override
    public List<DataStructureDefinitionMetamacBasicDto> retrieveDataStructureDefinitionVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canDataStructureDefinitionVersions(ctx);

        // Retrieve and sort by date desc
        List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionsMetamac = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionVersions(ctx, urn);
        List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionsSortedByCreationDate = new ArrayList<DataStructureDefinitionVersionMetamac>(dataStructureDefinitionVersionsMetamac);
        Collections.sort(dataStructureDefinitionVersionsSortedByCreationDate, STRUCTURE_CREATED_DATE_DESC_COMPARATOR);

        // Transform
        List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper
                .dataStructureDefinitionMetamacDoListToDtoList(dataStructureDefinitionVersionsSortedByCreationDate);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto sendDataStructureDefinitionToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canSendDataStructureDefinitionToProductionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().sendDataStructureDefinitionToProductionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto sendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canSendDataStructureDefinitionToDiffusionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().sendDataStructureDefinitionToDiffusionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto rejectDataStructureDefinitionProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canRejectDataStructureDefinitionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().rejectDataStructureDefinitionProductionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto rejectDataStructureDefinitionDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canRejectDataStructureDefinitionValidation(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().rejectDataStructureDefinitionDiffusionValidation(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto publishDataStructureDefinitionInternally(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canPublishDataStructureDefinitionInternally(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().publishInternallyDataStructureDefinition(ctx, urn, forceLatestFinal);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public DataStructureDefinitionMetamacDto publishDataStructureDefinitionExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canPublishDataStructureDefinitionExternally(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().publishExternallyDataStructureDefinition(ctx, urn);

        // Transform to Dto
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        return dataStructureDefinitionMetamacDto;
    }

    @Override
    public TaskInfo copyDataStructureDefinition(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        DataStructureDefinitionSecurityUtils.canCopyDataStructureDefinition(ctx);

        return getDataStructureDefinitionMetamacService().copyDataStructureDefinition(ctx, urnToCopy);
    }

    @Override
    public TaskInfo versioningDataStructureDefinition(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);
        DataStructureDefinitionSecurityUtils.canVersioningDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        if (GeneratorUrnUtils.isTemporalUrn(urnToCopy)) {
            return getDataStructureDefinitionMetamacService().createVersionFromTemporalDataStructureDefinition(ctx, urnToCopy, versionType);
        } else {
            return getDataStructureDefinitionMetamacService().versioningDataStructureDefinition(ctx, urnToCopy, versionType);
        }
    }

    @Override
    public TaskInfo createTemporalVersionDataStructureDefinition(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnToCopy);
        DataStructureDefinitionSecurityUtils.canCreateDataStructureDefinitionTemporalVersion(ctx, dataStructureDefinitionVersionMetamacOld);

        return getDataStructureDefinitionMetamacService().createTemporalDataStructureDefinition(ctx, urnToCopy);
    }

    @Override
    public DataStructureDefinitionMetamacDto endDataStructureDefinitionValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urn);
        DataStructureDefinitionSecurityUtils.canEndDataStructureDefinitionValidity(ctx, dataStructureDefinitionVersionMetamacOld);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().endDataStructureDefinitionValidity(ctx, urn);

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
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd);

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
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(descriptorDto);

        // Save
        componentListDescriptor = getDataStructureDefinitionMetamacService().saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(),
                componentListDescriptor);

        // Entities to DTOs
        return getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.COPY_ALL_METADATA, componentListDescriptor);
    }

    @Override
    public void deleteDescriptorForDataStructureDefinition(ServiceContext ctx, String urnDsd, DescriptorDto descriptorDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(descriptorDto);

        // Delete descriptor for DSD
        getDataStructureDefinitionMetamacService()
                .deleteDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), componentListDescriptor);
    }

    /**************************************************************************
     * Component
     **************************************************************************/

    @Override
    public ComponentDto saveComponentForDataStructureDefinition(ServiceContext ctx, String urnDsd, ComponentDto componentDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canSaveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto);

        // Save component for DSD
        component = getDataStructureDefinitionMetamacService().saveComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), component);

        // Entitys to DTOs
        return getDo2DtoMapper().componentToComponentDto(TypeDozerCopyMode.COPY_ALL_METADATA, component);
    }

    @Override
    public void deleteComponentForDataStructureDefinition(ServiceContext ctx, String urnDsd, ComponentDto componentDto) throws MetamacException {
        // Security
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd); // Load DSD
        DataStructureDefinitionSecurityUtils.canDeleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld);

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto);

        // Delete component for DSD
        getDataStructureDefinitionMetamacService().deleteComponentForDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamacOld.getMaintainableArtefact().getUrn(), component);
    }

    /**************************************************************************
     * Import/Export
     **************************************************************************/
    @Override
    public void importSDMXStructureMsgInBackground(ServiceContext ctx, ContentInputDto contentDto) throws MetamacException {
        // Security
        TasksSecurityUtils.canImportStructure(ctx);

        // Import
        getTasksMetamacService().importSDMXStructureInBackground(ctx, contentDto.getInput(), contentDto.getName());
    }

    @Override
    public MetamacCriteriaResult<TaskDto> findTasksByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        TasksSecurityUtils.canFindTasksByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getDataStructureDefinitionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<Task> result = getTasksMetamacService().findTasksByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<TaskDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultTask(result, sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
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

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn)
            throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getDataStructureDefinitionMetamacService().findConceptsCanBeDsdPrimaryMeasureByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn)
            throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeDsdTimeDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getDataStructureDefinitionMetamacService().findConceptsCanBeDsdTimeDimensionByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn)
            throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeDsdMeasureDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getDataStructureDefinitionMetamacService().findConceptsCanBeDsdMeasureDimensionByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeDsdDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getDataStructureDefinitionMetamacService().findConceptsCanBeDsdDimensionByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesWithConceptsCanBeDsdRoleByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesWithConceptsCanBeDsdRoleByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeDsdRoleByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getDataStructureDefinitionMetamacService().findConceptsCanBeDsdRoleByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String conceptUrn)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canFindCodelistsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistVersionMetamac> result = getDataStructureDefinitionMetamacService().findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), conceptUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultCodelistToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canFindCodelistsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistVersionMetamac> result = getDataStructureDefinitionMetamacService().findCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultCodelistToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria)
            throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getDataStructureDefinitionMetamacService().findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeDsdAttributeByCondition(ServiceContext ctx, MetamacCriteria criteria, String dsdUrn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getDataStructureDefinitionMetamacService().findConceptsCanBeDsdAttributeByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), dsdUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultConceptToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(ServiceContext ctx, MetamacCriteria criteria, String conceptUrn)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canFindCodelistsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistVersionMetamac> result = getDataStructureDefinitionMetamacService().findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), conceptUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultCodelistToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dimensionUrn)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistOrderVisualisation(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistOrderVisualisation> result = getDataStructureDefinitionMetamacService().findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), dimensionUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultCodelistOrderVisualisationToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(ServiceContext ctx, MetamacCriteria criteria, String dimensionUrn)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistOpennessVisualisation(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistOpennessVisualisation> result = getDataStructureDefinitionMetamacService().findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(ctx,
                sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(), dimensionUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultCodelistOpennessVisualisationToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------

    @Override
    public CodelistMetamacDto retrieveCodelistByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveCodelistByUrn(ctx);

        // Retrieve
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, urn);

        // Transform
        CodelistMetamacDto codelistMetamacDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersion);

        return codelistMetamacDto;
    }

    @Override
    public CodelistMetamacDto createCodelist(ServiceContext ctx, CodelistMetamacDto codelistDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCreateCodelist(ctx);

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
        CodesSecurityUtils.canUpdateCodelist(ctx, codelistVersionOld.getLifeCycleMetadata().getProcStatus());

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
        CodesSecurityUtils.canDeleteCodelist(ctx);

        // Delete
        getCodesMetamacService().deleteCodelist(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<CodelistMetamacBasicDto> findCodelistsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canFindCodelistsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistVersionMetamac> result = getCodesMetamacService().findCodelistsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CodelistMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCodelistVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public List<CodelistMetamacBasicDto> retrieveCodelistVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveCodelistVersions(ctx);

        // Retrieve and sort by date desc
        List<CodelistVersionMetamac> codelistVersions = getCodesMetamacService().retrieveCodelistVersions(ctx, urn);
        List<CodelistVersionMetamac> codelistVersionsSortedByCreationDate = new ArrayList<CodelistVersionMetamac>(codelistVersions);
        Collections.sort(codelistVersionsSortedByCreationDate, ITEM_SCHEME_CREATED_DATE_DESC_COMPARATOR);

        // Transform
        List<CodelistMetamacBasicDto> codelistMetamacDtos = codesDo2DtoMapper.codelistMetamacDoListToDtoList(codelistVersionsSortedByCreationDate);

        return codelistMetamacDtos;
    }

    @Override
    public CodelistMetamacDto sendCodelistToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canSendCodelistToProductionValidation(ctx);

        // Send
        CodelistVersionMetamac codelistVersionProductionValidation = getCodesMetamacService().sendCodelistToProductionValidation(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionProductionValidation);
        return codelistDto;
    }

    @Override
    public CodelistMetamacDto sendCodelistToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canSendCodelistToDiffusionValidation(ctx);

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
        CodesSecurityUtils.canRejectCodelistValidation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

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
        CodesSecurityUtils.canRejectCodelistValidation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        CodelistVersionMetamac codelistVersionRejected = getCodesMetamacService().rejectCodelistDiffusionValidation(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionRejected);
        return codelistDto;
    }

    @Override
    public TaskInfo publishCodelistInternally(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        // Security
        CodesSecurityUtils.canPublishCodelistInternally(ctx);

        // Publish
        TaskInfo versioningResult = getCodesMetamacService().publishInternallyCodelist(ctx, urn, forceLatestFinal, Boolean.TRUE);

        return versioningResult;
    }

    @Override
    public CodelistMetamacDto publishCodelistExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canPublishCodelistExternally(ctx);

        CodelistVersionMetamac codelistVersionPublished = getCodesMetamacService().publishExternallyCodelist(ctx, urn);

        // Transform to DTO
        CodelistMetamacDto codelistDto = codesDo2DtoMapper.codelistMetamacDoToDto(codelistVersionPublished);
        return codelistDto;
    }

    @Override
    public TaskInfo copyCodelist(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        CodesSecurityUtils.canCopyCodelist(ctx);

        // Copy
        return getCodesMetamacService().copyCodelist(ctx, urnToCopy);
    }

    @Override
    public TaskInfo versioningCodelist(ServiceContext ctx, String urnToCopy, Boolean versioningCodes, VersionTypeEnum versionType) throws MetamacException {
        // Security
        CodesSecurityUtils.canVersioningCodelist(ctx);

        TaskInfo versioningResult = null;
        if (GeneratorUrnUtils.isTemporalUrn(urnToCopy)) {
            versioningResult = getCodesMetamacService().createVersionFromTemporalCodelist(ctx, urnToCopy, versionType);
        } else {
            versioningResult = getCodesMetamacService().versioningCodelist(ctx, urnToCopy, versioningCodes, versionType);
        }

        return versioningResult;
    }

    @Override
    public TaskInfo createTemporalVersionCodelist(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        CodesSecurityUtils.canCreateCodelistTemporalVersion(ctx);

        return getCodesMetamacService().createTemporalCodelist(ctx, urnToCopy);
    }

    @Override
    public CodelistMetamacDto endCodelistValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canEndCodelistValidity(ctx);

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
        CodesSecurityUtils.canCreateCode(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodeMetamac codeMetamac = codesDto2DoMapper.codeDtoToDo(codeMetamacDto);

        // Create
        CodeMetamac codeMetamacCreated = getCodesMetamacService().createCode(ctx, codeMetamacDto.getItemSchemeVersionUrn(), codeMetamac);

        // Transform to DTO
        codeMetamacDto = codesDo2DtoMapper.codeMetamacDoToDto(codeMetamacCreated);

        return codeMetamacDto;
    }

    @Override
    public void copyCodesInCodelist(ServiceContext ctx, String codelistSourceUrn, String codelistTargetUrn, List<CodeToCopy> codesToCopy) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistTargetUrn);
        CodesSecurityUtils.canCreateCode(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Copy
        getCodesMetamacService().copyCodesInCodelist(ctx, codelistSourceUrn, codelistTargetUrn, codesToCopy);
    }

    @Override
    public void importCodesTsvInBackground(ServiceContext ctx, String codelistUrn, InputStream tsvStream, String fileName, boolean updateAlreadyExisting) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistUrn);
        CodesSecurityUtils.canImportCodes(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Import in background
        getTasksMetamacService().importCodesTsvInBackground(ctx, codelistUrn, tsvStream, fileName, updateAlreadyExisting);
    }

    @Override
    public void importCodeOrdersTsvInBackground(ServiceContext ctx, String codelistUrn, InputStream tsvStream, String fileName) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistUrn);
        CodesSecurityUtils.canImportCodelistOrderVisualisations(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Import in background
        getTasksMetamacService().importCodeOrdersTsvInBackground(ctx, codelistUrn, tsvStream, fileName);
    }

    @Override
    public String exportCodesTsv(ServiceContext ctx, String codelistUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canExportCodesTsv(ctx);

        // Export
        return getCodesMetamacService().exportCodesTsv(ctx, codelistUrn);
    }

    @Override
    public String exportCodeOrdersTsv(ServiceContext ctx, String codelistUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canExportCodeOrdersTsv(ctx);

        // Export
        return getCodesMetamacService().exportCodeOrdersTsv(ctx, codelistUrn);
    }

    @Override
    public CodeMetamacDto updateCode(ServiceContext ctx, CodeMetamacDto codeDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, codeDto.getUrn());
        CodesSecurityUtils.canUpdateCode(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodeMetamac codeMetamac = codesDto2DoMapper.codeDtoToDo(codeDto);

        // Update
        CodeMetamac conceptUpdated = getCodesMetamacService().updateCode(ctx, codeMetamac);

        // Transform to DTO
        codeDto = codesDo2DtoMapper.codeMetamacDoToDto(conceptUpdated);
        return codeDto;
    }

    @Override
    public CodeMetamacDto updateCodeVariableElement(ServiceContext ctx, String codeUrn, String variableElementUrn) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, codeUrn);
        CodesSecurityUtils.canUpdateCodeVariableElement(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Update
        CodeMetamac conceptUpdated = getCodesMetamacService().updateCodeVariableElement(ctx, codeUrn, variableElementUrn);

        // Transform to DTO
        CodeMetamacDto codeDto = codesDo2DtoMapper.codeMetamacDoToDto(conceptUpdated);
        return codeDto;
    }

    @Override
    public void updateCodesVariableElements(ServiceContext ctx, String codelistUrn, Map<Long, Long> variableElementsIdByCodeId) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistUrn);
        CodesSecurityUtils.canUpdateCodeVariableElement(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Update
        getCodesMetamacService().updateCodesVariableElements(ctx, codelistUrn, variableElementsIdByCodeId);
    }

    @Override
    public void updateCodeParent(ServiceContext ctx, String codeUrn, String newParentUrn) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, codeUrn);
        CodesSecurityUtils.canUpdateCode(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Update parent
        getCodesMetamacService().updateCodeParent(ctx, codeUrn, newParentUrn);
    }

    @Override
    public void updateCodeInOrderVisualisation(ServiceContext ctx, String codeUrn, String codelistOrderVisualisationUrn, Integer newCodeIndex) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, codeUrn);
        CodesSecurityUtils.canUpdateCode(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Update order
        getCodesMetamacService().updateCodeInOrderVisualisation(ctx, codeUrn, codelistOrderVisualisationUrn, newCodeIndex);
    }

    @Override
    public void updateCodesInOpennessVisualisation(ServiceContext ctx, String codelistOpennessVisualisationUrn, Map<String, Boolean> openness) throws MetamacException {
        // Security
        CodelistOpennessVisualisation codelistOpennessVisualisation = getCodesMetamacService().retrieveCodelistOpennessVisualisationByUrn(ctx, codelistOpennessVisualisationUrn);
        CodesSecurityUtils.canUpdateCode(ctx, codelistOpennessVisualisation.getCodelistVersion().getLifeCycleMetadata().getProcStatus());

        // Update order
        getCodesMetamacService().updateCodesInOpennessVisualisation(ctx, codelistOpennessVisualisationUrn, openness);
    }

    @Override
    public CodeMetamacDto retrieveCodeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveCodeByUrn(ctx);

        // Retrieve
        CodeMetamac codeMetamac = getCodesMetamacService().retrieveCodeByUrn(ctx, urn);

        // Transform
        CodeMetamacDto codeMetamacDto = codesDo2DtoMapper.codeMetamacDoToDto(codeMetamac);

        return codeMetamacDto;
    }

    @Override
    public MetamacCriteriaResult<CodeMetamacBasicDto> findCodesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canFindCodesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodeMetamac> result = getCodesMetamacService().findCodesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CodeMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCode(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public void deleteCode(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByCodeUrn(ctx, urn);
        CodesSecurityUtils.canDeleteCode(ctx, codelistVersion);

        // Delete
        getCodesMetamacService().deleteCode(ctx, urn);
    }

    @Override
    public List<CodeMetamacVisualisationResult> retrieveCodesByCodelistUrn(ServiceContext ctx, String codelistUrn, String locale, String orderVisualisationUrn, String opennessVisualisationUrn)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveCodesByCodelistUrn(ctx);

        // Retrieve
        return getCodesMetamacService().retrieveCodesByCodelistUrn(ctx, codelistUrn, locale, orderVisualisationUrn, opennessVisualisationUrn);
    }

    @Override
    public List<CodeVariableElementNormalisationResult> normaliseVariableElementsToCodes(ServiceContext ctx, String codelistUrn, String locale, boolean proposeOnlyWithoutVariableElement)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canNormaliseVariableElementsToCodes(ctx);

        // Normalise
        return getCodesMetamacService().normaliseVariableElementsToCodes(ctx, codelistUrn, locale, proposeOnlyWithoutVariableElement);
    }

    // ------------------------------------------------------------------------
    // ORDER VISUALISATIONS
    // ------------------------------------------------------------------------

    @Override
    public CodelistVisualisationDto retrieveCodelistOrderVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistOrderVisualisation(ctx);

        // Retrieve
        CodelistOrderVisualisation codelistOrderVisualisation = getCodesMetamacService().retrieveCodelistOrderVisualisationByUrn(ctx, urn);

        // Transform
        CodelistVisualisationDto codelistOrderVisualisationDto = codesDo2DtoMapper.codelistOrderVisualisationDoToDto(codelistOrderVisualisation);
        return codelistOrderVisualisationDto;
    }

    @Override
    public CodelistVisualisationDto createCodelistOrderVisualisation(ServiceContext ctx, CodelistVisualisationDto codelistOrderVisualisationDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistOrderVisualisationDto.getCodelist().getUrn());
        CodesSecurityUtils.canCrudCodelistOrderVisualisation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodelistOrderVisualisation codelistOrderVisualisation = codesDto2DoMapper.codelistOrderVisualisationDtoToDo(codelistOrderVisualisationDto);

        // Create
        CodelistOrderVisualisation codelistOrderVisualisationCreated = getCodesMetamacService().createCodelistOrderVisualisation(ctx, codelistOrderVisualisationDto.getCodelist().getUrn(),
                codelistOrderVisualisation);

        // Transform to DTO
        codelistOrderVisualisationDto = codesDo2DtoMapper.codelistOrderVisualisationDoToDto(codelistOrderVisualisationCreated);
        return codelistOrderVisualisationDto;
    }

    @Override
    public CodelistVisualisationDto updateCodelistOrderVisualisation(ServiceContext ctx, CodelistVisualisationDto codelistOrderVisualisationDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistOrderVisualisationDto.getCodelist().getUrn());
        CodesSecurityUtils.canCrudCodelistOrderVisualisation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodelistOrderVisualisation codelistOrderVisualisationToUpdate = codesDto2DoMapper.codelistOrderVisualisationDtoToDo(codelistOrderVisualisationDto);

        // Update
        CodelistOrderVisualisation codelistOrderVisualisationUpdated = getCodesMetamacService().updateCodelistOrderVisualisation(ctx, codelistOrderVisualisationToUpdate);

        // Transform to DTO
        codelistOrderVisualisationDto = codesDo2DtoMapper.codelistOrderVisualisationDoToDto(codelistOrderVisualisationUpdated);
        return codelistOrderVisualisationDto;
    }

    @Override
    public void deleteCodelistOrderVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodelistOrderVisualisation codelistOrderVisualisation = getCodesMetamacService().retrieveCodelistOrderVisualisationByUrn(ctx, urn);
        CodesSecurityUtils.canCrudCodelistOrderVisualisation(ctx, codelistOrderVisualisation.getCodelistVersion().getLifeCycleMetadata().getProcStatus());

        // Delete
        getCodesMetamacService().deleteCodelistOrderVisualisation(ctx, urn);
    }

    @Override
    public List<CodelistVisualisationDto> retrieveCodelistOrderVisualisationsByCodelist(ServiceContext ctx, String codelistUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistOrderVisualisation(ctx);

        // Retrieve
        List<CodelistOrderVisualisation> codelistOrderVisualisations = getCodesMetamacService().retrieveCodelistOrderVisualisationsByCodelist(ctx, codelistUrn);

        // Transform
        List<CodelistVisualisationDto> codelistOrderVisualisationsDto = codesDo2DtoMapper.codelistOrderVisualisationsDoToDto(codelistOrderVisualisations);
        return codelistOrderVisualisationsDto;
    }

    // ------------------------------------------------------------------------
    // OPENNESS VISUALISATIONS
    // ------------------------------------------------------------------------

    @Override
    public CodelistVisualisationDto retrieveCodelistOpennessVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistOpennessVisualisation(ctx);

        // Retrieve
        CodelistOpennessVisualisation codelistOpennessVisualisation = getCodesMetamacService().retrieveCodelistOpennessVisualisationByUrn(ctx, urn);

        // Transform
        CodelistVisualisationDto codelistOpennessVisualisationDto = codesDo2DtoMapper.codelistOpennessVisualisationDoToDto(codelistOpennessVisualisation);
        return codelistOpennessVisualisationDto;
    }

    @Override
    public CodelistVisualisationDto createCodelistOpennessVisualisation(ServiceContext ctx, CodelistVisualisationDto codelistOpennessVisualisationDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistOpennessVisualisationDto.getCodelist().getUrn());
        CodesSecurityUtils.canCrudCodelistOpennessVisualisation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodelistOpennessVisualisation codelistOpennessVisualisation = codesDto2DoMapper.codelistOpennessVisualisationDtoToDo(codelistOpennessVisualisationDto);

        // Create
        CodelistOpennessVisualisation codelistOpennessVisualisationCreated = getCodesMetamacService().createCodelistOpennessVisualisation(ctx, codelistOpennessVisualisationDto.getCodelist().getUrn(),
                codelistOpennessVisualisation);

        // Transform to DTO
        codelistOpennessVisualisationDto = codesDo2DtoMapper.codelistOpennessVisualisationDoToDto(codelistOpennessVisualisationCreated);
        return codelistOpennessVisualisationDto;
    }

    @Override
    public CodelistVisualisationDto updateCodelistOpennessVisualisation(ServiceContext ctx, CodelistVisualisationDto codelistOpennessVisualisationDto) throws MetamacException {
        // Security
        CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, codelistOpennessVisualisationDto.getCodelist().getUrn());
        CodesSecurityUtils.canCrudCodelistOpennessVisualisation(ctx, codelistVersion.getLifeCycleMetadata().getProcStatus());

        // Transform
        CodelistOpennessVisualisation codelistOpennessVisualisationToUpdate = codesDto2DoMapper.codelistOpennessVisualisationDtoToDo(codelistOpennessVisualisationDto);

        // Update
        CodelistOpennessVisualisation codelistOpennessVisualisationUpdated = getCodesMetamacService().updateCodelistOpennessVisualisation(ctx, codelistOpennessVisualisationToUpdate);

        // Transform to DTO
        codelistOpennessVisualisationDto = codesDo2DtoMapper.codelistOpennessVisualisationDoToDto(codelistOpennessVisualisationUpdated);
        return codelistOpennessVisualisationDto;
    }

    @Override
    public void deleteCodelistOpennessVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodelistOpennessVisualisation codelistOpennessVisualisation = getCodesMetamacService().retrieveCodelistOpennessVisualisationByUrn(ctx, urn);
        CodesSecurityUtils.canCrudCodelistOpennessVisualisation(ctx, codelistOpennessVisualisation.getCodelistVersion().getLifeCycleMetadata().getProcStatus());

        // Delete
        getCodesMetamacService().deleteCodelistOpennessVisualisation(ctx, urn);
    }

    @Override
    public List<CodelistVisualisationDto> retrieveCodelistOpennessVisualisationsByCodelist(ServiceContext ctx, String codelistUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistOpennessVisualisation(ctx);

        // Retrieve
        List<CodelistOpennessVisualisation> codelistOpennessVisualisations = getCodesMetamacService().retrieveCodelistOpennessVisualisationsByCodelist(ctx, codelistUrn);

        // Transform
        List<CodelistVisualisationDto> codelistOpennessVisualisationsDto = codesDo2DtoMapper.codelistOpennessVisualisationsDoToDto(codelistOpennessVisualisations);
        return codelistOpennessVisualisationsDto;
    }

    // ------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------

    @Override
    public CodelistFamilyDto retrieveCodelistFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistFamily(ctx);

        // Retrieve
        CodelistFamily codelistFamily = getCodesMetamacService().retrieveCodelistFamilyByUrn(ctx, urn);

        // Transform
        CodelistFamilyDto codelistFamilyDto = codesDo2DtoMapper.codelistFamilyDoToDto(codelistFamily);
        return codelistFamilyDto;
    }

    @Override
    public CodelistFamilyDto createCodelistFamily(ServiceContext ctx, CodelistFamilyDto codelistFamilyDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudCodelistFamily(ctx);

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
        CodesSecurityUtils.canCrudCodelistFamily(ctx);

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
        CodesSecurityUtils.canCrudCodelistFamily(ctx);

        // Delete
        getCodesMetamacService().deleteCodelistFamily(ctx, urn);
    }

    @Override
    public void addCodelistsToCodelistFamily(ServiceContext ctx, List<String> codelistUrns, String codelistFamilyUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudCodelistFamily(ctx);

        // Add
        getCodesMetamacService().addCodelistsToCodelistFamily(ctx, codelistUrns, codelistFamilyUrn);
    }

    @Override
    public void removeCodelistFromCodelistFamily(ServiceContext ctx, String codelistUrn, String codelistFamilyUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudCodelistFamily(ctx);

        // Delete
        getCodesMetamacService().removeCodelistFromCodelistFamily(ctx, codelistUrn, codelistFamilyUrn);
    }

    @Override
    public MetamacCriteriaResult<CodelistFamilyBasicDto> findCodelistFamiliesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindCodelistFamily(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistFamilyCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistFamily> result = getCodesMetamacService().findCodelistFamiliesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CodelistFamilyBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCodelistFamily(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------

    @Override
    public VariableFamilyDto retrieveVariableFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableFamily(ctx);

        // Retrieve
        VariableFamily variableFamily = getCodesMetamacService().retrieveVariableFamilyByUrn(ctx, urn);

        // Transform
        VariableFamilyDto variableFamilyDto = codesDo2DtoMapper.variableFamilyDoToDto(variableFamily);
        return variableFamilyDto;
    }

    @Override
    public VariableFamilyDto createVariableFamily(ServiceContext ctx, VariableFamilyDto variableFamilyDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableFamily(ctx);

        // Transform
        VariableFamily variableFamily = codesDto2DoMapper.variableFamilyDtoToDo(variableFamilyDto);

        // Create
        VariableFamily variableFamilyCreated = getCodesMetamacService().createVariableFamily(ctx, variableFamily);

        // Transform to DTO
        variableFamilyDto = codesDo2DtoMapper.variableFamilyDoToDto(variableFamilyCreated);
        return variableFamilyDto;
    }

    @Override
    public VariableFamilyDto updateVariableFamily(ServiceContext ctx, VariableFamilyDto variableFamilyDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableFamily(ctx);

        // Transform
        VariableFamily variableFamilyToUpdate = codesDto2DoMapper.variableFamilyDtoToDo(variableFamilyDto);

        // Update
        VariableFamily variableFamilyUpdated = getCodesMetamacService().updateVariableFamily(ctx, variableFamilyToUpdate);

        // Transform to DTO
        variableFamilyDto = codesDo2DtoMapper.variableFamilyDoToDto(variableFamilyUpdated);
        return variableFamilyDto;
    }

    @Override
    public void deleteVariableFamily(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableFamily(ctx);

        // Delete
        getCodesMetamacService().deleteVariableFamily(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<VariableFamilyBasicDto> findVariableFamiliesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableFamily(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getVariableFamilyCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<VariableFamily> result = getCodesMetamacService().findVariableFamiliesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<VariableFamilyBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultVariableFamily(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------

    @Override
    public VariableDto retrieveVariableByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariable(ctx);

        // Retrieve
        Variable variable = getCodesMetamacService().retrieveVariableByUrn(ctx, urn);

        // Transform
        VariableDto variableDto = codesDo2DtoMapper.variableDoToDto(variable);
        return variableDto;
    }

    @Override
    public VariableDto createVariable(ServiceContext ctx, VariableDto variableDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariable(ctx);

        // Transform
        Variable variable = codesDto2DoMapper.variableDtoToDo(variableDto);

        // Create
        Variable variableCreated = getCodesMetamacService().createVariable(ctx, variable);

        // Transform to DTO
        variableDto = codesDo2DtoMapper.variableDoToDto(variableCreated);
        return variableDto;
    }

    @Override
    public VariableDto updateVariable(ServiceContext ctx, VariableDto variableDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariable(ctx);

        // Transform
        Variable variableToUpdate = codesDto2DoMapper.variableDtoToDo(variableDto);

        // Update
        Variable variableUpdated = getCodesMetamacService().updateVariable(ctx, variableToUpdate);

        // Transform to DTO
        variableDto = codesDo2DtoMapper.variableDoToDto(variableUpdated);
        return variableDto;
    }

    @Override
    public void deleteVariable(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariable(ctx);

        // Delete
        getCodesMetamacService().deleteVariable(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<VariableBasicDto> findVariablesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariable(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getVariableCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<Variable> result = getCodesMetamacService().findVariablesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<VariableBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultVariable(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public void addVariablesToVariableFamily(ServiceContext ctx, List<String> variableUrns, String variableFamilyUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canAddVariablesToVariableFamily(ctx);

        // Add
        getCodesMetamacService().addVariablesToVariableFamily(ctx, variableUrns, variableFamilyUrn);
    }

    @Override
    public void removeVariableFromVariableFamily(ServiceContext ctx, String variableUrn, String variableFamilyUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRemoveVariableFromVariableFamily(ctx);

        // Delete
        getCodesMetamacService().removeVariableFromVariableFamily(ctx, variableUrn, variableFamilyUrn);
    }

    // ------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------

    @Override
    public VariableElementDto retrieveVariableElementByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Retrieve
        VariableElement variableElement = getCodesMetamacService().retrieveVariableElementByUrn(ctx, urn);

        // Transform
        VariableElementDto variableElementDto = codesDo2DtoMapper.variableElementDoToDto(variableElement);
        return variableElementDto;
    }

    @Override
    public VariableElementDto createVariableElement(ServiceContext ctx, VariableElementDto variableElementDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableElement(ctx);

        // Transform
        VariableElement variableElement = codesDto2DoMapper.variableElementDtoToDo(variableElementDto);

        // Create
        VariableElement variableElementCreated = getCodesMetamacService().createVariableElement(ctx, variableElement);

        // Transform to DTO
        variableElementDto = codesDo2DtoMapper.variableElementDoToDto(variableElementCreated);
        return variableElementDto;
    }

    @Override
    public void importVariableElementsTsvInBackground(ServiceContext ctx, String variableUrn, InputStream tsvStream, String fileName, boolean updateAlreadyExisting) throws MetamacException {
        // Security
        CodesSecurityUtils.canImportVariableElements(ctx);

        // Import in background
        getTasksMetamacService().importVariableElementsTsvInBackground(ctx, variableUrn, tsvStream, fileName, updateAlreadyExisting);
    }

    @Override
    public VariableElementDto updateVariableElement(ServiceContext ctx, VariableElementDto variableElementDto) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableElement(ctx);

        // Transform
        VariableElement variableElementToUpdate = codesDto2DoMapper.variableElementDtoToDo(variableElementDto);

        // Update
        VariableElement variableElementUpdated = getCodesMetamacService().updateVariableElement(ctx, variableElementToUpdate);

        // Transform to DTO
        variableElementDto = codesDo2DtoMapper.variableElementDoToDto(variableElementUpdated);
        return variableElementDto;
    }

    @Override
    public void deleteVariableElement(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableElement(ctx);

        // Delete
        getCodesMetamacService().deleteVariableElement(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<VariableElementBasicDto> findVariableElementsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getVariableElementCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<VariableElement> result = getCodesMetamacService().findVariableElementsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<VariableElementBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultVariableElement(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findVariableElementsForCodesByCondition(ServiceContext ctx, MetamacCriteria criteria, String codelistUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getVariableElementCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<VariableElement> result = getCodesMetamacService().findVariableElementsForCodesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter(),
                codelistUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultVariableElementRelatedResource(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public List<VariableElementResult> retrieveVariableElementsByVariable(ServiceContext ctx, String variableUrn, String locale) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Find
        return getCodesMetamacService().retrieveVariableElementsByVariable(ctx, variableUrn, locale);
    }

    @Override
    public void addVariableElementsToVariable(ServiceContext ctx, List<String> variableElementUrns, String variableUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canAddVariableElementsToVariable(ctx);

        // Add
        getCodesMetamacService().addVariableElementsToVariable(ctx, variableElementUrns, variableUrn);
    }

    @Override
    public VariableElementOperationDto createVariableElementFusionOperation(ServiceContext ctx, List<String> sourcesUrn, String targetUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableElement(ctx);

        // Create
        VariableElementOperation variableElementOperationCreated = getCodesMetamacService().createVariableElementFusionOperation(ctx, sourcesUrn, targetUrn);

        // Transform to DTO
        VariableElementOperationDto variableElementOperationDto = codesDo2DtoMapper.variableElementOperationDoToDto(variableElementOperationCreated);
        return variableElementOperationDto;
    }

    @Override
    public VariableElementOperationDto createVariableElementSegregationOperation(ServiceContext ctx, String sourceUrn, List<String> targetsUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableElement(ctx);

        // Create
        VariableElementOperation variableElementOperationCreated = getCodesMetamacService().createVariableElementSegregationOperation(ctx, sourceUrn, targetsUrn);

        // Transform to DTO
        VariableElementOperationDto variableElementOperationDto = codesDo2DtoMapper.variableElementOperationDoToDto(variableElementOperationCreated);
        return variableElementOperationDto;
    }

    @Override
    public VariableElementOperationDto retrieveVariableElementOperationByCode(ServiceContext ctx, String code) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Retrieve
        VariableElementOperation variableElementOperation = getCodesMetamacService().retrieveVariableElementOperationByCode(ctx, code);

        // Transform
        VariableElementOperationDto variableElementOperationDto = codesDo2DtoMapper.variableElementOperationDoToDto(variableElementOperation);
        return variableElementOperationDto;
    }

    @Override
    public void deleteVariableElementOperation(ServiceContext ctx, String code) throws MetamacException {
        // Security
        CodesSecurityUtils.canCrudVariableElement(ctx);

        // Delete
        getCodesMetamacService().deleteVariableElementOperation(ctx, code);
    }

    @Override
    public List<VariableElementOperationDto> retrieveVariableElementsOperationsByVariable(ServiceContext ctx, String variableUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Retrieve
        List<VariableElementOperation> result = getCodesMetamacService().retrieveVariableElementsOperationsByVariable(ctx, variableUrn);

        // Transform
        List<VariableElementOperationDto> resultDto = codesDo2DtoMapper.variableElementOperationsDoToDto(result);
        return resultDto;
    }

    @Override
    public List<VariableElementOperationDto> retrieveVariableElementsOperationsByVariableElement(ServiceContext ctx, String variableElementUrn) throws MetamacException {
        // Security
        CodesSecurityUtils.canRetrieveOrFindVariableElement(ctx);

        // Retrieve
        List<VariableElementOperation> result = getCodesMetamacService().retrieveVariableElementsOperationsByVariableElement(ctx, variableElementUrn);

        // Transform
        List<VariableElementOperationDto> resultDto = codesDo2DtoMapper.variableElementOperationsDoToDto(result);
        return resultDto;
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
    public MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto> findOrganisationSchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canFindOrganisationSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getOrganisationSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<OrganisationSchemeVersionMetamac> result = getOrganisationsMetamacService().findOrganisationSchemesByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultOrganisationSchemeVersion(result,
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
    public List<OrganisationSchemeMetamacBasicDto> retrieveOrganisationSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationSchemeVersions(ctx);

        // Retrieve and sort by date desc
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersionsMetamac = getOrganisationsMetamacService().retrieveOrganisationSchemeVersions(ctx, urn);
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersionsSortedByCreationDate = new ArrayList<OrganisationSchemeVersionMetamac>(organisationSchemeVersionsMetamac);
        Collections.sort(organisationSchemeVersionsSortedByCreationDate, ITEM_SCHEME_CREATED_DATE_DESC_COMPARATOR);

        // Transform
        List<OrganisationSchemeMetamacBasicDto> organisationSchemeMetamacDtos = organisationsDo2DtoMapper.organisationSchemeMetamacDoListToDtoList(organisationSchemeVersionsSortedByCreationDate);

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
    public OrganisationSchemeMetamacDto publishOrganisationSchemeInternally(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(ctx);

        // Publish
        OrganisationSchemeVersionMetamac organisationSchemeVersionPublished = getOrganisationsMetamacService().publishInternallyOrganisationScheme(ctx, urn, forceLatestFinal);

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
    public TaskInfo copyOrganisationScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canCopyOrganisationScheme(ctx);

        // Copy
        return getOrganisationsMetamacService().copyOrganisationScheme(ctx, urnToCopy);
    }

    @Override
    public TaskInfo versioningOrganisationScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canVersioningOrganisationScheme(ctx);

        TaskInfo versioningResult = null;
        if (GeneratorUrnUtils.isTemporalUrn(urnToCopy)) {
            getOrganisationsMetamacService().createVersionFromTemporalOrganisationScheme(ctx, urnToCopy, versionType);
        } else {
            versioningResult = getOrganisationsMetamacService().versioningOrganisationScheme(ctx, urnToCopy, versionType);
        }
        return versioningResult;
    }

    @Override
    public TaskInfo createTemporalVersionOrganisationScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canCreateOrganisationSchemeTemporalVersion(ctx);

        return getOrganisationsMetamacService().createTemporalOrganisationScheme(ctx, urnToCopy);
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
    public List<OrganisationMetamacVisualisationResult> retrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx, String organisationSchemeUrn, String locale) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canRetrieveOrganisationsByOrganisationSchemeUrn(ctx);

        // Retrieve
        return getOrganisationsMetamacService().retrieveOrganisationsByOrganisationSchemeUrn(ctx, organisationSchemeUrn, locale);
    }

    @Override
    public MetamacCriteriaResult<OrganisationMetamacBasicDto> findOrganisationsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canFindOrganisationsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getOrganisationMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<OrganisationMetamac> result = getOrganisationsMetamacService().findOrganisationsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<OrganisationMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultOrganisation(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<ContactDto> findOrganisationContactsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        OrganisationsSecurityUtils.canFindOrganisationContactsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getOrganisationContactCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<Contact> result = getOrganisationsMetamacService().findOrganisationContactsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<ContactDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultOrganisationContact(result, sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public OrganisationMetamacDto retrieveMaintainerDefault(ServiceContext ctx) throws MetamacException {
        // DO NOT CHECK SECURITY! The application should always call this method

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
    public List<ConceptSchemeMetamacBasicDto> retrieveConceptSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canRetrieveConceptSchemeVersions(ctx);

        // Retrieve and sort by date desc
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionsMetamac = getConceptsMetamacService().retrieveConceptSchemeVersions(ctx, urn);
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionsSortedByCreationDate = new ArrayList<ConceptSchemeVersionMetamac>(conceptSchemeVersionsMetamac);
        Collections.sort(conceptSchemeVersionsSortedByCreationDate, ITEM_SCHEME_CREATED_DATE_DESC_COMPARATOR);

        // Transform
        List<ConceptSchemeMetamacBasicDto> conceptSchemeMetamacDtos = conceptsDo2DtoMapper.conceptSchemeMetamacDoListToDtoList(conceptSchemeVersionsSortedByCreationDate);

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
    public MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> findConceptSchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptSchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptSchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = getConceptsMetamacService().findConceptSchemesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultConceptSchemeVersion(result,
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
    public ConceptSchemeMetamacDto publishConceptSchemeInternally(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canPublishConceptSchemeInternally(ctx, conceptSchemeVersion);

        // Publish
        ConceptSchemeVersionMetamac conceptSchemeVersionPublished = getConceptsMetamacService().publishInternallyConceptScheme(ctx, urn, forceLatestFinal);

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
    public TaskInfo copyConceptScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptsSecurityUtils.canCopyConceptScheme(ctx, conceptSchemeVersionToCopy);

        // Copy
        return getConceptsMetamacService().copyConceptScheme(ctx, urnToCopy);
    }

    @Override
    public TaskInfo versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptsSecurityUtils.canVersioningConceptScheme(ctx, conceptSchemeVersionToCopy);

        TaskInfo versioningResult = null;
        if (GeneratorUrnUtils.isTemporalUrn(urnToCopy)) {
            versioningResult = getConceptsMetamacService().createVersionFromTemporalConceptScheme(ctx, urnToCopy, versionType);
        } else {
            versioningResult = getConceptsMetamacService().versioningConceptScheme(ctx, urnToCopy, versionType);
        }
        return versioningResult;
    }

    @Override
    public TaskInfo createTemporalVersionConceptScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = getConceptsMetamacService().retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptsSecurityUtils.canCreateConceptSchemeTemporalVersion(ctx, conceptSchemeVersionToCopy);

        return getConceptsMetamacService().createTemporalVersionConceptScheme(ctx, urnToCopy);
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
    public List<ConceptMetamacVisualisationResult> retrieveConceptsByConceptSchemeUrn(ServiceContext ctx, String conceptSchemeUrn, String locale) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveConceptsByConceptSchemeUrn(ctx);

        // Retrieve
        List<ConceptMetamacVisualisationResult> concepts = getConceptsMetamacService().retrieveConceptsByConceptSchemeUrn(ctx, conceptSchemeUrn, locale);
        return concepts;
    }

    @Override
    public MetamacCriteriaResult<ConceptMetamacBasicDto> findConceptsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getConceptMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<ConceptMetamac> result = getConceptsMetamacService().findConceptsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<ConceptMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultConcept(result, sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findConceptsCanBeRoleByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

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
        ConceptsSecurityUtils.canFindConceptsByCondition(ctx);

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
    public List<ConceptMetamacBasicDto> retrieveRelatedConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveRelatedConcepts(ctx);

        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveRelatedConcepts(ctx, urn);

        // Transform
        List<ConceptMetamacBasicDto> conceptsDto = conceptsDo2DtoMapper.conceptMetamacDoListToDtoList(concepts);
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
    public List<ConceptMetamacBasicDto> retrieveRoleConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Security
        ConceptsSecurityUtils.canRetrieveRoleConcepts(ctx);

        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveRoleConcepts(ctx, urn);

        // Transform
        List<ConceptMetamacBasicDto> conceptsDto = conceptsDo2DtoMapper.conceptMetamacDoListToDtoList(concepts);
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

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ServiceContext ctx, MetamacCriteria criteria, String conceptUrn)
            throws MetamacException {
        // Security
        CodesSecurityUtils.canFindCodelistsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCodelistMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CodelistVersionMetamac> result = getConceptsMetamacService().findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ctx, sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter(), conceptUrn);

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultCodelistToMetamacCriteriaResultRelatedResource(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // ------------------------------------------------------------------------

    @Override
    public CategorySchemeMetamacDto createCategoryScheme(ServiceContext ctx, CategorySchemeMetamacDto categorySchemeDto) throws MetamacException {
        // Security and transform
        CategoriesSecurityUtils.canCreateCategoryScheme(ctx);
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
        CategoriesSecurityUtils.canUpdateCategoryScheme(ctx, categorySchemeVersionOld.getLifeCycleMetadata().getProcStatus());

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
        CategoriesSecurityUtils.canDeleteCategoryScheme(ctx);

        // Delete
        getCategoriesMetamacService().deleteCategoryScheme(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<CategorySchemeMetamacBasicDto> findCategorySchemesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canFindCategorySchemesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCategorySchemeMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CategorySchemeVersionMetamac> result = getCategoriesMetamacService().findCategorySchemesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CategorySchemeMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCategorySchemeVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public CategorySchemeMetamacDto retrieveCategorySchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canRetrieveCategorySchemeByUrn(ctx);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = getCategoriesMetamacService().retrieveCategorySchemeByUrn(ctx, urn);

        // Transform
        CategorySchemeMetamacDto categorySchemeMetamacDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersion);

        return categorySchemeMetamacDto;
    }

    @Override
    public List<CategorySchemeMetamacBasicDto> retrieveCategorySchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canRetrieveCategorySchemeVersions(ctx);

        // Retrieve and sort by date desc
        List<CategorySchemeVersionMetamac> categorySchemeVersionsMetamac = getCategoriesMetamacService().retrieveCategorySchemeVersions(ctx, urn);
        List<CategorySchemeVersionMetamac> categorySchemeVersionsSortedByCreationDate = new ArrayList<CategorySchemeVersionMetamac>(categorySchemeVersionsMetamac);
        Collections.sort(categorySchemeVersionsSortedByCreationDate, ITEM_SCHEME_CREATED_DATE_DESC_COMPARATOR);

        // Transform
        List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos = categoriesDo2DtoMapper.categorySchemeMetamacDoListToDtoList(categorySchemeVersionsSortedByCreationDate);
        return categorySchemeMetamacDtos;
    }

    @Override
    public CategorySchemeMetamacDto sendCategorySchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canSendCategorySchemeToProductionValidation(ctx);

        // Send
        CategorySchemeVersionMetamac categorySchemeVersionProductionValidation = getCategoriesMetamacService().sendCategorySchemeToProductionValidation(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionProductionValidation);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto sendCategorySchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canSendCategorySchemeToDiffusionValidation(ctx);

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
        CategoriesSecurityUtils.canRejectCategorySchemeValidation(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

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
        CategoriesSecurityUtils.canRejectCategorySchemeValidation(ctx, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Reject
        CategorySchemeVersionMetamac categorySchemeVersionRejected = getCategoriesMetamacService().rejectCategorySchemeDiffusionValidation(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionRejected);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto publishCategorySchemeInternally(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canPublishCategorySchemeInternally(ctx);

        // Publish
        CategorySchemeVersionMetamac categorySchemeVersionPublished = getCategoriesMetamacService().publishInternallyCategoryScheme(ctx, urn, forceLatestFinal);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionPublished);
        return categorySchemeDto;
    }

    @Override
    public CategorySchemeMetamacDto publishCategorySchemeExternally(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canPublishCategorySchemeExternally(ctx);

        CategorySchemeVersionMetamac categorySchemeVersionPublished = getCategoriesMetamacService().publishExternallyCategoryScheme(ctx, urn);

        // Transform to DTO
        CategorySchemeMetamacDto categorySchemeDto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(categorySchemeVersionPublished);
        return categorySchemeDto;
    }

    @Override
    public TaskInfo copyCategoryScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canCopyCategoryScheme(ctx);

        // Copy
        return getCategoriesMetamacService().copyCategoryScheme(ctx, urnToCopy);
    }

    @Override
    public TaskInfo versioningCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canVersioningCategoryScheme(ctx);

        TaskInfo versioningResult = null;
        if (GeneratorUrnUtils.isTemporalUrn(urnToCopy)) {
            versioningResult = getCategoriesMetamacService().createVersionFromTemporalCategoryScheme(ctx, urnToCopy, versionType);
        } else {
            versioningResult = getCategoriesMetamacService().versioningCategoryScheme(ctx, urnToCopy, versionType);
        }

        return versioningResult;
    }

    @Override
    public TaskInfo createTemporalVersionCategoryScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canCreateCategorySchemeTemporalVersion(ctx);

        return getCategoriesMetamacService().createTemporalVersionCategoryScheme(ctx, urnToCopy);
    }

    @Override
    public CategorySchemeMetamacDto endCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canEndCategorySchemeValidity(ctx);

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
        CategoriesSecurityUtils.canCreateCategory(ctx, categorySchemeVersion);

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
        CategoriesSecurityUtils.canUpdateCategory(ctx, categorySchemeVersion);

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
        CategoriesSecurityUtils.canRetrieveCategoryByUrn(ctx);

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
        CategoriesSecurityUtils.canDeleteCategory(ctx, categorySchemeVersion);

        // Delete
        getCategoriesMetamacService().deleteCategory(ctx, urn);
    }

    @Override
    public List<ItemVisualisationResult> retrieveCategoriesByCategorySchemeUrn(ServiceContext ctx, String categorySchemeUrn, String locale) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canRetrieveCategoriesByCategorySchemeUrn(ctx);

        // Retrieve
        return getCategoriesMetamacService().retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, locale);
    }

    @Override
    public MetamacCriteriaResult<CategoryMetamacBasicDto> findCategoriesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        CategoriesSecurityUtils.canFindCategoriesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getCategoryMetamacCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<CategoryMetamac> result = getCategoriesMetamacService().findCategoriesByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<CategoryMetamacBasicDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultCategory(result, sculptorCriteria.getPageSize());

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
        CategoriesSecurityUtils.canRetrieveCategorisationByUrn(ctx);

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
        CategoriesSecurityUtils.canRetrieveCategorisationByUrn(ctx);

        // Retrieve
        List<Categorisation> categorisations = getCategoriesMetamacService().retrieveCategorisationsByArtefact(ctx, urn);

        // Transform
        List<CategorisationDto> categorisationsDto = categoriesDo2DtoMapper.categorisationDoListToDtoList(categorisations);
        return categorisationsDto;
    }

    @Override
    public CategorisationDto endCategorisationValidity(ServiceContext ctx, String urn, DateTime validTo) throws MetamacException {
        // Security
        Categorisation categorisation = getCategoriesMetamacService().retrieveCategorisationByUrn(ctx, urn);
        canModifyCategorisation(ctx, categorisation.getArtefactCategorised().getUrn());

        // Delete
        categorisation = getCategoriesMetamacService().endCategorisationValidity(ctx, urn, validTo);

        // Transform
        CategorisationDto categorisationDto = categoriesDo2DtoMapper.categorisationDoToDto(categorisation);
        return categorisationDto;
    }

    /**************************************************************************
     * PRIVATE METHODS
     *************************************************************************/
    private List<DescriptorDto> findDescriptorsForDsd(ServiceContext ctx, String urnDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // 2 - Retrieve Descriptor
        List<DescriptorDto> descriptorDtos;
        // try {
        // 1 - Retrieve DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, urnDsd);

        descriptorDtos = new ArrayList<DescriptorDto>(dataStructureDefinitionVersion.getGrouping().size());
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
            CategoriesSecurityUtils.canModifyCategorisation(ctx, categorySchemeVersion);

            // Organisation schemes
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_ORGANISATIONUNITSCHEME_PREFIX)
                || artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_DATAPROVIDERSCHEME_PREFIX) || artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_DATACONSUMERSCHEME_PREFIX)
                || artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_AGENCYSCHEME_PREFIX)) {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = getOrganisationsMetamacService().retrieveOrganisationSchemeByUrn(ctx, artefactCategorisedUrn);
            OrganisationsSecurityUtils.canModifyCategorisation(ctx, organisationSchemeVersion);

            // Codelists
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_CODELIST_PREFIX)) {
            CodelistVersionMetamac codelistVersion = getCodesMetamacService().retrieveCodelistByUrn(ctx, artefactCategorisedUrn);
            CodesSecurityUtils.canModifyCategorisation(ctx, codelistVersion);

            // Dsd
        } else if (artefactCategorisedUrn.startsWith(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX)) {
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = getDataStructureDefinitionMetamacService().retrieveDataStructureDefinitionByUrn(ctx, artefactCategorisedUrn);
            DataStructureDefinitionSecurityUtils.canModifyCategorisation(ctx, dataStructureDefinitionVersion);
        } else {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    private static class ItemSchemeCreatedDateDescComparator implements Comparator<ItemSchemeVersion> {

        @Override
        public int compare(ItemSchemeVersion is1, ItemSchemeVersion is2) {
            return is1.getCreatedDate().compareTo(is2.getCreatedDate());
        }

    };

    private static class StructureCreatedDateDescComparator implements Comparator<StructureVersion> {

        @Override
        public int compare(StructureVersion s1, StructureVersion s2) {
            return s1.getCreatedDate().compareTo(s2.getCreatedDate());
        }

    }

}
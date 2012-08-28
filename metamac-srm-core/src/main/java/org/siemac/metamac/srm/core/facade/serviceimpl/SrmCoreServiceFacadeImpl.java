package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.core.common.ws.ServicesResolver;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.mapper.Do2DtoMapper;
import org.siemac.metamac.srm.core.mapper.Dto2DoMapper;
import org.siemac.metamac.srm.core.mapper.MetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.srm.core.mapper.SculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.srm.core.security.ConceptsSecurityUtils;
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
import com.arte.statistic.sdmx.srm.core.structure.exception.DataStructureDefinitionVersionNotFoundException;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RelationshipDto;
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
    @Qualifier("do2DtoMapper")
    private Do2DtoMapper                           do2DtoMapper;

    @Autowired
    @Qualifier("dto2DoMapper")
    private Dto2DoMapper                           dto2DoMapper;

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

    protected Do2DtoMapper getDo2DtoMapper() {
        return do2DtoMapper;
    }

    protected Dto2DoMapper getDto2DoMapper() {
        return dto2DoMapper;
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
    public DataStructureDefinitionDto saveDsd(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {

        // DTOs to Entities
        DataStructureDefinitionVersion dataStructureDefinitionVersion = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(ctx, dataStructureDefinitionDto);

        // Save
        dataStructureDefinitionVersion = getDataStructureDefinitionService().saveDsd(ctx, dataStructureDefinitionVersion);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dataStructureDefinitionVersion);
    }

    @Override
    public void deleteDsd(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {

        // DTOs to Entitys
        DataStructureDefinitionVersion dataStructureDefinitionVersion = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(ctx, dataStructureDefinitionDto);

        // Remove DSD
        getDataStructureDefinitionService().deleteDsd(ctx, dataStructureDefinitionVersion);
    }

    @Override
    public DataStructureDefinitionDto createDsdVersion(ServiceContext ctx, Long idDsd, boolean minorVersion) throws MetamacException {

        // Load extends DSD (in create mode)
        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = retrieveExtendedDsd(ctx, idDsd, TypeDozerCopyMode.CREATE);

        dataStructureDefinitionExtendDto.setVersionLogic(VersionUtil.createNextVersionTag(dataStructureDefinitionExtendDto.getVersionLogic(), minorVersion));

        // Save graph
        return saveDsdGraph(ctx, dataStructureDefinitionExtendDto);
    }

    @Override
    public MetamacCriteriaResult<DataStructureDefinitionDto> findDsdByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getDataStructureDefinitionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<DataStructureDefinitionVersion> result = getDataStructureDefinitionService().findDsdByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<DataStructureDefinitionDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDataStructureDefinition(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public List<DataStructureDefinitionDto> findAllDsds(ServiceContext ctx) {

        // Search
        List<DataStructureDefinitionVersion> dataStructureDefinitionList = getDataStructureDefinitionService().findAllDsds(ctx);

        // To DTO
        List<DataStructureDefinitionDto> dataStructureDefinitionDtoList = new ArrayList<DataStructureDefinitionDto>();
        for (DataStructureDefinitionVersion dsd : dataStructureDefinitionList) {
            dataStructureDefinitionDtoList.add(getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dsd));
        }

        return dataStructureDefinitionDtoList;
    }

    @Override
    public DataStructureDefinitionExtendDto retrieveExtendedDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {

        // Load Dsd
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinitionVersion);
    }

    @Override
    public DataStructureDefinitionDto retrieveDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {

        // Load Dsd
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(typeDozerCopyMode, dataStructureDefinitionVersion);
    }

    @Override
    public DataStructureDefinitionDto retrieveDsdByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Search
        DataStructureDefinitionVersion dataStructureDefinitionVersion = getDataStructureDefinitionService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dataStructureDefinitionVersion);
    }

    /**************************************************************************
     * Descriptors
     **************************************************************************/

    @Override
    public List<DescriptorDto> findDescriptorForDsd(ServiceContext ctx, Long idDsd, TypeComponentList typeComponentList) throws MetamacException {

        // Load Dsd
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // Check Type
        if (!typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR) && !typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)
                && !typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR) && !typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withLoggedLevel(ExceptionLevelEnum.INFO)
                    .withMessageParameters(ServiceExceptionParameters.COMPONENT_LIST).build();
        }

        // To DTOs
        List<DescriptorDto> descriptorDtos = new ArrayList<DescriptorDto>();
        for (ComponentList componentList : dataStructureDefinitionVersion.getGrouping()) {
            if ((componentList instanceof AttributeDescriptor) && typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.UPDATE, componentList));
            } else if ((componentList instanceof DimensionDescriptor) && typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.UPDATE, componentList));
            } else if ((componentList instanceof GroupDimensionDescriptor) && typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.UPDATE, componentList));
            } else if ((componentList instanceof MeasureDescriptor) && typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.UPDATE, componentList));
            }
        }

        return descriptorDtos;
    }

    @Override
    public List<DescriptorDto> findDescriptorsForDsd(ServiceContext ctx, Long idDsd) throws MetamacException {
        return findDescriptorsForDsd(ctx, idDsd, TypeDozerCopyMode.UPDATE);
    }

    @Override
    public DescriptorDto saveDescriptorForDsd(ServiceContext ctx, Long idDsd, DescriptorDto descriptorDto) throws MetamacException {

        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(ctx, descriptorDto);

        // Load DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // Save
        componentListDescriptor = getDataStructureDefinitionService().saveDescriptorForDsd(ctx, dataStructureDefinitionVersion, componentListDescriptor);

        // Entities to DTOs
        return getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.UPDATE, componentListDescriptor);
    }

    @Override
    public void deleteDescriptorForDsd(ServiceContext ctx, Long idDsd, DescriptorDto descriptorDto) throws MetamacException {
        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(ctx, descriptorDto);

        // Load DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // Delete descriptor for DSD
        getDataStructureDefinitionService().deleteDescriptorForDsd(ctx, dataStructureDefinitionVersion, componentListDescriptor);
    }

    /**************************************************************************
     * Component
     **************************************************************************/

    @Override
    public ComponentDto saveComponentForDsd(ServiceContext ctx, Long idDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(ctx, componentDto);

        // Load DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // Save component for DSD
        component = getDataStructureDefinitionService().saveComponentForDsd(ctx, dataStructureDefinitionVersion, component, typeComponentList);

        // Entitys to DTOs
        return getDo2DtoMapper().componentToComponentDto(TypeDozerCopyMode.UPDATE, component);
    }

    @Override
    public void deleteComponentForDsd(ServiceContext ctx, Long idDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(ctx, componentDto);

        // Load DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion = loadDsdById(ctx, idDsd);

        // Delete component for DSD
        getDataStructureDefinitionService().deleteComponentForDsd(ctx, dataStructureDefinitionVersion, component, typeComponentList);
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

    /**************************************************************************
     * Import/Export
     **************************************************************************/
    @Override
    public void importSDMXStructureMsg(ServiceContext ctx, ContentInputDto contentDto) throws MetamacException {
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
     * CONCEPTS
     *************************************************************************/

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
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);

        return conceptSchemeMetamacDto;
    }

    @Override
    public List<ConceptSchemeMetamacDto> retrieveConceptSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canRetrieveConceptSchemeVersions(ctx);

        // Retrieve
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionMetamacs = getConceptsMetamacService().retrieveConceptSchemeVersions(ctx, urn);

        // Transform
        List<ConceptSchemeMetamacDto> conceptSchemeMetamacDtos = do2DtoMapper.conceptSchemeMetamacDoListToDtoList(conceptSchemeVersionMetamacs);

        return conceptSchemeMetamacDtos;
    }

    @Override
    public ConceptSchemeMetamacDto createConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeDto) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canCreateConceptScheme(ctx);

        // Transform
        ConceptSchemeVersionMetamac conceptSchemeVersion = dto2DoMapper.conceptSchemeDtoToDo(ctx, conceptSchemeDto);

        // Create
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = getConceptsMetamacService().createConceptScheme(ctx, conceptSchemeVersion);

        // Transform to DTO
        conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionCreated);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto updateConceptScheme(ServiceContext ctx, ConceptSchemeMetamacDto conceptSchemeDto) throws MetamacException {
        // Security
        ConceptsSecurityUtils.canUpdateConceptScheme(ctx, conceptSchemeDto);

        // Transform
        ConceptSchemeVersionMetamac conceptSchemeVersion = dto2DoMapper.conceptSchemeDtoToDo(ctx, conceptSchemeDto);

        // Update
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = getConceptsMetamacService().updateConceptScheme(ctx, conceptSchemeVersion);

        // Transform to DTO
        conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionCreated);
        return conceptSchemeDto;
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canDeleteConceptScheme(ctx, conceptSchemeMetamacDto);

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
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canSendConceptSchemeToProductionValidation(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().sendConceptSchemeToProductionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto sendConceptSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canSendConceptSchemeToDiffusionValidation(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto rejectConceptSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canRejectConceptSchemeValidation(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().rejectConceptSchemeProductionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto rejectConceptSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canRejectConceptSchemeValidation(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto publishInternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canPublishConceptSchemeInternally(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().publishInternallyConceptScheme(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto publishExternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canPublishConceptSchemeExternally(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().publishExternallyConceptScheme(ctx, urn);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urnToCopy);
        ConceptsSecurityUtils.canVersioningConceptScheme(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptsMetamacService().versioningConceptScheme(ctx, urnToCopy, versionType);

        // Transform to Dto
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersion);
        return conceptSchemeDto;
    }

    @Override
    public ConceptSchemeMetamacDto cancelConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = retrieveConceptSchemeByUrn(ctx, urn);
        ConceptsSecurityUtils.canCancelConceptSchemeValidity(ctx, conceptSchemeMetamacDto);

        ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = getConceptsMetamacService().cancelConceptSchemeValidity(ctx, urn);

        // Transform
        ConceptSchemeMetamacDto conceptSchemeDto = do2DtoMapper.conceptSchemeMetamacDoToDto(conceptSchemeVersionMetamac);

        return conceptSchemeDto;
    }

    @Override
    public ConceptMetamacDto retrieveConceptByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security

        ConceptMetamac conceptMetamac = getConceptsMetamacService().retrieveConceptByUrn(ctx, urn);

        // Transform
        ConceptMetamacDto conceptMetamacDto = do2DtoMapper.conceptMetamacDoToDto(conceptMetamac);

        return conceptMetamacDto;
    }

    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {
        // TODO Security

        // Delete
        getConceptsMetamacService().deleteConcept(ctx, urn);
    }

    @Override
    public List<ItemHierarchyDto> retrieveConceptsByConceptSchemeUrn(ServiceContext ctx, String conceptSchemeUrn) throws MetamacException {
        // TODO Security
        
        // Retrieve
        List<ConceptMetamac> concepts = getConceptsMetamacService().retrieveConceptsByConceptSchemeUrn(ctx, conceptSchemeUrn);

        // Transform
        List<ItemHierarchyDto> itemsHierarchyDto = do2DtoMapper.conceptMetamacDoListToItemHierarchyDtoList(concepts);
        return itemsHierarchyDto;
    }

    /**************************************************************************
     * PRIVATE METHODS
     *************************************************************************/

    private DataStructureDefinitionVersion loadDsdById(ServiceContext ctx, Long idDsd) throws MetamacException {
        // Load DSD
        DataStructureDefinitionVersion dataStructureDefinitionVersion;
        try {
            dataStructureDefinitionVersion = getDataStructureDefinitionService().findDsdById(ctx, idDsd);
        } catch (DataStructureDefinitionVersionNotFoundException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(DataStructureDefinitionVersion.class.getName())
                    .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
        return dataStructureDefinitionVersion;
    }

    private DataStructureDefinitionDto saveDsdGraph(ServiceContext ctx, DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto) throws MetamacException {

        // Save DSD (without grouping)
        DataStructureDefinitionDto dataStructureDefinitionDto = saveDsd(ctx, dataStructureDefinitionExtendDto);

        // Save DimensionDescriptor
        DescriptorDto dimensionDesDto = new DescriptorDto();
        for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
            if (componentListDto.getTypeComponentList().equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
                dimensionDesDto = (DescriptorDto) saveDescriptorAndComponents(ctx, dataStructureDefinitionDto.getId(), componentListDto);
            }
        }

        // Auxiliary structure for update references
        HashMap<String, DimensionComponentDto> dimensionComponents = new HashMap<String, DimensionComponentDto>();
        for (ComponentDto componentDto : dimensionDesDto.getComponents()) {
            dimensionComponents.put(componentDto.getCode(), (DimensionComponentDto) componentDto);
        }

        // Save GroupDescriptor
        HashMap<String, DescriptorDto> groupDescriptors = new HashMap<String, DescriptorDto>();
        for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
            if (componentListDto.getTypeComponentList().equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                // 1: Update References For GroupDimensionDescriptor->components
                Set<ComponentDto> componentsUpdate = new HashSet<ComponentDto>();
                for (ComponentDto componentDto : componentListDto.getComponents()) {
                    if (dimensionComponents.get(componentDto.getCode()) == null) {
                        throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_GROUP_DESCRIPTOR_UNABLE_UPDATE).build();
                    } else {
                        componentsUpdate.add(dimensionComponents.get(componentDto.getCode()));
                    }
                }

                componentListDto.getComponents().clear();
                componentListDto.getComponents().addAll(componentsUpdate);

                // 2: Save groupDescriptor
                // Don't need save new components only descriptor
                DescriptorDto groupDescriptorDto = saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), (DescriptorDto) componentListDto);

                groupDescriptors.put(groupDescriptorDto.getCode(), groupDescriptorDto); // Auxiliary structure for update references
            }
        }

        // Save AttributeDescriptor
        for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
            if (componentListDto.getTypeComponentList().equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
                for (ComponentDto componentDto : componentListDto.getComponents()) {
                    // 1: Update references for AttributeRelationship
                    RelationshipDto relationshipDto = ((DataAttributeDto) componentDto).getRelateTo();

                    if (relationshipDto.getDimensionForDimensionRelationship() != null) {
                        Set<DimensionComponentDto> componentsUpdate = new HashSet<DimensionComponentDto>();
                        for (ComponentDto itemRefDto : relationshipDto.getDimensionForDimensionRelationship()) {
                            if (dimensionComponents.get(itemRefDto.getCode()) == null) {
                                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE).build();
                            } else {
                                componentsUpdate.add(dimensionComponents.get(itemRefDto.getCode()));
                            }
                        }
                        relationshipDto.getDimensionForDimensionRelationship().clear();
                        relationshipDto.getDimensionForDimensionRelationship().addAll(componentsUpdate);
                    }

                    if (relationshipDto.getGroupKeyForDimensionRelationship() != null) {
                        Set<DescriptorDto> descriptorsDtoUpdate = new HashSet<DescriptorDto>();
                        for (DescriptorDto itemRefDto : relationshipDto.getGroupKeyForDimensionRelationship()) {
                            if (groupDescriptors.get(itemRefDto.getCode()) == null) {
                                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE).build();
                            } else {
                                descriptorsDtoUpdate.add(groupDescriptors.get(itemRefDto.getCode()));
                            }
                        }
                        relationshipDto.getGroupKeyForDimensionRelationship().clear();
                        relationshipDto.getGroupKeyForDimensionRelationship().addAll(descriptorsDtoUpdate);
                    }

                    if (relationshipDto.getGroupKeyForGroupRelationship() != null) {
                        if (groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getCode()) == null) {
                            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE).build();
                        } else {
                            relationshipDto.setGroupKeyForGroupRelationship(groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getCode()));
                        }
                    }
                }
                // 2: Save AttributeDescriptor
                saveDescriptorAndComponents(ctx, dataStructureDefinitionDto.getId(), componentListDto);
            }
        }

        // Save MeasureDescriptor
        for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
            if (componentListDto.getTypeComponentList().equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
                saveDescriptorAndComponents(ctx, dataStructureDefinitionDto.getId(), componentListDto);
            }
        }

        return dataStructureDefinitionDto;
    }

    private List<DescriptorDto> findDescriptorsForDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // 2 - Retrieve Descriptor
        List<DescriptorDto> descriptorDtos;
        try {
            // 1 - Retrieve DSD
            DataStructureDefinitionVersion dataStructureDefinitionVersion = getDataStructureDefinitionService().findDsdById(ctx, idDsd);

            descriptorDtos = new ArrayList<DescriptorDto>();
            for (ComponentList componentList : dataStructureDefinitionVersion.getGrouping()) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(typeDozerCopyMode, componentList));
            }
        } catch (DataStructureDefinitionVersionNotFoundException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND)
                    .withMessageParameters(ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION).build();
        }

        return descriptorDtos;
    }

    private ComponentListDto saveDescriptorAndComponents(ServiceContext ctx, Long dsdId, ComponentListDto componentListDto) throws MetamacException {
        Set<ComponentDto> componentBackupDtos = new HashSet<ComponentDto>();
        for (ComponentDto componentDto : componentListDto.getComponents()) {
            componentBackupDtos.add(componentDto);
        }

        // Remove association with components (temporal)
        componentListDto.removeAllComponents();

        // Save ComponentList
        componentListDto = saveDescriptorForDsd(ctx, dsdId, (DescriptorDto) componentListDto);

        // Save Components
        for (ComponentDto componentDto : componentBackupDtos) {
            componentDto = saveComponentForDsd(ctx, dsdId, componentDto, componentListDto.getTypeComponentList());
            componentListDto.getComponents().add(componentDto); // Recreate association with components
        }

        return componentListDto;
    }

}

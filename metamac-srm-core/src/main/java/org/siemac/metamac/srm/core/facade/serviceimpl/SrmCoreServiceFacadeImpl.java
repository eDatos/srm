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
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
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
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.dto.RelationshipDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.domain.trans.dto.StructureMsgDto;
import org.siemac.metamac.domain.util.dto.ContentInputDto;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.mapper.Do2DtoMapper;
import org.siemac.metamac.srm.core.mapper.Dto2DoMapper;
import org.siemac.metamac.srm.core.mapper.MetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.srm.core.mapper.SculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.exception.DataStructureDefinitionNotFoundException;
import org.siemac.metamac.trans.error.MetamacTransExceptionType;
import org.siemac.metamac.trans.v2_1.message.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

/**
 * Implementation of srmCoreServiceFacade.
 */
@Service("srmCoreServiceFacade")
public class SrmCoreServiceFacadeImpl extends SrmCoreServiceFacadeImplBase {

    public SrmCoreServiceFacadeImpl() {
    }

    private Logger                                 logger = LoggerFactory.getLogger(SrmCoreServiceFacade.class);

    @Autowired
    private Do2DtoMapper                           do2DtoMapper;

    @Autowired
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
        DataStructureDefinition dataStructureDefinition = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(ctx, dataStructureDefinitionDto);

        // Save
        dataStructureDefinition = getDataStructureDefinitionService().saveDsd(ctx, dataStructureDefinition);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dataStructureDefinition);
    }

    @Override
    public void deleteDsd(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {

        // DTOs to Entitys
        DataStructureDefinition dataStructureDefinition = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(ctx, dataStructureDefinitionDto);

        // Remove DSD
        getDataStructureDefinitionService().deleteDsd(ctx, dataStructureDefinition);
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
    public PagedResult<DataStructureDefinitionDto> findDsdByCondition(ServiceContext ctx, List<ConditionalCriteria> condition, PagingParameter pagingParameter) throws MetamacException {

        // Search
        PagedResult<DataStructureDefinition> dataStructureDefinitionPagedList = getDataStructureDefinitionService().findDsdByCondition(ctx, condition, pagingParameter);

        // To DTO
        List<DataStructureDefinitionDto> dataStructureDefinitionDtoList = new ArrayList<DataStructureDefinitionDto>();
        for (DataStructureDefinition dsd : dataStructureDefinitionPagedList.getValues()) {
            dataStructureDefinitionDtoList.add(getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dsd));
        }

        // Return preparation
        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = new PagedResult<DataStructureDefinitionDto>(dataStructureDefinitionDtoList,
                dataStructureDefinitionPagedList.getStartRow(), dataStructureDefinitionPagedList.getRowCount(), dataStructureDefinitionPagedList.getPageSize(),
                dataStructureDefinitionPagedList.getTotalRows(), dataStructureDefinitionPagedList.getAdditionalResultRows());

        return dataStructureDefinitionDtoPagedList;
    }

    @Override
    public MetamacCriteriaResult<DataStructureDefinitionDto> findDsdByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Transform
        SculptorCriteria sculptorCriteria = metamacCriteria2SculptorCriteriaMapper.getDataStructureDefinitionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<DataStructureDefinition> result = getDataStructureDefinitionService().findDsdByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<DataStructureDefinitionDto> metamacCriteriaResult = sculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDataStructureDefinition(result,
                sculptorCriteria.getPageSize());
        return metamacCriteriaResult;
    }

    @Override
    public List<DataStructureDefinitionDto> findAllDsds(ServiceContext ctx) {

        // Search
        List<DataStructureDefinition> dataStructureDefinitionList = getDataStructureDefinitionService().findAllDsds(ctx);

        // To DTO
        List<DataStructureDefinitionDto> dataStructureDefinitionDtoList = new ArrayList<DataStructureDefinitionDto>();
        for (DataStructureDefinition dsd : dataStructureDefinitionList) {
            dataStructureDefinitionDtoList.add(getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dsd));
        }

        return dataStructureDefinitionDtoList;
    }

    @Override
    public DataStructureDefinitionExtendDto retrieveExtendedDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {

        // Load Dsd
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinition);
    }

    @Override
    public DataStructureDefinitionDto retrieveDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {

        // Load Dsd
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(typeDozerCopyMode, dataStructureDefinition);
    }

    @Override
    public DataStructureDefinitionDto retrieveDsdByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Search
        DataStructureDefinition dataStructureDefinition = getDataStructureDefinitionService().retrieveDataStructureDefinitionByUrn(ctx, urn);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode.UPDATE, dataStructureDefinition);
    }

    @Override
    public DataStructureDefinitionDto saveDsdGraph(ServiceContext ctx, DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto) throws MetamacException {

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
                        MetamacException metamacException = new MetamacException(ServiceExceptionType.UNKNOWN, "Unable to update the references. (GroupDescriptor)");
                        logger.info(metamacException.getMessage());
                        metamacException.setLogged(true);
                        throw metamacException;
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
                                MetamacException metamacException = new MetamacException(ServiceExceptionType.UNKNOWN, "Unable to update the references. (AttributeDescriptor)");
                                logger.info(metamacException.getMessage());
                                metamacException.setLogged(true);
                                throw metamacException;
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
                                MetamacException metamacException = new MetamacException(ServiceExceptionType.UNKNOWN, "Unable to update the references. (AttributeDescriptor)");
                                logger.info(metamacException.getMessage());
                                metamacException.setLogged(true);
                                throw metamacException;
                            } else {
                                descriptorsDtoUpdate.add(groupDescriptors.get(itemRefDto.getCode()));
                            }
                        }
                        relationshipDto.getGroupKeyForDimensionRelationship().clear();
                        relationshipDto.getGroupKeyForDimensionRelationship().addAll(descriptorsDtoUpdate);
                    }

                    if (relationshipDto.getGroupKeyForGroupRelationship() != null) {
                        if (groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getCode()) == null) {
                            MetamacException metamacException = new MetamacException(ServiceExceptionType.UNKNOWN, "Unable to update the references. (AttributeDescriptor)");
                            logger.info(metamacException.getMessage());
                            metamacException.setLogged(true);
                            throw metamacException;
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

    /**************************************************************************
     * Descriptors
     **************************************************************************/

    @Override
    public List<DescriptorDto> findDescriptorForDsd(ServiceContext ctx, Long idDsd, TypeComponentList typeComponentList) throws MetamacException {

        // Load Dsd
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Check Type
        if (!typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR) && !typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)
                && !typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR) && !typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withLoggedLevel(ExceptionLevelEnum.INFO).withMessageParameters("typeComponentList")
                    .build();
        }

        // To DTOs
        List<DescriptorDto> descriptorDtos = new ArrayList<DescriptorDto>();
        for (ComponentList componentList : dataStructureDefinition.getGrouping()) {
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
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Save
        componentListDescriptor = getDataStructureDefinitionService().saveDescriptorForDsd(ctx, dataStructureDefinition, componentListDescriptor);

        // Entities to DTOs
        return getDo2DtoMapper().componentListToComponentListDto(TypeDozerCopyMode.UPDATE, componentListDescriptor);
    }

    @Override
    public void deleteDescriptorForDsd(ServiceContext ctx, Long idDsd, DescriptorDto descriptorDto) throws MetamacException {
        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(ctx, descriptorDto);

        // Load DSD
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Delete descriptor for DSD
        getDataStructureDefinitionService().deleteDescriptorForDsd(ctx, dataStructureDefinition, componentListDescriptor);
    }

    /**************************************************************************
     * Component
     **************************************************************************/

    @Override
    public ComponentDto saveComponentForDsd(ServiceContext ctx, Long idDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(ctx, componentDto);

        // Load DSD
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Save component for DSD
        component = getDataStructureDefinitionService().saveComponentForDsd(ctx, dataStructureDefinition, component, typeComponentList);

        // Entitys to DTOs
        return getDo2DtoMapper().componentToComponentDto(TypeDozerCopyMode.UPDATE, component);
    }

    @Override
    public void deleteComponentForDsd(ServiceContext ctx, Long idDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(ctx, componentDto);

        // Load DSD
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Delete component for DSD
        getDataStructureDefinitionService().deleteComponentForDsd(ctx, dataStructureDefinition, component, typeComponentList);
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
    public ExternalItemDto findOrganisation(ServiceContext ctx, String uriOrganistaion) throws MetamacException {
        // TODO find organistaion
        return ServicesResolver.resolveOrganisation(uriOrganistaion);
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

    // TODO para qué?
    @Override
    public List<ExternalItemDto> findConceptSchemeRefs(ServiceContext ctx) {
        return ServicesResolver.findAllConceptSchemes();
    }

    // TODO para qué?
    @Override
    public List<ExternalItemDto> findConcepts(ServiceContext ctx, String uriConceptScheme) {
        return ServicesResolver.retrieveConceptScheme(uriConceptScheme);
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // TODO security

        // Delete
        getConceptsService().deleteConceptScheme(ctx, urn);
    }

    /**************************************************************************
     * PRIVATE
     *************************************************************************/

    private DataStructureDefinition loadDsdById(ServiceContext ctx, Long idDsd) throws MetamacException {
        // Load DSD
        DataStructureDefinition dataStructureDefinition;
        try {
            dataStructureDefinition = getDataStructureDefinitionService().findDsdById(ctx, idDsd);
        } catch (DataStructureDefinitionNotFoundException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(DataStructureDefinition.class.getName())
                    .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
        return dataStructureDefinition;
    }

    private List<DescriptorDto> findDescriptorsForDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {
        // 2 - Retrieve Descriptor
        List<DescriptorDto> descriptorDtos;
        try {
            // 1 - Retrieve DSD
            DataStructureDefinition dataStructureDefinition = getDataStructureDefinitionService().findDsdById(ctx, idDsd);

            descriptorDtos = new ArrayList<DescriptorDto>();
            for (ComponentList componentList : dataStructureDefinition.getGrouping()) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(typeDozerCopyMode, componentList));
            }
        } catch (DataStructureDefinitionNotFoundException e) {
            // TODO poner la exp adecuada y quitar el unknow
            MetamacException metamacException = new MetamacException(e, ServiceExceptionType.PARAMETER_REQUIRED);
            logger.info(metamacException.getMessage());
            metamacException.setLogged(true);
            throw metamacException;
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

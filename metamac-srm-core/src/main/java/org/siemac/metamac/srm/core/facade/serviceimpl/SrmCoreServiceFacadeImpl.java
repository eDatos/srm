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
import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.core.common.ws.ServicesResolver;
import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
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
import org.siemac.metamac.srm.core.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.service.dto.Do2DtoMapper;
import org.siemac.metamac.srm.core.service.dto.Dto2DoMapper;
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
 * Implementation of SDMXStructureServiceFacade.
 */
@Service("srmCoreServiceFacade")
public class SrmCoreServiceFacadeImpl extends SrmCoreServiceFacadeImplBase {

    public SrmCoreServiceFacadeImpl() {
    }

    private Logger          logger = LoggerFactory.getLogger(SrmCoreServiceFacade.class);

    @Autowired
    private Do2DtoMapper    do2DtoMapper;

    @Autowired
    private Dto2DoMapper    dto2DoMapper;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller marshallerWithValidation;

    public Jaxb2Marshaller getMarshallerWithValidation() {
        return marshallerWithValidation;
    }

    protected Do2DtoMapper getDo2DtoMapper() {
        return do2DtoMapper;
    }

    protected Dto2DoMapper getDto2DoMapper() {
        return dto2DoMapper;
    }

    /**************************************************************************
     * DSDs
     **************************************************************************/
    @Override
    public DataStructureDefinitionDto saveDsd(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {

        // DTOs to Entities
        DataStructureDefinition dataStructureDefinition = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(dataStructureDefinitionDto, ctx, getSdmxBaseService());

        // Save
        dataStructureDefinition = getDataStructureDefinitionService().saveDsd(ctx, dataStructureDefinition);

        // Entities to DTOs
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(ctx, TypeDozerCopyMode.UPDATE, dataStructureDefinition, getSdmxBaseService());
    }

    @Override
    public void deleteDsd(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {

        // DTOs to Entitys
        DataStructureDefinition dataStructureDefinition = getDto2DoMapper().dataStructureDefinitionDtoToDataStructureDefinition(dataStructureDefinitionDto, ctx, getSdmxBaseService());

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
            dataStructureDefinitionDtoList.add(getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(ctx, TypeDozerCopyMode.UPDATE, dsd, getSdmxBaseService()));
        }

        // Return preparation
        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = new PagedResult<DataStructureDefinitionDto>(dataStructureDefinitionDtoList,
                dataStructureDefinitionPagedList.getStartRow(), dataStructureDefinitionPagedList.getRowCount(), dataStructureDefinitionPagedList.getPageSize(),
                dataStructureDefinitionPagedList.getTotalRows(), dataStructureDefinitionPagedList.getAdditionalResultRows());

        return dataStructureDefinitionDtoPagedList;
    }

    @Override
    public List<DataStructureDefinitionDto> findAllDsds(ServiceContext ctx) {

        // Search
        List<DataStructureDefinition> dataStructureDefinitionList = getDataStructureDefinitionService().findAllDsds(ctx);

        // To DTO
        List<DataStructureDefinitionDto> dataStructureDefinitionDtoList = new ArrayList<DataStructureDefinitionDto>();
        for (DataStructureDefinition dsd : dataStructureDefinitionList) {
            dataStructureDefinitionDtoList.add(getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(ctx, TypeDozerCopyMode.UPDATE, dsd, getSdmxBaseService()));
        }

        return dataStructureDefinitionDtoList;
    }

    @Override
    public DataStructureDefinitionExtendDto retrieveExtendedDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {

        // Load Dsd
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionExtendDto(ctx, typeDozerCopyMode, dataStructureDefinition, getSdmxBaseService());
    }

    @Override
    public DataStructureDefinitionDto retrieveDsd(ServiceContext ctx, Long idDsd, TypeDozerCopyMode typeDozerCopyMode) throws MetamacException {

        // Load Dsd
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // TO DTO
        return getDo2DtoMapper().dataStructureDefinitionToDataStructureDefinitionDto(ctx, typeDozerCopyMode, dataStructureDefinition, getSdmxBaseService());
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
            dimensionComponents.put(componentDto.getIdLogic(), (DimensionComponentDto) componentDto);
        }

        // Save GroupDescriptor
        HashMap<String, DescriptorDto> groupDescriptors = new HashMap<String, DescriptorDto>();
        for (ComponentListDto componentListDto : dataStructureDefinitionExtendDto.getGrouping()) {
            if (componentListDto.getTypeComponentList().equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                // 1: Update References For GroupDimensionDescriptor->components
                Set<ComponentDto> componentsUpdate = new HashSet<ComponentDto>();
                for (ComponentDto componentDto : componentListDto.getComponents()) {
                    if (dimensionComponents.get(componentDto.getIdLogic()) == null) {
                        MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.UNKNOWN, "Unable to update the references. (GroupDescriptor)");
                        logger.info(metamacException.getMessage());
                        metamacException.setLogged(true);
                        throw metamacException;
                    } else {
                        componentsUpdate.add(dimensionComponents.get(componentDto.getIdLogic()));
                    }
                }

                componentListDto.getComponents().clear();
                componentListDto.getComponents().addAll(componentsUpdate);

                // 2: Save groupDescriptor
                // Don't need save new components only descriptor
                DescriptorDto groupDescriptorDto = saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), (DescriptorDto) componentListDto);

                groupDescriptors.put(groupDescriptorDto.getIdLogic(), groupDescriptorDto); // Auxiliary structure for update references
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
                            if (dimensionComponents.get(itemRefDto.getIdLogic()) == null) {
                                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.UNKNOWN, "Unable to update the references. (AttributeDescriptor)");
                                logger.info(metamacException.getMessage());
                                metamacException.setLogged(true);
                                throw metamacException;
                            } else {
                                componentsUpdate.add(dimensionComponents.get(itemRefDto.getIdLogic()));
                            }
                        }
                        relationshipDto.getDimensionForDimensionRelationship().clear();
                        relationshipDto.getDimensionForDimensionRelationship().addAll(componentsUpdate);
                    }

                    if (relationshipDto.getGroupKeyForDimensionRelationship() != null) {
                        Set<DescriptorDto> descriptorsDtoUpdate = new HashSet<DescriptorDto>();
                        for (DescriptorDto itemRefDto : relationshipDto.getGroupKeyForDimensionRelationship()) {
                            if (groupDescriptors.get(itemRefDto.getIdLogic()) == null) {
                                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.UNKNOWN, "Unable to update the references. (AttributeDescriptor)");
                                logger.info(metamacException.getMessage());
                                metamacException.setLogged(true);
                                throw metamacException;
                            } else {
                                descriptorsDtoUpdate.add(groupDescriptors.get(itemRefDto.getIdLogic()));
                            }
                        }
                        relationshipDto.getGroupKeyForDimensionRelationship().clear();
                        relationshipDto.getGroupKeyForDimensionRelationship().addAll(descriptorsDtoUpdate);
                    }

                    if (relationshipDto.getGroupKeyForGroupRelationship() != null) {
                        if (groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getIdLogic()) == null) {
                            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.UNKNOWN, "Unable to update the references. (AttributeDescriptor)");
                            logger.info(metamacException.getMessage());
                            metamacException.setLogged(true);
                            throw metamacException;
                        } else {
                            relationshipDto.setGroupKeyForGroupRelationship(groupDescriptors.get(relationshipDto.getGroupKeyForGroupRelationship().getIdLogic()));
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
            throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_INCORRECT).withLoggedLevel(ExceptionLevelEnum.INFO)
                    .withMessageParameters("typeComponentList").build();
        }

        // To DTOs
        List<DescriptorDto> descriptorDtos = new ArrayList<DescriptorDto>();
        for (ComponentList componentList : dataStructureDefinition.getGrouping()) {
            if ((componentList instanceof AttributeDescriptor) && typeComponentList.equals(TypeComponentList.ATTRIBUTE_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(ctx, TypeDozerCopyMode.UPDATE, componentList, getSdmxBaseService()));
            } else if ((componentList instanceof DimensionDescriptor) && typeComponentList.equals(TypeComponentList.DIMENSION_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(ctx, TypeDozerCopyMode.UPDATE, componentList, getSdmxBaseService()));
            } else if ((componentList instanceof GroupDimensionDescriptor) && typeComponentList.equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(ctx, TypeDozerCopyMode.UPDATE, componentList, getSdmxBaseService()));
            } else if ((componentList instanceof MeasureDescriptor) && typeComponentList.equals(TypeComponentList.MEASURE_DESCRIPTOR)) {
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(ctx, TypeDozerCopyMode.UPDATE, componentList, getSdmxBaseService()));
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
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(descriptorDto, ctx, getSdmxBaseService());

        // Load DSD
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Save
        componentListDescriptor = getDataStructureDefinitionService().saveDescriptorForDsd(ctx, dataStructureDefinition, componentListDescriptor);

        // Entities to DTOs
        return getDo2DtoMapper().componentListToComponentListDto(ctx, TypeDozerCopyMode.UPDATE, componentListDescriptor, getSdmxBaseService());
    }

    @Override
    public void deleteDescriptorForDsd(ServiceContext ctx, Long idDsd, DescriptorDto descriptorDto) throws MetamacException {
        // DTOs to Entities
        ComponentList componentListDescriptor = getDto2DoMapper().componentListDtoToComponentList(descriptorDto, ctx, getSdmxBaseService());

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
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto, ctx, getSdmxBaseService());

        // Load DSD
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Save component for DSD
        component = getDataStructureDefinitionService().saveComponentForDsd(ctx, dataStructureDefinition, component, typeComponentList);

        // Entitys to DTOs
        return getDo2DtoMapper().componentToComponentDto(ctx, TypeDozerCopyMode.UPDATE, component, getSdmxBaseService());
    }

    @Override
    public void deleteComponentForDsd(ServiceContext ctx, Long idDsd, ComponentDto componentDto, TypeComponentList typeComponentList) throws MetamacException {

        // Dto to entity
        Component component = getDto2DoMapper().componentDtoToComponent(componentDto, ctx, getSdmxBaseService());

        // Load DSD
        DataStructureDefinition dataStructureDefinition = loadDsdById(ctx, idDsd);

        // Delete component for DSD
        getDataStructureDefinitionService().deleteComponentForDsd(ctx, dataStructureDefinition, component, typeComponentList);
    }

    /**************************************************************************
     * CODELIST
     **************************************************************************/
    @Override
    public List<ExternalItemBtDto> findCodelists(ServiceContext ctx, String uriConcept) throws MetamacException {
        // TODO devolver los codelist posibles para un concepto
        return ServicesResolver.findAllCodelists();
    }

    /**************************************************************************
     * ORGANISATION
     **************************************************************************/

    @Override
    public ExternalItemBtDto findOrganisation(ServiceContext ctx, String uriOrganistaion) throws MetamacException {
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
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.UNKNOWN).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .withMessageParameters(FileNotFoundException.class.getName()).build();
        } catch (IOException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.UNKNOWN).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .withMessageParameters(IOException.class.getName()).build();
        }

        return (file == null) ? StringUtils.EMPTY : file.getAbsolutePath();
    }

    /**************************************************************************
     * CONCEPTS
     *************************************************************************/

    @Override
    public List<ExternalItemBtDto> findConceptSchemeRefs(ServiceContext ctx) {
        return ServicesResolver.findAllConceptSchemes();
    }

    @Override
    public List<ExternalItemBtDto> findConcepts(ServiceContext ctx, String uriConceptScheme) {
        return ServicesResolver.retrieveConceptScheme(uriConceptScheme);
    }

    @Override
    public ConceptSchemeDto createConceptScheme(ServiceContext ctx, ConceptSchemeDto conceptSchemeDto) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeDto updateConceptScheme(ServiceContext ctx, ConceptSchemeDto conceptSchemeDto) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, Long conceptSchemeId) throws MetamacException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<ConceptSchemeDto> findAllConceptSchemes(ServiceContext ctx) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeDto retrieveConceptScheme(ServiceContext ctx, Long id) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeDto sendConceptSchemeToPendingPublication(ServiceContext ctx) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeDto publishConceptSchemeInternally(ServiceContext ctx, Long conceptSchemeId) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeDto publishConceptSchemeExternally(ServiceContext ctx, Long conceptSchemeId) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptDto createConcept(ServiceContext ctx, ConceptDto conceptDto, Long conceptSchemeId) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptDto updateConcept(ServiceContext ctx, ConceptDto conceptDto) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteConcept(ServiceContext ctx, Long conceptId) throws MetamacException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<ConceptDto> findConceptsForConceptScheme(ServiceContext ctx, Long conceptSchemeId) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptDto retrieveConcept(ServiceContext ctx, Long id) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptDto sendConceptToPendingPublication(ServiceContext ctx) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptDto publishConceptInternally(ServiceContext ctx, Long conceptId) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptDto publishConceptExternally(ServiceContext ctx, Long conceptId) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
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
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND).withMessageParameters(DataStructureDefinition.class.getName())
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
                descriptorDtos.add((DescriptorDto) getDo2DtoMapper().componentListToComponentListDto(ctx, typeDozerCopyMode, componentList, getSdmxBaseService()));
            }
        } catch (DataStructureDefinitionNotFoundException e) {
            // TODO poner la exp adecuada y quitar el unknow
            MetamacException metamacException = new MetamacException(e, MetamacCoreExceptionType.PARAMETER_REQUIRED);
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

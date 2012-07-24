package org.siemac.metamac.srm.core.facade.serviceapi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.xtext.util.StringInputStream;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DataflowDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.dto.RelationshipDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.domain.srm.enume.domain.TypeDataAttribute;
import org.siemac.metamac.domain.srm.enume.domain.TypeDimensionComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeRelathionship;
import org.siemac.metamac.domain.srm.enume.domain.TypeRepresentationEnum;
import org.siemac.metamac.domain.srm.enume.domain.UsageStatus;
import org.siemac.metamac.domain.util.dto.ContentInputDto;
import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;

public class SrmDtoMocks {

    private static final ServiceContext serviceContext = new ServiceContext("system", "123456", "junit");

    protected static ServiceContext getServiceContext() {
        return serviceContext;
    }


    /**************************************************************************
     * UTILS
     **************************************************************************/

    /**
     * DSD
     * 
     * @return
     */
    public static DataStructureDefinitionDto createDdsDTO() {
        DataStructureDefinitionDto dataStructureDefinitionDto = new DataStructureDefinitionDto();

        // Required --------
        dataStructureDefinitionDto.setIdLogic("DSD_" + RandomStringUtils.randomAlphabetic(10));
        
        // Name
        InternationalStringDto name = new InternationalStringDto();

        LocalisedStringDto name_es = new LocalisedStringDto();
        name_es.setLabel("NAME ES DSD");
        name_es.setLocale("es");
        LocalisedStringDto name_en = new LocalisedStringDto();
        name_en.setLabel("NAME EN DSD");
        name_en.setLocale("en");

        name.addText(name_en);
        name.addText(name_es);
        dataStructureDefinitionDto.setName(name);
        
        // Maintainer
        dataStructureDefinitionDto.setMaintainer(MetamacMocks.mockExternalItemDto(BaseDoMocks.mockAgencyUrn(), TypeExternalArtefactsEnum.AGENCY));
        
        // Other --------
        dataStructureDefinitionDto.setServiceURL("test");
        
        // Version Logic
        dataStructureDefinitionDto.setVersionLogic(VersionUtil.VERSION_LOGIC_INITIAL_VERSION);

        // Audit
        dataStructureDefinitionDto.setCreatedBy(serviceContext.getApplicationId());
        dataStructureDefinitionDto.setCreatedDate(new Date());

        return dataStructureDefinitionDto;
    }

    /**
     * @return
     */
    public static DataStructureDefinitionDto createDdsDTOwhithDescription() {
        DataStructureDefinitionDto dataStructureDefinitionDto = createDdsDTO();

        InternationalStringDto desc = new InternationalStringDto();

        LocalisedStringDto name_es = new LocalisedStringDto();
        name_es.setLabel("DESCRIPTION ES DSD");
        name_es.setLocale("es");
        LocalisedStringDto name_en = new LocalisedStringDto();
        name_en.setLabel("DESCRIPTION IN DSD");
        name_en.setLocale("en");

        desc.addText(name_en);
        desc.addText(name_es);

        dataStructureDefinitionDto.setDescription(desc);

        return dataStructureDefinitionDto;
    }

    /**
     * Dimension
     * 
     * @return
     */
    public static List<DimensionComponentDto> createDimensionDtos(SrmCoreServiceFacade srmCoreServiceFacade) {

        List<ExternalItemDto> conceptDtos = srmCoreServiceFacade.findConcepts(getServiceContext(), null);

        DimensionComponentDto dimensionComponentDto1 = new DimensionComponentDto();
        // Required
        dimensionComponentDto1.setUri(RandomStringUtils.random(50, true, true));
        dimensionComponentDto1.setTypeDimensionComponent(TypeDimensionComponent.DIMENSION);
        dimensionComponentDto1.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
        dimensionComponentDto1.setOrderLogic(1);
        dimensionComponentDto1.setCptIdRef(conceptDtos.get(0));

        // Some Auditory
        dimensionComponentDto1.setCreatedBy("Junit");
        dimensionComponentDto1.setCreatedDate(new Date());

        dimensionComponentDto1.getRole().add(conceptDtos.get(0));

        DimensionComponentDto dimensionComponentDto2 = new DimensionComponentDto();
        // Required
        dimensionComponentDto2.setUri(RandomStringUtils.random(50, true, true));
        dimensionComponentDto2.setTypeDimensionComponent(TypeDimensionComponent.MEASUREDIMENSION);
        dimensionComponentDto2.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
        dimensionComponentDto2.setOrderLogic(2);
        dimensionComponentDto2.setCptIdRef(conceptDtos.get(0));
        RepresentationDto repeRepresentationDto = new RepresentationDto();
        repeRepresentationDto.setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
        repeRepresentationDto.setEnumerated(MetamacMocks.mockExternalItemDto(BaseDoMocks.mockConceptSchemeUrn(), TypeExternalArtefactsEnum.CONCEPT_SCHEME));
        dimensionComponentDto2.setLocalRepresentation(repeRepresentationDto);

        // Some Auditory
        dimensionComponentDto2.setCreatedBy("Junit");
        dimensionComponentDto2.setCreatedDate(new Date());

        List<DimensionComponentDto> dimensionDtos = new ArrayList<DimensionComponentDto>();
        dimensionDtos.add(dimensionComponentDto1);
        dimensionDtos.add(dimensionComponentDto2);

        return dimensionDtos;
    }

    /**
     * @return
     * @throws ApplicationException
     */
    public static RelationshipDto createAttributeRelationship(SrmCoreServiceFacade srmCoreServiceFacade, Long dataStructureDefinitionId) throws MetamacException {

        RelationshipDto relationshipDto = new RelationshipDto();

        // Required
        relationshipDto.setTypeRelathionship(TypeRelathionship.GROUP_RELATIONSHIP);

        // For Group
        // Save GroupDimensionDescriptors
        DescriptorDto descriptorDto4 = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionId, createGroupDimensionDescriptorDto());

        relationshipDto.setGroupKeyForGroupRelationship(descriptorDto4);

        return relationshipDto;
    }

    /**
     * DataAttribute
     * 
     * @return
     * @throws ApplicationException
     */
    public static List<DataAttributeDto> createDataAttribute(SrmCoreServiceFacade srmCoreServiceFacade, Long dataStructureDefinitionId) throws MetamacException {

        List<ExternalItemDto> conceptDtos = srmCoreServiceFacade.findConcepts(getServiceContext(), null);

        DataAttributeDto dataAttributeDto1 = new DataAttributeDto();
        // Required
        dataAttributeDto1.setCptIdRef(conceptDtos.get(0));
        dataAttributeDto1.setRelateTo(createAttributeRelationship(srmCoreServiceFacade, dataStructureDefinitionId));
        dataAttributeDto1.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        dataAttributeDto1.setTypeDataAttribute(TypeDataAttribute.DATA_ATTRIBUTE);
        dataAttributeDto1.setUsageStatus(UsageStatus.CONDITIONAL);

        // Other
        dataAttributeDto1.setUri(RandomStringUtils.random(50, true, true));

        DataAttributeDto dataAttributeDto2 = new DataAttributeDto();
        // Required
        dataAttributeDto2.setCptIdRef(conceptDtos.get(0));
        dataAttributeDto2.setRelateTo(createAttributeRelationship(srmCoreServiceFacade, dataStructureDefinitionId));
        dataAttributeDto2.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        dataAttributeDto2.setTypeDataAttribute(TypeDataAttribute.REPORTING_YEAR_START_DAY);
        dataAttributeDto2.setUsageStatus(UsageStatus.CONDITIONAL);

        // Other
        dataAttributeDto2.setUri(RandomStringUtils.random(50, true, true));

        DataAttributeDto dataAttributeDto3 = new DataAttributeDto();
        // Required
        dataAttributeDto3.setCptIdRef(conceptDtos.get(0));
        dataAttributeDto3.setRelateTo(createAttributeRelationship(srmCoreServiceFacade, dataStructureDefinitionId));
        dataAttributeDto3.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        dataAttributeDto3.setTypeDataAttribute(TypeDataAttribute.DATA_ATTRIBUTE);
        dataAttributeDto3.setUsageStatus(UsageStatus.CONDITIONAL);

        // Other
        dataAttributeDto3.setUri(RandomStringUtils.random(50, true, true));

        List<DataAttributeDto> dataAttributeDtos = new ArrayList<DataAttributeDto>();
        dataAttributeDtos.add(dataAttributeDto1);
        dataAttributeDtos.add(dataAttributeDto2);
        dataAttributeDtos.add(dataAttributeDto3);

        return dataAttributeDtos;
    }

    public static ComponentDto createPrimaryMeasureDto(SrmCoreServiceFacade srmCoreServiceFacade) {

        List<ExternalItemDto> conceptDtos = srmCoreServiceFacade.findConcepts(getServiceContext(), null);

        ComponentDto primaryMeasure = new ComponentDto();

        // Required
        primaryMeasure.setCptIdRef(conceptDtos.get(0));
        primaryMeasure.setTypeComponent(TypeComponent.PRIMARY_MEASURE);
        primaryMeasure.setCptIdRef(MetamacMocks.mockExternalItemDto(BaseDoMocks.mockConceptUrn(), TypeExternalArtefactsEnum.CONCEPT));
        
        // Other
        primaryMeasure.setUri(RandomStringUtils.random(50, true, true));


        return primaryMeasure;
    }

    /**
     * createDimensionDescriptorDto
     * 
     * @return
     */
    public static DescriptorDto createDimensionDescriptorDto() {
        DescriptorDto descriptorDto = new DescriptorDto();

        // Required
        descriptorDto.setUri(RandomStringUtils.random(50, true, true));
        descriptorDto.setTypeComponentList(TypeComponentList.DIMENSION_DESCRIPTOR);

        return descriptorDto;
    }

    /**
     * createAttributeDescriptorDto
     * Descriptor
     * 
     * @return
     */
    public static DescriptorDto createAttributeDescriptorDto() {
        DescriptorDto descriptorDto = new DescriptorDto();

        // Required
        descriptorDto.setUri(RandomStringUtils.random(50, true, true));
        descriptorDto.setTypeComponentList(TypeComponentList.ATTRIBUTE_DESCRIPTOR);

        return descriptorDto;
    }

    /**
     * createMeasureDescriptorDto
     * Descriptor
     * 
     * @return
     */
    public static DescriptorDto createMeasureDescriptorDto() {
        DescriptorDto descriptorDto = new DescriptorDto();

        // Required
        descriptorDto.setUri(RandomStringUtils.random(50, true, true));
        descriptorDto.setTypeComponentList(TypeComponentList.MEASURE_DESCRIPTOR);

        return descriptorDto;
    }

    /**
     * createAttributeDescriptorDto
     * Descriptor
     * 
     * @return
     */
    public static DescriptorDto createGroupDimensionDescriptorDto() {
        DescriptorDto descriptorDto = new DescriptorDto();

        // Required
        descriptorDto.setUri(RandomStringUtils.random(50, true, true));
        descriptorDto.setTypeComponentList(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);

        return descriptorDto;
    }

//    /**
//     * Concept
//     * 
//     * @return
//     */
//    public static ConceptDto createConceptDTO() {
//        ConceptDto conceptDto = new ConceptDto();
//
//        // Required
//        conceptDto.setUri(RandomStringUtils.random(50, true, true));
//
//        // Required: Name
//        InternationalStringDto name = new InternationalStringDto();
//        LocalisedStringDto name_es = new LocalisedStringDto();
//        name_es.setLabel("NAME ES CONCEPT");
//        name_es.setLocale("es");
//        LocalisedStringDto name_en = new LocalisedStringDto();
//        name_en.setLabel("NAME EN CONCEPT");
//        name_en.setLocale("en");
//
//        name.addText(name_en);
//        name.addText(name_es);
//        conceptDto.setName(name);
//
//        // Others
//        conceptDto.setISOConceptReferenceAgency("iSOConceptReferenceAgency");
//        conceptDto.setISOConceptReferenceSchemeID("ISOConceptReferenceSchemeID");
//
//        return conceptDto;
//    }

    public static DataflowDefinitionDto createDataflowDefinitionDTO(String structureIdLogic) {
        DataflowDefinitionDto describedBy = new DataflowDefinitionDto();

        // Required
        // |_ name
        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto name_es = new LocalisedStringDto();
        name_es.setLabel("Ejemplo de nombre para DataFlow");
        name_es.setLocale("es");
        LocalisedStringDto name_en = new LocalisedStringDto();
        name_en.setLabel("Name example of DataFlow");
        name_en.setLocale("en");
        name.addText(name_en);
        name.addText(name_es);

        describedBy.setName(name);

        // maintainerIdLogic
        describedBy.setMaintainer(MetamacMocks.mockExternalItemDto(BaseDoMocks.mockAgencyUrn(), TypeExternalArtefactsEnum.AGENCY));

        // structure

        return describedBy;
    }

    public static ContentInputDto createContentInput(File file) throws FileNotFoundException {
        ContentInputDto contentDto = new ContentInputDto();
        contentDto.setInput(new FileInputStream(file));
        contentDto.setName(file.getName());
        return contentDto;
    }

    public static ContentInputDto createContentInput(String str) {
        ContentInputDto contentDto = new ContentInputDto();
        contentDto.setInput(new StringInputStream(str));
        contentDto.setName("DummyName");
        return contentDto;
    }
}

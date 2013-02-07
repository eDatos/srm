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
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;

import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.importation.ContentInputDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataflowDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RelationshipDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDataAttribute;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRelathionship;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.UsageStatus;

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
    public static DataStructureDefinitionMetamacDto createDataStructureDefinitionMetamacDtoMock() {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = new DataStructureDefinitionMetamacDto();

        // Required --------
        dataStructureDefinitionMetamacDto.setCode("DSD_" + RandomStringUtils.randomAlphabetic(10));

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
        dataStructureDefinitionMetamacDto.setName(name);

        // Maintainer
        dataStructureDefinitionMetamacDto.setMaintainer(new RelatedResourceDto(SrmBaseTest.AGENCY_ROOT_1_V1_CODE, SrmBaseTest.AGENCY_ROOT_1_V1, TypeExternalArtefactsEnum.AGENCY));

        // Other --------
        dataStructureDefinitionMetamacDto.setServiceURL("test");

        // Audit
        dataStructureDefinitionMetamacDto.setCreatedBy(serviceContext.getApplicationId());
        dataStructureDefinitionMetamacDto.setCreatedDate(new Date());

        return dataStructureDefinitionMetamacDto;
    }

    /**
     * @return
     */
    public static DataStructureDefinitionDto createDdsDTOwhithDescription() {
        DataStructureDefinitionDto dataStructureDefinitionDto = createDataStructureDefinitionMetamacDtoMock();

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

        DimensionComponentDto dimensionComponentDto1 = new DimensionComponentDto();
        // Required
        dimensionComponentDto1.setUriProvider(RandomStringUtils.random(50, true, true));
        dimensionComponentDto1.setTypeDimensionComponent(TypeDimensionComponent.DIMENSION);
        dimensionComponentDto1.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
        dimensionComponentDto1.setOrderLogic(1);
        dimensionComponentDto1.setCptIdRef(DataStructureDefinitionDtoMocks.mockExistingConceptIdentity());

        // Some Auditory
        dimensionComponentDto1.setCreatedBy("Junit");
        dimensionComponentDto1.setCreatedDate(new Date());

        dimensionComponentDto1.getRole().add(DataStructureDefinitionDtoMocks.mockExistingConceptRole());

        DimensionComponentDto dimensionComponentDto2 = new DimensionComponentDto();
        // Required
        dimensionComponentDto2.setUriProvider(RandomStringUtils.random(50, true, true));
        dimensionComponentDto2.setTypeDimensionComponent(TypeDimensionComponent.MEASUREDIMENSION);
        dimensionComponentDto2.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
        dimensionComponentDto2.setOrderLogic(2);
        dimensionComponentDto2.setCptIdRef(DataStructureDefinitionDtoMocks.mockExistingConceptIdentity());
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
    public static RelationshipDto createAttributeRelationship(SrmCoreServiceFacade srmCoreServiceFacade, String urnDsd) throws MetamacException {

        RelationshipDto relationshipDto = new RelationshipDto();

        // Required
        relationshipDto.setTypeRelathionship(TypeRelathionship.GROUP_RELATIONSHIP);

        // For Group
        // Save GroupDimensionDescriptors
        DescriptorDto descriptorDto4 = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(getServiceContext(), urnDsd, createGroupDimensionDescriptorDto());

        relationshipDto.setGroupKeyForGroupRelationship(descriptorDto4);

        return relationshipDto;
    }

    /**
     * DataAttribute
     * 
     * @return
     * @throws ApplicationException
     */
    public static List<DataAttributeDto> createDataAttribute(SrmCoreServiceFacade srmCoreServiceFacade, String urnDsd) throws MetamacException {

        // List<ExternalItemDto> conceptDtos = srmCoreServiceFacade.findConcepts(getServiceContext(), null);

        DataAttributeDto dataAttributeDto1 = new DataAttributeDto();
        // Required
        dataAttributeDto1.setCptIdRef(DataStructureDefinitionDtoMocks.mockExistingConceptIdentity());
        dataAttributeDto1.setRelateTo(createAttributeRelationship(srmCoreServiceFacade, urnDsd));
        dataAttributeDto1.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        dataAttributeDto1.setTypeDataAttribute(TypeDataAttribute.DATA_ATTRIBUTE);
        dataAttributeDto1.setUsageStatus(UsageStatus.CONDITIONAL);

        // Other
        dataAttributeDto1.setUriProvider(RandomStringUtils.random(50, true, true));

        DataAttributeDto dataAttributeDto2 = new DataAttributeDto();
        // Required
        dataAttributeDto2.setCptIdRef(DataStructureDefinitionDtoMocks.mockExistingConceptIdentity());
        dataAttributeDto2.setRelateTo(createAttributeRelationship(srmCoreServiceFacade, urnDsd));
        dataAttributeDto2.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        dataAttributeDto2.setTypeDataAttribute(TypeDataAttribute.REPORTING_YEAR_START_DAY);
        dataAttributeDto2.setUsageStatus(UsageStatus.CONDITIONAL);

        // Other
        dataAttributeDto2.setUriProvider(RandomStringUtils.random(50, true, true));

        DataAttributeDto dataAttributeDto3 = new DataAttributeDto();
        // Required
        dataAttributeDto3.setCptIdRef(DataStructureDefinitionDtoMocks.mockExistingConceptIdentity());
        dataAttributeDto3.setRelateTo(createAttributeRelationship(srmCoreServiceFacade, urnDsd));
        dataAttributeDto3.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);
        dataAttributeDto3.setTypeDataAttribute(TypeDataAttribute.DATA_ATTRIBUTE);
        dataAttributeDto3.setUsageStatus(UsageStatus.CONDITIONAL);

        // Other
        dataAttributeDto3.setUriProvider(RandomStringUtils.random(50, true, true));

        List<DataAttributeDto> dataAttributeDtos = new ArrayList<DataAttributeDto>();
        dataAttributeDtos.add(dataAttributeDto1);
        dataAttributeDtos.add(dataAttributeDto2);
        dataAttributeDtos.add(dataAttributeDto3);

        return dataAttributeDtos;
    }

    public static ComponentDto createPrimaryMeasureDto(SrmCoreServiceFacade srmCoreServiceFacade) {

        ComponentDto primaryMeasure = new ComponentDto();

        // Required
        primaryMeasure.setCptIdRef(DataStructureDefinitionDtoMocks.mockExistingConceptIdentity());
        primaryMeasure.setTypeComponent(TypeComponent.PRIMARY_MEASURE);

        // Other
        primaryMeasure.setUriProvider(RandomStringUtils.random(50, true, true));

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
        descriptorDto.setUriProvider(RandomStringUtils.random(50, true, true));
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
        descriptorDto.setUriProvider(RandomStringUtils.random(50, true, true));
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
        descriptorDto.setUriProvider(RandomStringUtils.random(50, true, true));
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
        descriptorDto.setCode("groupDimensionDescriptor-" + RandomStringUtils.random(10, true, true));
        descriptorDto.setUriProvider(RandomStringUtils.random(50, true, true));
        descriptorDto.setTypeComponentList(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);

        return descriptorDto;
    }

    // /**
    // * Concept
    // *
    // * @return
    // */
    // public static ConceptDto createConceptDTO() {
    // ConceptDto conceptDto = new ConceptDto();
    //
    // // Required
    // conceptDto.setUriProvider(RandomStringUtils.random(50, true, true));
    //
    // // Required: Name
    // InternationalStringDto name = new InternationalStringDto();
    // LocalisedStringDto name_es = new LocalisedStringDto();
    // name_es.setLabel("NAME ES CONCEPT");
    // name_es.setLocale("es");
    // LocalisedStringDto name_en = new LocalisedStringDto();
    // name_en.setLabel("NAME EN CONCEPT");
    // name_en.setLocale("en");
    //
    // name.addText(name_en);
    // name.addText(name_es);
    // conceptDto.setName(name);
    //
    // // Others
    // conceptDto.setISOConceptReferenceAgency("iSOConceptReferenceAgency");
    // conceptDto.setISOConceptReferenceSchemeID("ISOConceptReferenceSchemeID");
    //
    // return conceptDto;
    // }

    public static DataflowDefinitionDto createDataflowDefinitionDTO(String structureCode) {
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

        // maintainerCode
        describedBy.setMaintainer(new RelatedResourceDto(SrmBaseTest.AGENCY_ROOT_1_V1_CODE, SrmBaseTest.AGENCY_ROOT_1_V1, TypeExternalArtefactsEnum.AGENCY));

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

package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.dozer.DozerBeanMapper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.jaxb.CustomJaxb2Marshaller;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.serviceapi.DsdsMetamacService;
import org.siemac.metamac.srm.core.facade.serviceapi.utils.SrmDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.criteria.DataStructureDefinitionCriteriaPropertyEnum;
import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.trans.StructureMsgDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.message.Structure;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeDsdTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;
    
    @Autowired
    private DsdsMetamacService dsdsMetamacService;

    @Autowired
    protected BaseService          baseService;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller        marshallerWithValidation;

    @Autowired
    @Qualifier("jaxb2MarshallerWithoutValidation")
    private CustomJaxb2Marshaller  marshallerWithoutValidation;

    @Autowired
    @Qualifier("mapperCoreCopyAllMetadataMode")
    private DozerBeanMapper        mapper;

    private final ServiceContext   serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    // -------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------

//   @DataStructureDefinitionMetamacDto createDataStructureDefinition(@DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto updateDataStructureDefinition(@DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException;
//   
//   deleteDataStructureDefinition(String urn) throws MetamacException;
//   
//   MetamacCriteriaResult<@DataStructureDefinitionMetamacDto> findDsdByCondition(MetamacCriteria criteria) throws MetamacException;
//
    @Test
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> result;
        int previousSize;
//        dsdsMetamacService.createDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersion);
                
//        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> result = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), null);
//        int previousSize = result.getResults().size();
        
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacCreatedDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContext(), null);
//        assertEquals(previousSize + 1, result.getResults().size());
    }
//   @DataStructureDefinitionExtendDto retrieveExtendedDsd(String urn, @TypeDozerCopyMode typeDozerCopyMode) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto retrieveDsd(String urn, @TypeDozerCopyMode typeDozerCopyMode) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto retrieveDsdByUrn(String urn) throws MetamacException;                        
//
//   @DataStructureDefinitionMetamacDto createDsdVersion(String urn, boolean minorVersion) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto sendDataStructureDefinitionToProductionValidation(String urn) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto sendDataStructureDefinitionDtoToDiffusionValidation(String urn) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto rejectDataStructureDefinitionDtoProductionValidation(String urn) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto rejectDataStructureDefinitionDtoDiffusionValidation(String urn) throws MetamacException; 
//   
//   @DataStructureDefinitionMetamacDto publishInternallyDataStructureDefinitionDto(String urn) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto publishExternallyDataStructureDefinitionDto(String urn) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto versioningDataStructureDefinitionDto(String urnToCopy, @VersionTypeEnum versionType) throws MetamacException;
//   
//   @DataStructureDefinitionMetamacDto cancelDataStructureDefinitionDtoValidity(String urn) throws MetamacException;
//   
//   List<@DescriptorDto> findDescriptorForDsd(String urnDsd, @TypeComponentList typeComponentList) throws MetamacException;
//   
//   List<@DescriptorDto> findDescriptorsForDsd(String urnDsd) throws MetamacException;
//   
//   @DescriptorDto saveDescriptorForDsd(String urnDsd, @DescriptorDto descriptorDto) throws MetamacException;
//   
//   deleteDescriptorForDsd(String urnDsd, @DescriptorDto descriptorDto)throws MetamacException;
//
//   @ComponentDto saveComponentForDsd(String urnDsd, @ComponentDto componentDto, @TypeComponentList typeComponentList) throws MetamacException;
//   
//   deleteComponentForDsd(String urnDsd, @ComponentDto componentDto, @TypeComponentList typeComponentList) throws MetamacException;
//   
//   importSDMXStructureMsg(@ContentInputDto contentDto) throws MetamacException;
//   
//   String exportSDMXStructureMsg(@StructureMsgDto structureMsgDto) throws MetamacException;

    
    @Test
    @Ignore
    public void testSaveDsd() throws Exception {

        // Create ****************
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        assertNotNull(dataStructureDefinitionMetamacDto.getId());

        // Update Name & Description **********************
        InternationalStringDto name = new InternationalStringDto();

        LocalisedStringDto name_es = new LocalisedStringDto();
        name_es.setLabel("NAME ES DSD CHANGED");
        name_es.setLocale("es");
        LocalisedStringDto name_en = new LocalisedStringDto();
        name_en.setLabel("NAME EN DSD CHANGED");
        name_en.setLocale("en");

        name.addText(name_en);
        name.addText(name_es);

        dataStructureDefinitionMetamacDto.setName(name);

        dataStructureDefinitionMetamacDto.setDescription(null);

        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.updateDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto);

        assertTrue(dataStructureDefinitionMetamacDto.getName().getTexts().iterator().next().getLabel().contains("CHANGED"));
        assertNull(dataStructureDefinitionMetamacDto.getDescription());
    }
    
    @Test
    @Ignore
    public void testRemoveLabel() throws Exception {
        // Remove Label --> Dont' save ********************
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());
        InternationalStringDto name = dataStructureDefinitionMetamacDto.getName();
    
        LocalisedStringDto localisedStringDto = name.getTexts().iterator().next();
        localisedStringDto.setLabel(StringUtils.EMPTY);
    
        dataStructureDefinitionMetamacDto.setName(name);
    
        try {
            dataStructureDefinitionMetamacDto = srmCoreServiceFacade.updateDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto);
            fail("");
        } catch (Exception e) {
        }
    }

    @Test
    @Ignore
    public void testCreateDsdVersion() throws Exception {
        // Save
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());
        assertNotNull(dataStructureDefinitionMetamacDto);

        // Create DSD Version
        String previousVersion = dataStructureDefinitionMetamacDto.getUrn();
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinitionVersion(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), Boolean.TRUE);

        assertNotNull(dataStructureDefinitionMetamacDto.getUrn());
        assertTrue(previousVersion != dataStructureDefinitionMetamacDto.getUrn());
        assertTrue(dataStructureDefinitionMetamacDto.getVersionLogic().equals("01.001"));
    }

    @Test
    @Ignore
    public void testDeleteDsd() throws Exception {
        //TODO TEST delete DSD en fachada
        /*
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        MetamacCriteriaResult<DataStructureDefinitionDto> result = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), null);
        int previousSize = result.getResults().size();
        
        srmCoreServiceFacade.ddeleteDsd(getServiceContext(), dataStructureDefinitionDto);

        result = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), null);
        assertEquals(previousSize - 1, result.getResults().size());
        */
    }

    @Test
    @Ignore
    public void testFindDsdByCondition() throws Exception {
        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContext(), null);
        int previousSize = result.getResults().size();
        
        srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContext(), null);
        assertEquals(previousSize + 1, result.getResults().size());
    }

    // -------------------------------------------------------------------------------
    // Descriptors
    // -------------------------------------------------------------------------------

    @Test
    @Ignore
    public void testFindDescriptorsForDsd() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save DimensionDescriptor
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDescriptorDto());

        // Find DimensionDescriptor
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn());

        assertTrue(!descriptorDtos.isEmpty());
    }

    @Test
    @Ignore
    public void testFindDescriptorForDsd() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save DimensionDescriptor
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDescriptorDto());

        // Find DimensionDescriptor
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDtos.isEmpty());
    }

    // TODO está dando nullpointers
    @Test
    @Ignore
    public void testFindDescriptorForDsdThread() throws Exception {
        // TODO quitar este test???
        /*
        final List<DataStructureDefinitionDto> dataStructureDefinitionDtos = srmCoreServiceFacade.findAllDsds(getServiceContext());

        class BasicThread1 extends Thread {

            private String name = null;

            public BasicThread1(String name) {
                this.name = name;
            }
            // This method is called when the thread runs
            public void run() {
                List<DescriptorDto> descriptorDtos;
                try {
                    System.out.println("Begin: " + name);
                    descriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDtos.get(dataStructureDefinitionDtos.size() - 1).getId(),
                            TypeComponentList.DIMENSION_DESCRIPTOR);
                    System.out.println("End: " + name);
                    assertTrue(!descriptorDtos.isEmpty());
                } catch (MetamacException e) {
                    e.printStackTrace();
                }
            }
        }

        int numTh = 20;
        BasicThread1[] thArray = new BasicThread1[numTh];

        for (int i = 0; i < numTh; i++) {
            thArray[i] = new BasicThread1(String.valueOf(i));
        }

        for (int i = 0; i < numTh; i++) {

            thArray[i].start();
        }

        Thread.sleep(10000);
*/
    }

    @Test
    @Ignore
    public void testSaveDescriptorForDsd() throws Exception {
        saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);
    }

    public static DataStructureDefinitionMetamacDto saveDescriptorForDsd(ServiceContext ctx, SrmCoreServiceFacade srmCoreServiceFacade) throws MetamacException {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = SrmDtoMocks.createDdsDTO();

        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(ctx, dataStructureDefinitionMetamacDto);

        // Save DimensionDescriptors
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDescriptorDto());

        assertNotNull(descriptorDto);

        // Save AttributeDescriptors
        DescriptorDto descriptorDto2 = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createAttributeDescriptorDto());

        assertNotNull(descriptorDto2);

        // Save MeasureDescriptors
        DescriptorDto descriptorDto3 = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createMeasureDescriptorDto());

        assertNotNull(descriptorDto3);

        // Save GroupDimensionDescriptors
        DescriptorDto descriptorDto4 = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(ctx, dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createGroupDimensionDescriptorDto());

        assertNotNull(descriptorDto4);

        return dataStructureDefinitionMetamacDto;
    }

    @Test
    @Ignore
    public void testCreateDescriptorAndThenAssociateRepresentation() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save PrimaryMeasure
        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createPrimaryMeasureDto(srmCoreServiceFacade),
                TypeComponentList.MEASURE_DESCRIPTOR);

        assertNotNull(componentDto);

        assertNull(componentDto.getLocalRepresentation());

        RepresentationDto representationDto = new RepresentationDto();
        representationDto.setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);
        FacetDto facetDto = new FacetDto();
        facetDto.setFacetValue(FacetValueTypeEnum.ALPHA_FVT);
        // facetDto.setStringFVT("PEPE");
        facetDto.setPatternFT("*");
        representationDto.setNonEnumerated(facetDto);

        componentDto.setLocalRepresentation(representationDto);

        componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), componentDto, TypeComponentList.MEASURE_DESCRIPTOR);

        assertNotNull(componentDto);

        assertNotNull(componentDto.getLocalRepresentation());
    }

    @Test
    @Ignore
    public void testDeleteDescriptorForDsd() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save DimensionDescriptors
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDescriptorDto());

        srmCoreServiceFacade.deleteDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), descriptorDto);

        List<DescriptorDto> descriptorDtoList = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(descriptorDtoList.isEmpty());
    }

    @Test
    @Ignore
    public void testSaveComponentForDsd() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    @Ignore
    public void testSaveComponentForDsdWithDescriptorExist() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(1),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    @Ignore
    public void testSaveComponentForDsdWithDescriptorAndComponentExist() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        componentDto.setUrn("Change to test");

        componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), componentDto, TypeComponentList.DIMENSION_DESCRIPTOR);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    @Ignore
    public void testDeleteComponentForDsd() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // SAVE COMPONENT
        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDtoList = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDtoList.get(0).getComponents().isEmpty());

        // DELETE
        srmCoreServiceFacade.deleteComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), componentDto, TypeComponentList.DIMENSION_DESCRIPTOR);

        descriptorDtoList = srmCoreServiceFacade.findDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(descriptorDtoList.get(0).getComponents().isEmpty());
    }

    @Test
    @Ignore
    public void testRetrieveExtendedDsd() throws Exception {

        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = SrmCoreServiceFacadeDsdTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeDozerCopyMode.COPY_TO_VERSIONING);

        assertNotNull(dataStructureDefinitionExtendDto);
    }

    @Test
    @Ignore
    public void testRetrieveDsd() throws Exception {
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = SrmCoreServiceFacadeDsdTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto2 = srmCoreServiceFacade.retrieveDataStructureDefinition(getServiceContext(), dataStructureDefinitionMetamacDto.getUrn(), TypeDozerCopyMode.COPY_TO_VERSIONING);

        assertNotNull(dataStructureDefinitionMetamacDto2);
    }

    @Test
    @Ignore
    public void testImportSDMXStructureMsg() throws Exception {
        File file = null;

        // ECB **************************
        file = new File(SdmxResources.DSD_ECB_EXR_NG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_ECB_EXR_SG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_ECB_EXR_RG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        // INE **************************
        file = new File(SdmxResources.DSD_INE_DPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_INE_EPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_INE_IDB);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_INE_IPC);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_INE_IPCA);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SdmxResources.DSD_INE_MNP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

    }

    @Test
    @Ignore
    public void testExportSDMXStructureMsg() throws Exception {

        // DSD: ECB_EXR_RG
        exportStructureMessage("ECB_EXR_NG");

        // DSD: ECB_EXR_RG
        exportStructureMessage("ECB_EXR_RG");

        // DSD: ECB_EXR_RG
        exportStructureMessage("ECB_EXR_SG");
    }

    private void exportStructureMessage(String code) throws MetamacException, FileNotFoundException {
        StructureMsgDto structureMsgDto = new StructureMsgDto();

        // DSD
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        MetamacCriteriaDisjunctionRestriction disjunction = new MetamacCriteriaDisjunctionRestriction();
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(DataStructureDefinitionCriteriaPropertyEnum.CODE.name(), code, OperationType.EQ));
        metamacCriteria.setRestriction(disjunction);

        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> metamacCriteriaResult = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContext(), metamacCriteria);

        if (metamacCriteriaResult.getResults().isEmpty()) {
            if (code.equals("ECB_EXR_NG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, SrmDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_NG_FULL)));
            } else if (code.equals("ECB_EXR_RG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, SrmDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_RG_FULL)));
            } else if (code.equals("ECB_EXR_SG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, SrmDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_SG_FULL)));
            } else {
                fail("Error in test");
            }
            metamacCriteriaResult = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContext(), metamacCriteria);
        }

        DataStructureDefinitionDto dataStructureDefinitionDto = metamacCriteriaResult.getResults().iterator().next();
        // Create DSD extend (with descriptors)
        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = mapper.map(metamacCriteriaResult.getResults().iterator().next(), DataStructureDefinitionExtendDto.class);
        // Name and descriptions
        dataStructureDefinitionExtendDto.setName(dataStructureDefinitionDto.getName());
        dataStructureDefinitionExtendDto.setDescription(dataStructureDefinitionDto.getDescription());
        // Annotations
        dataStructureDefinitionExtendDto.getAnnotations().addAll(dataStructureDefinitionDto.getAnnotations());

        // Add descriptors
        dataStructureDefinitionExtendDto.getGrouping().addAll(srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(serviceContext, dataStructureDefinitionDto.getUrn()));

        structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);

        System.out.println("Output: " + srmCoreServiceFacade.exportSDMXStructureMsg(serviceContext, structureMsgDto));
    }

    @Test
    @Ignore
    public void testExportSDMXStructureMsgValidation() throws Exception {

        List<FileInputStream> fileInputStreams = new ArrayList<FileInputStream>();

        // ECB **************************
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_ECB_EXR_NG_FULL)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_ECB_EXR_SG_FULL)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_ECB_EXR_RG_FULL)));

        // INE **************************
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_INE_DPOP)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_INE_EPOP)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_INE_IDB)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_INE_IPC)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_INE_IPCA)));
        fileInputStreams.add(new FileInputStream(new File(SdmxResources.DSD_INE_MNP)));

        for (FileInputStream fis : fileInputStreams) {

            // 1. Extract Structure
            Structure structure = (Structure) marshallerWithValidation.unmarshal(new StreamSource(fis));

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            // 2. Marshaller.JAXB_FORMATTED_OUTPUT, true
            Map<String, Object> marshallProperties = new HashMap<String, Object>();
            marshallProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerWithoutValidation.setMarshallerProperties(marshallProperties);

            marshallerWithoutValidation.marshal(structure, result);

            // System.out.println(writer.toString());
        }

    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
}

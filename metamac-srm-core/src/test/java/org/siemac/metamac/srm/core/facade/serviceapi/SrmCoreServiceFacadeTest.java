package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.id;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.idLogic;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.serviceURL;

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
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.mock.OracleJNDIMock;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.jaxb.CustomJaxb2Marshaller;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.domain.srm.enume.domain.TypeRepresentationEnum;
import org.siemac.metamac.domain.trans.dto.StructureMsgDto;
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseService;
import org.siemac.metamac.srm.core.common.test.SDMXResources;
import org.siemac.metamac.srm.core.mock.Mocks;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.trans.v2_1.message.Structure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-core/oracle/applicationContext-test.xml"})
public class SrmCoreServiceFacadeTest implements SrmCoreServiceFacadeTestBase {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    protected SdmxBaseService            sdmxBaseService;
    
    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller              marshallerWithValidation;
    
    @Autowired
    @Qualifier("jaxb2MarshallerWithoutValidation")
    private CustomJaxb2Marshaller        marshallerWithoutValidation;
    
    @Autowired
    @Qualifier("mapperCoreUpdateMode")
    private DozerBeanMapper              mapper;

    private final ServiceContext         serviceContext               = new ServiceContext("system", "123456", "junit");

    private final String                 organizationByDefaultLogicId = "METAMAC_ORGANISATION";

    static Logger                        logger                       = Logger.getLogger(SrmCoreServiceFacadeTest.class.getName());

    @BeforeClass
    public static void setUp() {
        // JNDI SDMXDS
        SimpleNamingContextBuilder simpleNamingContextBuilder = OracleJNDIMock.setUp("SDMXDS", "srm_test", "srm_test", "jdbc:oracle:thin:@localhost:1521:XE", null);
    }

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }


    /**************************************************************************
     * DSDs
     **************************************************************************/
    @Test
    public void testReSaveDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), Mocks.createDdsDTOwhitDescription());

        InternationalStringDto name = new InternationalStringDto();

        LocalisedStringDto name_es = new LocalisedStringDto();
        name_es.setLabel("NAME ES DSD CHANGED");
        name_es.setLocale("es");
        LocalisedStringDto name_en = new LocalisedStringDto();
        name_en.setLabel("NAME EN DSD CHANGED");
        name_en.setLocale("en");

        name.addText(name_en);
        name.addText(name_es);

        dataStructureDefinitionDto.setName(name);

        dataStructureDefinitionDto.setDescription(null);

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        assertNotNull(dataStructureDefinitionDto);
    }

    @Test
    public void testDeleteOneLocalisedSaveDsd() throws Exception {

        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), Mocks.createDdsDTOwhitDescription());

        InternationalStringDto name = dataStructureDefinitionDto.getName();

        LocalisedStringDto localisedStringDto = name.getTexts().iterator().next();
        localisedStringDto.setLabel(StringUtils.EMPTY);

        dataStructureDefinitionDto.setName(name);

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        assertNotNull(dataStructureDefinitionDto);
    }

    @Test
    @Override
    public void testSaveDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        assertNotNull(dataStructureDefinitionDto);
    }
    

    @Test
    @Override
    public void testCreateDsdVersion() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        assertNotNull(dataStructureDefinitionDto);
        
        dataStructureDefinitionDto = srmCoreServiceFacade.createDsdVersion(getServiceContext(), dataStructureDefinitionDto.getId(), Boolean.TRUE);
        
        assertNotNull(dataStructureDefinitionDto);
        assertTrue(dataStructureDefinitionDto.getVersionLogic().equals("01.001"));
    }

    @Test
    @Override
    public void testDeleteDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), Mocks.createDdsDTO());

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(id()).eq(dataStructureDefinitionDto.getId()).build();

        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));
        assertTrue(!dataStructureDefinitionDtoPagedList.getValues().isEmpty());

        srmCoreServiceFacade.deleteDsd(getServiceContext(), dataStructureDefinitionDtoPagedList.getValues().get(0));

        dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(dataStructureDefinitionDtoPagedList.getValues().isEmpty());
    }

    @Test
    @Override
    public void testFindDsdByCondition() throws Exception {
        // Save DSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(serviceURL()).eq("www.prueba.url").build();

        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(!dataStructureDefinitionDtoPagedList.getValues().isEmpty());
    }

    @Test
    @Override
    public void testFindAllDsds() throws Exception {
        // Save DSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        List<DataStructureDefinitionDto> dsds = srmCoreServiceFacade.findAllDsds(getServiceContext());
        assertTrue(!dsds.isEmpty());
    }

    /**************************************************************************
     * Descriptors
     **************************************************************************/

    @Test
    @Override
    public void testFindDescriptorsForDsd() throws Exception {
        // Save DSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        // Save DimensionDescriptor
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDescriptorDto());

        // Find DimensionDescriptor
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorsForDsd(getServiceContext(), dataStructureDefinitionDto.getId());

        assertTrue(!descriptorDtos.isEmpty());
    }
    
    @Test
    @Override
    public void testFindDescriptorForDsd() throws Exception {
        // Save DSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        // Save DimensionDescriptor
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDescriptorDto());

        // Find DimensionDescriptor
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDtos.isEmpty());
    }

    @Test
    public void testFindDescriptorForDsdThread() throws Exception {
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

    }

    @Test
    @Override
    public void testSaveDescriptorForDsd() throws Exception {
        saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);
    }

    public static DataStructureDefinitionDto saveDescriptorForDsd(ServiceContext ctx, SrmCoreServiceFacade srmCoreServiceFacade) throws MetamacException {
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(ctx, dataStructureDefinitionDto);

        // Save DimensionDescriptors
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), Mocks.createDimensionDescriptorDto());

        assertNotNull(descriptorDto);

        // Save AttributeDescriptors
        DescriptorDto descriptorDto2 = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), Mocks.createAttributeDescriptorDto());

        assertNotNull(descriptorDto2);

        // Save MeasureDescriptors
        DescriptorDto descriptorDto3 = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), Mocks.createMeasureDescriptorDto());

        assertNotNull(descriptorDto3);

        // Save GroupDimensionDescriptors
        DescriptorDto descriptorDto4 = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), Mocks.createGroupDimensionDescriptorDto());

        assertNotNull(descriptorDto4);

        return dataStructureDefinitionDto;
    }

    @Test
    public void testCreateDescriptorAndThenAssociateRepresentation() throws Exception {
        // Test SaveDSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        // Save PrimaryMeasure
        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createPrimaryMeasureDto(srmCoreServiceFacade),
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

        componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), componentDto, TypeComponentList.MEASURE_DESCRIPTOR);

        assertNotNull(componentDto);

        assertNotNull(componentDto.getLocalRepresentation());
    }

    @Test
    @Override
    public void testDeleteDescriptorForDsd() throws Exception {
        // Test SaveDSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        // Save DimensionDescriptors
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDescriptorDto());

        srmCoreServiceFacade.deleteDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), descriptorDto);

        List<DescriptorDto> descriptorDtoList = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(descriptorDtoList.isEmpty());
    }

    @Test
    @Override
    public void testSaveComponentForDsd() throws Exception {
        // Test SaveDSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDtos(srmCoreServiceFacade)
                .get(0), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    public void testSaveComponentForDsdWithDescriptorExist() throws Exception {
        // SaveDSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDtos(srmCoreServiceFacade)
                .get(0), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDtos(srmCoreServiceFacade).get(1),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    public void testSaveComponentForDsdWithDescriptorAndComponentExist() throws Exception {
        // SaveDSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDtos(srmCoreServiceFacade)
                .get(0), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        componentDto.setUrn("Change to test");

        componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), componentDto, TypeComponentList.DIMENSION_DESCRIPTOR);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    @Override
    public void testDeleteComponentForDsd() throws Exception {
        // SaveDSD
        DataStructureDefinitionDto dataStructureDefinitionDto = Mocks.createDdsDTO();
        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        // SAVE COMPONENT
        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), Mocks.createDimensionDtos(srmCoreServiceFacade)
                .get(0), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDtoList = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDtoList.get(0).getComponents().isEmpty());

        // DELETE
        srmCoreServiceFacade.deleteComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), componentDto, TypeComponentList.DIMENSION_DESCRIPTOR);

        descriptorDtoList = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(descriptorDtoList.get(0).getComponents().isEmpty());
    }

    @Test
    @Override
    public void testFindConceptSchemeRefs() throws Exception {
        // TODO make test
        srmCoreServiceFacade.findConceptSchemeRefs(getServiceContext());
    }

    @Test
    @Override
    public void testFindConcepts() throws Exception {
        // TODO make test
        srmCoreServiceFacade.findConcepts(getServiceContext(), StringUtils.EMPTY);
    }


    @Test
    @Override
    public void testFindCodelists() throws Exception {
        // TODO make test
        srmCoreServiceFacade.findCodelists(getServiceContext(), StringUtils.EMPTY);
    }


    @Override
    @Test
    public void testRetrieveExtendedDsd() throws Exception {

        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDsd(getServiceContext(), dataStructureDefinitionDto.getId(),
                TypeDozerCopyMode.CREATE);

        assertNotNull(dataStructureDefinitionExtendDto);
    }

    @Override
    @Test
    public void testSaveDsdGraph() throws Exception {

        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        dataStructureDefinitionDto = srmCoreServiceFacade.createDsdVersion(getServiceContext(), dataStructureDefinitionDto.getId(), Boolean.TRUE);

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(idLogic()).eq(dataStructureDefinitionDto.getIdLogic()).build();

        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(dataStructureDefinitionDtoPagedList.getTotalRows() == 2);

    }

    @Test
    @Override
    public void testRetrieveDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionDto dataStructureDefinitionDto2 = srmCoreServiceFacade.retrieveDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeDozerCopyMode.CREATE);

        assertNotNull(dataStructureDefinitionDto2);
    }

    @Test
    @Override
    public void testFindOrganisation() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testCreateConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testUpdateConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testDeleteConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testFindAllConceptSchemes() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testRetrieveConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testSendConceptSchemeToPendingPublication() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testPublishConceptSchemeInternally() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testPublishConceptSchemeExternally() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testCreateConcept() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testUpdateConcept() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testDeleteConcept() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testFindConceptsForConceptScheme() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testRetrieveConcept() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testSendConceptToPendingPublication() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testPublishConceptInternally() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testPublishConceptExternally() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testImportSDMXStructureMsg() throws Exception {
        File file = null;

        // ECB **************************
        file = new File(SDMXResources.DSD_ECB_EXR_NG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_ECB_EXR_SG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_ECB_EXR_RG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        // INE **************************
        file = new File(SDMXResources.DSD_INE_DPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_EPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_IDB);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_IPC);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_IPCA);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_MNP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), Mocks.createContentInput(file));

    }

    @Test
    @Override
    public void testExportSDMXStructureMsg() throws Exception {

        // DSD: ECB_EXR_RG
        exportStructureMessage("ECB_EXR_NG");

        // DSD: ECB_EXR_RG
        exportStructureMessage("ECB_EXR_RG");

        // DSD: ECB_EXR_RG
        exportStructureMessage("ECB_EXR_SG"); 
    }
    
    private void exportStructureMessage(String dsdId) throws MetamacException, FileNotFoundException {
        StructureMsgDto structureMsgDto = new StructureMsgDto();

        // DSD
        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = findDSD(dsdId);

        if (dataStructureDefinitionDtoPagedList.getValues().isEmpty()) {
            if (dsdId.equals("ECB_EXR_NG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, Mocks.createContentInput(new File(SDMXResources.DSD_ECB_EXR_NG_FULL)));
            } else if (dsdId.equals("ECB_EXR_RG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, Mocks.createContentInput(new File(SDMXResources.DSD_ECB_EXR_RG_FULL)));
            } else if (dsdId.equals("ECB_EXR_SG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, Mocks.createContentInput(new File(SDMXResources.DSD_ECB_EXR_SG_FULL)));
            } else {
                fail("Error in test");
            }
            dataStructureDefinitionDtoPagedList = findDSD(dsdId);
        }

        // Create DSD extend (with desciptors)
        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = mapper.map(dataStructureDefinitionDtoPagedList.getValues().get(0), DataStructureDefinitionExtendDto.class);
        // Name and descriptions
        dataStructureDefinitionExtendDto.setName(dataStructureDefinitionDtoPagedList.getValues().get(0).getName());
        dataStructureDefinitionExtendDto.setDescription(dataStructureDefinitionDtoPagedList.getValues().get(0).getDescription());
        // Annotations
        dataStructureDefinitionExtendDto.getAnnotations().addAll(dataStructureDefinitionDtoPagedList.getValues().get(0).getAnnotations());

        // Add descriptors
        dataStructureDefinitionExtendDto.getGrouping().addAll(srmCoreServiceFacade.findDescriptorsForDsd(serviceContext, dataStructureDefinitionDtoPagedList.getValues().get(0).getId()));

        structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);

        System.out.println("Output: " + srmCoreServiceFacade.exportSDMXStructureMsg(serviceContext, structureMsgDto));
    }

    @Test
    public void testExportSDMXStructureMsgValidation() throws Exception {

        List<FileInputStream> fileInputStreams = new ArrayList<FileInputStream>();

        // ECB **************************
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_ECB_EXR_NG_FULL)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_ECB_EXR_SG_FULL)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_ECB_EXR_RG_FULL)));

        // INE **************************
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_INE_DPOP)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_INE_EPOP)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_INE_IDB)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_INE_IPC)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_INE_IPCA)));
        fileInputStreams.add(new FileInputStream(new File(SDMXResources.DSD_INE_MNP)));


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
    
    
    // *************************************************************************
    // COMMON
    // *************************************************************************

    private PagedResult<DataStructureDefinitionDto> findDSD(String dsdId) throws MetamacException {
        List<ConditionalCriteria> dsdConditions = criteriaFor(DataStructureDefinition.class).withProperty(idLogic()).eq(dsdId).build();

        return srmCoreServiceFacade.findDsdByCondition(serviceContext, dsdConditions, PagingParameter.pageAccess(10));
    }

}

package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.id;
import static org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties.code;
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
import org.dozer.DozerBeanMapper;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.siemac.metamac.srm.core.base.serviceapi.BaseService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.facade.serviceapi.utils.SDMXResources;
import org.siemac.metamac.srm.core.facade.serviceapi.utils.SrmDtoMocks;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.trans.v2_1.message.Structure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

// TODO esta clase debería tender a sustituirse por mockear los servicios con Mockito (SrmCoreServiceFacadeCoreMockedTest)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeTest extends SrmBaseTest /* implements SrmCoreServiceFacadeTestBase */{

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    protected BaseService          baseService;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller        marshallerWithValidation;

    @Autowired
    @Qualifier("jaxb2MarshallerWithoutValidation")
    private CustomJaxb2Marshaller  marshallerWithoutValidation;

    @Autowired
    @Qualifier("mapperCoreUpdateMode")
    private DozerBeanMapper        mapper;

    private final ServiceContext   serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    /**************************************************************************
     * DSDs
     **************************************************************************/

    @Test
    public void testSaveDsd() throws Exception {

        // Save
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        assertNotNull(dataStructureDefinitionDto.getId());

        // Update Name & Description
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

        srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);

        assertTrue(dataStructureDefinitionDto.getName().getTexts().iterator().next().getLabel().contains("CHANGED"));
        assertNull(dataStructureDefinitionDto.getDescription());

        // Remove Label --> Dont' save
        name = dataStructureDefinitionDto.getName();

        LocalisedStringDto localisedStringDto = name.getTexts().iterator().next();
        localisedStringDto.setLabel(StringUtils.EMPTY);

        dataStructureDefinitionDto.setName(name);

        try {
            dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), dataStructureDefinitionDto);
            fail("");
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateDsdVersion() throws Exception {
        // Save
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());
        assertNotNull(dataStructureDefinitionDto);

        // Create DSD Version
        Long previousVersion = dataStructureDefinitionDto.getId();
        dataStructureDefinitionDto = srmCoreServiceFacade.createDsdVersion(getServiceContext(), dataStructureDefinitionDto.getId(), Boolean.TRUE);

        assertNotNull(dataStructureDefinitionDto.getId());
        assertTrue(previousVersion != dataStructureDefinitionDto.getId());
        assertTrue(dataStructureDefinitionDto.getVersionLogic().equals("01.001"));
    }

    @Test
    public void testDeleteDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(id()).eq(dataStructureDefinitionDto.getId()).build();

        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));
        assertTrue(!dataStructureDefinitionDtoPagedList.getValues().isEmpty());

        srmCoreServiceFacade.deleteDsd(getServiceContext(), dataStructureDefinitionDtoPagedList.getValues().get(0));

        dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(dataStructureDefinitionDtoPagedList.getValues().isEmpty());
    }

    @Test
    public void testFindDsdByCondition() throws Exception {
        srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        List<ConditionalCriteria> conditions = criteriaFor(DataStructureDefinition.class).withProperty(serviceURL()).eq("test").build();

        PagedResult<DataStructureDefinitionDto> dataStructureDefinitionDtoPagedList = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), conditions, PagingParameter.pageAccess(10));

        assertTrue(!dataStructureDefinitionDtoPagedList.getValues().isEmpty());
    }

    @Test
    public void testFindAllDsds() throws Exception {
        List<DataStructureDefinitionDto> dsds = srmCoreServiceFacade.findAllDsds(getServiceContext());
        int previousSize = dsds.size();

        srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        dsds = srmCoreServiceFacade.findAllDsds(getServiceContext());
        assertTrue(!dsds.isEmpty());
        assertTrue(previousSize + 1 == dsds.size());

    }

    /**************************************************************************
     * Descriptors
     **************************************************************************/

    @Test
    public void testFindDescriptorsForDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save DimensionDescriptor
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDescriptorDto());

        // Find DimensionDescriptor
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorsForDsd(getServiceContext(), dataStructureDefinitionDto.getId());

        assertTrue(!descriptorDtos.isEmpty());
    }

    @Test
    public void testFindDescriptorForDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save DimensionDescriptor
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDescriptorDto());

        // Find DimensionDescriptor
        List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDtos.isEmpty());
    }

    // TODO está dando nullpointers
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
    public void testSaveDescriptorForDsd() throws Exception {
        saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);
    }

    public static DataStructureDefinitionDto saveDescriptorForDsd(ServiceContext ctx, SrmCoreServiceFacade srmCoreServiceFacade) throws MetamacException {
        DataStructureDefinitionDto dataStructureDefinitionDto = SrmDtoMocks.createDdsDTO();

        dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(ctx, dataStructureDefinitionDto);

        // Save DimensionDescriptors
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDescriptorDto());

        assertNotNull(descriptorDto);

        // Save AttributeDescriptors
        DescriptorDto descriptorDto2 = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), SrmDtoMocks.createAttributeDescriptorDto());

        assertNotNull(descriptorDto2);

        // Save MeasureDescriptors
        DescriptorDto descriptorDto3 = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), SrmDtoMocks.createMeasureDescriptorDto());

        assertNotNull(descriptorDto3);

        // Save GroupDimensionDescriptors
        DescriptorDto descriptorDto4 = srmCoreServiceFacade.saveDescriptorForDsd(ctx, dataStructureDefinitionDto.getId(), SrmDtoMocks.createGroupDimensionDescriptorDto());

        assertNotNull(descriptorDto4);

        return dataStructureDefinitionDto;
    }

    @Test
    public void testCreateDescriptorAndThenAssociateRepresentation() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save PrimaryMeasure
        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createPrimaryMeasureDto(srmCoreServiceFacade),
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
    public void testDeleteDescriptorForDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // Save DimensionDescriptors
        DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDescriptorDto());

        srmCoreServiceFacade.deleteDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), descriptorDto);

        List<DescriptorDto> descriptorDtoList = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(descriptorDtoList.isEmpty());
    }

    @Test
    public void testSaveComponentForDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    public void testSaveComponentForDsdWithDescriptorExist() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(1),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    public void testSaveComponentForDsdWithDescriptorAndComponentExist() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        componentDto.setUrn("Change to test");

        componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), componentDto, TypeComponentList.DIMENSION_DESCRIPTOR);

        List<DescriptorDto> descriptorDto = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDto.isEmpty());

    }

    @Test
    public void testDeleteComponentForDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        // SAVE COMPONENT
        ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), SrmDtoMocks.createDimensionDtos(srmCoreServiceFacade).get(0),
                TypeComponentList.DIMENSION_DESCRIPTOR);

        assertNotNull(componentDto);

        List<DescriptorDto> descriptorDtoList = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(!descriptorDtoList.get(0).getComponents().isEmpty());

        // DELETE
        srmCoreServiceFacade.deleteComponentForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), componentDto, TypeComponentList.DIMENSION_DESCRIPTOR);

        descriptorDtoList = srmCoreServiceFacade.findDescriptorForDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeComponentList.DIMENSION_DESCRIPTOR);

        assertTrue(descriptorDtoList.get(0).getComponents().isEmpty());
    }

    @Test
    public void testFindConceptSchemeRefs() throws Exception {
        // TODO make test
        srmCoreServiceFacade.findConceptSchemeRefs(getServiceContext());
    }

    @Test
    public void testFindConcepts() throws Exception {
        // TODO make test
        srmCoreServiceFacade.findConcepts(getServiceContext(), StringUtils.EMPTY);
    }

    @Test
    public void testFindCodelists() throws Exception {
        // TODO make test
        srmCoreServiceFacade.findCodelists(getServiceContext(), StringUtils.EMPTY);
    }

    @Test
    public void testRetrieveExtendedDsd() throws Exception {

        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeDozerCopyMode.CREATE);

        assertNotNull(dataStructureDefinitionExtendDto);
    }

    @Test
    public void testSaveDsdGraph() throws Exception {

        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        List<DataStructureDefinitionDto> dataStructureDefinitionDtoList = srmCoreServiceFacade.findAllDsds(getServiceContext());
        assertTrue(dataStructureDefinitionDtoList.size() == 1);

        // In UPDATE MODE (no create)
        DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeDozerCopyMode.UPDATE);

        srmCoreServiceFacade.saveDsdGraph(getServiceContext(), dataStructureDefinitionExtendDto);

        dataStructureDefinitionDtoList = srmCoreServiceFacade.findAllDsds(getServiceContext());
        assertTrue(dataStructureDefinitionDtoList.size() == 1);

        // In UPDATE MODE (no create)
        dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeDozerCopyMode.CREATE);

        srmCoreServiceFacade.saveDsdGraph(getServiceContext(), dataStructureDefinitionExtendDto);

        dataStructureDefinitionDtoList = srmCoreServiceFacade.findAllDsds(getServiceContext());
        assertTrue(dataStructureDefinitionDtoList.size() == 2);
    }

    @Test
    public void testRetrieveDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionDto dataStructureDefinitionDto2 = srmCoreServiceFacade.retrieveDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeDozerCopyMode.CREATE);

        assertNotNull(dataStructureDefinitionDto2);
    }

    @Ignore
    // TODO está fallando
    @Test
    public void testImportSDMXStructureMsg() throws Exception {
        File file = null;

        // ECB **************************
        file = new File(SDMXResources.DSD_ECB_EXR_NG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_ECB_EXR_SG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_ECB_EXR_RG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        // INE **************************
        file = new File(SDMXResources.DSD_INE_DPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_EPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_IDB);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_IPC);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_IPCA);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

        file = new File(SDMXResources.DSD_INE_MNP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContext(), SrmDtoMocks.createContentInput(file));

    }

    @Ignore
    // TODO está fallando
    @Test
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
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, SrmDtoMocks.createContentInput(new File(SDMXResources.DSD_ECB_EXR_NG_FULL)));
            } else if (dsdId.equals("ECB_EXR_RG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, SrmDtoMocks.createContentInput(new File(SDMXResources.DSD_ECB_EXR_RG_FULL)));
            } else if (dsdId.equals("ECB_EXR_SG")) {
                srmCoreServiceFacade.importSDMXStructureMsg(serviceContext, SrmDtoMocks.createContentInput(new File(SDMXResources.DSD_ECB_EXR_SG_FULL)));
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
        List<ConditionalCriteria> dsdConditions = criteriaFor(DataStructureDefinition.class).withProperty(code()).eq(dsdId).build();

        return srmCoreServiceFacade.findDsdByCondition(serviceContext, dsdConditions, PagingParameter.pageAccess(10));
    }

    @Test
    public void testFindOrganisation() throws Exception {
        // TODO Auto-generated method stub

    }
}

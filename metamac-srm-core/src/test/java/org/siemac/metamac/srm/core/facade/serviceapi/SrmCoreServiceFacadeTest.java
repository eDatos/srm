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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.jaxb.CustomJaxb2Marshaller;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.utils.SrmDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
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

// TODO esta clase debería tender a sustituirse por mockear los servicios con Mockito (SrmCoreServiceFacadeCoreMockedTest)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeTest extends SrmBaseTest implements SrmCoreServiceFacadeTestBase {

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

    private static final String    CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";
    private static final String    CONCEPT_SCHEME_1_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(02.000)";
    private static final String    CONCEPT_SCHEME_2_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME02(01.000)";
    private static final String    CONCEPT_SCHEME_3_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(01.000)";
    private static final String    NOT_EXISTS          = "not-exists";

    private final ServiceContext   serviceContext      = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    // -------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------

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

        srmCoreServiceFacade.deleteDsd(getServiceContext(), dataStructureDefinitionDto);

        MetamacCriteriaResult<DataStructureDefinitionDto> result = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), null);
        assertEquals(1, result.getResults().size());
    }

    @Test
    public void testFindDsdByCondition() throws Exception {
        srmCoreServiceFacade.saveDsd(getServiceContext(), SrmDtoMocks.createDdsDTO());

        MetamacCriteriaResult<DataStructureDefinitionDto> result = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), null);
        assertEquals(2, result.getResults().size());
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

    // -------------------------------------------------------------------------------
    // Descriptors
    // -------------------------------------------------------------------------------

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
    public void testRetrieveDsd() throws Exception {
        DataStructureDefinitionDto dataStructureDefinitionDto = SrmCoreServiceFacadeTest.saveDescriptorForDsd(getServiceContext(), srmCoreServiceFacade);

        DataStructureDefinitionDto dataStructureDefinitionDto2 = srmCoreServiceFacade.retrieveDsd(getServiceContext(), dataStructureDefinitionDto.getId(), TypeDozerCopyMode.CREATE);

        assertNotNull(dataStructureDefinitionDto2);
    }

    @Test
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

        MetamacCriteriaResult<DataStructureDefinitionDto> metamacCriteriaResult = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), metamacCriteria);

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
            metamacCriteriaResult = srmCoreServiceFacade.findDsdByCondition(getServiceContext(), metamacCriteria);
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
        dataStructureDefinitionExtendDto.getGrouping().addAll(srmCoreServiceFacade.findDescriptorsForDsd(serviceContext, dataStructureDefinitionDto.getId()));

        structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);

        System.out.println("Output: " + srmCoreServiceFacade.exportSDMXStructureMsg(serviceContext, structureMsgDto));
    }

    @Test
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

    @Test
    public void testFindOrganisation() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testRetrieveDsdByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    // -------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------

    @Test
    public void testRetrieveConceptSchemeByUrn() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V1);
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
        assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
        assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());

        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeMetamacDto.getType());
        assertEquals("op1", conceptSchemeMetamacDto.getRelatedOperation().getCode());
        assertEquals("http://op1", conceptSchemeMetamacDto.getRelatedOperation().getUri());
        assertEquals("urn:op1", conceptSchemeMetamacDto.getRelatedOperation().getUrn());
        assertEquals("http://app/operations", conceptSchemeMetamacDto.getRelatedOperation().getManagementAppUrl());
        assertEquals(1, conceptSchemeMetamacDto.getRelatedOperation().getVersion().longValue());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnWithoutRelatedOperation() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1);
        assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
        assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());

        assertEquals(ConceptSchemeTypeEnum.ROLE, conceptSchemeMetamacDto.getType());
        assertNull(conceptSchemeMetamacDto.getRelatedOperation());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.URN, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveConceptSchemeErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setCode("code-" + MetamacMocks.mockString(10));
        conceptSchemeDto.setName(MetamacMocks.mockInternationalString());
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintiner", TypeExternalArtefactsEnum.AGENCY));

        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), conceptSchemeDto);
        ConceptSchemeMetamacDto dto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeMetamacCreated.getUrn());

        assertNotNull(conceptSchemeMetamacCreated);
        assertEquals(conceptSchemeDto.getCode(), conceptSchemeMetamacCreated.getCode());
        // URI
        // assert
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());
        MetamacAsserts.assertEqualsInternationalStringDto(conceptSchemeDto.getName(), conceptSchemeMetamacCreated.getName());
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getProcStatus());

    }

    @Override
    public void testUpdateConceptScheme() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void testDeleteConceptScheme() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    public void testFindConceptSchemesByCondition() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);

        MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());

        String firstUrn = result.getResults().get(0).getUrn();
        for (ConceptSchemeDto conceptSchemeDto : result.getResults()) {
            assertTrue((conceptSchemeDto.getUrn().compareTo(firstUrn) == 0) || (conceptSchemeDto.getUrn().compareTo(firstUrn) > 0));
        }

        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(0);
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
            assertEquals("02.000", conceptSchemeMetamacDto.getReplacedBy());
            assertNull(conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeMetamacDto.getType());
            assertEquals("urn:op1", conceptSchemeMetamacDto.getRelatedOperation().getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(1);
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("02.000", conceptSchemeMetamacDto.getVersionLogic());
            assertNull(conceptSchemeMetamacDto.getReplacedBy());
            assertEquals("01.000", conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.GLOSSARY, conceptSchemeMetamacDto.getType());
            assertNull(conceptSchemeMetamacDto.getRelatedOperation());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(2);
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME02", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
            assertNull(conceptSchemeMetamacDto.getReplacedBy());
            assertNull(conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.ROLE, conceptSchemeMetamacDto.getType());
            assertNull(conceptSchemeMetamacDto.getRelatedOperation());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(3);
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME03", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
            assertNull(conceptSchemeMetamacDto.getReplacedBy());
            assertNull(conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.ROLE, conceptSchemeMetamacDto.getType());
            assertNull(conceptSchemeMetamacDto.getRelatedOperation());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
    }

    @Test
    public void testFindConceptSchemesByProcStatus() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);

        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.DRAFT, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : result.getResults()) {
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : result.getResults()) {
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
        }
    }

    @Test
    public void testFindConceptSchemesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        // Pagination
        int maxResultSize = 2;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CONCEPT_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(0).getUrn());
        }
    }

}

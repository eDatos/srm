package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.jaxb.CustomJaxb2Marshaller;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.utils.SrmDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.message.Structure;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeDsdTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // @Autowired
    // private DsdsMetamacService srmCoreServiceFacade;

    @Autowired
    @Qualifier("jaxb2MarshallerWithValidation")
    private Jaxb2Marshaller        marshallerWithValidation;

    @Autowired
    @Qualifier("jaxb2MarshallerWithoutValidation")
    private CustomJaxb2Marshaller  marshallerWithoutValidation;

    // @Autowired
    // @Qualifier("mapperCoreCopyAllMetadataMode")
    // private DozerBeanMapper mapper;

    // -------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------

    // @DataStructureDefinitionMetamacDto createDataStructureDefinition(@DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto updateDataStructureDefinition(@DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) throws MetamacException;
    //
    // deleteDataStructureDefinition(String urn) throws MetamacException;
    //
    // MetamacCriteriaResult<@DataStructureDefinitionMetamacDto> findDsdByCondition(MetamacCriteria criteria) throws MetamacException;
    //
    @Test
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> result = null;

        srmCoreServiceFacade.createDataStructureDefinition(getServiceContextAdministrador(), SrmDtoMocks.createDataStructureDefinitionMetamacDtoMock());

        // By Name
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.NAME.name(), "NAME ES DSD", OperationType.EQ));

        result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(1, result.getResults().size());

        metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));
        result = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContextAdministrador(), metamacCriteria);
        assertEquals(0, result.getResults().size());
    }
    // @DataStructureDefinitionMetamacDto retrieveDsd(String urn, @TypeDozerCopyMode typeDozerCopyMode) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto retrieveDsdByUrn(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto createDsdVersion(String urn, boolean minorVersion) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto sendDataStructureDefinitionToProductionValidation(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto sendDataStructureDefinitionDtoToDiffusionValidation(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto rejectDataStructureDefinitionDtoProductionValidation(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto rejectDataStructureDefinitionDtoDiffusionValidation(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto publishInternallyDataStructureDefinitionDto(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto publishExternallyDataStructureDefinitionDto(String urn) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto versioningDataStructureDefinitionDto(String urnToCopy, @VersionTypeEnum versionType) throws MetamacException;
    //
    // @DataStructureDefinitionMetamacDto endDataStructureDefinitionDtoValidity(String urn) throws MetamacException;
    //
    // List<@DescriptorDto> findDescriptorForDsd(String urnDsd, @TypeComponentList typeComponentList) throws MetamacException;
    //
    // List<@DescriptorDto> findDescriptorsForDsd(String urnDsd) throws MetamacException;
    //
    // @DescriptorDto saveDescriptorForDsd(String urnDsd, @DescriptorDto descriptorDto) throws MetamacException;
    //
    // deleteDescriptorForDsd(String urnDsd, @DescriptorDto descriptorDto)throws MetamacException;
    //
    // @ComponentDto saveComponentForDsd(String urnDsd, @ComponentDto componentDto, @TypeComponentList typeComponentList) throws MetamacException;
    //
    // deleteComponentForDsd(String urnDsd, @ComponentDto componentDto, @TypeComponentList typeComponentList) throws MetamacException;
    //
    // importSDMXStructureMsg(@ContentInputDto contentDto) throws MetamacException;
    //
    // String exportSDMXStructureMsg(@StructureMsgDto structureMsgDto) throws MetamacException;

    @Test
    public void testSaveDsd() throws Exception {

        // Create ****************
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createDataStructureDefinition(getServiceContextAdministrador(),
                SrmDtoMocks.createDataStructureDefinitionMetamacDtoMock());

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

        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.updateDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionMetamacDto);

        assertTrue(dataStructureDefinitionMetamacDto.getName().getTexts().iterator().next().getLabel().contains("CHANGED"));
        assertNull(dataStructureDefinitionMetamacDto.getDescription());
    }

    // @Ignore // TODO tests import: pendiente reestructuración del servicio de importación
    // @Test
    // public void testImportSDMXStructureMsg() throws Exception {
    // File file = null;
    // DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
    //
    // // ECB **************************
    // file = new File(SdmxResources.DSD_ECB_EXR_NG_FULL);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_NG_FULL_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_ECB_EXR_NG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_ECB_EXR_SG_FULL);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_SG_FULL_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_ECB_EXR_SG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_ECB_EXR_RG_FULL);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_RG_FULL_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_ECB_EXR_RG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // // INE **************************
    // file = new File(SdmxResources.DSD_INE_DPOP);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_DPOP_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_INE_DPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_INE_EPOP);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_EPOP_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_INE_EPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_INE_IDB);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IDB_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_INE_IDB_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_INE_IPC);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPC_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_INE_IPC_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_INE_IPCA);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPCA_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_INE_IPCA_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // file = new File(SdmxResources.DSD_INE_MNP);
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
    // dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_MNP_URN);
    // assertNotNull(dataStructureDefinitionMetamacDto.getId());
    // assertEquals(SdmxResources.DSD_INE_MNP_URN, dataStructureDefinitionMetamacDto.getUrn());
    //
    // }

    @Test
    // TODO tests export: pendiente reestructuración del servicio de exportación
    @Ignore
    public void testExportSDMXStructureMsg() throws Exception {

        // DSD: ECB_EXR_RG
        // exportStructureMessage("ECB_EXR_NG");

        // DSD: ECB_EXR_RG
        // exportStructureMessage("ECB_EXR_RG");

        // DSD: ECB_EXR_RG
        // exportStructureMessage("ECB_EXR_SG");
    }

    // private void exportStructureMessage(String code) throws MetamacException, FileNotFoundException {
    // StructureMsgDto structureMsgDto = new StructureMsgDto();
    //
    // // DSD
    // MetamacCriteria metamacCriteria = new MetamacCriteria();
    // MetamacCriteriaDisjunctionRestriction disjunction = new MetamacCriteriaDisjunctionRestriction();
    // disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(DataStructureDefinitionCriteriaPropertyEnum.CODE.name(), code, OperationType.EQ));
    // metamacCriteria.setRestriction(disjunction);
    //
    // MetamacCriteriaResult<DataStructureDefinitionMetamacDto> metamacCriteriaResult = srmCoreServiceFacade
    // .findDataStructureDefinitionsByCondition(getServiceContextAdministrador(), metamacCriteria);
    //
    // if (metamacCriteriaResult.getResults().isEmpty()) {
    // if (code.equals("ECB_EXR_NG")) {
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_NG_FULL)));
    // } else if (code.equals("ECB_EXR_RG")) {
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_RG_FULL)));
    // } else if (code.equals("ECB_EXR_SG")) {
    // srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_SG_FULL)));
    // } else {
    // fail("Error in test");
    // }
    // metamacCriteriaResult = srmCoreServiceFacade.findDataStructureDefinitionsByCondition(getServiceContextAdministrador(), metamacCriteria);
    // }
    //
    // DataStructureDefinitionDto dataStructureDefinitionDto = metamacCriteriaResult.getResults().iterator().next();
    // // Create DSD extend (with descriptors)
    // DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = mapper.map(metamacCriteriaResult.getResults().iterator().next(), DataStructureDefinitionExtendDto.class);
    // // Name and descriptions
    // dataStructureDefinitionExtendDto.setName(dataStructureDefinitionDto.getName());
    // dataStructureDefinitionExtendDto.setDescription(dataStructureDefinitionDto.getDescription());
    // // Annotations
    // dataStructureDefinitionExtendDto.getAnnotations().addAll(dataStructureDefinitionDto.getAnnotations());
    //
    // // Add descriptors
    // dataStructureDefinitionExtendDto.getGrouping().addAll(srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionDto.getUrn()));
    //
    // structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);
    //
    // System.out.println("Output: " + srmCoreServiceFacade.exportSDMXStructureMsg(getServiceContextAdministrador(), structureMsgDto));
    // }

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

    @Test
    public void testFindConceptsCanBeDsdPrimaryMeasureByCondition() throws Exception {

        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), "concept 5-1-1", OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_4_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition() throws Exception {

        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_4_V1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "concept-scheme-4-1", OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_4_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme urn
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), CONCEPT_SCHEME_5_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_5_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptsCanBeDsdTimeDimensionByCondition() throws Exception {

        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), "concept 3-1-1", OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_3_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition() throws Exception {

        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "concept-scheme-3-1", OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme urn
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), CONCEPT_SCHEME_2_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptsCanBeDsdMeasureDimensionByCondition() throws Exception {

        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_2_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition() throws Exception {

        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(),
                    metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptsCanBeDsdDimensionByCondition() throws Exception {
        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), "concept 3-1-1", OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_3_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdDimensionByCondition() throws Exception {
        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, result.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "concept-scheme-3-1", OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme urn
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), CONCEPT_SCHEME_2_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), metamacCriteria,
                    dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptsCanBeDsdRoleByCondition() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by concept scheme
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_3_V1, OperationType.EQ));
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeRoleRoleByCondition() throws Exception {
        String dsdUrn = DSD_1_V2;

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_6_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForDsdDimension() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdDimension(getServiceContextAdministrador(), metamacCriteria,
                    conceptUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CODELIST_7_V1, result.getResults().get(i++).getUrn());
            assertEquals(CODELIST_9_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            // Concept has Variable 2
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2_1;
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdDimension(getServiceContextAdministrador(), metamacCriteria,
                    conceptUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CODELIST_8_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimension() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimension(getServiceContextAdministrador(),
                    metamacCriteria);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_7_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdAttributeByCondition() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        String dsdUrn = DSD_1_V2;

        // Find
        MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

        // Validate
        assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_3_V1, result.getResults().get(i++).getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1, result.getResults().get(i++).getUrn());
        assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
    }

    @Test
    public void testFindConceptsCanBeDsdAttributeByCondition() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        String dsdUrn = DSD_1_V2;

        // Find
        MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdAttributeByCondition(getServiceContextAdministrador(), metamacCriteria, dsdUrn);

        // Validate
        assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, result.getResults().get(i++).getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getResults().get(i++).getUrn());
        assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
    }

    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForDsdAttribute() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdAttribute(getServiceContextAdministrador(), metamacCriteria,
                    conceptUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CODELIST_7_V1, result.getResults().get(i++).getUrn());
            assertEquals(CODELIST_9_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            // Concept has Variable 2
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2_1;
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdAttribute(getServiceContextAdministrador(), metamacCriteria,
                    conceptUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CODELIST_8_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
}

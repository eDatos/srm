package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.utils.SrmDtoMocks;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.v2_1.domain.dto.importation.ContentInputDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationTest extends SrmBaseTest {

    // AGENCIES SCHEMES
    private final String                  AGENCYSCHEME_SDMX01_AGENCIES_V1                    = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX01:AGENCIES(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_DATA_CONSUMERS_V1              = "urn:sdmx:org.sdmx.infomodel.base.DataConsumerScheme=SDMX01:DATA_CONSUMERS(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_DATA_PROVIDERS_V1              = "urn:sdmx:org.sdmx.infomodel.base.DataProviderScheme=SDMX01:DATA_PROVIDERS(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATION_UNIT_SCHEME_01(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V2 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATION_UNIT_SCHEME_01(2.0)";

    // CODELISTS
    private final String                  CODELIST_SDMX01_CL_DECIMALS_V1                     = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_DECIMALS(1.0)";
    private final String                  CODELIST_SDMX01_CL_DECIMALS_V2                     = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_DECIMALS(2.0)";
    private final String                  CODELIST_SDMX01_CL_FREQ_V1                         = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_FREQ(1.0)";
    private final String                  CODELIST_SDMX01_CL_CONF_STATUS_V1                  = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_CONF_STATUS(1.0)";
    private final String                  CODELIST_SDMX01_CL_OBS_STATUS_V1                   = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_OBS_STATUS(1.0)";
    private final String                  CODELIST_SDMX01_CL_UNIT_MULT_V1                    = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_UNIT_MULT(1.0)";

    // CONCEPTS SCHEMES
    private final String                  CONCEPTSCHEME_SDMX01_CROSS_DOMAIN_CONCEPTS_V1      = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CROSS_DOMAIN_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V2              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_CONCEPTS(2.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_MEASURES_V1              = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_MEASURES(1.0)";

    // DSDs
    private final String                  DSD_SDMX01_V1                                      = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DEMOGRAPHY(1.0)";
    private final String                  DSD_SDMX01_V2                                      = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DEMOGRAPHY(2.0)";

    // Categories
    private final String                  CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS               = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:SDW_ECONOMIC_CONCEPTS(1.0)";

    @Autowired
    protected SrmCoreServiceFacade        srmCoreServiceFacade;

    @Autowired
    protected CodesMetamacService         codesMetamacService;

    @Autowired
    protected OrganisationsMetamacService organisationsMetamacService;

    @Autowired
    protected ConceptsMetamacService      conceptsMetamacService;

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }

    @Test
    @Ignore
    public void testImport_EXAMPLE_ORGANISATIONS() throws Exception {
        File file = new File(SdmxResources.EXAMPLE_ORGANISATIONS);

        ContentInputDto contentInputDto = new ContentInputDto();
        contentInputDto.setInput(new FileInputStream(file));
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), contentInputDto);

        OrganisationSchemeVersionMetamac organisationSchemeVersion = null;

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_AGENCIES_V1);
        assertEquals(1, organisationSchemeVersion.getItems().size());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_DATA_CONSUMERS_V1);
        assertEquals(1, organisationSchemeVersion.getItems().size());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_DATA_PROVIDERS_V1);
        assertEquals(1, organisationSchemeVersion.getItems().size());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V1);
        assertEquals(2, organisationSchemeVersion.getItems().size());
        assertFalse(organisationSchemeVersion.getMaintainableArtefact().getIsLastVersion());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V2);
        assertEquals(1, organisationSchemeVersion.getItems().size());
        assertTrue(organisationSchemeVersion.getMaintainableArtefact().getIsLastVersion());

        assertEquals(2, organisationSchemeVersion.getItemScheme().getVersions().size());

        // TODO testear las condiciones de importacion de METAMAC, así como la herencia en el versionado
    }

    @Test
    @Ignore
    public void testImport_DEMOGRAPHY_CODELIST() throws Exception {
        File file = new File(SdmxResources.DEMOGRAPHY_CODELIST);

        ContentInputDto contentInputDto = new ContentInputDto();
        contentInputDto.setInput(new FileInputStream(file));

        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), contentInputDto);

        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_DECIMALS_V1);
        assertEquals(10, codelistVersion.getItems().size());
        assertFalse(codelistVersion.getMaintainableArtefact().getIsLastVersion());

        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_DECIMALS_V2);
        assertEquals(11, codelistVersion.getItems().size());
        assertTrue(codelistVersion.getMaintainableArtefact().getIsLastVersion());
        assertEquals(2, codelistVersion.getItemScheme().getVersions().size());

        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_FREQ_V1);
        assertEquals(8, codelistVersion.getItems().size());

        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_CONF_STATUS_V1);
        assertEquals(5, codelistVersion.getItems().size());

        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_OBS_STATUS_V1);
        assertEquals(8, codelistVersion.getItems().size());

        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_UNIT_MULT_V1);
        assertEquals(9, codelistVersion.getItems().size());

        // TODO testear las condiciones de importacion de METAMAC, así como la herencia en el versionado
    }

    @Test
    @Ignore
    public void testImport_DEMOGRAPHY_CONCEPTS() throws Exception {

        File file = new File(SdmxResources.DEMOGRAPHY_CONCEPTS);

        ContentInputDto contentInputDto = new ContentInputDto();
        contentInputDto.setInput(new FileInputStream(file));

        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), contentInputDto);

        ConceptSchemeVersionMetamac conceptSchemeVersion = null;

        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_CROSS_DOMAIN_CONCEPTS_V1);
        assertEquals(12, conceptSchemeVersion.getItems().size());

        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V1);
        assertEquals(3, conceptSchemeVersion.getItems().size());
        assertFalse(conceptSchemeVersion.getMaintainableArtefact().getIsLastVersion());

        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V2);
        assertEquals(4, conceptSchemeVersion.getItems().size());
        assertTrue(conceptSchemeVersion.getMaintainableArtefact().getIsLastVersion());

        assertEquals(2, conceptSchemeVersion.getItemScheme().getVersions().size());

        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_MEASURES_V1);
        assertEquals(14, conceptSchemeVersion.getItems().size());

    }

    @Test
    @Ignore
    public void testImport_DSD_ECB_EXR_NG_FULL() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_ECB_EXR_NG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_NG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_NG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Test
    @Ignore
    public void testImport_DSD_ECB_EXR_SG_FULL() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_ECB_EXR_SG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_SG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_SG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Test
    @Ignore
    public void testImport_DSD_ECB_EXR_RG_FULL() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_ECB_EXR_RG_FULL);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_RG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_RG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Test
    @Ignore
    public void testImport_DSD_INE_DPOP() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_INE_DPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_DPOP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_DPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    // TODO se ignora hasta que se arregle la incidencia del tipo de esquema de concepto
    @Test
    public void testImport_DSD_INE_EPOP() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_INE_EPOP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_EPOP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_EPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    // TODO se ignora hasta que se arregle la incidencia del tipo de esquema de concepto
    @Test
    public void testImport_DSD_INE_IDB() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_INE_IDB);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IDB_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IDB_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    // TODO se ignora hasta que se arregle la incidencia del tipo de esquema de concepto
    @Test
    public void testImport_DSD_INE_IPC() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_INE_IPC);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPC_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IPC_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    // TODO se ignora hasta que se arregle la incidencia del tipo de esquema de concepto
    @Test
    public void testImport_DSD_INE_IPCA() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_INE_IPCA);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPCA_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IPCA_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    // TODO se ignora hasta que se arregle la incidencia del tipo de esquema de concepto
    @Test
    public void testImport_DSD_INE_MNP() throws Exception {
        File file = null;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;

        file = new File(SdmxResources.DSD_INE_MNP);
        srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), SrmDtoMocks.createContentInput(file));
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_MNP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_MNP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

}

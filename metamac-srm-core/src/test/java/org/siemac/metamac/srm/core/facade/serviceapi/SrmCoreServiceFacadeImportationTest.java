package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.srm.core.importation.serviceapi.utils.ImportationsDtoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationTest extends SrmBaseTest {

    private static Logger                 logger                                             = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationTest.class);

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

    @Autowired
    protected PlatformTransactionManager  transactionManager;

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }

    @Test
    @DirtyDatabase
    public void testImport_EXAMPLE_ORGANISATIONS() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.EXAMPLE_ORGANISATIONS)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });

        WaitUntilJobFinished();

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
    @DirtyDatabase
    public void testImport_DEMOGRAPHY_CODELIST() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
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
    @DirtyDatabase
    @Rollback(false)
    public void testImport_CodelistNoPartialAndFinalWithPreviousAsPartialAndDraft() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt1 = new TransactionTemplate(transactionManager);
        tt1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt1.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_PARTIAL_A)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();

        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
        tt2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt2.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_NO_PARTIAL)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();

        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_FREQ_V1);
        assertEquals(8, codelistVersion.getItems().size());
    }

    @Test
    @DirtyDatabase
    @Rollback(false)
    public void testImport_CodelistPartialAndFinalWithPreviousAsPartialAndDraft() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt1 = new TransactionTemplate(transactionManager);
        tt1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt1.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_PARTIAL_A)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();

        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
        tt2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt2.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_PARTIAL_B)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();

        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_FREQ_V1);
        assertEquals(7, codelistVersion.getItems().size());
    }

    @Test
    @DirtyDatabase
    public void testImport_DEMOGRAPHY_CONCEPTS() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CONCEPTS)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
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
    @DirtyDatabase
    public void testImport_DSD_ECB_EXR_NG_FULL() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_NG_FULL)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_NG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_NG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Test
    @DirtyDatabase
    public void testImport_DSD_ECB_EXR_SG_FULL() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_SG_FULL)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_SG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_SG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Test
    @DirtyDatabase
    public void testImport_DSD_ECB_EXR_RG_FULL() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_RG_FULL)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_RG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_RG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_DPOP() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_DPOP)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_DPOP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_DPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_EPOP() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_EPOP)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_EPOP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_EPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_IDB() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_IDB)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IDB_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IDB_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_IPC() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_IPC)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPC_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IPC_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_IPCA() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_IPCA)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPCA_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IPCA_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_MNP() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), ImportationsDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_MNP)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        WaitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_MNP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_MNP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    private void WaitUntilJobFinished() throws InterruptedException, SchedulerException {
        // Wait until the job is finished
        Thread.sleep(5 * 1000l);
        Scheduler sched = SchedulerRepository.getInstance().lookup("SdmxSrmScheduler"); // get a reference to a scheduler
        while (sched.getCurrentlyExecutingJobs().size() != 0) {
            Thread.sleep(5 * 1000l);
        }
    }

}

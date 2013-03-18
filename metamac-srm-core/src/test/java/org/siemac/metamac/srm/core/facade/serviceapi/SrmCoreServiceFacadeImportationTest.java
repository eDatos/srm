package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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

    private static Logger                 logger                                        = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationTest.class);

    // CODELISTS
    private final String                  CODELIST_SDMX01_CL_DECIMALS_V1                = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_DECIMALS(1.0)";
    private final String                  CODELIST_SDMX01_CL_DECIMALS_V2                = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_DECIMALS(2.0)";
    private final String                  CODELIST_SDMX01_CL_FREQ_V1                    = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_FREQ(1.0)";
    private final String                  CODELIST_SDMX01_CL_CONF_STATUS_V1             = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_CONF_STATUS(1.0)";
    private final String                  CODELIST_SDMX01_CL_OBS_STATUS_V1              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_OBS_STATUS(1.0)";
    private final String                  CODELIST_SDMX01_CL_UNIT_MULT_V1               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_UNIT_MULT(1.0)";

    // CONCEPTS SCHEMES
    private final String                  CONCEPTSCHEME_SDMX01_CROSS_DOMAIN_CONCEPTS_V1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CROSS_DOMAIN_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V1         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V2         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_CONCEPTS(2.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_MEASURES_V1         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_MEASURES(1.0)";

    // DSDs
    private final String                  DSD_SDMX01_V1                                 = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DEMOGRAPHY(1.0)";
    private final String                  DSD_SDMX01_V2                                 = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DEMOGRAPHY(2.0)";

    // Categories
    private final String                  CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS          = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:SDW_ECONOMIC_CONCEPTS(1.0)";

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

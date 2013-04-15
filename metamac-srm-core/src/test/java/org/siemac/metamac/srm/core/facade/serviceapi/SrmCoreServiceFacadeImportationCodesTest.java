package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
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
import com.arte.statistic.sdmx.srm.core.task.serviceapi.utils.TaskDtoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationCodesTest extends SrmBaseTest {

    private static Logger                 logger                            = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationCodesTest.class);

    // CODELISTS
    private final String                  CODELIST_SDMX01_CL_DECIMALS_V1    = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_DECIMALS(1.0)";
    // private final String CODELIST_SDMX01_CL_DECIMALS_V2 = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_DECIMALS(2.0)";
    private final String                  CODELIST_SDMX01_CL_FREQ_V1        = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_FREQ(1.0)";
    private final String                  CODELIST_SDMX01_CL_CONF_STATUS_V1 = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_CONF_STATUS(1.0)";
    private final String                  CODELIST_SDMX01_CL_OBS_STATUS_V1  = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_OBS_STATUS(1.0)";
    private final String                  CODELIST_SDMX01_CL_UNIT_MULT_V1   = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CL_UNIT_MULT(1.0)";
    private final String                  CODELIST_SDMX01_CL_CODELIST03_V2  = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(2.0)";

    // Codes
    protected static final String         CODELIST_3_V2_CODE_1              = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE01";
    protected static final String         CODELIST_3_V2_CODE_1a             = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE01a";
    protected static final String         CODELIST_3_V2_CODE_2              = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE02";
    protected static final String         CODELIST_3_V2_CODE_2_1            = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE0201";
    protected static final String         CODELIST_3_V2_CODE_2_1_1          = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE020101";
    protected static final String         CODELIST_3_V2_CODE_2_2            = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE0202";
    protected static final String         CODELIST_3_V2_CODE_3              = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE03";

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
        return "dbunit/SrmCodesTest.xml";
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
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TaskDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_DECIMALS_V1);
        assertEquals(10, codelistVersion.getItems().size());
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
    public void testImportOrders_CL_CODELIST03() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TaskDtoMocks.createContentInput(new File(SdmxResources.CL_CODELIST03_V2)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_CODELIST03_V2);
        assertEquals(12, codelistVersion.getItems().size());
        assertEquals(2, codelistVersion.getOrderVisualisations().size());
        {
            CodelistOrderVisualisation visualisation = codesMetamacService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);
            List<CodeMetamacVisualisationResult> codes = codesMetamacService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistVersion.getMaintainableArtefact().getUrn(), "es",
                    visualisation.getNameableArtefact().getUrn(), null);
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_1a).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_3).getOrder());

        }
        {
            CodelistOrderVisualisation visualisation = codesMetamacService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesMetamacService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistVersion.getMaintainableArtefact().getUrn(), "es",
                    visualisation.getNameableArtefact().getUrn(), null);
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2_1_1).getOrder());
        }
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesMetamacService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_CODELIST03_V2, locale, null,
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);

            // Validate
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_3_V2_CODE_2_1_1).getOpenness());

            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE0204").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE0203").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE0205").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE020101").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE020100").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE020102").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE01a").getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(2.0).CODE0301").getOpenness());
        }
    }

    @Test
    @DirtyDatabase
    @Ignore
    public void testImport_CodelistNoPartialAndFinalWithPreviousAsPartialAndDraft() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt1 = new TransactionTemplate(transactionManager);
        tt1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt1.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(),
                            TaskDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_PARTIAL_A)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();

        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
        tt2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt2.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(),
                            TaskDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_NO_PARTIAL)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();

        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_FREQ_V1);
        assertEquals(8, codelistVersion.getItems().size());
    }

    @Test
    @DirtyDatabase
    @Ignore
    public void testImport_CodelistPartialAndFinalWithPreviousAsPartialAndDraft() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt1 = new TransactionTemplate(transactionManager);
        tt1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt1.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(),
                            TaskDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_PARTIAL_A)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();

        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
        tt2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt2.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(),
                            TaskDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CODELIST_FINAL_PARTIAL_B)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();

        CodelistVersionMetamac codelistVersion = null;
        codelistVersion = codesMetamacService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX01_CL_FREQ_V1);
        assertEquals(7, codelistVersion.getItems().size());
    }

}

package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
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
public class SrmCoreServiceFacadeImportationConceptTest extends SrmBaseTest {

    private static Logger                 logger                                        = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationConceptTest.class);

    // CONCEPTS SCHEMES
    private final String                  CONCEPTSCHEME_SDMX01_CROSS_DOMAIN_CONCEPTS_V1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CROSS_DOMAIN_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V1         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_SDMX01_DEMO_MEASURES_V1         = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:DEMO_MEASURES(1.0)";

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
        return "dbunit/SrmConceptsTest.xml";
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
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TaskDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CONCEPTS)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        ConceptSchemeVersionMetamac conceptSchemeVersion = null;
        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_CROSS_DOMAIN_CONCEPTS_V1);
        assertEquals(12, conceptSchemeVersion.getItems().size());
        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V1);
        assertEquals(3, conceptSchemeVersion.getItems().size());
        conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_MEASURES_V1);
        assertEquals(14, conceptSchemeVersion.getItems().size());
    }

    @Test
    @DirtyDatabase
    public void testImport_UPDATE_EXISTING_CS() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TaskDtoMocks.createContentInput(new File(SdmxResources.CS_DEMO_1_0_FINAL_NO_PARTIAL)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        // ConceptSchemeVersionMetamac conceptSchemeVersion = null;
        // conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_CROSS_DOMAIN_CONCEPTS_V1);
        // assertEquals(12, conceptSchemeVersion.getItems().size());
        // conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V1);
        // assertEquals(3, conceptSchemeVersion.getItems().size());
        // assertFalse(conceptSchemeVersion.getMaintainableArtefact().getIsLastVersion());
        // conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_CONCEPTS_V2);
        // assertEquals(4, conceptSchemeVersion.getItems().size());
        // assertTrue(conceptSchemeVersion.getMaintainableArtefact().getIsLastVersion());
        // assertEquals(2, conceptSchemeVersion.getItemScheme().getVersions().size());
        // conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX01_DEMO_MEASURES_V1);
        // assertEquals(14, conceptSchemeVersion.getItems().size());
    }

}

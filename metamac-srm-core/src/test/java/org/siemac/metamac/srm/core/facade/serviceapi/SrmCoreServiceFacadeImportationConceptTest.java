package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
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

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;
import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.srm.core.task.serviceapi.utils.TasksDtoMocks;

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
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DEMOGRAPHY_CONCEPTS)));
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
    public void testImport_UPDATE_CONCEPT_SCHEME_02() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.CS_DEMO_1_0_FINAL_NO_PARTIAL)));
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

    @Test
    @DirtyDatabase
    public void testImportUpdateExistingConceptSchemeCheckQuantity() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(),
                            TasksDtoMocks.createContentInput(new File("src/test/resources/sdmx/2_1/concepts/CS_DEMO_2_0.xml")));
                } catch (Exception e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();

        String conceptSchemeUrnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX02:CONCEPTSCHEME15(02.000)";
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(02.000).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(02.000).CONCEPT02";
        String urnExpectedConcept3 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(02.000).CONCEPT03";

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsMetamacService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrnExpected);
        assertEquals(3, conceptSchemeVersion.getItems().size());
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), urnExpectedConcept1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), urnExpectedConcept2);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), urnExpectedConcept3);

        // Validate quantity
        {
            // Concept 1
            {
                ConceptMetamac concept = conceptsMetamacService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept1);
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.CHANGE_RATE, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.END, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(2), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(3), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(10), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(100), quantity.getMinimum());
                assertEquals(Integer.valueOf(200), quantity.getMaximum());
                assertEquals(urnExpectedConcept2, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertEquals(Boolean.TRUE, quantity.getIsPercentage());
                ConceptsAsserts.assertEqualsInternationalString(quantity.getPercentageOf(), "es", "porcentaje quantity c1", "en", "percentage quantity c1");
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, quantity.getBaseQuantity().getNameableArtefact().getUrn());
            }

            // Concept 2
            {
                ConceptMetamac concept = conceptsMetamacService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept2);
                assertNull(concept.getQuantity());
            }
            // Concept 3
            {
                ConceptMetamac concept = conceptsMetamacService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept3);
                assertNotNull(concept.getQuantity());
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.FRACTION, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.START, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(3), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(2), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(100), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(200), quantity.getMinimum());
                assertEquals(Integer.valueOf(350), quantity.getMaximum());
                assertEquals(urnExpectedConcept2, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertNull(quantity.getIsPercentage());
                assertNull(quantity.getPercentageOf());
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertNull(quantity.getBaseQuantity());
            }
        }
    }

}

package org.siemac.metamac.srm.core.category.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CategoriesMetamacCategorisationServiceTest extends SrmBaseTest {

    @Autowired
    private CategoriesMetamacService categoriesService;

    @Autowired
    private ConceptsMetamacService   conceptsService;

    // Categorisations
    private final String             CATEGORISATION_4 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)";
    private final String             CATEGORISATION_5 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat5(01.000)";

    @Test
    public void testCreateCategorisationAutomaticallyPublished() throws Exception {

        // Create
        String categoryUrn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String artefactCategorisedUrn = CONCEPT_SCHEME_1_V1;
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), artefactCategorisedUrn).getLifeCycleMetadata().getProcStatus());
        String maintainerUrn = AGENCY_ROOT_1_V1; // maintainer with other categorisations
        Categorisation categorisationCreated = categoriesService.createCategorisation(getServiceContextAdministrador(), categoryUrn, artefactCategorisedUrn, maintainerUrn);

        // Validation
        String urn = categorisationCreated.getMaintainableArtefact().getUrn();
        Categorisation categorisationRetrieved = categoriesService.retrieveCategorisationByUrn(getServiceContextAdministrador(), urn);

        // Check metadata filled by service
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)", categorisationRetrieved.getMaintainableArtefact().getUrn());
        assertTrue(DateUtils.isSameDay(new Date(), categorisationRetrieved.getMaintainableArtefact().getValidFrom().toDate()));
        assertNull(categorisationRetrieved.getMaintainableArtefact().getValidTo());
        assertTrue(categorisationRetrieved.getMaintainableArtefact().getFinalLogic());
        assertTrue(categorisationRetrieved.getMaintainableArtefact().getLatestFinal());
        assertTrue(categorisationRetrieved.getMaintainableArtefact().getPublicLogic());
        assertTrue(categorisationRetrieved.getMaintainableArtefact().getLatestPublic());
        assertTrue(categorisationRetrieved.getMaintainableArtefact().getFinalLogicClient());
    }

    @Test
    public void testCreateCategorisationNotAutomaticallyPublished() throws Exception {

        // Create
        String categoryUrn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String artefactCategorisedUrn = CONCEPT_SCHEME_2_V1;
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), artefactCategorisedUrn).getLifeCycleMetadata().getProcStatus());
        String maintainerUrn = AGENCY_ROOT_1_V1; // maintainer with other categorisations
        Categorisation categorisationCreated = categoriesService.createCategorisation(getServiceContextAdministrador(), categoryUrn, artefactCategorisedUrn, maintainerUrn);

        // Validation
        String urn = categorisationCreated.getMaintainableArtefact().getUrn();
        Categorisation categorisationRetrieved = categoriesService.retrieveCategorisationByUrn(getServiceContextAdministrador(), urn);

        // Check metadata filled by service (not published)
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)", categorisationRetrieved.getMaintainableArtefact().getUrn());
        assertNull(categorisationRetrieved.getMaintainableArtefact().getValidFrom());
        assertNull(categorisationRetrieved.getMaintainableArtefact().getValidTo());
        assertFalse(categorisationRetrieved.getMaintainableArtefact().getFinalLogic());
        assertFalse(categorisationRetrieved.getMaintainableArtefact().getLatestFinal());
        assertFalse(categorisationRetrieved.getMaintainableArtefact().getPublicLogic());
        assertFalse(categorisationRetrieved.getMaintainableArtefact().getLatestPublic());
        assertFalse(categorisationRetrieved.getMaintainableArtefact().getFinalLogicClient());
    }

    @Test
    public void testCreateCategorisationCategoryInternallyPublished() throws Exception {
        String categoryUrn = CATEGORY_SCHEME_5_V1_CATEGORY_1;
        String artefactCategorisedUrn = CONCEPT_SCHEME_1_V1;
        String maintainerUrn = AGENCY_ROOT_1_V1;
        categoriesService.createCategorisation(getServiceContextAdministrador(), categoryUrn, artefactCategorisedUrn, maintainerUrn);
    }

    @Test
    public void testCreateCategorisationErrorCategoryDraft() throws Exception {

        // Create
        String categoryUrn = CATEGORY_SCHEME_8_V1_CATEGORY_1;
        String artefactCategorisedUrn = CONCEPT_SCHEME_1_V1;
        String maintainerUrn = AGENCY_ROOT_1_V1;
        try {
            categoriesService.createCategorisation(getServiceContextAdministrador(), categoryUrn, artefactCategorisedUrn, maintainerUrn);
            fail("not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CATEGORY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishArtefactPublishCategorisations() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        // Categorisations are not published
        {
            List<Categorisation> categorisations = categoriesService.retrieveCategorisationsByArtefact(getServiceContextAdministrador(), urn);
            assertEquals(2, categorisations.size());
            int i = 0;
            {
                Categorisation categorisation = categorisations.get(i);
                assertEquals(CATEGORISATION_4, categorisation.getMaintainableArtefact().getUrn());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
                assertNull(categorisation.getMaintainableArtefact().getValidFrom());
                i++;
            }
            {
                Categorisation categorisation = categorisations.get(i);
                assertEquals(CATEGORISATION_5, categorisation.getMaintainableArtefact().getUrn());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
                assertNull(categorisation.getMaintainableArtefact().getValidFrom());
                i++;
            }
            assertEquals(categorisations.size(), i);
        }

        // PUBLISH INTERNALLY artefact to check categorisations are "final" now
        conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);

        // Check categorisations are published
        {
            List<Categorisation> categorisations = categoriesService.retrieveCategorisationsByArtefact(getServiceContextAdministrador(), urn);
            assertEquals(2, categorisations.size());
            int i = 0;
            {
                Categorisation categorisation = categorisations.get(i);
                assertEquals(CATEGORISATION_4, categorisation.getMaintainableArtefact().getUrn());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
                assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertNull(categorisation.getMaintainableArtefact().getValidFrom());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
                i++;
            }
            {
                Categorisation categorisation = categorisations.get(i);
                assertEquals(CATEGORISATION_5, categorisation.getMaintainableArtefact().getUrn());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
                assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertNull(categorisation.getMaintainableArtefact().getValidFrom());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
                i++;
            }
            assertEquals(categorisations.size(), i);
        }

        // PUBLISH EXTERNALLY artefact to check categorisations are "valid" now and make public
        conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);

        // Check categorisations are published
        {
            List<Categorisation> categorisations = categoriesService.retrieveCategorisationsByArtefact(getServiceContextAdministrador(), urn);
            assertEquals(2, categorisations.size());
            int i = 0;
            {
                Categorisation categorisation = categorisations.get(i);
                assertEquals(CATEGORISATION_4, categorisation.getMaintainableArtefact().getUrn());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
                assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertTrue(DateUtils.isSameDay(new Date(), categorisation.getMaintainableArtefact().getValidFrom().toDate()));
                assertTrue(categorisation.getMaintainableArtefact().getPublicLogic());
                assertTrue(categorisation.getMaintainableArtefact().getLatestPublic());
                i++;
            }
            {
                Categorisation categorisation = categorisations.get(i);
                assertEquals(CATEGORISATION_5, categorisation.getMaintainableArtefact().getUrn());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
                assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
                assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertTrue(DateUtils.isSameDay(new Date(), categorisation.getMaintainableArtefact().getValidFrom().toDate()));
                assertTrue(categorisation.getMaintainableArtefact().getPublicLogic());
                assertTrue(categorisation.getMaintainableArtefact().getLatestPublic());
                i++;
            }
            assertEquals(categorisations.size(), i);
        }
    }

    @Test
    public void testCheckCategorisationsWithRelatedResourcesExternallyPublished() throws Exception {
        String artefactCategorisedUrn = CATEGORY_SCHEME_8_V1;
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        categoriesService.checkCategorisationsWithRelatedResourcesExternallyPublished(getServiceContextAdministrador(), artefactCategorisedUrn, exceptionItemsByUrn);

        // Validate
        assertEquals(1, exceptionItemsByUrn.size());
        assertListContainsExceptionItemOneParameter(exceptionItemsByUrn.values(), ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_5_V1_CATEGORY_1);
    }

    // In SDMX module
    public void testRetrieveCategorisationByUrn() throws Exception {
    }

    public void testDeleteCategorisation() throws Exception {
    }

    public void testRetrieveCategorisationsByArtefact() throws Exception {
    }

    public void testFindCategorisationsByCondition() throws Exception {
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategorisationsTest.xml";
    }
}
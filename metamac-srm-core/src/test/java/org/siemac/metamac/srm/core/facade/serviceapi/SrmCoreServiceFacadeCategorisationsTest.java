package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeCategorisationsTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // IMPORTANT: Metadata transformation is tested in Do2Dto tests

    // Categorisations
    private final String           CATEGORISATION_1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000)";
    private final String           CATEGORISATION_2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat2(01.000)";
    private final String           CATEGORISATION_4 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)";

    @Test
    public void testRetrieveCategorisationByUrn() throws Exception {

        String urn = CATEGORISATION_4;

        // Retrieve
        CategorisationDto categorisationDto = srmCoreServiceFacade.retrieveCategorisationByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(urn, categorisationDto.getUrn());
    }

    @Test
    public void testCreateCategorisation() throws Exception {

        // Create
        String categoryUrn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String artefactCategorisedUrn = CONCEPT_SCHEME_1_V1;
        String maintainerUrn = AGENCY_ROOT_1_V1; // maintainer with other categorisations
        CategorisationDto categorisationCreated = srmCoreServiceFacade.createCategorisation(getServiceContextAdministrador(), categoryUrn, artefactCategorisedUrn, maintainerUrn);

        // Validate some metadata
        assertTrue(categorisationCreated.getCode().startsWith(SdmxConstants.CATEGORISATION_CODE_PREFIX));
        assertNotNull(categorisationCreated.getUrn());
    }

    @Test
    public void testDeleteCategorisation() throws Exception {

        String urn = CATEGORISATION_4;

        // Delete
        srmCoreServiceFacade.deleteCategorisation(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCategorisationByUrn(getServiceContextAdministrador(), urn);
            fail("Categorisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveCategorisationsByArtefact() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;
        List<CategorisationDto> categorisations = srmCoreServiceFacade.retrieveCategorisationsByArtefact(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, categorisations.size());
        int i = 0;
        assertEquals(CATEGORISATION_1, categorisations.get(i++).getUrn());
        assertEquals(CATEGORISATION_2, categorisations.get(i++).getUrn());
        assertEquals(categorisations.size(), i);
    }

    @Test
    public void testEndCategorisationValidity() throws Exception {

        String urn = CATEGORISATION_1;

        CategorisationDto categorisation = srmCoreServiceFacade.endCategorisationValidity(getServiceContextAdministrador(), urn, null);

        assertNotNull(categorisation);
        assertTrue(DateUtils.isSameDay(new Date(), categorisation.getValidTo()));
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategorisationsTest.xml";
    }
}
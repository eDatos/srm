package org.siemac.metamac.srm.core.concept.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.enume.domain.MaintainableProcStatusEnum;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseAsserts;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class ConceptsServiceTest extends SrmBaseTest implements ConceptsServiceTestBase {

    @Autowired
    private ConceptsService conceptsService;

    private String          NOT_EXISTS          = "-1";

    // Concept schemes
    private String          CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(1.0)";
    private String          CONCEPT_SCHEME_1_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(2.0)";

    @Test
    public void testCreateConceptScheme() throws Exception {

        // Create
        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        ConceptSchemeVersion conceptSchemeVersionCreated = conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);

        // Validate
        assertNotNull(conceptSchemeVersionCreated);
        assertNotNull(conceptSchemeVersionCreated.getUuid());
        assertNotNull(conceptSchemeVersionCreated.getId());
        assertNotNull(conceptSchemeVersionCreated.getMaintainableArtefact().getUrn());

        // TODO
        ConceptSchemeVersion conceptSchemeVersionRetrieved = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionCreated.getMaintainableArtefact().getUrn());
        assertEquals(MaintainableProcStatusEnum.DRAFT, conceptSchemeVersionRetrieved.getMaintainableArtefact().getProcStatus());
        assertEquals("1.000", conceptSchemeVersionRetrieved.getMaintainableArtefact().getVersionLogic());
        assertNull(conceptSchemeVersionRetrieved.getMaintainableArtefact().getValidFrom());
        assertNull(conceptSchemeVersionRetrieved.getMaintainableArtefact().getValidTo());
        assertTrue(conceptSchemeVersionRetrieved.getMaintainableArtefact().getIsLastVersion());

        ConceptsAsserts.assertEqualsConceptScheme(conceptSchemeVersion, conceptSchemeVersionRetrieved);

        // Validate audit TODO
        // assertEquals(getServiceContextAdministrador().getUserId(), conceptSchemeVersionRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionRetrieved.getCreatedDate().toDate()));
        // assertEquals(getServiceContextAdministrador().getUserId(), conceptSchemeVersionRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionRetrieved.getLastUpdated().toDate()));
    }

    @Test
    public void testCreateConceptSchemeRequiredParameter() throws Exception {
        // TODO
        // try {
        // conceptsService.createConceptScheme(getServiceContextAdministrador(), null);
        // fail("parameter required");
        // } catch (MetamacException e) {
        // assertEquals(1, e.getExceptionItems().size());
        // assertEquals(MetamacCoreExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
        // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
        // assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME, e.getExceptionItems().get(0).getMessageParameters()[0]);
        // }
    }

    // TODO revisar
    @Test
    @Override
    public void testFindConceptSchemeByUrn() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_1_V1;
        ConceptSchemeVersion conceptSchemeVersion = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(Long.valueOf(1), conceptSchemeVersion.getId());
        assertEquals("conceptScheme-1-v1", conceptSchemeVersion.getUuid());

        MaintainableArtefact maintainableArtefact = conceptSchemeVersion.getMaintainableArtefact();
        assertEquals("CONCEPTSCHEME01", maintainableArtefact.getIdLogic());
        assertEquals(urn, maintainableArtefact.getUrn());
        assertEquals("http://data.siemac.org/srm/v1/conceptSchemes/conceptScheme01/v1", maintainableArtefact.getUri());
        assertNull(maintainableArtefact.getReplacedBy());

        assertEquals("ISTAC", maintainableArtefact.getMaintainer().getCode());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=ISTAC:STANDALONE(1.0).ISTAC", maintainableArtefact.getMaintainer().getUrn());
        assertEquals("http://data.siemac.org/srm/v1/agenciesSchemes/standalone/agencies/ISTAC", maintainableArtefact.getMaintainer().getUri());
        assertEquals(TypeExternalArtefactsEnum.AGENCY, maintainableArtefact.getMaintainer().getType());
        assertEquals(null, maintainableArtefact.getMaintainer().getTitle());
        assertEquals(null, maintainableArtefact.getMaintainer().getManagementAppUrl());
        assertEquals(Long.valueOf(1), maintainableArtefact.getMaintainer().getVersion());

        BaseAsserts.assertEqualsInternationalString(maintainableArtefact.getName(), "es", "Nombre conceptScheme-1-v1", "en", "Name conceptScheme-1-v1");
        BaseAsserts.assertEqualsInternationalString(maintainableArtefact.getDescription(), "es", "Descripción conceptScheme-1-v1", "en", "Description conceptScheme-1-v1");

        assertEquals("user1", conceptSchemeVersion.getCreatedBy());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getCreatedDate());
        assertEquals("user2", conceptSchemeVersion.getLastUpdatedBy());
        MetamacAsserts.assertEqualsDate("2011-01-22 01:02:03", conceptSchemeVersion.getLastUpdated());
    }

    @Test
    public void testFindConceptSchemeByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(MetamacCoreExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.URN, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindConceptSchemeByUrnErrorNotFound() throws Exception {
        String urn = NOT_EXISTS;
        try {
            conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(MetamacCoreExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // TODO revisar
    @Test
    @Override
    public void testRetrieveConceptSchemeHistoric() throws Exception {

        // Retrieve all versions
        String urn = CONCEPT_SCHEME_1_V1;
        List<ConceptSchemeVersion> conceptSchemeVersions = conceptsService.retrieveConceptSchemeHistoric(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, conceptSchemeVersions.size());
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersions.get(1).getMaintainableArtefact().getUrn());

    }

    // TODO revisar
    @Test
    @Override
    public void testFindConceptSchemeByCondition() throws Exception {
        
        // Find
        List<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersion> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemeByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(1, conceptSchemeVersionPagedResult.getTotalRows());
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersionPagedResult.getValues().get(0).getMaintainableArtefact().getUrn());
    }
}

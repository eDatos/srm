package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsMockitoVerify.verifyFindConceptSchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsMockitoVerify.verifyFindConcepts;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsDoMocks;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;

public class SrmRestInternalFacadeV10ConceptsTest extends SrmRestInternalFacadeV10BaseTest {

    private ConceptsMetamacService conceptsService;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindConceptSchemes() throws Exception {
        testFindConceptSchemes(null, null, null, null, null, null, null); // without limits
        testFindConceptSchemes(null, null, null, "10000", null, null, null); // without limits
        testFindConceptSchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindConceptSchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindConceptSchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindConceptSchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindConceptSchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindConceptSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindConceptSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindConceptSchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/findConceptSchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConceptSchemesByAgency() throws Exception {
        testFindConceptSchemes(AGENCY_1, null, null, null, null, null, null);
        testFindConceptSchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindConceptSchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindConceptSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindConceptSchemesByAgencyXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/findConceptSchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConceptSchemesByAgencyErrorWildcard() throws Exception {
        String requestUri = getUriItemSchemes(WILDCARD, null, null);
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindConceptSchemesByAgencyAndResource() throws Exception {
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindConceptSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindConceptSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindConceptSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindConceptSchemesByAgencyAndResourceXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/findConceptSchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConceptSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, WILDCARD, null);
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testRetrieveConceptScheme() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        ConceptScheme conceptScheme = getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(conceptScheme);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, conceptScheme.getAgencyID());
        assertEquals(resourceID, conceptScheme.getId());
        assertEquals(version, conceptScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, conceptScheme.getKind());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, conceptScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, conceptScheme.getParentLink().getKind());
        assertTrue(conceptScheme.getConcepts().get(0) instanceof ConceptType);
        assertFalse(conceptScheme.getConcepts().get(0) instanceof Concept);
    }

    @Test
    public void testRetrieveConceptSchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveConceptsSchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = ITEM_SCHEME_1_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Concept scheme not found in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
            assertEquals(3, exception.getParameters().getParameters().size());
            assertEquals(agencyID, exception.getParameters().getParameters().get(0));
            assertEquals(resourceID, exception.getParameters().getParameters().get(1));
            assertEquals(version, exception.getParameters().getParameters().get(2));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveConceptSchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptSchemeErrorWildcard() throws Exception {
        {
            String requestUri = getUriItemSchemes(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItemSchemes(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testFindConcepts() throws Exception {

        // without parameters
        testFindConcepts(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindConcepts(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindConceptsXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/findConcepts.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConcept() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String conceptID = ITEM_1_CODE;
        Concept concept = getSrmRestInternalFacadeClientXml().retrieveConcept(agencyID, resourceID, version, conceptID);

        // Validation
        assertNotNull(concept);
        assertEquals(conceptID, concept.getId());
        assertEquals(RestInternalConstants.KIND_CONCEPT, concept.getKind());
        assertEquals(RestInternalConstants.KIND_CONCEPT, concept.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, concept.getParentLink().getKind());
        assertTrue(concept instanceof ConceptType);
        assertTrue(concept instanceof Concept);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveConceptXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConcept.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveConceptErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String conceptID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveConcept(agencyID, resourceID, version, conceptID);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Concept not found with ID " + conceptID + " in Concept scheme in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(conceptID, exception.getParameters().getParameters().get(0));
            assertEquals(agencyID, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(version, exception.getParameters().getParameters().get(3));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveConceptErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConcept.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptErrorWildcard() throws Exception {
        {
            String requestUri = getUriItem(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItem(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testRetrieveConceptTypes() throws Exception {

        // Retrieve
        ConceptTypes conceptTypes = getSrmRestInternalFacadeClientXml().retrieveConceptTypes();

        // Validation
        assertEquals(RestInternalConstants.KIND_CONCEPT_TYPES, conceptTypes.getKind());
        assertTrue(conceptTypes.getConceptTypes().size() > 0);
    }

    @Test
    public void testRetrieveConceptTypesXml() throws Exception {

        String requestUri = getUriConceptTypes();
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptTypes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    private void testFindConceptSchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        ConceptSchemes conceptSchemes = null;
        if (agencyID == null) {
            conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, query, orderBy, limit, offset);
        } else {
            conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        // Verify with Mockito
        verifyFindConceptSchemes(conceptsService, agencyID, resourceID, limit, offset, query, orderBy, conceptSchemes);
    }

    private void testFindConcepts(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Concepts concepts = getSrmRestInternalFacadeClientXml().findConcepts(agencyID, resourceID, version, query, orderBy, limit, offset);
        verifyFindConcepts(conceptsService, agencyID, resourceID, version, limit, offset, query, orderBy, concepts);
    }

    @SuppressWarnings("unchecked")
    private void mockFindConceptSchemesByCondition() throws MetamacException {
        when(conceptsService.findConceptSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<ConceptSchemeVersionMetamac>>() {

            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<ConceptSchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptSchemeVersionMetamacProperties
                        .maintainableArtefact().maintainer().idAsMaintainer());
                ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptSchemeVersionMetamacProperties
                        .maintainableArtefact().code());
                ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptSchemeVersionMetamacProperties
                        .maintainableArtefact().versionLogic());

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null) {
                    // Retrieve one
                    ConceptSchemeVersionMetamac conceptSchemeVersion = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        conceptSchemeVersion = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        conceptSchemeVersion = ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                    } else {
                        fail();
                    }
                    List<ConceptSchemeVersionMetamac> conceptSchemes = new ArrayList<ConceptSchemeVersionMetamac>();
                    if (conceptSchemeVersion != null) {
                        conceptSchemes.add(conceptSchemeVersion);
                    }
                    return new PagedResult<ConceptSchemeVersionMetamac>(conceptSchemes, 0, conceptSchemes.size(), conceptSchemes.size());
                } else {
                    // any
                    List<ConceptSchemeVersionMetamac> conceptSchemes = new ArrayList<ConceptSchemeVersionMetamac>();
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1));
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1));
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2));
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1));
                    return new PagedResult<ConceptSchemeVersionMetamac>(conceptSchemes, conceptSchemes.size(), conceptSchemes.size(), conceptSchemes.size(), conceptSchemes.size() * 10, 0);
                }
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindConceptsByCondition() throws MetamacException {
        when(conceptsService.findConceptsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<ConceptMetamac>>() {

            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<ConceptMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer());
                ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().code());
                ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptMetamacProperties.itemSchemeVersion()
                        .maintainableArtefact().versionLogic());
                ConditionalCriteria conditionalCriteriaItem = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptMetamacProperties.nameableArtefact()
                        .code());

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null && conditionalCriteriaItem != null) {
                    // Retrieve one
                    ConceptMetamac concept = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaItem.getFirstOperant())) {
                        concept = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
                        ConceptSchemeVersionMetamac conceptScheme1 = ConceptsDoMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                        ConceptMetamac parent = ConceptsDoMocks.mockConcept(ITEM_2_CODE, conceptScheme1, null);
                        concept = ConceptsDoMocks.mockConcept(ITEM_1_CODE, conceptScheme1, parent);
                    } else {
                        fail();
                    }
                    List<ConceptMetamac> concepts = new ArrayList<ConceptMetamac>();
                    if (concept != null) {
                        concepts.add(concept);
                    }
                    return new PagedResult<ConceptMetamac>(concepts, 0, concepts.size(), concepts.size());
                } else {
                    // any
                    ConceptSchemeVersionMetamac conceptScheme1 = ConceptsDoMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                    ConceptSchemeVersionMetamac conceptScheme2 = ConceptsDoMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1);

                    List<ConceptMetamac> concepts = new ArrayList<ConceptMetamac>();
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_1_CODE, conceptScheme1, null));
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_2_CODE, conceptScheme1, null));
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_3_CODE, conceptScheme1, null));
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_1_CODE, conceptScheme2, null));

                    return new PagedResult<ConceptMetamac>(concepts, concepts.size(), concepts.size(), concepts.size(), concepts.size() * 10, 0);
                }
            };
        });
    }

    private void mockRetrieveConceptTypes() throws MetamacException {
        when(conceptsService.findAllConceptTypes(any(ServiceContext.class))).thenReturn(ConceptsDoMocks.mockConceptTypes());
    }

    private String getUriConceptTypes() {
        return baseApi + "/conceptTypes";
    }

    @Override
    protected void resetMocks() throws MetamacException {
        conceptsService = applicationContext.getBean(ConceptsMetamacService.class);
        reset(conceptsService);
        mockFindConceptSchemesByCondition();
        mockFindConceptsByCondition();
        mockRetrieveConceptTypes();
    }

    @Override
    protected String getSupathItemSchemes() {
        return "conceptschemes";
    }

    @Override
    protected String getSupathItems() {
        return "concepts";
    }
}
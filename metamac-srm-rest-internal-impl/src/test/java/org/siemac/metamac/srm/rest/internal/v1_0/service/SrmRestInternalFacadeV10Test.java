package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertFindConceptSchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertFindConcepts;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.common.test.MetamacRestBaseTest;
import org.siemac.metamac.rest.common.test.ServerResource;
import org.siemac.metamac.rest.constants.RestConstants;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmCoreMocks;
import org.springframework.context.ApplicationContext;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;

public class SrmRestInternalFacadeV10Test extends MetamacRestBaseTest {

    private static final String             PORT               = ServerResource.PORT;
    private static String                   jaxrsServerAddress = "http://localhost:" + PORT + "/apis/srm-internal";
    private static String                   baseApi            = jaxrsServerAddress + "/v1.0";

    private ConceptsMetamacService          conceptsService;
    private static SrmRestInternalFacadeV10 srmRestInternalFacadeClientXml;
    private static ApplicationContext       applicationContext = null;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // Start server
        assertTrue("server did not launch correctly", launchServer(ServerResource.class, true));

        // Get application context from Jetty
        applicationContext = ApplicationContextProvider.getApplicationContext();

        // Rest clients
        // xml
        {
            List providers = new ArrayList();
            providers.add(applicationContext.getBean("jaxbProvider", JAXBElementProvider.class));
            srmRestInternalFacadeClientXml = JAXRSClientFactory.create(jaxrsServerAddress, SrmRestInternalFacadeV10.class, providers, Boolean.TRUE);
        }
    }

    @Before
    public void setUp() throws MetamacException {
        resetMocks();
    }

    @Test
    public void testErrorWithoutMatchError404() throws Exception {
        String requestUri = baseApi + "/nomatch";
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindConceptsSchemes() throws Exception {
        testFindConceptsSchemes(null, null, null, null); // without limits
        testFindConceptsSchemes("10000", null, null, null); // without limits
        testFindConceptsSchemes(null, "0", null, null); // without limits, first page
        testFindConceptsSchemes("2", "0", null, null); // first page with pagination
        testFindConceptsSchemes("2", "2", null, null); // other page with pagination
        testFindConceptsSchemes(null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindConceptsSchemes(null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindConceptsSchemes("1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindConceptsSchemes("1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindConceptsSchemesWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/findConceptsSchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConceptsSchemesErrorParameterIncorrectWithoutJaxbTransformation() throws Exception {

        // Metadata not supported to search
        String limit = "1";
        String offset = "0";
        String query = "METADATA_INCORRECT LIKE \"1\"";
        String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, query, limit, offset);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/findConceptsSchemes.parameterIncorrect.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.INTERNAL_SERVER_ERROR, responseExpected);
    }

    @Test
    public void testFindConceptsSchemesByAgency() throws Exception {
        testFindConceptsSchemes(AGENCY_1, null, null, null, null);
        testFindConceptsSchemes(AGENCY_1, null, "0", null, null);
        testFindConceptsSchemes(AGENCY_1, "2", "0", null, null);
        testFindConceptsSchemes(AGENCY_1, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptsSchemes(AGENCY_1, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindConceptsSchemesByAgencyWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/findConceptsSchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConceptsSchemesByAgencyErrorWildcard() throws Exception {
        String requestUri = baseApi + "/conceptschemes/" + WILDCARD;
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindConceptsSchemesByAgencyAndResource() throws Exception {
        testFindConceptsSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);
        testFindConceptsSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null);
        testFindConceptsSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, "2", null, null, null);
        testFindConceptsSchemes(WILDCARD, ITEM_SCHEME_1_CODE, "2", null, null, null);
        testFindConceptsSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "0", null, null);
        testFindConceptsSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "0", null, null);
        testFindConceptsSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, "2", "0", null, null);
        testFindConceptsSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptsSchemes(WILDCARD, ITEM_SCHEME_1_CODE, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptsSchemes(WILDCARD, ITEM_SCHEME_1_CODE, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindConceptsSchemesByAgencyAndResourceWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/findConceptsSchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConceptsSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, WILDCARD, null);
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
        // other metadata are tested in transformation tests
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
    public void testRetrieveConceptSchemeWithoutJaxbTransformation() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptSchemeWithoutJaxbTransformationWithXmlSufix() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1) + ".xml";
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptSchemeWithoutJaxbTransformationWithTypeParameter() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1) + "?_type=xml";
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
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
    public void testRetrieveConceptSchemeErrorNotExistsWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptsSchemeErrorWildcard() throws Exception {
        {
            String requestUri = getUriConceptSchemes(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriConceptSchemes(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testFindConcepts() throws Exception {
        // wildcard
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
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindConceptsWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConcepts(WILDCARD, WILDCARD, WILDCARD, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/findConcepts.xml");

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
    public void testRetrieveConceptWithoutJaxbTransformation() throws Exception {

        String requestUri = getUriConcept(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConcept.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptWithoutJaxbTransformationWithXmlSufix() throws Exception {

        String requestUri = getUriConcept(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE) + ".xml";
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConcept.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptWithoutJaxbTransformationWithTypeParameter() throws Exception {

        String requestUri = getUriConcept(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE) + "?_type=xml";
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConcept.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
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
            assertEquals("Concept not found with ID " + conceptID + " in concept scheme in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
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
    public void testRetrieveConceptErrorNotExistsWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConcept(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConcept.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptErrorWildcard() throws Exception {
        {
            String requestUri = getUriConcept(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriConcept(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriConcept(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
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
    public void testRetrieveConceptTypesWithoutJaxbTransformation() throws Exception {

        String requestUri = getUriConceptTypes();
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptTypes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    private void testFindConceptsSchemes(String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(query, orderBy, limit, offset);
        assertFindConceptSchemes(conceptsService, null, null, limit, offset, query, orderBy, conceptSchemes);
    }

    private void testFindConceptsSchemes(String agencyID, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, query, orderBy, limit, offset);
        assertFindConceptSchemes(conceptsService, agencyID, null, limit, offset, query, orderBy, conceptSchemes);
    }

    private void testFindConceptsSchemes(String agencyID, String resourceID, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, resourceID, query, orderBy, limit, offset);
        assertFindConceptSchemes(conceptsService, agencyID, resourceID, limit, offset, query, orderBy, conceptSchemes);
    }

    private void testFindConcepts(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Concepts concepts = getSrmRestInternalFacadeClientXml().findConcepts(agencyID, resourceID, version, query, orderBy, limit, offset);
        assertFindConcepts(conceptsService, agencyID, resourceID, version, limit, offset, query, orderBy, concepts);
    }

    private void resetMocks() throws MetamacException {
        conceptsService = applicationContext.getBean(ConceptsMetamacService.class);
        reset(conceptsService);
        mockFindConceptSchemesByCondition();
        mockFindConceptsByCondition();
        mockRetrieveConceptTypes();
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
                    // Retrieve one concept scheme
                    ConceptSchemeVersionMetamac conceptSchemeVersion = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        conceptSchemeVersion = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        conceptSchemeVersion = SrmCoreMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
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
                    conceptSchemes.add(SrmCoreMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1));
                    conceptSchemes.add(SrmCoreMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1));
                    conceptSchemes.add(SrmCoreMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2));
                    conceptSchemes.add(SrmCoreMocks.mockConceptSchemeWithConcepts(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1));
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
                ConditionalCriteria conditionalCriteriaConcept = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, ConceptMetamacProperties.nameableArtefact()
                        .code());

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null && conditionalCriteriaConcept != null) {
                    // Retrieve one concept
                    ConceptMetamac concept = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaConcept.getFirstOperant())) {
                        concept = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_1_CODE.equals(conditionalCriteriaConcept.getFirstOperant())) {
                        ConceptSchemeVersionMetamac conceptScheme1 = SrmCoreMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                        concept = SrmCoreMocks.mockConcept(ITEM_1_CODE, conceptScheme1, null);
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
                    ConceptSchemeVersionMetamac conceptScheme1 = SrmCoreMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                    ConceptSchemeVersionMetamac conceptScheme2 = SrmCoreMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1);

                    List<ConceptMetamac> concepts = new ArrayList<ConceptMetamac>();
                    concepts.add(SrmCoreMocks.mockConcept(ITEM_1_CODE, conceptScheme1, null));
                    concepts.add(SrmCoreMocks.mockConcept(ITEM_2_CODE, conceptScheme1, null));
                    concepts.add(SrmCoreMocks.mockConcept(ITEM_3_CODE, conceptScheme1, null));
                    concepts.add(SrmCoreMocks.mockConcept(ITEM_1_CODE, conceptScheme2, null));

                    return new PagedResult<ConceptMetamac>(concepts, concepts.size(), concepts.size(), concepts.size(), concepts.size() * 10, 0);
                }
            };
        });
    }

    private void mockRetrieveConceptTypes() throws MetamacException {
        when(conceptsService.findAllConceptTypes(any(ServiceContext.class))).thenReturn(SrmCoreMocks.mockConceptTypes());
    }

    private SrmRestInternalFacadeV10 getSrmRestInternalFacadeClientXml() {
        WebClient.client(srmRestInternalFacadeClientXml).reset();
        WebClient.client(srmRestInternalFacadeClientXml).accept(APPLICATION_XML);
        return srmRestInternalFacadeClientXml;
    }

    private String getUriConceptSchemes(String agencyID, String resourceID, String version) {
        StringBuilder uri = new StringBuilder();
        uri.append(baseApi + "/conceptschemes");
        if (agencyID != null) {
            uri.append("/" + agencyID);
            if (resourceID != null) {
                uri.append("/" + resourceID);
                if (version != null) {
                    uri.append("/" + version);
                }
            }
        }
        return uri.toString();
    }

    private String getUriConceptSchemes(String agencyID, String resourceID, String query, String limit, String offset) throws Exception {
        String uri = getUriConceptSchemes(agencyID, resourceID, null);
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriConcepts(String agencyID, String resourceID, String version, String query, String limit, String offset) throws Exception {
        String uri = getUriConceptSchemes(agencyID, resourceID, version) + "/concepts";
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriConcept(String agencyID, String resourceID, String version, String conceptID) throws Exception {
        return getUriConcepts(agencyID, resourceID, version, null, null, null) + "/" + conceptID;
    }

    private String getUriConceptTypes() {
        return baseApi + "/conceptTypes";
    }
}
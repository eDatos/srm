package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertFindConceptSchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_SCHEME_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;

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
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmCoreMocks;
import org.springframework.context.ApplicationContext;

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
    public void testWithoutMatchError404() throws Exception {
        String requestUri = baseApi + "/nomatch";
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindConceptsSchemes() throws Exception {
        testFindConceptsSchemes(null, null, null, null); // without limits
        testFindConceptsSchemes("10000", null, null, null); // without limits
        testFindConceptsSchemes(null, "0", null, null); // without limits, first page
        testFindConceptsSchemes("2", "0", null, null); // first page with pagination
        testFindConceptsSchemes("2", "2", null, null); // other page with pagination
        testFindConceptsSchemes(null, null, QUERY_CONCEPT_SCHEME_ID_LIKE_1, null); // query by id, without limits
        testFindConceptsSchemes(null, null, QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindConceptsSchemes("1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
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
        String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, query, limit, offset);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/findConceptsSchemes.parameterIncorrect.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.INTERNAL_SERVER_ERROR, responseExpected);
    }

    @Test
    public void testFindConceptsSchemesByAgency() throws Exception {
        testFindConceptsSchemes(AGENCY_1, null, null, null, null);
        testFindConceptsSchemes(AGENCY_1, null, "0", null, null);
        testFindConceptsSchemes(AGENCY_1, "2", "0", null, null);
        testFindConceptsSchemes(AGENCY_1, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
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
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, null, null, null, null);
        testFindConceptsSchemes(WILDCARD, CONCEPT_SCHEME_1_CODE, null, null, null, null);
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, "2", null, null, null);
        testFindConceptsSchemes(WILDCARD, CONCEPT_SCHEME_1_CODE, "2", null, null, null);
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, null, "0", null, null);
        testFindConceptsSchemes(WILDCARD, CONCEPT_SCHEME_1_CODE, null, "0", null, null);
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, "2", "0", null, null);
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptsSchemes(WILDCARD, CONCEPT_SCHEME_1_CODE, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
    }

    @Test
    public void testFindConceptsSchemesByAgencyAndResourceWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, null, "4", null);
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
        String resourceID = CONCEPT_SCHEME_1_CODE;
        String version = CONCEPT_SCHEME_1_VERSION_1;
        ConceptScheme conceptScheme = getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(conceptScheme);
        assertEquals("idAsMaintainer" + agencyID, conceptScheme.getAgencyID());
        assertEquals(resourceID, conceptScheme.getId());
        assertEquals(version, conceptScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, conceptScheme.getKind());
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveConceptSchemeWithoutJaxbTransformation() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptSchemeWithoutJaxbTransformationWithXmlSufix() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1) + ".xml";
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptSchemeWithoutJaxbTransformationWithTypeParameter() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1) + "?_type=xml";
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.id1.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConceptsSchemeErroNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = CONCEPT_SCHEME_1_VERSION_1;
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
    public void testRetrieveOperationByIdErrorNotExistsWithoutJaxbTransformation() throws Exception {
        String requestUri = getUriConceptSchemes(AGENCY_1, NOT_EXISTS, CONCEPT_SCHEME_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10Test.class.getResourceAsStream("/responses/retrieveConceptScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptsSchemeErrorWildcard() throws Exception {
        {
            String requestUri = getUriConceptSchemes(WILDCARD, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriConceptSchemes(AGENCY_1, WILDCARD, CONCEPT_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, WILDCARD);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testRetrieveConceptsSchemeErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriConceptSchemes(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
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

    private void resetMocks() throws MetamacException {
        conceptsService = applicationContext.getBean(ConceptsMetamacService.class);
        reset(conceptsService);
        mockFindConceptsByCondition();
    }

    @SuppressWarnings("unchecked")
    private void mockFindConceptsByCondition() throws MetamacException {
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
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && CONCEPT_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && CONCEPT_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        conceptSchemeVersion = SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1);
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
                    conceptSchemes.add(SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1));
                    conceptSchemes.add(SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_2_CODE, CONCEPT_SCHEME_2_VERSION_1));
                    conceptSchemes.add(SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_2_CODE, CONCEPT_SCHEME_2_VERSION_2));
                    conceptSchemes.add(SrmCoreMocks.mockConceptScheme(AGENCY_2, CONCEPT_SCHEME_3_CODE, CONCEPT_SCHEME_3_VERSION_1));
                    return new PagedResult<ConceptSchemeVersionMetamac>(conceptSchemes, conceptSchemes.size(), conceptSchemes.size(), conceptSchemes.size(), conceptSchemes.size() * 3, 0);
                }
            };
        });
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
}
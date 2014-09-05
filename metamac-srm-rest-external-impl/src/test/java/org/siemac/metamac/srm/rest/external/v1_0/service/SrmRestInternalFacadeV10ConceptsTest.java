package org.siemac.metamac.srm.rest.external.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_LATEST;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_2;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.external.v1_0.concept.utils.ConceptsDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class SrmRestInternalFacadeV10ConceptsTest extends SrmRestInternalFacadeV10BaseTest {

    private ConceptsMetamacService      conceptsService;
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Test
    public void testJsonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
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
        testFindConceptSchemes(null, null, null, null, null, QUERY_LATEST, null); // latest
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
    public void testFindConceptSchemesByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "?limit=4&offset=4", conceptSchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "?limit=4&offset=0", conceptSchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "?limit=4&offset=0", conceptSchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "?limit=4&offset=36", conceptSchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEMES, conceptSchemes.getKind());
    }

    @Test
    public void testFindConceptSchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findConceptSchemes(WILDCARD_ALL, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testFindConceptSchemesByAgencyAndResource() throws Exception {
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindConceptSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindConceptSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindConceptSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindConceptSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindConceptSchemesByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=4", conceptSchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", conceptSchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", conceptSchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/conceptschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=36", conceptSchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEMES, conceptSchemes.getKind());
    }

    @Test
    public void testFindConceptSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findConceptSchemes(AGENCY_1, WILDCARD_ALL, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveConceptScheme() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        ConceptScheme conceptScheme = getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(conceptScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, conceptScheme.getAgencyID());
        assertEquals(resourceID, conceptScheme.getId());
        assertEquals(version, conceptScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEME, conceptScheme.getKind());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEME, conceptScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEMES, conceptScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveConceptSchemeVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = WILDCARD_LATEST;
        ConceptScheme conceptScheme = getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(conceptScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, conceptScheme.getAgencyID());
        assertEquals(resourceID, conceptScheme.getId());
        assertEquals(VERSION_1, conceptScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEME, conceptScheme.getKind());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEME, conceptScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEMES, conceptScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveConceptSchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
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
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("ConceptScheme " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
            assertEquals(3, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(agencyID, exception.getParameters().getParameters().get(2));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveConceptSchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptSchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveConceptScheme(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Resource
        try {
            getSrmRestInternalFacadeClientXml().retrieveConceptScheme(AGENCY_1, WILDCARD_ALL, VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Version
        try {
            getSrmRestInternalFacadeClientXml().retrieveConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testFindConceptsRetrieveAll() throws Exception {

        resetMocks();
        Concepts concepts = getSrmRestInternalFacadeClientXml().findConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);

        assertNotNull(concepts);
        assertEquals(SrmRestConstants.KIND_CONCEPTS, concepts.getKind());
        assertEquals(BigInteger.valueOf(4), concepts.getTotal());

        // Verify with mockito
        ArgumentCaptor<String> conceptSchemeUrnArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ItemResultSelection> itemResultSelectionArgument = ArgumentCaptor.forClass(ItemResultSelection.class);
        verify(conceptsService).retrieveConceptsByConceptSchemeUrnUnordered(any(ServiceContext.class), conceptSchemeUrnArgument.capture(), itemResultSelectionArgument.capture());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=agency1:itemScheme1(01.000)", conceptSchemeUrnArgument.getValue());
        assertEquals(true, itemResultSelectionArgument.getValue().isNames());
        assertEquals(false, itemResultSelectionArgument.getValue().isDescriptions());
        assertEquals(false, itemResultSelectionArgument.getValue().isComments());
        assertEquals(false, itemResultSelectionArgument.getValue().isAnnotations());
    }

    @Test
    public void testFindConceptsRetrieveAllXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/findConceptsRetrieveAll.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindConcepts() throws Exception {

        // without parameters
        testFindConcepts(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindConcepts(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindConcepts(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindConcepts(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindConcepts(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindConcepts(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindConcepts(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindConcepts(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindConcepts(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null); // without limits
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindConcepts(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null); // with pagination
        testFindConcepts(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindConcepts(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindConceptsXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/findConcepts.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveConcept() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String conceptID = ITEM_1_CODE;
        Concept concept = getSrmRestInternalFacadeClientXml().retrieveConcept(agencyID, resourceID, version, conceptID);

        // Validation
        assertNotNull(concept);
        assertEquals(conceptID, concept.getId());
        assertEquals(SrmRestConstants.KIND_CONCEPT, concept.getKind());
        assertEquals(SrmRestConstants.KIND_CONCEPT, concept.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CONCEPTS, concept.getParentLink().getKind());
        assertTrue(concept instanceof Concept);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveConceptXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
        String version = VERSION_1;
        String conceptID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveConcept(agencyID, resourceID, version, conceptID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Concept " + conceptID + " not found in version " + version + " of ConceptScheme " + resourceID + " from Agency " + agencyID, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(conceptID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(agencyID, exception.getParameters().getParameters().get(3));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveConceptErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10ConceptsTest.class.getResourceAsStream("/responses/concepts/retrieveConcept.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveConceptErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveConcept(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveConcept(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveConcept(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // ItemID
        try {
            getSrmRestInternalFacadeClientXml().retrieveConcept(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter conceptID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_CONCEPT_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveConceptTypes() throws Exception {

        // Retrieve
        ConceptTypes conceptTypes = getSrmRestInternalFacadeClientXml().retrieveConceptTypes();

        // Validation
        assertEquals(SrmRestConstants.KIND_CONCEPT_TYPES, conceptTypes.getKind());
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

        assertNotNull(conceptSchemes);
        assertEquals(SrmRestConstants.KIND_CONCEPT_SCHEMES, conceptSchemes.getKind());
    }

    private void testFindConcepts(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Concepts concepts = getSrmRestInternalFacadeClientXml().findConcepts(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(concepts);
        assertEquals(SrmRestConstants.KIND_CONCEPTS, concepts.getKind());
    }

    private void mockRetrieveItemSchemeVersionByVersion() throws MetamacException {
        when(itemSchemeVersionRepository.retrieveByVersion(any(Long.class), any(String.class))).thenAnswer(new Answer<ItemSchemeVersion>() {

            @Override
            public ItemSchemeVersion answer(InvocationOnMock invocation) throws Throwable {
                String version = (String) invocation.getArguments()[1];
                return ConceptsDoMocks.mockConceptScheme("agencyID", version, version);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindConceptSchemesByCondition() throws MetamacException {
        when(conceptsService.findConceptSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<ConceptSchemeVersionMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<ConceptSchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
                Boolean latest = getVersionLatestFromConditionalCriteria(conditions, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
                if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                    // Retrieve one
                    ConceptSchemeVersionMetamac conceptSchemeVersion = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                        conceptSchemeVersion = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                        conceptSchemeVersion = ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
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
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
                    conceptSchemes.add(ConceptsDoMocks.mockConceptSchemeWithConcepts(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));
                    return new PagedResult<ConceptSchemeVersionMetamac>(conceptSchemes, conceptSchemes.size(), conceptSchemes.size(), conceptSchemes.size(), conceptSchemes.size() * 10, 0);
                }
            };
        });
    }

    private void mockRetrieveConceptsByConceptScheme() throws MetamacException {
        when(conceptsService.retrieveConceptsByConceptSchemeUrnUnordered(any(ServiceContext.class), any(String.class), any(ItemResultSelection.class))).thenAnswer(new Answer<List<ItemResult>>() {

            @Override
            public List<ItemResult> answer(InvocationOnMock invocation) throws Throwable {
                // any
                ItemResult concept1 = ConceptsDoMocks.mockConceptItemResult("concept1", null);
                ItemResult concept2 = ConceptsDoMocks.mockConceptItemResult("concept2", null);
                ItemResult concept2A = ConceptsDoMocks.mockConceptItemResult("concept2A", concept2);
                ItemResult concept2B = ConceptsDoMocks.mockConceptItemResult("concept2B", concept2);
                return Arrays.asList(concept1, concept2, concept2A, concept2B);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindConceptsByCondition() throws MetamacException {
        when(conceptsService.findConceptsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<ConceptMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<ConceptMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String itemID = getItemIdFromConditionalCriteria(conditions, ConceptMetamacProperties.nameableArtefact());

                if (agencyID != null && resourceID != null && version != null && itemID != null) {
                    // Retrieve one
                    ConceptMetamac concept = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version) || NOT_EXISTS.equals(itemID)) {
                        concept = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_1_CODE.equals(itemID)) {
                        ConceptSchemeVersionMetamac conceptScheme1 = ConceptsDoMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
                        ConceptMetamac parent = ConceptsDoMocks.mockConcept(ITEM_2_CODE, conceptScheme1, null);
                        concept = ConceptsDoMocks.mockConceptWithConceptRelations(ITEM_1_CODE, conceptScheme1, parent);
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
                    ConceptSchemeVersionMetamac conceptScheme1 = ConceptsDoMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
                    ConceptSchemeVersionMetamac conceptScheme2 = ConceptsDoMocks.mockConceptScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);

                    List<ConceptMetamac> concepts = new ArrayList<ConceptMetamac>();
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_1_CODE, conceptScheme1, null));
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_2_CODE, conceptScheme1, null));
                    concepts.add(ConceptsDoMocks.mockConcept(ITEM_3_CODE, conceptScheme1, concepts.get(1)));
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
        itemSchemeVersionRepository = applicationContext.getBean(ItemSchemeVersionRepository.class);
        reset(itemSchemeVersionRepository);

        mockRetrieveItemSchemeVersionByVersion();
        mockFindConceptSchemesByCondition();
        mockFindConceptsByCondition();
        mockRetrieveConceptsByConceptScheme();
        mockRetrieveConceptTypes();
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "conceptschemes";
    }

    @Override
    protected String getSupathItems() {
        return "concepts";
    }
}
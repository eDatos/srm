package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.LATEST;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindCodelists;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindCodes;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveCode;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveCodelist;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;

import java.io.InputStream;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks;

import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class SrmRestInternalFacadeV10CodesTest extends SrmRestInternalFacadeV10BaseTest {

    private CodesMetamacService   codesService;
    private CodeRepository        codeRepository;
    private CodeMetamacRepository codeMetamacRepository;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindCodelists() throws Exception {
        testFindCodelists(null, null, null, null, null, null, null); // without limits
        testFindCodelists(null, null, null, "10000", null, null, null); // without limits
        testFindCodelists(null, null, null, null, "0", null, null); // without limits, first page
        testFindCodelists(null, null, null, "2", "0", null, null); // first page with pagination
        testFindCodelists(null, null, null, "2", "2", null, null); // other page with pagination
        testFindCodelists(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindCodelists(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindCodelists(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindCodelists(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindCodelists(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindCodelistsXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findCodelists.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCodelistsByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        Codelists codelists = getSrmRestInternalFacadeClientXml().findCodelists(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "?limit=4&offset=4", codelists.getSelfLink());
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "?limit=4&offset=0", codelists.getFirstLink());
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "?limit=4&offset=0", codelists.getPreviousLink());
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "?limit=4&offset=36", codelists.getLastLink());
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelists.getKind());
    }

    @Test
    public void testFindCodelistsByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCodelists(WILDCARD, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testFindCodelistsByAgencyAndResource() throws Exception {
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCodelists(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCodelists(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCodelists(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCodelists(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCodelists(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCodelistsByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        Codelists codelists = getSrmRestInternalFacadeClientXml().findCodelists(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "/" + resourceID + "?limit=4&offset=4", codelists.getSelfLink());
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "/" + resourceID + "?limit=4&offset=0", codelists.getFirstLink());
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "/" + resourceID + "?limit=4&offset=0", codelists.getPreviousLink());
        assertEquals(getApiEndpoint() + "/codelists/" + agencyID + "/" + resourceID + "?limit=4&offset=36", codelists.getLastLink());
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelists.getKind());
    }

    @Test
    public void testFindCodelistsByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCodelists(AGENCY_1, WILDCARD, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCodelist() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        Codelist codelist = getSrmRestInternalFacadeClientXml().retrieveCodelist(agencyID, resourceID, version);

        // Validation
        assertNotNull(codelist);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, codelist.getAgencyID());
        assertEquals(resourceID, codelist.getId());
        assertEquals(version, codelist.getVersion());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getKind());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelist.getParentLink().getKind());
        assertTrue(codelist.getCodes().get(0) instanceof CodeType);
        assertFalse(codelist.getCodes().get(0) instanceof Code);

        // Verify with Mockito
        verifyRetrieveCodelist(codesService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveCodelistVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = LATEST;
        Codelist codelist = getSrmRestInternalFacadeClientXml().retrieveCodelist(agencyID, resourceID, version);

        // Validation
        assertNotNull(codelist);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, codelist.getAgencyID());
        assertEquals(resourceID, codelist.getId());
        assertEquals(ITEM_SCHEME_VERSION_1, codelist.getVersion());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getKind());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelist.getParentLink().getKind());
        assertTrue(codelist.getCodes().get(0) instanceof CodeType);
        assertFalse(codelist.getCodes().get(0) instanceof Code);

        // Verify with Mockito
        verifyRetrieveCodelist(codesService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveCodelistXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCodelist.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCodesSchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = ITEM_SCHEME_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCodelist(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CODELIST_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Codelist " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveCodelistErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCodelist.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCodelistErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveCodelist(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Resource
        try {
            getSrmRestInternalFacadeClientXml().retrieveCodelist(AGENCY_1, WILDCARD, ITEM_SCHEME_VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Version
        try {
            getSrmRestInternalFacadeClientXml().retrieveCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testFindCodes() throws Exception {

        // without parameters
        testFindCodes(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindCodes(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindCodes(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindCodes(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindCodes(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindCodes(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindCodes(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindCodes(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindCodes(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindCodes(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindCodes(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindCodes(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, "2", "0", null, null); // with pagination
        testFindCodes(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(AGENCY_1, WILDCARD, ITEM_SCHEME_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindCodesXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findCodes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveCode() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        String codeID = ITEM_1_CODE;
        Code code = getSrmRestInternalFacadeClientXml().retrieveCode(agencyID, resourceID, version, codeID);

        // Validation
        assertNotNull(code);
        assertEquals(codeID, code.getId());
        assertEquals(RestInternalConstants.KIND_CODE, code.getKind());
        assertEquals(RestInternalConstants.KIND_CODE, code.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CODES, code.getParentLink().getKind());
        assertTrue(code instanceof CodeType);
        assertTrue(code instanceof Code);
        // other metadata are tested in transformation tests

        // Verify with Mockito
        verifyRetrieveCode(codesService, agencyID, resourceID, version, codeID);
    }

    @Test
    public void testRetrieveCodeXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCode.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCodeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        String codeID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCode(agencyID, resourceID, version, codeID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CODE_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Code " + codeID + " not found in version " + version + " of Codelist " + resourceID + " from Agency " + agencyID, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(codeID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(agencyID, exception.getParameters().getParameters().get(3));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCodeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCode.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCodeErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCode(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, ITEM_1_CODE);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCode(AGENCY_1, WILDCARD, ITEM_SCHEME_VERSION_1, ITEM_1_CODE);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCode(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // ItemID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCode(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, WILDCARD);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter codeID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_CODE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    private void testFindCodelists(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        Codelists codelists = null;
        if (agencyID == null) {
            codelists = getSrmRestInternalFacadeClientXml().findCodelists(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            codelists = getSrmRestInternalFacadeClientXml().findCodelists(agencyID, query, orderBy, limit, offset);
        } else {
            codelists = getSrmRestInternalFacadeClientXml().findCodelists(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(codelists);
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelists.getKind());

        // Verify with Mockito
        verifyFindCodelists(codesService, agencyID, resourceID, version, limit, offset, query, orderBy);
    }

    private void testFindCodes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Codes codes = getSrmRestInternalFacadeClientXml().findCodes(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(codes);
        assertEquals(RestInternalConstants.KIND_CODES, codes.getKind());

        // Verify with mockito
        verifyFindCodes(codesService, agencyID, resourceID, version, limit, offset, query, orderBy);
    }

    @SuppressWarnings("unchecked")
    private void mockFindCodelistsByCondition() throws MetamacException {
        when(codesService.findCodelistsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CodelistVersionMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CodelistVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, CodelistVersionMetamacProperties.maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CodelistVersionMetamacProperties.maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, CodelistVersionMetamacProperties.maintainableArtefact());
                Boolean latest = getVersionLatestFromConditionalCriteria(conditions, CodelistVersionMetamacProperties.maintainableArtefact());
                if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                    // Retrieve one
                    CodelistVersionMetamac codelistVersion = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                        codelistVersion = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && (ITEM_SCHEME_VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                        codelistVersion = CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
                    } else {
                        fail();
                    }
                    List<CodelistVersionMetamac> codelists = new ArrayList<CodelistVersionMetamac>();
                    if (codelistVersion != null) {
                        codelists.add(codelistVersion);
                    }
                    return new PagedResult<CodelistVersionMetamac>(codelists, 0, codelists.size(), codelists.size());
                } else {
                    // any
                    List<CodelistVersionMetamac> codelists = new ArrayList<CodelistVersionMetamac>();
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1));
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_1));
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_2));
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_VERSION_1));
                    return new PagedResult<CodelistVersionMetamac>(codelists, codelists.size(), codelists.size(), codelists.size(), codelists.size() * 10, 0);
                }
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCodesByCondition() throws MetamacException {
        when(codesService.findCodesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CodeMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CodeMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, CodeMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CodeMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, CodeMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String itemID = getItemIdFromConditionalCriteria(conditions, CodeMetamacProperties.nameableArtefact());

                if (agencyID != null && resourceID != null && version != null && itemID != null) {
                    // Retrieve one
                    CodeMetamac code = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version) || NOT_EXISTS.equals(itemID)) {
                        code = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && ITEM_SCHEME_VERSION_1.equals(version) && ITEM_1_CODE.equals(itemID)) {
                        CodelistVersionMetamac codelist1 = CodesDoMocks.mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
                        CodeMetamac parent = CodesDoMocks.mockCode(ITEM_2_CODE, codelist1, null);
                        code = CodesDoMocks.mockCode(ITEM_1_CODE, codelist1, parent);
                    } else {
                        fail();
                    }
                    List<CodeMetamac> codes = new ArrayList<CodeMetamac>();
                    if (code != null) {
                        codes.add(code);
                    }
                    return new PagedResult<CodeMetamac>(codes, 0, codes.size(), codes.size());
                } else {
                    // any
                    CodelistVersionMetamac codelist1 = CodesDoMocks.mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
                    CodelistVersionMetamac codelist2 = CodesDoMocks.mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_1);

                    List<CodeMetamac> codes = new ArrayList<CodeMetamac>();
                    codes.add(CodesDoMocks.mockCode(ITEM_1_CODE, codelist1, null));
                    codes.add(CodesDoMocks.mockCode(ITEM_2_CODE, codelist1, null));
                    codes.add(CodesDoMocks.mockCode(ITEM_3_CODE, codelist1, null));
                    codes.add(CodesDoMocks.mockCode(ITEM_1_CODE, codelist2, null));

                    return new PagedResult<CodeMetamac>(codes, codes.size(), codes.size(), codes.size(), codes.size() * 10, 0);
                }
            };
        });
    }

    private void mockFindCodesByNativeSqlQuery() throws MetamacException {
        when(codeRepository.findCodesByCodelistUnordered(any(Long.class), any(Boolean.class))).thenAnswer(new Answer<List<ItemResult>>() {

            @Override
            public List<ItemResult> answer(InvocationOnMock invocation) throws Throwable {
                // any
                ItemResult code1 = CodesDoMocks.mockCodeResult("code1", null);
                ItemResult code2 = CodesDoMocks.mockCodeResult("code2", null);
                ItemResult code2A = CodesDoMocks.mockCodeResult("code2A", code2);
                ItemResult code2B = CodesDoMocks.mockCodeResult("code2B", code2);
                return Arrays.asList(code1, code2, code2A, code2B);
            };
        });

        when(codeMetamacRepository.findCodesByCodelistOrderedInDepth(any(Long.class), any(Integer.class), eq(Boolean.FALSE))).thenAnswer(new Answer<List<ItemResult>>() {

            @Override
            public List<ItemResult> answer(InvocationOnMock invocation) throws Throwable {
                // any
                ItemResult code1 = CodesDoMocks.mockCodeResult("code1", null);
                ItemResult code2 = CodesDoMocks.mockCodeResult("code2", null);
                ItemResult code2A = CodesDoMocks.mockCodeResult("code2A", code2);
                ItemResult code2B = CodesDoMocks.mockCodeResult("code2B", code2);
                return Arrays.asList(code1, code2, code2A, code2B);
            };
        });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        codesService = applicationContext.getBean(CodesMetamacService.class);
        reset(codesService);
        codeRepository = applicationContext.getBean(CodeRepository.class);
        reset(codeRepository);
        codeMetamacRepository = applicationContext.getBean(CodeMetamacRepository.class);
        reset(codeMetamacRepository);
        mockFindCodelistsByCondition();
        mockFindCodesByCondition();
        mockFindCodesByNativeSqlQuery();
    }

    @Override
    protected String getSupathItemSchemes() {
        return "codelists";
    }

    @Override
    protected String getSupathItems() {
        return "codes";
    }
}
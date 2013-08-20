package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindCodelistFamilies;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindCodelists;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindCodes;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindVariableElements;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindVariableElementsRetrieveAll;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindVariableFamilies;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyFindVariables;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveCode;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveCodelist;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveCodelistFamily;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveVariable;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesMockitoVerify.verifyRetrieveVariableFamily;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_4_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
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
import org.siemac.metamac.rest.constants.RestConstants;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElements;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class SrmRestInternalFacadeV10CodesTest extends SrmRestInternalFacadeV10BaseTest {

    private CodesMetamacService         codesService;
    private ItemSchemeVersionRepository itemSchemeVersionRepository;
    private CodeRepository              codeRepository;
    private CodeMetamacRepository       codeMetamacRepository;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);

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
            getSrmRestInternalFacadeClientXml().findCodelists(WILDCARD_ALL, null, null, null, null);
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
        testFindCodelists(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCodelists(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCodelists(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindCodelists(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCodelists(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCodelists(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
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
            getSrmRestInternalFacadeClientXml().findCodelists(AGENCY_1, WILDCARD_ALL, null, null, null, null);
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
        String version = VERSION_1;
        Codelist codelist = getSrmRestInternalFacadeClientXml().retrieveCodelist(agencyID, resourceID, version);

        // Validation
        assertNotNull(codelist);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, codelist.getAgencyID());
        assertEquals(resourceID, codelist.getId());
        assertEquals(version, codelist.getVersion());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getKind());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelist.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveCodelist(codesService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveCodelistVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = WILDCARD_LATEST;
        Codelist codelist = getSrmRestInternalFacadeClientXml().retrieveCodelist(agencyID, resourceID, version);

        // Validation
        assertNotNull(codelist);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, codelist.getAgencyID());
        assertEquals(resourceID, codelist.getId());
        assertEquals(VERSION_1, codelist.getVersion());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getKind());
        assertEquals(RestInternalConstants.KIND_CODELIST, codelist.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CODELISTS, codelist.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveCodelist(codesService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveCodelistXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
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
        String version = VERSION_1;
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
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCodelist.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCodelistErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveCodelist(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveCodelist(AGENCY_1, WILDCARD_ALL, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
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
        testFindCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindCodes(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindCodes(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindCodes(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindCodes(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindCodes(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null); // without limits
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindCodes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindCodes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindCodes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null); // with pagination
        testFindCodes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCodes(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindCodesXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findCodes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCodesRetrieveAll() throws Exception {

        resetMocks();
        String order = "ORDER1";
        String openness = "OPENNESS1";
        Codes codes = getSrmRestInternalFacadeClientXml().findCodes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null, order, openness);

        assertNotNull(codes);
        assertEquals(RestInternalConstants.KIND_CODES, codes.getKind());
        assertEquals(BigInteger.valueOf(4), codes.getTotal());

        // Verify with mockito
        ArgumentCaptor<String> codeSchemeUrnArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ItemMetamacResultSelection> itemResultSelectionArgument = ArgumentCaptor.forClass(ItemMetamacResultSelection.class);
        ArgumentCaptor<String> orderArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> opennessArgument = ArgumentCaptor.forClass(String.class);
        verify(codesService).retrieveCodesByCodelistUrnOrderedInDepth(any(ServiceContext.class), codeSchemeUrnArgument.capture(), itemResultSelectionArgument.capture(), orderArgument.capture(),
                opennessArgument.capture());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency1:itemScheme1(01.000)", codeSchemeUrnArgument.getValue());
        assertEquals(true, itemResultSelectionArgument.getValue().isNames());
        assertEquals(false, itemResultSelectionArgument.getValue().isDescriptions());
        assertEquals(false, itemResultSelectionArgument.getValue().isComments());
        assertEquals(false, itemResultSelectionArgument.getValue().isAnnotations());
        assertEquals(order, orderArgument.getValue());
        assertEquals(openness, opennessArgument.getValue());
    }

    @Test
    public void testFindCodesRetrieveAllXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findCodesRetrieveAll.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveCode() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String codeID = ITEM_1_CODE;
        Code code = getSrmRestInternalFacadeClientXml().retrieveCode(agencyID, resourceID, version, codeID);

        // Validation
        assertNotNull(code);
        assertEquals(codeID, code.getId());
        assertEquals(RestInternalConstants.KIND_CODE, code.getKind());
        assertEquals(RestInternalConstants.KIND_CODE, code.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CODES, code.getParentLink().getKind());
        assertTrue(code instanceof Code);
        // other metadata are tested in transformation tests

        // Verify with Mockito
        verifyRetrieveCode(codesService, agencyID, resourceID, version, codeID);
    }

    @Test
    public void testRetrieveCodeXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
        String version = VERSION_1;
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
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCode.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCodeErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCode(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveCode(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveCode(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveCode(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL);
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

    @Test
    public void testFindVariableFamilies() throws Exception {
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        String limit = "4";
        String offset = "4";

        // Find
        VariableFamilies variableFamilies = getSrmRestInternalFacadeClientXml().findVariableFamilies(query, orderBy, limit, offset);

        assertNotNull(variableFamilies);
        assertEquals(RestInternalConstants.KIND_VARIABLE_FAMILIES, variableFamilies.getKind());
        // Verify with Mockito
        verifyFindVariableFamilies(codesService, null, limit, offset, query, orderBy);
    }

    @Test
    public void testFindVariableFamiliesXml() throws Exception {
        String requestUri = getUriVariableFamilies(null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findVariableFamilies.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveVariableFamily() throws Exception {
        String resourceID = ARTEFACT_1_CODE;
        VariableFamily variableFamily = getSrmRestInternalFacadeClientXml().retrieveVariableFamilyById(resourceID);

        // Validation
        assertNotNull(variableFamily);
        // other metadata are tested in mapper tests
        assertEquals(resourceID, variableFamily.getId());
        assertEquals(RestInternalConstants.KIND_VARIABLE_FAMILY, variableFamily.getKind());
        assertEquals(RestInternalConstants.KIND_VARIABLE_FAMILY, variableFamily.getSelfLink().getKind());

        // Verify with Mockito
        verifyRetrieveVariableFamily(codesService, resourceID);
    }

    @Test
    public void testRetrieveVariableFamilyXml() throws Exception {

        String requestBase = getUriVariableFamily(ARTEFACT_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveVariableFamily.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveVariableFamilyErrorNotExists() throws Exception {
        String resourceID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveVariableFamilyById(resourceID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Variable family " + resourceID + " not found", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveVariableFamilyErrorNotExistsXml() throws Exception {
        String requestUri = getUriVariableFamily(NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveVariableFamily.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testFindVariablesByFamily() throws Exception {
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        String limit = "4";
        String offset = "4";

        // Find
        String familyID = ARTEFACT_1_CODE;
        Variables variables = getSrmRestInternalFacadeClientXml().findVariablesByFamily(familyID, query, orderBy, limit, offset);

        assertNotNull(variables);
        assertEquals(RestInternalConstants.KIND_VARIABLES, variables.getKind());
        // Verify with Mockito
        verifyFindVariables(codesService, null, familyID, limit, offset, query, orderBy);
    }

    @Test
    public void testFindVariablesByFamilyXml() throws Exception {
        String familyID = ARTEFACT_1_CODE;
        String requestUri = getUriVariablesByFamily(familyID, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findVariablesByFamily.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindVariables() throws Exception {
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        String limit = "4";
        String offset = "4";

        // Find
        Variables variables = getSrmRestInternalFacadeClientXml().findVariables(query, orderBy, limit, offset);

        assertNotNull(variables);
        assertEquals(RestInternalConstants.KIND_VARIABLES, variables.getKind());
        // Verify with Mockito
        verifyFindVariables(codesService, null, null, limit, offset, query, orderBy);
    }

    @Test
    public void testFindVariablesXml() throws Exception {
        String requestUri = getUriVariables(null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findVariables.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveVariable() throws Exception {
        String resourceID = ARTEFACT_1_CODE;
        Variable variable = getSrmRestInternalFacadeClientXml().retrieveVariableById(resourceID);

        // Validation
        assertNotNull(variable);
        // other metadata are tested in mapper tests
        assertEquals(resourceID, variable.getId());
        assertEquals(RestInternalConstants.KIND_VARIABLE, variable.getKind());
        assertEquals(RestInternalConstants.KIND_VARIABLE, variable.getSelfLink().getKind());

        // Verify with Mockito
        verifyRetrieveVariable(codesService, resourceID);
    }

    @Test
    public void testRetrieveVariableXml() throws Exception {

        String requestBase = getUriVariable(ARTEFACT_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveVariable.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveVariableErrorNotExists() throws Exception {
        String resourceID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveVariableById(resourceID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.VARIABLE_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Variable " + resourceID + " not found", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveVariableErrorNotExistsXml() throws Exception {
        String requestUri = getUriVariable(NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveVariable.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testFindVariableElements() throws Exception {
        String query = QUERY_ID_LIKE_1;
        String orderBy = ORDER_BY_ID_DESC;
        String limit = "4";
        String offset = "4";
        String variableID = "variable01";
        // Find
        VariableElements variableElements = getSrmRestInternalFacadeClientXml().findVariableElements(variableID, query, orderBy, limit, offset);

        assertNotNull(variableElements);
        assertEquals(RestInternalConstants.KIND_VARIABLE_ELEMENTS, variableElements.getKind());
        // Verify with Mockito
        verifyFindVariableElements(codesService, null, variableID, limit, offset, query, orderBy);
    }

    @Test
    public void testFindVariableElementsXml() throws Exception {
        String requestUri = getUriVariableElements("variable01", null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findVariableElements.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindVariableElementsRetrieveAll() throws Exception {
        String orderBy = null;
        String limit = null;
        String offset = null;
        String variableID = "variable01";
        {
            String query = null;
            VariableElements variableElements = getSrmRestInternalFacadeClientXml().findVariableElements(variableID, query, orderBy, limit, offset);
            assertEquals(RestInternalConstants.KIND_VARIABLE_ELEMENTS, variableElements.getKind());
            verifyFindVariableElementsRetrieveAll(codesService, variableID, null);
        }
        resetMocks();
        {
            String query = "ID IN ('code1', 'code2')";
            getSrmRestInternalFacadeClientXml().findVariableElements(variableID, query, orderBy, limit, offset);
            verifyFindVariableElementsRetrieveAll(codesService, variableID, Arrays.asList("code1", "code2"));
        }
        resetMocks();
        {
            String query = "ID EQ 'code1'";
            getSrmRestInternalFacadeClientXml().findVariableElements(variableID, query, orderBy, limit, offset);
            verifyFindVariableElementsRetrieveAll(codesService, variableID, Arrays.asList("code1"));
        }
    }

    @Test
    public void testFindVariableElementsGeoJson() throws Exception {
        String requestUri = getUriVariableElementsGeo("variable01");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findVariableElementsGeoJson.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, MediaType.APPLICATION_JSON, Status.OK, responseExpected);
    }

    @Test
    public void testFindVariableElementsGeoXml() throws Exception {
        String requestUri = getUriVariableElementsGeo("variable01");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findVariableElementsGeoXml.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, MediaType.APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveVariableElementXml() throws Exception {
        String variableID = "variable01";
        String resourceID = ARTEFACT_1_CODE;
        String requestBase = getUriVariableElement(variableID, resourceID);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveVariableElement.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveVariableElementErrorNotExists() throws Exception {
        String variableID = "variable01";
        String resourceID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveVariableElementById(variableID, resourceID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.VARIABLE_ELEMENT_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Variable element " + resourceID + " not found in Variable " + variableID, exception.getMessage());
            assertEquals(2, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertEquals(variableID, exception.getParameters().getParameters().get(1));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveVariableElementErrorNotExistsXml() throws Exception {
        String requestUri = getUriVariableElement("variable01", NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveVariableElement.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testFindCodelistFamilies() throws Exception {
        resetMocks();
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        String limit = "4";
        String offset = "4";

        // Find
        CodelistFamilies codelistFamilies = getSrmRestInternalFacadeClientXml().findCodelistFamilies(query, orderBy, limit, offset);

        assertNotNull(codelistFamilies);
        assertEquals(RestInternalConstants.KIND_CODELIST_FAMILIES, codelistFamilies.getKind());
        // Verify with Mockito
        verifyFindCodelistFamilies(codesService, null, limit, offset, query, orderBy);
    }

    @Test
    public void testFindCodelistFamiliesXml() throws Exception {
        String requestUri = getUriCodelistFamilies(null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/findCodelistFamilies.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveCodelistFamily() throws Exception {
        String resourceID = ARTEFACT_1_CODE;
        CodelistFamily codelistFamily = getSrmRestInternalFacadeClientXml().retrieveCodelistFamilyById(resourceID);

        // Validation
        assertNotNull(codelistFamily);
        // other metadata are tested in mapper tests
        assertEquals(resourceID, codelistFamily.getId());
        assertEquals(RestInternalConstants.KIND_CODELIST_FAMILY, codelistFamily.getKind());
        assertEquals(RestInternalConstants.KIND_CODELIST_FAMILY, codelistFamily.getSelfLink().getKind());

        // Verify with Mockito
        verifyRetrieveCodelistFamily(codesService, resourceID);
    }

    @Test
    public void testRetrieveCodelistFamilyXml() throws Exception {

        String requestBase = getUriCodelistFamily(ARTEFACT_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCodelistFamily.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCodelistFamilyErrorNotExists() throws Exception {
        String resourceID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCodelistFamilyById(resourceID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CODELIST_FAMILY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Codelist family " + resourceID + " not found", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCodelistFamilyErrorNotExistsXml() throws Exception {
        String requestUri = getUriCodelistFamily(NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/codes/retrieveCodelistFamily.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
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
        Codes codes = getSrmRestInternalFacadeClientXml().findCodes(agencyID, resourceID, version, query, orderBy, limit, offset, null, null);

        assertNotNull(codes);
        assertEquals(RestInternalConstants.KIND_CODES, codes.getKind());

        // Verify with mockito
        verifyFindCodes(codesService, agencyID, resourceID, version, limit, offset, query, orderBy);
    }

    private void mockRetrieveItemSchemeVersionByVersion() throws MetamacException {
        when(itemSchemeVersionRepository.retrieveByVersion(any(Long.class), any(String.class))).thenAnswer(new Answer<ItemSchemeVersion>() {

            @Override
            public ItemSchemeVersion answer(InvocationOnMock invocation) throws Throwable {
                String version = (String) invocation.getArguments()[1];
                return CodesDoMocks.mockCodelist("agencyID", version, version);
            };
        });
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
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                        codelistVersion = CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
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
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
                    codelists.add(CodesDoMocks.mockCodelistWithCodes(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));
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
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_1_CODE.equals(itemID)) {
                        CodelistVersionMetamac codelist1 = CodesDoMocks.mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
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
                    CodelistVersionMetamac codelist1 = CodesDoMocks.mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
                    CodelistVersionMetamac codelist2 = CodesDoMocks.mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);

                    List<CodeMetamac> codes = new ArrayList<CodeMetamac>();
                    codes.add(CodesDoMocks.mockCode(ITEM_1_CODE, codelist1, null));
                    codes.add(CodesDoMocks.mockCode(ITEM_2_CODE, codelist1, null));
                    codes.add(CodesDoMocks.mockCode(ITEM_3_CODE, codelist1, codes.get(1)));
                    codes.add(CodesDoMocks.mockCode(ITEM_1_CODE, codelist2, null));

                    return new PagedResult<CodeMetamac>(codes, codes.size(), codes.size(), codes.size(), codes.size() * 10, 0);
                }
            };
        });
    }

    private void mockRetrieveCodesByCodelistUrnOrderedInDepth() throws MetamacException {
        when(codesService.retrieveCodesByCodelistUrnOrderedInDepth(any(ServiceContext.class), any(String.class), any(ItemMetamacResultSelection.class), any(String.class), any(String.class)))
                .thenAnswer(new Answer<List<ItemResult>>() {

                    @Override
                    public List<ItemResult> answer(InvocationOnMock invocation) throws Throwable {
                        // any
                        ItemResult code1 = CodesDoMocks.mockCodeItemResult("code1", null, 0, true);
                        ItemResult code2 = CodesDoMocks.mockCodeItemResult("code2", null, 1, false);
                        ItemResult code2A = CodesDoMocks.mockCodeItemResult("code2A", code2, 0, true);
                        ItemResult code2B = CodesDoMocks.mockCodeItemResult("code2B", code2, 1, true);
                        return Arrays.asList(code1, code2, code2A, code2B);
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindVariableFamiliesByCondition() throws MetamacException {
        when(codesService.findVariableFamiliesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                        String resourceID = getNameableArtefactCodeFromConditionalCriteria(conditions, CodeMetamacProperties.nameableArtefact());

                        if (resourceID != null) {
                            // Retrieve one
                            org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily = null;
                            if (NOT_EXISTS.equals(resourceID)) {
                                variableFamily = null;
                            } else {
                                variableFamily = CodesDoMocks.mockVariableFamily(resourceID);
                            }
                            List<org.siemac.metamac.srm.core.code.domain.VariableFamily> variableFamilies = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableFamily>();
                            if (variableFamily != null) {
                                variableFamilies.add(variableFamily);
                            }
                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily>(variableFamilies, 0, variableFamilies.size(), variableFamilies.size());
                        } else {
                            // any
                            List<org.siemac.metamac.srm.core.code.domain.VariableFamily> variableFamilies = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableFamily>();
                            variableFamilies.add(CodesDoMocks.mockVariableFamily(ARTEFACT_1_CODE));
                            variableFamilies.add(CodesDoMocks.mockVariableFamily(ARTEFACT_2_CODE));
                            variableFamilies.add(CodesDoMocks.mockVariableFamily(ARTEFACT_3_CODE));
                            variableFamilies.add(CodesDoMocks.mockVariableFamily(ARTEFACT_4_CODE));

                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily>(variableFamilies, variableFamilies.size(), variableFamilies.size(), variableFamilies.size(),
                                    variableFamilies.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindVariablesByCondition() throws MetamacException {
        when(codesService.findVariablesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<org.siemac.metamac.srm.core.code.domain.Variable>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                        String resourceID = getNameableArtefactCodeFromConditionalCriteria(conditions, CodeMetamacProperties.nameableArtefact());

                        if (resourceID != null) {
                            // Retrieve one
                            org.siemac.metamac.srm.core.code.domain.Variable variable = null;
                            if (NOT_EXISTS.equals(resourceID)) {
                                variable = null;
                            } else {
                                variable = CodesDoMocks.mockVariable(resourceID);
                            }
                            List<org.siemac.metamac.srm.core.code.domain.Variable> variables = new ArrayList<org.siemac.metamac.srm.core.code.domain.Variable>();
                            if (variable != null) {
                                variables.add(variable);
                            }
                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.Variable>(variables, 0, variables.size(), variables.size());
                        } else {
                            // any
                            List<org.siemac.metamac.srm.core.code.domain.Variable> variables = new ArrayList<org.siemac.metamac.srm.core.code.domain.Variable>();
                            variables.add(CodesDoMocks.mockVariable(ARTEFACT_1_CODE));
                            variables.add(CodesDoMocks.mockVariable(ARTEFACT_2_CODE));
                            variables.add(CodesDoMocks.mockVariable(ARTEFACT_3_CODE));
                            variables.add(CodesDoMocks.mockVariable(ARTEFACT_4_CODE));

                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.Variable>(variables, variables.size(), variables.size(), variables.size(), variables.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindVariableElementsByCondition() throws MetamacException {
        when(codesService.findVariableElementsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                        String resourceID = getIdentifiableArtefactCodeFromConditionalCriteria(conditions, VariableElementProperties.identifiableArtefact());

                        if (resourceID != null) {
                            // Retrieve one
                            org.siemac.metamac.srm.core.code.domain.VariableElement variableElement = null;
                            if (NOT_EXISTS.equals(resourceID)) {
                                variableElement = null;
                            } else {
                                variableElement = CodesDoMocks.mockVariableElement(resourceID);
                            }
                            List<org.siemac.metamac.srm.core.code.domain.VariableElement> variableElements = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableElement>();
                            if (variableElement != null) {
                                variableElements.add(variableElement);
                            }
                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement>(variableElements, 0, variableElements.size(), variableElements.size());
                        } else {
                            // any
                            List<org.siemac.metamac.srm.core.code.domain.VariableElement> variableElements = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableElement>();
                            variableElements.add(CodesDoMocks.mockVariableElement(ARTEFACT_1_CODE));
                            variableElements.add(CodesDoMocks.mockVariableElement(ARTEFACT_2_CODE));
                            variableElements.add(CodesDoMocks.mockVariableElement(ARTEFACT_3_CODE));
                            variableElements.add(CodesDoMocks.mockVariableElement(ARTEFACT_4_CODE));

                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement>(variableElements, variableElements.size(), variableElements.size(),
                                    variableElements.size(), variableElements.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindVariableElementsByVariableEfficiently() throws MetamacException {
        when(codesService.findVariableElementsByVariableEfficiently(any(ServiceContext.class), any(String.class), any(List.class), any(VariableElementResultSelection.class))).thenAnswer(
                new Answer<List<org.siemac.metamac.srm.core.code.domain.VariableElementResult>>() {

                    @Override
                    public List<org.siemac.metamac.srm.core.code.domain.VariableElementResult> answer(InvocationOnMock invocation) throws Throwable {
                        // any
                        List<org.siemac.metamac.srm.core.code.domain.VariableElementResult> variableElements = new ArrayList<org.siemac.metamac.srm.core.code.domain.VariableElementResult>();
                        variableElements.add(CodesDoMocks.mockVariableElementResult(ARTEFACT_1_CODE));
                        variableElements.add(CodesDoMocks.mockVariableElementResult(ARTEFACT_2_CODE));
                        return variableElements;
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCodelistFamiliesByCondition() throws MetamacException {
        when(codesService.findCodelistFamiliesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                        String resourceID = getNameableArtefactCodeFromConditionalCriteria(conditions, CodeMetamacProperties.nameableArtefact());

                        if (resourceID != null) {
                            // Retrieve one
                            org.siemac.metamac.srm.core.code.domain.CodelistFamily codelistFamily = null;
                            if (NOT_EXISTS.equals(resourceID)) {
                                codelistFamily = null;
                            } else {
                                codelistFamily = CodesDoMocks.mockCodelistFamily(resourceID);
                            }
                            List<org.siemac.metamac.srm.core.code.domain.CodelistFamily> codelistFamilies = new ArrayList<org.siemac.metamac.srm.core.code.domain.CodelistFamily>();
                            if (codelistFamily != null) {
                                codelistFamilies.add(codelistFamily);
                            }
                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily>(codelistFamilies, 0, codelistFamilies.size(), codelistFamilies.size());
                        } else {
                            // any
                            List<org.siemac.metamac.srm.core.code.domain.CodelistFamily> codelistFamilies = new ArrayList<org.siemac.metamac.srm.core.code.domain.CodelistFamily>();
                            codelistFamilies.add(CodesDoMocks.mockCodelistFamily(ARTEFACT_1_CODE));
                            codelistFamilies.add(CodesDoMocks.mockCodelistFamily(ARTEFACT_2_CODE));
                            codelistFamilies.add(CodesDoMocks.mockCodelistFamily(ARTEFACT_3_CODE));
                            codelistFamilies.add(CodesDoMocks.mockCodelistFamily(ARTEFACT_4_CODE));

                            return new PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily>(codelistFamilies, codelistFamilies.size(), codelistFamilies.size(), codelistFamilies.size(),
                                    codelistFamilies.size() * 10, 0);
                        }
                    };
                });
    }

    private String getUriVariableFamilies(String resourceID, String query, String limit, String offset) throws Exception {
        String uri = baseApi + "/" + "variablefamilies";
        if (resourceID != null) {
            uri += "/" + resourceID;
        }
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriVariableFamily(String resourceID) throws Exception {
        return getUriVariableFamilies(resourceID, null, null, null);
    }

    private String getUriVariablesByFamily(String familyID, String query, String limit, String offset) throws Exception {
        String uri = baseApi + "/" + "variablefamilies/" + familyID + "/variables";
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriVariables(String resourceID, String query, String limit, String offset) throws Exception {
        String uri = baseApi + "/" + "variables";
        if (resourceID != null) {
            uri += "/" + resourceID;
        }
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriVariable(String resourceID) throws Exception {
        return getUriVariables(resourceID, null, null, null);
    }

    private String getUriVariableElements(String variableID, String resourceID, String query, String limit, String offset) throws Exception {
        String uri = baseApi + "/" + "variables/" + variableID + "/variableelements";
        if (resourceID != null) {
            uri += "/" + resourceID;
        }
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriVariableElementsGeo(String variableID) throws Exception {
        return baseApi + "/" + "variables/" + variableID + "/variableelements/~all/geoinfo";
    }

    private String getUriVariableElement(String variableID, String resourceID) throws Exception {
        return getUriVariableElements(variableID, resourceID, null, null, null);
    }

    private String getUriCodelistFamilies(String resourceID, String query, String limit, String offset) throws Exception {
        String uri = baseApi + "/" + "codelistfamilies";
        if (resourceID != null) {
            uri += "/" + resourceID;
        }
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    private String getUriCodelistFamily(String resourceID) throws Exception {
        return getUriCodelistFamilies(resourceID, null, null, null);
    }

    @Override
    protected void resetMocks() throws MetamacException {
        codesService = applicationContext.getBean(CodesMetamacService.class);
        reset(codesService);
        itemSchemeVersionRepository = applicationContext.getBean(ItemSchemeVersionRepository.class);
        reset(itemSchemeVersionRepository);
        codeRepository = applicationContext.getBean(CodeRepository.class);
        reset(codeRepository);
        codeMetamacRepository = applicationContext.getBean(CodeMetamacRepository.class);
        reset(codeMetamacRepository);
        mockRetrieveItemSchemeVersionByVersion();
        mockFindCodelistsByCondition();
        mockFindCodesByCondition();
        mockRetrieveCodesByCodelistUrnOrderedInDepth();
        mockFindVariableFamiliesByCondition();
        mockFindVariablesByCondition();
        mockFindVariableElementsByCondition();
        mockFindVariableElementsByVariableEfficiently();
        mockFindCodelistFamiliesByCondition();
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "codelists";
    }

    @Override
    protected String getSupathItems() {
        return "codes";
    }
}
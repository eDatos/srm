package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;

import java.io.InputStream;
import java.math.BigInteger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest extends SrmRestInternalFacadeV10OrganisationsTest {

    @Override
    protected void resetMocks() throws MetamacException {
        super.resetMocks();
        mockRetrieveOrganisationsByOrganisationScheme(OrganisationTypeEnum.AGENCY);
    }

    @Test
    public void testJsonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindAgenciesSchemes() throws Exception {
        testFindAgencySchemes(null, null, null, null, null, null, null); // without limits
        testFindAgencySchemes(null, null, null, "10000", null, null, null); // without limits
        testFindAgencySchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindAgencySchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindAgencySchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindAgencySchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindAgencySchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindAgencySchemes(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindAgencySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindAgencySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindAgenciesSchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest.class.getResourceAsStream("/responses/organisations/findAgencySchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindAgenciesSchemesByAgency() throws Exception {
        testFindAgencySchemes(AGENCY_1, null, null, null, null, null, null);
        testFindAgencySchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindAgencySchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindAgencySchemes(AGENCY_1, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindAgencySchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindAgencySchemesByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        AgencySchemes agencySchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/agencyschemes/" + agencyID + "?limit=4&offset=4", agencySchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/agencyschemes/" + agencyID + "?limit=4&offset=0", agencySchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/agencyschemes/" + agencyID + "?limit=4&offset=0", agencySchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/agencyschemes/" + agencyID + "?limit=4&offset=36", agencySchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, agencySchemes.getKind());
    }

    @Test
    public void testFindAgencySchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findAgencySchemes(WILDCARD_ALL, null, null, null, null);
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
    public void testFindAgenciesSchemesByAgencyAndResource() throws Exception {
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindAgencySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindAgencySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindAgencySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindAgencySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindAgencySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindAgencySchemesByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        AgencySchemes agencySchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(agencyID, resourceID, null, null, "4", "0");
        assertEquals(getApiEndpoint() + "/agencyschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", agencySchemes.getSelfLink());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, agencySchemes.getKind());
    }

    @Test
    public void testFindAgencySchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findAgencySchemes(AGENCY_1, WILDCARD_ALL, null, null, null, null);
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
    public void testRetrieveAgencyScheme() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        AgencyScheme agencyScheme = getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(agencyScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, agencyScheme.getAgencyID());
        assertEquals(resourceID, agencyScheme.getId());
        assertEquals(version, agencyScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, agencyScheme.getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, agencyScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, agencyScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveAgencySchemeVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = WILDCARD_LATEST;
        AgencyScheme agencyScheme = getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(agencyScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, agencyScheme.getAgencyID());
        assertEquals(resourceID, agencyScheme.getId());
        assertEquals(VERSION_1, agencyScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, agencyScheme.getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, agencyScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, agencyScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveAgencySchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest.class.getResourceAsStream("/responses/organisations/retrieveAgencyScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveAgencySchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.AGENCY_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("AgencyScheme " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveAgencySchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest.class.getResourceAsStream("/responses/organisations/retrieveAgencyScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveAgencySchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(AGENCY_1, WILDCARD_ALL, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
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
    public void testFindOrganisations() throws Exception {

        // without parameters
        testFindAgencies(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindAgencies(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindAgencies(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindAgencies(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindAgencies(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindAgencies(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindAgencies(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindAgencies(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindAgencies(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindAgencies(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null); // without limits
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindAgencies(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindAgencies(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null); // with pagination
        testFindAgencies(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindAgenciesXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest.class.getResourceAsStream("/responses/organisations/findAgencies.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindAgenciesRetrieveAll() throws Exception {

        resetMocks();
        Agencies agencies = getSrmRestInternalFacadeClientXml().findAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);

        assertNotNull(agencies);
        assertEquals(SrmRestConstants.KIND_AGENCIES, agencies.getKind());
        assertEquals(BigInteger.valueOf(4), agencies.getTotal());

        // Verify with mockito
        ArgumentCaptor<String> organisationSchemeUrnArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ItemResultSelection> itemResultSelectionArgument = ArgumentCaptor.forClass(ItemResultSelection.class);
        verify(organisationsService).retrieveOrganisationsByOrganisationSchemeUrnUnordered(any(ServiceContext.class), organisationSchemeUrnArgument.capture(), itemResultSelectionArgument.capture());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=agency1:itemScheme1(01.000)", organisationSchemeUrnArgument.getValue());
        assertEquals(true, itemResultSelectionArgument.getValue().isNames());
        assertEquals(false, itemResultSelectionArgument.getValue().isDescriptions());
        assertEquals(false, itemResultSelectionArgument.getValue().isComments());
        assertEquals(false, itemResultSelectionArgument.getValue().isAnnotations());
    }

    @Test
    public void testFindAgenciesRetrieveAllXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findAgenciesRetrieveAll.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveAgency() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String organsationID = ITEM_1_CODE;
        Agency agency = getSrmRestInternalFacadeClientXml().retrieveAgency(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(agency);
        assertEquals(organsationID, agency.getId());
        assertEquals(SrmRestConstants.KIND_AGENCY, agency.getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY, agency.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_AGENCIES, agency.getParentLink().getKind());
        assertTrue(agency instanceof Agency);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveAgencyXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest.class.getResourceAsStream("/responses/organisations/retrieveAgency.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveAgencyErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String organisationID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveAgency(agencyID, resourceID, version, organisationID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.AGENCY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Agency " + organisationID + " not found in version " + version + " of AgencyScheme " + resourceID + " from Agency " + agencyID, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(organisationID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(agencyID, exception.getParameters().getParameters().get(3));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveAgencyErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeAgenciesTest.class.getResourceAsStream("/responses/organisations/retrieveAgency.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveAgencyErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveAgency(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveAgency(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveAgency(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveAgency(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter organisationID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_ORGANISATION_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    private void testFindAgencySchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        AgencySchemes itemSchemes = null;
        if (agencyID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(agencyID, query, orderBy, limit, offset);
        } else {
            itemSchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(itemSchemes);
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, itemSchemes.getKind());
    }

    private void testFindAgencies(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Agencies items = getSrmRestInternalFacadeClientXml().findAgencies(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(items);
        assertEquals(SrmRestConstants.KIND_AGENCIES, items.getKind());
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return SrmRestConstants.LINK_SUBPATH_AGENCY_SCHEMES;
    }

    @Override
    protected String getSupathItems() {
        return SrmRestConstants.LINK_SUBPATH_AGENCIES;
    }
}
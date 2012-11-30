package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindDataConsumerSchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindDataConsumers;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumers;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataConsumerType;

public class SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest extends SrmRestInternalFacadeV10OrganisationsTest {

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindDataConsumerSchemes() throws Exception {
        testFindDataConsumerSchemes(null, null, null, null, null, null, null); // without limits
        testFindDataConsumerSchemes(null, null, null, "10000", null, null, null); // without limits
        testFindDataConsumerSchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindDataConsumerSchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindDataConsumerSchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindDataConsumerSchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindDataConsumerSchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindDataConsumerSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindDataConsumerSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindDataConsumerSchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/findDataConsumerSchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindDataConsumerSchemesByAgency() throws Exception {
        testFindDataConsumerSchemes(AGENCY_1, null, null, null, null, null, null);
        testFindDataConsumerSchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindDataConsumerSchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindDataConsumerSchemes(AGENCY_1, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindDataConsumerSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindDataConsumerSchemesByAgencyXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/findDataConsumerSchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindDataConsumerSchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findDataConsumerSchemes(WILDCARD, null, null, null, null);
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
    public void testFindDataConsumerSchemesByAgencyAndResource() throws Exception {
        testFindDataConsumerSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindDataConsumerSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindDataConsumerSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindDataConsumerSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindDataConsumerSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindDataConsumerSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindDataConsumerSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindDataConsumerSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindDataConsumerSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindDataConsumerSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindDataConsumerSchemesByAgencyAndResourceXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/findDataConsumerSchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindDataConsumerSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findDataConsumerSchemes(AGENCY_1, WILDCARD, null, null, null, null);
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
    public void testRetrieveDataConsumerScheme() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        DataConsumerScheme dataConsumerScheme = getSrmRestInternalFacadeClientXml().retrieveDataConsumerScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(dataConsumerScheme);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, dataConsumerScheme.getAgencyID());
        assertEquals(resourceID, dataConsumerScheme.getId());
        assertEquals(version, dataConsumerScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMER_SCHEME, dataConsumerScheme.getKind());
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMER_SCHEME, dataConsumerScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMER_SCHEMES, dataConsumerScheme.getParentLink().getKind());
        assertTrue(dataConsumerScheme.getDataConsumers().get(0) instanceof DataConsumerType);
        assertFalse(dataConsumerScheme.getDataConsumers().get(0) instanceof DataConsumer);
    }

    @Test
    public void testRetrieveDataConsumerSchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, null, null, null);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/retrieveDataConsumerScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveDataConsumerSchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = ITEM_SCHEME_1_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataConsumerScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());
            
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.DATA_CONSUMER_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("DataConsumerScheme not found in agencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
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
    public void testRetrieveDataConsumerSchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/retrieveDataConsumerScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveDataConsumerSchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataConsumerScheme(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveDataConsumerScheme(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveDataConsumerScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
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
    public void testFindOrganisations() throws Exception {

        // without parameters
        testFindDataConsumers(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindDataConsumers(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindDataConsumers(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindDataConsumers(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindDataConsumers(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindDataConsumers(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindDataConsumers(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindDataConsumers(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindDataConsumers(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindDataConsumers(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindDataConsumers(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindDataConsumers(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindDataConsumers(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindDataConsumers(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindDataConsumers(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindDataConsumers(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindDataConsumers(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindDataConsumers(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindDataConsumers(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
        testFindDataConsumers(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindDataConsumers(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindDataConsumersXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/findDataConsumers.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveDataConsumer() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String organsationID = ITEM_1_CODE;
        DataConsumer dataConsumer = getSrmRestInternalFacadeClientXml().retrieveDataConsumer(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(dataConsumer);
        assertEquals(organsationID, dataConsumer.getId());
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMER, dataConsumer.getKind());
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMER, dataConsumer.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMERS, dataConsumer.getParentLink().getKind());
        assertTrue(dataConsumer instanceof DataConsumerType);
        assertTrue(dataConsumer instanceof DataConsumer);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveDataConsumerXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/retrieveDataConsumer.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveDataConsumerErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String organisationID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataConsumer(agencyID, resourceID, version, organisationID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());
            
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.DATA_CONSUMER_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("DataConsumer not found with ID " + organisationID + " in DataConsumerScheme in agencyID " + agencyID + " with ID " + resourceID + " and version " + version,
                    exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(organisationID, exception.getParameters().getParameters().get(0));
            assertEquals(agencyID, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(version, exception.getParameters().getParameters().get(3));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveDataConsumerErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeDataConsumersTest.class.getResourceAsStream("/responses/organisations/retrieveDataConsumer.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveDataConsumerErrorWildcard() throws Exception {

     // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataConsumer(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveDataConsumer(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveDataConsumer(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveDataConsumer(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, WILDCARD);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter organisationID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_ORGANISATION_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    private void testFindDataConsumerSchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        DataConsumerSchemes itemSchemes = null;
        if (agencyID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findDataConsumerSchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findDataConsumerSchemes(agencyID, query, orderBy, limit, offset);
        } else {
            itemSchemes = getSrmRestInternalFacadeClientXml().findDataConsumerSchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        // Verify with Mockito
        verifyFindDataConsumerSchemes(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, itemSchemes);
    }

    private void testFindDataConsumers(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        DataConsumers items = getSrmRestInternalFacadeClientXml().findDataConsumers(agencyID, resourceID, version, query, orderBy, limit, offset);
        verifyFindDataConsumers(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, items);
    }

    @Override
    protected String getSupathItemSchemes() {
        return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES;
    }

    @Override
    protected String getSupathItems() {
        return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMERS;
    }
}
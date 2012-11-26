package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindOrganisationUnitSchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindOrganisationUnits;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitType;

public class SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest extends SrmRestInternalFacadeV10OrganisationsTest {

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindOrganisationUnitSchemes() throws Exception {
        testFindOrganisationUnitSchemes(null, null, null, null, null, null, null); // without limits
        testFindOrganisationUnitSchemes(null, null, null, "10000", null, null, null); // without limits
        testFindOrganisationUnitSchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindOrganisationUnitSchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindOrganisationUnitSchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindOrganisationUnitSchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindOrganisationUnitSchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindOrganisationUnitSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindOrganisationUnitSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindOrganisationUnitSchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/findOrganisationUnitSchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgency() throws Exception {
        testFindOrganisationUnitSchemes(AGENCY_1, null, null, null, null, null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationUnitSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/findOrganisationUnitSchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyErrorWildcard() throws Exception {
        String requestUri = getUriItemSchemes(WILDCARD, null, null, null, "4", "4");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyAndResource() throws Exception {
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindOrganisationUnitSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationUnitSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationUnitSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationUnitSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationUnitSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyAndResourceXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/findOrganisationUnitSchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, WILDCARD, null, null, "4", null);
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testRetrieveOrganisationUnitScheme() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        OrganisationUnitScheme organisationUnitScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationUnitScheme);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, organisationUnitScheme.getAgencyID());
        assertEquals(resourceID, organisationUnitScheme.getId());
        assertEquals(version, organisationUnitScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationUnitScheme.getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationUnitScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEMES, organisationUnitScheme.getParentLink().getKind());
        assertTrue(organisationUnitScheme.getOrganisationUnits().get(0) instanceof OrganisationUnitType);
        assertFalse(organisationUnitScheme.getOrganisationUnits().get(0) instanceof OrganisationUnit);
    }

    @Test
    public void testRetrieveOrganisationUnitSchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, null, null, null);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationUnitScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveOrganisationUnitSchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = ITEM_SCHEME_1_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.ORGANISATION_UNIT_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("OrganisationUnitScheme not found in agencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
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
    public void testRetrieveOrganisationUnitSchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationUnitScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationUnitSchemeErrorWildcard() throws Exception {
        {
            String requestUri = getUriItemSchemes(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, null, null, null);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItemSchemes(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, null, null, null);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testFindOrganisations() throws Exception {

        // without parameters
        testFindOrganisationUnits(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindOrganisationUnits(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindOrganisationUnits(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindOrganisationUnits(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindOrganisationUnits(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindOrganisationUnits(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindOrganisationUnits(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindOrganisationUnits(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindOrganisationUnits(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindOrganisationUnitsXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/findOrganisationUnits.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationUnit() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String organsationID = ITEM_1_CODE;
        OrganisationUnit organisationUnit = getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(organisationUnit);
        assertEquals(organsationID, organisationUnit.getId());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT, organisationUnit.getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT, organisationUnit.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNITS, organisationUnit.getParentLink().getKind());
        assertTrue(organisationUnit instanceof OrganisationUnitType);
        assertTrue(organisationUnit instanceof OrganisationUnit);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveOrganisationUnitXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationUnit.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveOrganisationUnitErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String organisationID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(agencyID, resourceID, version, organisationID);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.ORGANISATION_UNIT_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("OrganisationUnit not found with ID " + organisationID + " in OrganisationUnitScheme in agencyID " + agencyID + " with ID " + resourceID + " and version " + version,
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
    public void testRetrieveOrganisationUnitErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationUnit.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationUnitErrorWildcard() throws Exception {
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

    private void testFindOrganisationUnitSchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        OrganisationUnitSchemes itemSchemes = null;
        if (agencyID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(agencyID, query, orderBy, limit, offset);
        } else {
            itemSchemes = getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        // Verify with Mockito
        verifyFindOrganisationUnitSchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, itemSchemes);
    }

    private void testFindOrganisationUnits(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        OrganisationUnits items = getSrmRestInternalFacadeClientXml().findOrganisationUnits(agencyID, resourceID, version, query, orderBy, limit, offset);
        verifyFindOrganisationUnits(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, items);
    }

    @Override
    protected String getSupathItemSchemes() {
        return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES;
    }

    @Override
    protected String getSupathItems() {
        return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNITS;
    }
}
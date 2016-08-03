package org.siemac.metamac.srm.rest.internal.v1_0.service;

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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

public class SrmRestInternalFacadeV10OrganisationsNoTypeTest extends SrmRestInternalFacadeV10OrganisationsTest {

    @Test
    public void testJsonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindOrganisationSchemes() throws Exception {
        testFindOrganisationSchemes(null, null, null, null, null, null, null); // without limits
        testFindOrganisationSchemes(null, null, null, "10000", null, null, null); // without limits
        testFindOrganisationSchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindOrganisationSchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindOrganisationSchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindOrganisationSchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindOrganisationSchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindOrganisationSchemes(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindOrganisationSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindOrganisationSchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindOrganisationSchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/findOrganisationSchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationSchemesByAgency() throws Exception {
        testFindOrganisationSchemes(AGENCY_1, null, null, null, null, null, null);
        testFindOrganisationSchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindOrganisationSchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindOrganisationSchemes(AGENCY_1, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindOrganisationSchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findOrganisationSchemes(WILDCARD_ALL, null, null, null, null);
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
    public void testFindOrganisationSchemesByAgencyAndResource() throws Exception {
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindOrganisationSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindOrganisationSchemesByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        OrganisationSchemes organisationSchemes = getSrmRestInternalFacadeClientXml().findOrganisationSchemes(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "?limit=4&offset=4", organisationSchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "?limit=4&offset=0", organisationSchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "?limit=4&offset=0", organisationSchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "?limit=4&offset=36", organisationSchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_SCHEMES, organisationSchemes.getKind());
    }

    @Test
    public void testFindOrganisationSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findOrganisationSchemes(AGENCY_1, WILDCARD_ALL, null, null, null, null);
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
    public void testRetrieveOrganisationSchemeTypeAgency() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE;
        String version = VERSION_1;
        OrganisationScheme organisationScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationScheme);
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, organisationScheme.getKind());
        assertTrue(organisationScheme instanceof AgencyScheme);

        // other metadata are tested in mapper tests
        assertEquals(agencyID, organisationScheme.getAgencyID());
        assertEquals(resourceID, organisationScheme.getId());
        assertEquals(version, organisationScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, organisationScheme.getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, organisationScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, organisationScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveOrganisationSchemeTypeAgencyVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE;
        String version = WILDCARD_LATEST;
        OrganisationScheme organisationScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationScheme);
    }

    @Test
    public void testRetrieveOrganisationSchemeTypeOrganisationUnit() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE;
        String version = VERSION_1;
        OrganisationScheme organisationScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationScheme);
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationScheme.getKind());
        assertTrue(organisationScheme instanceof OrganisationUnitScheme);

        // other metadata are tested in mapper tests
        assertEquals(agencyID, organisationScheme.getAgencyID());
        assertEquals(resourceID, organisationScheme.getId());
        assertEquals(version, organisationScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationScheme.getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, organisationScheme.getParentLink().getKind());
    }

    @Test
    public void testFindOrganisationSchemesByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        OrganisationSchemes organisationSchemes = getSrmRestInternalFacadeClientXml().findOrganisationSchemes(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=4", organisationSchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", organisationSchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", organisationSchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/organisationschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=36", organisationSchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_SCHEMES, organisationSchemes.getKind());
    }

    @Test
    public void testRetrieveOrganisationSchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("OrganisationScheme " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveOrganisationSchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationSchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(AGENCY_1, WILDCARD_ALL, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
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
        testFindOrganisations(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindOrganisations(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindOrganisations(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindOrganisations(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindOrganisations(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null); // without limits
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindOrganisations(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindOrganisations(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null); // with pagination
        testFindOrganisations(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindOrganisationsXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/findOrganisations.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationTypeAgency() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String organsationID = ITEM_ORGANISATION_TYPE_AGENCY_1_CODE;
        Organisation organisation = getSrmRestInternalFacadeClientXml().retrieveOrganisation(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(organisation);
        assertEquals(SrmRestConstants.KIND_AGENCY, organisation.getKind());
        assertTrue(organisation instanceof Agency);

        assertEquals(organsationID, organisation.getId());
        assertEquals(SrmRestConstants.KIND_AGENCY, organisation.getKind());
        assertEquals(SrmRestConstants.KIND_AGENCY, organisation.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_AGENCIES, organisation.getParentLink().getKind());
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveOrganisationTypeOrganisationUnit() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String organsationID = ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE;
        Organisation organisation = getSrmRestInternalFacadeClientXml().retrieveOrganisation(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(organisation);
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, organisation.getKind());
        assertTrue(organisation instanceof OrganisationUnit);

        assertEquals(organsationID, organisation.getId());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, organisation.getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, organisation.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, organisation.getParentLink().getKind());
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveOrganisationErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String organisationID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(agencyID, resourceID, version, organisationID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.ORGANISATION_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Organisation " + organisationID + " not found in version " + version + " of OrganisationScheme " + resourceID + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveOrganisationErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisation.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL);
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

    private void testFindOrganisationSchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        OrganisationSchemes itemSchemes = null;
        if (agencyID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findOrganisationSchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findOrganisationSchemes(agencyID, query, orderBy, limit, offset);
        } else {
            itemSchemes = getSrmRestInternalFacadeClientXml().findOrganisationSchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(itemSchemes);
        assertEquals(SrmRestConstants.KIND_ORGANISATION_SCHEMES, itemSchemes.getKind());
    }

    private void testFindOrganisations(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Organisations items = getSrmRestInternalFacadeClientXml().findOrganisations(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(items);
        assertEquals(SrmRestConstants.KIND_ORGANISATIONS, items.getKind());
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return SrmRestConstants.LINK_SUBPATH_ORGANISATION_SCHEMES;
    }

    @Override
    protected String getSupathItems() {
        return SrmRestConstants.LINK_SUBPATH_ORGANISATIONS;
    }
}
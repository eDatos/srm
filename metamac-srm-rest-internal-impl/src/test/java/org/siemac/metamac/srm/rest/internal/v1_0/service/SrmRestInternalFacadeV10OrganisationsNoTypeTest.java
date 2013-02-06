package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.LATEST;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindOrganisationSchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindOrganisations;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyRetrieveOrganisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyRetrieveOrganisationScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;

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
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencyType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitType;

public class SrmRestInternalFacadeV10OrganisationsNoTypeTest extends SrmRestInternalFacadeV10OrganisationsTest {

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
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
    public void testFindOrganisationSchemesByAgencyXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/findOrganisationSchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationSchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findOrganisationSchemes(WILDCARD, null, null, null, null);
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
    public void testFindOrganisationSchemesByAgencyAndResource() throws Exception {
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindOrganisationSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindOrganisationSchemesByAgencyAndResourceXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/findOrganisationSchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findOrganisationSchemes(AGENCY_1, WILDCARD, null, null, null, null);
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
    public void testRetrieveOrganisationSchemeTypeAgency() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        OrganisationScheme organisationScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationScheme);
        assertEquals(RestInternalConstants.KIND_ORGANISATION_SCHEME, organisationScheme.getKind());
        assertNotNull(organisationScheme.getAgencyScheme());
        assertNull(organisationScheme.getOrganisationUnitScheme());
        assertNull(organisationScheme.getDataConsumerScheme());
        assertNull(organisationScheme.getDataProviderScheme());

        AgencyScheme itemScheme = organisationScheme.getAgencyScheme();
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, itemScheme.getAgencyID());
        assertEquals(resourceID, itemScheme.getId());
        assertEquals(version, itemScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, itemScheme.getKind());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, itemScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEMES, itemScheme.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveOrganisationScheme(organisationsService, agencyID, resourceID, version, null);
    }

    @Test
    public void testRetrieveOrganisationSchemeTypeAgencyVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE;
        String version = LATEST;
        OrganisationScheme organisationScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationScheme);
        // Verify with Mockito
        verifyRetrieveOrganisationScheme(organisationsService, agencyID, resourceID, version, null);
    }

    @Test
    public void testRetrieveOrganisationSchemeTypeOrganisationUnit() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        OrganisationScheme organisationScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationScheme);
        assertEquals(RestInternalConstants.KIND_ORGANISATION_SCHEME, organisationScheme.getKind());
        assertNotNull(organisationScheme.getOrganisationUnitScheme());
        assertNull(organisationScheme.getAgencyScheme());
        assertNull(organisationScheme.getDataConsumerScheme());
        assertNull(organisationScheme.getDataProviderScheme());

        OrganisationUnitScheme itemScheme = organisationScheme.getOrganisationUnitScheme();
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, itemScheme.getAgencyID());
        assertEquals(resourceID, itemScheme.getId());
        assertEquals(version, itemScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME, itemScheme.getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME, itemScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEMES, itemScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveOrganisationSchemeTypeAgencyXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE, ITEM_SCHEME_VERSION_1, null, null, null);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationScheme.typeAgency.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveOrganisationSchemeTypeOrganisationUnitXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE, ITEM_SCHEME_VERSION_1, null, null, null);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class
                    .getResourceAsStream("/responses/organisations/retrieveOrganisationScheme.typeOrganisationUnit.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveOrganisationSchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = ITEM_SCHEME_VERSION_1;
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
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationSchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(AGENCY_1, WILDCARD, ITEM_SCHEME_VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
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
        testFindOrganisations(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindOrganisations(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindOrganisations(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindOrganisations(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindOrganisations(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindOrganisations(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindOrganisations(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindOrganisations(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindOrganisations(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindOrganisations(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindOrganisations(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindOrganisations(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, "2", "0", null, null); // with pagination
        testFindOrganisations(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisations(AGENCY_1, WILDCARD, ITEM_SCHEME_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindOrganisationsXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/findOrganisations.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationTypeAgency() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        String organsationID = ITEM_ORGANISATION_TYPE_AGENCY_1_CODE;
        Organisation organisation = getSrmRestInternalFacadeClientXml().retrieveOrganisation(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(organisation);
        assertEquals(RestInternalConstants.KIND_ORGANISATION, organisation.getKind());
        assertNotNull(organisation.getAgency());
        assertNull(organisation.getOrganisationUnit());
        assertNull(organisation.getDataConsumer());
        assertNull(organisation.getDataProvider());

        Agency item = organisation.getAgency();
        assertEquals(organsationID, item.getId());
        assertEquals(RestInternalConstants.KIND_AGENCY, item.getKind());
        assertEquals(RestInternalConstants.KIND_AGENCY, item.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_AGENCIES, item.getParentLink().getKind());
        assertTrue(item instanceof AgencyType);
        assertTrue(item instanceof Agency);
        // other metadata are tested in transformation tests

        // Verify with Mockito
        verifyRetrieveOrganisation(organisationsService, agencyID, resourceID, version, organsationID, null);
    }

    @Test
    public void testRetrieveOrganisationTypeOrganisationUnit() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
        String organsationID = ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE;
        Organisation organisation = getSrmRestInternalFacadeClientXml().retrieveOrganisation(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(organisation);
        assertEquals(RestInternalConstants.KIND_ORGANISATION, organisation.getKind());
        assertNotNull(organisation.getOrganisationUnit());
        assertNull(organisation.getAgency());
        assertNull(organisation.getDataConsumer());
        assertNull(organisation.getDataProvider());

        OrganisationUnit item = organisation.getOrganisationUnit();
        assertEquals(organsationID, item.getId());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT, item.getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT, item.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNITS, item.getParentLink().getKind());
        assertTrue(item instanceof OrganisationUnitType);
        assertTrue(item instanceof OrganisationUnit);
        // other metadata are tested in transformation tests

        // Verify with Mockito
        verifyRetrieveOrganisation(organisationsService, agencyID, resourceID, version, organsationID, null);
    }

    @Test
    public void testRetrieveOrganisationTypeAgencyXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, ITEM_ORGANISATION_TYPE_AGENCY_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisation.typeAgency.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveOrganisationTypeOrganisationUnitXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisation.typeOrganisationUnit.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveOrganisationErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_VERSION_1;
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
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsNoTypeTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisation.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(AGENCY_1, WILDCARD, ITEM_SCHEME_VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisation(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1, WILDCARD);
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
        assertEquals(RestInternalConstants.KIND_ORGANISATION_SCHEMES, itemSchemes.getKind());

        // Verify with Mockito
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, null);
    }

    private void testFindOrganisations(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Organisations items = getSrmRestInternalFacadeClientXml().findOrganisations(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(items);
        assertEquals(RestInternalConstants.KIND_ORGANISATIONS, items.getKind());

        // Verify with Mockito
        verifyFindOrganisations(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, null);
    }

    @Override
    protected String getSupathItemSchemes() {
        return RestInternalConstants.LINK_SUBPATH_ORGANISATION_SCHEMES;
    }

    @Override
    protected String getSupathItems() {
        return RestInternalConstants.LINK_SUBPATH_ORGANISATIONS;
    }
}
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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest extends SrmRestInternalFacadeV10OrganisationsTest {

    @Override
    protected void resetMocks() throws MetamacException {
        super.resetMocks();
        mockRetrieveOrganisationsByOrganisationScheme(OrganisationTypeEnum.ORGANISATION_UNIT);
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
    public void testFindOrganisationUnitSchemes() throws Exception {
        testFindOrganisationUnitSchemes(null, null, null, null, null, null, null); // without limits
        testFindOrganisationUnitSchemes(null, null, null, "10000", null, null, null); // without limits
        testFindOrganisationUnitSchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindOrganisationUnitSchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindOrganisationUnitSchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindOrganisationUnitSchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindOrganisationUnitSchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindOrganisationUnitSchemes(null, null, null, null, null, QUERY_LATEST, null); // latest
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
    public void testFindOrganisationUnitSchemesByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        OrganisationUnitSchemes organisationUnitSchemes = getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "?limit=4&offset=4", organisationUnitSchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "?limit=4&offset=0", organisationUnitSchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "?limit=4&offset=0", organisationUnitSchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "?limit=4&offset=36", organisationUnitSchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, organisationUnitSchemes.getKind());
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(WILDCARD_ALL, null, null, null, null);
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
    public void testFindOrganisationUnitSchemesByAgencyAndResource() throws Exception {
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationUnitSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindOrganisationUnitSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindOrganisationUnitSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindOrganisationUnitSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindOrganisationUnitSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindOrganisationUnitSchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        OrganisationUnitSchemes organisationUnitSchemes = getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=4", organisationUnitSchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", organisationUnitSchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", organisationUnitSchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/organisationunitschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=36", organisationUnitSchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, organisationUnitSchemes.getKind());
    }

    @Test
    public void testFindOrganisationUnitSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findOrganisationUnitSchemes(AGENCY_1, WILDCARD_ALL, null, null, null, null);
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
    public void testRetrieveOrganisationUnitScheme() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        OrganisationUnitScheme organisationUnitScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationUnitScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, organisationUnitScheme.getAgencyID());
        assertEquals(resourceID, organisationUnitScheme.getId());
        assertEquals(version, organisationUnitScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationUnitScheme.getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, organisationUnitScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, organisationUnitScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveOrganisationUnitSchemeVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = WILDCARD_LATEST;
        OrganisationUnitScheme organisationUnitScheme = getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(organisationUnitScheme);
    }

    @Test
    public void testRetrieveOrganisationUnitSchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null);
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
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.ORGANISATION_UNIT_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("OrganisationUnitScheme " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveOrganisationUnitSchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationUnitScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationUnitSchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(AGENCY_1, WILDCARD_ALL, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnitScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
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
        testFindOrganisationUnits(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindOrganisationUnits(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindOrganisationUnits(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindOrganisationUnits(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null); // without limits
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindOrganisationUnits(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null); // with pagination
        testFindOrganisationUnits(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindOrganisationUnits(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindOrganisationUnitsXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/findOrganisationUnits.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindOrganisationUnitsRetrieveAll() throws Exception {

        resetMocks();
        OrganisationUnits organisationUnits = getSrmRestInternalFacadeClientXml().findOrganisationUnits(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);

        assertNotNull(organisationUnits);
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, organisationUnits.getKind());
        assertEquals(BigInteger.valueOf(4), organisationUnits.getTotal());

        // Verify with mockito
        ArgumentCaptor<String> organisationSchemeUrnArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ItemResultSelection> itemResultSelectionArgument = ArgumentCaptor.forClass(ItemResultSelection.class);
        verify(organisationsService).retrieveOrganisationsByOrganisationSchemeUrnUnordered(any(ServiceContext.class), organisationSchemeUrnArgument.capture(), itemResultSelectionArgument.capture());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=agency1:itemScheme1(01.000)", organisationSchemeUrnArgument.getValue());
        assertEquals(true, itemResultSelectionArgument.getValue().isNames());
        assertEquals(false, itemResultSelectionArgument.getValue().isDescriptions());
        assertEquals(false, itemResultSelectionArgument.getValue().isComments());
        assertEquals(false, itemResultSelectionArgument.getValue().isAnnotations());
    }

    @Test
    public void testFindOrganisationUnitsRetrieveAllXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findOrganisationUnitsRetrieveAll.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationUnit() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String organsationID = ITEM_1_CODE;
        OrganisationUnit OrganisationUnit = getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(OrganisationUnit);
        assertEquals(organsationID, OrganisationUnit.getId());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, OrganisationUnit.getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, OrganisationUnit.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, OrganisationUnit.getParentLink().getKind());
        assertTrue(OrganisationUnit instanceof OrganisationUnit);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveOrganisationUnitXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
        String version = VERSION_1;
        String organisationID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(agencyID, resourceID, version, organisationID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.ORGANISATION_UNIT_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("OrganisationUnit " + organisationID + " not found in version " + version + " of OrganisationUnitScheme " + resourceID + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveOrganisationUnitErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTypeOrganisationUnitsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationUnit.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveOrganisationUnitErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveOrganisationUnit(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL);
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

        assertNotNull(itemSchemes);
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, itemSchemes.getKind());
    }

    private void testFindOrganisationUnits(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        OrganisationUnits items = getSrmRestInternalFacadeClientXml().findOrganisationUnits(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(items);
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, items.getKind());
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return SrmRestConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES;
    }

    @Override
    protected String getSupathItems() {
        return SrmRestConstants.LINK_SUBPATH_ORGANISATION_UNITS;
    }
}
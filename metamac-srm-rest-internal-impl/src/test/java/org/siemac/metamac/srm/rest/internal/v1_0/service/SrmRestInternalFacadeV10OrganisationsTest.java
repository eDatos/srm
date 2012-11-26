package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindAgencies;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindAgencySchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencyType;

public class SrmRestInternalFacadeV10OrganisationsTest extends SrmRestInternalFacadeV10BaseTest {

    private OrganisationsMetamacService organisationsService;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
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
        testFindAgencySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindAgencySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindAgenciesSchemesXml() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findAgencySchemes.xml");

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
    public void testFindAgenciesSchemesByAgencyXml() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findAgencySchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindAgenciesSchemesByAgencyErrorWildcard() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, WILDCARD, null, null, null, "4", "4");
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindAgenciesSchemesByAgencyAndResource() throws Exception {
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindAgencySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);

        testFindAgencySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindAgencySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindAgencySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindAgencySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindAgencySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindAgenciesSchemesByAgencyAndResourceXml() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findAgencySchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindAgenciesSchemesByAgencyAndResourceErrorWildcard() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, WILDCARD, null, null, "4", null);
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testRetrieveAgencyScheme() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        AgencyScheme agencyScheme = getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(agencyScheme);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, agencyScheme.getAgencyID());
        assertEquals(resourceID, agencyScheme.getId());
        assertEquals(version, agencyScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, agencyScheme.getKind());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, agencyScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEMES, agencyScheme.getParentLink().getKind());
        assertTrue(agencyScheme.getAgencies().get(0) instanceof AgencyType);
        assertFalse(agencyScheme.getAgencies().get(0) instanceof Agency);
    }

    @Test
    public void testRetrieveAgencySchemeXml() throws Exception {

        String requestBase = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, null, null, null);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/retrieveAgencyScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveAgencySchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = ITEM_SCHEME_1_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveAgencyScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Organisation scheme not found in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
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
    public void testRetrieveAgencySchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisationScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveAgencySchemeErrorWildcard() throws Exception {
        {
            String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, null, null, null);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, null, null, null);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testFindOrganisations() throws Exception {

        // without parameters
        testFindAgencies(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindAgencies(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindAgencies(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindAgencies(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindAgencies(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindAgencies(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindAgencies(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindAgencies(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindAgencies(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindAgencies(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindAgencies(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindAgencies(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindAgencies(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
        testFindAgencies(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindAgencies(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindAgenciesXml() throws Exception {
        String requestUri = getUriOrganisations(OrganisationTypeEnum.AGENCY, AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findAgencies.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveAgency() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String organsationID = ITEM_1_CODE;
        Agency agency = getSrmRestInternalFacadeClientXml().retrieveAgency(agencyID, resourceID, version, organsationID);

        // Validation
        assertNotNull(agency);
        assertEquals(organsationID, agency.getId());
        assertEquals(RestInternalConstants.KIND_AGENCY, agency.getKind());
        assertEquals(RestInternalConstants.KIND_AGENCY, agency.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_AGENCIES, agency.getParentLink().getKind());
        assertTrue(agency instanceof AgencyType);
        assertTrue(agency instanceof Agency);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveAgencyXml() throws Exception {

        String requestBase = getUriOrganisation(OrganisationTypeEnum.AGENCY, AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/retrieveAgency.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveAgencyErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String organisationID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveAgency(agencyID, resourceID, version, organisationID);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.ORGANISATION_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Organisation not found with ID " + organisationID + " in Organisation scheme in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version,
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
    public void testRetrieveAgencyErrorNotExistsXml() throws Exception {
        String requestUri = getUriOrganisation(OrganisationTypeEnum.AGENCY, AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/retrieveOrganisation.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveAgencyErrorWildcard() throws Exception {
        {
            String requestUri = getUriOrganisation(OrganisationTypeEnum.AGENCY, WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriOrganisation(OrganisationTypeEnum.AGENCY, AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriOrganisation(OrganisationTypeEnum.AGENCY, AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
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

        // Verify with Mockito
        verifyFindAgencySchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, itemSchemes);
    }

    private void testFindAgencies(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Agencies items = getSrmRestInternalFacadeClientXml().findAgencies(agencyID, resourceID, version, query, orderBy, limit, offset);
        verifyFindAgencies(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, items);
    }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationSchemesByCondition() throws MetamacException {
        when(organisationsService.findOrganisationSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<OrganisationSchemeVersionMetamac>>() {

                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationSchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().maintainer().idAsMaintainer());
                        ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code());
                        ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().versionLogic());
                        ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.organisationSchemeType());

                        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = conditionalCriteriaType != null ? (OrganisationSchemeTypeEnum) conditionalCriteriaType.getFirstOperant() : null;
                        if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null) {
                            // Retrieve one
                            OrganisationSchemeVersionMetamac itemSchemeVersion = null;
                            if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = null;
                            } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum);
                            } else {
                                fail();
                            }
                            List<OrganisationSchemeVersionMetamac> itemSchemes = new ArrayList<OrganisationSchemeVersionMetamac>();
                            if (itemSchemeVersion != null) {
                                itemSchemes.add(itemSchemeVersion);
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, 0, itemSchemes.size(), itemSchemes.size());
                        } else {
                            // any
                            List<OrganisationSchemeVersionMetamac> itemSchemes = new ArrayList<OrganisationSchemeVersionMetamac>();
                            if (organisationSchemeTypeEnum != null) {
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1, organisationSchemeTypeEnum));
                            } else {
                                // different types
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2,
                                        OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1,
                                        OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, itemSchemes.size(), itemSchemes.size(), itemSchemes.size(), itemSchemes.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationsByCondition() throws MetamacException {
        when(organisationsService.findOrganisationsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<OrganisationMetamac>>() {

            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer());
                ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().code());
                ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().versionLogic());
                ConditionalCriteria conditionalCriteriaItem = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .nameableArtefact().code());
                ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                        OrganisationMetamacProperties.organisationType());

                OrganisationTypeEnum organisationTypeEnum = conditionalCriteriaType != null ? (OrganisationTypeEnum) conditionalCriteriaType.getFirstOperant() : null;

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null && conditionalCriteriaItem != null) {
                    // Retrieve one
                    OrganisationMetamac item = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaItem.getFirstOperant())) {
                        item = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));
                        OrganisationMetamac parent = null;
                        if (OrganisationTypeEnum.ORGANISATION_UNIT.equals(organisationTypeEnum)) {
                            parent = OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum);;
                        }
                        item = OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, parent, organisationTypeEnum);
                    } else {
                        fail();
                    }
                    List<OrganisationMetamac> items = new ArrayList<OrganisationMetamac>();
                    if (item != null) {
                        items.add(item);
                    }
                    return new PagedResult<OrganisationMetamac>(items, 0, items.size(), items.size());
                } else {
                    // any
                    OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                            SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));
                    OrganisationSchemeVersionMetamac itemScheme2 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1,
                            SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));

                    List<OrganisationMetamac> items = new ArrayList<OrganisationMetamac>();
                    items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, null, organisationTypeEnum));
                    items.add(OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum));
                    items.add(OrganisationsDoMocks.mockOrganisation(ITEM_3_CODE, itemScheme1, null, organisationTypeEnum));
                    items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme2, null, organisationTypeEnum));

                    return new PagedResult<OrganisationMetamac>(items, items.size(), items.size(), items.size(), items.size() * 10, 0);
                }
            };
        });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        organisationsService = applicationContext.getBean(OrganisationsMetamacService.class);
        reset(organisationsService);
        mockFindOrganisationSchemesByCondition();
        mockFindOrganisationsByCondition();
    }

    private String getUriOrganisationSchemes(OrganisationSchemeTypeEnum type, String agencyID, String resourceID, String version, String query, String limit, String offset) throws Exception {
        return getUriItemSchemes(getSupathItemSchemes(type), agencyID, resourceID, version, query, limit, offset);
    }

    private String getUriOrganisations(OrganisationTypeEnum type, String agencyID, String resourceID, String version, String query, String limit, String offset) throws Exception {
        return getUriItems(getSupathItemSchemes(type), getSupathItems(type), agencyID, resourceID, version, query, limit, offset);
    }

    private String getUriOrganisation(OrganisationTypeEnum type, String agencyID, String resourceID, String version, String organisationID) throws Exception {
        return getUriItem(getSupathItemSchemes(type), getSupathItems(type), agencyID, resourceID, version, organisationID);
    }

    private String getSupathItemSchemes(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES;
            default:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_SCHEMES;
        }
    }

    private String getSupathItemSchemes(OrganisationTypeEnum type) {
        return getSupathItemSchemes(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String getSupathItems(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_AGENCIES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNITS;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMERS;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_PROVIDERS;
            default:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATIONS;
        }
    }

    private String getSupathItems(OrganisationTypeEnum type) {
        return getSupathItems(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    @Override
    protected String getSupathItemSchemes() {
        fail("This method can not be called because subpath is different in organisation scheme types");
        return null;
    }

    @Override
    protected String getSupathItems() {
        fail("This method can not be called because subpath is different in organisation scheme types");
        return null;
    }
}
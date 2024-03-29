package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;

import java.io.InputStream;
import java.util.ArrayList;
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
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks;

import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationProperties;

public class SrmRestInternalFacadeV10CategorisationsTest extends SrmRestInternalFacadeV10BaseTest {

    private CategoriesMetamacService categoriesService;

    public static String             CATEGORISATION_1_CODE      = "categorisation1";
    public static String             CATEGORISATION_1_VERSION_1 = "01.000";
    public static String             CATEGORISATION_2_CODE      = "categorisation2";
    public static String             CATEGORISATION_2_VERSION_1 = "01.000";
    public static String             CATEGORISATION_3_CODE      = "categorisation3";
    public static String             CATEGORISATION_3_VERSION_1 = "01.000";

    @Test
    public void testJsonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, CATEGORISATION_1_CODE, CATEGORISATION_1_VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindCategorisations() throws Exception {
        testFindCategorisations(null, null, null, null, null, null, null); // without limits
        testFindCategorisations(null, null, null, "10000", null, null, null); // without limits
        testFindCategorisations(null, null, null, null, "0", null, null); // without limits, first page
        testFindCategorisations(null, null, null, "2", "0", null, null); // first page with pagination
        testFindCategorisations(null, null, null, "2", "2", null, null); // other page with pagination
        testFindCategorisations(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindCategorisations(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindCategorisations(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindCategorisations(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindCategorisations(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindCategorisationsXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CategorisationsTest.class.getResourceAsStream("/responses/categories/findCategorisations.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategorisationsByAgency() throws Exception {
        testFindCategorisations(AGENCY_1, null, null, null, null, null, null);
        testFindCategorisations(AGENCY_1, null, null, null, "0", null, null);
        testFindCategorisations(AGENCY_1, null, null, "2", "0", null, null);
        testFindCategorisations(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorisations(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorisationsByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        Categorisations categorisations = getSrmRestInternalFacadeClientXml().findCategorisations(agencyID, null, null, "3", "3");
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "?limit=3&offset=3", categorisations.getSelfLink());
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "?limit=3&offset=0", categorisations.getFirstLink());
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "?limit=3&offset=0", categorisations.getPreviousLink());
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "?limit=3&offset=27", categorisations.getLastLink());
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, categorisations.getKind());
    }

    @Test
    public void testFindCategorisationsByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCategorisations(WILDCARD_ALL, null, null, null, null);
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
    public void testFindCategorisationsByAgencyAndResource() throws Exception {
        testFindCategorisations(AGENCY_1, CATEGORISATION_1_CODE, null, null, null, null, null);
        testFindCategorisations(WILDCARD_ALL, CATEGORISATION_1_CODE, null, null, null, null, null);
        testFindCategorisations(AGENCY_1, CATEGORISATION_1_CODE, null, "2", null, null, null);
        testFindCategorisations(WILDCARD_ALL, CATEGORISATION_1_CODE, null, "2", null, null, null);
        testFindCategorisations(AGENCY_1, CATEGORISATION_1_CODE, null, null, "0", null, null);
        testFindCategorisations(WILDCARD_ALL, CATEGORISATION_1_CODE, null, null, "0", null, null);
        testFindCategorisations(AGENCY_1, CATEGORISATION_1_CODE, null, "2", "0", null, null);
        testFindCategorisations(AGENCY_1, CATEGORISATION_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorisations(WILDCARD_ALL, CATEGORISATION_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorisations(WILDCARD_ALL, CATEGORISATION_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorisationsByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = CATEGORISATION_1_CODE;
        Categorisations categorisations = getSrmRestInternalFacadeClientXml().findCategorisations(agencyID, resourceID, null, null, "3", "3");
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "/" + resourceID + "?limit=3&offset=3", categorisations.getSelfLink());
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "/" + resourceID + "?limit=3&offset=0", categorisations.getFirstLink());
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "/" + resourceID + "?limit=3&offset=0", categorisations.getPreviousLink());
        assertEquals(getApiEndpoint() + "/categorisations/" + agencyID + "/" + resourceID + "?limit=3&offset=27", categorisations.getLastLink());
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, categorisations.getKind());
    }

    @Test
    public void testFindCategorisationsByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCategorisations(AGENCY_1, WILDCARD_ALL, null, null, null, null);
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
    public void testRetrieveCategorisation() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = CATEGORISATION_1_CODE;
        String version = CATEGORISATION_1_VERSION_1;
        Categorisation categorisation = getSrmRestInternalFacadeClientXml().retrieveCategorisation(agencyID, resourceID, version);

        // Validation
        assertNotNull(categorisation);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, categorisation.getAgencyID());
        assertEquals(resourceID, categorisation.getId());
        assertEquals(version, categorisation.getVersion());
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, categorisation.getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, categorisation.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, categorisation.getParentLink().getKind());

    }

    @Test
    public void testRetrieveCategorisationVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = CATEGORISATION_1_CODE;
        String version = WILDCARD_LATEST;
        Categorisation categorisation = getSrmRestInternalFacadeClientXml().retrieveCategorisation(agencyID, resourceID, version);

        // Validation
        assertNotNull(categorisation);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, categorisation.getAgencyID());
        assertEquals(resourceID, categorisation.getId());
        assertEquals(CATEGORISATION_1_VERSION_1, categorisation.getVersion());
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, categorisation.getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, categorisation.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, categorisation.getParentLink().getKind());

    }

    @Test
    public void testRetrieveCategorisationXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, CATEGORISATION_1_CODE, CATEGORISATION_1_VERSION_1);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CategorisationsTest.class.getResourceAsStream("/responses/categories/retrieveCategorisation.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCategorisationErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = CATEGORISATION_1_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategorisation(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CATEGORISATION_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Categorisation " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveCategorisationErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, CATEGORISATION_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10CategorisationsTest.class.getResourceAsStream("/responses/categories/retrieveCategorisation.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategorisationErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategorisation(WILDCARD_ALL, CATEGORISATION_1_CODE, CATEGORISATION_1_VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveCategorisation(AGENCY_1, WILDCARD_ALL, CATEGORISATION_1_VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveCategorisation(AGENCY_1, CATEGORISATION_1_CODE, WILDCARD_ALL);
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

    private void testFindCategorisations(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        Categorisations categorisations = null;
        if (agencyID == null) {
            categorisations = getSrmRestInternalFacadeClientXml().findCategorisations(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            categorisations = getSrmRestInternalFacadeClientXml().findCategorisations(agencyID, query, orderBy, limit, offset);
        } else {
            categorisations = getSrmRestInternalFacadeClientXml().findCategorisations(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(categorisations);
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, categorisations.getKind());

    }

    @SuppressWarnings("unchecked")
    private void mockFindCategorisationsByCondition() throws MetamacException {
        when(categoriesService.findCategorisationsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> answer(InvocationOnMock invocation)
                            throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        String agencyID = getAgencyIdFromConditionalCriteria(conditions, CategorisationProperties.maintainableArtefact());
                        String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CategorisationProperties.maintainableArtefact());
                        String version = getVersionFromConditionalCriteria(conditions, CategorisationProperties.maintainableArtefact());
                        Boolean latest = getVersionLatestFromConditionalCriteria(conditions, CategorisationProperties.maintainableArtefact());
                        if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                            // Retrieve one
                            com.arte.statistic.sdmx.srm.core.category.domain.Categorisation categorisation = null;
                            if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                                categorisation = null;
                            } else if (AGENCY_1.equals(agencyID) && CATEGORISATION_1_CODE.equals(resourceID) && (CATEGORISATION_1_VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                categorisation = CategoriesDoMocks.mockCategorisation(AGENCY_1, CATEGORISATION_1_CODE, CATEGORISATION_1_VERSION_1);
                            } else {
                                fail();
                            }
                            List<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> categorisations = new ArrayList<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>();
                            if (categorisation != null) {
                                categorisations.add(categorisation);
                            }
                            return new PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>(categorisations, 0, categorisations.size(), categorisations.size());
                        } else {
                            // any
                            List<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> categorisations = new ArrayList<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>();
                            categorisations.add(CategoriesDoMocks.mockCategorisation(AGENCY_1, CATEGORISATION_1_CODE, CATEGORISATION_1_VERSION_1));
                            categorisations.add(CategoriesDoMocks.mockCategorisation(AGENCY_1, CATEGORISATION_2_CODE, CATEGORISATION_2_VERSION_1));
                            categorisations.add(CategoriesDoMocks.mockCategorisation(AGENCY_2, CATEGORISATION_3_CODE, CATEGORISATION_3_VERSION_1));
                            return new PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>(categorisations, categorisations.size(), categorisations.size(), categorisations
                                    .size(), categorisations.size() * 10, 0);
                        }
                    };
                });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        categoriesService = applicationContext.getBean(CategoriesMetamacService.class);
        reset(categoriesService);
        mockFindCategorisationsByCondition();
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "categorisations";
    }

    @Override
    protected String getSupathItems() {
        return null; // no subpaths
    }
}
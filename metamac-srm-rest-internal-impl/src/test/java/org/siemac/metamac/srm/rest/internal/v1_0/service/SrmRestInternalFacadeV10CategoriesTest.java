package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesMockitoVerify.verifyFindCategories;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesMockitoVerify.verifyFindCategorySchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesMockitoVerify.verifyRetrieveCategory;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesMockitoVerify.verifyRetrieveCategoryScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class SrmRestInternalFacadeV10CategoriesTest extends SrmRestInternalFacadeV10BaseTest {

    private CategoriesMetamacService    categoriesService;
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindCategorySchemes() throws Exception {
        testFindCategorySchemes(null, null, null, null, null, null, null); // without limits
        testFindCategorySchemes(null, null, null, "10000", null, null, null); // without limits
        testFindCategorySchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindCategorySchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindCategorySchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindCategorySchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindCategorySchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindCategorySchemes(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindCategorySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindCategorySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindCategorySchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategorySchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategorySchemesByAgency() throws Exception {
        testFindCategorySchemes(AGENCY_1, null, null, null, null, null, null);
        testFindCategorySchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindCategorySchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindCategorySchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorySchemesByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        CategorySchemes categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=4", categorySchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=0", categorySchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=0", categorySchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=36", categorySchemes.getLastLink());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categorySchemes.getKind());
    }
    @Test
    public void testFindCategorySchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCategorySchemes(WILDCARD_ALL, null, null, null, null);
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
    public void testFindCategorySchemesByAgencyAndResource() throws Exception {
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        CategorySchemes categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=4", categorySchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", categorySchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", categorySchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=36", categorySchemes.getLastLink());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categorySchemes.getKind());
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCategorySchemes(AGENCY_1, WILDCARD_ALL, null, null, null, null);
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
    public void testRetrieveCategoryScheme() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        CategoryScheme categoryScheme = getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(categoryScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, categoryScheme.getAgencyID());
        assertEquals(resourceID, categoryScheme.getId());
        assertEquals(version, categoryScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, categoryScheme.getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, categoryScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categoryScheme.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveCategoryScheme(categoriesService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveCategorySchemeVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = WILDCARD_LATEST;
        CategoryScheme categoryScheme = getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(categoryScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, categoryScheme.getAgencyID());
        assertEquals(resourceID, categoryScheme.getId());
        assertEquals(VERSION_1, categoryScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, categoryScheme.getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, categoryScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categoryScheme.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveCategoryScheme(categoriesService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveCategorySchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategoryScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCategorySchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("CategoryScheme " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
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
    public void testRetrieveCategorySchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategoryScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategorySchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(AGENCY_1, WILDCARD_ALL, VERSION_1);
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
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
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
    public void testFindCategories() throws Exception {

        // without parameters
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null); // without limits
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null); // without limits
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null); // without limits
        testFindCategories(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null); // without limits, first page
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null); // with pagination
        testFindCategories(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null); // with pagination
        testFindCategories(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindCategoriesXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategories.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategoriesRetrieveAll() throws Exception {

        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);

        assertNotNull(categories);
        assertEquals(RestInternalConstants.KIND_CATEGORIES, categories.getKind());
        assertEquals(BigInteger.valueOf(4), categories.getTotal());

        // Verify with mockito
        ArgumentCaptor<String> categorySchemeUrnArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ItemResultSelection> itemResultSelectionArgument = ArgumentCaptor.forClass(ItemResultSelection.class);
        verify(categoriesService).retrieveCategoriesByCategorySchemeUrnUnordered(any(ServiceContext.class), categorySchemeUrnArgument.capture(), itemResultSelectionArgument.capture());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=agency1:itemScheme1(01.000)", categorySchemeUrnArgument.getValue());
        assertEquals(true, itemResultSelectionArgument.getValue().isNames());
        assertEquals(false, itemResultSelectionArgument.getValue().isDescriptions());
        assertEquals(false, itemResultSelectionArgument.getValue().isComments());
        assertEquals(false, itemResultSelectionArgument.getValue().isAnnotations());
    }

    @Test
    public void testFindCategoriesRetrieveAllXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategoriesRetrieveAll.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveCategory() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String categoryID = ITEM_1_CODE;
        Category category = getSrmRestInternalFacadeClientXml().retrieveCategory(agencyID, resourceID, version, categoryID);

        // Validation
        assertNotNull(category);
        assertEquals(categoryID, category.getId());
        assertEquals(RestInternalConstants.KIND_CATEGORY, category.getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY, category.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, category.getParentLink().getKind());
        assertTrue(category instanceof Category);
        // other metadata are tested in transformation tests

        // Verify with Mockito
        verifyRetrieveCategory(categoriesService, agencyID, resourceID, version, categoryID);
    }

    @Test
    public void testRetrieveCategoryXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategory.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCategoryErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String categoryID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(agencyID, resourceID, version, categoryID);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CATEGORY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Category " + categoryID + " not found in version " + version + " of CategoryScheme " + resourceID + " from Agency " + agencyID, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(categoryID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(agencyID, exception.getParameters().getParameters().get(3));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCategoryErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategory.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategoryErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveCategory(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveCategory(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE);
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
            getSrmRestInternalFacadeClientXml().retrieveCategory(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter categoryID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(RestInternalConstants.PARAMETER_CATEGORY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    private void testFindCategorySchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        CategorySchemes categorySchemes = null;
        if (agencyID == null) {
            categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, query, orderBy, limit, offset);
        } else {
            categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(categorySchemes);
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categorySchemes.getKind());

        // Verify with Mockito
        verifyFindCategorySchemes(categoriesService, agencyID, resourceID, null, limit, offset, query, orderBy);
    }

    private void testFindCategories(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(agencyID, resourceID, version, query, orderBy, limit, offset);

        assertNotNull(categories);
        assertEquals(RestInternalConstants.KIND_CATEGORIES, categories.getKind());

        // Verify with Mockito
        verifyFindCategories(categoriesService, agencyID, resourceID, version, limit, offset, query, orderBy);
    }

    private void mockRetrieveItemSchemeVersionByVersion() throws MetamacException {
        when(itemSchemeVersionRepository.retrieveByVersion(any(Long.class), any(String.class))).thenAnswer(new Answer<ItemSchemeVersion>() {

            @Override
            public ItemSchemeVersion answer(InvocationOnMock invocation) throws Throwable {
                String version = (String) invocation.getArguments()[1];
                return CategoriesDoMocks.mockCategoryScheme("agencyID", version, version);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCategorySchemesByCondition() throws MetamacException {
        when(categoriesService.findCategorySchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<CategorySchemeVersionMetamac>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CategorySchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        String agencyID = getAgencyIdFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        String version = getVersionFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        Boolean latest = getVersionLatestFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                            // Retrieve one
                            CategorySchemeVersionMetamac categorySchemeVersion = null;
                            if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                                categorySchemeVersion = null;
                            } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                categorySchemeVersion = CategoriesDoMocks.mockCategorySchemeWithCategories(agencyID, resourceID, VERSION_1);
                            } else {
                                fail();
                            }
                            List<CategorySchemeVersionMetamac> categorySchemes = new ArrayList<CategorySchemeVersionMetamac>();
                            if (categorySchemeVersion != null) {
                                categorySchemes.add(categorySchemeVersion);
                            }
                            return new PagedResult<CategorySchemeVersionMetamac>(categorySchemes, 0, categorySchemes.size(), categorySchemes.size());
                        } else {
                            // any
                            List<CategorySchemeVersionMetamac> categorySchemes = new ArrayList<CategorySchemeVersionMetamac>();
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));
                            return new PagedResult<CategorySchemeVersionMetamac>(categorySchemes, categorySchemes.size(), categorySchemes.size(), categorySchemes.size(), categorySchemes.size() * 10,
                                    0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCategoriesByCondition() throws MetamacException {
        when(categoriesService.findCategoriesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CategoryMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CategoryMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String itemID = getItemIdFromConditionalCriteria(conditions, CategoryMetamacProperties.nameableArtefact());

                if (agencyID != null && resourceID != null && version != null && itemID != null) {
                    // Retrieve one
                    CategoryMetamac category = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version) || NOT_EXISTS.equals(itemID)) {
                        category = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_1_CODE.equals(itemID)) {
                        CategorySchemeVersionMetamac categoryScheme1 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, resourceID, version);
                        CategoryMetamac parent = CategoriesDoMocks.mockCategory(ITEM_2_CODE, categoryScheme1, null);
                        category = CategoriesDoMocks.mockCategory(ITEM_1_CODE, categoryScheme1, parent);
                    } else {
                        fail();
                    }
                    List<CategoryMetamac> categories = new ArrayList<CategoryMetamac>();
                    if (category != null) {
                        categories.add(category);
                    }
                    return new PagedResult<CategoryMetamac>(categories, 0, categories.size(), categories.size());
                } else {
                    // any
                    CategorySchemeVersionMetamac categoryScheme1 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
                    CategorySchemeVersionMetamac categoryScheme2 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);

                    List<CategoryMetamac> categories = new ArrayList<CategoryMetamac>();
                    categories.add(CategoriesDoMocks.mockCategory("category1", categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategory("category2", categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategory("category2A", categoryScheme1, categories.get(1)));
                    categories.add(CategoriesDoMocks.mockCategory("category1", categoryScheme2, null));

                    return new PagedResult<CategoryMetamac>(categories, categories.size(), categories.size(), categories.size(), categories.size() * 10, 0);
                }
            };
        });
    }

    private void mockRetrieveCategoriesByCategoryScheme() throws MetamacException {
        when(categoriesService.retrieveCategoriesByCategorySchemeUrnUnordered(any(ServiceContext.class), any(String.class), any(ItemResultSelection.class))).thenAnswer(new Answer<List<ItemResult>>() {

            @Override
            public List<ItemResult> answer(InvocationOnMock invocation) throws Throwable {
                // any
                ItemResult category1 = CategoriesDoMocks.mockCategoryItemResult("category1", null);
                ItemResult category2 = CategoriesDoMocks.mockCategoryItemResult("category2", null);
                ItemResult category2A = CategoriesDoMocks.mockCategoryItemResult("category2A", category2);
                ItemResult category2B = CategoriesDoMocks.mockCategoryItemResult("category2B", category2);
                return Arrays.asList(category1, category2, category2A, category2B);
            };
        });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        categoriesService = applicationContext.getBean(CategoriesMetamacService.class);
        reset(categoriesService);
        itemSchemeVersionRepository = applicationContext.getBean(ItemSchemeVersionRepository.class);
        reset(itemSchemeVersionRepository);

        mockRetrieveItemSchemeVersionByVersion();
        mockFindCategorySchemesByCondition();
        mockFindCategoriesByCondition();
        mockRetrieveCategoriesByCategoryScheme();
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "categoryschemes";
    }

    @Override
    protected String getSupathItems() {
        return "categories";
    }
}
package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesMockitoVerify.verifyFindCategories;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesMockitoVerify.verifyFindCategorySchemes;
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
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategoryType;

public class SrmRestInternalFacadeV10CategoriesTest extends SrmRestInternalFacadeV10BaseTest {

    private CategoriesMetamacService categoriesService;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);

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
        testFindCategorySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindCategorySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindCategorySchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, "4", "4");
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
    public void testFindCategorySchemesByAgencyXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategorySchemes.byAgency.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategorySchemesByAgencyErrorWildcard() throws Exception {
        String requestUri = getUriItemSchemes(WILDCARD, null, null);
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResource() throws Exception {
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCategorySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCategorySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCategorySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResourceXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategorySchemes.byAgencyResource.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResourceErrorWildcard() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, WILDCARD, null);
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testRetrieveCategoryScheme() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        CategoryScheme categoryScheme = getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(categoryScheme);
        // other metadata are tested in mapper tests
        assertEquals("idAsMaintainer" + agencyID, categoryScheme.getAgencyID());
        assertEquals(resourceID, categoryScheme.getId());
        assertEquals(version, categoryScheme.getVersion());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, categoryScheme.getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, categoryScheme.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categoryScheme.getParentLink().getKind());
        assertTrue(categoryScheme.getCategories().get(0) instanceof CategoryType);
        assertFalse(categoryScheme.getCategories().get(0) instanceof Category);
    }

    @Test
    public void testRetrieveCategorySchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
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
        String version = ITEM_SCHEME_1_VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Category scheme not found in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
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
    public void testRetrieveCategorySchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategoryScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategorySchemeErrorWildcard() throws Exception {
        {
            String requestUri = getUriItemSchemes(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItemSchemes(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
        {
            String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
        }
    }

    @Test
    public void testFindCategories() throws Exception {

        // without parameters
        testFindCategories(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindCategories(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindCategories(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindCategories(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindCategories(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // agency
        testFindCategories(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
        testFindCategories(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
        testFindCategories(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
        testFindCategories(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
        testFindCategories(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // resource
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
        testFindCategories(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
        testFindCategories(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order

        // version
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
        testFindCategories(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
        testFindCategories(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    }

    @Test
    public void testFindCategoriesXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategories.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveCategory() throws Exception {
        resetMocks();

        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = ITEM_SCHEME_1_VERSION_1;
        String categoryID = ITEM_1_CODE;
        Category category = getSrmRestInternalFacadeClientXml().retrieveCategory(agencyID, resourceID, version, categoryID);

        // Validation
        assertNotNull(category);
        assertEquals(categoryID, category.getId());
        assertEquals(RestInternalConstants.KIND_CATEGORY, category.getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORY, category.getSelfLink().getKind());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, category.getParentLink().getKind());
        assertTrue(category instanceof CategoryType);
        assertTrue(category instanceof Category);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveCategoryXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
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
        String version = ITEM_SCHEME_1_VERSION_1;
        String categoryID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(agencyID, resourceID, version, categoryID);
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);

            assertEquals(RestServiceExceptionType.CATEGORY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Category not found with ID " + categoryID + " in Category scheme in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(categoryID, exception.getParameters().getParameters().get(0));
            assertEquals(agencyID, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(version, exception.getParameters().getParameters().get(3));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCategoryErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategory.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategoryErrorWildcard() throws Exception {
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

        // Verify with Mockito
        verifyFindCategorySchemes(categoriesService, agencyID, resourceID, limit, offset, query, orderBy, categorySchemes);
    }

    private void testFindCategories(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        Categories categories= getSrmRestInternalFacadeClientXml().findCategories(agencyID, resourceID, version, query, orderBy, limit, offset);
        verifyFindCategories(categoriesService, agencyID, resourceID, version, limit, offset, query, orderBy, categories);
    }

    @SuppressWarnings("unchecked")
    private void mockFindCategorySchemesByCondition() throws MetamacException {
        when(categoriesService.findCategorySchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CategorySchemeVersionMetamac>>() {

            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CategorySchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategorySchemeVersionMetamacProperties
                        .maintainableArtefact().maintainer().idAsMaintainer());
                ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategorySchemeVersionMetamacProperties
                        .maintainableArtefact().code());
                ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategorySchemeVersionMetamacProperties
                        .maintainableArtefact().versionLogic());

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null) {
                    // Retrieve one scheme
                    CategorySchemeVersionMetamac categorySchemeVersion = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        categorySchemeVersion = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                        categorySchemeVersion = CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
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
                    categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1));
                    categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1));
                    categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2));
                    categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1));
                    return new PagedResult<CategorySchemeVersionMetamac>(categorySchemes, categorySchemes.size(), categorySchemes.size(), categorySchemes.size(), categorySchemes.size() * 10, 0);
                }
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCategoriesByCondition() throws MetamacException {
        when(categoriesService.findCategoriesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CategoryMetamac>>() {

            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CategoryMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategoryMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer());
                ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategoryMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().code());
                ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategoryMetamacProperties.itemSchemeVersion()
                        .maintainableArtefact().versionLogic());
                ConditionalCriteria conditionalCriteriaItem = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, CategoryMetamacProperties.nameableArtefact()
                        .code());

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null && conditionalCriteriaItem != null) {
                    // Retrieve one
                    CategoryMetamac category = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaItem.getFirstOperant())) {
                        category = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
                        CategorySchemeVersionMetamac categoryScheme1 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                        category = CategoriesDoMocks.mockCategory(ITEM_1_CODE, categoryScheme1, null);
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
                    CategorySchemeVersionMetamac categoryScheme1 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
                    CategorySchemeVersionMetamac categoryScheme2 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1);

                    List<CategoryMetamac> categories = new ArrayList<CategoryMetamac>();
                    categories.add(CategoriesDoMocks.mockCategory(ITEM_1_CODE, categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategory(ITEM_2_CODE, categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategory(ITEM_3_CODE, categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategory(ITEM_1_CODE, categoryScheme2, null));

                    return new PagedResult<CategoryMetamac>(categories, categories.size(), categories.size(), categories.size(), categories.size() * 10, 0);
                }
            };
        });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        categoriesService = applicationContext.getBean(CategoriesMetamacService.class);
        reset(categoriesService);
        mockFindCategorySchemesByCondition();
        mockFindCategoriesByCondition();
    }

    @Override
    protected String getSupathItemSchemes() {
        return "categoryschemes";
    }

    @Override
    protected String getSupathItems() {
        return "categories";
    }
}
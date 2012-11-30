package org.siemac.metamac.srm.rest.internal.v1_0.category.mapper;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesAsserts.assertEqualsCategorisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesAsserts.assertEqualsCategory;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesAsserts.assertEqualsCategoryScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategorisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategory;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategoryScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategorySchemeWithCategories;
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
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class CategoriesDo2RestMapperTest {

    @Autowired
    private CategoriesDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToCategorySchemes() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<CategorySchemeVersionMetamac> source = new ArrayList<CategorySchemeVersionMetamac>();
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1));
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1));
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2));
        source.add(mockCategoryScheme(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<CategorySchemeVersionMetamac> sources = new PagedResult<CategorySchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        CategorySchemes target = do2RestInternalMapper.toCategorySchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCategorySchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCategorySchemes().get(i));
        }
    }

    @Test
    public void testToCategoryScheme() {

        CategorySchemeVersionMetamac source = mockCategorySchemeWithCategories("agencyID1", "resourceID1", "01.123");

        // Transform
        CategoryScheme target = do2RestInternalMapper.toCategoryScheme(source);

        // Validate
        assertEqualsCategoryScheme(source, target);
    }

    @Test
    public void testToCategories() {

        String agencyID = WILDCARD;
        String categorySchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        CategorySchemeVersionMetamac categoryScheme1 = mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
        CategorySchemeVersionMetamac categoryScheme2 = mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1);
        List<CategoryMetamac> source = new ArrayList<CategoryMetamac>();
        source.add(mockCategory(ITEM_1_CODE, categoryScheme1, null));
        source.add(mockCategory(ITEM_2_CODE, categoryScheme1, null));
        source.add(mockCategory(ITEM_3_CODE, categoryScheme1, null));
        source.add(mockCategory(ITEM_1_CODE, categoryScheme2, null));

        Integer totalRows = source.size() * 5;
        PagedResult<CategoryMetamac> sources = new PagedResult<CategoryMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Categories target = do2RestInternalMapper.toCategories(sources, agencyID, categorySchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes" + "/" + agencyID + "/" + categorySchemeID + "/" + version + "/categories?query=" + query + "&orderBy=" + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCategories().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCategories().get(i));
        }
    }

    @Test
    public void testToCategory() {
        CategorySchemeVersionMetamac categoryScheme = mockCategoryScheme("agencyID1", "resourceID1", "01.123");
        CategoryMetamac parent = mockCategory("categoryParent1", categoryScheme, null);
        CategoryMetamac source = mockCategory("category2", categoryScheme, parent);

        // Transform
        Category target = do2RestInternalMapper.toCategory(source);

        // Validate
        assertEqualsCategory(source, target);
    }

    @Test
    public void testToCategorisations() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> source = new ArrayList<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>();
        source.add(mockCategorisation(AGENCY_1, "categorisation1", "01.000"));
        source.add(mockCategorisation(AGENCY_1, "categorisation2", "01.000"));
        source.add(mockCategorisation(AGENCY_1, "categorisation3", "01.000"));
        source.add(mockCategorisation(AGENCY_2, "categorisation4", "01.000"));

        Integer totalRows = source.size() * 5;
        PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> sources = new PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation>(source, offset,
                source.size(), limit, totalRows, 0);

        // Transform
        Categorisations target = do2RestInternalMapper.toCategorisations(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CATEGORISATIONS, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/categorisations" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCategorisations().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCategorisations().get(i));
        }
    }

    @Test
    public void testToCategorisation() {

        com.arte.statistic.sdmx.srm.core.category.domain.Categorisation source = mockCategorisation("agencyID1", "resourceID1", "01.123");

        // Transform
        Categorisation target = do2RestInternalMapper.toCategorisation(source);

        // Validate
        assertEqualsCategorisation(source, target);
    }
}
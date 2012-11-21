package org.siemac.metamac.srm.rest.internal.v1_0.mapper;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsCategory;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsCategoryScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmCoreMocks;
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
        source.add(SrmCoreMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1));
        source.add(SrmCoreMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1));
        source.add(SrmCoreMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2));
        source.add(SrmCoreMocks.mockCategoryScheme(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1));

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

        CategorySchemeVersionMetamac source = SrmCoreMocks.mockCategorySchemeWithCategories("agencyID1", "resourceID1", "01.123");

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

        CategorySchemeVersionMetamac categoryScheme1 = SrmCoreMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
        CategorySchemeVersionMetamac categoryScheme2 = SrmCoreMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1);
        List<CategoryMetamac> source = new ArrayList<CategoryMetamac>();
        source.add(SrmCoreMocks.mockCategory(ITEM_1_CODE, categoryScheme1, null));
        source.add(SrmCoreMocks.mockCategory(ITEM_2_CODE, categoryScheme1, null));
        source.add(SrmCoreMocks.mockCategory(ITEM_3_CODE, categoryScheme1, null));
        source.add(SrmCoreMocks.mockCategory(ITEM_1_CODE, categoryScheme2, null));

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
        CategorySchemeVersionMetamac categoryScheme = SrmCoreMocks.mockCategoryScheme("agencyID1", "resourceID1", "01.123");
        CategoryMetamac parent = SrmCoreMocks.mockCategory("categoryParent1", categoryScheme, null);
        CategoryMetamac source = SrmCoreMocks.mockCategory("category2", categoryScheme, parent);

        // Transform
        Category target = do2RestInternalMapper.toCategory(source);

        // Validate
        assertEqualsCategory(source, target);
    }
}
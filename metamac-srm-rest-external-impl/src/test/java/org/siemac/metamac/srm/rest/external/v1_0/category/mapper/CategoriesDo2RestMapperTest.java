package org.siemac.metamac.srm.rest.external.v1_0.category.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesDoMocks.mockCategorisation;
import static org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesDoMocks.mockCategory;
import static org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesDoMocks.mockCategoryItemResult;
import static org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesDoMocks.mockCategoryScheme;
import static org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesDoMocks.mockCategorySchemeWithCategories;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-external/applicationContext-test.xml"})
public class CategoriesDo2RestMapperTest {

    @Autowired
    private CategoriesDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToCategorySchemes() {

        String agencyID = WILDCARD_ALL;
        String resourceID = WILDCARD_ALL;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<CategorySchemeVersionMetamac> source = new ArrayList<CategorySchemeVersionMetamac>();
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
        source.add(mockCategoryScheme(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<CategorySchemeVersionMetamac> sources = new PagedResult<CategorySchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        CategorySchemes target = do2RestInternalMapper.toCategorySchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources/v1.0/categoryschemes" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
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

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEME, target.getKind());
        assertEquals("resourceID1", target.getId());
        assertEquals("01.123", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources/v1.0/categoryschemes/agencyID1/resourceID1/01.123";
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources/v1.0/categoryschemes", target.getParentLink().getHref());
        assertNull(target.getManagementAppLink());
        // replaceX no tested, because it is necessary a repository access
        // assertEquals("replaceTo", target.getReplaceToVersion());
        // assertEquals("replacedBy", target.getReplacedByVersion());
        assertEqualsDate(new DateTime(2012, 12, 4, 1, 1, 1, 1), target.getLifeCycle().getLastUpdatedDate());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(SrmRestConstants.KIND_CATEGORIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/categories", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToCategorySchemeImported() {

        CategorySchemeVersionMetamac source = mockCategorySchemeWithCategories("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");
        source.getMaintainableArtefact().setUrnProvider("urnProvider");

        // Transform
        CategoryScheme target = do2RestInternalMapper.toCategoryScheme(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCategories() {

        String agencyID = WILDCARD_ALL;
        String categorySchemeID = WILDCARD_ALL;
        String version = WILDCARD_ALL;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        CategorySchemeVersionMetamac categoryScheme1 = mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
        List<CategoryMetamac> source = new ArrayList<CategoryMetamac>();
        CategoryMetamac category1 = mockCategory("category1", categoryScheme1, null);
        source.add(category1);
        CategoryMetamac category2 = mockCategory("category2", categoryScheme1, null);
        source.add(category2);
        CategoryMetamac category2A = mockCategory("category2A", categoryScheme1, category2);
        source.add(category2A);
        CategoryMetamac category2B = mockCategory("category2B", categoryScheme1, category2);
        source.add(category2B);

        Integer totalRows = source.size() * 5;
        PagedResult<CategoryMetamac> sources = new PagedResult<CategoryMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Categories target = do2RestInternalMapper.toCategories(sources, agencyID, categorySchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_CATEGORIES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources/v1.0/categoryschemes" + "/" + agencyID + "/" + categorySchemeID + "/" + version + "/categories?query=" + query + "&orderBy="
                + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCategories().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i).getItemSchemeVersion(), source.get(i), null, target.getCategories().get(i));
        }
    }

    @Test
    public void testToCategoriesItemResult() {

        String agencyID = AGENCY_1;
        String categorySchemeID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;

        CategorySchemeVersionMetamac categoryScheme1 = mockCategoryScheme(agencyID, categorySchemeID, version);
        List<ItemResult> sources = new ArrayList<ItemResult>();
        sources.add(mockCategoryItemResult(ITEM_1_CODE, null));
        sources.add(mockCategoryItemResult(ITEM_2_CODE, null));
        sources.add(mockCategoryItemResult(ITEM_3_CODE, null));

        // Transform
        Categories target = do2RestInternalMapper.toCategories(sources, categoryScheme1);

        // Validate
        assertEquals(SrmRestConstants.KIND_CATEGORIES, target.getKind());

        assertEquals("http://data.istac.es/apis/structural-resources/v1.0/categoryschemes" + "/" + agencyID + "/" + categorySchemeID + "/" + version + "/categories", target.getSelfLink());
        assertEquals(null, target.getFirstLink());
        assertEquals(null, target.getPreviousLink());
        assertEquals(null, target.getNextLink());
        assertEquals(null, target.getLastLink());

        assertEquals(null, target.getLimit());
        assertEquals(null, target.getOffset());
        assertEquals(sources.size(), target.getTotal().intValue());

        assertEquals(sources.size(), target.getCategories().size());
        for (int i = 0; i < sources.size(); i++) {
            assertEqualsResource(categoryScheme1, null, sources.get(i), target.getCategories().get(i));
        }
    }

    @Test
    public void testToCategory() {
        CategorySchemeVersionMetamac categoryScheme = mockCategoryScheme("agencyID1", "resourceID1", "01.123");
        CategoryMetamac parent = mockCategory("categoryParent1", categoryScheme, null);
        CategoryMetamac source = mockCategory("category2", categoryScheme, parent);

        // Transform
        Category target = do2RestInternalMapper.toCategory(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_CATEGORY, target.getKind());
        assertEquals("category2", target.getId());
        assertEquals("categoryParent1.category2", target.getNestedId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).categoryParent1.category2", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String parentLink = "http://data.istac.es/apis/structural-resources/v1.0/categoryschemes/agencyID1/resourceID1/01.123/categories";
        String selfLink = parentLink + "/categoryParent1.category2";
        assertEquals(SrmRestConstants.KIND_CATEGORY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_CATEGORIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
        assertNull(target.getManagementAppLink());

        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).categoryParent1", target.getParent());
    }

    @Test
    public void testToCategoryImported() {
        CategorySchemeVersionMetamac categoryScheme = mockCategoryScheme("agencyID1", "resourceID1", "01.123");
        categoryScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CategoryMetamac source = mockCategory("category2", categoryScheme, null);
        source.getNameableArtefact().setUriProvider("uriProviderDb");
        source.getNameableArtefact().setUrnProvider("urnProvider");

        // Transform
        Category target = do2RestInternalMapper.toCategory(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).category2", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCategorisations() {

        String agencyID = WILDCARD_ALL;
        String resourceID = WILDCARD_ALL;
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
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources/v1.0/categorisations" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
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
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, target.getKind());
        String parentLink = "http://data.istac.es/apis/structural-resources/v1.0/categorisations";
        String selfLink = parentLink + "/agencyID1/resourceID1/01.123";
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(SrmRestConstants.KIND_CATEGORISATIONS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=agencyID1:categoryScheme-resourceID1(01.123)", target.getSource());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:categoryScheme-resourceID1(01.123).category-resourceID1", target.getTarget());
    }
}
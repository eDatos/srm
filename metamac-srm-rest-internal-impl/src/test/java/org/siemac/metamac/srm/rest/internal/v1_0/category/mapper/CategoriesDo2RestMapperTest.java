package org.siemac.metamac.srm.rest.internal.v1_0.category.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategorisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategory;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategoryScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.category.utils.CategoriesDoMocks.mockCategorySchemeWithCategories;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts.assertEqualsInternationalString;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CategoryType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
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
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
        source.add(mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
        source.add(mockCategoryScheme(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<CategorySchemeVersionMetamac> sources = new PagedResult<CategorySchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        CategorySchemes target = do2RestInternalMapper.toCategorySchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

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
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, target.getKind());
        assertEquals("resourceID1", target.getId());
        assertEquals("01.123", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=agencyID1:resourceID1(01.123)", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes/agencyID1/resourceID1/01.123";
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes", target.getParentLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/categorySchemes/categoryScheme;id=agencyID1:resourceID1(01.123)", target.getManagementAppLink());
        assertEqualsInternationalString("es", "comment-resourceID1v01.123 en Español", "en", "comment-resourceID1v01.123 in English", target.getComment());
        // replaceX no tested, because it is necessary a repository access
        // assertEquals("replaceTo", target.getReplaceToVersion());
        // assertEquals("replacedBy", target.getReplacedByVersion());
        assertEquals(ProcStatus.EXTERNALLY_PUBLISHED, target.getLifeCycle().getProcStatus());
        assertEqualsDate(new DateTime(2009, 9, 1, 1, 1, 1, 1), target.getLifeCycle().getProductionValidationDate());
        assertEquals("production-user", target.getLifeCycle().getProductionValidationUser());
        assertEqualsDate(new DateTime(2010, 10, 2, 1, 1, 1, 1), target.getLifeCycle().getDiffusionValidationDate());
        assertEquals("diffusion-user", target.getLifeCycle().getDiffusionValidationUser());
        assertEqualsDate(new DateTime(2011, 11, 3, 1, 1, 1, 1), target.getLifeCycle().getInternalPublicationDate());
        assertEquals("internal-publication-user", target.getLifeCycle().getInternalPublicationUser());
        assertEqualsDate(new DateTime(2012, 12, 4, 1, 1, 1, 1), target.getLifeCycle().getExternalPublicationDate());
        assertEquals("external-publication-user", target.getLifeCycle().getExternalPublicationUser());
        assertEqualsDate(new DateTime(2013, 10, 1, 10, 12, 13, 14), target.getCreatedDate());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/categories", target.getChildLinks().getChildLinks().get(0).getHref());

        // Categories (SDMX type) // IMPORTANT! categories are printed in hierarchy

        assertEquals(2, target.getCategories().size());
        int i = 0;
        {
            CategoryType category = target.getCategories().get(i++);
            assertTrue(category instanceof CategoryType);
            assertFalse(category instanceof Category);
            assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).category1", category.getUrn());
            assertEquals(0, category.getCategories().size());
        }
        {
            CategoryType category = target.getCategories().get(i++);
            assertTrue(category instanceof CategoryType);
            assertFalse(category instanceof Category);
            assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).category2", category.getUrn());
            assertEquals(2, category.getCategories().size());
            {
                CategoryType categoryChild = category.getCategories().get(0);
                assertTrue(categoryChild instanceof CategoryType);
                assertFalse(categoryChild instanceof Category);
                assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).category2.category2A", categoryChild.getUrn());
            }
            {
                CategoryType categoryChild = category.getCategories().get(1);
                assertTrue(categoryChild instanceof CategoryType);
                assertFalse(categoryChild instanceof Category);
                assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).category2.category2B", categoryChild.getUrn());
            }
        }
        assertEquals(i, target.getCategories().size());
    }

    @Test
    public void testToCategorySchemeImported() {

        CategorySchemeVersionMetamac source = mockCategorySchemeWithCategories("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        CategoryScheme target = do2RestInternalMapper.toCategoryScheme(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
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

        CategorySchemeVersionMetamac categoryScheme1 = mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
        CategorySchemeVersionMetamac categoryScheme2 = mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);
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

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes" + "/" + agencyID + "/" + categorySchemeID + "/" + version + "/categories?query=" + query
                + "&orderBy=" + orderBy;
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

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_CATEGORY, target.getKind());
        assertEquals("category2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).categoryParent1.category2", target.getUrn());
        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes/agencyID1/resourceID1/01.123/categories";
        String selfLink = parentLink + "/category2";
        assertEquals(RestInternalConstants.KIND_CATEGORY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/categorySchemes/categoryScheme;id=agencyID1:resourceID1(01.123)/category;id=category2", target.getManagementAppLink());

        assertEqualsInternationalString("es", "comment-category2 en Español", "en", "comment-category2 in English", target.getComment());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agencyID1:resourceID1(01.123).categoryParent1", target.getParent());
    }

    @Test
    public void testToCategoryImported() {
        CategorySchemeVersionMetamac categoryScheme = mockCategoryScheme("agencyID1", "resourceID1", "01.123");
        categoryScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CategoryMetamac source = mockCategory("category2", categoryScheme, null);
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Category target = do2RestInternalMapper.toCategory(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
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

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categorisations" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

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
        assertEquals(RestInternalConstants.KIND_CATEGORISATION, target.getKind());
        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categorisations";
        String selfLink = parentLink + "/agencyID1/resourceID1/01.123";
        assertEquals(RestInternalConstants.KIND_CATEGORISATION, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CATEGORISATIONS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
    }
}
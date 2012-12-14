package org.siemac.metamac.srm.rest.internal.v1_0.category.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.List;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategoryType;

public class CategoriesAsserts extends Asserts {

    public static void assertEqualsResource(CategorySchemeVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CATEGORY_SCHEME, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(CategoryMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/categories/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CATEGORY, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(Categorisation expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categorisations/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEquals(RestInternalConstants.KIND_CATEGORISATION, actual.getKind());
        assertEquals(expected.getMaintainableArtefact().getCode(), actual.getId());
        assertEquals(expected.getMaintainableArtefact().getUrnProvider(), actual.getUrn());
        assertEquals(RestInternalConstants.KIND_CATEGORISATION, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getMaintainableArtefact().getName(), actual.getTitle());
    }

    public static void assertEqualsCategoryScheme(CategorySchemeVersionMetamac source, CategoryScheme target) {
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        if (source.getMaintainableArtefact().getIsImported()) {
            assertEquals(source.getMaintainableArtefact().getUriProvider(), target.getUri());
        } else {
            assertEquals(target.getSelfLink().getHref(), target.getUri());
        }
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/categories", target.getChildLinks().getChildLinks().get(0).getHref());

        // Categories (SDMX type)
        assertEqualsCategoriesSdmxHierarchy(source.getItemsFirstLevel(), target.getCategories()); // IMPORTANT! first level, because categories are printed in hierarchy
    }

    public static void assertEqualsCategorySdmx(CategoryMetamac source, CategoryType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrnProvider(), target.getUrn());
        assertUriProviderExpected(source.getItemSchemeVersion().getMaintainableArtefact(), target.getUri());
    }

    public static void assertEqualsCategory(CategoryMetamac source, Category target) {

        assertEquals(RestInternalConstants.KIND_CATEGORY, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/categories";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_CATEGORY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        if (source.getItemSchemeVersion().getMaintainableArtefact().getIsImported()) {
            assertEquals(source.getNameableArtefact().getUriProvider(), target.getUri());
        } else {
            assertEquals(target.getSelfLink().getHref(), target.getUri());
        }
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
        assertEqualsNullability(source.getParent(), target.getParent());
        if (source.getParent() != null) {
            assertEquals(source.getParent().getNameableArtefact().getUrnProvider(), target.getParent());
        }

        // Sdmx
        assertEqualsCategorySdmx(source, target);
    }

    public static void assertEqualsCategorisation(Categorisation source, org.siemac.metamac.rest.srm_internal.v1_0.domain.Categorisation target) {
        assertEquals(RestInternalConstants.KIND_CATEGORISATION, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/categorisations";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_CATEGORISATION, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CATEGORISATIONS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertNull(target.getChildLinks());
    }

    @SuppressWarnings("rawtypes")
    private static void assertEqualsCategoriesSdmxHierarchy(List expecteds, List<CategoryType> actuals) {
        assertEquals(expecteds.size(), actuals.size());
        for (int i = 0; i < expecteds.size(); i++) {
            CategoryType actual = actuals.get(i);
            assertTrue(actual instanceof CategoryType);
            assertFalse(actual instanceof Category);
            CategoryMetamac expected = (CategoryMetamac) expecteds.get(i);

            assertEqualsCategorySdmx(expected, actual);
            assertEqualsCategoriesSdmxHierarchy(expected.getChildren(), actual.getCategories());
        }
    }
}
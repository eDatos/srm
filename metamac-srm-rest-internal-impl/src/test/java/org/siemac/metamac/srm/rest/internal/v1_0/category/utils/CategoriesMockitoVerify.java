package org.siemac.metamac.srm.rest.internal.v1_0.category.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

public class CategoriesMockitoVerify extends MockitoVerify {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void verifyFindCategorySchemes(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String limit, String offset, String query, String orderBy,
            CategorySchemes categorySchemesActual) throws Exception {

        assertNotNull(categorySchemesActual);
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, categorySchemesActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(categoriesService).findCategorySchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, query, orderBy, CategorySchemeVersionMetamac.class);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void verifyFindCategories(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy,
            Categories categoriesActual) throws Exception {

        assertNotNull(categoriesActual);
        assertEquals(RestInternalConstants.KIND_CATEGORIES, categoriesActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(categoriesService).findCategoriesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, query, orderBy, CategoryMetamac.class);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }
}
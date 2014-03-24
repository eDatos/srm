package org.siemac.metamac.srm.rest.external.v1_0.category.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.rest.external.v1_0.utils.MockitoVerify;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;

public class CategoriesMockitoVerify extends MockitoVerify {

    public static void verifyRetrieveCategoryScheme(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version) throws Exception {
        verifyFindCategorySchemes(categoriesService, agencyID, resourceID, version, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindCategorySchemes(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy) throws Exception {
        verifyFindCategorySchemes(categoriesService, agencyID, resourceID, version, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    public static void verifyRetrieveCategory(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String itemID) throws Exception {
        verifyFindCategories(categoriesService, agencyID, resourceID, version, itemID, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindCategories(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy)
            throws Exception {
        verifyFindCategories(categoriesService, agencyID, resourceID, version, null, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    public static void verifyRetrieveCategorisation(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version) throws Exception {
        verifyFindCategorisations(categoriesService, agencyID, resourceID, version, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindCategorisations(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy) throws Exception {
        verifyFindCategorisations(categoriesService, agencyID, resourceID, version, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCategorySchemes(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(categoriesService).findCategorySchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, version, query, orderBy, CategorySchemeVersionMetamac.class,
                restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCategories(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String itemID, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(categoriesService).findCategoriesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, itemID, query, orderBy, CategoryMetamac.class,
                CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact(), CategoryMetamacProperties.nameableArtefact(), restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCategorisations(CategoriesMetamacService categoriesService, String agencyID, String resourceID, String version, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(categoriesService).findCategorisationsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, version, query, orderBy, Categorisation.class, restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }
}
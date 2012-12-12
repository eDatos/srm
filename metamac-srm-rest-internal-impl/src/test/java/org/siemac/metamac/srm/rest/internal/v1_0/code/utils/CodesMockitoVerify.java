package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

public class CodesMockitoVerify extends MockitoVerify {

    public static void verifyRetrieveCodelist(CodesMetamacService codesService, String agencyID, String resourceID, String version) throws Exception {
        verifyFindCodelists(codesService, agencyID, resourceID, version, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindCodelists(CodesMetamacService codesService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy)
            throws Exception {
        verifyFindCodelists(codesService, agencyID, resourceID, version, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    public static void verifyRetrieveCode(CodesMetamacService codesService, String agencyID, String resourceID, String version, String itemID) throws Exception {
        verifyFindCodes(codesService, agencyID, resourceID, version, itemID, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindCodes(CodesMetamacService codesService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy)
            throws Exception {
        verifyFindCodes(codesService, agencyID, resourceID, version, null, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCodelists(CodesMetamacService codesService, String agencyID, String resourceID, String version, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(codesService).findCodelistsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, version, query, orderBy, CodelistVersionMetamac.class,
                restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCodes(CodesMetamacService codesService, String agencyID, String resourceID, String version, String itemID, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(codesService).findCodesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, itemID, query, orderBy, CodeMetamac.class, restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }
}
package org.siemac.metamac.srm.rest.internal.v1_0.code.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
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

    public static void verifyRetrieveVariableFamily(CodesMetamacService codesService, String resourceID) throws Exception {
        verifyFindVariableFamilies(codesService, resourceID, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindVariableFamilies(CodesMetamacService codesService, String resourceID, String limit, String offset, String query, String orderBy) throws Exception {
        verifyFindVariableFamilies(codesService, resourceID, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    public static void verifyRetrieveVariable(CodesMetamacService codesService, String resourceID) throws Exception {
        verifyFindVariables(codesService, resourceID, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindVariables(CodesMetamacService codesService, String resourceID, String limit, String offset, String query, String orderBy) throws Exception {
        verifyFindVariables(codesService, resourceID, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    public static void verifyRetrieveCodelistFamily(CodesMetamacService codesService, String resourceID) throws Exception {
        verifyFindCodelistFamilies(codesService, resourceID, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindCodelistFamilies(CodesMetamacService codesService, String resourceID, String limit, String offset, String query, String orderBy) throws Exception {
        verifyFindCodelistFamilies(codesService, resourceID, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
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
        conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC)
                .buildSingle());

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
        conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());

        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindVariableFamilies(CodesMetamacService codesService, String resourceID, String query, String orderBy, PagingParameter pagingParameterExpected,
            RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(codesService).findVariableFamiliesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = new ArrayList<ConditionalCriteria>();
        if (RestOperationEnum.FIND.equals(restOperation)) {
            conditionalCriteriaExpected.addAll(buildFindExpectedOrder(orderBy, VariableFamily.class, VariableFamilyProperties.nameableArtefact()));
            conditionalCriteriaExpected.addAll(buildFindExpectedQuery(query, VariableFamily.class, VariableFamilyProperties.nameableArtefact()));
        }
        conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).distinctRoot().buildSingle());
        if (resourceID != null) {
            conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).withProperty(VariableFamilyProperties.nameableArtefact().code()).eq(resourceID).buildSingle());
        }

        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindVariables(CodesMetamacService codesService, String resourceID, String query, String orderBy, PagingParameter pagingParameterExpected, RestOperationEnum restOperation)
            throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(codesService).findVariablesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = new ArrayList<ConditionalCriteria>();
        if (RestOperationEnum.FIND.equals(restOperation)) {
            conditionalCriteriaExpected.addAll(buildFindExpectedOrder(orderBy, Variable.class, VariableProperties.nameableArtefact()));
            conditionalCriteriaExpected.addAll(buildFindExpectedQuery(query, Variable.class, VariableProperties.nameableArtefact()));
        }
        conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(Variable.class).distinctRoot().buildSingle());
        if (resourceID != null) {
            conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(Variable.class).withProperty(VariableProperties.nameableArtefact().code()).eq(resourceID).buildSingle());
        }

        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCodelistFamilies(CodesMetamacService codesService, String resourceID, String query, String orderBy, PagingParameter pagingParameterExpected,
            RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(codesService).findCodelistFamiliesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = new ArrayList<ConditionalCriteria>();
        if (RestOperationEnum.FIND.equals(restOperation)) {
            conditionalCriteriaExpected.addAll(buildFindExpectedOrder(orderBy, CodelistFamily.class, CodelistFamilyProperties.nameableArtefact()));
            conditionalCriteriaExpected.addAll(buildFindExpectedQuery(query, CodelistFamily.class, CodelistFamilyProperties.nameableArtefact()));
        }
        conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).distinctRoot().buildSingle());
        if (resourceID != null) {
            conditionalCriteriaExpected.add(ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).withProperty(CodelistFamilyProperties.nameableArtefact().code()).eq(resourceID).buildSingle());
        }

        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }
}
package org.siemac.metamac.srm.soap.external.v1_0.code.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;

public class CodesMockitoVerify {

    public static void verifyFindCodelists(CodesMetamacService codesService) throws Exception {
        verifyFindCodelistsByCriteria(codesService, null);
    }

    public static void verifyRetrieveCodelist(CodesMetamacService codesService, String urn) throws Exception {
        verifyFindCodelistsByCriteria(codesService, urn);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindCodelistsByCriteria(CodesMetamacService codesService, String urn) throws Exception {
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(codesService).findCodelistsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        ConditionalCriteria propertyExternallyPublished = getPropertyFromConditionalCriteria(conditions.getValue(), CodelistVersionMetamacProperties.maintainableArtefact().publicLogic());
        assertNotNull(propertyExternallyPublished);
        assertEquals(Boolean.TRUE, propertyExternallyPublished.getFirstOperant());

        ConditionalCriteria propertyAccessTypePublic = getPropertyFromConditionalCriteria(conditions.getValue(), CodelistVersionMetamacProperties.accessType());
        assertNotNull(propertyAccessTypePublic);
        assertEquals(AccessTypeEnum.PUBLIC, propertyAccessTypePublic.getFirstOperant());

        if (urn != null) {
            ConditionalCriteria propertyUrn = getPropertyOrFromConditionalCriteria(conditions.getValue(), CodelistVersionMetamacProperties.maintainableArtefact().urnProvider());
            assertNotNull(propertyUrn);
            assertEquals(urn, propertyUrn.getFirstOperant());
        }
    }

    @SuppressWarnings("rawtypes")
    private static ConditionalCriteria getPropertyOrFromConditionalCriteria(List<ConditionalCriteria> conditions, Property property) {
        return ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Or, property);
    }

    @SuppressWarnings("rawtypes")
    private static ConditionalCriteria getPropertyFromConditionalCriteria(List<ConditionalCriteria> conditions, Property property) {
        return ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, property);
    }

}
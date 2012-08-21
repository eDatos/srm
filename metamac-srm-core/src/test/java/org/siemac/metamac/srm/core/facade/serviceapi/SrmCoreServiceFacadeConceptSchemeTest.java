package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeConceptSchemeTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    private static final String    CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";
    private static final String    CONCEPT_SCHEME_1_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(02.000)";
    private static final String    CONCEPT_SCHEME_2_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME02(01.000)";
    private static final String    CONCEPT_SCHEME_3_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(01.000)";
    private static final String    NOT_EXISTS          = "not-exists";

    private final ServiceContext   serviceContext      = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    // -------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------

    @Test
    public void testRetrieveConceptSchemeByUrn() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V1);
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
        assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
        assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());

        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeMetamacDto.getType());
        assertEquals("op1", conceptSchemeMetamacDto.getRelatedOperation().getCode());
        assertEquals("http://op1", conceptSchemeMetamacDto.getRelatedOperation().getUri());
        assertEquals("urn:op1", conceptSchemeMetamacDto.getRelatedOperation().getUrn());
        assertEquals("http://app/operations", conceptSchemeMetamacDto.getRelatedOperation().getManagementAppUrl());
        assertEquals(1, conceptSchemeMetamacDto.getRelatedOperation().getVersion().longValue());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnWithoutRelatedOperation() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1);
        assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
        assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());

        assertEquals(ConceptSchemeTypeEnum.ROLE, conceptSchemeMetamacDto.getType());
        assertNull(conceptSchemeMetamacDto.getRelatedOperation());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.URN, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveConceptSchemeErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setCode("code-" + MetamacMocks.mockString(10));
        conceptSchemeDto.setName(MetamacMocks.mockInternationalString());
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintiner", TypeExternalArtefactsEnum.AGENCY));

        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), conceptSchemeDto);
        conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeMetamacCreated.getUrn());

        assertNotNull(conceptSchemeMetamacCreated);
        assertEquals(conceptSchemeDto.getCode(), conceptSchemeMetamacCreated.getCode());
        // URI
        // assert
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());
        MetamacAsserts.assertEqualsInternationalStringDto(conceptSchemeDto.getName(), conceptSchemeMetamacCreated.getName());
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getProcStatus());

    }

    @Test
    public void testUpdateConceptScheme() throws Exception {
        // TODO Auto-generated method stub
    }

    @Test
    public void testDeleteConceptScheme() throws Exception {
        // TODO Auto-generated method stub
    }

    @Test
    public void testFindConceptSchemesByCondition() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);

        MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());

        String firstUrn = result.getResults().get(0).getUrn();
        for (ConceptSchemeDto conceptSchemeDto : result.getResults()) {
            assertTrue((conceptSchemeDto.getUrn().compareTo(firstUrn) == 0) || (conceptSchemeDto.getUrn().compareTo(firstUrn) > 0));
        }

        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(0);
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
            assertEquals("02.000", conceptSchemeMetamacDto.getReplacedBy());
            assertNull(conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeMetamacDto.getType());
            assertEquals("urn:op1", conceptSchemeMetamacDto.getRelatedOperation().getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(1);
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("02.000", conceptSchemeMetamacDto.getVersionLogic());
            assertNull(conceptSchemeMetamacDto.getReplacedBy());
            assertEquals("01.000", conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.GLOSSARY, conceptSchemeMetamacDto.getType());
            assertNull(conceptSchemeMetamacDto.getRelatedOperation());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(2);
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME02", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
            assertNull(conceptSchemeMetamacDto.getReplacedBy());
            assertNull(conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.ROLE, conceptSchemeMetamacDto.getType());
            assertNull(conceptSchemeMetamacDto.getRelatedOperation());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(3);
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals("CONCEPTSCHEME03", conceptSchemeMetamacDto.getCode());
            assertTrue(conceptSchemeMetamacDto.getFinalLogic());
            assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
            assertNull(conceptSchemeMetamacDto.getReplacedBy());
            assertNull(conceptSchemeMetamacDto.getReplaceTo());

            assertEquals(ConceptSchemeTypeEnum.ROLE, conceptSchemeMetamacDto.getType());
            assertNull(conceptSchemeMetamacDto.getRelatedOperation());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
    }

    @Test
    public void testFindConceptSchemesByProcStatus() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);

        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.DRAFT, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : result.getResults()) {
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : result.getResults()) {
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
        }
    }

    @Test
    public void testFindConceptSchemesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        // Pagination
        int maxResultSize = 2;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CONCEPT_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(serviceContext, metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(0).getUrn());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptSchemeTest.xml";
    }
}
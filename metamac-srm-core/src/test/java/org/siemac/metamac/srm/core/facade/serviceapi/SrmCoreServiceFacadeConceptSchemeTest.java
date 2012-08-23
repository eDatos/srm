package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsInternationalStringDto;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
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
import org.siemac.metamac.core.common.dto.InternationalStringDto;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeConceptSchemeTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

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

        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeMetamacDto.getProductionValidationDate());
        assertEquals("user1", conceptSchemeMetamacDto.getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", conceptSchemeMetamacDto.getDiffusionValidationDate());
        assertEquals("user2", conceptSchemeMetamacDto.getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", conceptSchemeMetamacDto.getInternalPublicationDate());
        assertEquals("user3", conceptSchemeMetamacDto.getInternalPublicationUser());
        assertNull(conceptSchemeMetamacDto.getExternalPublicationDate());
        assertNull(conceptSchemeMetamacDto.getExternalPublicationUser());
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

        // Identifiers
        assertNotNull(conceptSchemeMetamacCreated);
        assertEquals(conceptSchemeDto.getCode(), conceptSchemeMetamacCreated.getCode());
        assertNull(conceptSchemeMetamacCreated.getUri());
        assertNotNull(conceptSchemeMetamacCreated.getUrn());
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());
        assertEqualsInternationalStringDto(conceptSchemeDto.getName(), conceptSchemeMetamacCreated.getName());

        // Content descriptors
        assertEqualsInternationalStringDto(conceptSchemeDto.getDescription(), conceptSchemeMetamacCreated.getDescription());
        assertEquals(conceptSchemeDto.getIsPartial(), conceptSchemeMetamacCreated.getIsPartial());

        // Class descriptors
        assertEquals(conceptSchemeDto.getType(), conceptSchemeMetamacCreated.getType());
        assertNull(conceptSchemeMetamacCreated.getRelatedOperation());

        // Production descriptors
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getProcStatus());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationUser());

        // Diffusion descriptors
        assertNull(conceptSchemeMetamacCreated.getValidFrom());
        assertNull(conceptSchemeMetamacCreated.getValidTo());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationUser());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationUser());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationUser());
    }

    @Test
    public void testCreateConceptSchemeOperationType() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setCode("code-" + MetamacMocks.mockString(10));
        conceptSchemeDto.setName(MetamacMocks.mockInternationalString());
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeDto.setRelatedOperation(MetamacMocks.mockExternalItemDto("urn:operation", TypeExternalArtefactsEnum.STATISTICAL_OPERATION));
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintiner", TypeExternalArtefactsEnum.AGENCY));

        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), conceptSchemeDto);

        // Identifiers
        assertNotNull(conceptSchemeMetamacCreated);
        assertEquals(conceptSchemeDto.getCode(), conceptSchemeMetamacCreated.getCode());
        assertNull(conceptSchemeMetamacCreated.getUri());
        assertNotNull(conceptSchemeMetamacCreated.getUrn());
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());
        assertEqualsInternationalStringDto(conceptSchemeDto.getName(), conceptSchemeMetamacCreated.getName());

        // Content descriptors
        assertEqualsInternationalStringDto(conceptSchemeDto.getDescription(), conceptSchemeMetamacCreated.getDescription());
        assertEquals(conceptSchemeDto.getIsPartial(), conceptSchemeMetamacCreated.getIsPartial());

        // Class descriptors
        assertEquals(conceptSchemeDto.getType(), conceptSchemeMetamacCreated.getType());
        assertNotNull(conceptSchemeMetamacCreated.getRelatedOperation());
        assertEquals(conceptSchemeDto.getRelatedOperation(), conceptSchemeMetamacCreated.getRelatedOperation());

        // Production descriptors
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getProcStatus());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationUser());

        // Diffusion descriptors
        assertNull(conceptSchemeMetamacCreated.getValidFrom());
        assertNull(conceptSchemeMetamacCreated.getValidTo());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationUser());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationUser());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationUser());
    }

    @Test
    public void testUpdateConceptScheme() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1);

        InternationalStringDto name = MetamacMocks.mockInternationalString();
        InternationalStringDto description = MetamacMocks.mockInternationalString();
        Boolean isPartial = Boolean.TRUE;
        ConceptSchemeTypeEnum type = ConceptSchemeTypeEnum.ROLE;

        conceptSchemeMetamacDto = srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDto);

        assertNotNull(conceptSchemeMetamacDto);
        assertEqualsInternationalStringDto(name, conceptSchemeMetamacDto.getName());
        assertEqualsInternationalStringDto(description, conceptSchemeMetamacDto.getDescription());
        assertEquals(isPartial, conceptSchemeMetamacDto.getIsPartial());
        assertEquals(type, conceptSchemeMetamacDto.getType());
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

        assertEquals(9, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_5_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_6_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
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

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
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

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CONCEPT_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeDto.getProcStatus());
        }

        // Sends to production validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getProductionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getProductionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Sends to production validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Rejects validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, conceptSchemeDto.getProcStatus());
            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Rejects validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, conceptSchemeDto.getProcStatus());
            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testPublishInternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Publish internally
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.publishInternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
            assertTrue(conceptSchemeDto.getFinalLogic());
        }
        // Validate retrieving
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
            assertTrue(conceptSchemeDto.getFinalLogic());
        }
    }

    @Test
    public void testPublishExternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNull(conceptSchemeDto.getValidFrom());
            assertNull(conceptSchemeDto.getValidTo());
        }

        // Publish externally
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.publishExternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNotNull(conceptSchemeDto.getInternalPublicationDate());
            assertNotNull(conceptSchemeDto.getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getValidFrom()));
            assertNull(conceptSchemeDto.getValidTo());
        }
        // Validate retrieving
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNotNull(conceptSchemeDto.getInternalPublicationDate());
            assertNotNull(conceptSchemeDto.getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getValidFrom()));
            assertNull(conceptSchemeDto.getValidTo());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptSchemeTest.xml";
    }
}
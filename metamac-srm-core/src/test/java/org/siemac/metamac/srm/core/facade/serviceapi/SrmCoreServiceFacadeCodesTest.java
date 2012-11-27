package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsInternationalStringDto;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodeDto;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistMetamacDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeCodesTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // IMPORTANT: Metadata transformation is tested in Do2Dto tests

    // ---------------------------------------------------------------------------------------
    // CODELISTS
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCodelistByUrn() throws Exception {
        // Retrieve
        CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1);

        // Validate
        assertEquals(CODELIST_1_V1, codelistMetamacDto.getUrn());
    }

    @Test
    public void testCreateCodelist() throws Exception {
        // Create
        CodelistMetamacDto codelistDto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        CodelistMetamacDto codelistMetamacCreated = srmCoreServiceFacade.createCodelist(getServiceContextAdministrador(), codelistDto);

        // Validate some metadata
        assertEquals(codelistDto.getCode(), codelistMetamacCreated.getCode());
        assertNotNull(codelistMetamacCreated.getUrn());
        assertEquals(ProcStatusEnum.DRAFT, codelistMetamacCreated.getLifeCycle().getProcStatus());
        assertEquals(Long.valueOf(0), codelistMetamacCreated.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCodelist() throws Exception {
        // Update
        CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        codelistMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        CodelistMetamacDto codelistMetamacDtoUpdated = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDto);

        // Validate
        assertNotNull(codelistMetamacDto);
        assertEqualsCodelistMetamacDto(codelistMetamacDto, codelistMetamacDtoUpdated);
        assertTrue(codelistMetamacDtoUpdated.getVersionOptimisticLocking() > codelistMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCodelistErrorOptimisticLocking() throws Exception {
        String urn = CODELIST_1_V2;

        CodelistMetamacDto codelistMetamacDtoSession1 = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), codelistMetamacDtoSession1.getVersionOptimisticLocking());
        codelistMetamacDtoSession1.setIsPartial(Boolean.TRUE);

        CodelistMetamacDto codelistMetamacDtoSession2 = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), codelistMetamacDtoSession2.getVersionOptimisticLocking());
        codelistMetamacDtoSession2.setIsPartial(Boolean.TRUE);

        // Update by session 1
        CodelistMetamacDto codelistMetamacDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDtoSession1);
        assertTrue(codelistMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking() > codelistMetamacDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        codelistMetamacDtoSession1AfterUpdate1.setIsPartial(Boolean.FALSE);
        CodelistMetamacDto codelistMetamacDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDtoSession1AfterUpdate1);
        assertTrue(codelistMetamacDtoSession1AfterUpdate2.getVersionOptimisticLocking() > codelistMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteCodelist() throws Exception {
        String urn = CODELIST_2_V1;

        // Delete codelist only with version in draft
        srmCoreServiceFacade.deleteCodelist(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            fail("Codelist deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindCodelistsByCondition() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(17, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_1_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_1_V2, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_2_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_3_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_4_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_5_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_6_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_7_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_7_V2, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_8_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_9_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_10_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_10_V2, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_10_V3, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_11_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_12_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        {
            CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
            assertEquals(CODELIST_13_V1, codelistMetamacDto.getUrn());
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
        }
        assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);

        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), "Nombre codelist-1-v1", OperationType.EQ));

            result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindCodelistsByProcStatus() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), ProcStatusEnum.DRAFT,
                    OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_1_V2, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_2_V1, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_8_V1, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_9_V1, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_1_V1, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_3_V1, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_7_V2, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CodelistMetamacDto codelistMetamacDto = result.getResults().get(i++);
                assertEquals(CODELIST_10_V2, codelistMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindCodelistsPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.URN.name());
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

            MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CODELIST_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CODELIST_2_V1, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testRetrieveCodelistVersions() throws Exception {
        // Retrieve all versions
        String urn = CODELIST_1_V1;
        List<CodelistMetamacDto> codelistMetamacDtos = srmCoreServiceFacade.retrieveCodelistVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, codelistMetamacDtos.size());
        assertEquals(CODELIST_1_V1, codelistMetamacDtos.get(0).getUrn());
        assertEquals(CODELIST_1_V2, codelistMetamacDtos.get(1).getUrn());
    }

    @Test
    public void testSendCodelistToProductionValidation() throws Exception {

        String urn = CODELIST_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistMetamacDto codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, codelistDto.getLifeCycle().getProcStatus());
        }

        // Sends to production validation
        CodelistMetamacDto codelistDto = srmCoreServiceFacade.sendCodelistToProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getProductionValidationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getProductionValidationUser());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getProductionValidationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getProductionValidationUser());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCodelistToDiffusionValidation() throws Exception {
        String urn = CODELIST_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistMetamacDto codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
        }

        // Sends to production validation
        CodelistMetamacDto codelistDto = srmCoreServiceFacade.sendCodelistToDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCodelistProductionValidation() throws Exception {
        String urn = CODELIST_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistMetamacDto codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
        }

        // Rejects validation
        CodelistMetamacDto codelistDto = srmCoreServiceFacade.rejectCodelistProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, codelistDto.getLifeCycle().getProcStatus());
            assertNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);

            assertNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCodelistDiffusionValidation() throws Exception {
        String urn = CODELIST_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistMetamacDto codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
        }

        // Rejects validation
        CodelistMetamacDto codelistDto = srmCoreServiceFacade.rejectCodelistDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, codelistDto.getLifeCycle().getProcStatus());
            assertNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);

            assertNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testPublishInternallyCodelist() throws Exception {
        String urn = CODELIST_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistMetamacDto codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistDto.getLifeCycle().getProcStatus());
        }

        // Publish internally
        CodelistMetamacDto codelistDto = srmCoreServiceFacade.publishCodelistInternally(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistDto.getLifeCycle().getProcStatus());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(codelistDto.getFinalLogic());
        }
        // Validate retrieving
        {
            codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistDto.getLifeCycle().getProcStatus());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getInternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationDate());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(codelistDto.getFinalLogic());
        }
    }

    @Test
    public void testPublishExternallyCodelist() throws Exception {
        String urn = CODELIST_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistMetamacDto codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistDto.getLifeCycle().getProcStatus());
            assertNull(codelistDto.getValidFrom());
            assertNull(codelistDto.getValidTo());
        }

        // Publish externally
        CodelistMetamacDto codelistDto = srmCoreServiceFacade.publishCodelistExternally(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistDto.getLifeCycle().getProcStatus());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNotNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNotNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getExternalPublicationUser());
            assertNull(codelistDto.getLifeCycle().getIsExternalPublicationFailed());
            assertNull(codelistDto.getLifeCycle().getExternalPublicationFailedDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getValidFrom()));
            assertNull(codelistDto.getValidTo());
        }
        // Validate retrieving
        {
            codelistDto = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistDto.getLifeCycle().getProcStatus());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(codelistDto.getLifeCycle().getDiffusionValidationUser());
            assertNotNull(codelistDto.getLifeCycle().getInternalPublicationDate());
            assertNotNull(codelistDto.getLifeCycle().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getLifeCycle().getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), codelistDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistDto.getValidFrom()));
            assertNull(codelistDto.getValidTo());
        }
    }

    @Test
    public void testVersioningCodelist() throws Exception {

        String urn = CODELIST_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(02.000)";

        CodelistMetamacDto codelistDtoToCopy = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        CodelistMetamacDto codelistDtoNewVersion = srmCoreServiceFacade.versioningCodelist(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(versionExpected, codelistDtoNewVersion.getVersionLogic());
            assertEquals(urnExpected, codelistDtoNewVersion.getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistDtoNewVersion.getLifeCycle().getProcStatus());
            CodesMetamacAsserts.assertEqualsCodelistMetamacDto(codelistDtoToCopy, codelistDtoNewVersion);
        }

        // Validate retrieving
        {
            // New version
            codelistDtoNewVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistDtoNewVersion.getUrn());
            assertEquals(versionExpected, codelistDtoNewVersion.getVersionLogic());
            assertEquals(urnExpected, codelistDtoNewVersion.getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistDtoNewVersion.getLifeCycle().getProcStatus());
            assertEquals("01.000", codelistDtoNewVersion.getReplaceToVersion());
            assertEquals(null, codelistDtoNewVersion.getReplacedByVersion());
            CodesMetamacAsserts.assertEqualsCodelistMetamacDto(codelistDtoToCopy, codelistDtoNewVersion);

            // Copied version
            codelistDtoToCopy = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", codelistDtoToCopy.getVersionLogic());
            assertEquals(urn, codelistDtoToCopy.getUrn());
            assertEquals(null, codelistDtoToCopy.getReplaceToVersion());
            assertEquals(versionExpected, codelistDtoToCopy.getReplacedByVersion());

            // All versions
            List<CodelistMetamacDto> allVersions = srmCoreServiceFacade.retrieveCodelistVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getUrn());
            assertEquals(urnExpected, allVersions.get(1).getUrn());
        }
    }

    @Test
    public void testEndCodelistValidity() throws Exception {
        CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.endCodelistValidity(getServiceContextAdministrador(), CODELIST_7_V1);

        assertNotNull(codelistMetamacDto);
        assertTrue(DateUtils.isSameDay(new Date(), codelistMetamacDto.getValidTo()));
    }

    // ---------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testCreateCode() throws Exception {
        CodeMetamacDto codeMetamacDto = CodesMetamacDtoMocks.mockCodeDto();
        codeMetamacDto.setItemSchemeVersionUrn(CODELIST_1_V2);

        CodeMetamacDto codeMetamacDtoCreated = srmCoreServiceFacade.createCode(getServiceContextAdministrador(), codeMetamacDto);
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000)." + codeMetamacDto.getCode(), codeMetamacDtoCreated.getUrn());
        assertNull(codeMetamacDtoCreated.getUri());

        assertEqualsCodeDto(codeMetamacDto, codeMetamacDtoCreated);
    }

    @Test
    public void testCreateCodeWithCodeParent() throws Exception {
        CodeMetamacDto codeMetamacDto = CodesMetamacDtoMocks.mockCodeDto();
        codeMetamacDto.setItemParentUrn(CODELIST_1_V2_CODE_1);
        codeMetamacDto.setItemSchemeVersionUrn(CODELIST_1_V2);

        CodeMetamacDto codeMetamacDtoCreated = srmCoreServiceFacade.createCode(getServiceContextAdministrador(), codeMetamacDto);
        assertEquals(CODELIST_1_V2_CODE_1, codeMetamacDtoCreated.getItemParentUrn());
        assertEqualsCodeDto(codeMetamacDto, codeMetamacDtoCreated);
    }

    @Test
    public void testCreateCodeErrorParentNotExists() throws Exception {
        CodeMetamacDto codeMetamacDto = CodesMetamacDtoMocks.mockCodeDto();
        codeMetamacDto.setItemParentUrn(NOT_EXISTS);
        codeMetamacDto.setItemSchemeVersionUrn(CODELIST_1_V2);

        try {
            srmCoreServiceFacade.createCode(getServiceContextAdministrador(), codeMetamacDto);
            fail("wrong parent");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateCode() throws Exception {
        CodeMetamacDto codeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
        codeMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        codeMetamacDto.setDescription(MetamacMocks.mockInternationalStringDto());

        CodeMetamacDto codeMetamacDtoUpdated = srmCoreServiceFacade.updateCode(getServiceContextAdministrador(), codeMetamacDto);

        assertNotNull(codeMetamacDto);
        assertEqualsCodeDto(codeMetamacDto, codeMetamacDtoUpdated);
        assertTrue(codeMetamacDtoUpdated.getVersionOptimisticLocking() > codeMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testRetrieveCodeByUrn() throws Exception {
        CodeMetamacDto codeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);

        assertNotNull(codeMetamacDto);
        assertEquals(CODELIST_1_V2_CODE_1, codeMetamacDto.getUrn());

        assertEqualsInternationalStringDto(codeMetamacDto.getName(), "es", "Nombre codelist-1-v2-code-1", null, null);
        assertEqualsInternationalStringDto(codeMetamacDto.getDescription(), "es", "Descripción codelist-1-v2-code-1", null, null);
    }

    @Test
    public void testFindCodesByCondition() throws Exception {
        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(CodeMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            MetamacCriteriaOrder orderCodelistUrn = new MetamacCriteriaOrder();
            orderCodelistUrn.setType(OrderTypeEnum.ASC);
            orderCodelistUrn.setPropertyName(CodeMetamacCriteriaOrderEnum.CODELIST_URN.name());
            metamacCriteria.getOrdersBy().add(orderCodelistUrn);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<CodeMetamacDto> codesPagedResult = srmCoreServiceFacade.findCodesByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(28, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(28, codesPagedResult.getResults().size());
            assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V1, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CODELIST_1_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_2_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_2_V1_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_2_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_2_1_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_2_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_4_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_5_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_6_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_7_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_8_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_10_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_10_V3_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_11_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_12_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_13_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_13_V1_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_13_V1_CODE_3, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(codesPagedResult.getResults().size(), i);
        }

        // Find only codes in first level
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CodeMetamacCriteriaOrderEnum.CODELIST_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CodeMetamacCriteriaOrderEnum.CODELIST_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CodeMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
            propertyRestriction.setPropertyName(CodeMetamacCriteriaPropertyEnum.CODE_PARENT_URN.name());
            propertyRestriction.setOperationType(OperationType.IS_NULL);
            metamacCriteria.setRestriction(propertyRestriction);

            // Find
            MetamacCriteriaResult<CodeMetamacDto> codesPagedResult = srmCoreServiceFacade.findCodesByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(21, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(21, codesPagedResult.getResults().size());
            assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V1, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CODELIST_1_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_2_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_2_V1_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_3_V1_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_4_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_5_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_6_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_7_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_8_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_10_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_10_V3_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_11_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_12_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_13_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_13_V1_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_13_V1_CODE_3, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(codesPagedResult.getResults().size(), i);
        }

        // Find by name (like), code (like) and codelist urn
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(CodeMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.NAME.name(), "Nombre codelist-1-v2-code-2-", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODE.name(), "CODE02", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_URN.name(), CODELIST_1_V2, OperationType.EQ));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<CodeMetamacDto> codesPagedResult = srmCoreServiceFacade.findCodesByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, codesPagedResult.getResults().size());
            assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(codesPagedResult.getResults().size(), i);
        }

        // Find by codelist urn paginated
        {

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(CodeMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_URN.name(), CODELIST_1_V2, OperationType.EQ));

            // First page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(0);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<CodeMetamacDto> codesPagedResult = srmCoreServiceFacade.findCodesByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, codesPagedResult.getResults().size());
                assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(codesPagedResult.getResults().size(), i);
            }
            // Second page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(3);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<CodeMetamacDto> codesPagedResult = srmCoreServiceFacade.findCodesByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, codesPagedResult.getResults().size());
                assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(codesPagedResult.getResults().size(), i);
            }
            // Third page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(6);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<CodeMetamacDto> codesPagedResult = srmCoreServiceFacade.findCodesByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(2, codesPagedResult.getResults().size());
                assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(codesPagedResult.getResults().size(), i);
            }
        }
    }

    @Test
    public void testDeleteCode() throws Exception {
        String urn = CODELIST_1_V2_CODE_3;

        // Delete code
        srmCoreServiceFacade.deleteCode(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
            fail("Code deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrn() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        List<ItemHierarchyDto> codes = srmCoreServiceFacade.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn);

        // Validate
        assertEquals(4, codes.size());
        {
            // Code 01
            ItemHierarchyDto code = codes.get(0);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_1, code.getItem().getUrn());
            assertEquals(0, code.getChildren().size());
        }
        {
            // Code 02
            ItemHierarchyDto code = codes.get(1);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_2, code.getItem().getUrn());
            assertEquals(1, code.getChildren().size());
            {
                // Code 02 01
                ItemHierarchyDto codeChild = (ItemHierarchyDto) code.getChildren().get(0);
                assertTrue(codeChild.getItem() instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2_CODE_2_1, codeChild.getItem().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 02 01 01
                    ItemHierarchyDto codeChildChild = (ItemHierarchyDto) codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_2_1_1, codeChildChild.getItem().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
        {
            // Code 03
            ItemHierarchyDto code = codes.get(2);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_3, code.getItem().getUrn());
            assertEquals(0, code.getChildren().size());
        }
        {
            // Code 04
            ItemHierarchyDto code = codes.get(3);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_4, code.getItem().getUrn());
            assertEquals(1, code.getChildren().size());
            {
                // Code 04 01
                ItemHierarchyDto codeChild = (ItemHierarchyDto) code.getChildren().get(0);
                assertEquals(CODELIST_1_V2_CODE_4_1, codeChild.getItem().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 04 01 01
                    ItemHierarchyDto codeChildChild = (ItemHierarchyDto) codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_4_1_1, codeChildChild.getItem().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }

}

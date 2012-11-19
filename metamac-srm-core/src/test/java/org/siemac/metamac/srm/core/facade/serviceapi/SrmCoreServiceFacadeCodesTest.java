package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistMetamacDto;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
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

    // ---------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }

}

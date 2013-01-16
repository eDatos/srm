package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsInternationalStringDto;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodeDto;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistMetamacDto;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistOrderVisualisationDto;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.siemac.metamac.srm.core.code.dto.CodeHierarchyDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableFamilyCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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
        codelistDto.getReplaceToCodelists().add(new RelatedResourceDto("CODELIST01", CODELIST_1_V1, null));
        codelistDto.getReplaceToCodelists().add(new RelatedResourceDto("CODELIST10", CODELIST_10_V1, null));
        codelistDto.setFamily(new RelatedResourceDto("CODELIST_FAMILY_01", CODELIST_FAMILY_1, null));
        codelistDto.setVariable(new RelatedResourceDto("VARIABLE_01", VARIABLE_1, null));
        CodelistMetamacDto codelistMetamacCreated = srmCoreServiceFacade.createCodelist(getServiceContextAdministrador(), codelistDto);

        // Validate some metadata
        assertEquals(codelistDto.getCode(), codelistMetamacCreated.getCode());
        assertNotNull(codelistMetamacCreated.getUrn());
        assertEquals(ProcStatusEnum.DRAFT, codelistMetamacCreated.getLifeCycle().getProcStatus());
        assertEquals(2, codelistMetamacCreated.getReplaceToCodelists().size());
        assertEquals(CODELIST_1_V1, codelistMetamacCreated.getReplaceToCodelists().get(0).getUrn());
        assertEquals(CODELIST_10_V1, codelistMetamacCreated.getReplaceToCodelists().get(1).getUrn());
        assertEquals(CODELIST_FAMILY_1, codelistMetamacCreated.getFamily().getUrn());
        assertEquals(VARIABLE_1, codelistMetamacCreated.getVariable().getUrn());
    }

    @Test
    public void testUpdateCodelist() throws Exception {
        // Update
        CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_2_V1);
        assertEquals(1, codelistMetamacDto.getReplaceToCodelists().size());
        assertEquals(CODELIST_1_V1, codelistMetamacDto.getReplaceToCodelists().get(0).getUrn());

        codelistMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        // add two replace to
        codelistMetamacDto.getReplaceToCodelists().add(CodesMetamacDtoMocks.mockCodelistRelatedResourceDto("CODELIST03", CODELIST_3_V1));
        codelistMetamacDto.getReplaceToCodelists().add(CodesMetamacDtoMocks.mockCodelistRelatedResourceDto("CODELIST10", CODELIST_10_V1));
        CodelistMetamacDto codelistMetamacDtoUpdated = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDto);

        // Validate
        assertNotNull(codelistMetamacDtoUpdated);
        assertEqualsCodelistMetamacDto(codelistMetamacDto, codelistMetamacDtoUpdated);
        assertTrue(codelistMetamacDtoUpdated.getVersionOptimisticLocking() > codelistMetamacDto.getVersionOptimisticLocking());
        assertEquals(3, codelistMetamacDtoUpdated.getReplaceToCodelists().size());
        assertEquals(CODELIST_1_V1, codelistMetamacDtoUpdated.getReplaceToCodelists().get(0).getUrn());
        assertEquals(CODELIST_3_V1, codelistMetamacDtoUpdated.getReplaceToCodelists().get(1).getUrn());
        assertEquals(CODELIST_10_V1, codelistMetamacDtoUpdated.getReplaceToCodelists().get(2).getUrn());

        // Validate replaced by
        assertEquals(CODELIST_2_V1, srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_3_V1).getReplacedByCodelist().getUrn());
        assertEquals(CODELIST_2_V1, srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_10_V1).getReplacedByCodelist().getUrn());
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
        CodelistMetamacDto codelistDtoNewVersion = srmCoreServiceFacade.versioningCodelist(getServiceContextAdministrador(), urn, null, VersionTypeEnum.MAJOR);

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
        assertNull(codeMetamacDtoCreated.getUriProvider());

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

        assertNotNull(codeMetamacDtoUpdated);
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
            assertEquals(29, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(29, codesPagedResult.getResults().size());
            assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V1, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CODELIST_1_V1_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_2, codesPagedResult.getResults().get(i++).getUrn());
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
                assertEquals(9, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
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
                assertEquals(9, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, codesPagedResult.getResults().size());
                assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_2_2, codesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getResults().get(i++).getUrn());
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
                assertEquals(9, codesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, codesPagedResult.getResults().size());
                assertTrue(codesPagedResult.getResults().get(0) instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2, codesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getResults().get(i++).getUrn());
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
        List<CodeHierarchyDto> codes = srmCoreServiceFacade.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, null);

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
            assertEquals(2, code.getChildren().size());
            {
                // Code 02 01
                ItemHierarchyDto codeChild = code.getChildren().get(0);
                assertTrue(codeChild.getItem() instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2_CODE_2_1, codeChild.getItem().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 02 01 01
                    ItemHierarchyDto codeChildChild = codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_2_1_1, codeChildChild.getItem().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
            {
                // Code 02 02
                ItemHierarchyDto codeChild = code.getChildren().get(1);
                assertTrue(codeChild.getItem() instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2_CODE_2_2, codeChild.getItem().getUrn());
                assertEquals(0, codeChild.getChildren().size());
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
                ItemHierarchyDto codeChild = code.getChildren().get(0);
                assertEquals(CODELIST_1_V2_CODE_4_1, codeChild.getItem().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 04 01 01
                    ItemHierarchyDto codeChildChild = codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_4_1_1, codeChildChild.getItem().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrnOrdered() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        String codelistOrderVisualisationIdentifier = CODELIST_1_V2_ORDER_VISUALISATION_02;

        List<CodeHierarchyDto> codes = srmCoreServiceFacade.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, codelistOrderVisualisationIdentifier);

        // Validate
        assertEquals(4, codes.size());
        {
            // Code 03
            ItemHierarchyDto code = codes.get(0);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_3, code.getItem().getUrn());
            assertEquals(0, code.getChildren().size());
        }
        {
            // Code 01
            ItemHierarchyDto code = codes.get(1);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_1, code.getItem().getUrn());
            assertEquals(0, code.getChildren().size());
        }
        {
            // Code 02
            ItemHierarchyDto code = codes.get(2);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_2, code.getItem().getUrn());
            assertEquals(2, code.getChildren().size());
            {
                // Code 02 02
                ItemHierarchyDto codeChild = code.getChildren().get(0);
                assertTrue(codeChild.getItem() instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2_CODE_2_2, codeChild.getItem().getUrn());
                assertEquals(0, codeChild.getChildren().size());
            }
            {
                // Code 02 01
                ItemHierarchyDto codeChild = code.getChildren().get(1);
                assertTrue(codeChild.getItem() instanceof CodeMetamacDto);
                assertEquals(CODELIST_1_V2_CODE_2_1, codeChild.getItem().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 02 01 01
                    ItemHierarchyDto codeChildChild = codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_2_1_1, codeChildChild.getItem().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
        {
            // Code 04
            ItemHierarchyDto code = codes.get(3);
            assertTrue(code.getItem() instanceof CodeMetamacDto);
            assertEquals(CODELIST_1_V2_CODE_4, code.getItem().getUrn());
            assertEquals(1, code.getChildren().size());
            {
                // Code 04 01
                ItemHierarchyDto codeChild = code.getChildren().get(0);
                assertEquals(CODELIST_1_V2_CODE_4_1, codeChild.getItem().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 04 01 01
                    ItemHierarchyDto codeChildChild = codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_4_1_1, codeChildChild.getItem().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCodelistFamilyByUrn() throws Exception {
        // Retrieve
        CodelistFamilyDto codelistFamilyDto = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);

        // Validate
        assertEquals(CODELIST_FAMILY_1, codelistFamilyDto.getUrn());
    }

    @Test
    public void testCreateCodelistFamily() throws Exception {
        // Create
        CodelistFamilyDto codelistFamilyDto = CodesMetamacDtoMocks.mockCodelistFamilyDto();
        CodelistFamilyDto codelistFamilyCreated = srmCoreServiceFacade.createCodelistFamily(getServiceContextAdministrador(), codelistFamilyDto);

        // Validate some metadata
        assertEquals(codelistFamilyDto.getCode(), codelistFamilyCreated.getCode());
        assertNotNull(codelistFamilyCreated.getUrn());
    }

    @Test
    public void testUpdateCodelistFamily() throws Exception {
        // Update
        CodelistFamilyDto codelistFamilyDto = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        codelistFamilyDto.setName(MetamacMocks.mockInternationalStringDto());
        CodelistFamilyDto codelistFamilyDtoUpdated = srmCoreServiceFacade.updateCodelistFamily(getServiceContextAdministrador(), codelistFamilyDto);

        // Validate
        assertNotNull(codelistFamilyDtoUpdated);
        CodesMetamacAsserts.assertEqualsCodelistFamilyDto(codelistFamilyDto, codelistFamilyDtoUpdated);
        assertTrue(codelistFamilyDtoUpdated.getVersionOptimisticLocking() > codelistFamilyDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCodelistFamilyErrorOptimisticLocking() throws Exception {
        String urn = CODELIST_FAMILY_1;

        CodelistFamilyDto codelistFamilyDtoSession1 = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), codelistFamilyDtoSession1.getVersionOptimisticLocking());
        codelistFamilyDtoSession1.setName(MetamacMocks.mockInternationalStringDto());

        CodelistFamilyDto codelistFamilyDtoSession2 = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), codelistFamilyDtoSession2.getVersionOptimisticLocking());
        codelistFamilyDtoSession2.setName(MetamacMocks.mockInternationalStringDto());

        // Update by session 1
        CodelistFamilyDto codelistFamilyDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateCodelistFamily(getServiceContextAdministrador(), codelistFamilyDtoSession1);
        assertTrue(codelistFamilyDtoSession1AfterUpdate1.getVersionOptimisticLocking() > codelistFamilyDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateCodelistFamily(getServiceContextAdministrador(), codelistFamilyDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        codelistFamilyDtoSession1AfterUpdate1.setName(MetamacMocks.mockInternationalStringDto());
        CodelistFamilyDto codelistFamilyDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateCodelistFamily(getServiceContextAdministrador(), codelistFamilyDtoSession1AfterUpdate1);
        assertTrue(codelistFamilyDtoSession1AfterUpdate2.getVersionOptimisticLocking() > codelistFamilyDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteCodelistFamily() throws Exception {
        String urn = CODELIST_FAMILY_2;

        // Delete codelist
        srmCoreServiceFacade.deleteCodelistFamily(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), urn);
            fail("CodelistFamily deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindCodelistFamiliesByCondition() throws Exception {
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

        // Find all
        {
            MetamacCriteriaResult<CodelistFamilyDto> result = srmCoreServiceFacade.findCodelistFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CodelistFamilyDto codelistFamilyDto = result.getResults().get(i++);
                assertEquals(CODELIST_FAMILY_1, codelistFamilyDto.getUrn());
            }
            {
                CodelistFamilyDto codelistFamilyDto = result.getResults().get(i++);
                assertEquals(CODELIST_FAMILY_2, codelistFamilyDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), "familia-de-codelists-51", OperationType.EQ));

            MetamacCriteriaResult<CodelistFamilyDto> result = srmCoreServiceFacade.findCodelistFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CodelistFamilyDto codelistFamilyDto = result.getResults().get(i++);
                assertEquals(CODELIST_FAMILY_1, codelistFamilyDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findCodelistFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindCodelistFamiliesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        // Pagination
        int maxResultSize = 1;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);
        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<CodelistFamilyDto> result = srmCoreServiceFacade.findCodelistFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(1, result.getResults().size());
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CODELIST_FAMILY_1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 1;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<CodelistFamilyDto> result = srmCoreServiceFacade.findCodelistFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(1, result.getResults().size());
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CODELIST_FAMILY_2, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testAddCodelistsToCodelistFamily() throws Exception {

        String codelistFamilyUrn = CODELIST_FAMILY_1;
        List<String> codelistUrns = new ArrayList<String>();
        {
            String codelistUrn = CODELIST_9_V1; // change family
            codelistUrns.add(codelistUrn);
            CodelistMetamacDto codelist = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
            assertEquals(CODELIST_FAMILY_2, codelist.getFamily().getUrn());
        }
        {
            String codelistUrn = CODELIST_2_V1; // add family
            codelistUrns.add(codelistUrn);
            CodelistMetamacDto codelist = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
            assertNull(codelist.getFamily());
        }
        srmCoreServiceFacade.addCodelistsToCodelistFamily(getServiceContextAdministrador(), codelistUrns, codelistFamilyUrn);

        // Validation
        {
            CodelistMetamacDto codelist = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);
            assertEquals(codelistFamilyUrn, codelist.getFamily().getUrn());
        }
        {
            CodelistMetamacDto codelist = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_2_V1);
            assertEquals(codelistFamilyUrn, codelist.getFamily().getUrn());
        }
    }

    @Test
    public void testRemoveCodelistFromCodelistFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String codelistFamily = CODELIST_FAMILY_2;
        String codelist = CODELIST_1_V2;

        CodelistFamilyDto family = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(ctx, codelistFamily);
        CodelistMetamacDto codelistVersion = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, codelist);
        assertEquals(family.getUrn(), codelistVersion.getFamily().getUrn());

        srmCoreServiceFacade.removeCodelistFromCodelistFamily(ctx, codelist, codelistFamily);

        family = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(ctx, codelistFamily);
        codelistVersion = srmCoreServiceFacade.retrieveCodelistByUrn(ctx, codelist);
        assertNull(codelistVersion.getFamily());
    }

    // ---------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveVariableFamilyByUrn() throws Exception {
        // Retrieve
        VariableFamilyDto variableFamilyDto = srmCoreServiceFacade.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1);

        // Validate
        assertEquals(VARIABLE_FAMILY_1, variableFamilyDto.getUrn());
    }
    //
    @Test
    public void testCreateVariableFamily() throws Exception {
        // Create
        VariableFamilyDto variableFamilyDto = CodesMetamacDtoMocks.mockVariableFamilyDto();
        VariableFamilyDto variableFamilyCreated = srmCoreServiceFacade.createVariableFamily(getServiceContextAdministrador(), variableFamilyDto);

        // Validate some metadata
        assertEquals(variableFamilyDto.getCode(), variableFamilyCreated.getCode());
        assertNotNull(variableFamilyCreated.getUrn());
    }

    @Test
    public void testUpdateVariableFamily() throws Exception {
        // Update
        VariableFamilyDto variableFamilyDto = srmCoreServiceFacade.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_2);
        variableFamilyDto.setName(MetamacMocks.mockInternationalStringDto());
        VariableFamilyDto variableFamilyDtoUpdated = srmCoreServiceFacade.updateVariableFamily(getServiceContextAdministrador(), variableFamilyDto);

        // Validate
        assertNotNull(variableFamilyDtoUpdated);
        CodesMetamacAsserts.assertEqualsVariableFamilyDto(variableFamilyDto, variableFamilyDtoUpdated);
        assertTrue(variableFamilyDtoUpdated.getVersionOptimisticLocking() > variableFamilyDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateVariableFamilyErrorOptimisticLocking() throws Exception {
        String urn = VARIABLE_FAMILY_1;

        VariableFamilyDto variableFamilyDtoSession1 = srmCoreServiceFacade.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), variableFamilyDtoSession1.getVersionOptimisticLocking());
        variableFamilyDtoSession1.setName(MetamacMocks.mockInternationalStringDto());

        VariableFamilyDto variableFamilyDtoSession2 = srmCoreServiceFacade.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), variableFamilyDtoSession2.getVersionOptimisticLocking());
        variableFamilyDtoSession2.setName(MetamacMocks.mockInternationalStringDto());

        // Update by session 1
        VariableFamilyDto variableFamilyDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateVariableFamily(getServiceContextAdministrador(), variableFamilyDtoSession1);
        assertTrue(variableFamilyDtoSession1AfterUpdate1.getVersionOptimisticLocking() > variableFamilyDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateVariableFamily(getServiceContextAdministrador(), variableFamilyDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        variableFamilyDtoSession1AfterUpdate1.setName(MetamacMocks.mockInternationalStringDto());
        VariableFamilyDto variableFamilyDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateVariableFamily(getServiceContextAdministrador(), variableFamilyDtoSession1AfterUpdate1);
        assertTrue(variableFamilyDtoSession1AfterUpdate2.getVersionOptimisticLocking() > variableFamilyDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteVariableFamily() throws Exception {
        String urn = VARIABLE_FAMILY_2;

        // Delete codelist
        srmCoreServiceFacade.deleteVariableFamily(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), urn);
            fail("VariableFamily deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindVariableFamiliesByCondition() throws Exception {
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

        // Find all
        {
            MetamacCriteriaResult<VariableFamilyDto> result = srmCoreServiceFacade.findVariableFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                VariableFamilyDto variableFamilyDto = result.getResults().get(i++);
                assertEquals(VARIABLE_FAMILY_1, variableFamilyDto.getUrn());
            }
            {
                VariableFamilyDto variableFamilyDto = result.getResults().get(i++);
                assertEquals(VARIABLE_FAMILY_2, variableFamilyDto.getUrn());
            }
            {
                VariableFamilyDto variableFamilyDto = result.getResults().get(i++);
                assertEquals(VARIABLE_FAMILY_3, variableFamilyDto.getUrn());
            }
            {
                VariableFamilyDto variableFamilyDto = result.getResults().get(i++);
                assertEquals(VARIABLE_FAMILY_4, variableFamilyDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(VariableFamilyCriteriaPropertyEnum.NAME.name(), "familia-de-variables-53", OperationType.EQ));

            MetamacCriteriaResult<VariableFamilyDto> result = srmCoreServiceFacade.findVariableFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                VariableFamilyDto variableFamilyDto = result.getResults().get(i++);
                assertEquals(VARIABLE_FAMILY_1, variableFamilyDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(VariableFamilyCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findVariableFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindVariableFamiliesPaginated() throws Exception {
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

            MetamacCriteriaResult<VariableFamilyDto> result = srmCoreServiceFacade.findVariableFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getResults().size());
            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(VARIABLE_FAMILY_1, result.getResults().get(0).getUrn());
            assertEquals(VARIABLE_FAMILY_2, result.getResults().get(1).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<VariableFamilyDto> result = srmCoreServiceFacade.findVariableFamiliesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getResults().size());
            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(VARIABLE_FAMILY_3, result.getResults().get(0).getUrn());
            assertEquals(VARIABLE_FAMILY_4, result.getResults().get(1).getUrn());
        }
    }

    // ---------------------------------------------------------------------------------------
    // VARIABLES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveVariableByUrn() throws Exception {
        // Retrieve
        VariableDto variableDto = srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);

        // Validate
        assertEquals(VARIABLE_1, variableDto.getUrn());
    }

    @Test
    public void testCreateVariable() throws Exception {
        // Create
        VariableDto variableDto = CodesMetamacDtoMocks.mockVariableDto();
        variableDto.addFamily(CodesMetamacDtoMocks.mockVariableFamilyRelatedResourceDto("VARIABLE_FAMILY_01", VARIABLE_FAMILY_1));
        variableDto.addFamily(CodesMetamacDtoMocks.mockVariableFamilyRelatedResourceDto("VARIABLE_FAMILY_02", VARIABLE_FAMILY_2));
        variableDto.getReplaceToVariables().add(new RelatedResourceDto("VARIABLE_01", VARIABLE_1, null));
        variableDto.getReplaceToVariables().add(new RelatedResourceDto("VARIABLE_03", VARIABLE_3, null));

        VariableDto variableCreated = srmCoreServiceFacade.createVariable(getServiceContextAdministrador(), variableDto);

        // Validate some metadata
        assertEquals(variableDto.getCode(), variableCreated.getCode());
        assertNotNull(variableCreated.getUrn());
        assertEquals(2, variableCreated.getFamilies().size());
        assertEquals(2, variableCreated.getReplaceToVariables().size());
        assertEquals(VARIABLE_1, variableCreated.getReplaceToVariables().get(0).getUrn());
        assertEquals(VARIABLE_3, variableCreated.getReplaceToVariables().get(1).getUrn());

        // Retrieve variable to check replacedBy
        VariableDto variableReplaced = srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        assertEquals(variableCreated.getUrn(), variableReplaced.getReplacedByVariable().getUrn());
    }

    @Test
    public void testUpdateVariable() throws Exception {
        VariableDto variableDto = srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_3);
        // families
        assertEquals(2, variableDto.getFamilies().size());
        RelatedResourceDto family2 = SrmServiceUtils.getRelatedResource(VARIABLE_FAMILY_2, variableDto.getFamilies());
        assertNotNull(family2);
        RelatedResourceDto family3 = SrmServiceUtils.getRelatedResource(VARIABLE_FAMILY_3, variableDto.getFamilies());
        assertNotNull(family3);
        // replace to
        assertEquals(2, variableDto.getReplaceToVariables().size());
        RelatedResourceDto variable1 = SrmServiceUtils.getRelatedResource(VARIABLE_1, variableDto.getReplaceToVariables());
        assertNotNull(variable1);
        RelatedResourceDto variable2 = SrmServiceUtils.getRelatedResource(VARIABLE_2, variableDto.getReplaceToVariables());
        assertNotNull(variable2);

        // Update
        variableDto.setName(MetamacMocks.mockInternationalStringDto());
        variableDto.setShortName(MetamacMocks.mockInternationalStringDto());
        variableDto.setValidTo(new Date());
        // change families
        variableDto.removeFamily(family2);
        variableDto.addFamily(CodesMetamacDtoMocks.mockVariableFamilyRelatedResourceDto("VARIABLE_FAMILY_01", VARIABLE_FAMILY_1));
        // change replace to
        variableDto.removeReplaceToVariable(variable2);
        variableDto.addReplaceToVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_04", VARIABLE_4));

        VariableDto variableDtoUpdated = srmCoreServiceFacade.updateVariable(getServiceContextAdministrador(), variableDto);

        // Validate
        assertNotNull(variableDtoUpdated);
        CodesMetamacAsserts.assertEqualsVariableDto(variableDto, variableDtoUpdated);
        assertTrue(variableDtoUpdated.getVersionOptimisticLocking() > variableDto.getVersionOptimisticLocking());

        // Validate replaced by
        assertNull(srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_2).getReplacedByVariable());
        assertEquals(VARIABLE_3, srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1).getReplacedByVariable().getUrn());
        assertEquals(VARIABLE_3, srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_4).getReplacedByVariable().getUrn());
    }

    @Test
    public void testUpdateVariableErrorOptimisticLocking() throws Exception {
        String urn = VARIABLE_1;

        VariableDto variableDtoSession1 = srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), variableDtoSession1.getVersionOptimisticLocking());
        variableDtoSession1.setName(MetamacMocks.mockInternationalStringDto());

        VariableDto variableDtoSession2 = srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), variableDtoSession2.getVersionOptimisticLocking());
        variableDtoSession2.setName(MetamacMocks.mockInternationalStringDto());

        // Update by session 1
        VariableDto variableDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateVariable(getServiceContextAdministrador(), variableDtoSession1);
        assertTrue(variableDtoSession1AfterUpdate1.getVersionOptimisticLocking() > variableDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateVariable(getServiceContextAdministrador(), variableDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        variableDtoSession1AfterUpdate1.setName(MetamacMocks.mockInternationalStringDto());
        VariableDto variableDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateVariable(getServiceContextAdministrador(), variableDtoSession1AfterUpdate1);
        assertTrue(variableDtoSession1AfterUpdate2.getVersionOptimisticLocking() > variableDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteVariable() throws Exception {
        String urn = VARIABLE_1;

        // Delete codelist
        srmCoreServiceFacade.deleteVariable(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveVariableByUrn(getServiceContextAdministrador(), urn);
            fail("Variable deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindVariablesByCondition() throws Exception {
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

        // Find all
        {
            MetamacCriteriaResult<VariableDto> result = srmCoreServiceFacade.findVariablesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(6, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_1, variableDto.getUrn());
            }
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_2, variableDto.getUrn());
            }
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_3, variableDto.getUrn());
            }
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_4, variableDto.getUrn());
            }
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_5, variableDto.getUrn());
            }
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_6, variableDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.NAME.name(), "variable--59", OperationType.EQ));

            MetamacCriteriaResult<VariableDto> result = srmCoreServiceFacade.findVariablesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                VariableDto variableDto = result.getResults().get(i++);
                assertEquals(VARIABLE_3, variableDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findVariablesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindVariablesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        // Pagination
        int maxResultSize = 4;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);
        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<VariableDto> result = srmCoreServiceFacade.findVariablesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getResults().size());
            assertEquals(6, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(VARIABLE_1, result.getResults().get(0).getUrn());
            assertEquals(VARIABLE_2, result.getResults().get(1).getUrn());
            assertEquals(VARIABLE_3, result.getResults().get(2).getUrn());
            assertEquals(VARIABLE_4, result.getResults().get(3).getUrn());
        }
        {
            int firstResult = 4;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<VariableDto> result = srmCoreServiceFacade.findVariablesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getResults().size());
            assertEquals(6, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(VARIABLE_5, result.getResults().get(0).getUrn());
            assertEquals(VARIABLE_6, result.getResults().get(1).getUrn());
        }
    }

    @Test
    public void testAddVariablesToVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableUrn = VARIABLE_1;
        String variableFamilyUrn = VARIABLE_FAMILY_1;

        VariableDto variable = srmCoreServiceFacade.retrieveVariableByUrn(ctx, variableUrn);
        assertNull(SrmServiceUtils.getRelatedResource(variableFamilyUrn, variable.getFamilies()));

        List<String> variablesUrn = Arrays.asList(variableUrn);
        srmCoreServiceFacade.addVariablesToVariableFamily(ctx, variablesUrn, variableFamilyUrn);

        variable = srmCoreServiceFacade.retrieveVariableByUrn(ctx, variableUrn);
        assertNotNull(SrmServiceUtils.getRelatedResource(variableFamilyUrn, variable.getFamilies()));
    }

    @Test
    public void testRemoveVariableFromVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String variableUrn = VARIABLE_3;
        String variableFamilyUrn = VARIABLE_FAMILY_3;

        VariableDto variable = srmCoreServiceFacade.retrieveVariableByUrn(ctx, variableUrn);
        assertNotNull(SrmServiceUtils.getRelatedResource(variableFamilyUrn, variable.getFamilies()));

        srmCoreServiceFacade.removeVariableFromVariableFamily(ctx, variableUrn, variableFamilyUrn);

        variable = srmCoreServiceFacade.retrieveVariableByUrn(ctx, variableUrn);
        assertNull(SrmServiceUtils.getRelatedResource(variableFamilyUrn, variable.getFamilies()));
    }

    // ---------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveVariableElementByUrn() throws Exception {
        // Retrieve
        VariableElementDto variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);

        // Validate
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, variableElementDto.getUrn());
    }

    @Test
    public void testCreateVariableElement() throws Exception {
        // Create
        VariableElementDto variableElementDto = CodesMetamacDtoMocks.mockVariableElementDto();
        variableElementDto.setVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_01", VARIABLE_1));
        // TODO replaceTo, replacedBy

        VariableElementDto variableElementCreated = srmCoreServiceFacade.createVariableElement(getServiceContextAdministrador(), variableElementDto);

        // Validate some metadata
        assertEquals(variableElementDto.getCode(), variableElementCreated.getCode());
        assertNotNull(variableElementCreated.getUrn());
        assertEquals(VARIABLE_1, variableElementCreated.getVariable().getUrn());
    }

    @Test
    public void testUpdateVariableElement() throws Exception {
        VariableElementDto variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);

        // Update
        variableElementDto.setName(MetamacMocks.mockInternationalStringDto());
        variableElementDto.setShortName(MetamacMocks.mockInternationalStringDto());
        variableElementDto.setValidTo(new Date());
        variableElementDto.setVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_01", VARIABLE_1));
        // TODO replaceTo, replacedBy

        VariableElementDto variableElementDtoUpdated = srmCoreServiceFacade.updateVariableElement(getServiceContextAdministrador(), variableElementDto);

        // Validate
        assertNotNull(variableElementDtoUpdated);
        CodesMetamacAsserts.assertEqualsVariableElementDto(variableElementDto, variableElementDtoUpdated);
        assertTrue(variableElementDtoUpdated.getVersionOptimisticLocking() > variableElementDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateVariableElementErrorOptimisticLocking() throws Exception {
        String urn = VARIABLE_2_VARIABLE_ELEMENT_1;

        VariableElementDto variableElementDtoSession1 = srmCoreServiceFacade.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), variableElementDtoSession1.getVersionOptimisticLocking());
        variableElementDtoSession1.setName(MetamacMocks.mockInternationalStringDto());

        VariableElementDto variableElementDtoSession2 = srmCoreServiceFacade.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), variableElementDtoSession2.getVersionOptimisticLocking());
        variableElementDtoSession2.setName(MetamacMocks.mockInternationalStringDto());

        // Update by session 1
        VariableElementDto variableElementDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateVariableElement(getServiceContextAdministrador(), variableElementDtoSession1);
        assertTrue(variableElementDtoSession1AfterUpdate1.getVersionOptimisticLocking() > variableElementDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateVariableElement(getServiceContextAdministrador(), variableElementDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        variableElementDtoSession1AfterUpdate1.setName(MetamacMocks.mockInternationalStringDto());
        VariableElementDto variableElementDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateVariableElement(getServiceContextAdministrador(), variableElementDtoSession1AfterUpdate1);
        assertTrue(variableElementDtoSession1AfterUpdate2.getVersionOptimisticLocking() > variableElementDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteVariableElement() throws Exception {
        String urn = VARIABLE_2_VARIABLE_ELEMENT_1;

        // Delete codelist
        srmCoreServiceFacade.deleteVariableElement(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);
            fail("VariableElement deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindVariableElementsByCondition() throws Exception {
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

        // Find all
        {
            MetamacCriteriaResult<VariableElementDto> result = srmCoreServiceFacade.findVariableElementsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                VariableElementDto variableElementDto = result.getResults().get(i++);
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, variableElementDto.getUrn());
            }
            {
                VariableElementDto variableElementDto = result.getResults().get(i++);
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, variableElementDto.getUrn());
            }
            {
                VariableElementDto variableElementDto = result.getResults().get(i++);
                assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, variableElementDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.NAME.name(), "-1", OperationType.LIKE));

            MetamacCriteriaResult<VariableElementDto> result = srmCoreServiceFacade.findVariableElementsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                VariableElementDto variableElementDto = result.getResults().get(i++);
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, variableElementDto.getUrn());
            }
            {
                VariableElementDto variableElementDto = result.getResults().get(i++);
                assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, variableElementDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findVariableElementsByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindVariableElementsPaginated() throws Exception {
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

            MetamacCriteriaResult<VariableElementDto> result = srmCoreServiceFacade.findVariableElementsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getResults().size());
            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getResults().get(0).getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, result.getResults().get(1).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<VariableElementDto> result = srmCoreServiceFacade.findVariableElementsByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(1, result.getResults().size());
            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testAddVariableElementsToVariable() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_1;
        String variableUrn = VARIABLE_1;

        VariableElementDto variableElement = srmCoreServiceFacade.retrieveVariableElementByUrn(ctx, variableElementUrn);
        assertFalse(variableElement.getVariable().getUrn().equals(variableUrn));

        List<String> variableElementsUrn = Arrays.asList(variableElementUrn);
        srmCoreServiceFacade.addVariableElementsToVariable(ctx, variableElementsUrn, variableUrn);

        variableElement = srmCoreServiceFacade.retrieveVariableElementByUrn(ctx, variableElementUrn);
        assertEquals(variableUrn, variableElement.getVariable().getUrn());
    }

    // ------------------------------------------------------------------------------------
    // CODELIST VISUALISATIONS
    // ------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCodelistOrderVisualisationByIdentifier() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        String orderVisualisationIdentifier = CODELIST_1_V2_ORDER_VISUALISATION_01;
        CodelistOrderVisualisationDto codelistOrderVisualisation = srmCoreServiceFacade.retrieveCodelistOrderVisualisationByIdentifier(getServiceContextAdministrador(), codelistUrn,
                orderVisualisationIdentifier);

        // Validate
        assertNotNull(codelistOrderVisualisation);
        assertEquals(CODELIST_1_V2_ORDER_VISUALISATION_01, codelistOrderVisualisation.getIdentifier());
        assertEqualsInternationalStringDto(codelistOrderVisualisation.getName(), "es", "visualización - órdenes 1-2-1", "en", "order - visualisation 1-2-1");
    }

    @Test
    public void testCreateCodelistOrderVisualisation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        CodelistOrderVisualisationDto codelistOrderVisualisation = CodesMetamacDtoMocks.mockCodelistOrderVisualisationDto();
        String codelistUrn = CODELIST_1_V2;

        // Create
        CodelistOrderVisualisationDto codelistOrderVisualisationCreated = srmCoreServiceFacade.createCodelistOrderVisualisation(ctx, codelistUrn, codelistOrderVisualisation);
        assertEqualsCodelistOrderVisualisationDto(codelistOrderVisualisation, codelistOrderVisualisationCreated);
    }

    @Test
    public void testUpdateCodelistOrderVisualisation() throws Exception {
        CodelistOrderVisualisationDto codelistOrderVisualisation = srmCoreServiceFacade.retrieveCodelistOrderVisualisationByIdentifier(getServiceContextAdministrador(), CODELIST_1_V2,
                CODELIST_1_V2_ORDER_VISUALISATION_01);
        codelistOrderVisualisation.setName(MetamacMocks.mockInternationalStringDto());

        // Update
        CodelistOrderVisualisationDto codelistOrderVisualisationUpdated = srmCoreServiceFacade.updateCodelistOrderVisualisation(getServiceContextAdministrador(), CODELIST_1_V2,
                codelistOrderVisualisation);

        // Validate
        assertEqualsCodelistOrderVisualisationDto(codelistOrderVisualisation, codelistOrderVisualisationUpdated);
    }

    @Test
    public void testDeleteCodelistOrderVisualisation() throws Exception {

        String codelistUrn = CODELIST_1_V2;
        String visualisationIdentifier = CODELIST_1_V2_ORDER_VISUALISATION_01;

        srmCoreServiceFacade.deleteCodelistOrderVisualisation(getServiceContextAdministrador(), codelistUrn, visualisationIdentifier);

        // Retrieve deleted visualisation
        try {
            srmCoreServiceFacade.retrieveCodelistOrderVisualisationByIdentifier(getServiceContextAdministrador(), codelistUrn, visualisationIdentifier);
            fail("visualisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODELIST_ORDER_VISUALISATION_NOT_FOUND, 2, new String[]{visualisationIdentifier, codelistUrn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveCodelistOrderVisualisationsByCodelist() throws Exception {
        String codelistUrn = CODELIST_1_V2;

        List<CodelistOrderVisualisationDto> visualisations = srmCoreServiceFacade.retrieveCodelistOrderVisualisationsByCodelist(getServiceContextAdministrador(), codelistUrn);

        assertEquals(2, visualisations.size());
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_01, visualisations);
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_02, visualisations);
    }

    @Test
    public void testUpdateCodeInOrderVisualisation() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String codeUrn = CODELIST_1_V2_CODE_1;
        String visualisationIdentifier = CODELIST_1_V2_ORDER_VISUALISATION_01;

        srmCoreServiceFacade.updateCodeInOrderVisualisation(getServiceContextAdministrador(), codeUrn, visualisationIdentifier, Long.valueOf(2));

        // Validate visualisation
        List<CodeHierarchyDto> codes = srmCoreServiceFacade.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, visualisationIdentifier);
        assertEquals(4, codes.size());
        assertEquals(CODELIST_1_V2_CODE_2, codes.get(0).getItem().getUrn());
        assertEquals(CODELIST_1_V2_CODE_3, codes.get(1).getItem().getUrn());
        assertEquals(CODELIST_1_V2_CODE_1, codes.get(2).getItem().getUrn());
        assertEquals(CODELIST_1_V2_CODE_4, codes.get(3).getItem().getUrn());
    }

    private void assertContainsCodelistOrderVisualisation(String codelistOrderVisualisationIdentifierExpected, List<CodelistOrderVisualisationDto> actuals) {
        for (CodelistOrderVisualisationDto actual : actuals) {
            if (actual.getIdentifier().equals(codelistOrderVisualisationIdentifierExpected)) {
                return;
            }
        }
        fail("visualisation not found");
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }

}

package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts.assertEqualsCategoryDto;
import static org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts.assertEqualsCategorySchemeMetamacDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
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
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaPropertyEnum;
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
public class SrmCoreServiceFacadeCategoriesTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // IMPORTANT: Metadata transformation is tested in Do2Dto tests

    // ---------------------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCategorySchemeByUrn() throws Exception {

        // Retrieve
        CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V1);

        // Validate
        assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeMetamacDto.getUrn());
    }

    @Test
    public void testRetrieveCategorySchemeVersions() throws Exception {

        // Retrieve all versions
        List<CategorySchemeMetamacDto> categorySchemeMetamacDtos = srmCoreServiceFacade.retrieveCategorySchemeVersions(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V1);

        // Validate
        assertEquals(2, categorySchemeMetamacDtos.size());
        assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeMetamacDtos.get(0).getUrn());
        assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeMetamacDtos.get(1).getUrn());
    }

    @Test
    public void testCreateCategoryScheme() throws Exception {

        // Create
        CategorySchemeMetamacDto categorySchemeDto = CategoriesMetamacDtoMocks.mockCategorySchemeDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        CategorySchemeMetamacDto categorySchemeMetamacCreated = srmCoreServiceFacade.createCategoryScheme(getServiceContextAdministrador(), categorySchemeDto);

        // Validate some metadata
        assertEquals(categorySchemeDto.getCode(), categorySchemeMetamacCreated.getCode());
        assertNotNull(categorySchemeMetamacCreated.getUrn());
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeMetamacCreated.getLifeCycle().getProcStatus());
        assertEquals(Long.valueOf(0), categorySchemeMetamacCreated.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCategoryScheme() throws Exception {

        // Update
        CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        categorySchemeMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        CategorySchemeMetamacDto categorySchemeMetamacDtoUpdated = srmCoreServiceFacade.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeMetamacDto);

        // Validate
        assertNotNull(categorySchemeMetamacDto);
        assertEqualsCategorySchemeMetamacDto(categorySchemeMetamacDto, categorySchemeMetamacDtoUpdated);
        assertTrue(categorySchemeMetamacDtoUpdated.getVersionOptimisticLocking() > categorySchemeMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCategorySchemeErrorOptimisticLocking() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2;

        CategorySchemeMetamacDto categorySchemeMetamacDtoSession1 = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), categorySchemeMetamacDtoSession1.getVersionOptimisticLocking());
        categorySchemeMetamacDtoSession1.setIsPartial(Boolean.TRUE);

        CategorySchemeMetamacDto categorySchemeMetamacDtoSession2 = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), categorySchemeMetamacDtoSession2.getVersionOptimisticLocking());
        categorySchemeMetamacDtoSession2.setIsPartial(Boolean.TRUE);

        // Update by session 1
        CategorySchemeMetamacDto categorySchemeMetamacDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeMetamacDtoSession1);
        assertTrue(categorySchemeMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking() > categorySchemeMetamacDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeMetamacDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        categorySchemeMetamacDtoSession1AfterUpdate1.setIsPartial(Boolean.FALSE);
        CategorySchemeMetamacDto categorySchemeMetamacDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateCategoryScheme(getServiceContextAdministrador(),
                categorySchemeMetamacDtoSession1AfterUpdate1);
        assertTrue(categorySchemeMetamacDtoSession1AfterUpdate2.getVersionOptimisticLocking() > categorySchemeMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteCategoryScheme() throws Exception {
        String urn = CATEGORY_SCHEME_2_V1;

        // Delete category scheme only with version in draft
        srmCoreServiceFacade.deleteCategoryScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            fail("CategoryScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindCategorySchemesByCondition() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.CODE.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(9, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_4_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_5_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_6_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_7_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }

        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "Nombre categoryScheme-1-v1", OperationType.EQ));

            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindCategorySchemesByProcStatus() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.CODE.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(order);
        }

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), ProcStatusEnum.DRAFT,
                    OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                CategorySchemeMetamacDto categorySchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindCategorySchemesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.CODE.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        // Pagination
        int maxResultSize = 2;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CATEGORY_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CATEGORY_SCHEME_2_V1, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        // Sends to production validation
        CategorySchemeMetamacDto categorySchemeDto = srmCoreServiceFacade.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);

        // Validation
        categorySchemeDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_5_V1;

        // Sends to diffusion validation
        CategorySchemeMetamacDto categorySchemeDto = srmCoreServiceFacade.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(), urn);

        // Validation
        categorySchemeDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testRejectCategorySchemeProductionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_5_V1;

        // Reject
        CategorySchemeMetamacDto categorySchemeDto = srmCoreServiceFacade.rejectCategorySchemeProductionValidation(getServiceContextAdministrador(), urn);

        // Validation
        categorySchemeDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_6_V1;

        // Reject
        CategorySchemeMetamacDto categorySchemeDto = srmCoreServiceFacade.rejectCategorySchemeDiffusionValidation(getServiceContextAdministrador(), urn);

        // Validation
        categorySchemeDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testPublishInternallyCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_6_V1;

        // Publish
        CategorySchemeMetamacDto categorySchemeDto = srmCoreServiceFacade.publishCategorySchemeInternally(getServiceContextAdministrador(), urn);

        // Validation
        categorySchemeDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testPublishExternallyCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_7_V2;

        // Publish
        CategorySchemeMetamacDto categorySchemeDto = srmCoreServiceFacade.publishCategorySchemeExternally(getServiceContextAdministrador(), urn);

        // Validation
        categorySchemeDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testVersioningCategoryScheme() throws Exception {

        // Versioning
        String urn = CATEGORY_SCHEME_3_V1;
        CategorySchemeMetamacDto categorySchemeDtoNewVersion = srmCoreServiceFacade.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate
        categorySchemeDtoNewVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeDtoNewVersion.getUrn());
        assertEquals("02.000", categorySchemeDtoNewVersion.getVersionLogic());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(02.000)", categorySchemeDtoNewVersion.getUrn());
    }

    @Test
    public void testEndCategorySchemeValidity() throws Exception {
        CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.endCategorySchemeValidity(getServiceContextAdministrador(), CATEGORY_SCHEME_7_V1);
        assertTrue(DateUtils.isSameDay(new Date(), categorySchemeMetamacDto.getValidTo()));
    }

    // ---------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCategoryByUrn() throws Exception {
        CategoryMetamacDto categoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoryMetamacDto.getUrn());
    }

    @Test
    public void testFindCategoriesByCondition() throws Exception {

        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CategoryMetamacCriteriaOrderEnum.CATEGORY_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CategoryMetamacCriteriaOrderEnum.CATEGORY_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CategoryMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<CategoryMetamacDto> categoriesPagedResult = srmCoreServiceFacade.findCategoriesByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(20, categoriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(20, categoriesPagedResult.getResults().size());
            assertTrue(categoriesPagedResult.getResults().get(0) instanceof CategoryMetamacDto);
            assertEquals(CATEGORY_SCHEME_1_V1, categoriesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(categoriesPagedResult.getResults().size(), i);
        }

        // Find only categories in first level
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CategoryMetamacCriteriaOrderEnum.CATEGORY_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CategoryMetamacCriteriaOrderEnum.CATEGORY_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(CategoryMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
            propertyRestriction.setPropertyName(CategoryMetamacCriteriaPropertyEnum.CATEGORY_PARENT_URN.name());
            propertyRestriction.setOperationType(OperationType.IS_NULL);
            metamacCriteria.setRestriction(propertyRestriction);

            // Find
            MetamacCriteriaResult<CategoryMetamacDto> categoriesPagedResult = srmCoreServiceFacade.findCategoriesByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(13, categoriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(13, categoriesPagedResult.getResults().size());
            assertTrue(categoriesPagedResult.getResults().get(0) instanceof CategoryMetamacDto);
            assertEquals(CATEGORY_SCHEME_1_V1, categoriesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());

            assertEquals(categoriesPagedResult.getResults().size(), i);
        }

        // Find by name (like), code (like) and category scheme urn
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(CategoryMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.NAME.name(), "Nombre categoryScheme-1-v2-category-2-", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CODE.name(), "CATEGORY02", OperationType.LIKE));
            conjunctionRestriction.getRestrictions()
                    .add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_URN.name(), CATEGORY_SCHEME_1_V2, OperationType.EQ));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<CategoryMetamacDto> categoriesPagedResult = srmCoreServiceFacade.findCategoriesByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, categoriesPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, categoriesPagedResult.getResults().size());
            assertTrue(categoriesPagedResult.getResults().get(0) instanceof CategoryMetamacDto);
            assertEquals(CATEGORY_SCHEME_1_V2, categoriesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getResults().get(i++).getUrn());
            assertEquals(categoriesPagedResult.getResults().size(), i);
        }

        // Find by category scheme urn paginated
        {

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(CategoryMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_URN.name(), CATEGORY_SCHEME_1_V2, OperationType.EQ));

            // First page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(0);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<CategoryMetamacDto> categoriesPagedResult = srmCoreServiceFacade.findCategoriesByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, categoriesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, categoriesPagedResult.getResults().size());
                assertTrue(categoriesPagedResult.getResults().get(0) instanceof CategoryMetamacDto);
                assertEquals(CATEGORY_SCHEME_1_V2, categoriesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(categoriesPagedResult.getResults().size(), i);
            }
            // Second page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(3);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<CategoryMetamacDto> categoriesPagedResult = srmCoreServiceFacade.findCategoriesByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, categoriesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, categoriesPagedResult.getResults().size());
                assertTrue(categoriesPagedResult.getResults().get(0) instanceof CategoryMetamacDto);
                assertEquals(CATEGORY_SCHEME_1_V2, categoriesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(categoriesPagedResult.getResults().size(), i);
            }
            // Third page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(6);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<CategoryMetamacDto> categoriesPagedResult = srmCoreServiceFacade.findCategoriesByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, categoriesPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(2, categoriesPagedResult.getResults().size());
                assertTrue(categoriesPagedResult.getResults().get(0) instanceof CategoryMetamacDto);
                assertEquals(CATEGORY_SCHEME_1_V2, categoriesPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getResults().get(i++).getUrn());
                assertEquals(categoriesPagedResult.getResults().size(), i);
            }
        }
    }

    @Test
    public void testCreateCategory() throws Exception {
        CategoryMetamacDto categoryMetamacDto = CategoriesMetamacDtoMocks.mockCategoryDto();
        categoryMetamacDto.setItemSchemeVersionUrn(CATEGORY_SCHEME_1_V2);

        CategoryMetamacDto categoryMetamacDtoCreated = srmCoreServiceFacade.createCategory(getServiceContextAdministrador(), categoryMetamacDto);
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME01(02.000)." + categoryMetamacDto.getCode(), categoryMetamacDtoCreated.getUrn());
        assertEqualsCategoryDto(categoryMetamacDto, categoryMetamacDtoCreated);
    }

    @Test
    public void testCreateCategoryWithCategoryParent() throws Exception {
        CategoryMetamacDto categoryMetamacDto = CategoriesMetamacDtoMocks.mockCategoryDto();
        categoryMetamacDto.setItemParentUrn(CATEGORY_SCHEME_1_V2_CATEGORY_1);
        categoryMetamacDto.setItemSchemeVersionUrn(CATEGORY_SCHEME_1_V2);

        CategoryMetamacDto categoryMetamacDtoCreated = srmCoreServiceFacade.createCategory(getServiceContextAdministrador(), categoryMetamacDto);
        assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoryMetamacDtoCreated.getItemParentUrn());
        assertEqualsCategoryDto(categoryMetamacDto, categoryMetamacDtoCreated);
    }

    @Test
    public void testCreateCategoryErrorParentNotExists() throws Exception {
        CategoryMetamacDto categoryMetamacDto = CategoriesMetamacDtoMocks.mockCategoryDto();
        categoryMetamacDto.setItemParentUrn(NOT_EXISTS);
        categoryMetamacDto.setItemSchemeVersionUrn(CATEGORY_SCHEME_1_V2);

        try {
            srmCoreServiceFacade.createCategory(getServiceContextAdministrador(), categoryMetamacDto);
            fail("wrong parent");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateCategory() throws Exception {
        CategoryMetamacDto categoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        categoryMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        categoryMetamacDto.setDescription(MetamacMocks.mockInternationalStringDto());

        CategoryMetamacDto categoryMetamacDtoUpdated = srmCoreServiceFacade.updateCategory(getServiceContextAdministrador(), categoryMetamacDto);

        assertEqualsCategoryDto(categoryMetamacDto, categoryMetamacDtoUpdated);
        assertTrue(categoryMetamacDtoUpdated.getVersionOptimisticLocking() > categoryMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteCategory() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_3;

        // Delete category
        srmCoreServiceFacade.deleteCategory(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
            fail("Category deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveCategoriesByCategorySchemeUrn() throws Exception {

        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        List<ItemHierarchyDto> categories = srmCoreServiceFacade.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn);

        // Validate
        assertEquals(4, categories.size());
        {
            // Category 01
            ItemHierarchyDto category = assertListContainsItemHierarchy(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
            assertTrue(category.getItem() instanceof CategoryMetamacDto);
            assertEquals(0, category.getChildren().size());
        }
        {
            // Category 02
            ItemHierarchyDto category = assertListContainsItemHierarchy(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
            assertTrue(category.getItem() instanceof CategoryMetamacDto);
            assertEquals(1, category.getChildren().size());
            {
                // Category 02 01
                ItemHierarchyDto categoryChild = assertListContainsItemHierarchy(category.getChildren(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertTrue(categoryChild.getItem() instanceof CategoryMetamacDto);
                assertEquals(1, categoryChild.getChildren().size());
                {
                    // Category 02 01 01
                    ItemHierarchyDto categoryChildChild = assertListContainsItemHierarchy(categoryChild.getChildren(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                    assertEquals(0, categoryChildChild.getChildren().size());
                }
            }
        }
        {
            // Category 03
            ItemHierarchyDto category = assertListContainsItemHierarchy(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
            assertTrue(category.getItem() instanceof CategoryMetamacDto);
            assertEquals(0, category.getChildren().size());
        }
        {
            // Category 04
            ItemHierarchyDto category = assertListContainsItemHierarchy(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
            assertTrue(category.getItem() instanceof CategoryMetamacDto);
            assertEquals(1, category.getChildren().size());
            {
                // Category 04 01
                ItemHierarchyDto categoryChild = assertListContainsItemHierarchy(category.getChildren(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(1, categoryChild.getChildren().size());
                {
                    // Category 04 01 01
                    ItemHierarchyDto categoryChildChild = assertListContainsItemHierarchy(categoryChild.getChildren(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                    assertEquals(0, categoryChildChild.getChildren().size());
                }
            }
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }
}
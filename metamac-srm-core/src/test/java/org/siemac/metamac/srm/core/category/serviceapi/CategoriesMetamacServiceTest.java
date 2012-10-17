package org.siemac.metamac.srm.core.category.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class CategoriesMetamacServiceTest extends SrmBaseTest implements CategoriesMetamacServiceTestBase {

    @Autowired
    private CategoriesMetamacService categoriesService;

    @Test
    public void testCreateCategoryScheme() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = CategoriesMetamacDoMocks.mockCategoryScheme();
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        CategorySchemeVersionMetamac categorySchemeVersionCreated = categoriesService.createCategoryScheme(ctx, categorySchemeVersion);
        String urn = categorySchemeVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", categorySchemeVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), categorySchemeVersionCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategorySchemeVersionMetamac categorySchemeVersionRetrieved = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(categorySchemeVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationUser());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationUser());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationUser());
        assertEquals(ctx.getUserId(), categorySchemeVersionRetrieved.getCreatedBy());
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(categorySchemeVersion, categorySchemeVersionRetrieved);
    }

    @Test
    public void testUpdateCategoryScheme() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1);
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        categorySchemeVersion.getMaintainableArtefact().setName(CategoriesMetamacDoMocks.mockInternationalString("name"));

        ServiceContext ctx = getServiceContextAdministrador();
        CategorySchemeVersion categorySchemeVersionUpdated = categoriesService.updateCategoryScheme(ctx, categorySchemeVersion);
        assertNotNull(categorySchemeVersionUpdated);
        assertEquals("user1", categorySchemeVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), categorySchemeVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateCategorySchemePublished() throws Exception {
        String[] urns = {CATEGORY_SCHEME_1_V1};
        for (String urn : urns) {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);

            try {
                categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
                categorySchemeVersion = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
                fail("wrong proc status");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }
        }
    }

    @Test
    public void testUpdateCategorySchemeErrorExternalReference() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        categorySchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);

        try {
            categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
            categorySchemeVersion = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
            fail("category scheme cannot be a external reference");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveCategorySchemeByUrn() throws Exception {

        // Retrieve
        String urn = CATEGORY_SCHEME_1_V1;
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, categorySchemeVersion.getMaintainableArtefact().getUrn());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Test
    @Override
    public void testFindCategorySchemesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                    .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().code()).orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categoriesService
                    .findCategorySchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(9, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(categorySchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find internally published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                    .withProperty(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                    .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categoriesService
                    .findCategorySchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(3, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(categorySchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find lasts versions
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                    .withProperty(CategorySchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE)
                    .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().code()).orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categoriesService
                    .findCategorySchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(7, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testRetrieveCategorySchemeVersions() throws Exception {

        // Retrieve all versions
        String urn = CATEGORY_SCHEME_1_V1;
        List<CategorySchemeVersionMetamac> categorySchemeVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, categorySchemeVersions.size());
        assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersions.get(1).getMaintainableArtefact().getUrn());
    }

    @Test
    public void testSendCategorySchemeToProductionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.sendCategorySchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationInProcStatusRejected() throws Exception {

        String urn = CATEGORY_SCHEME_4_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.sendCategorySchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorMetadataRequired() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        // Update to clear required metadata to send to production
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        categorySchemeVersion.setIsPartial(null);
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);

        // Send to production validation
        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Sends to diffusion validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.sendCategorySchemeToDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        try {
            categoriesService.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectCategorySchemeProductionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Reject validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.rejectCategorySchemeProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate restrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCategorySchemeProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.rejectCategorySchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectCategorySchemeProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.rejectCategorySchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Reject validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.rejectCategorySchemeDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.rejectCategorySchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.rejectCategorySchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishInternallyCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
        }

        // Publish internally
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.publishInternallyCategoryScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
        }
    }

    @Test
    public void testPublishInternallyCategorySchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishInternallyCategorySchemeErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishExternallyCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());

            CategorySchemeVersionMetamac categorySchemeVersionExternallyPublished = categoriesService.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertNull(categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.publishExternallyCategoryScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());
        }
        // Validate previous published externally versions
        {
            CategorySchemeVersionMetamac categorySchemeVersionExternallyPublished = categoriesService.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo().toDate()));
        }
    }

    @Test
    public void testPublishExternallyCategorySchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.publishExternallyCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishExternallyCategorySchemeErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.publishExternallyCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testDeleteCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        // Delete category scheme only with version in draft
        categoriesService.deleteCategoryScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            fail("CategoryScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteCategorySchemeWithVersionPublishedAndVersionDraft() throws Exception {

        String urnV1 = CATEGORY_SCHEME_1_V1;
        String urnV2 = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersionV1 = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersionV1.getLifeCycleMetadata().getProcStatus());
        CategorySchemeVersionMetamac categorySchemeVersionV2 = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV2);
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionV2.getLifeCycleMetadata().getProcStatus());

        categoriesService.deleteCategoryScheme(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV2);
            fail("CategoryScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        categorySchemeVersionV1 = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(categorySchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(categorySchemeVersionV1.getMaintainableArtefact().getReplacedBy());
    }

    @Test
    public void testDeleteCategorySchemeErrorPublished() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        // Validation
        try {
            categoriesService.deleteCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=ISTAC:CATEGORYSCHEME03(02.000)";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=ISTAC:CATEGORYSCHEME03(02.000).CATEGORY01";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=ISTAC:CATEGORYSCHEME03(02.000).CATEGORY02";
        String urnExpectedCategory21 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=ISTAC:CATEGORYSCHEME03(02.000).CATEGORY0201";
        String urnExpectedCategory211 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=ISTAC:CATEGORYSCHEME03(02.000).CATEGORY020101";
        String urnExpectedCategory22 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=ISTAC:CATEGORYSCHEME03(02.000).CATEGORY0202";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceTo());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedBy());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Categories
            assertEquals(5, categorySchemeVersionNewVersion.getItems().size());
            assertEquals(urnExpectedCategory1, categorySchemeVersionNewVersion.getItems().get(0).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCategory2, categorySchemeVersionNewVersion.getItems().get(1).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCategory21, categorySchemeVersionNewVersion.getItems().get(2).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCategory211, categorySchemeVersionNewVersion.getItems().get(3).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCategory22, categorySchemeVersionNewVersion.getItems().get(4).getNameableArtefact().getUrn());

            assertEquals(2, categorySchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                CategoryMetamac category = (CategoryMetamac) categorySchemeVersionNewVersion.getItemsFirstLevel().get(0);
                assertEquals(urnExpectedCategory1, category.getNameableArtefact().getUrn());
                assertEquals(0, category.getChildren().size());
            }
            {
                CategoryMetamac category = (CategoryMetamac) categorySchemeVersionNewVersion.getItemsFirstLevel().get(1);
                assertEquals(urnExpectedCategory2, category.getNameableArtefact().getUrn());
                {
                    Category categoryChild = (Category) category.getChildren().get(0);
                    assertEquals(urnExpectedCategory21, categoryChild.getNameableArtefact().getUrn());
                    {
                        Category categoryChildChild = (Category) categoryChild.getChildren().get(0);
                        assertEquals(urnExpectedCategory211, categoryChildChild.getNameableArtefact().getUrn());
                    }
                }
                {
                    Category categoryChild = (Category) category.getChildren().get(1);
                    assertEquals(urnExpectedCategory22, categoryChild.getNameableArtefact().getUrn());
                }
            }
        }

        // Copied version
        {
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, categorySchemeVersionToCopy.getMaintainableArtefact().getReplaceTo());
            assertEquals(versionExpected, categorySchemeVersionToCopy.getMaintainableArtefact().getReplacedBy());
            assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<CategorySchemeVersionMetamac> allVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningCategorySchemeWithTwoVersionsPublished() throws Exception {

        // This test checks the copy from one version but replacing to another one that is last version.

        String urnToCopy = CATEGORY_SCHEME_7_V1;
        String urnLastVersion = CATEGORY_SCHEME_7_V2;
        String versionExpected = "03.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=ISTAC:CATEGORYSCHEME07(03.000)";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionToCopy.getLifeCycleMetadata().getProcStatus());
        assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        CategorySchemeVersionMetamac categorySchemeVersionLast = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersionLast.getLifeCycleMetadata().getProcStatus());
        assertTrue(categorySchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals("02.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceTo());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedBy());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Version copied
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, categorySchemeVersionToCopy.getMaintainableArtefact().getReplaceTo());
            assertEquals("02.000", categorySchemeVersionToCopy.getMaintainableArtefact().getReplacedBy());
            assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            categorySchemeVersionLast = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", categorySchemeVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, categorySchemeVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", categorySchemeVersionLast.getMaintainableArtefact().getReplaceTo());
            assertEquals(versionExpected, categorySchemeVersionLast.getMaintainableArtefact().getReplacedBy());
            assertFalse(categorySchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

            // All versions
            List<CategorySchemeVersionMetamac> allVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(), categorySchemeVersionNewVersion
                    .getMaintainableArtefact().getUrn());
            assertEquals(3, allVersions.size());
            assertEquals(urnToCopy, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnLastVersion, allVersions.get(1).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(2).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningCategorySchemeErrorAlreadyExistsDraft() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        try {
            categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("CategoryScheme already exists in no final");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CATEGORY_SCHEME_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningCategorySchemeErrorNotPublished() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        try {
            categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("CategoryScheme not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    @Test
    public void testEndCategorySchemeValidity() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.endCategorySchemeValidity(getServiceContextAdministrador(), CATEGORY_SCHEME_7_V1);

        assertNotNull(categorySchemeVersion);
        assertNotNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testEndCategorySchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CATEGORY_SCHEME_1_V1, CATEGORY_SCHEME_4_V1, CATEGORY_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                categoriesService.endCategorySchemeValidity(getServiceContextAdministrador(), urn);
                fail("wrong procStatus");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            }
        }
    }

    // @Test
    // public void testCreateCategory() throws Exception {
    //
    // CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory();
    // category.setParent(null);
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    //
    // // Create
    // CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
    // String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();
    //
    // // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
    // CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
    // CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);
    //
    // // Validate new structure
    // CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
    // assertEquals(5, categorySchemeVersion.getItemsFirstLevel().size());
    // assertEquals(9, categorySchemeVersion.getItems().size());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categorySchemeVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categorySchemeVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categorySchemeVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categorySchemeVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    // assertEquals(categoryRetrieved.getNameableArtefact().getUrn(), categorySchemeVersion.getItemsFirstLevel().get(4).getNameableArtefact().getUrn());
    // }
    //
    // @Test
    // public void testCreateCategorySubcategory() throws Exception {
    //
    // CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory();
    // CategoryMetamac categoryParent = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
    // category.setParent(categoryParent);
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    //
    // // Create
    // CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
    // String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();
    //
    // // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
    // CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
    // CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);
    //
    // // Validate new structure
    // CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
    // assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
    // assertEquals(9, categorySchemeVersion.getItems().size());
    //
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categorySchemeVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
    // assertEquals(categoryRetrieved.getNameableArtefact().getUrn(), categorySchemeVersion.getItemsFirstLevel().get(0).getChildren().get(0).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categorySchemeVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categorySchemeVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categorySchemeVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    // }
    //
    // @Test
    // public void testUpdateCategory() throws Exception {
    //
    // CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
    // category.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
    // category.getNameableArtefact().setName(CategoriesDoMocks.mockInternationalString());
    //
    // // Update
    // CategoryMetamac categoryUpdated = categoriesService.updateCategory(getServiceContextAdministrador(), category);
    //
    // // Validate
    // CategoriesMetamacAsserts.assertEqualsCategory(category, categoryUpdated);
    // }

    @Test
    public void testRetrieveCategoryByUrn() throws Exception {
        // Retrieve
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        assertEquals(urn, category.getNameableArtefact().getUrn());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        // no metadata in Metamac
    }

    @Test
    public void testRetrieveCategoryByUrnWithParentAndChildren() throws Exception {

        // Retrieve
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_2_1;
        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);

        // Validate
        // Parent
        assertTrue(category.getParent() instanceof CategoryMetamac);
        assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getParent().getNameableArtefact().getUrn());
        // Children
        assertEquals(1, category.getChildren().size());
        assertTrue(category.getChildren().get(0) instanceof CategoryMetamac);
        assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, category.getChildren().get(0).getNameableArtefact().getUrn());
    }

    // @Test
    // public void testDeleteCategory() throws Exception {
    //
    // String urn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    //
    // CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
    // assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
    // assertEquals(8, categorySchemeVersion.getItems().size());
    //
    // // Delete category
    // categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
    //
    // // Validation
    // try {
    // categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
    // fail("Category deleted");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.CATEGORY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    //
    // // Check hierarchy
    // categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
    // assertEquals(3, categorySchemeVersion.getItemsFirstLevel().size());
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
    // assertEquals(7, categorySchemeVersion.getItems().size());
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
    // }
    //
    // @Test
    // public void testDeleteCategoryWithParentAndChildren() throws Exception {
    //
    // String urn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1;
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    //
    // CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
    // assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
    // assertEquals(8, categorySchemeVersion.getItems().size());
    //
    // // Delete category
    // categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
    //
    // // Validation
    // try {
    // categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
    // fail("Category deleted");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.CATEGORY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    //
    // categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
    // assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
    // assertListItemsContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
    // assertEquals(6, categorySchemeVersion.getItems().size());
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
    // assertListItemsContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
    // }
    //
    // @Test
    // public void testDeleteCategoryErrorCategorySchemePublished() throws Exception {
    //
    // String urn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V1;
    //
    // // Validation
    // try {
    // categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
    // fail("Category can not be deleted");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(categorySchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testRetrieveCategoriesByCategorySchemeUrn() throws Exception {
    //
    // // Retrieve
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    // List<CategoryMetamac> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn);
    //
    // // Validate
    // assertEquals(4, categories.size());
    // {
    // // Category 01
    // CategoryMetamac category = categories.get(0);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, category.getNameableArtefact().getUrn());
    // assertEquals(0, category.getChildren().size());
    // }
    // {
    // // Category 02
    // CategoryMetamac category = categories.get(1);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getNameableArtefact().getUrn());
    // assertEquals(1, category.getChildren().size());
    // {
    // // Category 02 01
    // CategoryMetamac categoryChild = (CategoryMetamac) category.getChildren().get(0);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoryChild.getNameableArtefact().getUrn());
    // assertEquals(1, categoryChild.getChildren().size());
    // {
    // // Category 02 01 01
    // CategoryMetamac categoryChildChild = (CategoryMetamac) categoryChild.getChildren().get(0);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoryChildChild.getNameableArtefact().getUrn());
    // assertEquals(0, categoryChildChild.getChildren().size());
    // }
    // }
    // }
    // {
    // // Category 03
    // CategoryMetamac category = categories.get(2);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, category.getNameableArtefact().getUrn());
    // assertEquals(0, category.getChildren().size());
    // }
    // {
    // // Category 04
    // CategoryMetamac category = categories.get(3);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, category.getNameableArtefact().getUrn());
    // assertEquals(1, category.getChildren().size());
    // {
    // // Category 04 01
    // CategoryMetamac categoryChild = (CategoryMetamac) category.getChildren().get(0);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoryChild.getNameableArtefact().getUrn());
    // assertEquals(1, categoryChild.getChildren().size());
    // {
    // // Category 04 01 01
    // CategoryMetamac categoryChildChild = (CategoryMetamac) categoryChild.getChildren().get(0);
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoryChildChild.getNameableArtefact().getUrn());
    // assertEquals(0, categoryChildChild.getChildren().size());
    // }
    // }
    // }
    // }
    //
    // @Test
    // public void testFindCategoriesByCondition() throws Exception {
    //
    // // Find all
    // {
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().code())
    // .orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending().orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
    // PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
    // PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(20, categoriesPagedResult.getTotalRows());
    // assertEquals(20, categoriesPagedResult.getValues().size());
    // assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);
    //
    // int i = 0;
    // assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_4_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_5_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_6_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_7_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    //
    // assertEquals(categoriesPagedResult.getValues().size(), i);
    // }
    //
    // // Find by name (like), code (like) and category scheme urn
    // {
    // String name = "Nombre categoryScheme-1-v2-category-2-";
    // String code = "CATEGORY02";
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).withProperty(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn())
    // .eq(categorySchemeUrn).withProperty(CategoryProperties.nameableArtefact().code()).like(code + "%")
    // .withProperty(CategoryProperties.nameableArtefact().name().texts().label()).like(name + "%").orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
    // PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
    // PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(2, categoriesPagedResult.getTotalRows());
    // assertEquals(2, categoriesPagedResult.getValues().size());
    // assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);
    //
    // int i = 0;
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(categoriesPagedResult.getValues().size(), i);
    // }
    //
    // // Find by category scheme urn paginated
    // {
    // String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).withProperty(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn())
    // .eq(categorySchemeUrn).orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
    //
    // // First page
    // {
    // PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
    // PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(8, categoriesPagedResult.getTotalRows());
    // assertEquals(3, categoriesPagedResult.getValues().size());
    // assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);
    //
    // int i = 0;
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(categoriesPagedResult.getValues().size(), i);
    // }
    // // Second page
    // {
    // PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
    // PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(8, categoriesPagedResult.getTotalRows());
    // assertEquals(3, categoriesPagedResult.getValues().size());
    // assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);
    //
    // int i = 0;
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(categoriesPagedResult.getValues().size(), i);
    // }
    // // Third page
    // {
    // PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
    // PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(8, categoriesPagedResult.getTotalRows());
    // assertEquals(2, categoriesPagedResult.getValues().size());
    // assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);
    //
    // int i = 0;
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(categoriesPagedResult.getValues().size(), i);
    // }
    // }
    // }
    //
    // @Override
    // public void testRetrieveCategorySchemeByCategoryUrn() throws Exception {
    // // Retrieve
    // String urn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
    // CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByCategoryUrn(getServiceContextAdministrador(), urn);
    //
    // // Validate
    // assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersion.getMaintainableArtefact().getUrn());
    // }
    //
    // @Test
    // public void testRetrieveCategorySchemeByCategoryUrnErrorNotExists() throws Exception {
    //
    // String urn = NOT_EXISTS;
    //
    // try {
    // categoriesService.retrieveCategorySchemeByCategoryUrn(getServiceContextAdministrador(), urn);
    // fail("not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.CATEGORY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // private void assertListItemsContainsCategory(List<Item> items, String urn) {
    // for (Item item : items) {
    // if (item.getNameableArtefact().getUrn().equals(urn)) {
    // return;
    // }
    // }
    // fail("List does not contain item with urn " + urn);
    // }
    //
    // // private void assertListCategoriesContainsCategory(List<CategoryMetamac> items, String urn) {
    // // for (Item item : items) {
    // // if (item.getNameableArtefact().getUrn().equals(urn)) {
    // // return;
    // // }
    // // }
    // // fail("List does not contain item with urn " + urn);
    // // }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }
}

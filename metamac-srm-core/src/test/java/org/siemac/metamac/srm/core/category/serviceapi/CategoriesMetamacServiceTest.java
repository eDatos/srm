package org.siemac.metamac.srm.core.category.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategoryProperties;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDoMocks;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CategoriesMetamacServiceTest extends SrmBaseTest implements CategoriesMetamacServiceTestBase {

    @Autowired
    private CategoriesMetamacService      categoriesService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

    @Override
    @Test
    public void testCreateCategoryScheme() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CategorySchemeVersionMetamac categorySchemeVersion = CategoriesMetamacDoMocks.mockCategoryScheme(organisationMetamac);
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
        assertFalse(categorySchemeVersionRetrieved.getMaintainableArtefact().getFinalLogicClient());
        assertEquals(ctx.getUserId(), categorySchemeVersionRetrieved.getCreatedBy());
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(categorySchemeVersion, categorySchemeVersionRetrieved);
    }

    @Test
    public void testCreateCategorySchemeErrorMaintainerNotDefault() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_2_V1);
        CategorySchemeVersionMetamac categorySchemeVersion = CategoriesMetamacDoMocks.mockCategoryScheme(organisationMetamac);

        try {
            categoriesService.createCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
            fail("maintainer not default");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINER_MUST_BE_DEFAULT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(AGENCY_ROOT_2_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(AGENCY_ROOT_1_V1, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
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
    public void testUpdateCategorySchemeChangingCode() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1);

        // Change code
        categorySchemeVersion.getMaintainableArtefact().setCode("codeNew");
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        CategorySchemeVersion categorySchemeVersionUpdated = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:codeNew(01.000)", categorySchemeVersionUpdated.getMaintainableArtefact().getUrn());
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
                assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
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
    public void testUpdateCategorySchemeErrorChangeCodeInCategorySchemeWithVersionAlreadyPublished() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        categorySchemeVersion.getMaintainableArtefact().setCode("newCode");
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);

        try {
            categorySchemeVersion = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
            fail("code can not be changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
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
            assertEquals(10, categorySchemeVersionPagedResult.getTotalRows());
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
            assertEquals(CATEGORY_SCHEME_8_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
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
            assertEquals(8, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
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

    @Override
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
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
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

    @Override
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
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
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

    @Override
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
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
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

    @Override
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
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
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

    @Override
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
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getLatestFinal());
        }

        // Publish internally
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.publishInternallyCategoryScheme(ctx, urn, Boolean.FALSE);

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
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
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
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getLatestFinal());
        }
    }

    @Test
    public void testPublishInternallyCategorySchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
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
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    public void testCheckCategorySchemeVersionTranslations() throws Exception {
        // Tested in testPublishInternallyCategorySchemeCheckTranslations
    }

    @Test
    public void testPublishInternallyCategorySchemeCheckTranslations() throws Exception {
        String urn = CATEGORY_SCHEME_8_V1;
        String code = "CATEGORYSCHEME08";

        try {
            // Note: publishInternallyCategoryScheme calls to 'checkCategorySchemeVersionTranslates'
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("CategoryScheme wrong translations");
        } catch (MetamacException e) {
            assertEquals(8, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.MAINTAINABLE_ARTEFACT_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{
                    ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION, code}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.MAINTAINABLE_ARTEFACT_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{code}, e.getExceptionItems().get(i++));
            // Categories
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME,
                    "CATEGORY01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT,
                    "CATEGORY01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION,
                    "CATEGORY01.CATEGORY0101"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME,
                    "CATEGORY02"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"CATEGORY01.CATEGORY0101"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"CATEGORY02"}, e.getExceptionItems().get(i++));

            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Override
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
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
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

    @Override
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
        assertNull(categorySchemeVersionV1.getMaintainableArtefact().getReplacedByVersion());
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
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Override
    @Test
    public void testCopyCategoryScheme() throws Exception {

        String urnToCopy = CATEGORY_SCHEME_8_V1;
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        String versionExpected = "01.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME08(01.000)";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY01";
        String urnExpectedCategory11 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY01.CATEGORY0101";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY02";
        String urnExpectedCategory3 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY03";

        TaskInfo copyResult = categoriesService.copyCategoryScheme(getServiceContextAdministrador(), urnToCopy);

        // Validate (only some metadata, already tested in statistic module)
        entityManager.clear();
        CategorySchemeVersionMetamac categorySchemeVersionNewArtefact = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), copyResult.getUrnResult());
        assertEquals(maintainerUrnExpected, categorySchemeVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, categorySchemeVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, categorySchemeVersionNewArtefact.getMaintainableArtefact().getUrn());
        assertEquals(null, categorySchemeVersionNewArtefact.getMaintainableArtefact().getReplaceToVersion());
        assertEquals(null, categorySchemeVersionNewArtefact.getMaintainableArtefact().getReplacedByVersion());
        assertTrue(categorySchemeVersionNewArtefact.getMaintainableArtefact().getIsLastVersion());
        CategoriesMetamacAsserts.assertEqualsInternationalString(categorySchemeVersionNewArtefact.getMaintainableArtefact().getName(), "es", "nombre catScheme8-1", null, null);
        CategoriesMetamacAsserts.assertEqualsInternationalString(categorySchemeVersionNewArtefact.getMaintainableArtefact().getDescription(), "en", "description catScheme8-1", "it",
                "descripcion it catScheme8-1");
        assertEquals(null, categorySchemeVersionNewArtefact.getMaintainableArtefact().getComment());

        // Categories
        assertEquals(4, categorySchemeVersionNewArtefact.getItems().size());
        assertListItemsContainsItem(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory1);
        assertListItemsContainsItem(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory11);
        assertListItemsContainsItem(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory2);
        assertListItemsContainsItem(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory3);

        assertEquals(3, categorySchemeVersionNewArtefact.getItemsFirstLevel().size());
        {
            CategoryMetamac category = assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedCategory1);
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getName(), "en", "name cat1", "it", "nombre it cat1");
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getDescription(), "es", "descripción cat1", "it", "descripción it cat1");
            assertEquals(null, category.getNameableArtefact().getComment());

            assertEquals(1, category.getChildren().size());
            {
                CategoryMetamac categoryChild = assertListCategoriesContainsCategory(category.getChildren(), urnExpectedCategory11);
                assertEquals(0, categoryChild.getChildren().size());
            }

        }
        {
            CategoryMetamac category = assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedCategory2);
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getName(), "en", "name cat2", null, null);

            assertEquals(0, category.getChildren().size());
        }
        {
            CategoryMetamac category = assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedCategory3);
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getName(), "es", "nombre cat3", null, null);

            assertEquals(0, category.getChildren().size());
        }
    }

    @Override
    @Test
    public void testVersioningCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(02.000)";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY01";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02";
        String urnExpectedCategory21 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02.CATEGORY0201";
        String urnExpectedCategory211 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02.CATEGORY0201.CATEGORY020101";
        String urnExpectedCategory22 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02.CATEGORY0202";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

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
            assertEquals("01.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Categories
            assertEquals(5, categorySchemeVersionNewVersion.getItems().size());
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory1);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory2);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory21);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory211);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory22);

            assertEquals(2, categorySchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                Item category = assertListItemsContainsItem(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory1);
                assertEquals(0, category.getChildren().size());
            }
            {
                Item category = assertListItemsContainsItem(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory2);
                {
                    Item categoryChild = assertListItemsContainsItem(category.getChildren(), urnExpectedCategory21);
                    {
                        Item categoryChildChild = assertListItemsContainsItem(categoryChild.getChildren(), urnExpectedCategory211);
                        assertEquals(0, categoryChildChild.getChildren().size());
                    }
                }
                {
                    Item categoryChild = assertListItemsContainsItem(category.getChildren(), urnExpectedCategory22);
                    assertEquals(0, categoryChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, categorySchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, categorySchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
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
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME07(03.000)";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionToCopy.getLifeCycleMetadata().getProcStatus());
        assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        CategorySchemeVersionMetamac categorySchemeVersionLast = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersionLast.getLifeCycleMetadata().getProcStatus());
        assertTrue(categorySchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

        TaskInfo versioningResult = categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

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
            assertEquals("02.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Version copied
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, categorySchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals("02.000", categorySchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            categorySchemeVersionLast = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", categorySchemeVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, categorySchemeVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", categorySchemeVersionLast.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, categorySchemeVersionLast.getMaintainableArtefact().getReplacedByVersion());
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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Override
    @Test
    public void testCreateTemporalVersionCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_3_V1;
        String versionExpected = "01.000" + UrnConstants.URN_SDMX_TEMPORAL_SUFFIX;
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(" + versionExpected + ")";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY01";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02";
        String urnExpectedCategory21 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02.CATEGORY0201";
        String urnExpectedCategory211 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02.CATEGORY0201.CATEGORY020101";
        String urnExpectedCategory22 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02.CATEGORY0202";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = categoriesService.createTemporalVersionCategoryScheme(getServiceContextAdministrador(), urn);

        // Validate response
        entityManager.clear();
        categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

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
            assertEquals("01.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Categories
            assertEquals(5, categorySchemeVersionNewVersion.getItems().size());
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory1);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory2);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory21);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory211);
            assertListItemsContainsItem(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory22);

            assertEquals(2, categorySchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                Item category = assertListItemsContainsItem(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory1);
                assertEquals(0, category.getChildren().size());
            }
            {
                Item category = assertListItemsContainsItem(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory2);
                {
                    Item categoryChild = assertListItemsContainsItem(category.getChildren(), urnExpectedCategory21);
                    {
                        Item categoryChildChild = assertListItemsContainsItem(categoryChild.getChildren(), urnExpectedCategory211);
                        assertEquals(0, categoryChildChild.getChildren().size());
                    }
                }
                {
                    Item categoryChild = assertListItemsContainsItem(category.getChildren(), urnExpectedCategory22);
                    assertEquals(0, categoryChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
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

    @Override
    @Test
    public void testCreateVersionFromTemporalCategoryScheme() throws Exception {
        String urn = CATEGORY_SCHEME_3_V1;

        TaskInfo versioningResult1 = categoriesService.createTemporalVersionCategoryScheme(getServiceContextAdministrador(), urn);
        entityManager.clear();
        CategorySchemeVersionMetamac categorySchemeVersionTemporal = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult1.getUrnResult());
        assertEquals(3, categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations().size());
        assertListContainsCategorisation(categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000_temporal)");
        assertListContainsCategorisation(categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat2(01.000_temporal)");
        assertListContainsCategorisation(categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat3(01.000_temporal)");

        TaskInfo versioningResult2 = categoriesService.createVersionFromTemporalCategoryScheme(getServiceContextAdministrador(), categorySchemeVersionTemporal.getMaintainableArtefact().getUrn(),
                VersionTypeEnum.MAJOR);
        entityManager.clear();
        CategorySchemeVersionMetamac categorySchemeNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult2.getUrnResult());

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(" + versionExpected + ")";

        // Validate

        {
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeNewVersion.getMaintainableArtefact().getUrn());

            assertEquals(null, categorySchemeNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(categorySchemeNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorySchemeNewVersion.getMaintainableArtefact().getLatestPublic());

            assertEquals(3, categorySchemeNewVersion.getMaintainableArtefact().getCategorisations().size());
            assertListContainsCategorisation(categorySchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)");
            assertListContainsCategorisation(categorySchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat5(01.000)");
            assertListContainsCategorisation(categorySchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)");
        }
    }

    @Override
    @Test
    public void testMergeTemporalVersion() throws Exception {
        {
            String urn = CATEGORY_SCHEME_3_V1;
            TaskInfo versioningResult = categoriesService.createTemporalVersionCategoryScheme(getServiceContextAdministrador(), urn);

            entityManager.clear();
            CategorySchemeVersionMetamac categorySchemeVersionTemporal = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());
            // Change temporal version *********************

            // Item scheme: Change Name
            {
                LocalisedString localisedString = new LocalisedString("fr", "its - text sample");
                categorySchemeVersionTemporal.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Item: change name
            {
                CategoryMetamac categoryTemporal = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CATEGORY_SCHEME_3_V1_CATEGORY_1));

                LocalisedString localisedString = new LocalisedString("fr", "it - text sample");
                InternationalString internationalString = new InternationalString();
                internationalString.addText(localisedString);
                categoryTemporal.getNameableArtefact().setName(internationalString);
            }

            // Merge
            categorySchemeVersionTemporal = categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), categorySchemeVersionTemporal.getMaintainableArtefact()
                    .getUrn());
            categorySchemeVersionTemporal = categoriesService.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(), categorySchemeVersionTemporal.getMaintainableArtefact()
                    .getUrn());
            CategorySchemeVersionMetamac categorySchemeVersionMetamac = categoriesService.mergeTemporalVersion(getServiceContextAdministrador(), categorySchemeVersionTemporal);

            // Assert **************************************

            // Item Scheme
            assertEquals(3, categorySchemeVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("its - text sample", categorySchemeVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));

            // Item
            {
                CategoryMetamac categoryTemporal = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_3_V1_CATEGORY_1);
                assertEquals(1, categoryTemporal.getNameableArtefact().getName().getTexts().size());
                assertEquals("it - text sample", categoryTemporal.getNameableArtefact().getName().getLocalisedLabel("fr"));
            }
        }
    }

    @Override
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

    @Override
    @Test
    public void testCreateCategory() throws Exception {

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory();
        category.setParent(null);
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
        String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);

        // Validate new structure
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(5, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, categorySchemeVersion.getItems().size());
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), categoryRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCategorySubcategory() throws Exception {

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory();
        CategoryMetamac categoryParent = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        category.setParent(categoryParent);
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
        String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);

        // Validate new structure
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, categorySchemeVersion.getItems().size());

        Item category1 = assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListItemsContainsItem(category1.getChildren(), categoryRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCategoryErrorCategoryImported() throws Exception {
        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory();
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        // save to force incorrect metadata
        categorySchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        categorySchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(categorySchemeVersion);

        try {
            categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    @Test
    public void testUpdateCategory() throws Exception {

        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        category.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        category.getNameableArtefact().setName(CategoriesDoMocks.mockInternationalString());

        // Update
        CategoryMetamac categoryUpdated = categoriesService.updateCategory(getServiceContextAdministrador(), category);

        // Validate
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryUpdated);
    }

    @Override
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

    @Override
    @Test
    public void testDeleteCategory() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, categorySchemeVersion.getItems().size());

        // Delete category
        categoriesService.deleteCategory(getServiceContextAdministrador(), urn);

        // Validation
        try {
            categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
            fail("Category deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Check hierarchy
        categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(3, categorySchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertEquals(7, categorySchemeVersion.getItems().size());
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
    }

    @Test
    public void testDeleteCategoryWithParentAndChildren() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, categorySchemeVersion.getItems().size());

        // Delete category
        categoriesService.deleteCategory(getServiceContextAdministrador(), urn);

        // Validation
        try {
            categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
            fail("Category deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        assertListItemsContainsItem(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertEquals(6, categorySchemeVersion.getItems().size());
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        assertListItemsContainsItem(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
    }

    @Test
    public void testDeleteCategoryErrorCategorySchemePublished() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V1;

        // Validation
        try {
            categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
            fail("Category can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(categorySchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteCategoryErrorCategorySchemeImported() throws Exception {
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        // save to force incorrect metadata
        categorySchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        categorySchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(categorySchemeVersion);

        try {
            categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    @Test
    public void testRetrieveCategoriesByCategorySchemeUrn() throws Exception {

        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // LOCALE = 'es'
        {
            String locale = "es";
            List<ItemVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale);

            // Validate
            assertEquals(8, categories.size());
            {
                // Category 01 (validate all metadata)
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, category.getUrn());
                assertEquals("CATEGORY01", category.getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-1", category.getName());
                assertEquals("Descripción categoryScheme-1-v2-category-1", category.getDescription());
                assertEquals(Long.valueOf(121), category.getItemIdDatabase());
                assertEquals(null, category.getParent());
                assertEquals(null, category.getParentIdDatabase());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", category.getCreatedDate());
            }
            {
                // Category 02
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getUrn());
                assertEquals("CATEGORY02", category.getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2", category.getName());
                assertEquals(null, category.getDescription());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", category.getCreatedDate());
            }
            {
                // Category 02 01 (validate parent)
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, category.getUrn());
                assertEquals("CATEGORY02", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2-1", category.getName());
                assertEquals(null, category.getDescription());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getParent().getUrn());
                assertEquals(Long.valueOf("122"), category.getParentIdDatabase());
            }
            {
                // Category 02 01 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, category.getUrn());
                assertEquals("CATEGORY0201", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2-1-1", category.getName());
                assertEquals("Descripción cat2-1-1", category.getDescription());
                assertEquals(Long.valueOf("1221"), category.getParentIdDatabase());
            }
            {
                // Category 03
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, category.getUrn());
                assertEquals("nombre category-3", category.getName());
            }
            {
                // Category 04
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, category.getUrn());
                assertEquals("nombre category-4", category.getName());
            }
            {
                // Category 04 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getUrn());
                assertEquals("nombre category 4-1", category.getName());
            }
            {
                // Category 04 01 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, category.getUrn());
                assertEquals("CATEGORY0401", category.getParent().getCode());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getParent().getUrn());
                assertEquals("Nombre categoryScheme-1-v2-category-4-1-1", category.getName());
            }
        }

        // LOCALE = 'en'
        {
            String locale = "en";
            List<ItemVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale);

            // Validate
            assertEquals(8, categories.size());
            {
                // Category 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals("Name categoryScheme-1-v2-category-1", category.getName());
                assertEquals(null, category.getDescription());
            }
            {
                // Category 02
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals(null, category.getName());
                assertEquals(null, category.getDescription());
            }
            {
                // Category 02 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals("Name categoryScheme-1-v2-category-2-1", category.getName());
                assertEquals("Description cat2-1", category.getDescription());
            }
            {
                // Category 02 01 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals(null, category.getName());
                assertEquals(null, category.getDescription());
            }
            {
                // Category 03
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals("name category-3", category.getName());
            }
            {
                // Category 04
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals(null, category.getName());
            }
            {
                // Category 04 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(null, category.getName());
            }
            {
                // Category 04 01 01
                ItemVisualisationResult category = getItemVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals("Name categoryScheme-1-v2-category-4-1-1", category.getName());
            }
        }
    }

    @Override
    @Test
    public void testFindCategoriesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().code())
                    .orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending().orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(24, categoriesPagedResult.getTotalRows());
            assertEquals(24, categoriesPagedResult.getValues().size());
            assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());

            assertEquals(categoriesPagedResult.getValues().size(), i);
        }

        // Find by name (like), code (like) and category scheme urn
        {
            String name = "Nombre categoryScheme-1-v2-category-2-";
            String code = "CATEGORY02";
            String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).withProperty(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(categorySchemeUrn).withProperty(CategoryProperties.nameableArtefact().code()).like(code + "%").withProperty(CategoryProperties.nameableArtefact().name().texts().label())
                    .like(name + "%").orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, categoriesPagedResult.getTotalRows());
            assertEquals(2, categoriesPagedResult.getValues().size());
            assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(categoriesPagedResult.getValues().size(), i);
        }

        // Find by category scheme urn paginated
        {
            String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).withProperty(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(categorySchemeUrn).orderBy(CategoryProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
                PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, categoriesPagedResult.getTotalRows());
                assertEquals(3, categoriesPagedResult.getValues().size());
                assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(categoriesPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
                PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, categoriesPagedResult.getTotalRows());
                assertEquals(3, categoriesPagedResult.getValues().size());
                assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(categoriesPagedResult.getValues().size(), i);
            }
            // Third page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
                PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, categoriesPagedResult.getTotalRows());
                assertEquals(2, categoriesPagedResult.getValues().size());
                assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(categoriesPagedResult.getValues().size(), i);
            }
        }
    }

    @Override
    @Test
    public void testRetrieveCategorySchemeByCategoryUrn() throws Exception {
        // Retrieve
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByCategoryUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersion.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testRetrieveCategorySchemeByCategoryUrnErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;

        try {
            categoriesService.retrieveCategorySchemeByCategoryUrn(getServiceContextAdministrador(), urn);
            fail("not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Override
    public void testPreCreateCategoryScheme() throws Exception {
        // TODO testPreCreateCategoryScheme

    }

    @Test
    @Override
    public void testPreCreateCategory() throws Exception {
        // TODO testPreCreateCategory

    }

    @Override
    public void testPreCreateCategorisation() throws Exception {
        // TODO testPreCreateCategorisation

    }

    @Override
    public void testPostCreateCategorisation() throws Exception {
        // TODO testPostCreateCategorisation

    }

    // In CategoriesMetamacCategorisationServiceTest.java
    @Override
    public void testRetrieveCategorisationByUrn() throws Exception {
    }
    @Override
    public void testDeleteCategorisation() throws Exception {
    }
    @Override
    public void testRetrieveCategorisationsByArtefact() throws Exception {
    }
    @Override
    public void testCreateCategorisation() throws Exception {
    }
    @Override
    public void testFindCategorisationsByCondition() throws Exception {
    }
    @Override
    public void testMarkCategorisationAsFinal() throws Exception {
    }
    @Override
    public void testMarkCategorisationAsPublic() throws Exception {
    }
    @Override
    public void testStartCategorisationValidity() throws Exception {
    }
    @Override
    public void testEndCategorisationValidity() throws Exception {
    }
    @Override
    public void testCreateVersionFromTemporalCategorisations() throws Exception {
        // in services of categorised artefacts
    }

    @SuppressWarnings("rawtypes")
    private CategoryMetamac assertListCategoriesContainsCategory(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            CategoryMetamac category = (CategoryMetamac) iterator.next();
            if (category.getNameableArtefact().getUrn().equals(urn)) {
                return category;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }

}
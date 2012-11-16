package org.siemac.metamac.srm.core.code.serviceapi;

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
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class CodesMetamacServiceTest extends SrmBaseTest implements CodesMetamacServiceTestBase {

    @Autowired
    protected CodesMetamacService         codesService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Test
    public void testCreateCodelist() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        CodelistVersionMetamac codelistVersionCreated = codesService.createCodelist(ctx, codelistVersion);
        String urn = codelistVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", codelistVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), codelistVersionCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CodelistVersionMetamac codelistVersionRetrieved = codesService.retrieveCodelistByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, codelistVersionRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(codelistVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getProductionValidationDate());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getProductionValidationUser());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationDate());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationUser());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getInternalPublicationDate());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(codelistVersionRetrieved.getLifeCycleMetadata().getExternalPublicationUser());
        assertEquals(ctx.getUserId(), codelistVersionRetrieved.getCreatedBy());
        assertEquals(ctx.getUserId(), codelistVersionRetrieved.getLastUpdatedBy());
        CodesMetamacAsserts.assertEqualsCodelist(codelistVersion, codelistVersionRetrieved);
    }

    @Test
    public void testCreateCodelistErrorDuplicatedCode() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion1 = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        CodelistVersionMetamac codelistVersion2 = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        String code = "code-" + MetamacMocks.mockString(10);
        codelistVersion1.getMaintainableArtefact().setCode(code);
        codelistVersion2.getMaintainableArtefact().setCode(code);

        codesService.createCodelist(getServiceContextAdministrador(), codelistVersion1);
        try {
            codesService.createCodelist(getServiceContextAdministrador(), codelistVersion2);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateCodelist() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, CODELIST_2_V1);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        CodelistVersion codelistVersionUpdated = codesService.updateCodelist(ctx, codelistVersion);
        assertNotNull(codelistVersionUpdated);
        assertEquals("user1", codelistVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), codelistVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateCodelistPublished() throws Exception {
        String[] urns = {CODELIST_1_V1, CODELIST_7_V2, CODELIST_7_V1};
        for (String urn : urns) {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);

            try {
                codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
                codelistVersion = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
                fail("wrong proc status");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }
        }
    }

    @Test
    public void testUpdateCodelistErrorExternalReference() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V2);
        codelistVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);

        try {
            codelistVersion = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
            fail("codelist cannot be a external reference");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveCodelistByUrn() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V1;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, codelistVersion.getMaintainableArtefact().getUrn());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Test
    @Override
    public void testFindCodelistsByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistVersionMetamac> codelistVersionPagedResult = codesService.findCodelistsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(17, codelistVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_1_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_1_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_2_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_3_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_4_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_5_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_6_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_8_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_9_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V3, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_11_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_12_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_13_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(codelistVersionPagedResult.getTotalRows(), i);
        }

        // Find internally published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus())
                    .eq(ProcStatusEnum.INTERNALLY_PUBLISHED).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistVersionMetamac> codelistVersionPagedResult = codesService.findCodelistsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(4, codelistVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_1_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_3_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(codelistVersionPagedResult.getTotalRows(), i);
        }

        // Find lasts versions
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                    .withProperty(CodelistVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn())
                    .build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistVersionMetamac> codelistVersionPagedResult = codesService.findCodelistsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(13, codelistVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_1_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_2_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_3_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_4_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_5_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_6_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V2, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_8_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_9_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V3, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_11_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_12_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_13_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testRetrieveCodelistVersions() throws Exception {
        // Retrieve all versions
        String urn = CODELIST_10_V1;
        List<CodelistVersionMetamac> codelistVersions = codesService.retrieveCodelistVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(3, codelistVersions.size());
        assertEquals(CODELIST_10_V1, codelistVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(CODELIST_10_V2, codelistVersions.get(1).getMaintainableArtefact().getUrn());
        assertEquals(CODELIST_10_V3, codelistVersions.get(2).getMaintainableArtefact().getUrn());
    }

    @Test
    public void testSendCodelistToProductionValidation() throws Exception {
        String urn = CODELIST_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        CodelistVersionMetamac codelistVersion = codesService.sendCodelistToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCodelistToProductionValidationInProcStatusRejected() throws Exception {
        String urn = CODELIST_4_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        CodelistVersionMetamac codelistVersion = codesService.sendCodelistToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
        // Validate retrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
    }

    @Test
    public void testSendCodelistToProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCodelistToProductionValidationErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_1_V1;

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist wrong proc status");
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
    public void testSendCodelistToProductionValidationErrorMetadataRequired() throws Exception {
        String urn = CODELIST_2_V1;

        // Update to clear required metadata to send to production
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        codelistVersion.setIsPartial(null);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);

        // Send to production validation
        try {
            codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCodelistToDiffusionValidation() throws Exception {
        String urn = CODELIST_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Sends to diffusion validation
        CodelistVersionMetamac codelistVersion = codesService.sendCodelistToDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCodelistToDiffusionValidationErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.sendCodelistToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCodelistToDiffusionValidationErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        try {
            codesService.sendCodelistToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectCodelistProductionValidation() throws Exception {
        String urn = CODELIST_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Reject validation
        CodelistVersionMetamac codelistVersion = codesService.rejectCodelistProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate restrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);

            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCodelistProductionValidationErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.rejectCodelistProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectCodelistProductionValidationErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_1_V1;

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            codesService.rejectCodelistProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectCodelistDiffusionValidation() throws Exception {
        String urn = CODELIST_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Reject validation
        CodelistVersionMetamac codelistVersion = codesService.rejectCodelistDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);

            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCodelistDiffusionValidationErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.rejectCodelistDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectCodelistDiffusionValidationErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_1_V1;

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            codesService.rejectCodelistDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishInternallyCodelist() throws Exception {
        String urn = CODELIST_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertFalse(codelistVersion.getMaintainableArtefact().getFinalLogic());
        }

        // Publish internally
        CodelistVersionMetamac codelistVersion = codesService.publishInternallyCodelist(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(codelistVersion.getMaintainableArtefact().getFinalLogic());
        }
        // Validate retrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(codelistVersion.getMaintainableArtefact().getFinalLogic());
        }
    }

    @Test
    public void testPublishInternallyCodelistErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn);
            fail("Codelist not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishInternallyCodelistErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_1_V1;

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn);
            fail("Codelist wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishExternallyCodelist() throws Exception {
        String urn = CODELIST_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertNull(codelistVersion.getMaintainableArtefact().getValidFrom());
            assertNull(codelistVersion.getMaintainableArtefact().getValidTo());
            assertNull(codelistVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());

            CodelistVersionMetamac codelistVersionExternallyPublished = codesService.retrieveCodelistByUrn(ctx, CODELIST_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertNull(codelistVersionExternallyPublished.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        CodelistVersionMetamac codelistVersion = codesService.publishExternallyCodelist(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(codelistVersion.getMaintainableArtefact().getValidTo());
            assertNull(codelistVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());
        }
        // Validate retrieving
        {
            codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(codelistVersion.getMaintainableArtefact().getValidTo());
            assertNull(codelistVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());
        }
        // Validate previous published externally versions
        {
            CodelistVersionMetamac codelistVersionExternallyPublished = codesService.retrieveCodelistByUrn(ctx, CODELIST_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(codelistVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersionExternallyPublished.getMaintainableArtefact().getValidTo().toDate()));
        }
    }

    @Test
    public void testPublishExternallyCodelistErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.publishExternallyCodelist(getServiceContextAdministrador(), urn);
            fail("Codelist not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishExternallyCodelistErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_1_V2;
        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.DRAFT, codelistVersion.getLifeCycleMetadata().getProcStatus());
        }
        try {
            codesService.publishExternallyCodelist(getServiceContextAdministrador(), urn);
            fail("Codelist wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testDeleteCodelist() throws Exception {
        String urn = CODELIST_2_V1;

        // Delete codelist only with version in draft
        codesService.deleteCodelist(getServiceContextAdministrador(), urn);

        // Validation
        try {
            codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            fail("Codelist deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteCodelistWithVersionPublishedAndVersionDraft() throws Exception {
        String urnV1 = CODELIST_1_V1;
        String urnV2 = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersionV1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersionV1.getLifeCycleMetadata().getProcStatus());
        CodelistVersionMetamac codelistVersionV2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnV2);
        assertEquals(ProcStatusEnum.DRAFT, codelistVersionV2.getLifeCycleMetadata().getProcStatus());

        codesService.deleteCodelist(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnV2);
            fail("Codelist deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        codelistVersionV1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(codelistVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(codelistVersionV1.getMaintainableArtefact().getReplacedBy());
    }

    @Test
    public void testDeleteCodelistErrorPublished() throws Exception {
        String urn = CODELIST_10_V2;

        // Validation
        try {
            codesService.deleteCodelist(getServiceContextAdministrador(), urn);
            fail("Codelist can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningCodelist() throws Exception {
        String urn = CODELIST_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(02.000)";
        String urnExpectedCode1 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE01";
        String urnExpectedCode2 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE02";
        String urnExpectedCode21 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE0201";
        String urnExpectedCode211 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE020101";
        String urnExpectedCode22 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE0202";

        CodelistVersionMetamac codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        CodelistVersionMetamac codelistVersionNewVersion = codesService.versioningCodelist(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", codelistVersionNewVersion.getMaintainableArtefact().getReplaceTo());
            assertEquals(null, codelistVersionNewVersion.getMaintainableArtefact().getReplacedBy());
            assertTrue(codelistVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);

            // Codes
            assertEquals(5, codelistVersionNewVersion.getItems().size());
            assertEquals(urnExpectedCode1, codelistVersionNewVersion.getItems().get(0).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCode2, codelistVersionNewVersion.getItems().get(1).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCode21, codelistVersionNewVersion.getItems().get(2).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCode211, codelistVersionNewVersion.getItems().get(3).getNameableArtefact().getUrn());
            assertEquals(urnExpectedCode22, codelistVersionNewVersion.getItems().get(4).getNameableArtefact().getUrn());

            assertEquals(2, codelistVersionNewVersion.getItemsFirstLevel().size());
            {
                CodeMetamac code = (CodeMetamac) codelistVersionNewVersion.getItemsFirstLevel().get(0);
                assertEquals(urnExpectedCode1, code.getNameableArtefact().getUrn());

                CodesMetamacAsserts.assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-1", null, null);

                assertEquals(0, code.getChildren().size());
            }
            {
                CodeMetamac code = (CodeMetamac) codelistVersionNewVersion.getItemsFirstLevel().get(1);
                assertEquals(urnExpectedCode2, code.getNameableArtefact().getUrn());

                CodesMetamacAsserts.assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-2", null, null);

                assertEquals(2, code.getChildren().size());
                {
                    CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(0);
                    assertEquals(urnExpectedCode21, codeChild.getNameableArtefact().getUrn());

                    assertEquals(1, codeChild.getChildren().size());
                    {
                        CodeMetamac codeChildChild = (CodeMetamac) codeChild.getChildren().get(0);
                        assertEquals(urnExpectedCode211, codeChildChild.getNameableArtefact().getUrn());

                        assertEquals(0, codeChildChild.getChildren().size());
                    }
                }
                {
                    CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(1);
                    assertEquals(urnExpectedCode22, codeChild.getNameableArtefact().getUrn());

                    assertEquals(0, codeChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", codelistVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, codelistVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, codelistVersionToCopy.getMaintainableArtefact().getReplaceTo());
            assertEquals(versionExpected, codelistVersionToCopy.getMaintainableArtefact().getReplacedBy());
            assertFalse(codelistVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<CodelistVersionMetamac> allVersions = codesService.retrieveCodelistVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningCodelistWithTwoVersionsPublished() throws Exception {
        // This test checks the copy from one version but replacing to another one that is last version.

        String urnToCopy = CODELIST_7_V1;
        String urnLastVersion = CODELIST_7_V2;
        String versionExpected = "03.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST07(03.000)";

        CodelistVersionMetamac codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistVersionToCopy.getLifeCycleMetadata().getProcStatus());
        assertFalse(codelistVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        CodelistVersionMetamac codelistVersionLast = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersionLast.getLifeCycleMetadata().getProcStatus());
        assertTrue(codelistVersionLast.getMaintainableArtefact().getIsLastVersion());

        CodelistVersionMetamac codelistVersionNewVersion = codesService.versioningCodelist(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals("02.000", codelistVersionNewVersion.getMaintainableArtefact().getReplaceTo());
            assertEquals(null, codelistVersionNewVersion.getMaintainableArtefact().getReplacedBy());
            assertTrue(codelistVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);

            // Version copied
            codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", codelistVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, codelistVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, codelistVersionToCopy.getMaintainableArtefact().getReplaceTo());
            assertEquals("02.000", codelistVersionToCopy.getMaintainableArtefact().getReplacedBy());
            assertFalse(codelistVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            codelistVersionLast = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", codelistVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, codelistVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", codelistVersionLast.getMaintainableArtefact().getReplaceTo());
            assertEquals(versionExpected, codelistVersionLast.getMaintainableArtefact().getReplacedBy());
            assertFalse(codelistVersionLast.getMaintainableArtefact().getIsLastVersion());

            // All versions
            List<CodelistVersionMetamac> allVersions = codesService.retrieveCodelistVersions(getServiceContextAdministrador(), codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(3, allVersions.size());
            assertEquals(urnToCopy, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnLastVersion, allVersions.get(1).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(2).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningCodelistErrorAlreadyExistsDraft() throws Exception {
        String urn = CODELIST_1_V1;

        try {
            codesService.versioningCodelist(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("Codelist already exists in no final");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CODELIST_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningCodelistErrorNotPublished() throws Exception {
        String urn = CODELIST_2_V1;

        try {
            codesService.versioningCodelist(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("Codelist not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testEndCodelistValidity() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.endCodelistValidity(getServiceContextAdministrador(), CODELIST_7_V1);

        assertNotNull(codelistVersion);
        assertNotNull(codelistVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testEndCodelistValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CODELIST_1_V1, CODELIST_4_V1, CODELIST_6_V1};
        for (String urn : urns) {
            try {
                codesService.endCodelistValidity(getServiceContextAdministrador(), urn);
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
    public void testRetrieveCodelistByCodeUrn() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V2_CODE_1;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByCodeUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(CODELIST_1_V2, codelistVersion.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testRetrieveCodelistByCodeUrnErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;

        try {
            codesService.retrieveCodelistByCodeUrn(getServiceContextAdministrador(), urn);
            fail("not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // ------------------------------------------------------------------------------------
    // CODES
    // ------------------------------------------------------------------------------------

    // @Test
    // public void testCreateCode() throws Exception {
    //
    // ServiceContext ctx = getServiceContextAdministrador();
    //
    // CodeMetamac code = CodesMetamacDoMocks.mockCode();
    // code.setParent(null);
    // CodeMetamac codeExtends = codesService.retrieveCodeByUrn(ctx, CODELIST_13_V1_CODE_1);
    // code.setCodeExtends(codeExtends);
    //
    // String codelistUrn = CODELIST_1_V2;
    //
    // // Create
    // CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);
    // String urn = codeCreated.getNameableArtefact().getUrn();
    // assertEquals(ctx.getUserId(), codeCreated.getCreatedBy());
    // assertEquals(ctx.getUserId(), codeCreated.getLastUpdatedBy());
    //
    // // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
    // CodeMetamac codeRetrieved = codesService.retrieveCodeByUrn(ctx, urn);
    // CodesMetamacAsserts.assertEqualsCode(code, codeRetrieved);
    //
    // // Validate new structure
    // CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, codelistUrn);
    // assertEquals(5, codelistVersion.getItemsFirstLevel().size());
    // assertEquals(9, codelistVersion.getItems().size());
    // assertEquals(CODELIST_1_V2_CODE_1, codelistVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2, codelistVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_3, codelistVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4, codelistVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    // assertEquals(codeRetrieved.getNameableArtefact().getUrn(), codelistVersion.getItemsFirstLevel().get(4).getNameableArtefact().getUrn());
    // }
    //
    // @Test
    // public void testCreateCodeSubcode() throws Exception {
    //
    // CodeType codeType = null;
    // CodeMetamac code = CodesMetamacDoMocks.mockCode(codeType);
    // CodeMetamac codeParent = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
    // code.setParent(codeParent);
    // String codelistUrn = CODELIST_1_V2;
    //
    // // Create
    // CodeMetamac codelistVersionCreated = codesService.createCode(getServiceContextAdministrador(), codelistUrn, code);
    // String urn = codelistVersionCreated.getNameableArtefact().getUrn();
    //
    // // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
    // CodeMetamac codeRetrieved = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
    // CodesMetamacAsserts.assertEqualsCode(code, codeRetrieved);
    //
    // // Validate new structure
    // CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
    // assertEquals(4, codelistVersion.getItemsFirstLevel().size());
    // assertEquals(9, codelistVersion.getItems().size());
    //
    // assertEquals(CODELIST_1_V2_CODE_1, codelistVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
    // assertEquals(codeRetrieved.getNameableArtefact().getUrn(), codelistVersion.getItemsFirstLevel().get(0).getChildren().get(0).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2, codelistVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_3, codelistVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4, codelistVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    // }
    //
    // @Test
    // public void testCreateCodeErrorMetadataIncorrect() throws Exception {
    //
    // CodeType codeType = null;
    // CodeMetamac code = CodesMetamacDoMocks.mockCode(codeType);
    // code.setPluralName(new InternationalString());
    // code.setDocMethod(new InternationalString());
    // code.getDocMethod().addText(new LocalisedString());
    // code.setLegalActs(new InternationalString());
    // LocalisedString lsLegalActs = new LocalisedString();
    // lsLegalActs.setLocale("es");
    // code.getLegalActs().addText(lsLegalActs);
    //
    // try {
    // codesService.createCode(getServiceContextAdministrador(), CODELIST_1_V2, code);
    // fail("parameters incorrect");
    // } catch (MetamacException e) {
    // assertEquals(3, e.getExceptionItems().size());
    //
    // assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(ServiceExceptionParameters.CODE_PLURAL_NAME, e.getExceptionItems().get(0).getMessageParameters()[0]);
    //
    // assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(1).getCode());
    // assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
    // assertEquals(ServiceExceptionParameters.CODE_DOC_METHOD, e.getExceptionItems().get(1).getMessageParameters()[0]);
    //
    // assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(2).getCode());
    // assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
    // assertEquals(ServiceExceptionParameters.CODE_LEGAL_ACTS, e.getExceptionItems().get(2).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testUpdateCode() throws Exception {
    //
    // CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
    // code.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
    // code.getNameableArtefact().setName(CodesDoMocks.mockInternationalString());
    // code.setCodeExtends(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_13_V1_CODE_2));
    // assertTrue(code.getCoreRepresentation() instanceof EnumeratedRepresentation);
    // code.setCoreRepresentation(CodesDoMocks.mockTextFormatRepresentation());
    //
    // // Update
    // CodeMetamac codeUpdated = codesService.updateCode(getServiceContextAdministrador(), code);
    //
    // // Validate
    // CodesMetamacAsserts.assertEqualsCode(code, codeUpdated);
    //
    // // Update to remove metadata 'extends'
    // codeUpdated.setCodeExtends(null);
    // codeUpdated = codesService.updateCode(getServiceContextAdministrador(), code);
    //
    // // Validate
    // CodesMetamacAsserts.assertEqualsCode(code, codeUpdated);
    // }

    @Test
    public void testRetrieveCodeByUrn() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V2_CODE_1;
        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, code.getNameableArtefact().getUrn());
    }

    @Test
    public void testRetrieveCodeByUrnWithParentAndChildren() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V2_CODE_2_1;
        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals("codelist-1-v2-code-2-1", code.getUuid());
        assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getNameableArtefact().getUrn());
        assertEquals(1, code.getChildren().size());
        assertEquals(CODELIST_1_V2_CODE_2_1_1, code.getChildren().get(0).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2, code.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        assertEquals(null, code.getItemSchemeVersionFirstLevel());
    }

    // @Test
    // public void testDeleteCode() throws Exception {
    //
    // String urn = CODELIST_1_V2_CODE_3;
    // String codelistUrn = CODELIST_1_V2;
    // String codeExtendsBeforeDeleteUrn = CODELIST_12_V1_CODE_1;
    //
    // CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
    // assertEquals(4, codelistVersion.getItemsFirstLevel().size());
    // assertEquals(8, codelistVersion.getItems().size());
    //
    // // Retrieve code to check extends metadata
    // CodeMetamac codeMetamac = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
    // assertEquals(codeExtendsBeforeDeleteUrn, codeMetamac.getCodeExtends().getNameableArtefact().getUrn());
    //
    // // Delete code
    // codesService.deleteCode(getServiceContextAdministrador(), urn);
    //
    // // Validation
    // try {
    // codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
    // fail("Code deleted");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // // Check do not delete code extends
    // codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeExtendsBeforeDeleteUrn);
    //
    // // Check hierarchy
    // codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
    // assertEquals(3, codelistVersion.getItemsFirstLevel().size());
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
    // assertEquals(7, codelistVersion.getItems().size());
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1);
    // }
    //
    // @Test
    // public void testDeleteCodeWithParentAndChildren() throws Exception {
    //
    // String urn = CODELIST_1_V2_CODE_4_1;
    // String codelistUrn = CODELIST_1_V2;
    //
    // CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
    // assertEquals(4, codelistVersion.getItemsFirstLevel().size());
    // assertEquals(8, codelistVersion.getItems().size());
    //
    // // Delete code
    // codesService.deleteCode(getServiceContextAdministrador(), urn);
    //
    // // Validation
    // try {
    // codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
    // fail("Code deleted");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    //
    // codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
    // assertEquals(4, codelistVersion.getItemsFirstLevel().size());
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_3);
    // assertListItemsContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
    // assertEquals(6, codelistVersion.getItems().size());
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_3);
    // assertListItemsContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
    // }
    //
    // @Test
    // public void testDeleteCodeErrorCodelistPublished() throws Exception {
    //
    // String urn = CODELIST_12_V1_CODE_1;
    // String codelistUrn = CODELIST_12_V1;
    //
    // // Validation
    // try {
    // codesService.deleteCode(getServiceContextAdministrador(), urn);
    // fail("Code can not be deleted");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(codelistUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testRetrieveCodesByCodelistUrn() throws Exception {
    //
    // // Retrieve
    // String codelistUrn = CODELIST_1_V2;
    // List<CodeMetamac> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn);
    //
    // // Validate
    // assertEquals(4, codes.size());
    // {
    // // Code 01
    // CodeMetamac code = codes.get(0);
    // assertEquals(CODELIST_1_V2_CODE_1, code.getNameableArtefact().getUrn());
    // assertEquals(0, code.getChildren().size());
    // }
    // {
    // // Code 02
    // CodeMetamac code = codes.get(1);
    // assertEquals(CODELIST_1_V2_CODE_2, code.getNameableArtefact().getUrn());
    // assertEquals(1, code.getChildren().size());
    // {
    // // Code 02 01
    // CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(0);
    // assertEquals(CODELIST_1_V2_CODE_2_1, codeChild.getNameableArtefact().getUrn());
    // assertEquals(1, codeChild.getChildren().size());
    // {
    // // Code 02 01 01
    // CodeMetamac codeChildChild = (CodeMetamac) codeChild.getChildren().get(0);
    // assertEquals(CODELIST_1_V2_CODE_2_1_1, codeChildChild.getNameableArtefact().getUrn());
    // assertEquals(0, codeChildChild.getChildren().size());
    // }
    // }
    // }
    // {
    // // Code 03
    // CodeMetamac code = codes.get(2);
    // assertEquals(CODELIST_1_V2_CODE_3, code.getNameableArtefact().getUrn());
    // assertEquals(0, code.getChildren().size());
    // }
    // {
    // // Code 04
    // CodeMetamac code = codes.get(3);
    // assertEquals(CODELIST_1_V2_CODE_4, code.getNameableArtefact().getUrn());
    // assertEquals(1, code.getChildren().size());
    // {
    // // Code 04 01
    // CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(0);
    // assertEquals(CODELIST_1_V2_CODE_4_1, codeChild.getNameableArtefact().getUrn());
    // assertEquals(1, codeChild.getChildren().size());
    // {
    // // Code 04 01 01
    // CodeMetamac codeChildChild = (CodeMetamac) codeChild.getChildren().get(0);
    // assertEquals(CODELIST_1_V2_CODE_4_1_1, codeChildChild.getNameableArtefact().getUrn());
    // assertEquals(0, codeChildChild.getChildren().size());
    // }
    // }
    // }
    // }
    //
    // @Test
    // public void testFindCodesByCondition() throws Exception {
    //
    // // Find all
    // {
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).orderBy(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
    // .orderBy(CodeProperties.id()).ascending().distinctRoot().build();
    // PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
    // PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(28, codesPagedResult.getTotalRows());
    // assertEquals(28, codesPagedResult.getValues().size());
    // assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);
    //
    // int i = 0;
    // assertEquals(CODELIST_1_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_2_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_2_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_3_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_3_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_3_V1_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_3_V1_CODE_2_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_3_V1_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_4_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_5_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_6_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_7_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_8_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_10_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_10_V3_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_11_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_12_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_13_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_13_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_13_V1_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(codesPagedResult.getValues().size(), i);
    // }
    //
    // // Find by name (like), code (like) and codelist urn
    // {
    // String name = "Nombre codelist-1-v2-code-2-";
    // String code = "CODE02";
    // String codelistUrn = CODELIST_1_V2;
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).withProperty(CodeProperties.itemSchemeVersion().maintainableArtefact().urn())
    // .eq(codelistUrn).withProperty(CodeProperties.nameableArtefact().code()).like(code + "%").withProperty(CodeProperties.nameableArtefact().name().texts().label())
    // .like(name + "%").orderBy(CodeProperties.id()).ascending().distinctRoot().build();
    // PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
    // PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(2, codesPagedResult.getTotalRows());
    // assertEquals(2, codesPagedResult.getValues().size());
    // assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);
    //
    // int i = 0;
    // assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(codesPagedResult.getValues().size(), i);
    // }
    //
    // // Find by codelist urn paginated
    // {
    // String codelistUrn = CODELIST_1_V2;
    // List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).withProperty(CodeProperties.itemSchemeVersion().maintainableArtefact().urn())
    // .eq(codelistUrn).orderBy(CodeProperties.id()).ascending().distinctRoot().build();
    //
    // // First page
    // {
    // PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
    // PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(8, codesPagedResult.getTotalRows());
    // assertEquals(3, codesPagedResult.getValues().size());
    // assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);
    //
    // int i = 0;
    // assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(codesPagedResult.getValues().size(), i);
    // }
    // // Second page
    // {
    // PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
    // PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(8, codesPagedResult.getTotalRows());
    // assertEquals(3, codesPagedResult.getValues().size());
    // assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);
    //
    // int i = 0;
    // assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(codesPagedResult.getValues().size(), i);
    // }
    // // Third page
    // {
    // PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
    // PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
    //
    // // Validate
    // assertEquals(8, codesPagedResult.getTotalRows());
    // assertEquals(2, codesPagedResult.getValues().size());
    // assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);
    //
    // int i = 0;
    // assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
    // assertEquals(codesPagedResult.getValues().size(), i);
    // }
    // }
    // }

    // private void assertListItemsContainsCode(List<Item> items, String urn) {
    // for (Item item : items) {
    // if (item.getNameableArtefact().getUrn().equals(urn)) {
    // return;
    // }
    // }
    // fail("List does not contain item with urn " + urn);
    // }
    //
    // private void assertListCodesContainsCode(List<CodeMetamac> items, String urn) {
    // for (Item item : items) {
    // if (item.getNameableArtefact().getUrn().equals(urn)) {
    // return;
    // }
    // }
    // fail("List does not contain item with urn " + urn);
    // }

    // ------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------------------

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}

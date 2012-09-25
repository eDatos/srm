package org.siemac.metamac.srm.core.organisation.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class OrganisationsMetamacServiceTest extends SrmBaseTest implements OrganisationsMetamacServiceTestBase {

    @Autowired
    private OrganisationsMetamacService organisationsService;

    @Test
    public void testCreateOrganisationScheme() throws Exception {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = OrganisationsMetamacDoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.AGENCY_SCHEME);

        // Create
        OrganisationSchemeVersionMetamac organisationSchemeVersionCreated = organisationsService.createOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
        String urn = organisationSchemeVersionCreated.getMaintainableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        OrganisationSchemeVersionMetamac organisationSchemeVersionRetrieved = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionRetrieved.getLifecycleMetadata().getProcStatus());
        assertFalse(organisationSchemeVersion.getMaintainableArtefact().getIsExternalReference());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(organisationSchemeVersion, organisationSchemeVersionRetrieved);
    }

//    @Test
//    public void testUpdateOrganisationScheme() throws Exception {
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1);
//        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
//
//        OrganisationSchemeVersion organisationSchemeVersionUpdated = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
//
//        assertNotNull(organisationSchemeVersionUpdated);
//    }
//    
//    @Test
//    public void testUpdateOrganisationSchemePublished() throws Exception {
//        String[] urns = {ORGANISATION_SCHEME_7_V2, ORGANISATION_SCHEME_7_V1};
//        for (String urn : urns) {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//
//            try {
//                organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
//                organisationSchemeVersion = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
//                fail("wrong proc status");
//            } catch (MetamacException e) {
//                assertEquals(1, e.getExceptionItems().size());
//                assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
//                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            }
//        }
//    }
//
//    @Test
//    public void testUpdateOrganisationSchemeErrorExternalReference() throws Exception {
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_7_V2);
//        organisationSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);
//
//        try {
//            organisationSchemeVersion = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
//            fail("organisation scheme cannot be a external reference");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testUpdateOrganisationSchemeErrorChangeTypeOrganisationSchemeAlreadyPublished() throws Exception {
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
//        assertEquals(OrganisationSchemeTypeEnum.TRANSVERSAL, organisationSchemeVersion.getType());
//        organisationSchemeVersion.setType(OrganisationSchemeTypeEnum.GLOSSARY);
//
//        try {
//            organisationSchemeVersion = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
//            fail("metadata unmodifiable");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ServiceExceptionParameters.ORGANISATION_SCHEME_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }

    @Test
    public void testRetrieveOrganisationSchemeByUrn() throws Exception {

        // Retrieve
        String urn = ORGANISATION_SCHEME_1_V1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, organisationSchemeVersion.getMaintainableArtefact().getUrn());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
        assertEquals("user1", organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
        assertEquals("user3", organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
        assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
    }

//    @Test
//    @Override
//    public void testFindOrganisationSchemesByCondition() throws Exception {
//
//        // Find all
//        {
//            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
//                    .orderBy(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
//            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
//            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//            // Validate
//            assertEquals(16, organisationSchemeVersionPagedResult.getTotalRows());
//            int i = 0;
//            assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_4_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_5_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_6_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_7_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_8_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V3, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_11_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_12_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(organisationSchemeVersionPagedResult.getTotalRows(), i);
//        }
//
//        // Find internally published
//        {
//            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
//                    .withProperty(OrganisationSchemeVersionMetamacProperties.lifecycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
//                    .orderBy(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
//            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
//            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//            // Validate
//            assertEquals(4, organisationSchemeVersionPagedResult.getTotalRows());
//            int i = 0;
//            assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(organisationSchemeVersionPagedResult.getTotalRows(), i);
//        }
//
//        // Find lasts versions
//        {
//            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
//                    .withProperty(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE)
//                    .orderBy(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
//            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
//            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//            // Validate
//            assertEquals(12, organisationSchemeVersionPagedResult.getTotalRows());
//            int i = 0;
//            assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_4_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_5_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_6_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_8_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V3, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_11_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_12_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
//        }
//    }
//
//    @Test
//    @Override
//    public void testRetrieveOrganisationSchemeVersions() throws Exception {
//
//        // Retrieve all versions
//        String urn = ORGANISATION_SCHEME_1_V1;
//        List<OrganisationSchemeVersionMetamac> organisationSchemeVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), urn);
//
//        // Validate
//        assertEquals(2, organisationSchemeVersions.size());
//        assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeVersions.get(0).getMaintainableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersions.get(1).getMaintainableArtefact().getUrn());
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToProductionValidation() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_2_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//        }
//
//        // Send to production validation
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.sendOrganisationSchemeToProductionValidation(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//        // Validate retrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//
//            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToProductionValidationInProcStatusRejected() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_4_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//        }
//
//        // Send to production validation
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.sendOrganisationSchemeToProductionValidation(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//        }
//        // Validate retrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//
//            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToProductionValidationErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//        try {
//            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToProductionValidationErrorWrongProcStatus() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V1;
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//        }
//
//        try {
//            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToProductionValidationErrorMetadataRequired() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_2_V1;
//
//        // Update to clear metadata
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//        organisationSchemeVersion.setIsPartial(null);
//        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
//        organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
//
//        // Send to production validation
//        try {
//            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme metadata required");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//
//            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToDiffusionValidation() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_5_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//        }
//
//        // Sends to diffusion validation
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.sendOrganisationSchemeToDiffusionValidation(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//        // Validate retrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//
//            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToDiffusionValidationErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//        try {
//            organisationsService.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testSendOrganisationSchemeToDiffusionValidationErrorWrongProcStatus() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_2_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//        }
//
//        try {
//            organisationsService.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//        }
//    }
//
//    @Test
//    public void testRejectOrganisationSchemeProductionValidation() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_5_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//        }
//
//        // Reject validation
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.rejectOrganisationSchemeProductionValidation(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//        // Validate restrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//    }
//
//    @Test
//    public void testRejectOrganisationSchemeProductionValidationErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//        try {
//            organisationsService.rejectOrganisationSchemeProductionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testRejectOrganisationSchemeProductionValidationErrorWrongProcStatus() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V1;
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//        }
//
//        try {
//            organisationsService.rejectOrganisationSchemeProductionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//        }
//    }
//
//    @Test
//    public void testRejectOrganisationSchemeDiffusionValidation() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_6_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//        }
//
//        // Reject validation
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.rejectOrganisationSchemeDiffusionValidation(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//        // Validate retrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//        }
//    }
//
//    @Test
//    public void testRejectOrganisationSchemeDiffusionValidationErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//        try {
//            organisationsService.rejectOrganisationSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testRejectOrganisationSchemeDiffusionValidationErrorWrongProcStatus() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V1;
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//        }
//
//        try {
//            organisationsService.rejectOrganisationSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//        }
//    }
//
//    @Test
//    public void testPublishInternallyOrganisationScheme() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_6_V1;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
//        }
//
//        // Publish internally
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.publishInternallyOrganisationScheme(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
//        }
//        // Validate retrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
//        }
//    }
//
//    @Test
//    public void testPublishInternallyOrganisationSchemeErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//        try {
//            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testPublishInternallyOrganisationSchemeErrorWrongProcStatus() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V1;
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//        }
//
//        try {
//            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//        }
//    }
//
//    @Test
//    public void testPublishInternallyOrganisationSchemeErrorOrganisationExtendsInOrganisationSchemeNoPublished() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_6_V1;
//        String urnOrganisationOrganisationSchemeToPublish = ORGANISATION_SCHEME_6_V1_ORGANISATION_1;
//        String urnOrganisationExtends = ORGANISATION_SCHEME_1_V2_ORGANISATION_1;
//
//        // Update organisation to add extends organisation of organisation scheme to publish
//        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urnOrganisationExtends);
//        OrganisationMetamac organisationOrganisationSchemeToPublish = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urnOrganisationOrganisationSchemeToPublish);
//        organisationOrganisationSchemeToPublish.setOrganisationExtends(organisation);
//        organisationOrganisationSchemeToPublish.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
//        organisationsService.updateOrganisation(getServiceContextAdministrador(), organisationOrganisationSchemeToPublish);
//
//        try {
//            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WITH_RELATED_ORGANISATIONS_NOT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(urnOrganisationExtends, e.getExceptionItems().get(0).getMessageParameters()[1]);
//        }
//    }
//
//    @Test
//    public void testPublishExternallyOrganisationScheme() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_7_V2;
//        ServiceContext ctx = getServiceContextAdministrador();
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidFrom());
//            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getIsExternalPublicationFailed());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationFailedDate());
//
//            OrganisationSchemeVersionMetamac organisationSchemeVersionExternallyPublished = organisationsService.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_7_V1);
//            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersionExternallyPublished.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
//            assertNull(organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo());
//        }
//
//        // Publish externally
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.publishExternallyOrganisationScheme(ctx, urn);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
//            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getIsExternalPublicationFailed());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationFailedDate());
//        }
//        // Validate retrieving
//        {
//            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
//            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
//            assertNotNull(organisationSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationDate().toDate()));
//            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
//            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getIsExternalPublicationFailed());
//            assertNull(organisationSchemeVersion.getLifecycleMetadata().getExternalPublicationFailedDate());
//        }
//        // Validate previous published externally versions
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersionExternallyPublished = organisationsService.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_7_V1);
//            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersionExternallyPublished.getLifecycleMetadata().getProcStatus());
//            assertNotNull(organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
//            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo().toDate()));
//        }
//    }
//
//    @Test
//    public void testPublishExternallyOrganisationSchemeErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//        try {
//            organisationsService.publishExternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testPublishExternallyOrganisationSchemeErrorWrongProcStatus() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V2;
//
//        {
//            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersion.getLifecycleMetadata().getProcStatus());
//        }
//
//        try {
//            organisationsService.publishExternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//        }
//    }
//
//    @Test
//    public void testPublishExternallyOrganisationSchemeErrorOrganisationExtendsInOrganisationSchemeNoPublished() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V1;
//
//        try {
//            organisationsService.publishExternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme wrong proc status");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WITH_RELATED_ORGANISATIONS_VALIDITY_NOT_STARTED.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ORGANISATION_SCHEME_10_V3_ORGANISATION_1, e.getExceptionItems().get(0).getMessageParameters()[1]);
//        }
//    }
//
//    @Test
//    public void testDeleteOrganisationScheme() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_2_V1;
//
//        // Delete organisation scheme only with version in draft
//        organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urn);
//
//        // Validation
//        try {
//            organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testDeleteOrganisationSchemeWithVersionPublishedAndVersionDraft() throws Exception {
//
//        String urnV1 = ORGANISATION_SCHEME_1_V1;
//        String urnV2 = ORGANISATION_SCHEME_1_V2;
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersionV1 = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV1);
//        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersionV1.getLifecycleMetadata().getProcStatus());
//        OrganisationSchemeVersionMetamac organisationSchemeVersionV2 = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV2);
//        assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionV2.getLifecycleMetadata().getProcStatus());
//
//        organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urnV2);
//
//        // Validation
//        try {
//            organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV2);
//            fail("OrganisationScheme deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//        organisationSchemeVersionV1 = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV1);
//        assertTrue(organisationSchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
//        assertNull(organisationSchemeVersionV1.getMaintainableArtefact().getReplacedBy());
//    }
//
//    @Test
//    public void testDeleteOrganisationSchemeErrorPublished() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_10_V2;
//
//        // Validation
//        try {
//            organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme can not be deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testDeleteOrganisationSchemeErrorOtherOrganisationExtends() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_5_V1;
//
//        // Validation
//        try {
//            organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urn);
//            fail("OrganisationScheme can not be deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WITH_RELATED_ORGANISATIONS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ORGANISATION_SCHEME_3_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, e.getExceptionItems().get(0).getMessageParameters()[1]);
//        }
//    }
//
//    @Test
//    public void testVersioningOrganisationScheme() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_3_V1;
//        String versionExpected = "02.000";
//        String urnExpected = "urn:sdmx:org.sdmx.infomodel.organisationscheme.OrganisationScheme=ISTAC:ORGANISATIONSCHEME03(02.000)";
//        String urnExpectedOrganisation1 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME03(02.000).ORGANISATION01";
//        String urnExpectedOrganisation2 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME03(02.000).ORGANISATION02";
//        String urnExpectedOrganisation21 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME03(02.000).ORGANISATION0201";
//        String urnExpectedOrganisation211 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME03(02.000).ORGANISATION020101";
//        String urnExpectedOrganisation22 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME03(02.000).ORGANISATION0202";
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//        OrganisationSchemeVersionMetamac organisationSchemeVersionNewVersion = organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
//
//        // Validate response
//        {
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
//            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
//            OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
//        }
//
//        // Validate retrieving
//        // New version
//        {
//            organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
//            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
//            assertEquals("01.000", organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplaceTo());
//            assertEquals(null, organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplacedBy());
//            assertTrue(organisationSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
//            OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
//
//            // Organisations
//            assertEquals(5, organisationSchemeVersionNewVersion.getItems().size());
//            assertEquals(urnExpectedOrganisation1, organisationSchemeVersionNewVersion.getItems().get(0).getNameableArtefact().getUrn());
//            assertEquals(urnExpectedOrganisation2, organisationSchemeVersionNewVersion.getItems().get(1).getNameableArtefact().getUrn());
//            assertEquals(urnExpectedOrganisation21, organisationSchemeVersionNewVersion.getItems().get(2).getNameableArtefact().getUrn());
//            assertEquals(urnExpectedOrganisation211, organisationSchemeVersionNewVersion.getItems().get(3).getNameableArtefact().getUrn());
//            assertEquals(urnExpectedOrganisation22, organisationSchemeVersionNewVersion.getItems().get(4).getNameableArtefact().getUrn());
//
//            assertEquals(2, organisationSchemeVersionNewVersion.getItemsFirstLevel().size());
//            {
//                OrganisationMetamac organisation = (OrganisationMetamac) organisationSchemeVersionNewVersion.getItemsFirstLevel().get(0);
//                assertEquals(urnExpectedOrganisation1, organisation.getNameableArtefact().getUrn());
//
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "es", "Nombre organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getPluralName(), "es", "PluralName organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getAcronym(), "es", "Acronym organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getDescriptionSource(), "es", "DescriptionSource organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getContext(), "es", "Context organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getDocMethod(), "es", "DocMethod organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getDerivation(), "es", "Derivation organisationScheme-3-v1-organisation-1", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getLegalActs(), "es", "LegalActs organisationScheme-3-v1-organisation-1", null, null);
//                assertEquals(OrganisationRoleEnum.ATTRIBUTE, organisation.getSdmxRelatedArtefact());
//                assertEquals(ORGANISATION_TYPE_DERIVED, organisation.getType().getIdentifier());
//                assertEquals(ORGANISATION_SCHEME_5_V1_ORGANISATION_1, organisation.getOrganisationExtends().getNameableArtefact().getUrn());
//
//                // related organisations
//                List<OrganisationMetamac> relatedOrganisations = organisationsService.retrieveRelatedOrganisations(getServiceContextAdministrador(), organisation.getNameableArtefact().getUrn());
//                assertEquals(1, relatedOrganisations.size());
//                assertListOrganisationsContainsOrganisation(relatedOrganisations, urnExpectedOrganisation211);
//
//                assertEquals(0, organisation.getChildren().size());
//            }
//            {
//                OrganisationMetamac organisation = (OrganisationMetamac) organisationSchemeVersionNewVersion.getItemsFirstLevel().get(1);
//                assertEquals(urnExpectedOrganisation2, organisation.getNameableArtefact().getUrn());
//
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "es", "Nombre organisationScheme-3-v1-organisation-2", null, null);
//                OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getPluralName(), "es", "PluralName organisationScheme-3-v1-organisation-2", null, null);
//                assertNull(organisation.getAcronym());
//                assertNull(organisation.getDescriptionSource());
//                assertNull(organisation.getContext());
//                assertNull(organisation.getDocMethod());
//                assertNull(organisation.getDerivation());
//                assertNull(organisation.getLegalActs());
//                assertNull(organisation.getSdmxRelatedArtefact());
//                assertNull(organisation.getType());
//                assertNull(organisation.getOrganisationExtends());
//
//                // related organisations
//                List<OrganisationMetamac> relatedOrganisations = organisationsService.retrieveRelatedOrganisations(getServiceContextAdministrador(), organisation.getNameableArtefact().getUrn());
//                assertEquals(2, relatedOrganisations.size());
//                assertListOrganisationsContainsOrganisation(relatedOrganisations, urnExpectedOrganisation21);
//                assertListOrganisationsContainsOrganisation(relatedOrganisations, urnExpectedOrganisation22);
//
//                assertEquals(2, organisation.getChildren().size());
//                {
//                    OrganisationMetamac organisationChild = (OrganisationMetamac) organisation.getChildren().get(0);
//                    assertEquals(urnExpectedOrganisation21, organisationChild.getNameableArtefact().getUrn());
//
//                    // related organisations
//                    relatedOrganisations = organisationsService.retrieveRelatedOrganisations(getServiceContextAdministrador(), organisationChild.getNameableArtefact().getUrn());
//                    assertEquals(1, relatedOrganisations.size());
//                    assertListOrganisationsContainsOrganisation(relatedOrganisations, urnExpectedOrganisation2);
//
//                    assertEquals(1, organisationChild.getChildren().size());
//                    {
//                        OrganisationMetamac organisationChildChild = (OrganisationMetamac) organisationChild.getChildren().get(0);
//                        assertEquals(urnExpectedOrganisation211, organisationChildChild.getNameableArtefact().getUrn());
//                        assertEquals(ORGANISATION_SCHEME_6_V1_ORGANISATION_1, organisationChildChild.getOrganisationExtends().getNameableArtefact().getUrn());
//
//                        // related organisations
//                        relatedOrganisations = organisationsService.retrieveRelatedOrganisations(getServiceContextAdministrador(), organisationChildChild.getNameableArtefact().getUrn());
//                        assertEquals(1, relatedOrganisations.size());
//                        assertListOrganisationsContainsOrganisation(relatedOrganisations, urnExpectedOrganisation1);
//
//                        assertEquals(0, organisationChildChild.getChildren().size());
//                    }
//                }
//                {
//                    OrganisationMetamac organisationChild = (OrganisationMetamac) organisation.getChildren().get(1);
//                    assertEquals(urnExpectedOrganisation22, organisationChild.getNameableArtefact().getUrn());
//
//                    // related organisations
//                    relatedOrganisations = organisationsService.retrieveRelatedOrganisations(getServiceContextAdministrador(), organisationChild.getNameableArtefact().getUrn());
//                    assertEquals(1, relatedOrganisations.size());
//                    assertListOrganisationsContainsOrganisation(relatedOrganisations, urnExpectedOrganisation2);
//
//                    assertEquals(0, organisationChild.getChildren().size());
//                }
//            }
//        }
//
//        // Copied version
//        {
//            organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
//            assertEquals("01.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urn, organisationSchemeVersionToCopy.getMaintainableArtefact().getUrn());
//            assertEquals(null, organisationSchemeVersionToCopy.getMaintainableArtefact().getReplaceTo());
//            assertEquals(versionExpected, organisationSchemeVersionToCopy.getMaintainableArtefact().getReplacedBy());
//            assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
//        }
//        // All versions
//        {
//            List<OrganisationSchemeVersionMetamac> allVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), urn);
//            assertEquals(2, allVersions.size());
//            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
//            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
//        }
//    }
//
//    @Test
//    public void testVersioningOrganisationSchemeWithTwoVersionsPublished() throws Exception {
//
//        // This test checks the copy from one version but replacing to another one that is last version.
//
//        String urnToCopy = ORGANISATION_SCHEME_7_V1;
//        String urnLastVersion = ORGANISATION_SCHEME_7_V2;
//        String versionExpected = "03.000";
//        String urnExpected = "urn:sdmx:org.sdmx.infomodel.organisationscheme.OrganisationScheme=ISTAC:ORGANISATIONSCHEME07(03.000)";
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
//        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersionToCopy.getLifecycleMetadata().getProcStatus());
//        assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersionLast = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
//        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersionLast.getLifecycleMetadata().getProcStatus());
//        assertTrue(organisationSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersionNewVersion = organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);
//
//        // Validate response
//        {
//            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
//            OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
//        }
//
//        // Validate retrieving
//        {
//            // New version
//            organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
//            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
//            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
//            assertEquals("02.000", organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplaceTo());
//            assertEquals(null, organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplacedBy());
//            assertTrue(organisationSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
//            OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
//
//            // Version copied
//            organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
//            assertEquals("01.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urnToCopy, organisationSchemeVersionToCopy.getMaintainableArtefact().getUrn());
//            assertEquals(null, organisationSchemeVersionToCopy.getMaintainableArtefact().getReplaceTo());
//            assertEquals("02.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getReplacedBy());
//            assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
//
//            // Last version
//            organisationSchemeVersionLast = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
//            assertEquals("02.000", organisationSchemeVersionLast.getMaintainableArtefact().getVersionLogic());
//            assertEquals(urnLastVersion, organisationSchemeVersionLast.getMaintainableArtefact().getUrn());
//            assertEquals("01.000", organisationSchemeVersionLast.getMaintainableArtefact().getReplaceTo());
//            assertEquals(versionExpected, organisationSchemeVersionLast.getMaintainableArtefact().getReplacedBy());
//            assertFalse(organisationSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());
//
//            // All versions
//            List<OrganisationSchemeVersionMetamac> allVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), organisationSchemeVersionNewVersion.getMaintainableArtefact()
//                    .getUrn());
//            assertEquals(3, allVersions.size());
//            assertEquals(urnToCopy, allVersions.get(0).getMaintainableArtefact().getUrn());
//            assertEquals(urnLastVersion, allVersions.get(1).getMaintainableArtefact().getUrn());
//            assertEquals(urnExpected, allVersions.get(2).getMaintainableArtefact().getUrn());
//        }
//    }
//
//    @Test
//    public void testVersioningOrganisationSchemeCheckRelatedOrganisationsRole() throws Exception {
//
//        // Check related roles until versioning
//        {
//            // Organisation 1
//            {
//                List<OrganisationMetamac> relatedOrganisationsRole = organisationsService.retrieveRelatedOrganisationsRoles(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1_ORGANISATION_1);
//                assertEquals(2, relatedOrganisationsRole.size());
//                assertListOrganisationsContainsOrganisation(relatedOrganisationsRole, ORGANISATION_SCHEME_3_V1_ORGANISATION_1);
//                assertListOrganisationsContainsOrganisation(relatedOrganisationsRole, ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1);
//            }
//
//            // Organisation 2
//            {
//                List<OrganisationMetamac> relatedOrganisationsRole = organisationsService.retrieveRelatedOrganisationsRoles(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1_ORGANISATION_2);
//                assertEquals(1, relatedOrganisationsRole.size());
//                assertListOrganisationsContainsOrganisation(relatedOrganisationsRole, ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1);
//            }
//
//        }
//
//        // We prefer do isolated tests but.. publish internally to versioning and check copy of related organisations type 'role'
//        String urn = ORGANISATION_SCHEME_2_V1;
//        organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
//        organisationsService.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
//        organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn);
//
//        // Versioning
//        organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1, VersionTypeEnum.MAJOR);
//        String urnExpectedOrganisation1 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME02(01.000).ORGANISATION01";
//        String urnExpectedOrganisation2 = "urn:sdmx:org.sdmx.infomodel.organisationscheme.Organisation=ISTAC:ORGANISATIONSCHEME02(01.000).ORGANISATION02";
//
//        // Only check related roles
//        {
//            // Organisation 1
//            {
//                List<OrganisationMetamac> relatedOrganisationsRole = organisationsService.retrieveRelatedOrganisationsRoles(getServiceContextAdministrador(), urnExpectedOrganisation1);
//                assertEquals(2, relatedOrganisationsRole.size());
//                assertListOrganisationsContainsOrganisation(relatedOrganisationsRole, ORGANISATION_SCHEME_3_V1_ORGANISATION_1);
//                assertListOrganisationsContainsOrganisation(relatedOrganisationsRole, ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1);
//            }
//
//            // Organisation 2
//            {
//                List<OrganisationMetamac> relatedOrganisationsRole = organisationsService.retrieveRelatedOrganisationsRoles(getServiceContextAdministrador(), urnExpectedOrganisation2);
//                assertEquals(1, relatedOrganisationsRole.size());
//                assertListOrganisationsContainsOrganisation(relatedOrganisationsRole, ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1);
//            }
//
//        }
//    }
//
//    @Test
//    public void testVersioningOrganisationSchemeErrorAlreadyExistsDraft() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V1;
//
//        try {
//            organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
//            fail("OrganisationScheme already exists in no final");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ORGANISATION_SCHEME_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testCancelOrganisationSchemeValidity() throws Exception {
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.cancelOrganisationSchemeValidity(getServiceContextAdministrador(), ORGANISATION_SCHEME_7_V1);
//
//        assertNotNull(organisationSchemeVersion);
//        assertNotNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
//    }
//
//    @Test
//    public void testCancelOrganisationSchemeValidityErrorWrongProcStatus() throws Exception {
//        String[] urns = {ORGANISATION_SCHEME_1_V1, ORGANISATION_SCHEME_4_V1, ORGANISATION_SCHEME_6_V1};
//        for (String urn : urns) {
//            try {
//                organisationsService.cancelOrganisationSchemeValidity(getServiceContextAdministrador(), urn);
//                fail("wrong procStatus");
//            } catch (MetamacException e) {
//                assertEquals(1, e.getExceptionItems().size());
//                assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
//                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//                assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
//            }
//        }
//    }
//
//    @Test
//    public void testCreateOrganisation() throws Exception {
//
//        OrganisationType organisationType = organisationsService.retrieveOrganisationTypeByIdentifier(getServiceContextAdministrador(), ORGANISATION_TYPE_DIRECT);
//        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(organisationType);
//        organisation.setParent(null);
//        OrganisationMetamac organisationExtends = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_3_V1_ORGANISATION_1);
//        organisation.setOrganisationExtends(organisationExtends);
//
//        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//
//        // Create
//        OrganisationMetamac organisationSchemeVersionCreated = organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
//        String urn = organisationSchemeVersionCreated.getNameableArtefact().getUrn();
//
//        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
//        OrganisationMetamac organisationRetrieved = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationRetrieved);
//
//        // Validate new structure
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//        assertEquals(5, organisationSchemeVersion.getItemsFirstLevel().size());
//        assertEquals(9, organisationSchemeVersion.getItems().size());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationSchemeVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationSchemeVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationSchemeVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationSchemeVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
//        assertEquals(organisationRetrieved.getNameableArtefact().getUrn(), organisationSchemeVersion.getItemsFirstLevel().get(4).getNameableArtefact().getUrn());
//    }
//
//    @Test
//    public void testCreateOrganisationSuborganisation() throws Exception {
//
//        OrganisationType organisationType = null;
//        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(organisationType);
//        OrganisationMetamac organisationParent = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        organisation.setParent(organisationParent);
//        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//
//        // Create
//        OrganisationMetamac organisationSchemeVersionCreated = organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
//        String urn = organisationSchemeVersionCreated.getNameableArtefact().getUrn();
//
//        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
//        OrganisationMetamac organisationRetrieved = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationRetrieved);
//
//        // Validate new structure
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
//        assertEquals(9, organisationSchemeVersion.getItems().size());
//
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationSchemeVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
//        assertEquals(organisationRetrieved.getNameableArtefact().getUrn(), organisationSchemeVersion.getItemsFirstLevel().get(0).getChildren().get(0).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationSchemeVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationSchemeVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationSchemeVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
//    }
//
//    @Test
//    public void testCreateOrganisationErrorExtendsSameOrganisationScheme() throws Exception {
//
//        OrganisationType organisationType = null;
//        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(organisationType);
//        OrganisationMetamac organisationExtends = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        organisation.setOrganisationExtends(organisationExtends);
//        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//
//        // Create
//        try {
//            organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
//            fail("Organisation deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ServiceExceptionParameters.ORGANISATION_EXTENDS, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testUpdateOrganisation() throws Exception {
//
//        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
//        organisation.getNameableArtefact().setName(OrganisationsDoMocks.mockInternationalString());
//        organisation.setOrganisationExtends(organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_10_V3_ORGANISATION_1));
//        assertTrue(organisation.getCoreRepresentation() instanceof EnumeratedRepresentation);
//        organisation.setCoreRepresentation(OrganisationsDoMocks.mockOrganisationTextFormatRepresentation());
//
//        // Update
//        OrganisationMetamac organisationUpdated = organisationsService.updateOrganisation(getServiceContextAdministrador(), organisation);
//
//        // Validate
//        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationUpdated);
//
//        // Update to remove metadata 'extends'
//        organisationUpdated.setOrganisationExtends(null);
//        organisationUpdated = organisationsService.updateOrganisation(getServiceContextAdministrador(), organisation);
//
//        // Validate
//        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationUpdated);
//    }
//
//    @Test
//    public void testRetrieveOrganisationByUrn() throws Exception {
//        // Retrieve
//        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_1;
//        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//
//        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
//        assertEquals(urn, organisation.getNameableArtefact().getUrn());
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getPluralName(), "es", "PluralName organisationScheme-1-v2-organisation-1", null, null);
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getAcronym(), "es", "Acronym organisationScheme-1-v2-organisation-1", null, null);
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getDescriptionSource(), "es", "DescriptionSource organisationScheme-1-v2-organisation-1", null, null);
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getContext(), "es", "Context organisationScheme-1-v2-organisation-1", null, null);
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getDocMethod(), "es", "DocMethod organisationScheme-1-v2-organisation-1", null, null);
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getDerivation(), "es", "Derivation organisationScheme-1-v2-organisation-1", null, null);
//        OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getLegalActs(), "es", "LegalActs organisationScheme-1-v2-organisation-1", null, null);
//        assertEquals(OrganisationRoleEnum.ATTRIBUTE, organisation.getSdmxRelatedArtefact());
//        assertEquals(ORGANISATION_TYPE_DIRECT, organisation.getType().getIdentifier());
//        assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, organisation.getOrganisationExtends().getNameableArtefact().getUrn());
//    }
//
//    @Test
//    public void testRetrieveOrganisationByUrnWithParentAndChildren() throws Exception {
//
//        // Retrieve
//        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1;
//        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//
//        // Validate
//        assertEquals("organisationScheme-1-v2-organisation-2-1", organisation.getUuid());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getParent().getNameableArtefact().getUrn());
//        assertEquals(1, organisation.getChildren().size());
//        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisation.getChildren().get(0).getNameableArtefact().getUrn());
//        assertEquals(ORGANISATION_SCHEME_1_V2, organisation.getItemSchemeVersion().getMaintainableArtefact().getUrn());
//        assertEquals(null, organisation.getItemSchemeVersionFirstLevel());
//    }
//
//    @Test
//    public void testDeleteOrganisation() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_3;
//        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//        String organisationExtendsBeforeDeleteUrn = ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1;
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
//        assertEquals(8, organisationSchemeVersion.getItems().size());
//
//        // Retrieve organisation to check extends metadata
//        OrganisationMetamac organisationMetamac = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//        assertEquals(organisationExtendsBeforeDeleteUrn, organisationMetamac.getOrganisationExtends().getNameableArtefact().getUrn());
//
//        // Delete organisation
//        organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
//
//        // Validation
//        try {
//            organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//            fail("Organisation deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//        // Check do not delete organisation extends
//        organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), organisationExtendsBeforeDeleteUrn);
//
//        // Check hierarchy
//        organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//        assertEquals(3, organisationSchemeVersion.getItemsFirstLevel().size());
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
//        assertEquals(7, organisationSchemeVersion.getItems().size());
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
//    }
//
//    @Test
//    public void testDeleteOrganisationWithParentAndChildren() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1;
//        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
//        assertEquals(8, organisationSchemeVersion.getItems().size());
//
//        // Delete organisation
//        organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
//
//        // Validation
//        try {
//            organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
//            fail("Organisation deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//
//        organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
//        assertEquals(6, organisationSchemeVersion.getItems().size());
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
//        assertListItemsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
//    }
//
//    @Test
//    public void testDeleteOrganisationErrorOrganisationRelatedRole() throws Exception {
//        // In SDMX module
//    }
//
//    @Test
//    public void testDeleteOrganisationErrorChildrenWithOrganisationRelated() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_2;
//
//        // Validation
//        try {
//            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
//            fail("Organisation can not be deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_WITH_RELATED_ORGANISATIONS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertTrue(ORGANISATION_SCHEME_1_V2_ORGANISATION_1.equals(e.getExceptionItems().get(0).getMessageParameters()[1])
//                    || ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1.equals(e.getExceptionItems().get(0).getMessageParameters()[1]));
//        }
//    }
//
//    @Test
//    public void testDeleteOrganisationErrorOrganisationRelatedAsExtends() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_5_V1_ORGANISATION_1;
//
//        // Validation
//        try {
//            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
//            fail("Organisation can not be deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_WITH_RELATED_ORGANISATIONS.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, e.getExceptionItems().get(0).getMessageParameters()[1]);
//        }
//    }
//
//    @Test
//    public void testDeleteOrganisationErrorOrganisationSchemePublished() throws Exception {
//
//        String urn = ORGANISATION_SCHEME_12_V1_ORGANISATION_1;
//        String organisationSchemeUrn = ORGANISATION_SCHEME_12_V1;
//
//        // Validation
//        try {
//            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
//            fail("Organisation can not be deleted");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_SCHEME_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(organisationSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    @Test
//    public void testRetrieveOrganisationsByOrganisationSchemeUrn() throws Exception {
//
//        // Retrieve
//        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//        List<OrganisationMetamac> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrn(getServiceContextAdministrador(), organisationSchemeUrn);
//
//        // Validate
//        assertEquals(4, organisations.size());
//        {
//            // Organisation 01
//            OrganisationMetamac organisation = organisations.get(0);
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisation.getNameableArtefact().getUrn());
//            assertEquals(0, organisation.getChildren().size());
//        }
//        {
//            // Organisation 02
//            OrganisationMetamac organisation = organisations.get(1);
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getNameableArtefact().getUrn());
//            assertEquals(1, organisation.getChildren().size());
//            {
//                // Organisation 02 01
//                OrganisationMetamac organisationChild = (OrganisationMetamac) organisation.getChildren().get(0);
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationChild.getNameableArtefact().getUrn());
//                assertEquals(1, organisationChild.getChildren().size());
//                {
//                    // Organisation 02 01 01
//                    OrganisationMetamac organisationChildChild = (OrganisationMetamac) organisationChild.getChildren().get(0);
//                    assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationChildChild.getNameableArtefact().getUrn());
//                    assertEquals(0, organisationChildChild.getChildren().size());
//                }
//            }
//        }
//        {
//            // Organisation 03
//            OrganisationMetamac organisation = organisations.get(2);
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisation.getNameableArtefact().getUrn());
//            assertEquals(0, organisation.getChildren().size());
//        }
//        {
//            // Organisation 04
//            OrganisationMetamac organisation = organisations.get(3);
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisation.getNameableArtefact().getUrn());
//            assertEquals(1, organisation.getChildren().size());
//            {
//                // Organisation 04 01
//                OrganisationMetamac organisationChild = (OrganisationMetamac) organisation.getChildren().get(0);
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationChild.getNameableArtefact().getUrn());
//                assertEquals(1, organisationChild.getChildren().size());
//                {
//                    // Organisation 04 01 01
//                    OrganisationMetamac organisationChildChild = (OrganisationMetamac) organisationChild.getChildren().get(0);
//                    assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationChildChild.getNameableArtefact().getUrn());
//                    assertEquals(0, organisationChildChild.getChildren().size());
//                }
//            }
//        }
//    }
//
//    @Test
//    public void testFindOrganisationsByCondition() throws Exception {
//
//        // Find all
//        {
//            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Organisation.class).orderBy(OrganisationProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
//                    .orderBy(OrganisationProperties.id()).ascending().distinctRoot().build();
//            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
//            PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//            // Validate
//            assertEquals(25, organisationsPagedResult.getTotalRows());
//            assertEquals(25, organisationsPagedResult.getValues().size());
//            assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);
//
//            int i = 0;
//            assertEquals(ORGANISATION_SCHEME_1_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_4_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_5_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_6_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_7_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_8_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_10_V3_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_11_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_12_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(organisationsPagedResult.getValues().size(), i);
//        }
//
//        // Find by name (like), code (like) and organisation scheme urn
//        {
//            String name = "Nombre organisationScheme-1-v2-organisation-2-";
//            String code = "ORGANISATION02";
//            String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Organisation.class).withProperty(OrganisationProperties.itemSchemeVersion().maintainableArtefact().urn())
//                    .eq(organisationSchemeUrn).withProperty(OrganisationProperties.nameableArtefact().code()).like(code + "%").withProperty(OrganisationProperties.nameableArtefact().name().texts().label())
//                    .like(name + "%").orderBy(OrganisationProperties.id()).ascending().distinctRoot().build();
//            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
//            PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//            // Validate
//            assertEquals(2, organisationsPagedResult.getTotalRows());
//            assertEquals(2, organisationsPagedResult.getValues().size());
//            assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);
//
//            int i = 0;
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//            assertEquals(organisationsPagedResult.getValues().size(), i);
//        }
//
//        // Find by organisation scheme urn paginated
//        {
//            String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
//            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Organisation.class).withProperty(OrganisationProperties.itemSchemeVersion().maintainableArtefact().urn())
//                    .eq(organisationSchemeUrn).orderBy(OrganisationProperties.id()).ascending().distinctRoot().build();
//
//            // First page
//            {
//                PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
//                PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//                // Validate
//                assertEquals(8, organisationsPagedResult.getTotalRows());
//                assertEquals(3, organisationsPagedResult.getValues().size());
//                assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);
//
//                int i = 0;
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(organisationsPagedResult.getValues().size(), i);
//            }
//            // Second page
//            {
//                PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
//                PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//                // Validate
//                assertEquals(8, organisationsPagedResult.getTotalRows());
//                assertEquals(3, organisationsPagedResult.getValues().size());
//                assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);
//
//                int i = 0;
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(organisationsPagedResult.getValues().size(), i);
//            }
//            // Third page
//            {
//                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
//                PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
//
//                // Validate
//                assertEquals(8, organisationsPagedResult.getTotalRows());
//                assertEquals(2, organisationsPagedResult.getValues().size());
//                assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);
//
//                int i = 0;
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
//                assertEquals(organisationsPagedResult.getValues().size(), i);
//            }
//        }
//    }
//
//    @Override
//    public void testRetrieveOrganisationSchemeByOrganisationUrn() throws Exception {
//        // Retrieve
//        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_1;
//        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(getServiceContextAdministrador(), urn);
//
//        // Validate
//        assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersion.getMaintainableArtefact().getUrn());
//    }
//
//    @Test
//    public void testRetrieveOrganisationSchemeByOrganisationUrnErrorNotExists() throws Exception {
//
//        String urn = NOT_EXISTS;
//
//        try {
//            organisationsService.retrieveOrganisationSchemeByOrganisationUrn(getServiceContextAdministrador(), urn);
//            fail("not exists");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.ORGANISATION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
//    private void assertListItemsContainsOrganisation(List<Item> items, String urn) {
//        for (Item item : items) {
//            if (item.getNameableArtefact().getUrn().equals(urn)) {
//                return;
//            }
//        }
//        fail("List does not contain item with urn " + urn);
//    }
//
//    private void assertListOrganisationsContainsOrganisation(List<OrganisationMetamac> items, String urn) {
//        for (Item item : items) {
//            if (item.getNameableArtefact().getUrn().equals(urn)) {
//                return;
//            }
//        }
//        fail("List does not contain item with urn " + urn);
//    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmOrganisationsTest.xml";
    }
}

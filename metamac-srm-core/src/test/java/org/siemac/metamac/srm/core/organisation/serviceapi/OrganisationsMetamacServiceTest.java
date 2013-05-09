package org.siemac.metamac.srm.core.organisation.serviceapi;

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
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.enume.domain.ContactItemTypeEnum;
import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class OrganisationsMetamacServiceTest extends SrmBaseTest implements OrganisationsMetamacServiceTestBase {

    @Autowired
    private OrganisationsMetamacService   organisationsService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

    @Override
    @Test
    public void testCreateOrganisationScheme() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = OrganisationsMetamacDoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, organisationMetamac);
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        OrganisationSchemeVersionMetamac organisationSchemeVersionCreated = organisationsService.createOrganisationScheme(ctx, organisationSchemeVersion);
        String urn = organisationSchemeVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", organisationSchemeVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), organisationSchemeVersionCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        OrganisationSchemeVersionMetamac organisationSchemeVersionRetrieved = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(organisationSchemeVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationDate());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationUser());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationDate());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationUser());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationDate());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(organisationSchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationUser());
        assertFalse(organisationSchemeVersion.getMaintainableArtefact().getFinalLogicClient());
        assertEquals(ctx.getUserId(), organisationSchemeVersionRetrieved.getCreatedBy());
        assertFalse(organisationSchemeVersionRetrieved.getMaintainableArtefact().getFinalLogic());
        assertNull(organisationSchemeVersionRetrieved.getMaintainableArtefact().getValidFrom());
        OrganisationsMetamacAsserts.assertEqualsOrganisationScheme(organisationSchemeVersion, organisationSchemeVersionRetrieved);
    }

    @Test
    public void testCreateOrganisationSchemeTypeAgency() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        OrganisationSchemeVersionMetamac organisationSchemeVersion = OrganisationsMetamacDoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.AGENCY_SCHEME, organisationMetamac);
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        OrganisationSchemeVersionMetamac organisationSchemeVersionCreated = organisationsService.createOrganisationScheme(ctx, organisationSchemeVersion);
        String urn = organisationSchemeVersionCreated.getMaintainableArtefact().getUrn();

        // Validate it is not final
        OrganisationSchemeVersionMetamac organisationSchemeVersionRetrieved = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
        assertFalse(organisationSchemeVersionRetrieved.getMaintainableArtefact().getFinalLogic());
        assertNull(organisationSchemeVersionRetrieved.getMaintainableArtefact().getValidFrom());
    }

    @Override
    @Test
    public void testUpdateOrganisationScheme() throws Exception {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1);
        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        organisationSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
        organisationSchemeVersion.getMaintainableArtefact().setName(OrganisationsMetamacDoMocks.mockInternationalString("name"));

        ServiceContext ctx = getServiceContextAdministrador();
        OrganisationSchemeVersion organisationSchemeVersionUpdated = organisationsService.updateOrganisationScheme(ctx, organisationSchemeVersion);
        assertNotNull(organisationSchemeVersionUpdated);
        assertEquals("user1", organisationSchemeVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), organisationSchemeVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateOrganisationSchemeErrorAgencySchemePublished() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String organisationSchemeUrn = ORGANISATION_SCHEME_100_V1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
        assertNull(organisationSchemeVersion.getMaintainableArtefact().getMaintainer());
        assertTrue(SdmxSrmUtils.isAgencySchemeSdmx(organisationSchemeVersion.getMaintainableArtefact().getUrn()));
        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        organisationSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
        organisationSchemeVersion.getMaintainableArtefact().setName(OrganisationsDoMocks.mockInternationalString());

        try {
            organisationsService.updateOrganisationScheme(ctx, organisationSchemeVersion);
            fail("published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(organisationSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateOrganisationSchemeErrorDataConsumerPublished() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_9_V1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, organisationSchemeVersion.getOrganisationSchemeType());
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        organisationSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
        organisationSchemeVersion.getMaintainableArtefact().setName(OrganisationsMetamacDoMocks.mockInternationalString("name"));

        try {
            organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
            fail("published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(organisationSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateOrganisationSchemeErrorPublished() throws Exception {
        String[] urns = {ORGANISATION_SCHEME_1_V1};
        for (String urn : urns) {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);

            try {
                organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
                organisationSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
                organisationSchemeVersion = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
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
    public void testUpdateOrganisationSchemeErrorExternalReference() throws Exception {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
        organisationSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);

        try {
            organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
            organisationSchemeVersion = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
            fail("organisation scheme cannot be a external reference");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateOrganisationSchemeErrorChangeCodeInOrganisationSchemeWithVersionAlreadyPublished() throws Exception {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
        organisationSchemeVersion.getMaintainableArtefact().setCode("newCode");
        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);

        try {
            organisationSchemeVersion = organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);
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
    public void testRetrieveOrganisationSchemeByUrn() throws Exception {

        // Retrieve
        String urn = ORGANISATION_SCHEME_1_V1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, organisationSchemeVersion.getMaintainableArtefact().getUrn());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Test
    @Override
    public void testFindOrganisationSchemesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).orderBy(OrganisationSchemeVersionMetamacProperties.itemScheme().id())
                    .orderBy(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(getServiceContextAdministrador(), conditions,
                    pagingParameter);

            // Validate
            assertEquals(14, organisationSchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_4_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_5_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_6_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_8_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_10_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_11_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(organisationSchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find internally published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
                    .withProperty(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                    .orderBy(OrganisationSchemeVersionMetamacProperties.itemScheme().id()).orderBy(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(getServiceContextAdministrador(), conditions,
                    pagingParameter);

            // Validate
            assertEquals(4, organisationSchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(organisationSchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find lasts versions
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class)
                    .withProperty(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE)
                    .orderBy(OrganisationSchemeVersionMetamacProperties.itemScheme().id()).orderBy(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<OrganisationSchemeVersionMetamac> organisationSchemeVersionPagedResult = organisationsService.findOrganisationSchemesByCondition(getServiceContextAdministrador(), conditions,
                    pagingParameter);

            // Validate
            assertEquals(12, organisationSchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_4_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_5_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_6_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_8_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_10_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_11_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_ROOT_1_V1, organisationSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testRetrieveOrganisationSchemeVersions() throws Exception {

        // Retrieve all versions
        String urn = ORGANISATION_SCHEME_1_V1;
        List<OrganisationSchemeVersionMetamac> organisationSchemeVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, organisationSchemeVersions.size());
        assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersions.get(1).getMaintainableArtefact().getUrn());
    }

    @Override
    @Test
    public void testSendOrganisationSchemeToProductionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.sendOrganisationSchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationInProcStatusRejected() throws Exception {

        String urn = ORGANISATION_SCHEME_4_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.sendOrganisationSchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
        // Validate retrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1;

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme wrong proc status");
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
    public void testSendOrganisationSchemeToProductionValidationErrorMetadataRequired() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;

        // Update to clear required metadata to send to production
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        organisationSchemeVersion.setIsPartial(null);
        organisationSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        organisationSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
        organisationsService.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersion);

        // Send to production validation
        try {
            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testSendOrganisationSchemeToDiffusionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Sends to diffusion validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.sendOrganisationSchemeToDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
    }

    @Test
    public void testSendOrganisationSchemeToDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            organisationsService.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendOrganisationSchemeToDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        try {
            organisationsService.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme wrong proc status");
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
    public void testRejectOrganisationSchemeProductionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Reject validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.rejectOrganisationSchemeProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate restrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);

            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectOrganisationSchemeProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            organisationsService.rejectOrganisationSchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectOrganisationSchemeProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1;

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            organisationsService.rejectOrganisationSchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme wrong proc status");
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
    public void testRejectOrganisationSchemeDiffusionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Reject validation
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.rejectOrganisationSchemeDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);

            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectOrganisationSchemeDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            organisationsService.rejectOrganisationSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectOrganisationSchemeDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1;

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            organisationsService.rejectOrganisationSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme wrong proc status");
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
    public void testPublishInternallyOrganisationScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, organisationSchemeVersion.getOrganisationSchemeType());
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }

        // Publish internally
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.publishInternallyOrganisationScheme(ctx, urn, Boolean.FALSE);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }
    }

    @Test
    public void testPublishInternallyAgencyScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_10_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(OrganisationSchemeTypeEnum.AGENCY_SCHEME, organisationSchemeVersion.getOrganisationSchemeType());
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }

        // Publish internally
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.publishInternallyOrganisationScheme(ctx, urn, Boolean.FALSE);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getFinalLogicClient());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }
    }

    @Test
    public void testPublishInternallyOrganisationSchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("OrganisationScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishInternallyOrganisationSchemeErrorWrongProcStatus() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1;

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("OrganisationScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    public void testCheckOrganisationSchemeVersionTranslations() throws Exception {
        // Tested in testPublishInternallyOrganisationSchemeCheckTranslations
    }

    @Test
    public void testPublishInternallyOrganisationSchemeCheckTranslations() throws Exception {
        String urn = ORGANISATION_SCHEME_11_V1;
        String code = "ORGANISATIONSCHEME11";

        try {
            // Note: publishInternallyOrganisationScheme calls to 'checkOrganisationSchemeVersionTranslates'
            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("OrganisationScheme wrong translations");
        } catch (MetamacException e) {
            assertEquals(10, e.getExceptionItems().size());
            int i = 0;
            // OrganisationScheme
            assertEqualsMetamacExceptionItem(ServiceExceptionType.MAINTAINABLE_ARTEFACT_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{
                    ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION, code}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.MAINTAINABLE_ARTEFACT_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{code}, e.getExceptionItems().get(i++));
            // Organisations
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME,
                    "ORGANISATION01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT,
                    "ORGANISATION01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION,
                    "ORGANISATION0101"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME,
                    "ORGANISATION02"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"ORGANISATION02"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"ORGANISATION03"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ORGANISATION_WITH_CONTACT_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"ORGANISATION01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ORGANISATION_WITH_CONTACT_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"ORGANISATION0101"}, e.getExceptionItems().get(i++));

            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Override
    @Test
    public void testPublishExternallyOrganisationScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, organisationSchemeVersion.getOrganisationSchemeType());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());

            OrganisationSchemeVersionMetamac organisationSchemeVersionExternallyPublished = organisationsService.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertNull(organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.publishExternallyOrganisationScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(organisationSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
        }
        // Validate previous published externally versions
        {
            OrganisationSchemeVersionMetamac organisationSchemeVersionExternallyPublished = organisationsService.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo().toDate()));
        }
    }

    @Test
    public void testPublishExternallyOrganisationSchemeDataProviderScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_9_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, organisationSchemeVersion.getOrganisationSchemeType());
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.publishExternallyOrganisationScheme(ctx, urn);

        // Validate response. validFrom is not filled
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), organisationSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(organisationSchemeVersion.getMaintainableArtefact().getLatestPublic()); // neve marked as latest. They only has one version
        }

    }
    @Test
    public void testPublishExternallyOrganisationSchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            organisationsService.publishExternallyOrganisationScheme(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishExternallyOrganisationSchemeErrorWrongProcStatus() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V2;

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            organisationsService.publishExternallyOrganisationScheme(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme wrong proc status");
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
    public void testDeleteOrganisationScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;

        // Delete organisation scheme only with version in draft
        organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteOrganisationSchemeWithVersionPublishedAndVersionDraft() throws Exception {

        String urnV1 = ORGANISATION_SCHEME_1_V1;
        String urnV2 = ORGANISATION_SCHEME_1_V2;

        OrganisationSchemeVersionMetamac organisationSchemeVersionV1 = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersionV1.getLifeCycleMetadata().getProcStatus());
        OrganisationSchemeVersionMetamac organisationSchemeVersionV2 = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV2);
        assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionV2.getLifeCycleMetadata().getProcStatus());

        organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV2);
            fail("OrganisationScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        organisationSchemeVersionV1 = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(organisationSchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(organisationSchemeVersionV1.getMaintainableArtefact().getReplacedByVersion());
    }

    @Test
    public void testDeleteOrganisationSchemeErrorPublished() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1;

        // Validation
        try {
            organisationsService.deleteOrganisationScheme(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme can not be deleted");
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
    public void testCopyOrganisationScheme() throws Exception {

        String urnToCopy = ORGANISATION_SCHEME_11_V1;
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        String versionExpected = "01.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME11(01.000)";
        String urnExpectedOrganisation1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME11(01.000).ORGANISATION01";
        String urnExpectedOrganisation11 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME11(01.000).ORGANISATION0101";
        String urnExpectedOrganisation2 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME11(01.000).ORGANISATION02";
        String urnExpectedOrganisation3 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME11(01.000).ORGANISATION03";

        TaskInfo copyResult = organisationsService.copyOrganisationScheme(getServiceContextAdministrador(), urnToCopy);

        // Validate (only some metadata, already tested in statistic module)
        entityManager.clear();
        OrganisationSchemeVersionMetamac organisationSchemeVersionNewArtefact = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), copyResult.getUrnResult());
        assertEquals(maintainerUrnExpected, organisationSchemeVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, organisationSchemeVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, organisationSchemeVersionNewArtefact.getMaintainableArtefact().getUrn());
        assertEquals(null, organisationSchemeVersionNewArtefact.getMaintainableArtefact().getReplaceToVersion());
        assertEquals(null, organisationSchemeVersionNewArtefact.getMaintainableArtefact().getReplacedByVersion());
        assertTrue(organisationSchemeVersionNewArtefact.getMaintainableArtefact().getIsLastVersion());

        // Organisations
        assertEquals(4, organisationSchemeVersionNewArtefact.getItems().size());
        assertListItemsContainsItem(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation1);
        assertListItemsContainsItem(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation11);
        assertListItemsContainsItem(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation2);
        assertListItemsContainsItem(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation3);

        assertEquals(3, organisationSchemeVersionNewArtefact.getItemsFirstLevel().size());
        {
            OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedOrganisation1);
            OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "en", "name org1", "it", "nombre it org1");
            OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getNameableArtefact().getDescription(), "es", "descripción org1", "it", "descripción it org1");
            assertEquals(null, organisation.getNameableArtefact().getComment());

            assertEquals(1, organisation.getChildren().size());
            {
                OrganisationMetamac organisationChild = assertListOrganisationsContainsOrganisation(organisation.getChildren(), urnExpectedOrganisation11);
                assertEquals(0, organisationChild.getChildren().size());
            }

        }
        {
            OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedOrganisation2);
            OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "en", "name org2", null, null);

            assertEquals(0, organisation.getChildren().size());
        }
        {
            OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedOrganisation3);
            OrganisationsMetamacAsserts.assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "es", "nombre org3", null, null);

            assertEquals(0, organisation.getChildren().size());
        }
    }

    @Override
    @Test
    public void testVersioningOrganisationScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(02.000)";
        String urnExpectedOrganisation1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(02.000).ORGANISATION01";
        String urnExpectedOrganisation2 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(02.000).ORGANISATION02";
        String urnExpectedOrganisation21 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(02.000).ORGANISATION0201";
        String urnExpectedOrganisation211 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(02.000).ORGANISATION020101";
        String urnExpectedOrganisation22 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(02.000).ORGANISATION0202";

        TaskInfo versioningResult = organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        OrganisationSchemeVersionMetamac organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, organisationSchemeVersionNewVersion.getOrganisationSchemeType());
            OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeVersionNewVersion.getMaintainableArtefact()
                    .getUrn());
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(organisationSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);

            // Organisations
            assertEquals(5, organisationSchemeVersionNewVersion.getItems().size());
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation1);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation2);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation21);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation211);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation22);

            assertEquals(2, organisationSchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedOrganisation1);
                assertEquals(0, organisation.getChildren().size());
                // Contacts
                assertEquals(2, organisation.getContacts().size());
                int i = 0;
                {
                    Contact contact = organisation.getContacts().get(i++);
                    BaseAsserts.assertEqualsInternationalString(contact.getName(), "es", "nombre contacto 311", "en", "contact name 311");
                    BaseAsserts.assertEqualsInternationalString(contact.getOrganisationUnit(), "es", "unidad organizativa 311", "en", "organisation unit 311");
                    BaseAsserts.assertEqualsInternationalString(contact.getResponsibility(), "es", "responsabilidad 311", "en", "responsibility 311");

                    assertEquals(4, contact.getContactItems().size());
                    assertEquals(ContactItemTypeEnum.TELEPHONE, contact.getContactItems().get(0).getItemType());
                    assertEquals("+922333333", contact.getContactItems().get(0).getItemValue());
                    assertEquals(ContactItemTypeEnum.EMAIL, contact.getContactItems().get(1).getItemType());
                    assertEquals("contact3112@email.com", contact.getContactItems().get(1).getItemValue());
                    assertEquals(ContactItemTypeEnum.FAX, contact.getContactItems().get(2).getItemType());
                    assertEquals("111111111", contact.getContactItems().get(2).getItemValue());
                    assertEquals(ContactItemTypeEnum.URL, contact.getContactItems().get(3).getItemType());
                    assertEquals("http://www.contact3111.com", contact.getContactItems().get(3).getItemValue());

                }
                {
                    Contact contact = organisation.getContacts().get(i++);
                    assertNull(contact.getName());
                    assertNull(contact.getOrganisationUnit());
                    assertNull(contact.getResponsibility());
                    assertEquals(1, contact.getContactItems().size());
                    assertEquals(ContactItemTypeEnum.TELEPHONE, contact.getContactItems().get(0).getItemType());
                    assertEquals("11333333", contact.getContactItems().get(0).getItemValue());
                }
                assertEquals(organisation.getContacts().size(), i);
            }
            {
                OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedOrganisation2);
                assertEquals(0, organisation.getContacts().size());
                assertEquals(2, organisation.getChildren().size());
                {
                    OrganisationMetamac organisationChild = assertListOrganisationsContainsOrganisation(organisation.getChildren(), urnExpectedOrganisation21);
                    assertEquals(0, organisationChild.getContacts().size());
                    assertEquals(1, organisationChild.getChildren().size());
                    {
                        OrganisationMetamac organisationChildChild = assertListOrganisationsContainsOrganisation(organisationChild.getChildren(), urnExpectedOrganisation211);
                        assertEquals(0, organisationChildChild.getContacts().size());
                        assertEquals(0, organisationChildChild.getChildren().size());
                    }
                }
                {
                    OrganisationMetamac organisationChild = assertListOrganisationsContainsOrganisation(organisation.getChildren(), urnExpectedOrganisation22);
                    assertEquals(0, organisationChild.getContacts().size());
                    assertEquals(0, organisationChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, organisationSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, organisationSchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, organisationSchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<OrganisationSchemeVersionMetamac> allVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningOrganisationSchemeWithTwoVersionsPublished() throws Exception {

        // This test checks the copy from one version but replacing to another one that is last version.

        String urnToCopy = ORGANISATION_SCHEME_7_V1;
        String urnLastVersion = ORGANISATION_SCHEME_7_V2;
        String versionExpected = "03.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME07(03.000)";

        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeVersionToCopy.getLifeCycleMetadata().getProcStatus());
        assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        OrganisationSchemeVersionMetamac organisationSchemeVersionLast = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersionLast.getLifeCycleMetadata().getProcStatus());
        assertTrue(organisationSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

        TaskInfo versioningResult = organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnToCopy);;
        OrganisationSchemeVersionMetamac organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeVersionNewVersion.getMaintainableArtefact()
                    .getUrn());
            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals("02.000", organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(organisationSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);

            // Version copied
            organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, organisationSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, organisationSchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals("02.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            organisationSchemeVersionLast = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", organisationSchemeVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, organisationSchemeVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", organisationSchemeVersionLast.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, organisationSchemeVersionLast.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(organisationSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

            // All versions
            List<OrganisationSchemeVersionMetamac> allVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), organisationSchemeVersionNewVersion
                    .getMaintainableArtefact().getUrn());
            assertEquals(3, allVersions.size());
            assertEquals(urnToCopy, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnLastVersion, allVersions.get(1).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(2).getMaintainableArtefact().getUrn());
        }
    }
    @Test
    public void testVersioningOrganisationSchemeErrorAlreadyExistsDraft() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1;

        try {
            organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("OrganisationScheme already exists in no final");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ORGANISATION_SCHEME_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningOrganisationSchemeErrorNotPublished() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;

        try {
            organisationsService.versioningOrganisationScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("OrganisationScheme not published");
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
    public void testCreateTemporalOrganisationScheme() throws Exception {
        String urn = ORGANISATION_SCHEME_3_V1;
        String versionExpected = "01.000" + UrnConstants.URN_SDMX_TEMPORAL_SUFFIX;
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ")";
        String urnExpectedOrganisation1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ").ORGANISATION01";
        String urnExpectedOrganisation2 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ").ORGANISATION02";
        String urnExpectedOrganisation21 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ").ORGANISATION0201";
        String urnExpectedOrganisation211 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ").ORGANISATION020101";
        String urnExpectedOrganisation22 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ").ORGANISATION0202";

        OrganisationSchemeVersionMetamac organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), urn);

        // Validate response
        entityManager.clear();
        OrganisationSchemeVersionMetamac organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());
        organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);

        {
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            organisationSchemeVersionNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeVersionNewVersion.getMaintainableArtefact()
                    .getUrn());
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, organisationSchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(organisationSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(organisationSchemeVersionToCopy, organisationSchemeVersionNewVersion);

            // Organisations
            assertEquals(5, organisationSchemeVersionNewVersion.getItems().size());
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation1);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation2);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation21);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation211);
            assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItems(), urnExpectedOrganisation22);

            assertEquals(2, organisationSchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedOrganisation1);
                assertEquals(0, organisation.getChildren().size());
                // Contacts
                assertEquals(2, organisation.getContacts().size());
                int i = 0;
                {
                    Contact contact = organisation.getContacts().get(i++);
                    BaseAsserts.assertEqualsInternationalString(contact.getName(), "es", "nombre contacto 311", "en", "contact name 311");
                    BaseAsserts.assertEqualsInternationalString(contact.getOrganisationUnit(), "es", "unidad organizativa 311", "en", "organisation unit 311");
                    BaseAsserts.assertEqualsInternationalString(contact.getResponsibility(), "es", "responsabilidad 311", "en", "responsibility 311");

                    assertEquals(4, contact.getContactItems().size());
                    assertEquals(ContactItemTypeEnum.TELEPHONE, contact.getContactItems().get(0).getItemType());
                    assertEquals("+922333333", contact.getContactItems().get(0).getItemValue());
                    assertEquals(ContactItemTypeEnum.EMAIL, contact.getContactItems().get(1).getItemType());
                    assertEquals("contact3112@email.com", contact.getContactItems().get(1).getItemValue());
                    assertEquals(ContactItemTypeEnum.FAX, contact.getContactItems().get(2).getItemType());
                    assertEquals("111111111", contact.getContactItems().get(2).getItemValue());
                    assertEquals(ContactItemTypeEnum.URL, contact.getContactItems().get(3).getItemType());
                    assertEquals("http://www.contact3111.com", contact.getContactItems().get(3).getItemValue());

                }
                {
                    Contact contact = organisation.getContacts().get(i++);
                    assertNull(contact.getName());
                    assertNull(contact.getOrganisationUnit());
                    assertNull(contact.getResponsibility());
                    assertEquals(1, contact.getContactItems().size());
                    assertEquals(ContactItemTypeEnum.TELEPHONE, contact.getContactItems().get(0).getItemType());
                    assertEquals("11333333", contact.getContactItems().get(0).getItemValue());
                }
                assertEquals(organisation.getContacts().size(), i);
            }
            {
                OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedOrganisation2);
                assertEquals(0, organisation.getContacts().size());
                assertEquals(2, organisation.getChildren().size());
                {
                    OrganisationMetamac organisationChild = assertListOrganisationsContainsOrganisation(organisation.getChildren(), urnExpectedOrganisation21);
                    assertEquals(0, organisationChild.getContacts().size());
                    assertEquals(1, organisationChild.getChildren().size());
                    {
                        OrganisationMetamac organisationChildChild = assertListOrganisationsContainsOrganisation(organisationChild.getChildren(), urnExpectedOrganisation211);
                        assertEquals(0, organisationChildChild.getContacts().size());
                        assertEquals(0, organisationChildChild.getChildren().size());
                    }
                }
                {
                    OrganisationMetamac organisationChild = assertListOrganisationsContainsOrganisation(organisation.getChildren(), urnExpectedOrganisation22);
                    assertEquals(0, organisationChild.getContacts().size());
                    assertEquals(0, organisationChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            organisationSchemeVersionToCopy = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", organisationSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, organisationSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertFalse(organisationSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<OrganisationSchemeVersionMetamac> allVersions = organisationsService.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testCreateVersionFromTemporalOrganisationScheme() throws Exception {
        String urn = ORGANISATION_SCHEME_3_V1;

        TaskInfo versioningResult = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), urn);
        entityManager.clear();
        OrganisationSchemeVersionMetamac organisationSchemeVersionTemporal = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        // Create no temporal version
        TaskInfo versioningResult2 = organisationsService.createVersionFromTemporalOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersionTemporal.getMaintainableArtefact()
                .getUrn(), VersionTypeEnum.MAJOR);

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ")";

        // Validate response
        entityManager.clear();
        OrganisationSchemeVersionMetamac organisationSchemeNewVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult2.getUrnResult());
        {
            assertEquals(ProcStatusEnum.DRAFT, organisationSchemeNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, organisationSchemeNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, organisationSchemeNewVersion.getMaintainableArtefact().getUrn());

            assertEquals(null, organisationSchemeNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(organisationSchemeNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(organisationSchemeNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(organisationSchemeNewVersion.getMaintainableArtefact().getLatestPublic());
        }
    }

    @Override
    @Test
    public void testMergeTemporalVersion() throws Exception {
        {
            String urn = ORGANISATION_SCHEME_3_V1;
            TaskInfo versioningResult = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), urn);

            entityManager.clear();
            OrganisationSchemeVersionMetamac organisationSchemeVersionTemporal = organisationsService
                    .retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

            // Change temporal version *********************

            // Item scheme: Change Name
            {
                LocalisedString localisedString = new LocalisedString("fr", "its - text sample");
                organisationSchemeVersionTemporal.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Item: Change Name
            {
                OrganisationMetamac organisationTemporal = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(),
                        GeneratorUrnUtils.makeUrnAsTemporal(ORGANISATION_SCHEME_3_V1_ORGANISATION_1));

                LocalisedString localisedString = new LocalisedString("fr", "it - text sample");
                InternationalString internationalString = new InternationalString();
                internationalString.addText(localisedString);
                organisationTemporal.getNameableArtefact().setName(internationalString);
            }

            // Merge
            organisationSchemeVersionTemporal = organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), organisationSchemeVersionTemporal
                    .getMaintainableArtefact().getUrn());
            organisationSchemeVersionTemporal = organisationsService.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), organisationSchemeVersionTemporal
                    .getMaintainableArtefact().getUrn());
            OrganisationSchemeVersionMetamac organisationSchemeVersionMetamac = organisationsService.mergeTemporalVersion(getServiceContextAdministrador(), organisationSchemeVersionTemporal);

            // Assert **************************************

            // Item Scheme
            assertEquals(3, organisationSchemeVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("its - text sample", organisationSchemeVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));

            // Item
            {
                OrganisationMetamac organisationTemporal = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_3_V1_ORGANISATION_1);
                assertEquals(1, organisationTemporal.getNameableArtefact().getName().getTexts().size());
                assertEquals("it - text sample", organisationTemporal.getNameableArtefact().getName().getLocalisedLabel("fr"));
            }
        }
    }

    @Override
    @Test
    public void testEndOrganisationSchemeValidity() throws Exception {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.endOrganisationSchemeValidity(getServiceContextAdministrador(), ORGANISATION_SCHEME_7_V1);

        assertNotNull(organisationSchemeVersion);
        assertNotNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testEndOrganisationSchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {ORGANISATION_SCHEME_1_V1, ORGANISATION_SCHEME_4_V1, ORGANISATION_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                organisationsService.endOrganisationSchemeValidity(getServiceContextAdministrador(), urn);
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
    public void testCreateOrganisation() throws Exception {

        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.ORGANISATION_UNIT);
        organisation.setParent(null);
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        // Create
        OrganisationMetamac organisationSchemeVersionCreated = organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
        String urn = organisationSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        OrganisationMetamac organisationRetrieved = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationRetrieved);

        // Validate new structure
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(5, organisationSchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, organisationSchemeVersion.getItems().size());
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), organisationRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateOrganisationSuborganisation() throws Exception {

        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.ORGANISATION_UNIT);
        OrganisationMetamac organisationParent = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        organisation.setParent(organisationParent);
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        // Create
        OrganisationMetamac organisationSchemeVersionCreated = organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
        String urn = organisationSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        OrganisationMetamac organisationRetrieved = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationRetrieved);

        // Validate new structure
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, organisationSchemeVersion.getItems().size());

        Item organisation1 = assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListItemsContainsItem(organisation1.getChildren(), organisationRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateOrganisationInAgencySchemeWithoutMaintainer() throws Exception {

        // Force DIFFUSION_VALIDATION procStatus to can test organisations creation
        String organisationSchemeUrn = ORGANISATION_SCHEME_100_V1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        organisationSchemeVersion.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        itemSchemeRepository.save(organisationSchemeVersion);

        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.AGENCY);

        // Create
        OrganisationMetamac organisationCreated = organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
        String urn = organisationCreated.getNameableArtefact().getUrn();
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0)." + organisationCreated.getNameableArtefact().getCode(), urn);
    }

    @Test
    public void testCreateOrganisationErrorDataProviderSchemePublished() throws Exception {

        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.DATA_PROVIDER);
        organisation.setParent(null);
        String organisationSchemeUrn = ORGANISATION_SCHEME_9_V1;

        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, organisationSchemeVersion.getOrganisationSchemeType());
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeVersion.getLifeCycleMetadata().getProcStatus());

        try {
            organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
            fail("published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(organisationSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testCreateOrganisationErrorOrganisationSchemeImported() throws Exception {
        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.ORGANISATION_UNIT);
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        // save to force incorrect metadata
        organisationSchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        organisationSchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(organisationSchemeVersion);

        try {
            organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeUrn, organisation);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    @Test
    public void testUpdateOrganisation() throws Exception {

        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        organisation.getNameableArtefact().setName(OrganisationsDoMocks.mockInternationalString());

        // Update
        OrganisationMetamac organisationUpdated = organisationsService.updateOrganisation(getServiceContextAdministrador(), organisation);

        // Validate
        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationUpdated);
    }

    @Test
    public void testUpdateOrganisationErrorAgencySchemePublished() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_100_V1_ORGANISATION_2);
        assertNull(organisation.getItemSchemeVersion().getMaintainableArtefact().getMaintainer());
        assertTrue(SdmxSrmUtils.isAgencySchemeSdmx(organisation.getItemSchemeVersion().getMaintainableArtefact().getUrn()));
        organisation.getNameableArtefact().setName(OrganisationsDoMocks.mockInternationalString());
        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Update
        try {
            organisationsService.updateOrganisation(ctx, organisation);
            fail("published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ORGANISATION_SCHEME_100_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateOrganisationCodeErrorAgency() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String urn = ORGANISATION_SCHEME_10_V1_ORGANISATION_1;
        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(ctx, urn);
        organisation.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(1));
        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        // Validation
        try {
            organisationsService.updateOrganisation(getServiceContextAdministrador(), organisation);
            fail("Organisation code can not be changed - agency");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.ORGANISATION_TYPE_AGENCY_UPDATE_CODE_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateOrganisationInAgencySchemeWithoutMaintainer() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        // Force DIFFUSION_VALIDATION procStatus to can test organisations update
        String organisationSchemeUrn = ORGANISATION_SCHEME_100_V1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(ctx, organisationSchemeUrn);
        organisationSchemeVersion.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        itemSchemeRepository.save(organisationSchemeVersion);

        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_100_V1_ORGANISATION_2);
        assertNull(organisation.getItemSchemeVersion().getMaintainableArtefact().getMaintainer());
        assertTrue(SdmxSrmUtils.isAgencySchemeSdmx(organisation.getItemSchemeVersion().getMaintainableArtefact().getUrn()));
        organisation.getNameableArtefact().setName(OrganisationsDoMocks.mockInternationalString());
        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Update
        OrganisationMetamac organisationUpdated = organisationsService.updateOrganisation(ctx, organisation);

        // Validate
        assertNull(organisationUpdated.getItemSchemeVersion().getMaintainableArtefact().getMaintainer());
        OrganisationsMetamacAsserts.assertEqualsOrganisation(organisation, organisationUpdated);
    }

    @Override
    @Test
    public void testRetrieveOrganisationByUrn() throws Exception {
        // Retrieve
        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_1;
        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
        assertEquals(urn, organisation.getNameableArtefact().getUrn());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        // no metadata in Metamac
    }

    @Test
    public void testRetrieveOrganisationByUrnWithParentAndChildren() throws Exception {

        // Retrieve
        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1;
        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);

        // Validate
        // Parent
        assertTrue(organisation.getParent() instanceof OrganisationMetamac);
        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getParent().getNameableArtefact().getUrn());
        // Children
        assertEquals(1, organisation.getChildren().size());
        assertTrue(organisation.getChildren().get(0) instanceof OrganisationMetamac);
        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisation.getChildren().get(0).getNameableArtefact().getUrn());
    }

    @Override
    @Test
    public void testDeleteOrganisation() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_3;
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, organisationSchemeVersion.getItems().size());

        // Delete organisation
        organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);

        // Validation
        try {
            organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
            fail("Organisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Check hierarchy
        organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(3, organisationSchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertEquals(7, organisationSchemeVersion.getItems().size());
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
    }

    @Test
    public void testDeleteOrganisationWithParentAndChildren() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1;
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, organisationSchemeVersion.getItems().size());

        // Delete organisation
        organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);

        // Validation
        try {
            organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
            fail("Organisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEquals(4, organisationSchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
        assertListItemsContainsItem(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertEquals(6, organisationSchemeVersion.getItems().size());
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
        assertListItemsContainsItem(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
    }

    @Test
    public void testDeleteOrganisationErrorOrganisationSchemePublished() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V1_ORGANISATION_1;
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V1;

        // Validation
        try {
            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
            fail("Organisation can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(organisationSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteOrganisationErrorOrganisationSchemeImported() throws Exception {
        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_3;
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        // save to force incorrect metadata
        organisationSchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        organisationSchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(organisationSchemeVersion);

        try {
            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteOrganisationErrorAgency() throws Exception {

        String urn = ORGANISATION_SCHEME_10_V1_ORGANISATION_1;

        // Validation
        try {
            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
            fail("Organisation can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.ORGANISATION_TYPE_AGENCY_DELETING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveOrganisationsByOrganisationSchemeUrn() throws Exception {

        // Retrieve
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        // LOCALE = 'es'
        {
            String locale = "es";
            List<OrganisationMetamacVisualisationResult> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrn(getServiceContextAdministrador(), organisationSchemeUrn,
                    locale);

            // Validate
            assertEquals(8, organisations.size());
            {
                // Organisation 01 (validate all metadata)
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisation.getUrn());
                assertEquals("ORGANISATION01", organisation.getCode());
                assertEquals(OrganisationTypeEnum.ORGANISATION_UNIT, organisation.getType());
                assertEquals("Nombre organisationScheme-1-v2-organisation-1", organisation.getName());
                assertEquals(Long.valueOf(121), organisation.getItemIdDatabase());
                assertEquals(null, organisation.getParent());
                assertEquals(null, organisation.getParentIdDatabase());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", organisation.getCreatedDate());
            }
            {
                // Organisation 02
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getUrn());
                assertEquals("ORGANISATION02", organisation.getCode());
                assertEquals("Nombre organisationScheme-1-v2-organisation-2", organisation.getName());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", organisation.getCreatedDate());
            }
            {
                // Organisation 02 01 (validate parent)
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisation.getUrn());
                assertEquals("ORGANISATION02", organisation.getParent().getCode());
                assertEquals("Nombre organisationScheme-1-v2-organisation-2-1", organisation.getName());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getParent().getUrn());
                assertEquals(Long.valueOf("122"), organisation.getParentIdDatabase());
            }
            {
                // Organisation 02 01 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisation.getUrn());
                assertEquals("ORGANISATION0201", organisation.getParent().getCode());
                assertEquals("Nombre organisationScheme-1-v2-organisation-2-1-1", organisation.getName());
                assertEquals(Long.valueOf("1221"), organisation.getParentIdDatabase());
            }
            {
                // Organisation 03
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisation.getUrn());
                assertEquals("nombre organisation-3", organisation.getName());
            }
            {
                // Organisation 04
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisation.getUrn());
                assertEquals("nombre organisation-4", organisation.getName());
            }
            {
                // Organisation 04 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisation.getUrn());
                assertEquals("nombre organisation 4-1", organisation.getName());
            }
            {
                // Organisation 04 01 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisation.getUrn());
                assertEquals("ORGANISATION0401", organisation.getParent().getCode());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisation.getParent().getUrn());
                assertEquals("Nombre organisationScheme-1-v2-organisation-4-1-1", organisation.getName());
            }
        }

        // LOCALE = 'en'
        {
            String locale = "en";
            List<OrganisationMetamacVisualisationResult> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrn(getServiceContextAdministrador(), organisationSchemeUrn,
                    locale);

            // Validate
            assertEquals(8, organisations.size());
            {
                // Organisation 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
                assertEquals("Name organisationScheme-1-v2-organisation-1", organisation.getName());
            }
            {
                // Organisation 02
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
                assertEquals(null, organisation.getName());
            }
            {
                // Organisation 02 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
                assertEquals("Name organisationScheme-1-v2-organisation-2-1", organisation.getName());
            }
            {
                // Organisation 02 01 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
                assertEquals(null, organisation.getName());
            }
            {
                // Organisation 03
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
                assertEquals("name organisation-3", organisation.getName());
            }
            {
                // Organisation 04
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
                assertEquals(null, organisation.getName());
            }
            {
                // Organisation 04 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1);
                assertEquals(null, organisation.getName());
            }
            {
                // Organisation 04 01 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
                assertEquals("Name organisationScheme-1-v2-organisation-4-1-1", organisation.getName());
            }
        }
    }

    @Override
    @Test
    public void testFindOrganisationsByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Organisation.class).orderBy(OrganisationProperties.itemSchemeVersion().itemScheme().id())
                    .orderBy(OrganisationProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending().orderBy(OrganisationProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(30, organisationsPagedResult.getTotalRows());
            assertEquals(30, organisationsPagedResult.getValues().size());
            assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_4_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_5_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_6_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_8_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_10_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_11_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_11_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_11_V1_ORGANISATION_3, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_11_V1_ORGANISATION_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());

            assertEquals(organisationsPagedResult.getValues().size(), i);
        }

        // Find by name (like), code (like) and organisation scheme urn
        {
            String name = "Nombre organisationScheme-1-v2-organisation-2-";
            String code = "ORGANISATION02";
            String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Organisation.class).withProperty(OrganisationProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(organisationSchemeUrn).withProperty(OrganisationProperties.nameableArtefact().code()).like(code + "%")
                    .withProperty(OrganisationProperties.nameableArtefact().name().texts().label()).like(name + "%").orderBy(OrganisationProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, organisationsPagedResult.getTotalRows());
            assertEquals(2, organisationsPagedResult.getValues().size());
            assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(organisationsPagedResult.getValues().size(), i);
        }

        // Find by organisation scheme urn paginated
        {
            String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Organisation.class).withProperty(OrganisationProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(organisationSchemeUrn).orderBy(OrganisationProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
                PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, organisationsPagedResult.getTotalRows());
                assertEquals(3, organisationsPagedResult.getValues().size());
                assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(organisationsPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
                PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, organisationsPagedResult.getTotalRows());
                assertEquals(3, organisationsPagedResult.getValues().size());
                assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(organisationsPagedResult.getValues().size(), i);
            }
            // Third page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
                PagedResult<OrganisationMetamac> organisationsPagedResult = organisationsService.findOrganisationsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, organisationsPagedResult.getTotalRows());
                assertEquals(2, organisationsPagedResult.getValues().size());
                assertTrue(organisationsPagedResult.getValues().get(0) instanceof OrganisationMetamac);

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(organisationsPagedResult.getValues().size(), i);
            }
        }
    }

    @Override
    @Test
    public void testFindOrganisationContactsByCondition() throws Exception {
        // In SDMX Module
    }

    @Override
    @Test
    public void testRetrieveMaintainerDefault() throws Exception {
        OrganisationMetamac organisation = organisationsService.retrieveMaintainerDefault(getServiceContextAdministrador());
        assertEquals(AGENCY_ROOT_1_V1, organisation.getNameableArtefact().getUrn());
    }

    @Override
    @Test
    public void testRetrieveOrganisationSchemeByOrganisationUrn() throws Exception {
        // Retrieve
        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_1;
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeVersion.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testRetrieveOrganisationSchemeByOrganisationUrnErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;

        try {
            organisationsService.retrieveOrganisationSchemeByOrganisationUrn(getServiceContextAdministrador(), urn);
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
    public void testPreCreateOrganisationScheme() throws Exception {
        // TODO testPreCreateOrganisationScheme

    }

    @Test
    @Override
    public void testPreCreateOrganisation() throws Exception {
        // TODO testPreCreateOrganisation

    }

    @SuppressWarnings("rawtypes")
    private OrganisationMetamac assertListOrganisationsContainsOrganisation(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            OrganisationMetamac concept = (OrganisationMetamac) iterator.next();
            if (concept.getNameableArtefact().getUrn().equals(urn)) {
                return concept;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    protected OrganisationMetamacVisualisationResult getOrganisationVisualisationResult(List<OrganisationMetamacVisualisationResult> actuals, String codeUrn) {
        for (OrganisationMetamacVisualisationResult actual : actuals) {
            if (actual.getUrn().equals(codeUrn)) {
                return actual;
            }
        }
        fail("not found");
        return null;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmOrganisationsTest.xml";
    }

}

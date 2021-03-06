package org.siemac.metamac.srm.core.organisation.serviceapi;

import static com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.assertEqualsInternationalString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.code.domain.OrganisationMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
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

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationResultExtensionPoint;
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
    private CategoriesMetamacService      categoriesService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeRepository;

    @Autowired
    private ItemRepository                itemRepository;

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

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL}, exceptionItem.getExceptionItems().get(0));
        }
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationErrorTranslations() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;

        // Update to change metadata to send to production

        {
            OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            organisationSchemeVersion.getMaintainableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemSchemeRepository.save(organisationSchemeVersion);
        }
        {
            OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1_ORGANISATION_1);
            organisation.getNameableArtefact().setDescription(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(organisation);
        }
        {
            OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_2_V1_ORGANISATION_2);
            organisation.getNameableArtefact().setComment(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(organisation);
        }

        entityManager.flush();

        // Send to production validation
        try {
            organisationsService.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            int i = 0;
            // OrganisationScheme
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, exceptionItem
                        .getExceptionItems().get(0));
            }
            // Organisations
            {
                // Organisation01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, ORGANISATION_SCHEME_2_V1_ORGANISATION_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
            }
            {
                // Organisation02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, ORGANISATION_SCHEME_2_V1_ORGANISATION_2);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT}, exceptionItem
                        .getExceptionItems().get(0));
            }
            assertEquals(e.getExceptionItems().size(), i);
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
        {
            OrganisationMetamac organisation1 = organisationsService.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_6_V1_ORGANISATION_1);
            assertFalse(organisation1.getSpecialOrganisationHasBeenPublished());
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
        entityManager.clear();

        // Validate organisations (it has been published FALSE)
        {
            OrganisationMetamac organisation1 = organisationsService.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_6_V1_ORGANISATION_1);
            assertFalse(organisation1.getSpecialOrganisationHasBeenPublished());
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
            assertTrue(organisationSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }

        // Validate organisations (it has been published TRUE)
        {
            OrganisationMetamac organisation1 = organisationsService.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_10_V1_ORGANISATION_1);
            assertTrue(organisation1.getSpecialOrganisationHasBeenPublished());
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

        try {
            // Note: publishInternallyOrganisationScheme calls to 'checkOrganisationSchemeVersionTranslates'
            organisationsService.publishInternallyOrganisationScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("OrganisationScheme wrong translations");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());
            int i = 0;
            // OrganisationScheme
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            // Organisations
            {
                // Organisation01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, ORGANISATION_SCHEME_11_V1_ORGANISATION_1);
                // children
                assertEquals(3, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT}, exceptionItem
                        .getExceptionItems().get(1));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ORGANISATION_CONTACT}, exceptionItem
                        .getExceptionItems().get(2));
            }
            {
                // Organisation0101
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, ORGANISATION_SCHEME_11_V1_ORGANISATION_1_1);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ORGANISATION_CONTACT}, exceptionItem
                        .getExceptionItems().get(1));
            }
            {
                // Organisation02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, ORGANISATION_SCHEME_11_V1_ORGANISATION_2);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            {
                // Organisation03
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, ORGANISATION_SCHEME_11_V1_ORGANISATION_3);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
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

    @Test
    public void testPublishExternallyOrganisationSchemeErrorRelatedResourcesCategorisationsNotExternallyPublished() throws Exception {

        String urn = ORGANISATION_SCHEME_3_V1;

        // Save category scheme to force error
        CategorySchemeVersion categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V1);
        categorySchemeVersion.getMaintainableArtefact().setPublicLogic(false);
        itemSchemeRepository.save(categorySchemeVersion);

        try {
            organisationsService.publishExternallyOrganisationScheme(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_2);
        }
    }

    @Override
    public void testCheckOrganisationSchemeWithRelatedResourcesExternallyPublished() throws Exception {
        // tested in testPublishExternallyOrganisationSchemeErrorRelatedResourcesNotExternallyPublished
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

        TaskInfo copyResult = organisationsService.copyOrganisationScheme(getServiceContextAdministrador(), urnToCopy, null);

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
        assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation11);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation2);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItems(), urnExpectedOrganisation3);

        assertEquals(3, organisationSchemeVersionNewArtefact.getItemsFirstLevel().size());
        {
            OrganisationMetamac organisation = assertListOrganisationsContainsOrganisation(organisationSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedOrganisation1);
            assertEquals(null, organisation.getSpecialOrganisationHasBeenPublished());
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
            assertEquals(null, organisation.getSpecialOrganisationHasBeenPublished());
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
                assertEquals(null, organisation.getSpecialOrganisationHasBeenPublished());
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
                assertEquals(null, organisation.getSpecialOrganisationHasBeenPublished());
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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertTrue(organisationSchemeVersionNewVersion.getMaintainableArtefact().getIsTemporal());
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

        // Try delete organisation (unsupported)
        try {
            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urnExpectedOrganisation1);
            fail("temporal unsupported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_CAN_NOT_BE_TEMPORAL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnExpected, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testCreateVersionFromTemporalOrganisationScheme() throws Exception {
        String urn = ORGANISATION_SCHEME_3_V1;

        TaskInfo versioningResult = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), urn);
        entityManager.clear();
        OrganisationSchemeVersionMetamac organisationSchemeVersionTemporal = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());
        assertEquals(3, organisationSchemeVersionTemporal.getMaintainableArtefact().getCategorisations().size());
        {
            Categorisation categorisation = assertListContainsCategorisation(organisationSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000_temporal)");
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
        }
        assertListContainsCategorisation(organisationSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat2(01.000_temporal)");
        assertListContainsCategorisation(organisationSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat3(01.000_temporal)");

        // Organisations
        assertEquals(5, organisationSchemeVersionTemporal.getItems().size());
        // Check any organisation
        {
            String organisationUrn = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(01.000_temporal).ORGANISATION01";
            OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), organisationUrn);
            assertFalse(organisation.getSpecialOrganisationHasBeenPublished());
        }

        // Create no temporal version
        TaskInfo versioningResult2 = organisationsService.createVersionFromTemporalOrganisationScheme(getServiceContextAdministrador(), organisationSchemeVersionTemporal.getMaintainableArtefact()
                .getUrn(), VersionTypeEnum.MAJOR);

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(" + versionExpected + ")";

        // Validate
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

            assertEquals(3, organisationSchemeNewVersion.getMaintainableArtefact().getCategorisations().size());
            {
                Categorisation categorisation = assertListContainsCategorisation(organisationSchemeNewVersion.getMaintainableArtefact().getCategorisations(),
                        "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)");
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
            }
            assertListContainsCategorisation(organisationSchemeNewVersion.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat5(01.000)");
            assertListContainsCategorisation(organisationSchemeNewVersion.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)");

            // Organisations
            assertEquals(5, organisationSchemeVersionTemporal.getItems().size());
            // Check any organisation
            {
                String organisationUrn = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME03(02.000).ORGANISATION01";
                OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), organisationUrn);
                assertFalse(organisation.getSpecialOrganisationHasBeenPublished());
            }
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
                LocalisedString localisedString = new LocalisedString("fr", "fr - text sample");
                organisationSchemeVersionTemporal.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Item: Change Name
            {
                OrganisationMetamac organisationTemporal = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(),
                        GeneratorUrnUtils.makeUrnAsTemporal(ORGANISATION_SCHEME_3_V1_ORGANISATION_1));

                organisationTemporal.getNameableArtefact().setName(new InternationalString());
                organisationTemporal.getNameableArtefact().getName().addText(new LocalisedString("fr", "fr - text sample"));
                organisationTemporal.getNameableArtefact().getName().addText(new LocalisedString("es", "es - text sample"));
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
            assertEquals("fr - text sample", organisationSchemeVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));
            assertNull(organisationSchemeVersionMetamac.getMaintainableArtefact().getIsTemporal());

            // Item
            {
                OrganisationMetamac organisationTemporal = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_3_V1_ORGANISATION_1);
                assertEquals(2, organisationTemporal.getNameableArtefact().getName().getTexts().size());
                assertEquals("fr - text sample", organisationTemporal.getNameableArtefact().getName().getLocalisedLabel("fr"));
                assertEquals("es - text sample", organisationTemporal.getNameableArtefact().getName().getLocalisedLabel("es"));
            }
        }
    }

    @Override
    @Test
    public void testStartOrganisationSchemeValidity() throws Exception {
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.startOrganisationSchemeValidity(getServiceContextAdministrador(), ORGANISATION_SCHEME_7_V2);

        assertNotNull(organisationSchemeVersion);
        assertNotNull(organisationSchemeVersion.getMaintainableArtefact().getValidFrom());
        assertNull(organisationSchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testStartOrganisationSchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {ORGANISATION_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                organisationsService.startOrganisationSchemeValidity(getServiceContextAdministrador(), urn);
                fail("wrong procStatus");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
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
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), organisationRetrieved.getNameableArtefact().getUrn());
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

        Organisation organisation1 = assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListOrganisationsContainsOrganisation(organisation1.getChildren(), organisationRetrieved.getNameableArtefact().getUrn());
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
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateOrganisationAgencySchemeTemporal() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_100_V1;
        TaskInfo taskInfo = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), organisationSchemeUrn);
        String organisationSchemeTemporalUrn = taskInfo.getUrnResult();
        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.AGENCY);

        organisation = organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeTemporalUrn, organisation);
        assertNotNull(organisation.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateOrganisationErrorAgencySchemeMaintainerIncorrect() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_8_V1;
        TaskInfo taskInfo = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), organisationSchemeUrn);
        String organisationSchemeTemporalUrn = taskInfo.getUrnResult();
        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.AGENCY);
        try {
            organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeTemporalUrn, organisation);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_MAINTAINER_IS_NOT_DEFAULT_NOR_SDMX.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateOrganisationErrorOrganisationUnitSchemeTemporal() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_3_V1;
        TaskInfo taskInfo = organisationsService.createTemporalOrganisationScheme(getServiceContextAdministrador(), organisationSchemeUrn);
        String organisationSchemeTemporalUrn = taskInfo.getUrnResult();
        OrganisationMetamac organisation = OrganisationsMetamacDoMocks.mockOrganisation(OrganisationTypeEnum.ORGANISATION_UNIT);
        try {
            organisationsService.createOrganisation(getServiceContextAdministrador(), organisationSchemeTemporalUrn, organisation);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_CAN_NOT_BE_TEMPORAL.getCode(), e.getExceptionItems().get(0).getCode());
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
    public void testUpdateOrganisationCodeAgencyNotPublished() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String urn = ORGANISATION_SCHEME_10_V1_ORGANISATION_1;
        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(ctx, urn);
        assertFalse(organisation.getSpecialOrganisationHasBeenPublished());
        organisation.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(1));
        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        organisationsService.updateOrganisation(getServiceContextAdministrador(), organisation);
    }

    @Test
    public void testUpdateOrganisationCodeErrorSpecialOrganisationWasEverPublished() throws Exception {

        String organisationSchemeUrn = ORGANISATION_SCHEME_8_V1;
        String urn = ORGANISATION_SCHEME_8_V1_ORGANISATION_1;

        // save to force draft to skip validation of proc status
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        organisationSchemeVersion.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DRAFT);
        itemSchemeRepository.save(organisationSchemeVersion);

        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
        assertTrue(organisation.getSpecialOrganisationHasBeenPublished());
        organisation.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(1));
        organisation.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);

        // Validation
        try {
            organisationsService.updateOrganisation(getServiceContextAdministrador(), organisation);
            fail("update code error");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.ORGANISATION_UPDATE_CODE_NOT_SUPPORTED_ORGANISATION_SCHEME_WAS_EVER_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
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
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertEquals(7, organisationSchemeVersion.getItems().size());
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
    }

    @Test
    public void testDeleteOrganisationWithSpecialTreatment() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1_ORGANISATION_1;
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
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItemsFirstLevel(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
        assertEquals(6, organisationSchemeVersion.getItems().size());
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
        assertListOrganisationsContainsOrganisation(organisationSchemeVersion.getItems(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
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
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteOrganisationErrorSpecialOrganisationWasPublished() throws Exception {

        String organisationSchemeUrn = ORGANISATION_SCHEME_8_V1;
        String urn = ORGANISATION_SCHEME_8_V1_ORGANISATION_1;

        // save to force draft to skip error with proc status
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        organisationSchemeVersion.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DRAFT);
        itemSchemeRepository.save(organisationSchemeVersion);

        OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
        assertTrue(organisation.getSpecialOrganisationHasBeenPublished());

        // Validation
        try {
            organisationsService.deleteOrganisation(getServiceContextAdministrador(), urn);
            fail("Organisation can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.ORGANISATION_DELETING_NOT_SUPPORTED_ORGANISATION_SCHEME_WAS_EVER_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
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
                assertEquals(Boolean.FALSE, organisation.getSpecialOrganisationHasBeenPublished());
                assertEquals("Nombre organisationScheme-1-v2-organisation-1", organisation.getName());
                assertEquals("Descripción organisationScheme-1-v2-organisation-1", organisation.getDescription());
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
                assertEquals(OrganisationTypeEnum.ORGANISATION_UNIT, organisation.getType());
                assertEquals(null, organisation.getSpecialOrganisationHasBeenPublished());
                assertEquals("Nombre organisationScheme-1-v2-organisation-2", organisation.getName());
                assertEquals("Descripción organisationScheme-1-v2-organisation-2", organisation.getDescription());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", organisation.getCreatedDate());
            }
            {
                // Organisation 02 01 (validate parent)
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisation.getUrn());
                assertEquals("ORGANISATION02", organisation.getParent().getCode());
                assertEquals("Nombre organisationScheme-1-v2-organisation-2-1", organisation.getName());
                assertEquals("Descripción organisationScheme-1-v2-organisation-2-1", organisation.getDescription());
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
                assertEquals(null, organisation.getDescription());
            }
            {
                // Organisation 02
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
                assertEquals(null, organisation.getName());
                assertEquals("Description organisationScheme-1-v2-organisation-2", organisation.getDescription());
            }
            {
                // Organisation 02 01
                OrganisationMetamacVisualisationResult organisation = getOrganisationVisualisationResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
                assertEquals("Name organisationScheme-1-v2-organisation-2-1", organisation.getName());
                assertEquals("Description organisationScheme-1-v2-organisation-2-1", organisation.getDescription());
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
    public void testRetrieveOrganisationsByOrganisationSchemeUrnUnordered() throws Exception {

        // Retrieve
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        ItemResultSelection itemResultSelection = new ItemResultSelection(true, false, false);
        List<ItemResult> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(getServiceContextAdministrador(), organisationSchemeUrn, itemResultSelection);

        // Validate
        assertEquals(8, organisations.size());
        {
            // Organisation 01 (validate all metadata)
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisation.getUrn());
            assertEquals("ORGANISATION01", organisation.getCode());
            assertEquals(null, organisation.getCodeFull());
            assertEquals("Nombre organisationScheme-1-v2-organisation-1", organisation.getName().get("es"));
            assertEquals("Name organisationScheme-1-v2-organisation-1", organisation.getName().get("en"));
            assertEquals("Descripción organisationScheme-1-v2-organisation-1", organisation.getDescription().get("es"));
            assertEquals(null, organisation.getDescription().get("en"));
            assertEquals(Long.valueOf(121), organisation.getItemIdDatabase());
            assertEquals(null, organisation.getParent());
            assertEquals(null, organisation.getParentIdDatabase());
            assertEquals(null, ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getIdAsMaintainer());
            assertEquals(OrganisationTypeEnum.ORGANISATION_UNIT, ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getOrganisationType());
        }
        {
            // Organisation 02
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getUrn());
            assertEquals("ORGANISATION02", organisation.getCode());
            assertEquals("Nombre organisationScheme-1-v2-organisation-2", organisation.getName().get("es"));
            assertEquals("Descripción organisationScheme-1-v2-organisation-2", organisation.getDescription().get("es"));
            assertEquals(null, ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getIdAsMaintainer());
            assertEquals(OrganisationTypeEnum.ORGANISATION_UNIT, ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getOrganisationType());
        }
        {
            // Organisation 02 01 (validate parent)
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisation.getUrn());
            assertEquals("ORGANISATION0201", organisation.getCode());
            assertEquals(null, organisation.getCodeFull());
            assertEquals("ORGANISATION02", organisation.getParent().getCode());
            assertEquals("Nombre organisationScheme-1-v2-organisation-2-1", organisation.getName().get("es"));
            assertEquals("Descripción organisationScheme-1-v2-organisation-2-1", organisation.getDescription().get("es"));
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getParent().getUrn());
            assertEquals(Long.valueOf("122"), organisation.getParentIdDatabase());
        }
        {
            // Organisation 02 01 01
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisation.getUrn());
            assertEquals("ORGANISATION0201", organisation.getParent().getCode());
            assertEquals("Nombre organisationScheme-1-v2-organisation-2-1-1", organisation.getName().get("es"));
            assertEquals(Long.valueOf("1221"), organisation.getParentIdDatabase());
        }
        {
            // Organisation 03
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_3);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisation.getUrn());
            assertEquals("nombre organisation-3", organisation.getName().get("es"));
        }
        {
            // Organisation 04
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisation.getUrn());
            assertEquals("nombre organisation-4", organisation.getName().get("es"));
        }
        {
            // Organisation 04 01
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisation.getUrn());
            assertEquals("nombre organisation 4-1", organisation.getName().get("es"));
        }
        {
            // Organisation 04 01 01
            ItemResult organisation = getItemResult(organisations, ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisation.getUrn());
            assertEquals("ORGANISATION0401", organisation.getParent().getCode());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisation.getParent().getUrn());
            assertEquals("Nombre organisationScheme-1-v2-organisation-4-1-1", organisation.getName().get("es"));
        }
    }

    @Test
    public void testRetrieveOrganisationsByOrganisationSchemeUrnUnorderedTypeAgency() throws Exception {

        // Retrieve
        String organisationSchemeUrn = ORGANISATION_SCHEME_ROOT_1_V1;

        ItemResultSelection itemResultSelection = new ItemResultSelection(true, false, false);
        List<ItemResult> organisations = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(getServiceContextAdministrador(), organisationSchemeUrn, itemResultSelection);

        // Validate
        assertEquals(2, organisations.size());
        {
            // Organisation 01
            ItemResult organisation = getItemResult(organisations, AGENCY_ROOT_1_V1);
            assertEquals("SDMX01", organisation.getCode());
            assertEquals(null, organisation.getCodeFull());
            assertEquals("SDMX01", ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getIdAsMaintainer());
            assertEquals(OrganisationTypeEnum.AGENCY, ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getOrganisationType());
        }
        {
            // Organisation 02
            ItemResult organisation = getItemResult(organisations, AGENCY_ROOT_2_V1);
            assertEquals("SDMX02", organisation.getCode());
            assertEquals(null, organisation.getCodeFull());
            assertEquals("SDMX02", ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getIdAsMaintainer());
            assertEquals(OrganisationTypeEnum.AGENCY, ((OrganisationResultExtensionPoint) organisation.getExtensionPoint()).getOrganisationType());
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
        // tested in createOrganisation
    }

    @Test
    @Override
    public void testPreCreateOrganisation() throws Exception {
        // tested in createOrganisation
    }

    @Test
    @Override
    public void testExportOrganisationsTsv() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        String fileName = organisationsService.exportOrganisationsTsv(getServiceContextAdministrador(), organisationSchemeUrn);
        assertNotNull(fileName);

        // Validate
        File file = new File(tempDirPath() + File.separatorChar + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        assertEquals("code\tparent\tname#es\tname#pt\tname#en\tname#ca\tdescription#es\tdescription#pt\tdescription#en\tdescription#ca\tcomment#es\tcomment#pt\tcomment#en\tcomment#ca",
                bufferedReader.readLine());
        Set<String> lines = new HashSet<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line.replaceAll("\t", "\\\\t"));
            lines.add(line);
        }
        assertEquals(8, lines.size());

        assertTrue(lines
                .contains("ORGANISATION01\t\tNombre organisationScheme-1-v2-organisation-1\t\tName organisationScheme-1-v2-organisation-1\t\tDescripción organisationScheme-1-v2-organisation-1\t\t\t\t\t\t\t"));
        assertTrue(lines
                .contains("ORGANISATION02\t\tNombre organisationScheme-1-v2-organisation-2\t\t\t\tDescripción organisationScheme-1-v2-organisation-2\t\tDescription organisationScheme-1-v2-organisation-2\t\tComentario organisationScheme-1-v2-organisation-2\t\tComment organisationScheme-1-v2-organisation-2\t"));
        assertTrue(lines
                .contains("ORGANISATION0201\tORGANISATION02\tNombre organisationScheme-1-v2-organisation-2-1\t\tName organisationScheme-1-v2-organisation-2-1\t\tDescripción organisationScheme-1-v2-organisation-2-1\t\tDescription organisationScheme-1-v2-organisation-2-1\t\t\t\t\t"));
        assertTrue(lines.contains("ORGANISATION020101\tORGANISATION0201\tNombre organisationScheme-1-v2-organisation-2-1-1\t\t\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("ORGANISATION03\t\tnombre organisation-3\t\tname organisation-3\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("ORGANISATION04\t\tnombre organisation-4\t\t\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("ORGANISATION0401\tORGANISATION04\tnombre organisation 4-1\t\t\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("ORGANISATION040101\tORGANISATION0401\tNombre organisationScheme-1-v2-organisation-4-1-1\t\tName organisationScheme-1-v2-organisation-4-1-1\t\t\t\t\t\t\t\t\t"));
        bufferedReader.close();
    }

    @Test
    @Override
    public void testImportOrganisationsTsv() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;
        String fileName = "importation-organisation-unit-01.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        boolean updateAlreadyExisting = false;

        TaskImportationInfo taskImportTsvInfo = organisationsService.importOrganisationsTsv(getServiceContextAdministrador(), organisationSchemeUrn, file, fileName, updateAlreadyExisting,
                Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(0, taskImportTsvInfo.getInformationItems().size());

        // Validate item scheme
        OrganisationSchemeVersionMetamac organisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeUrn);
        assertEqualsDate("2011-01-01 01:02:03", organisationSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, organisationSchemeVersion.getItemScheme().getIsTaskInBackground());

        // Validate organisations
        List<ItemResult> result = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(getServiceContextAdministrador(), ORGANISATION_SCHEME_5_V1, ItemResultSelection.ALL);
        assertEquals(result.size(), 20);

        String organisationSchemeUrnPart = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME05(01.000).";
        {
            String semanticIdentifier = "ORGANISATION01";
            OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), organisationSchemeUrnPart + semanticIdentifier);
            assertEquals(semanticIdentifier, organisation.getNameableArtefact().getCode());
            assertEquals(organisation.getNameableArtefact().getUrn(), organisation.getNameableArtefact().getUrnProvider());
            assertEquals(null, organisation.getParent());
            assertEqualsDate("2011-01-22 01:02:03", organisation.getLastUpdated().toDate());
        }
        {
            String semanticIdentifier = "PRESIDENCIA";
            OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), organisationSchemeUrnPart + semanticIdentifier);
            assertEquals(semanticIdentifier, organisation.getNameableArtefact().getCode());
            assertEquals(organisation.getNameableArtefact().getUrn(), organisation.getNameableArtefact().getUrnProvider());
            assertEquals(null, organisation.getParent());
            assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "es", "Presidencia del Gobierno", null, null);
            assertEqualsInternationalString(organisation.getNameableArtefact().getDescription(), "es", "Descripción ES", "en", "Descripción EN");
            assertEqualsInternationalString(organisation.getNameableArtefact().getComment(), "es", "Comentario ES", "en", "Comentario EN");
            BaseAsserts.assertEqualsDay(new DateTime(), organisation.getLastUpdated());
        }
        {
            String semanticIdentifier = "DGRAL_PROMOCION_TURISTICA";
            OrganisationMetamac organisation = organisationsService.retrieveOrganisationByUrn(getServiceContextAdministrador(), organisationSchemeUrnPart + semanticIdentifier);
            assertEquals(semanticIdentifier, organisation.getNameableArtefact().getCode());
            assertEquals(organisation.getNameableArtefact().getUrn(), organisation.getNameableArtefact().getUrnProvider());
            assertEquals("VICECONSEJERIA_TURISMO", organisation.getParent().getNameableArtefact().getCode());
            assertEqualsInternationalString(organisation.getNameableArtefact().getName(), "es", "Dirección General de Ordenación y Promoción Turística", null, null);
            BaseAsserts.assertEqualsDay(new DateTime(), organisation.getLastUpdated());
        }
    }

    @Test
    public void testImportOrganisationWithWrongHierarchyTsv() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_2_V1;
        String fileName = "importation-organisation-unit-01.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        boolean updateAlreadyExisting = false;

        try {
            organisationsService.importOrganisationsTsv(getServiceContextAdministrador(), organisationSchemeUrn, file, fileName, updateAlreadyExisting, Boolean.TRUE);
            fail("The hierarchy of organisations cannot be imported into a data consumer scheme");
        } catch (MetamacException e) {
            assertEquals(11, e.getExceptionItems().size());
            for (MetamacExceptionItem item : e.getExceptionItems()) {
                assertEquals(ServiceExceptionType.IMPORTATION_TSV_METADATA_UNEXPECTED_PARENT.getCode(), item.getCode());
            }
            assertEquals("VICEPRESIDENCIA", e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("PRESIDENCIA", e.getExceptionItems().get(0).getMessageParameters()[1]);
            assertEquals("DGRAL_PROMOCION_TURISTICA", e.getExceptionItems().get(9).getMessageParameters()[0]);
            assertEquals("VICECONSEJERIA_TURISMO", e.getExceptionItems().get(9).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveOrganisationsOrderedInDepthByOrganisationSchemeUrn() throws Exception {

        List<ItemResult> organisations = organisationMetamacRepository.findOrganisationsByOrganisationSchemeOrderedInDepth(Long.valueOf(12), new OrganisationMetamacResultSelection(true, true, true,
                true));

        assertEquals(8, organisations.size());

        Set<String> readOrganisationCodes = new HashSet<String>();

        for (ItemResult organisation : organisations) {
            if (organisation.getParent() != null) {
                assertTrue(readOrganisationCodes.contains(organisation.getParent().getCode()));
            }
            readOrganisationCodes.add(organisation.getCode());
        }

        assertTrue(readOrganisationCodes.contains("ORGANISATION01"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION02"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION0201"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION020101"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION03"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION04"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION0401"));
        assertTrue(readOrganisationCodes.contains("ORGANISATION040101"));
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

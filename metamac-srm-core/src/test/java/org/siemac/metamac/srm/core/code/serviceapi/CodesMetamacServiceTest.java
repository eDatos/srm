package org.siemac.metamac.srm.core.code.serviceapi;

import static com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.assertEqualsInternationalString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;
import static org.siemac.metamac.srm.core.base.utils.BaseServiceTestUtils.assertListItemsContainsItem;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCode;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelist;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistFamily;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariable;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariableFamily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.joda.time.tz.DateTimeZoneBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
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

import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeProperties;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesDoMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CodesMetamacServiceTest extends SrmBaseTest implements CodesMetamacServiceTestBase {

    @Autowired
    protected CodesMetamacService         codesService;

    @Autowired
    protected CodelistFamilyRepository    codelistFamilyRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

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
        assertFalse(codelistVersionRetrieved.getMaintainableArtefact().getFinalLogicClient());
        assertEquals(ctx.getUserId(), codelistVersionRetrieved.getCreatedBy());
        assertEquals(ctx.getUserId(), codelistVersionRetrieved.getLastUpdatedBy());
        assertEqualsCodelist(codelistVersion, codelistVersionRetrieved);
    }

    @Test
    public void testCreateCodelistWithFamily() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);

        // Check the family has no codelists
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(0, codelistFamily.getCodelists().size());

        // Associate the codelist to the family
        codelistVersion.setFamily(codelistFamily);

        // Create codelist
        CodelistVersionMetamac codelistVersionCreated = codesService.createCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistFamily(codelistFamily, codelistVersionCreated.getFamily());

        // Check family has one codelists (and it's the one we have added previously)
        codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(1, codelistFamily.getCodelists().size());
        assertEqualsCodelist(codelistVersionCreated, codelistFamily.getCodelists().get(0));
    }

    @Test
    public void testCreateCodelistWithAditionalMetadata() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);

        codelistVersion.setShortName(null);
        codelistVersion.setIsRecommended(null);
        codelistVersion.setAccessType(null);

        // Create
        CodelistVersionMetamac codelistVersionCreated = codesService.createCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersion, codelistVersionCreated);
    }

    @Test
    public void testCreateConceptSchemeErrorMaintainerNotDefault() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_2_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);

        try {
            codesService.createCodelist(getServiceContextAdministrador(), codelistVersion);
            fail("maintainer not default");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINER_MUST_BE_DEFAULT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(AGENCY_ROOT_2_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(AGENCY_ROOT_1_V1, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateCodelist() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, CODELIST_2_V1);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(ctx, codelistVersion);
        assertNotNull(codelistVersionUpdated);
        assertEquals("user1", codelistVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), codelistVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateCodelistChangingCode() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_2_V1);

        // Change code
        codelistVersion.getMaintainableArtefact().setCode("codeNew");
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        CodelistVersion codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:codeNew(01.000)", codelistVersionUpdated.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testUpdateCodelistAddFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);

        // Check the family has no codelists
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(0, codelistFamily.getCodelists().size());

        // Associate the codelist to the family
        codelistVersion.setFamily(codelistFamily);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistFamily(codelistFamily, codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family has one codelists (and it's the one we have added previously)
        codelistFamily = codelistFamilyRepository.findByIdentifier(CODELIST_FAMILY_1);
        assertEquals(1, codelistFamily.getCodelists().size());
    }

    /**
     * Change codelist from family CODELIST_FAMILY_2 to CODELIST_FAMILY_1
     */
    @Test
    public void testUpdateCodelistChangeFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);

        // Check family members
        CodelistFamily family1 = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        CodelistFamily family2 = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(0, family1.getCodelists().size());
        assertEquals(1, family2.getCodelists().size());

        // Associate the codelist to the family
        codelistVersion.setFamily(family1);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistFamily(family1, codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family after updating codelist
        family1 = codelistFamilyRepository.findByIdentifier(CODELIST_FAMILY_1);
        family2 = codelistFamilyRepository.findByIdentifier(CODELIST_FAMILY_2);
        assertEquals(1, family1.getCodelists().size());
        assertEquals(0, family2.getCodelists().size());
    }

    @Test
    public void testUpdateCodelistRemoveFromFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);

        // Check family members
        CodelistFamily family = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(1, family.getCodelists().size());

        // Update codelist (remove from family)
        codelistVersion.setFamily(null);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertNull(codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family members
        family = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(0, family.getCodelists().size());
    }

    @Test
    public void testUpdateCodelistWithAditionalMetadata() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, CODELIST_1_V2);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setAccessType(AccessTypeEnum.PUBLIC);

        codelistVersion.setShortName(com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks.mockInternationalString());
        codelistVersion.setIsRecommended(Boolean.TRUE);

        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(ctx, codelistVersion);
        assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersion, codelistVersionUpdated);
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
        assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
        assertEqualsDate("2011-01-02 02:02:03", codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        assertEqualsDate("2011-01-03 03:02:03", codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Test
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
            assertFalse(codelistVersion.getMaintainableArtefact().getLatestFinal());
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
            assertTrue(codelistVersion.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(codelistVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(codelistVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(codelistVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(codelistVersion.getMaintainableArtefact().getLatestPublic());
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
            assertTrue(codelistVersion.getMaintainableArtefact().getLatestFinal());
        }
    }

    @Test
    public void testPublishInternallyCodelistErrorRequiredMetadata() throws Exception {
        String urn = CODELIST_11_V1;
        try {
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn);
            fail("codelist cannot be publish without an access type defined and with an associated variable");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_ACCESS_TYPE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE}, e.getExceptionItems().get(1));
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
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testPublishInternallyCodelistErrorWrongProcStatus() throws Exception {
        String urn = CODELIST_1_V1;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());

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
            assertTrue(codelistVersion.getMaintainableArtefact().getPublicLogic());
            assertTrue(codelistVersion.getMaintainableArtefact().getLatestPublic());
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
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urnV2}, e.getExceptionItems().get(0));
        }
        codelistVersionV1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(codelistVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(codelistVersionV1.getMaintainableArtefact().getReplacedByVersion());
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
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteCodelistsWithVariable() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelist6v1 = codesService.retrieveCodelistByUrn(ctx, CODELIST_6_V1);
        assertEquals(VARIABLE_6, codelist6v1.getVariable().getIdentifier());

        codesService.deleteCodelist(ctx, CODELIST_6_V1);

        // Check that the codelist has been deleted deleted
        try {
            codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_6_V1);
            fail("Codelist deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{CODELIST_6_V1}, e.getExceptionItems().get(0));
        }

        // Check that the variable has not been deleted
        Variable variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_6);
        assertFalse(SrmValidationUtils.isCodelistInList(CODELIST_6_V1, variable.getCodelists()));
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
            assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", codelistVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, codelistVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(codelistVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);

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

                assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-1", null, null);

                assertEquals(0, code.getChildren().size());
            }
            {
                CodeMetamac code = (CodeMetamac) codelistVersionNewVersion.getItemsFirstLevel().get(1);
                assertEquals(urnExpectedCode2, code.getNameableArtefact().getUrn());

                assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-2", null, null);

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
            assertEquals(null, codelistVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, codelistVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
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
            assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals("02.000", codelistVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, codelistVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(codelistVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);

            // Version copied
            codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", codelistVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, codelistVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, codelistVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals("02.000", codelistVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(codelistVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            codelistVersionLast = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", codelistVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, codelistVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", codelistVersionLast.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, codelistVersionLast.getMaintainableArtefact().getReplacedByVersion());
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
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
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

    @Test
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

    @Test
    public void testCreateCode() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setParent(null);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);
        String urn = codeCreated.getNameableArtefact().getUrn();
        assertEquals(ctx.getUserId(), codeCreated.getCreatedBy());
        assertEquals(ctx.getUserId(), codeCreated.getLastUpdatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CodeMetamac codeRetrieved = codesService.retrieveCodeByUrn(ctx, urn);
        assertEqualsCode(code, codeRetrieved);

        // Validate new structure
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, codelistUrn);
        assertEquals(5, codelistVersion.getItemsFirstLevel().size());
        assertEquals(9, codelistVersion.getItems().size());
        assertEquals(CODELIST_1_V2_CODE_1, codelistVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_2, codelistVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_3, codelistVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_4, codelistVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
        assertEquals(codeRetrieved.getNameableArtefact().getUrn(), codelistVersion.getItemsFirstLevel().get(4).getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCodeSubcode() throws Exception {
        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        CodeMetamac codeParent = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
        code.setParent(codeParent);
        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codelistVersionCreated = codesService.createCode(getServiceContextAdministrador(), codelistUrn, code);
        String urn = codelistVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CodeMetamac codeRetrieved = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
        assertEqualsCode(code, codeRetrieved);

        // Validate new structure
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(4, codelistVersion.getItemsFirstLevel().size());
        assertEquals(9, codelistVersion.getItems().size());

        assertEquals(CODELIST_1_V2_CODE_1, codelistVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
        assertEquals(codeRetrieved.getNameableArtefact().getUrn(), codelistVersion.getItemsFirstLevel().get(0).getChildren().get(0).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_2, codelistVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_3, codelistVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_4, codelistVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCodeIncorrectCodelist() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setParent(null);

        String codelistUrn = NOT_EXISTS;

        try {
            codesService.createCode(ctx, codelistUrn, code);
            fail("Code cannot be created without a associated codelist");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateCode() throws Exception {
        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
        code.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        code.getNameableArtefact().setName(CodesDoMocks.mockInternationalString());

        // Update
        CodeMetamac codeUpdated = codesService.updateCode(getServiceContextAdministrador(), code);

        // Validate
        assertEqualsCode(code, codeUpdated);
    }

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

    @Test
    public void testDeleteCode() throws Exception {
        String urn = CODELIST_1_V2_CODE_3;
        String codelistUrn = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(4, codelistVersion.getItemsFirstLevel().size());
        assertEquals(8, codelistVersion.getItems().size());

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validation
        try {
            codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
            fail("Code deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }

        // Check hierarchy
        codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(3, codelistVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
        assertEquals(7, codelistVersion.getItems().size());
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1);
    }

    @Test
    public void testDeleteCodeWithParentAndChildren() throws Exception {

        String urn = CODELIST_1_V2_CODE_4_1;
        String codelistUrn = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(4, codelistVersion.getItemsFirstLevel().size());
        assertEquals(8, codelistVersion.getItems().size());

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validation
        try {
            codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);
            fail("Code deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }

        codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(4, codelistVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_3);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
        assertEquals(6, codelistVersion.getItems().size());
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_3);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
    }

    @Test
    public void testDeleteCodeErrorCodelistPublished() throws Exception {
        String urn = CODELIST_12_V1_CODE_1;
        String codelistUrn = CODELIST_12_V1;
        try {
            codesService.deleteCode(getServiceContextAdministrador(), urn);
            fail("Code can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(codelistUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrn() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        List<CodeMetamac> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn);

        // Validate
        assertEquals(4, codes.size());
        {
            // Code 01
            CodeMetamac code = codes.get(0);
            assertEquals(CODELIST_1_V2_CODE_1, code.getNameableArtefact().getUrn());
            assertEquals(0, code.getChildren().size());
        }
        {
            // Code 02
            CodeMetamac code = codes.get(1);
            assertEquals(CODELIST_1_V2_CODE_2, code.getNameableArtefact().getUrn());
            assertEquals(1, code.getChildren().size());
            {
                // Code 02 01
                CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(0);
                assertEquals(CODELIST_1_V2_CODE_2_1, codeChild.getNameableArtefact().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 02 01 01
                    CodeMetamac codeChildChild = (CodeMetamac) codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_2_1_1, codeChildChild.getNameableArtefact().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
        {
            // Code 03
            CodeMetamac code = codes.get(2);
            assertEquals(CODELIST_1_V2_CODE_3, code.getNameableArtefact().getUrn());
            assertEquals(0, code.getChildren().size());
        }
        {
            // Code 04
            CodeMetamac code = codes.get(3);
            assertEquals(CODELIST_1_V2_CODE_4, code.getNameableArtefact().getUrn());
            assertEquals(1, code.getChildren().size());
            {
                // Code 04 01
                CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(0);
                assertEquals(CODELIST_1_V2_CODE_4_1, codeChild.getNameableArtefact().getUrn());
                assertEquals(1, codeChild.getChildren().size());
                {
                    // Code 04 01 01
                    CodeMetamac codeChildChild = (CodeMetamac) codeChild.getChildren().get(0);
                    assertEquals(CODELIST_1_V2_CODE_4_1_1, codeChildChild.getNameableArtefact().getUrn());
                    assertEquals(0, codeChildChild.getChildren().size());
                }
            }
        }
    }

    @Test
    public void testFindCodesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).orderBy(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                    .orderBy(CodeProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(28, codesPagedResult.getTotalRows());
            assertEquals(28, codesPagedResult.getValues().size());
            assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

            int i = 0;
            assertEquals(CODELIST_1_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_2_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_2_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_3_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_3_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_3_V1_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_3_V1_CODE_2_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_3_V1_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_4_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_5_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_6_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_7_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_8_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_10_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_10_V3_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_11_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_12_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_13_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_13_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_13_V1_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(codesPagedResult.getValues().size(), i);
        }

        // Find by name (like), code (like) and codelist urn
        {
            String name = "Nombre codelist-1-v2-code-2-";
            String code = "CODE02";
            String codelistUrn = CODELIST_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).withProperty(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(codelistUrn)
                    .withProperty(CodeProperties.nameableArtefact().code()).like(code + "%").withProperty(CodeProperties.nameableArtefact().name().texts().label()).like(name + "%")
                    .orderBy(CodeProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, codesPagedResult.getTotalRows());
            assertEquals(2, codesPagedResult.getValues().size());
            assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

            int i = 0;
            assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(codesPagedResult.getValues().size(), i);
        }

        // Find by codelist urn paginated
        {
            String codelistUrn = CODELIST_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).withProperty(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(codelistUrn)
                    .orderBy(CodeProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
                PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, codesPagedResult.getTotalRows());
                assertEquals(3, codesPagedResult.getValues().size());
                assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(codesPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
                PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, codesPagedResult.getTotalRows());
                assertEquals(3, codesPagedResult.getValues().size());
                assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(codesPagedResult.getValues().size(), i);
            }
            // Third page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
                PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, codesPagedResult.getTotalRows());
                assertEquals(2, codesPagedResult.getValues().size());
                assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(codesPagedResult.getValues().size(), i);
            }
        }
    }

    // ------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCodelistFamilyByIdentifier() throws Exception {
        String identifier = CODELIST_FAMILY_1;
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), identifier);

        assertEquals(identifier, codelistFamily.getIdentifier());
        assertEqualsInternationalString(codelistFamily.getName(), "es", "familia-de-codelists-51", "pt", "codelist-family");
        assertEquals("codelist-family-01", codelistFamily.getUuid());
        assertEquals("user1", codelistFamily.getCreatedBy());
        assertEquals("user2", codelistFamily.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), codelistFamily.getVersion());
    }

    @Test
    public void testRetrieveCodelistFamilyByIdentifierErrorNotFound() throws Exception {
        String identifier = NOT_EXISTS;
        try {
            codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), identifier);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODELIST_FAMILY_NOT_FOUND, 1, new String[]{identifier}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveCodelistFamilyByIdentifierErrorParameterRequired() throws Exception {
        String identifier = null;
        try {
            codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), identifier);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodelistFamily() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        CodelistFamily codelistVersionCreated = codesService.createCodelistFamily(ctx, codelistFamily);
        assertEquals(ctx.getUserId(), codelistVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), codelistVersionCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersionCreated.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), codelistVersionCreated.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersionCreated.getLastUpdated().toDate()));
        assertEqualsCodelistFamily(codelistVersionCreated, codelistFamily);
    }

    @Test
    public void testCreateCodelistFamilyErrorWrongIdentifier() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        codelistFamily.setIdentifier(" 0 - invalid identifier");
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("wrong identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodelistFamilyErrorDuplicatedIdentifier() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        codelistFamily.setIdentifier(CODELIST_FAMILY_1);
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("duplicated identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODELIST_FAMILY_DUPLICATED_IDENTIFIER, 1, new String[]{CODELIST_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodelistFamilyErrorRequiredMetadata() throws Exception {
        CodelistFamily codelistFamily = new CodelistFamily();
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_NAME}, e.getExceptionItems().get(1));
        }
    }

    @Test
    public void testUpdateCodelistFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(ctx, CODELIST_FAMILY_1);
        codelistFamily.setIdentifier("code-" + MetamacMocks.mockString(10));
        codelistFamily.setName(BaseDoMocks.mockInternationalString());

        CodelistFamily codelistFamilyUpdated = codesService.updateCodelistFamily(ctx, codelistFamily);

        assertEqualsCodelistFamily(codelistFamily, codelistFamilyUpdated);
    }

    @Test
    public void testUpdateCodelistErrorChangeCodeInCodelistWithVersionAlreadyPublished() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        codelistVersion.getMaintainableArtefact().setCode("newCode");
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);

        try {
            codelistVersion = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
            fail("code can not be changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateCodelistFamilyErrorWrongIdentifier() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.setIdentifier(" 0 - invalid identifier");
        try {
            codesService.updateCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("wrong identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateCodelistFamilyErrorDuplicatedIdentifier() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.setIdentifier(CODELIST_FAMILY_2);
        try {
            codesService.updateCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("duplicated identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODELIST_FAMILY_DUPLICATED_IDENTIFIER, 1, new String[]{CODELIST_FAMILY_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateCodelistFamilyErrorRequiredMetadata() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.setIdentifier(null);
        codelistFamily.setName(null);
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_FAMILY_NAME}, e.getExceptionItems().get(1));
        }
    }

    @Test
    public void testFindCodelistFamiliesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).orderBy(CodelistFamilyProperties.identifier()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistFamily> result = codesService.findCodelistFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(2, result.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_FAMILY_1, result.getValues().get(i++).getIdentifier());
            assertEquals(CODELIST_FAMILY_2, result.getValues().get(i++).getIdentifier());
        }
        // Find by identifier
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).withProperty(CodelistFamilyProperties.identifier()).like(CODELIST_FAMILY_1)
                    .orderBy(CodelistFamilyProperties.identifier()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistFamily> result = codesService.findCodelistFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_FAMILY_1, result.getValues().get(i++).getIdentifier());

        }
    }

    @Test
    public void testDeleteCodelistFamily() throws Exception {
        codesService.deleteCodelistFamily(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        // Retrieve deleted family
        try {
            codesService.retrieveCodelistFamilyByIdentifier(getServiceContextAdministrador(), CODELIST_FAMILY_1);
            fail("codelist family already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODELIST_FAMILY_NOT_FOUND, 1, new String[]{CODELIST_FAMILY_1}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted codelist family
        try {
            codesService.deleteCodelistFamily(getServiceContextAdministrador(), CODELIST_FAMILY_1);
            fail("codelist already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODELIST_FAMILY_NOT_FOUND, 1, new String[]{CODELIST_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testDeleteCodelistFamilyWithCodelists() throws Exception {
        codesService.deleteCodelistFamily(getServiceContextAdministrador(), CODELIST_FAMILY_2);

        CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);
        assertNotNull(codelist);
        assertNull(codelist.getFamily());
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    @Test
    public void testRetrieveVariableFamilyByIdentifier() throws Exception {
        String identifier = VARIABLE_FAMILY_1;
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), identifier);

        assertEquals(identifier, variableFamily.getIdentifier());
        assertEqualsInternationalString(variableFamily.getName(), "es", "familia-de-variables-53", null, null);
        assertEquals("variable-family-01", variableFamily.getUuid());
        assertEquals("user1", variableFamily.getCreatedBy());
        assertEquals("user2", variableFamily.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variableFamily.getVersion());
    }

    @Test
    public void testRetrieveVariableFamilyByIdentifierErrorNotFound() throws Exception {
        String identifier = NOT_EXISTS;
        try {
            codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), identifier);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, 1, new String[]{identifier}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveVariableFamilyByIdentifierErrorParameterRequired() throws Exception {
        String identifier = null;
        try {
            codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), identifier);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableFamily() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        VariableFamily variableVersionCreated = codesService.createVariableFamily(ctx, variableFamily);

        assertEquals(ctx.getUserId(), variableVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), variableVersionCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableVersionCreated.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), variableVersionCreated.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableVersionCreated.getLastUpdated().toDate()));
        assertEqualsVariableFamily(variableVersionCreated, variableFamily);
    }

    @Test
    public void testCreateVariableFamilyErrorWrongIdentifier() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        variableFamily.setIdentifier(" 0 - invalid identifier");
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("wrong identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableFamilyErrorDuplicatedIdentifier() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        variableFamily.setIdentifier(VARIABLE_FAMILY_1);
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("duplicated identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_DUPLICATED_IDENTIFIER, 1, new String[]{VARIABLE_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableFamilyErrorRequiredMetadata() throws Exception {
        VariableFamily variableFamily = new VariableFamily();
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_NAME}, e.getExceptionItems().get(1));
        }
    }

    @Test
    public void testUpdateVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        VariableFamily variableFamily = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_1);
        variableFamily.setIdentifier("code-" + MetamacMocks.mockString(10));
        variableFamily.setName(BaseDoMocks.mockInternationalString());

        VariableFamily variableFamilyUpdated = codesService.updateVariableFamily(ctx, variableFamily);

        assertEqualsVariableFamily(variableFamily, variableFamilyUpdated);
    }

    @Test
    public void testUpdateVariableFamilyErrorWrongIdentifier() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.setIdentifier(" 0 - invalid identifier");
        try {
            codesService.updateVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("wrong identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableFamilyErrorDuplicatedIdentifier() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.setIdentifier(VARIABLE_FAMILY_2);
        try {
            codesService.updateVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("duplicated identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_DUPLICATED_IDENTIFIER, 1, new String[]{VARIABLE_FAMILY_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableFamilyErrorRequiredMetadata() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.setIdentifier(null);
        variableFamily.setName(null);
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_NAME}, e.getExceptionItems().get(1));
        }
    }

    @Test
    public void testDeleteVariableFamily() throws Exception {
        codesService.deleteVariableFamily(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        // Retrieve deleted family
        try {
            codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
            fail("variable family already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, 1, new String[]{VARIABLE_FAMILY_1}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted variable family
        try {
            codesService.deleteVariableFamily(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, 1, new String[]{VARIABLE_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testDeleteVariableFamilyWithVariables() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        VariableFamily family = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_3);
        assertEquals(2, family.getVariables().size());
        assertTrue(SrmValidationUtils.isVariableInList(VARIABLE_3, family.getVariables()));
        assertTrue(SrmValidationUtils.isVariableInList(VARIABLE_4, family.getVariables()));

        codesService.deleteVariableFamily(ctx, VARIABLE_FAMILY_3);

        // Check that the variable family has been deleted
        try {
            codesService.retrieveVariableFamilyByIdentifier(getServiceContextAdministrador(), VARIABLE_FAMILY_3);
            fail("variable family already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, 1, new String[]{VARIABLE_FAMILY_3}, e.getExceptionItems().get(0));
        }
        try {
            codesService.deleteVariableFamily(getServiceContextAdministrador(), VARIABLE_FAMILY_3);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, 1, new String[]{VARIABLE_FAMILY_3}, e.getExceptionItems().get(0));
        }

        // Check that the associated variables has not been deleted
        Variable variable3 = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_3);
        Variable variable4 = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_4);
        assertFalse(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_3, variable3.getFamilies()));
        assertFalse(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_3, variable4.getFamilies()));
    }

    @Test
    public void testFindVariableFamiliesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).orderBy(VariableFamilyProperties.identifier()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableFamily> result = codesService.findVariableFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(4, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_FAMILY_1, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_FAMILY_2, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_FAMILY_3, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_FAMILY_4, result.getValues().get(i++).getIdentifier());
        }
        // Find by identifier
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).withProperty(VariableFamilyProperties.identifier()).like(VARIABLE_FAMILY_1)
                    .orderBy(VariableFamilyProperties.identifier()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableFamily> result = codesService.findVariableFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_FAMILY_1, result.getValues().get(i++).getIdentifier());

        }
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    @Test
    public void testCreateVariable() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        List<String> familyIdentifiers = Arrays.asList(VARIABLE_FAMILY_1, VARIABLE_FAMILY_2);
        Variable variableCreated = codesService.createVariable(ctx, familyIdentifiers, variable);

        assertEquals(ctx.getUserId(), variableCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), variableCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableCreated.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), variableCreated.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableCreated.getLastUpdated().toDate()));

        // Check that the variable was created in both families
        assertTrue(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_1, variableCreated.getFamilies()));
        assertTrue(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_2, variableCreated.getFamilies()));
        VariableFamily family1 = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_1);
        VariableFamily family2 = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_2);
        assertTrue(SrmValidationUtils.isVariableInList(variableCreated.getIdentifier(), family1.getVariables()));
        assertTrue(SrmValidationUtils.isVariableInList(variableCreated.getIdentifier(), family2.getVariables()));

        assertEqualsVariable(variableCreated, variable);
    }

    @Test
    public void testCreateVariableErrorWrongIdentifier() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.setIdentifier(" 0 - invalid identifier");
        try {
            codesService.createVariable(getServiceContextAdministrador(), Arrays.asList(VARIABLE_FAMILY_1), variable);
            fail("wrong identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableErrorDuplicatedIdentifier() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.setIdentifier(VARIABLE_1);
        try {
            codesService.createVariable(getServiceContextAdministrador(), Arrays.asList(VARIABLE_FAMILY_1), variable);
            fail("duplicated identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_DUPLICATED_IDENTIFIER, 1, new String[]{VARIABLE_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableErrorIncorrectMetadata() throws Exception {
        Variable variable = new Variable();
        variable.setValidFrom(null);
        variable.setValidTo(new DateTime());
        try {
            codesService.createVariable(getServiceContextAdministrador(), Arrays.asList(VARIABLE_FAMILY_1), variable);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_IDENTIFIER}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_NAME}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_SHORT_NAME}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_VALID_TO}, e.getExceptionItems().get(3));
        }
    }

    @Test
    public void testCreateVariableErrorIncorrectFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = CodesMetamacDoMocks.mockVariable();

        // Null family
        try {
            codesService.createVariable(ctx, null, variable);
            fail("required family to create a variable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }

        // Empty family list
        try {
            codesService.createVariable(ctx, new ArrayList<String>(), variable);
            fail("required family to create a variable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY_IDENTIFIER}, e.getExceptionItems().get(0));
        }

        // Non-existent family
        try {
            codesService.createVariable(ctx, Arrays.asList(NOT_EXISTS), variable);
            fail("required family to create a variable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, 1, new String[]{NOT_EXISTS}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariable() throws Exception {
        Variable variable = codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_1);
        variable.setIdentifier("code-" + MetamacMocks.mockString(10));
        variable.setName(BaseDoMocks.mockInternationalString());
        variable.setShortName(BaseDoMocks.mockInternationalString());
        variable.setValidFrom(MetamacMocks.mockDateTime());
        variable.setValidTo(null);

        Variable variableUpdated = codesService.updateVariable(getServiceContextAdministrador(), variable);

        assertEqualsVariable(variable, variableUpdated);
    }

    @Test
    public void testUpdateVariableErrorWrongIdentifier() throws Exception {
        Variable variable = codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_1);
        variable.setIdentifier(" 0 - invalid identifier");
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("wrong identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableErrorDuplicatedIdentifier() throws Exception {
        Variable variable = codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_1);
        variable.setIdentifier(VARIABLE_2);
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("duplicated identifier");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_DUPLICATED_IDENTIFIER, 1, new String[]{VARIABLE_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableErrorIncorrectMetadata() throws Exception {
        Variable variable = codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_1);
        variable.setIdentifier(null);
        variable.setName(null);
        variable.setShortName(null);
        variable.setValidFrom(null);
        variable.setValidTo(new DateTime());
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_IDENTIFIER}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_NAME}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_SHORT_NAME}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_VALID_TO}, e.getExceptionItems().get(3));
        }
    }

    @Test
    public void testRetrieveVariableByIdentifier() throws Exception {
        String identifier = VARIABLE_1;
        Variable variable = codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), identifier);

        assertEquals(identifier, variable.getIdentifier());
        assertEqualsInternationalString(variable.getName(), "es", "variable-57", null, null);
        assertEqualsInternationalString(variable.getShortName(), "es", "variable-63", null, null);
        assertEqualsDate(new DateTime(2011, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variable.getValidFrom());
        assertEqualsDate(new DateTime(2012, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variable.getValidTo());

        assertEquals("variable-01", variable.getUuid());
        assertEquals("user1", variable.getCreatedBy());
        assertEquals("user2", variable.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variable.getVersion());
    }

    @Test
    public void testRetrieveVariableByIdentifierErrorNotFound() throws Exception {
        String identifier = NOT_EXISTS;
        try {
            codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), identifier);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_NOT_FOUND, 1, new String[]{identifier}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveVariableByIdentifierErrorParameterRequired() throws Exception {
        String identifier = null;
        try {
            codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), identifier);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_IDENTIFIER}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testFindVariablesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).orderBy(VariableProperties.identifier()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<Variable> result = codesService.findVariablesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(6, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_2, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_3, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_4, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_5, result.getValues().get(i++).getIdentifier());
            assertEquals(VARIABLE_6, result.getValues().get(i++).getIdentifier());
        }
        // Find by identifier
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).withProperty(VariableProperties.identifier()).like(VARIABLE_1)
                    .orderBy(VariableProperties.identifier()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<Variable> result = codesService.findVariablesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1, result.getValues().get(i++).getIdentifier());
        }
    }

    @Test
    public void testDeleteVariable() throws Exception {
        codesService.deleteVariable(getServiceContextAdministrador(), VARIABLE_1);
        // Retrieve deleted variable
        try {
            codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_1);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_NOT_FOUND, 1, new String[]{VARIABLE_1}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted variable
        try {
            codesService.deleteVariable(getServiceContextAdministrador(), VARIABLE_1);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_NOT_FOUND, 1, new String[]{VARIABLE_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testDeleteVariableWithFamilies() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_4);
        assertEquals(2, variable.getFamilies().size());
        assertTrue(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_3, variable.getFamilies()));
        assertTrue(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_4, variable.getFamilies()));

        // Delete variable
        codesService.deleteVariable(ctx, VARIABLE_4);

        // Check that the variable has beed deleted
        try {
            codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_4);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_NOT_FOUND, 1, new String[]{VARIABLE_4}, e.getExceptionItems().get(0));
        }

        // Check that the families has not been deleted
        VariableFamily family3 = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_3);
        VariableFamily family4 = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_4);
        assertFalse(SrmValidationUtils.isVariableInList(VARIABLE_4, family3.getVariables()));
        assertFalse(SrmValidationUtils.isVariableInList(VARIABLE_4, family4.getVariables()));
    }

    @Test
    public void testDeleteVariableWithCodelists() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_6);
        assertEquals(1, variable.getCodelists().size());
        assertTrue(SrmValidationUtils.isCodelistInList(CODELIST_6_V1, variable.getCodelists()));

        // Delete variable
        codesService.deleteVariable(ctx, VARIABLE_6);

        // Check that the variable has been deleted
        try {
            codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_6);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_NOT_FOUND, 1, new String[]{VARIABLE_6}, e.getExceptionItems().get(0));
        }

        // Chech that the codelists has not been deleted
        CodelistVersionMetamac codelist6v1 = codesService.retrieveCodelistByUrn(ctx, CODELIST_6_V1);
        assertNull(codelist6v1.getVariable());
    }

    @Test
    // TODO A variable can be deleted if its associated with a published codelist?
    public void testDeleteVariableWithPublishedCodelists() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_5);
        assertEquals(1, variable.getCodelists().size());
        assertTrue(SrmValidationUtils.isCodelistInList(CODELIST_7_V2, variable.getCodelists()));

        // Delete variable
        codesService.deleteVariable(ctx, VARIABLE_5);

        // Check that the variable has been deleted
        try {
            codesService.retrieveVariableByIdentifier(getServiceContextAdministrador(), VARIABLE_5);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_NOT_FOUND, 1, new String[]{VARIABLE_5}, e.getExceptionItems().get(0));
        }

        // Chech that the codelists has not been deleted
        CodelistVersionMetamac codelist7v2 = codesService.retrieveCodelistByUrn(ctx, CODELIST_7_V2);
        assertNull(codelist7v2.getVariable());
    }

    @Test
    public void testAddVariableToFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        VariableFamily family = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_1);
        Variable variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_1);
        assertFalse(SrmValidationUtils.isVariableInList(VARIABLE_1, family.getVariables()));
        assertFalse(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_1, variable.getFamilies()));

        codesService.addVariableToFamily(ctx, VARIABLE_1, VARIABLE_FAMILY_1);

        family = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_1);
        variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_1);
        assertTrue(SrmValidationUtils.isVariableInList(VARIABLE_1, family.getVariables()));
        assertTrue(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_1, variable.getFamilies()));

        // Adding the variable to the family again has no consequences
        codesService.addVariableToFamily(ctx, VARIABLE_1, VARIABLE_FAMILY_1);
    }

    @Test
    public void testRemoveVariableFromFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        VariableFamily family = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_3);
        Variable variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_3);
        assertTrue(SrmValidationUtils.isVariableInList(VARIABLE_3, family.getVariables()));
        assertTrue(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_3, variable.getFamilies()));

        codesService.removeVariableFromFamily(ctx, VARIABLE_3, VARIABLE_FAMILY_3);

        family = codesService.retrieveVariableFamilyByIdentifier(ctx, VARIABLE_FAMILY_3);
        variable = codesService.retrieveVariableByIdentifier(ctx, VARIABLE_3);
        assertFalse(SrmValidationUtils.isVariableInList(VARIABLE_3, family.getVariables()));
        assertFalse(SrmValidationUtils.isFamilyInList(VARIABLE_FAMILY_3, variable.getFamilies()));

        // Removing the variable from the family again has no consequences
        codesService.removeVariableFromFamily(ctx, VARIABLE_3, VARIABLE_FAMILY_3);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}

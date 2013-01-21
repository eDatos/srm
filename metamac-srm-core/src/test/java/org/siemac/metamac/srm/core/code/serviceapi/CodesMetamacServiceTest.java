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
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistOrderVisualisation;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariable;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariableElement;
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
import org.siemac.metamac.srm.core.base.utils.BaseServiceTestUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
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
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
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

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateCodelist() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        codelistVersion.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));
        ServiceContext ctx = getServiceContextAdministrador();

        // Replace to
        CodelistVersionMetamac codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1);
        CodelistVersionMetamac codelistReplaced2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_2_V1);
        codelistVersion.addReplaceToCodelist(codelistReplaced1);
        codelistVersion.addReplaceToCodelist(codelistReplaced2);

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

        // Check replace metadata
        assertEquals(2, codelistVersionRetrieved.getReplaceToCodelists().size());
        assertEquals(CODELIST_1_V1, codelistVersionRetrieved.getReplaceToCodelists().get(0).getMaintainableArtefact().getUrn());
        assertEquals(CODELIST_2_V1, codelistVersionRetrieved.getReplaceToCodelists().get(1).getMaintainableArtefact().getUrn());

        // Check replaced by metadata
        codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1);
        assertEquals(urn, codelistReplaced1.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        codelistReplaced2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_2_V1);
        assertEquals(urn, codelistReplaced2.getReplacedByCodelist().getMaintainableArtefact().getUrn());
    }

    @Test
    public void testCreateCodelistWithFamily() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);

        // Check the family has no codelists
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(0, codelistFamily.getCodelists().size());

        // Associate the codelist to the family
        codelistVersion.setFamily(codelistFamily);

        // Create codelist
        CodelistVersionMetamac codelistVersionCreated = codesService.createCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistFamily(codelistFamily, codelistVersionCreated.getFamily());

        // Check family has one codelists (and it's the one we have added previously)
        codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
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

    @Override
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
    public void testUpdateCodelistChangingReplaceTo() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String urn = CODELIST_2_V1;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        assertEquals(null, codelistVersion.getReplacedByCodelist());
        assertEquals(1, codelistVersion.getReplaceToCodelists().size());
        assertEquals(CODELIST_1_V1, codelistVersion.getReplaceToCodelists().get(0).getMaintainableArtefact().getUrn());

        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        // Replace to
        codelistVersion.removeAllReplaceToCodelists();
        codelistVersion.addReplaceToCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_3_V1));
        codelistVersion.addReplaceToCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_11_V1));
        codesService.updateCodelist(ctx, codelistVersion);

        // Check replaced by metadata
        CodelistVersionMetamac codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_3_V1);
        assertEquals(urn, codelistReplaced1.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        CodelistVersionMetamac codelistReplaced2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_11_V1);
        assertEquals(urn, codelistReplaced2.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        CodelistVersionMetamac codelistNotReplaced3 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1);
        assertEquals(null, codelistNotReplaced3.getReplacedByCodelist());
    }

    @Test
    public void testUpdateCodelistChangingDefaultOrderVisualisation() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(CODELIST_1_V2_ORDER_VISUALISATION_01, codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());

        String visualisationUrnNew = CODELIST_1_V2_ORDER_VISUALISATION_02;
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setDefaultOrderVisualisation(codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrnNew));
        codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);

        CodelistVersionMetamac codelistVersionRetrieved = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(visualisationUrnNew, codelistVersionRetrieved.getDefaultOrderVisualisation().getNameableArtefact().getUrn());
    }

    @Test
    public void testUpdateCodelistAddFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);

        // Check the family has no codelists
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(0, codelistFamily.getCodelists().size());

        // Associate the codelist to the family
        codelistVersion.setFamily(codelistFamily);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistFamily(codelistFamily, codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family has one codelists (and it's the one we have added previously)
        codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(1, codelistFamily.getCodelists().size());
        assertEquals(codelistVersion.getMaintainableArtefact().getUrn(), codelistFamily.getCodelists().get(0).getMaintainableArtefact().getUrn());
    }

    @Test
    public void testUpdateCodelistChangingVariable() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);

        // Check the variable has no codelists
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        assertEquals(0, variable.getCodelists().size());

        // Associate the codelist to the variable
        codelistVersion.setVariable(variable);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsVariable(variable, codelistVersionUpdated.getVariable());

        entityManager.clear(); // Clear hibernate cache to check that the variable has been updated

        // Check variable has one codelists (and it's the one we have added previously)
        variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variable.getNameableArtefact().getUrn());
        assertEquals(1, variable.getCodelists().size());
        assertEquals(codelistVersion.getMaintainableArtefact().getUrn(), variable.getCodelists().get(0).getMaintainableArtefact().getUrn());
    }

    /**
     * Change codelist from family CODELIST_FAMILY_2 to CODELIST_FAMILY_1
     */
    @Test
    public void testUpdateCodelistChangingFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);

        // Check family members
        CodelistFamily family1 = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        CodelistFamily family2 = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(0, family1.getCodelists().size());
        assertEquals(3, family2.getCodelists().size());
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_9_V1, family2.getCodelists()));

        // Associate the codelist to the family
        codelistVersion.setFamily(family1);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsCodelistFamily(family1, codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family after updating codelist
        family1 = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        family2 = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(1, family1.getCodelists().size());
        assertEquals(2, family2.getCodelists().size());
        assertFalse(SrmServiceUtils.isCodelistInList(CODELIST_9_V1, family2.getCodelists()));
    }

    @Test
    public void testUpdateCodelistRemoveFromFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);

        // Check family members
        CodelistFamily family = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(3, family.getCodelists().size());
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_9_V1, family.getCodelists()));

        // Update codelist (remove from family)
        codelistVersion.setFamily(null);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertNull(codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family members
        family = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(2, family.getCodelists().size());
        assertFalse(SrmServiceUtils.isCodelistInList(CODELIST_9_V1, family.getCodelists()));
    }

    @Test
    public void testUpdateCodelistRemoveFromVariable() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);

        // Check variable members
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), codelistVersion.getVariable().getNameableArtefact().getUrn());
        assertEquals(2, variable.getCodelists().size());

        // Update codelist (remove from variable)
        codelistVersion.setVariable(null);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(false);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertNull(codelistVersionUpdated.getVariable());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check variable members
        variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variable.getNameableArtefact().getUrn());
        assertEquals(1, variable.getCodelists().size());
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

    @Override
    @Test
    public void testRetrieveCodelistByUrn() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V1;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, codelistVersion.getMaintainableArtefact().getUrn());
        assertEquals(AccessTypeEnum.PUBLIC, codelistVersion.getAccessType());
        assertEquals(Boolean.FALSE, codelistVersion.getIsRecommended());
        assertEqualsInternationalString(codelistVersion.getShortName(), "es", "Nombre corto 1-1", "en", "Short name 1-1");
        assertEquals(CODELIST_FAMILY_2, codelistVersion.getFamily().getNameableArtefact().getUrn());
        assertEquals(VARIABLE_6, codelistVersion.getVariable().getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V1_ORDER_VISUALISATION_01, codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());

        assertEquals(CODELIST_2_V1, codelistVersion.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        assertEquals(0, codelistVersion.getReplaceToCodelists().size());

        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", codelistVersion.getLifeCycleMetadata().getProductionValidationUser());
        assertEqualsDate("2011-01-02 02:02:03", codelistVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", codelistVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        assertEqualsDate("2011-01-03 03:02:03", codelistVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", codelistVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(codelistVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
            assertEquals(3, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_ACCESS_TYPE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION}, e.getExceptionItems().get(2));
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

    @Override
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

    @Override
    @Test
    public void testDeleteCodelist() throws Exception {
        String urn = CODELIST_2_V1;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        String urnReplaceTo = codelistVersion.getReplaceToCodelists().get(0).getMaintainableArtefact().getUrn();

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

        // Check replace to is not delete
        CodelistVersionMetamac codelistVersionReplaceTo = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnReplaceTo);
        assertNull(codelistVersionReplaceTo.getReplacedByCodelist());
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
        assertEquals(VARIABLE_6, codelist6v1.getVariable().getNameableArtefact().getUrn());

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
        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_6);
        assertFalse(SrmServiceUtils.isCodelistInList(CODELIST_6_V1, variable.getCodelists()));
    }

    @Override
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
        CodelistVersionMetamac codelistVersionNewVersion = codesService.versioningCodelist(getServiceContextAdministrador(), urn, Boolean.TRUE, VersionTypeEnum.MAJOR);

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

        CodelistVersionMetamac codelistVersionNewVersion = codesService.versioningCodelist(getServiceContextAdministrador(), urnToCopy, Boolean.TRUE, VersionTypeEnum.MAJOR);

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
    public void testVersioningCodelistNotCopyCodes() throws Exception {
        String urn = CODELIST_3_V1;

        CodelistVersionMetamac codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        CodelistVersionMetamac codelistVersionNewVersion = codesService.versioningCodelist(getServiceContextAdministrador(), urn, Boolean.FALSE, VersionTypeEnum.MAJOR);

        // Validate
        // New version
        {
            codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEqualsCodelistWithoutLifeCycleMetadata(codelistVersionToCopy, codelistVersionNewVersion);

            // Codes
            assertEquals(0, codelistVersionNewVersion.getItems().size());
            assertEquals(0, codelistVersionNewVersion.getItemsFirstLevel().size());
        }

        // Copied version
        {
            codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", codelistVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(5, codelistVersionToCopy.getItems().size());
        }
    }

    @Test
    public void testVersioningCodelistErrorAlreadyExistsDraft() throws Exception {
        String urn = CODELIST_1_V1;

        try {
            codesService.versioningCodelist(getServiceContextAdministrador(), urn, Boolean.TRUE, VersionTypeEnum.MAJOR);
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
            codesService.versioningCodelist(getServiceContextAdministrador(), urn, Boolean.TRUE, VersionTypeEnum.MAJOR);
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

    @Override
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

    @Override
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
        assertEquals(10, codelistVersion.getItems().size());
        assertEquals(CODELIST_1_V2_CODE_1, codelistVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_2, codelistVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_3, codelistVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_4, codelistVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
        assertEquals(codeRetrieved.getNameableArtefact().getUrn(), codelistVersion.getItemsFirstLevel().get(4).getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCodeWithVariableElement() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setParent(null);
        code.setShortName(null);
        code.setVariableElement(codesService.retrieveVariableElementByUrn(ctx, VARIABLE_2_VARIABLE_ELEMENT_1));

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);
        String urn = codeCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CodeMetamac codeRetrieved = codesService.retrieveCodeByUrn(ctx, urn);
        assertEqualsCode(code, codeRetrieved);
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
        assertEquals(10, codelistVersion.getItems().size());

        assertEquals(CODELIST_1_V2_CODE_1, codelistVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
        assertEquals(codeRetrieved.getNameableArtefact().getUrn(), codelistVersion.getItemsFirstLevel().get(0).getChildren().get(0).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_2, codelistVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_3, codelistVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V2_CODE_4, codelistVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCodeIncorrectMetadata() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setShortName(BaseDoMocks.mockInternationalString()); // must be null when variable element is not null
        code.setVariableElement(codesService.retrieveVariableElementByUrn(ctx, VARIABLE_2_VARIABLE_ELEMENT_1));
        String codelistUrn = CODELIST_1_V2;

        // Create
        try {
            codesService.createCode(ctx, codelistUrn, code);
            fail("incorrect metadata");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, 1, new String[]{ServiceExceptionParameters.CODE_SHORT_NAME}, e.getExceptionItems().get(0));
        }
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

    @Override
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

    @Override
    @Test
    public void testRetrieveCodeByUrn() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V2_CODE_1;
        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, code.getNameableArtefact().getUrn());
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getNameableArtefact().getUrn());
        assertEquals(null, code.getShortName());
    }

    @Test
    public void testRetrieveCodeByUrnWithoutVariableElement() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V2_CODE_2;
        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, code.getNameableArtefact().getUrn());
        assertNull(code.getVariableElement());
        assertEqualsInternationalString(code.getShortName(), "es", "nombre corto code2", "en", "short name code2");
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

    @Override
    @Test
    public void testDeleteCode() throws Exception {
        String urn = CODELIST_1_V2_CODE_3;
        String codelistUrn = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(4, codelistVersion.getItemsFirstLevel().size());
        assertEquals(9, codelistVersion.getItems().size());

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
        assertEquals(8, codelistVersion.getItems().size());
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2);
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
        assertEquals(9, codelistVersion.getItems().size());

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
        assertEquals(7, codelistVersion.getItems().size());
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
        assertListItemsContainsItem(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2);
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

    @Override
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
            assertEquals(2, code.getChildren().size());
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
            {
                // Code 02 02
                CodeMetamac codeChild = (CodeMetamac) code.getChildren().get(1);
                assertEquals(CODELIST_1_V2_CODE_2_2, codeChild.getNameableArtefact().getUrn());
                assertEquals(0, codeChild.getChildren().size());
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

    @Override
    @Test
    public void testFindCodesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).orderBy(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                    .orderBy(CodeProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(29, codesPagedResult.getTotalRows());
            assertEquals(29, codesPagedResult.getValues().size());
            assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

            int i = 0;
            assertEquals(CODELIST_1_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_1_V2_CODE_2_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
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
                assertEquals(9, codesPagedResult.getTotalRows());
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
                assertEquals(9, codesPagedResult.getTotalRows());
                assertEquals(3, codesPagedResult.getValues().size());
                assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_4, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_2_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_2_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(codesPagedResult.getValues().size(), i);
            }
            // Third page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
                PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(9, codesPagedResult.getTotalRows());
                assertEquals(3, codesPagedResult.getValues().size());
                assertTrue(codesPagedResult.getValues().get(0) instanceof CodeMetamac);

                int i = 0;
                assertEquals(CODELIST_1_V2_CODE_4_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_2_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CODELIST_1_V2_CODE_4_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(codesPagedResult.getValues().size(), i);
            }
        }
    }

    // ------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testRetrieveCodelistFamilyByUrn() throws Exception {
        String urn = CODELIST_FAMILY_1;
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), urn);

        assertEquals(urn, codelistFamily.getNameableArtefact().getUrn());
        assertEquals("CODELIST_FAMILY_01", codelistFamily.getNameableArtefact().getCode());
        assertEqualsInternationalString(codelistFamily.getNameableArtefact().getName(), "es", "familia-de-codelists-51", "pt", "codelist-family");
        assertEquals("codelist-family-01", codelistFamily.getUuid());
        assertEquals("user1", codelistFamily.getCreatedBy());
        assertEquals("user2", codelistFamily.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), codelistFamily.getVersion());
    }

    @Test
    public void testRetrieveCodelistFamilyByUrnErrorNotFound() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), urn);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveCodelistFamilyByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testCreateCodelistFamily() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        CodelistFamily codelistVersionCreated = codesService.createCodelistFamily(ctx, codelistFamily);
        assertNotNull(codelistVersionCreated.getNameableArtefact().getUrn());
        assertEquals(ctx.getUserId(), codelistVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), codelistVersionCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersionCreated.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), codelistVersionCreated.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersionCreated.getLastUpdated().toDate()));
        assertEqualsCodelistFamily(codelistFamily, codelistVersionCreated);
    }

    @Test
    public void testCreateCodelistFamilyErrorWrongCode() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        codelistFamily.getNameableArtefact().setCode(" 0 - invalid identifier");
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodelistFamilyErrorDuplicatedCode() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        codelistFamily.getNameableArtefact().setCode("CODELIST_FAMILY_01");
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodelistFamilyErrorRequiredMetadata() throws Exception {
        CodelistFamily codelistFamily = new CodelistFamily();
        codelistFamily.setNameableArtefact(new NameableArtefact());
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(1));
        }
    }

    @Override
    @Test
    public void testUpdateCodelistFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(ctx, CODELIST_FAMILY_1);
        codelistFamily.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(10));
        codelistFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        codelistFamily.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());

        CodelistFamily codelistFamilyUpdated = codesService.updateCodelistFamily(ctx, codelistFamily);

        assertEqualsCodelistFamily(codelistFamily, codelistFamilyUpdated);
    }

    @Test
    public void testUpdateCodelistFamilyErrorWrongCode() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.getNameableArtefact().setCode(" 0 - invalid identifier");
        codelistFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateCodelistFamilyErrorDuplicatedCode() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.getNameableArtefact().setCode("CODELIST_FAMILY_02");
        codelistFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_FAMILY_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateCodelistFamilyErrorRequiredMetadata() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.getNameableArtefact().setCode(null);
        codelistFamily.getNameableArtefact().setName(null);
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_IS_CODE_UPDATED}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(2));
        }
    }

    @Override
    @Test
    public void testFindCodelistFamiliesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).orderBy(CodelistFamilyProperties.nameableArtefact().code()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistFamily> result = codesService.findCodelistFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(2, result.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_FAMILY_1, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_FAMILY_2, result.getValues().get(i++).getNameableArtefact().getUrn());
        }
        // Find by urn
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).withProperty(CodelistFamilyProperties.nameableArtefact().urn()).like(CODELIST_FAMILY_1)
                    .orderBy(CodelistFamilyProperties.nameableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistFamily> result = codesService.findCodelistFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_FAMILY_1, result.getValues().get(i++).getNameableArtefact().getUrn());

        }
    }

    @Override
    @Test
    public void testDeleteCodelistFamily() throws Exception {
        codesService.deleteCodelistFamily(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        // Retrieve deleted family
        try {
            codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
            fail("codelist family already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{CODELIST_FAMILY_1}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted codelist family
        try {
            codesService.deleteCodelistFamily(getServiceContextAdministrador(), CODELIST_FAMILY_1);
            fail("codelist already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{CODELIST_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testDeleteCodelistFamilyWithCodelists() throws Exception {
        codesService.deleteCodelistFamily(getServiceContextAdministrador(), CODELIST_FAMILY_2);

        CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);
        assertNotNull(codelist);
        assertNull(codelist.getFamily());
    }

    @Override
    @Test
    public void testAddCodelistsToCodelistFamily() throws Exception {

        String codelistFamilyUrn = CODELIST_FAMILY_1;
        List<String> codelistUrns = new ArrayList<String>();
        {
            String codelistUrn = CODELIST_9_V1; // change family
            codelistUrns.add(codelistUrn);
            CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
            assertEquals(CODELIST_FAMILY_2, codelist.getFamily().getNameableArtefact().getUrn());
        }
        {
            String codelistUrn = CODELIST_2_V1; // add family
            codelistUrns.add(codelistUrn);
            CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
            assertNull(codelist.getFamily());
        }
        codesService.addCodelistsToCodelistFamily(getServiceContextAdministrador(), codelistUrns, codelistFamilyUrn);

        // Validation
        {
            CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1);
            assertEquals(codelistFamilyUrn, codelist.getFamily().getNameableArtefact().getUrn());
        }
        {
            CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_2_V1);
            assertEquals(codelistFamilyUrn, codelist.getFamily().getNameableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testRemoveCodelistFromCodelistFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String codelistFamily = CODELIST_FAMILY_2;
        String codelist = CODELIST_1_V2;

        CodelistFamily family = codesService.retrieveCodelistFamilyByUrn(ctx, codelistFamily);
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, codelist);
        assertEquals(family.getNameableArtefact().getUrn(), codelistVersion.getFamily().getNameableArtefact().getUrn());
        assertTrue(SrmServiceUtils.isCodelistInList(codelist, family.getCodelists()));

        codesService.removeCodelistFromCodelistFamily(ctx, codelist, codelistFamily);

        family = codesService.retrieveCodelistFamilyByUrn(ctx, codelistFamily);
        codelistVersion = codesService.retrieveCodelistByUrn(ctx, codelist);
        assertNull(codelistVersion.getFamily());
        assertFalse(SrmServiceUtils.isCodelistInList(codelist, family.getCodelists()));

        // Removing the codelist from the family again has no consequences
        codesService.removeCodelistFromCodelistFamily(ctx, codelist, codelistFamily);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testRetrieveVariableFamilyByUrn() throws Exception {
        String urn = VARIABLE_FAMILY_1;
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), urn);

        assertEquals(urn, variableFamily.getNameableArtefact().getUrn());
        assertEquals("VARIABLE_FAMILY_01", variableFamily.getNameableArtefact().getCode());
        assertEqualsInternationalString(variableFamily.getNameableArtefact().getName(), "es", "familia-de-variables-53", null, null);
        assertEquals("variable-family-01", variableFamily.getUuid());
        assertEquals("user1", variableFamily.getCreatedBy());
        assertEquals("user2", variableFamily.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variableFamily.getVersion());
    }

    @Test
    public void testRetrieveVariableFamilyByUrnErrorNotFound() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), urn);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveVariableFamilyByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testCreateVariableFamily() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        VariableFamily variableFamilyCreated = codesService.createVariableFamily(ctx, variableFamily);
        String urn = variableFamilyCreated.getNameableArtefact().getUrn();
        assertNotNull(urn);

        // Validate
        VariableFamily variableFamilyRetrieved = codesService.retrieveVariableFamilyByUrn(ctx, urn);

        assertEquals(ctx.getUserId(), variableFamilyRetrieved.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), variableFamilyRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableFamilyRetrieved.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), variableFamilyRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableFamilyRetrieved.getLastUpdated().toDate()));
        assertEqualsVariableFamily(variableFamily, variableFamilyRetrieved);
    }

    @Test
    public void testCreateVariableFamilyErrorWrongCode() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        variableFamily.getNameableArtefact().setCode(" 0 - invalid identifier");
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableFamilyErrorDuplicatedCode() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        variableFamily.getNameableArtefact().setCode("VARIABLE_FAMILY_01");
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableFamilyErrorRequiredMetadata() throws Exception {
        VariableFamily variableFamily = new VariableFamily();
        variableFamily.setNameableArtefact(new NameableArtefact());
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(1));
        }
    }

    @Override
    @Test
    public void testUpdateVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_1);
        variableFamily.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(10));
        variableFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        variableFamily.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());

        VariableFamily variableFamilyUpdated = codesService.updateVariableFamily(ctx, variableFamily);

        assertEqualsVariableFamily(variableFamily, variableFamilyUpdated);
    }

    @Test
    public void testUpdateVariableFamilyErrorWrongCode() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.getNameableArtefact().setCode(" 0 - invalid identifier");
        variableFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableFamilyErrorDuplicatedCode() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.getNameableArtefact().setCode("VARIABLE_FAMILY_02");
        variableFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_FAMILY_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableFamilyErrorRequiredMetadata() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.getNameableArtefact().setCode(null);
        variableFamily.getNameableArtefact().setIsCodeUpdated(null);
        variableFamily.getNameableArtefact().setName(null);
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_IS_CODE_UPDATED}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(2));
        }
    }

    @Override
    @Test
    public void testDeleteVariableFamily() throws Exception {
        codesService.deleteVariableFamily(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        // Retrieve deleted family
        try {
            codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
            fail("variable family already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{VARIABLE_FAMILY_1}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted variable family
        try {
            codesService.deleteVariableFamily(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{VARIABLE_FAMILY_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testDeleteVariableFamilyWithVariables() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableFamilyUrn = VARIABLE_FAMILY_3;

        VariableFamily family = codesService.retrieveVariableFamilyByUrn(ctx, variableFamilyUrn);
        assertEquals(4, family.getVariables().size());
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_1, family.getVariables()));
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_2, family.getVariables()));
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_3, family.getVariables()));
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_4, family.getVariables()));

        codesService.deleteVariableFamily(ctx, variableFamilyUrn);

        // Check that the variable family has been deleted
        try {
            codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), variableFamilyUrn);
            fail("variable family already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{variableFamilyUrn}, e.getExceptionItems().get(0));
        }
        try {
            codesService.deleteVariableFamily(getServiceContextAdministrador(), variableFamilyUrn);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{variableFamilyUrn}, e.getExceptionItems().get(0));
        }

        // Check that the associated variables has not been deleted
        Variable variable1 = codesService.retrieveVariableByUrn(ctx, VARIABLE_1);
        Variable variable2 = codesService.retrieveVariableByUrn(ctx, VARIABLE_2);
        Variable variable3 = codesService.retrieveVariableByUrn(ctx, VARIABLE_3);
        Variable variable4 = codesService.retrieveVariableByUrn(ctx, VARIABLE_4);
        assertFalse(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable1.getFamilies()));
        assertFalse(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable2.getFamilies()));
        assertFalse(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable3.getFamilies()));
        assertFalse(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable4.getFamilies()));
    }

    @Override
    @Test
    public void testFindVariableFamiliesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).orderBy(VariableFamilyProperties.nameableArtefact().code()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableFamily> result = codesService.findVariableFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(4, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_FAMILY_1, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_FAMILY_2, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_FAMILY_3, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_FAMILY_4, result.getValues().get(i++).getNameableArtefact().getUrn());
        }
        // Find by code
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).withProperty(VariableFamilyProperties.nameableArtefact().urn()).like(VARIABLE_FAMILY_1)
                    .orderBy(VariableFamilyProperties.nameableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableFamily> result = codesService.findVariableFamiliesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_FAMILY_1, result.getValues().get(i++).getNameableArtefact().getUrn());

        }
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateVariable() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_1));
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_2));
        // Replace to
        Variable variableReplaced1 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        Variable variableReplaced2 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_3);
        variable.addReplaceToVariable(variableReplaced1);
        variable.addReplaceToVariable(variableReplaced2);

        // Create
        Variable variableCreated = codesService.createVariable(ctx, variable);
        String urn = variableCreated.getNameableArtefact().getUrn();
        assertNotNull(urn);

        // Validate
        Variable variableRetrieved = codesService.retrieveVariableByUrn(ctx, urn);

        assertEquals(ctx.getUserId(), variableRetrieved.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), variableRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableRetrieved.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), variableRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableRetrieved.getLastUpdated().toDate()));

        // Check that the variable was created in both families
        assertTrue(SrmServiceUtils.isFamilyInList(VARIABLE_FAMILY_1, variableRetrieved.getFamilies()));
        assertTrue(SrmServiceUtils.isFamilyInList(VARIABLE_FAMILY_2, variableRetrieved.getFamilies()));
        VariableFamily family1 = codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_1);
        VariableFamily family2 = codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_2);
        assertTrue(SrmServiceUtils.isVariableInList(variableRetrieved.getNameableArtefact().getUrn(), family1.getVariables()));
        assertTrue(SrmServiceUtils.isVariableInList(variableRetrieved.getNameableArtefact().getUrn(), family2.getVariables()));

        assertEqualsVariable(variable, variableRetrieved);

        // Check replace metadata
        assertEquals(2, variableRetrieved.getReplaceToVariables().size());
        assertEquals(VARIABLE_1, variableRetrieved.getReplaceToVariables().get(0).getNameableArtefact().getUrn());
        assertEquals(VARIABLE_3, variableRetrieved.getReplaceToVariables().get(1).getNameableArtefact().getUrn());

        // Check replaced by metadata
        variableReplaced1 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        assertEquals(urn, variableReplaced1.getReplacedByVariable().getNameableArtefact().getUrn());
        variableReplaced2 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_3);
        assertEquals(urn, variableReplaced2.getReplacedByVariable().getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateVariableErrorWrongCode() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1));
        variable.getNameableArtefact().setCode(" 0 - invalid identifier");
        try {
            codesService.createVariable(getServiceContextAdministrador(), variable);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableErrorDuplicatedCode() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1));
        variable.getNameableArtefact().setCode("VARIABLE_01");
        try {
            codesService.createVariable(getServiceContextAdministrador(), variable);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableErrorIncorrectMetadata() throws Exception {
        Variable variable = new Variable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1));
        variable.setNameableArtefact(new NameableArtefact());
        variable.setValidFrom(null);
        variable.setValidTo(new DateTime());
        try {
            codesService.createVariable(getServiceContextAdministrador(), variable);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_SHORT_NAME}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_VALID_TO}, e.getExceptionItems().get(3));
        }
    }

    @Override
    @Test
    public void testUpdateVariable() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        variable.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(10));
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        variable.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());
        variable.setShortName(BaseDoMocks.mockInternationalString());
        variable.setValidFrom(MetamacMocks.mockDateTime());
        variable.setValidTo(null);

        Variable variableUpdated = codesService.updateVariable(getServiceContextAdministrador(), variable);

        assertEqualsVariable(variable, variableUpdated);
    }

    @Test
    public void testUpdateVariableChangingReplaceTo() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String urn = VARIABLE_3;
        Variable variable = codesService.retrieveVariableByUrn(ctx, urn);
        assertEquals(2, variable.getReplaceToVariables().size());
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_1, variable.getReplaceToVariables()));
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_2, variable.getReplaceToVariables()));

        // Replace to
        variable.removeAllReplaceToVariables();
        variable.addReplaceToVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));
        variable.addReplaceToVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_4));
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codesService.updateVariable(ctx, variable);

        // Check replaced by metadata
        Variable variableReplaced1 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        assertEquals(urn, variableReplaced1.getReplacedByVariable().getNameableArtefact().getUrn());
        Variable variableReplaced2 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_4);
        assertEquals(urn, variableReplaced2.getReplacedByVariable().getNameableArtefact().getUrn());
        Variable variableNotReplaced3 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_2);
        assertEquals(null, variableNotReplaced3.getReplacedByVariable());
    }
    @Test
    public void testUpdateVariableErrorWrongCode() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        variable.getNameableArtefact().setCode(" 0 - invalid identifier");
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableErrorDuplicatedCode() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        variable.getNameableArtefact().setCode("VARIABLE_02");
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableErrorIncorrectMetadata() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        variable.removeAllFamilies();
        variable.getNameableArtefact().setCode(null);
        variable.getNameableArtefact().setIsCodeUpdated(null);
        variable.getNameableArtefact().setName(null);
        variable.setShortName(null);
        variable.setValidFrom(null);
        variable.setValidTo(new DateTime());
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(6, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_FAMILY}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_IS_CODE_UPDATED}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(3));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_SHORT_NAME}, e.getExceptionItems().get(4));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_VALID_TO}, e.getExceptionItems().get(5));
        }
    }

    @Override
    @Test
    public void testRetrieveVariableByUrn() throws Exception {
        String urn = VARIABLE_3;
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), urn);

        assertEquals(urn, variable.getNameableArtefact().getUrn());
        assertEquals("VARIABLE_03", variable.getNameableArtefact().getCode());
        assertEqualsInternationalString(variable.getNameableArtefact().getName(), "es", "variable--59", null, null);
        assertEqualsInternationalString(variable.getShortName(), "es", "variable-65", null, null);
        assertEqualsDate(new DateTime(2011, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variable.getValidFrom());
        assertEqualsDate(new DateTime(2012, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variable.getValidTo());

        assertEquals(VARIABLE_4, variable.getReplacedByVariable().getNameableArtefact().getUrn());
        assertEquals(2, variable.getReplaceToVariables().size());
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_1, variable.getReplaceToVariables()));
        assertTrue(SrmServiceUtils.isVariableInList(VARIABLE_2, variable.getReplaceToVariables()));

        assertEquals("variable-03", variable.getUuid());
        assertEquals("user1", variable.getCreatedBy());
        assertEquals("user2", variable.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variable.getVersion());
    }
    @Test
    public void testRetrieveVariableByUrnErrorNotFound() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.retrieveVariableByUrn(getServiceContextAdministrador(), urn);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveVariableByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            codesService.retrieveVariableByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testFindVariablesByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).orderBy(VariableProperties.nameableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<Variable> result = codesService.findVariablesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(6, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_2, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_3, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_4, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_5, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_6, result.getValues().get(i++).getNameableArtefact().getUrn());
        }
        // Find by urn
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).withProperty(VariableProperties.nameableArtefact().urn()).like(VARIABLE_1)
                    .orderBy(VariableProperties.nameableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<Variable> result = codesService.findVariablesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testDeleteVariable() throws Exception {

        String variableUrn = VARIABLE_1;
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variableUrn);

        assertEquals(2, variable.getFamilies().size());
        assertTrue(SrmServiceUtils.isFamilyInList(VARIABLE_FAMILY_2, variable.getFamilies()));
        assertTrue(SrmServiceUtils.isFamilyInList(VARIABLE_FAMILY_3, variable.getFamilies()));

        codesService.deleteVariable(getServiceContextAdministrador(), variableUrn);

        // Retrieve deleted variable
        try {
            codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variableUrn);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{variableUrn}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted variable
        try {
            codesService.deleteVariable(getServiceContextAdministrador(), variableUrn);
            fail("variable already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{variableUrn}, e.getExceptionItems().get(0));
        }

        // Check that the families has not been deleted
        VariableFamily family1 = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_2);
        assertFalse(SrmServiceUtils.isVariableInList(variableUrn, family1.getVariables()));
        VariableFamily family2 = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_3);
        assertFalse(SrmServiceUtils.isVariableInList(variableUrn, family2.getVariables()));
    }

    @Test
    public void testDeleteVariableErrorWithCodelists() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableUrn = VARIABLE_6;
        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_6);
        assertEquals(2, variable.getCodelists().size());
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_6_V1, variable.getCodelists()));
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_1_V1, variable.getCodelists()));

        // Delete variable
        try {
            codesService.deleteVariable(ctx, variableUrn);
            fail("variable can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_WITH_RELATIONS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(variableUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertTrue(CODELIST_6_V1.equals(e.getExceptionItems().get(0).getMessageParameters()[1]) || CODELIST_1_V1.equals(e.getExceptionItems().get(0).getMessageParameters()[1]));
        }

        // Check that the codelists have not been deleted
        codesService.retrieveCodelistByUrn(ctx, CODELIST_6_V1);
    }

    @Test
    public void testDeleteVariableErrorWithConcepts() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableUrn = VARIABLE_3;
        Variable variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertEquals(1, variable.getConcepts().size());

        // Delete variable
        try {
            codesService.deleteVariable(ctx, variableUrn);
            fail("variable can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_WITH_RELATIONS, 2, new String[]{variableUrn, CONCEPT_SCHEME_1_V1_CONCEPT_1}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testAddVariablesToVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableUrn = VARIABLE_1;
        String variableFamilyUrn = VARIABLE_FAMILY_1;

        VariableFamily family = codesService.retrieveVariableFamilyByUrn(ctx, variableFamilyUrn);
        Variable variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertFalse(SrmServiceUtils.isVariableInList(variableUrn, family.getVariables()));
        assertFalse(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable.getFamilies()));

        List<String> variablesUrn = Arrays.asList(variableUrn);
        codesService.addVariablesToVariableFamily(ctx, variablesUrn, variableFamilyUrn);

        family = codesService.retrieveVariableFamilyByUrn(ctx, variableFamilyUrn);
        variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertTrue(SrmServiceUtils.isVariableInList(variableUrn, family.getVariables()));
        assertTrue(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable.getFamilies()));

        // Adding the variable to the family again has no consequences
        codesService.addVariablesToVariableFamily(ctx, variablesUrn, variableFamilyUrn);
    }

    @Override
    @Test
    public void testRemoveVariableFromVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableUrn = VARIABLE_3;
        String variableFamilyUrn = VARIABLE_FAMILY_3;

        VariableFamily family = codesService.retrieveVariableFamilyByUrn(ctx, variableFamilyUrn);
        Variable variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertTrue(SrmServiceUtils.isVariableInList(variableUrn, family.getVariables()));
        assertTrue(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable.getFamilies()));
        assertTrue(SrmServiceUtils.isFamilyInList(VARIABLE_FAMILY_2, variable.getFamilies()));

        codesService.removeVariableFromVariableFamily(ctx, variableUrn, variableFamilyUrn);

        family = codesService.retrieveVariableFamilyByUrn(ctx, variableFamilyUrn);
        variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertFalse(SrmServiceUtils.isVariableInList(variableUrn, family.getVariables()));
        assertFalse(SrmServiceUtils.isFamilyInList(variableFamilyUrn, variable.getFamilies()));
        assertTrue(SrmServiceUtils.isFamilyInList(VARIABLE_FAMILY_2, variable.getFamilies()));

        // Removing the variable from the family again has no consequences
        codesService.removeVariableFromVariableFamily(ctx, variableUrn, variableFamilyUrn);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testCreateVariableElement() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_1);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);

        // Create
        VariableElement variableElementCreated = codesService.createVariableElement(ctx, variableElement);
        String urn = variableElementCreated.getNameableArtefact().getUrn();

        // Validate
        assertNotNull(urn);
        VariableElement variableElementRetrieved = codesService.retrieveVariableElementByUrn(ctx, urn);

        assertEquals(ctx.getUserId(), variableElementRetrieved.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), variableElementRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableElementRetrieved.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), variableElementRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableElementRetrieved.getLastUpdated().toDate()));

        // Check that the variableElement was added to variable
        variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_1);
        assertTrue(SrmServiceUtils.isVariableElementInList(urn, variable.getVariableElements()));

        assertEqualsVariableElement(variableElement, variableElementRetrieved);
    }

    @Test
    public void testCreateVariableElementErrorWrongCode() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.getNameableArtefact().setCode(" 0 - invalid identifier");
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableElementErrorDuplicatedCode() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_2);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.getNameableArtefact().setCode("VARIABLE_ELEMENT_01");
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_2_VARIABLE_ELEMENT_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableElementErrorIncorrectMetadata() throws Exception {
        VariableElement variableElement = new VariableElement();
        variableElement.setVariable(null);
        variableElement.setNameableArtefact(new NameableArtefact());
        variableElement.setValidFrom(null);
        variableElement.setValidTo(new DateTime());
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME}, e.getExceptionItems().get(3));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VALID_TO}, e.getExceptionItems().get(4));
        }
    }

    @Override
    @Test
    public void testUpdateVariableElement() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        variableElement.getNameableArtefact().setCode("code-" + MetamacMocks.mockString(10));
        variableElement.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        variableElement.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());
        variableElement.setShortName(BaseDoMocks.mockInternationalString());
        variableElement.setValidFrom(MetamacMocks.mockDateTime());
        variableElement.setValidTo(null);
        assertEquals(VARIABLE_2, variableElement.getVariable().getNameableArtefact().getUrn());
        variableElement.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));

        VariableElement variableElementUpdated = codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);

        assertEqualsVariableElement(variableElement, variableElementUpdated);
    }

    @Test
    public void testUpdateVariableElementErrorWrongCode() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        variableElement.getNameableArtefact().setCode(" 0 - invalid identifier");
        variableElement.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableElementErrorDuplicatedCode() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        variableElement.getNameableArtefact().setCode("VARIABLE_ELEMENT_02");
        variableElement.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_2_VARIABLE_ELEMENT_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableElementErrorIncorrectMetadata() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        variableElement.setVariable(null);
        variableElement.getNameableArtefact().setCode(null);
        variableElement.getNameableArtefact().setIsCodeUpdated(null);
        variableElement.getNameableArtefact().setName(null);
        variableElement.setShortName(null);
        variableElement.setValidFrom(null);
        variableElement.setValidTo(new DateTime());
        try {
            codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(6, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_IS_CODE_UPDATED}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(3));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME}, e.getExceptionItems().get(4));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VALID_TO}, e.getExceptionItems().get(5));
        }
    }

    @Override
    @Test
    public void testRetrieveVariableElementByUrn() throws Exception {
        String urn = VARIABLE_2_VARIABLE_ELEMENT_1;
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);

        assertEquals(urn, variableElement.getNameableArtefact().getUrn());
        assertEquals("VARIABLE_ELEMENT_01", variableElement.getNameableArtefact().getCode());
        assertEqualsInternationalString(variableElement.getNameableArtefact().getName(), "es", "Nombre 2-1", "en", "Name 2-1");
        assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 2-1", "en", "Short name 2-1");
        assertEqualsDate(new DateTime(2011, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variableElement.getValidFrom());
        assertEqualsDate(new DateTime(2012, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variableElement.getValidTo());
        assertEquals(VARIABLE_2, variableElement.getVariable().getNameableArtefact().getUrn());

        // TODO replaceTo, replacedBy

        assertEquals("variable-element-21", variableElement.getUuid());
        assertEquals("user1", variableElement.getCreatedBy());
        assertEquals("user2", variableElement.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variableElement.getVersion());
    }

    @Test
    public void testRetrieveVariableElementByUrnErrorNotFound() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testRetrieveVariableElementByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.PARAMETER_REQUIRED, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testFindVariableElementsByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).orderBy(VariableElementProperties.nameableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableElement> result = codesService.findVariableElementsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(3, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, result.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        }
        // Find by urn
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).withProperty(VariableElementProperties.nameableArtefact().urn())
                    .like(VARIABLE_2_VARIABLE_ELEMENT_1).orderBy(VariableElementProperties.nameableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableElement> result = codesService.findVariableElementsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testDeleteVariableElement() throws Exception {

        String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_1;
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), variableElementUrn);
        String variableUrn = variableElement.getVariable().getNameableArtefact().getUrn();

        codesService.deleteVariableElement(getServiceContextAdministrador(), variableElementUrn);

        // Retrieve deleted variableElement
        try {
            codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), variableElementUrn);
            fail("variableElement already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{variableElementUrn}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted variableElement
        try {
            codesService.deleteVariableElement(getServiceContextAdministrador(), variableElementUrn);
            fail("variableElement already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{variableElementUrn}, e.getExceptionItems().get(0));
        }

        // Check that the variable has not been deleted
        codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variableUrn);
    }

    @Test
    public void testDeleteVariableElementErrorWithCodes() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_2;
        String codeUrn = CODELIST_1_V2_CODE_1;
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(ctx, variableElementUrn);
        assertEquals(1, variableElement.getCodes().size());
        assertEquals(codeUrn, variableElement.getCodes().get(0).getNameableArtefact().getUrn());

        // Delete variableElement
        try {
            codesService.deleteVariableElement(ctx, variableElementUrn);
            fail("variableElement can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_WITH_RELATIONS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(variableElementUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(codeUrn, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }

        // Check that the code has not been deleted
        codesService.retrieveCodeByUrn(ctx, codeUrn);
    }

    @Override
    @Test
    public void testAddVariableElementsToVariable() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        String variable2variableElement2Urn = VARIABLE_2_VARIABLE_ELEMENT_2;
        String variable5variableElement1Urn = VARIABLE_5_VARIABLE_ELEMENT_1;
        String variableUrn = VARIABLE_1;

        Variable variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertFalse(SrmServiceUtils.isVariableElementInList(variable2variableElement2Urn, variable.getVariableElements()));
        assertFalse(SrmServiceUtils.isVariableElementInList(variable5variableElement1Urn, variable.getVariableElements()));

        VariableElement variable2variableElement2 = codesService.retrieveVariableElementByUrn(ctx, variable2variableElement2Urn);
        assertEquals(VARIABLE_2, variable2variableElement2.getVariable().getNameableArtefact().getUrn());
        VariableElement variable5variableElement1 = codesService.retrieveVariableElementByUrn(ctx, variable5variableElement1Urn);
        assertEquals(VARIABLE_5, variable5variableElement1.getVariable().getNameableArtefact().getUrn());

        // Update variable of variable elements
        List<String> variableElementsUrn = Arrays.asList(variable2variableElement2Urn, variable5variableElement1Urn);
        codesService.addVariableElementsToVariable(ctx, variableElementsUrn, variableUrn);

        // Validate
        variable = codesService.retrieveVariableByUrn(ctx, variableUrn);
        assertTrue(SrmServiceUtils.isVariableElementInList(variable2variableElement2Urn, variable.getVariableElements()));
        assertTrue(SrmServiceUtils.isVariableElementInList(variable5variableElement1Urn, variable.getVariableElements()));

        variable2variableElement2 = codesService.retrieveVariableElementByUrn(ctx, variable2variableElement2Urn);
        assertEquals(variableUrn, variable2variableElement2.getVariable().getNameableArtefact().getUrn());
        variable5variableElement1 = codesService.retrieveVariableElementByUrn(ctx, variable5variableElement1Urn);
        assertEquals(variableUrn, variable5variableElement1.getVariable().getNameableArtefact().getUrn());
    }

    // ------------------------------------------------------------------------------------
    // CODELIST VISUALISATIONS
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testRetrieveCodelistOrderVisualisationByUrn() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        String orderVisualisationUrn = CODELIST_1_V2_ORDER_VISUALISATION_01;
        CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), orderVisualisationUrn);

        // Validate
        assertNotNull(codelistOrderVisualisation);
        assertEquals("VISUALISATION01", codelistOrderVisualisation.getNameableArtefact().getCode());
        assertEquals(orderVisualisationUrn, codelistOrderVisualisation.getNameableArtefact().getUrn());
        assertEquals(codelistUrn, codelistOrderVisualisation.getCodelistVersion().getMaintainableArtefact().getUrn());
        assertEqualsInternationalString(codelistOrderVisualisation.getNameableArtefact().getName(), "es", "visualización - órdenes 1-2-1", "en", "order - visualisation 1-2-1");

        assertEquals("user1", codelistOrderVisualisation.getCreatedBy());
        assertEqualsDate("2011-01-01 01:02:03", codelistOrderVisualisation.getCreatedDate());
        assertEquals("user2", codelistOrderVisualisation.getLastUpdatedBy());
        assertEqualsDate("2011-01-22 01:02:03", codelistOrderVisualisation.getLastUpdated());

        // Codes
        assertEquals(9, codelistOrderVisualisation.getCodes().size());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(1), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
    }

    @Override
    @Test
    public void testCreateCodelistOrderVisualisation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        CodelistOrderVisualisation codelistOrderVisualisation = CodesMetamacDoMocks.mockCodelistOrderVisualisation();
        String codelistUrn = CODELIST_1_V2;

        // Create
        CodelistOrderVisualisation codelistOrderVisualisationCreated = codesService.createCodelistOrderVisualisation(ctx, codelistUrn, codelistOrderVisualisation);
        assertNotNull(codelistOrderVisualisationCreated.getId());
        assertNotNull(codelistOrderVisualisationCreated.getNameableArtefact().getUrn());
        assertEqualsCodelistOrderVisualisation(codelistOrderVisualisation, codelistOrderVisualisationCreated);

        // Validate codes
        assertNotNull(codelistOrderVisualisationCreated.getCodes());
        assertEquals(9, codelistOrderVisualisationCreated.getCodes().size());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(0), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(1), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), codelistOrderVisualisationCreated.getCodes());
        assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), codelistOrderVisualisationCreated.getCodes());
    }

    @Test
    public void testCreateCodelistOrderVisualisationErrorDuplicatedCode() throws Exception {

        CodelistOrderVisualisation codelistOrderVisualisation = CodesMetamacDoMocks.mockCodelistOrderVisualisation();
        codelistOrderVisualisation.getNameableArtefact().setCode("VISUALISATION01");
        String codelistUrn = CODELIST_1_V2;
        try {
            codesService.createCodelistOrderVisualisation(getServiceContextAdministrador(), codelistUrn, codelistOrderVisualisation);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_1_V2_ORDER_VISUALISATION_01}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testUpdateCodelistOrderVisualisation() throws Exception {
        CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
        codelistOrderVisualisation.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());
        codelistOrderVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Update
        CodelistOrderVisualisation codelistOrderVisualisationUpdated = codesService.updateCodelistOrderVisualisation(getServiceContextAdministrador(), codelistOrderVisualisation);

        // Validate
        assertEqualsCodelistOrderVisualisation(codelistOrderVisualisation, codelistOrderVisualisationUpdated);
    }

    @Test
    public void testUpdateCodelistOrderVisualisationErrorDuplicatedCode() throws Exception {
        CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
        codelistOrderVisualisation.getNameableArtefact().setCode("VISUALISATION02");
        codelistOrderVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateCodelistOrderVisualisation(getServiceContextAdministrador(), codelistOrderVisualisation);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_1_V2_ORDER_VISUALISATION_02}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodeTestingOrderVisualisation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setParent(null);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);

            // Validate codes
            assertEquals(10, codelistOrderVisualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(codeCreated.getNameableArtefact().getUrn(), Long.valueOf(4), codelistOrderVisualisation.getCodes());
        }
        // Visualisation 02
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);

            // Validate codes
            assertEquals(10, codelistOrderVisualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(1), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(2), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(codeCreated.getNameableArtefact().getUrn(), Long.valueOf(4), codelistOrderVisualisation.getCodes());
        }
    }

    @Test
    public void testCreateCodeSubCodeTestingOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        CodeMetamac codeParent = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
        code.setParent(codeParent);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);

            // Validate codes
            assertEquals(10, codelistOrderVisualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(0), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(1), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(codeCreated.getNameableArtefact().getUrn(), Long.valueOf(2), codelistOrderVisualisation.getCodes());
        }
        // Visualisation 02
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);

            // Validate codes
            assertEquals(10, codelistOrderVisualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), codelistOrderVisualisation.getCodes());
            assertContainsCodeOrderVisualisation(codeCreated.getNameableArtefact().getUrn(), Long.valueOf(2), codelistOrderVisualisation.getCodes());
        }
    }

    @Override
    @Test
    public void testDeleteCodelistOrderVisualisation() throws Exception {

        String urn = CODELIST_1_V2_ORDER_VISUALISATION_02;

        codesService.deleteCodelistOrderVisualisation(getServiceContextAdministrador(), urn);

        // Retrieve deleted visualisation
        try {
            codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), urn);
            fail("visualisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testDeleteCodelistOrderVisualisationAsDefault() throws Exception {

        String urn = CODELIST_1_V2_ORDER_VISUALISATION_01;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(urn, codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());

        codesService.deleteCodelistOrderVisualisation(getServiceContextAdministrador(), urn);

        // Check default is null
        codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertNull(codelistVersion.getDefaultOrderVisualisation());
    }

    @Override
    @Test
    public void testRetrieveCodelistOrderVisualisationsByCodelist() throws Exception {
        String codelistUrn = CODELIST_1_V2;

        List<CodelistOrderVisualisation> visualisations = codesService.retrieveCodelistOrderVisualisationsByCodelist(getServiceContextAdministrador(), codelistUrn);

        assertEquals(2, visualisations.size());
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_01, visualisations);
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_02, visualisations);
    }

    @Override
    @Test
    public void testUpdateCodeInOrderVisualisation() throws Exception {
        String codeUrn = CODELIST_1_V2_CODE_1;
        String visualisationUrn = CODELIST_1_V2_ORDER_VISUALISATION_01;

        codesService.updateCodeInOrderVisualisation(getServiceContextAdministrador(), codeUrn, visualisationUrn, Long.valueOf(2));

        // Validate visualisation
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(2), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(0), visualisation.getCodes());// changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(1), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }

        // Validate other visualisation does not change
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }
    }

    @Test
    public void testUpdateCodeSubcodeInOrderVisualisation() throws Exception {
        String codeUrn = CODELIST_1_V2_CODE_2_1;
        String visualisationUrn = CODELIST_1_V2_ORDER_VISUALISATION_01;

        codesService.updateCodeInOrderVisualisation(getServiceContextAdministrador(), codeUrn, visualisationUrn, Long.valueOf(1));

        // Validate visualisation
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }

        // Validate other visualisation does not change
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }
    }

    @Test
    public void testDeleteCodeCheckUpdateOrderVisualisation() throws Exception {
        String urn = CODELIST_1_V2_CODE_3;

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validation
        // Validate visualisation 01
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
            assertEquals(8, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(2), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }

        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(8, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(2), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }
    }

    @Test
    public void testDeleteCodeWithParentAndChildrenCheckUpdateOrderVisualisation() throws Exception {

        String urn = CODELIST_1_V2_CODE_2_1;

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validate visualisation 01
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
            assertEquals(7, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // changed
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }

        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(7, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // in same position, due to it is before deleted code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }
    }

    @Override
    public void testUpdateCodeParent() throws Exception {
        // in other tests 'testUpdateCodeParent*'
    }

    @Test
    public void testUpdateCodeParentSourceWithParentTargetWithoutParent() throws Exception {

        String codelistUrn = CODELIST_1_V2;
        String codeUrn = CODELIST_1_V2_CODE_2_1;
        String newParentUrn = null; // first level

        Code code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertNotNull(code.getParent());
        assertNull(code.getItemSchemeVersionFirstLevel());

        codesService.updateCodeParent(getServiceContextAdministrador(), codeUrn, newParentUrn);

        // Validate new parent
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertNull(code.getParent());
        assertEquals(code.getItemSchemeVersion().getMaintainableArtefact().getUrn(), code.getItemSchemeVersionFirstLevel().getMaintainableArtefact().getUrn());

        // Validate hierarchy
        CodelistVersion codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(5, codelistVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_3);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
        assertListItemsContainsItem(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2_1);

        // Validate visualisation 01
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(4), visualisation.getCodes()); // changed (change level, put at the end)
        }

        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(4), visualisation.getCodes()); // changed (change level, put at the end)
        }
    }

    @Test
    public void testUpdateCodeParentSourceWithParentTargetWithParent() throws Exception {

        String codelistUrn = CODELIST_1_V2;
        String codeUrn = CODELIST_1_V2_CODE_2_1;
        String newParentUrn = CODELIST_1_V2_CODE_4;

        Code code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertNotNull(code.getParent());
        assertNull(code.getItemSchemeVersionFirstLevel());

        codesService.updateCodeParent(getServiceContextAdministrador(), codeUrn, newParentUrn);

        // Validate new parent
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(newParentUrn, code.getParent().getNameableArtefact().getUrn());
        assertNull(code.getItemSchemeVersionFirstLevel());

        // Validate hierarchy
        CodelistVersion codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(4, codelistVersion.getItemsFirstLevel().size());
        // new parent
        Item newParent = BaseServiceTestUtils.getItemFromList(codelistVersion.getItems(), newParentUrn);
        assertEquals(2, newParent.getChildren().size());
        assertListItemsContainsItem(newParent.getChildren(), codeUrn);

        // Validate visualisation 01
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes()); // changed (change level, put at the end)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }

        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(1), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(2), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(3), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes()); // changed (change level, put at the end)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }
    }

    @Test
    public void testUpdateCodeParentSourceWithoutParentTargetWithoutParent() throws Exception {

        String codelistUrn = CODELIST_1_V2;
        String codeUrn = CODELIST_1_V2_CODE_1;
        String newParentUrn = CODELIST_1_V2_CODE_2;

        Code code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertNull(code.getParent());
        assertNotNull(code.getItemSchemeVersionFirstLevel());

        codesService.updateCodeParent(getServiceContextAdministrador(), codeUrn, newParentUrn);

        // Validate new parent
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(newParentUrn, code.getParent().getNameableArtefact().getUrn());
        assertNull(code.getItemSchemeVersionFirstLevel());

        // Validate hierarchy
        CodelistVersion codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        Item newParent = BaseServiceTestUtils.getItemFromList(codelistVersion.getItems(), newParentUrn);
        assertEquals(3, newParent.getChildren().size());
        assertListItemsContainsItem(newParent.getChildren(), codeUrn);

        // Validate visualisation 01
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(0), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(0), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(1), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(2), visualisation.getCodes()); // changed (change level, put at the end)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(1), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(2), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }

        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            assertEquals(9, visualisation.getCodes().size());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2, Long.valueOf(1), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_3, Long.valueOf(0), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1, Long.valueOf(1), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_1_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_2_2, Long.valueOf(0), visualisation.getCodes()); // in same position, due to it is before moved code
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_1, Long.valueOf(2), visualisation.getCodes()); // changed (change level, put at the end)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4, Long.valueOf(2), visualisation.getCodes()); // changed (up)
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1, Long.valueOf(0), visualisation.getCodes());
            assertContainsCodeOrderVisualisation(CODELIST_1_V2_CODE_4_1_1, Long.valueOf(0), visualisation.getCodes());
        }
    }

    private void assertContainsCodelistOrderVisualisation(String codelistOrderVisualisationUrnExpected, List<CodelistOrderVisualisation> actuals) {
        for (CodelistOrderVisualisation actual : actuals) {
            if (actual.getNameableArtefact().getUrn().equals(codelistOrderVisualisationUrnExpected)) {
                return;
            }
        }
        fail("visualisation not found");
    }

    private void assertContainsCodeOrderVisualisation(String codeUrnExpected, Long orderExpected, List<CodeOrderVisualisation> actuals) {
        for (CodeOrderVisualisation actual : actuals) {
            if (actual.getCode().getNameableArtefact().getUrn().equals(codeUrnExpected)) {
                assertEquals(orderExpected, actual.getCodeIndex());
                return;
            }
        }
        fail("code not found in order");
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}

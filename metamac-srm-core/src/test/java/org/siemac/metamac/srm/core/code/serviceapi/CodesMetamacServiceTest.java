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
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCode;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelist;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistFamily;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistOpennessVisualisation;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistOrderVisualisation;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistWithoutLifeCycleMetadata;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariable;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariableElement;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsVariableFamily;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.TaskImportTsvInfo;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementResult;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.task.serviceapi.TasksMetamacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeProperties;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesDoMocks;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.srm.core.task.domain.Task;
import com.arte.statistic.sdmx.srm.core.task.domain.TaskResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TaskResultTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TaskStatusTypeEnum;

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
    private TasksMetamacService           tasksService;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeRepository;

    @Autowired
    private ItemRepository                itemRepository;

    @Autowired
    protected PlatformTransactionManager  transactionManager;

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
        String replaceTo1Urn = CODELIST_7_V1;
        String replaceTo2Urn = CODELIST_10_V1;
        CodelistVersionMetamac codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo1Urn);
        CodelistVersionMetamac codelistReplaced2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo2Urn);
        codelistVersion.getReplaceToCodelists().add(codelistReplaced1);
        codelistVersion.getReplaceToCodelists().add(codelistReplaced2);

        // Create
        CodelistVersionMetamac codelistVersionCreated = codesService.createCodelist(ctx, codelistVersion);
        String urn = codelistVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", codelistVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), codelistVersionCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CodelistVersionMetamac codelistVersionRetrieved = codesService.retrieveCodelistByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, codelistVersionRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(codelistVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(codelistVersionRetrieved.getDefaultOrderVisualisation());
        assertNull(codelistVersionRetrieved.getDefaultOpennessVisualisation());
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
        assertEquals(replaceTo1Urn, codelistVersionRetrieved.getReplaceToCodelists().get(0).getMaintainableArtefact().getUrn());
        assertEquals(replaceTo2Urn, codelistVersionRetrieved.getReplaceToCodelists().get(1).getMaintainableArtefact().getUrn());

        // Check replaced by metadata
        codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo1Urn);
        assertEquals(urn, codelistReplaced1.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        codelistReplaced2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo2Urn);
        assertEquals(urn, codelistReplaced2.getReplacedByCodelist().getMaintainableArtefact().getUrn());

        // Check alphabetical order is created
        assertEquals(1, codelistVersionRetrieved.getOrderVisualisations().size());
        CodelistOrderVisualisation alphabetical = codelistVersionRetrieved.getOrderVisualisations().get(0);
        assertEquals(SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE, alphabetical.getNameableArtefact().getCode());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=" + codelistVersion.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + ":"
                + codelistVersion.getMaintainableArtefact().getCode() + "(01.000)." + alphabetical.getNameableArtefact().getCode(), alphabetical.getNameableArtefact().getUrn());
        assertNotNull(alphabetical.getNameableArtefact().getName());

        // Check "all expanded" openness visualisation is created
        assertEquals(1, codelistVersionRetrieved.getOpennessVisualisations().size());
        CodelistOpennessVisualisation allExpanded = codelistVersionRetrieved.getOpennessVisualisations().get(0);
        assertEquals(SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE, allExpanded.getNameableArtefact().getCode());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=" + codelistVersion.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + ":"
                + codelistVersion.getMaintainableArtefact().getCode() + "(01.000)." + allExpanded.getNameableArtefact().getCode(), allExpanded.getNameableArtefact().getUrn());
        assertNotNull(allExpanded.getNameableArtefact().getName());

    }

    @Test
    public void testCreateCodelistWithFamily() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        codelistVersion.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));

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
        codelistVersion.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));

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
        codelistVersion.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));

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
    public void testCreateCodelistErrorReplaceToWrongProcStatus() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        codelistVersion.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));
        ServiceContext ctx = getServiceContextAdministrador();

        // Replace to
        CodelistVersionMetamac codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        codelistVersion.getReplaceToCodelists().add(codelistReplaced1);

        // Create
        try {
            codesService.createCodelist(ctx, codelistVersion);
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CODELIST_REPLACE_TO, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateCodelistErrorReplaceToAlreadyReplaced() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CodelistVersionMetamac codelistVersion = CodesMetamacDoMocks.mockCodelist(organisationMetamac);
        codelistVersion.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));
        ServiceContext ctx = getServiceContextAdministrador();

        // Replace to
        CodelistVersionMetamac codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_12_V1);
        codelistVersion.getReplaceToCodelists().add(codelistReplaced1);

        // Create
        try {
            codesService.createCodelist(ctx, codelistVersion);
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CODELIST_REPLACE_TO, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    public void testPreCreateCodelist() throws Exception {
        // TODO testPreCreateCodelist
    }

    @Override
    public void testPostCreateCodelist() throws Exception {
        // TODO testPostCreateCodelist
    }

    @Override
    @Test
    public void testUpdateCodelist() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, CODELIST_2_V1);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);

        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(ctx, codelistVersion);
        assertNotNull(codelistVersionUpdated);
        assertEquals("user1", codelistVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), codelistVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateCodelistChangingCode() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_3_V1);

        // save to force incorrect metadata
        codelistVersion.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DRAFT);
        codelistVersion.getMaintainableArtefact().setFinalLogic(false);
        itemSchemeRepository.save(codelistVersion);

        codelistVersion.setIsVariableUpdated(Boolean.FALSE);

        // Change code
        codelistVersion.getMaintainableArtefact().setCode("codeNew");
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:codeNew(01.000)", codelistVersionUpdated.getMaintainableArtefact().getUrn());
        String urnUpdated = codelistVersionUpdated.getMaintainableArtefact().getUrn();

        entityManager.clear(); // force update of codes urn
        codelistVersionUpdated = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnUpdated);

        // Assert URN of CodelistOrder
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:codeNew(01.000).ALPHABETICAL", codelistVersionUpdated.getOrderVisualisations().iterator().next()
                .getNameableArtefact().getUrn());

        // Assert URN of CodelistOpen
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:codeNew(01.000).ALL_EXPANDED", codelistVersionUpdated.getOpennessVisualisations()
                .iterator().next().getNameableArtefact().getUrn());

    }

    @Test
    public void testUpdateCodelistChangingReplaceTo() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String urn = CODELIST_2_V1;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        assertNull(codelistVersion.getReplacedByCodelist());

        String replaceTo1Old = CODELIST_12_V1;
        assertEquals(1, codelistVersion.getReplaceToCodelists().size());
        assertEquals(replaceTo1Old, codelistVersion.getReplaceToCodelists().get(0).getMaintainableArtefact().getUrn());

        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);

        // New 'Replace to'
        String replaceTo1New = CODELIST_7_V1;
        String replaceTo2New = CODELIST_10_V1;
        codelistVersion.getReplaceToCodelists().add(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo1New));
        codelistVersion.getReplaceToCodelists().add(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo2New));
        codesService.updateCodelist(ctx, codelistVersion);

        // Check replaced by metadata
        CodelistVersionMetamac codelistReplaced1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo1New);
        assertEquals(urn, codelistReplaced1.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        CodelistVersionMetamac codelistReplaced2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo2New);
        assertEquals(urn, codelistReplaced2.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        CodelistVersionMetamac codelistNotReplaced3 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), replaceTo1Old);
        assertEquals(urn, codelistNotReplaced3.getReplacedByCodelist().getMaintainableArtefact().getUrn());
    }

    @Test
    public void testUpdateCodelistChangingDefaultOrderVisualisation() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(CODELIST_1_V2_ORDER_VISUALISATION_02, codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());

        String visualisationUrnNew = CODELIST_1_V2_ORDER_VISUALISATION_03;
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
        codelistVersion.setDefaultOrderVisualisation(codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrnNew));
        codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);

        CodelistVersionMetamac codelistVersionRetrieved = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(visualisationUrnNew, codelistVersionRetrieved.getDefaultOrderVisualisation().getNameableArtefact().getUrn());
    }

    @Test
    public void testUpdateCodelistChangingDefaultOpennessVisualisation() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(CODELIST_1_V2_OPENNESS_VISUALISATION_02, codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn());

        String visualisationUrnNew = CODELIST_1_V2_OPENNESS_VISUALISATION_03;
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
        codelistVersion.setDefaultOpennessVisualisation(codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), visualisationUrnNew));
        codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);

        CodelistVersionMetamac codelistVersionRetrieved = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(visualisationUrnNew, codelistVersionRetrieved.getDefaultOpennessVisualisation().getNameableArtefact().getUrn());
    }

    @Test
    public void testUpdateCodelistAddFamily() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);

        // Check the family has no codelists
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        assertEquals(0, codelistFamily.getCodelists().size());

        // Associate the codelist to the family
        codelistVersion.setFamily(codelistFamily);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
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
        String codelistUrn = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(VARIABLE_2, codelistVersion.getVariable().getNameableArtefact().getUrn());

        // Check actual element variables of codes
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1).getVariableElement().getIdentifiableArtefact().getUrn());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2).getVariableElement());
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1).getVariableElement().getIdentifiableArtefact().getUrn());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2).getVariableElement());
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_3).getVariableElement().getIdentifiableArtefact().getUrn());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1).getVariableElement());
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1).getVariableElement().getIdentifiableArtefact().getUrn());

        // Check the variable before
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_5);
        assertEquals(3, variable.getCodelists().size());
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_7_V2, variable.getCodelists()));
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_8_V1, variable.getCodelists()));
        assertTrue(SrmServiceUtils.isCodelistInList(CODELIST_9_V1, variable.getCodelists()));

        // Associate the codelist to the variable
        codelistVersion.setVariable(variable);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.TRUE);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertEqualsVariable(variable, codelistVersionUpdated.getVariable());

        entityManager.clear();
        codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);

        // Check element variables are removed from codes
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_3).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1).getVariableElement());
        assertNull(getCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1).getVariableElement());

        // Check variable element is not removed
        codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_2);
        codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_3);

        // Check variable has more codelists (and it's the one we have added previously)
        entityManager.clear(); // Clear hibernate cache to check that the variable has been updated
        variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variable.getNameableArtefact().getUrn());
        assertEquals(4, variable.getCodelists().size());
        assertTrue(SrmServiceUtils.isCodelistInList(codelistVersion.getMaintainableArtefact().getUrn(), variable.getCodelists()));
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
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
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
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
        CodelistVersionMetamac codelistVersionUpdated = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
        assertNull(codelistVersionUpdated.getFamily());

        entityManager.clear(); // Clear hibernate cache to check that the family has been updated

        // Check family members
        family = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_2);
        assertEquals(2, family.getCodelists().size());
        assertFalse(SrmServiceUtils.isCodelistInList(CODELIST_9_V1, family.getCodelists()));
    }

    @Test
    public void testUpdateCodelistWithAditionalMetadata() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, CODELIST_1_V2);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
        codelistVersion.setAccessType(AccessTypeEnum.PUBLIC);

        codelistVersion.setShortName(com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks.mockInternationalString());
        codelistVersion.setDescriptionSource(com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseDoMocks.mockInternationalString());
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
                codelistVersion.setIsVariableUpdated(Boolean.FALSE);
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
    public void testUpdateCodelistErrorMetadataRequired() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        codelistVersion.setVariable(null);
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
        try {
            codelistVersion = codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);
            fail("wrong metadata");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CODELIST_VARIABLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateCodelistErrorChangeCodeInCodelistWithVersionAlreadyPublished() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        codelistVersion.getMaintainableArtefact().setCode("newCode");
        codelistVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);

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
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);

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
        assertEqualsInternationalString(codelistVersion.getDescriptionSource(), "es", "descripción fuente codelist11-v1", null, null);

        assertEquals(CODELIST_FAMILY_2, codelistVersion.getFamily().getNameableArtefact().getUrn());
        assertEquals(VARIABLE_6, codelistVersion.getVariable().getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V1_ORDER_VISUALISATION_01_ALPHABETICAL, codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());
        assertEquals(CODELIST_1_V1_OPENNESS_VISUALISATION_01_ALL_EXPANDED, codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn());

        assertNull(codelistVersion.getReplacedByCodelist());
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

    @Test
    public void testRetrieveCodelistByUrnToCheckReplaceToAndReplacedByMetadatas() throws Exception {
        // Retrieve
        String urn12 = CODELIST_12_V1;
        String urn13 = CODELIST_2_V1;
        CodelistVersionMetamac codelistVersion12v1 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn12);
        CodelistVersionMetamac codelistVersion1v2 = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn13);

        assertEquals(codelistVersion1v2.getMaintainableArtefact().getUrn(), codelistVersion12v1.getReplacedByCodelist().getMaintainableArtefact().getUrn());
        assertEquals(0, codelistVersion12v1.getReplaceToCodelists().size());

        assertNull(codelistVersion1v2.getReplacedByCodelist());
        assertEquals(1, codelistVersion1v2.getReplaceToCodelists().size());
        assertEquals(codelistVersion12v1.getMaintainableArtefact().getUrn(), codelistVersion1v2.getReplaceToCodelists().get(0).getMaintainableArtefact().getUrn());
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
            assertEquals(18, codelistVersionPagedResult.getTotalRows());
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
            assertEquals(CODELIST_14_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
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
            assertEquals(14, codelistVersionPagedResult.getTotalRows());
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
            assertEquals(CODELIST_14_V1, codelistVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testFindCodelistsByConditionCanReplaceTo() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        // New codelist
        {
            String codelistUrn = null;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.id()).ascending().distinctRoot()
                    .build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistVersionMetamac> codelistsPagedResult = codesService.findCodelistsByConditionCanReplaceTo(ctx, codelistUrn, conditions, pagingParameter);

            // Validate
            assertEquals(7, codelistsPagedResult.getTotalRows());
            assertEquals(7, codelistsPagedResult.getValues().size());

            int i = 0;
            assertEquals(CODELIST_1_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_3_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V2, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V2, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            // assertEquals(CODELIST_12_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // replacedBy filled
            assertEquals(CODELIST_13_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(codelistsPagedResult.getValues().size(), i);
        }

        // Existing codelist
        {
            String codelistUrn = CODELIST_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.id()).ascending().distinctRoot()
                    .build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistVersionMetamac> codelistsPagedResult = codesService.findCodelistsByConditionCanReplaceTo(ctx, codelistUrn, conditions, pagingParameter);

            // Validate
            assertEquals(6, codelistsPagedResult.getTotalRows());
            assertEquals(6, codelistsPagedResult.getValues().size());

            int i = 0;
            // assertEquals(CODELIST_1_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // same scheme
            assertEquals(CODELIST_3_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V2, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V2, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            // assertEquals(CODELIST_12_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // replacedBy filled
            assertEquals(CODELIST_13_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(codelistsPagedResult.getValues().size(), i);
        }

        // Existing codelist with replaceTo already filled
        {
            String codelistUrn = CODELIST_2_V1;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.id()).ascending().distinctRoot()
                    .build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CodelistVersionMetamac> codelistsPagedResult = codesService.findCodelistsByConditionCanReplaceTo(ctx, codelistUrn, conditions, pagingParameter);

            // Validate
            assertEquals(7, codelistsPagedResult.getTotalRows());
            assertEquals(7, codelistsPagedResult.getValues().size());

            int i = 0;
            assertEquals(CODELIST_1_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_3_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_7_V2, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CODELIST_10_V2, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            // assertEquals(CODELIST_12_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // replacedBy filled
            assertEquals(CODELIST_13_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(codelistsPagedResult.getValues().size(), i);
        }
    }

    @Test
    @Override
    public void testFindCodelistsByConditionWithCodesCanBeVariableElementGeographicalGranularity() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodelistVersionMetamac> codelistsPagedResult = codesService.findCodelistsByConditionWithCodesCanBeVariableElementGeographicalGranularity(ctx, null, pagingParameter);

        // Validate
        assertEquals(1, codelistsPagedResult.getTotalRows());
        assertEquals(1, codelistsPagedResult.getValues().size());

        int i = 0;
        assertEquals(CODELIST_10_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(codelistsPagedResult.getValues().size(), i);
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
            assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
    }

    @Test
    public void testSendCodelistToProductionValidationWithGeographical() throws Exception {
        String urn = CODELIST_9_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, codelistVersion.getLifeCycleMetadata().getProcStatus());
        assertEquals(VariableTypeEnum.GEOGRAPHICAL, codelistVersion.getVariable().getType());

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_9_V1_CODE_1);
        assertNotNull(code.getVariableElement());

        // Send to production validation
        codelistVersion = codesService.sendCodelistToProductionValidation(ctx, urn);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, codelistVersion.getLifeCycleMetadata().getProcStatus());
    }

    @Test
    public void testSendCodelistToProductionValidationErrorGeographicalWithoutVariableElements() throws Exception {
        String urn = CODELIST_8_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, codelistVersion.getLifeCycleMetadata().getProcStatus());
        assertEquals(VariableTypeEnum.GEOGRAPHICAL, codelistVersion.getVariable().getType());

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_8_V1_CODE_1);
        assertNull(code.getVariableElement());

        // Send to production validation
        try {
            codesService.sendCodelistToProductionValidation(ctx, urn);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_8_V1_CODE_1);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.CODE_VARIABLE_ELEMENT_REQUIRED_WHEN_GEOGRAPHICAL, 0, null, exceptionItem.getExceptionItems().get(0));
        }
    }

    @Test
    public void testSendCodelistToProductionValidationErrorToImportedRequiredMetadataInCodelist() throws Exception {

        String urn = CODELIST_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        // save to force incorrect metadata
        codelistVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        codelistVersion.setVariable(null);
        itemSchemeRepository.save(codelistVersion);

        try {
            codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), urn);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_VARIABLE}, exceptionItem.getExceptionItems().get(0));
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
        codelistVersion.setIsVariableUpdated(Boolean.FALSE);
        codesService.updateCodelist(getServiceContextAdministrador(), codelistVersion);

        // Send to production validation
        try {
            codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL}, exceptionItem.getExceptionItems().get(0));
        }
    }

    @Test
    public void testSendCodelistToProductionValidationErrorTranslations() throws Exception {

        String urn = CODELIST_2_V1;

        // Update to change metadata to send to production

        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            codelistVersion.getMaintainableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemSchemeRepository.save(codelistVersion);
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_2_V1_CODE_1);
            code.getNameableArtefact().setDescription(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(code);
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_2_V1_CODE_2);
            code.getNameableArtefact().setComment(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(code);
        }

        entityManager.flush();

        // Send to production validation
        try {
            codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), urn);
            fail("Codelist metadata required");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            int i = 0;
            // Codelist
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, exceptionItem
                        .getExceptionItems().get(0));
            }
            // Codes
            {
                // Code01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_2_V1_CODE_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
            }
            {
                // Code02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_2_V1_CODE_2);
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
            assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
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
            assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
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
            assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
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
        TaskInfo versioningResult = codesService.publishInternallyCodelist(ctx, urn, Boolean.FALSE, Boolean.FALSE);
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, versioningResult.getUrnResult());

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
            assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
    }

    @Test
    public void testPublishInternallyCodelistToTestOpennessVisualisationInLeafCodes() throws Exception {
        String urn = CODELIST_1_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        // Force DIFFUSION_VALIDATION procStatus to can test a codelist with multiple codes and visualisations
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        codelistVersion.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        itemSchemeRepository.save(codelistVersion);

        // Before
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_3);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, null));
        }

        // Publish internally
        TaskInfo versioningResult = codesService.publishInternallyCodelist(ctx, urn, Boolean.FALSE, Boolean.FALSE);
        codelistVersion = codesService.retrieveCodelistByUrn(ctx, versioningResult.getUrnResult());

        // After
        entityManager.clear();
        codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        {
            // Leaf code
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            // Leaf code
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            // Leaf code
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            // Leaf code
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_3);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null));
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
        {
            // Leaf code
            CodeMetamac code = assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1);
            assertOpennessVisualisationColumns(code, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null));
        }
    }

    @Override
    public void testCheckCodelistVersionTranslations() throws Exception {
        // Tested in testPublishInternallyCodelistCheckTranslations
    }

    @Test
    public void testPublishInternallyCodelistCheckTranslations() throws Exception {
        String urn = CODELIST_14_V1;

        try {
            // Note: publishInternallyCodelist calls to 'checkCodelistVersionTranslates'
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn, Boolean.FALSE, Boolean.FALSE);
            fail("Codelist wrong translations");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());
            int i = 0;
            // Codelist
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(3, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CODELIST_SHORT_NAME}, exceptionItem
                        .getExceptionItems().get(2));
            }
            // Codes
            {
                // Code01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_14_V1_CODE_1);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT}, exceptionItem
                        .getExceptionItems().get(1));
            }
            {
                // Code0101
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_14_V1_CODE_1_1);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CODE_SHORT_NAME}, exceptionItem.getExceptionItems()
                        .get(1));
            }
            {
                // Code02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_14_V1_CODE_2);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            {
                // Code03
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CODELIST_14_V1_CODE_3);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CODE_SHORT_NAME}, exceptionItem.getExceptionItems()
                        .get(1));
            }
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    public void testPublishInternallyCodelistErrorRequiredMetadata() throws Exception {
        String urn = CODELIST_11_V1;
        try {
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn, Boolean.FALSE, Boolean.FALSE);
            fail("codelist cannot be publish without an access type defined and with an associated variable and others...");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(3, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_ACCESS_TYPE}, exceptionItem.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION}, exceptionItem
                    .getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CODELIST_DEFAULT_OPENNESS_VISUALISATION}, exceptionItem
                    .getExceptionItems().get(2));
        }
    }

    @Test
    public void testPublishInternallyCodelistErrorNotExists() throws Exception {
        String urn = NOT_EXISTS;
        try {
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn, Boolean.FALSE, Boolean.FALSE);
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
            codesService.publishInternallyCodelist(getServiceContextAdministrador(), urn, Boolean.FALSE, Boolean.FALSE);
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
            assertEquals(AccessTypeEnum.PUBLIC, codelistVersion.getAccessType());
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
            assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
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
    public void testPublishExternallyCodelistRestricted() throws Exception {

        String urn = CODELIST_3_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        assertEquals(AccessTypeEnum.RESTRICTED, codelistVersion.getAccessType());

        // save to avoid categorisations error
        codelistVersion.getMaintainableArtefact().removeAllCategorisations();
        itemSchemeRepository.save(codelistVersion);

        // Publish externally
        codelistVersion = codesService.publishExternallyCodelist(ctx, urn);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        assertFalse(codelistVersion.getMaintainableArtefact().getPublicLogic());
        assertFalse(codelistVersion.getMaintainableArtefact().getLatestPublic());
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
    public void testPublishExternallyCodelistErrorRelatedResourcesReplaceToNotExternallyPublished() throws Exception {

        String urn = CODELIST_7_V2;

        // Change some metadata to force errors
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        codelistVersion.addReplaceToCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1));
        codelistVersion.addReplaceToCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_3_V1));
        codelistVersion.addReplaceToCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_10_V1)); // ok
        itemSchemeRepository.save(codelistVersion);

        entityManager.flush();

        try {
            codesService.publishExternallyCodelist(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CODELIST_NOT_EXTERNALLY_PUBLISHED, CODELIST_1_V1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CODELIST_NOT_EXTERNALLY_PUBLISHED, CODELIST_3_V1);
        }
    }

    @Test
    public void testPublishExternallyCodelistErrorRelatedResourcesCategorisationsNotExternallyPublished() throws Exception {

        String urn = CODELIST_3_V1;

        try {
            codesService.publishExternallyCodelist(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_2);
        }
    }

    @Override
    public void testCheckCodelistWithRelatedResourcesExternallyPublished() throws Exception {
        // tested in testPublishExternallyCodelistErrorRelatedResourcesNotExternallyPublished
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
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(02.000)";

        TaskInfo versioningResult = codesService.versioningCodelist(getServiceContextAdministrador(), urn, Boolean.TRUE, VersionTypeEnum.MAJOR);
        assertEquals(urnExpected, versioningResult.getUrnResult());
        assertNull(versioningResult.getIsPlannedInBackground());
        assertNull(versioningResult.getJobKey());

        // Validate
        validateVersioningCodelist3V1();
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

        TaskInfo versioningResult = codesService.versioningCodelist(getServiceContextAdministrador(), urnToCopy, Boolean.TRUE, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnToCopy);
        CodelistVersionMetamac codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

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
        TaskInfo versioningResult = codesService.versioningCodelist(getServiceContextAdministrador(), urn, Boolean.FALSE, VersionTypeEnum.MAJOR);

        // Validate
        entityManager.clear();
        codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        CodelistVersionMetamac codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
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

    @Test
    @DirtyDatabase
    public void testVersioningCodelistInBackground() throws Exception {

        int previousValueLimitToBackground = SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND;
        SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND = 3; // modify to force in background
        final String codelistUrn = CODELIST_3_V1;
        final StringBuilder jobKey = new StringBuilder();
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    ItemSchemeVersion itemSchemeVersion = itemSchemeRepository.findByUrn(codelistUrn);
                    assertEquals(null, itemSchemeVersion.getItemScheme().getIsTaskInBackground());

                    TaskInfo versioningResult = codesService.versioningCodelist(getServiceContextAdministrador(), codelistUrn, Boolean.TRUE, VersionTypeEnum.MAJOR);
                    assertEquals(true, versioningResult.getIsPlannedInBackground());
                    assertEquals(null, versioningResult.getUrnResult());
                    jobKey.append(versioningResult.getJobKey());

                    itemSchemeVersion = itemSchemeRepository.findByUrn(codelistUrn);
                    assertEquals(true, itemSchemeVersion.getItemScheme().getIsTaskInBackground());

                    try {
                        codesService.versioningCodelist(getServiceContextAdministrador(), codelistUrn, Boolean.TRUE, VersionTypeEnum.MAJOR);
                        fail("already versioning");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_ACTION_NOT_SUPPORTED_WHEN_TASK_IN_BACKGROUND.getCode(), e.getExceptionItems().get(0).getCode());
                        assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                        assertEquals(codelistUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                    }
                } catch (MetamacException e) {
                    fail("versioning failed");
                }
            }
        });
        waitUntilJobFinished();
        SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND = previousValueLimitToBackground;

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());

        // Validate versioning
        validateVersioningCodelist3V1();

        // Validate background task as finished in item scheme
        CodelistVersion codelistVersionVersioned = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(false, codelistVersionVersioned.getItemScheme().getIsTaskInBackground());
    }

    @Override
    @Test
    public void testCopyCodelist() throws Exception {

        String urnToCopy = CODELIST_14_V1;
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        String versionExpected = "01.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST14(01.000)";
        String urnExpectedCode1 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST14(01.000).CODE01";
        String urnExpectedCode11 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST14(01.000).CODE0101";
        String urnExpectedCode2 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST14(01.000).CODE02";
        String urnExpectedCode3 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST14(01.000).CODE03";

        TaskInfo copyResult = codesService.copyCodelist(getServiceContextAdministrador(), urnToCopy);

        // Validate (only some metadata, already tested in statistic module)
        entityManager.clear();
        CodelistVersionMetamac codelistVersionNewArtefact = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), copyResult.getUrnResult());
        assertEquals(maintainerUrnExpected, codelistVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, codelistVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, codelistVersionNewArtefact.getMaintainableArtefact().getUrn());
        assertEquals(null, codelistVersionNewArtefact.getMaintainableArtefact().getReplaceToVersion());
        assertEquals(null, codelistVersionNewArtefact.getMaintainableArtefact().getReplacedByVersion());
        assertTrue(codelistVersionNewArtefact.getMaintainableArtefact().getIsLastVersion());

        // Codes
        assertEquals(4, codelistVersionNewArtefact.getItems().size());
        assertListCodesContainsCode(codelistVersionNewArtefact.getItems(), urnExpectedCode1);
        assertListCodesContainsCode(codelistVersionNewArtefact.getItems(), urnExpectedCode11);
        assertListCodesContainsCode(codelistVersionNewArtefact.getItems(), urnExpectedCode2);
        assertListCodesContainsCode(codelistVersionNewArtefact.getItems(), urnExpectedCode3);

        assertEquals(3, codelistVersionNewArtefact.getItemsFirstLevel().size());
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersionNewArtefact.getItemsFirstLevel(), urnExpectedCode1);
            CodesMetamacAsserts.assertEqualsInternationalString(code.getNameableArtefact().getName(), "en", "name code1", "it", "nombre it code1");
            CodesMetamacAsserts.assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "descripción code1", "it", "descripción it code1");
            assertEquals(null, code.getNameableArtefact().getComment());

            assertEquals(1, code.getChildren().size());
            {
                CodeMetamac codeChild = assertListCodesContainsCode(code.getChildren(), urnExpectedCode11);
                assertEquals(0, codeChild.getChildren().size());
            }

        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersionNewArtefact.getItemsFirstLevel(), urnExpectedCode2);
            CodesMetamacAsserts.assertEqualsInternationalString(code.getNameableArtefact().getName(), "en", "name code2", null, null);

            assertEquals(0, code.getChildren().size());
        }
        {
            CodeMetamac code = assertListCodesContainsCode(codelistVersionNewArtefact.getItemsFirstLevel(), urnExpectedCode3);
            CodesMetamacAsserts.assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre code3", "en", "name code3");

            assertEquals(0, code.getChildren().size());
        }
    }

    @Test
    @DirtyDatabase
    public void testCopyCodelistInBackground() throws Exception {

        int previousValueLimitToBackground = SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND;
        SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND = 3; // modify to force in background
        final String urnToCopy = CODELIST_14_V1;
        final StringBuilder jobKey = new StringBuilder();
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    ItemSchemeVersion itemSchemeVersion = itemSchemeRepository.findByUrn(urnToCopy);
                    assertEquals(null, itemSchemeVersion.getItemScheme().getIsTaskInBackground());

                    TaskInfo copyResult = codesService.copyCodelist(getServiceContextAdministrador(), urnToCopy);
                    assertEquals(true, copyResult.getIsPlannedInBackground());
                    assertEquals(null, copyResult.getUrnResult());
                    jobKey.append(copyResult.getJobKey());

                    itemSchemeVersion = itemSchemeRepository.findByUrn(urnToCopy);
                    assertEquals(true, itemSchemeVersion.getItemScheme().getIsTaskInBackground());

                    try {
                        codesService.copyCodelist(getServiceContextAdministrador(), urnToCopy);
                        fail("already copying");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_ACTION_NOT_SUPPORTED_WHEN_TASK_IN_BACKGROUND.getCode(), e.getExceptionItems().get(0).getCode());
                        assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                        assertEquals(urnToCopy, e.getExceptionItems().get(0).getMessageParameters()[0]);
                    }
                } catch (MetamacException e) {
                    fail("copy failed");
                }
            }
        });
        waitUntilJobFinished();
        SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND = previousValueLimitToBackground;

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());

        // Validate
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST14(01.000)";
        String versionExpected = "01.000";
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        entityManager.clear();
        CodelistVersionMetamac codelistVersionNewArtefact = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnExpected);
        assertEquals(maintainerUrnExpected, codelistVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, codelistVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, codelistVersionNewArtefact.getMaintainableArtefact().getUrn());

        // Validate background task as finished in original item scheme
        CodelistVersion codelistVersionCopied = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(false, codelistVersionCopied.getItemScheme().getIsTaskInBackground());
    }

    @Override
    @Test
    public void testStartCodelistValidity() throws Exception {
        CodelistVersionMetamac codelistVersion = codesService.startCodelistValidity(getServiceContextAdministrador(), CODELIST_7_V2);

        assertNotNull(codelistVersion);
        assertNotNull(codelistVersion.getMaintainableArtefact().getValidFrom());
        assertNull(codelistVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testStartCodelistValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CODELIST_6_V1};
        for (String urn : urns) {
            try {
                codesService.startCodelistValidity(getServiceContextAdministrador(), urn);
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
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_3);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), codeRetrieved.getNameableArtefact().getUrn());
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
    public void testCreateCodeErrorVariableElementDifferentCodelistVariable() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setParent(null);
        code.setShortName(null);
        code.setVariableElement(codesService.retrieveVariableElementByUrn(ctx, VARIABLE_5_VARIABLE_ELEMENT_1));

        String codelistUrn = CODELIST_1_V2;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertFalse(codelistVersion.getVariable().getNameableArtefact().getUrn().equals(code.getVariableElement().getVariable().getNameableArtefact().getUrn()));

        try {
            codesService.createCode(ctx, codelistUrn, code);
            fail("incorrect metadata");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.CODE_VARIABLE_ELEMENT}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodeErrorCodelistImported() throws Exception {
        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        String codelistUrn = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        // save to force incorrect metadata
        codelistVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        codelistVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(codelistVersion);

        try {
            codesService.createCode(getServiceContextAdministrador(), codelistUrn, code);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
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

        Code code1 = assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListCodesContainsCode(code1.getChildren(), codeRetrieved.getNameableArtefact().getUrn());
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
    public void testCopyCodesInCodelist() throws Exception {
        String codelistSourceUrn = CODELIST_1_V2;
        String codelistTargetUrn = CODELIST_1_V2;

        // Before
        List<CodeMetamacVisualisationResult> hierarchyBefore = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistTargetUrn, "es", null, null);
        assertEquals(9, hierarchyBefore.size());

        // Codes to copy
        List<CodeToCopy> codesToCopy = new ArrayList<CodeToCopy>();
        {
            CodeToCopy code = new CodeToCopy();
            code.setSourceUrn(CODELIST_1_V2_CODE_3);
            code.setNewCodeIdentifier("CODE00");
            code.setParentNewCodeIdentifier(null);
            codesToCopy.add(code);
        }
        {
            CodeToCopy code = new CodeToCopy();
            code.setSourceUrn(CODELIST_1_V2_CODE_4);
            code.setNewCodeIdentifier("CODE01B");
            code.setParentNewCodeIdentifier(null);
            codesToCopy.add(code);
            {
                CodeToCopy codeChild = new CodeToCopy();
                codeChild.setSourceUrn(CODELIST_1_V2_CODE_4_1_1);
                codeChild.setNewCodeIdentifier("CODE01B01");
                codeChild.setParentNewCodeIdentifier(code.getNewCodeIdentifier());
                codesToCopy.add(codeChild);
            }
        }
        {
            CodeToCopy code = new CodeToCopy();
            code.setSourceUrn(CODELIST_1_V2_CODE_2_1);
            code.setNewCodeIdentifier("CODE05");
            code.setParentNewCodeIdentifier(null);
            codesToCopy.add(code);
        }

        // Copy
        codesService.copyCodesInCodelist(getServiceContextAdministrador(), codelistSourceUrn, codelistTargetUrn, codesToCopy);

        entityManager.clear();
        String code0201TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE05";
        String code03TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE00";
        String code04TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE01B";
        String code040101TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE01B01";

        // Validate
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistTargetUrn);
        assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));

        // Validate items
        List<CodeMetamacVisualisationResult> hierarchyAfter = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistTargetUrn, "es", null, null);
        assertEquals(9 + 4, hierarchyAfter.size());
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_1);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_2);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_2_1);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_2_1_1);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_2_2);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_3);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_4);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_4_1);
        getCodeMetamacVisualisationResult(hierarchyAfter, CODELIST_1_V2_CODE_4_1_1);
        getCodeMetamacVisualisationResult(hierarchyAfter, code0201TargetUrn);
        getCodeMetamacVisualisationResult(hierarchyAfter, code03TargetUrn);
        getCodeMetamacVisualisationResult(hierarchyAfter, code04TargetUrn);
        getCodeMetamacVisualisationResult(hierarchyAfter, code040101TargetUrn);

        // Validate previous in first level are reorder
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
            assertEquals(Integer.valueOf(3), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(2), code.getOrder3());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_3);
            assertEquals(Integer.valueOf(4), code.getOrder1());
            assertEquals(Integer.valueOf(2), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4);
            assertEquals(Integer.valueOf(5), code.getOrder1());
            assertEquals(Integer.valueOf(3), code.getOrder2());
            assertEquals(Integer.valueOf(3), code.getOrder3());
        }

        // Validate copied
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code0201TargetUrn);
            assertEquals("CODE05", code.getNameableArtefact().getCode());
            assertNull(code.getParent());
            assertNotNull(code.getItemSchemeVersionFirstLevel());
            assertNull(code.getNameableArtefact().getUriProvider());
            assertNull(code.getShortName());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "codelist-1-v2-code-2- Isla de La Gomera", "en", "Name codelist-1-v2-code-2-1");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "descripción CODELIST_1_V2_CODE_2_1", "en", "description CODELIST_1_V2_CODE_2_1");
            assertNull(code.getNameableArtefact().getComment());
            assertEquals(code.getVariableElement().getIdentifiableArtefact().getUrn(), VARIABLE_2_VARIABLE_ELEMENT_1);
            assertEquals(2, code.getNameableArtefact().getAnnotations().size());
            {
                Annotation annotation = assertSetAnnotationsContainsAnnotations(code.getNameableArtefact().getAnnotations(), "CODE0201_ANNOTATION211");
                assertEqualsInternationalString(annotation.getText(), "es", "Anotación 21", null, null);
                assertEquals("title-annotation211", annotation.getTitle());
                assertEquals("type-annotation211", annotation.getType());
                assertEquals("http://annotation211", annotation.getUrl());
                assertEquals("title-annotation211", annotation.getTitle());
            }
            {
                Annotation annotation = assertSetAnnotationsContainsAnnotations(code.getNameableArtefact().getAnnotations(), "CODE0201_ANNOTATION212");
                assertNull(annotation.getText());
                assertEquals("title-annotation212", annotation.getTitle());
                assertEquals("type-annotation212", annotation.getType());
                assertEquals("http://annotation212", annotation.getUrl());
                assertEquals("title-annotation212", annotation.getTitle());
            }
            assertEquals(Integer.valueOf(6), code.getOrder1());
            assertEquals(Integer.valueOf(6), code.getOrder2());
            assertEquals(Integer.valueOf(6), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(0, code.getChildren().size());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code03TargetUrn);
            assertEquals("CODE00", code.getNameableArtefact().getCode());
            assertNull(code.getParent());
            assertNotNull(code.getItemSchemeVersionFirstLevel());
            assertNull(code.getNameableArtefact().getUriProvider());
            assertNull(code.getShortName());
            assertEquals(3, code.getNameableArtefact().getName().getTexts().size());
            assertEquals("name code-3", code.getNameableArtefact().getName().getLocalisedLabel("en"));
            assertEquals("nombre it code-3", code.getNameableArtefact().getName().getLocalisedLabel("it"));
            assertEquals("Fuerteventura", code.getNameableArtefact().getName().getLocalisedLabel("es"));
            assertNull(code.getNameableArtefact().getDescription());
            assertEquals(code.getVariableElement().getIdentifiableArtefact().getUrn(), VARIABLE_2_VARIABLE_ELEMENT_3);
            assertNull(code.getNameableArtefact().getComment());
            assertEquals(0, code.getNameableArtefact().getAnnotations().size());
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(4), code.getOrder2());
            assertEquals(Integer.valueOf(4), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(0, code.getChildren().size());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code04TargetUrn);
            assertEquals("CODE01B", code.getNameableArtefact().getCode());
            assertNull(code.getParent());
            assertNotNull(code.getItemSchemeVersionFirstLevel());
            assertNull(code.getNameableArtefact().getUriProvider());
            assertEqualsInternationalString(code.getShortName(), "es", "Lanzarote", "en", "Lanzarote en");
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Lanzarote", null, null);
            assertNull(code.getNameableArtefact().getDescription());
            assertNull(code.getVariableElement());
            assertNull(code.getNameableArtefact().getComment());
            assertEquals(0, code.getNameableArtefact().getAnnotations().size());
            assertEquals(Integer.valueOf(2), code.getOrder1());
            assertEquals(Integer.valueOf(5), code.getOrder2());
            assertEquals(Integer.valueOf(5), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(1, code.getChildren().size());
            assertEquals(code040101TargetUrn, code.getChildren().get(0).getNameableArtefact().getUrn());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code040101TargetUrn);
            assertEquals("CODE01B01", code.getNameableArtefact().getCode());
            assertEquals(code04TargetUrn, code.getParent().getNameableArtefact().getUrn());
            assertNull(code.getNameableArtefact().getUriProvider());
            assertNull(code.getShortName());
            assertEquals(3, code.getNameableArtefact().getName().getTexts().size());
            assertEquals("Name codelist-1-v2-code-4-1-1", code.getNameableArtefact().getName().getLocalisedLabel("en"));
            assertEquals("nombre it codelist-1-v2-code-4-1-1", code.getNameableArtefact().getName().getLocalisedLabel("it"));
            assertEquals("Nombre codelist-1-v2-code-4-1-1", code.getNameableArtefact().getName().getLocalisedLabel("es"));
            assertNull(code.getNameableArtefact().getDescription());
            assertEquals(code.getVariableElement().getIdentifiableArtefact().getUrn(), VARIABLE_2_VARIABLE_ELEMENT_1);
            assertNull(code.getNameableArtefact().getComment());
            assertEquals(0, code.getNameableArtefact().getAnnotations().size());
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(0, code.getChildren().size());
        }
    }

    @Test
    public void testCopyCodesInCodelistInParentOnlyCheckStructureAndOrder() throws Exception {
        String codelistSourceUrn = CODELIST_1_V2;
        String codelistTargetUrn = CODELIST_1_V2;

        List<CodeToCopy> codesToCopy = new ArrayList<CodeToCopy>();
        {
            CodeToCopy code = new CodeToCopy();
            code.setSourceUrn(CODELIST_1_V2_CODE_3);
            code.setNewCodeIdentifier("CODE00");
            code.setParentNewCodeIdentifier("CODE02");
            codesToCopy.add(code);
        }
        {
            CodeToCopy code = new CodeToCopy();
            code.setSourceUrn(CODELIST_1_V2_CODE_4);
            code.setNewCodeIdentifier("CODE01B");
            code.setParentNewCodeIdentifier("CODE02");
            codesToCopy.add(code);
            {
                CodeToCopy codeChild = new CodeToCopy();
                codeChild.setSourceUrn(CODELIST_1_V2_CODE_4_1_1);
                codeChild.setNewCodeIdentifier("CODE01B01");
                codeChild.setParentNewCodeIdentifier(code.getNewCodeIdentifier());
                codesToCopy.add(codeChild);
            }
        }
        {
            CodeToCopy code = new CodeToCopy();
            code.setSourceUrn(CODELIST_1_V2_CODE_2_1);
            code.setNewCodeIdentifier("CODE05");
            code.setParentNewCodeIdentifier("CODE02");
            codesToCopy.add(code);
        }

        // Copy
        codesService.copyCodesInCodelist(getServiceContextAdministrador(), codelistSourceUrn, codelistTargetUrn, codesToCopy);

        entityManager.clear();
        String code0201TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE05";
        String code03TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE00";
        String code04TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE01B";
        String code040101TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).CODE01B01";

        // Validate
        List<CodeMetamacVisualisationResult> hierarchyAfter = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistTargetUrn, "es", null, null);
        assertEquals(9 + 4, hierarchyAfter.size());
        getCodeMetamacVisualisationResult(hierarchyAfter, code0201TargetUrn);
        getCodeMetamacVisualisationResult(hierarchyAfter, code03TargetUrn);
        getCodeMetamacVisualisationResult(hierarchyAfter, code04TargetUrn);
        getCodeMetamacVisualisationResult(hierarchyAfter, code040101TargetUrn);

        // Validate structure (previous in level and copied)
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_1);
            assertEquals(Integer.valueOf(2), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_2);
            assertEquals(Integer.valueOf(3), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code0201TargetUrn);
            assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getNameableArtefact().getUrn());
            assertNull(code.getItemSchemeVersionFirstLevel());
            assertEquals(Integer.valueOf(4), code.getOrder1());
            assertEquals(Integer.valueOf(4), code.getOrder2());
            assertEquals(Integer.valueOf(4), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(0, code.getChildren().size());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code03TargetUrn);
            assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getNameableArtefact().getUrn());
            assertNull(code.getItemSchemeVersionFirstLevel());
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(2), code.getOrder2());
            assertEquals(Integer.valueOf(2), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(0, code.getChildren().size());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code04TargetUrn);
            assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getNameableArtefact().getUrn());
            assertNull(code.getItemSchemeVersionFirstLevel());
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(3), code.getOrder2());
            assertEquals(Integer.valueOf(3), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(1, code.getChildren().size());
            assertEquals(code040101TargetUrn, code.getChildren().get(0).getNameableArtefact().getUrn());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code040101TargetUrn);
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertNull(code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertNull(code.getOpenness4());

            assertEquals(0, code.getChildren().size());
        }
    }

    @Test
    public void testCopyCodesInCodelistWithDifferentVariable() throws Exception {
        String codelistSourceUrn = CODELIST_1_V2;
        CodelistVersionMetamac codelistSource = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistSourceUrn);
        String code01SourceUrn = CODELIST_1_V2_CODE_1;
        String codelistTargetUrn = CODELIST_2_V1;
        CodelistVersionMetamac codelistTarget = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistTargetUrn);
        assertFalse(codelistTarget.getVariable().getNameableArtefact().getUrn().equals(codelistSource.getVariable().getNameableArtefact().getUrn()));

        // Codes to copy
        List<CodeToCopy> codesToCopy = new ArrayList<CodeToCopy>();
        CodeToCopy code = new CodeToCopy();
        code.setSourceUrn(code01SourceUrn);
        code.setNewCodeIdentifier("CODE03");
        code.setParentNewCodeIdentifier(null);
        codesToCopy.add(code);

        // Copy
        codesService.copyCodesInCodelist(getServiceContextAdministrador(), codelistSourceUrn, codelistTargetUrn, codesToCopy);

        entityManager.clear();
        String code01TargetUrn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST02(01.000).CODE03";

        // Validate
        List<CodeMetamacVisualisationResult> hierarchyAfter = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistTargetUrn, "es", null, null);
        assertEquals(2 + 1, hierarchyAfter.size());
        getCodeMetamacVisualisationResult(hierarchyAfter, code01TargetUrn);

        // Validate short name
        CodeMetamac codeSource = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code01SourceUrn);
        assertNotNull(codeSource.getVariableElement());

        CodeMetamac codeTarget = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code01TargetUrn);
        assertNull(codeTarget.getVariableElement());
        assertNull(codeTarget.getShortName()); // because in source it was null
    }

    @Test
    @Override
    public void testPreCreateCode() throws Exception {
        // TODO testPreCreateCode
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

    @Test
    public void testUpdateCodeErrorCodelistWithoutVariable() throws Exception {
        String urn = CODELIST_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(ctx, urn);
        // save to force incorrect metadata
        codelistVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        codelistVersion.setVariable(null);
        itemSchemeRepository.save(codelistVersion);

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_2_V1_CODE_1);
        assertEquals(urn, code.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        code.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        code.getNameableArtefact().setName(CodesDoMocks.mockInternationalString());

        try {
            codesService.updateCode(getServiceContextAdministrador(), code);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CODELIST_VARIABLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testUpdateCodeVariableElement() throws Exception {
        String codeUrn = CODELIST_1_V2_CODE_1;

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getIdentifiableArtefact().getUrn());

        // Change variable element
        String variableElementUrnNew = VARIABLE_2_VARIABLE_ELEMENT_1;
        codesService.updateCodeVariableElement(getServiceContextAdministrador(), code.getNameableArtefact().getUrn(), variableElementUrnNew);
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(variableElementUrnNew, code.getVariableElement().getIdentifiableArtefact().getUrn());

        // Reset variable element
        codesService.updateCodeVariableElement(getServiceContextAdministrador(), code.getNameableArtefact().getUrn(), null);
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertNull(variableElementUrnNew, code.getVariableElement());

        // Add short name
        code.setShortName(BaseDoMocks.mockInternationalString());
        code.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        code = codesService.updateCode(getServiceContextAdministrador(), code);
        assertNotNull(code.getShortName());

        // Add variable element again to test short name was cleared
        codesService.updateCodeVariableElement(getServiceContextAdministrador(), code.getNameableArtefact().getUrn(), variableElementUrnNew);
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(variableElementUrnNew, code.getVariableElement().getIdentifiableArtefact().getUrn());
        assertNull(code.getShortName());
    }

    @Override
    @Test
    public void testUpdateCodesVariableElements() throws Exception {
        String codelistUrn = CODELIST_1_V2;

        CodeMetamac code1 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
        CodeMetamac code2 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
        CodeMetamac code211 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_1_1);
        CodeMetamac code22 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_2);
        CodeMetamac code3 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_3);
        CodeMetamac code4 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4);
        CodeMetamac code41 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4_1);

        VariableElement variableElement1 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        VariableElement variableElement2 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_2);
        VariableElement variableElement3 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_3);

        Map<Long, Long> variableElementsIdByCodeId = new HashMap<Long, Long>();
        variableElementsIdByCodeId.put(code1.getId(), variableElement1.getId()); // change variableElement
        variableElementsIdByCodeId.put(code2.getId(), variableElement1.getId()); // add variableElement
        variableElementsIdByCodeId.put(code211.getId(), null); // remove variableElement
        variableElementsIdByCodeId.put(code22.getId(), variableElement3.getId()); // add variableElement
        variableElementsIdByCodeId.put(code3.getId(), null); // remove variableElement
        variableElementsIdByCodeId.put(code4.getId(), variableElement3.getId()); // add variableElement
        variableElementsIdByCodeId.put(code41.getId(), variableElement2.getId()); // add variableElement

        codesService.updateCodesVariableElements(getServiceContextAdministrador(), codelistUrn, variableElementsIdByCodeId);

        entityManager.clear();
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code1.getNameableArtefact().getUrn());
            assertEquals(variableElement1.getIdentifiableArtefact().getUrn(), code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertNull(code.getShortName());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code2.getNameableArtefact().getUrn());
            assertEquals(variableElement1.getIdentifiableArtefact().getUrn(), code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertNull(code.getShortName()); // cleared
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code211.getNameableArtefact().getUrn());
            assertNull(code.getVariableElement());
            assertNotNull(code.getShortName());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code22.getNameableArtefact().getUrn());
            assertEquals(variableElement3.getIdentifiableArtefact().getUrn(), code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertNull(code.getShortName());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code3.getNameableArtefact().getUrn());
            assertNull(code.getVariableElement());
            assertNull(code.getShortName());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code4.getNameableArtefact().getUrn());
            assertEquals(variableElement3.getIdentifiableArtefact().getUrn(), code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertNull(code.getShortName()); // cleared
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), code41.getNameableArtefact().getUrn());
            assertEquals(variableElement2.getIdentifiableArtefact().getUrn(), code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertNull(code.getShortName());
        }
    }

    @Test
    public void testUpdateCodeVariableElementBeforeWithoutVariableElementRemoveShortName() throws Exception {
        String codeUrn = CODELIST_1_V2_CODE_2_1_1;

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertNull(code.getVariableElement());
        assertNotNull(code.getShortName());

        // Add variable element
        String variableElementUrnNew = VARIABLE_2_VARIABLE_ELEMENT_1;
        codesService.updateCodeVariableElement(getServiceContextAdministrador(), code.getNameableArtefact().getUrn(), variableElementUrnNew);
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(variableElementUrnNew, code.getVariableElement().getIdentifiableArtefact().getUrn());
        assertNull(code.getShortName());
    }

    @Test
    public void testUpdateCodeVariableElementErrorDifferentVariableCodelist() throws Exception {
        String codeUrn = CODELIST_1_V2_CODE_1;

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getIdentifiableArtefact().getUrn());

        // Change variable element
        String variableElementUrnNew = VARIABLE_5_VARIABLE_ELEMENT_1;
        try {
            codesService.updateCodeVariableElement(getServiceContextAdministrador(), code.getNameableArtefact().getUrn(), variableElementUrnNew);
            fail("error");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.CODE_VARIABLE_ELEMENT}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateCodeVariableElementInCodelistPublished() throws Exception {
        String codeUrn = CODELIST_7_V2_CODE_1;

        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByCodeUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, codelistVersion.getLifeCycleMetadata().getProcStatus());
        assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, code.getVariableElement().getIdentifiableArtefact().getUrn());

        // Add variable element
        String variableElementUrnNew = VARIABLE_5_VARIABLE_ELEMENT_2;
        codesService.updateCodeVariableElement(getServiceContextAdministrador(), code.getNameableArtefact().getUrn(), variableElementUrnNew);
        code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codeUrn);
        assertEquals(variableElementUrnNew, code.getVariableElement().getIdentifiableArtefact().getUrn());
    }

    @Override
    @Test
    public void testRetrieveCodeByUrn() throws Exception {
        // Retrieve
        String urn = CODELIST_1_V2_CODE_1;
        CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, code.getNameableArtefact().getUrn());
        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getIdentifiableArtefact().getUrn());
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
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
        assertEquals(8, codelistVersion.getItems().size());
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4_1_1);
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
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_1);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_2);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_3);
        assertListCodesContainsCode(codelistVersion.getItemsFirstLevel(), CODELIST_1_V2_CODE_4);
        assertEquals(7, codelistVersion.getItems().size());
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_2);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_2_1_1);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_3);
        assertListCodesContainsCode(codelistVersion.getItems(), CODELIST_1_V2_CODE_4);
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
    public void testDeleteCodeErrorCodelistImported() throws Exception {
        String urn = CODELIST_1_V2_CODE_3;
        String codelistUrn = CODELIST_1_V2;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        // save to force incorrect metadata
        codelistVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        codelistVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(codelistVersion);

        try {
            codesService.deleteCode(getServiceContextAdministrador(), urn);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    public void testRetrieveCodesByCodelistUrn() throws Exception {
        // In other testRetrieveCodesByCodelistUrn* methods
    };

    @Test
    public void testRetrieveCodesByCodelistUrnCheckOrderVisualisationsAndMetadataInCodeMetamacVisualisationResult() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;

        // LOCALE = 'es' and ORDER = '1'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale,
                    CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL, null);

            // Validate
            assertEquals(9, codes.size());
            {
                // Code 01 (validate all metadata)
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1);
                assertEquals(CODELIST_1_V2_CODE_1, code.getUrn());
                assertEquals("CODE01", code.getCode());

                assertEquals("Isla de Tenerife", code.getName());
                assertEquals("Descripción codelist-1-v2-code-1", code.getDescription());
                assertEquals(Long.valueOf(121), code.getItemIdDatabase());
                assertEquals(null, code.getParent());
                assertEquals(null, code.getParentIdDatabase());
                assertEquals(Integer.valueOf(0), code.getOrder());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", code.getCreatedDate());
            }
            {
                // Code 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2);
                assertEquals(CODELIST_1_V2_CODE_2, code.getUrn());
                assertEquals("CODE02", code.getCode());
                assertEquals("Nombre codelist-1-v2-code-2 Canaria, Gran", code.getName());
                assertEquals(null, code.getDescription());
                assertEquals(Integer.valueOf(1), code.getOrder());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", code.getCreatedDate());
            }
            {
                // Code 02 01 (validate parent)
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1);
                assertEquals(CODELIST_1_V2_CODE_2_1, code.getUrn());
                assertEquals("CODE02", code.getParent().getCode());
                assertEquals("codelist-1-v2-code-2- Isla de La Gomera", code.getName());
                assertEquals("descripción CODELIST_1_V2_CODE_2_1", code.getDescription());
                assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getUrn());
                assertEquals(Long.valueOf("122"), code.getParentIdDatabase());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 02 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1);
                assertEquals(CODELIST_1_V2_CODE_2_1_1, code.getUrn());
                assertEquals("CODE0201", code.getParent().getCode());
                assertEquals("Santa Cruz de La Palma codelist-1-v2-code-2-1-1", code.getName());
                assertEquals(Long.valueOf("1221"), code.getParentIdDatabase());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 02 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2);
                assertEquals(CODELIST_1_V2_CODE_2_2, code.getUrn());
                assertEquals("CODE02", code.getParent().getCode());
                assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getUrn());
                assertEquals(Long.valueOf("122"), code.getParentIdDatabase());
                assertEquals("Isla de El Hierro", code.getName());
                assertEquals(Integer.valueOf(1), code.getOrder());
            }
            {
                // Code 03
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3);
                assertEquals(CODELIST_1_V2_CODE_3, code.getUrn());
                assertEquals("Fuerteventura", code.getName());
                assertEquals(Integer.valueOf(2), code.getOrder());
            }
            {
                // Code 04
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4);
                assertEquals(CODELIST_1_V2_CODE_4, code.getUrn());
                assertEquals("Lanzarote", code.getName());
                assertEquals(Integer.valueOf(3), code.getOrder());
            }
            {
                // Code 04 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1);
                assertEquals(CODELIST_1_V2_CODE_4_1, code.getUrn());
                assertEquals("Canarias, Tenerife", code.getName());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 04 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1);
                assertEquals(CODELIST_1_V2_CODE_4_1_1, code.getUrn());
                assertEquals("CODE0401", code.getParent().getCode());
                assertEquals(CODELIST_1_V2_CODE_4_1, code.getParent().getUrn());
                assertEquals("Nombre codelist-1-v2-code-4-1-1", code.getName());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
        }

        // LOCALE = 'en' and ORDER = '3'
        {
            String locale = "en";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, CODELIST_1_V2_ORDER_VISUALISATION_03, null);

            // Validate
            assertEquals(9, codes.size());
            {
                // Code 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1);
                assertEquals("Name codelist-1-v2-code-1", code.getName());
                assertEquals(null, code.getDescription());
                assertEquals(Integer.valueOf(1), code.getOrder());
            }
            {
                // Code 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2);
                assertEquals(null, code.getName());
                assertEquals(null, code.getDescription());
                assertEquals(Integer.valueOf(2), code.getOrder());
            }
            {
                // Code 02 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1);
                assertEquals("Name codelist-1-v2-code-2-1", code.getName());
                assertEquals("description CODELIST_1_V2_CODE_2_1", code.getDescription());
                assertEquals(Integer.valueOf(1), code.getOrder());
            }
            {
                // Code 02 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1);
                assertEquals(null, code.getName());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 02 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2);
                assertEquals(null, code.getName()); // it has not name
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 03
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3);
                assertEquals("name code-3", code.getName());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 04
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4);
                assertEquals(null, code.getName());
                assertEquals(Integer.valueOf(3), code.getOrder());
            }
            {
                // Code 04 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1);
                assertEquals(null, code.getName());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
            {
                // Code 04 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1);
                assertEquals("Name codelist-1-v2-code-4-1-1", code.getName());
                assertEquals(Integer.valueOf(0), code.getOrder());
            }
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrnCheckOpennessVisualisations() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;

        // OPENNESS = '1'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, null,
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);

            // Validate
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
        // OPENNESS = '2'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, null, CODELIST_1_V2_OPENNESS_VISUALISATION_02);

            // Validate
            assertEquals(9, codes.size());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
        // OPENNESS = '3'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, null, CODELIST_1_V2_OPENNESS_VISUALISATION_03);

            // Validate
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrnCheckAllVisualisationsTogether() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;

        // OPENNESS = '1', ORDER = '1'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale,
                    CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL, CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);

            // Validate
            assertEquals(9, codes.size());
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2), Integer.valueOf(1), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2), Integer.valueOf(1), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3), Integer.valueOf(2), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4), Integer.valueOf(3), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1), Integer.valueOf(0), Boolean.TRUE);
        }
        // OPENNESS = '2', ORDER = '3'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, CODELIST_1_V2_ORDER_VISUALISATION_03,
                    CODELIST_1_V2_OPENNESS_VISUALISATION_02);

            // Validate
            assertEquals(9, codes.size());
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1), Integer.valueOf(1), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2), Integer.valueOf(2), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1), Integer.valueOf(1), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1), Integer.valueOf(0), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4), Integer.valueOf(3), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1), Integer.valueOf(0), Boolean.FALSE);
        }
        // OPENNESS = '3', ORDER = '2'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, CODELIST_1_V2_ORDER_VISUALISATION_02,
                    CODELIST_1_V2_OPENNESS_VISUALISATION_03);

            // Validate
            assertEquals(9, codes.size());
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2), Integer.valueOf(1), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1), Integer.valueOf(0), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2), Integer.valueOf(1), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3), Integer.valueOf(2), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4), Integer.valueOf(3), Boolean.FALSE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1), Integer.valueOf(0), Boolean.TRUE);
            assertCodeVisualisations(getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1), Integer.valueOf(0), Boolean.TRUE);
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrnCheckVariableElements() throws Exception {

        // Retrieve
        String codelistUrn = CODELIST_1_V2;

        // LOCALE = 'es'
        {
            String locale = "es";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, null, null);

            // Validate
            assertEquals(9, codes.size());
            {
                // Code 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1);
                assertEquals(Long.valueOf(22), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_02", code.getVariableElement().getCode());
                assertEquals("Fuerteventura", code.getVariableElement().getShortName());
            }
            {
                // Code 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 02 01 (validate parent)
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1);
                assertEquals(Long.valueOf(21), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_01", code.getVariableElement().getCode());
                assertEquals("El Hierro", code.getVariableElement().getShortName());
            }
            {
                // Code 02 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 02 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 03
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3);
                assertEquals(Long.valueOf(23), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_03", code.getVariableElement().getCode());
                assertEquals("Gran Canaria", code.getVariableElement().getShortName());
            }
            {
                // Code 04
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 04 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 04 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1);
                assertEquals(Long.valueOf(21), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_01", code.getVariableElement().getCode());
                assertEquals("El Hierro", code.getVariableElement().getShortName());
            }
        }

        // LOCALE = 'en'
        {
            String locale = "en";
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, locale, null, null);

            // Validate
            assertEquals(9, codes.size());
            {
                // Code 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1);
                assertEquals(Long.valueOf(22), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_02", code.getVariableElement().getCode());
                assertEquals("Short name variableElement 2-2", code.getVariableElement().getShortName());
            }
            {
                // Code 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 02 01 (validate parent)
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1);
                assertEquals(Long.valueOf(21), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_01", code.getVariableElement().getCode());
                assertEquals("short name variableElement 2-1", code.getVariableElement().getShortName());
            }
            {
                // Code 02 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 02 02
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 03
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3);
                assertEquals(Long.valueOf(23), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_03", code.getVariableElement().getCode());
                assertEquals(null, code.getVariableElement().getShortName());
            }
            {
                // Code 04
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 04 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1);
                assertEquals(null, code.getVariableElement());
            }
            {
                // Code 04 01 01
                CodeMetamacVisualisationResult code = getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1);
                assertEquals(Long.valueOf(21), code.getVariableElement().getIdDatabase());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getUrn());
                assertEquals("VARIABLE_ELEMENT_01", code.getVariableElement().getCode());
                assertEquals("short name variableElement 2-1", code.getVariableElement().getShortName());
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
            assertEquals(35, codesPagedResult.getTotalRows());
            assertEquals(35, codesPagedResult.getValues().size());
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
            assertEquals(CODELIST_9_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_10_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_10_V2_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_10_V3_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_11_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_12_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_13_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_13_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_13_V1_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_14_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_14_V1_CODE_2, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_14_V1_CODE_3, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CODELIST_14_V1_CODE_1_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(codesPagedResult.getValues().size(), i);
        }

        // Find by name (like), code (like) and codelist urn
        {
            String name = "codelist-1-v2-code-2-";
            String code = "CODE02";
            String codelistUrn = CODELIST_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).withProperty(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(codelistUrn)
                    .withProperty(CodeProperties.nameableArtefact().code()).like(code + "%").withProperty(CodeProperties.nameableArtefact().name().texts().label()).like("%" + name + "%")
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

    @Test
    @Override
    public void testFindCodesByConditionCanBeVariableElementGeographicalGranularity() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodeMetamac> codesPagedResult = codesService.findCodesByConditionCanBeVariableElementGeographicalGranularity(ctx, null, pagingParameter);

        // Validate
        assertEquals(1, codesPagedResult.getTotalRows());
        assertEquals(1, codesPagedResult.getValues().size());

        int i = 0;
        assertEquals(CODELIST_10_V1_CODE_1, codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(codesPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    @DirtyDatabase
    public void testImportCodesTsv() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-01.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = true;

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodesTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, updateAlreadyExisting, null, Boolean.TRUE);
                    assertEquals(true, taskImportTsvInfo.getIsPlannedInBackground());
                    assertNotNull(taskImportTsvInfo.getJobKey());

                    jobKey.append(taskImportTsvInfo.getJobKey());

                    CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
                    assertEquals(true, codelistVersion.getItemScheme().getIsTaskInBackground());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(6, task.getTaskResults().size());
        int i = 0;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED.getCode(), "CODE01", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED.getCode(), "CODE02", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND.getCode(), "VARIABLE_ELEMENT_NOT_EXISTS#@#code01b", Boolean.FALSE, TaskResultTypeEnum.INFO, task
                .getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND.getCode(), "VARIABLE_ELEMENT_NOT_EXISTS#@#code8", Boolean.FALSE, TaskResultTypeEnum.INFO, task
                .getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED.getCode(), "CODE03", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED.getCode(), "CODE0201", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate item scheme
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, codelistVersion.getItemScheme().getIsTaskInBackground());

        // Validate codes

        /**
         * RESULT:
         * ------------------------------------------------------------------------------------------------------------
         * ALPHABETICAL
         * ------------------------------------------------------------------------------------------------------------
         * CODE01
         * -->CODE0201
         * ---->CODE020101
         * -->code8
         * code01b
         * CODE02
         * -->CODE0202
         * CODE03
         * CODE04
         * -->CODE0400
         * -->CODE0401
         * ---->CODE040101
         * ---->CODE040102
         * code1
         * -->code2
         * ----->code6
         * -->code5
         * code3
         * code7
         * ------------------------------------------------------------------------------------------------------------
         * VISUALISATION01
         * ------------------------------------------------------------------------------------------------------------
         * CODE01
         * -->CODE0201
         * ---->CODE020101
         * -->code8
         * CODE02
         * -->CODE0202
         * CODE03
         * CODE04
         * -->CODE0401
         * ---->CODE040101
         * ---->CODE040102
         * -->CODE0400
         * code01b
         * code1
         * -->code2
         * ----->code6
         * -->code5
         * code3
         * code7
         * ------------------------------------------------------------------------------------------------------------
         * VISUALISATION01
         * ------------------------------------------------------------------------------------------------------------
         * CODE03
         * CODE01
         * -->CODE0201
         * ---->CODE020101
         * -->code8
         * CODE02
         * -->CODE0202
         * CODE04
         * -->CODE0401
         * ---->CODE040101
         * ---->CODE040102
         * -->CODE0400
         * code01b
         * code1
         * -->code2
         * ----->code6
         * -->code5
         * code3
         * code7
         */

        String codelistUrnPart = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).";
        CodeMetamac code1 = null;
        CodeMetamac code2 = null;
        {
            // UPDATE EXISTING: CODE01 nombre nuevo 1 new name 1 description new 1
            String semanticIdentifier = "CODE01";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf("121"), code.getId());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre nuevo 1", "en", "new name 1");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "en", "description new 1", null, null);
            assertEquals(null, code.getVariableElement());
            BaseAsserts.assertEqualsDay(new DateTime(2011, 01, 01, 01, 02, 03, 0), code.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getLastUpdated()); // today
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.FALSE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code1 VARIABLE_ELEMENT_01 Nombre 1 Name 1 Nombre it 1 Description 1 Descripción 1
            String semanticIdentifier = "code1";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            code1 = code;
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre 1", "en", "Name 1", "it", "Nombre it 1");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "Descripción 1", "en", "Description 1");
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getIdentifiableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(5), code.getOrder1());
            assertEquals(Integer.valueOf(5), code.getOrder2());
            assertEquals(Integer.valueOf(5), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // UPDATE EXISTING: CODE02 VARIABLE_ELEMENT_03 nombre nuevo 2 con ácéntós new name 2 nombre nuevo it 2 descripción nueva 2 description new 2
            String semanticIdentifier = "CODE02";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf("122"), code.getId());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre nuevo con ácéntós 2", "en", "new name 2", "it", "nombre nuevo it 2");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "en", "description new 2", "es", "descripción nueva 2");
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertEquals(null, code.getShortName());
            BaseAsserts.assertEqualsDay(new DateTime(2011, 03, 02, 04, 05, 06, 0), code.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getLastUpdated()); // today
            assertEquals(Integer.valueOf(2), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(2), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.FALSE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code2 code1 VARIABLE_ELEMENT_01 Nombre 2 Name 2 Description 2
            String semanticIdentifier = "code2";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            code2 = code;
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(code1.getNameableArtefact().getUrn(), code.getParent().getNameableArtefact().getUrn());
            assertEquals(code1.getId(), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre 2", "en", "Name 2");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "en", "Description 2", null, null);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getIdentifiableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code3 VARIABLE_ELEMENT_02 Name 3 Nombre it 3
            String semanticIdentifier = "code3";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "en", "Name 3", "it", "Nombre it 3");
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getIdentifiableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(6), code.getOrder1());
            assertEquals(Integer.valueOf(6), code.getOrder2());
            assertEquals(Integer.valueOf(6), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // UPDATE EXISTING: CODE03 VARIABLE_ELEMENT_04 nombre nuevo 03
            String semanticIdentifier = "CODE03";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf("123"), code.getId());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre nuevo 03", null, null);
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_4, code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertEquals(null, code.getShortName());
            BaseAsserts.assertEqualsDay(new DateTime(2011, 01, 01, 01, 02, 03, 0), code.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getLastUpdated()); // today
            assertEquals(Integer.valueOf(3), code.getOrder1());
            assertEquals(Integer.valueOf(2), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.FALSE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code01b VARIABLE_ELEMENT_NOT_EXISTS Name 01B Nombre it 01B
            String semanticIdentifier = "code01b";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "en", "Name 01B", "it", "Nombre it 01B");
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(null, code.getVariableElement()); // not exists
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(4), code.getOrder2());
            assertEquals(Integer.valueOf(4), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // CODE0400 CODE04 Nombre 4 name 4 Nombre it 4 Description 4 Descripción 4
            String semanticIdentifier = "CODE0400";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(CODELIST_1_V2_CODE_4, code.getParent().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(124), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre 4", "en", "name 4", "it", "Nombre it 4");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "en", "Description 4", "es", "Descripción 4");
            assertEquals(null, code.getVariableElement());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // CODE040102 CODE040102 Nombre CODE040102 name CODE040102 Nombre it CODE040102 Descripción CODE040102
            String semanticIdentifier = "CODE040102";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(CODELIST_1_V2_CODE_4_1, code.getParent().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(1241), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre CODE040102", "en", "name CODE040102", "it", "Nombre it CODE040102");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "Descripción CODE040102", null, null);
            assertEquals(null, code.getVariableElement());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code5 code1 VARIABLE_ELEMENT_03 Nombre it 5 Description 5 Descripción 5
            String semanticIdentifier = "code5";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code1.getNameableArtefact().getUrn(), code.getParent().getNameableArtefact().getUrn());
            assertEquals(code1.getId(), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "it", "Nombre it 5", null, null);
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "Descripción 5", "en", "Description 5");
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, code.getVariableElement().getIdentifiableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code6 code2 Nombre it 6
            String semanticIdentifier = "code6";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code2.getNameableArtefact().getUrn(), code.getParent().getNameableArtefact().getUrn());
            assertEquals(code2.getId(), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "it", "Nombre it 6", null, null);
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(null, code.getVariableElement());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code7 VARIABLE_ELEMENT_01 Nombre 7
            String semanticIdentifier = "code7";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre 7", null, null);
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getIdentifiableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(7), code.getOrder1());
            assertEquals(Integer.valueOf(7), code.getOrder2());
            assertEquals(Integer.valueOf(7), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // code8 CODE01 VARIABLE_ELEMENT_NOT_EXISTS Nombre 8
            String semanticIdentifier = "code8";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(CODELIST_1_V2_CODE_1, code.getParent().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf("121"), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre 8", null, null);
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(null, code.getVariableElement());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getCreatedDate()); // today
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // UPDATE EXISTING: CODE0201 CODE01 Nombre CODE0201 name CODE0201 Nombre it CODE0201 Descripción CODE0201
            String semanticIdentifier = "CODE0201";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf(1221), code.getId());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(CODELIST_1_V2_CODE_1, code.getParent().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(121), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre CODE0201", "en", "name CODE0201", "it", "Nombre it CODE0201");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "Descripción CODE0201", null, null);
            assertEquals(null, code.getVariableElement());
            BaseAsserts.assertEqualsDay(new DateTime(2011, 01, 01, 01, 02, 03, 0), code.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(), code.getLastUpdated()); // today
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // CODE04 (not updated)
            String semanticIdentifier = "CODE04";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Integer.valueOf(4), code.getOrder1());
            assertEquals(Integer.valueOf(3), code.getOrder2());
            assertEquals(Integer.valueOf(3), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.FALSE, code.getOpenness2());
            assertEquals(Boolean.FALSE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCodesTsvCharsetIso() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-01-charset-iso.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = true;

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodesTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, updateAlreadyExisting, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());

        // Validate codes
        String codelistUrnPart = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).";
        {
            String semanticIdentifier = "code1";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre con acéntós 1", null, null);
        }
        {
            String semanticIdentifier = "code2";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre con eñe 2", null, null);
        }
        {
            String semanticIdentifier = "code3";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "nombre con símbolos raros @#~%&12345678 áéíóúÁÉÍÓÚäëïöü 3", null, null);
        }

    }

    @Test
    @DirtyDatabase
    public void testImportCodesTsvNotUpdatingAlreadyExistingCodes() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-01.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = false;

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodesTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, updateAlreadyExisting, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        entityManager.clear();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(6, task.getTaskResults().size());
        int i = 0;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED.getCode(), "CODE01", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED.getCode(), "CODE02", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND.getCode(), "VARIABLE_ELEMENT_NOT_EXISTS#@#code01b", Boolean.FALSE, TaskResultTypeEnum.INFO, task
                .getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND.getCode(), "VARIABLE_ELEMENT_NOT_EXISTS#@#code8", Boolean.FALSE, TaskResultTypeEnum.INFO, task
                .getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED.getCode(), "CODE03", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED.getCode(), "CODE0201", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate codes (only check that existing codes not updated)

        String codelistUrnPart = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST01(02.000).";
        {
            // NOT UPDATED: CODE01 nombre nuevo 1 new name 1 description new 1
            String semanticIdentifier = "CODE01";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf("121"), code.getId());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Isla de Tenerife", "en", "Name codelist-1-v2-code-1");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "Descripción codelist-1-v2-code-1", null, null);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.FALSE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // NOT UPDATED: CODE02 VARIABLE_ELEMENT_03 nombre nuevo 2 new name 2 nombre nuevo it 2 descripción nueva 2 description new 2
            String semanticIdentifier = "CODE02";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf("122"), code.getId());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-1-v2-code-2 Canaria, Gran", null, null);
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(null, code.getVariableElement());
            assertEqualsInternationalString(code.getShortName(), "es", "nombre corto code2", "en", "short name code2");
            assertEquals(Integer.valueOf(2), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(2), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.FALSE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // NOT UPDATED: CODE03 VARIABLE_ELEMENT_04 nombre nuevo 03
            String semanticIdentifier = "CODE03";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf("123"), code.getId());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(null, code.getParent());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Fuerteventura", "it", "nombre it code-3", "en", "name code-3");
            assertEquals(null, code.getNameableArtefact().getDescription());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertEquals(null, code.getShortName());
            assertEquals(Integer.valueOf(3), code.getOrder1());
            assertEquals(Integer.valueOf(2), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.FALSE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
        {
            // NOT UPDATED: CODE0201 CODE01 Nombre CODE0201 name CODE0201 Nombre it CODE0201 Descripción CODE0201
            String semanticIdentifier = "CODE0201";
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), codelistUrnPart + semanticIdentifier);
            assertEquals(Long.valueOf(1221), code.getId());
            assertEquals(code.getNameableArtefact().getUrn(), code.getNameableArtefact().getUrnProvider());
            assertEquals(semanticIdentifier, code.getNameableArtefact().getCode());
            assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(122), code.getParent().getId());
            assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "codelist-1-v2-code-2- Isla de La Gomera", "en", "Name codelist-1-v2-code-2-1");
            assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "descripción CODELIST_1_V2_CODE_2_1", "en", "description CODELIST_1_V2_CODE_2_1");
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, code.getVariableElement().getIdentifiableArtefact().getUrn());
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
            assertEquals(Boolean.TRUE, code.getOpenness1());
            assertEquals(Boolean.TRUE, code.getOpenness2());
            assertEquals(Boolean.TRUE, code.getOpenness3());
            assertEquals(null, code.getOpenness4());
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCodesTsvErrorWithHeaderIncorrect() throws Exception {

        final String codelistUrn = CODELIST_1_V2;

        // Import
        final String fileName = "importation-code-02-errors-header.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = false;
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodesTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, updateAlreadyExisting, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN.getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE, Boolean.FALSE, type, task
                .getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate item scheme
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(false, codelistVersion.getItemScheme().getIsTaskInBackground());
    }

    @Test
    @DirtyDatabase
    public void testImportCodesTsvErrorWithBodyIncorrect() throws Exception {

        final String codelistUrn = CODELIST_1_V2;

        // Import
        final String fileName = "importation-code-03-errors-body.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = false;
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodesTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, updateAlreadyExisting, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(5, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER.getCode(), "%code1#@#parameter.srm.importation.code", Boolean.FALSE, type, task
                .getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND.getCode(), "parentNotExists#@#codeParentNotExists", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED.getCode(), "codeWithoutName#@#parameter.srm.importation.name", Boolean.FALSE, type, task.getTaskResults()
                .get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT.getCode(), "5", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Override
    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsv() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-01.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    assertEquals(true, taskImportTsvInfo.getIsPlannedInBackground());
                    assertNotNull(taskImportTsvInfo.getJobKey());

                    jobKey.append(taskImportTsvInfo.getJobKey());

                    CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
                    assertEquals(true, codelistVersion.getItemScheme().getIsTaskInBackground());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());

        // Validate item scheme
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEqualsDate("2011-01-01 01:02:03", codelistVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, codelistVersion.getItemScheme().getIsTaskInBackground());

        // Validate orders
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(3), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(2), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_3);
            assertEquals(Integer.valueOf(2), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(3), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4);
            assertEquals(Integer.valueOf(3), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(2), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_1);
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(1), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_2);
            assertEquals(Integer.valueOf(1), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(1), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2_1_1);
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4_1);
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
        {
            CodeMetamac code = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4_1_1);
            assertEquals(Integer.valueOf(0), code.getOrder1());
            assertEquals(Integer.valueOf(0), code.getOrder2());
            assertEquals(Integer.valueOf(0), code.getOrder3());
            assertEquals(null, code.getOrder4());
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsvErrorHeaderIncorrectCodeNotFound() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-02-errors-header-01.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN.getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE, Boolean.FALSE, type, task
                .getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate item scheme
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(false, codelistVersion.getItemScheme().getIsTaskInBackground());
    }

    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsvErrorHeaderColumnVisualisationNotFound() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-02-errors-header-02.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN.getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ORDER, Boolean.FALSE, type, task
                .getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsvErrorHeaderIncorrectVisualisationAlphabetical() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-02-errors-header-03-alphabetical.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR_ALPHABETICAL_VISUALISATION_NOT_SUPPORTED.getCode(), null, Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsvErrorBodyIncorrectNumberCodes() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-03-errors-body-01.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR_INCORRECT_NUMBER_CODES.getCode(), "9#@#6", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsvErrorBodyVisualisationNotFound() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-03-errors-body-02.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR_ORDER_VISUALISATION_NOT_FOUND.getCode(),
                "VISUALISATION001#@#urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST01(02.000)", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Test
    @DirtyDatabase
    public void testImportCodeOrdersTsvErrorBodyOrderEmpty() throws Exception {

        final String codelistUrn = CODELIST_1_V2;
        final String fileName = "importation-code-orders-03-errors-body-03.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn, stream, null, fileName, null, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(3, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED.getCode(), "CODE0201#@#parameter.srm.importation.order", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED.getCode(), "CODE04#@#parameter.srm.importation.order", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @DirtyDatabase
    @Test
    public void testClearDirtyDatabaseBecausePreviuosTestCanNotHaveNotTransactionalAnnotation() throws Exception {
    }

    @Override
    @Test
    public void testNormaliseVariableElementsToCodes() throws Exception {

        String codelistUrn = CODELIST_1_V2;
        List<CodeVariableElementNormalisationResult> results = codesService.normaliseVariableElementsToCodes(getServiceContextAdministrador(), codelistUrn, "es", false);
        assertEquals(9, results.size());

        {
            String codeUrn = CODELIST_1_V2_CODE_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Isla de Tenerife", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_7, result.getVariableElementProposed().getUrn());
            assertEquals("Tenerife", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Nombre codelist-1-v2-code-2 Canaria, Gran", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, result.getVariableElementProposed().getUrn());
            assertEquals("Gran Canaria", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("codelist-1-v2-code-2- Isla de La Gomera", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_6, result.getVariableElementProposed().getUrn());
            assertEquals("La Gomera", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2_1_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Santa Cruz de La Palma codelist-1-v2-code-2-1-1", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_5, result.getVariableElementProposed().getUrn());
            assertEquals("La Palma", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2_2;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Isla de El Hierro", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getVariableElementProposed().getUrn());
            assertEquals("El Hierro", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_3;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Fuerteventura", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, result.getVariableElementProposed().getUrn());
            assertEquals("Fuerteventura", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_4;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Lanzarote", result.getCode().getName());
            assertEquals(null, result.getVariableElementProposed());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_4_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Canarias, Tenerife", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_7, result.getVariableElementProposed().getUrn());
            assertEquals("Tenerife", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_4_1_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Nombre codelist-1-v2-code-4-1-1", result.getCode().getName());
            assertEquals(null, result.getVariableElementProposed());
        }
    }

    @Test
    public void testNormaliseVariableElementsToCodesOnlyWithoutVariableElements() throws Exception {

        String codelistUrn = CODELIST_1_V2;
        List<CodeVariableElementNormalisationResult> results = codesService.normaliseVariableElementsToCodes(getServiceContextAdministrador(), codelistUrn, "es", true);
        assertEquals(9, results.size());

        {
            String codeUrn = CODELIST_1_V2_CODE_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Isla de Tenerife", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, result.getVariableElementProposed().getUrn()); // not overrided
            assertEquals("Fuerteventura", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Nombre codelist-1-v2-code-2 Canaria, Gran", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, result.getVariableElementProposed().getUrn()); // it was empty
            assertEquals("Gran Canaria", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("codelist-1-v2-code-2- Isla de La Gomera", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getVariableElementProposed().getUrn()); // overrided
            assertEquals("El Hierro", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2_1_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Santa Cruz de La Palma codelist-1-v2-code-2-1-1", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_5, result.getVariableElementProposed().getUrn()); // it was empty
            assertEquals("La Palma", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_2_2;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Isla de El Hierro", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getVariableElementProposed().getUrn()); // it was empty
            assertEquals("El Hierro", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_3;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Fuerteventura", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, result.getVariableElementProposed().getUrn()); // overrided
            assertEquals("Gran Canaria", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_4;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Lanzarote", result.getCode().getName());
            assertEquals(null, result.getVariableElementProposed()); // it was empty
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_4_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Canarias, Tenerife", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_7, result.getVariableElementProposed().getUrn()); // it was empty
            assertEquals("Tenerife", result.getVariableElementProposed().getShortName());
        }
        {
            String codeUrn = CODELIST_1_V2_CODE_4_1_1;
            CodeVariableElementNormalisationResult result = assertContainsCodeVariableElementNormalisationResult(codeUrn, results);
            assertEquals("Nombre codelist-1-v2-code-4-1-1", result.getCode().getName());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getVariableElementProposed().getUrn()); // not overrided
            assertEquals("El Hierro", result.getVariableElementProposed().getShortName());
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

    @Test
    public void testCreateCodelistFamilyErrorMetadataTranslations() throws Exception {
        CodelistFamily codelistFamily = CodesMetamacDoMocks.mockCodelistFamily();
        codelistFamily.getNameableArtefact().setName(new InternationalString());
        codelistFamily.getNameableArtefact().getName().addText(new LocalisedString("aa", "Label"));
        try {
            codesService.createCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("translations");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testUpdateCodelistFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(ctx, CODELIST_FAMILY_1);
        codelistFamily.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codelistFamily.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());

        CodelistFamily codelistFamilyUpdated = codesService.updateCodelistFamily(ctx, codelistFamily);

        assertEqualsCodelistFamily(codelistFamily, codelistFamilyUpdated);
    }

    @Test
    public void testUpdateCodelistFamilyErrorCodeUnmodifiable() throws Exception {
        CodelistFamily codelistFamily = codesService.retrieveCodelistFamilyByUrn(getServiceContextAdministrador(), CODELIST_FAMILY_1);
        codelistFamily.getNameableArtefact().setCode("newCode");
        codelistFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateCodelistFamily(getServiceContextAdministrador(), codelistFamily);
            fail("code unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNMODIFIABLE, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
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
    public void testDeleteCodelistFamilyWithCodelistsAndPublishedCodelists() throws Exception {

        String familyUrn = CODELIST_FAMILY_2;
        String codelistPublishedUrn = CODELIST_1_V1;
        String codelistDraftUrn = CODELIST_9_V1;

        CodelistVersionMetamac codelistPublished = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistPublishedUrn);
        assertEquals(familyUrn, codelistPublished.getFamily().getNameableArtefact().getUrn());
        CodelistVersionMetamac codelistDraft = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistDraftUrn);
        assertEquals(familyUrn, codelistDraft.getFamily().getNameableArtefact().getUrn());

        codesService.deleteCodelistFamily(getServiceContextAdministrador(), familyUrn);

        codelistPublished = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistPublishedUrn);
        assertNull(codelistPublished.getFamily());
        codelistDraft = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistDraftUrn);
        assertNull(codelistDraft.getFamily());
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

    @Test
    public void testAddCodelistsPublishedToCodelistFamily() throws Exception {

        String codelistFamilyUrn = CODELIST_FAMILY_1;
        List<String> codelistUrns = new ArrayList<String>();
        {
            String codelistUrn = CODELIST_1_V1; // change family
            codelistUrns.add(codelistUrn);
            CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
            assertEquals(CODELIST_FAMILY_2, codelist.getFamily().getNameableArtefact().getUrn());
        }
        codesService.addCodelistsToCodelistFamily(getServiceContextAdministrador(), codelistUrns, codelistFamilyUrn);

        // Validation
        {
            CodelistVersionMetamac codelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1);
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

    @Test
    public void testCreateVariableFamilyErrorMetadataTranslations() throws Exception {
        VariableFamily variableFamily = CodesMetamacDoMocks.mockVariableFamily();
        variableFamily.getNameableArtefact().setName(new InternationalString());
        variableFamily.getNameableArtefact().getName().addText(new LocalisedString("aa", "Label"));
        try {
            codesService.createVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("translations");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testUpdateVariableFamily() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_1);
        variableFamily.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        variableFamily.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());

        VariableFamily variableFamilyUpdated = codesService.updateVariableFamily(ctx, variableFamily);

        assertEqualsVariableFamily(variableFamily, variableFamilyUpdated);
    }

    @Test
    public void testUpdateVariableFamilyErrorCodeUnmodifiable() throws Exception {
        VariableFamily variableFamily = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1);
        variableFamily.getNameableArtefact().setCode("newIdentifier");
        variableFamily.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariableFamily(getServiceContextAdministrador(), variableFamily);
            fail("code unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNMODIFIABLE, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
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
        Variable variableReplaced1 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_4);
        Variable variableReplaced2 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_5);
        variable.getReplaceToVariables().add(variableReplaced1);
        variable.getReplaceToVariables().add(variableReplaced2);

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
        assertEquals(variableReplaced1.getNameableArtefact().getUrn(), variableRetrieved.getReplaceToVariables().get(0).getNameableArtefact().getUrn());
        assertEquals(variableReplaced2.getNameableArtefact().getUrn(), variableRetrieved.getReplaceToVariables().get(1).getNameableArtefact().getUrn());

        // Check replaced by metadata
        variableReplaced1 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variableReplaced1.getNameableArtefact().getUrn());
        assertEquals(urn, variableReplaced1.getReplacedByVariable().getNameableArtefact().getUrn());
        variableReplaced2 = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variableReplaced2.getNameableArtefact().getUrn());
        assertEquals(urn, variableReplaced2.getReplacedByVariable().getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateVariableGeographical() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(ctx, VARIABLE_FAMILY_1));
        variable.setType(VariableTypeEnum.GEOGRAPHICAL);

        // Create
        Variable variableCreated = codesService.createVariable(ctx, variable);
        String urn = variableCreated.getNameableArtefact().getUrn();

        // Validate
        Variable variableRetrieved = codesService.retrieveVariableByUrn(ctx, urn);
        assertEquals(VariableTypeEnum.GEOGRAPHICAL, variableRetrieved.getType());
    }

    @Test
    public void testCreateVariableErrorReplaceTo() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1));
        variable.getReplaceToVariables().add(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));
        try {
            codesService.createVariable(getServiceContextAdministrador(), variable);
            fail("wrong replace to");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED, 1, new String[]{VARIABLE_1}, e.getExceptionItems().get(0));
        }
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

    @Test
    public void testCreateVariableErrorMetadataMaximumLength() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1));
        variable.getShortName().getLocalisedLabelEntity("es").setLabel(MetamacMocks.mockString(1000));
        try {
            codesService.createVariable(getServiceContextAdministrador(), variable);
            fail("metadata incorrect length");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_MAXIMUM_LENGTH, 2,
                    new Serializable[]{ServiceExceptionParameters.VARIABLE_SHORT_NAME, Integer.valueOf(SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH)}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableErrorMetadataTranslations() throws Exception {
        Variable variable = CodesMetamacDoMocks.mockVariable();
        variable.addFamily(codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), VARIABLE_FAMILY_1));
        variable.getNameableArtefact().setName(new InternationalString());
        variable.getNameableArtefact().getName().addText(new LocalisedString("aa", "Label"));
        variable.setShortName(new InternationalString());
        variable.getShortName().addText(new LocalisedString("aa2", "Label"));
        try {
            codesService.createVariable(getServiceContextAdministrador(), variable);
            fail("translations");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.VARIABLE_SHORT_NAME}, e.getExceptionItems().get(1));
        }
    }

    @Override
    @Test
    public void testUpdateVariable() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
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
        variable.getReplaceToVariables().add(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1));
        variable.getReplaceToVariables().add(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_4));
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
    public void testUpdateVariableTypeGeographicalToNull() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_5);
        assertEquals(VariableTypeEnum.GEOGRAPHICAL, variable.getType());
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        variable.setPreviousType(variable.getType());
        variable.setType(null);

        // Check variable elements before update
        {
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_1);
            assertNotNull(variableElement.getLatitude());
            assertNotNull(variableElement.getLongitude());
            assertNotNull(variableElement.getShape());
            assertNotNull(variableElement.getGeographicalGranularity());
        }
        {
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_2);
            assertNotNull(variableElement.getLatitude());
            assertNotNull(variableElement.getLongitude());
            assertNotNull(variableElement.getShape());
            assertNotNull(variableElement.getGeographicalGranularity());
        }

        Variable variableUpdated = codesService.updateVariable(getServiceContextAdministrador(), variable);
        assertEquals(null, variableUpdated.getType());
        assertEquals(VariableTypeEnum.GEOGRAPHICAL, variableUpdated.getPreviousType());

        entityManager.clear();

        // Check variable elements
        {
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_1);
            assertNull(variableElement.getLatitude());
            assertNull(variableElement.getLongitude());
            assertNull(variableElement.getShape());
            assertNull(variableElement.getGeographicalGranularity());
        }
        {
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_2);
            assertNull(variableElement.getLatitude());
            assertNull(variableElement.getLongitude());
            assertNull(variableElement.getShape());
            assertNull(variableElement.getGeographicalGranularity());
        }

        // Check another variable elements is not clear
        {
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_4_VARIABLE_ELEMENT_1);
            assertNotNull(variableElement.getLatitude());
            assertNotNull(variableElement.getLongitude());
            assertNotNull(variableElement.getShape());
            assertNotNull(variableElement.getGeographicalGranularity());
        }
    }

    @Test
    public void testUpdateVariableTypeErrorNullToGeographicalWithVariableElements() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        assertEquals(null, variable.getType());
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        variable.setPreviousType(variable.getType());
        variable.setType(VariableTypeEnum.GEOGRAPHICAL);

        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("updating type is unsupported with variable elements");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_TYPE_UPDATE_TO_GEOGRAPHICAL_UNSUPPORTED, 1, new String[]{variable.getNameableArtefact().getUrn()}, e.getExceptionItems()
                    .get(0));
        }
    }

    @Test
    public void testUpdateVariableErrorWrongCode() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        variable.getNameableArtefact().setCode("newIdentifier");
        variable.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariable(getServiceContextAdministrador(), variable);
            fail("code unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNMODIFIABLE, 1, new String[]{ServiceExceptionParameters.URN}, e.getExceptionItems().get(0));
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
        assertNull(variable.getType());
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

        String variableUrn = VARIABLE_4;
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), variableUrn);

        assertEquals(2, variable.getFamilies().size());
        String family1Urn = VARIABLE_FAMILY_3;
        String family2Urn = VARIABLE_FAMILY_4;
        assertTrue(SrmServiceUtils.isFamilyInList(family1Urn, variable.getFamilies()));
        assertTrue(SrmServiceUtils.isFamilyInList(family2Urn, variable.getFamilies()));

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
        VariableFamily family1 = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), family1Urn);
        assertFalse(SrmServiceUtils.isVariableInList(variableUrn, family1.getVariables()));
        VariableFamily family2 = codesService.retrieveVariableFamilyByUrn(getServiceContextAdministrador(), family2Urn);
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

        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_5);
        CodeMetamac geographicalGranularity = codesService.retrieveCodeByUrn(ctx, CODELIST_1_V2_CODE_1);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElementGeographical(variable, geographicalGranularity);
        // Replace to
        VariableElement variableElementReplaced1 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_3);
        variableElement.getReplaceToVariableElements().add(variableElementReplaced1);

        // Create
        VariableElement variableElementCreated = codesService.createVariableElement(ctx, variableElement);
        String urn = variableElementCreated.getIdentifiableArtefact().getUrn();

        // Validate
        assertNotNull(urn);
        VariableElement variableElementRetrieved = codesService.retrieveVariableElementByUrn(ctx, urn);

        assertEquals(ctx.getUserId(), variableElementRetrieved.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), variableElementRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableElementRetrieved.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), variableElementRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), variableElementRetrieved.getLastUpdated().toDate()));

        assertEqualsVariableElement(variableElement, variableElementRetrieved);

        // Check replace metadata
        assertEquals(1, variableElementRetrieved.getReplaceToVariableElements().size());
        assertEquals(variableElementReplaced1.getIdentifiableArtefact().getUrn(), variableElementRetrieved.getReplaceToVariableElements().get(0).getIdentifiableArtefact().getUrn());

        // Check replaced by metadata
        variableElementReplaced1 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), variableElementReplaced1.getIdentifiableArtefact().getUrn());
        assertEquals(urn, variableElementReplaced1.getReplacedByVariableElement().getIdentifiableArtefact().getUrn());
    }

    @Test
    public void testCreateVariableElementNonGeographical() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_3);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);

        // Create
        VariableElement variableElementCreated = codesService.createVariableElement(ctx, variableElement);
        String urn = variableElementCreated.getIdentifiableArtefact().getUrn();

        // Validate
        assertNotNull(urn);
        VariableElement variableElementRetrieved = codesService.retrieveVariableElementByUrn(ctx, urn);
        assertEqualsVariableElement(variableElement, variableElementRetrieved);
    }

    @Test
    public void testCreateVariableElementErrorReplaceTo() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_2);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.getReplaceToVariableElements().add(codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1));
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("wrong replace to");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED, 1, new String[]{VARIABLE_2_VARIABLE_ELEMENT_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableElementErrorReplaceToDifferentFamily() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_3);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);

        // Replace to
        VariableElement variableElementAnotherFamily = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_1);
        variableElement.addReplaceToVariableElement(variableElementAnotherFamily);

        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("wrong code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE, 1, new String[]{VARIABLE_5_VARIABLE_ELEMENT_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableElementErrorWrongCode() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.getIdentifiableArtefact().setCode(" 0 - invalid identifier");
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
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_1);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.getIdentifiableArtefact().setCode("VARIABLE_ELEMENT_01");
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_1_VARIABLE_ELEMENT_1}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateVariableElementErrorIncorrectMetadata() throws Exception {
        VariableElement variableElement = new VariableElement();
        variableElement.setVariable(null);
        variableElement.setIdentifiableArtefact(new IdentifiableArtefact());
        variableElement.setValidFrom(null);
        variableElement.setValidTo(new DateTime());
        variableElement.setComment(new InternationalString());
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_COMMENT}, e.getExceptionItems().get(3));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VALID_TO}, e.getExceptionItems().get(4));
        }
    }

    @Test
    public void testCreateVariableElementErrorIncorrectMetadataGeographical() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_5);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.setLatitude(null);
        variableElement.setLongitude(null);
        variableElement.setShape(null);
        variableElement.setGeographicalGranularity(null);
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY}, e.getExceptionItems()
                    .get(0));
        }
    }

    @Test
    public void testCreateVariableElementErrorIncorrectMetadataNonGeographical() throws Exception {
        Variable variable = codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_3);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.setLatitude("1");
        variableElement.setLongitude("2");
        variableElement.setShape("3");
        variableElement.setGeographicalGranularity(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1));
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_LATITUDE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_LONGITUDE}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_SHAPE}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY}, e.getExceptionItems()
                    .get(3));
        }
    }

    @Test
    public void testCreateVariableElementErrorMetadataMaximumLength() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_3);
        VariableElement variableElement = CodesMetamacDoMocks.mockVariableElement(variable);
        variableElement.getShortName().getLocalisedLabelEntity("es").setLabel(MetamacMocks.mockString(1000));
        try {
            codesService.createVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata incorrect length");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_MAXIMUM_LENGTH, 2,
                    new Serializable[]{ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME, Integer.valueOf(SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH)}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testUpdateVariableElement() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        variableElement.getIdentifiableArtefact().setCode("code-" + MetamacMocks.mockString(10));
        variableElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.TRUE);
        variableElement.setShortName(BaseDoMocks.mockInternationalString());
        variableElement.setValidFrom(MetamacMocks.mockDateTime());
        variableElement.setValidTo(null);

        VariableElement variableElementUpdated = codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);

        assertEqualsVariableElement(variableElement, variableElementUpdated);
    }

    @Test
    public void testUpdateVariableElementErrorWrongCode() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        variableElement.getIdentifiableArtefact().setCode(" 0 - invalid identifier");
        variableElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.TRUE);
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
        variableElement.getIdentifiableArtefact().setCode("VARIABLE_ELEMENT_02");
        variableElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{VARIABLE_2_VARIABLE_ELEMENT_2}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableElementErrorChangeVariable() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_1_VARIABLE_ELEMENT_1);
        assertEquals(VARIABLE_1, variableElement.getVariable().getNameableArtefact().getUrn());
        variableElement.setVariable(codesService.retrieveVariableByUrn(getServiceContextAdministrador(), VARIABLE_2));
        variableElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.FALSE);
        try {
            codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);
            fail("variable unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_UNMODIFIABLE, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testUpdateVariableElementChangingReplaceTo() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String urn = VARIABLE_2_VARIABLE_ELEMENT_2;
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(ctx, urn);
        assertEquals(1, variableElement.getReplaceToVariableElements().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(VARIABLE_2_VARIABLE_ELEMENT_1, variableElement.getReplaceToVariableElements()));

        // Replace to
        variableElement.removeAllReplaceToVariableElements();
        variableElement.addReplaceToVariableElement(codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_3));
        variableElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.FALSE);
        codesService.updateVariableElement(ctx, variableElement);

        // Check replaced by metadata
        VariableElement variableElementReplaced2 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_3);
        assertEquals(urn, variableElementReplaced2.getReplacedByVariableElement().getIdentifiableArtefact().getUrn());
        VariableElement variableElementNotReplaced3 = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1);
        assertEquals(null, variableElementNotReplaced3.getReplacedByVariableElement());
    }

    @Test
    public void testUpdateVariableElementErrorIncorrectMetadata() throws Exception {
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_5_VARIABLE_ELEMENT_1);
        variableElement.setVariable(null);
        variableElement.getIdentifiableArtefact().setCode(null);
        variableElement.getIdentifiableArtefact().setIsCodeUpdated(null);
        variableElement.setShortName(null);
        variableElement.setValidFrom(null);
        variableElement.setValidTo(new DateTime());
        try {
            codesService.updateVariableElement(getServiceContextAdministrador(), variableElement);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE}, e.getExceptionItems().get(0));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_IS_CODE_UPDATED}, e.getExceptionItems().get(1));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE}, e.getExceptionItems().get(2));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME}, e.getExceptionItems().get(3));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.VARIABLE_ELEMENT_VALID_TO}, e.getExceptionItems().get(4));
        }
    }

    @Override
    @Test
    public void testRetrieveVariableElementByUrn() throws Exception {
        String urn = VARIABLE_2_VARIABLE_ELEMENT_2;
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);

        assertEquals(urn, variableElement.getIdentifiableArtefact().getUrn());
        assertEquals("VARIABLE_ELEMENT_02", variableElement.getIdentifiableArtefact().getCode());
        assertEqualsInternationalString(variableElement.getShortName(), "es", "Fuerteventura", "en", "Short name variableElement 2-2");
        assertEqualsInternationalString(variableElement.getComment(), "es", "comentario ve", "en", "comment ve");
        assertEqualsDate(new DateTime(2011, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variableElement.getValidFrom());
        assertEqualsDate(new DateTime(2012, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variableElement.getValidTo());
        assertEquals(VARIABLE_2, variableElement.getVariable().getNameableArtefact().getUrn());

        assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, variableElement.getReplacedByVariableElement().getIdentifiableArtefact().getUrn());
        assertEquals(1, variableElement.getReplaceToVariableElements().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(VARIABLE_2_VARIABLE_ELEMENT_1, variableElement.getReplaceToVariableElements()));

        assertNull(variableElement.getVariable().getType());
        assertNull(variableElement.getLatitude());
        assertNull(variableElement.getLongitude());
        assertNull(variableElement.getShape());
        assertNull(variableElement.getGeographicalGranularity());

        assertEquals("variable-element-22", variableElement.getUuid());
        assertEquals("user1", variableElement.getCreatedBy());
        assertEquals("user2", variableElement.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variableElement.getVersion());
    }

    @Test
    public void testRetrieveVariableElementGeographicalByUrn() throws Exception {
        String urn = VARIABLE_5_VARIABLE_ELEMENT_1;
        VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(VariableTypeEnum.GEOGRAPHICAL, variableElement.getVariable().getType());
        assertEquals(urn, variableElement.getIdentifiableArtefact().getUrn());
        assertEquals("70° 55' 59''", variableElement.getLatitude());
        assertEquals("90° 30' 45''", variableElement.getLongitude());
        assertEquals("{shape1:1}", variableElement.getShape());
        assertEquals(CODELIST_10_V1_CODE_1, variableElement.getGeographicalGranularity().getNameableArtefact().getUrn());
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
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).orderBy(VariableElementProperties.identifiableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableElement> result = codesService.findVariableElementsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(13, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_4, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_5, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_6, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_7, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_8, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_4_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_2, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_3, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
        }
        // Find by urn
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).withProperty(VariableElementProperties.identifiableArtefact().urn())
                    .like(VARIABLE_2_VARIABLE_ELEMENT_1).orderBy(VariableElementProperties.identifiableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableElement> result = codesService.findVariableElementsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testFindVariableElementsForCodesByCondition() throws Exception {
        String codelistUrn = CODELIST_7_V2;
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(VARIABLE_5, codelistVersion.getVariable().getNameableArtefact().getUrn());

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).orderBy(VariableElementProperties.identifiableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableElement> result = codesService.findVariableElementsForCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter, codelistUrn);

            assertEquals(3, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_2, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_3, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
        }
        // Find by short name
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).withProperty(VariableElementProperties.shortName().texts().label())
                    .like("Nombre corto 5-1").orderBy(VariableElementProperties.identifiableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<VariableElement> result = codesService.findVariableElementsForCodesByCondition(getServiceContextAdministrador(), conditions, pagingParameter, codelistUrn);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testRetrieveVariableElementsByVariable() throws Exception {
        {
            String variableUrn = VARIABLE_6;
            String locale = "es";
            List<VariableElementResult> result = codesService.retrieveVariableElementsByVariable(getServiceContextAdministrador(), variableUrn, locale);
            assertEquals(0, result.size());
        }
        {
            String variableUrn = VARIABLE_2;
            String locale = "es";
            List<VariableElementResult> result = codesService.retrieveVariableElementsByVariable(getServiceContextAdministrador(), variableUrn, locale);
            assertEquals(8, result.size());
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_1, result);
                assertEquals(Long.valueOf(21), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_01", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, ve.getUrn());
                assertEquals("El Hierro", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_2, result);
                assertEquals(Long.valueOf(22), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_02", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_2, ve.getUrn());
                assertEquals("Fuerteventura", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_3, result);
                assertEquals(Long.valueOf(23), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_03", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, ve.getUrn());
                assertEquals("Gran Canaria", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_4, result);
                assertEquals(Long.valueOf(24), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_04", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_4, ve.getUrn());
                assertEquals(null, ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_5, result);
                assertEquals(Long.valueOf(25), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_05", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_5, ve.getUrn());
                assertEquals("La Palma", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_6, result);
                assertEquals(Long.valueOf(26), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_06", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_6, ve.getUrn());
                assertEquals("La Gomera", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_7, result);
                assertEquals(Long.valueOf(27), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_07", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_7, ve.getUrn());
                assertEquals("Tenerife", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_2_VARIABLE_ELEMENT_8, result);
                assertEquals(Long.valueOf(28), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_08", ve.getCode());
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_8, ve.getUrn());
            }
        }
        {
            String variableUrn = VARIABLE_5;
            String locale = "en";
            List<VariableElementResult> result = codesService.retrieveVariableElementsByVariable(getServiceContextAdministrador(), variableUrn, locale);
            assertEquals(3, result.size());
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_5_VARIABLE_ELEMENT_1, result);
                assertEquals(Long.valueOf(51), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_01", ve.getCode());
                assertEquals(VARIABLE_5_VARIABLE_ELEMENT_1, ve.getUrn());
                assertEquals("Short name 5-1", ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_5_VARIABLE_ELEMENT_2, result);
                assertEquals(Long.valueOf(52), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_02", ve.getCode());
                assertEquals(VARIABLE_5_VARIABLE_ELEMENT_2, ve.getUrn());
                assertEquals(null, ve.getShortName());
            }
            {
                VariableElementResult ve = assertContainsVariableElementResult(VARIABLE_5_VARIABLE_ELEMENT_3, result);
                assertEquals(Long.valueOf(53), ve.getIdDatabase());
                assertEquals("VARIABLE_ELEMENT_03", ve.getCode());
                assertEquals(VARIABLE_5_VARIABLE_ELEMENT_3, ve.getUrn());
                assertEquals("short name ve5-3", ve.getShortName());
            }
        }
    }

    @Override
    @Test
    public void testDeleteVariableElement() throws Exception {

        String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_6;
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
    public void testDeleteVariableElementErrorWithOperations() throws Exception {
        String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_4;

        try {
            codesService.deleteVariableElement(getServiceContextAdministrador(), variableElementUrn);
            fail("variableElement has operations");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_ELEMENT_WITH_OPERATIONS, 1, new String[]{variableElementUrn}, e.getExceptionItems().get(0));
        }
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
    // CODELIST ORDER VISUALISATIONS
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testRetrieveCodelistOrderVisualisationByUrn() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        String orderVisualisationUrn = CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL;
        CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), orderVisualisationUrn);

        // Validate
        assertNotNull(codelistOrderVisualisation);
        assertEquals("ALPHABETICAL", codelistOrderVisualisation.getNameableArtefact().getCode());
        assertEquals(orderVisualisationUrn, codelistOrderVisualisation.getNameableArtefact().getUrn());
        assertEquals(codelistUrn, codelistOrderVisualisation.getCodelistVersion().getMaintainableArtefact().getUrn());
        assertEqualsInternationalString(codelistOrderVisualisation.getNameableArtefact().getName(), "es", "visualización - órdenes 1-2-1", "en", "order - visualisation 1-2-1");
        assertEquals(Integer.valueOf(1), codelistOrderVisualisation.getColumnIndex());

        assertEquals("user1", codelistOrderVisualisation.getCreatedBy());
        assertEqualsDate("2011-01-01 01:02:03", codelistOrderVisualisation.getCreatedDate());
        assertEquals("user2", codelistOrderVisualisation.getLastUpdatedBy());
        assertEqualsDate("2011-01-22 01:02:03", codelistOrderVisualisation.getLastUpdated());
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
        assertEqualsDate("2011-01-01 01:02:03", codelistOrderVisualisationCreated.getCodelistVersion().getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistOrderVisualisationCreated.getCodelistVersion().getItemScheme().getResourceLastUpdated().toDate()));

        // Validate codes
        List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisationCreated.getNameableArtefact().getUrn(), null);
        assertEquals(9, codes.size());
        assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
        assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
        assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
        assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
        assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
        assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
        assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
        assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
        assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
    }

    @Test
    public void testCreateCodelistOrderVisualisationErrorDuplicatedCode() throws Exception {

        CodelistOrderVisualisation codelistOrderVisualisation = CodesMetamacDoMocks.mockCodelistOrderVisualisation();
        codelistOrderVisualisation.getNameableArtefact().setCode("VISUALISATION02");
        String codelistUrn = CODELIST_1_V2;
        try {
            codesService.createCodelistOrderVisualisation(getServiceContextAdministrador(), codelistUrn, codelistOrderVisualisation);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_1_V2_ORDER_VISUALISATION_02}, e.getExceptionItems().get(0));
        }
    }

    @Override
    public void testCreateCodelistOrderVisualisationAlphabetical() throws Exception {
        // Already tested
    }

    @Override
    @Test
    public void testUpdateCodelistOrderVisualisation() throws Exception {
        CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
        codelistOrderVisualisation.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());
        codelistOrderVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Update
        CodelistOrderVisualisation codelistOrderVisualisationUpdated = codesService.updateCodelistOrderVisualisation(getServiceContextAdministrador(), codelistOrderVisualisation);

        // Validate
        assertEqualsCodelistOrderVisualisation(codelistOrderVisualisation, codelistOrderVisualisationUpdated);
        assertEqualsDate("2011-01-01 01:02:03", codelistOrderVisualisationUpdated.getCodelistVersion().getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistOrderVisualisationUpdated.getCodelistVersion().getItemScheme().getResourceLastUpdated().toDate()));
    }

    @Test
    public void testUpdateCodelistOrderVisualisationErrorDuplicatedCode() throws Exception {
        CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
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
        code.getNameableArtefact().setCode("code029");
        code.setParent(null);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder()); // before CODE02
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(4), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
        }
        // Visualisation 02
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(4), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 03
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(4), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());

        }
    }

    @Test
    public void testCreateCodeSubCodeTestingOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.getNameableArtefact().setCode("CODE0200");
        CodeMetamac codeParent = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
        code.setParent(codeParent);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
        }
        // Visualisation 02
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 03
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());
        }
    }

    @Test
    public void testCreateCodeSubCodeInLevelWithoutOtherCodesTestingOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        CodeMetamac codeParent = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_4_1_1);
        code.setParent(codeParent);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 02
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 03
        {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", codelistOrderVisualisation.getNameableArtefact().getUrn(), null);
            assertEquals(10, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOrder());

        }
    }

    @Override
    @Test
    public void testDeleteCodelistOrderVisualisation() throws Exception {

        String urn = CODELIST_1_V2_ORDER_VISUALISATION_03;

        // Check any column of code
        {
            CodeMetamac code1 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
            assertNotNull(code1.getOrder1());
            assertNotNull(code1.getOrder2());
            assertNotNull(code1.getOrder3());

            CodeMetamac code2 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
            assertNotNull(code2.getOrder1());
            assertNotNull(code2.getOrder2());
            assertNotNull(code1.getOrder3());
        }

        //
        codesService.deleteCodelistOrderVisualisation(getServiceContextAdministrador(), urn);

        // Retrieve deleted visualisation
        try {
            codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), urn);
            fail("visualisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }

        entityManager.clear();

        // Check clear order
        {
            CodeMetamac code1 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
            assertNotNull(code1.getOrder1());
            assertNotNull(code1.getOrder2());
            assertNull(code1.getOrder3());

            CodeMetamac code2 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
            assertNotNull(code2.getOrder1());
            assertNotNull(code2.getOrder2());
            assertNull(code2.getOrder3());
        }
    }

    @Test
    public void testDeleteCodelistOrderVisualisationWhenIsDefault() throws Exception {

        String urn = CODELIST_1_V2_ORDER_VISUALISATION_02;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(urn, codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());

        codesService.deleteCodelistOrderVisualisation(getServiceContextAdministrador(), urn);

        // Check default is null
        codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertNull(codelistVersion.getDefaultOrderVisualisation());
    }

    @Override
    public void testVersioningCodelistOrderVisualisations() throws Exception {
        // Already tested
    }

    @Override
    public void testRecalculateCodesVisualisations() throws Exception {
        // tested in other operations
    }

    @Override
    @Test
    public void testRetrieveCodelistOrderVisualisationsByCodelist() throws Exception {
        String codelistUrn = CODELIST_1_V2;

        List<CodelistOrderVisualisation> visualisations = codesService.retrieveCodelistOrderVisualisationsByCodelist(getServiceContextAdministrador(), codelistUrn);

        assertEquals(3, visualisations.size());
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL, visualisations);
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_02, visualisations);
        assertContainsCodelistOrderVisualisation(CODELIST_1_V2_ORDER_VISUALISATION_03, visualisations);
    }

    @Override
    @Test
    public void testUpdateCodeInOrderVisualisation() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String codeUrn = CODELIST_1_V2_CODE_1;
        String visualisationUrn = CODELIST_1_V2_ORDER_VISUALISATION_02;

        codesService.updateCodeInOrderVisualisation(getServiceContextAdministrador(), codeUrn, visualisationUrn, Integer.valueOf(2));

        // Validate visualisation
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder()); // changed
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }

        // Validate other visualisation does not change
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
    }

    @Test
    public void testUpdateCodeSubcodeInOrderVisualisation() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String codeUrn = CODELIST_1_V2_CODE_2_1;
        String visualisationUrn = CODELIST_1_V2_ORDER_VISUALISATION_02;

        codesService.updateCodeInOrderVisualisation(getServiceContextAdministrador(), codeUrn, visualisationUrn, Integer.valueOf(1));

        // Validate visualisation
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }

        // Validate other visualisation does not change
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
    }

    @Test
    public void testDeleteCodeCheckUpdateOrderVisualisation() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String urn = CODELIST_1_V2_CODE_3;

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validation
        // Validate visualisation 01
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(8, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(8, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 03
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(8, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder()); // changed
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder()); // changed
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
    }

    @Test
    public void testDeleteCodeWithParentAndChildrenCheckUpdateOrderVisualisation() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String urn = CODELIST_1_V2_CODE_2_1;

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validate visualisation 01, alphabetic
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(7, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(7, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(7, codes.size());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // in same position, due to it is before deleted code
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
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

        // Validate visualisation 01, alphabetic
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (change level, put in alphabetical order)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder()); // change (down)
            assertEquals(Integer.valueOf(4), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // change (down)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
            assertEquals(Integer.valueOf(4), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (change level, put at the end)
        }

        // Validate visualisation 03
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
            assertEquals(Integer.valueOf(4), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (change level, put at the end)
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
        Code newParent = assertListCodesContainsCode(codelistVersion.getItems(), newParentUrn);
        assertEquals(2, newParent.getChildren().size());
        assertListCodesContainsCode(newParent.getChildren(), codeUrn);

        // Validate visualisation 01, alphabetic
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (change level, put in alphabetical order)
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder()); // changed (down)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (change level, put at the end)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }

        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder());
            assertEquals(Integer.valueOf(3), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (change level, put at the end)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
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
        Code newParent = assertListCodesContainsCode(codelistVersion.getItems(), newParentUrn);
        assertEquals(3, newParent.getChildren().size());
        assertListCodesContainsCode(newParent.getChildren(), codeUrn);

        // Validate visualisation 01, alphabetical
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());// changed (change level, put in alphabetical order)
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // changed (down)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // changed (down)
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());// changed (change level, put at the end)
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
        // Validate visualisation 02
        {
            CodelistOrderVisualisation visualisation = codesService.retrieveCodelistOrderVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_ORDER_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", visualisation.getNameableArtefact().getUrn(),
                    null);
            assertEquals(9, codes.size());
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(1), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOrder());// changed (change level, put at the end)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOrder()); // in same position, due to it is before moved code
            assertEquals(Integer.valueOf(2), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOrder()); // changed (up)
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOrder());
        }
    }

    // ------------------------------------------------------------------------------------
    // CODELIST OPENNESS VISUALISATIONS
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    public void testRetrieveCodelistOpennessVisualisationByUrn() throws Exception {
        // Retrieve
        String codelistUrn = CODELIST_1_V2;
        String opennessVisualisationUrn = CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED;
        CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), opennessVisualisationUrn);

        // Validate
        assertNotNull(codelistOpennessVisualisation);
        assertEquals("ALL_EXPANDED", codelistOpennessVisualisation.getNameableArtefact().getCode());
        assertEquals(opennessVisualisationUrn, codelistOpennessVisualisation.getNameableArtefact().getUrn());
        assertEquals(codelistUrn, codelistOpennessVisualisation.getCodelistVersion().getMaintainableArtefact().getUrn());
        assertEqualsInternationalString(codelistOpennessVisualisation.getNameableArtefact().getName(), "es", "Visualización de apertura Todos abiertos 1-2", null, null);
        assertEquals(Integer.valueOf(1), codelistOpennessVisualisation.getColumnIndex());

        assertEquals("user1", codelistOpennessVisualisation.getCreatedBy());
        assertEqualsDate("2011-01-01 01:02:03", codelistOpennessVisualisation.getCreatedDate());
        assertEquals("user2", codelistOpennessVisualisation.getLastUpdatedBy());
        assertEqualsDate("2011-01-22 01:02:03", codelistOpennessVisualisation.getLastUpdated());
    }

    @Override
    @Test
    public void testCreateCodelistOpennessVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodelistOpennessVisualisation codelistOpennessVisualisation = CodesMetamacDoMocks.mockCodelistOpennessVisualisation();
        String codelistUrn = CODELIST_1_V2;

        // Create
        CodelistOpennessVisualisation codelistOpennessVisualisationCreated = codesService.createCodelistOpennessVisualisation(ctx, codelistUrn, codelistOpennessVisualisation);
        assertNotNull(codelistOpennessVisualisationCreated.getId());
        assertNotNull(codelistOpennessVisualisationCreated.getNameableArtefact().getUrn());
        assertEqualsCodelistOpennessVisualisation(codelistOpennessVisualisation, codelistOpennessVisualisationCreated);
        assertEqualsDate("2011-01-01 01:02:03", codelistOpennessVisualisation.getCodelistVersion().getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistOpennessVisualisation.getCodelistVersion().getItemScheme().getResourceLastUpdated().toDate()));

        // Validate codes
        List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisationCreated.getNameableArtefact().getUrn());
        assertEquals(9, codes.size());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
        assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
    }

    @Test
    public void testCreateCodelistOpennessVisualisationErrorDuplicatedCode() throws Exception {

        CodelistOpennessVisualisation codelistOpennessVisualisation = CodesMetamacDoMocks.mockCodelistOpennessVisualisation();
        codelistOpennessVisualisation.getNameableArtefact().setCode("VISUALISATION02");
        String codelistUrn = CODELIST_1_V2;
        try {
            codesService.createCodelistOpennessVisualisation(getServiceContextAdministrador(), codelistUrn, codelistOpennessVisualisation);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_1_V2_OPENNESS_VISUALISATION_02}, e.getExceptionItems().get(0));
        }
    }

    @Override
    public void testCreateCodelistOpennessVisualisationAllOpened() throws Exception {
        // Already tested
    }

    @Override
    @Test
    public void testUpdateCodelistOpennessVisualisation() throws Exception {
        CodelistOpennessVisualisation codelistOpennessVisualisation = codesService
                .retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_02);
        codelistOpennessVisualisation.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());
        codelistOpennessVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Update
        CodelistOpennessVisualisation codelistOpennessVisualisationUpdated = codesService.updateCodelistOpennessVisualisation(getServiceContextAdministrador(), codelistOpennessVisualisation);

        // Validate
        assertEqualsCodelistOpennessVisualisation(codelistOpennessVisualisation, codelistOpennessVisualisationUpdated);
        assertEqualsDate("2011-01-01 01:02:03", codelistOpennessVisualisation.getCodelistVersion().getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), codelistOpennessVisualisation.getCodelistVersion().getItemScheme().getResourceLastUpdated().toDate()));
    }

    @Test
    public void testUpdateCodelistOpennessVisualisationErrorDuplicatedCode() throws Exception {
        CodelistOpennessVisualisation codelistOpennessVisualisation = codesService
                .retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_03);
        codelistOpennessVisualisation.getNameableArtefact().setCode("VISUALISATION02");
        codelistOpennessVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            codesService.updateCodelistOpennessVisualisation(getServiceContextAdministrador(), codelistOpennessVisualisation);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CODELIST_1_V2_OPENNESS_VISUALISATION_02}, e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCodeTestingOpennessVisualisation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.setParent(null);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01, all expanded
        {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisation.getNameableArtefact().getUrn());
            assertEquals(10, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOpenness());
        }
        // Visualisation 02
        {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_02);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisation.getNameableArtefact().getUrn());
            assertEquals(10, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOpenness());
        }
        // Visualisation 03
        {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_03);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisation.getNameableArtefact().getUrn());
            assertEquals(10, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOpenness());
        }
    }

    @Test
    public void testCreateCodeSubCodeTestingOpennessVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CodeMetamac code = CodesMetamacDoMocks.mockCode();
        code.getNameableArtefact().setCode("CODE0200");
        CodeMetamac codeParent = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
        code.setParent(codeParent);

        String codelistUrn = CODELIST_1_V2;

        // Create
        CodeMetamac codeCreated = codesService.createCode(ctx, codelistUrn, code);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisation.getNameableArtefact().getUrn());
            assertEquals(10, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOpenness());
        }
        // Visualisation 02
        {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_02);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisation.getNameableArtefact().getUrn());
            assertEquals(10, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOpenness());
        }
        // Visualisation 03
        {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_03);

            // Validate codes
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn, "es", null, codelistOpennessVisualisation.getNameableArtefact().getUrn());
            assertEquals(10, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeCreated.getNameableArtefact().getUrn()).getOpenness());
        }
    }

    @Override
    @Test
    public void testDeleteCodelistOpennessVisualisation() throws Exception {

        String urn = CODELIST_1_V2_OPENNESS_VISUALISATION_03;

        // Check any column of code
        {
            CodeMetamac code1 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
            assertNotNull(code1.getOpenness1());
            assertNotNull(code1.getOpenness2());
            assertNotNull(code1.getOpenness3());

            CodeMetamac code2 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
            assertNotNull(code2.getOpenness1());
            assertNotNull(code2.getOpenness2());
            assertNotNull(code1.getOpenness3());
        }

        //
        codesService.deleteCodelistOpennessVisualisation(getServiceContextAdministrador(), urn);

        // Retrieve deleted visualisation
        try {
            codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), urn);
            fail("visualisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }

        entityManager.clear();

        // Check clear openness
        {
            CodeMetamac code1 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);
            assertNotNull(code1.getOpenness1());
            assertNotNull(code1.getOpenness2());
            assertNull(code1.getOpenness3());

            CodeMetamac code2 = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_2);
            assertNotNull(code2.getOpenness1());
            assertNotNull(code2.getOpenness2());
            assertNull(code2.getOpenness3());
        }
    }

    @Test
    public void testDeleteCodelistOpennessVisualisationWhenIsDefault() throws Exception {

        String urn = CODELIST_1_V2_OPENNESS_VISUALISATION_02;

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertEquals(urn, codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn());

        codesService.deleteCodelistOpennessVisualisation(getServiceContextAdministrador(), urn);

        // Check default is null
        codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        assertNull(codelistVersion.getDefaultOpennessVisualisation());
    }

    @Override
    public void testVersioningCodelistOpennessVisualisations() throws Exception {
        // Already tested
    }

    @Override
    @Test
    public void testCreateTemporalCodelist() throws Exception {
        String urn = CODELIST_3_V1;
        String versionExpected = "01.000" + UrnConstants.URN_SDMX_TEMPORAL_SUFFIX;;
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(" + versionExpected + ")";
        String urnExpectedCode1 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(" + versionExpected + ").CODE01";
        String urnExpectedCode2 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(" + versionExpected + ").CODE02";
        String urnExpectedCode21 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(" + versionExpected + ").CODE0201";
        String urnExpectedCode211 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(" + versionExpected + ").CODE020101";
        String urnExpectedCode22 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(" + versionExpected + ").CODE0202";
        String urnExpectedOrderVisualisation01 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST03(" + versionExpected + ").ALPHABETICAL";
        String urnExpectedOrderVisualisation02 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST03(" + versionExpected + ").VISUALISATION02";
        String urnExpectedOpennessVisualisation01 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST03(" + versionExpected + ").ALL_EXPANDED";
        String urnExpectedOpennessVisualisation02 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST03(" + versionExpected + ").VISUALISATION02";

        CodelistVersionMetamac codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = codesService.createTemporalCodelist(getServiceContextAdministrador(), urn);

        // Validate response
        entityManager.clear();
        codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        CodelistVersionMetamac codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());
            assertTrue(codelistVersionNewVersion.getMaintainableArtefact().getIsTemporal());
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
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode1);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode2);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode21);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode211);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode22);

            assertEquals(2, codelistVersionNewVersion.getItemsFirstLevel().size());
            {
                CodeMetamac code = assertListCodesContainsCode(codelistVersionNewVersion.getItemsFirstLevel(), urnExpectedCode1);
                assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-1", null, null);
                assertEquals(Integer.valueOf(0), code.getOrder1());
                assertEquals(Integer.valueOf(1), code.getOrder2());
                assertEquals(null, code.getOrder3());
                assertEquals(null, code.getOrder4());
                assertEquals(null, code.getOrder5());
                assertEquals(null, code.getOrder6());
                assertEquals(null, code.getOrder7());
                assertEquals(null, code.getOrder8());
                assertEquals(null, code.getOrder9());
                assertEquals(null, code.getOrder10());
                assertEquals(null, code.getOrder11());
                assertEquals(null, code.getOrder12());
                assertEquals(null, code.getOrder13());
                assertEquals(null, code.getOrder14());
                assertEquals(null, code.getOrder15());
                assertEquals(null, code.getOrder16());
                assertEquals(null, code.getOrder17());
                assertEquals(null, code.getOrder18());
                assertEquals(null, code.getOrder19());
                assertEquals(null, code.getOrder20());

                assertEquals(Boolean.TRUE, code.getOpenness1());
                assertEquals(Boolean.TRUE, code.getOpenness2());
                assertEquals(null, code.getOpenness3());
                assertEquals(null, code.getOpenness4());
                assertEquals(null, code.getOpenness5());
                assertEquals(null, code.getOpenness6());
                assertEquals(null, code.getOpenness7());
                assertEquals(null, code.getOpenness8());
                assertEquals(null, code.getOpenness9());
                assertEquals(null, code.getOpenness10());
                assertEquals(null, code.getOpenness11());
                assertEquals(null, code.getOpenness12());
                assertEquals(null, code.getOpenness13());
                assertEquals(null, code.getOpenness14());
                assertEquals(null, code.getOpenness15());
                assertEquals(null, code.getOpenness16());
                assertEquals(null, code.getOpenness17());
                assertEquals(null, code.getOpenness18());
                assertEquals(null, code.getOpenness19());
                assertEquals(null, code.getOpenness20());

                assertEquals(0, code.getChildren().size());
            }
            {
                CodeMetamac code = assertListCodesContainsCode(codelistVersionNewVersion.getItemsFirstLevel(), urnExpectedCode2);
                assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-2", null, null);
                assertEquals(Integer.valueOf(1), code.getOrder1());
                assertEquals(Integer.valueOf(0), code.getOrder2());
                assertEquals(Boolean.TRUE, code.getOpenness1());
                assertEquals(Boolean.FALSE, code.getOpenness2());

                assertEquals(2, code.getChildren().size());
                {
                    CodeMetamac codeChild = assertListCodesContainsCode(code.getChildren(), urnExpectedCode21);
                    assertEquals(urnExpectedCode21, codeChild.getNameableArtefact().getUrn());
                    assertEquals(Integer.valueOf(0), codeChild.getOrder1());
                    assertEquals(Integer.valueOf(1), codeChild.getOrder2());
                    assertEquals(Boolean.TRUE, codeChild.getOpenness1());
                    assertEquals(Boolean.TRUE, codeChild.getOpenness2());

                    assertEquals(1, codeChild.getChildren().size());
                    {
                        CodeMetamac codeChildChild = assertListCodesContainsCode(codeChild.getChildren(), urnExpectedCode211);
                        assertEquals(urnExpectedCode211, codeChildChild.getNameableArtefact().getUrn());
                        assertEquals(Integer.valueOf(0), codeChildChild.getOrder1());
                        assertEquals(Integer.valueOf(0), codeChildChild.getOrder2());
                        assertEquals(Boolean.TRUE, codeChildChild.getOpenness1());
                        assertEquals(Boolean.FALSE, codeChildChild.getOpenness2());

                        assertEquals(0, codeChildChild.getChildren().size());
                    }
                }
                {
                    CodeMetamac codeChild = assertListCodesContainsCode(code.getChildren(), urnExpectedCode22);
                    assertEquals(Integer.valueOf(1), codeChild.getOrder1());
                    assertEquals(Integer.valueOf(0), codeChild.getOrder2());
                    assertEquals(Boolean.TRUE, codeChild.getOpenness1());
                    assertEquals(Boolean.FALSE, codeChild.getOpenness2());

                    assertEquals(0, codeChild.getChildren().size());
                }
            }

            // Order visualisations
            assertEquals(urnExpectedOrderVisualisation01, codelistVersionNewVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());
            assertEquals(2, codelistVersionNewVersion.getOrderVisualisations().size());
            {
                CodelistOrderVisualisation codelistOrderVisualisation = assertContainsCodelistOrderVisualisation(urnExpectedOrderVisualisation01, codelistVersionNewVersion.getOrderVisualisations());
                assertEquals("ALPHABETICAL", codelistOrderVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(1), codelistOrderVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOrderVisualisation.getNameableArtefact().getName(), "es", "Alfabético", null, null);
            }
            {
                CodelistOrderVisualisation codelistOrderVisualisation = assertContainsCodelistOrderVisualisation(urnExpectedOrderVisualisation02, codelistVersionNewVersion.getOrderVisualisations());
                assertEquals("VISUALISATION02", codelistOrderVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(2), codelistOrderVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOrderVisualisation.getNameableArtefact().getName(), "es", "Visualización 02", null, null);
            }

            // Openness visualisations
            assertEquals(urnExpectedOpennessVisualisation01, codelistVersionNewVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn());
            assertEquals(2, codelistVersionNewVersion.getOpennessVisualisations().size());
            {
                CodelistOpennessVisualisation codelistOpennessVisualisation = assertContainsCodelistOpennessVisualisation(urnExpectedOpennessVisualisation01,
                        codelistVersionNewVersion.getOpennessVisualisations());
                assertEquals("ALL_EXPANDED", codelistOpennessVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(1), codelistOpennessVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOpennessVisualisation.getNameableArtefact().getName(), "es", "Visualización de apertura Todos abiertos 3-1", null, null);
            }
            {
                CodelistOpennessVisualisation codelistOpennessVisualisation = assertContainsCodelistOpennessVisualisation(urnExpectedOpennessVisualisation02,
                        codelistVersionNewVersion.getOpennessVisualisations());
                assertEquals("VISUALISATION02", codelistOpennessVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(2), codelistOpennessVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOpennessVisualisation.getNameableArtefact().getName(), "es", "Visualización 02 3-1", null, null);
            }
        }

        // Copied version
        {
            codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", codelistVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, codelistVersionToCopy.getMaintainableArtefact().getUrn());
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

    @Override
    @Test
    public void testCreateVersionFromTemporalCodelist() throws Exception {
        String urn = CODELIST_3_V1;

        // --- Temporal version
        TaskInfo versioningTemporalResult = codesService.createTemporalCodelist(getServiceContextAdministrador(), urn);
        entityManager.clear();
        CodelistVersionMetamac temporalCodelistVersionMetamac = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), versioningTemporalResult.getUrnResult());
        assertEquals(3, temporalCodelistVersionMetamac.getMaintainableArtefact().getCategorisations().size());
        {
            Categorisation categorisation = assertListContainsCategorisation(temporalCodelistVersionMetamac.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000_temporal)");
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
        }
        assertListContainsCategorisation(temporalCodelistVersionMetamac.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat2(01.000_temporal)");
        assertListContainsCategorisation(temporalCodelistVersionMetamac.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat3(01.000_temporal)");

        // -- No temporal
        TaskInfo versioningTemporalToVersionResult = codesService.createVersionFromTemporalCodelist(getServiceContextAdministrador(),
                temporalCodelistVersionMetamac.getMaintainableArtefact().getUrn(), VersionTypeEnum.MAJOR);
        entityManager.clear();
        CodelistVersionMetamac codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), versioningTemporalToVersionResult.getUrnResult());

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(" + versionExpected + ")";

        // Validate
        {
            assertEquals(ProcStatusEnum.DRAFT, codelistVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, codelistVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, codelistVersionNewVersion.getMaintainableArtefact().getUrn());

            assertEquals(null, codelistVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(codelistVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(codelistVersionNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(codelistVersionNewVersion.getMaintainableArtefact().getLatestPublic());

            assertEquals(3, codelistVersionNewVersion.getMaintainableArtefact().getCategorisations().size());
            {
                Categorisation categorisation = assertListContainsCategorisation(codelistVersionNewVersion.getMaintainableArtefact().getCategorisations(),
                        "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)");
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
            }
            assertListContainsCategorisation(codelistVersionNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat5(01.000)");
            assertListContainsCategorisation(codelistVersionNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)");
        }
    }

    @Override
    @Test
    public void testMergeTemporalVersion() throws Exception {
        {
            String urn = CODELIST_3_V1;
            TaskInfo createTemporalCodelistResult = codesService.createTemporalCodelist(getServiceContextAdministrador(), urn);

            entityManager.clear();
            CodelistVersionMetamac createTemporalCodelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), createTemporalCodelistResult.getUrnResult());
            // Change temporal version *********************

            // Item scheme: Change Name
            {
                LocalisedString localisedString = new LocalisedString("fr", "fr - text sample");
                createTemporalCodelist.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Code 1: change short name
            {
                CodeMetamac codeTemporal = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CODELIST_3_V1_CODE_1));
                codeTemporal.setVariableElement(null);

                InternationalString internationalString = new InternationalString();
                internationalString.addText(new LocalisedString("fr", "fr - text sample"));
                internationalString.addText(new LocalisedString("es", "es - text sample"));
                codeTemporal.setShortName(internationalString);
                codeTemporal.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
                codesService.updateCode(getServiceContextAdministrador(), codeTemporal);
            }
            // Code 2: change variable element
            {
                CodeMetamac codeTemporal = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CODELIST_3_V1_CODE_2));
                assertEquals(null, codeTemporal.getVariableElement());
                codeTemporal.setVariableElement(codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), VARIABLE_2_VARIABLE_ELEMENT_1));
                codeTemporal.setShortName(null);
                codeTemporal.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
                codesService.updateCode(getServiceContextAdministrador(), codeTemporal);
            }

            // Merge
            createTemporalCodelist = codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn());
            createTemporalCodelist = codesService.sendCodelistToDiffusionValidation(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn());
            CodelistVersionMetamac codelistVersionMetamac = codesService.mergeTemporalVersion(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn());
            // String codelistVersionMetamacUrn = versioningResult.getUrnResult();
            // entityManager.clear();
            // CodelistVersionMetamac codelistVersionMetamac = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionMetamacUrn);

            // Assert **************************************

            // Item Scheme
            assertEquals(2, codelistVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("fr - text sample", codelistVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));

            // Code 1: change short name
            {
                CodeMetamac codeTemporal = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_3_V1_CODE_1);
                assertEquals(null, codeTemporal.getVariableElement());
                assertEquals(2, codeTemporal.getShortName().getTexts().size());
                assertEquals("fr - text sample", codeTemporal.getShortName().getLocalisedLabel("fr"));
                assertEquals("es - text sample", codeTemporal.getShortName().getLocalisedLabel("es"));
            }
            // Code 2: change variable element
            {
                CodeMetamac codeTemporal = codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_3_V1_CODE_2);
                assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, codeTemporal.getVariableElement().getIdentifiableArtefact().getUrn());
                assertEquals(null, codeTemporal.getShortName());
            }
        }

        {
            // save to force incorrect metadata
            CodelistVersionMetamac codelistSchemeForce = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_10_V3);
            codelistSchemeForce.getMaintainableArtefact().setFinalLogic(Boolean.TRUE);
            codelistSchemeForce.getMaintainableArtefact().setFinalLogicClient(Boolean.TRUE);
            codelistSchemeForce.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
            itemSchemeRepository.save(codelistSchemeForce);

            String urn = CODELIST_10_V1;
            TaskInfo createTemporalCodelistResult = codesService.createTemporalCodelist(getServiceContextAdministrador(), urn);
            entityManager.clear();
            CodelistVersionMetamac codelistVersionTemporal = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), createTemporalCodelistResult.getUrnResult());

            assertTrue(codelistVersionTemporal.getMaintainableArtefact().getIsLastVersion());

            // Merge
            // save to force incorrect metadata
            codelistVersionTemporal.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
            itemSchemeRepository.save(codelistVersionTemporal);
            CodelistVersionMetamac codelistVersionMetamac = codesService.mergeTemporalVersion(getServiceContextAdministrador(), codelistVersionTemporal.getMaintainableArtefact().getUrn());
            // String codelistVersionMetamacUrn = versioningResult.getUrnResult();
            // entityManager.clear();
            // CodelistVersionMetamac codelistVersionMetamac = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistVersionMetamacUrn);
            assertFalse(codelistVersionMetamac.getMaintainableArtefact().getIsLastVersion());
            assertNull(codelistVersionMetamac.getMaintainableArtefact().getIsTemporal());
        }
    }

    @Test
    @DirtyDatabase
    public void testPublishInternallyCodelistInBackground() throws Exception {

        int previousValueLimitToBackground = SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND;
        final String codelistUrn = CODELIST_3_V1;
        final StringBuilder jobKey = new StringBuilder();
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    ItemSchemeVersion itemSchemeVersion = itemSchemeRepository.findByUrn(codelistUrn);
                    assertEquals(null, itemSchemeVersion.getItemScheme().getIsTaskInBackground());

                    // Create Temporal Codelist and go to PublicationInternally phase
                    TaskInfo createTemporalCodelistResult = codesService.createTemporalCodelist(getServiceContextAdministrador(), codelistUrn);
                    CodelistVersionMetamac createTemporalCodelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), createTemporalCodelistResult.getUrnResult());
                    createTemporalCodelist = codesService.sendCodelistToProductionValidation(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn());
                    createTemporalCodelist = codesService.sendCodelistToDiffusionValidation(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn());

                    // PublicationInternally phase
                    SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND = 3; // modify to force in background
                    TaskInfo versioningResult = codesService
                            .publishInternallyCodelist(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn(), false, Boolean.TRUE);

                    assertEquals(true, versioningResult.getIsPlannedInBackground());
                    assertEquals(null, versioningResult.getUrnResult());
                    jobKey.append(versioningResult.getJobKey());

                    itemSchemeVersion = itemSchemeRepository.findByUrn(createTemporalCodelist.getMaintainableArtefact().getUrn());
                    assertEquals(true, itemSchemeVersion.getItemScheme().getIsTaskInBackground());

                    try {
                        codesService.publishInternallyCodelist(getServiceContextAdministrador(), createTemporalCodelist.getMaintainableArtefact().getUrn(), false, Boolean.TRUE);
                        fail("already publishing");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_ACTION_NOT_SUPPORTED_WHEN_TASK_IN_BACKGROUND.getCode(), e.getExceptionItems().get(0).getCode());
                        assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                        assertEquals(createTemporalCodelist.getMaintainableArtefact().getUrn(), e.getExceptionItems().get(0).getMessageParameters()[0]);
                    }
                } catch (MetamacException e) {
                    fail("publish failed");
                }
            }
        });
        waitUntilJobFinished();
        SdmxConstants.ITEMS_LIMIT_TO_EXECUTE_TASK_IN_BACKGROUND = previousValueLimitToBackground;

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());

        // Validate background task as finished in item scheme
        CodelistVersion codelistVersionPublished = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), codelistUrn);
        assertEquals(false, codelistVersionPublished.getItemScheme().getIsTaskInBackground());
    }

    @Override
    @Test
    public void testRetrieveCodelistOpennessVisualisationsByCodelist() throws Exception {
        String codelistUrn = CODELIST_1_V2;

        List<CodelistOpennessVisualisation> visualisations = codesService.retrieveCodelistOpennessVisualisationsByCodelist(getServiceContextAdministrador(), codelistUrn);

        assertEquals(3, visualisations.size());
        assertContainsCodelistOpennessVisualisation(CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED, visualisations);
        assertContainsCodelistOpennessVisualisation(CODELIST_1_V2_OPENNESS_VISUALISATION_02, visualisations);
        assertContainsCodelistOpennessVisualisation(CODELIST_1_V2_OPENNESS_VISUALISATION_03, visualisations);
    }

    @Override
    @Test
    public void testUpdateCodesInOpennessVisualisation() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String codeUrn1 = CODELIST_1_V2_CODE_1;
        String codeUrn2 = CODELIST_1_V2_CODE_2_2;
        String visualisationUrn = CODELIST_1_V2_OPENNESS_VISUALISATION_02;

        // Before
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, codeUrn1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeUrn2).getOpenness());
        }

        // Update
        Map<String, Boolean> values = new HashMap<String, Boolean>();
        values.put(codeUrn1, Boolean.TRUE);
        values.put(codeUrn2, Boolean.FALSE);
        codesService.updateCodesInOpennessVisualisation(getServiceContextAdministrador(), visualisationUrn, values);

        // Validate visualisation
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeUrn1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, codeUrn2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }

        // Validate other visualisation does not change
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeUrn1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, codeUrn1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
    }

    @Test
    public void testDeleteCodeCheckDoNotChangeOpennessVisualisationOfOtherCodes() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String urn = CODELIST_1_V2_CODE_2_1;

        // Delete code
        codesService.deleteCode(getServiceContextAdministrador(), urn);

        // Validate visualisation 01, alphabetic
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(7, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
        // Validate visualisation 02
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(7, codes.size());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
        // Validate visualisation 02
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(7, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
    }

    @Test
    public void testUpdateCodeParentCheckDoNotChangeOpennessVisualisationOfOtherCodes() throws Exception {

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

        // Validate visualisation 01, alphabetic
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(),
                    CODELIST_1_V2_OPENNESS_VISUALISATION_01_ALL_EXPANDED);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
        // Validate visualisation 02
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_02);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(9, codes.size());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }

        // Validate visualisation 03
        {
            CodelistOpennessVisualisation visualisation = codesService.retrieveCodelistOpennessVisualisationByUrn(getServiceContextAdministrador(), CODELIST_1_V2_OPENNESS_VISUALISATION_03);
            List<CodeMetamacVisualisationResult> codes = codesService.retrieveCodesByCodelistUrn(getServiceContextAdministrador(), codelistUrn, "es", null, visualisation.getNameableArtefact()
                    .getUrn());
            assertEquals(9, codes.size());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_1_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_2_2).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_3).getOpenness());
            assertEquals(Boolean.FALSE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1).getOpenness());
            assertEquals(Boolean.TRUE, getCodeMetamacVisualisationResult(codes, CODELIST_1_V2_CODE_4_1_1).getOpenness());
        }
    }

    @Override
    @Test
    public void testRetrieveVariableElementOperationByCode() throws Exception {
        String code = VARIABLE_2_VARIABLE_ELEMENT_OPERATION_1;
        VariableElementOperation variableElementOperation = codesService.retrieveVariableElementOperationByCode(getServiceContextAdministrador(), code);

        // Validate
        assertEquals(code, variableElementOperation.getCode());
        assertEquals(2, variableElementOperation.getSources().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(VARIABLE_2_VARIABLE_ELEMENT_1, variableElementOperation.getSources()));
        assertTrue(SrmServiceUtils.isVariableElementInList(VARIABLE_2_VARIABLE_ELEMENT_2, variableElementOperation.getSources()));
        assertEquals(1, variableElementOperation.getTargets().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(VARIABLE_2_VARIABLE_ELEMENT_3, variableElementOperation.getTargets()));

        assertEquals("VARIABLE_2_OPERATION_1", variableElementOperation.getUuid());
        assertEqualsDate(new DateTime(2011, 01, 01, 01, 02, 03, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), variableElementOperation.getCreatedDate());
        assertEquals("user1", variableElementOperation.getCreatedBy());
        assertEquals("user2", variableElementOperation.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), variableElementOperation.getVersion());
    }

    @Override
    @Test
    public void testCreateVariableElementFusionOperation() throws Exception {
        List<String> sources = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_5, VARIABLE_2_VARIABLE_ELEMENT_6);
        String target = VARIABLE_2_VARIABLE_ELEMENT_7;
        VariableElementOperation variableElementOperationCreated = codesService.createVariableElementFusionOperation(getServiceContextAdministrador(), sources, target);
        assertNotNull(variableElementOperationCreated.getCode());

        // Validate
        VariableElementOperation variableElementOperationRetrieved = codesService.retrieveVariableElementOperationByCode(getServiceContextAdministrador(), variableElementOperationCreated.getCode());

        // Validate
        assertEquals(VariableElementOperationTypeEnum.FUSION, variableElementOperationRetrieved.getOperationType());
        assertEquals(VARIABLE_2, variableElementOperationRetrieved.getVariable().getNameableArtefact().getUrn());
        assertEquals(2, variableElementOperationRetrieved.getSources().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(sources.get(0), variableElementOperationRetrieved.getSources()));
        assertTrue(SrmServiceUtils.isVariableElementInList(sources.get(1), variableElementOperationRetrieved.getSources()));
        assertEquals(1, variableElementOperationRetrieved.getTargets().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(target, variableElementOperationRetrieved.getTargets()));
    }

    @Test
    public void testCreateVariableElementFusionOperationErrorDifferentVariable() throws Exception {
        List<String> sources = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_6, VARIABLE_5_VARIABLE_ELEMENT_3);
        String target = VARIABLE_2_VARIABLE_ELEMENT_7;
        try {
            codesService.createVariableElementFusionOperation(getServiceContextAdministrador(), sources, target);
            fail("different variable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(VARIABLE_5_VARIABLE_ELEMENT_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateVariableElementFusionOperationErrorValidToEmpty() throws Exception {
        List<String> sources = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_5, VARIABLE_2_VARIABLE_ELEMENT_6);
        String target = VARIABLE_2_VARIABLE_ELEMENT_8;
        try {
            codesService.createVariableElementFusionOperation(getServiceContextAdministrador(), sources, target);
            fail("validTo");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_MUST_HAVE_VALID_TO_FILLED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(target, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateVariableElementFusionOperationErrorInSourceAndTarget() throws Exception {
        List<String> sources = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_5, VARIABLE_2_VARIABLE_ELEMENT_6);
        String target = VARIABLE_2_VARIABLE_ELEMENT_6;
        try {
            codesService.createVariableElementFusionOperation(getServiceContextAdministrador(), sources, target);
            fail("source and target");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_OPERATION_VARIABLE_ELEMENT_IN_SOURCE_AND_TARGET.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_6, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateVariableElementFusionOperationErrorAlreadyAsSource() throws Exception {
        List<String> sources = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_1, VARIABLE_2_VARIABLE_ELEMENT_6);
        String target = VARIABLE_2_VARIABLE_ELEMENT_7;
        try {
            codesService.createVariableElementFusionOperation(getServiceContextAdministrador(), sources, target);
            fail("source");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_ALREADY_AS_SOURCE_IN_OPERATION.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateVariableElementFusionOperationErrorAlreadyAsTarget() throws Exception {
        List<String> sources = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_5, VARIABLE_2_VARIABLE_ELEMENT_6);
        String target = VARIABLE_2_VARIABLE_ELEMENT_3;
        try {
            codesService.createVariableElementFusionOperation(getServiceContextAdministrador(), sources, target);
            fail("source");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_ALREADY_AS_TARGET_IN_OPERATION.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testCreateVariableElementSegregationOperation() throws Exception {
        String source = VARIABLE_2_VARIABLE_ELEMENT_4;
        List<String> targets = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_6, VARIABLE_2_VARIABLE_ELEMENT_7);
        VariableElementOperation variableElementOperationCreated = codesService.createVariableElementSegregationOperation(getServiceContextAdministrador(), source, targets);
        assertNotNull(variableElementOperationCreated.getCode());

        // Validate
        VariableElementOperation variableElementOperationRetrieved = codesService.retrieveVariableElementOperationByCode(getServiceContextAdministrador(), variableElementOperationCreated.getCode());

        // Validate
        assertEquals(VariableElementOperationTypeEnum.SEGREGATION, variableElementOperationRetrieved.getOperationType());
        assertEquals(VARIABLE_2, variableElementOperationRetrieved.getVariable().getNameableArtefact().getUrn());
        assertEquals(1, variableElementOperationRetrieved.getSources().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(source, variableElementOperationRetrieved.getSources()));
        assertEquals(2, variableElementOperationRetrieved.getTargets().size());
        assertTrue(SrmServiceUtils.isVariableElementInList(targets.get(0), variableElementOperationRetrieved.getTargets()));
        assertTrue(SrmServiceUtils.isVariableElementInList(targets.get(1), variableElementOperationRetrieved.getTargets()));
    }

    @Test
    public void testCreateVariableElementSegregationOperationErrorAlreadyAsSource() throws Exception {
        String source = VARIABLE_2_VARIABLE_ELEMENT_3;
        List<String> targets = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_6, VARIABLE_2_VARIABLE_ELEMENT_7);
        try {
            codesService.createVariableElementSegregationOperation(getServiceContextAdministrador(), source, targets);
            fail("source");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_ALREADY_AS_SOURCE_IN_OPERATION.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateVariableElementSegregationOperationErrorAlreadyAsTarget() throws Exception {
        String source = VARIABLE_2_VARIABLE_ELEMENT_4;
        List<String> targets = Arrays.asList(VARIABLE_2_VARIABLE_ELEMENT_6, VARIABLE_2_VARIABLE_ELEMENT_5);
        try {
            codesService.createVariableElementSegregationOperation(getServiceContextAdministrador(), source, targets);
            fail("source");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.VARIABLE_ELEMENT_ALREADY_AS_TARGET_IN_OPERATION.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(VARIABLE_2_VARIABLE_ELEMENT_5, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testDeleteVariableElementOperation() throws Exception {
        String code = VARIABLE_2_VARIABLE_ELEMENT_OPERATION_1;

        VariableElementOperation variableElementOperationRetrieved = codesService.retrieveVariableElementOperationByCode(getServiceContextAdministrador(), code);
        VariableElement variableElementToCheckIsNotDeleted = variableElementOperationRetrieved.getSources().get(0);

        // Delete
        codesService.deleteVariableElementOperation(getServiceContextAdministrador(), code);

        // Retrieve deleted operation
        try {
            codesService.retrieveVariableElementOperationByCode(getServiceContextAdministrador(), code);
            fail("deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_ELEMENT_OPERATION_NOT_FOUND, 1, new String[]{code}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted operation
        try {
            codesService.deleteVariableElementOperation(getServiceContextAdministrador(), code);
            fail("already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.VARIABLE_ELEMENT_OPERATION_NOT_FOUND, 1, new String[]{code}, e.getExceptionItems().get(0));
        }

        // Test variable element is not deleted
        codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(), variableElementToCheckIsNotDeleted.getIdentifiableArtefact().getUrn());
    }

    @Override
    @Test
    public void testRetrieveVariableElementsOperationsByVariable() throws Exception {
        {
            List<VariableElementOperation> operationsByVariable = codesService.retrieveVariableElementsOperationsByVariable(getServiceContextAdministrador(), VARIABLE_2);
            assertEquals(3, operationsByVariable.size());

            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_1);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.FUSION, variableElementOperation.getOperationType());
            }
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_2);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.SEGREGATION, variableElementOperation.getOperationType());
            }
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_3);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.SEGREGATION, variableElementOperation.getOperationType());
            }
        }
        {
            List<VariableElementOperation> operationsByVariable = codesService.retrieveVariableElementsOperationsByVariable(getServiceContextAdministrador(), VARIABLE_5);
            assertEquals(1, operationsByVariable.size());
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_5_VARIABLE_ELEMENT_OPERATION_1);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.FUSION, variableElementOperation.getOperationType());
            }
        }
    }

    @Override
    @Test
    public void testRetrieveVariableElementsOperationsByVariableElement() throws Exception {

        // Variable element in sources and targets
        {
            String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_3;
            List<VariableElementOperation> operationsByVariable = codesService.retrieveVariableElementsOperationsByVariableElement(getServiceContextAdministrador(), variableElementUrn);
            assertEquals(3, operationsByVariable.size());
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_1);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.FUSION, variableElementOperation.getOperationType());
                assertFalse(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getSources()));
                assertTrue(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getTargets()));
            }
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_2);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.SEGREGATION, variableElementOperation.getOperationType());
                assertFalse(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getSources()));
                assertTrue(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getTargets()));
            }
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_3);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.SEGREGATION, variableElementOperation.getOperationType());
                assertTrue(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getSources()));
                assertFalse(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getTargets()));
            }
        }

        // Variable element only in sources
        {
            String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_1;
            List<VariableElementOperation> operationsByVariable = codesService.retrieveVariableElementsOperationsByVariableElement(getServiceContextAdministrador(), variableElementUrn);
            assertEquals(1, operationsByVariable.size());
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_1);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.FUSION, variableElementOperation.getOperationType());
                assertTrue(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getSources()));
                assertFalse(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getTargets()));
            }
        }

        // Variable element only in targets
        {
            String variableElementUrn = VARIABLE_2_VARIABLE_ELEMENT_5;
            List<VariableElementOperation> operationsByVariable = codesService.retrieveVariableElementsOperationsByVariableElement(getServiceContextAdministrador(), variableElementUrn);
            assertEquals(1, operationsByVariable.size());
            {
                VariableElementOperation variableElementOperation = getVariableElementOperationByCode(operationsByVariable, VARIABLE_2_VARIABLE_ELEMENT_OPERATION_3);
                assertNotNull(variableElementOperation);
                assertEquals(VariableElementOperationTypeEnum.SEGREGATION, variableElementOperation.getOperationType());
                assertFalse(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getSources()));
                assertTrue(SrmServiceUtils.isVariableElementInList(variableElementUrn, variableElementOperation.getTargets()));
            }
        }
    }

    @Override
    @Test
    @DirtyDatabase
    @Ignore
    // TODO review test
    public void testImportVariableElementsTsv() throws Exception {

        final String variableUrn = VARIABLE_2;
        final String fileName = "importation-variable-element-01.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = false;
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importVariableElementsTsv(getServiceContextAdministrador(), variableUrn, stream, null, fileName, updateAlreadyExisting, null,
                            Boolean.TRUE);
                    assertEquals(true, taskImportTsvInfo.getIsPlannedInBackground());
                    assertNotNull(taskImportTsvInfo.getJobKey());

                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());
        // Validate variable elements
        {
            // variableElement1;Nombre corto 1;Short name 1;Nombre corto it 1
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement1");
            assertEquals("variableElement1", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 1", "en", "Short name 1", "it", "Nombre corto it 1");
            assertEquals(null, variableElement.getValidFrom());
            assertEquals(null, variableElement.getValidTo());
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // variableElement2;Nombre corto 2;;Nombre corto it 2
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement2");
            assertEquals("variableElement2", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 2", "it", "Nombre corto it 2");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // variableElement3;Nombre corto 3;;
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement3");
            assertEquals("variableElement3", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 3", "en", "Short name 3");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // variableElement4;Nombre corto 4;Short name 4;Nombre corto it 4
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement4");
            assertEquals("variableElement4", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 4", "en", "Short name 4", "it", "Nombre corto it 4");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // variableElement5;;;Nombre corto it 5
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement5");
            assertEquals("variableElement5", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "it", "Nombre corto it 5", null, null);
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
    }

    @Test
    @Ignore
    // TODO review test
    @DirtyDatabase
    public void testImportVariableElementsTsvUpdatingAlreadyExistingCodes() throws Exception {

        final boolean updateAlreadyExisting = true;

        // Import
        final String variableUrn = VARIABLE_2;
        final String fileName = "importation-variable-element-04.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importVariableElementsTsv(getServiceContextAdministrador(), variableUrn, stream, null, fileName, updateAlreadyExisting, null,
                            Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        entityManager.clear();
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED.getCode(), "VARIABLE_ELEMENT_02", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED.getCode(), "VARIABLE_ELEMENT_04", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate variable elements
        {
            // variableElement1;Nombre corto 1;Short name 1;Nombre corto it 1
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement1");
            assertEquals("variableElement1", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 1", "en", "Short name 1", "it", "Nombre corto it 1");
            assertEquals(null, variableElement.getValidFrom());
            assertEquals(null, variableElement.getValidTo());
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // VARIABLE_ELEMENT_02;Nombre corto 2;;Nombre corto it 2
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_02");
            assertEquals("VARIABLE_ELEMENT_02", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 2", "it", "Nombre corto it 2");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(22), variableElement.getId());
            BaseAsserts.assertEqualsDay(new DateTime(2011, 01, 01, 01, 02, 03, 0), variableElement.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getLastUpdated()); // today
        }
        {
            // variableElement3;Nombre corto 3;;
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement3");
            assertEquals("variableElement3", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 3", "en", "Short name 3");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // VARIABLE_ELEMENT_04;Nombre corto 4;Short name 4;Nombre corto it 4
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_04");
            assertEquals("VARIABLE_ELEMENT_04", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 4", "en", "Short name 4", "it", "Nombre corto it 4");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(24), variableElement.getId());
            BaseAsserts.assertEqualsDay(new DateTime(2012, 01, 01, 01, 02, 03, 0), variableElement.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getLastUpdated()); // today
        }
        {
            // variableElement5;;;Nombre corto it 5
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement5");
            assertEquals("variableElement5", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "it", "Nombre corto it 5", null, null);
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
    }

    @Test
    @Ignore
    // TODO review test
    @DirtyDatabase
    public void testImportVariableElementsTsvNotUpdatingAlreadyExistingCodes() throws Exception {

        final boolean updateAlreadyExisting = false;

        // Import
        final String variableUrn = VARIABLE_2;
        final String fileName = "importation-variable-element-04.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importVariableElementsTsv(getServiceContextAdministrador(), variableUrn, stream, null, fileName, updateAlreadyExisting, null,
                            Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED.getCode(), "VARIABLE_ELEMENT_02", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED.getCode(), "VARIABLE_ELEMENT_04", Boolean.FALSE, TaskResultTypeEnum.INFO, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate variable elements
        {
            // variableElement1;Nombre corto 1;Short name 1;Nombre corto it 1
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement1");
            assertEquals("variableElement1", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 1", "en", "Short name 1", "it", "Nombre corto it 1");
            assertEquals(null, variableElement.getValidFrom());
            assertEquals(null, variableElement.getValidTo());
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // VARIABLE_ELEMENT_02;Nombre corto 2;;Nombre corto it 2
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_02");
            assertEquals("VARIABLE_ELEMENT_02", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Fuerteventura", "en", "Short name variableElement 2-2");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(22), variableElement.getId());
            BaseAsserts.assertEqualsDay(new DateTime(2011, 01, 01, 01, 02, 03, 0), variableElement.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(2012, 01, 01, 01, 02, 03, 0), variableElement.getLastUpdated());
        }
        {
            // variableElement3;Nombre corto 3;;
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement3");
            assertEquals("variableElement3", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "es", "Nombre corto 3", "en", "Short name 3");
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
        {
            // VARIABLE_ELEMENT_04;Nombre corto 4;Short name 4;Nombre corto it 4
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.VARIABLE_ELEMENT_04");
            assertEquals("VARIABLE_ELEMENT_04", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "en", "short name v2-4", null, null);
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            assertEquals(Long.valueOf(24), variableElement.getId());
            BaseAsserts.assertEqualsDay(new DateTime(2012, 01, 01, 01, 02, 03, 0), variableElement.getCreatedDate());
            BaseAsserts.assertEqualsDay(new DateTime(2012, 01, 01, 01, 02, 03, 0), variableElement.getLastUpdated());
        }
        {
            // variableElement5;;;Nombre corto it 5
            VariableElement variableElement = codesService.retrieveVariableElementByUrn(getServiceContextAdministrador(),
                    "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=VARIABLE_02.variableElement5");
            assertEquals("variableElement5", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(variableElement.getIdentifiableArtefact().getUrn(), variableElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(variableElement.getShortName(), "it", "Nombre corto it 5", null, null);
            assertEquals(variableUrn, variableElement.getVariable().getNameableArtefact().getUrn());
            BaseAsserts.assertEqualsDay(new DateTime(), variableElement.getCreatedDate()); // today
        }
    }

    @Test
    @Ignore
    // TODO review test
    @DirtyDatabase
    public void testImportVariableElementsTsvErrorWithHeaderIncorrect() throws Exception {

        final String variableUrn = VARIABLE_1;

        // Import
        final String fileName = "importation-variable-element-02-errors-header.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = false;
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importVariableElementsTsv(getServiceContextAdministrador(), variableUrn, stream, null, fileName, updateAlreadyExisting, null,
                            Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN.getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE, Boolean.FALSE, type, task
                .getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Test
    @Ignore
    // TODO review test
    @DirtyDatabase
    public void testImportVariableElementsTsvErrorWithBodyIncorrect() throws Exception {

        final String variableUrn = VARIABLE_1;

        // Import
        final String fileName = "importation-variable-element-03-errors-body.tsv";
        final InputStream stream = this.getClass().getResourceAsStream("/tsv/" + fileName);
        final StringBuilder jobKey = new StringBuilder();
        final boolean updateAlreadyExisting = false;
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportTsvInfo taskImportTsvInfo = codesService.importVariableElementsTsv(getServiceContextAdministrador(), variableUrn, stream, null, fileName, updateAlreadyExisting, null,
                            Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());
                } catch (MetamacException e) {
                    fail("importation failed");
                }
            }
        });
        waitUntilJobFinished();

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(5, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT.getCode(), "3", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED.getCode(), "variableElement4WithoutShortName#@#parameter.srm.importation.short_name", Boolean.FALSE, type, task
                .getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER.getCode(), "#variableNotSemanticCode#@#parameter.srm.importation.code", Boolean.FALSE, type,
                task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT.getCode(), "7", Boolean.FALSE, type, task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);
    }

    @Override
    @Test
    public void testExportCodesTsv() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String filename = codesService.exportCodesTsv(getServiceContextAdministrador(), codelistUrn);
        assertNotNull(filename);

        // Validate
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        assertEquals("code\tparent\tvariable_element\tname#es\tname#pt\tname#en\tname#ca\tdescription#es\tdescription#pt\tdescription#en\tdescription#ca", bufferedReader.readLine());
        assertEquals("CODE01\t\tVARIABLE_ELEMENT_02\tIsla de Tenerife\t\tName codelist-1-v2-code-1\t\tDescripción codelist-1-v2-code-1\t\t\t", bufferedReader.readLine());
        assertEquals("CODE02\t\t\tNombre codelist-1-v2-code-2 Canaria, Gran\t\t\t\t\t\t\t", bufferedReader.readLine());
        assertEquals(
                "CODE0201\tCODE02\tVARIABLE_ELEMENT_01\tcodelist-1-v2-code-2- Isla de La Gomera\t\tName codelist-1-v2-code-2-1\t\tdescripción CODELIST_1_V2_CODE_2_1\t\tdescription CODELIST_1_V2_CODE_2_1\t",
                bufferedReader.readLine());
        assertEquals("CODE020101\tCODE0201\t\tSanta Cruz de La Palma codelist-1-v2-code-2-1-1\t\t\t\t\t\t\t", bufferedReader.readLine());
        assertEquals("CODE0202\tCODE02\t\tIsla de El Hierro\t\t\t\t\t\t\t", bufferedReader.readLine());
        assertEquals("CODE03\t\tVARIABLE_ELEMENT_03\tFuerteventura\t\tname code-3\t\t\t\t\t", bufferedReader.readLine());
        assertEquals("CODE04\t\t\tLanzarote\t\t\t\t\t\t\t", bufferedReader.readLine());
        assertEquals("CODE0401\tCODE04\t\tCanarias, Tenerife\t\t\t\t\t\t\t", bufferedReader.readLine());
        assertEquals("CODE040101\tCODE0401\tVARIABLE_ELEMENT_01\tNombre codelist-1-v2-code-4-1-1\t\tName codelist-1-v2-code-4-1-1\t\t\t\t\t", bufferedReader.readLine());
    }

    @Override
    @Test
    public void testExportCodeOrdersTsv() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        String filename = codesService.exportCodeOrdersTsv(getServiceContextAdministrador(), codelistUrn);
        assertNotNull(filename);

        // Validate
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        assertEquals("code\tlabel\tlevel\tparent\tVISUALISATION02\tVISUALISATION03\tALPHABETICAL", bufferedReader.readLine());
        assertEquals("CODE01\tIsla de Tenerife\t1\t\t1\t2\t1", bufferedReader.readLine());
        assertEquals("CODE02\tNombre codelist-1-v2-code-2 Canaria, Gran\t1\t\t2\t3\t2", bufferedReader.readLine());
        assertEquals("CODE0201\tcodelist-1-v2-code-2- Isla de La Gomera\t2\tCODE02\t1\t2\t1", bufferedReader.readLine());
        assertEquals("CODE020101\tSanta Cruz de La Palma codelist-1-v2-code-2-1-1\t3\tCODE0201\t1\t1\t1", bufferedReader.readLine());
        assertEquals("CODE0202\tIsla de El Hierro\t2\tCODE02\t2\t1\t2", bufferedReader.readLine());
        assertEquals("CODE03\tFuerteventura\t1\t\t3\t1\t3", bufferedReader.readLine());
        assertEquals("CODE04\tLanzarote\t1\t\t4\t4\t4", bufferedReader.readLine());
        assertEquals("CODE0401\tCanarias, Tenerife\t2\tCODE04\t1\t1\t1", bufferedReader.readLine());
        assertEquals("CODE040101\tNombre codelist-1-v2-code-4-1-1\t3\tCODE0401\t1\t1\t1", bufferedReader.readLine());
    }

    private VariableElementOperation getVariableElementOperationByCode(List<VariableElementOperation> operations, String code) {
        for (VariableElementOperation variableElementOperation : operations) {
            if (code.equals(variableElementOperation.getCode())) {
                return variableElementOperation;
            }
        }
        return null;
    }

    private CodelistOrderVisualisation assertContainsCodelistOrderVisualisation(String codelistOrderVisualisationUrnExpected, List<CodelistOrderVisualisation> actuals) {
        for (CodelistOrderVisualisation actual : actuals) {
            if (actual.getNameableArtefact().getUrn().equals(codelistOrderVisualisationUrnExpected)) {
                return actual;
            }
        }
        fail("order visualisation not found");
        return null;
    }

    private CodelistOpennessVisualisation assertContainsCodelistOpennessVisualisation(String codelistOpennessVisualisationUrnExpected, List<CodelistOpennessVisualisation> actuals) {
        for (CodelistOpennessVisualisation actual : actuals) {
            if (actual.getNameableArtefact().getUrn().equals(codelistOpennessVisualisationUrnExpected)) {
                return actual;
            }
        }
        fail("openness visualisation not found");
        return null;
    }

    private CodeMetamac getCode(List<Code> items, String urn) {
        for (Item item : items) {
            if (item.getNameableArtefact().getUrn().equals(urn)) {
                return (CodeMetamac) item;
            }
        }
        return null;
    }

    private void assertCodeVisualisations(CodeMetamacVisualisationResult codeVisualisation, Integer order, Boolean openness) {
        assertEquals(order, codeVisualisation.getOrder());
        assertEquals(openness, codeVisualisation.getOpenness());
    }

    private void assertOpennessVisualisationColumns(CodeMetamac code, List<Boolean> values) {
        int columnIndex = 1;
        for (Boolean value : values) {
            assertEquals(value, SrmServiceUtils.getCodeOpenness(code, columnIndex));
            columnIndex++;
        }
    }

    private void assertEqualsTaskResult(String codeExpected, String infoExpected, Boolean isPrincipalExpected, TaskResultTypeEnum typeExpected, TaskResult actual) {
        assertEquals(codeExpected, actual.getCode());
        assertEquals(infoExpected, actual.getParameters());
        assertEquals(isPrincipalExpected, actual.isPrincipal());
        assertEquals(typeExpected, actual.getResultType());
    }

    private VariableElementResult assertContainsVariableElementResult(String variableElementUrnExpected, List<VariableElementResult> actuals) {
        for (VariableElementResult actual : actuals) {
            if (actual.getUrn().equals(variableElementUrnExpected)) {
                return actual;
            }
        }
        fail("variable element not found");
        return null;
    }

    private CodeVariableElementNormalisationResult assertContainsCodeVariableElementNormalisationResult(String codeUrnExpected, List<CodeVariableElementNormalisationResult> actuals) {
        for (CodeVariableElementNormalisationResult actual : actuals) {
            if (actual.getCode().getUrn().equals(codeUrnExpected)) {
                return actual;
            }
        }
        fail("not found");
        return null;
    }

    private void validateVersioningCodelist3V1() throws Exception {
        String urn = CODELIST_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST03(02.000)";
        String urnExpectedCode1 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE01";
        String urnExpectedCode2 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE02";
        String urnExpectedCode21 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE0201";
        String urnExpectedCode211 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE020101";
        String urnExpectedCode22 = "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST03(02.000).CODE0202";
        String urnExpectedOrderVisualisation01 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST03(02.000).ALPHABETICAL";
        String urnExpectedOrderVisualisation02 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=SDMX01:CODELIST03(02.000).VISUALISATION02";
        String urnExpectedOpennessVisualisation01 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST03(02.000).ALL_EXPANDED";
        String urnExpectedOpennessVisualisation02 = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=SDMX01:CODELIST03(02.000).VISUALISATION02";

        // Validate response
        entityManager.clear();
        CodelistVersionMetamac codelistVersionToCopy = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        CodelistVersionMetamac codelistVersionNewVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), urnExpected);
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
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode1);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode2);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode21);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode211);
            assertListCodesContainsCode(codelistVersionNewVersion.getItems(), urnExpectedCode22);

            assertEquals(2, codelistVersionNewVersion.getItemsFirstLevel().size());
            {
                CodeMetamac code = assertListCodesContainsCode(codelistVersionNewVersion.getItemsFirstLevel(), urnExpectedCode1);
                assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-1", null, null);
                assertEqualsInternationalString(code.getNameableArtefact().getDescription(), "es", "descripción codelist-3-v1-code-1", "it", "descripción it codelist-3-v1-code-1");
                assertEquals(null, code.getNameableArtefact().getComment());
                assertEquals(null, code.getShortName());
                assertEquals(Integer.valueOf(0), code.getOrder1());
                assertEquals(Integer.valueOf(1), code.getOrder2());
                assertEquals(null, code.getOrder3());
                assertEquals(null, code.getOrder4());
                assertEquals(null, code.getOrder5());
                assertEquals(null, code.getOrder6());
                assertEquals(null, code.getOrder7());
                assertEquals(null, code.getOrder8());
                assertEquals(null, code.getOrder9());
                assertEquals(null, code.getOrder10());
                assertEquals(null, code.getOrder11());
                assertEquals(null, code.getOrder12());
                assertEquals(null, code.getOrder13());
                assertEquals(null, code.getOrder14());
                assertEquals(null, code.getOrder15());
                assertEquals(null, code.getOrder16());
                assertEquals(null, code.getOrder17());
                assertEquals(null, code.getOrder18());
                assertEquals(null, code.getOrder19());
                assertEquals(null, code.getOrder20());

                assertEquals(Boolean.TRUE, code.getOpenness1());
                assertEquals(Boolean.TRUE, code.getOpenness2());
                assertEquals(null, code.getOpenness3());
                assertEquals(null, code.getOpenness4());
                assertEquals(null, code.getOpenness5());
                assertEquals(null, code.getOpenness6());
                assertEquals(null, code.getOpenness7());
                assertEquals(null, code.getOpenness8());
                assertEquals(null, code.getOpenness9());
                assertEquals(null, code.getOpenness10());
                assertEquals(null, code.getOpenness11());
                assertEquals(null, code.getOpenness12());
                assertEquals(null, code.getOpenness13());
                assertEquals(null, code.getOpenness14());
                assertEquals(null, code.getOpenness15());
                assertEquals(null, code.getOpenness16());
                assertEquals(null, code.getOpenness17());
                assertEquals(null, code.getOpenness18());
                assertEquals(null, code.getOpenness19());
                assertEquals(null, code.getOpenness20());

                assertEquals(0, code.getChildren().size());
            }
            {
                CodeMetamac code = assertListCodesContainsCode(codelistVersionNewVersion.getItemsFirstLevel(), urnExpectedCode2);
                assertEqualsInternationalString(code.getNameableArtefact().getName(), "es", "Nombre codelist-3-v1-code-2", null, null);
                assertEqualsInternationalString(code.getShortName(), "es", "nombre corto codelist3-v1-code-2", "en", "short name codelist3-v1-code-2");
                assertEquals(Integer.valueOf(1), code.getOrder1());
                assertEquals(Integer.valueOf(0), code.getOrder2());
                assertEquals(Boolean.TRUE, code.getOpenness1());
                assertEquals(Boolean.FALSE, code.getOpenness2());

                assertEquals(2, code.getChildren().size());
                {
                    CodeMetamac codeChild = assertListCodesContainsCode(code.getChildren(), urnExpectedCode21);
                    assertEqualsInternationalString(codeChild.getShortName(), "es", "nombre corto codelist3-v1-code-2-1", "en", "short name codelist3-v1-code-2-1", "it",
                            "nombre corto it codelist3-v1-code-2-1");
                    assertEquals(urnExpectedCode21, codeChild.getNameableArtefact().getUrn());
                    assertEquals(Integer.valueOf(0), codeChild.getOrder1());
                    assertEquals(Integer.valueOf(1), codeChild.getOrder2());
                    assertEquals(Boolean.TRUE, codeChild.getOpenness1());
                    assertEquals(Boolean.TRUE, codeChild.getOpenness2());

                    assertEquals(1, codeChild.getChildren().size());
                    {
                        CodeMetamac codeChildChild = assertListCodesContainsCode(codeChild.getChildren(), urnExpectedCode211);
                        assertEquals(null, codeChildChild.getShortName());
                        assertEquals(urnExpectedCode211, codeChildChild.getNameableArtefact().getUrn());
                        assertEquals(Integer.valueOf(0), codeChildChild.getOrder1());
                        assertEquals(Integer.valueOf(0), codeChildChild.getOrder2());
                        assertEquals(Boolean.TRUE, codeChildChild.getOpenness1());
                        assertEquals(Boolean.FALSE, codeChildChild.getOpenness2());

                        assertEquals(0, codeChildChild.getChildren().size());
                    }
                }
                {
                    CodeMetamac codeChild = assertListCodesContainsCode(code.getChildren(), urnExpectedCode22);
                    assertEquals(null, codeChild.getShortName());
                    assertEquals(Integer.valueOf(1), codeChild.getOrder1());
                    assertEquals(Integer.valueOf(0), codeChild.getOrder2());
                    assertEquals(Boolean.TRUE, codeChild.getOpenness1());
                    assertEquals(Boolean.FALSE, codeChild.getOpenness2());

                    assertEquals(0, codeChild.getChildren().size());
                }
            }

            // Order visualisations
            assertEquals(urnExpectedOrderVisualisation01, codelistVersionNewVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());
            assertEquals(2, codelistVersionNewVersion.getOrderVisualisations().size());
            {
                CodelistOrderVisualisation codelistOrderVisualisation = assertContainsCodelistOrderVisualisation(urnExpectedOrderVisualisation01, codelistVersionNewVersion.getOrderVisualisations());
                assertEquals("ALPHABETICAL", codelistOrderVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(1), codelistOrderVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOrderVisualisation.getNameableArtefact().getName(), "es", "Alfabético", null, null);
            }
            {
                CodelistOrderVisualisation codelistOrderVisualisation = assertContainsCodelistOrderVisualisation(urnExpectedOrderVisualisation02, codelistVersionNewVersion.getOrderVisualisations());
                assertEquals("VISUALISATION02", codelistOrderVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(2), codelistOrderVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOrderVisualisation.getNameableArtefact().getName(), "es", "Visualización 02", null, null);
            }

            // Openness visualisations
            assertEquals(urnExpectedOpennessVisualisation01, codelistVersionNewVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn());
            assertEquals(2, codelistVersionNewVersion.getOpennessVisualisations().size());
            {
                CodelistOpennessVisualisation codelistOpennessVisualisation = assertContainsCodelistOpennessVisualisation(urnExpectedOpennessVisualisation01,
                        codelistVersionNewVersion.getOpennessVisualisations());
                assertEquals("ALL_EXPANDED", codelistOpennessVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(1), codelistOpennessVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOpennessVisualisation.getNameableArtefact().getName(), "es", "Visualización de apertura Todos abiertos 3-1", null, null);
            }
            {
                CodelistOpennessVisualisation codelistOpennessVisualisation = assertContainsCodelistOpennessVisualisation(urnExpectedOpennessVisualisation02,
                        codelistVersionNewVersion.getOpennessVisualisations());
                assertEquals("VISUALISATION02", codelistOpennessVisualisation.getNameableArtefact().getCode());
                assertEquals(Integer.valueOf(2), codelistOpennessVisualisation.getColumnIndex());
                assertEqualsInternationalString(codelistOpennessVisualisation.getNameableArtefact().getName(), "es", "Visualización 02 3-1", null, null);
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

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }

}

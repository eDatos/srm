package org.siemac.metamac.srm.core.concept.serviceapi;

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
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
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
import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptProperties;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ConceptsMetamacServiceTest extends SrmBaseTest implements ConceptsMetamacServiceTestBase {

    @Autowired
    private ConceptsMetamacService        conceptsService;

    @Autowired
    private CodesMetamacService           codesService;

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
    public void testCreateConceptScheme() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
        String urn = conceptSchemeVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", conceptSchemeVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), conceptSchemeVersionCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        ConceptSchemeVersionMetamac conceptSchemeVersionRetrieved = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(conceptSchemeVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationUser());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationUser());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationUser());
        assertFalse(conceptSchemeVersionRetrieved.getMaintainableArtefact().getFinalLogicClient());
        assertEquals(ctx.getUserId(), conceptSchemeVersionRetrieved.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptSchemeVersionRetrieved.getLastUpdatedBy());
        ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersion, conceptSchemeVersionRetrieved);
    }

    @Test
    public void testCreateConceptSchemeErrorMaintainerNotDefault() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_2_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);

        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
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
    public void testCreateConceptSchemeErrorMetadataRequired() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);
        conceptSchemeVersion.setType(null);
        conceptSchemeVersion.setRelatedOperation(null); // avoid error unexpected metadata

        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptSchemeErrorMetadataUnexpected() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.GLOSSARY);
        assertNotNull(conceptSchemeVersion.getRelatedOperation());

        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("metadatas unexpected");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testUpdateConceptScheme() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_2_V1);
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);

        ConceptSchemeVersion conceptSchemeVersionUpdated = conceptsService.updateConceptScheme(ctx, conceptSchemeVersion);
        assertNotNull(conceptSchemeVersionUpdated);
        assertEquals("user1", conceptSchemeVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptSchemeVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateConceptSchemeChangingCode() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1);

        // Change code
        conceptSchemeVersion.getMaintainableArtefact().setCode("codeNew");
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);

        ConceptSchemeVersion conceptSchemeVersionUpdated = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:codeNew(01.000)", conceptSchemeVersionUpdated.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testUpdateConceptSchemeFromGlossaryToOperationTypeErrorRelatedOperationRequired() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1);

        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.TRUE);

        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("empty related operation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateConceptSchemeFromOperationToGlossaryTypeErrorRelatedOperationUnexpected() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_8_V1);
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.TRUE);

        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("unexpected related operation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptSchemePublished() throws Exception {
        String[] urns = {CONCEPT_SCHEME_7_V2, CONCEPT_SCHEME_7_V1};
        for (String urn : urns) {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);

            try {
                conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
                conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
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
    public void testUpdateConceptSchemeErrorExternalReference() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V2);
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);

        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("concept scheme cannot be a external reference");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptSchemeErrorMetadataRequired() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1);
        conceptSchemeVersion.setType(null);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.TRUE);
        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("wrong metadata");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptSchemeErrorChangeTypeHasChildren() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1);
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.TRUE);
        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("type can not be modified because the concept scheme has children");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptSchemeErrorChangeTypeConceptSchemeAlreadyPublished() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2);
        assertEquals(ConceptSchemeTypeEnum.TRANSVERSAL, conceptSchemeVersion.getType());
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.TRUE);

        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("metadata unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptSchemeErrorChangeCodeInConceptSchemeWithVersionAlreadyPublished() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2);
        conceptSchemeVersion.getMaintainableArtefact().setCode("newCode");
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);

        try {
            conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
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
    public void testRetrieveConceptSchemeByUrn() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_1_V1;
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, conceptSchemeVersion.getMaintainableArtefact().getUrn());
        assertEquals(ConceptSchemeTypeEnum.TRANSVERSAL, conceptSchemeVersion.getType());
        assertNull(conceptSchemeVersion.getRelatedOperation());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnWithRelatedOperation() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_8_V1;
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(urn, conceptSchemeVersion.getMaintainableArtefact().getUrn());
        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeVersion.getType());
        assertEquals("op1", conceptSchemeVersion.getRelatedOperation().getCode());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=op1", conceptSchemeVersion.getRelatedOperation().getUrn());
        assertEquals("/operations/op1", conceptSchemeVersion.getRelatedOperation().getUri());
        assertEquals(TypeExternalArtefactsEnum.STATISTICAL_OPERATION, conceptSchemeVersion.getRelatedOperation().getType());
        assertEquals("/#operations;id=op1", conceptSchemeVersion.getRelatedOperation().getManagementAppUrl());
    }

    @Test
    @Override
    public void testFindConceptSchemesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(18, conceptSchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_9_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(conceptSchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find internally published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                    .orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(4, conceptSchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(conceptSchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find lasts versions
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE)
                    .orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(14, conceptSchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_9_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testFindConceptSchemesByConditionWithConceptsCanBeRole() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByConditionWithConceptsCanBeRole(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(1, conceptSchemeVersionPagedResult.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
    }

    @Test
    @Override
    public void testFindConceptSchemesByConditionWithConceptsCanBeExtended() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByConditionWithConceptsCanBeExtended(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(1, conceptSchemeVersionPagedResult.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
    }

    @Test
    @Override
    public void testRetrieveConceptSchemeVersions() throws Exception {

        // Retrieve all versions
        String urn = CONCEPT_SCHEME_1_V1;
        List<ConceptSchemeVersionMetamac> conceptSchemeVersions = conceptsService.retrieveConceptSchemeVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, conceptSchemeVersions.size());
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersions.get(1).getMaintainableArtefact().getUrn());
    }

    @Override
    @Test
    public void testSendConceptSchemeToProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationInProcStatusRejected() throws Exception {

        String urn = CONCEPT_SCHEME_4_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationCheckAssignVariableConcepts() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        ConceptMetamac concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept1.setVariable(null);
        concept1.getNameableArtefact().setIsCodeUpdated(false);
        concept1 = (ConceptMetamac) itemRepository.save(concept1); // update in repository, because service will assign it automatically in update
        assertEquals(null, concept1.getVariable());

        ConceptMetamac concept2 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertEquals(null, concept2.getVariable());

        // Send to production validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToProductionValidation(ctx, urn);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());

        // Validate concepts
        concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertEquals(VARIABLE_1, concept1.getVariable().getNameableArtefact().getUrn());
        concept2 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertEquals(null, concept2.getVariable());
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
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
    public void testSendConceptSchemeToProductionValidationErrorMetadataRequired() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        // Update to clear required metadata to send to production
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        conceptSchemeVersion.setIsPartial(null);
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
        conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);

        // Send to production validation
        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorToImportedRequiredMetadataInConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
        // save to force incorrect metadata
        conceptSchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.TRUE);
        conceptSchemeVersion.setType(null);
        conceptSchemeVersion.setIsPartial(Boolean.FALSE);
        itemSchemeRepository.save(conceptSchemeVersion);

        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorToImportedRequiredMetadataInConcept() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2;
        ServiceContext ctx = getServiceContextAdministrador();
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
        assertEquals(ConceptSchemeTypeEnum.TRANSVERSAL, conceptSchemeVersion.getType());

        // save to force it is imported
        conceptSchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        conceptSchemeVersion.setIsPartial(Boolean.FALSE);
        itemSchemeRepository.save(conceptSchemeVersion);

        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        concept.setSdmxRelatedArtefact(null);
        // save to force incorrect metadata
        itemRepository.save(concept);

        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            MetamacAsserts.assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_INCORRECT_METADATA, 1, new String[]{concept.getNameableArtefact().getUrn()}, e.getExceptionItems().get(0));
        }
    }

    @Override
    @Test
    public void testSendConceptSchemeToDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Sends to diffusion validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        try {
            conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
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
    public void testRejectConceptSchemeProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Reject validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.rejectConceptSchemeProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate restrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            conceptsService.rejectConceptSchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectConceptSchemeProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            conceptsService.rejectConceptSchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
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
    public void testRejectConceptSchemeDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Reject validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            conceptsService.rejectConceptSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            conceptsService.rejectConceptSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
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
    public void testPublishInternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }

        // Publish internally
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.publishInternallyConceptScheme(ctx, urn, Boolean.FALSE);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getLatestFinal());
        }
    }

    @Test
    public void testPublishInternallyConceptSchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishInternallyConceptSchemeErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    public void testCheckConceptSchemeVersionTranslations() throws Exception {
        // Tested in testPublishInternallyConceptSchemeCheckTranslations
    }

    @Test
    public void testPublishInternallyConceptSchemeCheckTranslations() throws Exception {
        String urn = CONCEPT_SCHEME_14_V1;
        String code = "CONCEPTSCHEME14";

        try {
            // Note: publishInternallyConceptScheme calls to 'checkConceptSchemeVersionTranslates'
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("ConceptScheme wrong translations");
        } catch (MetamacException e) {
            assertEquals(16, e.getExceptionItems().size());
            int i = 0;
            // ConceptScheme
            assertEqualsMetamacExceptionItem(ServiceExceptionType.MAINTAINABLE_ARTEFACT_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{
                    ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION, code}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.MAINTAINABLE_ARTEFACT_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{code}, e.getExceptionItems().get(i++));
            // Concepts
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2,
                    new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME, "CONCEPT01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT,
                    "CONCEPT01"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION,
                    "CONCEPT0101"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2,
                    new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME, "CONCEPT02"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"CONCEPT02"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_ANNOTATION_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 1, new String[]{"CONCEPT03"}, e.getExceptionItems().get(i++));

            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_ACRONYM, "CONCEPT01"}, e
                    .getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_DERIVATION, "CONCEPT01"}, e
                    .getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2,
                    new String[]{ServiceExceptionParameters.CONCEPT_PLURAL_NAME, "CONCEPT0101"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_CONTEXT, "CONCEPT0101"}, e
                    .getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_DOC_METHOD, "CONCEPT02"}, e
                    .getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_PLURAL_NAME, "CONCEPT03"},
                    e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE,
                    "CONCEPT03"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.ITEM_WITH_METADATA_WITHOUT_TRANSLATION_DEFAULT_LOCALE, 2, new String[]{ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, "CONCEPT03"}, e
                    .getExceptionItems().get(i++));

            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Override
    @Test
    public void testPublishExternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());

            ConceptSchemeVersionMetamac conceptSchemeVersionExternallyPublished = conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertNull(conceptSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.publishExternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(conceptSchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
        }
        // Validate previous published externally versions
        {
            ConceptSchemeVersionMetamac conceptSchemeVersionExternallyPublished = conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo().toDate()));
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
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
    public void testDeleteConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        // Delete concept scheme only with version in draft
        conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("ConceptScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteConceptSchemeWithVersionPublishedAndVersionDraft() throws Exception {

        String urnV1 = CONCEPT_SCHEME_1_V1;
        String urnV2 = CONCEPT_SCHEME_1_V2;

        ConceptSchemeVersionMetamac conceptSchemeVersionV1 = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersionV1.getLifeCycleMetadata().getProcStatus());
        ConceptSchemeVersionMetamac conceptSchemeVersionV2 = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV2);
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionV2.getLifeCycleMetadata().getProcStatus());

        conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV2);
            fail("ConceptScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        conceptSchemeVersionV1 = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(conceptSchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(conceptSchemeVersionV1.getMaintainableArtefact().getReplacedByVersion());
    }

    @Test
    public void testDeleteConceptSchemeErrorPublished() throws Exception {

        String urn = CONCEPT_SCHEME_10_V2;

        // Validation
        try {
            conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme can not be deleted");
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
    public void testCopyConceptScheme() throws Exception {

        String urnToCopy = CONCEPT_SCHEME_14_V1;
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        String versionExpected = "01.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME14(01.000)";
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME14(01.000).CONCEPT01";
        String urnExpectedConcept11 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME14(01.000).CONCEPT0101";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME14(01.000).CONCEPT02";
        String urnExpectedConcept3 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME14(01.000).CONCEPT03";

        TaskInfo copyResult = conceptsService.copyConceptScheme(getServiceContextAdministrador(), urnToCopy);

        // Validate (only some metadata, already tested in statistic module)
        entityManager.clear();
        ConceptSchemeVersionMetamac conceptSchemeVersionNewArtefact = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), copyResult.getUrnResult());
        assertEquals(maintainerUrnExpected, conceptSchemeVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, conceptSchemeVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, conceptSchemeVersionNewArtefact.getMaintainableArtefact().getUrn());
        assertEquals(null, conceptSchemeVersionNewArtefact.getMaintainableArtefact().getReplaceToVersion());
        assertEquals(null, conceptSchemeVersionNewArtefact.getMaintainableArtefact().getReplacedByVersion());
        assertTrue(conceptSchemeVersionNewArtefact.getMaintainableArtefact().getIsLastVersion());

        // Concepts
        assertEquals(4, conceptSchemeVersionNewArtefact.getItems().size());
        assertListItemsContainsItem(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept1);
        assertListItemsContainsItem(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept11);
        assertListItemsContainsItem(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept2);
        assertListItemsContainsItem(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept3);

        assertEquals(3, conceptSchemeVersionNewArtefact.getItemsFirstLevel().size());
        {
            ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedConcept1);
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "en", "name concept1", "it", "nombre it concept1");
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getDescription(), "es", "descripción concept1", "it", "descripción it concept1");
            assertEquals(null, concept.getNameableArtefact().getComment());
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "plural name es concept1", null, null);
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "en", "acronym es concept1", null, null);
            assertEquals(null, concept.getDescriptionSource());
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getContext(), "es", "contexto concept1", null, null);
            assertEquals(null, concept.getDocMethod());
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDerivation(), "en", "derivation concept1", null, null);
            assertEquals(null, concept.getLegalActs());
            assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
            assertEquals(CONCEPT_TYPE_DERIVED, concept.getConceptType().getIdentifier());
            assertEquals(null, concept.getConceptExtends());
            assertEquals(null, concept.getVariable());

            assertEquals(1, concept.getChildren().size());
            {
                ConceptMetamac conceptChild = assertListConceptsContainsConcept(concept.getChildren(), urnExpectedConcept11);
                assertEquals(0, conceptChild.getChildren().size());
            }

        }
        {
            ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedConcept2);
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "en", "name concept2", null, null);
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "plural name es concept2", "en", "plural name concept2");
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "es", "acronym es concept2", null, null);
            assertEquals(null, concept.getDescriptionSource());
            assertEquals(null, concept.getContext());
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDocMethod(), "en", "doc method concept2", null, null);
            assertEquals(null, concept.getDerivation());
            assertEquals(null, concept.getLegalActs());
            assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
            assertEquals(CONCEPT_TYPE_DERIVED, concept.getConceptType().getIdentifier());
            assertEquals(null, concept.getConceptExtends());
            assertEquals(null, concept.getVariable());

            assertEquals(0, concept.getChildren().size());
        }
        {
            ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedConcept3);
            ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "es", "nombre concept-3", "en", "name concept-3");

            assertEquals(0, concept.getChildren().size());
        }
    }

    @Override
    @Test
    public void testVersioningConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME03(02.000)";
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT02";
        String urnExpectedConcept21 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT0201";
        String urnExpectedConcept211 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT020101";
        String urnExpectedConcept22 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT0202";

        TaskInfo versioningResult = conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(conceptSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);

            // Concepts
            assertEquals(5, conceptSchemeVersionNewVersion.getItems().size());
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept1);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept2);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept21);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept211);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept22);

            assertEquals(2, conceptSchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedConcept1);

                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "es", "Nombre conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getDescription(), "es", "descripción concept1", "it", "descripción it concept1");
                assertEquals(null, concept.getNameableArtefact().getComment());
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "es", "Acronym conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDescriptionSource(), "es", "DescriptionSource conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getContext(), "es", "Context conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDocMethod(), "es", "DocMethod conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDerivation(), "es", "Derivation conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getLegalActs(), "es", "LegalActs conceptScheme-3-v1-concept-1", null, null);
                assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
                assertEquals(CONCEPT_TYPE_DERIVED, concept.getConceptType().getIdentifier());
                assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());
                assertEquals(VARIABLE_1, concept.getVariable().getNameableArtefact().getUrn());

                assertEquals(0, concept.getChildren().size());
            }
            {
                ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedConcept2);
                assertEquals(urnExpectedConcept2, concept.getNameableArtefact().getUrn());

                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "es", "Nombre conceptScheme-3-v1-concept-2", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-3-v1-concept-2", null, null);
                assertNull(concept.getAcronym());
                assertNull(concept.getDescriptionSource());
                assertNull(concept.getContext());
                assertNull(concept.getDocMethod());
                assertNull(concept.getDerivation());
                assertNull(concept.getLegalActs());
                assertNull(concept.getSdmxRelatedArtefact());
                assertNull(concept.getConceptType());
                assertNull(concept.getConceptExtends());
                assertEquals(VARIABLE_2, concept.getVariable().getNameableArtefact().getUrn());

                assertEquals(2, concept.getChildren().size());
                {
                    ConceptMetamac conceptChild = assertListConceptsContainsConcept(concept.getChildren(), urnExpectedConcept21);
                    assertNull(conceptChild.getVariable());

                    assertEquals(1, conceptChild.getChildren().size());
                    {
                        ConceptMetamac conceptChildChild = assertListConceptsContainsConcept(conceptChild.getChildren(), urnExpectedConcept211);
                        assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptChildChild.getConceptExtends().getNameableArtefact().getUrn());

                        assertEquals(0, conceptChildChild.getChildren().size());
                    }
                }
                {
                    ConceptMetamac conceptChild = assertListConceptsContainsConcept(concept.getChildren(), urnExpectedConcept22);
                    assertEquals(urnExpectedConcept22, conceptChild.getNameableArtefact().getUrn());

                    assertEquals(0, conceptChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, conceptSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, conceptSchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, conceptSchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(conceptSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<ConceptSchemeVersionMetamac> allVersions = conceptsService.retrieveConceptSchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningConceptSchemeWithTwoVersionsPublished() throws Exception {

        // This test checks the copy from one version but replacing to another one that is last version.

        String urnToCopy = CONCEPT_SCHEME_7_V1;
        String urnLastVersion = CONCEPT_SCHEME_7_V2;
        String versionExpected = "03.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME07(03.000)";

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersionToCopy.getLifeCycleMetadata().getProcStatus());
        assertFalse(conceptSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        ConceptSchemeVersionMetamac conceptSchemeVersionLast = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersionLast.getLifeCycleMetadata().getProcStatus());
        assertTrue(conceptSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

        TaskInfo versioningResult = conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());
        {
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals("02.000", conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(conceptSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);

            // Version copied
            conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, conceptSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, conceptSchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals("02.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(conceptSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            conceptSchemeVersionLast = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", conceptSchemeVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, conceptSchemeVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", conceptSchemeVersionLast.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, conceptSchemeVersionLast.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(conceptSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

            // All versions
            List<ConceptSchemeVersionMetamac> allVersions = conceptsService.retrieveConceptSchemeVersions(getServiceContextAdministrador(), conceptSchemeVersionNewVersion.getMaintainableArtefact()
                    .getUrn());
            assertEquals(3, allVersions.size());
            assertEquals(urnToCopy, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnLastVersion, allVersions.get(1).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(2).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningConceptSchemeCheckConceptRelations() throws Exception {

        String urn = CONCEPT_SCHEME_3_V1;

        // Versioning
        conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT02";
        String urnExpectedConcept2_1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT0201";
        String urnExpectedConcept2_1_1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT020101";
        String urnExpectedConcept2_2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(02.000).CONCEPT0202";

        // Check ROLE CONCEPTS after versioning
        entityManager.clear();

        {
            // Concept 1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urnExpectedConcept1);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_13_V1_CONCEPT_1);
            }

            // Concept 2
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urnExpectedConcept2);
                assertEquals(0, relatedConceptsRole.size());
            }
            // Concept 2_1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urnExpectedConcept2_1);
                assertEquals(0, relatedConceptsRole.size());
            }
            // Concept 2_1_1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urnExpectedConcept2_1_1);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_13_V1_CONCEPT_2);
            }
            // Concept 2_2
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urnExpectedConcept2_2);
                assertEquals(2, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_13_V1_CONCEPT_1);
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_13_V1_CONCEPT_2);
            }
        }

        // Check RELATED CONCEPTS after versioning
        {
            // Concept 1
            {
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept1);
                assertEquals(1, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2_1);
            }

            // Concept 2
            {
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2);
                assertEquals(1, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2_1_1);
            }
            // Concept 2_1
            {
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2_1);
                assertEquals(2, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept1);
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2_1_1);
            }
            // Concept 2_1_1
            {
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2_1_1);
                assertEquals(3, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2);
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2_1);
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2_2);
            }
            // Concept 2_2
            {
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2_2);
                assertEquals(1, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2_1_1);
            }
        }
    }

    @Test
    public void testVersioningConceptSchemeErrorAlreadyExistsDraft() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        try {
            conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("ConceptScheme already exists in no final");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningConceptSchemeErrorNotPublished() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        try {
            conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("ConceptScheme not published");
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
    public void testCreateTemporalVersionConceptScheme() throws Exception {
        String urn = CONCEPT_SCHEME_3_V1;
        String versionExpected = "01.000" + UrnConstants.URN_SDMX_TEMPORAL_SUFFIX;
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME03(" + versionExpected + ")";
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(" + versionExpected + ").CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(" + versionExpected + ").CONCEPT02";
        String urnExpectedConcept21 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(" + versionExpected + ").CONCEPT0201";
        String urnExpectedConcept211 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(" + versionExpected + ").CONCEPT020101";
        String urnExpectedConcept22 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME03(" + versionExpected + ").CONCEPT0202";

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = conceptsService.createTemporalVersionConceptScheme(getServiceContextAdministrador(), urn);

        // Validate response
        entityManager.clear();
        conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(conceptSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);

            // Concepts
            assertEquals(5, conceptSchemeVersionNewVersion.getItems().size());
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept1);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept2);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept21);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept211);
            assertListItemsContainsItem(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept22);

            assertEquals(2, conceptSchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedConcept1);

                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "es", "Nombre conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "es", "Acronym conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDescriptionSource(), "es", "DescriptionSource conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getContext(), "es", "Context conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDocMethod(), "es", "DocMethod conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDerivation(), "es", "Derivation conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getLegalActs(), "es", "LegalActs conceptScheme-3-v1-concept-1", null, null);
                assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
                assertEquals(CONCEPT_TYPE_DERIVED, concept.getConceptType().getIdentifier());
                assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());
                assertEquals(VARIABLE_1, concept.getVariable().getNameableArtefact().getUrn());

                assertEquals(0, concept.getChildren().size());
            }
            {
                ConceptMetamac concept = assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedConcept2);
                assertEquals(urnExpectedConcept2, concept.getNameableArtefact().getUrn());

                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "es", "Nombre conceptScheme-3-v1-concept-2", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-3-v1-concept-2", null, null);
                assertNull(concept.getAcronym());
                assertNull(concept.getDescriptionSource());
                assertNull(concept.getContext());
                assertNull(concept.getDocMethod());
                assertNull(concept.getDerivation());
                assertNull(concept.getLegalActs());
                assertNull(concept.getSdmxRelatedArtefact());
                assertNull(concept.getConceptType());
                assertNull(concept.getConceptExtends());
                assertEquals(VARIABLE_2, concept.getVariable().getNameableArtefact().getUrn());

                assertEquals(2, concept.getChildren().size());
                {
                    ConceptMetamac conceptChild = assertListConceptsContainsConcept(concept.getChildren(), urnExpectedConcept21);
                    assertNull(conceptChild.getVariable());

                    assertEquals(1, conceptChild.getChildren().size());
                    {
                        ConceptMetamac conceptChildChild = assertListConceptsContainsConcept(conceptChild.getChildren(), urnExpectedConcept211);
                        assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptChildChild.getConceptExtends().getNameableArtefact().getUrn());

                        assertEquals(0, conceptChildChild.getChildren().size());
                    }
                }
                {
                    ConceptMetamac conceptChild = assertListConceptsContainsConcept(concept.getChildren(), urnExpectedConcept22);
                    assertEquals(urnExpectedConcept22, conceptChild.getNameableArtefact().getUrn());

                    assertEquals(0, conceptChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, conceptSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertFalse(conceptSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<ConceptSchemeVersionMetamac> allVersions = conceptsService.retrieveConceptSchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testCreateVersionFromTemporalConceptScheme() throws Exception {
        String urn = CONCEPT_SCHEME_3_V1;

        TaskInfo versioningResult1 = conceptsService.createTemporalVersionConceptScheme(getServiceContextAdministrador(), urn);
        entityManager.clear();
        ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult1.getUrnResult());
        assertEquals(3, conceptSchemeVersionTemporal.getMaintainableArtefact().getCategorisations().size());
        assertListContainsCategorisation(conceptSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000_temporal)");
        assertListContainsCategorisation(conceptSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat2(01.000_temporal)");
        assertListContainsCategorisation(conceptSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat3(01.000_temporal)");

        TaskInfo versioningResult2 = conceptsService.createVersionFromTemporalConceptScheme(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn(),
                VersionTypeEnum.MAJOR);
        entityManager.clear();
        ConceptSchemeVersionMetamac conceptSchemeNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult2.getUrnResult());

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME03(" + versionExpected + ")";

        // Validate
        {
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeNewVersion.getMaintainableArtefact().getUrn());

            assertEquals(null, conceptSchemeNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(conceptSchemeNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(conceptSchemeNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(conceptSchemeNewVersion.getMaintainableArtefact().getLatestPublic());

            assertEquals(3, conceptSchemeNewVersion.getMaintainableArtefact().getCategorisations().size());
            assertListContainsCategorisation(conceptSchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)");
            assertListContainsCategorisation(conceptSchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat5(01.000)");
            assertListContainsCategorisation(conceptSchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)");
        }
    }

    @Override
    @Test
    public void testMergeTemporalVersion() throws Exception {
        {
            String urn = CONCEPT_SCHEME_3_V1;
            TaskInfo versioningResult = conceptsService.createTemporalVersionConceptScheme(getServiceContextAdministrador(), urn);

            entityManager.clear();
            ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

            // Change temporal version *********************

            // Item scheme: Change Name
            {
                LocalisedString localisedString = new LocalisedString("fr", "its - text sample");
                conceptSchemeVersionTemporal.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Item 1: Change plural name
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CONCEPT_SCHEME_3_V1_CONCEPT_1));
                conceptTemporal.setSdmxRelatedArtefact(ConceptRoleEnum.MEASURE_DIMENSION);

                LocalisedString localisedString = new LocalisedString("fr", "it - text sample");
                conceptTemporal.getPluralName().addText(localisedString);
            }
            // Item 2: Add legal acts
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CONCEPT_SCHEME_3_V1_CONCEPT_2));
                conceptTemporal.setSdmxRelatedArtefact(ConceptRoleEnum.MEASURE_DIMENSION);

                LocalisedString localisedString = new LocalisedString("fr", "it - text sample legal acts");
                conceptTemporal.setLegalActs(new InternationalString());
                conceptTemporal.getLegalActs().addText(localisedString);
            }
            // Merge
            conceptSchemeVersionTemporal = conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
            conceptSchemeVersionTemporal = conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
            ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = conceptsService.mergeTemporalVersion(getServiceContextAdministrador(), conceptSchemeVersionTemporal);

            // Assert **************************************

            // Item Scheme
            assertEquals(2, conceptSchemeVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("its - text sample", conceptSchemeVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));

            // Item 1: plural name changed
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_1);
                assertTrue(ConceptRoleEnum.MEASURE_DIMENSION.equals(conceptTemporal.getSdmxRelatedArtefact()));
                assertEquals(2, conceptTemporal.getPluralName().getTexts().size());
                assertEquals("it - text sample", conceptTemporal.getPluralName().getLocalisedLabel("fr"));
            }
            // Item 2: legal acts changed
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_2);
                assertEquals(1, conceptTemporal.getLegalActs().getTexts().size());
                assertEquals("it - text sample legal acts", conceptTemporal.getLegalActs().getLocalisedLabel("fr"));
            }
        }

        {
            // save to no final
            ConceptSchemeVersionMetamac conceptSchemeForce = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_10_V3);
            conceptSchemeForce.getMaintainableArtefact().setFinalLogic(Boolean.TRUE);
            conceptSchemeForce.getMaintainableArtefact().setFinalLogicClient(Boolean.TRUE);
            conceptSchemeForce.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
            itemSchemeRepository.save(conceptSchemeForce);

            String urn = CONCEPT_SCHEME_10_V1;
            TaskInfo versioningResult = conceptsService.createTemporalVersionConceptScheme(getServiceContextAdministrador(), urn);

            entityManager.clear();
            ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

            assertTrue(conceptSchemeVersionTemporal.getMaintainableArtefact().getIsLastVersion());

            // Merge
            conceptSchemeVersionTemporal.getLifeCycleMetadata().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
            itemSchemeRepository.save(conceptSchemeVersionTemporal);
            ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = conceptsService.mergeTemporalVersion(getServiceContextAdministrador(), conceptSchemeVersionTemporal);

            assertFalse(conceptSchemeVersionMetamac.getMaintainableArtefact().getIsLastVersion());
        }
    }

    @Override
    @Test
    public void testVersioningRelatedConcepts() throws Exception {
        // tested in testVersioningConceptSchemeCheckConceptRelations
    }

    @Override
    @Test
    public void testEndConceptSchemeValidity() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.endConceptSchemeValidity(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1);

        assertNotNull(conceptSchemeVersion);
        assertNotNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testEndConceptSchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CONCEPT_SCHEME_1_V1, CONCEPT_SCHEME_4_V1, CONCEPT_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                conceptsService.endConceptSchemeValidity(getServiceContextAdministrador(), urn);
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
    public void testCreateConcept() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1));
        concept.setParent(null);
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V1_CONCEPT_1));
        concept.setVariable(codesService.retrieveVariableByUrn(ctx, VARIABLE_1));
        Representation enumeratedRepresentation = new Representation();
        enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
        enumeratedRepresentation.setEnumerationCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V1));
        concept.setCoreRepresentation(enumeratedRepresentation);

        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // Create
        ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
        String urn = conceptCreated.getNameableArtefact().getUrn();
        assertEquals(ctx.getUserId(), conceptCreated.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptCreated.getLastUpdatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(ctx, urn);
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);

        // Validate new structure
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        assertEquals(5, conceptSchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, conceptSchemeVersion.getItems().size());
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), conceptRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptSubconcept() throws Exception {

        ConceptType conceptType = null;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1));
        ConceptMetamac conceptParent = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.setParent(conceptParent);
        concept.setCoreRepresentation(null);
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // Create
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);

        // Validate new structure
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(4, conceptSchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, conceptSchemeVersion.getItems().size());

        Item concept1 = assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsItem(concept1.getChildren(), conceptRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptEnumeratedRepresentationAssignAutomaticallyVariable() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1));
        concept.setParent(null);

        // Concept has not variable
        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1);
        Representation enumeratedRepresentation = new Representation();
        enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
        enumeratedRepresentation.setEnumerationCodelist(codelistVersion);
        concept.setCoreRepresentation(enumeratedRepresentation);
        concept.setVariable(null);

        concept = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
        assertEquals(codelistVersion.getVariable().getNameableArtefact().getUrn(), concept.getVariable().getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptErrorEnumeratedRepresentation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1));
        concept.setParent(null);
        concept.setVariable(codesService.retrieveVariableByUrn(ctx, VARIABLE_1));

        // Codelist has not same variable
        {
            CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1);
            try {
                assertFalse(codelistVersion.getVariable().getNameableArtefact().getUrn().equals(concept.getVariable().getNameableArtefact().getUrn()));
                Representation enumeratedRepresentation = new Representation();
                enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
                enumeratedRepresentation.setEnumerationCodelist(codelistVersion);
                concept.setCoreRepresentation(enumeratedRepresentation);

                conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
                fail("wrong enumerated representation");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_DIFFERENT_VARIABLE.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(concept.getNameableArtefact().getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(codelistVersion.getMaintainableArtefact().getUrn(), e.getExceptionItems().get(0).getMessageParameters()[1]);
            }
        }
    }

    @Test
    public void testCreateConceptErrorConceptExtendsWrongProcStatus() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null);
        concept.setParent(null);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_1);
        concept.setConceptExtends(conceptExtends);

        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_7_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testCreateConceptErrorConceptIsRolAndCanNotHaveConceptExtends() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null);
        concept.setParent(null);
        concept.setSdmxRelatedArtefact(null);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_1);
        concept.setConceptExtends(conceptExtends);

        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1; // role type

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(conceptSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_GLOSSARY, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_OPERATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_TRANSVERSAL, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_MEASURE, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testCreateConceptErrorConceptIsRolAndCanNotHaveVariable() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null);
        concept.setParent(null);
        concept.setSdmxRelatedArtefact(null);
        concept.setVariable(codesService.retrieveVariableByUrn(ctx, VARIABLE_1));

        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1; // role type

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("error");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_VARIABLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorMetadataIncorrect() throws Exception {

        ConceptType conceptType = null;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null);
        concept.setPluralName(new InternationalString());
        concept.setDocMethod(new InternationalString());
        concept.getDocMethod().addText(new LocalisedString());
        concept.setLegalActs(new InternationalString());
        LocalisedString lsLegalActs = new LocalisedString();
        lsLegalActs.setLocale("es");
        concept.getLegalActs().addText(lsLegalActs);

        try {
            conceptsService.createConcept(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2, concept);
            fail("parameters incorrect");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_PLURAL_NAME, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_DOC_METHOD, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, e.getExceptionItems().get(2).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorExtendsSameConceptScheme() throws Exception {

        ConceptType conceptType = null;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.setConceptExtends(conceptExtends);
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // Create
        try {
            conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
            fail("Concept deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_EXTENDS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorConceptImported() throws Exception {
        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(getServiceContextAdministrador(), CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1));
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        // save to force incorrect metadata
        conceptSchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        conceptSchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(conceptSchemeVersion);

        try {
            conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    public void testCheckConceptEnumeratedRepresentation() throws Exception {
        // already tested in testCreateConcept*
    }

    @Override
    @Test
    public void testUpdateConcept() throws Exception {

        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1);
        try {
            assertFalse(codelistVersion.getVariable().getNameableArtefact().getUrn().equals(concept.getVariable().getNameableArtefact().getUrn()));
            Representation enumeratedRepresentation = new Representation();
            enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
            enumeratedRepresentation.setEnumerationCodelist(codelistVersion);
            concept.setCoreRepresentation(enumeratedRepresentation);

            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
            fail("wrong enumerated representation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_DIFFERENT_VARIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(concept.getNameableArtefact().getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(codelistVersion.getMaintainableArtefact().getUrn(), e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateConceptErrorEnumeratedRepresentation() throws Exception {

        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1_CONCEPT_1));
        assertTrue(RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType()));
        concept.setCoreRepresentation(ConceptsDoMocks.mockTextFormatRepresentation());

        // Update
        ConceptMetamac conceptUpdated = conceptsService.updateConcept(getServiceContextAdministrador(), concept);

        // Validate
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptUpdated);

        // Update to remove metadata 'extends'
        conceptUpdated.setConceptExtends(null);
        conceptUpdated = conceptsService.updateConcept(getServiceContextAdministrador(), concept);

        // Validate
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptUpdated);
    }

    @Test
    public void testUpdateConceptErrorConceptExtendsWrongProcStatus() throws Exception {

        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V2_CONCEPT_1));

        // Create
        try {
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_7_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveConceptByUrn() throws Exception {
        // Retrieve
        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, concept.getNameableArtefact().getUrn());
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "es", "Acrónimo conceptScheme-1-v2-concept-1", "en", "Acronym conceptScheme-1-v2-concept-1");
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDescriptionSource(), "es", "DescriptionSource conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getContext(), "es", "Context conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDocMethod(), "es", "DocMethod conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDerivation(), "es", "Derivation conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getLegalActs(), "es", "LegalActs conceptScheme-1-v2-concept-1", null, null);
        assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
        assertEquals(CONCEPT_TYPE_DIRECT, concept.getConceptType().getIdentifier());
        assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());
        assertEquals(VARIABLE_1, concept.getVariable().getNameableArtefact().getUrn());
    }

    @Test
    public void testRetrieveConceptByUrnWithParentAndChildren() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals("conceptScheme-1-v2-concept-2-1", concept.getUuid());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, concept.getParent().getNameableArtefact().getUrn());
        assertEquals(1, concept.getChildren().size());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, concept.getChildren().get(0).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2, concept.getItemSchemeVersion().getMaintainableArtefact().getUrn());
        assertEquals(null, concept.getItemSchemeVersionFirstLevel());
    }

    @Override
    @Test
    public void testDeleteConcept() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptExtendsBeforeDeleteUrn = CONCEPT_SCHEME_7_V1_CONCEPT_1;

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(4, conceptSchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, conceptSchemeVersion.getItems().size());

        // Retrieve concept to check extends metadata
        ConceptMetamac conceptMetamac = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertEquals(conceptExtendsBeforeDeleteUrn, conceptMetamac.getConceptExtends().getNameableArtefact().getUrn());

        // Delete concept
        conceptsService.deleteConcept(getServiceContextAdministrador(), urn);

        // Validation
        try {
            conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
            fail("Concept deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        // Check do not delete concept extends
        conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptExtendsBeforeDeleteUrn);

        // Check hierarchy
        conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(3, conceptSchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertEquals(7, conceptSchemeVersion.getItems().size());
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1);
    }

    @Test
    public void testDeleteConceptWithParentAndChildren() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_4_1;
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(4, conceptSchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, conceptSchemeVersion.getItems().size());

        // Delete concept
        conceptsService.deleteConcept(getServiceContextAdministrador(), urn);

        // Validation
        try {
            conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
            fail("Concept deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(4, conceptSchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListItemsContainsItem(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertEquals(6, conceptSchemeVersion.getItems().size());
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListItemsContainsItem(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
    }

    @Test
    public void testDeleteConceptWithRelatedConcepts() throws Exception {

        String urn1 = CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1; // to delete
        String urn2 = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        String urn3 = CONCEPT_SCHEME_1_V2_CONCEPT_1;

        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn1);
            assertEquals(2, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, urn2);
            assertListConceptsContainsConcept(relatedConceptsConcept1, urn3);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept2 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn2);
            assertEquals(2, relatedConceptsConcept2.size());
            assertListConceptsContainsConcept(relatedConceptsConcept2, urn1);
            assertListConceptsContainsConcept(relatedConceptsConcept2, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept2 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn3);
            assertEquals(3, relatedConceptsConcept2.size());
            assertListConceptsContainsConcept(relatedConceptsConcept2, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConceptsConcept2, urn1);
            assertListConceptsContainsConcept(relatedConceptsConcept2, urn2);
        }

        // Delete relation
        conceptsService.deleteConcept(getServiceContextAdministrador(), urn1);

        // Validate
        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn2);
            assertEquals(1, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept2 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn3);
            assertEquals(2, relatedConceptsConcept2.size());
            assertListConceptsContainsConcept(relatedConceptsConcept2, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConceptsConcept2, urn2);
        }
    }

    @Test
    public void testDeleteConceptWithRelatedConceptAndWithChildrenWithRelatedConcepts() throws Exception {

        String urn1 = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String urn2 = CONCEPT_SCHEME_1_V2_CONCEPT_1;

        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn1);
            assertEquals(1, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, urn2);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept2 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn2);
            assertEquals(3, relatedConceptsConcept2.size());
            assertListConceptsContainsConcept(relatedConceptsConcept2, urn1);
            assertListConceptsContainsConcept(relatedConceptsConcept2, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1); // it will be deleted too, because it is child of concept to delete
            assertListConceptsContainsConcept(relatedConceptsConcept2, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }

        // Delete relation
        conceptsService.deleteConcept(getServiceContextAdministrador(), urn1);

        // Validate
        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn2);
            assertEquals(1, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
    }

    @Test
    public void testDeleteConceptErrorConceptSchemePublished() throws Exception {

        String urn = CONCEPT_SCHEME_12_V1_CONCEPT_1;
        String conceptSchemeUrn = CONCEPT_SCHEME_12_V1;

        // Validation
        try {
            conceptsService.deleteConcept(getServiceContextAdministrador(), urn);
            fail("Concept can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(conceptSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Override
    @Test
    public void testRetrieveConceptsByConceptSchemeUrn() throws Exception {

        // Retrieve
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // LOCALE = 'es'
        {
            String locale = "es";
            List<ConceptMetamacVisualisationResult> concepts = conceptsService.retrieveConceptsByConceptSchemeUrn(getServiceContextAdministrador(), conceptSchemeUrn, locale);

            // Validate
            assertEquals(8, concepts.size());
            {
                // Concept 01 (validate all metadata)
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, concept.getUrn());
                assertEquals("CONCEPT01", concept.getCode());
                assertEquals("Nombre conceptScheme-1-v2-concept-1", concept.getName());
                assertEquals("Descripción conceptScheme-1-v2-concept-1", concept.getDescription());
                assertEquals(Long.valueOf(121), concept.getItemIdDatabase());
                assertEquals(null, concept.getParent());
                assertEquals(null, concept.getParentIdDatabase());
                assertEquals("Acrónimo conceptScheme-1-v2-concept-1", concept.getAcronym());
                assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
                assertEquals("VARIABLE_01", concept.getVariable().getCode());
                assertEquals("nombre variable 1", concept.getVariable().getTitle());
                assertEquals(VARIABLE_1, concept.getVariable().getUrn());
                assertEquals(VARIABLE_1, concept.getVariable().getUrnProvider());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", concept.getCreatedDate());
            }
            {
                // Concept 02
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_2);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, concept.getUrn());
                assertEquals("CONCEPT02", concept.getCode());
                assertEquals("Nombre conceptScheme-1-v2-concept-2", concept.getName());
                assertEquals(null, concept.getDescription());
                assertEquals(null, concept.getAcronym());
                assertEquals(ConceptRoleEnum.DIMENSION, concept.getSdmxRelatedArtefact());
                assertEquals(null, concept.getVariable());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", concept.getCreatedDate());
            }
            {
                // Concept 02 01 (validate parent)
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, concept.getUrn());
                assertEquals("CONCEPT02", concept.getParent().getCode());
                assertEquals("Nombre conceptScheme-1-v2-concept-2-1", concept.getName());
                assertEquals("Descripción conceptScheme-1-v2-concept-2-1", concept.getDescription());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, concept.getParent().getUrn());
                assertEquals(Long.valueOf("122"), concept.getParentIdDatabase());
                assertEquals("Acrónimo conceptScheme-1-v2-concept-2-1", concept.getAcronym());
                assertEquals(ConceptRoleEnum.PRIMARY_MEASURE, concept.getSdmxRelatedArtefact());
                assertEquals("VARIABLE_02", concept.getVariable().getCode());
                assertEquals("nombre variable 2", concept.getVariable().getTitle());
                assertEquals(VARIABLE_2, concept.getVariable().getUrn());
                assertEquals(VARIABLE_2, concept.getVariable().getUrnProvider());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", concept.getCreatedDate());
            }
            {
                // Concept 02 01 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, concept.getUrn());
                assertEquals("CONCEPT0201", concept.getParent().getCode());
                assertEquals("Nombre conceptScheme-1-v2-concept-2-1-1", concept.getName());
                assertEquals(Long.valueOf("1221"), concept.getParentIdDatabase());
                assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
            }
            {
                // Concept 03
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_3);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, concept.getUrn());
                assertEquals("nombre concept-3", concept.getName());
            }
            {
                // Concept 04
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_4);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, concept.getUrn());
                assertEquals("nombre concept-4", concept.getName());
            }
            {
                // Concept 04 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_4_1);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, concept.getUrn());
                assertEquals("nombre concept 4-1", concept.getName());
            }
            {
                // Concept 04 01 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, concept.getUrn());
                assertEquals("CONCEPT0401", concept.getParent().getCode());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, concept.getParent().getUrn());
                assertEquals("Nombre conceptScheme-1-v2-concept-4-1-1", concept.getName());
            }
        }

        // LOCALE = 'en'
        {
            String locale = "en";
            List<ConceptMetamacVisualisationResult> concepts = conceptsService.retrieveConceptsByConceptSchemeUrn(getServiceContextAdministrador(), conceptSchemeUrn, locale);

            // Validate
            assertEquals(8, concepts.size());
            {
                // Concept 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
                assertEquals("Name conceptScheme-1-v2-concept-1", concept.getName());
                assertEquals(null, concept.getDescription());
            }
            {
                // Concept 02
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_2);
                assertEquals(null, concept.getName());
                assertEquals(null, concept.getDescription());
            }
            {
                // Concept 02 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
                assertEquals("Name conceptScheme-1-v2-concept-2-1", concept.getName());
                assertEquals("Description conceptScheme-1-v2-concept-2-1", concept.getDescription());
            }
            {
                // Concept 02 01 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
                assertEquals(null, concept.getName());
            }
            {
                // Concept 03
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_3);
                assertEquals("name concept-3", concept.getName());
            }
            {
                // Concept 04
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_4);
                assertEquals(null, concept.getName());
            }
            {
                // Concept 04 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_4_1);
                assertEquals(null, concept.getName());
            }
            {
                // Concept 04 01 01
                ConceptMetamacVisualisationResult concept = getConceptMetamacVisualisationResult(concepts, CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1);
                assertEquals("Name conceptScheme-1-v2-concept-4-1-1", concept.getName());
            }
        }
    }
    @Override
    @Test
    public void testFindConceptsByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(33, conceptsPagedResult.getTotalRows());
            assertEquals(33, conceptsPagedResult.getValues().size());
            assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(conceptsPagedResult.getValues().size(), i);
        }

        // Find by name (like), code (like) and concept scheme urn
        {
            String name = "Nombre conceptScheme-1-v2-concept-2-";
            String code = "CONCEPT02";
            String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).withProperty(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(conceptSchemeUrn).withProperty(ConceptProperties.nameableArtefact().code()).like(code + "%").withProperty(ConceptProperties.nameableArtefact().name().texts().label())
                    .like(name + "%").orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, conceptsPagedResult.getTotalRows());
            assertEquals(2, conceptsPagedResult.getValues().size());
            assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(conceptsPagedResult.getValues().size(), i);
        }

        // Find by concept scheme urn paginated
        {
            String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).withProperty(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(conceptSchemeUrn).orderBy(ConceptProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, conceptsPagedResult.getTotalRows());
                assertEquals(3, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(conceptsPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, conceptsPagedResult.getTotalRows());
                assertEquals(3, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(conceptsPagedResult.getValues().size(), i);
            }
            // Third page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, conceptsPagedResult.getTotalRows());
                assertEquals(2, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(conceptsPagedResult.getValues().size(), i);
            }
        }
    }

    @Override
    @Test
    public void testFindConceptsCanBeRoleByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(3, conceptsPagedResult.getTotalRows());
            assertEquals(3, conceptsPagedResult.getValues().size());
            assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(conceptsPagedResult.getValues().size(), i);
        }

        // Find by code (like)
        {
            String code = "CONCEPT02";
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).withProperty(ConceptProperties.nameableArtefact().code()).like(code + "%")
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(1, conceptsPagedResult.getTotalRows());
            assertEquals(1, conceptsPagedResult.getValues().size());
            assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(conceptsPagedResult.getValues().size(), i);
        }

        // Find by code (like) paginated
        {
            String code = "CONCEPT0";
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).withProperty(ConceptProperties.nameableArtefact().code()).like(code + "%")
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.pageAccess(2, 1, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(3, conceptsPagedResult.getTotalRows());
                assertEquals(2, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(conceptsPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.pageAccess(2, 2, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(3, conceptsPagedResult.getTotalRows());
                assertEquals(1, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(conceptsPagedResult.getValues().size(), i);
            }
        }
    }

    @Test
    @Override
    public void testFindConceptsCanBeExtendedByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeExtendedByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(1, conceptsPagedResult.getTotalRows());
        assertEquals(1, conceptsPagedResult.getValues().size());
        assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

        int i = 0;
        assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(conceptsPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    public void testFindAllConceptTypes() throws Exception {

        // Find
        List<ConceptType> conceptTypes = conceptsService.findAllConceptTypes(getServiceContextAdministrador());

        // Validate
        assertEquals(2, conceptTypes.size());
        int i = 0;
        {
            ConceptType conceptType = conceptTypes.get(i++);
            assertEquals(CONCEPT_TYPE_DIRECT, conceptType.getIdentifier());
            ConceptsAsserts.assertEqualsInternationalString(conceptType.getDescription(), "en", "Direct", "es", "Directo");
        }
        {
            ConceptType conceptType = conceptTypes.get(i++);
            assertEquals(CONCEPT_TYPE_DERIVED, conceptType.getIdentifier());
            ConceptsAsserts.assertEqualsInternationalString(conceptType.getDescription(), "en", "Derived", "es", "Derivado");
        }
        assertEquals(conceptTypes.size(), i);
    }

    @Override
    @Test
    public void testRetrieveConceptTypeByIdentifier() throws Exception {

        String identifier = CONCEPT_TYPE_DERIVED;

        // Retrieve
        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(getServiceContextAdministrador(), identifier);

        // Validate
        assertEquals(identifier, conceptType.getIdentifier());
        ConceptsAsserts.assertEqualsInternationalString(conceptType.getDescription(), "en", "Derived", "es", "Derivado");
    }

    @Override
    @Test
    public void testAddRelatedConcept() throws Exception {

        String urnConcept1 = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urnConcept2 = CONCEPT_SCHEME_1_V2_CONCEPT_4;

        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnConcept1);
            assertEquals(3, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept2 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnConcept2);
            assertEquals(0, relatedConceptsConcept2.size());
        }

        // Add relation
        conceptsService.addRelatedConcept(getServiceContextAdministrador(), urnConcept1, urnConcept2);

        // Validate
        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnConcept1);
            assertEquals(4, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_3);
            assertListConceptsContainsConcept(relatedConceptsConcept1, urnConcept2);
        }

        // Validate relation is bidirectional
        {
            List<ConceptMetamac> relatedConceptsConcept2 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnConcept2);
            assertEquals(1, relatedConceptsConcept2.size());
            assertListConceptsContainsConcept(relatedConceptsConcept2, urnConcept1);
        }
    }

    @Override
    @Test
    public void testDeleteRelatedConcept() throws Exception {

        String urn1 = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urn2 = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;

        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn1);
            assertEquals(3, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, urn2);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn2);
            assertEquals(1, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, urn1);
        }

        // Delete relation
        conceptsService.deleteRelatedConcept(getServiceContextAdministrador(), urn1, urn2);

        // Validate
        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn1);
            assertEquals(2, relatedConceptsConcept1.size());
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConceptsConcept1, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
        {
            List<ConceptMetamac> relatedConceptsConcept1 = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn2);
            assertEquals(0, relatedConceptsConcept1.size());
        }
    }

    @Override
    @Test
    public void testRetrieveRelatedConcepts() throws Exception {

        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

            // Validate
            assertEquals(3, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);

            // Validate
            assertEquals(1, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        }
        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
    }

    @Override
    @Test
    public void testAddRoleConcept() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urn);
        assertEquals(2, relatedConcepts.size());

        // Add relation
        String urnNewRelation = CONCEPT_SCHEME_13_V1_CONCEPT_2;
        conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, urnNewRelation);

        // Validate
        relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urn);
        assertEquals(3, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_1);
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);
        assertListConceptsContainsConcept(relatedConcepts, urnNewRelation);
    }

    @Test
    public void testAddRoleConceptErrorConceptSchemeWrongType() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1_CONCEPT_1;
        String urnNewRelation = CONCEPT_SCHEME_1_V2_CONCEPT_1;

        try {
            conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, urnNewRelation);
            fail("wrong type");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_2_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_OPERATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_TRANSVERSAL, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_MEASURE, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
        }
    }

    @Test
    public void testAddRoleConceptErrorConceptSchemeTargetWrongType() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urnNewRelation = CONCEPT_SCHEME_2_V1_CONCEPT_1;

        try {
            conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, urnNewRelation);
            fail("wrong type");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_2_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_ROLE, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testAddRoleConceptErrorConceptRoleWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urnNewRelation = CONCEPT_SCHEME_3_V1_CONCEPT_1;

        try {
            conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, urnNewRelation);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_3_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testDeleteRoleConcept() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urnRoleRelationToRemove = CONCEPT_SCHEME_13_V1_CONCEPT_1;

        List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urn);
        assertEquals(2, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, urnRoleRelationToRemove);
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);

        // Delete relation
        conceptsService.deleteRoleConcept(getServiceContextAdministrador(), urn, urnRoleRelationToRemove);

        // Validate
        relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urn);
        assertEquals(1, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);

        // Check role is not delete
        conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnRoleRelationToRemove);
    }

    @Override
    @Test
    public void testRetrieveRoleConcepts() throws Exception {

        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);
        }
        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_13_V1_CONCEPT_1);

            // Validate
            assertEquals(0, relatedConcepts.size());
        }
        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1_CONCEPT_2);

            // Validate
            assertEquals(3, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_2);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);
        }
    }

    @Override
    @Test
    public void testRetrieveConceptSchemeByConceptUrn() throws Exception {
        // Retrieve
        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByConceptUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersion.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testRetrieveConceptSchemeByConceptUrnErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;

        try {
            conceptsService.retrieveConceptSchemeByConceptUrn(getServiceContextAdministrador(), urn);
            fail("not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForConceptByCondition() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = conceptsService.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                    conceptUrn);

            // Validate
            assertEquals(2, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(VARIABLE_1, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            assertEquals(CODELIST_9_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(result.getTotalRows(), i);
        }
        {
            // Concept has Variable 2
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = conceptsService.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                    conceptUrn);

            // Validate
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_2, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            assertEquals(CODELIST_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(result.getTotalRows(), i);
        }
    }

    @Test
    @Override
    public void testPreCreateConceptScheme() throws Exception {
        // TODO testPreCreateConceptScheme

    }

    @Test
    @Override
    public void testPreCreateConcept() throws Exception {
        // TODO testPreCreateConcept

    }

    @SuppressWarnings("rawtypes")
    private ConceptMetamac assertListConceptsContainsConcept(List items, String urn) {
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            ConceptMetamac concept = (ConceptMetamac) iterator.next();
            if (concept.getNameableArtefact().getUrn().equals(urn)) {
                return concept;
            }
        }
        fail("List does not contain item with urn " + urn);
        return null;
    }

    private ConceptMetamacVisualisationResult getConceptMetamacVisualisationResult(List<ConceptMetamacVisualisationResult> actuals, String codeUrn) {
        for (ConceptMetamacVisualisationResult actual : actuals) {
            if (actual.getUrn().equals(codeUrn)) {
                return actual;
            }
        }
        fail("code not found");
        return null;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }

}

package org.siemac.metamac.srm.core.concept.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.base.utils.BaseServiceTestUtils.assertListItemsContainsItem;

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
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
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

import com.arte.statistic.sdmx.srm.core.base.domain.EnumeratedRepresentation;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptProperties;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ConceptsMetamacServiceTest extends SrmBaseTest implements ConceptsMetamacServiceTestBase {

    @Autowired
    private ConceptsMetamacService        conceptsService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

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
        assertEquals(ctx.getUserId(), conceptSchemeVersionRetrieved.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptSchemeVersionRetrieved.getLastUpdatedBy());
        ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersion, conceptSchemeVersionRetrieved);
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

    @Test
    public void testCreateConceptSchemeErrorDuplicatedCode() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion1 = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);
        ConceptSchemeVersionMetamac conceptSchemeVersion2 = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);
        String code = "code-" + MetamacMocks.mockString(10);
        conceptSchemeVersion1.getMaintainableArtefact().setCode(code);
        conceptSchemeVersion2.getMaintainableArtefact().setCode(code);

        conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion1);
        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion2);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateConceptScheme() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_2_V1);
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        ConceptSchemeVersion conceptSchemeVersionUpdated = conceptsService.updateConceptScheme(ctx, conceptSchemeVersion);
        assertNotNull(conceptSchemeVersionUpdated);
        assertEquals("user1", conceptSchemeVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptSchemeVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateConceptSchemeFromGlossaryToOperationTypeErrorRelatedOperationRequired() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1);

        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.OPERATION);

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

            try {
                conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
                conceptSchemeVersion = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
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
    public void testUpdateConceptSchemeErrorExternalReference() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V2);
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);

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
    public void testUpdateConceptSchemeErrorChangeTypeConceptSchemeAlreadyPublished() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2);
        assertEquals(ConceptSchemeTypeEnum.TRANSVERSAL, conceptSchemeVersion.getType());
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.GLOSSARY);

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
        assertEquals("urn:op1", conceptSchemeVersion.getRelatedOperation().getUrn());
        assertEquals("http://op1", conceptSchemeVersion.getRelatedOperation().getUri());
        assertEquals(TypeExternalArtefactsEnum.STATISTICAL_OPERATION, conceptSchemeVersion.getRelatedOperation().getType());
        assertEquals("http://app/operations", conceptSchemeVersion.getRelatedOperation().getManagementAppUrl());
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
            assertEquals(17, conceptSchemeVersionPagedResult.getTotalRows());
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
            assertEquals(13, conceptSchemeVersionPagedResult.getTotalRows());
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
        }
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
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.publishInternallyConceptScheme(ctx, urn);

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
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getLatestPublic());
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
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn);
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
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

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
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());

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
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getLatestPublic());
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
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getIsExternalPublicationFailed());
            assertNull(conceptSchemeVersion.getLifeCycleMetadata().getExternalPublicationFailedDate());
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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

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

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
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
            assertEquals(urnExpectedConcept1, conceptSchemeVersionNewVersion.getItems().get(0).getNameableArtefact().getUrn());
            assertEquals(urnExpectedConcept2, conceptSchemeVersionNewVersion.getItems().get(1).getNameableArtefact().getUrn());
            assertEquals(urnExpectedConcept21, conceptSchemeVersionNewVersion.getItems().get(2).getNameableArtefact().getUrn());
            assertEquals(urnExpectedConcept211, conceptSchemeVersionNewVersion.getItems().get(3).getNameableArtefact().getUrn());
            assertEquals(urnExpectedConcept22, conceptSchemeVersionNewVersion.getItems().get(4).getNameableArtefact().getUrn());

            assertEquals(2, conceptSchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                ConceptMetamac concept = (ConceptMetamac) conceptSchemeVersionNewVersion.getItemsFirstLevel().get(0);
                assertEquals(urnExpectedConcept1, concept.getNameableArtefact().getUrn());

                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getNameableArtefact().getName(), "es", "Nombre conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "es", "Acronym conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDescriptionSource(), "es", "DescriptionSource conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getContext(), "es", "Context conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDocMethod(), "es", "DocMethod conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDerivation(), "es", "Derivation conceptScheme-3-v1-concept-1", null, null);
                ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getLegalActs(), "es", "LegalActs conceptScheme-3-v1-concept-1", null, null);
                assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
                assertEquals(CONCEPT_TYPE_DERIVED, concept.getType().getIdentifier());
                assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());

                assertEquals(0, concept.getChildren().size());
            }
            {
                ConceptMetamac concept = (ConceptMetamac) conceptSchemeVersionNewVersion.getItemsFirstLevel().get(1);
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
                assertNull(concept.getType());
                assertNull(concept.getConceptExtends());

                assertEquals(2, concept.getChildren().size());
                {
                    ConceptMetamac conceptChild = (ConceptMetamac) concept.getChildren().get(0);
                    assertEquals(urnExpectedConcept21, conceptChild.getNameableArtefact().getUrn());

                    assertEquals(1, conceptChild.getChildren().size());
                    {
                        ConceptMetamac conceptChildChild = (ConceptMetamac) conceptChild.getChildren().get(0);
                        assertEquals(urnExpectedConcept211, conceptChildChild.getNameableArtefact().getUrn());
                        assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptChildChild.getConceptExtends().getNameableArtefact().getUrn());

                        assertEquals(0, conceptChildChild.getChildren().size());
                    }
                }
                {
                    ConceptMetamac conceptChild = (ConceptMetamac) concept.getChildren().get(1);
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

        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
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
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept1);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2_1);
            }

            // Concept 2
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2_1_1);
            }
            // Concept 2_1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2_1);
                assertEquals(2, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept1);
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2_1_1);
            }
            // Concept 2_1_1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2_1_1);
                assertEquals(3, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2);
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2_1);
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2_2);
            }
            // Concept 2_2
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urnExpectedConcept2_2);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, urnExpectedConcept2_1_1);
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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

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

    @Test
    public void testCreateConcept() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType);
        concept.setParent(null);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_13_V1_CONCEPT_1);
        concept.setConceptExtends(conceptExtends);

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
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptSchemeVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptSchemeVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptSchemeVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptSchemeVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
        assertEquals(conceptRetrieved.getNameableArtefact().getUrn(), conceptSchemeVersion.getItemsFirstLevel().get(4).getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptSubconcept() throws Exception {

        ConceptType conceptType = null;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType);
        ConceptMetamac conceptParent = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.setParent(conceptParent);
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

        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptSchemeVersion.getItemsFirstLevel().get(0).getNameableArtefact().getUrn());
        assertEquals(conceptRetrieved.getNameableArtefact().getUrn(), conceptSchemeVersion.getItemsFirstLevel().get(0).getChildren().get(0).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptSchemeVersion.getItemsFirstLevel().get(1).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptSchemeVersion.getItemsFirstLevel().get(2).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptSchemeVersion.getItemsFirstLevel().get(3).getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptErrorConceptExtendsWrongProcStatus() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType);
        concept.setParent(null);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_3_V1_CONCEPT_1);
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
            assertEquals(CONCEPT_SCHEME_3_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testCreateConceptErrorMetadataIncorrect() throws Exception {

        ConceptType conceptType = null;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType);
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
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType);
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
    public void testUpdateConcept() throws Exception {

        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_13_V1_CONCEPT_2));
        assertTrue(concept.getCoreRepresentation() instanceof EnumeratedRepresentation);
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
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_1));

        // Create
        try {
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_3_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRetrieveConceptByUrn() throws Exception {
        // Retrieve
        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, concept.getNameableArtefact().getUrn());
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getPluralName(), "es", "PluralName conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getAcronym(), "es", "Acronym conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDescriptionSource(), "es", "DescriptionSource conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getContext(), "es", "Context conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDocMethod(), "es", "DocMethod conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getDerivation(), "es", "Derivation conceptScheme-1-v2-concept-1", null, null);
        ConceptsMetamacAsserts.assertEqualsInternationalString(concept.getLegalActs(), "es", "LegalActs conceptScheme-1-v2-concept-1", null, null);
        assertEquals(ConceptRoleEnum.ATTRIBUTE, concept.getSdmxRelatedArtefact());
        assertEquals(CONCEPT_TYPE_DIRECT, concept.getType().getIdentifier());
        assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());
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

    @Test
    public void testDeleteConcept() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptExtendsBeforeDeleteUrn = CONCEPT_SCHEME_12_V1_CONCEPT_1;

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
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(conceptSchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveConceptsByConceptSchemeUrn() throws Exception {

        // Retrieve
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        List<ConceptMetamac> concepts = conceptsService.retrieveConceptsByConceptSchemeUrn(getServiceContextAdministrador(), conceptSchemeUrn);

        // Validate
        assertEquals(4, concepts.size());
        {
            // Concept 01
            ConceptMetamac concept = concepts.get(0);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, concept.getNameableArtefact().getUrn());
            assertEquals(0, concept.getChildren().size());
        }
        {
            // Concept 02
            ConceptMetamac concept = concepts.get(1);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, concept.getNameableArtefact().getUrn());
            assertEquals(1, concept.getChildren().size());
            {
                // Concept 02 01
                ConceptMetamac conceptChild = (ConceptMetamac) concept.getChildren().get(0);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptChild.getNameableArtefact().getUrn());
                assertEquals(1, conceptChild.getChildren().size());
                {
                    // Concept 02 01 01
                    ConceptMetamac conceptChildChild = (ConceptMetamac) conceptChild.getChildren().get(0);
                    assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptChildChild.getNameableArtefact().getUrn());
                    assertEquals(0, conceptChildChild.getChildren().size());
                }
            }
        }
        {
            // Concept 03
            ConceptMetamac concept = concepts.get(2);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, concept.getNameableArtefact().getUrn());
            assertEquals(0, concept.getChildren().size());
        }
        {
            // Concept 04
            ConceptMetamac concept = concepts.get(3);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, concept.getNameableArtefact().getUrn());
            assertEquals(1, concept.getChildren().size());
            {
                // Concept 04 01
                ConceptMetamac conceptChild = (ConceptMetamac) concept.getChildren().get(0);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptChild.getNameableArtefact().getUrn());
                assertEquals(1, conceptChild.getChildren().size());
                {
                    // Concept 04 01 01
                    ConceptMetamac conceptChildChild = (ConceptMetamac) conceptChild.getChildren().get(0);
                    assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptChildChild.getNameableArtefact().getUrn());
                    assertEquals(0, conceptChildChild.getChildren().size());
                }
            }
        }
    }

    @Test
    public void testFindConceptsByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(28, conceptsPagedResult.getTotalRows());
            assertEquals(28, conceptsPagedResult.getValues().size());
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
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
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

    @Test
    public void testFindConceptsAsRoleByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsAsRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

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
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsAsRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

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
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsAsRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

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
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsAsRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

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

    @Test
    public void testRetrieveConceptTypeByIdentifier() throws Exception {

        String identifier = CONCEPT_TYPE_DERIVED;

        // Retrieve
        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(getServiceContextAdministrador(), identifier);

        // Validate
        assertEquals(identifier, conceptType.getIdentifier());
        ConceptsAsserts.assertEqualsInternationalString(conceptType.getDescription(), "en", "Derived", "es", "Derivado");
    }

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

    private void assertListConceptsContainsConcept(List<ConceptMetamac> items, String urn) {
        for (Item item : items) {
            if (item.getNameableArtefact().getUrn().equals(urn)) {
                return;
            }
        }
        fail("List does not contain item with urn " + urn);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }
}

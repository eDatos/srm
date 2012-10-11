package org.siemac.metamac.srm.core.concept.serviceapi;

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
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class ConceptsMetamacServiceTest extends SrmBaseTest implements ConceptsMetamacServiceTestBase {

    @Autowired
    private ConceptsMetamacService conceptsService;

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme();
        ServiceContext ctx = getServiceContextAdministrador();
        
        // Create
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
        String urn = conceptSchemeVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", conceptSchemeVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), conceptSchemeVersionCreated.getCreatedBy());
        
        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        ConceptSchemeVersionMetamac conceptSchemeVersionRetrieved = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionRetrieved.getLifecycleMetadata().getProcStatus());
        assertFalse(conceptSchemeVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getProductionValidationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getProductionValidationUser());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getDiffusionValidationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getDiffusionValidationUser());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getInternalPublicationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getInternalPublicationUser());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getExternalPublicationDate());
        assertNull(conceptSchemeVersionRetrieved.getLifecycleMetadata().getExternalPublicationUser());
        assertEquals(ctx.getUserId(), conceptSchemeVersionRetrieved.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptSchemeVersionRetrieved.getLastUpdatedBy());
        ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersion, conceptSchemeVersionRetrieved);
    }

    @Test
    public void testCreateConceptSchemeErrorMetadatasRequired() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme();
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
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme();
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
        ConceptSchemeVersionMetamac conceptSchemeVersion1 = ConceptsMetamacDoMocks.mockConceptScheme();
        ConceptSchemeVersionMetamac conceptSchemeVersion2 = ConceptsMetamacDoMocks.mockConceptScheme();
        String code = "code-" + MetamacMocks.mockString(10);
        conceptSchemeVersion1.getMaintainableArtefact().setCode(code);
        conceptSchemeVersion2.getMaintainableArtefact().setCode(code);

        conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion1);
        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion2);
            fail("duplicated code");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
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
                assertEquals(ServiceExceptionType.CONCEPT_SCHEME_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
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
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
        assertEquals("user1", conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
        assertEquals("user3", conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
        assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
        assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
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
            assertEquals(16, conceptSchemeVersionPagedResult.getTotalRows());
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
            assertEquals(conceptSchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find internally published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.lifecycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
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
            assertEquals(12, conceptSchemeVersionPagedResult.getTotalRows());
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
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationInProcStatusRejected() throws Exception {

        String urn = CONCEPT_SCHEME_4_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
        }

        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorMetadataRequired() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        // Update to clear metadata
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
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        }

        // Sends to diffusion validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        }

        try {
            conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
        }

        // Reject validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.rejectConceptSchemeProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
        }
        // Validate restrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectConceptSchemeProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
        }

        try {
            conceptsService.rejectConceptSchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
        }

        // Reject validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
        }

        try {
            conceptsService.rejectConceptSchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertFalse(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
        }

        // Publish internally
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.publishInternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
            assertTrue(conceptSchemeVersion.getMaintainableArtefact().getFinalLogic());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishInternallyConceptSchemeErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
        }

        try {
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishInternallyConceptSchemeErrorConceptExtendsInConceptSchemeNoPublished() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        String urnConceptConceptSchemeToPublish = CONCEPT_SCHEME_6_V1_CONCEPT_1;
        String urnConceptExtends = CONCEPT_SCHEME_1_V2_CONCEPT_1;

        // Update concept to add extends concept of concept scheme to publish
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnConceptExtends);
        ConceptMetamac conceptConceptSchemeToPublish = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnConceptConceptSchemeToPublish);
        conceptConceptSchemeToPublish.setConceptExtends(concept);
        conceptConceptSchemeToPublish.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        conceptsService.updateConcept(getServiceContextAdministrador(), conceptConceptSchemeToPublish);

        try {
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WITH_RELATED_CONCEPTS_NOT_FINAL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(urnConceptExtends, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testPublishExternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getIsExternalPublicationFailed());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationFailedDate());

            ConceptSchemeVersionMetamac conceptSchemeVersionExternallyPublished = conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersionExternallyPublished.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertNull(conceptSchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.publishExternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getIsExternalPublicationFailed());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationFailedDate());
        }
        // Validate retrieving
        {
            conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getProductionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getDiffusionValidationUser());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationDate());
            assertNotNull(conceptSchemeVersion.getLifecycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getIsExternalPublicationFailed());
            assertNull(conceptSchemeVersion.getLifecycleMetadata().getExternalPublicationFailedDate());
        }
        // Validate previous published externally versions
        {
            ConceptSchemeVersionMetamac conceptSchemeVersionExternallyPublished = conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersionExternallyPublished.getLifecycleMetadata().getProcStatus());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2;

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersion.getLifecycleMetadata().getProcStatus());
        }

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorConceptExtendsInConceptSchemeNoPublished() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WITH_RELATED_CONCEPTS_VALIDITY_NOT_STARTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, e.getExceptionItems().get(0).getMessageParameters()[1]);
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteConceptSchemeWithVersionPublishedAndVersionDraft() throws Exception {

        String urnV1 = CONCEPT_SCHEME_1_V1;
        String urnV2 = CONCEPT_SCHEME_1_V2;

        ConceptSchemeVersionMetamac conceptSchemeVersionV1 = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersionV1.getLifecycleMetadata().getProcStatus());
        ConceptSchemeVersionMetamac conceptSchemeVersionV2 = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV2);
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionV2.getLifecycleMetadata().getProcStatus());

        conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV2);
            fail("ConceptScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        conceptSchemeVersionV1 = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(conceptSchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(conceptSchemeVersionV1.getMaintainableArtefact().getReplacedBy());
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteConceptSchemeErrorOtherConceptExtends() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;

        // Validation
        try {
            conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WITH_RELATED_CONCEPTS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_3_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testVersioningConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(02.000)";
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(02.000).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(02.000).CONCEPT02";
        String urnExpectedConcept21 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(02.000).CONCEPT0201";
        String urnExpectedConcept211 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(02.000).CONCEPT020101";
        String urnExpectedConcept22 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME03(02.000).CONCEPT0202";

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplaceTo());
            assertEquals(null, conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplacedBy());
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
                assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());

                // related concepts
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), concept.getNameableArtefact().getUrn());
                assertEquals(1, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept211);

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

                // related concepts
                List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), concept.getNameableArtefact().getUrn());
                assertEquals(2, relatedConcepts.size());
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept21);
                assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept22);

                assertEquals(2, concept.getChildren().size());
                {
                    ConceptMetamac conceptChild = (ConceptMetamac) concept.getChildren().get(0);
                    assertEquals(urnExpectedConcept21, conceptChild.getNameableArtefact().getUrn());

                    // related concepts
                    relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), conceptChild.getNameableArtefact().getUrn());
                    assertEquals(1, relatedConcepts.size());
                    assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2);

                    assertEquals(1, conceptChild.getChildren().size());
                    {
                        ConceptMetamac conceptChildChild = (ConceptMetamac) conceptChild.getChildren().get(0);
                        assertEquals(urnExpectedConcept211, conceptChildChild.getNameableArtefact().getUrn());
                        assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, conceptChildChild.getConceptExtends().getNameableArtefact().getUrn());

                        // related concepts
                        relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), conceptChildChild.getNameableArtefact().getUrn());
                        assertEquals(1, relatedConcepts.size());
                        assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept1);

                        assertEquals(0, conceptChildChild.getChildren().size());
                    }
                }
                {
                    ConceptMetamac conceptChild = (ConceptMetamac) concept.getChildren().get(1);
                    assertEquals(urnExpectedConcept22, conceptChild.getNameableArtefact().getUrn());

                    // related concepts
                    relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), conceptChild.getNameableArtefact().getUrn());
                    assertEquals(1, relatedConcepts.size());
                    assertListConceptsContainsConcept(relatedConcepts, urnExpectedConcept2);

                    assertEquals(0, conceptChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, conceptSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, conceptSchemeVersionToCopy.getMaintainableArtefact().getReplaceTo());
            assertEquals(versionExpected, conceptSchemeVersionToCopy.getMaintainableArtefact().getReplacedBy());
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
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME07(03.000)";

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeVersionToCopy.getLifecycleMetadata().getProcStatus());
        assertFalse(conceptSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        ConceptSchemeVersionMetamac conceptSchemeVersionLast = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeVersionLast.getLifecycleMetadata().getProcStatus());
        assertTrue(conceptSchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

        ConceptSchemeVersionMetamac conceptSchemeVersionNewVersion = conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            conceptSchemeVersionNewVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(versionExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeVersionNewVersion.getLifecycleMetadata().getProcStatus());
            assertEquals("02.000", conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplaceTo());
            assertEquals(null, conceptSchemeVersionNewVersion.getMaintainableArtefact().getReplacedBy());
            assertTrue(conceptSchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            ConceptsMetamacAsserts.assertEqualsConceptScheme(conceptSchemeVersionToCopy, conceptSchemeVersionNewVersion);

            // Version copied
            conceptSchemeVersionToCopy = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, conceptSchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, conceptSchemeVersionToCopy.getMaintainableArtefact().getReplaceTo());
            assertEquals("02.000", conceptSchemeVersionToCopy.getMaintainableArtefact().getReplacedBy());
            assertFalse(conceptSchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            conceptSchemeVersionLast = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", conceptSchemeVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, conceptSchemeVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", conceptSchemeVersionLast.getMaintainableArtefact().getReplaceTo());
            assertEquals(versionExpected, conceptSchemeVersionLast.getMaintainableArtefact().getReplacedBy());
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
    public void testVersioningConceptSchemeCheckRelatedConceptsRole() throws Exception {

        // Check related roles until versioning
        {
            // Concept 1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1_CONCEPT_1);
                assertEquals(2, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_3_V1_CONCEPT_1);
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_3_V1_CONCEPT_2_1);
            }

            // Concept 2
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1_CONCEPT_2);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_3_V1_CONCEPT_2_1);
            }

        }

        // We prefer do isolated tests but.. publish internally to versioning and check copy of related concepts type 'role'
        String urn = CONCEPT_SCHEME_2_V1;
        conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
        conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
        conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn);

        // Versioning
        conceptsService.versioningConceptScheme(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1, VersionTypeEnum.MAJOR);
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME02(01.000).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ISTAC:CONCEPTSCHEME02(01.000).CONCEPT02";

        // Only check related roles
        {
            // Concept 1
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), urnExpectedConcept1);
                assertEquals(2, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_3_V1_CONCEPT_1);
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_3_V1_CONCEPT_2_1);
            }

            // Concept 2
            {
                List<ConceptMetamac> relatedConceptsRole = conceptsService.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), urnExpectedConcept2);
                assertEquals(1, relatedConceptsRole.size());
                assertListConceptsContainsConcept(relatedConceptsRole, CONCEPT_SCHEME_3_V1_CONCEPT_2_1);
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_VERSIONING_NOT_SUPPORTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCancelConceptSchemeValidity() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.cancelConceptSchemeValidity(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1);

        assertNotNull(conceptSchemeVersion);
        assertNotNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testCancelConceptSchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CONCEPT_SCHEME_1_V1, CONCEPT_SCHEME_4_V1, CONCEPT_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                conceptsService.cancelConceptSchemeValidity(getServiceContextAdministrador(), urn);
                fail("wrong procStatus");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
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
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_3_V1_CONCEPT_1);
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
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_10_V3_CONCEPT_1));
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
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, concept.getConceptExtends().getNameableArtefact().getUrn());
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
        String conceptExtendsBeforeDeleteUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2_1;

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
            assertEquals(ServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        // Check do not delete concept extends
        conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptExtendsBeforeDeleteUrn);

        // Check hierarchy
        conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(3, conceptSchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertEquals(7, conceptSchemeVersion.getItems().size());
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1);
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
            assertEquals(ServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(4, conceptSchemeVersion.getItemsFirstLevel().size());
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListItemsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertEquals(6, conceptSchemeVersion.getItems().size());
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListItemsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
    }

    @Test
    public void testDeleteConceptErrorConceptRelatedRole() throws Exception {
        // In SDMX module
    }

    @Test
    public void testDeleteConceptErrorChildrenWithConceptRelated() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_2;

        // Validation
        try {
            conceptsService.deleteConcept(getServiceContextAdministrador(), urn);
            fail("Concept can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_WITH_RELATED_CONCEPTS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertTrue(CONCEPT_SCHEME_1_V2_CONCEPT_1.equals(e.getExceptionItems().get(0).getMessageParameters()[1])
                    || CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1.equals(e.getExceptionItems().get(0).getMessageParameters()[1]));
        }
    }

    @Test
    public void testDeleteConceptErrorConceptRelatedAsExtends() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1_CONCEPT_1;

        // Validation
        try {
            conceptsService.deleteConcept(getServiceContextAdministrador(), urn);
            fail("Concept can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_WITH_RELATED_CONCEPTS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, e.getExceptionItems().get(0).getMessageParameters()[1]);
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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(25, conceptsPagedResult.getTotalRows());
            assertEquals(25, conceptsPagedResult.getValues().size());
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
    public void testAddConceptRelation() throws Exception {

        {
            String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn);
            assertEquals(2, relatedConcepts.size());

            String urnNewRelation = CONCEPT_SCHEME_1_V2_CONCEPT_3;
            // Add relation
            conceptsService.addConceptRelation(getServiceContextAdministrador(), urn, urnNewRelation);

            // Validate
            relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn);
            assertEquals(3, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConcepts, urnNewRelation);
        }
        {
            String urn = CONCEPT_SCHEME_1_V2_CONCEPT_4;
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn);
            assertEquals(0, relatedConcepts.size());

            String urnNewRelation = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
            // Add relation
            conceptsService.addConceptRelation(getServiceContextAdministrador(), urn, urnNewRelation);

            // Validate
            relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn);
            assertEquals(1, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, urnNewRelation);
        }
    }

    @Test
    public void testDeleteConceptRelation() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn);
        assertEquals(2, relatedConcepts.size());

        // Delete relation
        conceptsService.deleteConceptRelation(getServiceContextAdministrador(), urn, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);

        // Validate
        relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), urn);
        assertEquals(1, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
    }

    @Test
    public void testRetrieveRelatedConcepts() throws Exception {

        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        }
        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        }
        {
            // Retrieve
            List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        }
    }

    @Test
    public void testAddConceptRelationRoles() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        List<ConceptMetamac> relatedConcepts = conceptsService.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), urn);
        assertEquals(2, relatedConcepts.size());

        String urnNewRelation = CONCEPT_SCHEME_3_V1_CONCEPT_1;
        // Add relation
        conceptsService.addConceptRelationRoles(getServiceContextAdministrador(), urn, urnNewRelation);

        // Validate
        relatedConcepts = conceptsService.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), urn);
        assertEquals(3, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_3_V1_CONCEPT_2);
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1);
        assertListConceptsContainsConcept(relatedConcepts, urnNewRelation);
    }

    @Test
    public void testAddConceptRelationRolesErrorConceptSchemeWrongType() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1_CONCEPT_1;
        String urnNewRelation = CONCEPT_SCHEME_1_V2_CONCEPT_1;

        try {
            conceptsService.addConceptRelationRoles(getServiceContextAdministrador(), urn, urnNewRelation);
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
    public void testAddConceptRelationRolesErrorConceptSchemeTargetWrongType() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urnNewRelation = CONCEPT_SCHEME_2_V1_CONCEPT_1;

        try {
            conceptsService.addConceptRelationRoles(getServiceContextAdministrador(), urn, urnNewRelation);
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
    public void testDeleteConceptRelationRoles() throws Exception {
        // In SDMX Module
    }

    @Test
    public void testRetrieveRelatedConceptsRoles() throws Exception {
        // In SDMX Module
    }

    @Override
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
            assertEquals(ServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    private void assertListItemsContainsConcept(List<Item> items, String urn) {
        for (Item item : items) {
            if (item.getNameableArtefact().getUrn().equals(urn)) {
                return;
            }
        }
        fail("List does not contain item with urn " + urn);
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

package org.siemac.metamac.srm.core.concept.serviceapi;

import static com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.assertEqualsInternationalString;
import static com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.assertEqualsRepresentation;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
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
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.ConceptMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.domain.QuantityRepository;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
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

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodeProperties;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
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
    private ConceptMetamacRepository      conceptMetamacRepository;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeRepository;

    @Autowired
    private ItemRepository                itemRepository;

    @Autowired
    private QuantityRepository            quantityRepository;

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
    public void testCreateConceptSchemeTypeOperations() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);
        conceptSchemeVersion.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeVersion.setRelatedOperation(ConceptsMetamacDoMocks.mockExternalItemOperation(UUID.randomUUID().toString()));

        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(ctx, conceptSchemeVersion);

        // Asserts
        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeVersionCreated.getType());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getCode(), conceptSchemeVersionCreated.getRelatedOperation().getCode());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getTitle(), conceptSchemeVersionCreated.getRelatedOperation().getTitle());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getType(), conceptSchemeVersionCreated.getRelatedOperation().getType());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getUrn(), conceptSchemeVersionCreated.getRelatedOperation().getUrn());
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
    public void testUpdateConceptSchemeTypeOperationChangingRelatedOperation() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_8_V1);
        conceptSchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        conceptSchemeVersion.setIsTypeUpdated(Boolean.FALSE);
        conceptSchemeVersion.setRelatedOperation(ConceptsMetamacDoMocks.mockExternalItemOperation(UUID.randomUUID().toString()));

        ConceptSchemeVersionMetamac conceptSchemeVersionUpdated = conceptsService.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);

        // Asserts
        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeVersionUpdated.getType());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getCode(), conceptSchemeVersionUpdated.getRelatedOperation().getCode());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getTitle(), conceptSchemeVersionUpdated.getRelatedOperation().getTitle());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getType(), conceptSchemeVersionUpdated.getRelatedOperation().getType());
        assertEquals(conceptSchemeVersion.getRelatedOperation().getUrn(), conceptSchemeVersionUpdated.getRelatedOperation().getUrn());
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
        assertEquals("/latest/operations/op1", conceptSchemeVersion.getRelatedOperation().getUri());
        assertEquals(TypeExternalArtefactsEnum.STATISTICAL_OPERATION, conceptSchemeVersion.getRelatedOperation().getType());
        assertEquals("/#operations;id=op1", conceptSchemeVersion.getRelatedOperation().getManagementAppUrl());
    }

    @Test
    @Override
    public void testFindConceptSchemesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.id()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(21, conceptSchemeVersionPagedResult.getTotalRows());
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
            assertEquals(CONCEPT_SCHEME_15_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_16_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_17_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
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
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE).orderBy(ConceptSchemeVersionMetamacProperties.id()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(17, conceptSchemeVersionPagedResult.getTotalRows());
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
            assertEquals(CONCEPT_SCHEME_15_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_16_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_17_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testFindConceptSchemesWithConceptsCanBeRoleByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesWithConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(2, conceptSchemeVersionPagedResult.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(conceptSchemeVersionPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptSchemesWithConceptsCanBeExtendedByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesWithConceptsCanBeExtendedByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(2, conceptSchemeVersionPagedResult.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(conceptSchemeVersionPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptSchemesCanBeEnumeratedRepresentationForConceptsByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService
                .findConceptSchemesCanBeEnumeratedRepresentationForConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter, CONCEPT_SCHEME_15_V1_CONCEPT_1);

        // Validate
        assertEquals(2, conceptSchemeVersionPagedResult.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_15_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1, conceptSchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(conceptSchemeVersionPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptSchemesWithConceptsCanBeQuantityNumeratorByCondition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1;
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.id()).ascending().distinctRoot()
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemesPagedResult = conceptsService.findConceptSchemesWithConceptsCanBeQuantityNumeratorByCondition(getServiceContextAdministrador(),
                conceptSchemeUrn, conditions, pagingParameter);

        // Validate
        assertEquals(3, conceptSchemesPagedResult.getTotalRows());
        assertEquals(3, conceptSchemesPagedResult.getValues().size());

        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(conceptSchemesPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptSchemesWithConceptsCanBeQuantityDenominatorByCondition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1;
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.id()).ascending().distinctRoot()
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemesPagedResult = conceptsService.findConceptSchemesWithConceptsCanBeQuantityDenominatorByCondition(getServiceContextAdministrador(),
                conceptSchemeUrn, conditions, pagingParameter);

        // Validate
        assertEquals(3, conceptSchemesPagedResult.getTotalRows());
        assertEquals(3, conceptSchemesPagedResult.getValues().size());

        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(conceptSchemesPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptSchemesWithConceptsCanBeQuantityBaseQuantityByCondition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1;
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.id()).ascending().distinctRoot()
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemesPagedResult = conceptsService.findConceptSchemesWithConceptsCanBeQuantityBaseQuantityByCondition(getServiceContextAdministrador(),
                conceptSchemeUrn, conditions, pagingParameter);

        // Validate
        assertEquals(3, conceptSchemesPagedResult.getTotalRows());
        assertEquals(3, conceptSchemesPagedResult.getValues().size());

        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1, conceptSchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(conceptSchemesPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    public void testFindCodelistsWithCodesCanBeQuantityUnitByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodelistVersionMetamac> codelistsPagedResult = conceptsService.findCodelistsWithCodesCanBeQuantityUnitByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(2, codelistsPagedResult.getTotalRows());
        assertEquals(2, codelistsPagedResult.getValues().size());

        int i = 0;
        assertEquals(CODELIST_7_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CODELIST_8_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        // assertEquals(CODELIST_9_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // restricted
        // codelist10 draft
        assertEquals(codelistsPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    public void testFindCodelistsWithCodesCanBeQuantityBaseLocationByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodelistVersionMetamac> codelistsPagedResult = conceptsService.findCodelistsWithCodesCanBeQuantityBaseLocationByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(1, codelistsPagedResult.getTotalRows());
        assertEquals(1, codelistsPagedResult.getValues().size());

        int i = 0;
        assertEquals(CODELIST_7_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        // assertEquals(CODELIST_8_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // not geographical
        // assertEquals(CODELIST_9_V1, codelistsPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn()); // restricted
        // codelist10 draft
        assertEquals(codelistsPagedResult.getValues().size(), i);
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

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL}, exceptionItem.getExceptionItems().get(0));
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationErrorTranslations() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        // Update to change metadata to send to production

        {
            ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            conceptSchemeVersion.getMaintainableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemSchemeRepository.save(conceptSchemeVersion);
        }
        {
            ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1_CONCEPT_1);
            concept.getNameableArtefact().setDescription(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(concept);
        }
        {
            ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1_CONCEPT_2);
            concept.setAcronym(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(concept);
        }

        entityManager.flush();

        // Send to production validation
        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("ConceptScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            int i = 0;
            // ConceptScheme
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME},
                        exceptionItem.getExceptionItems().get(0));
            }
            // Concepts
            {
                // Concept01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CONCEPT_SCHEME_2_V1_CONCEPT_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // Concept02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CONCEPT_SCHEME_2_V1_CONCEPT_2);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_ACRONYM},
                        exceptionItem.getExceptionItems().get(0));
            }
            assertEquals(e.getExceptionItems().size(), i);
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

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE}, exceptionItem.getExceptionItems().get(0));
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

        // change concepts to force incorrect metadata
        ConceptMetamac concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept1.setSdmxRelatedArtefact(null);
        itemRepository.save(concept1);

        ConceptMetamac concept211 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        concept211.setSdmxRelatedArtefact(null);
        concept211.setCoreRepresentation(CodesMetamacDoMocks.mockRepresentationEnumerated(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1))); // restricted
        itemRepository.save(concept211);

        try {
            conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            {
                MetamacExceptionItem exceptionItemConcept = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA,
                        concept1.getNameableArtefact().getUrn());
                // children
                assertEquals(1, exceptionItemConcept.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT},
                        exceptionItemConcept.getExceptionItems().get(0));
            }
            {
                MetamacExceptionItem exceptionItemConcept = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA,
                        concept211.getNameableArtefact().getUrn());
                // children
                assertEquals(2, exceptionItemConcept.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.CONCEPT_SDMX_RELATED_ARTEFACT},
                        exceptionItemConcept.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, 1, new String[]{ServiceExceptionParameters.CONCEPT_REPRESENTATION},
                        exceptionItemConcept.getExceptionItems().get(1));
            }
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

        try {
            // Note: publishInternallyConceptScheme calls to 'checkConceptSchemeVersionTranslates'
            conceptsService.publishInternallyConceptScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("ConceptScheme wrong translations");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());
            int i = 0;
            // ConceptScheme
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            // Concepts
            {
                // Concept01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CONCEPT_SCHEME_14_V1_CONCEPT_1);
                // children
                assertEquals(4, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT},
                        exceptionItem.getExceptionItems().get(1));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_ACRONYM},
                        exceptionItem.getExceptionItems().get(2));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_DERIVATION},
                        exceptionItem.getExceptionItems().get(3));
            }
            {
                // Concept0101
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CONCEPT_SCHEME_14_V1_CONCEPT_1_1);
                // children
                assertEquals(3, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_PLURAL_NAME},
                        exceptionItem.getExceptionItems().get(1));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_CONTEXT},
                        exceptionItem.getExceptionItems().get(2));
            }
            {
                // Concept02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CONCEPT_SCHEME_14_V1_CONCEPT_2);
                // children
                assertEquals(3, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_DOC_METHOD},
                        exceptionItem.getExceptionItems().get(2));
            }
            {
                // Concept03
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CONCEPT_SCHEME_14_V1_CONCEPT_3);
                // children
                assertEquals(4, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_PLURAL_NAME},
                        exceptionItem.getExceptionItems().get(1));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE},
                        exceptionItem.getExceptionItems().get(2));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.CONCEPT_LEGAL_ACTS},
                        exceptionItem.getExceptionItems().get(3));
            }

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

    @Test
    public void testPublishExternallyConceptSchemeErrorRelatedResourcesExtendsNotExternallyPublished() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        // Change some metadata to force errors
        {
            ConceptMetamac concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_1);
            concept1.setConceptExtends(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_1_V1_CONCEPT_1));
            itemRepository.save(concept1);
        }
        {
            ConceptMetamac concept2 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_2);
            concept2.setConceptExtends(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_3_V1_CONCEPT_1));
            itemRepository.save(concept2);
        }
        {
            ConceptMetamac concept3 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_3);
            concept3.setConceptExtends(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_12_V1_CONCEPT_1)); // ok
            itemRepository.save(concept3);
        }

        entityManager.flush();

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_1_V1_CONCEPT_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_3_V1_CONCEPT_1);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorRelatedResourcesRoleNotExternallyPublished() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        // Change some metadata to force errors
        {
            ConceptMetamac concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_1);
            concept1.addRoleConcept(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_3_V1_CONCEPT_2));
            concept1.addRoleConcept(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_13_V1_CONCEPT_1)); // ok
            itemRepository.save(concept1);
        }
        {
            ConceptMetamac concept2 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_2);
            concept2.addRoleConcept(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_13_V1_CONCEPT_1)); // ok
            itemRepository.save(concept2);
        }
        {
            ConceptMetamac concept3 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_3);
            concept3.addRoleConcept(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_3_V1_CONCEPT_2_1));
            itemRepository.save(concept3);
        }

        entityManager.flush();

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_3_V1_CONCEPT_2);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_3_V1_CONCEPT_2_1);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorRelatedResourcesQuantityNotExternallyPublished() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        // Change some metadata to force errors
        {
            ConceptMetamac concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_1);
            concept1.setQuantity(new Quantity());
            concept1.getQuantity().setQuantityType(QuantityTypeEnum.FRACTION);
            concept1.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_7_V2_CODE_1));
            concept1.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
            concept1.getQuantity().setSignificantDigits(Integer.valueOf(2));
            concept1.getQuantity().setDecimalPlaces(Integer.valueOf(3));
            concept1.getQuantity().setUnitMultiplier(Integer.valueOf(100));
            concept1.getQuantity().setMinimum(Integer.valueOf(1000));
            concept1.getQuantity().setMaximum(Integer.valueOf(2000));
            concept1.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2));
            concept1.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V2_CONCEPT_2));
            itemRepository.save(concept1);
        }
        {
            ConceptMetamac concept2 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_2);
            concept2.setQuantity(new Quantity());
            concept2.getQuantity().setQuantityType(QuantityTypeEnum.FRACTION);
            concept2.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_7_V2_CODE_1));
            concept2.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
            concept2.getQuantity().setSignificantDigits(Integer.valueOf(2));
            concept2.getQuantity().setDecimalPlaces(Integer.valueOf(3));
            concept2.getQuantity().setUnitMultiplier(Integer.valueOf(100));
            concept2.getQuantity().setMinimum(Integer.valueOf(1000));
            concept2.getQuantity().setMaximum(Integer.valueOf(2000));
            concept2.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_1));
            concept2.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_2));
            itemRepository.save(concept2);
        }
        {
            ConceptMetamac concept3 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_3);
            concept3.setQuantity(new Quantity());
            concept3.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
            concept3.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_10_V1_CODE_1));
            concept3.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
            concept3.getQuantity().setSignificantDigits(Integer.valueOf(2));
            concept3.getQuantity().setDecimalPlaces(Integer.valueOf(3));
            concept3.getQuantity().setUnitMultiplier(Integer.valueOf(100));
            concept3.getQuantity().setMinimum(Integer.valueOf(1000));
            concept3.getQuantity().setMaximum(Integer.valueOf(2000));
            concept3.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_15_V1_CONCEPT_1));
            concept3.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_15_V1_CONCEPT_2));
            concept3.getQuantity().setIsPercentage(Boolean.FALSE);
            concept3.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
            concept3.getQuantity().setBaseQuantity(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_11_V1_CONCEPT_1));
            concept3.getQuantity().setBaseLocation(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_10_V1_CODE_2));
            itemRepository.save(concept3);
        }

        entityManager.flush();

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(6, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_1_V2_CONCEPT_2);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_3_V1_CONCEPT_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_3_V1_CONCEPT_2);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_11_V1_CONCEPT_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CODE_NOT_EXTERNALLY_PUBLISHED, CODELIST_10_V1_CODE_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CODE_NOT_EXTERNALLY_PUBLISHED, CODELIST_10_V1_CODE_2);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorRelatedResourcesCoreRepresentationNotExternallyPublished() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        // Change some metadata to force errors
        {
            ConceptMetamac concept1 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_1);
            Representation enumeratedRepresentation = new Representation();
            enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
            enumeratedRepresentation.setEnumerationConceptScheme(conceptsService.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_1_V1));
            concept1.setCoreRepresentation(enumeratedRepresentation);
            itemRepository.save(concept1);
        }
        {
            ConceptMetamac concept2 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_2);
            Representation enumeratedRepresentation = new Representation();
            enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
            enumeratedRepresentation.setEnumerationCodelist(codesService.retrieveCodelistByUrn(ctx, CODELIST_9_V1));
            concept2.setCoreRepresentation(enumeratedRepresentation);
            itemRepository.save(concept2);
        }
        {
            ConceptMetamac concept3 = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V2_CONCEPT_3);
            Representation enumeratedRepresentation = new Representation();
            enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
            enumeratedRepresentation.setEnumerationCodelist(codesService.retrieveCodelistByUrn(ctx, CODELIST_8_V1)); // ok
            concept3.setCoreRepresentation(enumeratedRepresentation);
            itemRepository.save(concept3);
        }

        entityManager.flush();

        try {
            conceptsService.publishExternallyConceptScheme(getServiceContextAdministrador(), urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CONCEPT_SCHEME_NOT_EXTERNALLY_PUBLISHED, CONCEPT_SCHEME_1_V1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CODELIST_NOT_EXTERNALLY_PUBLISHED, CODELIST_9_V1);
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeErrorRelatedResourcesCategorisationsNotExternallyPublished() throws Exception {

        String urn = CONCEPT_SCHEME_3_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        try {
            conceptsService.publishExternallyConceptScheme(ctx, urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_2);
        }
    }

    @Override
    public void testCheckConceptSchemeWithRelatedResourcesExternallyPublished() throws Exception {
        // tested in testPublishExternallyConceptSchemeErrorRelatedResourcesNotExternallyPublished
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

        TaskInfo copyResult = conceptsService.copyConceptScheme(getServiceContextAdministrador(), urnToCopy, null);

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
        assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept1);
        assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept11);
        assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept2);
        assertListConceptsContainsConcept(conceptSchemeVersionNewArtefact.getItems(), urnExpectedConcept3);

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

        // Check order value of concepts was keeped when a concept scheme was copied
        {
            assertConceptOrderValue(urnExpectedConcept1, 0);
            assertConceptOrderValue(urnExpectedConcept11, 0);
            assertConceptOrderValue(urnExpectedConcept2, 1);
            assertConceptOrderValue(urnExpectedConcept3, 2);
        }

    }

    @Test
    public void testCopyConceptSchemeCheckQuantity() throws Exception {

        String urnToCopy = CONCEPT_SCHEME_15_V1;

        // Versioning
        TaskInfo copyResult = conceptsService.copyConceptScheme(getServiceContextAdministrador(), urnToCopy, null);
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME15(01.000).CONCEPT01";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME15(01.000)";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME15(01.000).CONCEPT02";
        String urnExpectedConcept3 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME15(01.000).CONCEPT03";

        // Check quantity after versioning
        ConceptSchemeVersionMetamac conceptSchemeVersionNewArtefact = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), copyResult.getUrnResult());
        assertEquals(urnExpected, conceptSchemeVersionNewArtefact.getMaintainableArtefact().getUrn());

        {
            // Concept 1
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept1);
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.CHANGE_RATE, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.END, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(2), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(3), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(10), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(100), quantity.getMinimum());
                assertEquals(Integer.valueOf(200), quantity.getMaximum());
                assertEquals(urnExpectedConcept2, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertEquals(Boolean.TRUE, quantity.getIsPercentage());
                ConceptsAsserts.assertEqualsInternationalString(quantity.getPercentageOf(), "es", "porcentaje quantity c1", "en", "percentage quantity c1");
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, quantity.getBaseQuantity().getNameableArtefact().getUrn());
            }

            // Concept 2
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept2);
                assertNull(concept.getQuantity());
            }
            // Concept 3
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept3);
                assertNotNull(concept.getQuantity());
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.FRACTION, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.START, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(3), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(2), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(100), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(200), quantity.getMinimum());
                assertEquals(Integer.valueOf(350), quantity.getMaximum());
                assertEquals(urnExpectedConcept2, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertNull(quantity.getIsPercentage());
                assertNull(quantity.getPercentageOf());
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertNull(quantity.getBaseQuantity());
            }
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
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept1);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept2);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept21);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept211);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept22);

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

        // Check order value of concepts was keeped when a concept scheme was versioned
        {
            assertConceptOrderValue(urnExpectedConcept1, 0);
            assertConceptOrderValue(urnExpectedConcept2, 1);
            assertConceptOrderValue(urnExpectedConcept21, 0);
            assertConceptOrderValue(urnExpectedConcept211, 0);
            assertConceptOrderValue(urnExpectedConcept22, 1);
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
            List<ConceptSchemeVersionMetamac> allVersions = conceptsService.retrieveConceptSchemeVersions(getServiceContextAdministrador(),
                    conceptSchemeVersionNewVersion.getMaintainableArtefact().getUrn());
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
    public void testVersioningConceptSchemeCheckQuantity() throws Exception {

        String urn = CONCEPT_SCHEME_15_V1;

        // Versioning
        conceptsService.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(02.000).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(02.000).CONCEPT02";
        String urnExpectedConcept3 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(02.000).CONCEPT03";

        // Check quantity after versioning
        entityManager.clear();

        {
            // Concept 1
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept1);
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.CHANGE_RATE, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.END, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(2), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(3), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(10), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(100), quantity.getMinimum());
                assertEquals(Integer.valueOf(200), quantity.getMaximum());
                assertEquals(urnExpectedConcept2, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertEquals(Boolean.TRUE, quantity.getIsPercentage());
                ConceptsAsserts.assertEqualsInternationalString(quantity.getPercentageOf(), "es", "porcentaje quantity c1", "en", "percentage quantity c1");
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, quantity.getBaseQuantity().getNameableArtefact().getUrn());
            }

            // Concept 2
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept2);
                assertNull(concept.getQuantity());
            }
            // Concept 3
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept3);
                assertNotNull(concept.getQuantity());
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.FRACTION, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.START, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(3), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(2), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(100), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(200), quantity.getMinimum());
                assertEquals(Integer.valueOf(350), quantity.getMaximum());
                assertEquals(urnExpectedConcept2, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertNull(quantity.getIsPercentage());
                assertNull(quantity.getPercentageOf());
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertNull(quantity.getBaseQuantity());
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
            assertTrue(conceptSchemeVersionNewVersion.getMaintainableArtefact().getIsTemporal());
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
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept1);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept2);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept21);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept211);
            assertListConceptsContainsConcept(conceptSchemeVersionNewVersion.getItems(), urnExpectedConcept22);

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
        {
            Categorisation categorisation = assertListContainsCategorisation(conceptSchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000_temporal)");
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
        }
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
            assertTrue(conceptSchemeNewVersion.getMaintainableArtefact().getIsTemporal());

            assertEquals(null, conceptSchemeNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(conceptSchemeNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(conceptSchemeNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(conceptSchemeNewVersion.getMaintainableArtefact().getLatestPublic());

            assertEquals(3, conceptSchemeNewVersion.getMaintainableArtefact().getCategorisations().size());
            {
                Categorisation categorisation = assertListContainsCategorisation(conceptSchemeNewVersion.getMaintainableArtefact().getCategorisations(),
                        "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)");
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
            }

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
                LocalisedString localisedString = new LocalisedString("fr", "fr - text sample");
                conceptSchemeVersionTemporal.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Item 1: Change plural name and order value (0 -> 1)
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CONCEPT_SCHEME_3_V1_CONCEPT_1));
                conceptTemporal.setSdmxRelatedArtefact(ConceptRoleEnum.MEASURE_DIMENSION);

                LocalisedString localisedString = new LocalisedString("fr", "fr - text sample");
                conceptTemporal.getPluralName().addText(localisedString);

                conceptTemporal.setOrderValue(1);
            }
            // Item 2: Add legal acts and order value (1 -> 0)
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CONCEPT_SCHEME_3_V1_CONCEPT_2));
                conceptTemporal.setSdmxRelatedArtefact(ConceptRoleEnum.MEASURE_DIMENSION);

                conceptTemporal.setLegalActs(new InternationalString());
                conceptTemporal.getLegalActs().addText(new LocalisedString("fr", "fr - text sample legal acts"));
                conceptTemporal.getLegalActs().addText(new LocalisedString("es", "es - text sample legal acts"));

                conceptTemporal.setOrderValue(0);
            }
            // Merge
            conceptSchemeVersionTemporal = conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
            conceptSchemeVersionTemporal = conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
            ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = conceptsService.mergeTemporalVersion(getServiceContextAdministrador(), conceptSchemeVersionTemporal);

            // Assert **************************************

            // Item Scheme
            assertEquals(2, conceptSchemeVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("fr - text sample", conceptSchemeVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));

            // Item 1: plural name changed
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_1);
                assertTrue(ConceptRoleEnum.MEASURE_DIMENSION.equals(conceptTemporal.getSdmxRelatedArtefact()));
                assertEquals(2, conceptTemporal.getPluralName().getTexts().size());
                assertEquals("fr - text sample", conceptTemporal.getPluralName().getLocalisedLabel("fr"));
            }
            // Item 2: legal acts changed
            {
                ConceptMetamac conceptTemporal = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_3_V1_CONCEPT_2);
                assertEquals(2, conceptTemporal.getLegalActs().getTexts().size());
                assertEquals("fr - text sample legal acts", conceptTemporal.getLegalActs().getLocalisedLabel("fr"));
                assertEquals("es - text sample legal acts", conceptTemporal.getLegalActs().getLocalisedLabel("es"));
            }

            // Check order value of concepts was changed when a temporal concept scheme was merge with another one
            {
                assertConceptOrderValue(CONCEPT_SCHEME_3_V1_CONCEPT_1, 1);
                assertConceptOrderValue(CONCEPT_SCHEME_3_V1_CONCEPT_2, 0);
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
            assertNull(conceptSchemeVersionMetamac.getMaintainableArtefact().getIsTemporal());
        }
    }

    @Test
    public void testMergeConceptSchemeCheckQuantity() throws Exception {

        String urn = CONCEPT_SCHEME_15_V1;

        // Create temporal version
        TaskInfo versioningResult = conceptsService.createTemporalVersionConceptScheme(getServiceContextAdministrador(), urn);

        entityManager.clear();
        ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        // // Modify quantities
        String urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000_temporal).CONCEPT01";
        String urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000_temporal).CONCEPT02";
        String urnExpectedConcept3 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000_temporal).CONCEPT03";
        // Concept 1: modify
        {
            ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept1);
            assertEquals(urnExpectedConcept2, concept.getQuantity().getNumerator().getNameableArtefact().getUrn());
            // Modify
            concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
            concept.getQuantity().setSignificantDigits(Integer.valueOf(6));
            concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept3));
            concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_17_V1_CONCEPT_1));
            concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02"));
            concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
        }
        // Concept 2: add
        {

            ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept2);
            assertNull(concept.getQuantity());

            concept.setQuantity(new Quantity());
            concept.getQuantity().setQuantityType(QuantityTypeEnum.FRACTION);
            concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), "urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01"));
            concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
            concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
            concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
            concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
            concept.getQuantity().setMinimum(Integer.valueOf(1000));
            concept.getQuantity().setMaximum(Integer.valueOf(2000));
            concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept1));
            concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_17_V1_CONCEPT_2));
            concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
        }
        // Concept 3: remove
        {
            ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept3);
            assertNotNull(concept.getQuantity());
            quantityRepository.delete(concept.getQuantity());
            concept.setQuantity(null);
            concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
        }

        // Merge
        conceptSchemeVersionTemporal = conceptsService.sendConceptSchemeToProductionValidation(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
        conceptSchemeVersionTemporal = conceptsService.sendConceptSchemeToDiffusionValidation(getServiceContextAdministrador(), conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
        conceptsService.mergeTemporalVersion(getServiceContextAdministrador(), conceptSchemeVersionTemporal);

        urnExpectedConcept1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000).CONCEPT01";
        urnExpectedConcept2 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000).CONCEPT02";
        urnExpectedConcept3 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX02:CONCEPTSCHEME15(01.000).CONCEPT03";

        // Check quantity after merge
        entityManager.clear();
        {
            // Concept 1 (modified)
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept1);
                Quantity quantity = concept.getQuantity();
                assertNotNull(quantity);
                assertEquals(QuantityTypeEnum.CHANGE_RATE, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.START, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(6), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(3), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(10), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(100), quantity.getMinimum());
                assertEquals(Integer.valueOf(200), quantity.getMaximum());
                assertEquals(urnExpectedConcept3, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, quantity.getDenominator().getNameableArtefact().getUrn());
                assertEquals(Boolean.TRUE, quantity.getIsPercentage());
                ConceptsAsserts.assertEqualsInternationalString(quantity.getPercentageOf(), "es", "porcentaje quantity c1", "en", "percentage quantity c1");
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, quantity.getBaseQuantity().getNameableArtefact().getUrn());
            }

            // Concept 2 (added)
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept2);
                assertNotNull(concept.getQuantity());
                Quantity quantity = concept.getQuantity();
                assertEquals(QuantityTypeEnum.FRACTION, quantity.getQuantityType());
                assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01", quantity.getUnitCode().getNameableArtefact().getUrn());
                assertEquals(QuantityUnitSymbolPositionEnum.START, quantity.getUnitSymbolPosition());
                assertEquals(Integer.valueOf(2), quantity.getSignificantDigits());
                assertEquals(Integer.valueOf(3), quantity.getDecimalPlaces());
                assertEquals(Integer.valueOf(100), quantity.getUnitMultiplier());
                assertEquals(Integer.valueOf(1000), quantity.getMinimum());
                assertEquals(Integer.valueOf(2000), quantity.getMaximum());
                assertEquals(urnExpectedConcept1, quantity.getNumerator().getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, quantity.getDenominator().getNameableArtefact().getUrn());
                assertNull(quantity.getIsPercentage());
                assertNull(quantity.getPercentageOf());
                assertNull(quantity.getBaseValue());
                assertNull(quantity.getBaseTime());
                assertNull(quantity.getBaseLocation());
                assertNull(quantity.getBaseQuantity());
            }
            // Concept 3 (removed)
            {
                ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urnExpectedConcept3);
                assertNull(concept.getQuantity());
            }

            // Check order value of concepts was keeped when a temporal concept scheme was merge with another one
            {
                assertConceptOrderValue(urnExpectedConcept1, 0);
                assertConceptOrderValue(urnExpectedConcept2, 1);
            }
        }
    }

    @Override
    @Test
    public void testVersioningRelatedConcepts() throws Exception {
        // tested in testVersioningConceptSchemeCheckConceptRelations
    }

    @Override
    public void testVersioningConceptsQuantity() throws Exception {
        // tested in testVersioningConceptSchemeCheckQuantity
    }

    @Override
    @Test
    public void testStartConceptSchemeValidity() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.startConceptSchemeValidity(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V2);

        assertNotNull(conceptSchemeVersion);
        assertNotNull(conceptSchemeVersion.getMaintainableArtefact().getValidFrom());
        assertNull(conceptSchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testStartConceptSchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CONCEPT_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                conceptsService.startConceptSchemeValidity(getServiceContextAdministrador(), urn);
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
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_9_V1), ConceptRoleEnum.ATTRIBUTE);
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
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), conceptRetrieved.getNameableArtefact().getUrn());

        // Check order value of the new concept
        assertConceptOrderValue(urn, 4);
    }

    @Test
    public void testCreateConceptWithExtends() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, ConceptRoleEnum.ATTRIBUTE);
        concept.setParent(null);
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V1_CONCEPT_1));
        concept.setVariable(codesService.retrieveVariableByUrn(ctx, VARIABLE_1));
        concept.setSdmxRelatedArtefact(ConceptRoleEnum.MEASURE_DIMENSION);

        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // Create
        ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(ctx, conceptCreated.getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptRetrieved.getConceptExtends().getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptWithRepresentation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setParent(null);
        concept.setSdmxRelatedArtefact(null);
        Representation enumeratedRepresentation = new Representation();
        enumeratedRepresentation.setIsExtended(true);
        enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
        enumeratedRepresentation.setEnumerationConceptScheme(conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_15_V1));
        concept.setCoreRepresentation(enumeratedRepresentation);

        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;

        // Create
        ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
        assertEquals(ctx.getUserId(), conceptCreated.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptCreated.getLastUpdatedBy());

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(ctx, conceptCreated.getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1, conceptRetrieved.getCoreRepresentation().getEnumerationConceptScheme().getMaintainableArtefact().getUrn());
    }

    @Test
    public void testCreateConceptSubconcept() throws Exception {

        ConceptType conceptType = null;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
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

        Concept concept1 = assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListConceptsContainsConcept(concept1.getChildren(), conceptRetrieved.getNameableArtefact().getUrn());

        // Check order value of the new concept
        assertConceptOrderValue(urn, 0);
    }

    @Test
    public void testCreateConceptQuantity() throws Exception {

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, null);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.QUANTITY);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));

        // Create
        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertNotNull(conceptRetrieved.getQuantity()); // test values in assertEqualsConcept
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);
    }

    @Test
    public void testCreateConceptMagnitude() throws Exception {

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, null);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.MAGNITUDE);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));

        // Create
        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertNotNull(conceptRetrieved.getQuantity()); // test values in assertEqualsConcept
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);
    }

    @Test
    public void testCreateConceptFraction() throws Exception {

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_15_V1_CONCEPT_1;
        String conceptDenominator = CONCEPT_SCHEME_16_V1_CONCEPT_1;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, null);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.FRACTION);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));

        // Create
        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertNotNull(conceptRetrieved.getQuantity()); // test values in assertEqualsConcept
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);
    }

    @Test
    public void testCreateConceptRatio() throws Exception {

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_15_V1_CONCEPT_1;
        String conceptDenominator = CONCEPT_SCHEME_16_V1_CONCEPT_1;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, null);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.RATIO);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());

        // Create
        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertNotNull(conceptRetrieved.getQuantity()); // test values in assertEqualsConcept
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);
    }

    @Test
    public void testCreateConceptIndex() throws Exception {

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_15_V1_CONCEPT_1;
        String conceptDenominator = CONCEPT_SCHEME_16_V1_CONCEPT_1;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, null);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseValue(Integer.valueOf(15));

        // Create
        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertNotNull(conceptRetrieved.getQuantity()); // test values in assertEqualsConcept
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);
    }

    @Test
    public void testCreateConceptChangeRate() throws Exception {

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_15_V1_CONCEPT_1;
        String conceptDenominator = CONCEPT_SCHEME_16_V1_CONCEPT_1;
        String conceptBaseQuantity = CONCEPT_SCHEME_15_V1_CONCEPT_2;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, null);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.CHANGE_RATE);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseQuantity(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptBaseQuantity));

        // Create
        String conceptSchemeUrn = CONCEPT_SCHEME_16_V1;
        ConceptMetamac conceptSchemeVersionCreated = conceptsService.createConcept(getServiceContextAdministrador(), conceptSchemeUrn, concept);
        String urn = conceptSchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate
        ConceptMetamac conceptRetrieved = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
        assertNotNull(conceptRetrieved.getQuantity()); // test values in assertEqualsConcept
        ConceptsMetamacAsserts.assertEqualsConcept(concept, conceptRetrieved);
    }

    @Test
    public void testCreateConceptEnumeratedRepresentationAssignAutomaticallyVariable() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
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
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setParent(null);
        concept.setVariable(codesService.retrieveVariableByUrn(ctx, VARIABLE_1));

        // Codelist has not same variable
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
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_REPRESENTATION, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorTextRepresentation() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_2_V1;

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setSdmxRelatedArtefact(null);
        concept.setParent(null);

        CodelistVersionMetamac codelistVersion = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1);
        try {
            Representation enumeratedRepresentation = new Representation();
            enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
            enumeratedRepresentation.setEnumerationCodelist(codelistVersion);
            concept.setCoreRepresentation(enumeratedRepresentation);

            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("representation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_REPRESENTATION, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorMetadataUnexpectedQuantityRatio() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);

        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.RATIO);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(null);
        concept.getQuantity().setDenominator(null);
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseValue(Integer.valueOf(15));
        concept.getQuantity().setBaseTime("2011");
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("base location unexpected");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorMetadataUnexpectedQuantityAmount() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String conceptDenominator = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        String conceptBaseQuantity = CONCEPT_SCHEME_1_V2_CONCEPT_4;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.AMOUNT);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseValue(Integer.valueOf(15));
        concept.getQuantity().setBaseTime("2011");
        concept.getQuantity().setBaseQuantity(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptBaseQuantity));

        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("metadatas unexpected");
        } catch (MetamacException e) {
            assertEquals(9, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_MIN, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_MAX, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_NUMERATOR, e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_DENOMINATOR, e.getExceptionItems().get(3).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(4).getCode());
            assertEquals(1, e.getExceptionItems().get(4).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_IS_PERCENTAGE, e.getExceptionItems().get(4).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(5).getCode());
            assertEquals(1, e.getExceptionItems().get(5).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_PERCENTAGE_OF, e.getExceptionItems().get(5).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(6).getCode());
            assertEquals(1, e.getExceptionItems().get(6).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE, e.getExceptionItems().get(6).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(7).getCode());
            assertEquals(1, e.getExceptionItems().get(7).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, e.getExceptionItems().get(7).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(8).getCode());
            assertEquals(1, e.getExceptionItems().get(8).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_QUANTITY, e.getExceptionItems().get(8).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorBaseTimeIncorrect() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String conceptDenominator = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setUnitMultiplier(Integer.valueOf(100));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseTime("2011xx");

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("base time incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_TIME, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorNullQuantityUnitMultiplier() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String conceptDenominator = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseTime("2011");

        concept.getQuantity().setUnitMultiplier(null);

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("Quantity unit multiplier null");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_MULTIPLIER, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorQuantityUnitMultiplierNotPowerOfTen() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptType conceptType = null;
        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String conceptDenominator = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseTime("2011");

        concept.getQuantity().setUnitMultiplier(11);

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("Quantity unit multiplier not power of ten");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_MULTIPLIER, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorConceptExtendsWrongProcStatus() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, ConceptRoleEnum.ATTRIBUTE);
        concept.setParent(null);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_2_V1_CONCEPT_1);
        concept.setConceptExtends(conceptExtends);

        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        // Create
        try {
            conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_EXTENDS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorConceptIsRolAndCanNotHaveConceptExtends() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, ConceptRoleEnum.ATTRIBUTE);
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
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_EXTENDS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptErrorConceptIsRolAndCanNotHaveVariable() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, ConceptRoleEnum.ATTRIBUTE);
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
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, ConceptRoleEnum.ATTRIBUTE);
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
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, null, ConceptRoleEnum.ATTRIBUTE);
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
        ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_8_V1), ConceptRoleEnum.ATTRIBUTE);
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
    public void testCheckConceptRepresentation() throws Exception {
        // already tested in testCreateConcept*
    }

    @Override
    @Test
    public void testUpdateConcept() throws Exception {
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1_CONCEPT_1));
        assertTrue(RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType()));
        concept.setCoreRepresentation(ConceptsDoMocks.mockTextFormatRepresentation());
        concept.setQuantity(null);

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
    public void testUpdateConceptExtends() throws Exception {

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
    public void testUpdateConceptQuantity() throws Exception {

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
    public void testUpdateConceptErrorEnumeratedRepresentation() throws Exception {
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
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_REPRESENTATION, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptErrorConceptExtendsWrongProcStatus() throws Exception {

        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_2_V1_CONCEPT_1));

        // Create
        try {
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_EXTENDS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptErrorNullQuantityUnitMultiplier() throws Exception {
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1_CONCEPT_1));
        assertTrue(RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType()));
        concept.setCoreRepresentation(ConceptsDoMocks.mockTextFormatRepresentation());

        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String conceptDenominator = CONCEPT_SCHEME_1_V2_CONCEPT_3;

        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseTime("2011");

        concept.getQuantity().setUnitMultiplier(null);

        try {
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
            fail("Quantity unit multiplier null");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_MULTIPLIER, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConceptErrorQuantityUnitMultiplierNotPowerOfTen() throws Exception {
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        concept.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        concept.getNameableArtefact().setName(ConceptsDoMocks.mockInternationalString());
        concept.setConceptExtends(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1_CONCEPT_1));
        assertTrue(RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType()));
        concept.setCoreRepresentation(ConceptsDoMocks.mockTextFormatRepresentation());

        String unitCodeUrn = CODELIST_7_V2_CODE_1;
        String conceptNumerator = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String conceptDenominator = CONCEPT_SCHEME_1_V2_CONCEPT_3;

        concept.setQuantity(new Quantity());
        concept.getQuantity().setQuantityType(QuantityTypeEnum.INDEX);
        concept.getQuantity().setUnitCode(codesService.retrieveCodeByUrn(getServiceContextAdministrador(), unitCodeUrn));
        concept.getQuantity().setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.START);
        concept.getQuantity().setSignificantDigits(Integer.valueOf(2));
        concept.getQuantity().setDecimalPlaces(Integer.valueOf(3));
        concept.getQuantity().setMinimum(Integer.valueOf(1000));
        concept.getQuantity().setMaximum(Integer.valueOf(2000));
        concept.getQuantity().setNumerator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptNumerator));
        concept.getQuantity().setDenominator(conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptDenominator));
        concept.getQuantity().setIsPercentage(Boolean.FALSE);
        concept.getQuantity().setPercentageOf(BaseDoMocks.mockInternationalString());
        concept.getQuantity().setBaseTime("2011");

        concept.getQuantity().setUnitMultiplier(11);

        try {
            conceptsService.updateConcept(getServiceContextAdministrador(), concept);
            fail("Quantity unit multiplier not power of ten");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_MULTIPLIER, e.getExceptionItems().get(0).getMessageParameters()[0]);
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
        assertNull(concept.getQuantity());
    }

    @Test
    public void testRetrieveConceptByUrnWithQuantity() throws Exception {
        // Retrieve
        String urn = CONCEPT_SCHEME_16_V1_CONCEPT_1;
        ConceptMetamac concept = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(urn, concept.getNameableArtefact().getUrn());
        assertNotNull(concept.getQuantity());
        assertEquals(QuantityTypeEnum.CHANGE_RATE, concept.getQuantity().getQuantityType());
        assertEquals(CODELIST_7_V2_CODE_1, concept.getQuantity().getUnitCode().getNameableArtefact().getUrn());
        assertEquals(QuantityUnitSymbolPositionEnum.END, concept.getQuantity().getUnitSymbolPosition());
        assertEquals(Integer.valueOf(2), concept.getQuantity().getSignificantDigits());
        assertEquals(Integer.valueOf(3), concept.getQuantity().getDecimalPlaces());
        assertEquals(Integer.valueOf(10), concept.getQuantity().getUnitMultiplier());
        assertEquals(Integer.valueOf(100), concept.getQuantity().getMinimum());
        assertEquals(Integer.valueOf(200), concept.getQuantity().getMaximum());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_2, concept.getQuantity().getNumerator().getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_1, concept.getQuantity().getDenominator().getNameableArtefact().getUrn());
        assertEquals(Boolean.TRUE, concept.getQuantity().getIsPercentage());
        ConceptsAsserts.assertEqualsInternationalString(concept.getQuantity().getPercentageOf(), "es", "Porcentaje de 1", "en", "Percentage of 1");
        assertNull(concept.getQuantity().getBaseValue());
        assertNull(concept.getQuantity().getBaseTime());
        assertNull(concept.getQuantity().getBaseLocation());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_3, concept.getQuantity().getBaseQuantity().getNameableArtefact().getUrn());
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

        // Check order value of the rest of concepts before delete concept
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);

        // Delete concept
        conceptsService.deleteConcept(getServiceContextAdministrador(), urn);
        entityManager.clear();

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
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertEquals(7, conceptSchemeVersion.getItems().size());
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1);

        // Check order value of the rest of concepts after delete concept
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 2);
    }

    @Test
    public void testDeleteConceptWithParentAndChildren() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_4_1;
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEquals(4, conceptSchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, conceptSchemeVersion.getItems().size());

        // Check order value of the rest of concepts before delete concept
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

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
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItemsFirstLevel(), CONCEPT_SCHEME_1_V2_CONCEPT_4);
        assertEquals(6, conceptSchemeVersion.getItems().size());
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_3);
        assertListConceptsContainsConcept(conceptSchemeVersion.getItems(), CONCEPT_SCHEME_1_V2_CONCEPT_4);

        // Check order value of the rest of concepts after delete concept
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
    }

    @Test
    @Override
    public void testImportConceptsTsv() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_9_V1;
        String fileName = "importation-concept-01.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        boolean updateAlreadyExisting = false;

        TaskImportationInfo taskImportTsvInfo = conceptsService.importConceptsTsv(getServiceContextAdministrador(), conceptSchemeUrn, file, fileName, updateAlreadyExisting, Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(0, taskImportTsvInfo.getInformationItems().size());

        // Validate item scheme
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, conceptSchemeVersion.getItemScheme().getIsTaskInBackground());

        // Validate concepts

        List<ConceptMetamacVisualisationResult> result = conceptsService.retrieveConceptsByConceptSchemeUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1, "es");
        assertEquals(result.size(), 5);

        String conceptSchemeUrnPart = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME09(01.000).";
        {
            String semanticIdentifier = "CORRELACION";
            ConceptMetamac conceptMetamac = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptSchemeUrnPart + semanticIdentifier);
            assertEquals(semanticIdentifier, conceptMetamac.getNameableArtefact().getCode());
            assertEquals(conceptMetamac.getNameableArtefact().getUrn(), conceptMetamac.getNameableArtefact().getUrnProvider());
            assertEquals(null, conceptMetamac.getParent());
            assertEqualsInternationalString(conceptMetamac.getNameableArtefact().getName(), "es", "Correlación", null, null);
            assertEqualsInternationalString(conceptMetamac.getNameableArtefact().getDescription(), "es",
                    "<span style=\"color: rgb(84, 84, 84); font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: 18px; text-align: justify; background-color: rgb(255, 255, 255);\">Medida de la relación existente entre dos variables. Su valor está comprendido entre –1 y 1. Si es negativo la relación entre las variables es inversa, es decir, a medida que aumentan los valores de una decrecen los de la otra. Si es positivo la asociación es directa, es decir, los valores de una variable aumentan con la otra. Un valor de cero indica ausencia de relación. Cuando las variables son continuas y tienen una relación lineal, el coeficiente de correlación lineal de Pearson es una medida de asociación adecuada. Cuando las variables no son continuas se utilizan otros coeficientes de correlación.</span>",
                    null, null);
            assertEqualsInternationalString(conceptMetamac.getNameableArtefact().getComment(), "ru", "Comment RU", "zh", "Comment ZH");
            assertEqualsInternationalString(conceptMetamac.getPluralName(), "es", "Plura ES", null, null);
            assertEqualsInternationalString(conceptMetamac.getAcronym(), "es", "Acrónimo ES", null, null);
            assertEqualsInternationalString(conceptMetamac.getDescriptionSource(), "es", "Descripción Source ES", "pt", "Descripción Source PT");
            assertEqualsInternationalString(conceptMetamac.getContext(), "es", "Contexto ES", null, null);
            assertEqualsInternationalString(conceptMetamac.getDocMethod(), "es", "Documentación Metodológica ES", null, null);
            assertEqualsInternationalString(conceptMetamac.getDerivation(), "es", "Derivación Es", null, null);
            assertEqualsInternationalString(conceptMetamac.getLegalActs(), "es", "Lega ES", null, null);

            assertEquals("DIRECT", conceptMetamac.getConceptType().getIdentifier());

            Representation representation = new Representation();
            representation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
            representation.setEnumerationCodelist(codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V1));
            assertEqualsRepresentation(representation, conceptMetamac.getCoreRepresentation());

            assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptMetamac.getConceptExtends().getNameableArtefact().getUrn());

            BaseAsserts.assertEqualsDay(new DateTime(), conceptMetamac.getLastUpdated());
        }
    }

    @Test
    @Override
    public void testExportConceptsTsv() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String fileName = conceptsService.exportConceptsTsv(getServiceContextAdministrador(), conceptSchemeUrn);
        assertNotNull(fileName);

        // Validate
        File file = new File(tempDirPath() + File.separatorChar + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder header = new StringBuilder("code\tparent");
        String nameHeader = "name#es\tname#pt\tname#en\tname#ca";
        header.append("\t" + nameHeader);
        String descriptionHeader = "description#es\tdescription#pt\tdescription#en\tdescription#ca";
        header.append("\t" + descriptionHeader);
        String commentHeader = "comment#es\tcomment#pt\tcomment#en\tcomment#ca";
        header.append("\t" + commentHeader);
        String pluralNameHeader = "plural_name#es\tplural_name#pt\tplural_name#en\tplural_name#ca";
        header.append("\t" + pluralNameHeader);
        String acronymNameHeader = "acronym#es\tacronym#pt\tacronym#en\tacronym#ca";
        header.append("\t" + acronymNameHeader);
        String descriptionSourceHeader = "description_source#es\tdescription_source#pt\tdescription_source#en\tdescription_source#ca";
        header.append("\t" + descriptionSourceHeader);
        String contextHeader = "context#es\tcontext#pt\tcontext#en\tcontext#ca";
        header.append("\t" + contextHeader);
        String docMethodHeader = "docMethod#es\tdocMethod#pt\tdocMethod#en\tdocMethod#ca";
        header.append("\t" + docMethodHeader);
        String derivationHeader = "derivation#es\tderivation#pt\tderivation#en\tderivation#ca";
        header.append("\t" + derivationHeader);
        String legalActsHeader = "legal_acts#es\tlegal_acts#pt\tlegal_acts#en\tlegal_acts#ca";
        header.append("\t" + legalActsHeader);
        String conceptTypeHeader = "concept_type\trepresentation#type\trepresentation#value\tconcept_extends";
        header.append("\t" + conceptTypeHeader);

        assertEquals(header.toString(), bufferedReader.readLine());
        Set<String> lines = new HashSet<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line.replaceAll("\t", "\\\\t"));
            lines.add(line);
        }
        assertEquals(8, lines.size());

        String concepto01Line = "CONCEPT01\t\tNombre conceptScheme-1-v2-concept-1\t\tName conceptScheme-1-v2-concept-1\t\tDescripción conceptScheme-1-v2-concept-1\t\t\t\tComentario conceptScheme-1-v2-concept-1\t\tComment conceptScheme-1-v2-concept-1\t\tPluralName conceptScheme-1-v2-concept-1\t\t\t\tAcrónimo conceptScheme-1-v2-concept-1\t\tAcronym conceptScheme-1-v2-concept-1\t\tDescriptionSource conceptScheme-1-v2-concept-1\t\t\t\tContext conceptScheme-1-v2-concept-1\t\t\t\tDocMethod conceptScheme-1-v2-concept-1\t\t\t\tDerivation conceptScheme-1-v2-concept-1\t\t\t\tLegalActs conceptScheme-1-v2-concept-1\t\t\t\tDIRECT\tENUMERATED\turn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX01:CODELIST07(01.000)\turn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME07(01.000).CONCEPT01";
        assertTrue(lines.contains(concepto01Line));

        String concepto02Line = "CONCEPT02\t\tNombre conceptScheme-1-v2-concept-2\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        assertTrue(lines.contains(concepto02Line));

        String concepto0201Line = "CONCEPT0201\tCONCEPT02\tNombre conceptScheme-1-v2-concept-2-1\t\tName conceptScheme-1-v2-concept-2-1\t\tDescripción conceptScheme-1-v2-concept-2-1\t\tDescription conceptScheme-1-v2-concept-2-1\t\t\t\t\t\t\t\t\t\tAcrónimo conceptScheme-1-v2-concept-2-1\t\tAcronym conceptScheme-1-v2-concept-2-1\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        assertTrue(lines.contains(concepto0201Line));

        String concepto020101Line = "CONCEPT020101\tCONCEPT0201\tNombre conceptScheme-1-v2-concept-2-1-1\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        assertTrue(lines.contains(concepto020101Line));

        String concepto03Line = "CONCEPT03\t\tnombre concept-3\t\tname concept-3\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tDIRECT\t\t\turn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME07(01.000).CONCEPT01";
        assertTrue(lines.contains(concepto03Line));

        String concepto04Line = "CONCEPT04\t\tnombre concept-4\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        assertTrue(lines.contains(concepto04Line));

        String concepto0401Line = "CONCEPT0401\tCONCEPT04\tnombre concept 4-1\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        assertTrue(lines.contains(concepto0401Line));

        String concepto040101Line = "CONCEPT040101\tCONCEPT0401\tNombre conceptScheme-1-v2-concept-4-1-1\t\tName conceptScheme-1-v2-concept-4-1-1\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        assertTrue(lines.contains(concepto040101Line));

        bufferedReader.close();
    }

    @Test
    public void testRetrieveConceptsOrderedInDepthByConceptSchemeUrn() throws Exception {

        List<ItemResult> concepts = conceptMetamacRepository.findConceptsByConceptSchemeOrderedInDepth(Long.valueOf(12), new ConceptMetamacResultSelection(true, true, true, true));

        assertEquals(8, concepts.size());

        Set<String> readConceptCodes = new HashSet<String>();

        for (ItemResult concept : concepts) {
            if (concept.getParent() != null) {
                assertTrue(readConceptCodes.contains(concept.getParent().getCode()));
            }
            readConceptCodes.add(concept.getCode());
        }

        assertTrue(readConceptCodes.contains("CONCEPT01"));
        assertTrue(readConceptCodes.contains("CONCEPT02"));
        assertTrue(readConceptCodes.contains("CONCEPT0201"));
        assertTrue(readConceptCodes.contains("CONCEPT020101"));
        assertTrue(readConceptCodes.contains("CONCEPT03"));
        assertTrue(readConceptCodes.contains("CONCEPT04"));
        assertTrue(readConceptCodes.contains("CONCEPT0401"));
        assertTrue(readConceptCodes.contains("CONCEPT040101"));

        assertEquals("CONCEPT01", concepts.get(0).getCode());
        assertEquals("CONCEPT02", concepts.get(1).getCode());
        assertEquals("CONCEPT0201", concepts.get(2).getCode());
        assertEquals("CONCEPT020101", concepts.get(3).getCode());
        assertEquals("CONCEPT03", concepts.get(4).getCode());
        assertEquals("CONCEPT04", concepts.get(5).getCode());
        assertEquals("CONCEPT0401", concepts.get(6).getCode());
        assertEquals("CONCEPT040101", concepts.get(7).getCode());
    }

    @Test
    public void testRetrieveConceptsOrderedInDepthByConceptSchemeUrnMoreThanTenConceptsWithNoParent() throws Exception {
        // @formatter:off
        /*
         * - CONCEPT_SCHEME
         * - |_ CONCEPT_0
         * - |_ CONCEPT_1
         * - |_ ....
         * - |_ CONCEPT_10
         */
        // @formatter:on

        int conceptsNumber = 11;
        ServiceContext ctx = getServiceContextAdministrador();

        // Create concept scheme
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);

        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
        String conceptSchemeUrn = conceptSchemeVersionCreated.getMaintainableArtefact().getUrn();

        CodelistVersionMetamac coreRepresentation = codesService.retrieveCodelistByUrn(ctx, CODELIST_9_V1);
        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V1_CONCEPT_1);
        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_1);
        CodelistVersion enumerationCodelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V1);

        Representation enumeratedRepresentation = new Representation();
        enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
        enumeratedRepresentation.setEnumerationCodelist(enumerationCodelist);

        // Create child concepts for parent concept
        List<String> urnConcepts = new ArrayList<>(conceptsNumber);

        for (int i = 0; i < conceptsNumber; i++) {
            ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
            concept.setParent(null);
            concept.setConceptExtends(conceptExtends);
            concept.setVariable(variable);
            concept.setCoreRepresentation(enumeratedRepresentation);

            ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            urnConcepts.add(conceptCreated.getNameableArtefact().getUrn());
        }

        List<ItemResult> concepts = conceptMetamacRepository.findConceptsByConceptSchemeOrderedInDepth(conceptSchemeVersionCreated.getId(), new ConceptMetamacResultSelection(true, true, true, true));

        // Validation
        // The number of concepts must be 11
        assertFalse(concepts.isEmpty());
        assertEquals(conceptsNumber, concepts.size());
        assertEquals(conceptsNumber, urnConcepts.size());

        // Concepts are in the expected position
        for (int i = 0; i < conceptsNumber; i++) {
            assertConceptOrderValue(urnConcepts.get(i), i);
            assertNull(concepts.get(i).getParent());
            assertEquals(urnConcepts.get(i), concepts.get(i).getUrn());
        }

    }

    @Test
    public void testRetrieveConceptsOrderedInDepthByConceptSchemeUrnMoreThanTenConceptsWithParent() throws Exception {
        // @formatter:off
        /*
         * - CONCEPT_SCHEME
         * - |_CONCEPT_PARENT
         * -   |_ CONCEPT_0
         * -   |_ CONCEPT_1
         * -   |_ ....
         * -   |_ CONCEPT_10
         */
        // @formatter:on

        int conceptsNumber = 11;
        ServiceContext ctx = getServiceContextAdministrador();

        // Create concept scheme
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);

        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
        String conceptSchemeUrn = conceptSchemeVersionCreated.getMaintainableArtefact().getUrn();

        CodelistVersionMetamac coreRepresentation = codesService.retrieveCodelistByUrn(ctx, CODELIST_9_V1);
        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V1_CONCEPT_1);
        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_1);
        CodelistVersion enumerationCodelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V1);

        Representation enumeratedRepresentation = new Representation();
        enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
        enumeratedRepresentation.setEnumerationCodelist(enumerationCodelist);

        List<String> urnConcepts = new ArrayList<>(conceptsNumber);

        // Create parent concept
        ConceptMetamac conceptParent = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
        conceptParent.setParent(null);
        conceptParent.setConceptExtends(conceptExtends);
        conceptParent.setVariable(variable);
        conceptParent.setCoreRepresentation(enumeratedRepresentation);

        ConceptMetamac conceptParentCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, conceptParent);
        String conceptParentUrn = conceptParentCreated.getNameableArtefact().getUrn();

        // Create child concepts for parent concept
        for (int i = 0; i < conceptsNumber; i++) {
            ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
            concept.setParent(conceptParentCreated);
            concept.setConceptExtends(conceptExtends);
            concept.setVariable(variable);
            concept.setCoreRepresentation(enumeratedRepresentation);

            ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            urnConcepts.add(conceptCreated.getNameableArtefact().getUrn());
        }

        List<ItemResult> concepts = conceptMetamacRepository.findConceptsByConceptSchemeOrderedInDepth(conceptSchemeVersionCreated.getId(), new ConceptMetamacResultSelection(true, true, true, true));

        // Validation
        // The number of concepts must be 12: 1 parent concept and 11 child concepts
        assertFalse(concepts.isEmpty());
        assertEquals(conceptsNumber + 1, concepts.size());
        assertEquals(conceptsNumber, urnConcepts.size());

        // Parent concept is in the expected position
        assertEquals(conceptParentUrn, concepts.get(0).getUrn());
        assertNull(concepts.get(0).getParent());

        // Child concepts are in the expected position
        List<ItemResult> siblingsConcepts = concepts.subList(1, concepts.size());

        for (int i = 0; i < conceptsNumber; i++) {
            assertConceptOrderValue(urnConcepts.get(i), i);
            assertEquals(urnConcepts.get(i), siblingsConcepts.get(i).getUrn());
            assertNotNull(siblingsConcepts.get(i).getParent());
            assertEquals(conceptParentUrn, siblingsConcepts.get(i).getParent().getUrn());
        }
    }

    @Test
    public void testRetrieveConceptsOrderedInDepthByConceptSchemeUrnMoreThanTenConceptsWithTwoParents() throws Exception {
        // @formatter:off
        /*
         * - CONCEPT_SCHEME
         * - |_PARENT_CONCEPT_0
         * -   |_ CONCEPT_0
         * -   |_ CONCEPT_1
         * -   |_ ....
         * -   |_ CONCEPT_10
         * - |_PARENT_CONCEPT_1
         * -   |_ CONCEPT_0
         * -   |_ CONCEPT_1
         * -   |_ ....
         * -   |_ CONCEPT_10
         */
        // @formatter:on

        int conceptsNumber = 11;
        ServiceContext ctx = getServiceContextAdministrador();

        // Create concept scheme
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme(organisationMetamac);

        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
        String conceptSchemeUrn = conceptSchemeVersionCreated.getMaintainableArtefact().getUrn();

        CodelistVersionMetamac coreRepresentation = codesService.retrieveCodelistByUrn(ctx, CODELIST_9_V1);
        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        ConceptMetamac conceptExtends = conceptsService.retrieveConceptByUrn(ctx, CONCEPT_SCHEME_7_V1_CONCEPT_1);
        Variable variable = codesService.retrieveVariableByUrn(ctx, VARIABLE_1);
        CodelistVersion enumerationCodelist = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V1);

        Representation enumeratedRepresentation = new Representation();
        enumeratedRepresentation.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
        enumeratedRepresentation.setEnumerationCodelist(enumerationCodelist);

        List<String> urnConcepts1 = new ArrayList<>(conceptsNumber);

        // Create parent concept 1
        ConceptMetamac parentConcept1 = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
        parentConcept1.setParent(null);
        parentConcept1.setConceptExtends(conceptExtends);
        parentConcept1.setVariable(variable);
        parentConcept1.setCoreRepresentation(enumeratedRepresentation);

        ConceptMetamac parentConcept1Created = conceptsService.createConcept(ctx, conceptSchemeUrn, parentConcept1);
        String parentConcept1Urn = parentConcept1Created.getNameableArtefact().getUrn();

        // Create child concepts for parent concept 1
        for (int i = 0; i < conceptsNumber; i++) {
            ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
            concept.setParent(parentConcept1Created);
            concept.setConceptExtends(conceptExtends);
            concept.setVariable(variable);
            concept.setCoreRepresentation(enumeratedRepresentation);

            ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            urnConcepts1.add(conceptCreated.getNameableArtefact().getUrn());
        }

        List<String> urnConcepts2 = new ArrayList<>(conceptsNumber);

        // Create parent concept 2
        ConceptMetamac parentConcept2 = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
        parentConcept2.setParent(null);
        parentConcept2.setConceptExtends(conceptExtends);
        parentConcept2.setVariable(variable);
        parentConcept2.setCoreRepresentation(enumeratedRepresentation);

        ConceptMetamac parentConcept2Created = conceptsService.createConcept(ctx, conceptSchemeUrn, parentConcept2);
        String parentConcept2Urn = parentConcept2Created.getNameableArtefact().getUrn();

        // Create child concepts for parent concept 2
        for (int i = 0; i < conceptsNumber; i++) {
            ConceptMetamac concept = ConceptsMetamacDoMocks.mockConcept(conceptType, coreRepresentation, ConceptRoleEnum.ATTRIBUTE);
            concept.setParent(parentConcept2Created);
            concept.setConceptExtends(conceptExtends);
            concept.setVariable(variable);
            concept.setCoreRepresentation(enumeratedRepresentation);

            ConceptMetamac conceptCreated = conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
            urnConcepts2.add(conceptCreated.getNameableArtefact().getUrn());
        }

        List<ItemResult> concepts = conceptMetamacRepository.findConceptsByConceptSchemeOrderedInDepth(conceptSchemeVersionCreated.getId(), new ConceptMetamacResultSelection(true, true, true, true));

        // Validation
        // The number of concepts must be 24: 2 parent concepts and 11 child concepts for each parent
        assertFalse(concepts.isEmpty());
        assertEquals(conceptsNumber * 2 + 2, concepts.size());
        assertEquals(conceptsNumber, urnConcepts1.size());
        assertEquals(conceptsNumber, urnConcepts2.size());

        // Parent concepts are in the expected position
        assertEquals(parentConcept1Urn, concepts.get(0).getUrn());
        assertNull(concepts.get(0).getParent());

        assertEquals(parentConcept2Urn, concepts.get(conceptsNumber + 1).getUrn());
        assertNull(concepts.get(0).getParent());

        // Child concepts are in the expected position
        List<ItemResult> siblingsConcepts1 = concepts.subList(1, conceptsNumber + 1);
        List<ItemResult> siblingsConcepts2 = concepts.subList(conceptsNumber + 2, concepts.size());

        assertEquals(conceptsNumber, siblingsConcepts1.size());
        assertEquals(conceptsNumber, siblingsConcepts2.size());

        for (int i = 0; i < conceptsNumber; i++) {
            assertConceptOrderValue(urnConcepts1.get(i), i);
            assertEquals(urnConcepts1.get(i), siblingsConcepts1.get(i).getUrn());
            assertNotNull(siblingsConcepts1.get(i).getParent());
            assertEquals(parentConcept1Urn, siblingsConcepts1.get(i).getParent().getUrn());
        }

        for (int i = 0; i < conceptsNumber; i++) {
            assertConceptOrderValue(urnConcepts2.get(i), i);
            assertEquals(urnConcepts2.get(i), siblingsConcepts2.get(i).getUrn());
            assertNotNull(siblingsConcepts2.get(i).getParent());
            assertEquals(parentConcept2Urn, siblingsConcepts2.get(i).getParent().getUrn());
        }
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

    @Test
    public void testDeleteConceptErrorAsQuantity() throws Exception {

        String urn = CONCEPT_SCHEME_16_V1_CONCEPT_1;

        // Validation
        try {
            conceptsService.deleteConcept(getServiceContextAdministrador(), urn);
            fail("quantity");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_DELETE_NOT_SUPPORTED_CONCEPT_IN_QUANTITY.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(CONCEPT_SCHEME_16_V1_CONCEPT_2, e.getExceptionItems().get(0).getMessageParameters()[1]);
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
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().id()).ascending().orderBy(ConceptProperties.id())
                    .ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(42, conceptsPagedResult.getTotalRows());
            assertEquals(42, conceptsPagedResult.getValues().size());
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
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
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
            assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_16_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_16_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
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
            assertEquals(8, conceptsPagedResult.getTotalRows());
            assertEquals(8, conceptsPagedResult.getValues().size());
            assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(conceptsPagedResult.getValues().size(), i);
        }

        // Find by code (like)
        {
            String code = "CONCEPT01";
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).withProperty(ConceptProperties.nameableArtefact().code()).like(code + "%")
                    .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, conceptsPagedResult.getTotalRows());
            assertEquals(2, conceptsPagedResult.getValues().size());
            assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(conceptsPagedResult.getValues().size(), i);
        }

        // Find by code (like) paginated
        {
            String code = "CONCEPT02";
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).withProperty(ConceptProperties.nameableArtefact().code()).like(code + "%")
                    .orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending().orderBy(ConceptProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.pageAccess(3, 1, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(5, conceptsPagedResult.getTotalRows());
                assertEquals(3, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(conceptsPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.pageAccess(3, 2, true);
                PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(5, conceptsPagedResult.getTotalRows());
                assertEquals(2, conceptsPagedResult.getValues().size());
                assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

                int i = 0;
                assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
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
        assertEquals(4, conceptsPagedResult.getTotalRows());
        assertEquals(4, conceptsPagedResult.getValues().size());
        assertTrue(conceptsPagedResult.getValues().get(0) instanceof ConceptMetamac);

        int i = 0;
        assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(conceptsPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptsCanBeQuantityDenominatorByCondition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1;
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeQuantityDenominatorByCondition(getServiceContextAdministrador(), conceptSchemeUrn, conditions,
                pagingParameter);

        // Validate
        assertEquals(6, conceptsPagedResult.getTotalRows());
        assertEquals(6, conceptsPagedResult.getValues().size());

        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(conceptsPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptsCanBeQuantityNumeratorByCondition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1;
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeQuantityNumeratorByCondition(getServiceContextAdministrador(), conceptSchemeUrn, conditions,
                pagingParameter);

        // Validate
        assertEquals(6, conceptsPagedResult.getTotalRows());
        assertEquals(6, conceptsPagedResult.getValues().size());

        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(conceptsPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testFindConceptsCanBeQuantityBaseQuantityByCondition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_4_V1;
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Concept.class).orderBy(ConceptProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                .orderBy(ConceptProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptMetamac> conceptsPagedResult = conceptsService.findConceptsCanBeQuantityBaseQuantityByCondition(getServiceContextAdministrador(), conceptSchemeUrn, conditions,
                pagingParameter);

        // Validate
        assertEquals(6, conceptsPagedResult.getTotalRows());
        assertEquals(6, conceptsPagedResult.getValues().size());

        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_15_V1_CONCEPT_3, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_1, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_17_V1_CONCEPT_2, conceptsPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(conceptsPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    public void testFindCodesCanBeQuantityUnitByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).orderBy(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                .orderBy(CodeProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodeMetamac> codesPagedResult = conceptsService.findCodesCanBeQuantityUnitByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(3, codesPagedResult.getTotalRows());
        assertEquals(3, codesPagedResult.getValues().size());

        int i = 0;
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01", codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02", codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST08(01.000).CODE01", codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(codesPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    public void testFindCodesCanBeQuantityBaseLocationByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Code.class).orderBy(CodeProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending()
                .orderBy(CodeProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodeMetamac> codesPagedResult = conceptsService.findCodesCanBeQuantityBaseLocationByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(2, codesPagedResult.getTotalRows());
        assertEquals(2, codesPagedResult.getValues().size());

        int i = 0;
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE01", codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX01:CODELIST07(02.000).CODE02", codesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(codesPagedResult.getValues().size(), i);
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
        List<ConceptMetamac> roleConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urn);
        assertEquals(2, roleConcepts.size());

        // Add relation
        String roleNew1 = CONCEPT_SCHEME_13_V1_CONCEPT_2;
        String roleNew2 = CONCEPT_SCHEME_3_V1_CONCEPT_2;
        conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, roleNew1); // externally
        conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, roleNew2); // internally

        // Validate
        roleConcepts = conceptsService.retrieveRoleConcepts(getServiceContextAdministrador(), urn);
        assertEquals(4, roleConcepts.size());
        assertListConceptsContainsConcept(roleConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_1);
        assertListConceptsContainsConcept(roleConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);
        assertListConceptsContainsConcept(roleConcepts, roleNew1);
        assertListConceptsContainsConcept(roleConcepts, roleNew2);
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
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_ROLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
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
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_ROLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testAddRoleConceptErrorConceptRoleWrongProcStatus() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String urnNewRelation = CONCEPT_SCHEME_4_V1_CONCEPT_1;

        try {
            conceptsService.addRoleConcept(getServiceContextAdministrador(), urn, urnNewRelation);
            fail("not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_ROLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
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
                    conceptUrn, null);

            // Validate
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_1, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
            // assertEquals(VARIABLE_1, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            // assertEquals(CODELIST_9_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn()); // access restricted
            assertEquals(result.getTotalRows(), i);
        }
        {
            // Concept has Variable 2
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = conceptsService.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                    conceptUrn, null);

            // Validate
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_2, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            assertEquals(CODELIST_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(result.getTotalRows(), i);
        }
        {
            // Variable 2
            String variableUrn = VARIABLE_2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = conceptsService.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                    null, variableUrn);

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
        // tested in createConcept
    }

    @Test
    @Override
    public void testPreCreateConcept() throws Exception {
        // tested in createConcept
    }

    @Test
    @Override
    public void testUpdateConceptParent() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1;
        String newConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        String previousConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        Integer newConceptIndex = 0;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Validate previous parent concept
        assertParentConcept(conceptUrn, previousConceptParentUrn);

        conceptsService.updateConceptParent(getServiceContextAdministrador(), conceptUrn, newConceptParentUrn, newConceptIndex);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newConceptIndex);

        // Validate parent concept updated
        assertParentConcept(conceptUrn, newConceptParentUrn);

        // Validate reordered concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptParentNoParentFirstPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1;
        String newConceptParentUrn = null;
        String previousConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        Integer newConceptIndex = 0;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Validate previous parent concept
        assertParentConcept(conceptUrn, previousConceptParentUrn);

        conceptsService.updateConceptParent(getServiceContextAdministrador(), conceptUrn, newConceptParentUrn, newConceptIndex);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newConceptIndex);

        // Validate parent concept updated
        assertParentConcept(conceptUrn, newConceptParentUrn);

        // Validate reordered concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 4);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptParentNoParentLastPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1;
        String newConceptParentUrn = null;
        String previousConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        Integer newConceptIndex = 4;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Validate previous parent concept
        assertParentConcept(conceptUrn, previousConceptParentUrn);

        conceptsService.updateConceptParent(getServiceContextAdministrador(), conceptUrn, newConceptParentUrn, newConceptIndex);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newConceptIndex);

        // Validate parent concept updated
        assertParentConcept(conceptUrn, newConceptParentUrn);

        // Validate reordered concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptParentConceptWithChildMoveToAnotherChild() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_4_1;
        String newConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1;
        String previousConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_4;
        Integer newConceptIndex = 0;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Validate previous parent concept
        assertParentConcept(conceptUrn, previousConceptParentUrn);

        conceptsService.updateConceptParent(getServiceContextAdministrador(), conceptUrn, newConceptParentUrn, newConceptIndex);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newConceptIndex);

        // Validate parent concept updated
        assertParentConcept(conceptUrn, newConceptParentUrn);

        // Validate reordered concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

        // Validate child concept parent not updated
        assertParentConcept(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptUrn);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptParentConceptWithChildMoveToConceptWithChild() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_4_1;
        String newConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2;
        String previousConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_4;
        Integer newConceptIndex = 0;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Validate previous parent concept
        assertParentConcept(conceptUrn, previousConceptParentUrn);

        conceptsService.updateConceptParent(getServiceContextAdministrador(), conceptUrn, newConceptParentUrn, newConceptIndex);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newConceptIndex);

        // Validate parent concept updated
        assertParentConcept(conceptUrn, newConceptParentUrn);

        // Validate reordered concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

        // Validate child concept parent not updated
        assertParentConcept(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptUrn);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptParentNoParentThirdPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2_1;
        String newConceptParentUrn = null;
        String previousConceptParentUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2;
        Integer newConceptIndex = 2;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Validate previous parent concept
        assertParentConcept(conceptUrn, previousConceptParentUrn);

        conceptsService.updateConceptParent(getServiceContextAdministrador(), conceptUrn, newConceptParentUrn, newConceptIndex);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newConceptIndex);

        // Validate parent concept updated
        assertParentConcept(conceptUrn, newConceptParentUrn);

        // Validate reordered concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 4);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);

        // Validate child concept parent not updated
        assertParentConcept(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptUrn);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    @Override
    public void testUpdateConceptInOrder() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        Integer newOrderValue = 0;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Update the order value of the concept
        conceptsService.updateConceptInOrder(getServiceContextAdministrador(), conceptUrn, conceptSchemeUrn, newOrderValue);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newOrderValue);

        // Validate reordered concepts at same level
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptInOrderUpThirdToSecondPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        Integer newOrderValue = 1;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Update the order value of the concept
        conceptsService.updateConceptInOrder(getServiceContextAdministrador(), conceptUrn, conceptSchemeUrn, newOrderValue);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newOrderValue);

        // Validate reordered concepts at same level
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptInOrderFirstDownToLastPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
        Integer newOrderValue = 3;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Update the order value of the concept
        conceptsService.updateConceptInOrder(getServiceContextAdministrador(), conceptUrn, conceptSchemeUrn, newOrderValue);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newOrderValue);

        // Validate reordered concepts at same level
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 2);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptInOrderSecondDownToThirdPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_2;
        Integer newOrderValue = 2;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Update the order value of the concept
        conceptsService.updateConceptInOrder(getServiceContextAdministrador(), conceptUrn, conceptSchemeUrn, newOrderValue);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newOrderValue);

        // Validate reordered concepts at same level
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptInOrderLastUpToFirstPosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_4;
        Integer newOrderValue = 0;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Update the order value of the concept
        conceptsService.updateConceptInOrder(getServiceContextAdministrador(), conceptUrn, conceptSchemeUrn, newOrderValue);
        entityManager.clear();

        // Validate concept order value updated
        assertConceptOrderValue(conceptUrn, newOrderValue);

        // Validate reordered concepts at same level
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 3);

        // Validate concept scheme updated
        assertConceptSchemeItemSchemeLastUpdated(previousResourceLastUpdated, getConceptResourceLastUpdated(conceptSchemeUrn));
    }

    @Test
    public void testUpdateConceptInOrderMoveToTheSamePosition() throws Exception {
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_3;
        Integer newOrderValue = 2;

        DateTime previousResourceLastUpdated = getConceptResourceLastUpdated(conceptSchemeUrn);

        // Validate the previous order value of the concepts
        validateOrderValuePreviousConcepts();

        // Try to update the order value of the concept with the current value
        conceptsService.updateConceptInOrder(getServiceContextAdministrador(), conceptUrn, conceptSchemeUrn, newOrderValue);
        entityManager.clear();

        // Validate that nothing has happens: the order value of the concepts has not been modified
        validateOrderValuePreviousConcepts();

        // Validate concept scheme not updated
        assertTrue(previousResourceLastUpdated.isEqual(getConceptResourceLastUpdated(conceptSchemeUrn)));
    }

    private void assertParentConcept(String conceptUrn, String expectedNewConceptParentUrn) throws MetamacException {
        ConceptMetamac conceptMetamac = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptUrn);
        String actualNewConceptParentUrn = conceptMetamac.getParent() != null ? conceptMetamac.getParent().getNameableArtefact().getUrn() : null;
        assertTrue(StringUtils.equals(expectedNewConceptParentUrn, actualNewConceptParentUrn));
    }

    private DateTime getConceptResourceLastUpdated(String conceptSchemeUrn) throws MetamacException {
        ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeUrn);
        return conceptSchemeVersionMetamac.getItemScheme().getResourceLastUpdated();
    }

    private void validateOrderValuePreviousConcepts() throws MetamacException {
        // Validate previous concepts
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2, 1);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_3, 2);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4, 3);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, 0);
        assertConceptOrderValue(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, 0);
    }

    private void assertConceptOrderValue(String conceptUrn, int expectedOrderValue) throws MetamacException {
        ConceptMetamac conceptMetamac = conceptsService.retrieveConceptByUrn(getServiceContextAdministrador(), conceptUrn);
        assertNotNull(conceptMetamac.getOrderValue());
        assertEquals(expectedOrderValue, conceptMetamac.getOrderValue().intValue());
    }

    private void assertConceptSchemeItemSchemeLastUpdated(DateTime previousResourceLastUpdated, DateTime currentResourceLastUpdated) {
        assertNotNull(previousResourceLastUpdated);
        assertTrue(previousResourceLastUpdated.isBefore(currentResourceLastUpdated));
    }

    @Test
    @Override
    public void testRetrieveConceptsByConceptSchemeUrnOrderedInDepth() throws Exception {
        // Retrieve
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;

        List<ItemResult> concepts = conceptsService.retrieveConceptsByConceptSchemeUrnOrderedInDepth(getServiceContextAdministrador(), conceptSchemeUrn, ItemMetamacResultSelection.API);

        // Validate
        assertEquals(8, concepts.size());

        assertEquals("CONCEPT01", concepts.get(0).getCode());
        assertEquals("CONCEPT02", concepts.get(1).getCode());
        assertEquals("CONCEPT0201", concepts.get(2).getCode());
        assertEquals("CONCEPT020101", concepts.get(3).getCode());
        assertEquals("CONCEPT03", concepts.get(4).getCode());
        assertEquals("CONCEPT04", concepts.get(5).getCode());
        assertEquals("CONCEPT0401", concepts.get(6).getCode());
        assertEquals("CONCEPT040101", concepts.get(7).getCode());

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

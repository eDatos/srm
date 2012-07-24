package org.siemac.metamac.srm.core.concept.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.domain.concept.enums.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.domain.srm.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.base.serviceapi.utils.BaseAsserts;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class ConceptsServiceTest extends SrmBaseTest implements ConceptsServiceTestBase {

    @Autowired
    private ConceptsService conceptsService;

    private String          NOT_EXISTS          = "-1";

    // Concept schemes
    private String          CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";
    private String          CONCEPT_SCHEME_1_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(02.000)";
    private String          CONCEPT_SCHEME_2_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME02(01.000)";
    private String          CONCEPT_SCHEME_3_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(01.000)";

    @Test
    public void testCreateConceptScheme() throws Exception {

        // Create
        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        ConceptSchemeVersion conceptSchemeVersionCreated = conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);

        // Validate
        assertNotNull(conceptSchemeVersionCreated);
        assertNotNull(conceptSchemeVersionCreated.getUuid());
        assertNotNull(conceptSchemeVersionCreated.getId());

        ConceptSchemeVersion conceptSchemeVersionRetrieved = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionCreated.getMaintainableArtefact().getUrn());
        assertEquals(MaintainableArtefactProcStatusEnum.DRAFT, conceptSchemeVersionRetrieved.getMaintainableArtefact().getProcStatus());
        assertEquals("01.000", conceptSchemeVersionRetrieved.getMaintainableArtefact().getVersionLogic());
        assertEquals(GeneratorUrnUtils.generateSdmxConceptSchemeUrn(conceptSchemeVersion.getMaintainableArtefact().getMaintainer().getCode(), conceptSchemeVersion.getMaintainableArtefact()
                .getCode(), "01.000"), conceptSchemeVersionRetrieved.getMaintainableArtefact().getUrn());
        assertNull(conceptSchemeVersionRetrieved.getMaintainableArtefact().getValidFrom());
        assertNull(conceptSchemeVersionRetrieved.getMaintainableArtefact().getValidTo());
        assertTrue(conceptSchemeVersionRetrieved.getMaintainableArtefact().getIsLastVersion());
        assertFalse(conceptSchemeVersionRetrieved.getMaintainableArtefact().getFinalLogic());
        assertNull(conceptSchemeVersionRetrieved.getMaintainableArtefact().getReplacedBy());
        assertNull(conceptSchemeVersionRetrieved.getMaintainableArtefact().getReplaceTo());
        // TODO uri?
        ConceptsAsserts.assertEqualsConceptScheme(conceptSchemeVersion, conceptSchemeVersionRetrieved);

        // Validate audit TODO
        // assertEquals(getServiceContextAdministrador().getUserId(), conceptSchemeVersionRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionRetrieved.getCreatedDate().toDate()));
        // assertEquals(getServiceContextAdministrador().getUserId(), conceptSchemeVersionRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionRetrieved.getLastUpdated().toDate()));
    }

    @Test
    public void testCreateConceptSchemeSameCodeAnotherMantainer() throws Exception {

        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        conceptSchemeVersion.getMaintainableArtefact().setCode("CONCEPTSCHEME01");

        // Create
        ConceptSchemeVersion conceptSchemeVersionCreated = conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);

        // Validate
        ConceptSchemeVersion conceptSchemeVersionRetrieved = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeVersionCreated.getMaintainableArtefact().getUrn());
        ConceptsAsserts.assertEqualsConceptScheme(conceptSchemeVersion, conceptSchemeVersionRetrieved);
    }

    @Test
    public void testCreateConceptSchemeErrorMetadatasRequired() throws Exception {
        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        conceptSchemeVersion.setType(null);
        conceptSchemeVersion.setRelatedOperation(null); // avoid unexpected
        conceptSchemeVersion.getMaintainableArtefact().setCode(null);
        conceptSchemeVersion.getMaintainableArtefact().setName(null);

        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("metadatas required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateConceptSchemeErrorMetadataUnexpected() throws Exception {

        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
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
    public void testCreateConceptSchemeErrorCodeDuplicated() throws Exception {

        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        conceptSchemeVersion.getMaintainableArtefact().setCode("CONCEPTSCHEME01");
        conceptSchemeVersion.getMaintainableArtefact().getMaintainer().setUrn("urn:sdmx:org.sdmx.infomodel.base.Agency=ISTAC:STANDALONE(01.000).ISTAC");

        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(conceptSchemeVersion.getMaintainableArtefact().getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(conceptSchemeVersion.getMaintainableArtefact().getMaintainer().getUrn(), e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testCreateConceptSchemeErrorCodeDuplicatedInsensitive() throws Exception {

        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        conceptSchemeVersion.getMaintainableArtefact().setCode("conceptscheme01");
        conceptSchemeVersion.getMaintainableArtefact().getMaintainer().setUrn("urn:sdmx:org.sdmx.infomodel.base.Agency=ISTAC:STANDALONE(01.000).ISTAC");

        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(conceptSchemeVersion.getMaintainableArtefact().getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(conceptSchemeVersion.getMaintainableArtefact().getMaintainer().getUrn(), e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }
    @Test
    public void testCreateConceptSchemeErrorCodeIncorrect() throws Exception {

        ConceptSchemeVersion conceptSchemeVersion = ConceptsDoMocks.createConceptScheme();
        conceptSchemeVersion.getMaintainableArtefact().setCode("A*b-?");

        // Create
        try {
            conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
            fail("code incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // TODO revisar
    @Test
    @Override
    public void testFindConceptSchemeByUrn() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_1_V1;
        ConceptSchemeVersion conceptSchemeVersion = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals("conceptScheme-1-v1", conceptSchemeVersion.getUuid());
        MaintainableArtefact maintainableArtefact = conceptSchemeVersion.getMaintainableArtefact();
        assertEquals("CONCEPTSCHEME01", maintainableArtefact.getCode());
        assertEquals(urn, maintainableArtefact.getUrn());
        assertEquals("02.000", maintainableArtefact.getReplacedBy());

        assertEquals("ISTAC", maintainableArtefact.getMaintainer().getCode());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=ISTAC:STANDALONE(01.000).ISTAC", maintainableArtefact.getMaintainer().getUrn());
        assertEquals("http://data.siemac.org/srm/v1/agenciesSchemes/standalone/agencies/ISTAC", maintainableArtefact.getMaintainer().getUri());
        assertEquals(TypeExternalArtefactsEnum.AGENCY, maintainableArtefact.getMaintainer().getType());
        assertEquals(null, maintainableArtefact.getMaintainer().getTitle());
        assertEquals(null, maintainableArtefact.getMaintainer().getManagementAppUrl());
        assertEquals(Long.valueOf(1), maintainableArtefact.getMaintainer().getVersion());

        BaseAsserts.assertEqualsInternationalString(maintainableArtefact.getName(), "es", "Nombre conceptScheme-1-v1", "en", "Name conceptScheme-1-v1");
        BaseAsserts.assertEqualsInternationalString(maintainableArtefact.getDescription(), "es", "Descripción conceptScheme-1-v1", "en", "Description conceptScheme-1-v1");

        assertEquals("user1", conceptSchemeVersion.getCreatedBy());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeVersion.getCreatedDate());
        assertEquals("user2", conceptSchemeVersion.getLastUpdatedBy());
        MetamacAsserts.assertEqualsDate("2011-01-22 01:02:03", conceptSchemeVersion.getLastUpdated());
    }

    @Test
    public void testFindConceptSchemeByUrnErrorParameterRequired() throws Exception {
        String urn = null;
        try {
            conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.URN, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindConceptSchemeByUrnErrorNotFound() throws Exception {
        String urn = NOT_EXISTS;
        try {
            conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("not found");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // TODO revisar
    @Test
    @Override
    public void testRetrieveConceptSchemeHistoric() throws Exception {

        // Retrieve all versions
        String urn = CONCEPT_SCHEME_1_V1;
        List<ConceptSchemeVersion> conceptSchemeVersions = conceptsService.retrieveConceptSchemeHistoric(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, conceptSchemeVersions.size());
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersions.get(1).getMaintainableArtefact().getUrn());

    }

    // TODO revisar
    @Test
    @Override
    public void testFindConceptSchemeByCondition() throws Exception {

        // Find
        List<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<ConceptSchemeVersion> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemeByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(3, conceptSchemeVersionPagedResult.getTotalRows());

        assertResultContainsConceptScheme(conceptSchemeVersionPagedResult, CONCEPT_SCHEME_1_V2);
        assertResultContainsConceptScheme(conceptSchemeVersionPagedResult, CONCEPT_SCHEME_2_V1);
        assertResultContainsConceptScheme(conceptSchemeVersionPagedResult, CONCEPT_SCHEME_3_V1);
    }

    @Test
    public void testDeleteConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;

        // Delete concept scheme only with version in draft
        conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("ConceptScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteConceptSchemeWithPublishedVersion() throws Exception {

        String urnV1 = CONCEPT_SCHEME_1_V1;
        String urnV2 = CONCEPT_SCHEME_1_V2;

        ConceptSchemeVersion conceptSchemeVersionV1 = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertFalse(conceptSchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        ConceptSchemeVersion conceptSchemeVersionV2 = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urnV2);
        assertTrue(conceptSchemeVersionV2.getMaintainableArtefact().getIsLastVersion());
        assertEquals("02.000", conceptSchemeVersionV1.getMaintainableArtefact().getReplacedBy());

        conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urnV2);
            fail("ConceptScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        conceptSchemeVersionV1 = conceptsService.findConceptSchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(conceptSchemeVersionV2.getItemScheme().getUuid(), conceptSchemeVersionV1.getItemScheme().getUuid());
        assertTrue(conceptSchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(conceptSchemeVersionV1.getMaintainableArtefact().getReplacedBy());
    }

    @Test
    public void testDeleteConceptSchemeErrorVersionPublished() throws Exception {

        String urn = CONCEPT_SCHEME_1_V1;

        // Validation
        try {
            conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme is not in draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testDeleteConceptSchemeErrorPublished() throws Exception {

        String urn = CONCEPT_SCHEME_3_V1;

        // Validation
        try {
            conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testDeleteConceptSchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;

        // Validation
        try {
            conceptsService.deleteConceptScheme(getServiceContextAdministrador(), urn);
            fail("ConceptScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    private void assertResultContainsConceptScheme(PagedResult<ConceptSchemeVersion> conceptSchemeVersionPagedResult, String urn) {
        for (ConceptSchemeVersion conceptSchemeVersion : conceptSchemeVersionPagedResult.getValues()) {
            if (conceptSchemeVersion.getMaintainableArtefact().getUrn().equals(urn)) {
                return;
            }
        }
        fail("Result does not contain conceptScheme with urn " + urn);
    }
}

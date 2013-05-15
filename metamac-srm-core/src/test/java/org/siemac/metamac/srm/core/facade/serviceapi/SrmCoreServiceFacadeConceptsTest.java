package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsInternationalStringDto;
import static org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts.assertEqualsConceptDto;
import static org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts.assertEqualsConceptSchemeMetamacDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDtoMocks;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeConceptsTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // IMPORTANT: Metadata transformation is tested in Do2Dto tests. In Concepts this code was done before we decided to do Do2Dto tests and we didnt want remove it

    // ---------------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveConceptSchemeByUrn() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V1);
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());

        assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());

        assertFalse(conceptSchemeMetamacDto.getIsPartial());

        assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
        assertNull(conceptSchemeMetamacDto.getValidFrom());
        assertNull(conceptSchemeMetamacDto.getValidTo());
        assertTrue(conceptSchemeMetamacDto.getFinalLogic());
        assertFalse(conceptSchemeMetamacDto.getIsExternalReference());
        assertEquals("http://structureUrl1", conceptSchemeMetamacDto.getStructureURL());
        assertEquals("http://serviceUrl1", conceptSchemeMetamacDto.getServiceURL());

        assertEquals(AGENCY_ROOT_1_V1_CODE, conceptSchemeMetamacDto.getMaintainer().getCode());
        assertEquals(AGENCY_ROOT_1_V1, conceptSchemeMetamacDto.getMaintainer().getUrn());

        assertEqualsInternationalStringDto(conceptSchemeMetamacDto.getName(), "es", "Nombre conceptScheme-1-v1", "en", "Name conceptScheme-1-v1");
        assertEqualsInternationalStringDto(conceptSchemeMetamacDto.getDescription(), "es", "Descripción conceptScheme-1-v1", "en", "Description conceptScheme-1-v1");

        assertEquals(ConceptSchemeTypeEnum.TRANSVERSAL, conceptSchemeMetamacDto.getType());
        assertNull(conceptSchemeMetamacDto.getRelatedOperation());

        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeMetamacDto.getLifeCycle().getProductionValidationDate());
        assertEquals("user1", conceptSchemeMetamacDto.getLifeCycle().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", conceptSchemeMetamacDto.getLifeCycle().getDiffusionValidationDate());
        assertEquals("user2", conceptSchemeMetamacDto.getLifeCycle().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", conceptSchemeMetamacDto.getLifeCycle().getInternalPublicationDate());
        assertEquals("user3", conceptSchemeMetamacDto.getLifeCycle().getInternalPublicationUser());
        assertNull(conceptSchemeMetamacDto.getLifeCycle().getExternalPublicationDate());
        assertNull(conceptSchemeMetamacDto.getLifeCycle().getExternalPublicationUser());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnWithRelatedOperation() throws Exception {
        String urn = CONCEPT_SCHEME_8_V1;

        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(urn, conceptSchemeMetamacDto.getUrn());

        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeMetamacDto.getType());
        assertEquals("op1", conceptSchemeMetamacDto.getRelatedOperation().getCode());
        assertEquals("apis.metamac.org/statistical-operations-internal/operations/op1", conceptSchemeMetamacDto.getRelatedOperation().getUri());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=op1", conceptSchemeMetamacDto.getRelatedOperation().getUrn());
        assertEquals("http://localhost:8080/metamac-statistical-operations-web/#operations;id=op1", conceptSchemeMetamacDto.getRelatedOperation().getManagementAppUrl());
        assertEquals(1, conceptSchemeMetamacDto.getRelatedOperation().getVersion().longValue());
    }

    @Test
    public void testRetrieveConceptSchemeVersions() throws Exception {
        // Retrieve all versions
        String urn = CONCEPT_SCHEME_1_V1;
        List<ConceptSchemeMetamacBasicDto> conceptSchemeMetamacDtos = srmCoreServiceFacade.retrieveConceptSchemeVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, conceptSchemeMetamacDtos.size());
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDtos.get(0).getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDtos.get(1).getUrn());
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = ConceptsMetamacDtoMocks.mockConceptSchemeDtoGlossaryType(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(ctx, conceptSchemeDto);
        assertEquals(ctx.getUserId(), conceptSchemeMetamacCreated.getCreatedBy());

        // Identifiers
        assertNotNull(conceptSchemeMetamacCreated);
        assertEquals(conceptSchemeDto.getCode(), conceptSchemeMetamacCreated.getCode());
        assertNull(conceptSchemeMetamacCreated.getUriProvider());
        assertNotNull(conceptSchemeMetamacCreated.getUrn());
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());
        assertEqualsInternationalStringDto(conceptSchemeDto.getName(), conceptSchemeMetamacCreated.getName());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=" + conceptSchemeDto.getMaintainer().getCode() + ":" + conceptSchemeDto.getCode() + "(01.000)",
                conceptSchemeMetamacCreated.getUrn());

        // Content descriptors
        assertEqualsInternationalStringDto(conceptSchemeDto.getDescription(), conceptSchemeMetamacCreated.getDescription());
        assertEquals(conceptSchemeDto.getIsPartial(), conceptSchemeMetamacCreated.getIsPartial());

        // Class descriptors
        assertEquals(conceptSchemeDto.getType(), conceptSchemeMetamacCreated.getType());
        assertNull(conceptSchemeMetamacCreated.getRelatedOperation());

        // Production descriptors
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getLifeCycle().getProcStatus());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getProductionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getProductionValidationUser());

        // Diffusion descriptors
        assertNull(conceptSchemeMetamacCreated.getValidFrom());
        assertNull(conceptSchemeMetamacCreated.getValidTo());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getDiffusionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getDiffusionValidationUser());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getInternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getInternalPublicationUser());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getExternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getExternalPublicationUser());

        // Other
        assertEquals(Long.valueOf(0), conceptSchemeMetamacCreated.getVersionOptimisticLocking());
    }

    @Test
    public void testCreateConceptSchemeOperationType() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = ConceptsMetamacDtoMocks.mockConceptSchemeDtoOperationType(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);

        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), conceptSchemeDto);

        assertEqualsConceptSchemeMetamacDto(conceptSchemeDto, conceptSchemeMetamacCreated);

        // Identifiers
        assertNotNull(conceptSchemeMetamacCreated);
        assertNull(conceptSchemeMetamacCreated.getUriProvider());
        assertNotNull(conceptSchemeMetamacCreated.getUrn());
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());

        // Content descriptors

        // Class descriptors
        assertNotNull(conceptSchemeMetamacCreated.getRelatedOperation());

        // Production descriptors
        assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getLifeCycle().getProcStatus());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getProductionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getProductionValidationUser());

        // Diffusion descriptors
        assertNull(conceptSchemeMetamacCreated.getValidFrom());
        assertNull(conceptSchemeMetamacCreated.getValidTo());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getDiffusionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getDiffusionValidationUser());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getInternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getInternalPublicationUser());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getExternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getLifeCycle().getExternalPublicationUser());
    }

    @Test
    public void testUpdateConceptScheme() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_9_V1);

        conceptSchemeMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        conceptSchemeMetamacDto.setDescription(MetamacMocks.mockInternationalStringDto());
        conceptSchemeMetamacDto.setIsPartial(Boolean.FALSE);
        conceptSchemeMetamacDto.setType(ConceptSchemeTypeEnum.ROLE);

        ConceptSchemeMetamacDto conceptSchemeMetamacDtoUpdated = srmCoreServiceFacade.updateConceptScheme(ctx, conceptSchemeMetamacDto);

        assertNotNull(conceptSchemeMetamacDto);
        assertEqualsConceptSchemeMetamacDto(conceptSchemeMetamacDto, conceptSchemeMetamacDtoUpdated);
        assertTrue(conceptSchemeMetamacDtoUpdated.getVersionOptimisticLocking() > conceptSchemeMetamacDto.getVersionOptimisticLocking());
        assertEquals("user9", conceptSchemeMetamacDtoUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), conceptSchemeMetamacDtoUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateConceptSchemeCode() throws Exception {
        String code = "code-" + MetamacMocks.mockString(10);
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1);
        conceptSchemeMetamacDto.setCode(code);

        conceptSchemeMetamacDto = srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDto);

        // Validate
        assertEquals(code, conceptSchemeMetamacDto.getCode());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=" + conceptSchemeMetamacDto.getMaintainer().getCode() + ":" + code + "(01.000)", conceptSchemeMetamacDto.getUrn());
    }

    @Test
    public void testUpdateConceptSchemeErrorOptimisticLocking() throws Exception {

        String urn = CONCEPT_SCHEME_9_V1;

        ConceptSchemeMetamacDto conceptSchemeMetamacDtoSession1 = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), conceptSchemeMetamacDtoSession1.getVersionOptimisticLocking());
        conceptSchemeMetamacDtoSession1.setIsPartial(Boolean.FALSE);

        ConceptSchemeMetamacDto conceptSchemeMetamacDtoSession2 = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), conceptSchemeMetamacDtoSession2.getVersionOptimisticLocking());
        conceptSchemeMetamacDtoSession2.setIsPartial(Boolean.FALSE);

        // Update by session 1
        ConceptSchemeMetamacDto conceptSchemeMetamacDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDtoSession1);
        assertTrue(conceptSchemeMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking() > conceptSchemeMetamacDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        conceptSchemeMetamacDtoSession1AfterUpdate1.setIsPartial(Boolean.FALSE);
        ConceptSchemeMetamacDto conceptSchemeMetamacDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDtoSession1AfterUpdate1);
        assertTrue(conceptSchemeMetamacDtoSession1AfterUpdate2.getVersionOptimisticLocking() > conceptSchemeMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteConceptScheme() throws Exception {
        String urn = CONCEPT_SCHEME_2_V1;

        // Delete concept scheme only with version in draft
        srmCoreServiceFacade.deleteConceptScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("ConceptScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindConceptSchemesByCondition() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(18, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_5_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_6_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_8_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_9_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V3, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_11_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_12_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_14_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        // Find by Name
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "Nombre conceptScheme-1-v1", OperationType.EQ));

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }

        // Find by external publication date > X
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), new DateTime(2011, 04, 3, 1, 1, 1,
                    1).toDate(), OperationType.GT));

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(3, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V1, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_12_V1, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeMetamacDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }

        // Find by external publication date < X
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), new DateTime(2012, 01, 1, 1, 1, 1,
                    1).toDate(), OperationType.LT));

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeMetamacDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }

        // Find by internal publication user
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_USER.name(), "user3", OperationType.EQ));

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(5, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V1, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeMetamacDto.getUrn());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_12_V1, conceptSchemeMetamacDto.getUrn());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesByConditionWithConceptsCanBeRole() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesByConditionWithConceptsCanBeRole(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        {
            RelatedResourceDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_13_V1, conceptSchemeMetamacDto.getUrn());
        }
        assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
    }

    @Test
    public void testFindConceptSchemesByConditionWithConceptsCanBeExtended() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesByConditionWithConceptsCanBeExtended(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        {
            RelatedResourceDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeMetamacDto.getUrn());
        }
        assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
    }

    @Test
    public void testFindConceptSchemesByProcStatus() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), ProcStatusEnum.DRAFT,
                    OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_8_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_9_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindConceptSchemesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaOrderEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        // Pagination
        int maxResultSize = 2;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CONCEPT_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(CONCEPT_SCHEME_2_V1, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, conceptSchemeDto.getLifeCycle().getProcStatus());
        }

        // Sends to production validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getProductionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getProductionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
        }

        // Sends to production validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
        }

        // Rejects validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
        }

        // Rejects validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        }
    }

    @Test
    public void testPublishInternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getLifeCycle().getProcStatus());
        }

        // Publish internally
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.publishConceptSchemeInternally(ctx, urn, Boolean.FALSE);

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(conceptSchemeDto.getFinalLogic());
        }
        // Validate retrieving
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationDate());
            assertNull(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(conceptSchemeDto.getFinalLogic());
        }
    }

    @Test
    public void testPublishExternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNull(conceptSchemeDto.getValidFrom());
            assertNull(conceptSchemeDto.getValidTo());
        }

        // Publish externally
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.publishConceptSchemeExternally(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNotNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getValidFrom()));
            assertNull(conceptSchemeDto.getValidTo());
        }
        // Validate retrieving
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeDto.getLifeCycle().getProcStatus());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getDiffusionValidationUser());
            assertNotNull(conceptSchemeDto.getLifeCycle().getInternalPublicationDate());
            assertNotNull(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getLifeCycle().getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getValidFrom()));
            assertNull(conceptSchemeDto.getValidTo());
        }
    }

    @Test
    public void testVersioningConceptScheme() throws Exception {
        // Versioning
        String urn = CONCEPT_SCHEME_3_V1;
        TaskInfo versioningResult = srmCoreServiceFacade.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME03(02.000)", versioningResult.getUrnResult());
        assertEquals(null, versioningResult.getIsPlannedInBackground());
        assertEquals(null, versioningResult.getJobKey());
    }

    @Test
    public void testCopyConceptScheme() throws Exception {
        // Copy
        String urn = CONCEPT_SCHEME_14_V1;
        TaskInfo copyResult = srmCoreServiceFacade.copyConceptScheme(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME14(01.000)", copyResult.getUrnResult());
        assertEquals(null, copyResult.getIsPlannedInBackground());
        assertEquals(null, copyResult.getJobKey());
    }

    @Test
    public void testEndConceptSchemeValidity() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.endConceptSchemeValidity(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1);

        assertNotNull(conceptSchemeMetamacDto);
        assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeMetamacDto.getValidTo()));
    }

    @Test
    public void testDeleteConcept() throws Exception {

        String urn = CONCEPT_SCHEME_1_V2_CONCEPT_3;

        // Delete concept
        srmCoreServiceFacade.deleteConcept(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveConceptByUrn(getServiceContextAdministrador(), urn);
            fail("Concept deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    @Test
    public void testRetrieveConceptsByConceptSchemeUrn() throws Exception {
        // Do not test because facade operation has same signature as service operation (without dto)
    }

    // ---------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveConceptByUrn() throws Exception {
        ConceptMetamacDto conceptMetamacDto = srmCoreServiceFacade.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

        assertNotNull(conceptMetamacDto);
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptMetamacDto.getUrn());

        assertEqualsInternationalStringDto(conceptMetamacDto.getName(), "es", "Nombre conceptScheme-1-v2-concept-1", "en", "Name conceptScheme-1-v2-concept-1");
        assertEqualsInternationalStringDto(conceptMetamacDto.getDescription(), "es", "Descripción conceptScheme-1-v2-concept-1", null, null);

        assertEqualsInternationalStringDto(conceptMetamacDto.getPluralName(), "es", "PluralName conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getAcronym(), "es", "Acrónimo conceptScheme-1-v2-concept-1", "en", "Acronym conceptScheme-1-v2-concept-1");
        assertEqualsInternationalStringDto(conceptMetamacDto.getDescriptionSource(), "es", "DescriptionSource conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getContext(), "es", "Context conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getDocMethod(), "es", "DocMethod conceptScheme-1-v2-concept-1", null, null);
        assertEquals(ConceptRoleEnum.ATTRIBUTE, conceptMetamacDto.getSdmxRelatedArtefact());
        assertEquals("DIRECT", conceptMetamacDto.getConceptType().getIdentifier());
        assertEqualsInternationalStringDto(conceptMetamacDto.getDerivation(), "es", "Derivation conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getLegalActs(), "es", "LegalActs conceptScheme-1-v2-concept-1", null, null);
        assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptMetamacDto.getConceptExtends().getUrn());
        assertEquals(VARIABLE_1, conceptMetamacDto.getVariable().getUrn());

        // Core representation
        assertEquals(RepresentationTypeEnum.ENUMERATION, conceptMetamacDto.getCoreRepresentation().getRepresentationType());
        assertEquals("CODELIST07", conceptMetamacDto.getCoreRepresentation().getEnumeration().getCode());
        assertEqualsInternationalStringDto(conceptMetamacDto.getCoreRepresentation().getEnumeration().getTitle(), "es", "Comentario codelist-7-v1", "en", "Comment codelist-7-v1");
        assertEquals(RelatedResourceTypeEnum.CODELIST, conceptMetamacDto.getCoreRepresentation().getEnumeration().getType());
        assertEquals(CODELIST_7_V1, conceptMetamacDto.getCoreRepresentation().getEnumeration().getUrn());
    }

    @Test
    public void testFindConceptsByCondition() throws Exception {

        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            MetamacCriteriaOrder orderConceptSchemeUrn = new MetamacCriteriaOrder();
            orderConceptSchemeUrn.setType(OrderTypeEnum.ASC);
            orderConceptSchemeUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.CONCEPT_SCHEME_URN.name());
            metamacCriteria.getOrdersBy().add(orderConceptSchemeUrn);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(33, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(33, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V1, conceptsPagedResult.getResults().get(0).getItemSchemeVersion().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }

        // Find only concepts in first level
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(ConceptMetamacCriteriaOrderEnum.CONCEPT_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(ConceptMetamacCriteriaOrderEnum.CONCEPT_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
            propertyRestriction.setPropertyName(ConceptMetamacCriteriaPropertyEnum.CONCEPT_PARENT_URN.name());
            propertyRestriction.setOperationType(OperationType.IS_NULL);
            metamacCriteria.setRestriction(propertyRestriction);

            // Find
            MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(25, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(25, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_14_V1_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }

        // Find by concept scheme type
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            MetamacCriteriaOrder orderConceptSchemeUrn = new MetamacCriteriaOrder();
            orderConceptSchemeUrn.setType(OrderTypeEnum.ASC);
            orderConceptSchemeUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.CONCEPT_SCHEME_URN.name());
            metamacCriteria.getOrdersBy().add(orderConceptSchemeUrn);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_TYPE.name(), ConceptSchemeTypeEnum.ROLE, OperationType.EQ));

            // Find
            MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(11, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(11, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }

        // Find by name (like), code (like) and concept scheme urn
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), "Nombre conceptScheme-1-v2-concept-2-", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), "CONCEPT02", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_1_V2, OperationType.EQ));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }

        // Find by concept scheme urn paginated
        {

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), CONCEPT_SCHEME_1_V2, OperationType.EQ));

            // First page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(0);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, conceptsPagedResult.getResults().size());

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(conceptsPagedResult.getResults().size(), i);
            }
            // Second page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(3);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, conceptsPagedResult.getResults().size());

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(conceptsPagedResult.getResults().size(), i);
            }
            // Third page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(6);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<ConceptMetamacBasicDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(2, conceptsPagedResult.getResults().size());

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(conceptsPagedResult.getResults().size(), i);
            }
        }
    }

    @Test
    public void testFindConceptsCanBeRoleByCondition() throws Exception {

        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            MetamacCriteriaOrder orderConceptSchemeUrn = new MetamacCriteriaOrder();
            orderConceptSchemeUrn.setType(OrderTypeEnum.ASC);
            orderConceptSchemeUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.CONCEPT_SCHEME_URN.name());
            metamacCriteria.getOrdersBy().add(orderConceptSchemeUrn);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<RelatedResourceDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(3, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(3, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_3, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }

        // Find by code (like)
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), "CONCEPT02", OperationType.LIKE));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<RelatedResourceDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsCanBeRoleByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_13_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }
    }

    @Test
    public void testFindConceptsCanBeExtendedByCondition() throws Exception {

        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            MetamacCriteriaOrder orderConceptSchemeUrn = new MetamacCriteriaOrder();
            orderConceptSchemeUrn.setType(OrderTypeEnum.ASC);
            orderConceptSchemeUrn.setPropertyName(ConceptMetamacCriteriaOrderEnum.CONCEPT_SCHEME_URN.name());
            metamacCriteria.getOrdersBy().add(orderConceptSchemeUrn);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<RelatedResourceDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsCanBeExtendedByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, conceptsPagedResult.getResults().size());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_7_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(conceptsPagedResult.getResults().size(), i);
        }
    }

    @Test
    public void testCreateConcept() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.TEXT_FORMAT);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);
        conceptMetamacDto.setConceptExtends(ConceptsMetamacDtoMocks.mockConceptRelatedResourceDto("CONCEPT01", CONCEPT_SCHEME_7_V1_CONCEPT_1));
        conceptMetamacDto.setVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_01", VARIABLE_1));

        ConceptMetamacDto conceptMetamacDtoCreated = srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000)." + conceptMetamacDto.getCode(), conceptMetamacDtoCreated.getUrn());
        assertNull(conceptMetamacDtoCreated.getUriProvider());

        assertEqualsConceptDto(conceptMetamacDto, conceptMetamacDtoCreated);
    }

    @Test
    public void testCreateConceptRepresentationNotEnumerated() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.TEXT_FORMAT);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);

        ConceptMetamacDto conceptMetamacDtoCreated = srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME01(02.000)." + conceptMetamacDto.getCode(), conceptMetamacDtoCreated.getUrn());
        assertNull(conceptMetamacDtoCreated.getUriProvider());

        assertEqualsConceptDto(conceptMetamacDto, conceptMetamacDtoCreated);
    }

    @Test
    public void testCreateConceptWithConceptParent() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.TEXT_FORMAT);
        conceptMetamacDto.setItemParentUrn(CONCEPT_SCHEME_1_V2_CONCEPT_1);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);

        ConceptMetamacDto conceptMetamacDtoCreated = srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptMetamacDtoCreated.getItemParentUrn());
        assertEqualsConceptDto(conceptMetamacDto, conceptMetamacDtoCreated);
    }

    @Test
    public void testCreateConceptErrorParentNotExists() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.TEXT_FORMAT);
        conceptMetamacDto.setItemParentUrn(NOT_EXISTS);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);

        try {
            srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
            fail("wrong parent");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateConcept() throws Exception {
        ConceptMetamacDto conceptMetamacDto = srmCoreServiceFacade.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertNotNull(conceptMetamacDto.getConceptExtends());
        conceptMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        conceptMetamacDto.setDescription(MetamacMocks.mockInternationalStringDto());
        conceptMetamacDto.getConceptExtends().setUrn(CONCEPT_SCHEME_7_V1_CONCEPT_1);

        ConceptMetamacDto conceptMetamacDtoUpdated = srmCoreServiceFacade.updateConcept(getServiceContextAdministrador(), conceptMetamacDto);

        assertNotNull(conceptMetamacDto);
        assertEqualsConceptDto(conceptMetamacDto, conceptMetamacDtoUpdated);
        assertTrue(conceptMetamacDtoUpdated.getVersionOptimisticLocking() > conceptMetamacDto.getVersionOptimisticLocking());

        // Update again to check removing concept extends
        conceptMetamacDtoUpdated.setConceptExtends(null);
        ConceptMetamacDto conceptMetamacDtoUpdatedAgain = srmCoreServiceFacade.updateConcept(getServiceContextAdministrador(), conceptMetamacDtoUpdated);

        assertNotNull(conceptMetamacDto);
        assertEqualsConceptDto(conceptMetamacDtoUpdated, conceptMetamacDtoUpdatedAgain);
        assertTrue(conceptMetamacDtoUpdatedAgain.getVersionOptimisticLocking() > conceptMetamacDtoUpdated.getVersionOptimisticLocking());
    }

    @Test
    public void testRetrieveRelatedConcepts() throws Exception {

        {
            // Retrieve
            List<ConceptMetamacBasicDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

            // Validate
            assertEquals(3, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
        {
            // Retrieve
            List<ConceptMetamacBasicDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);

            // Validate
            assertEquals(1, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
        }
        {
            // Retrieve
            List<ConceptMetamacBasicDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_3);
        }
    }

    @Test
    public void testRetrieveRelatedConceptsRoles() throws Exception {

        // Retrieve
        List<ConceptMetamacBasicDto> relatedConcepts = srmCoreServiceFacade.retrieveRoleConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

        // Validate
        assertEquals(2, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_1);
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_13_V1_CONCEPT_3);
    }

    @Test
    public void testFindAllConceptTypes() throws Exception {

        // Find
        List<ConceptTypeDto> conceptTypes = srmCoreServiceFacade.findAllConceptTypes(getServiceContextAdministrador());

        // Validate
        assertEquals(2, conceptTypes.size());
        int i = 0;
        {
            ConceptTypeDto conceptType = conceptTypes.get(i++);
            assertEquals(CONCEPT_TYPE_DIRECT, conceptType.getIdentifier());
            assertEqualsInternationalStringDto(conceptType.getDescription(), "en", "Direct", "es", "Directo");
        }
        {
            ConceptTypeDto conceptType = conceptTypes.get(i++);
            assertEquals(CONCEPT_TYPE_DERIVED, conceptType.getIdentifier());
            assertEqualsInternationalStringDto(conceptType.getDescription(), "en", "Derived", "es", "Derivado");
        }
        assertEquals(conceptTypes.size(), i);
    }

    @Test
    public void testRetrieveConceptTypeByIdentifier() throws Exception {

        String identifier = CONCEPT_TYPE_DERIVED;

        // Retrieve
        ConceptTypeDto conceptType = srmCoreServiceFacade.retrieveConceptTypeByIdentifier(getServiceContextAdministrador(), identifier);

        // Validate
        assertEquals(identifier, conceptType.getIdentifier());
        assertEqualsInternationalStringDto(conceptType.getDescription(), "en", "Derived", "es", "Derivado");
    }

    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForConceptByCondition() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        metamacCriteria.setPaginator(buildMetamacCriteriaPaginatorNoLimitsAndCountResults());
        metamacCriteria.setOrdersBy(buildMetamacCriteriaOrderByUrn());

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_1_V2_CONCEPT_1;
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(getServiceContextAdministrador(), metamacCriteria,
                    conceptUrn);

            // Validate
            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CODELIST_7_V1, result.getResults().get(i++).getUrn());
            assertEquals(CODELIST_9_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            // Concept has Variable 2
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(getServiceContextAdministrador(), metamacCriteria,
                    conceptUrn);

            // Validate
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            assertEquals(CODELIST_8_V1, result.getResults().get(i++).getUrn());
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }

    private void assertListConceptsContainsConcept(List<ConceptMetamacBasicDto> items, String urn) {
        for (ConceptMetamacBasicDto item : items) {
            if (item.getUrn().equals(urn)) {
                return;
            }
        }
        fail("List does not contain item with urn " + urn);
    }

}
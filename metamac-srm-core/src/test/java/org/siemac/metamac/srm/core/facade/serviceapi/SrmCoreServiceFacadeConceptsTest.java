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
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDtoMocks;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeRepresentationEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeConceptsTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // ---------------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveConceptSchemeByUrn() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V1);
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
        assertEquals("CONCEPTSCHEME01", conceptSchemeMetamacDto.getCode());
        assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());

        assertFalse(conceptSchemeMetamacDto.getIsPartial());

        assertEquals("01.000", conceptSchemeMetamacDto.getVersionLogic());
        assertNull(conceptSchemeMetamacDto.getValidFrom());
        assertNull(conceptSchemeMetamacDto.getValidTo());
        assertTrue(conceptSchemeMetamacDto.getFinalLogic());
        assertFalse(conceptSchemeMetamacDto.getIsExternalReference());
        assertEquals("http://structureUrl1", conceptSchemeMetamacDto.getStructureURL());
        assertEquals("http://serviceUrl1", conceptSchemeMetamacDto.getServiceURL());

        assertEquals("ISTAC", conceptSchemeMetamacDto.getMaintainer().getCode());
        assertEquals("http://data.siemac.org/srm/v1/agenciesSchemes/standalone/agencies/ISTAC", conceptSchemeMetamacDto.getMaintainer().getUri());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=ISTAC:STANDALONE(01.000).ISTAC", conceptSchemeMetamacDto.getMaintainer().getUrn());
        assertEquals("http://manageISTAC", conceptSchemeMetamacDto.getMaintainer().getManagementAppUrl());

        assertEqualsInternationalStringDto(conceptSchemeMetamacDto.getName(), "es", "Nombre conceptScheme-1-v1", "en", "Name conceptScheme-1-v1");
        assertEqualsInternationalStringDto(conceptSchemeMetamacDto.getDescription(), "es", "Descripción conceptScheme-1-v1", "en", "Description conceptScheme-1-v1");

        assertEquals(ConceptSchemeTypeEnum.TRANSVERSAL, conceptSchemeMetamacDto.getType());
        assertNull(conceptSchemeMetamacDto.getRelatedOperation());

        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", conceptSchemeMetamacDto.getProductionValidationDate());
        assertEquals("user1", conceptSchemeMetamacDto.getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", conceptSchemeMetamacDto.getDiffusionValidationDate());
        assertEquals("user2", conceptSchemeMetamacDto.getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", conceptSchemeMetamacDto.getInternalPublicationDate());
        assertEquals("user3", conceptSchemeMetamacDto.getInternalPublicationUser());
        assertNull(conceptSchemeMetamacDto.getExternalPublicationDate());
        assertNull(conceptSchemeMetamacDto.getExternalPublicationUser());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnWithRelatedOperation() throws Exception {
        String urn = CONCEPT_SCHEME_8_V1;
        
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(urn, conceptSchemeMetamacDto.getUrn());
        
        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeMetamacDto.getType());
        assertEquals("op1", conceptSchemeMetamacDto.getRelatedOperation().getCode());
        assertEquals("http://op1", conceptSchemeMetamacDto.getRelatedOperation().getUri());
        assertEquals("urn:op1", conceptSchemeMetamacDto.getRelatedOperation().getUrn());
        assertEquals("http://app/operations", conceptSchemeMetamacDto.getRelatedOperation().getManagementAppUrl());
        assertEquals(1, conceptSchemeMetamacDto.getRelatedOperation().getVersion().longValue());        
    }

    @Test
    public void testRetrieveConceptSchemeVersions() throws Exception {
        // Retrieve all versions
        String urn = CONCEPT_SCHEME_1_V1;
        List<ConceptSchemeMetamacDto> conceptSchemeMetamacDtos = srmCoreServiceFacade.retrieveConceptSchemeVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, conceptSchemeMetamacDtos.size());
        assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDtos.get(0).getUrn());
        assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDtos.get(1).getUrn());
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = ConceptsMetamacDtoMocks.mockConceptSchemeDtoGlossaryType();

        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), conceptSchemeDto);

        // Identifiers
        assertNotNull(conceptSchemeMetamacCreated);
        assertEquals(conceptSchemeDto.getCode(), conceptSchemeMetamacCreated.getCode());
        assertNull(conceptSchemeMetamacCreated.getUri());
        assertNotNull(conceptSchemeMetamacCreated.getUrn());
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());
        assertEqualsInternationalStringDto(conceptSchemeDto.getName(), conceptSchemeMetamacCreated.getName());
        assertEquals(GeneratorUrnUtils.generateSdmxConceptSchemeUrn(conceptSchemeDto.getMaintainer().getCode(), conceptSchemeDto.getCode(), "01.000"), conceptSchemeMetamacCreated.getUrn());

        // Content descriptors
        assertEqualsInternationalStringDto(conceptSchemeDto.getDescription(), conceptSchemeMetamacCreated.getDescription());
        assertEquals(conceptSchemeDto.getIsPartial(), conceptSchemeMetamacCreated.getIsPartial());

        // Class descriptors
        assertEquals(conceptSchemeDto.getType(), conceptSchemeMetamacCreated.getType());
        assertNull(conceptSchemeMetamacCreated.getRelatedOperation());

        // Production descriptors
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getProcStatus());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationUser());

        // Diffusion descriptors
        assertNull(conceptSchemeMetamacCreated.getValidFrom());
        assertNull(conceptSchemeMetamacCreated.getValidTo());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationUser());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationUser());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationUser());
        
        // Other
        assertEquals(Long.valueOf(0), conceptSchemeMetamacCreated.getVersionOptimisticLocking());
    }

    @Test
    public void testCreateConceptSchemeOperationType() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeDto = ConceptsMetamacDtoMocks.mockConceptSchemeDtoOperationType();

        ConceptSchemeMetamacDto conceptSchemeMetamacCreated = srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), conceptSchemeDto);

        assertEqualsConceptSchemeMetamacDto(conceptSchemeDto, conceptSchemeMetamacCreated);

        // Identifiers
        assertNotNull(conceptSchemeMetamacCreated);
        assertNull(conceptSchemeMetamacCreated.getUri());
        assertNotNull(conceptSchemeMetamacCreated.getUrn());
        assertNotNull(conceptSchemeMetamacCreated.getVersionLogic());

        // Content descriptors

        // Class descriptors
        assertNotNull(conceptSchemeMetamacCreated.getRelatedOperation());

        // Production descriptors
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacCreated.getProcStatus());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getProductionValidationUser());

        // Diffusion descriptors
        assertNull(conceptSchemeMetamacCreated.getValidFrom());
        assertNull(conceptSchemeMetamacCreated.getValidTo());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationDate());
        assertNull(conceptSchemeMetamacCreated.getDiffusionValidationUser());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getInternalPublicationUser());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationDate());
        assertNull(conceptSchemeMetamacCreated.getExternalPublicationUser());
    }

    @Test
    public void testUpdateConceptScheme() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1);

        conceptSchemeMetamacDto.setName(MetamacMocks.mockInternationalString());
        conceptSchemeMetamacDto.setDescription(MetamacMocks.mockInternationalString());
        conceptSchemeMetamacDto.setIsPartial(Boolean.TRUE);
        conceptSchemeMetamacDto.setType(ConceptSchemeTypeEnum.ROLE);

        ConceptSchemeMetamacDto conceptSchemeMetamacDtoUpdated = srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDto);

        assertNotNull(conceptSchemeMetamacDto);
        assertEqualsConceptSchemeMetamacDto(conceptSchemeMetamacDto, conceptSchemeMetamacDtoUpdated);
        assertTrue(conceptSchemeMetamacDtoUpdated.getVersionOptimisticLocking() > conceptSchemeMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateConceptSchemeCode() throws Exception {
        String code = "code-" + MetamacMocks.mockString(10);
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_9_V1);
        conceptSchemeMetamacDto.setCode(code);

        conceptSchemeMetamacDto = srmCoreServiceFacade.updateConceptScheme(getServiceContextAdministrador(), conceptSchemeMetamacDto);

        String expextedUrn = GeneratorUrnUtils.generateSdmxConceptSchemeUrn(conceptSchemeMetamacDto.getMaintainer().getCode(), code, conceptSchemeMetamacDto.getVersionLogic());

        assertEquals(code, conceptSchemeMetamacDto.getCode());
        assertEquals(expextedUrn, conceptSchemeMetamacDto.getUrn());
    }
    
    @Test
    public void testUpdateConceptSchemeErrorOptimisticLocking() throws Exception {

        String urn = CONCEPT_SCHEME_9_V1;

        ConceptSchemeMetamacDto conceptSchemeMetamacDtoSession1 = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), conceptSchemeMetamacDtoSession1.getVersionOptimisticLocking());
        conceptSchemeMetamacDtoSession1.setIsPartial(Boolean.TRUE);

        ConceptSchemeMetamacDto conceptSchemeMetamacDtoSession2 = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), conceptSchemeMetamacDtoSession2.getVersionOptimisticLocking());
        conceptSchemeMetamacDtoSession2.setIsPartial(Boolean.TRUE);

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
            assertEquals(ServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
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

        MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

        assertEquals(16, result.getPaginatorResult().getTotalResults().intValue());
        int i = 0;
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_4_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_5_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_6_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_7_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_8_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_9_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_10_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_10_V3, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_11_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeMetamacDto.getProcStatus());
        }
        {
            ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
            assertEquals(CONCEPT_SCHEME_12_V1, conceptSchemeMetamacDto.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
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
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.DRAFT, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_8_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_9_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeMetamacDto.getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_7_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
            }
            {
                ConceptSchemeMetamacDto conceptSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(CONCEPT_SCHEME_10_V2, conceptSchemeMetamacDto.getUrn());
                assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeMetamacDto.getProcStatus());
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

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(CONCEPT_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<ConceptSchemeMetamacDto> result = srmCoreServiceFacade.findConceptSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

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
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeDto.getProcStatus());
        }

        // Sends to production validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getProductionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getProductionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Sends to production validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getDiffusionValidationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Rejects validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, conceptSchemeDto.getProcStatus());
            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Rejects validation
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, conceptSchemeDto.getProcStatus());
            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertNull(conceptSchemeDto.getProductionValidationDate());
            assertNull(conceptSchemeDto.getProductionValidationUser());
            assertNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNull(conceptSchemeDto.getInternalPublicationDate());
            assertNull(conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
        }
    }

    @Test
    public void testPublishInternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, conceptSchemeDto.getProcStatus());
        }

        // Publish internally
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.publishInternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
            assertTrue(conceptSchemeDto.getFinalLogic());
        }
        // Validate retrieving
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getInternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getInternalPublicationUser());
            assertNull(conceptSchemeDto.getExternalPublicationDate());
            assertNull(conceptSchemeDto.getExternalPublicationUser());
            assertTrue(conceptSchemeDto.getFinalLogic());
        }
    }

    @Test
    public void testPublishExternallyConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNull(conceptSchemeDto.getValidFrom());
            assertNull(conceptSchemeDto.getValidTo());
        }

        // Publish externally
        ConceptSchemeMetamacDto conceptSchemeDto = srmCoreServiceFacade.publishExternallyConceptScheme(ctx, urn);

        // Validate response
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNotNull(conceptSchemeDto.getInternalPublicationDate());
            assertNotNull(conceptSchemeDto.getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getExternalPublicationUser());
            assertNull(conceptSchemeDto.getIsExternalPublicationFailed());
            assertNull(conceptSchemeDto.getExternalPublicationFailedDate());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getValidFrom()));
            assertNull(conceptSchemeDto.getValidTo());
        }
        // Validate retrieving
        {
            conceptSchemeDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, urn);

            assertEquals(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, conceptSchemeDto.getProcStatus());
            assertNotNull(conceptSchemeDto.getProductionValidationDate());
            assertNotNull(conceptSchemeDto.getProductionValidationUser());
            assertNotNull(conceptSchemeDto.getDiffusionValidationDate());
            assertNotNull(conceptSchemeDto.getDiffusionValidationUser());
            assertNotNull(conceptSchemeDto.getInternalPublicationDate());
            assertNotNull(conceptSchemeDto.getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getExternalPublicationDate()));
            assertEquals(ctx.getUserId(), conceptSchemeDto.getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeDto.getValidFrom()));
            assertNull(conceptSchemeDto.getValidTo());
        }
    }

    @Test
    public void testVersioningConceptScheme() throws Exception {

        String urn = CONCEPT_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(02.000)";

        ConceptSchemeMetamacDto conceptSchemeDtoToCopy = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        ConceptSchemeMetamacDto conceptSchemeDtoNewVersion = srmCoreServiceFacade.versioningConceptScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(versionExpected, conceptSchemeDtoNewVersion.getVersionLogic());
            assertEquals(urnExpected, conceptSchemeDtoNewVersion.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeDtoNewVersion.getProcStatus());
            ConceptsMetamacAsserts.assertEqualsConceptSchemeMetamacDto(conceptSchemeDtoToCopy, conceptSchemeDtoNewVersion);
        }

        // Validate retrieving
        {
            // New version
            conceptSchemeDtoNewVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), conceptSchemeDtoNewVersion.getUrn());
            assertEquals(versionExpected, conceptSchemeDtoNewVersion.getVersionLogic());
            assertEquals(urnExpected, conceptSchemeDtoNewVersion.getUrn());
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeDtoNewVersion.getProcStatus());
            assertEquals("01.000", conceptSchemeDtoNewVersion.getReplaceTo());
            assertEquals(null, conceptSchemeDtoNewVersion.getReplacedBy());
            ConceptsMetamacAsserts.assertEqualsConceptSchemeMetamacDto(conceptSchemeDtoToCopy, conceptSchemeDtoNewVersion);

            // Copied version
            conceptSchemeDtoToCopy = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", conceptSchemeDtoToCopy.getVersionLogic());
            assertEquals(urn, conceptSchemeDtoToCopy.getUrn());
            assertEquals(null, conceptSchemeDtoToCopy.getReplaceTo());
            assertEquals(versionExpected, conceptSchemeDtoToCopy.getReplacedBy());

            // All versions
            List<ConceptSchemeMetamacDto> allVersions = srmCoreServiceFacade.retrieveConceptSchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getUrn());
            assertEquals(urnExpected, allVersions.get(1).getUrn());
        }
    }

    @Test
    public void testCancelConceptSchemeValidity() throws Exception {
        ConceptSchemeMetamacDto conceptSchemeMetamacDto = srmCoreServiceFacade.cancelConceptSchemeValidity(getServiceContextAdministrador(), CONCEPT_SCHEME_7_V1);

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
            assertEquals(ServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveConceptsByConceptSchemeUrn() throws Exception {

        // Retrieve
        String conceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        List<ItemHierarchyDto> concepts = srmCoreServiceFacade.retrieveConceptsByConceptSchemeUrn(getServiceContextAdministrador(), conceptSchemeUrn);

        // Validate
        assertEquals(4, concepts.size());
        {
            // Concept 01
            ItemHierarchyDto concept = concepts.get(0);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, concept.getItem().getUrn());
            assertEquals(0, concept.getChildren().size());
        }
        {
            // Concept 02
            ItemHierarchyDto concept = concepts.get(1);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, concept.getItem().getUrn());
            assertEquals(1, concept.getChildren().size());
            {
                // Concept 02 01
                ItemHierarchyDto conceptChild = (ItemHierarchyDto) concept.getChildren().get(0);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, conceptChild.getItem().getUrn());
                assertEquals(1, conceptChild.getChildren().size());
                {
                    // Concept 02 01 01
                    ItemHierarchyDto conceptChildChild = (ItemHierarchyDto) conceptChild.getChildren().get(0);
                    assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, conceptChildChild.getItem().getUrn());
                    assertEquals(0, conceptChildChild.getChildren().size());
                }
            }
        }
        {
            // Concept 03
            ItemHierarchyDto concept = concepts.get(2);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, concept.getItem().getUrn());
            assertEquals(0, concept.getChildren().size());
        }
        {
            // Concept 04
            ItemHierarchyDto concept = concepts.get(3);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, concept.getItem().getUrn());
            assertEquals(1, concept.getChildren().size());
            {
                // Concept 04 01
                ItemHierarchyDto conceptChild = (ItemHierarchyDto) concept.getChildren().get(0);
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptChild.getItem().getUrn());
                assertEquals(1, conceptChild.getChildren().size());
                {
                    // Concept 04 01 01
                    ItemHierarchyDto conceptChildChild = (ItemHierarchyDto) conceptChild.getChildren().get(0);
                    assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptChildChild.getItem().getUrn());
                    assertEquals(0, conceptChildChild.getChildren().size());
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveConceptByUrn() throws Exception {
        ConceptMetamacDto conceptMetamacDto = srmCoreServiceFacade.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

        assertNotNull(conceptMetamacDto);
        assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, conceptMetamacDto.getUrn());

        assertEqualsInternationalStringDto(conceptMetamacDto.getName(), "es", "Nombre conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getDescription(), "es", "Descripción conceptScheme-1-v2-concept-1", null, null);

        assertEqualsInternationalStringDto(conceptMetamacDto.getPluralName(), "es", "PluralName conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getAcronym(), "es", "Acronym conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getDescriptionSource(), "es", "DescriptionSource conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getContext(), "es", "Context conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getDocMethod(), "es", "DocMethod conceptScheme-1-v2-concept-1", null, null);
        assertEquals(ConceptRoleEnum.ATTRIBUTE, conceptMetamacDto.getSdmxRelatedArtefact());
        assertEquals("DIRECT", conceptMetamacDto.getType().getIdentifier());
        assertEqualsInternationalStringDto(conceptMetamacDto.getDerivation(), "es", "Derivation conceptScheme-1-v2-concept-1", null, null);
        assertEqualsInternationalStringDto(conceptMetamacDto.getLegalActs(), "es", "LegalActs conceptScheme-1-v2-concept-1", null, null);
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptMetamacDto.getConceptExtendsUrn());
        
        // Core representation
        assertEquals(TypeRepresentationEnum.ENUMERATED, conceptMetamacDto.getCoreRepresentation().getTypeRepresentationEnum());
        assertEquals("CODE_LIST_1", conceptMetamacDto.getCoreRepresentation().getEnumerated().getCode());
        assertEquals("http://managementCodeList1", conceptMetamacDto.getCoreRepresentation().getEnumerated().getManagementAppUrl());
        assertEqualsInternationalStringDto(conceptMetamacDto.getCoreRepresentation().getEnumerated().getTitle(), "es", "CodeList1", null, null);
        assertEquals(TypeExternalArtefactsEnum.CODELIST, conceptMetamacDto.getCoreRepresentation().getEnumerated().getType());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.CodeList.1", conceptMetamacDto.getCoreRepresentation().getEnumerated().getUrn());
        assertEquals("http://data.siemac.org/srm/v1/codeLists/1", conceptMetamacDto.getCoreRepresentation().getEnumerated().getUri());
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
            MetamacCriteriaResult<ConceptMetamacDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(25, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(25, conceptsPagedResult.getResults().size());
            assertTrue(conceptsPagedResult.getResults().get(0) instanceof ConceptMetamacDto);
            assertEquals(CONCEPT_SCHEME_1_V1, conceptsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(CONCEPT_SCHEME_1_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
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
            assertEquals(CONCEPT_SCHEME_7_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_10_V2_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_10_V3_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_11_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_12_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
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
            MetamacCriteriaResult<ConceptMetamacDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(8, conceptsPagedResult.getResults().size());
            assertTrue(conceptsPagedResult.getResults().get(0) instanceof ConceptMetamacDto);

            int i = 0;
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_2, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
            assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, conceptsPagedResult.getResults().get(i++).getUrn());
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
            MetamacCriteriaResult<ConceptMetamacDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, conceptsPagedResult.getResults().size());
            assertTrue(conceptsPagedResult.getResults().get(0) instanceof ConceptMetamacDto);
            assertEquals(CONCEPT_SCHEME_1_V2, conceptsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

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
                MetamacCriteriaResult<ConceptMetamacDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, conceptsPagedResult.getResults().size());
                assertTrue(conceptsPagedResult.getResults().get(0) instanceof ConceptMetamacDto);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

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
                MetamacCriteriaResult<ConceptMetamacDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, conceptsPagedResult.getResults().size());
                assertTrue(conceptsPagedResult.getResults().get(0) instanceof ConceptMetamacDto);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

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
                MetamacCriteriaResult<ConceptMetamacDto> conceptsPagedResult = srmCoreServiceFacade.findConceptsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, conceptsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(2, conceptsPagedResult.getResults().size());
                assertTrue(conceptsPagedResult.getResults().get(0) instanceof ConceptMetamacDto);
                assertEquals(CONCEPT_SCHEME_1_V2, conceptsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, conceptsPagedResult.getResults().get(i++).getUrn());
                assertEquals(conceptsPagedResult.getResults().size(), i);
            }
        }
    }

    @Test
    public void testCreateConcept() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(Boolean.TRUE);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);
        conceptMetamacDto.setConceptExtendsUrn(CONCEPT_SCHEME_12_V1_CONCEPT_1);

        ConceptMetamacDto conceptMetamacDtoCreated = srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
        assertEquals(GeneratorUrnUtils.generateSdmxConceptUrn("ISTAC", "CONCEPTSCHEME01", "02.000", conceptMetamacDto.getCode()), conceptMetamacDtoCreated.getUrn());
        assertNull(conceptMetamacDtoCreated.getUri());

        assertEqualsConceptDto(conceptMetamacDtoCreated, conceptMetamacDto);
    }

    @Test
    public void testCreateConceptRepresentationNotEnumerated() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(Boolean.FALSE);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);

        ConceptMetamacDto conceptMetamacDtoCreated = srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
        assertEquals(GeneratorUrnUtils.generateSdmxConceptUrn("ISTAC", "CONCEPTSCHEME01", "02.000", conceptMetamacDto.getCode()), conceptMetamacDtoCreated.getUrn());
        assertNull(conceptMetamacDtoCreated.getUri());

        assertEqualsConceptDto(conceptMetamacDtoCreated, conceptMetamacDto);
    }

    @Test
    public void testCreateConceptWithConceptParent() throws Exception {
        ConceptMetamacDto conceptMetamacDto = ConceptsMetamacDtoMocks.mockConceptDto(Boolean.TRUE);
        conceptMetamacDto.setItemParentUrn(CONCEPT_SCHEME_1_V2_CONCEPT_1);
        conceptMetamacDto.setItemSchemeVersionUrn(CONCEPT_SCHEME_1_V2);

        ConceptMetamacDto conceptMetamacDtoCreated = srmCoreServiceFacade.createConcept(getServiceContextAdministrador(), conceptMetamacDto);
        assertEqualsConceptDto(conceptMetamacDtoCreated, conceptMetamacDto);
    }

    @Test
    public void testUpdateConcept() throws Exception {
        ConceptMetamacDto conceptMetamacDto = srmCoreServiceFacade.retrieveConceptByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);
        assertNotNull(conceptMetamacDto.getConceptExtendsUrn());
        conceptMetamacDto.setName(MetamacMocks.mockInternationalString());
        conceptMetamacDto.setDescription(MetamacMocks.mockInternationalString());
        conceptMetamacDto.setConceptExtendsUrn(CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1);

        ConceptMetamacDto conceptMetamacDtoUpdated = srmCoreServiceFacade.updateConcept(getServiceContextAdministrador(), conceptMetamacDto);

        assertNotNull(conceptMetamacDto);
        assertEqualsConceptDto(conceptMetamacDto, conceptMetamacDtoUpdated);
        assertTrue(conceptMetamacDtoUpdated.getVersionOptimisticLocking() > conceptMetamacDto.getVersionOptimisticLocking());
        
        // Update again to check removing concept extends
        conceptMetamacDtoUpdated.setConceptExtendsUrn(null);
        ConceptMetamacDto conceptMetamacDtoUpdatedAgain = srmCoreServiceFacade.updateConcept(getServiceContextAdministrador(), conceptMetamacDtoUpdated);

        assertNotNull(conceptMetamacDto);
        assertEqualsConceptDto(conceptMetamacDtoUpdated, conceptMetamacDtoUpdatedAgain);
        assertTrue(conceptMetamacDtoUpdatedAgain.getVersionOptimisticLocking() > conceptMetamacDtoUpdated.getVersionOptimisticLocking());
    }

    @Test
    public void testRetrieveRelatedConcepts() throws Exception {

        {
            // Retrieve
            List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        }
        {
            // Retrieve
            List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);
        }
        {
            // Retrieve
            List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1);

            // Validate
            assertEquals(2, relatedConcepts.size());
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_1);
            assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        }
    }

    @Test
    public void testRetrieveRelatedConceptsRoles() throws Exception {

        // Retrieve
        List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConceptsRoles(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2_CONCEPT_1);

        // Validate
        assertEquals(2, relatedConcepts.size());
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_3_V1_CONCEPT_2);
        assertListConceptsContainsConcept(relatedConcepts, CONCEPT_SCHEME_3_V1_CONCEPT_2_1_1);
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

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }

    private void assertListConceptsContainsConcept(List<ConceptMetamacDto> items, String urn) {
        for (ConceptMetamacDto item : items) {
            if (item.getUrn().equals(urn)) {
                return;
            }
        }
        fail("List does not contain item with urn " + urn);
    }

}
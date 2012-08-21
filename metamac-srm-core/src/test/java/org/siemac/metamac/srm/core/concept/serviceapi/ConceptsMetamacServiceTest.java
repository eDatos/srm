package org.siemac.metamac.srm.core.concept.serviceapi;

import static org.junit.Assert.assertEquals;
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
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class ConceptsMetamacServiceTest extends SrmBaseTest implements ConceptsMetamacServiceTestBase {

    @Autowired
    private ConceptsMetamacService conceptsService;

    private String                 CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";
    private String                 CONCEPT_SCHEME_1_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(02.000)";
    private String                 CONCEPT_SCHEME_2_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME02(01.000)";
    private String                 CONCEPT_SCHEME_3_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(01.000)";

    @Test
    public void testCreateConceptScheme() throws Exception {
        ConceptSchemeVersionMetamac conceptSchemeVersion = ConceptsMetamacDoMocks.mockConceptScheme();

        // Create
        ConceptSchemeVersionMetamac conceptSchemeVersionCreated = conceptsService.createConceptScheme(getServiceContextAdministrador(), conceptSchemeVersion);
        String urn = conceptSchemeVersionCreated.getMaintainableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        ConceptSchemeVersionMetamac conceptSchemeVersionRetrieved = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeVersionRetrieved.getProcStatus());
        ConceptsMetamacAsserts.assertEqualsConceptSchemeMetamac(conceptSchemeVersion, conceptSchemeVersionRetrieved);
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

    @Override
    @Test
    public void testUpdateConceptScheme() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    @Test
    public void testRetrieveConceptSchemeByUrn() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_1_V1;
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, conceptSchemeVersion.getMaintainableArtefact().getUrn());
        assertEquals(ConceptSchemeTypeEnum.OPERATION, conceptSchemeVersion.getType());
        assertEquals("op1", conceptSchemeVersion.getRelatedOperation().getCode());
        assertEquals("urn:op1", conceptSchemeVersion.getRelatedOperation().getUrn());
        assertEquals("http://op1", conceptSchemeVersion.getRelatedOperation().getUri());
        assertEquals(TypeExternalArtefactsEnum.STATISTICAL_OPERATION, conceptSchemeVersion.getRelatedOperation().getType());
        assertEquals("http://app/operations", conceptSchemeVersion.getRelatedOperation().getManagementAppUrl());
    }

    @Test
    public void testRetrieveConceptSchemeByUrnWithoutRelatedOperation() throws Exception {

        // Retrieve
        String urn = CONCEPT_SCHEME_1_V2;
        ConceptSchemeVersionMetamac conceptSchemeVersion = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(urn, conceptSchemeVersion.getMaintainableArtefact().getUrn());
        assertEquals(ConceptSchemeTypeEnum.GLOSSARY, conceptSchemeVersion.getType());
        assertNull(conceptSchemeVersion.getRelatedOperation());
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
            assertEquals(4, conceptSchemeVersionPagedResult.getTotalRows());
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersionPagedResult.getValues().get(0).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersionPagedResult.getValues().get(1).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeVersionPagedResult.getValues().get(2).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(3).getMaintainableArtefact().getUrn());
        }

        // Find published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.procStatus())
                    .eq(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, conceptSchemeVersionPagedResult.getTotalRows());
            assertEquals(CONCEPT_SCHEME_1_V1, conceptSchemeVersionPagedResult.getValues().get(0).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(1).getMaintainableArtefact().getUrn());
        }

        // Find lasts versions
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE)
                    .orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(3, conceptSchemeVersionPagedResult.getTotalRows());
            assertEquals(CONCEPT_SCHEME_1_V2, conceptSchemeVersionPagedResult.getValues().get(0).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_2_V1, conceptSchemeVersionPagedResult.getValues().get(1).getMaintainableArtefact().getUrn());
            assertEquals(CONCEPT_SCHEME_3_V1, conceptSchemeVersionPagedResult.getValues().get(2).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidation() throws Exception {

        String urn = CONCEPT_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.DRAFT, conceptSchemeVersionMetamac.getProcStatus());
            assertNull(conceptSchemeVersionMetamac.getProductionValidationDate());
            assertNull(conceptSchemeVersionMetamac.getProductionValidationUser());
        }

        // Sends to production validation
        ConceptSchemeVersionMetamac conceptSchemeVersionProductionValidation = conceptsService.sendConceptSchemeToProductionValidation(ctx, urn);

        // Validation
        {
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersionProductionValidation.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionProductionValidation.getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersionProductionValidation.getProductionValidationUser());
            assertNull(conceptSchemeVersionProductionValidation.getDiffusionValidationDate());
            assertNull(conceptSchemeVersionProductionValidation.getDiffusionValidationUser());
        }
        {
            conceptSchemeVersionProductionValidation = conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
            assertEquals(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, conceptSchemeVersionProductionValidation.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), conceptSchemeVersionProductionValidation.getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), conceptSchemeVersionProductionValidation.getProductionValidationUser());
            assertNull(conceptSchemeVersionProductionValidation.getDiffusionValidationDate());
            assertNull(conceptSchemeVersionProductionValidation.getDiffusionValidationUser());
        }
    }

    // TODO
    // @Test
    // public void testSendIndicatorToProductionValidationInProcStatusRejected() throws Exception {
    //
    // String uuid = INDICATOR_9;
    // String productionVersion = "1.000";
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
    // assertEquals(productionVersion, indicatorDto.getProductionVersion());
    // assertNull(indicatorDto.getPublishedVersion());
    // assertNull(indicatorDto.getArchivedVersion());
    // assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());
    // }
    //
    // // Sends to production validation
    // indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador2(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
    // assertEquals(productionVersion, indicatorDto.getProductionVersion());
    // assertNull(indicatorDto.getPublishedVersion());
    // assertNull(indicatorDto.getArchivedVersion());
    // assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToProductionValidationErrorNotExists() throws Exception {
    //
    // try {
    // indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), NOT_EXISTS);
    // fail("Indicator not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToProductionValidationErrorWrongProcStatus() throws Exception {
    //
    // String uuid = INDICATOR_3;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
    // assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
    // assertNull(indicatorDto.getProductionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), uuid);
    // fail("Indicator is not draft");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
    // assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
    //
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToProductionValidationErrorWithoutDataSources() throws Exception {
    //
    // String uuid = INDICATOR_2;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
    // assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
    //
    // // Check zero data sources
    // List<DataSourceDto> dataSources = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuid, "1.000");
    // assertEquals(0, dataSources.size());
    // }
    //
    // try {
    // indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), uuid);
    // fail("Indicator hasn't data sources");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_MUST_HAVE_DATA_SOURCES.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToProductionValidationErrorQuantityIncomplete() throws Exception {
    //
    // String uuid = INDICATOR_1;
    // String productionVersion = "2.000";
    //
    // IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
    // assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
    // assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());
    //
    // // Update to clear quantity required attributes
    // indicatorDtoV2.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
    // indicatorDtoV2.getQuantity().setUnitUuid(null);
    // indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDtoV2);
    //
    // // Sends to production validation
    // try {
    // indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), uuid);
    // fail("Indicator quantity incomplete");
    // } catch (MetamacException e) {
    // assertEquals(3, e.getExceptionItems().size());
    //
    // assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_UNIT_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
    //
    // assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
    // assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
    // assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_IS_PERCENTAGE, e.getExceptionItems().get(1).getMessageParameters()[0]);
    //
    // assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
    // assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
    // assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_QUANTITY_INDICATOR_UUID, e.getExceptionItems().get(2).getMessageParameters()[0]);
    // }
    // }

    
    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptSchemeTest.xml";
    }
}

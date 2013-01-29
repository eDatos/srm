package org.siemac.metamac.srm.core.dsd.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionMetamacDoMocks;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionsMetamacAsserts;
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

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DsdsMetamacServiceTest extends SrmBaseTest implements DsdsMetamacServiceTestBase {

    @Autowired
    protected DsdsMetamacService          dsdsMetamacService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    private final ServiceContext          serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    // TODO Test dsd. Hacer tests de mappers

    @Test
    @Override
    public void testCreateDataStructureDefinition() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac);
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacCreated = dsdsMetamacService.createDataStructureDefinition(ctx, dataStructureDefinitionVersionMetamac);
        String urn = dataStructureDefinitionVersionMetamacCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", dataStructureDefinitionVersionMetamacCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), dataStructureDefinitionVersionMetamacCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacRetrieved = dsdsMetamacService.retrieveDataStructureDefinitionByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(dataStructureDefinitionVersionMetamacRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getProductionValidationDate());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getProductionValidationUser());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getDiffusionValidationDate());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getDiffusionValidationUser());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getInternalPublicationDate());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(dataStructureDefinitionVersionMetamacRetrieved.getLifeCycleMetadata().getExternalPublicationUser());
        assertFalse(dataStructureDefinitionVersionMetamacRetrieved.getMaintainableArtefact().getFinalLogicClient());
        assertEquals(ctx.getUserId(), dataStructureDefinitionVersionMetamacRetrieved.getCreatedBy());
        assertEquals(ctx.getUserId(), dataStructureDefinitionVersionMetamacRetrieved.getLastUpdatedBy());
        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dataStructureDefinitionVersionMetamac, dataStructureDefinitionVersionMetamacRetrieved);
    }

    @Test
    public void testCreateDataStructureDefinitionErrorNotDefault() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_2_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac);

        try {
            dsdsMetamacService.createDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamac);
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
    public void testCreateDataStructureDefinitionErrorValidation() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac);
        dataStructureDefinitionVersionMetamac.setShowDecimals(45);

        try {
            dsdsMetamacService.createDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamac);
            fail("maintainer not default");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_SHOWDECIMALS.getCode(), e.getExceptionItems().get(0).getCode());
        } catch (Exception e) {
            fail("wrong exception");
        }
    }

    @Test
    @Override
    public void testPreCreateDataStructureDefinition() throws Exception {
        // TODO testPreCreateDataStructureDefinition

    }

    @Test
    @Override
    public void testUpdateDataStructureDefinition() throws Exception {
        // TODO Test dsd
        // TODO hacer 2 tests para la modificación del code: 1) permitir modificar si es primera versión y es final. 2) dar error si no es primera versión
        // TODO test no se puede crear de un maintainer != default

    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionByUrn() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionVersions() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testSaveDescriptorForDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testDeleteDescriptorForDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testSaveComponentForDataStructureDefinition() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String urn = DSD_1_V2;

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = dsdsMetamacService.retrieveDataStructureDefinitionByUrn(ctx, urn);
        assertTrue(dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions().size() != 0);

        // Create Dimension Descriptor and components
        ComponentList componentList = DataStructureDefinitionDoMocks.mockDimensionDescriptor();
        dsdsMetamacService.saveDescriptorForDataStructureDefinition(ctx, urn, componentList);

        Component measureDim = DataStructureDefinitionDoMocks.mockMeasureDimension();
        ((MeasureDimension) measureDim).setIsRepresentationUpdated(Boolean.TRUE);
        /* Component measureDimCreated = */dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), urn, measureDim);

        assertTrue(dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions().size() == 0);

    }
    @Test
    @Override
    public void testDeleteComponentForDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testSendDataStructureDefinitionToProductionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testSendDataStructureDefinitionToDiffusionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testRejectDataStructureDefinitionProductionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testRejectDataStructureDefinitionDiffusionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testPublishInternallyDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testPublishExternallyDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    public void testDeleteDataStructureDefinition() throws Exception {
        // dsdsMetamacService.deleteDataStructureDefinition(getServiceContextAdministrador(), DSD_06_URN);
    }

    @Test
    @Override
    public void testVersioningDataStructureDefinition() throws Exception {
        String urn = DSD_6_V1;
        String versionExpected = "02.000";

        DataStructureDefinitionVersionMetamac dsdToCopy = dsdsMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), urn);
        DataStructureDefinitionVersionMetamac dsdNewVersion = dsdsMetamacService.versioningDataStructureDefinition(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DRAFT, dsdNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, dsdNewVersion.getMaintainableArtefact().getVersionLogic());
        }

        {
            // Metamac Metadata
            assertEquals(dsdToCopy.getAutoOpen(), dsdNewVersion.getAutoOpen());
            assertEquals(dsdToCopy.getShowDecimals(), dsdNewVersion.getShowDecimals());
            assertEquals(dsdToCopy.getHeadingDimensions().size(), dsdNewVersion.getHeadingDimensions().size());
            // heading
            for (int i = 0; i < dsdToCopy.getHeadingDimensions().size(); i++) {
                DimensionOrder dimOrderToCopy = dsdToCopy.getHeadingDimensions().get(i);
                DimensionOrder dimOrderToNewVersion = dsdNewVersion.getHeadingDimensions().get(i);
                assertEquals(dimOrderToCopy.getDimension().getCode(), dimOrderToNewVersion.getDimension().getCode());
                assertEquals(dimOrderToCopy.getDimOrder(), dimOrderToNewVersion.getDimOrder());
            }
            // Stub
            for (int i = 0; i < dsdToCopy.getStubDimensions().size(); i++) {
                DimensionOrder dimOrderToCopy = dsdToCopy.getStubDimensions().get(i);
                DimensionOrder dimOrderToNewVersion = dsdNewVersion.getStubDimensions().get(i);
                assertEquals(dimOrderToCopy.getDimension().getCode(), dimOrderToNewVersion.getDimension().getCode());
                assertEquals(dimOrderToCopy.getDimOrder(), dimOrderToNewVersion.getDimOrder());
            }
            // ShowDecimalsPrecisions
            for (int i = 0; i < dsdToCopy.getShowDecimalsPrecisions().size(); i++) {
                MeasureDimensionPrecision measureDimensionPrecisionToCopy = dsdToCopy.getShowDecimalsPrecisions().get(i);
                MeasureDimensionPrecision measureDimensionPrecisionToNewVersion = dsdNewVersion.getShowDecimalsPrecisions().get(i);
                assertEquals(measureDimensionPrecisionToCopy.getConcept().getNameableArtefact().getUrn(), measureDimensionPrecisionToNewVersion.getConcept().getNameableArtefact().getUrn());
                assertEquals(measureDimensionPrecisionToCopy.getShowDecimalPrecision(), measureDimensionPrecisionToNewVersion.getShowDecimalPrecision());
            }

        }

    }
    @Test
    public void testVersioningDataStructureDefinitionErrorAlreadyExistsDraft() throws Exception {
        // TODO Test dsd
    }

    @Test
    public void testVersioningDataStructureDefinitionErrorNotPublished() throws Exception {
        // TODO Test dsd
    }

    @Test
    @Override
    public void testEndDataStructureDefinitionValidity() throws Exception {
        // TODO Test dsd

    }

    @Override
    @Test
    public void testFindConceptsCanBeDsdPrimaryMeasureByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptMetamac> result = dsdsMetamacService.findConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

        // Validate
        assertEquals(2, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_5_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter, dsdUrn);

        // Validate
        assertEquals(2, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_5_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByConditionWithAdditionalCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label()).eq("concept-scheme-4-1")
                .orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter, dsdUrn);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_4_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptsCanBeDsdTimeDimensionByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptMetamac> result = dsdsMetamacService.findConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

        // Validate
        assertEquals(4, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter, dsdUrn);

        // Validate
        assertEquals(3, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_2_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptsCanBeDsdMeasureDimensionByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptMetamac> result = dsdsMetamacService.findConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter, dsdUrn);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_2_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptsCanBeDsdDimensionByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptMetamac> result = dsdsMetamacService.findConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

        // Validate
        assertEquals(4, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_2_V1_CONCEPT_2, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdDimensionByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                dsdUrn);

        // Validate
        assertEquals(3, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_2_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptsCanBeDsdRoleByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        PagedResult<ConceptMetamac> result = dsdsMetamacService.findConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_6_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdRoleByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                dsdUrn);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_6_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForDsdDimension() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = dsdsMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdDimension(getServiceContextAdministrador(), conditions, pagingParameter,
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
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2_1;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = dsdsMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdDimension(getServiceContextAdministrador(), conditions, pagingParameter,
                    conceptUrn);

            // Validate
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(VARIABLE_2, result.getValues().get(i).getVariable().getNameableArtefact().getUrn());
            assertEquals(CODELIST_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(result.getTotalRows(), i);
        }
    }

    @Override
    @Test
    public void testFindConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimension() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimension(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_7_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptSchemesWithConceptsCanBeDsdAttributeByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn())
                .build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dsdsMetamacService.findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                dsdUrn);

        // Validate
        assertEquals(2, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_3_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindConceptsCanBeDsdAttributeByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        String dsdUrn = DSD_1_V2;

        // Find
        PagedResult<ConceptMetamac> result = dsdsMetamacService.findConceptsCanBeDsdAttributeByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

        // Validate
        assertEquals(3, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }

    /************************************************************************************
     * PRIVATE
     ************************************************************************************/
    private DataStructureDefinitionVersionMetamac createDataStructureDefinitionGraph() throws MetamacException {

        // Create DSD
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = dsdsMetamacService.createDataStructureDefinition(getServiceContext(),
                DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac));
        dataStructureDefinitionVersionMetamac.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Create Dimension Descriptor and components
        Component measureDim = DataStructureDefinitionDoMocks.mockMeasureDimension();
        Component measureDimCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(),
                measureDim);

        Component dim = DataStructureDefinitionDoMocks.mockDimension();
        Component dimCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), dim);

        Component timeDim = DataStructureDefinitionDoMocks.mockTimeDimension();
        /* Component timeDimCreated = */dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(),
                timeDim);

        // Create GroupDimension Descriptor
        ComponentList groupDescriptor = DataStructureDefinitionDoMocks.mockGroupDimensionDescriptor((DimensionComponent) measureDimCreated);
        ComponentList groupDescriptorCreated = dsdsMetamacService.saveDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact()
                .getUrn(), groupDescriptor);

        // Create Attribute Descriptor and components
        Component dataAttribute = DataStructureDefinitionDoMocks.mockDataAttribute((GroupDimensionDescriptor) groupDescriptorCreated, (DimensionComponent) dimCreated);
        /* Component dataAttributeCreated = */dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(),
                dataAttribute);

        // Create Measure Descriptor and component
        Component primaryMeasure = DataStructureDefinitionDoMocks.mockPrimaryMeasure();
        /* Component componentCreated = */dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(),
                primaryMeasure);

        return dataStructureDefinitionVersionMetamac;
    }

}

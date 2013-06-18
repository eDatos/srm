package org.siemac.metamac.srm.core.dsd.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.DimensionVisualisationInfo;
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

import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DataStructureDefinitionMetamacServiceTest extends SrmBaseTest implements DataStructureDefinitionMetamacServiceTestBase {

    @Autowired
    protected DataStructureDefinitionMetamacService dataStructureDefinitionMetamacService;

    @Autowired
    private OrganisationMetamacRepository           organisationMetamacRepository;

    @Autowired
    private ConceptMetamacRepository                conceptMetamacRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository   conceptSchemeVersionMetamacRepository;

    @Autowired
    private CodelistVersionMetamacRepository        codelistVersionMetamacRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager                         entityManager;

    private final ServiceContext                    serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    @Test
    @Override
    public void testCreateDataStructureDefinition() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac, "op3");
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacCreated = dataStructureDefinitionMetamacService.createDataStructureDefinition(ctx,
                dataStructureDefinitionVersionMetamac);
        String urn = dataStructureDefinitionVersionMetamacCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", dataStructureDefinitionVersionMetamacCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), dataStructureDefinitionVersionMetamacCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacRetrieved = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(ctx, urn);
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
        assertEquals(1, dataStructureDefinitionVersionMetamacRetrieved.getGrouping().size());
        assertEquals(1, dataStructureDefinitionVersionMetamacRetrieved.getGrouping().iterator().next().getComponents().size());
        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dataStructureDefinitionVersionMetamac, dataStructureDefinitionVersionMetamacRetrieved);
    }

    @Test
    public void testCreateDataStructureDefinitionErrorNotDefault() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_2_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac, "op8");

        try {
            dataStructureDefinitionMetamacService.createDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamac);
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
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac, "op8");
        dataStructureDefinitionVersionMetamac.setShowDecimals(45);

        try {
            dataStructureDefinitionMetamacService.createDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamac);
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
        // Tested in testCreateDataStructureDefinition

    }

    @Test
    @Override
    public void testUpdateDataStructureDefinition() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(ctx, DSD_2_V1);
        dataStructureDefinitionVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionUpdated = dataStructureDefinitionMetamacService.updateDataStructureDefinition(ctx, dataStructureDefinitionVersion);
        assertNotNull(dataStructureDefinitionVersionUpdated);
        assertEquals("user1", dataStructureDefinitionVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), dataStructureDefinitionVersionUpdated.getLastUpdatedBy());
    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionByUrn() throws Exception {
        // Retrieve
        String urn = DSD_6_V1;
        DataStructureDefinitionVersionMetamac dsd = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), urn);

        assertFalse(dsd.getAutoOpen());
        assertEquals(1, dsd.getDimensionVisualisationInfos().size());
        assertEquals(2, dsd.getHeadingDimensions().size());
        assertEquals(1, dsd.getStubDimensions().size());
    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionVersions() throws Exception {
        // Already tested in statistic-sdmx, See DataStructureDefinitionServiceTest.testRetrieveDataStructureDefinitionVersions
    }

    @Test
    @Override
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        // Already tested in statistic-sdmx, See DataStructureDefinitionServiceTest.testFindDataStructureDefinitionByCondition
    }

    @Test
    @Override
    public void testSaveDescriptorForDataStructureDefinition() throws Exception {
        // Already tested in statistic-sdmx, See DataStructureDefinitionServiceTest.testSaveDescriptorForDataStructureDefinition
    }

    @Test
    @Override
    public void testDeleteDescriptorForDataStructureDefinition() throws Exception {
        // Already tested in statistic-sdmx, See DataStructureDefinitionServiceTest.testDeleteDescriptorForDataStructureDefinition
    }

    @Test
    @Override
    public void testSaveComponentForDataStructureDefinition() throws Exception {

        ServiceContext ctx = getServiceContextAdministrador();
        String urn = DSD_1_V2;

        {
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(ctx, urn);
            assertTrue(dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions().size() != 0);

            // Create Dimension Descriptor and components
            ComponentList componentList = DataStructureDefinitionDoMocks.mockDimensionDescriptor();
            dataStructureDefinitionMetamacService.saveDescriptorForDataStructureDefinition(ctx, urn, componentList);

            Concept identity = conceptMetamacRepository.findByUrn(CONCEPT_SCHEME_2_V1_CONCEPT_1);
            Concept role = conceptMetamacRepository.findByUrn(CONCEPT_SCHEME_6_V1_CONCEPT_1);
            ConceptSchemeVersion conceptScheme = conceptSchemeVersionMetamacRepository.findByUrn(CONCEPT_SCHEME_7_V1);
            Component measureDim = DataStructureDefinitionDoMocks.mockMeasureDimension(identity, Arrays.asList(role), conceptScheme);
            ((MeasureDimension) measureDim).setIsEnumeratedRepresentationUpdated(Boolean.TRUE);
            ((MeasureDimension) measureDim).setIsConceptIdUpdated(Boolean.FALSE);
            dataStructureDefinitionMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), urn, measureDim);
            assertTrue(dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions().size() == 0);
        }

    }

    @Test
    @Override
    @Ignore
    public void testDeleteComponentForDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    @Ignore
    public void testSendDataStructureDefinitionToProductionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    @Ignore
    public void testSendDataStructureDefinitionToDiffusionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    @Ignore
    public void testRejectDataStructureDefinitionProductionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    @Ignore
    public void testRejectDataStructureDefinitionDiffusionValidation() throws Exception {
        // TODO Test dsd

    }

    @Test
    @Override
    @Ignore
    public void testPublishInternallyDataStructureDefinition() throws Exception {
        // TODO Test dsd

    }

    @Override
    public void testCheckDataStructureDefinitionTranslations() throws Exception {
        // Tested in testPublishInternallyDataStructureDefinitionCheckTranslations
    }

    @Test
    public void testPublishInternallyDataStructureDefinitionCheckTranslations() throws Exception {
        String urn = DSD_7_V1;

        try {
            // Note: publishInternallyDataStructureDefinition calls to 'testCheckDataStructureDefinitionTranslations'
            dataStructureDefinitionMetamacService.publishInternallyDataStructureDefinition(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("DataStructureDefinition wrong translations");
        } catch (MetamacException e) {
            assertEquals(10, e.getExceptionItems().size());
            int i = 0;
            // DataStructureDefinition
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION}, exceptionItem
                        .getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            // Components List
            {
                // AttributeDescriptor
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_ATTRIBUTE_DESCRIPTOR);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // MeasureDescriptor
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_MEASURE_DESCRIPTOR);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // groupDimensionDescriptor02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_GROUP_DIMENSION_DESCRIPTOR_2);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // groupDimensionDescriptor03
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_GROUP_DIMENSION_DESCRIPTOR_3);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            // Components
            {
                // dim-01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_DIMENSION_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // TIME_PERIOD
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_TIME_DIMENSION_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // DataAttribute01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_DATA_ATTRIBUTE_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // DataAttribute03
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_DATA_ATTRIBUTE_3);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // PrimartyMeasure
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, DSD_7_V1_PRIMARY_MEASURE);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(0));
            }
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @Override
    @Ignore
    public void testPublishExternallyDataStructureDefinition() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    public void testCheckDataStructureDefinitionWithRelatedResourcesExternallyPublished() throws Exception {
        // TODO Test dsd
    }

    @Test
    @Override
    @Ignore
    public void testDeleteDataStructureDefinition() throws Exception {
        // TODO Test dsd
    }

    @Test
    @Override
    public void testCopyDataStructureDefinition() throws Exception {
        String urnToCopy = DSD_6_V1;
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        String versionExpected = "01.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION06(01.000)";

        TaskInfo copyResult = dataStructureDefinitionMetamacService.copyDataStructureDefinition(getServiceContextAdministrador(), urnToCopy);

        // Validate (only some metadata, already tested in statistic module)
        entityManager.clear();
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionNewArtefact = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(),
                copyResult.getUrnResult());
        assertEquals(maintainerUrnExpected, dataStructureDefinitionVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, dataStructureDefinitionVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, dataStructureDefinitionVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, dataStructureDefinitionVersionNewArtefact.getMaintainableArtefact().getUrn());
        assertEquals(null, dataStructureDefinitionVersionNewArtefact.getMaintainableArtefact().getReplaceToVersion());
        assertEquals(null, dataStructureDefinitionVersionNewArtefact.getMaintainableArtefact().getReplacedByVersion());
        assertTrue(dataStructureDefinitionVersionNewArtefact.getMaintainableArtefact().getIsLastVersion());

        DataStructureDefinitionVersionMetamac dsdToCopy = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), urnToCopy);
        {
            // Metamac Metadata
            assertEquals(dsdToCopy.getAutoOpen(), dataStructureDefinitionVersionNewArtefact.getAutoOpen());
            assertEquals(dsdToCopy.getShowDecimals(), dataStructureDefinitionVersionNewArtefact.getShowDecimals());
            assertEquals(dsdToCopy.getHeadingDimensions().size(), dataStructureDefinitionVersionNewArtefact.getHeadingDimensions().size());
            assertEquals(dsdToCopy.getDimensionVisualisationInfos().size(), dataStructureDefinitionVersionNewArtefact.getDimensionVisualisationInfos().size());
            // heading
            for (int i = 0; i < dsdToCopy.getHeadingDimensions().size(); i++) {
                DimensionOrder dimOrderToCopy = dsdToCopy.getHeadingDimensions().get(i);
                DimensionOrder dimOrderToNewVersion = dataStructureDefinitionVersionNewArtefact.getHeadingDimensions().get(i);
                assertEquals(dimOrderToCopy.getDimension().getCode(), dimOrderToNewVersion.getDimension().getCode());
                assertEquals(dimOrderToCopy.getDimOrder(), dimOrderToNewVersion.getDimOrder());
            }
            // Stub
            for (int i = 0; i < dsdToCopy.getStubDimensions().size(); i++) {
                DimensionOrder dimOrderToCopy = dsdToCopy.getStubDimensions().get(i);
                DimensionOrder dimOrderToNewVersion = dataStructureDefinitionVersionNewArtefact.getStubDimensions().get(i);
                assertEquals(dimOrderToCopy.getDimension().getCode(), dimOrderToNewVersion.getDimension().getCode());
                assertEquals(dimOrderToCopy.getDimOrder(), dimOrderToNewVersion.getDimOrder());
            }
            // ShowDecimalsPrecisions
            for (int i = 0; i < dsdToCopy.getShowDecimalsPrecisions().size(); i++) {
                MeasureDimensionPrecision measureDimensionPrecisionToCopy = dsdToCopy.getShowDecimalsPrecisions().get(i);
                MeasureDimensionPrecision measureDimensionPrecisionToNewVersion = dataStructureDefinitionVersionNewArtefact.getShowDecimalsPrecisions().get(i);
                assertEquals(measureDimensionPrecisionToCopy.getConcept().getNameableArtefact().getUrn(), measureDimensionPrecisionToNewVersion.getConcept().getNameableArtefact().getUrn());
                assertEquals(measureDimensionPrecisionToCopy.getShowDecimalPrecision(), measureDimensionPrecisionToNewVersion.getShowDecimalPrecision());
            }
            // DimensionVisualisationInfo
            for (int i = 0; i < dsdToCopy.getDimensionVisualisationInfos().size(); i++) {
                DimensionVisualisationInfo dimensionVisualizationInfoToCopy = dsdToCopy.getDimensionVisualisationInfos().get(i);
                DimensionVisualisationInfo dimensionVisualizationInfoToNew = dataStructureDefinitionVersionNewArtefact.getDimensionVisualisationInfos().get(i);
                assertEquals(dimensionVisualizationInfoToCopy.getDimension().getCode(), dimensionVisualizationInfoToNew.getDimension().getCode());
                assertEquals(dimensionVisualizationInfoToCopy.getDisplayOrder().getNameableArtefact().getUrn(), dimensionVisualizationInfoToNew.getDisplayOrder().getNameableArtefact().getUrn());
                assertEquals(dimensionVisualizationInfoToNew.getHierarchyLevelsOpen().getNameableArtefact().getUrn(), dimensionVisualizationInfoToNew.getHierarchyLevelsOpen().getNameableArtefact()
                        .getUrn());
            }
        }
    }

    @Test
    @Override
    public void testVersioningDataStructureDefinition() throws Exception {
        String urn = DSD_6_V1;
        String versionExpected = "02.000";

        TaskInfo versioningResult = dataStructureDefinitionMetamacService.versioningDataStructureDefinition(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        DataStructureDefinitionVersionMetamac dsdToCopy = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), urn);
        DataStructureDefinitionVersionMetamac dsdNewVersion = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(),
                versioningResult.getUrnResult());

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
            assertEquals(dsdToCopy.getDimensionVisualisationInfos().size(), dsdNewVersion.getDimensionVisualisationInfos().size());
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
            // DimensionVisualisationInfo
            for (int i = 0; i < dsdToCopy.getDimensionVisualisationInfos().size(); i++) {
                DimensionVisualisationInfo dimensionVisualizationInfoToCopy = dsdToCopy.getDimensionVisualisationInfos().get(i);
                DimensionVisualisationInfo dimensionVisualizationInfoToNew = dsdNewVersion.getDimensionVisualisationInfos().get(i);
                assertEquals(dimensionVisualizationInfoToCopy.getDimension().getCode(), dimensionVisualizationInfoToNew.getDimension().getCode());
                assertEquals(dimensionVisualizationInfoToCopy.getDisplayOrder().getNameableArtefact().getUrn(), dimensionVisualizationInfoToNew.getDisplayOrder().getNameableArtefact().getUrn());
                assertEquals(dimensionVisualizationInfoToNew.getHierarchyLevelsOpen().getNameableArtefact().getUrn(), dimensionVisualizationInfoToNew.getHierarchyLevelsOpen().getNameableArtefact()
                        .getUrn());
            }
        }
    }

    @Test
    @Ignore
    public void testVersioningDataStructureDefinitionErrorAlreadyExistsDraft() throws Exception {
        // TODO Test dsd
    }

    @Test
    @Ignore
    public void testVersioningDataStructureDefinitionErrorNotPublished() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Test
    public void testCreateTemporalDataStructureDefinition() throws Exception {
        String urn = DSD_6_V1;
        String versionExpected = "01.000" + UrnConstants.URN_SDMX_TEMPORAL_SUFFIX;
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX02:DATASTRUCTUREDEFINITION06(" + versionExpected + ")";
        String dsd_6_v1temp_dimension_1 = "urn:sdmx:org.sdmx.infomodel.datastructure.Dimension=SDMX02:DATASTRUCTUREDEFINITION06(" + versionExpected + ").dim-01";
        String dsd_6_v1temp_time_dimension_1 = "urn:sdmx:org.sdmx.infomodel.datastructure.TimeDimension=SDMX02:DATASTRUCTUREDEFINITION06(" + versionExpected + ").timeDimension-01";
        String dsd_6_v1temp_measure_dimension_1 = "urn:sdmx:org.sdmx.infomodel.datastructure.MeasureDimension=SDMX02:DATASTRUCTUREDEFINITION06(" + versionExpected + ").measureDimension-01";

        TaskInfo versioningResult = dataStructureDefinitionMetamacService.createTemporalDataStructureDefinition(getServiceContextAdministrador(), urn);

        // Validate response
        entityManager.clear();
        DataStructureDefinitionVersionMetamac dsdToCopy = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), urn);
        DataStructureDefinitionVersionMetamac dsdNewVersion = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(),
                versioningResult.getUrnResult());

        // Validate response
        {
            assertEquals(ProcStatusEnum.DRAFT, dsdNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, dsdNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, dsdNewVersion.getMaintainableArtefact().getUrn());
            assertTrue(dsdNewVersion.getMaintainableArtefact().getIsTemporal());
        }

        // Validate retrieving
        // New version
        {
            for (ComponentList componentListNew : dsdNewVersion.getGrouping()) {
                if (componentListNew instanceof DimensionDescriptor) {
                    for (Component componentNew : componentListNew.getComponents()) {
                        if (componentNew.getCode().contains("dim-01")) {
                            assertEquals(dsd_6_v1temp_dimension_1, componentNew.getUrn());
                        } else if (componentNew.getCode().contains("timeDimension-01")) {
                            assertEquals(dsd_6_v1temp_time_dimension_1, componentNew.getUrn());
                        } else if (componentNew.getCode().contains("measureDimension-01")) {
                            assertEquals(dsd_6_v1temp_measure_dimension_1, componentNew.getUrn());
                        }
                    }
                }
            }
        }

        {
            // Metamac Metadata
            assertEquals(dsdToCopy.getAutoOpen(), dsdNewVersion.getAutoOpen());
            assertEquals(dsdToCopy.getShowDecimals(), dsdNewVersion.getShowDecimals());
            assertEquals(dsdToCopy.getHeadingDimensions().size(), dsdNewVersion.getHeadingDimensions().size());
            assertEquals(dsdToCopy.getDimensionVisualisationInfos().size(), dsdNewVersion.getDimensionVisualisationInfos().size());
            // heading
            for (int i = 0; i < dsdToCopy.getHeadingDimensions().size(); i++) {
                DimensionOrder dimOrderToCopy = dsdToCopy.getHeadingDimensions().get(i);
                DimensionOrder dimOrderToNewVersion = dsdNewVersion.getHeadingDimensions().get(i);
                assertEquals(dimOrderToCopy.getDimension().getCode(), dimOrderToNewVersion.getDimension().getCode());
                assertEquals(dimOrderToCopy.getDimOrder(), dimOrderToNewVersion.getDimOrder());
                assertTrue(dimOrderToNewVersion.getDimension().getUrn().contains(UrnConstants.URN_SDMX_TEMPORAL_SUFFIX));
            }
            // Stub
            for (int i = 0; i < dsdToCopy.getStubDimensions().size(); i++) {
                DimensionOrder dimOrderToCopy = dsdToCopy.getStubDimensions().get(i);
                DimensionOrder dimOrderToNewVersion = dsdNewVersion.getStubDimensions().get(i);
                assertEquals(dimOrderToCopy.getDimension().getCode(), dimOrderToNewVersion.getDimension().getCode());
                assertEquals(dimOrderToCopy.getDimOrder(), dimOrderToNewVersion.getDimOrder());
                assertTrue(dimOrderToNewVersion.getDimension().getUrn().contains(UrnConstants.URN_SDMX_TEMPORAL_SUFFIX));
            }
            // ShowDecimalsPrecisions
            for (int i = 0; i < dsdToCopy.getShowDecimalsPrecisions().size(); i++) {
                MeasureDimensionPrecision measureDimensionPrecisionToCopy = dsdToCopy.getShowDecimalsPrecisions().get(i);
                MeasureDimensionPrecision measureDimensionPrecisionToNewVersion = dsdNewVersion.getShowDecimalsPrecisions().get(i);
                assertEquals(measureDimensionPrecisionToCopy.getConcept().getNameableArtefact().getUrn(), measureDimensionPrecisionToNewVersion.getConcept().getNameableArtefact().getUrn());
                assertEquals(measureDimensionPrecisionToCopy.getShowDecimalPrecision(), measureDimensionPrecisionToNewVersion.getShowDecimalPrecision());
            }
            // DimensionVisualisationInfo
            for (int i = 0; i < dsdToCopy.getDimensionVisualisationInfos().size(); i++) {
                DimensionVisualisationInfo dimensionVisualizationInfoToCopy = dsdToCopy.getDimensionVisualisationInfos().get(i);
                DimensionVisualisationInfo dimensionVisualizationInfoToNew = dsdNewVersion.getDimensionVisualisationInfos().get(i);
                assertTrue(dimensionVisualizationInfoToNew.getDimension().getUrn().contains(UrnConstants.URN_SDMX_TEMPORAL_SUFFIX));
                assertEquals(dimensionVisualizationInfoToCopy.getHierarchyLevelsOpen().getNameableArtefact().getUrn(), dimensionVisualizationInfoToNew.getHierarchyLevelsOpen().getNameableArtefact()
                        .getUrn());
                assertEquals(dimensionVisualizationInfoToCopy.getDisplayOrder().getNameableArtefact().getUrn(), dimensionVisualizationInfoToNew.getDisplayOrder().getNameableArtefact().getUrn());
            }
        }
    }

    @Override
    @Test
    public void testCreateVersionFromTemporalDataStructureDefinition() throws Exception {
        String urn = DSD_6_V1;

        TaskInfo versioningResult1 = dataStructureDefinitionMetamacService.createTemporalDataStructureDefinition(getServiceContextAdministrador(), urn);
        entityManager.clear();
        DataStructureDefinitionVersionMetamac temporalDataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(
                getServiceContextAdministrador(), versioningResult1.getUrnResult());

        assertEquals(3, temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getCategorisations().size());
        {
            Categorisation categorisation = assertListContainsCategorisation(temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX02:cat1(01.000_temporal)");
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
        }
        assertListContainsCategorisation(temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX02:cat2(01.000_temporal)");
        assertListContainsCategorisation(temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX02:cat3(01.000_temporal)");

        TaskInfo versioningResult2 = dataStructureDefinitionMetamacService.createVersionFromTemporalDataStructureDefinition(getServiceContextAdministrador(),
                temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), VersionTypeEnum.MAJOR);
        entityManager.clear();
        DataStructureDefinitionVersionMetamac dataStructureDefinitionNewVersion = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(),
                versioningResult2.getUrnResult());

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX02:DATASTRUCTUREDEFINITION06(" + versionExpected + ")";

        // Validate
        {
            assertEquals(ProcStatusEnum.DRAFT, dataStructureDefinitionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, dataStructureDefinitionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, dataStructureDefinitionNewVersion.getMaintainableArtefact().getUrn());

            assertEquals(null, dataStructureDefinitionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(dataStructureDefinitionNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(dataStructureDefinitionNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(dataStructureDefinitionNewVersion.getMaintainableArtefact().getLatestPublic());

            assertEquals(3, dataStructureDefinitionNewVersion.getMaintainableArtefact().getCategorisations().size());
            {
                Categorisation categorisation = assertListContainsCategorisation(dataStructureDefinitionNewVersion.getMaintainableArtefact().getCategorisations(),
                        "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX02:cat4(01.000)");
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
            }
            assertListContainsCategorisation(dataStructureDefinitionNewVersion.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX02:cat5(01.000)");
            assertListContainsCategorisation(dataStructureDefinitionNewVersion.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX02:cat6(01.000)");
        }
    }

    @Override
    @Test
    public void testMergeTemporalVersion() throws Exception {
        {
            String urn = DSD_6_V1;
            TaskInfo versioningResult = dataStructureDefinitionMetamacService.createTemporalDataStructureDefinition(getServiceContextAdministrador(), urn);

            entityManager.clear();
            DataStructureDefinitionVersionMetamac temporalDataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.retrieveDataStructureDefinitionByUrn(
                    getServiceContextAdministrador(), versioningResult.getUrnResult());
            // Change temporal version *********************

            // DataStructure: Change Name
            {
                LocalisedString localisedString = new LocalisedString("fr", "dsd - text sample");
                temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getName().addText(localisedString);
            }

            // DataStructure: Change AutoOpen
            {
                temporalDataStructureDefinitionVersionMetamac.setAutoOpen(Boolean.FALSE);
            }

            // ComponentList: Add Annotation
            {
                ComponentList componentListTemporal = temporalDataStructureDefinitionVersionMetamac.getGrouping().iterator().next();

                {
                    Annotation annotationTemporal = new Annotation();
                    annotationTemporal.setTitle("title");
                    annotationTemporal.setType("type");
                    annotationTemporal.setUrl("url");
                    InternationalString internationalString = new InternationalString();
                    internationalString.addText(new LocalisedString("fr", "cl - text sample"));
                    internationalString.addText(new LocalisedString("es", "es - cl - text sample"));
                    annotationTemporal.setText(internationalString);
                    componentListTemporal.addAnnotation(annotationTemporal);
                }

                // Component
                Component component = componentListTemporal.getComponents().iterator().next();
                {
                    Annotation annotationTemporal = new Annotation();
                    annotationTemporal.setTitle("title");
                    annotationTemporal.setType("type");
                    annotationTemporal.setUrl("url");
                    InternationalString internationalString = new InternationalString();
                    internationalString.addText(new LocalisedString("fr", "fr - annotation - text sample"));
                    internationalString.addText(new LocalisedString("es", "es - annotation - text sample"));
                    annotationTemporal.setText(internationalString);
                    component.addAnnotation(annotationTemporal);
                }
            }

            // Merge
            temporalDataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.sendDataStructureDefinitionToProductionValidation(getServiceContextAdministrador(),
                    temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn());
            temporalDataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.sendDataStructureDefinitionToDiffusionValidation(getServiceContextAdministrador(),
                    temporalDataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn());
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.mergeTemporalVersion(getServiceContextAdministrador(),
                    temporalDataStructureDefinitionVersionMetamac);

            // Assert **************************************

            // DataStructure
            assertEquals(3, dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("dsd - text sample", dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));
            assertFalse(dataStructureDefinitionVersionMetamac.getAutoOpen());
            assertNull(dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getIsTemporal());

            // ComponentList
            {
                ComponentList componentList = temporalDataStructureDefinitionVersionMetamac.getGrouping().iterator().next();

                {
                    Annotation annotation = componentList.getAnnotations().iterator().next();
                    assertEquals(2, annotation.getText().getTexts().size());
                    assertEquals("cl - text sample", annotation.getText().getLocalisedLabel("fr"));
                    assertEquals("es - cl - text sample", annotation.getText().getLocalisedLabel("es"));
                }

                Component component = componentList.getComponents().iterator().next();

                {
                    Annotation annotation = component.getAnnotations().iterator().next();
                    assertEquals(2, annotation.getText().getTexts().size());
                    assertEquals("fr - annotation - text sample", annotation.getText().getLocalisedLabel("fr"));
                    assertEquals("es - annotation - text sample", annotation.getText().getLocalisedLabel("es"));
                }
            }
        }
    }

    @Test
    @Override
    @Ignore
    public void testStartDataStructureDefinitionValidity() throws Exception {
        // TODO Test dsd
    }

    @Test
    @Override
    @Ignore
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
        PagedResult<ConceptMetamac> result = dataStructureDefinitionMetamacService.findConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptMetamac> result = dataStructureDefinitionMetamacService.findConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptMetamac> result = dataStructureDefinitionMetamacService.findConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter,
                dsdUrn);

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
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptMetamac> result = dataStructureDefinitionMetamacService.findConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptMetamac> result = dataStructureDefinitionMetamacService.findConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

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

        // Find
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdRoleByCondition(getServiceContextAdministrador(), conditions,
                pagingParameter);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_6_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = dataStructureDefinitionMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(getServiceContextAdministrador(),
                    conditions, pagingParameter, conceptUrn);

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
            PagedResult<CodelistVersionMetamac> result = dataStructureDefinitionMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(getServiceContextAdministrador(),
                    conditions, pagingParameter, conceptUrn);

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
    public void testFindConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(
                getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(1, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_7_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CodelistVersionMetamac> result = dataStructureDefinitionMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter);

        // Validate
        assertEquals(3, result.getTotalRows());
        int i = 0;
        assertEquals(CODELIST_7_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CODELIST_8_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CODELIST_9_V1, result.getValues().get(i++).getMaintainableArtefact().getUrn());
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
        PagedResult<ConceptSchemeVersionMetamac> result = dataStructureDefinitionMetamacService.findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(getServiceContextAdministrador(),
                conditions, pagingParameter, dsdUrn);

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
        PagedResult<ConceptMetamac> result = dataStructureDefinitionMetamacService.findConceptsCanBeDsdAttributeByCondition(getServiceContextAdministrador(), conditions, pagingParameter, dsdUrn);

        // Validate
        assertEquals(3, result.getTotalRows());
        int i = 0;
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_3_V1_CONCEPT_2, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CONCEPT_SCHEME_8_V1_CONCEPT_1, result.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(result.getTotalRows(), i);
    }

    @Override
    @Test
    public void testFindCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        {
            // Concept has Variable 1
            String conceptUrn = CONCEPT_SCHEME_3_V1_CONCEPT_2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).orderBy(CodelistVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagedResult<CodelistVersionMetamac> result = dataStructureDefinitionMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(getServiceContextAdministrador(),
                    conditions, pagingParameter, conceptUrn);

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
            PagedResult<CodelistVersionMetamac> result = dataStructureDefinitionMetamacService.findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(getServiceContextAdministrador(),
                    conditions, pagingParameter, conceptUrn);

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
    public void testFindOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        {

            String dimensionUrn = DSD_6_V1_DIMENSION_1;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistOrderVisualisation.class).orderBy(CodelistOrderVisualisationProperties.nameableArtefact().urn())
                    .build();
            PagedResult<CodelistOrderVisualisation> result = dataStructureDefinitionMetamacService.findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(getServiceContextAdministrador(),
                    conditions, pagingParameter, dimensionUrn);

            // Validate
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_7_V1_ORDER_VISUALISATION_01_ALPHABETICAL, result.getValues().get(i).getNameableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, result.getValues().get(i).getCodelistVersion().getMaintainableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testFindOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition() throws Exception {
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);

        // Find
        {

            String dimensionUrn = DSD_6_V1_DIMENSION_1;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistOrderVisualisation.class).orderBy(CodelistOrderVisualisationProperties.nameableArtefact().urn())
                    .build();
            PagedResult<CodelistOpennessVisualisation> result = dataStructureDefinitionMetamacService.findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(
                    getServiceContextAdministrador(), conditions, pagingParameter, dimensionUrn);

            // Validate
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(CODELIST_7_V1_OPENNESS_VISUALISATION_01_ALL_EXPANDED, result.getValues().get(i).getNameableArtefact().getUrn());
            assertEquals(CODELIST_7_V1, result.getValues().get(i).getCodelistVersion().getMaintainableArtefact().getUrn());
        }
    }

    @Override
    @Ignore
    @Test
    public void testVersioningHeadingAndStub() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    @Test
    public void testVersioningShowDecimalsPrecision() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    @Test
    public void testVersioningDimensionVisualisationInfo() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    @Test
    public void testCheckPrimaryMeasure() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    @Test
    public void testCheckTimeDimension() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    public void testCheckMeasureDimension() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    @Test
    public void testCheckDimension() throws Exception {
        // TODO Test dsd
    }

    @Override
    @Ignore
    @Test
    public void testCheckAttribute() throws Exception {
        // TODO Test dsd
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
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = dataStructureDefinitionMetamacService.createDataStructureDefinition(getServiceContext(),
                DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac, "op8"));
        dataStructureDefinitionVersionMetamac.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Create Dimension Descriptor and components
        Concept concept01 = conceptMetamacRepository.findByUrn(CONCEPT_SCHEME_1_V2_CONCEPT_2);
        Concept concept02 = conceptMetamacRepository.findByUrn(CONCEPT_SCHEME_1_V2_CONCEPT_3);
        ConceptSchemeVersion conceptScheme = conceptSchemeVersionMetamacRepository.findByUrn(CONCEPT_SCHEME_3_V1);
        Component measureDim = DataStructureDefinitionDoMocks.mockMeasureDimension(concept01, Arrays.asList(concept01, concept02), conceptScheme);
        Component measureDimCreated = dataStructureDefinitionMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac
                .getMaintainableArtefact().getUrn(), measureDim);

        CodelistVersion codelist = codelistVersionMetamacRepository.findByUrn(CODELIST_7_V1);
        Component dim = DataStructureDefinitionDoMocks.mockDimension(concept01, Arrays.asList(concept01, concept02), codelist);
        Component dimCreated = dataStructureDefinitionMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact()
                .getUrn(), dim);

        Component timeDim = DataStructureDefinitionDoMocks.mockTimeDimension(concept01);
        /* Component timeDimCreated = */dataStructureDefinitionMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac
                .getMaintainableArtefact().getUrn(), timeDim);

        // Create GroupDimension Descriptor
        ComponentList groupDescriptor = DataStructureDefinitionDoMocks.mockGroupDimensionDescriptor((DimensionComponent) measureDimCreated);
        ComponentList groupDescriptorCreated = dataStructureDefinitionMetamacService.saveDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac
                .getMaintainableArtefact().getUrn(), groupDescriptor);

        // Create Attribute Descriptor and components
        Component dataAttribute = DataStructureDefinitionDoMocks.mockDataAttribute((GroupDimensionDescriptor) groupDescriptorCreated, (DimensionComponent) dimCreated, concept01,
                Arrays.asList(concept01, concept02), codelist);
        /* Component dataAttributeCreated = */dataStructureDefinitionMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac
                .getMaintainableArtefact().getUrn(), dataAttribute);

        // Create Measure Descriptor and component
        Component primaryMeasure = DataStructureDefinitionDoMocks.mockPrimaryMeasure(concept01, codelist);
        /* Component componentCreated = */dataStructureDefinitionMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac
                .getMaintainableArtefact().getUrn(), primaryMeasure);

        return dataStructureDefinitionVersionMetamac;
    }

}

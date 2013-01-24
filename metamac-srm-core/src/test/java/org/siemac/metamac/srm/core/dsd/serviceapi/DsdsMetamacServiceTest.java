package org.siemac.metamac.srm.core.dsd.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
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

    private static String                 DSD_06_URN     = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION06(01.000)";

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
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(AGENCY_ROOT_2_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(AGENCY_ROOT_1_V1, e.getExceptionItems().get(0).getMessageParameters()[1]);
        } catch (Exception e) {
            int kaa = 2;
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
        // TODO Test dsd

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
        String urn = DSD_06_URN;
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

package org.siemac.metamac.srm.core.dsd.serviceapi;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class DsdsMetamacServiceTest extends SrmBaseTest implements DsdsMetamacServiceTestBase {

    @Autowired
    protected DsdsMetamacService dsdsMetamacService;
    
    @Autowired
    protected DataStructureDefinitionService dataStructureDefinitionService;
    
    private final ServiceContext serviceContext = new ServiceContext("system", "123456", "junit");

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }
    
    @Test
    @Override
    public void testCreateDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testUpdateDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionByUrn() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionVersions() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testSaveDescriptorForDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testDeleteDescriptorForDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testSaveComponentForDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testDeleteComponentForDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testSendDataStructureDefinitionToProductionValidation() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testSendDataStructureDefinitionToDiffusionValidation() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testRejectDataStructureDefinitionProductionValidation() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testRejectDataStructureDefinitionDiffusionValidation() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testPublishInternallyDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testPublishExternallyDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testDeleteDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testVersioningDataStructureDefinition() throws Exception {
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacToCopy = createDataStructureDefinitionGraph();
        
        dsdsMetamacService.sendDataStructureDefinitionToProductionValidation(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamacToCopy.getMaintainableArtefact().getUrn());
        dsdsMetamacService.sendDataStructureDefinitionToDiffusionValidation(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamacToCopy.getMaintainableArtefact().getUrn());
        dsdsMetamacService.publishInternallyDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamacToCopy.getMaintainableArtefact().getUrn());
        
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacCreated = dsdsMetamacService.versioningDataStructureDefinition(getServiceContextAdministrador(), dataStructureDefinitionVersionMetamacToCopy.getMaintainableArtefact().getUrn(), VersionTypeEnum.MAJOR);
    
        assertNotNull(dataStructureDefinitionVersionMetamacCreated);
    }

    @Test
    @Override
    public void testCancelDataStructureDefinitionValidity() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }

    /************************************************************************************
     * 
     * PRIVATE
     * 
     ************************************************************************************/
    private DataStructureDefinitionVersionMetamac createDataStructureDefinitionGraph() throws MetamacException {

        // Create DSD
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = dsdsMetamacService.createDataStructureDefinition(getServiceContext(), DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac());
        dataStructureDefinitionVersionMetamac.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Create Dimension Descriptor and components
        Component measureDim = DataStructureDefinitionDoMocks.createMeasureDimension();
        Component measureDimCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), measureDim,
                TypeComponentList.DIMENSION_DESCRIPTOR);

        Component dim = DataStructureDefinitionDoMocks.createDimension();
        Component dimCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), dim,
                TypeComponentList.DIMENSION_DESCRIPTOR);

        Component timeDim = DataStructureDefinitionDoMocks.createTimeDimension();
        Component timeDimCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), timeDim,
                TypeComponentList.DIMENSION_DESCRIPTOR);

        // Create GroupDimension Descriptor
        ComponentList groupDescriptor = DataStructureDefinitionDoMocks.createGroupDimensionDescriptor((DimensionComponent) measureDimCreated);
        ComponentList groupDescriptorCreated = dsdsMetamacService.saveDescriptorForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(),
                groupDescriptor);

        // Create Attribute Descriptor and components
        Component dataAttribute = DataStructureDefinitionDoMocks.createDataAttribute((GroupDimensionDescriptor) groupDescriptorCreated, (DimensionComponent) dimCreated);
        Component dataAttributeCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(),
                dataAttribute, TypeComponentList.ATTRIBUTE_DESCRIPTOR);

        // Create Measure Descriptor and component
        Component primaryMeasure = DataStructureDefinitionDoMocks.createPrimaryMeasure();
        Component componentCreated = dsdsMetamacService.saveComponentForDataStructureDefinition(getServiceContext(), dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getUrn(), primaryMeasure,
                TypeComponentList.MEASURE_DESCRIPTOR);

        return dataStructureDefinitionVersionMetamac;
    }

}

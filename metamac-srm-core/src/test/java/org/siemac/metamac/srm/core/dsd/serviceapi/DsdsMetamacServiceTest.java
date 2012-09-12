package org.siemac.metamac.srm.core.dsd.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class DsdsMetamacServiceTest extends AbstractDbUnitJpaTests
    implements DsdsMetamacServiceTestBase {
    @Autowired
    protected DsdsMetamacService dsdsMetamacService;

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
        // TODO Auto-generated method stub
        
    }

    @Test
    @Override
    public void testCancelDataStructureDefinitionValidity() throws Exception {
        // TODO Auto-generated method stub
        
    }
   
}

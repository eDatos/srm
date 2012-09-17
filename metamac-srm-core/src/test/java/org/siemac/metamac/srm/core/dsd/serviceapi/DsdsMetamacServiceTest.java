package org.siemac.metamac.srm.core.dsd.serviceapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class DsdsMetamacServiceTest extends SrmBaseTest
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

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
   
}

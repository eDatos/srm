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
    public void testSendDataStructureDefinitionToProductionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testSendDataStructureDefinitionToProductionValidation not implemented");
    }

    @Test
    public void testSendDataStructureDefinitionToDiffusionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testSendDataStructureDefinitionToDiffusionValidation not implemented");
    }

    @Test
    public void testRejectDataStructureDefinitionProductionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testRejectDataStructureDefinitionProductionValidation not implemented");
    }

    @Test
    public void testRejectDataStructureDefinitionDiffusionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testRejectDataStructureDefinitionDiffusionValidation not implemented");
    }

    @Test
    public void testPublishInternallyDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail("testPublishInternallyDataStructureDefinition not implemented");
    }

    @Test
    public void testPublishExternallyDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail("testPublishExternallyDataStructureDefinition not implemented");
    }

    @Test
    public void testDeleteDataStructureDefinitionVersionMetamac()
        throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteDataStructureDefinitionVersionMetamac not implemented");
    }

    @Test
    public void testVersioningDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail("testVersioningDataStructureDefinition not implemented");
    }

    @Test
    public void testCancelDataStructureDefinitionVersionMetamacValidity()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testCancelDataStructureDefinitionVersionMetamacValidity not implemented");
    }

    @Override
    public void testRetrieveDataStructureDefinitionByUrn() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testRetrieveDataStructureDefinitionVersions() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        // TODO Auto-generated method stub
        
    }
}

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
    public void testSendDataStructureDefinitionToProductionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testSendDataStructureDefinitionToProductionValidation not implemented");
    }

    @Test
    @Override
    public void testSendDataStructureDefinitionToDiffusionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testSendDataStructureDefinitionToDiffusionValidation not implemented");
    }

    @Test
    @Override
    public void testRejectDataStructureDefinitionProductionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testRejectDataStructureDefinitionProductionValidation not implemented");
    }

    @Test
    @Override
    public void testRejectDataStructureDefinitionDiffusionValidation()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testRejectDataStructureDefinitionDiffusionValidation not implemented");
    }

    @Test
    @Override
    public void testPublishInternallyDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail("testPublishInternallyDataStructureDefinition not implemented");
    }

    @Test
    @Override
    public void testPublishExternallyDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail("testPublishExternallyDataStructureDefinition not implemented");
    }

    @Test
    @Override
    public void testDeleteDataStructureDefinitionVersionMetamac()
        throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteDataStructureDefinitionVersionMetamac not implemented");
    }

    @Test
    @Override
    public void testVersioningDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail("testVersioningDataStructureDefinition not implemented");
    }

    @Test
    @Override
    public void testCancelDataStructureDefinitionVersionMetamacValidity()
        throws Exception {
        // TODO Auto-generated method stub
        fail(
            "testCancelDataStructureDefinitionVersionMetamacValidity not implemented");
    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionByUrn() throws Exception {
        // TODO Auto-generated method stub
        fail(
        "testRetrieveDataStructureDefinitionByUrn not implemented");
    }

    @Test
    @Override
    public void testRetrieveDataStructureDefinitionVersions() throws Exception {
        // TODO Auto-generated method stub
        fail(
        "testRetrieveDataStructureDefinitionVersions not implemented");
    }

    @Test
    @Override
    public void testFindDataStructureDefinitionsByCondition() throws Exception {
        // TODO Auto-generated method stub
        fail(
        "testFindDataStructureDefinitionsByCondition not implemented");
    }

    @Override
    public void testCreateDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail(
        "testCreateDataStructureDefinition not implemented");
    }

    @Override
    public void testUpdateDataStructureDefinition() throws Exception {
        // TODO Auto-generated method stub
        fail(
        "testUpdateDataStructureDefinition not implemented");
    }

}

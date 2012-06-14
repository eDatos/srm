package org.siemac.metamac.srm.core.base.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class SdmxBaseServiceTest extends AbstractDbUnitJpaTests
    implements SdmxBaseServiceTestBase {
    @Autowired
    protected SdmxBaseService sdmxBaseService;

    @Test
    public void testSaveComponent() throws Exception {
        // TODO Auto-generated method stub
        fail("testSaveComponent not implemented");
    }

    @Test
    public void testDeleteComponent() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteComponent not implemented");
    }

    @Test
    public void testFindAllComponent() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindAllComponent not implemented");
    }

    @Test
    public void testFindComponentListById() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindComponentListById not implemented");
    }

    @Test
    public void testSaveComponentList() throws Exception {
        // TODO Auto-generated method stub
        fail("testSaveComponentList not implemented");
    }

    @Test
    public void testDeleteComponentList() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteComponentList not implemented");
    }

    @Test
    public void testFindAllComponentList() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindAllComponentList not implemented");
    }

    @Test
    public void testPopulateAssociationsComponentList() throws Exception {
        // TODO Auto-generated method stub
        fail("testPopulateAssociationsComponentList not implemented");
    }
}

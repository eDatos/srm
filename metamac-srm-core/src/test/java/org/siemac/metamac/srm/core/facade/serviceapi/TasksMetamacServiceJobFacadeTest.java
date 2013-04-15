package org.siemac.metamac.srm.core.facade.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TasksMetamacServiceJobFacadeTest extends AbstractDbUnitJpaTests
    implements TasksMetamacServiceJobFacadeTestBase {
    @Autowired
    protected TasksMetamacServiceJobFacade tasksMetamacServiceJobFacade;

    @Test
    public void testImportCodesCsv() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportCodesCsv not implemented");
    }

    @Test
    public void testImportCodeOrdersCsv() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportCodeOrdersCsv not implemented");
    }

    @Test
    public void testImportVariableElementsCsv() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportVariableElementsCsv not implemented");
    }

    @Test
    public void testMarkTaskAsFailed() throws Exception {
        // TODO Auto-generated method stub
        fail("testMarkTaskAsFailed not implemented");
    }
}

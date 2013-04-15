package org.siemac.metamac.srm.core.task.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TasksMetamacServiceTest extends AbstractDbUnitJpaTests
    implements TasksMetamacServiceTestBase {
    @Autowired
    protected TasksMetamacService tasksMetamacService;

    @Test
    public void testImportSDMXStructureInBackground() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportSDMXStructureInBackground not implemented");
    }

    @Test
    public void testImportCodesCsvInBackground() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportCodesCsvInBackground not implemented");
    }

    @Test
    public void testImportCodeOrdersCsvInBackground() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportCodeOrdersCsvInBackground not implemented");
    }

    @Test
    public void testImportVariableElementsCsvInBackground() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportVariableElementsCsvInBackground not implemented");
    }

    @Test
    public void testRetrieveTaskByJob() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveTaskByJob not implemented");
    }

    @Test
    public void testMarkTaskAsFinished() throws Exception {
        // TODO Auto-generated method stub
        fail("testMarkTaskAsFinished not implemented");
    }

    @Test
    public void testMarkTaskAsFailed() throws Exception {
        // TODO Auto-generated method stub
        fail("testMarkTaskAsFailed not implemented");
    }

    @Test
    public void testFindTasksByCondition() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindTasksByCondition not implemented");
    }
}

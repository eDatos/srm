package com.arte.statistic.sdmx.srm.core.task.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TasksServiceTest extends AbstractDbUnitJpaTests
    implements TasksServiceTestBase {
    @Autowired
    protected TasksService tasksService;

    @Test
    public void testPreviewImportSDMXStructure() throws Exception {
        // TODO Auto-generated method stub
        fail("testPreviewImportSDMXStructure not implemented");
    }

    @Test
    public void testImportSDMXStructure() throws Exception {
        // TODO Auto-generated method stub
        fail("testImportSDMXStructure not implemented");
    }

    @Test
    public void testProcessImportationTask() throws Exception {
        // TODO Auto-generated method stub
        fail("testProcessImportationTask not implemented");
    }

    @Test
    public void testCreateTask() throws Exception {
        // TODO Auto-generated method stub
        fail("testCreateTask not implemented");
    }

    @Test
    public void testUpdateTask() throws Exception {
        // TODO Auto-generated method stub
        fail("testUpdateTask not implemented");
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

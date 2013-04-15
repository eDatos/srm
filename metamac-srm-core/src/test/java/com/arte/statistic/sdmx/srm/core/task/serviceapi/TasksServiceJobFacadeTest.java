package com.arte.statistic.sdmx.srm.core.task.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TasksServiceJobFacadeTest extends AbstractDbUnitJpaTests
    implements TasksServiceJobFacadeTestBase {
    @Autowired
    protected TasksServiceJobFacade tasksServiceJobFacade;

    @Test
    public void testProcessImportationTask() throws Exception {
        // TODO Auto-generated method stub
        fail("testProcessImportationTask not implemented");
    }

    @Test
    public void testMarkTaskAsFailed() throws Exception {
        // TODO Auto-generated method stub
        fail("testMarkTaskAsFailed not implemented");
    }

    @Test
    public void testMarkAllInProgressJobToFailed() throws Exception {
        // TODO Auto-generated method stub
        fail("testMarkAllInProgressJobToFailed not implemented");
    }
}

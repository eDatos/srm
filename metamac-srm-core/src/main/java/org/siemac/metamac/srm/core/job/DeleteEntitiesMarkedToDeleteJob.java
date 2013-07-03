package org.siemac.metamac.srm.core.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.TasksMetamacServiceFacade;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DeleteEntitiesMarkedToDeleteJob extends QuartzJobBean {

    private static Log                      log = LogFactory.getLog(DeleteEntitiesMarkedToDeleteJob.class);
    private final ServiceContext            ctx = new ServiceContext("DeleteEntitiesMarkedToDeleteJob", "DeleteEntitiesMarkedToDeleteJob", "DeleteEntitiesMarkedToDeleteJob");

    private final TasksMetamacServiceFacade tasksMetamacServiceFacade;

    public DeleteEntitiesMarkedToDeleteJob() {
        tasksMetamacServiceFacade = ApplicationContextProvider.getApplicationContext().getBean(TasksMetamacServiceFacade.class);
    }

    @Override
    protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
        log.info("Job started: Deleting rows marked to delete");
        try {
            tasksMetamacServiceFacade.deleteEntitiesMarkedToDelete(ctx);
            log.info("Job finished: Deleting rows marked to delete");
        } catch (MetamacException e) {
            log.error("Job ERROR: Deleting rows marked to delete", e);
        }
    }
}
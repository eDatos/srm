package org.siemac.metamac.srm.core.task.serviceimpl;

import java.net.URL;
import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.TasksMetamacServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ImportationShapeJob implements Job {

    private static Logger             logger                    = LoggerFactory.getLogger(ImportationShapeJob.class);

    // Job Context Param
    public static final String        USER                      = "user";
    public static final String        VARIABLE_URN              = "variableUrn";
    public static final String        SHAPEFILE_URL             = "shapefileUrl";

    private TasksMetamacServiceFacade tasksMetamacServiceFacade = null;

    public TasksMetamacServiceFacade getTaskMetamacServiceFacade() {
        if (tasksMetamacServiceFacade == null) {
            tasksMetamacServiceFacade = (TasksMetamacServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TasksMetamacServiceFacade.BEAN_ID);
        }
        return tasksMetamacServiceFacade;
    }

    @Override
    public void execute(JobExecutionContext context) {

        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap data = context.getJobDetail().getJobDataMap();
        ServiceContext serviceContext = new ServiceContext(data.getString(USER), context.getFireInstanceId(), "sdmx-srm-core");
        serviceContext.setProperty(SdmxConstants.SERVICE_CONTEXT_PROP_IS_JOB_INVOCATION, Boolean.TRUE);

        try {
            // Parameters
            String variableUrn = data.getString(VARIABLE_URN);
            URL shapeFileUrl = new URL(data.getString(SHAPEFILE_URL));

            logger.info("ImportationShapeJob: Import " + shapeFileUrl + ", job " + jobKey + " starting at " + new Date());
            getTaskMetamacServiceFacade().processImportVariableElementsShape(serviceContext, variableUrn, shapeFileUrl, jobKey.getName());
            logger.info("ImportationShapeJob: Import " + shapeFileUrl + ", job " + jobKey + " finished at " + new Date());
            // TODO sistema de avisos
        } catch (Exception e) {
            // TODO sistema de avisos
            logger.error("ImportationShapeJob: job with key " + jobKey.getName() + " has failed", e);
            try {
                getTaskMetamacServiceFacade().markTaskAsFailed(serviceContext, jobKey.getName(), e);
            } catch (MetamacException e1) {
                logger.error("ImportationShapeJob: job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
            }
        }
    }
}

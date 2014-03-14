package org.siemac.metamac.srm.core.task.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.code.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.facade.serviceapi.TasksMetamacServiceFacade;
import org.siemac.metamac.srm.core.notices.ServiceNoticeAction;
import org.siemac.metamac.srm.core.notices.ServiceNoticeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MergeCodelistJob implements Job {

    private static Logger             logger                    = LoggerFactory.getLogger(MergeCodelistJob.class);

    // Job Context Param
    public static final String        USER                      = "user";
    public static final String        URN                       = "urn";
    public static final String        FORCE_LATEST_FINAL        = "forceLatestFinal";

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
        String urnToCopy = data.getString(URN);
        Boolean forceLatestFinal = data.getBoolean(FORCE_LATEST_FINAL);
        String user = data.getString(USER);

        try {
            logger.info("MergingJob: Copy " + urnToCopy + ", job " + jobKey + " starting at " + new Date());
            getTaskMetamacServiceFacade().processPublishInternallyCodelist(serviceContext, urnToCopy, forceLatestFinal, jobKey.getName());
            logger.info("MergingJob: Urn to copy " + urnToCopy + ", job " + jobKey + " finished at " + new Date());

            getNoticesRestInternalService().createSuccessBackgroundNotification(user, ServiceNoticeAction.PUBLISH_INTERNALLY_CODELIST_JOB, ServiceNoticeMessage.PUBLISH_INTERNALLY_CODELIST_JOB_OK,
                    urnToCopy);
        } catch (MetamacException e) {
            logger.error("MergingJob: job with key " + jobKey.getName() + " has failed", e);
            try {
                getTaskMetamacServiceFacade().markTaskAsFailed(serviceContext, jobKey.getName(), e);
                getTaskMetamacServiceFacade().markTaskItemSchemeAsFailed(serviceContext, urnToCopy);

                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.PUBLISH_INTERNALLY_CODELIST_JOB_ERROR, urnToCopy));
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.PUBLISH_INTERNALLY_CODELIST_JOB, e);
            } catch (MetamacException e1) {
                logger.error("MergingJob: job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.PUBLISH_INTERNALLY_CODELIST_JOB_ERROR_AND_CANT_MARK_AS_ERROR, urnToCopy));
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.PUBLISH_INTERNALLY_CODELIST_JOB, e1);
            }

        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}

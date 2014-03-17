package org.siemac.metamac.srm.core.task.serviceimpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ExceptionHelper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.code.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.facade.serviceapi.TasksMetamacServiceFacade;
import org.siemac.metamac.srm.core.notices.ServiceNoticeAction;
import org.siemac.metamac.srm.core.notices.ServiceNoticeMessage;
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
    public static final String        OPERATION                 = "operation";

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
        String user = data.getString(USER);
        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "sdmx-srm-core");
        serviceContext.setProperty(SdmxConstants.SERVICE_CONTEXT_PROP_IS_JOB_INVOCATION, Boolean.TRUE);
        String variableUrn = data.getString(VARIABLE_URN);
        String shapeFile = data.getString(SHAPEFILE_URL);
        String operation = data.getString(OPERATION);

        try {
            URL shapeFileUrl = new URL(shapeFile);

            // Parameters
            logger.info("ImportationShapeJob: Import " + shapeFileUrl + ", job " + jobKey + " starting at " + new Date());
            if (SrmConstants.SHAPE_OPERATION_IMPORT_SHAPES.equals(operation)) {
                getTaskMetamacServiceFacade().processImportVariableElementsShape(serviceContext, variableUrn, shapeFileUrl, jobKey.getName());
            } else if (SrmConstants.SHAPE_OPERATION_IMPORT_POINTS.equals(operation)) {
                getTaskMetamacServiceFacade().processImportVariableElementsPoints(serviceContext, variableUrn, shapeFileUrl, jobKey.getName());
            } else {
                throw new IllegalArgumentException("Job with operation " + operation + " is not supported");
            }
            logger.info("ImportationShapeJob: Import " + shapeFileUrl + ", job " + jobKey + " finished at " + new Date());
            getNoticesRestInternalService().createSuccessBackgroundNotification(user, ServiceNoticeAction.IMPORT_SHAPE_JOB, ServiceNoticeMessage.IMPORT_SHAPE_JOB_OK, shapeFile);
        } catch (Exception e) {
            // Concert parser exception to metamac exception
            MetamacException metamacException = null;
            if (e instanceof MetamacException) {
                metamacException = (MetamacException) e;
                logger.error("ImportationShapeJob: job with key " + jobKey.getName() + " has failed", metamacException);
            } else if (e instanceof MalformedURLException) {
                metamacException = MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(ExceptionHelper.excMessage(e)).build();
                logger.info("ImportationShapeJob: Import " + shapeFile + ", job " + jobKey + " could not be executed because shapeFileUrl is malformed");
            }

            try {
                getTaskMetamacServiceFacade().markTaskAsFailed(serviceContext, jobKey.getName(), metamacException);
                metamacException.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORT_SHAPE_JOB_ERROR, shapeFile));
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.IMPORT_SHAPE_JOB, metamacException);
            } catch (MetamacException e1) {
                logger.error("ImportationShapeJob: job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
                metamacException.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORT_SHAPE_JOB_ERROR_AND_CANT_MARK_AS_ERROR, shapeFile));
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.IMPORT_SHAPE_JOB, e1);
            }
        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}

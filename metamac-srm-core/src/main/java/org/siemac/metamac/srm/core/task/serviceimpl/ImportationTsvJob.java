package org.siemac.metamac.srm.core.task.serviceimpl;

import java.io.File;
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
public class ImportationTsvJob implements Job {

    private static Logger             logger                             = LoggerFactory.getLogger(ImportationTsvJob.class);

    // Job Context Param
    public static final String        USER                               = "user";
    public static final String        FILE_PATH                          = "filePath";
    public static final String        FILE_NAME                          = "fileName";
    public static final String        VARIABLE_URN                       = "variableUrn";
    public static final String        CODELIST_URN                       = "codelistUrn";
    public static final String        UPDATE_ALREADY_EXISTING            = "updateAlreadyExisting";
    public static final String        OPERATION                          = "operation";
    public static final String        OPERATION_IMPORT_CODES             = "importCodes";
    public static final String        OPERATION_IMPORT_CODE_ORDERS       = "importCodeOrdes";
    public static final String        OPERATION_IMPORT_VARIABLE_ELEMENTS = "importVariableElements";

    private TasksMetamacServiceFacade tasksMetamacServiceFacade          = null;

    private final ServiceContext      serviceContextDefault              = new ServiceContext("importationTsvJob", "importationTsvJob", "metamac-srm");

    public TasksMetamacServiceFacade getTaskMetamacServiceFacade() {
        if (tasksMetamacServiceFacade == null) {
            tasksMetamacServiceFacade = (TasksMetamacServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TasksMetamacServiceFacade.BEAN_ID);
        }
        return tasksMetamacServiceFacade;
    }

    @Override
    public void execute(JobExecutionContext context) {

        JobKey jobKey = context.getJobDetail().getKey();
        ServiceContext serviceContext = null;
        JobDataMap data = context.getJobDetail().getJobDataMap();
        try {

            // Parameters
            String user = data.getString(USER);
            serviceContext = new ServiceContext(user, context.getFireInstanceId(), "sdmx-srm-core");
            serviceContext.setProperty(SdmxConstants.SERVICE_CONTEXT_PROP_IS_JOB_INVOCATION, Boolean.TRUE);
            String operation = data.getString(OPERATION);
            if (OPERATION_IMPORT_CODES.equals(operation)) {
                importCodes(jobKey, data, serviceContext);
            } else if (OPERATION_IMPORT_CODE_ORDERS.equals(operation)) {
                importCodeOrders(jobKey, data, serviceContext);
            } else if (OPERATION_IMPORT_VARIABLE_ELEMENTS.equals(operation)) {
                importVariableElements(jobKey, data, serviceContext);
            } else {
                throw new IllegalArgumentException("Job with operation " + operation + " is not supported");
            }
        } catch (Exception e) {
            logger.error("ImportationJob: the importation with key " + jobKey.getName() + " has failed", e);
            try {
                if (serviceContext == null) {
                    serviceContext = serviceContextDefault;
                }
                getTaskMetamacServiceFacade().markTaskAsFailed(serviceContext, jobKey.getName(), e);
                // TODO sistema de avisos

                String codelistUrn = data.getString(CODELIST_URN);
                if (codelistUrn != null) {
                    getTaskMetamacServiceFacade().markTaskItemSchemeAsFailed(serviceContext, codelistUrn);
                }
            } catch (MetamacException e1) {
                logger.error("ImportationJob: the importation with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
            }
        }
    }

    private void importVariableElements(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws Exception {

        // Parameters
        String variableUrn = data.getString(VARIABLE_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        // Execution
        logger.info("ImportationJob [importVariableElements]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportVariableElementsTsv(serviceContext, variableUrn, new File(filePath), fileName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importVariableElements]: " + jobKey + " finished at " + new Date());
    }

    private void importCodes(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws Exception {

        // Parameters
        String codelistUrn = data.getString(CODELIST_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        logger.info("ImportationJob [importCodes]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportCodesTsv(serviceContext, codelistUrn, new File(filePath), fileName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importCodes]: " + jobKey + " finished at " + new Date());
    }

    private void importCodeOrders(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws Exception {

        // Parameters
        String codelistUrn = data.getString(CODELIST_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);

        // Execution
        logger.info("ImportationJob [importCodeOrders]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportCodeOrdersTsv(serviceContext, codelistUrn, new File(filePath), fileName, jobKey.getName());
        logger.info("ImportationJob [importCodeOrders]: " + jobKey + " finished at " + new Date());
    }
}

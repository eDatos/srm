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
public class ImportationTsvJob implements Job {

    private static Logger             logger                             = LoggerFactory.getLogger(ImportationTsvJob.class);

    // Job Context Param
    public static final String        USER                               = "user";
    public static final String        FILE_PATH                          = "filePath";
    public static final String        FILE_NAME                          = "fileName";
    public static final String        VARIABLE_URN                       = "variableUrn";
    public static final String        ITEM_SCHEME_URN                    = "codelistUrn";
    public static final String        UPDATE_ALREADY_EXISTING            = "updateAlreadyExisting";
    public static final String        OPERATION                          = "operation";
    public static final String        OPERATION_IMPORT_CONCEPTS          = "importConcepts";
    public static final String        OPERATION_IMPORT_ORGANISATIONS     = "importOrganisations";
    public static final String        OPERATION_IMPORT_CODES             = "importCodes";
    public static final String        OPERATION_IMPORT_CODE_ORDERS       = "importCodeOrdes";
    public static final String        OPERATION_IMPORT_VARIABLE_ELEMENTS = "importVariableElements";

    private TasksMetamacServiceFacade tasksMetamacServiceFacade          = null;

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
        String urnToCopy = data.getString(ITEM_SCHEME_URN);
        String user = data.getString(USER);
        String fileName = data.getString(FILE_NAME);
        serviceContext = new ServiceContext(user, context.getFireInstanceId(), "sdmx-srm-core");
        serviceContext.setProperty(SdmxConstants.SERVICE_CONTEXT_PROP_IS_JOB_INVOCATION, Boolean.TRUE);

        try {
            String operation = data.getString(OPERATION);
            if (OPERATION_IMPORT_CONCEPTS.equals(operation)) {
                importConcepts(jobKey, data, serviceContext);
            } else if (OPERATION_IMPORT_ORGANISATIONS.equals(operation)) {
                importOrganisations(jobKey, data, serviceContext);
            } else if (OPERATION_IMPORT_CODES.equals(operation)) {
                importCodes(jobKey, data, serviceContext);
            } else if (OPERATION_IMPORT_CODE_ORDERS.equals(operation)) {
                importCodeOrders(jobKey, data, serviceContext);
            } else if (OPERATION_IMPORT_VARIABLE_ELEMENTS.equals(operation)) {
                importVariableElements(jobKey, data, serviceContext);
            } else {
                throw new IllegalArgumentException("Job with operation " + operation + " is not supported");
            }
            logger.info("CopyJob: Urn to copy " + urnToCopy + ", job " + jobKey + " finished at " + new Date());
            getNoticesRestInternalService().createSuccessBackgroundNotification(user, ServiceNoticeAction.IMPORT_TSV_JOB, ServiceNoticeMessage.IMPORT_TSV_JOB_OK, fileName);
        } catch (MetamacException e) {
            logger.error("ImportationJob: the importation with key " + jobKey.getName() + " has failed", e);
            try {
                getTaskMetamacServiceFacade().markTaskAsFailed(serviceContext, jobKey.getName(), e);
                if (urnToCopy != null) {
                    getTaskMetamacServiceFacade().markTaskItemSchemeAsFailed(serviceContext, urnToCopy);
                }

                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORT_TSV_JOB_ERROR, fileName));
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.IMPORT_TSV_JOB, e);
            } catch (MetamacException e1) {
                logger.error("ImportationJob: the importation with key " + jobKey.getName() + " has failed and it can't marked as error", e1);

                e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORT_TSV_JOB_ERROR_AND_CANT_MARK_AS_ERROR, fileName));
                getNoticesRestInternalService().createErrorBackgroundNotification(user, ServiceNoticeAction.IMPORT_TSV_JOB, e1);
            }
        }
    }

    private void importVariableElements(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws MetamacException {

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

    private void importConcepts(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws MetamacException {

        // Parameters
        String conceptSchemeUrn = data.getString(ITEM_SCHEME_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        logger.info("ImportationJob [importConcepts]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportConceptsTsv(serviceContext, conceptSchemeUrn, new File(filePath), fileName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importConcepts]: " + jobKey + " finished at " + new Date());
    }

    private void importOrganisations(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws MetamacException {

        // Parameters
        String organisationSchemeUrn = data.getString(ITEM_SCHEME_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        logger.info("ImportationJob [importOrganisations]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportOrganisationsTsv(serviceContext, organisationSchemeUrn, new File(filePath), fileName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importOrganisations]: " + jobKey + " finished at " + new Date());
    }

    private void importCodes(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws MetamacException {

        // Parameters
        String codelistUrn = data.getString(ITEM_SCHEME_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        logger.info("ImportationJob [importCodes]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportCodesTsv(serviceContext, codelistUrn, new File(filePath), fileName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importCodes]: " + jobKey + " finished at " + new Date());
    }

    private void importCodeOrders(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws MetamacException {

        // Parameters
        String codelistUrn = data.getString(ITEM_SCHEME_URN);
        String filePath = data.getString(FILE_PATH);
        String fileName = data.getString(FILE_NAME);

        // Execution
        logger.info("ImportationJob [importCodeOrders]: " + jobKey + " starting at " + new Date());
        getTaskMetamacServiceFacade().processImportCodeOrdersTsv(serviceContext, codelistUrn, new File(filePath), fileName, jobKey.getName());
        logger.info("ImportationJob [importCodeOrders]: " + jobKey + " finished at " + new Date());
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}

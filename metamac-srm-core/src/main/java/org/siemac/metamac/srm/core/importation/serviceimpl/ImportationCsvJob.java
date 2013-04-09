package org.siemac.metamac.srm.core.importation.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.ImportationMetamacServiceJobFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ImportationCsvJob implements Job {

    private static Logger                      logger                             = LoggerFactory.getLogger(ImportationCsvJob.class);

    // Job Context Param
    public static final String                 USER                               = "user";
    public static final String                 FILE_PATH                          = "filePath";
    public static final String                 FILE_IMPORTED_NAME                 = "fileImportedName";
    public static final String                 VARIABLE_URN                       = "variableUrn";
    public static final String                 CODELIST_URN                       = "codelistUrn";
    public static final String                 UPDATE_ALREADY_EXISTING            = "updateAlreadyExisting";
    public static final String                 OPERATION                          = "operation";
    public static final String                 OPERATION_IMPORT_CODES             = "importCodes";
    public static final String                 OPERATION_IMPORT_CODE_ORDERS       = "importCodeOrdes";
    public static final String                 OPERATION_IMPORT_VARIABLE_ELEMENTS = "importVariableElements";

    private ImportationMetamacServiceJobFacade importationMetamacServiceJobFacade = null;

    private final ServiceContext               serviceContextDefault              = new ServiceContext("importationCsvJob", "importationCsvJob", "metamac-srm");

    public ImportationMetamacServiceJobFacade getImportationMetamacServiceJobFacade() {
        if (importationMetamacServiceJobFacade == null) {
            importationMetamacServiceJobFacade = (ImportationMetamacServiceJobFacade) ApplicationContextProvider.getApplicationContext().getBean(ImportationMetamacServiceJobFacade.BEAN_ID);
        }
        return importationMetamacServiceJobFacade;
    }

    @Override
    public void execute(JobExecutionContext context) {

        JobKey jobKey = context.getJobDetail().getKey();
        ServiceContext serviceContext = null;
        try {

            // Parameters
            JobDataMap data = context.getJobDetail().getJobDataMap();
            String user = data.getString(USER);
            serviceContext = new ServiceContext(user, context.getFireInstanceId(), "sdmx-srm-core");
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
            try {
                if (serviceContext == null) {
                    serviceContext = serviceContextDefault;
                }
                getImportationMetamacServiceJobFacade().markTaskAsFailed(serviceContext, jobKey.getName(), e);
                // TODO sistema de avisos
            } catch (MetamacException e1) {
                logger.error("ImportationJob: the importation with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
            }
        }
    }

    private void importVariableElements(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws Exception {

        // Parameters
        String variableUrn = data.getString(VARIABLE_URN);
        String filePath = data.getString(FILE_PATH);
        String fileImportedName = data.getString(FILE_IMPORTED_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        File file = new File(filePath);
        InputStream stream = new FileInputStream(file);
        String charset = guessCsvCharset(file);

        // Execution
        logger.info("ImportationJob [importVariableElements]: " + jobKey + " starting at " + new Date());
        getImportationMetamacServiceJobFacade().importVariableElementsCsv(serviceContext, variableUrn, stream, charset, fileImportedName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importVariableElements]: " + jobKey + " finished at " + new Date());
    }
    private void importCodes(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws Exception {

        // Parameters
        String codelistUrn = data.getString(CODELIST_URN);
        String filePath = data.getString(FILE_PATH);
        String fileImportedName = data.getString(FILE_IMPORTED_NAME);
        Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

        File file = new File(filePath);
        InputStream stream = new FileInputStream(file);
        String charset = guessCsvCharset(file);

        // Execution
        logger.info("ImportationJob [importCodes]: " + jobKey + " starting at " + new Date());
        getImportationMetamacServiceJobFacade().importCodesCsv(serviceContext, codelistUrn, stream, charset, fileImportedName, jobKey.getName(), updateAlreadyExisting);
        logger.info("ImportationJob [importCodes]: " + jobKey + " finished at " + new Date());
    }

    private void importCodeOrders(JobKey jobKey, JobDataMap data, ServiceContext serviceContext) throws Exception {

        // Parameters
        String codelistUrn = data.getString(CODELIST_URN);
        String filePath = data.getString(FILE_PATH);
        String fileImportedName = data.getString(FILE_IMPORTED_NAME);

        File file = new File(filePath);
        InputStream stream = new FileInputStream(file);
        String charset = guessCsvCharset(file);

        // Execution
        logger.info("ImportationJob [importCodeOrders]: " + jobKey + " starting at " + new Date());
        getImportationMetamacServiceJobFacade().importCodeOrdersCsv(serviceContext, codelistUrn, stream, charset, fileImportedName, jobKey.getName());
        logger.info("ImportationJob [importCodeOrders]: " + jobKey + " finished at " + new Date());
    }

    /**
     * Guess charset of file
     */
    private String guessCsvCharset(File file) throws Exception {
        return FileUtils.guessCharset(file);
    }
}

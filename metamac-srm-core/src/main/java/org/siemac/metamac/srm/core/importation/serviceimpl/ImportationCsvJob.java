package org.siemac.metamac.srm.core.importation.serviceimpl;

import java.io.FileInputStream;
import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
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
    public static final String                 VARIABLE_URN                       = "variableUrn";
    public static final String                 UPDATE_ALREADY_EXISTING            = "updateAlreadyExisting";

    private ImportationMetamacServiceJobFacade importationMetamacServiceJobFacade = null;

    private final ServiceContext               serviceContextDefault              = new ServiceContext("importationCsvJob", "importationCsvJob", "metamac-srm");

    public ImportationMetamacServiceJobFacade getImportationMetamacServiceJobFacade() {
        if (importationMetamacServiceJobFacade == null) {
            importationMetamacServiceJobFacade = (ImportationMetamacServiceJobFacade) ApplicationContextProvider.getApplicationContext().getBean(ImportationMetamacServiceJobFacade.BEAN_ID);
        }
        return importationMetamacServiceJobFacade;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();
        ServiceContext serviceContext = null;
        try {

            // Parameters
            JobDataMap data = context.getJobDetail().getJobDataMap();
            String user = data.getString(USER);
            String variableUrn = data.getString(VARIABLE_URN);
            String filePath = data.getString(FILE_PATH);
            Boolean updateAlreadyExisting = data.getBoolean(UPDATE_ALREADY_EXISTING);

            // Execution
            serviceContext = new ServiceContext(user, context.getFireInstanceId(), "sdmx-srm-core");

            logger.info("ImportationJob: " + jobKey + " starting at " + new Date());
            getImportationMetamacServiceJobFacade().importVariableElementsCsv(serviceContext, variableUrn, new FileInputStream(filePath), jobKey.getName(), updateAlreadyExisting);
            logger.info("ImportationJob: " + jobKey + " finished at " + new Date());
        } catch (Exception e) {
            try {
                if (serviceContext == null) {
                    serviceContext = serviceContextDefault;
                }
                getImportationMetamacServiceJobFacade().markTaskAsFailed(serviceContext, jobKey.getName(), e);
            } catch (MetamacException e1) {
                logger.error("ImportationJob: the importation with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
            }
        }
    }
}

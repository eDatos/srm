package org.siemac.metamac.srm.core.importation.serviceimpl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.SchedulerRepository;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.importation.serviceimpl.utils.ImportationMetamacInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.importation.domain.ImportData;
import com.arte.statistic.sdmx.srm.core.importation.serviceapi.ImportationService;
import com.arte.statistic.sdmx.srm.core.importation.serviceimpl.utils.ImportationJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.ImportationStatusTypeEnum;

/**
 * Implementation of ImportationMetamacService.
 */
@Service("importationMetamacService")
public class ImportationMetamacServiceImpl extends ImportationMetamacServiceImplBase {

    @Autowired
    private ImportationService         importationService;

    @Autowired
    @Qualifier("importationMetamacJaxb2DoCallback")
    private ImportationJaxb2DoCallback importationJaxb2DoCallback;

    public ImportationMetamacServiceImpl() {
    }

    @Override
    public String importSDMXStructureInBackground(ServiceContext ctx, InputStream inputMessage, String fileName) throws MetamacException {
        return importationService.importSDMXStructure(ctx, inputMessage, fileName, importationJaxb2DoCallback);
    }

    @Override
    public synchronized String importVariableElementsCsvInBackground(ServiceContext ctx, String variableUrn, InputStream csvStream, String fileName) throws MetamacException {

        // Validation
        ImportationMetamacInvocationValidator.checkImportVariableElementsCsvInBackground(variableUrn, csvStream, null);

        // Plan job
        OutputStream os = null;
        try {
            String jobKey = "job_import_csv_" + java.util.UUID.randomUUID().toString();

            // Mark importation in progress
            createTaskInProgress(ctx, jobKey, fileName);

            // Scheduler an importation job
            Scheduler sched = SchedulerRepository.getInstance().lookup("SdmxSrmScheduler"); // get a reference to a scheduler

            // Validation: There shouldn't be an import processing
            if (sched.getCurrentlyExecutingJobs().size() != 0) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORT_ERROR_MAX_CURRENT_JOBS).withLoggedLevel(ExceptionLevelEnum.ERROR).build(); // Error
            }

            // Save InputStream (TempFile)
            File file = File.createTempFile("srm_csv_", ".import");
            file.deleteOnExit();
            os = new FileOutputStream(file);
            IOUtils.copy(csvStream, os);

            // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
            JobDetail job = newJob(ImportCsvJob.class).withIdentity(jobKey, "importation").usingJobData(ImportCsvJob.FILE_PATH, file.getAbsolutePath())
                    .usingJobData(ImportCsvJob.USER, ctx.getUserId()).usingJobData(ImportCsvJob.VARIABLE_URN, variableUrn).requestRecovery().build();
            SimpleTrigger trigger = newTrigger().withIdentity("trigger_" + jobKey, "importation").startAt(futureDate(1, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();
            sched.scheduleJob(job, trigger);

            return jobKey;
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORT_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        } finally {
            IOUtils.closeQuietly(csvStream);
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public ImportData retrieveImportDataByJob(ServiceContext ctx, String job) throws MetamacException {
        return importationService.retrieveImportDataByJob(ctx, job);
    }

    @Override
    public void markJobAsFinished(ServiceContext ctx, String job) throws MetamacException {
        importationService.markJobAsFinished(ctx, job);
    }

    @Override
    public void markJobAsFailed(ServiceContext ctx, String job, Exception exception) throws MetamacException {
        importationService.markJobAsFailed(ctx, job, exception);
    }

    @Override
    public PagedResult<ImportData> findImportDataByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return importationService.findImportDataByCondition(ctx, conditions, pagingParameter);
    }

    private void createTaskInProgress(ServiceContext ctx, String jobKey, String fileName) throws MetamacException {
        ImportData importData = new ImportData(jobKey);
        importData.setFileName(fileName);
        importData.setStatus(ImportationStatusTypeEnum.IN_PROGRESS);
        importationService.createImportData(ctx, importData);
    }
}

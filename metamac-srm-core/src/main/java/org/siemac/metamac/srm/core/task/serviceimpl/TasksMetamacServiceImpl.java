package org.siemac.metamac.srm.core.task.serviceimpl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.SchedulerRepository;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.task.utils.TasksMetamacInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.task.domain.Task;
import com.arte.statistic.sdmx.srm.core.task.serviceapi.TasksService;
import com.arte.statistic.sdmx.srm.core.task.serviceimpl.utils.ImportationJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TaskStatusTypeEnum;

/**
 * Implementation of ImportationMetamacService.
 */
@Service("tasksMetamacService")
public class TasksMetamacServiceImpl extends TasksMetamacServiceImplBase {

    @Autowired
    private TasksService               tasksService;

    @Autowired
    @Qualifier("importationMetamacJaxb2DoCallback")
    private ImportationJaxb2DoCallback importationJaxb2DoCallback;

    public TasksMetamacServiceImpl() {
    }

    @Override
    public String importSDMXStructureInBackground(ServiceContext ctx, InputStream inputMessage, String fileName) throws MetamacException {
        return tasksService.importSDMXStructure(ctx, inputMessage, fileName, importationJaxb2DoCallback);
    }

    @Override
    public String plannifyImportCodesTsvInBackground(ServiceContext ctx, String codelistUrn, InputStream tsvStream, String fileName, boolean updateAlreadyExisting) throws MetamacException {
        // Validation
        TasksMetamacInvocationValidator.checkImportCodesTsvInBackground(codelistUrn, tsvStream, updateAlreadyExisting, null);

        // Plan job
        JobDataMap jobDataAdditional = new JobDataMap();
        jobDataAdditional.put(ImportationTsvJob.CODELIST_URN, codelistUrn);
        jobDataAdditional.put(ImportationTsvJob.UPDATE_ALREADY_EXISTING, updateAlreadyExisting);
        jobDataAdditional.put(ImportationTsvJob.OPERATION, ImportationTsvJob.OPERATION_IMPORT_CODES);
        return importTsvInBackground(ctx, tsvStream, fileName, jobDataAdditional);
    }

    @Override
    public String plannifyImportCodeOrdersTsvInBackground(ServiceContext ctx, String codelistUrn, InputStream tsvStream, String fileName) throws MetamacException {
        // Validation
        TasksMetamacInvocationValidator.checkImportCodeOrdersTsvInBackground(codelistUrn, tsvStream, null);

        // Plan job
        JobDataMap jobDataAdditional = new JobDataMap();
        jobDataAdditional.put(ImportationTsvJob.CODELIST_URN, codelistUrn);
        jobDataAdditional.put(ImportationTsvJob.OPERATION, ImportationTsvJob.OPERATION_IMPORT_CODE_ORDERS);
        return importTsvInBackground(ctx, tsvStream, fileName, jobDataAdditional);
    }

    @Override
    public String plannifyImportVariableElementsTsvInBackground(ServiceContext ctx, String variableUrn, InputStream tsvStream, String fileName, boolean updateAlreadyExisting) throws MetamacException {

        // Validation
        TasksMetamacInvocationValidator.checkImportVariableElementsTsvInBackground(variableUrn, tsvStream, updateAlreadyExisting, null);

        // Plan job
        JobDataMap jobDataAdditional = new JobDataMap();
        jobDataAdditional.put(ImportationTsvJob.VARIABLE_URN, variableUrn);
        jobDataAdditional.put(ImportationTsvJob.UPDATE_ALREADY_EXISTING, updateAlreadyExisting);
        jobDataAdditional.put(ImportationTsvJob.OPERATION, ImportationTsvJob.OPERATION_IMPORT_VARIABLE_ELEMENTS);
        return importTsvInBackground(ctx, tsvStream, fileName, jobDataAdditional);
    }

    @Override
    public String plannifyImportVariableElementsShapeInBackground(ServiceContext ctx, String variableUrn, URL shapeFileUrl) throws MetamacException {
        return plannifyImportVariableElementsShapefileInBackground(ctx, variableUrn, shapeFileUrl, SrmConstants.SHAPE_OPERATION_IMPORT_SHAPES);
    }

    @Override
    public String plannifyImportVariableElementsPointsInBackground(ServiceContext ctx, String variableUrn, URL shapeFileUrl) throws MetamacException {
        return plannifyImportVariableElementsShapefileInBackground(ctx, variableUrn, shapeFileUrl, SrmConstants.SHAPE_OPERATION_IMPORT_POINTS);
    }

    @Override
    public synchronized String plannifyPublicationInternallyCodelist(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        try {
            // Plan job
            String jobKey = "job_merge_" + java.util.UUID.randomUUID().toString();

            // Mark task in progress
            createTaskInProgress(ctx, jobKey, null);

            // Scheduler a job
            Scheduler sched = SchedulerRepository.getInstance().lookup("SdmxSrmScheduler"); // get a reference to a scheduler

            // Validation: There shouldn't be an background processing
            if (sched.getCurrentlyExecutingJobs().size() != 0) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR_MAX_CURRENT_JOBS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }

            // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
            JobDetail job = newJob(MergeCodelistJob.class).withIdentity(jobKey, "merge").usingJobData(MergeCodelistJob.USER, ctx.getUserId()).usingJobData(MergeCodelistJob.URN, urn)
                    .usingJobData(MergeCodelistJob.FORCE_LATEST_FINAL, forceLatestFinal).requestRecovery().build();

            SimpleTrigger trigger = newTrigger().withIdentity("trigger_" + jobKey, "merge").startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

            sched.scheduleJob(job, trigger);

            return jobKey;
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
    }

    @Override
    public Task retrieveTaskByJob(ServiceContext ctx, String job) throws MetamacException {
        return tasksService.retrieveTaskByJob(ctx, job);
    }

    @Override
    public void markTaskAsFinished(ServiceContext ctx, String job, List<MetamacExceptionItem> informationItems) throws MetamacException {
        tasksService.markTaskAsFinished(ctx, job, informationItems);
    }

    @Override
    public void markTaskAsFailed(ServiceContext ctx, String job, Exception exception) throws MetamacException {
        tasksService.markTaskAsFailed(ctx, job, exception);
    }

    @Override
    public PagedResult<Task> findTasksByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return tasksService.findTasksByCondition(ctx, conditions, pagingParameter);
    }

    private void createTaskInProgress(ServiceContext ctx, String jobKey, String fileName) throws MetamacException {
        Task importData = new Task(jobKey);
        importData.setFileName(fileName);
        importData.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
        tasksService.createTask(ctx, importData);
    }

    private synchronized String importTsvInBackground(ServiceContext ctx, InputStream tsvStream, String fileName, JobDataMap jobDataAdditional) throws MetamacException {

        // Plan job
        OutputStream os = null;
        try {
            String jobKey = "job_import_TSV_" + java.util.UUID.randomUUID().toString();

            // Mark importation in progress
            createTaskInProgress(ctx, jobKey, fileName);

            // Scheduler an importation job
            Scheduler sched = SchedulerRepository.getInstance().lookup("SdmxSrmScheduler"); // get a reference to a scheduler

            // Validation: There shouldn't be an import processing
            if (sched.getCurrentlyExecutingJobs().size() != 0) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR_MAX_CURRENT_JOBS).withLoggedLevel(ExceptionLevelEnum.ERROR).build(); // Error
            }

            // Save InputStream (TempFile)
            File file = File.createTempFile("srm_TSV_", ".import");
            file.deleteOnExit();
            os = new FileOutputStream(file);
            IOUtils.copy(tsvStream, os);

            // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
            JobDetail job = newJob(ImportationTsvJob.class).withIdentity(jobKey, "importation").usingJobData(ImportationTsvJob.FILE_PATH, file.getAbsolutePath())
                    .usingJobData(ImportationTsvJob.FILE_IMPORTED_NAME, fileName).usingJobData(ImportationTsvJob.USER, ctx.getUserId()).usingJobData(jobDataAdditional).requestRecovery().build();
            SimpleTrigger trigger = newTrigger().withIdentity("trigger_" + jobKey, "importation").startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();
            sched.scheduleJob(job, trigger);

            return jobKey;
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        } finally {
            IOUtils.closeQuietly(tsvStream);
            IOUtils.closeQuietly(os);
        }
    }

    private synchronized String plannifyImportVariableElementsShapefileInBackground(ServiceContext ctx, String variableUrn, URL shapeFileUrl, String operation) throws MetamacException {
        try {
            // Plan job
            String jobKey = "job_import_shapefile_to_variable_elements_" + java.util.UUID.randomUUID().toString();

            // Mark task in progress
            createTaskInProgress(ctx, jobKey, null);

            // Scheduler a job
            Scheduler sched = SchedulerRepository.getInstance().lookup("SdmxSrmScheduler");

            // Validation: There shouldn't be an background processing
            if (sched.getCurrentlyExecutingJobs().size() != 0) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR_MAX_CURRENT_JOBS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }

            // put triggers in group named after the cluster node instance just to distinguish (in logging) what was scheduled from where
            JobDetail job = newJob(ImportationShapeJob.class).withIdentity(jobKey, "shape").usingJobData(ImportationShapeJob.USER, ctx.getUserId())
                    .usingJobData(ImportationShapeJob.VARIABLE_URN, variableUrn).usingJobData(ImportationShapeJob.SHAPEFILE_URL, shapeFileUrl.toString())
                    .usingJobData(ImportationShapeJob.OPERATION, operation).requestRecovery().build();

            SimpleTrigger trigger = newTrigger().withIdentity("trigger_" + jobKey, "shape").startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

            sched.scheduleJob(job, trigger);

            return jobKey;
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
    }

}

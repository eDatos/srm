package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;

/**
 * Implementation of TasksMetamacServiceFacade.
 */
@Service("tasksMetamacServiceFacade")
public class TasksMetamacServiceFacadeImpl extends TasksMetamacServiceFacadeImplBase {

    @Autowired
    private BaseService baseService;

    public TasksMetamacServiceFacadeImpl() {
    }

    @Override
    public void importCodesCsv(ServiceContext ctx, String codelistUrn, InputStream csvStream, String charset, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importCodesCsv(ctx, codelistUrn, csvStream, charset, fileName, updateAlreadyExisting, informationItems);
        // Mark job as completed
        // TODO sistema de avisos
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, informationItems);
    }

    @Override
    public void importCodeOrdersCsv(ServiceContext ctx, String codelistUrn, InputStream csvStream, String charset, String fileName, String jobKey) throws MetamacException {
        getCodesMetamacService().importCodeOrdersCsv(ctx, codelistUrn, csvStream, charset, fileName);
        // Mark job as completed
        // TODO sistema de avisos
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, null);
    }

    @Override
    public void importVariableElementsCsv(ServiceContext ctx, String variableUrn, InputStream csvStream, String charset, String fileName, String jobKey, boolean updateAlreadyExisting)
            throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importVariableElementsCsv(ctx, variableUrn, csvStream, charset, fileName, updateAlreadyExisting, informationItems);
        // Mark job as completed
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, informationItems);
    }

    @Override
    public void processMergeCodelist(ServiceContext ctx, String urnToCopy, String jobKey) throws MetamacException {
        // Merge
        getCodesMetamacService().mergeTemporalVersion(ctx, urnToCopy);
        // Mark job as completed
        // TODO sistema de avisos
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, null);
    }

    @Override
    public void markTaskAsFailed(ServiceContext ctx, String job, Exception exception) throws MetamacException {
        getTasksMetamacService().markTaskAsFailed(ctx, job, exception);
    }

    @Override
    public void markTaskItemSchemeAsFailed(ServiceContext ctx, String urnToCopy) throws MetamacException {
        baseService.markTaskItemSchemeInBackgroundAsFailed(ctx, urnToCopy);
    }
}

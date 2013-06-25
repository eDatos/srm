package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.InputStream;
import java.net.URL;
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

    private final Boolean CAN_NOT_BE_BACKGROUND = Boolean.FALSE;

    @Override
    public void processImportCodesTsv(ServiceContext ctx, String codelistUrn, InputStream tsvStream, String charset, String fileName, String jobKey, boolean updateAlreadyExisting)
            throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importCodesTsv(ctx, codelistUrn, tsvStream, charset, fileName, updateAlreadyExisting, informationItems, CAN_NOT_BE_BACKGROUND);
        // Mark job as completed
        // TODO sistema de avisos
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, informationItems);
    }

    @Override
    public void processImportCodeOrdersTsv(ServiceContext ctx, String codelistUrn, InputStream tsvStream, String charset, String fileName, String jobKey) throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importCodeOrdersTsv(ctx, codelistUrn, tsvStream, charset, fileName, informationItems, CAN_NOT_BE_BACKGROUND);
        // Mark job as completed
        // TODO sistema de avisos
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, null);
    }

    @Override
    public void processImportVariableElementsTsv(ServiceContext ctx, String variableUrn, InputStream tsvStream, String charset, String fileName, String jobKey, boolean updateAlreadyExisting)
            throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importVariableElementsTsv(ctx, variableUrn, tsvStream, charset, fileName, updateAlreadyExisting, informationItems, CAN_NOT_BE_BACKGROUND);
        // Mark job as completed
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, informationItems);
    }

    @Override
    public void processImportVariableElementsShape(ServiceContext ctx, String variableUrn, URL shapeFile, String jobKey) throws MetamacException {
        // Import
        getCodesMetamacService().importVariableElementsShape(ctx, variableUrn, shapeFile, CAN_NOT_BE_BACKGROUND);
        // Mark job as completed
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, null);
    }

    @Override
    public void processPublishInternallyCodelist(ServiceContext ctx, String urn, boolean forceLatestFinal, String jobKey) throws MetamacException {
        // Publish
        getCodesMetamacService().publishInternallyCodelist(ctx, urn, forceLatestFinal, CAN_NOT_BE_BACKGROUND);

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

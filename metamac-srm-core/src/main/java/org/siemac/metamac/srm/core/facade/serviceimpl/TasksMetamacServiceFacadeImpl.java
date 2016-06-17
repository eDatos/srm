package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.File;
import java.net.URL;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
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
    public void processImportConceptsTsv(ServiceContext ctx, String conceptSchemeUrn, File tsvFile, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getConceptsMetamacService().importConceptsTsv(ctx, conceptSchemeUrn, tsvFile, fileName, updateAlreadyExisting, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processImportOrganisationsTsv(ServiceContext ctx, String organisationSchemeUrn, File tsvFile, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getOrganisationsMetamacService().importOrganisationsTsv(ctx, organisationSchemeUrn, tsvFile, fileName, updateAlreadyExisting, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processImportCategoriesTsv(ServiceContext ctx, String categorySchemeUrn, File tsvFile, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getCategoriesMetamacService().importCategoriesTsv(ctx, categorySchemeUrn, tsvFile, fileName, updateAlreadyExisting, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());        
    }
    
    @Override
    public void processImportCodesTsv(ServiceContext ctx, String codelistUrn, File file, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getCodesMetamacService().importCodesTsv(ctx, codelistUrn, file, fileName, updateAlreadyExisting, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processImportCodeOrdersTsv(ServiceContext ctx, String codelistUrn, File file, String fileName, String jobKey) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getCodesMetamacService().importCodeOrdersTsv(ctx, codelistUrn, file, fileName, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processImportVariableElementsTsv(ServiceContext ctx, String variableUrn, File file, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getCodesMetamacService().importVariableElementsTsv(ctx, variableUrn, file, fileName, updateAlreadyExisting, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processImportVariableElementsShape(ServiceContext ctx, String variableUrn, URL shapeFile, String jobKey) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getCodesMetamacService().importVariableElementsShape(ctx, variableUrn, shapeFile, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processImportVariableElementsPoints(ServiceContext ctx, String variableUrn, URL shapeFile, String jobKey) throws MetamacException {
        TaskImportationInfo taskImportationInfo = getCodesMetamacService().importVariableElementsPoints(ctx, variableUrn, shapeFile, CAN_NOT_BE_BACKGROUND);
        getTasksMetamacService().markTaskAsFinished(ctx, jobKey, taskImportationInfo.getInformationItems());
    }

    @Override
    public void processPublishInternallyCodelist(ServiceContext ctx, String urn, boolean forceLatestFinal, String jobKey) throws MetamacException {
        getCodesMetamacService().publishInternallyCodelist(ctx, urn, forceLatestFinal, CAN_NOT_BE_BACKGROUND);
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

    @Override
    public void deleteEntitiesMarkedToDelete(ServiceContext ctx) throws MetamacException {
        getTasksMetamacService().deleteEntitiesMarkedToDelete(ctx);
    }
}

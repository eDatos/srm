package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.springframework.stereotype.Service;

/**
 * Implementation of ImportationMetamacServiceJobFacade.
 */
@Service("importationMetamacServiceJobFacade")
public class ImportationMetamacServiceJobFacadeImpl extends ImportationMetamacServiceJobFacadeImplBase {

    public ImportationMetamacServiceJobFacadeImpl() {
    }

    @Override
    public void importCodesCsv(ServiceContext ctx, String codelistUrn, InputStream csvStream, String charset, String fileName, String jobKey, boolean updateAlreadyExisting) throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importCodesCsv(ctx, codelistUrn, csvStream, charset, fileName, updateAlreadyExisting, informationItems);
        // Mark job as completed
        // TODO sistema de avisos
        getImportationMetamacService().markTaskAsFinished(ctx, jobKey, informationItems);
    }

    @Override
    public void importCodeOrdersCsv(ServiceContext ctx, String codelistUrn, InputStream csvStream, String charset, String fileName, String jobKey) throws MetamacException {
        getCodesMetamacService().importCodeOrdersCsv(ctx, codelistUrn, csvStream, charset, fileName);
        // Mark job as completed
        // TODO sistema de avisos
        getImportationMetamacService().markTaskAsFinished(ctx, jobKey, null);

    }

    @Override
    public void importVariableElementsCsv(ServiceContext ctx, String variableUrn, InputStream csvStream, String charset, String fileName, String jobKey, boolean updateAlreadyExisting)
            throws MetamacException {
        // Import
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        getCodesMetamacService().importVariableElementsCsv(ctx, variableUrn, csvStream, charset, fileName, updateAlreadyExisting, informationItems);
        // Mark job as completed
        getImportationMetamacService().markTaskAsFinished(ctx, jobKey, informationItems);
    }

    @Override
    public void markTaskAsFailed(ServiceContext ctx, String job, Exception exception) throws MetamacException {
        getImportationMetamacService().markTaskAsFailed(ctx, job, exception);
    }

}

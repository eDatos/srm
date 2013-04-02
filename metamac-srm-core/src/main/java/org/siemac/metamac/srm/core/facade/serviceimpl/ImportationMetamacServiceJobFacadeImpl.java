package org.siemac.metamac.srm.core.facade.serviceimpl;

import java.io.InputStream;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

/**
 * Implementation of ImportationMetamacServiceJobFacade.
 */
@Service("importationMetamacServiceJobFacade")
public class ImportationMetamacServiceJobFacadeImpl extends ImportationMetamacServiceJobFacadeImplBase {

    public ImportationMetamacServiceJobFacadeImpl() {
    }

    @Override
    public void importVariableElementsCsv(ServiceContext ctx, String variableUrn, InputStream csvStream, String jobKey) throws MetamacException {
        // Import
        getCodesMetamacService().importVariableElementsCsv(ctx, variableUrn, csvStream);
        // Mark job as completed
        getImportationMetamacService().markJobAsFinished(ctx, jobKey);
    }

    @Override
    public void markJobAsFailed(ServiceContext ctx, String job, Exception exception) throws MetamacException {
        getImportationMetamacService().markJobAsFailed(ctx, job, exception);
    }

}

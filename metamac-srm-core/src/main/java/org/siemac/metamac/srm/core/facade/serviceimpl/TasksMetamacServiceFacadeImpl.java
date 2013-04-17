package org.siemac.metamac.srm.core.facade.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

import org.siemac.metamac.core.common.exception.MetamacException;

import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Implementation of TasksMetamacServiceFacade.
 */
@Service("tasksMetamacServiceFacade")
public class TasksMetamacServiceFacadeImpl
    extends TasksMetamacServiceFacadeImplBase {
    public TasksMetamacServiceFacadeImpl() {
    }

    public void importCodesCsv(ServiceContext ctx, String codelistUrn,
        InputStream csvStream, String charset, String fileName, String jobKey,
        boolean updateAlreadyExisting) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "importCodesCsv not implemented");

    }

    public void importCodeOrdersCsv(ServiceContext ctx, String codelistUrn,
        InputStream csvStream, String charset, String fileName, String jobKey)
        throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "importCodeOrdersCsv not implemented");

    }

    public void importVariableElementsCsv(ServiceContext ctx,
        String variableUrn, InputStream csvStream, String charset,
        String fileName, String jobKey, boolean updateAlreadyExisting)
        throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "importVariableElementsCsv not implemented");

    }

    public void processMergeCodelist(ServiceContext ctx, String urnToCopy,
        String jobKey) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "processMergeCodelist not implemented");

    }

    public void markTaskAsFailed(ServiceContext ctx, String job,
        Exception exception) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "markTaskAsFailed not implemented");

    }
}

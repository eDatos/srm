package org.siemac.metamac.srm.core.common;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public interface LifeCycle {

    public Object sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public Object sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public Object rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public Object rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public Object publishInternally(ServiceContext ctx, String urn, Boolean forceLastestFinal, Boolean skipValidation) throws MetamacException;
    public Object publishExternally(ServiceContext ctx, String urn) throws MetamacException;

    public Object prePublishResourceInInternallyPublished(ServiceContext ctx, String urn, ProcStatusEnum targetStatus, Boolean skipValidation) throws MetamacException;
}

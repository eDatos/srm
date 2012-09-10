package org.siemac.metamac.srm.core.dsd.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;

public interface DsdLifecycle {

    public StructureVersion sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public StructureVersion sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public StructureVersion rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public StructureVersion rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public StructureVersion publishInternally(ServiceContext ctx, String urn) throws MetamacException;
    public StructureVersion publishExternally(ServiceContext ctx, String urn) throws MetamacException;
}

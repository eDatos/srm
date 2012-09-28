package org.siemac.metamac.srm.core.organisation.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;

public interface OrganisationSchemeLifecycle {

    public ItemSchemeVersion sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public ItemSchemeVersion sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public ItemSchemeVersion rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public ItemSchemeVersion rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException;
    public ItemSchemeVersion publishInternally(ServiceContext ctx, String urn) throws MetamacException;
    public ItemSchemeVersion publishExternally(ServiceContext ctx, String urn) throws MetamacException;

}

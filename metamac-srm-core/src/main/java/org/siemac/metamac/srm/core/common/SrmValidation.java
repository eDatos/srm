package org.siemac.metamac.srm.core.common;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;

public interface SrmValidation {

    public void checkMaintainer(ServiceContext ctx, MaintainableArtefact maintainableArtefact, Boolean artefactImported) throws MetamacException;
    public void checkItemsStructureCanBeModified(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException;
    public void checkItemsStructureCanBeModified(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException;
}

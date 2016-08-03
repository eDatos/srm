package org.siemac.metamac.srm.core.common;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

public interface SrmValidation {

    public boolean isMaintainerSdmxRoot(ServiceContext ctx, Organisation maintainer) throws MetamacException;
    public boolean isMaintainerIsDefault(ServiceContext ctx, Organisation maintainer) throws MetamacException;
    public void checkMaintainerIsDefault(ServiceContext ctx, String maintainerUrn) throws MetamacException;
    public void checkMaintainer(ServiceContext ctx, MaintainableArtefact maintainableArtefact, Boolean artefactImported) throws MetamacException;
    public void checkItemsStructureCanBeModified(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException;
    public void checkItemsStructureCanBeModified(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException;
    public void checkStartValidity(ServiceContext ctx, MaintainableArtefact maintainableArtefact, SrmLifeCycleMetadata lifeCycle) throws MetamacException;
    public void checkEndValidity(ServiceContext ctx, MaintainableArtefact maintainableArtefact, SrmLifeCycleMetadata lifeCycle) throws MetamacException;
    public void checkModifyCategorisationValidity(ServiceContext ctx, MaintainableArtefact maintainableArtefact) throws MetamacException;

}

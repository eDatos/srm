package org.siemac.metamac.srm.core.common;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public interface SrmValidation {

    public void checkMaintainer(ServiceContext ctx, MaintainableArtefact maintainableArtefact, Boolean artefactImported) throws MetamacException;
}

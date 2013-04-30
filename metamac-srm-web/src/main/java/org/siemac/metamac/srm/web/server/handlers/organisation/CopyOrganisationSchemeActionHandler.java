package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.CopyOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.CopyOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyOrganisationSchemeActionHandler extends SecurityActionHandler<CopyOrganisationSchemeAction, CopyOrganisationSchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CopyOrganisationSchemeActionHandler() {
        super(CopyOrganisationSchemeAction.class);
    }

    @Override
    public CopyOrganisationSchemeResult executeSecurityAction(CopyOrganisationSchemeAction action) throws ActionException {
        try {
            TaskInfo taskInfo = srmCoreServiceFacade.copyOrganisationScheme(ServiceContextHolder.getCurrentServiceContext(), action.getOrganisationSchemeUrn());
            // Organisation scheme will never be copied in background
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), taskInfo.getUrnResult());
            return new CopyOrganisationSchemeResult(organisationSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

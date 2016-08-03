package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.ExportOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.ExportOrganisationsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportOrganisationsActionHandler extends SecurityActionHandler<ExportOrganisationsAction, ExportOrganisationsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportOrganisationsActionHandler() {
        super(ExportOrganisationsAction.class);
    }

    @Override
    public ExportOrganisationsResult executeSecurityAction(ExportOrganisationsAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportOrganisationsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getOrganisationSchemeUrn());
            return new ExportOrganisationsResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

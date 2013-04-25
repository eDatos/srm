package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationsBySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationsBySchemeActionHandler extends SecurityActionHandler<GetOrganisationsBySchemeAction, GetOrganisationsBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationsBySchemeActionHandler() {
        super(GetOrganisationsBySchemeAction.class);
    }

    @Override
    public GetOrganisationsBySchemeResult executeSecurityAction(GetOrganisationsBySchemeAction action) throws ActionException {
        try {
            List<ItemVisualisationResult> organisations = srmCoreServiceFacade.retrieveOrganisationsByOrganisationSchemeUrn(ServiceContextHolder.getCurrentServiceContext(), action.getSchemeUrn(),
                    action.getLocale());
            return new GetOrganisationsBySchemeResult(organisations);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationListBySchemeActionHandler extends SecurityActionHandler<GetOrganisationListBySchemeAction, GetOrganisationListBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationListBySchemeActionHandler() {
        super(GetOrganisationListBySchemeAction.class);
    }

    @Override
    public GetOrganisationListBySchemeResult executeSecurityAction(GetOrganisationListBySchemeAction action) throws ActionException {
        try {
            List<ItemHierarchyDto> organisations = srmCoreServiceFacade.retrieveOrganisationsByOrganisationSchemeUrn(ServiceContextHolder.getCurrentServiceContext(), action.getSchemeUrn());
            return new GetOrganisationListBySchemeResult(organisations);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}

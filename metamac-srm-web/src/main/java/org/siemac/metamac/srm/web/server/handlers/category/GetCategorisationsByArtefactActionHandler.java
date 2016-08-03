package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationsByArtefactResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorisationsByArtefactActionHandler extends SecurityActionHandler<GetCategorisationsByArtefactAction, GetCategorisationsByArtefactResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorisationsByArtefactActionHandler() {
        super(GetCategorisationsByArtefactAction.class);
    }

    @Override
    public GetCategorisationsByArtefactResult executeSecurityAction(GetCategorisationsByArtefactAction action) throws ActionException {
        try {
            List<CategorisationDto> categorisations = srmCoreServiceFacade.retrieveCategorisationsByArtefact(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCategorisationsByArtefactResult(categorisations);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

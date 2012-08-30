package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteConceptSchemeListActionHandler extends SecurityActionHandler<DeleteConceptSchemeListAction, DeleteConceptSchemeListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteConceptSchemeListActionHandler() {
        super(DeleteConceptSchemeListAction.class);
    }

    @Override
    public DeleteConceptSchemeListResult executeSecurityAction(DeleteConceptSchemeListAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteConceptScheme(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteConceptSchemeListResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}

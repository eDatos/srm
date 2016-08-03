package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.ExportConceptsAction;
import org.siemac.metamac.srm.web.shared.concept.ExportConceptsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportConceptsActionHandler extends SecurityActionHandler<ExportConceptsAction, ExportConceptsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportConceptsActionHandler() {
        super(ExportConceptsAction.class);
    }

    @Override
    public ExportConceptsResult executeSecurityAction(ExportConceptsAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportConceptsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getConceptSchemeUrn());
            return new ExportConceptsResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

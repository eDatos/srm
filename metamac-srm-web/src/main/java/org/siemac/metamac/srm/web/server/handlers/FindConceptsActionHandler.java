package org.siemac.metamac.srm.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.FindConceptsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class FindConceptsActionHandler extends SecurityActionHandler<FindConceptsAction, FindConceptsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindConceptsActionHandler() {
        super(FindConceptsAction.class);
    }

    @Override
    public FindConceptsResult executeSecurityAction(FindConceptsAction action) throws ActionException {
        List<ExternalItemBtDto> concepts = srmCoreServiceFacade.findConcepts(ServiceContextHolder.getCurrentServiceContext(), action.getUriConceptScheme());
        return new FindConceptsResult(concepts);
    }

    @Override
    public void undo(FindConceptsAction action, FindConceptsResult result, ExecutionContext context) throws ActionException {

    }

}

package org.siemac.metamac.srm.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.srm.web.server.ServiceContextHelper;
import org.siemac.metamac.srm.web.shared.FindConceptsAction;
import org.siemac.metamac.srm.web.shared.FindConceptsResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class FindConceptsActionHandler extends AbstractActionHandler<FindConceptsAction, FindConceptsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindConceptsActionHandler() {
        super(FindConceptsAction.class);
    }

    @Override
    public FindConceptsResult execute(FindConceptsAction action, ExecutionContext context) throws ActionException {
        List<ExternalItemBtDto> concepts = srmCoreServiceFacade.findConcepts(ServiceContextHelper.getServiceContext(), action.getUriConceptScheme());
        return new FindConceptsResult(concepts);
    }

    @Override
    public void undo(FindConceptsAction action, FindConceptsResult result, ExecutionContext context) throws ActionException {

    }

}

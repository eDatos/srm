package org.siemac.metamac.internal.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.core_facades.serviceapi.SDMXStructureServiceFacade;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.FindCodeListsAction;
import org.siemac.metamac.internal.web.shared.FindCodeListsResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class FindCodeListsActionHandler extends AbstractActionHandler<FindCodeListsAction, FindCodeListsResult> {

    @Autowired
    private SDMXStructureServiceFacade sDMXStructureServiceFacade;

    public FindCodeListsActionHandler() {
        super(FindCodeListsAction.class);
    }

    @Override
    public FindCodeListsResult execute(FindCodeListsAction action, ExecutionContext context) throws ActionException {
        List<ExternalItemBtDto> codeLists = sDMXStructureServiceFacade.findCodelists(ServiceContextHelper.getServiceContext(), action.getUriConcept());
        return new FindCodeListsResult(codeLists);
    }

    @Override
    public void undo(FindCodeListsAction action, FindCodeListsResult result, ExecutionContext context) throws ActionException {

    }

}

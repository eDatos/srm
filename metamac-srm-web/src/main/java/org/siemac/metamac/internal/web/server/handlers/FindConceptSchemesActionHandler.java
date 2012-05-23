package org.siemac.metamac.internal.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.FindConceptSchemesAction;
import org.siemac.metamac.internal.web.shared.FindConceptSchemesResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class FindConceptSchemesActionHandler extends AbstractActionHandler<FindConceptSchemesAction, FindConceptSchemesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindConceptSchemesActionHandler() {
        super(FindConceptSchemesAction.class);
    }

    @Override
    public FindConceptSchemesResult execute(FindConceptSchemesAction action, ExecutionContext context) throws ActionException {
        List<ExternalItemBtDto> conceptSchemes = srmCoreServiceFacade.findConceptSchemeRefs(ServiceContextHelper.getServiceContext());
        return new FindConceptSchemesResult(conceptSchemes);
    }

    @Override
    public void undo(FindConceptSchemesAction action, FindConceptSchemesResult result, ExecutionContext context) throws ActionException {

    }

}

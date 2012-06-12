package org.siemac.metamac.srm.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.FindConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.FindConceptSchemesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class FindConceptSchemesActionHandler extends SecurityActionHandler<FindConceptSchemesAction, FindConceptSchemesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindConceptSchemesActionHandler() {
        super(FindConceptSchemesAction.class);
    }

    @Override
    public FindConceptSchemesResult executeSecurityAction(FindConceptSchemesAction action) throws ActionException {
        List<ExternalItemBtDto> conceptSchemes = srmCoreServiceFacade.findConceptSchemeRefs(ServiceContextHolder.getCurrentServiceContext());
        return new FindConceptSchemesResult(conceptSchemes);
    }

    @Override
    public void undo(FindConceptSchemesAction action, FindConceptSchemesResult result, ExecutionContext context) throws ActionException {

    }

}

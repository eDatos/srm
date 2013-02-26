package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CreateCodesHierarchyAction;
import org.siemac.metamac.srm.web.shared.code.CreateCodesHierarchyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateCodesHierarchyActionHandler extends SecurityActionHandler<CreateCodesHierarchyAction, CreateCodesHierarchyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateCodesHierarchyActionHandler() {
        super(CreateCodesHierarchyAction.class);
    }

    @Override
    public CreateCodesHierarchyResult executeSecurityAction(CreateCodesHierarchyAction action) throws ActionException {
        try {
            srmCoreServiceFacade.createCodesHierarchy(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), action.getParentUrn(), action.getCodesMetamacDto());
            return new CreateCodesHierarchyResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

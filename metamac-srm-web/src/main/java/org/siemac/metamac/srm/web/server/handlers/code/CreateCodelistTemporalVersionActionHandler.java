package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateCodelistTemporalVersionActionHandler extends SecurityActionHandler<CreateCodelistTemporalVersionAction, CreateCodelistTemporalVersionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateCodelistTemporalVersionActionHandler() {
        super(CreateCodelistTemporalVersionAction.class);
    }

    @Override
    public CreateCodelistTemporalVersionResult executeSecurityAction(CreateCodelistTemporalVersionAction action) throws ActionException {
        try {
            CodelistMetamacDto codelistMetamacDto = null;
            // FIXME
            srmCoreServiceFacade.createTemporalVersionCodelist(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new CreateCodelistTemporalVersionResult(codelistMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

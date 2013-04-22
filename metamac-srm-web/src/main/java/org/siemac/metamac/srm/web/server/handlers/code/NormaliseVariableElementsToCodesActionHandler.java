package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.NormaliseVariableElementsToCodesAction;
import org.siemac.metamac.srm.web.shared.code.NormaliseVariableElementsToCodesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class NormaliseVariableElementsToCodesActionHandler extends SecurityActionHandler<NormaliseVariableElementsToCodesAction, NormaliseVariableElementsToCodesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public NormaliseVariableElementsToCodesActionHandler() {
        super(NormaliseVariableElementsToCodesAction.class);
    }

    @Override
    public NormaliseVariableElementsToCodesResult executeSecurityAction(NormaliseVariableElementsToCodesAction action) throws ActionException {
        try {
            List<CodeVariableElementNormalisationResult> result = srmCoreServiceFacade.normaliseVariableElementsToCodes(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(),
                    action.getLocale());
            return new NormaliseVariableElementsToCodesResult(result);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

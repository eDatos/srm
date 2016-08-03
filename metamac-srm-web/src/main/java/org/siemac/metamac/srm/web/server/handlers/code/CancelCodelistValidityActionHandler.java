package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityAction;
import org.siemac.metamac.srm.web.shared.code.CancelCodelistValidityResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CancelCodelistValidityActionHandler extends SecurityActionHandler<CancelCodelistValidityAction, CancelCodelistValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelCodelistValidityActionHandler() {
        super(CancelCodelistValidityAction.class);
    }

    @Override
    public CancelCodelistValidityResult executeSecurityAction(CancelCodelistValidityAction action) throws ActionException {
        List<CodelistMetamacDto> codelistMetamacDtos = new ArrayList<CodelistMetamacDto>();
        for (String urn : action.getUrns()) {
            try {
                CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.endCodelistValidity(ServiceContextHolder.getCurrentServiceContext(), urn);
                codelistMetamacDtos.add(codelistMetamacDto);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new CancelCodelistValidityResult(codelistMetamacDtos);
    }
}

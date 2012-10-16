package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityAction;
import org.siemac.metamac.srm.web.shared.dsd.CancelDsdValidityResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CancelDsdValidityActionHandler extends SecurityActionHandler<CancelDsdValidityAction, CancelDsdValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelDsdValidityActionHandler() {
        super(CancelDsdValidityAction.class);
    }

    @Override
    public CancelDsdValidityResult executeSecurityAction(CancelDsdValidityAction action) throws ActionException {
        List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos = new ArrayList<DataStructureDefinitionMetamacDto>();
        for (String urn : action.getUrns()) {
            try {
                DataStructureDefinitionMetamacDto dsd = srmCoreServiceFacade.endDataStructureDefinitionValidity(ServiceContextHolder.getCurrentServiceContext(), urn);
                dataStructureDefinitionMetamacDtos.add(dsd);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new CancelDsdValidityResult(dataStructureDefinitionMetamacDtos);
    }

}

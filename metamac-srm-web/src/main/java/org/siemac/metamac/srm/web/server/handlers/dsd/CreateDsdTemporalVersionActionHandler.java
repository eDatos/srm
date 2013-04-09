package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.CreateDsdTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.dsd.CreateDsdTemporalVersionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateDsdTemporalVersionActionHandler extends SecurityActionHandler<CreateDsdTemporalVersionAction, CreateDsdTemporalVersionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateDsdTemporalVersionActionHandler() {
        super(CreateDsdTemporalVersionAction.class);
    }

    @Override
    public CreateDsdTemporalVersionResult executeSecurityAction(CreateDsdTemporalVersionAction action) throws ActionException {
        try {
            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.createTemporalVersionDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(),
                    action.getUrn());
            return new CreateDsdTemporalVersionResult(dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
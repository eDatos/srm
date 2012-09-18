package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.VersionDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionDsdActionHandler extends SecurityActionHandler<VersionDsdAction, VersionDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public VersionDsdActionHandler() {
        super(VersionDsdAction.class);
    }

    @Override
    public VersionDsdResult executeSecurityAction(VersionDsdAction action) throws ActionException {
        try {
            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.versioningDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(),
                    action.getUrn(), action.getVersionType());
            return new VersionDsdResult(dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}

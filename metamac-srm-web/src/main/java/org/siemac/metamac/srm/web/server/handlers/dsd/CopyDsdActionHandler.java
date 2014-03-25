package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.CopyDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.CopyDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyDsdActionHandler extends SecurityActionHandler<CopyDsdAction, CopyDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CopyDsdActionHandler() {
        super(CopyDsdAction.class);
    }

    @Override
    public CopyDsdResult executeSecurityAction(CopyDsdAction action) throws ActionException {
        try {
            TaskInfo taskInfo = srmCoreServiceFacade.copyDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(), action.getCode());
            // DSD is never copied in background
            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(ServiceContextHolder.getCurrentServiceContext(),
                    taskInfo.getUrnResult());
            return new CopyDsdResult(dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

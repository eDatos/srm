package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportDsdActionHandler extends SecurityActionHandler<ExportDsdAction, ExportDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportDsdActionHandler() {
        super(ExportDsdAction.class);
    }

    @Override
    public ExportDsdResult executeSecurityAction(ExportDsdAction action) throws ActionException {
        // StructureMsgDto structureMsgDto = new StructureMsgDto();

        // try {
        // TODO export
        // DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(),
        // action.getUrn(), TypeDozerCopyMode.COPY_ALL_METADATA);
        // structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);
        // String fileName = srmCoreServiceFacade.exportSDMXStructureMsg(ServiceContextHolder.getCurrentServiceContext(), structureMsgDto);
        return new ExportDsdResult(null);
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
    }
}

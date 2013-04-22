package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceAction;
import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportSDMXResourceActionHandler extends SecurityActionHandler<ExportSDMXResourceAction, ExportSDMXResourceResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportSDMXResourceActionHandler() {
        super(ExportSDMXResourceAction.class);
    }

    @Override
    public ExportSDMXResourceResult executeSecurityAction(ExportSDMXResourceAction action) throws ActionException {
        // StructureMsgDto structureMsgDto = new StructureMsgDto();

        // try {
        // TODO export
        // DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(),
        // action.getUrn(), TypeDozerCopyMode.COPY_ALL_METADATA);
        // structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);
        // String fileName = srmCoreServiceFacade.exportSDMXStructureMsg(ServiceContextHolder.getCurrentServiceContext(), structureMsgDto);
        return new ExportSDMXResourceResult(null);
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
    }
}

package org.siemac.metamac.internal.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core_facades.serviceapi.SDMXStructureServiceFacade;
import org.siemac.metamac.domain_dtoext.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain_dtotrans.StructureMsgDto;
import org.siemac.metamac.domain_enum.domain.TypeDozerCopyMode;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.ExportDsdAction;
import org.siemac.metamac.internal.web.shared.ExportDsdResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ExportDsdActionHandler extends AbstractActionHandler<ExportDsdAction, ExportDsdResult> {
    
    @Autowired
    private SDMXStructureServiceFacade sDMXStructureServiceFacade;

    public ExportDsdActionHandler() {
        super(ExportDsdAction.class);
    }

    @Override
    public ExportDsdResult execute(ExportDsdAction action, ExecutionContext context) throws ActionException {
        StructureMsgDto structureMsgDto = new StructureMsgDto();
        
        
        try {
            DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = sDMXStructureServiceFacade.retrieveExtendedDsd(ServiceContextHelper.getServiceContext(), action.getDsd().getId(), TypeDozerCopyMode.UPDATE);            
            structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);
            String fileName = sDMXStructureServiceFacade.exportSDMXStructureMsg(ServiceContextHelper.getServiceContext(), structureMsgDto);
            return new ExportDsdResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(ExportDsdAction action, ExportDsdResult result, ExecutionContext context) throws ActionException {

    }

}

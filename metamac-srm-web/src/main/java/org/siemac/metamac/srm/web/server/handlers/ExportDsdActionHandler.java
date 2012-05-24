package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.domain.trans.dto.StructureMsgDto;
import org.siemac.metamac.srm.web.server.ServiceContextHolder;
import org.siemac.metamac.srm.web.shared.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.ExportDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ExportDsdActionHandler extends AbstractActionHandler<ExportDsdAction, ExportDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportDsdActionHandler() {
        super(ExportDsdAction.class);
    }

    @Override
    public ExportDsdResult execute(ExportDsdAction action, ExecutionContext context) throws ActionException {
        StructureMsgDto structureMsgDto = new StructureMsgDto();

        try {
            DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDsd(ServiceContextHolder.getCurrentServiceContext(), action.getDsd().getId(),
                    TypeDozerCopyMode.UPDATE);
            structureMsgDto.getDataStructureDefinitionDtos().add(dataStructureDefinitionExtendDto);
            String fileName = srmCoreServiceFacade.exportSDMXStructureMsg(ServiceContextHolder.getCurrentServiceContext(), structureMsgDto);
            return new ExportDsdResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(ExportDsdAction action, ExportDsdResult result, ExecutionContext context) throws ActionException {

    }

}

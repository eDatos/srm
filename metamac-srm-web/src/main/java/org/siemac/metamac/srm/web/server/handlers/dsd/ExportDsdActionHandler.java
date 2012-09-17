package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.ExportDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.trans.StructureMsgDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;
import com.gwtplatform.dispatch.server.ExecutionContext;
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
        StructureMsgDto structureMsgDto = new StructureMsgDto();

        try {
            DataStructureDefinitionExtendDto dataStructureDefinitionExtendDto = srmCoreServiceFacade.retrieveExtendedDsd(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(),
                    TypeDozerCopyMode.COPY_ALL_METADATA);
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

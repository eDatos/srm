package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CopyCodelistAction;
import org.siemac.metamac.srm.web.shared.code.CopyCodelistResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyCodelistActionHandler extends SecurityActionHandler<CopyCodelistAction, CopyCodelistResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CopyCodelistActionHandler() {
        super(CopyCodelistAction.class);
    }

    @Override
    public CopyCodelistResult executeSecurityAction(CopyCodelistAction action) throws ActionException {
        try {
            TaskInfo taskInfo = srmCoreServiceFacade.copyCodelist(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), null);

            if (BooleanUtils.isTrue(taskInfo.getIsPlannedInBackground())) {
                // Copied in background
                return new CopyCodelistResult(null, true);
            } else {
                // Copied synchronously
                CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(ServiceContextHolder.getCurrentServiceContext(), taskInfo.getUrnResult());
                return new CopyCodelistResult(codelistMetamacDto, false);
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

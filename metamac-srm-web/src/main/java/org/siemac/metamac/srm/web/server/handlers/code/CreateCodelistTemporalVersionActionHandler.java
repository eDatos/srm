package org.siemac.metamac.srm.web.server.handlers.code;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.code.CreateCodelistTemporalVersionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateCodelistTemporalVersionActionHandler extends SecurityActionHandler<CreateCodelistTemporalVersionAction, CreateCodelistTemporalVersionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateCodelistTemporalVersionActionHandler() {
        super(CreateCodelistTemporalVersionAction.class);
    }

    @Override
    public CreateCodelistTemporalVersionResult executeSecurityAction(CreateCodelistTemporalVersionAction action) throws ActionException {
        try {
            TaskInfo result = srmCoreServiceFacade.createTemporalVersionCodelist(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            if (BooleanUtils.isTrue(result.getIsPlannedInBackground())) {
                CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                return new CreateCodelistTemporalVersionResult(true, codelistMetamacDto);
            } else {
                CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(ServiceContextHolder.getCurrentServiceContext(), result.getUrnResult());
                return new CreateCodelistTemporalVersionResult(false, codelistMetamacDto);
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}

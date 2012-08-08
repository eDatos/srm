package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateDsdProcStatusActionHandlder extends SecurityActionHandler<UpdateDsdProcStatusAction, UpdateDsdProcStatusResult> {

    public UpdateDsdProcStatusActionHandlder() {
        super(UpdateDsdProcStatusAction.class);
    }

    @Override
    public UpdateDsdProcStatusResult executeSecurityAction(UpdateDsdProcStatusAction action) throws ActionException {
        return new UpdateDsdProcStatusResult(new DataStructureDefinitionDto());
    }

}

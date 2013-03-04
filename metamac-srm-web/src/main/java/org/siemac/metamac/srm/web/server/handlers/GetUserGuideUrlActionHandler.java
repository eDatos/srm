package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlAction;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetUserGuideUrlActionHandler extends SecurityActionHandler<GetUserGuideUrlAction, GetUserGuideUrlResult> {

    @Autowired
    private ConfigurationService configurationService = null;

    public GetUserGuideUrlActionHandler() {
        super(GetUserGuideUrlAction.class);
    }

    @Override
    public GetUserGuideUrlResult executeSecurityAction(GetUserGuideUrlAction action) throws ActionException {
        String dataUrl = configurationService.getConfig().getString(SrmWebConstants.DATA_URL);
        String userGuideFileName = configurationService.getConfig().getString(SrmWebConstants.USER_GUIDE_FILE_NAME);
        return new GetUserGuideUrlResult(dataUrl + "/srm/srm-web/docs/" + userGuideFileName);
    }
}

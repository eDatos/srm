package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlAction;
import org.siemac.metamac.srm.web.shared.GetUserGuideUrlResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetUserGuideUrlActionHandler extends SecurityActionHandler<GetUserGuideUrlAction, GetUserGuideUrlResult> {

    private static String        PROP_DATA_URL             = "environment.metamac.data";
    private static String        PROP_USER_GUIDE_FILE_NAME = "metamac.srm.user.guide.file.name";

    @Autowired
    private ConfigurationService configurationService      = null;

    public GetUserGuideUrlActionHandler() {
        super(GetUserGuideUrlAction.class);
    }

    @Override
    public GetUserGuideUrlResult executeSecurityAction(GetUserGuideUrlAction action) throws ActionException {
        String dataUrl = configurationService.getConfig().getString(PROP_DATA_URL);
        String userGuideFileName = configurationService.getConfig().getString(PROP_USER_GUIDE_FILE_NAME);
        return new GetUserGuideUrlResult(dataUrl + "/srm/docs/" + userGuideFileName);
    }

}

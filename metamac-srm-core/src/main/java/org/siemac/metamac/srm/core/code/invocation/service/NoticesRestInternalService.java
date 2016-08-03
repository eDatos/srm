package org.siemac.metamac.srm.core.code.invocation.service;

import java.io.Serializable;

import org.siemac.metamac.core.common.exception.MetamacException;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception);

    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters);

}

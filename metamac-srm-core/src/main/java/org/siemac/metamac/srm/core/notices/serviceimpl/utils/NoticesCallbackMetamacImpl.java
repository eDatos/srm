package org.siemac.metamac.srm.core.notices.serviceimpl.utils;

import java.io.Serializable;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.invocation.service.NoticesRestInternalService;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.notices.serviceimpl.utils.NoticesCallback;

/**
 * Copy Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class NoticesCallbackMetamacImpl implements NoticesCallback {

    public static final String BEAN_ID = "noticesCallbackMetamac";

    @Autowired
    NoticesRestInternalService noticesRestInternalService;

    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception) {
        noticesRestInternalService.createErrorBackgroundNotification(user, actionCode, exception);
    }

    @Override
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters) {
        noticesRestInternalService.createSuccessBackgroundNotification(user, actionCode, successMessageCode, successMessageParameters);
    }

    @Override
    public String getBeanName() {
        return BEAN_ID;
    }
}
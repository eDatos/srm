package org.siemac.metamac.srm.core.notices.serviceimpl.utils;

import java.io.Serializable;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.invocation.service.NoticesRestInternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.notices.serviceimpl.utils.NoticesCallback;

/**
 * Copy Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class NoticesCallbackMetamacImpl implements NoticesCallback {

    public static final String BEAN_ID = "noticesCallbackMetamac";

    private static Logger      logger  = LoggerFactory.getLogger(NoticesCallbackMetamacImpl.class);

    @Autowired
    NoticesRestInternalService noticesRestInternalService;

    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception) {
        try {
            noticesRestInternalService.createErrorBackgroundNotification(user, actionCode, exception);
        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }
    }

    @Override
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters) {
        try {
            noticesRestInternalService.createSuccessBackgroundNotification(user, actionCode, successMessageCode, successMessageParameters);
        } catch (MetamacException e) {
            logger.error("Error creating createSuccessBackgroundNotification:", e);
        }

    }

    @Override
    public String getBeanName() {
        return BEAN_ID;
    }
}
package org.siemac.metamac.srm.core.notices.serviceimpl.utils;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.notices.serviceimpl.utils.NoticesCallback;

/**
 * Copy Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class NoticesCallbackMetamacImpl implements NoticesCallback {

    public static final String BEAN_ID = "noticesCallbackMetamac";

    private static Logger      logger  = LoggerFactory.getLogger(NoticesCallbackMetamacImpl.class);

    // @Autowired
    // NoticesRestInternalService noticesRestInternalService;

    @Override
    public void createNotification(ExceptionLevelEnum exceptionLevel, String message, Exception e) {
        // TODO: METAMAC-2113
        // noticesRestInternalService.createNotification(notification)

        logger.info("----------------------------------------------------------------------------");
        logger.info("                            NOTICE");
        logger.info("----------------------------------------------------------------------------");
        logger.info(message);
        logger.info("----------------------------------------------------------------------------");
    }

    @Override
    public String getBeanName() {
        return BEAN_ID;
    }

}
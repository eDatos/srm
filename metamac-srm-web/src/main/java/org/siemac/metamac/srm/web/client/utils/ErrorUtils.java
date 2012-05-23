package org.siemac.metamac.srm.web.client.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;

public class ErrorUtils extends CommonErrorUtils {

    private static Logger logger = Logger.getLogger(ErrorUtils.class.getName());

    public static List<String> getErrorMessages(Throwable caught, String alternativeMessage) {
        List<String> list = new ArrayList<String>();
        if (caught instanceof MetamacWebException) {
            List<MetamacWebExceptionItem> metamacExceptionItems = ((MetamacWebException) caught).getWebExceptionItems();
            if (metamacExceptionItems.isEmpty()) {
                list.add(alternativeMessage);
            } else {
                for (MetamacWebExceptionItem item : metamacExceptionItems) {
                    if (EXCEPTION_UNKNOWN.equals(item.getCode())) {
                        // If exception code is EXCEPTION_UNKNOWN, show alternative message
                        list.add(alternativeMessage);
                    } else {
                        // If there is any problem getting message from code, show alternative message
                        try {
                            list.add(getMessage(item));
                        } catch (Exception e) {
                            list.add(alternativeMessage);
                        }
                    }
                }
            }
        } else {
            list.add(alternativeMessage);
        }
        return list;
    }

    private static String getMessage(MetamacWebExceptionItem item) throws Exception {
        if (item != null && item.getCode() != null) {
            // GWT generate a "_" separated method when the key is separated by "."
            String code = item.getCode().replace(".", "_");
            try {
                String message = MetamacSrmWeb.getCoreMessages().getString(code);
                return getMessageWithParameters(message, item.getMessageParameters());
            } catch (Exception e) {
                String errorMessage = "Message with code = " + code + " not found";
                logger.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        }
        String errorMessage = "Message with null code";
        logger.log(Level.SEVERE, errorMessage);
        throw new Exception(errorMessage);
    }

}

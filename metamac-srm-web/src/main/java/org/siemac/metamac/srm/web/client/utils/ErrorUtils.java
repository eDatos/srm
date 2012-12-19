package org.siemac.metamac.srm.web.client.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;

public class ErrorUtils extends CommonErrorUtils {

    public static List<String> getErrorMessages(Throwable caught, String alternativeMessage) {
        List<String> list = new ArrayList<String>();
        if (caught != null && caught instanceof MetamacWebException) {
            List<MetamacWebExceptionItem> metamacExceptionItems = ((MetamacWebException) caught).getWebExceptionItems();
            if (metamacExceptionItems.isEmpty()) {
                list.add(alternativeMessage);
            } else {
                for (MetamacWebExceptionItem item : metamacExceptionItems) {
                    if (CommonSharedConstants.EXCEPTION_UNKNOWN.equals(item.getCode())) {
                        // If exception code is EXCEPTION_UNKNOWN, show alternative message
                        list.add(alternativeMessage);
                    } else {
                        // If there is any problem getting message from code, show alternative message
                        try {
                            list.add(getMessage(item));
                        } catch (MetamacWebException e) {
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

    private static String getMessage(MetamacWebExceptionItem item) throws MetamacWebException {
        if (item != null && item.getCode() != null) {
            // GWT generate a "_" separated method when the key is separated by "."
            String code = transformMessageCode(item.getCode());
            try {
                String message = MetamacSrmWeb.getCoreMessages().getString(code);
                return getMessageWithParameters(message, getTranslatedParameters(item.getMessageParameters()));
            } catch (Exception e) {
                throw new MetamacWebException("MESSAGE_NOT_FOUND", "Message with code = " + code + " not found");
            }
        }
        throw new MetamacWebException("NULL_MESSAGE", "Message with null code");
    }

    private static String[] getTranslatedParameters(Serializable[] messageParameters) {
        if (messageParameters != null) {
            String[] translatedParameters = new String[messageParameters.length];
            for (int i = 0; i < messageParameters.length; i++) {
                String translatedParameter = null;
                if (messageParameters[i] instanceof String) {
                    translatedParameter = getTranslatedParameter(String.valueOf(messageParameters[i]));
                } else if (messageParameters[i] instanceof String[]) {
                    List<String> params = new ArrayList<String>();
                    for (String param : (String[]) messageParameters[i]) {
                        params.add(getTranslatedParameter(param));
                    }
                    translatedParameter = CommonWebUtils.getStringListToString(params);
                }
                translatedParameters[i] = translatedParameter;
            }
            return translatedParameters;
        }
        return null;
    }

    private static String getTranslatedParameter(String parameter) {
        String code = transformMessageCode(String.valueOf(parameter));
        try {
            // If parameter is translated, return the translation
            return MetamacSrmWeb.getCoreMessages().getString(code) + " (" + String.valueOf(parameter) + ")";
        } catch (Exception e) {
            return String.valueOf(parameter);
        }
    }
}
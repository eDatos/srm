package org.siemac.metamac.srm.web.external;

import java.net.MalformedURLException;

import org.siemac.metamac.core.common.util.swagger.SwaggerUtils;

public class WebUtils {

    protected static String organisation = null;
    protected static String apiBaseUrl   = null;

    public static void setApiBaseURL(String apiBaseUrl) {
        WebUtils.apiBaseUrl = SwaggerUtils.normalizeUrl(apiBaseUrl);
    }

    public static String getApiBaseURL() throws MalformedURLException {
        return apiBaseUrl;
    }

    public static void setOrganisation(String organisation) {
        WebUtils.organisation = organisation;
    }

    public static String getFavicon() {
        return SwaggerUtils.getFavicon(organisation);
    }
}

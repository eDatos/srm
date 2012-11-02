package org.siemac.metamac.srm.web.shared.utils;

import com.gwtplatform.dispatch.shared.SecurityCookie;

public class SharedTokens extends org.siemac.metamac.web.common.shared.utils.SharedTokens {

    @SecurityCookie
    public static final String securityCookieName = "securityCookieName";

}

package org.siemac.metamac.srm.web.shared.utils;

import com.gwtplatform.dispatch.shared.SecurityCookie;

public class SharedTokens {

    public static final String FILE_UPLOAD_DIR_PATH   = "files/upload";
    public static final String FILE_DOWNLOAD_DIR_PATH = "files/download";
    public static final String PARAM_FILE_NAME        = "fileName";

    @SecurityCookie
    public static final String securityCookieName     = "securityCookieName";

}

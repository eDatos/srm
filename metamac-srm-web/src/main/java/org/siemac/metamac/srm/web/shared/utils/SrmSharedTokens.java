package org.siemac.metamac.srm.web.shared.utils;

import com.gwtplatform.dispatch.shared.SecurityCookie;

public class SrmSharedTokens extends org.siemac.metamac.web.common.shared.utils.SharedTokens {

    //
    // SECURITY
    //

    @SecurityCookie
    public static final String securityCookieName                       = "securityCookieName";

    //
    // FILE UPLOAD PARAMETERS
    //

    // Common upload configuration

    public static final String UPLOAD_PARAM_FILE_TYPE                   = "file-type";

    // Codes upload configuration

    public static final String UPLOAD_PARAM_CODELIST_URN                = "codelist-urn";
    public static final String UPLOAD_PARAM_VARIABLE_URN                = "variable-urn";
    public static final String UPLOAD_PARAM_UPDATE_EXISTING             = "update-existing";
    public static final String UPLOAD_PARAM_VARIABLE_ELEMENT_SHAPE_TYPE = "var-ele-shp-type";

    // Concepts upload configuration

    public static final String UPLOAD_PARAM_CONCEPT_SCHEME_URN          = "concept-scheme-urn";

    // Organisations upload configuration

    public static final String UPLOAD_PARAM_ORGANISATION_SCHEME_URN     = "organisation-scheme-urn";

    // Categories upload configuration

    public static final String UPLOAD_PARAM_CATEGORY_SCHEME_URN         = "category-scheme-urn";
}

package org.siemac.metamac.srm.web.code.model.ds;

import com.smartgwt.client.data.DataSource;

public class VariableDS extends DataSource {

    // Identifiers
    public static final String ID                   = "var-id";
    public static final String CODE                 = "var-code";
    public static final String NAME                 = "var-name";
    public static final String SHORT_NAME           = "var-short-name";
    public static final String URN                  = "var-urn";
    // Content descriptors
    public static final String FAMILIES             = "var-family";
    public static final String IS_GEOGRAPHICAL      = "var-type";
    public static final String IS_GEOGRAPHICAL_VIEW = "var-type-view";  // Not mapped in DTO
    // Diffusion descriptors
    public static final String REPLACE_TO_VARIABLES = "var-replace-to";
    public static final String REPLACED_BY_VARIABLE = "var-replaced-by";
    public static final String VALID_FROM           = "var-valid-from";
    public static final String VALID_TO             = "var-valid-to";

    public static final String DTO                  = "var-dto";

}

package org.siemac.metamac.srm.web.client.code.model.ds;

import com.smartgwt.client.data.DataSource;

public class VariableElementDS extends DataSource {

    // Identifiers
    public static final String ID                  = "ele-id";
    public static final String CODE                = "ele-code";
    public static final String NAME                = "ele-name";
    public static final String SHORT_NAME          = "ele-short-name";
    public static final String URN                 = "ele-urn";
    // Content descriptors
    public static final String VARIABLE            = "ele-variable";
    // Diffusion descriptors
    public static final String VALID_FROM          = "ele-valid-from";
    public static final String VALID_TO            = "ele-valid-to";
    public static final String REPLACE_TO_ELEMENTS = "ele-replace-to";
    public static final String REPLACED_BY_ELEMENT = "ele-replaced-by";

    public static final String DTO                 = "ele-dto";
}

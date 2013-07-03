package org.siemac.metamac.srm.web.code.model.ds;

import com.smartgwt.client.data.DataSource;

public class VariableElementDS extends DataSource {

    // Identifiers
    public static final String ID                       = "ele-id";
    public static final String CODE                     = "ele-code";
    public static final String SHORT_NAME               = "ele-short-name";
    public static final String URN                      = "ele-urn";
    // Content descriptors
    public static final String VARIABLE                 = "ele-variable";
    // Geographical information
    public static final String GEOGRAPHICAL_GRANULARITY = "ele-geo-gran";
    public static final String LATITUDE                 = "ele-lat";
    public static final String LONGITUDE                = "ele-lon";
    public static final String SHAPE                    = "ele-shape";
    // Diffusion descriptors
    public static final String VALID_FROM               = "ele-valid-from";
    public static final String VALID_TO                 = "ele-valid-to";
    public static final String REPLACE_TO_ELEMENTS      = "ele-replace-to";
    public static final String REPLACED_BY_ELEMENT      = "ele-replaced-by";
    // Annotations
    public static final String COMMENTS                 = "ele-comments";

    public static final String DTO                      = "ele-dto";
}

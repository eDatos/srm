package org.siemac.metamac.srm.web.dsd.model.ds;

public class DataStructureDefinitionDS {

    // IDENTIFIERS
    public static final String ID                               = "dsd-id";
    public static final String CODE                             = "dsd-code";
    public static final String CODE_VIEW                        = "dsd-code-view";        // Not mapped in DTO
    public static final String URI                              = "dsd-uri";
    public static final String URN                              = "dsd-urn";
    public static final String URN_PROVIDER                     = "dsd-urn-provider";
    public static final String VERSION_LOGIC                    = "dsd-version";
    public static final String NAME                             = "dsd-name";
    public static final String DESCRIPTION                      = "dsd-desc";
    public static final String MAINTAINER                       = "dsd-main";

    // LIFE CYCLE
    public static final String PROC_STATUS                      = "dsd-status";
    public static final String FINAL                            = "dsd-final";
    public static final String IS_EXTERNAL_REFERENCE            = "dsd-ext-ref";
    public static final String REPLACED_BY_VERSION              = "dsd-replaced-by";
    public static final String REPLACE_TO_VERSION               = "dsd-replace-to";
    public static final String VALID_FROM                       = "dsd-valid-from";
    public static final String VALID_TO                         = "dsd-valid-to";

    public static final String PRODUCTION_VALIDATION_DATE       = "dsd-prod-date";
    public static final String PRODUCTION_VALIDATION_USER       = "dsd-prod-user";
    public static final String DIFFUSION_VALIDATION_DATE        = "dsd-dif-date";
    public static final String DIFFUSION_VALIDATION_USER        = "dsd-dif-user";
    public static final String INTERNAL_PUBLICATION_DATE        = "dsd-int-pub-date";
    public static final String INTERNAL_PUBLICATION_USER        = "dsd-int-pub-user";
    public static final String EXTERNAL_PUBLICATION_DATE        = "dsd-ext-pub-date";
    public static final String EXTERNAL_PUBLICATION_USER        = "dsd-ext-pub-user";
    public static final String IS_EXTERNAL_PUBLICATION_FAILED   = "dsd-ext-pub-fail";
    public static final String EXTERNAL_PUBLICATION_FAILED_DATE = "dsd-ext-pub-fail-date";

    // Visualisation metadata
    public static final String AUTO_OPEN                        = "dsd-auto-open";

    // COMMENTS
    public static final String COMMENTS                         = "dsd-comments";

    public static final String DTO                              = "dsd-dto";

}

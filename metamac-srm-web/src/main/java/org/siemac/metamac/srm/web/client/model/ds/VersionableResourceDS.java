package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.DataSource;

public class VersionableResourceDS extends DataSource {

    // IDENTIFIERS
    public static final String ID                         = "lc-id";
    public static final String CODE                       = "lc-code";
    public static final String CODE_VIEW                  = "lc-code-view";             // Not mapped in DTO
    public static final String URI                        = "lc-uri";
    public static final String URN                        = "lc-urn";
    public static final String URN_PROVIDER               = "lc-urn-provider";
    public static final String VERSION_LOGIC              = "lc-version";
    public static final String NAME                       = "lc-name";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION                = "lc-desc";
    public static final String DESCRIPTION_SOURCE         = "lc-desc-source";
    public static final String IS_EXTERNAL_REFERENCE      = "lc-ext-ref";
    public static final String FINAL                      = "lc-final";
    // CLASS DESCRIPTORS
    public static final String MAINTAINER                 = "lc-maintainer";
    // PRODUCTION DESCRIPTORS
    public static final String PROC_STATUS                = "lc-status";
    public static final String VERSION_CREATION_DATE      = "lc-creation-date";
    public static final String VERSION_CREATION_USER      = "lc-creation-user";
    public static final String VERSION_LAST_UPDATE_DATE   = "lc-last-update-date";
    public static final String VERSION_LAST_UPDATE_USER   = "lc-last-update-user";
    public static final String RESOURCE_CREATION_DATE     = "lc-resource-creation-date";
    public static final String PRODUCTION_VALIDATION_DATE = "lc-prod-date";
    public static final String PRODUCTION_VALIDATION_USER = "lc-prod-user";
    // DIFFUSION DESCRIPTORS
    public static final String REPLACED_BY_VERSION        = "lc-replaced-by";
    public static final String REPLACE_TO_VERSION         = "lc-replace-to";
    public static final String VALID_FROM                 = "lc-valid-from";
    public static final String VALID_TO                   = "lc-valid-to";
    public static final String DIFFUSION_VALIDATION_DATE  = "lc-dif-date";
    public static final String DIFFUSION_VALIDATION_USER  = "lc-dif-user";
    public static final String INTERNAL_PUBLICATION_DATE  = "lc-int-pub-date";
    public static final String INTERNAL_PUBLICATION_USER  = "lc-int-pub-user";
    public static final String EXTERNAL_PUBLICATION_DATE  = "lc-ext-pub-date";
    public static final String EXTERNAL_PUBLICATION_USER  = "lc-ext-pub-user";
    // COMMENTS
    public static final String COMMENTS                   = "lc-comments";

    public static final String IS_LAST_VERSION            = "sch-last-version";         // For advanced search
    public static final String RESOURCE_ID                = "lc-resource-id";           // For grouping itemSchemes or DSDs
}

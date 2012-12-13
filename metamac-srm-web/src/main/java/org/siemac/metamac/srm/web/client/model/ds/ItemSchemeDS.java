package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ItemSchemeDS extends DataSource {

    // IDENTIFIERS
    public static final String ID                               = "sch-id";
    public static final String CODE                             = "sch-code";
    public static final String CODE_VIEW                        = "sch-code-view";        // Not mapped in DTO
    public static final String URI                              = "sch-uri";
    public static final String URN                              = "sch-urn";
    public static final String URN_PROVIDER                     = "sch-urn-provider";
    public static final String VERSION_LOGIC                    = "sch-version";
    public static final String NAME                             = "sch-name";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION                      = "sch-desc";
    public static final String IS_PARTIAL                       = "sch-is-partial";
    public static final String IS_EXTERNAL_REFERENCE            = "sch-ext-ref";
    public static final String FINAL                            = "sch-final";
    // CLASS DESCRIPTORS
    public static final String MAINTAINER                       = "sch-maintainer";
    // PRODUCTION DESCRIPTORS
    public static final String PROC_STATUS                      = "sch-status";
    public static final String PRODUCTION_VALIDATION_DATE       = "sch-prod-date";
    public static final String PRODUCTION_VALIDATION_USER       = "sch-prod-user";
    // DIFFUSION DESCRIPTORS
    public static final String REPLACED_BY_VERSION              = "sch-replaced-by";
    public static final String REPLACE_TO_VERSION               = "sch-replace-to";
    public static final String VALID_FROM                       = "sch-valid-from";
    public static final String VALID_TO                         = "sch-valid-to";
    public static final String DIFFUSION_VALIDATION_DATE        = "sch-dif-date";
    public static final String DIFFUSION_VALIDATION_USER        = "sch-dif-user";
    public static final String INTERNAL_PUBLICATION_DATE        = "sch-int-pub-date";
    public static final String INTERNAL_PUBLICATION_USER        = "sch-int-pub-user";
    public static final String EXTERNAL_PUBLICATION_DATE        = "sch-ext-pub-date";
    public static final String EXTERNAL_PUBLICATION_USER        = "sch-ext-pub-user";
    public static final String IS_EXTERNAL_PUBLICATION_FAILED   = "sch-ext-pub-fail";
    public static final String EXTERNAL_PUBLICATION_FAILED_DATE = "sch-ext-pub-fail-date";

    public static String       DTO                              = "sch-dto";

    public ItemSchemeDS() {
        DataSourceIntegerField identifier = new DataSourceIntegerField(ID, "identifier");
        identifier.setPrimaryKey(true);
        addField(identifier);
    }

}

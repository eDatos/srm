package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class RepresentationDS extends DataSource {

    public static final String ID                    = "repr-id";

    public static final String TYPE                  = "repr-type";
    public static final String TYPE_VIEW             = "repr-type-view"; // Not mapped in DTO

    public static final String ENUMERATED            = "repr-enum";
    public static final String ENUMERATED_VIEW       = "repr-enum-view"; // Not mapped in DTO

    public static final String FACET_IS_SEQUENCE     = "fac-seq";
    public static final String FACET_INTERVAL        = "fac-int";
    public static final String FACET_START_VALUE     = "fac-start-val";
    public static final String FACET_END_VALUE       = "fac-end-val";
    public static final String FACET_TIME_INTERVAL   = "fac-time-int";
    public static final String FACET_START_TIME      = "fac-start-tim";
    public static final String FACET_END_TIME        = "fac-end-tim";
    public static final String FACET_MIN_LENGTH      = "fac-min-len";
    public static final String FACET_MAX_LENGTH      = "fac-max-len";
    public static final String FACET_MIN_VALUE       = "fac-min-val";
    public static final String FACET_MAX_VALUE       = "fac-max-val";
    public static final String FACET_DECIMALS        = "fac-dec";
    public static final String FACET_PATTERN         = "fac-pat";
    public static final String FACET_XHTML           = "fac-xhtml";
    public static final String FACET_IS_MULTILINGUAL = "fac-multi";
    public static final String FACET_VALUE           = "fac-value";
    public static final String FACET_ITEM_SCHEME     = "fac-item-scheme";

    public RepresentationDS() {
        DataSourceIntegerField id = new DataSourceIntegerField(ID, "identifier");
        id.setPrimaryKey(true);
        addField(id);
    }
}

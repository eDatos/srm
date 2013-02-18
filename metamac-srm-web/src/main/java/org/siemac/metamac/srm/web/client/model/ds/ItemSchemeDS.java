package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ItemSchemeDS extends LifeCycleDS {

    public static final String IS_PARTIAL      = "sch-is-partial";
    public static final String IS_LAST_VERSION = "sch-last-version"; // For advanced search

    public static String       DTO             = "sch-dto";

    public ItemSchemeDS() {
        DataSourceIntegerField identifier = new DataSourceIntegerField(ID, "identifier");
        identifier.setPrimaryKey(true);
        addField(identifier);
    }
}

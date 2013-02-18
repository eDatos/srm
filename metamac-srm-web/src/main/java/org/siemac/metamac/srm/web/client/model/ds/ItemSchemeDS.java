package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ItemSchemeDS extends VersionableResourceDS {

    public static final String IS_PARTIAL = "sch-is-partial";

    public static String       DTO        = "sch-dto";

    public ItemSchemeDS() {
        DataSourceIntegerField identifier = new DataSourceIntegerField(ID, "identifier");
        identifier.setPrimaryKey(true);
        addField(identifier);
    }
}

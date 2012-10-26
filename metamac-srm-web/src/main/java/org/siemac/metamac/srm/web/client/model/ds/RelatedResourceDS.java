package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class RelatedResourceDS extends DataSource {

    public static final String CODE  = "code";
    public static final String URN   = "urn";
    public static final String TITLE = "title";

    public static final String DTO   = "dto";

    public RelatedResourceDS() {
        DataSourceTextField urn = new DataSourceTextField(URN);
        urn.setPrimaryKey(true);
        addField(urn);
    }

}

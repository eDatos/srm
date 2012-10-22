package org.siemac.metamac.srm.web.client.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class ItemDS extends DataSource {

    // IDENTIFIERS
    public static final String ID          = "item-id";
    public static final String CODE        = "item-code";
    public static final String URI         = "item-uri";
    public static final String URN         = "item-urn";
    public static final String NAME        = "item-name";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION = "item-desc";

    public static String       DTO         = "item-dto";

    public ItemDS() {
        DataSourceTextField code = new DataSourceTextField(CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        code.setPrimaryKey(true);
        addField(code);
    }

}

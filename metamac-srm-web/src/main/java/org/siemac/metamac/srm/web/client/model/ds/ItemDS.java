package org.siemac.metamac.srm.web.client.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class ItemDS extends DataSource {

    // IDENTIFIERS
    public static final String ID          = "item-id";
    public static final String CODE        = "item-code";
    public static final String CODE_VIEW   = "item-code-view"; // Not mapped in DTO
    public static final String URI         = "item-uri";
    public static final String URN         = "item-urn";
    public static final String NAME        = "item-name";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION = "item-desc";

    public static String       DTO         = "item-dto";

    public ItemDS() {
        DataSourceTextField urn = new DataSourceTextField(URN, MetamacSrmWeb.getConstants().identifiableArtefactUrn());
        urn.setPrimaryKey(true);
        addField(urn);

        DataSourceTextField code = new DataSourceTextField(CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        code.setCanFilter(true);
        addField(code);

        DataSourceTextField name = new DataSourceTextField(NAME, MetamacSrmWeb.getConstants().nameableArtefactName());
        name.setCanFilter(true);
        addField(name);

        setClientOnly(true);
    }

}

package org.siemac.metamac.srm.web.organisation.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ContactDS extends DataSource {

    public static final String ID                = "con-id";
    public static final String NAME              = "con-id";
    public static final String ORGANISATION_UNIT = "con-id";
    public static final String TELEPHONE         = "con-id";
    public static final String RESPONSIBILITY    = "con-id";
    public static final String FAX               = "con-id";
    public static final String EMAIL             = "con-id";
    public static final String URL               = "con-id";

    public ContactDS() {
        DataSourceIntegerField identifier = new DataSourceIntegerField(ID, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        identifier.setPrimaryKey(true);
        addField(identifier);
    }

}

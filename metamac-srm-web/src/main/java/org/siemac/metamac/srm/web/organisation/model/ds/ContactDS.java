package org.siemac.metamac.srm.web.organisation.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ContactDS extends DataSource {

    public static final String ID                = "con-id";
    public static final String NAME              = "con-name";
    public static final String ORGANISATION_UNIT = "con-ou";
    public static final String TELEPHONE         = "con-tel";
    public static final String RESPONSIBILITY    = "con-res";
    public static final String FAX               = "con-fax";
    public static final String EMAIL             = "con-email";
    public static final String URL               = "con-url";

    public static final String DTO               = "con-dto";

    public ContactDS() {
        DataSourceIntegerField identifier = new DataSourceIntegerField(ID, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        identifier.setPrimaryKey(true);
        addField(identifier);
    }

}

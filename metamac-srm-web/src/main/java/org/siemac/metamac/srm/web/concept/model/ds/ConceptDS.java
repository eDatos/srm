package org.siemac.metamac.srm.web.concept.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ConceptDS extends DataSource {

    public static final String ID          = "con-id";
    public static final String ID_LOGIC    = "con-id-logic";
    public static final String NAME        = "con-name";
    public static final String DESCRIPTION = "con-desc";

    public ConceptDS() {
        DataSourceIntegerField idLogic = new DataSourceIntegerField(ID_LOGIC, MetamacSrmWeb.getConstants().conceptSchemeIdLogic());
        idLogic.setPrimaryKey(true);
        addField(idLogic);
    }

}

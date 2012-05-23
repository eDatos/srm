package org.siemac.metamac.internal.web.concept.model.ds;

import org.siemac.metamac.internal.web.client.MetamacInternalWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ConceptSchemeDS extends DataSource {

    // IDENTIFIERS
    public static final String ID          = "sch-id";
    public static final String ID_LOGIC    = "sch-id-logic";
    public static final String UUID        = "sch-uuid";
    public static final String NAME        = "sch-name";
    public static final String DESCRIPTION = "sch-desc";
    public static final String PROC_STATUS = "sch-status";

    public static String       DTO         = "sch-dto";

    public ConceptSchemeDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(UUID, MetamacInternalWeb.getConstants().conceptId());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }

}

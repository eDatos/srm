package org.siemac.metamac.srm.web.concept.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ConceptSchemeDS extends DataSource {

    // IDENTIFIERS
    public static final String ID                = "sch-id";
    public static final String URI               = "sch-uri";
    public static final String URN               = "sch-urn";
    public static final String ID_LOGIC          = "sch-id-logic";
    public static final String VERSION           = "sch-version";
    public static final String NAME              = "sch-name";
    public static final String DESCRIPTION       = "sch-desc";
    public static final String IS_PARTIAL        = "sch-is-partial";
    public static final String AGENCY            = "sch-agency";
    public static final String TYPE              = "sch-type";
    public static final String RELATED_OPERATION = "sch-operation";
    public static final String PROC_STATUS       = "sch-status";
    public static final String VALID_FROM        = "sch-valid-from";
    public static final String VALID_TO          = "sch-valid-to";
    public static final String FINAL             = "sch-final";

    public static String       DTO               = "sch-dto";

    public ConceptSchemeDS() {
        DataSourceIntegerField idLogic = new DataSourceIntegerField(ID_LOGIC, MetamacSrmWeb.getConstants().conceptId());
        idLogic.setPrimaryKey(true);
        addField(idLogic);
    }

}

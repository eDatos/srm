package org.siemac.metamac.srm.web.concept.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ConceptSchemeDS extends DataSource {

    // IDENTIFIERS
    public static final String ID                = "sch-id";
    public static final String CODE              = "sch-code";
    public static final String CODE_VIEW         = "sch-code-view "; // Not mapped in DTO
    public static final String URI               = "sch-uri";
    public static final String URN               = "sch-urn";
    public static final String VERSION_LOGIC     = "sch-version";
    public static final String NAME              = "sch-name";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION       = "sch-desc";
    public static final String IS_PARTIAL        = "sch-is-partial";
    // CLASS DESCRIPTORS
    public static final String AGENCY            = "sch-agency";
    public static final String TYPE              = "sch-type";
    public static final String RELATED_OPERATION = "sch-operation";
    // PRODUCTION DESCRIPTORS
    public static final String PROC_STATUS       = "sch-status";
    // DIFFUSION DESCRIPTORS
    public static final String VALID_FROM        = "sch-valid-from";
    public static final String VALID_TO          = "sch-valid-to";

    public static String       DTO               = "sch-dto";

    public ConceptSchemeDS() {
        DataSourceIntegerField code = new DataSourceIntegerField(CODE, MetamacSrmWeb.getConstants().conceptCode());
        code.setPrimaryKey(true);
        addField(code);
    }

}

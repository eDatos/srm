package org.siemac.metamac.srm.web.organisation.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class OrganisationDS extends DataSource {

    // IDENTIFIERS
    public static final String ID          = "org-id";
    public static final String CODE        = "org-code";
    public static final String URI         = "org-uri";
    public static final String URN         = "org-urn";
    public static final String NAME        = "org-name";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION = "org-desc";
    public static final String TYPE        = "org-type";

    public static String       DTO         = "org-dto";

    public OrganisationDS() {
        DataSourceTextField code = new DataSourceTextField(CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        code.setPrimaryKey(true);
        addField(code);
    }

}

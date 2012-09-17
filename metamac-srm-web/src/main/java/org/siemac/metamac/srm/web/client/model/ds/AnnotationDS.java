package org.siemac.metamac.srm.web.client.model.ds;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class AnnotationDS extends DataSource {

    public static final String ID             = "an-id";
    public static final String CODE           = "an-code";
    public static final String TITLE          = "an-title";
    public static final String TYPE           = "an-type";
    public static final String URL            = "an-url";
    public static final String TEXT           = "an-text";
    public static final String ANNOTATION_DTO = "an-annotation";

    public AnnotationDS() {
        DataSourceIntegerField id = new DataSourceIntegerField(ID, "identifier");
        id.setPrimaryKey(true);
        addField(id);
    }

}

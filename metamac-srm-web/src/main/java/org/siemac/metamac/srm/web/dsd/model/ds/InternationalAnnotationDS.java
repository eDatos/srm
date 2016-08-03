package org.siemac.metamac.srm.web.dsd.model.ds;

import org.siemac.metamac.srm.web.dsd.model.record.InternationalAnnotationRecord;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class InternationalAnnotationDS extends DataSource {

    public InternationalAnnotationDS() {
        DataSourceIntegerField id = new DataSourceIntegerField(InternationalAnnotationRecord.ID, "Id");
        addField(id);

        DataSourceTextField idDs = new DataSourceTextField(InternationalAnnotationRecord.ID_DS, "Id DS");
        addField(idDs);
        idDs.setPrimaryKey(true);

        DataSourceTextField title = new DataSourceTextField(InternationalAnnotationRecord.TITLE, "Title");
        addField(title);

        DataSourceTextField locale = new DataSourceTextField(InternationalAnnotationRecord.LOCALE, "Locale");
        addField(locale);

        DataSourceTextField text = new DataSourceTextField(InternationalAnnotationRecord.TEXT, "Text");
        addField(text);

        setClientOnly(true);
    }

}

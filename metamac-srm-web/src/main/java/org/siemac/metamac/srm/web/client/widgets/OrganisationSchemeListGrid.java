package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.utils.RecordUtils;

import com.smartgwt.client.widgets.grid.ListGridField;

public class OrganisationSchemeListGrid extends ItemSchemeListGrid {

    public OrganisationSchemeListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField typeField = new ListGridField(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());

        ListGridField[] initialListGridFields = getAllFields();

        ListGridField[] fields = new ListGridField[initialListGridFields.length + 1];
        System.arraycopy(initialListGridFields, 0, fields, 0, initialListGridFields.length);
        fields[fields.length - 1] = typeField;

        this.setFields(fields);
    }

    public void setOrganisationSchemes(List<OrganisationSchemeMetamacDto> organisationSchemeDtos) {
        removeAllData();
        if (organisationSchemeDtos != null) {
            OrganisationSchemeRecord[] schemeRecords = new OrganisationSchemeRecord[organisationSchemeDtos.size()];
            for (int i = 0; i < organisationSchemeDtos.size(); i++) {
                schemeRecords[i] = RecordUtils.getOrganisationSchemeRecord(organisationSchemeDtos.get(i));
            }
            this.setData(schemeRecords);
        }
    }
}

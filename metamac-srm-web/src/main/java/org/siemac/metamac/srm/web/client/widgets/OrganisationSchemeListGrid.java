package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OrganisationSchemeListGrid extends BaseCustomListGrid {

    public OrganisationSchemeListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField codeField = new ListGridField(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        ListGridField nameField = new ListGridField(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());
        ListGridField procStatusField = new ListGridField(OrganisationSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ListGridField typeField = new ListGridField(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());

        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                OrganisationSchemeRecord schemeRecord = (OrganisationSchemeRecord) record;
                return schemeRecord.getCode();
            }
        });

        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                OrganisationSchemeRecord schemeRecord = (OrganisationSchemeRecord) record;
                return schemeRecord.getName();
            }
        });

        this.setFields(codeField, nameField, procStatusField, typeField);
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

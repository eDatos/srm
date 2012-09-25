package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.VersionsSectionStack;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.utils.RecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.grid.ListGridField;

public class OrganisationSchemeVersionsSectionStack extends VersionsSectionStack {

    public OrganisationSchemeVersionsSectionStack(String title) {
        super(title);

        ListGridField codeField = new ListGridField(OrganisationSchemeDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");

        ListGridField nameField = new ListGridField(OrganisationSchemeDS.NAME, getConstants().nameableArtefactName());

        ListGridField versionField = new ListGridField(OrganisationSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        versionField.setWidth("15%");

        listGrid.setFields(codeField, nameField, versionField);
    }

    public void setOrganisationSchemes(List<OrganisationSchemeMetamacDto> organisationSchemeDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (OrganisationSchemeMetamacDto organisationSchemeDto : organisationSchemeDtos) {
            listGrid.addData(RecordUtils.getOrganisationSchemeRecord(organisationSchemeDto));
        }
    }

    public void selectOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(OrganisationSchemeDS.URN, organisationSchemeMetamacDto.getUrn());
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }

}

package org.siemac.metamac.srm.web.organisation.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.ItemSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.srm.web.organisation.utils.OrganisationsRecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.SortDirection;

public class OrganisationSchemeVersionsSectionStack extends ItemSchemeVersionsSectionStack {

    public OrganisationSchemeVersionsSectionStack(String title) {
        super(title);
    }

    public void setOrganisationSchemes(List<OrganisationSchemeMetamacBasicDto> organisationSchemeDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (OrganisationSchemeMetamacBasicDto organisationSchemeDto : organisationSchemeDtos) {
            listGrid.addData(OrganisationsRecordUtils.getOrganisationSchemeRecord(organisationSchemeDto));
        }
        listGrid.sort(OrganisationSchemeDS.VERSION_LOGIC, SortDirection.DESCENDING);
    }

    public void selectOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(OrganisationSchemeDS.URN, organisationSchemeMetamacDto.getUrn());
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }
}

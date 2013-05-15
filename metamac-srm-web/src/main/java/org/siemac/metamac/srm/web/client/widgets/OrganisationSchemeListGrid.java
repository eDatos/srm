package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.organisation.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

public class OrganisationSchemeListGrid extends BaseCustomListGrid {

    public OrganisationSchemeListGrid() {
        super();
        this.setFields(ResourceFieldUtils.getOrganisationSchemeListGridFields());
    }

    public void setOrganisationSchemes(List<OrganisationSchemeMetamacBasicDto> organisationSchemeDtos) {
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

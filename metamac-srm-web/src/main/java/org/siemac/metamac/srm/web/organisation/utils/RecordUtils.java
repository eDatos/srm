package org.siemac.metamac.srm.web.organisation.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;

public class RecordUtils {

    // ORGANISATION SCHEMES

    public static OrganisationSchemeRecord getOrganisationSchemeRecord(OrganisationSchemeMetamacDto organisationSchemeDto) {
        OrganisationSchemeRecord record = new OrganisationSchemeRecord(organisationSchemeDto.getId(), organisationSchemeDto.getCode(), getLocalisedString(organisationSchemeDto.getName()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()),
                CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()), organisationSchemeDto.getVersionLogic(), organisationSchemeDto.getUrn(), organisationSchemeDto);
        return record;
    }

}

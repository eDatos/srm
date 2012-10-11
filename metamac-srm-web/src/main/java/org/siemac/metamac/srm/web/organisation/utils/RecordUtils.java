package org.siemac.metamac.srm.web.organisation.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.record.ContactRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public class RecordUtils {

    // ORGANISATION SCHEMES

    public static OrganisationSchemeRecord getOrganisationSchemeRecord(OrganisationSchemeMetamacDto organisationSchemeDto) {
        OrganisationSchemeRecord record = new OrganisationSchemeRecord(organisationSchemeDto.getId(), organisationSchemeDto.getCode(), getLocalisedString(organisationSchemeDto.getName()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()),
                CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()), organisationSchemeDto.getVersionLogic(), organisationSchemeDto.getUrn(), organisationSchemeDto);
        return record;
    }

    // ORGANISATIONS

    public static OrganisationRecord getOrganisationRecord(OrganisationMetamacDto organisationDto) {
        OrganisationRecord record = new OrganisationRecord(organisationDto.getId(), organisationDto.getCode(), getLocalisedString(organisationDto.getName()), organisationDto.getUrn());
        return record;
    }

    public static OrganisationRecord getOrganisationRecord(ItemHierarchyDto organisation) {
        OrganisationRecord record = new OrganisationRecord(organisation.getItem().getId(), organisation.getItem().getCode(), getLocalisedString(organisation.getItem().getName()), organisation
                .getItem().getUrn());
        return record;
    }

    // CONTACTS

    public static ContactRecord getContactRecord(ContactDto contactDto) {
        ContactRecord record = new ContactRecord(contactDto.getId(), InternationalStringUtils.getLocalisedString(contactDto.getName()), InternationalStringUtils.getLocalisedString(contactDto
                .getOrganisationUnit()), contactDto.getUrl(), contactDto);
        return record;
    }

}

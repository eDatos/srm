package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.organisation.model.record.ContactRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsRecordUtils extends org.siemac.metamac.srm.web.client.utils.RecordUtils {

    // ORGANISATION SCHEMES

    public static OrganisationSchemeRecord getOrganisationSchemeRecord(OrganisationSchemeMetamacBasicDto organisationSchemeDto) {
        OrganisationSchemeRecord record = new OrganisationSchemeRecord();
        record = (OrganisationSchemeRecord) getItemSchemeRecord(record, organisationSchemeDto, organisationSchemeDto.getLifeCycle());
        record.setType(CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        record.setOrganisationSchemeBasicDto(organisationSchemeDto);
        return record;
    }

    // ORGANISATIONS

    public static OrganisationRecord getOrganisationRecord(OrganisationMetamacBasicDto organisationDto) {
        OrganisationRecord record = new OrganisationRecord();
        record = (OrganisationRecord) getItemRecord(record, organisationDto);
        record.setOrganisationTypeName(CommonUtils.getOrganisationTypeName(organisationDto.getType()));
        record.setOrganisationType(organisationDto.getType());
        record.setOrganisationSpecialHasBeenPublished(organisationDto.getSpecialOrganisationHasBeenPublished());
        return record;
    }

    public static OrganisationRecord getOrganisationRecord(OrganisationMetamacVisualisationResult organisation, OrganisationTypeEnum organisationTypeEnum) {
        OrganisationRecord record = new OrganisationRecord();
        record.setId(organisation.getItemIdDatabase());
        record.setCode(organisation.getCode());
        record.setName(organisation.getName());
        record.setUrn(organisation.getUrn());
        record.setOrganisationSpecialHasBeenPublished(organisation.getSpecialOrganisationHasBeenPublished());
        return record;
    }

    // CONTACTS

    public static ContactRecord getContactRecord(ContactDto contactDto) {
        ContactRecord record = new ContactRecord(contactDto.getId(), InternationalStringUtils.getLocalisedString(contactDto.getName()), InternationalStringUtils.getLocalisedString(contactDto
                .getOrganisationUnit()), contactDto);
        return record;
    }
}

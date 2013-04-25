package org.siemac.metamac.srm.web.organisation.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.record.ContactRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationRecord;
import org.siemac.metamac.srm.web.organisation.model.record.OrganisationSchemeRecord;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class RecordUtils {

    // ORGANISATION SCHEMES

    public static OrganisationSchemeRecord getOrganisationSchemeRecord(OrganisationSchemeMetamacDto organisationSchemeDto) {
        OrganisationSchemeRecord record = new OrganisationSchemeRecord();
        record.setId(organisationSchemeDto.getId());
        record.setCode(organisationSchemeDto.getCode());
        record.setName(getLocalisedString(organisationSchemeDto.getName()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getLifeCycle().getProcStatus()));
        record.setType(CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        record.setVersionLogic(organisationSchemeDto.getVersionLogic());
        record.setUrn(organisationSchemeDto.getUrn());
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(organisationSchemeDto.getMaintainer()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(organisationSchemeDto.getLifeCycle().getInternalPublicationDate()));
        record.setInternalPublicationUser(organisationSchemeDto.getLifeCycle().getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(organisationSchemeDto.getLifeCycle().getExternalPublicationDate()));
        record.setExternalPublicationUser(organisationSchemeDto.getLifeCycle().getExternalPublicationUser());
        record.setOrganisationSchemeDto(organisationSchemeDto);
        return record;
    }

    public static OrganisationSchemeRecord getOrganisationSchemeRecord(OrganisationSchemeMetamacBasicDto organisationSchemeDto) {
        OrganisationSchemeRecord record = new OrganisationSchemeRecord();
        record.setCode(organisationSchemeDto.getCode());
        record.setName(getLocalisedString(organisationSchemeDto.getName()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(organisationSchemeDto.getProcStatus()));
        record.setType(CommonUtils.getOrganisationSchemeTypeName(organisationSchemeDto.getType()));
        record.setVersionLogic(organisationSchemeDto.getVersionLogic());
        record.setUrn(organisationSchemeDto.getUrn());
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(organisationSchemeDto.getMaintainer()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(organisationSchemeDto.getInternalPublicationDate()));
        record.setInternalPublicationUser(organisationSchemeDto.getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(organisationSchemeDto.getExternalPublicationDate()));
        record.setExternalPublicationUser(organisationSchemeDto.getExternalPublicationUser());
        record.setOrganisationSchemeBasicDto(organisationSchemeDto);
        return record;
    }

    // ORGANISATIONS

    public static OrganisationRecord getOrganisationRecord(OrganisationMetamacDto organisationDto) {
        OrganisationRecord record = new OrganisationRecord(organisationDto.getId(), organisationDto.getCode(), getLocalisedString(organisationDto.getName()), organisationDto.getUrn(),
                organisationDto.getItemSchemeVersionUrn(), organisationDto.getType());
        return record;
    }

    public static OrganisationRecord getOrganisationRecord(OrganisationMetamacBasicDto organisationDto) {
        OrganisationRecord record = new OrganisationRecord();
        record.setCode(organisationDto.getCode());
        record.setName(getLocalisedString(organisationDto.getName()));
        record.setUrn(organisationDto.getUrn());
        record.setOrganisationSchemeUrn(organisationDto.getItemSchemeVersionUrn());
        record.setOrganisationType(organisationDto.getType());
        return record;
    }

    public static OrganisationRecord getOrganisationRecord(ItemHierarchyDto organisation, OrganisationTypeEnum organisationTypeEnum) {
        OrganisationRecord record = new OrganisationRecord(organisation.getItem().getId(), organisation.getItem().getCode(), getLocalisedString(organisation.getItem().getName()), organisation
                .getItem().getUrn(), organisation.getItem().getItemSchemeVersionUrn(), organisationTypeEnum);
        return record;
    }

    // CONTACTS

    public static ContactRecord getContactRecord(ContactDto contactDto) {
        ContactRecord record = new ContactRecord(contactDto.getId(), InternationalStringUtils.getLocalisedString(contactDto.getName()), InternationalStringUtils.getLocalisedString(contactDto
                .getOrganisationUnit()), contactDto);
        return record;
    }
}

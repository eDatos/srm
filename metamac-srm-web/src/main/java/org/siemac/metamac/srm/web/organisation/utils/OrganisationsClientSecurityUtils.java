package org.siemac.metamac.srm.web.organisation.utils;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.security.shared.SharedOrganisationsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsClientSecurityUtils {

    // ORGANISATION SCHEMES

    public static boolean canCreateOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canCreateOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canUpdateOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteOrganisationScheme(ProcStatusEnum procStatus) {
        if (CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            return false;
        }
        return SharedOrganisationsSecurityUtils.canDeleteOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendOrganisationSchemeToProductionValidation() {
        return SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendOrganisationSchemeToDiffusionValidation() {
        return SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectOrganisationSchemeValidation(ProcStatusEnum procStatus) {
        return SharedOrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishOrganisationSchemeInternally() {
        return SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishOrganisationSchemeExternally() {
        return SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningOrganisationScheme(String urn, RelatedResourceDto maintainer, String versionLogic, OrganisationSchemeTypeEnum organisationSchemeType) {

        // Resources from other maintainers can not be version
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.hasDefaultMaintainerOrIsAgencySchemeSdmxResource(urn, maintainer)) {

            // Agency schemes, data consumer schemes and data provider schemes can not be version

            if (!org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isDataConsumerScheme(organisationSchemeType)
                    && !org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isDataProviderScheme(organisationSchemeType)
                    & !org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isAgencyScheme(organisationSchemeType)) {

                if (!VersionUtil.isTemporalVersion(versionLogic)) {
                    // The scheme can only be version when the temporal version has been previously created
                    return false;
                }

                return SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(MetamacSrmWeb.getCurrentUser());
            }
        }

        return false;
    }

    public static boolean canCreateOrganisationSchemeTemporalVersion() {
        return SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canAnnounceOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelOrganisationSchemeValidity(OrganisationSchemeMetamacBasicDto organisationSchemeMetamacBasicDto) {
        return canCancelOrganisationSchemeValidity(organisationSchemeMetamacBasicDto.getUrn(), organisationSchemeMetamacBasicDto.getMaintainer(), organisationSchemeMetamacBasicDto.getVersionLogic(),
                organisationSchemeMetamacBasicDto.getType(), organisationSchemeMetamacBasicDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacBasicDto.getValidTo());
    }

    public static boolean canCancelOrganisationSchemeValidity(String urn, RelatedResourceDto maintainer, String versionLogic, OrganisationSchemeTypeEnum organisationSchemeType,
            ProcStatusEnum procStatus, Date validTo) {

        // validity was already ended
        if (validTo != null) {
            return false;
        }

        // only externally published resources can be canceled
        if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return false;
        }

        // AGENCY SCHEMES, DATA CONSUMER SCHEMES and DATA PROVIDER SCHEMES can not be canceled

        if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isDataConsumerScheme(organisationSchemeType)
                || org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isDataProviderScheme(organisationSchemeType)
                || org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isAgencyScheme(organisationSchemeType)) {
            return false;
        }

        // ORGANISATION UNIT SCHEMES

        return SharedOrganisationsSecurityUtils.canEndOrganisationSchemeValidity(MetamacSrmWeb.getCurrentUser()) && CommonUtils.canSdmxMetadataAndStructureBeModified(urn, maintainer, versionLogic);
    }

    public static boolean canCreateCategorisationFromOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteCategorisationFromOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type, CategorisationDto categorisationDto) {

        if (BooleanUtils.isTrue(categorisationDto.getFinalLogic())) {

            // if it is final, can NEVER be deleted
            return false;

        } else {

            if (CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {

                return SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);

            } else {

                // if it does not have the default maintainer, can NEVER be deleted
                return false;
            }
        }
    }

    public static boolean canCancelCategorisationValidityFromOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type, CategorisationDto categorisationDto) {

        if (categorisationDto.getValidTo() != null) { // The validity has been canceled previously
            return false;
        }

        // Only categorisations of default maintainer can be canceled

        if (CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {
            return SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
        } else {
            return false;
        }
    }

    public static boolean canExportCategorisationFromOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type, CategorisationDto categorisationDto) {
        return true;
    }

    public static boolean canCopyOrganisationScheme(RelatedResourceDto maintainer) {
        // Only resources from other organisations can be copied
        return SharedOrganisationsSecurityUtils.canCopyOrganisationScheme(MetamacSrmWeb.getCurrentUser()) && !CommonUtils.isDefaultMaintainer(maintainer);
    }

    public static boolean canCopyOrganisationSchemeKeepingMaintainer(RelatedResourceDto maintainer) {
        // Only resources from default organisations can be copied keeping maintainer
        return SharedOrganisationsSecurityUtils.canCopyOrganisationScheme(MetamacSrmWeb.getCurrentUser()) && CommonUtils.isDefaultMaintainer(maintainer);
    }

    // ORGANISATIONS

    public static boolean canCreateOrganisation(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        if (!org.siemac.metamac.srm.web.client.utils.CommonUtils.hasDefaultMaintainerOrIsAgencySchemeSdmxResource(organisationSchemeMetamacDto)) {
            return false;

        } else if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(organisationSchemeMetamacDto.getUrn())
                && OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeMetamacDto.getType())) {
            return false;

        } else if (!org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(organisationSchemeMetamacDto.getUrn())
                && CommonUtils.isMaintainableArtefactPublished(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())) {
            return false;

        } else {
            return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), organisationSchemeMetamacDto.getLifeCycle().getProcStatus(),
                    organisationSchemeMetamacDto.getType());
        }
    }

    public static boolean canUpdateOrganisation(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteOrganisationUnit(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        return canDeleteOrganisation(organisationSchemeMetamacDto, null);
    }

    public static boolean canDeleteOrganisation(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, Boolean hasOrganisationBeenPublished) {
        if (!org.siemac.metamac.srm.web.client.utils.CommonUtils.hasDefaultMaintainerOrIsAgencySchemeSdmxResource(organisationSchemeMetamacDto)) {
            return false;
        }

        if (CommonUtils.isMaintainableArtefactPublished(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())) {
            return false;
        }

        if (org.siemac.metamac.core.common.util.shared.UrnUtils.isTemporalUrn(organisationSchemeMetamacDto.getUrn())) {
            if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isOrganisationUnitScheme(organisationSchemeMetamacDto)) {
                // ORGANISATION UNIT
                return false;
            } else {
                // AGENCY, DATA PROVIDER and DATA CONSUMER
                if (BooleanUtils.isTrue(hasOrganisationBeenPublished)) {
                    // if organisation has been published, DO not delete
                    return false;
                }
            }
        }

        // Maintainer is checked because the structure of an imported resource can not be modified
        return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), organisationSchemeMetamacDto.getLifeCycle().getProcStatus(),
                organisationSchemeMetamacDto.getType());
    }

    // CONTACTS

    public static boolean canCreateContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isOrganisationUnitScheme(organisationSchemeMetamacDto)) {

            // ORGANISATION UNIT

            return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType())
                    && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeMetamacDto);
        } else {

            // AGENCY, DATA PROVIDER and DATA CONSUMER

            return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType());
        }
    }

    public static boolean canUpdateContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {

        if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isOrganisationUnitScheme(organisationSchemeMetamacDto)) {

            // ORGANISATION UNIT

            return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType());

        } else {

            // AGENCY, DATA PROVIDER and DATA CONSUMER

            return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType());
        }
    }

    public static boolean canDeleteContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto, ContactDto contactDto) {

        if (CommonUtils.isMaintainableArtefactPublished(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())) {
            return false;
        }

        if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isOrganisationUnitScheme(organisationSchemeMetamacDto)) {

            // ORGANISATION UNIT

            return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType())
                    && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeMetamacDto);
        } else {

            // AGENCY, DATA PROVIDER and DATA CONSUMER

            if (BooleanUtils.isTrue(contactDto.getIsImported())) {
                return false;
            }

            return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType());
        }
    }
}

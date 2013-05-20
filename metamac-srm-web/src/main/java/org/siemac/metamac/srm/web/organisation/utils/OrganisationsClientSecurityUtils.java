package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.security.shared.SharedOrganisationsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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

    public static boolean canVersioningOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCreateOrganisationSchemeTemporalVersion() {
        return canVersioningOrganisationScheme();
    }

    public static boolean canAnnounceOrganisationScheme() {
        return SharedOrganisationsSecurityUtils.canAnnounceOrganisationScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelOrganisationSchemeValidity() {
        return SharedOrganisationsSecurityUtils.canEndOrganisationSchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCreateCategorisationFromOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteCategorisationFromOrganisationScheme(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type, CategorisationDto categorisationDto) {
        // Maintainer is checked because the creation/deletion of a categorisation is not allowed when the resource is imported (i am not the maintainer)
        return SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type)
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(categorisationDto);
    }

    public static boolean canCopyOrganisationScheme(RelatedResourceDto maintainer) {
        // Only resources from other organisations can be copied
        return SharedOrganisationsSecurityUtils.canCopyOrganisationScheme(MetamacSrmWeb.getCurrentUser()) && !CommonUtils.isDefaultMaintainer(maintainer);
    }

    // ORGANISATIONS

    public static boolean canCreateOrganisation(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeMetamacDto.getType())) {
            // Agencies can be added when the AgencyScheme is a temporal version
            return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), organisationSchemeMetamacDto.getLifeCycle().getProcStatus(),
                    organisationSchemeMetamacDto.getType()) && org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeMetamacDto.getMaintainer());
        } else {
            return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), organisationSchemeMetamacDto.getLifeCycle().getProcStatus(),
                    organisationSchemeMetamacDto.getType()) && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeMetamacDto);
        }
    }

    public static boolean canUpdateOrganisation(ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) {
        return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type);
    }

    public static boolean canDeleteOrganisation(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        // Agencies can never be deleted!
        if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isAgencyScheme(organisationSchemeMetamacDto.getType())) {
            return false;
        }
        if (CommonUtils.isMaintainableArtefactPublished(organisationSchemeMetamacDto.getLifeCycle().getProcStatus())) {
            return false;
        }
        // Maintainer is checked because the structure of an imported resource can not be modified
        return SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(MetamacSrmWeb.getCurrentUser(), organisationSchemeMetamacDto.getLifeCycle().getProcStatus(),
                organisationSchemeMetamacDto.getType()) && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeMetamacDto);
    }

    // CONTACTS

    public static boolean canCreateContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType())
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeMetamacDto);
    }

    public static boolean canUpdateContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType());
    }

    public static boolean canDeleteContact(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        return OrganisationsClientSecurityUtils.canUpdateOrganisation(organisationSchemeMetamacDto.getLifeCycle().getProcStatus(), organisationSchemeMetamacDto.getType())
                && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeMetamacDto);
    }
}

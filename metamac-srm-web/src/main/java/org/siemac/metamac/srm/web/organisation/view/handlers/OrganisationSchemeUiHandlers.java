package org.siemac.metamac.srm.web.organisation.view.handlers;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public interface OrganisationSchemeUiHandlers extends BaseOrganisationUiHandlers {

    void retrieveOrganisationScheme(String identifier, OrganisationSchemeTypeEnum type);
    void retrieveOrganisationSchemeVersions(String organisationSchemeUrn);
    void saveOrganisationScheme(OrganisationSchemeMetamacDto organisationScheme);
    void deleteOrganisationScheme(String urn);
    void cancelValidity(String urn);
    void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type);
    void deleteOrganisations(List<String> urns);
    void retrieveLatestOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto);
    void exportOrganisationScheme(String urn, ExportDetailEnum infoAmount, ExportReferencesEnum references);
    void copyOrganisationScheme(String urn);
    void copyOrganisationScheme(String urn, String code);

    // Organisations

    void exportOrganisations(String conceptSchemeUrn);

    // Stream messages

    void reSendStreamMessageOrganisationScheme(String urn);

    // Life cycle

    void sendToProductionValidation(String urn, OrganisationSchemeTypeEnum organisationSchemeTypeEnum, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, OrganisationSchemeTypeEnum organisationSchemeTypeEnum, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, OrganisationSchemeTypeEnum organisationSchemeTypeEnum, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, OrganisationSchemeTypeEnum organisationSchemeTypeEnum, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal);
    void publishExternally(String urn, OrganisationSchemeTypeEnum organisationSchemeTypeEnum, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);
    void createTemporalVersion(String urn);

    // Importation

    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String fileName);

    void showWaitPopup();
}

package org.siemac.metamac.srm.web.organisation.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganisationSchemeUiHandlers extends UiHandlers {

    void retrieveOrganisationScheme(String identifier, OrganisationSchemeTypeEnum type);
    void retrieveOrganisationSchemeVersions(String organisationSchemeUrn);
    void saveOrganisationScheme(OrganisationSchemeMetamacDto organisationScheme);
    void cancelValidity(String urn);
    void goToOrganisationScheme(String urn, OrganisationSchemeTypeEnum type);
    void goToOrganisation(String urn);
    void retrieveOrganisationListByScheme(String organisationSchemeUrn);
    void createOrganisation(OrganisationMetamacDto organisationDto);
    void deleteOrganisations(List<String> urns);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);

}

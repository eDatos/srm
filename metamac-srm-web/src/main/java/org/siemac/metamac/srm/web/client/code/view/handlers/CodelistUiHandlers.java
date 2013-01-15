package org.siemac.metamac.srm.web.client.code.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public interface CodelistUiHandlers extends BaseCodeUiHandlers {

    void retrieveCodelist(String identifier);
    void retrieveCodelistVersions(String codelistUrn);
    void saveCodelist(CodelistMetamacDto codelist);
    void cancelValidity(String urn);
    void goToCodelist(String urn);
    void retrieveCodesByCodelist(String codelistUrn);

    void retrieveFamilies(int firstResult, int maxResults, String criteria);
    void retrieveCodelistsThatCanBeReplaced(int firstResult, int maxResults, String criteria);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType, boolean versionCodes);
}

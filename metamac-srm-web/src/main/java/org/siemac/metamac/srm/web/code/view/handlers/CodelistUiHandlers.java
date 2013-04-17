package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public interface CodelistUiHandlers extends BaseCodeUiHandlers {

    void retrieveCodelistVersions(String codelistUrn);
    void saveCodelist(CodelistMetamacDto codelist);
    void cancelValidity(String urn);
    void goToCodelist(String urn);
    void addCodelistToFamily(String codelistUrn, String familyUrn);

    void retrieveFamilies(int firstResult, int maxResults, String criteria);
    void retrieveCodelistsThatCanBeReplaced(int firstResult, int maxResults, String criteria);
    void retrieveVariables(int firstResult, int maxResults, String criteria);

    // Codes

    void retrieveCodes();

    void retrieveCodesWithOrder(String orderIdentifier);
    void retrieveCodelistOrders(String codelistUrn);
    void saveCodelistOrder(CodelistVisualisationDto codelistOrderVisualisationDto);
    void deleteCodelistOrders(List<String> orderIdentifiers);

    void retrieveCodesWithOpennessLevel(String opennessLevelUrn);
    void retrieveCodelistOpennessLevels(String codelistUrn);
    void saveCodelistOpennessLevel(CodelistVisualisationDto codelistOpennessVisualisationDto);
    void deleteCodelistOpennessLevel(List<String> opennessLevelsUrns);
    void updateCodesOpennessLevel(String opennessLevelUrn, Map<String, Boolean> opennessLevels);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType, boolean versionCodes);
    void createTemporalVersion(String urn);
}

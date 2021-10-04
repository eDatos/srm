package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;

public interface CodelistUiHandlers extends BaseCodeUiHandlers {

    void retrieveCodelistVersions(String codelistUrn);
    void saveCodelist(CodelistMetamacDto codelist);
    void deleteCodelist(String urn);
    void cancelValidity(String urn);
    void addCodelistToFamily(String codelistUrn, String familyUrn);
    void retrieveLatestCodelist(CodelistMetamacDto codelistMetamacDto);
    void exportCodelist(String urn, ExportDetailEnum infoAmount, ExportReferencesEnum references);
    void copyCodelist(String urn, String code);
    void copyCodelist(String urn);

    void retrieveFamilies(int firstResult, int maxResults, String criteria);
    void retrieveVariables(int firstResult, int maxResults, VariableWebCriteria criteria);

    void retrieveCodelistsThatCanBeReplaced(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria);

    // Codes

    void retrieveCodes();
    void exportCodes(String codelistUrn);

    void normaliseVariableElementsToCodes(String codelistUrn, String locale, boolean onlyNormaliseCodesWithoutVariableElement);
    void updateCodesVariableElements(String codelistUrn, Map<Long, Long> variableElementsIdByCodeId);
    void retrieveVariableElementsForManualNormalisation(int firstResult, int maxResults, String criteria, String codelistUrn);

    void retrieveCodesWithOrder(String orderIdentifier);
    void retrieveCodelistOrders(String codelistUrn);
    void saveCodelistOrder(CodelistVisualisationDto codelistOrderVisualisationDto);
    void deleteCodelistOrders(List<String> orderIdentifiers);
    void exportCodesOrder(String codelistUrn);

    void retrieveCodesWithOpennessLevel(String opennessLevelUrn);
    void retrieveCodelistOpennessLevels(String codelistUrn);
    void saveCodelistOpennessLevel(CodelistVisualisationDto codelistOpennessVisualisationDto);
    void deleteCodelistOpennessLevel(List<String> opennessLevelsUrns);
    void updateCodesOpennessLevel(String opennessLevelUrn, Map<String, Boolean> opennessLevels);

    // Stream messages

    void reSendStreamMessageCodelist(CodelistMetamacDto codelistDto);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType, boolean versionCodes);
    void createTemporalVersion(String urn);

    // Importation

    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String fileName);

    void showWaitPopup();
}

package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;

public interface ConceptSchemeUiHandlers extends BaseConceptUiHandlers {

    // Schemes

    void retrieveConceptSchemeVersions(String conceptSchemeUrn);
    void saveConceptScheme(ConceptSchemeMetamacDto conceptScheme);
    void deleteConceptScheme(String urn);
    void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria);
    void cancelValidity(String urn);
    void retrieveLatestConceptScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void exportConceptScheme(String urn, ExportDetailEnum infoAmount, ExportReferencesEnum references);
    void copyConceptScheme(String urn);
    void copyConceptScheme(String urn, String code);

    // Life cycle

    void sendToProductionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void sendToDiffusionValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void rejectValidation(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void publishInternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto, Boolean forceLatestFinal);
    void publishExternally(ConceptSchemeMetamacDto conceptSchemeMetamacDto);
    void versioning(String urn, VersionTypeEnum versionType);
    void createTemporalVersion(String urn);
}

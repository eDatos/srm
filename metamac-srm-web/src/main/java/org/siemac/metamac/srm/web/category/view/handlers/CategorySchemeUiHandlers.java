package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;

public interface CategorySchemeUiHandlers extends BaseCategoryUiHandlers {

    void retrieveCategorySchemeVersions(String categorySchemeUrn);
    void saveCategoryScheme(CategorySchemeMetamacDto categoryScheme);
    void deleteCategoryScheme(String urn);
    void cancelValidity(String urn);
    void retrieveLatestCategoryScheme(CategorySchemeMetamacDto conceptSchemeMetamacDto);
    void exportCategoryScheme(String urn, ExportDetailEnum infoAmount, ExportReferencesEnum references);
    void copyCategoryScheme(String urn);
    void copyCategoryScheme(String urn, String code);

    // Categories
    void exportCategories(String categorySchemeUrn);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);
    void createTemporalVersion(String urn);

    // Importation

    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String fileName);

    void showWaitPopup();
}

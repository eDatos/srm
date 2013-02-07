package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public interface CategorySchemeUiHandlers extends BaseCategoryUiHandlers {

    void retrieveCategoryScheme(String identifier);
    void retrieveCategorySchemeVersions(String categorySchemeUrn);
    void saveCategoryScheme(CategorySchemeMetamacDto categoryScheme);
    void cancelValidity(String urn);
    void goToCategoryScheme(String urn);
    void retrieveCategoryListByScheme(String categorySchemeUrn);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);

}

package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;

public class CategorySchemeMainFormLayout extends LifeCycleMainFormLayout {

    public CategorySchemeMainFormLayout() {
    }

    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        super.updatePublishSection(categorySchemeMetamacDto.getLifeCycle().getProcStatus(), categorySchemeMetamacDto);
        setCanEdit();
        setCanDelete();
    }

    private void setCanEdit() {
        super.setCanEdit(CategoriesClientSecurityUtils.canUpdateCategoryScheme(procStatus));
    }

    private void setCanDelete() {
        boolean canDelete = false;
        canDelete = CategoriesClientSecurityUtils.canDeleteCategoryScheme(procStatus);
        super.setCanDelete(canDelete);
    }

    @Override
    protected void showSendToProductionValidation() {
        if (CategoriesClientSecurityUtils.canSendCategorySchemeToProductionValidation()) {
            productionValidation.show();
        }
    }

    @Override
    protected void showSendToDiffusionValidation() {
        if (CategoriesClientSecurityUtils.canSendCategorySchemeToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    @Override
    protected void showRejectValidationButton() {
        if (CategoriesClientSecurityUtils.canRejectCategorySchemeValidation(procStatus)) {
            rejectValidation.show();
        }
    }

    @Override
    protected void showPublishInternallyButton() {
        if (CategoriesClientSecurityUtils.canPublishCategorySchemeInternally()) {
            publishInternally.show();
        }
    }

    @Override
    protected void showPublishExternallyButton() {
        if (CategoriesClientSecurityUtils.canPublishCategorySchemeExternally()) {
            publishExternally.show();
        }
    }

    @Override
    protected void showCreateTemporalVersionButton() {
        if (CategoriesClientSecurityUtils.canCreateCategorySchemeTemporalVersion()) {
            createTemporalVersion.show();
        }
    }
    @Override
    protected void showCancelValidityButton() {
        if (CategoriesClientSecurityUtils.canCancelCategorySchemeValidity(urn, maintainer, versionLogic, procStatus, validTo)) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showConsolidateVersionButton() {
        if (CategoriesClientSecurityUtils.canVersioningCategoryScheme(maintainer, versionLogic)) {
            consolidateVersion.show();
        }
    }

    @Override
    protected void showExportButton() {
        if (CategoriesClientSecurityUtils.canExportCategoryScheme(versionLogic)) {
            export.show();
        }
    }

    @Override
    protected void showCopyButton() {
        if (CategoriesClientSecurityUtils.canCopyCategoryScheme(maintainer)) {
            copy.show();
        }
    }

    @Override
    protected void showCopyKeepingMaintainerButton() {
        if (CategoriesClientSecurityUtils.canCopyCategorySchemeKeepingMaintainer(maintainer)) {
            copyKeepingMaintainer.show();
        }
    }

    @Override
    protected void showLifeCycleReSendStreamMessage() {
        // TODO EDATOS-3433
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (CategoriesClientSecurityUtils.canAnnounceCategoryScheme()) {
    // announce.show();
    // }
    // }
}

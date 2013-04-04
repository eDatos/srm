package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;

public class CategorySchemeMainFormLayout extends LifeCycleMainFormLayout {

    public CategorySchemeMainFormLayout() {
    }

    public CategorySchemeMainFormLayout(boolean canEdit) {
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
        if (CategoriesClientSecurityUtils.canRejectCategorySchemeValidation(status)) {
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
    protected void showVersioningButton() {
        if (CategoriesClientSecurityUtils.canVersioningCategoryScheme()) {
            versioning.show();
        }
    }

    @Override
    protected void showCancelValidityButton() {
        if (CategoriesClientSecurityUtils.canCancelCategorySchemeValidity()) {
            cancelValidity.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (CategoriesClientSecurityUtils.canAnnounceCategoryScheme()) {
    // announce.show();
    // }
    // }
}

package org.siemac.metamac.srm.web.client.category.widgets;

import org.siemac.metamac.srm.web.client.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;

import com.smartgwt.client.types.Visibility;

public class CategorySchemeMainFormLayout extends LifeCycleMainFormLayout {

    public CategorySchemeMainFormLayout() {
        common();
    }

    public CategorySchemeMainFormLayout(boolean canEdit) {
        common();
    }

    private void common() {
        announce.setVisibility(CategoriesClientSecurityUtils.canAnnounceCategoryScheme() ? Visibility.VISIBLE : Visibility.HIDDEN);
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

}

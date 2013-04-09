package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;

public class CategorySchemeMainFormLayout extends LifeCycleMainFormLayout {

    public CategorySchemeMainFormLayout() {
    }

    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        super.updatePublishSection(categorySchemeMetamacDto.getLifeCycle().getProcStatus(), categorySchemeMetamacDto);
        setCanEdit();
    }

    private void setCanEdit() {
        boolean canEdit = false;
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(status)) {
            canEdit = CategoriesClientSecurityUtils.canCreateCategorySchemeTemporalVersion();
        } else {
            canEdit = CategoriesClientSecurityUtils.canUpdateCategoryScheme(status);
        }
        super.setCanEdit(canEdit);
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
        if (canCategorySchemeBeVersion()) {
            versioning.show();
        }
    }
    @Override
    protected void showCancelValidityButton() {
        if (CategoriesClientSecurityUtils.canCancelCategorySchemeValidity()) {
            cancelValidity.show();
        }
    }

    @Override
    protected void showVersionSdmxResourceButton() {
        if (canCategorySchemeBeVersion() && VersionUtil.isTemporalVersion(versionLogic)) {
            versionSdmxResource.show();
        }
    }

    // @Override
    // protected void showAnnounceButton() {
    // if (CategoriesClientSecurityUtils.canAnnounceCategoryScheme()) {
    // announce.show();
    // }
    // }

    private boolean canCategorySchemeBeVersion() {
        // Resources from other maintainers can not be version
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(maintainer) && CategoriesClientSecurityUtils.canVersioningCategoryScheme();
    }
}

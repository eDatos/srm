package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.LifeCycleMainFormLayout;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CategorySchemeMainFormLayout extends LifeCycleMainFormLayout {

    private RelatedResourceDto maintainer;

    public CategorySchemeMainFormLayout() {
    }

    public CategorySchemeMainFormLayout(boolean canEdit) {
    }

    public void updatePublishSection(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        super.updatePublishSection(categorySchemeMetamacDto.getLifeCycle().getProcStatus(), categorySchemeMetamacDto.getValidTo());
        this.maintainer = categorySchemeMetamacDto.getMaintainer();
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
        if (canCategorySchemeBeVersion()) { // TODO Check temporal version
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

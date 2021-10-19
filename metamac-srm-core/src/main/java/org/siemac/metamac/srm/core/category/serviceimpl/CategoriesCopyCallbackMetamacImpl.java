package org.siemac.metamac.srm.core.category.serviceimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.CategoriesCopyCallbackImpl;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Override callback copying Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class CategoriesCopyCallbackMetamacImpl extends CategoriesCopyCallbackImpl {

    @Autowired
    private CategoryMetamacRepository categoryMetamacRepository;

    @Override
    public ItemSchemeVersion createItemSchemeVersion() {
        return new CategorySchemeVersionMetamac();
    }

    @Override
    public void copyItemSchemeVersion(ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) {
        super.copyItemSchemeVersion(sourceItemSchemeVersion, targetItemSchemeVersion);
        CategorySchemeVersionMetamac target = (CategorySchemeVersionMetamac) targetItemSchemeVersion;
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        target.setStreamMessageStatus(StreamMessageStatusEnum.PENDING);
    }

    @Override
    public Item createItem() {
        return new CategoryMetamac();
    }

    @Override
    public void copyItem(Item sourceItem, ItemResult sourceItemResult, Item targetItem) {
        super.copyItem(sourceItem, sourceItemResult, targetItem);
        // nothing more

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) throws MetamacException {
        return categoryMetamacRepository.findCategoriesByCategorySchemeUnordered(itemSchemeId, SrmServiceUtils.getItemResultSelection(getCopyOperationType()));
    }
}
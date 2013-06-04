package org.siemac.metamac.srm.core.organisation.serviceimpl;

import java.util.List;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.OrganisationsCopyCallbackImpl;

/**
 * Copy Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class OrganisationsCopyCallbackMetamacImpl extends OrganisationsCopyCallbackImpl {

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Override
    public ItemSchemeVersion createItemSchemeVersion() {
        return new OrganisationSchemeVersionMetamac();
    }

    @Override
    public void copyItemSchemeVersion(ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) {
        super.copyItemSchemeVersion(sourceItemSchemeVersion, targetItemSchemeVersion);
        OrganisationSchemeVersionMetamac target = (OrganisationSchemeVersionMetamac) targetItemSchemeVersion;
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public Item createItem() {
        return new OrganisationMetamac();
    }

    @Override
    public void copyItem(Item sourceItem, ItemResult sourceItemResult, Item targetItem) {
        super.copyItem(sourceItem, sourceItemResult, targetItem);

        OrganisationMetamac source = (OrganisationMetamac) sourceItem;
        OrganisationMetamac target = (OrganisationMetamac) targetItem;

        switch (getCopyOperationType()) {
            case VERSIONING_DUMMY:
                target.setSpecialOrganisationHasBeenPublished(source.getSpecialOrganisationHasBeenPublished());
                break;
            case VERSIONING:
                target.setSpecialOrganisationHasBeenPublished(null); // do not copy. OrganisationUnit is unique type can be versioned, and they can be deleted always in new versions
                break;
            case COPY_TO_NEW_ARTEFACT:
                target.setSpecialOrganisationHasBeenPublished(null); // do not copy. it will be true when scheme is published
                break;
        }

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) {
        return organisationMetamacRepository.findOrganisationsByOrganisationSchemeUnordered(itemSchemeId, SrmServiceUtils.getItemResultSelection(getCopyOperationType()));
    }
}
package org.siemac.metamac.srm.core.organisation.serviceimpl.utils;

import java.util.List;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsVersioningCopyUtils.OrganisationVersioningCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("organisationVersioningCopyCallbackMetamac")
public class OrganisationVersioningCopyCallbackMetamacImpl implements OrganisationVersioningCopyCallback {

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Override
    public OrganisationSchemeVersion createOrganisationSchemeVersion() {
        return new OrganisationSchemeVersionMetamac();
    }

    @Override
    public void copyOrganisationSchemeVersion(OrganisationSchemeVersion source, OrganisationSchemeVersion targetSdmx) {
        OrganisationSchemeVersionMetamac target = (OrganisationSchemeVersionMetamac) targetSdmx;
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public Organisation createOrganisation() {
        return new OrganisationMetamac();
    }

    @Override
    public void copyOrganisation(Organisation source, ItemResult sourceItemResult, Organisation target) {
        // nothing more
        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult
    }

    @Override
    public Boolean isOverridedFindItemsEfficiently() {
        return Boolean.TRUE;
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) {
        return organisationMetamacRepository.findOrganisationsByOrganisationSchemeUnordered(itemSchemeId, ItemMetamacResultSelection.VERSIONING);
    }
}
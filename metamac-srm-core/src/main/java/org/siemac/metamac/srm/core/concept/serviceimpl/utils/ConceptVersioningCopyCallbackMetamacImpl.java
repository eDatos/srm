package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsVersioningCopyUtils.ConceptVersioningCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("conceptVersioningCopyCallbackMetamac")
public class ConceptVersioningCopyCallbackMetamacImpl implements ConceptVersioningCopyCallback {

    @Autowired
    private ConceptsMetamacService   conceptsMetamacService;

    @Autowired
    private ConceptMetamacRepository conceptMetamacRepository;

    @Override
    public ConceptSchemeVersion createConceptSchemeVersion() {
        return new ConceptSchemeVersionMetamac();
    }

    @Override
    public void copyConceptSchemeVersion(ConceptSchemeVersion sourceSdmx, ConceptSchemeVersion targetSdmx) {
        ConceptSchemeVersionMetamac source = (ConceptSchemeVersionMetamac) sourceSdmx;
        ConceptSchemeVersionMetamac target = (ConceptSchemeVersionMetamac) targetSdmx;
        target.setType(source.getType());
        target.setRelatedOperation(BaseVersioningCopyUtils.copy(source.getRelatedOperation()));
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public void createConceptSchemeRelations(ServiceContext ctx, ConceptSchemeVersion sourceSdmx, ConceptSchemeVersion targetSdmx) throws MetamacException {
        ConceptSchemeVersionMetamac source = (ConceptSchemeVersionMetamac) sourceSdmx;
        ConceptSchemeVersionMetamac target = (ConceptSchemeVersionMetamac) targetSdmx;
        conceptsMetamacService.versioningRelatedConcepts(ctx, source, target);
    }

    @Override
    public Boolean isOverridedFindItemsEfficiently() {
        return Boolean.TRUE;
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) {
        return conceptMetamacRepository.findConceptsByConceptSchemeUnordered(itemSchemeId, ItemMetamacResultSelection.VERSIONING);
    }

    @Override
    public Concept createConcept() {
        return new ConceptMetamac();
    }

    @Override
    public void copyConcept(Concept sourceSdmx, ItemResult itemResultSource, Concept targetSdmx) {

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult

        ConceptMetamac source = (ConceptMetamac) sourceSdmx;
        ConceptMetamac target = (ConceptMetamac) targetSdmx;

        if (itemResultSource != null) {
            // Copy efficiently
            ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResultSource.getExtensionPoint();
            target.setPluralName(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getPluralName()));
            target.setAcronym(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getAcronym()));
            target.setDescriptionSource(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getDescriptionSource()));
            target.setContext(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getContext()));
            target.setDocMethod(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getDocMethod()));
            target.setDerivation(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getDerivation()));
            target.setLegalActs(BaseCopyAllMetadataUtils.copyInternationalString(extensionPoint.getLegalActs()));
        } else {
            target.setPluralName(BaseVersioningCopyUtils.copy(source.getPluralName()));
            target.setAcronym(BaseVersioningCopyUtils.copy(source.getAcronym()));
            target.setDescriptionSource(BaseVersioningCopyUtils.copy(source.getDescriptionSource()));
            target.setContext(BaseVersioningCopyUtils.copy(source.getContext()));
            target.setDocMethod(BaseVersioningCopyUtils.copy(source.getDocMethod()));
            target.setDerivation(BaseVersioningCopyUtils.copy(source.getDerivation()));
            target.setLegalActs(BaseVersioningCopyUtils.copy(source.getLegalActs()));
        }

        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setConceptType(source.getConceptType());
        target.setVariable(source.getVariable());

        // can copy "extends" and "roles", because they are concepts in another concept scheme
        target.setConceptExtends(source.getConceptExtends());
        for (ConceptMetamac conceptRole : source.getRoleConcepts()) {
            target.addRoleConcept(conceptRole);
        }
        // Do not copy related concepts in this point, because they belong to same concept scheme, and new concept in new version must relate to versioned related concept.
        // They will be copied in createConceptSchemeRelations
    }
}
package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseDoCopyUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsDoCopyUtils.ConceptCopyCallback;

/**
 * Copy Metamac metadata
 */
@Component("conceptCopyCallbackMetamac")
public class ConceptCopyCallbackMetamacImpl implements ConceptCopyCallback {

    @Override
    public ConceptSchemeVersion createConceptSchemeVersion() {
        return new ConceptSchemeVersionMetamac();
    }

    @Override
    public void copyConceptSchemeVersion(ConceptSchemeVersion sourceSdmx, ConceptSchemeVersion targetSdmx) {
        ConceptSchemeVersionMetamac source = (ConceptSchemeVersionMetamac) sourceSdmx;
        ConceptSchemeVersionMetamac target = (ConceptSchemeVersionMetamac) targetSdmx;
        target.setType(source.getType());
        target.setRelatedOperation(BaseDoCopyUtils.copy(source.getRelatedOperation()));
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public Concept createConcept() {
        return new ConceptMetamac();
    }

    @Override
    public void copyConcept(Concept sourceSdmx, Concept targetSdmx) {
        ConceptMetamac source = (ConceptMetamac) sourceSdmx;
        ConceptMetamac target = (ConceptMetamac) targetSdmx;
        target.setPluralName(BaseDoCopyUtils.copy(source.getPluralName()));
        target.setAcronym(BaseDoCopyUtils.copy(source.getAcronym()));
        target.setDescriptionSource(BaseDoCopyUtils.copy(source.getDescriptionSource()));
        target.setContext(BaseDoCopyUtils.copy(source.getContext()));
        target.setDocMethod(BaseDoCopyUtils.copy(source.getDocMethod()));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setType(source.getType());
        target.setDerivation(BaseDoCopyUtils.copy(source.getDerivation()));
        target.setLegalActs(BaseDoCopyUtils.copy(source.getLegalActs()));
        target.setVariable(source.getVariable());

        // can copy "extends" and "roles", because they are concepts in another concept scheme
        target.setConceptExtends(source.getConceptExtends());
        for (ConceptMetamac conceptRole : source.getRoleConcepts()) {
            target.addRoleConcept(conceptRole);
        }
        // can not copy related concepts, because they belong to same concept scheme, and new concept in new version must relate to versioned related concept
    }
}
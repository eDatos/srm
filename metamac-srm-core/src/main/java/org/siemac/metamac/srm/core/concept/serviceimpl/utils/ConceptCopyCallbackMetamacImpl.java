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
    public ConceptSchemeVersion copyConceptSchemeVersion(ConceptSchemeVersion source) {
        return copyConceptSchemeVersion((ConceptSchemeVersionMetamac) source);
    }

    private ConceptSchemeVersionMetamac copyConceptSchemeVersion(ConceptSchemeVersionMetamac source) {
        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(source.getType());
        target.setRelatedOperation(BaseDoCopyUtils.copy(source.getRelatedOperation()));
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        return target;
    }

    @Override
    public Concept copyConcept(Concept source) {
        return copyConcept((ConceptMetamac) source);
    }

    private ConceptMetamac copyConcept(ConceptMetamac source) {
        ConceptMetamac target = new ConceptMetamac();
        target.setPluralName(BaseDoCopyUtils.copy(source.getPluralName()));
        target.setAcronym(BaseDoCopyUtils.copy(source.getAcronym()));
        target.setDescriptionSource(BaseDoCopyUtils.copy(source.getDescriptionSource()));
        target.setContext(BaseDoCopyUtils.copy(source.getContext()));
        target.setDocMethod(BaseDoCopyUtils.copy(source.getDocMethod()));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setType(source.getType());
        target.setDerivation(BaseDoCopyUtils.copy(source.getDerivation()));
        target.setLegalActs(BaseDoCopyUtils.copy(source.getLegalActs()));
        target.setConceptExtends(source.getConceptExtends()); // can copy reference, because it is a concept in another concept scheme
        return target;
    }
}
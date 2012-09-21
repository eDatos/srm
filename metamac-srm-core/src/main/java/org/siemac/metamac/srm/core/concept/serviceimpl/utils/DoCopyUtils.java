package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import static com.arte.statistic.sdmx.srm.core.common.service.utils.DoCopyUtils.copy;
import static com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.DoCopyUtils.copyConceptSchemeVersion;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;

public class DoCopyUtils {

    /**
     * Create a new ConceptSchemeVersion copying values from a source
     */
    public static ConceptSchemeVersionMetamac copyConceptSchemeVersionMetamac(ConceptSchemeVersionMetamac source) {

        ConceptSchemeVersionMetamac target = new ConceptSchemeVersionMetamac();
        target.setType(source.getType());
        target.setRelatedOperation(copy(source.getRelatedOperation()));

        // Common metadata of ConceptSchemeVersion
        copyConceptSchemeVersion(source, target);

        return target;
    }

    /**
     * Copy children hierarchy from a {@link ConceptSchemeVersionMetamac}
     */
    public static List<ConceptMetamac> copyConceptsMetamac(ConceptSchemeVersionMetamac conceptSchemeVersion) {
        List<Item> sources = conceptSchemeVersion.getItemsFirstLevel();
        List<ConceptMetamac> targets = new ArrayList<ConceptMetamac>();
        for (Item source : sources) {
            ConceptMetamac target = copyConceptMetamacWithChildren((ConceptMetamac) source);
            targets.add(target);
        }
        return targets;
    }

    /**
     * Create a new {@link Concept} copying values from a source with children
     */
    private static ConceptMetamac copyConceptMetamacWithChildren(ConceptMetamac source) {

        ConceptMetamac target = copyConceptMetamac(source);

        // Children
        for (Item childrenSource : source.getChildren()) {
            ConceptMetamac childrenTarget = copyConceptMetamacWithChildren((ConceptMetamac) childrenSource);
            childrenTarget.setParent(target);
            target.addChildren(childrenTarget);
        }

        return target;
    }

    /**
     * Create a new {@link ConceptMetamac} copying values from a source. Do not copy children
     */
    private static ConceptMetamac copyConceptMetamac(ConceptMetamac source) {
        ConceptMetamac target = new ConceptMetamac();

        // Common metadata of Concept
        com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.DoCopyUtils.copyConcept(source, target);

        // Metamac metadata
        target.setPluralName(copy(source.getPluralName()));
        target.setAcronym(copy(source.getAcronym()));
        target.setDescriptionSource(copy(source.getDescriptionSource()));
        target.setContext(copy(source.getContext()));
        target.setDocMethod(copy(source.getDocMethod()));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setType(source.getType());
        target.setDerivation(copy(source.getDerivation()));
        target.setLegalActs(copy(source.getLegalActs()));
        target.setConceptExtends(source.getConceptExtends()); // concept in another concept scheme

        return target;
    }
}
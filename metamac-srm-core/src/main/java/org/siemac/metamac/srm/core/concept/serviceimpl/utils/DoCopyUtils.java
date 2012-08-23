package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

public class DoCopyUtils {

    /**
     * Create a new ConceptSchemeVersion copying values from a source
     */
    public static void copy(ConceptSchemeVersionMetamac source, ConceptSchemeVersionMetamac target) {

        target.setType(source.getType());
        target.setRelatedOperation(com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.DoCopyUtils.copy(source.getRelatedOperation()));
        
        // Metadata of ConceptSchemeVersion
        com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.DoCopyUtils.copy(source, target);
    }
}
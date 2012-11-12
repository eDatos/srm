package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.util.List;

import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptsType;

@org.springframework.stereotype.Component("conceptsDo2JaxbCallbackMetamac")
public class ConceptsDo2JaxbCallbackImpl implements ConceptsDo2JaxbCallback {

    @Override
    public ConceptType createConceptDoToJaxb(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept concept = new org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept();
        return concept;
    }

    @Override
    public ConceptSchemeType createConceptSchemeDoToJaxb(com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion source) {
        org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme conceptScheme = new org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme();
        return conceptScheme;
    }

    @Override
    public ConceptsType createConceptDoToJaxb(List<ConceptSchemeVersion> sourceList) {
        throw new IllegalArgumentException("createConceptDoToJaxb not supported");
    }
}
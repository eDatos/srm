package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptsType;

@org.springframework.stereotype.Component("conceptsDo2JaxbCallbackMetamac")
public class ConceptsDo2JaxbCallbackImpl implements ConceptsDo2JaxbCallback {

    @Autowired
    private ConceptsDo2RestMapperV10 conceptsDo2RestMapperV10;

    @Override
    public ConceptSchemeType createConceptSchemeDoToJaxb(com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion source) {
        org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme target = new org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme();
        conceptsDo2RestMapperV10.toConceptScheme((ConceptSchemeVersionMetamac) source, target);
        return target;
    }

    @Override
    public ConceptType createConceptDoToJaxb(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept target = new org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept();
        conceptsDo2RestMapperV10.toConcept((ConceptMetamac) source, target);
        return target;
    }

    @Override
    public ConceptsType createConceptSchemesDoToJaxb(List<ConceptSchemeVersion> sourceList) {
        throw new IllegalArgumentException("createConceptSchemesDoToJaxb not supported");
    }
}
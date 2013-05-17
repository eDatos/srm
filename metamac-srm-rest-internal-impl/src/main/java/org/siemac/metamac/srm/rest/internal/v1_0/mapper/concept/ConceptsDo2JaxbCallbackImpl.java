package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ConceptSchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ConceptType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ConceptsType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbCallback;

@org.springframework.stereotype.Component("conceptsDo2JaxbRestInternalCallbackMetamac")
public class ConceptsDo2JaxbCallbackImpl implements ConceptsDo2JaxbCallback {

    @Autowired
    private ConceptsDo2RestMapperV10 conceptsDo2RestMapperV10;

    @Override
    public ConceptSchemeType createConceptSchemeJaxb(com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion source) {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme();
    }

    @Override
    public void fillConceptSchemeJaxb(ConceptSchemeVersion source, ConceptSchemeType target) {
        conceptsDo2RestMapperV10.toConceptScheme((ConceptSchemeVersionMetamac) source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme) target);
    }

    @Override
    public ConceptType createConceptJaxb(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        // do not return Metamac type because when ItemScheme is retrieved, the items must be SDMX type
        return new ConceptType();
    }

    @Override
    public void fillConceptJaxb(Concept source, ConceptType target) {
        // do not fill Metamac type because when ItemScheme is retrieved, the items must be SDMX type
        conceptsDo2RestMapperV10.toConcept(source, target);
    }

    @Override
    public ConceptsType createConceptSchemesJaxb(List<ConceptSchemeVersion> sourceList) {
        throw new IllegalArgumentException("createConceptSchemesDoToJaxb not supported");
    }

    @Override
    public List<ItemResult> findConceptsByConceptSchemeEfficiently(ConceptSchemeVersion conceptSchemeVersion) throws MetamacException {
        // TODO findConceptsByConceptSchemeEfficiently
        throw new UnsupportedOperationException();
    }
}
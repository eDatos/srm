package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ConceptSchemeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ConceptType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ConceptsType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbCallback;

@org.springframework.stereotype.Component("conceptsDo2JaxbRestInternalCallbackMetamac")
public class ConceptsDo2JaxbCallbackImpl implements ConceptsDo2JaxbCallback {

    @Autowired
    private ConceptsDo2RestMapperV10 conceptsDo2RestMapperV10;

    @Override
    public ConceptSchemeType createConceptSchemeJaxb() {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme();
    }

    @Override
    public void fillConceptSchemeJaxb(ConceptSchemeVersion source, ConceptSchemeType target) {
        conceptsDo2RestMapperV10.toConceptScheme((ConceptSchemeVersionMetamac) source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme) target);
    }

    @Override
    public boolean mustRetrieveConceptsInsideConceptScheme() {
        return false;
    }

    @Override
    public ConceptType createConceptJaxb() {
        throw new IllegalArgumentException("createConceptJaxb not supported. Do not return items when itemScheme is retrieved");
    }

    @Override
    public void fillConceptJaxb(Concept source, ItemResult sourceItemResult, ItemSchemeVersion itemSchemeVersion, ConceptType target) {
        throw new IllegalArgumentException("fillConceptJaxb not supported. Do not return items when itemScheme is retrieved");
    }

    @Override
    public ConceptsType createConceptSchemesJaxb() {
        throw new IllegalArgumentException("createConceptSchemesDoToJaxb not supported");
    }

    @Override
    public List<ItemResult> findConceptsByConceptSchemeEfficiently(ConceptSchemeVersion conceptSchemeVersion) throws MetamacException {
        throw new IllegalArgumentException("findConceptsByConceptSchemeEfficiently not supported: do not return concepts when retrieve conceptScheme");
    }
}
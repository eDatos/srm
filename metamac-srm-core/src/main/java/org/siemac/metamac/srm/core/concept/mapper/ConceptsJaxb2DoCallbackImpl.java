package org.siemac.metamac.srm.core.concept.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptsType;

@org.springframework.stereotype.Component("conceptsMetamacJaxb2DoCallback")
public class ConceptsJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements ConceptsJaxb2DoCallback {

    @Autowired
    private ConceptsMetamacService conceptsMetamacService;

    /**************************************************************************
     * CREATES
     **************************************************************************/
    @Override
    public Concept createConceptDo(ConceptType source) {
        Concept concept = new ConceptMetamac();

        return concept;
    }

    @Override
    public ConceptSchemeVersion createConceptSchemeDo(ConceptSchemeType source) {
        ConceptSchemeVersion conceptSchemeVersion = new ConceptSchemeVersionMetamac();

        return conceptSchemeVersion;
    }

    @Override
    public List<ConceptSchemeVersion> createConceptSchemesDo(ConceptsType source) {
        List<ConceptSchemeVersion> conceptSchemeVersions = new ArrayList<ConceptSchemeVersion>();

        return conceptSchemeVersions;
    }

    /**************************************************************************
     * EXTENIONS
     **************************************************************************/
    @Override
    public void conceptSchemeJaxbToDoExtension(ServiceContext ctx, ConceptSchemeType source, ConceptSchemeVersion target) throws MetamacException {
        ConceptSchemeVersionMetamac targetMetamac = (ConceptSchemeVersionMetamac) target;

        // Fill metadata
        conceptsMetamacService.preCreateConceptScheme(ctx, targetMetamac);

        // TODO decidir que hacer ccon el tipo por defecto en la importacion
        targetMetamac.setType(ConceptSchemeTypeEnum.TRANSVERSAL);
        targetMetamac.setIsTypeUpdated(Boolean.TRUE);
    }

    @Override
    public void conceptsJaxb2DoExtension(ServiceContext ctx, ConceptType source, ConceptSchemeVersion conceptSchemeVersion, Concept target) throws MetamacException {
        ConceptMetamac targetMetamac = (ConceptMetamac) target;

        // Fill metadata
        conceptsMetamacService.preCreateConcept(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);

        // TODO metamac conceptsJaxb2DoExtension
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, ConceptSchemeVersion source) throws MetamacException {
        validateRestrictionsGeneral(ctx, source);
        validateRestrictionsMaintainableArtefact(ctx, source.getMaintainableArtefact(), false);
    }

}

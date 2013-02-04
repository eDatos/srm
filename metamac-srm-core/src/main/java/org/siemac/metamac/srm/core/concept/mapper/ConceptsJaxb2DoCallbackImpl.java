package org.siemac.metamac.srm.core.concept.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;
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
    public void conceptSchemeJaxbToDoExtensionPreCreate(ServiceContext ctx, ConceptSchemeType source, ConceptSchemeVersion previous, ConceptSchemeVersion target) throws MetamacException {
        ConceptSchemeVersionMetamac previousMetamac = (ConceptSchemeVersionMetamac) previous;
        ConceptSchemeVersionMetamac targetMetamac = (ConceptSchemeVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // TODO herencia IString --> Name
            // TODO herencia IString --> Description
            // TODO herencia IString --> Annotations
            targetMetamac.setType(previousMetamac.getType());
            targetMetamac.setRelatedOperation(BaseVersioningCopyUtils.copy(previousMetamac.getRelatedOperation()));
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Fill metadata
        conceptsMetamacService.preCreateConceptScheme(ctx, targetMetamac);
    }

    @Override
    public void conceptSchemeJaxbToDoExtensionPostCreate(ServiceContext ctx, ConceptSchemeType source, ConceptSchemeVersion previous, ConceptSchemeVersion target) throws MetamacException {
        ConceptSchemeVersionMetamac previousMetamac = (ConceptSchemeVersionMetamac) previous;
        ConceptSchemeVersionMetamac targetMetamac = (ConceptSchemeVersionMetamac) target;

        // Versioning related concepts
        conceptsMetamacService.versioningRelatedConcepts(ctx, previousMetamac, targetMetamac);
    }

    @Override
    public void conceptsJaxb2DoExtensionPreCreate(ServiceContext ctx, ConceptType source, ConceptSchemeVersion conceptSchemeVersion, Concept previous, Concept target) throws MetamacException {
        ConceptMetamac previousMetamac = (ConceptMetamac) previous;
        ConceptMetamac targetMetamac = (ConceptMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // TODO herencia IString --> Name
            // TODO herencia IString --> Description
            // TODO herencia IString --> Annotations
            targetMetamac.setPluralName(BaseVersioningCopyUtils.copy(previousMetamac.getPluralName()));
            targetMetamac.setAcronym(BaseVersioningCopyUtils.copy(previousMetamac.getAcronym()));
            targetMetamac.setDescriptionSource(BaseVersioningCopyUtils.copy(previousMetamac.getDescriptionSource()));
            targetMetamac.setContext(BaseVersioningCopyUtils.copy(previousMetamac.getContext()));
            targetMetamac.setDocMethod(BaseVersioningCopyUtils.copy(previousMetamac.getDocMethod()));
            targetMetamac.setSdmxRelatedArtefact(previousMetamac.getSdmxRelatedArtefact());
            targetMetamac.setType(previousMetamac.getType());
            // Roles : can copy "roles" because they are concepts in another concept scheme
            for (ConceptMetamac conceptRole : previousMetamac.getRoleConcepts()) {
                targetMetamac.addRoleConcept(conceptRole);
            }
            targetMetamac.setVariable(previousMetamac.getVariable());
            targetMetamac.setDerivation(BaseVersioningCopyUtils.copy(previousMetamac.getDerivation()));
            // Extends : can copy "extend" because they are concepts in another concept scheme
            targetMetamac.setConceptExtends(previousMetamac.getConceptExtends());
            // Related concepts
            targetMetamac.setLegalActs(BaseVersioningCopyUtils.copy(previousMetamac.getLegalActs()));

            // can not copy related concepts, because they belong to same concept scheme, and new concept in new version must relate to versioned related concept
        }

        // Fill metadata
        conceptsMetamacService.preCreateConcept(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn(), targetMetamac);
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

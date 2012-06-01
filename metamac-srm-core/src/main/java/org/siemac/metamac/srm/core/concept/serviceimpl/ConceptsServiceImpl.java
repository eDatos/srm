package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.concept.domain.Concept;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeRepository;
import org.siemac.metamac.srm.core.concept.exception.ConceptSchemeNotFoundException;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.InvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of ConceptsService.
 */
@Service("conceptsService")
public class ConceptsServiceImpl extends ConceptsServiceImplBase {

    @Autowired
    private ConceptSchemeRepository conceptSchemeRepository;

    public ConceptsServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ------------------------------------------------------------------------------------

    public ConceptScheme findConceptSchemeById(ServiceContext ctx, Long id) throws MetamacException {

        InvocationValidator.checkFindConceptSchemeById(id, null);

        try {
            return conceptSchemeRepository.findById(id);
        } catch (ConceptSchemeNotFoundException e) {
            throw new MetamacException(MetamacCoreExceptionType.CONCEPT_SCHEME_NOT_FOUND, id);
        }

    }

    public ConceptScheme createConceptScheme(ServiceContext ctx, ConceptScheme entity) throws MetamacException {

        InvocationValidator.checkCreateConceptScheme(entity, null);
        // validateConceptSchemeUnique(ctx, entity.getItemScheme().getIdLogic(), null);
        // TODO: Validate code unique
        // validateRoleCodeUnique(ctx, entity.getCode(), null);

        return conceptSchemeRepository.save(entity);

    }

    public ConceptScheme updateConceptScheme(ServiceContext ctx, ConceptScheme entity) throws MetamacException {

        InvocationValidator.checkUpdateConceptScheme(entity, null);
        // TODO: Validate code unique
        // validateRoleCodeUnique(ctx, entity.getCode(), entity.getId());

        return conceptSchemeRepository.save(entity);

    }

    public void deleteConceptScheme(ServiceContext ctx, Long id) throws MetamacException {

        InvocationValidator.checkDeleteConceptScheme(id, null);

        ConceptScheme conceptScheme = findConceptSchemeById(ctx, id);
        conceptSchemeRepository.delete(conceptScheme);

    }

    public List<ConceptScheme> findAllConceptSchemes(ServiceContext ctx) throws MetamacException {

        InvocationValidator.checkFindAllConceptSchemes(null);

        return conceptSchemeRepository.findAll();

    }

    @Override
    public MetamacCriteriaResult<ConceptScheme> findConceptSchemeByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    // ------------------------------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------------------------------

    public Concept findConceptById(ServiceContext ctx, Long id) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findConceptById not implemented");

    }

    public Concept createConcept(ServiceContext ctx, Long conceptSchemeId, ConceptScheme entity) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("createConcept not implemented");

    }

    public Concept updateConcept(ServiceContext ctx, ConceptScheme entity) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("updateConcept not implemented");

    }

    public void deleteConcept(ServiceContext ctx, Long id) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("deleteConcept not implemented");

    }

    public List<Concept> findConceptSchemeConcepts(ServiceContext ctx, Long conceptSchemeId) throws MetamacException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findConceptSchemeConcepts not implemented");

    }
}

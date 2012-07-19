package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.concept.domain.Concept;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeRepository;
import org.siemac.metamac.srm.core.concept.exception.ConceptSchemeNotFoundException;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;

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

        ConceptsInvocationValidator.checkFindConceptSchemeById(id, null);

        try {
            return conceptSchemeRepository.findById(id);
        } catch (ConceptSchemeNotFoundException e) {
            throw new MetamacException(MetamacCoreExceptionType.CONCEPT_SCHEME_NOT_FOUND, id);
        }

    }

    public ConceptScheme createConceptScheme(ServiceContext ctx, ConceptScheme entity) throws MetamacException {

        ConceptsInvocationValidator.checkCreateConceptScheme(entity, null);
        // TODO: Validate code unique: organization, id_logic, version?
        // validateConceptSchemeUnique(ctx, entity.getItemScheme().getIdLogic(), null);

        // TODO setear urn
        
        return conceptSchemeRepository.save(entity);

    }

    public ConceptScheme updateConceptScheme(ServiceContext ctx, ConceptScheme entity) throws MetamacException {

        ConceptsInvocationValidator.checkUpdateConceptScheme(entity, null);
        
        // TODO: Validate code unique: organization, id_logic, version?
        // validateConceptSchemeUnique(ctx, entity.getItemScheme().getIdLogic(), null);

        // TODO actualizar urn y de los hijos, si cambia el code
        return conceptSchemeRepository.save(entity);

    }

    public void deleteConceptScheme(ServiceContext ctx, Long id) throws MetamacException {

        ConceptsInvocationValidator.checkDeleteConceptScheme(id, null);

        ConceptScheme conceptScheme = findConceptSchemeById(ctx, id);
        
        // TODO: Check if the conceptScheme can be deleted.
        // We have to check if delete whole conceptScheme or only the last version
        conceptSchemeRepository.delete(conceptScheme);

    }

    public List<ConceptScheme> findAllConceptSchemes(ServiceContext ctx) throws MetamacException {

        ConceptsInvocationValidator.checkFindAllConceptSchemes(null);

        return conceptSchemeRepository.findAll();

    }

    @Override
    public PagedResult<ConceptScheme> findConceptSchemeByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
       
        ConceptsInvocationValidator.checkFindConceptSchemeByCondition(conditions, pagingParameter, null);
        
        CriteriaUtils.initCriteriaConditions(conditions, ConceptScheme.class);
        
        return conceptSchemeRepository.findByCondition(conditions, pagingParameter);
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

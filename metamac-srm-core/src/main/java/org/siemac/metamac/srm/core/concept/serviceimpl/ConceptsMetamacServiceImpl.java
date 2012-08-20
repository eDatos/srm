package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;

/**
 * Implementation of ConceptsMetamacService.
 */
@Service("conceptsMetamacService")
public class ConceptsMetamacServiceImpl extends ConceptsMetamacServiceImplBase {

    @Autowired
    private ConceptsService conceptsService;

    public ConceptsMetamacServiceImpl() {
    }

    // TODO ciclo de vida común para todos los ItemScheme
    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);

        // Fill metadata
        conceptSchemeVersion.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion);
    }

    @Override
    public ConceptSchemeVersionMetamac updateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptsService.retrieveConceptSchemeByUrn(ctx, urn);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindConceptSchemesByCondition(conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).build();
        }
        PagedResult<ConceptSchemeVersionMetamac> conceptSchemeVersionPagedResult = getConceptSchemeVersionMetamacRepository().findByCondition(conditions, pagingParameter);
        return conceptSchemeVersionPagedResult;
    }

}

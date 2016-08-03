package org.siemac.metamac.srm.core.concept.repositoryimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.ConceptTypeProperties;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for ConceptType
 */
@Repository("conceptTypeRepository")
public class ConceptTypeRepositoryImpl extends ConceptTypeRepositoryBase {

    public ConceptTypeRepositoryImpl() {
    }

    public ConceptType findByIdentifier(String identifier) {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptType.class).withProperty(ConceptTypeProperties.identifier()).eq(identifier).distinctRoot().build();
        List<ConceptType> conceptsTypes = findByCondition(conditions);
        if (conceptsTypes.size() != 0) {
            return conceptsTypes.get(0);
        }
        return null;
    }
}

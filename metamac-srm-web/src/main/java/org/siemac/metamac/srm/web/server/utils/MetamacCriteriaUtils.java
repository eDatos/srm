package org.siemac.metamac.srm.web.server.utils;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;

public class MetamacCriteriaUtils {

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the ConceptScheme
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaDisjunctionRestriction getConceptSchemeCriteriaDisjunctionRestriction(String criteria) {
        MetamacCriteriaDisjunctionRestriction conceptSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (StringUtils.isNotBlank(criteria)) {
            conceptSchemeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria, OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria, OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria, OperationType.ILIKE));
        }
        return conceptSchemeCriteriaDisjuction;
    }

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the Concept
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaDisjunctionRestriction getConceptCriteriaDisjunctionRestriction(String criteria) {
        MetamacCriteriaDisjunctionRestriction conceptCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
        if (StringUtils.isNotBlank(criteria)) {
            conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), criteria, OperationType.ILIKE));
            conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), criteria, OperationType.ILIKE));
            conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), criteria, OperationType.ILIKE));
        }
        return conceptCriteriaDisjunction;
    }
}

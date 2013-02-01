package org.siemac.metamac.srm.web.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaRestriction;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;

public class MetamacCriteriaUtils {

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the ConceptScheme
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaRestriction getConceptSchemeCriteriaRestriction(ConceptSchemeWebCriteria criteria) {
        MetamacCriteriaDisjunctionRestriction conceptSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (criteria != null && StringUtils.isNotBlank(criteria.getCriteria())) {
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
        }
        return conceptSchemeCriteriaDisjuction;
    }

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the Concept
     * 
     * @param criteria
     * @return
     */
    public static List<MetamacCriteriaRestriction> getConceptCriteriaRestriction(ConceptWebCriteria criteria) {
        List<MetamacCriteriaRestriction> restrictions = new ArrayList<MetamacCriteriaRestriction>();
        if (criteria != null) {
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction conceptCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                restrictions.add(conceptCriteriaDisjunction);
            }
            if (StringUtils.isNotBlank(criteria.getConceptSchemeUrn())) {
                MetamacCriteriaPropertyRestriction conceptSchemePropertyRestriction = new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(),
                        criteria.getConceptSchemeUrn(), OperationType.EQ);
                restrictions.add(conceptSchemePropertyRestriction);
            }
        }
        return restrictions;
    }

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the Codelist
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaRestriction getCodelistCriteriaRestriction(CodelistWebCriteria criteria) {
        MetamacCriteriaDisjunctionRestriction codelistCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (criteria != null && StringUtils.isNotBlank(criteria.getCriteria())) {
            codelistCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            codelistCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
            codelistCriteriaDisjuction.getRestrictions()
                    .add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
        }
        return codelistCriteriaDisjuction;
    }
}

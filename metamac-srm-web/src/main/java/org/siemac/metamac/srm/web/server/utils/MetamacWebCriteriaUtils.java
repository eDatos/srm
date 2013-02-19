package org.siemac.metamac.srm.web.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaRestriction;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaPropertyEnum;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;

public class MetamacWebCriteriaUtils {

    // -------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getConceptSchemeCriteriaRestriction(ConceptSchemeWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
        // General criteria
        MetamacCriteriaDisjunctionRestriction conceptSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (criteria != null && StringUtils.isNotBlank(criteria.getCriteria())) {
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
        }
        conjunctionRestriction.getRestrictions().add(conceptSchemeCriteriaDisjuction);
        // Specific criteria
        if (StringUtils.isNotBlank(criteria.getCode())) {
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getName())) {
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getUrn())) {
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getDescription())) {
            // TODO
        }
        if (criteria.getProcStatus() != null) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
        }
        if (criteria.getInternalPublicationDate() != null) {
            // TODO
        }
        if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
            // TODO
        }
        if (criteria.getExternalPublicationDate() != null) {
            // TODO
        }
        if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
            // TODO
        }
        if (criteria.getIsLastVersion() != null) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), criteria.getIsLastVersion(), OperationType.EQ));
        }
        return conjunctionRestriction;
    }

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the Concept
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaConjunctionRestriction getConceptCriteriaRestriction(ConceptWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
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
        restriction.getRestrictions().addAll(restrictions);
        return restriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------------------------------------

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

    /**
     * Returns a {@link MetamacCriteriaConjunctionRestriction} that compares the criteria with the CODE and NAMEof the VariableElement
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaConjunctionRestriction getVariableElementCriteriaRestriction(VariableElementWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        List<MetamacCriteriaRestriction> restrictions = new ArrayList<MetamacCriteriaRestriction>();
        if (criteria != null) {
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction variableElementCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                variableElementCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                variableElementCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                restrictions.add(variableElementCriteriaDisjunction);
            }
            if (StringUtils.isNotBlank(criteria.getVariableUrn())) {
                MetamacCriteriaPropertyRestriction variableElementPropertyRestriction = new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.VARIABLE_URN.name(),
                        criteria.getVariableUrn(), OperationType.EQ);
                restrictions.add(variableElementPropertyRestriction);
            }
        }
        restriction.getRestrictions().addAll(restrictions);
        return restriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getCategorySchemeCriteriaRestriction(CategorySchemeWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
        // General criteria
        MetamacCriteriaDisjunctionRestriction categorySchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (criteria != null && StringUtils.isNotBlank(criteria.getCriteria())) {
            categorySchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            categorySchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
            categorySchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
        }
        conjunctionRestriction.getRestrictions().add(categorySchemeCriteriaDisjuction);
        // Specific criteria
        if (StringUtils.isNotBlank(criteria.getCode())) {
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getName())) {
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getUrn())) {
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getDescription())) {
            // TODO
        }
        if (criteria.getProcStatus() != null) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
        }
        if (criteria.getInternalPublicationDate() != null) {
            // TODO
        }
        if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
            // TODO
        }
        if (criteria.getExternalPublicationDate() != null) {
            // TODO
        }
        if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
            // TODO
        }
        if (criteria.getIsLastVersion() != null) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), criteria.getIsLastVersion(), OperationType.EQ));
        }
        return conjunctionRestriction;
    }

    /**
     * Returns a {@link MetamacCriteriaDisjunctionRestriction} that compares the criteria with the CODE, NAME and URN of the Category
     * 
     * @param criteria
     * @return
     */
    public static MetamacCriteriaConjunctionRestriction getCategoryCriteriaRestriction(CategoryWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        List<MetamacCriteriaRestriction> restrictions = new ArrayList<MetamacCriteriaRestriction>();
        if (criteria != null) {
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction categoryCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                categoryCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                categoryCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                categoryCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                restrictions.add(categoryCriteriaDisjunction);
            }
            if (StringUtils.isNotBlank(criteria.getCategorySchemeUrn())) {
                MetamacCriteriaPropertyRestriction categorySchemePropertyRestriction = new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_URN.name(),
                        criteria.getCategorySchemeUrn(), OperationType.EQ);
                restrictions.add(categorySchemePropertyRestriction);
            }
        }
        restriction.getRestrictions().addAll(restrictions);
        return restriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getDataStructureDefinitionCriteriaRestriction(DataStructureDefinitionWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
        // General criteria
        MetamacCriteriaDisjunctionRestriction dsdCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (criteria != null && StringUtils.isNotBlank(criteria.getCriteria())) {
            dsdCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
            dsdCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
            dsdCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
        }
        conjunctionRestriction.getRestrictions().add(dsdCriteriaDisjuction);
        // Specific criteria
        if (StringUtils.isNotBlank(criteria.getCode())) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getName())) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getUrn())) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
        }
        if (StringUtils.isNotBlank(criteria.getDescription())) {
            // TODO
        }
        if (criteria.getProcStatus() != null) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
        }
        if (criteria.getInternalPublicationDate() != null) {
            // TODO
        }
        if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
            // TODO
        }
        if (criteria.getExternalPublicationDate() != null) {
            // TODO
        }
        if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
            // TODO
        }
        if (criteria.getIsLastVersion() != null) {
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), criteria.getIsLastVersion(), OperationType.EQ));
        }
        return conjunctionRestriction;
    }
}

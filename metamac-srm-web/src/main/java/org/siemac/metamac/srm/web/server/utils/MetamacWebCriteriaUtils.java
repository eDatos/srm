package org.siemac.metamac.srm.web.server.utils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaRestriction;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationContactCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaPropertyEnum;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationContactWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;

public class MetamacWebCriteriaUtils {

    // -------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getConceptSchemeCriteriaRestriction(ConceptSchemeWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction conceptSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
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
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (criteria.getProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getStatisticalOperationUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.STATISTICAL_OPERATION_URN.name(), criteria.getStatisticalOperationUrn(),
                                OperationType.EQ));
            }
            if (criteria.getConceptSchemeTypeEnum() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_TYPE.name(), criteria.getConceptSchemeTypeEnum(), OperationType.EQ));
            }
            if (criteria.getInternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_DATE.name(), criteria.getInternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_USER.name(), criteria.getInternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (criteria.getExternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), criteria.getExternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_USER.name(), criteria.getExternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getMaintainerUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.MAINTAINER_URN.name(), criteria.getMaintainerUrn(), OperationType.EQ));
            }

        }
        return conjunctionRestriction;
    }

    public static MetamacCriteriaConjunctionRestriction getConceptCriteriaRestriction(ConceptWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction conceptCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                conceptCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                conceptCriteriaDisjunction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.ACRONYM.name(), criteria.getCriteria(), OperationType.ILIKE));
                conjunctionRestriction.getRestrictions().add(conceptCriteriaDisjunction);
            }

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (StringUtils.isNotBlank(criteria.getItemSchemeUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_URN.name(), criteria.getItemSchemeUrn(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getAcronym())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.ACRONYM.name(), criteria.getAcronym(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescriptionSource())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.DESCRIPTION_SOURCE.name(), criteria.getDescriptionSource(), OperationType.ILIKE));
            }
            if (criteria.getConceptSchemeTypeEnum() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_TYPE.name(), criteria.getConceptSchemeTypeEnum(), OperationType.EQ));
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CONCEPT_SCHEME_LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }

        }
        return conjunctionRestriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getCodelistCriteriaRestriction(CodelistWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction codelistCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                codelistCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                codelistCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                codelistCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(codelistCriteriaDisjuction);

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getCodelistFamilyUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODELIST_FAMILY_URN.name(), criteria.getCodelistFamilyUrn(), OperationType.EQ));
            }
            if (criteria.getAccessType() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.ACCESS_TYPE.name(), criteria.getAccessType(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (criteria.getProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
            }
            if (criteria.getInternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_DATE.name(), criteria.getInternalPublicationDate(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_USER.name(), criteria.getInternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (criteria.getExternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), criteria.getExternalPublicationDate(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_USER.name(), criteria.getExternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (criteria.getIsNotCodelistUrn() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getIsNotCodelistUrn(), OperationType.NE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getMaintainerUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.MAINTAINER_URN.name(), criteria.getMaintainerUrn(), OperationType.EQ));
            }

            if (StringUtils.isNotBlank(criteria.getVariableUrn())) {
                MetamacCriteriaRestriction restriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.VARIABLE.name(), criteria.getVariableUrn(),
                        OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(restriction);
            }
            if (StringUtils.isNotBlank(criteria.getVariableElementUrn())) {
                MetamacCriteriaRestriction restriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.VARIABLE_ELEMENT.name(), criteria.getVariableElementUrn(),
                        OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(restriction);
            }
            if (StringUtils.isNotBlank(criteria.getVariableFamilyUrn())) {
                MetamacCriteriaRestriction restriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.VARIABLE_FAMILY.name(), criteria.getVariableFamilyUrn(),
                        OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(restriction);
            }

        }
        return conjunctionRestriction;
    }

    public static MetamacCriteriaRestriction getCodeCriteriaRestriction(CodeWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction codeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                codeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                codeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                codeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                codeCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.SHORT_NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(codeCriteriaDisjuction);

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (StringUtils.isNotBlank(criteria.getItemSchemeUrn())) {
                MetamacCriteriaPropertyRestriction codePropertyRestriction = new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_URN.name(), criteria.getItemSchemeUrn(),
                        OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(codePropertyRestriction);
            }
            if (StringUtils.isNotBlank(criteria.getShortName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.SHORT_NAME.name(), criteria.getShortName(), OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CodeMetamacCriteriaPropertyEnum.CODELIST_LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }

        }
        return conjunctionRestriction;
    }

    public static MetamacCriteriaConjunctionRestriction getVariableCriteriaRestriction(VariableWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction variableElementCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                variableElementCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                variableElementCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                variableElementCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.SHORT_NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                variableElementCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                conjunctionRestriction.getRestrictions().add(variableElementCriteriaDisjunction);
            }

            // Specific criteria
            if (criteria.getVariableType() != null) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.GEOGRAPHICAL.name(), criteria.getVariableType(), OperationType.EQ));
            }

        }

        return conjunctionRestriction;
    }

    public static MetamacCriteriaConjunctionRestriction getVariableElementCriteriaRestriction(VariableElementWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction variableElementCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                variableElementCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                variableElementCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                variableElementCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.SHORT_NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                conjunctionRestriction.getRestrictions().add(variableElementCriteriaDisjunction);
            }

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getVariableUrn())) {
                MetamacCriteriaPropertyRestriction variableElementPropertyRestriction = new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.VARIABLE_URN.name(),
                        criteria.getVariableUrn(), OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(variableElementPropertyRestriction);
            }
            if (criteria.getVariableType() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.IS_GEOGRAPHICAL_VARIABLE_ELEMENT.name(), criteria.getVariableType(), OperationType.EQ));
            }
            if (BooleanUtils.isTrue(criteria.getIsHasShape())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.HAS_SHAPE.name(), criteria.getIsHasShape(), OperationType.IS_NOT_NULL));
            }
            if (StringUtils.isNotBlank(criteria.getGranularityCodeUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY.name(), criteria.getGranularityCodeUrn(), OperationType.ILIKE));
            }
            if (criteria.getValidFromDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.VALID_FROM_DATE.name(), criteria.getValidFromDate(), OperationType.EQ));
            }
            if (criteria.getValidToDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.VALID_TO_DATE.name(), criteria.getValidToDate(), OperationType.EQ));
            }
        }

        return conjunctionRestriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getCategorySchemeCriteriaRestriction(CategorySchemeWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction categorySchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
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
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (criteria.getProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
            }
            if (criteria.getInternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_DATE.name(), criteria.getInternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_USER.name(), criteria.getInternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (criteria.getExternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), criteria.getExternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_USER.name(), criteria.getExternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getMaintainerUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.MAINTAINER_URN.name(), criteria.getMaintainerUrn(), OperationType.EQ));
            }

        }

        return conjunctionRestriction;
    }

    public static MetamacCriteriaConjunctionRestriction getCategoryCriteriaRestriction(CategoryWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction categoryCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                categoryCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                categoryCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                categoryCriteriaDisjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                conjunctionRestriction.getRestrictions().add(categoryCriteriaDisjunction);
            }

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions()
                        .add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (StringUtils.isNotBlank(criteria.getItemSchemeUrn())) {
                MetamacCriteriaPropertyRestriction categorySchemePropertyRestriction = new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_URN.name(),
                        criteria.getItemSchemeUrn(), OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(categorySchemePropertyRestriction);
            }
            if (criteria.getIsExternallyPublished() != null) {
                MetamacCriteriaPropertyRestriction publishedRestriction = new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_EXTERNALLY_PUBLISHED.name(),
                        criteria.getIsExternallyPublished(), OperationType.EQ);
                conjunctionRestriction.getRestrictions().add(publishedRestriction);
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }

        }

        return conjunctionRestriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getDataStructureDefinitionCriteriaRestriction(DataStructureDefinitionWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction dsdCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
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
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
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
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (criteria.getProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getStatisticalOperationUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.STATISTICAL_OPERATION_URN.name(), criteria.getStatisticalOperationUrn(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getDimensionConceptUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.DIMENSION_CONCEPT_URN.name(), criteria.getDimensionConceptUrn(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getAttributeConceptUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.ATTRIBUTE_CONCEPT_URN.name(), criteria.getAttributeConceptUrn(),
                                OperationType.EQ));
            }
            if (criteria.getInternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_DATE.name(), criteria.getInternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_USER.name(), criteria.getInternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (criteria.getExternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), criteria.getExternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_USER.name(), criteria.getExternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getMaintainerUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.MAINTAINER_URN.name(), criteria.getMaintainerUrn(), OperationType.EQ));
            }

        }
        return conjunctionRestriction;
    }

    // -------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // -------------------------------------------------------------------------------------------------------------

    public static MetamacCriteriaRestriction getOrganisationSchemeCriteriaRestriction(OrganisationSchemeWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            MetamacCriteriaDisjunctionRestriction organisationSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                organisationSchemeCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                organisationSchemeCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                organisationSchemeCriteriaDisjuction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
            }
            conjunctionRestriction.getRestrictions().add(organisationSchemeCriteriaDisjuction);

            // Specific criteria

            if (criteria.getType() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.TYPE.name(), criteria.getType(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (criteria.getProcStatus() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), criteria.getProcStatus(), OperationType.EQ));
            }
            if (criteria.getInternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_DATE.name(), criteria.getInternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getInternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.INTERNAL_PUBLICATION_USER.name(), criteria.getInternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (criteria.getExternalPublicationDate() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_DATE.name(), criteria.getExternalPublicationDate(),
                                OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getExternalPublicationUser())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.EXTERNAL_PUBLICATION_USER.name(), criteria.getExternalPublicationUser(),
                                OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getMaintainerUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.MAINTAINER_URN.name(), criteria.getMaintainerUrn(), OperationType.EQ));
            }

        }
        return conjunctionRestriction;
    }

    public static MetamacCriteriaConjunctionRestriction getOrganisationCriteriaRestriction(OrganisationWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction organisationCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                organisationCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCriteria(), OperationType.ILIKE));
                organisationCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                organisationCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.URN.name(), criteria.getCriteria(), OperationType.ILIKE));
                conjunctionRestriction.getRestrictions().add(organisationCriteriaDisjunction);
            }

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getCode())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCode(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getCodeEQ())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.CODE.name(), criteria.getCodeEQ(), OperationType.EQ));
            }
            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getUrn())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.URN.name(), criteria.getUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getDescription())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.DESCRIPTION.name(), criteria.getDescription(), OperationType.ILIKE));
            }
            if (BooleanUtils.isTrue(criteria.getIsLastVersion())) {
                MetamacCriteriaDisjunctionRestriction lastVersionRestriction = new MetamacCriteriaDisjunctionRestriction();
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_IS_LAST_VERSION.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_LATEST_PUBLIC.name(), true, OperationType.EQ));
                lastVersionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_LATEST_FINAL.name(), true, OperationType.EQ));
                conjunctionRestriction.getRestrictions().add(lastVersionRestriction);
            }
            if (StringUtils.isNotBlank(criteria.getItemSchemeUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_URN.name(), criteria.getItemSchemeUrn(), OperationType.EQ));
            }
            if (criteria.getOrganisationSchemeType() != null) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_TYPE.name(), criteria.getOrganisationSchemeType(), OperationType.EQ));
            }
            if (BooleanUtils.isTrue(criteria.getIsLatestFinal())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_LATEST_FINAL.name(), criteria.getIsLatestFinal(), OperationType.EQ));
            }
        }

        return conjunctionRestriction;
    }

    public static MetamacCriteriaConjunctionRestriction getContactCriteriaRestriction(OrganisationContactWebCriteria criteria) {
        MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();

        if (criteria != null) {

            // General criteria

            if (StringUtils.isNotBlank(criteria.getCriteria())) {
                MetamacCriteriaDisjunctionRestriction organisationCriteriaDisjunction = new MetamacCriteriaDisjunctionRestriction();
                organisationCriteriaDisjunction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationContactCriteriaPropertyEnum.NAME.name(), criteria.getCriteria(), OperationType.ILIKE));
                conjunctionRestriction.getRestrictions().add(organisationCriteriaDisjunction);
            }

            // Specific criteria

            if (StringUtils.isNotBlank(criteria.getName())) {
                conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationContactCriteriaPropertyEnum.NAME.name(), criteria.getName(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getOrganisationUrn())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationContactCriteriaPropertyEnum.ORGANISATION_URN.name(), criteria.getOrganisationUrn(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getOrganisationUnit())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationContactCriteriaPropertyEnum.ORGANISATION_UNIT.name(), criteria.getOrganisationUnit(), OperationType.ILIKE));
            }
            if (StringUtils.isNotBlank(criteria.getResponsibility())) {
                conjunctionRestriction.getRestrictions().add(
                        new MetamacCriteriaPropertyRestriction(OrganisationContactCriteriaPropertyEnum.RESPONSIBILITY.name(), criteria.getResponsibility(), OperationType.ILIKE));
            }
        }

        return conjunctionRestriction;
    }
}

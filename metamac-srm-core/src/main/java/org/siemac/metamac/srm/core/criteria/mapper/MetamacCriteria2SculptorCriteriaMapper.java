package org.siemac.metamac.srm.core.criteria.mapper;

import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;

public interface MetamacCriteria2SculptorCriteriaMapper {

    public MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<ConceptMetamac> getConceptMetamacCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac> getOrganisationSchemeMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<OrganisationMetamac> getOrganisationMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<Contact> getOrganisationContactCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<CategorySchemeVersionMetamac> getCategorySchemeMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<CategoryMetamac> getCategoryMetamacCriteriaMapper();

    public MetamacCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<CodeMetamac> getCodeMetamacCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<CodelistFamily> getCodelistFamilyCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<Variable> getVariableCriteriaMapper();
    public MetamacCriteria2SculptorCriteria<VariableElement> getVariableElementCriteriaMapper();
}

package org.siemac.metamac.srm.core.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.importation.domain.ImportData;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.importation.ImportDataDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

public interface SculptorCriteria2MetamacCriteriaMapper {

    public MetamacCriteriaResult<DataStructureDefinitionMetamacDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinitionVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<ImportDataDto> pageResultToMetamacCriteriaResultImportData(PagedResult<ImportData> source, Integer pageSize);

    public MetamacCriteriaResult<ConceptSchemeMetamacDto> pageResultToMetamacCriteriaResultConceptSchemeVersion(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<ConceptMetamacDto> pageResultToMetamacCriteriaResultConcept(PagedResult<ConceptMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultConceptToMetamacCriteriaResultRelatedResource(PagedResult<ConceptMetamac> source, Integer pageSize) throws MetamacException;

    public MetamacCriteriaResult<OrganisationSchemeMetamacDto> pageResultToMetamacCriteriaResultOrganisationSchemeVersion(PagedResult<OrganisationSchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<OrganisationMetamacDto> pageResultToMetamacCriteriaResultOrganisation(PagedResult<OrganisationMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<ContactDto> pageResultToMetamacCriteriaResultOrganisationContact(PagedResult<Contact> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultOrganisationToMetamacCriteriaResultRelatedResource(PagedResult<OrganisationMetamac> source, Integer pageSize) throws MetamacException;

    public MetamacCriteriaResult<CategorySchemeMetamacDto> pageResultToMetamacCriteriaResultCategorySchemeVersion(PagedResult<CategorySchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<CategoryMetamacDto> pageResultToMetamacCriteriaResultCategory(PagedResult<CategoryMetamac> source, Integer pageSize);

    public MetamacCriteriaResult<CodelistMetamacDto> pageResultToMetamacCriteriaResultCodelistVersion(PagedResult<CodelistVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCodelistToMetamacCriteriaResultRelatedResource(PagedResult<CodelistVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<CodeMetamacDto> pageResultToMetamacCriteriaResultCode(PagedResult<CodeMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<CodelistFamilyDto> pageResultToMetamacCriteriaResultCodelistFamily(PagedResult<CodelistFamily> source, Integer pageSize);
    public MetamacCriteriaResult<VariableFamilyDto> pageResultToMetamacCriteriaResultVariableFamily(PagedResult<VariableFamily> source, Integer pageSize);
    public MetamacCriteriaResult<VariableDto> pageResultToMetamacCriteriaResultVariable(PagedResult<Variable> source, Integer pageSize);
    public MetamacCriteriaResult<VariableElementDto> pageResultToMetamacCriteriaResultVariableElement(PagedResult<VariableElement> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultToMetamacCriteriaResultVariableElementRelatedResource(PagedResult<VariableElement> source, Integer pageSize);
}

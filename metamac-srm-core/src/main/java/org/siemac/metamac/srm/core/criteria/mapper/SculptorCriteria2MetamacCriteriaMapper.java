package org.siemac.metamac.srm.core.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.task.domain.Task;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.task.TaskDto;

public interface SculptorCriteria2MetamacCriteriaMapper {

    public MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> pageResultDataStructureDefinitionToMetamacCriteriaResult(PagedResult<DataStructureDefinitionVersionMetamac> source,
            Integer pageSize) throws MetamacException;
    public MetamacCriteriaResult<TaskDto> pageResultTaskToMetamacCriteriaResult(PagedResult<Task> source, Integer pageSize);

    public MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> pageResultConceptSchemeVersionToMetamacCriteriaResult(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize)
            throws MetamacException;
    public MetamacCriteriaResult<RelatedResourceDto> pageResultConceptSchemeVersionToMetamacCriteriaResultRelatedResource(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<ConceptMetamacBasicDto> pageResultConceptToMetamacCriteriaResult(PagedResult<ConceptMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultConceptToMetamacCriteriaResultRelatedResource(PagedResult<ConceptMetamac> source, Integer pageSize) throws MetamacException;

    public MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto> pageResultOrganisationSchemeVersionToMetamacCriteriaResult(PagedResult<OrganisationSchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<OrganisationMetamacBasicDto> pageResultOrganisationToMetamacCriteriaResult(PagedResult<OrganisationMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<ContactDto> pageResultOrganisationContactToMetamacCriteriaResult(PagedResult<Contact> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultOrganisationToMetamacCriteriaResultRelatedResource(PagedResult<OrganisationMetamac> source, Integer pageSize) throws MetamacException;

    public MetamacCriteriaResult<CategorySchemeMetamacBasicDto> pageResultCategorySchemeVersionToMetamacCriteriaResult(PagedResult<CategorySchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCategorySchemeVersionToMetamacCriteriaResultRelatedResource(PagedResult<CategorySchemeVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<CategoryMetamacBasicDto> pageResultCategoryToMetamacCriteriaResult(PagedResult<CategoryMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCategoryToMetamacCriteriaResultRelatedResource(PagedResult<CategoryMetamac> source, Integer pageSize);

    public MetamacCriteriaResult<CodelistMetamacBasicDto> pageResultCodelistVersionToMetamacCriteriaResult(PagedResult<CodelistVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCodelistVersionToMetamacCriteriaResultRelatedResource(PagedResult<CodelistVersionMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<CodeMetamacBasicDto> pageResultCodeToMetamacCriteriaResult(PagedResult<CodeMetamac> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCodeToMetamacCriteriaResultRelatedResource(PagedResult<CodeMetamac> source, Integer pageSize) throws MetamacException;

    public MetamacCriteriaResult<CodelistFamilyBasicDto> pageResultCodelistFamilyToMetamacCriteriaResult(PagedResult<CodelistFamily> source, Integer pageSize);
    public MetamacCriteriaResult<VariableFamilyBasicDto> pageResultVariableFamilyToMetamacCriteriaResult(PagedResult<VariableFamily> source, Integer pageSize);
    public MetamacCriteriaResult<VariableBasicDto> pageResultVariableToMetamacCriteriaResult(PagedResult<Variable> source, Integer pageSize);
    public MetamacCriteriaResult<VariableElementBasicDto> pageResultVariableElementToMetamacCriteriaResult(PagedResult<VariableElement> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultVariableElementToMetamacCriteriaResultRelatedResource(PagedResult<VariableElement> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCodelistOrderVisualisationToMetamacCriteriaResultRelatedResource(PagedResult<CodelistOrderVisualisation> source, Integer pageSize);
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCodelistOpennessVisualisationToMetamacCriteriaResultRelatedResource(PagedResult<CodelistOpennessVisualisation> source, Integer pageSize);
}

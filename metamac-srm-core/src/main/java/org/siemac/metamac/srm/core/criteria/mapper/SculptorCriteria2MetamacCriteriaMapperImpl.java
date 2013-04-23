package org.siemac.metamac.srm.core.criteria.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapper;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
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
import org.siemac.metamac.srm.core.code.mapper.CodesDo2DtoMapper;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDo2DtoMapper;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDo2DtoMapper;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.task.domain.Task;
import com.arte.statistic.sdmx.srm.core.task.mapper.TasksDo2DtoMapper;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.task.TaskDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private DataStructureDefinitionDo2DtoMapper dataStructureDefinitionDo2DtoMapper;

    @Autowired
    private TasksDo2DtoMapper                   tasksDo2DtoMapper;

    @Autowired
    private ConceptsDo2DtoMapper                conceptsDo2DtoMapper;

    @Autowired
    private OrganisationsDo2DtoMapper           organisationsDo2DtoMapper;

    @Autowired
    private CategoriesDo2DtoMapper              categoriesDo2DtoMapper;

    @Autowired
    private CodesDo2DtoMapper                   codesDo2DtoMapper;

    //
    // DATA STRUCTURE DEFINITION
    //

    @Override
    public MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinitionVersionMetamac> source,
            Integer pageSize) {
        MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto> target = new MetamacCriteriaResult<DataStructureDefinitionMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<DataStructureDefinitionMetamacBasicDto>(source.getValues().size()));
            for (DataStructureDefinitionVersionMetamac scheme : source.getValues()) {
                target.getResults().add(dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToBasicDto(scheme));
            }
        }
        return target;
    }

    //
    // CONCEPTS
    //

    @Override
    public MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> pageResultToMetamacCriteriaResultConceptSchemeVersion(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<ConceptSchemeMetamacBasicDto> target = new MetamacCriteriaResult<ConceptSchemeMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ConceptSchemeMetamacBasicDto>(source.getValues().size()));
            for (ConceptSchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptSchemeMetamacDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> pageResultConceptSchemeToMetamacCriteriaResultRelatedResource(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<RelatedResourceDto> target = new MetamacCriteriaResult<RelatedResourceDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<RelatedResourceDto>(source.getValues().size()));
            for (ConceptSchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptSchemeMetamacDoToRelatedResourceDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<ConceptMetamacBasicDto> pageResultToMetamacCriteriaResultConcept(PagedResult<ConceptMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<ConceptMetamacBasicDto> target = new MetamacCriteriaResult<ConceptMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ConceptMetamacBasicDto>(source.getValues().size()));
            for (ConceptMetamac concept : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptMetamacDoToBasicDto(concept));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> pageResultConceptToMetamacCriteriaResultRelatedResource(PagedResult<ConceptMetamac> source, Integer pageSize) throws MetamacException {
        MetamacCriteriaResult<RelatedResourceDto> target = new MetamacCriteriaResult<RelatedResourceDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<RelatedResourceDto>(source.getValues().size()));
            for (ConceptMetamac scheme : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptMetamacDoToRelatedResourceDto(scheme));
            }
        }
        return target;
    }

    //
    // ORGANISATIONS
    //

    @Override
    public MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto> pageResultToMetamacCriteriaResultOrganisationSchemeVersion(PagedResult<OrganisationSchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto> target = new MetamacCriteriaResult<OrganisationSchemeMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<OrganisationSchemeMetamacBasicDto>(source.getValues().size()));
            for (OrganisationSchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationSchemeMetamacDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<OrganisationMetamacBasicDto> pageResultToMetamacCriteriaResultOrganisation(PagedResult<OrganisationMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<OrganisationMetamacBasicDto> target = new MetamacCriteriaResult<OrganisationMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<OrganisationMetamacBasicDto>(source.getValues().size()));
            for (OrganisationMetamac organisation : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationMetamacDoToBasicDto(organisation));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> pageResultOrganisationToMetamacCriteriaResultRelatedResource(PagedResult<OrganisationMetamac> source, Integer pageSize) throws MetamacException {
        MetamacCriteriaResult<RelatedResourceDto> target = new MetamacCriteriaResult<RelatedResourceDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<RelatedResourceDto>(source.getValues().size()));
            for (OrganisationMetamac scheme : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationMetamacDoToRelatedResourceDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<ContactDto> pageResultToMetamacCriteriaResultOrganisationContact(PagedResult<Contact> source, Integer pageSize) {
        MetamacCriteriaResult<ContactDto> target = new MetamacCriteriaResult<ContactDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ContactDto>(source.getValues().size()));
            for (Contact organisation : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.contactDoToDto(organisation));
            }
        }
        return target;
    }

    //
    // CATEGORIES
    //

    @Override
    public MetamacCriteriaResult<CategorySchemeMetamacBasicDto> pageResultToMetamacCriteriaResultCategorySchemeVersion(PagedResult<CategorySchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CategorySchemeMetamacBasicDto> target = new MetamacCriteriaResult<CategorySchemeMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CategorySchemeMetamacBasicDto>(source.getValues().size()));
            for (CategorySchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(categoriesDo2DtoMapper.categorySchemeMetamacDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<CategoryMetamacBasicDto> pageResultToMetamacCriteriaResultCategory(PagedResult<CategoryMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CategoryMetamacBasicDto> target = new MetamacCriteriaResult<CategoryMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CategoryMetamacBasicDto>(source.getValues().size()));
            for (CategoryMetamac category : source.getValues()) {
                target.getResults().add(categoriesDo2DtoMapper.categoryMetamacDoToBasicDto(category));
            }
        }
        return target;
    }

    //
    // CODES
    //

    @Override
    public MetamacCriteriaResult<CodelistMetamacBasicDto> pageResultToMetamacCriteriaResultCodelistVersion(PagedResult<CodelistVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CodelistMetamacBasicDto> target = new MetamacCriteriaResult<CodelistMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodelistMetamacBasicDto>(source.getValues().size()));
            for (CodelistVersionMetamac scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codelistMetamacDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> pageResultCodelistToMetamacCriteriaResultRelatedResource(PagedResult<CodelistVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<RelatedResourceDto> target = new MetamacCriteriaResult<RelatedResourceDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<RelatedResourceDto>(source.getValues().size()));
            for (CodelistVersionMetamac scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codelistMetamacDoToRelatedResourceDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<CodeMetamacBasicDto> pageResultToMetamacCriteriaResultCode(PagedResult<CodeMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CodeMetamacBasicDto> target = new MetamacCriteriaResult<CodeMetamacBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodeMetamacBasicDto>(source.getValues().size()));
            for (CodeMetamac code : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codeMetamacDoToBasicDto(code));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<CodelistFamilyBasicDto> pageResultToMetamacCriteriaResultCodelistFamily(PagedResult<CodelistFamily> source, Integer pageSize) {
        MetamacCriteriaResult<CodelistFamilyBasicDto> target = new MetamacCriteriaResult<CodelistFamilyBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodelistFamilyBasicDto>(source.getValues().size()));
            for (CodelistFamily scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codelistFamilyDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<VariableFamilyBasicDto> pageResultToMetamacCriteriaResultVariableFamily(PagedResult<VariableFamily> source, Integer pageSize) {
        MetamacCriteriaResult<VariableFamilyBasicDto> target = new MetamacCriteriaResult<VariableFamilyBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<VariableFamilyBasicDto>(source.getValues().size()));
            for (VariableFamily scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableFamilyDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<VariableBasicDto> pageResultToMetamacCriteriaResultVariable(PagedResult<Variable> source, Integer pageSize) {
        MetamacCriteriaResult<VariableBasicDto> target = new MetamacCriteriaResult<VariableBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<VariableBasicDto>(source.getValues().size()));
            for (Variable scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<VariableElementBasicDto> pageResultToMetamacCriteriaResultVariableElement(PagedResult<VariableElement> source, Integer pageSize) {
        MetamacCriteriaResult<VariableElementBasicDto> target = new MetamacCriteriaResult<VariableElementBasicDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<VariableElementBasicDto>(source.getValues().size()));
            for (VariableElement scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableElementDoToBasicDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> pageResultToMetamacCriteriaResultVariableElementRelatedResource(PagedResult<VariableElement> source, Integer pageSize) {
        MetamacCriteriaResult<RelatedResourceDto> target = new MetamacCriteriaResult<RelatedResourceDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<RelatedResourceDto>(source.getValues().size()));
            for (VariableElement scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableElementDoToRelatedResourceDto(scheme));
            }
        }
        return target;
    }

    //
    // TASKS
    //
    @Override
    public MetamacCriteriaResult<TaskDto> pageResultToMetamacCriteriaResultTask(PagedResult<Task> source, Integer pageSize) {
        MetamacCriteriaResult<TaskDto> target = new MetamacCriteriaResult<TaskDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<TaskDto>(source.getValues().size()));
            for (Task task : source.getValues()) {
                target.getResults().add(tasksDo2DtoMapper.taskToDto(task));
            }
        }
        return target;
    }
}
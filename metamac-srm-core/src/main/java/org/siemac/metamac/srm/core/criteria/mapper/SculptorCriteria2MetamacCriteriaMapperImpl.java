package org.siemac.metamac.srm.core.criteria.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapper;
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
import org.siemac.metamac.srm.core.code.mapper.CodesDo2DtoMapper;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.mapper.ConceptsDo2DtoMapper;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.mapper.DataStructureDefinitionDo2DtoMapper;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.mapper.OrganisationsDo2DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.importation.domain.Task;
import com.arte.statistic.sdmx.srm.core.importation.mapper.ImportationDo2DtoMapper;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.importation.TaskDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private DataStructureDefinitionDo2DtoMapper dataStructureDefinitionDo2DtoMapper;

    @Autowired
    private ImportationDo2DtoMapper             importationDo2DtoMapper;

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
    public MetamacCriteriaResult<DataStructureDefinitionMetamacDto> pageResultToMetamacCriteriaResultDataStructureDefinition(PagedResult<DataStructureDefinitionVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<DataStructureDefinitionMetamacDto> target = new MetamacCriteriaResult<DataStructureDefinitionMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<DataStructureDefinitionMetamacDto>(source.getValues().size()));
            for (DataStructureDefinitionVersionMetamac scheme : source.getValues()) {
                target.getResults().add(dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(scheme));
            }
        }
        return target;
    }

    //
    // CONCEPTS
    //

    @Override
    public MetamacCriteriaResult<ConceptSchemeMetamacDto> pageResultToMetamacCriteriaResultConceptSchemeVersion(PagedResult<ConceptSchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<ConceptSchemeMetamacDto> target = new MetamacCriteriaResult<ConceptSchemeMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ConceptSchemeMetamacDto>(source.getValues().size()));
            for (ConceptSchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(scheme));
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
    public MetamacCriteriaResult<ConceptMetamacDto> pageResultToMetamacCriteriaResultConcept(PagedResult<ConceptMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<ConceptMetamacDto> target = new MetamacCriteriaResult<ConceptMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ConceptMetamacDto>(source.getValues().size()));
            for (ConceptMetamac concept : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptMetamacDoToDto(concept));
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
    public MetamacCriteriaResult<OrganisationSchemeMetamacDto> pageResultToMetamacCriteriaResultOrganisationSchemeVersion(PagedResult<OrganisationSchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<OrganisationSchemeMetamacDto> target = new MetamacCriteriaResult<OrganisationSchemeMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<OrganisationSchemeMetamacDto>(source.getValues().size()));
            for (OrganisationSchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationSchemeMetamacDoToDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<OrganisationMetamacDto> pageResultToMetamacCriteriaResultOrganisation(PagedResult<OrganisationMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<OrganisationMetamacDto> target = new MetamacCriteriaResult<OrganisationMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<OrganisationMetamacDto>(source.getValues().size()));
            for (OrganisationMetamac organisation : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationMetamacDoToDto(organisation));
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
    public MetamacCriteriaResult<CategorySchemeMetamacDto> pageResultToMetamacCriteriaResultCategorySchemeVersion(PagedResult<CategorySchemeVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CategorySchemeMetamacDto> target = new MetamacCriteriaResult<CategorySchemeMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CategorySchemeMetamacDto>(source.getValues().size()));
            for (CategorySchemeVersionMetamac scheme : source.getValues()) {
                target.getResults().add(categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<CategoryMetamacDto> pageResultToMetamacCriteriaResultCategory(PagedResult<CategoryMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CategoryMetamacDto> target = new MetamacCriteriaResult<CategoryMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CategoryMetamacDto>(source.getValues().size()));
            for (CategoryMetamac category : source.getValues()) {
                target.getResults().add(categoriesDo2DtoMapper.categoryMetamacDoToDto(category));
            }
        }
        return target;
    }

    //
    // CODES
    //

    @Override
    public MetamacCriteriaResult<CodelistMetamacDto> pageResultToMetamacCriteriaResultCodelistVersion(PagedResult<CodelistVersionMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CodelistMetamacDto> target = new MetamacCriteriaResult<CodelistMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodelistMetamacDto>(source.getValues().size()));
            for (CodelistVersionMetamac scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codelistMetamacDoToDto(scheme));
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
    public MetamacCriteriaResult<CodeMetamacDto> pageResultToMetamacCriteriaResultCode(PagedResult<CodeMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CodeMetamacDto> target = new MetamacCriteriaResult<CodeMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodeMetamacDto>(source.getValues().size()));
            for (CodeMetamac code : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codeMetamacDoToDto(code));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<CodelistFamilyDto> pageResultToMetamacCriteriaResultCodelistFamily(PagedResult<CodelistFamily> source, Integer pageSize) {
        MetamacCriteriaResult<CodelistFamilyDto> target = new MetamacCriteriaResult<CodelistFamilyDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodelistFamilyDto>(source.getValues().size()));
            for (CodelistFamily scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codelistFamilyDoToDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<VariableFamilyDto> pageResultToMetamacCriteriaResultVariableFamily(PagedResult<VariableFamily> source, Integer pageSize) {
        MetamacCriteriaResult<VariableFamilyDto> target = new MetamacCriteriaResult<VariableFamilyDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<VariableFamilyDto>(source.getValues().size()));
            for (VariableFamily scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableFamilyDoToDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<VariableDto> pageResultToMetamacCriteriaResultVariable(PagedResult<Variable> source, Integer pageSize) {
        MetamacCriteriaResult<VariableDto> target = new MetamacCriteriaResult<VariableDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<VariableDto>(source.getValues().size()));
            for (Variable scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableDoToDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<VariableElementDto> pageResultToMetamacCriteriaResultVariableElement(PagedResult<VariableElement> source, Integer pageSize) {
        MetamacCriteriaResult<VariableElementDto> target = new MetamacCriteriaResult<VariableElementDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<VariableElementDto>(source.getValues().size()));
            for (VariableElement scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableElementDoToDto(scheme));
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
                target.getResults().add(importationDo2DtoMapper.taskToDto(task));
            }
        }
        return target;
    }
}
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
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
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

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

@Component
public class SculptorCriteria2MetamacCriteriaMapperImpl implements SculptorCriteria2MetamacCriteriaMapper {

    @Autowired
    private DataStructureDefinitionDo2DtoMapper dataStructureDefinitionDo2DtoMapper;

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
            target.setResults(new ArrayList<DataStructureDefinitionMetamacDto>());
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
            target.setResults(new ArrayList<ConceptSchemeMetamacDto>());
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
            target.setResults(new ArrayList<RelatedResourceDto>());
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
            target.setResults(new ArrayList<ConceptMetamacDto>());
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
            target.setResults(new ArrayList<RelatedResourceDto>());
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
            target.setResults(new ArrayList<OrganisationSchemeMetamacDto>());
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
            target.setResults(new ArrayList<OrganisationMetamacDto>());
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
            target.setResults(new ArrayList<RelatedResourceDto>());
            for (OrganisationMetamac scheme : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationMetamacDoToRelatedResourceDto(scheme));
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
            target.setResults(new ArrayList<CategorySchemeMetamacDto>());
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
            target.setResults(new ArrayList<CategoryMetamacDto>());
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
            target.setResults(new ArrayList<CodelistMetamacDto>());
            for (CodelistVersionMetamac scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.codelistMetamacDoToDto(scheme));
            }
        }
        return target;
    }

    @Override
    public MetamacCriteriaResult<CodeMetamacDto> pageResultToMetamacCriteriaResultCode(PagedResult<CodeMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<CodeMetamacDto> target = new MetamacCriteriaResult<CodeMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<CodeMetamacDto>());
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
            target.setResults(new ArrayList<CodelistFamilyDto>());
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
            target.setResults(new ArrayList<VariableFamilyDto>());
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
            target.setResults(new ArrayList<VariableDto>());
            for (Variable scheme : source.getValues()) {
                target.getResults().add(codesDo2DtoMapper.variableDoToDto(scheme));
            }
        }
        return target;
    }

}
package org.siemac.metamac.srm.core.criteria.mapper;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.mapper.SculptorCriteria2MetamacCriteria;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.mapper.CategoriesDo2DtoMapper;
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
    public MetamacCriteriaResult<ConceptMetamacDto> pageResultToMetamacCriteriaResultConcept(PagedResult<ConceptMetamac> source, Integer pageSize) {
        MetamacCriteriaResult<ConceptMetamacDto> target = new MetamacCriteriaResult<ConceptMetamacDto>();
        target.setPaginatorResult(SculptorCriteria2MetamacCriteria.sculptorResultToMetamacCriteriaResult(source, pageSize));
        if (source.getValues() != null) {
            target.setResults(new ArrayList<ConceptMetamacDto>());
            for (ConceptMetamac scheme : source.getValues()) {
                target.getResults().add(conceptsDo2DtoMapper.conceptMetamacDoToDto(scheme));
            }
        }
        return target;
    }

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
            for (OrganisationMetamac scheme : source.getValues()) {
                target.getResults().add(organisationsDo2DtoMapper.organisationMetamacDoToDto(scheme));
            }
        }
        return target;
    }

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
}
package org.siemac.metamac.srm.core.concept.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.code.mapper.CodesDo2DtoMapper;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("conceptsDo2DtoMapper")
public class ConceptsDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements ConceptsDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2DtoMapper do2DtoMapperSdmxSrm;

    @Autowired
    private CodesDo2DtoMapper                                                    codesDo2DtoMapper;

    @Override
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        ConceptSchemeMetamacDto target = new ConceptSchemeMetamacDto();

        target.setType(source.getType());
        target.setRelatedOperation(externalItemStatisticalOperationsToExternalItemDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getRelatedOperation()));
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));

        do2DtoMapperSdmxSrm.conceptSchemeDoToDto(source, target);
        return target;
    }

    @Override
    public ConceptSchemeMetamacBasicDto conceptSchemeMetamacDoToBasicDto(ConceptSchemeVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        ConceptSchemeMetamacBasicDto target = new ConceptSchemeMetamacBasicDto();
        itemSchemeVersionDoToItemSchemeBasicDto(source, source.getLifeCycleMetadata(), target);
        target.setType(source.getType());
        target.setRelatedOperation(externalItemStatisticalOperationsToExternalItemDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getRelatedOperation()));
        return target;
    }

    @Override
    public RelatedResourceDto conceptSchemeMetamacDoToRelatedResourceDto(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.conceptSchemeDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public List<ConceptSchemeMetamacBasicDto> conceptSchemeMetamacDoListToDtoList(List<ConceptSchemeVersionMetamac> conceptSchemeVersions) throws MetamacException {
        List<ConceptSchemeMetamacBasicDto> conceptSchemeMetamacDtos = new ArrayList<ConceptSchemeMetamacBasicDto>(conceptSchemeVersions.size());
        for (ConceptSchemeVersionMetamac conceptSchemeVersion : conceptSchemeVersions) {
            conceptSchemeMetamacDtos.add(conceptSchemeMetamacDoToBasicDto(conceptSchemeVersion));
        }
        return conceptSchemeMetamacDtos;
    }

    @Override
    public ConceptMetamacDto conceptMetamacDoToDto(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptMetamacDto target = new ConceptMetamacDto();

        target.setPluralName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getPluralName()));
        target.setAcronym(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getAcronym()));
        target.setDescriptionSource(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDescriptionSource()));
        target.setContext(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getContext()));
        target.setDocMethod(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDocMethod()));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setConceptType(conceptTypeDoToDto(source.getConceptType()));
        target.setDerivation(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDerivation()));
        target.setLegalActs(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getLegalActs()));
        target.setConceptExtends(conceptMetamacDoToRelatedResourceDto(source.getConceptExtends()));
        target.setVariable(codesDo2DtoMapper.variableDoToRelatedResourceDto(source.getVariable()));
        do2DtoMapperSdmxSrm.conceptDoToDto(source, target);

        // note: not conversion to relatedConcepts and roles. Call specific operations in Service

        return target;
    }

    @Override
    public ConceptMetamacBasicDto conceptMetamacDoToBasicDto(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptMetamacBasicDto target = new ConceptMetamacBasicDto();
        target.setAcronym(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getAcronym()));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setVariable(codesDo2DtoMapper.variableDoToRelatedResourceDto(source.getVariable()));

        itemDoToItemBasicDto(source, target);
        return target;
    }

    @Override
    public RelatedResourceDto conceptMetamacDoToRelatedResourceDto(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.conceptDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public List<ConceptMetamacBasicDto> conceptMetamacDoListToDtoList(List<ConceptMetamac> concepts) {
        List<ConceptMetamacBasicDto> conceptMetamacDtos = new ArrayList<ConceptMetamacBasicDto>(concepts.size());
        for (ConceptMetamac concept : concepts) {
            conceptMetamacDtos.add(conceptMetamacDoToBasicDto(concept));
        }
        return conceptMetamacDtos;
    }

    @Override
    public ConceptTypeDto conceptTypeDoToDto(ConceptType source) {
        if (source == null) {
            return null;
        }
        ConceptTypeDto target = new ConceptTypeDto();
        target.setIdentifier(source.getIdentifier());
        target.setDescription(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDescription()));
        return target;
    }

    @Override
    public List<ConceptTypeDto> conceptTypeDoListToConceptTypeDtoList(List<ConceptType> sources) {
        List<ConceptTypeDto> targets = new ArrayList<ConceptTypeDto>(sources.size());
        for (ConceptType source : sources) {
            targets.add(conceptTypeDoToDto(source));
        }
        return targets;
    }
}
package org.siemac.metamac.srm.core.concept.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("conceptsDo2DtoMapper")
public class ConceptsDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements ConceptsDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptSchemeMetamacDto target = new ConceptSchemeMetamacDto();

        target.setType(source.getType());
        target.setRelatedOperation(do2DtoMapperSdmxSrm.externalItemToExternalItemDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getRelatedOperation()));
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));

        do2DtoMapperSdmxSrm.conceptSchemeDoToDto(source, target);
        return target;
    }

    @Override
    public List<ConceptSchemeMetamacDto> conceptSchemeMetamacDoListToDtoList(List<ConceptSchemeVersionMetamac> conceptSchemeVersions) {
        List<ConceptSchemeMetamacDto> conceptSchemeMetamacDtos = new ArrayList<ConceptSchemeMetamacDto>();
        for (ConceptSchemeVersionMetamac conceptSchemeVersion : conceptSchemeVersions) {
            conceptSchemeMetamacDtos.add(conceptSchemeMetamacDoToDto(conceptSchemeVersion));
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
        target.setType(conceptTypeDoToDto(source.getType()));
        target.setDerivation(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDerivation()));
        target.setLegalActs(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getLegalActs()));
        if (source.getConceptExtends() != null) {
            target.setConceptExtendsUrn(source.getConceptExtends().getNameableArtefact().getUrn());
        }
        do2DtoMapperSdmxSrm.conceptDoToDto(source, target);

        // note: not conversion to relatedConcepts and roles. Call 'retrieveRelatedConcepts' operation of Service

        return target;
    }

    @Override
    public List<ConceptMetamacDto> conceptMetamacDoListToDtoList(List<ConceptMetamac> sources) {
        List<ConceptMetamacDto> targets = new ArrayList<ConceptMetamacDto>();
        for (ConceptMetamac source : sources) {
            targets.add(conceptMetamacDoToDto(source));
        }
        return targets;
    }

    @Override
    public List<ItemHierarchyDto> conceptMetamacDoListToItemHierarchyDtoList(List<ConceptMetamac> sources) {
        List<ItemHierarchyDto> targets = new ArrayList<ItemHierarchyDto>();
        for (ConceptMetamac source : sources) {
            ItemHierarchyDto target = conceptMetamacDoToItemHierarchyDto(source);
            targets.add(target);
        }
        return targets;
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
        List<ConceptTypeDto> targets = new ArrayList<ConceptTypeDto>();
        for (ConceptType source : sources) {
            targets.add(conceptTypeDoToDto(source));
        }
        return targets;
    }

    private ItemHierarchyDto conceptMetamacDoToItemHierarchyDto(ConceptMetamac conceptMetamac) {
        ItemHierarchyDto itemHierarchyDto = new ItemHierarchyDto();

        // Concept
        ConceptMetamacDto conceptMetamacDto = conceptMetamacDoToDto(conceptMetamac);
        itemHierarchyDto.setItem(conceptMetamacDto);

        // Children
        for (Item item : conceptMetamac.getChildren()) {
            ItemHierarchyDto itemHierarchyChildrenDto = conceptMetamacDoToItemHierarchyDto((ConceptMetamac) item);
            itemHierarchyDto.addChildren(itemHierarchyChildrenDto);
        }

        return itemHierarchyDto;
    }
}
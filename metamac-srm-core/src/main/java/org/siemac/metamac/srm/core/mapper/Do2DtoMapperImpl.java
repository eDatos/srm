package org.siemac.metamac.srm.core.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("do2DtoMapper")
public class Do2DtoMapperImpl implements Do2DtoMapper {

    // ------------------------------------------------------------
    // DSDs
    // ------------------------------------------------------------

    @Autowired
    @Qualifier("do2DtoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.mapper.Do2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component) {
        return (U) do2DtoMapperSdmxSrm.componentToComponentDto(typeDozerCopyMode, component);
    }

    @Override
    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList) {
        return (U) do2DtoMapperSdmxSrm.componentListToComponentListDto(typeDozerCopyMode, componentList);
    }

    @Override
    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }

        DataStructureDefinitionMetamacDto target = dataStructureDefinitionVersionMetamacDoToDto(source, typeDozerCopyMode);

        return target;
    }

    @Override
    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }

        DataStructureDefinitionMetamacDto target = dataStructureDefinitionVersionMetamacDoToDto(source, TypeDozerCopyMode.COPY_ALL_METADATA);

        return target;
    }

    private DataStructureDefinitionMetamacDto dataStructureDefinitionVersionMetamacDoToDto(DataStructureDefinitionVersionMetamac source, TypeDozerCopyMode typeDozerCopyMode) {
        DataStructureDefinitionMetamacDto target = new DataStructureDefinitionMetamacDto();
        target.setProcStatus(source.getLifecycleMetadata().getProcStatus());
        target.setProductionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getProductionValidationDate()));
        target.setProductionValidationUser(source.getLifecycleMetadata().getProductionValidationUser());
        target.setDiffusionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getLifecycleMetadata().getDiffusionValidationUser());
        target.setInternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getLifecycleMetadata().getInternalPublicationUser());
        target.setExternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getLifecycleMetadata().getExternalPublicationUser());

        do2DtoMapperSdmxSrm.dataStructureDefinitionDoToDto(typeDozerCopyMode, source, target);
        return target;
    }

    @Override
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion) {
        return do2DtoMapperSdmxSrm.dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinitionVersion);
    }

    @Override
    public List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDoListToDtoList(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacs) {
        List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos = new ArrayList<DataStructureDefinitionMetamacDto>();
        for (DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac : dataStructureDefinitionVersionMetamacs) {
            dataStructureDefinitionMetamacDtos.add(dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac));
        }
        return dataStructureDefinitionMetamacDtos;
    }

    // ------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------

    @Override
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptSchemeMetamacDto target = new ConceptSchemeMetamacDto();
        
        // TODO lifecycle conversion a común. tb el del dsd
        target.setProcStatus(source.getLifecycleMetadata().getProcStatus());
        target.setType(source.getType());
        target.setRelatedOperation(do2DtoMapperSdmxSrm.externalItemToExternalItemDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getRelatedOperation()));
        target.setProductionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getProductionValidationDate()));
        target.setProductionValidationUser(source.getLifecycleMetadata().getProductionValidationUser());
        target.setDiffusionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getLifecycleMetadata().getDiffusionValidationUser());
        target.setInternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getLifecycleMetadata().getInternalPublicationUser());
        target.setExternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getLifecycleMetadata().getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getLifecycleMetadata().getExternalPublicationUser());

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

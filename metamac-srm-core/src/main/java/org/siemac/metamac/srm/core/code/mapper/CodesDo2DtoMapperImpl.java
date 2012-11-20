package org.siemac.metamac.srm.core.code.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("codesDo2DtoMapper")
public class CodesDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements CodesDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public CodelistMetamacDto codelistMetamacDoToDto(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CodelistMetamacDto target = new CodelistMetamacDto();

        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());

        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));

        do2DtoMapperSdmxSrm.codelistDoToDto(source, target);
        return target;
    }

    @Override
    public List<CodelistMetamacDto> codelistMetamacDoListToDtoList(List<CodelistVersionMetamac> codelistVersions) {
        List<CodelistMetamacDto> codelistMetamacDtos = new ArrayList<CodelistMetamacDto>();
        for (CodelistVersionMetamac codelistVersion : codelistVersions) {
            codelistMetamacDtos.add(codelistMetamacDoToDto(codelistVersion));
        }
        return codelistMetamacDtos;
    }

    @Override
    public CodeMetamacDto codeMetamacDoToDto(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        CodeMetamacDto target = new CodeMetamacDto();
        do2DtoMapperSdmxSrm.codeDoToDto(source, target);

        return target;
    }

    @Override
    public List<CodeMetamacDto> codeMetamacDoListToDtoList(List<CodeMetamac> sources) {
        List<CodeMetamacDto> targets = new ArrayList<CodeMetamacDto>();
        for (CodeMetamac source : sources) {
            targets.add(codeMetamacDoToDto(source));
        }
        return targets;
    }

    @Override
    public List<ItemHierarchyDto> codeMetamacDoListToItemHierarchyDtoList(List<CodeMetamac> sources) {
        List<ItemHierarchyDto> targets = new ArrayList<ItemHierarchyDto>();
        for (CodeMetamac source : sources) {
            ItemHierarchyDto target = codeMetamacDoToItemHierarchyDto(source);
            targets.add(target);
        }
        return targets;
    }

    private ItemHierarchyDto codeMetamacDoToItemHierarchyDto(CodeMetamac codeMetamac) {
        ItemHierarchyDto itemHierarchyDto = new ItemHierarchyDto();

        // Code
        CodeMetamacDto codeMetamacDto = codeMetamacDoToDto(codeMetamac);
        itemHierarchyDto.setItem(codeMetamacDto);

        // Children
        for (Item item : codeMetamac.getChildren()) {
            ItemHierarchyDto itemHierarchyChildrenDto = codeMetamacDoToItemHierarchyDto((CodeMetamac) item);
            itemHierarchyDto.addChildren(itemHierarchyChildrenDto);
        }

        return itemHierarchyDto;
    }
}
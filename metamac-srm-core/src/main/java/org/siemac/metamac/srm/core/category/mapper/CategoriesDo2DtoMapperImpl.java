package org.siemac.metamac.srm.core.category.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("categoriesDo2DtoMapper")
public class CategoriesDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements CategoriesDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public CategorySchemeMetamacDto categorySchemeMetamacDoToDto(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CategorySchemeMetamacDto target = new CategorySchemeMetamacDto();
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));

        do2DtoMapperSdmxSrm.categorySchemeDoToDto(source, target);
        return target;
    }

    @Override
    public List<CategorySchemeMetamacDto> categorySchemeMetamacDoListToDtoList(List<CategorySchemeVersionMetamac> categorySchemeVersions) {
        List<CategorySchemeMetamacDto> categorySchemeMetamacDtos = new ArrayList<CategorySchemeMetamacDto>();
        for (CategorySchemeVersionMetamac categorySchemeVersion : categorySchemeVersions) {
            categorySchemeMetamacDtos.add(categorySchemeMetamacDoToDto(categorySchemeVersion));
        }
        return categorySchemeMetamacDtos;
    }

//    @Override
//    public CategoryMetamacDto categoryMetamacDoToDto(CategoryMetamac source) {
//        if (source == null) {
//            return null;
//        }
//        CategoryMetamacDto target = new CategoryMetamacDto();
//
//        do2DtoMapperSdmxSrm.categoryDoToDto(source, target);
//        return target;
//    }
//
//    @Override
//    public List<CategoryMetamacDto> categoryMetamacDoListToDtoList(List<CategoryMetamac> sources) {
//        List<CategoryMetamacDto> targets = new ArrayList<CategoryMetamacDto>();
//        for (CategoryMetamac source : sources) {
//            targets.add(categoryMetamacDoToDto(source));
//        }
//        return targets;
//    }
//
//    @Override
//    public List<ItemHierarchyDto> categoryMetamacDoListToItemHierarchyDtoList(List<CategoryMetamac> sources) {
//        List<ItemHierarchyDto> targets = new ArrayList<ItemHierarchyDto>();
//        for (CategoryMetamac source : sources) {
//            ItemHierarchyDto target = categoryMetamacDoToItemHierarchyDto(source);
//            targets.add(target);
//        }
//        return targets;
//    }
//
//    private ItemHierarchyDto categoryMetamacDoToItemHierarchyDto(CategoryMetamac categoryMetamac) {
//        ItemHierarchyDto itemHierarchyDto = new ItemHierarchyDto();
//
//        // Category
//        CategoryMetamacDto categoryMetamacDto = categoryMetamacDoToDto(categoryMetamac);
//        itemHierarchyDto.setItem(categoryMetamacDto);
//
//        // Children
//        for (Item item : categoryMetamac.getChildren()) {
//            ItemHierarchyDto itemHierarchyChildrenDto = categoryMetamacDoToItemHierarchyDto((CategoryMetamac) item);
//            itemHierarchyDto.addChildren(itemHierarchyChildrenDto);
//        }
//
//        return itemHierarchyDto;
//    }
}
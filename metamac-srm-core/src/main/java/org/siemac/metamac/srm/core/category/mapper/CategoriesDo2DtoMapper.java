package org.siemac.metamac.srm.core.category.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

public interface CategoriesDo2DtoMapper {

    // Category schemes
    public CategorySchemeMetamacDto categorySchemeMetamacDoToDto(CategorySchemeVersionMetamac source);
    public List<CategorySchemeMetamacDto> categorySchemeMetamacDoListToDtoList(List<CategorySchemeVersionMetamac> sources);

    // Categories
    public CategoryMetamacDto categoryMetamacDoToDto(CategoryMetamac source);
    // public List<CategoryMetamacDto> categoryMetamacDoListToDtoList(List<CategoryMetamac> sources);
    // public List<ItemHierarchyDto> categoryMetamacDoListToItemHierarchyDtoList(List<CategoryMetamac> sources);
}
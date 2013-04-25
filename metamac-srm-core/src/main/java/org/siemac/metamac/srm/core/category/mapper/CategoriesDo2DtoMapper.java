package org.siemac.metamac.srm.core.category.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;

public interface CategoriesDo2DtoMapper {

    // Category schemes
    public CategorySchemeMetamacDto categorySchemeMetamacDoToDto(CategorySchemeVersionMetamac source);
    public CategorySchemeMetamacBasicDto categorySchemeMetamacDoToBasicDto(CategorySchemeVersionMetamac source);
    public List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDoListToDtoList(List<CategorySchemeVersionMetamac> sources);

    // Categories
    public CategoryMetamacDto categoryMetamacDoToDto(CategoryMetamac source);
    public CategoryMetamacBasicDto categoryMetamacDoToBasicDto(CategoryMetamac source);
    public CategorisationDto categorisationDoToDto(Categorisation source);
    public List<CategorisationDto> categorisationDoListToDtoList(List<Categorisation> sources);
}
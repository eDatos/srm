package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CategoriesMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // -----------------------------------------------------------------------------------

    public static CategorySchemeMetamacDto mockCategorySchemeDto(String codeMaintainer, String urnMaintainer) {
        CategorySchemeMetamacDto categorySchemeDto = new CategorySchemeMetamacDto();
        CategoriesDtoMocks.mockCategorySchemeDto(categorySchemeDto);
        categorySchemeDto.setMaintainer(new RelatedResourceDto(codeMaintainer, urnMaintainer, TypeExternalArtefactsEnum.AGENCY));

        return categorySchemeDto;
    }

    // -----------------------------------------------------------------------------------
    // CATEGORIES
    // -----------------------------------------------------------------------------------

    public static CategoryMetamacDto mockCategoryDto() {
        CategoryMetamacDto categoryMetamacDto = new CategoryMetamacDto();
        CategoriesDtoMocks.mockCategoryDto(categoryMetamacDto);
        return categoryMetamacDto;
    }
}

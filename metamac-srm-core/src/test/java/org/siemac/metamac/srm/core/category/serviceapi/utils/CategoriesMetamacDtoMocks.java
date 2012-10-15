package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDtoMocks;

public class CategoriesMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // -----------------------------------------------------------------------------------

    public static CategorySchemeMetamacDto mockCategorySchemeDto() {
        CategorySchemeMetamacDto categorySchemeDto = new CategorySchemeMetamacDto();
        CategoriesDtoMocks.mockCategorySchemeDto(categorySchemeDto);
        categorySchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintainer", TypeExternalArtefactsEnum.AGENCY));

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

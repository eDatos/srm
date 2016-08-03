package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDtoMocks;

public class CategoriesMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // -----------------------------------------------------------------------------------

    public static CategorySchemeMetamacDto mockCategorySchemeDto(String codeMaintainer, String urnMaintainer) {
        CategorySchemeMetamacDto categorySchemeDto = new CategorySchemeMetamacDto();
        CategoriesDtoMocks.mockCategorySchemeDto(categorySchemeDto);
        categorySchemeDto.setMaintainer(OrganisationsMetamacDtoMocks.mockMaintainerDto(codeMaintainer, urnMaintainer));

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

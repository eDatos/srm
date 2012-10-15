package org.siemac.metamac.srm.core.category.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

public interface CategoriesDto2DoMapper {

    public CategorySchemeVersionMetamac categorySchemeDtoToDo(CategorySchemeMetamacDto source) throws MetamacException;
    // TODO categories
    // public CategoryMetamac categoryDtoToDo(CategoryMetamacDto source) throws MetamacException;
}
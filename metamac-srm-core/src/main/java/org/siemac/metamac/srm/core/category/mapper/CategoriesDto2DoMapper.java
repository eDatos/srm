package org.siemac.metamac.srm.core.category.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

public interface CategoriesDto2DoMapper {

    public CategorySchemeVersionMetamac categorySchemeMetamacDtoToDo(CategorySchemeMetamacDto source) throws MetamacException;
    public CategoryMetamac categoryMetamacDtoToDo(CategoryMetamacDto source) throws MetamacException;
}
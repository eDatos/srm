package org.siemac.metamac.srm.core.category.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.exception.CategorySchemeVersionMetamacNotFoundException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("categoriesDto2DoMapper")
public class CategoriesDto2DoMapperImpl implements CategoriesDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private CategorySchemeVersionMetamacRepository                                  categorySchemeVersionMetamacRepository;

    // @Autowired
    // private CategoryMetamacRepository categoryMetamacRepository;

    // ------------------------------------------------------------
    // CATEGORIES
    // ------------------------------------------------------------

    @Override
    public CategorySchemeVersionMetamac categorySchemeMetamacDtoToDo(CategorySchemeMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CategorySchemeVersionMetamac target = null;
        if (source.getId() == null) {
            target = new CategorySchemeVersionMetamac();
        } else {
            try {
                target = categorySchemeVersionMetamacRepository.findById(source.getId());
            } catch (CategorySchemeVersionMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.CATEGORY_SCHEME)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // Modifiable attributes
        // nothing

        // sdmx
        dto2DoMapperSdmxSrm.categorySchemeDtoToDo(source, target);

        return target;
    }

    // @Override
    // public CategoryMetamac categoryDtoToDo(CategoryMetamacDto source) throws MetamacException {
    // if (source == null) {
    // return null;
    // }
    //
    // // If exists, retrieves existing entity. Otherwise, creates new entity.
    // CategoryMetamac target = null;
    // if (source.getId() == null) {
    // target = new CategoryMetamac();
    // } else {
    // try {
    // target = categoryMetamacRepository.findById(source.getId());
    // } catch (CategoryMetamacNotFoundException e) {
    // throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.CATEGORY)
    // .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
    // }
    // OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
    // }
    //
    // // Modifiable attributes: nothing
    //
    // // Sdmx
    // dto2DoMapperSdmxSrm.categoryDtoToDo(source, target);
    //
    // return target;
    // }
}
package org.siemac.metamac.srm.core.category.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("categoriesDto2DoMapper")
public class CategoriesDto2DoMapperImpl extends BaseDto2DoMapperImpl implements CategoriesDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private CategorySchemeVersionMetamacRepository                                  categorySchemeVersionMetamacRepository;

    @Autowired
    private CategoryMetamacRepository                                               categoryMetamacRepository;

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
        if (source.getUrn() == null) {
            target = new CategorySchemeVersionMetamac();
        } else {
            target = categorySchemeVersionMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        // nothing

        // sdmx
        dto2DoMapperSdmxSrm.categorySchemeDtoToDo(source, target);

        return target;
    }

    @Override
    public CategoryMetamac categoryMetamacDtoToDo(CategoryMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CategoryMetamac target = null;
        if (source.getUrn() == null) {
            target = new CategoryMetamac();
        } else {
            target = categoryMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes: nothing

        // Sdmx
        dto2DoMapperSdmxSrm.categoryDtoToDo(source, target);

        return target;
    }
}
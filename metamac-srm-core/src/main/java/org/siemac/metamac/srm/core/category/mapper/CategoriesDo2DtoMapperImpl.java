package org.siemac.metamac.srm.core.category.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;

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
    public CategorySchemeMetamacBasicDto categorySchemeMetamacDoToBasicDto(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CategorySchemeMetamacBasicDto target = new CategorySchemeMetamacBasicDto();
        itemSchemeVersionDoToItemSchemeBasicDto(source, source.getLifeCycleMetadata(), target);
        return target;
    }

    @Override
    public List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDoListToDtoList(List<CategorySchemeVersionMetamac> categorySchemeVersions) {
        List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos = new ArrayList<CategorySchemeMetamacBasicDto>(categorySchemeVersions.size());
        for (CategorySchemeVersionMetamac categorySchemeVersion : categorySchemeVersions) {
            categorySchemeMetamacDtos.add(categorySchemeMetamacDoToBasicDto(categorySchemeVersion));
        }
        return categorySchemeMetamacDtos;
    }

    @Override
    public CategoryMetamacDto categoryMetamacDoToDto(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        CategoryMetamacDto target = new CategoryMetamacDto();

        do2DtoMapperSdmxSrm.categoryDoToDto(source, target);
        return target;
    }

    @Override
    public CategoryMetamacBasicDto categoryMetamacDoToBasicDto(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        CategoryMetamacBasicDto target = new CategoryMetamacBasicDto();
        itemDoToItemBasicDto(source, target);
        return target;
    }

    @Override
    public CategorisationDto categorisationDoToDto(Categorisation source) {
        return do2DtoMapperSdmxSrm.categorisationDoToDto(source);
    }

    @Override
    public List<CategorisationDto> categorisationDoListToDtoList(List<Categorisation> sources) {
        return do2DtoMapperSdmxSrm.categorisationsDoToDto(sources);
    }
}
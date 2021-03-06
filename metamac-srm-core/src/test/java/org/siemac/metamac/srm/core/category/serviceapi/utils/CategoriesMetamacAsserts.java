package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesAsserts;

public class CategoriesMetamacAsserts extends CategoriesAsserts {

    public static void assertEqualsCategoryScheme(CategorySchemeVersionMetamac expected, CategorySchemeVersionMetamac actual) {
        BaseAsserts.assertEqualsLifeCycle(expected.getLifeCycleMetadata(), actual.getLifeCycleMetadata());
        assertEqualsCategorySchemeWithoutLifeCycleMetadata(expected, actual);
    }

    public static void assertEqualsCategorySchemeWithoutLifeCycleMetadata(CategorySchemeVersionMetamac expected, CategorySchemeVersionMetamac actual) {
        // Metamac

        // Sdmx
        CategoriesAsserts.assertEqualsCategoryScheme(expected, actual);
    }

    public static void assertEqualsCategorySchemeMetamacDto(CategorySchemeMetamacDto expected, CategorySchemeMetamacDto actual) {

        // Metamac

        // Sdmx
        CategoriesAsserts.assertEqualsCategorySchemeDto(expected, actual);
    }

    public static void assertEqualsCategoryScheme(CategorySchemeVersionMetamac expected, CategorySchemeMetamacDto actual) {
        assertEqualsCategoryScheme(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCategoryScheme(CategorySchemeMetamacDto expected, CategorySchemeVersionMetamac actual) {
        assertEqualsCategoryScheme(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsCategoryScheme(CategorySchemeVersionMetamac expected, CategorySchemeMetamacBasicDto actual) {
        BaseAsserts.assertEqualsItemSchemeBasicDto(expected, expected.getLifeCycleMetadata(), actual);
    }

    public static void assertEqualsCategory(CategoryMetamac expected, CategoryMetamac actual) {

        // Metamac

        // Sdmx
        CategoriesAsserts.assertEqualsCategory(expected, actual);
    }

    public static void assertEqualsCategoryDto(CategoryMetamacDto expected, CategoryMetamacDto actual) {

        // Metamac

        // Sdmx
        CategoriesAsserts.assertEqualsCategoryDto(expected, actual);
    }

    public static void assertEqualsCategory(CategoryMetamac expected, CategoryMetamacDto actual) {
        assertEqualsCategory(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsCategory(CategoryMetamacDto expected, CategoryMetamac actual) {
        assertEqualsCategory(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsCategory(CategoryMetamac expected, CategoryMetamacBasicDto actual) {
        BaseAsserts.assertEqualsItemBasicDto(expected, actual);
    }

    private static void assertEqualsCategoryScheme(CategorySchemeVersionMetamac entity, CategorySchemeMetamacDto dto, MapperEnum mapperEnum) {

        // Metamac

        if (MapperEnum.DO2DTO.equals(mapperEnum)) {
            // generated by service
            BaseAsserts.assertEqualsLifeCycle(entity.getLifeCycleMetadata(), dto.getLifeCycle());
        }

        // Sdmx
        CategoriesAsserts.assertEqualsCategoryScheme(entity, dto, mapperEnum);
    }

    private static void assertEqualsCategory(CategoryMetamac entity, CategoryMetamacDto dto, MapperEnum mapperEnum) {

        // Metamac

        // Sdmx
        CategoriesAsserts.assertEqualsCategory(entity, dto, mapperEnum);
    }
}

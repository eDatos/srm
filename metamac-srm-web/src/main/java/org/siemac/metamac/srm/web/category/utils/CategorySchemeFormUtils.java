package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class CategorySchemeFormUtils {

    // CODE

    public static FormItemIfFunction getCodeFormItemIfFunction(final CategorySchemeMetamacDto categorySchemeDto) {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return canCategorySchemeCodeBeEdited(categorySchemeDto);
            }
        };
    }

    public static FormItemIfFunction getStaticCodeFormItemIfFunction(final CategorySchemeMetamacDto categorySchemeDto) {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !canCategorySchemeCodeBeEdited(categorySchemeDto);
            }
        };
    }

    private static boolean canCategorySchemeCodeBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(categorySchemeDto.getLifeCycle().getProcStatus(), categorySchemeDto.getVersionLogic())
                && CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer());
    }

    // NAME

    public static FormItemIfFunction getNameFormItemIfFunction(final CategorySchemeMetamacDto categorySchemeDto) {
        return CommonUtils.getEditableSDMXMetadataFormitemIfFunction(categorySchemeDto.getMaintainer());
    }

    public static FormItemIfFunction getStaticNameFormItemIfFunction(final CategorySchemeMetamacDto categorySchemeDto) {
        return CommonUtils.getNonEditableSDMXMetadataFormitemIfFunction(categorySchemeDto.getMaintainer());
    }

    // DESCRIPTION

    public static FormItemIfFunction getDescriptionFormItemIfFunction(final CategorySchemeMetamacDto categorySchemeDto) {
        return CommonUtils.getEditableSDMXMetadataFormitemIfFunction(categorySchemeDto.getMaintainer());
    }

    public static FormItemIfFunction getStaticDescriptionFormItemIfFunction(final CategorySchemeMetamacDto categorySchemeDto) {
        return CommonUtils.getNonEditableSDMXMetadataFormitemIfFunction(categorySchemeDto.getMaintainer());
    }
}

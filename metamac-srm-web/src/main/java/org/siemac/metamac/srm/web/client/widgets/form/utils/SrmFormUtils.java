package org.siemac.metamac.srm.web.client.widgets.form.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.form.utils.FormUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.DynamicForm;

public class SrmFormUtils extends FormUtils {

    public static List<RelatedResourceDto> getValueAsRelatedResourcesList(DynamicForm form, String name) {
        return getValueAsRelatedResourcesList(form, name, new ArrayList<RelatedResourceDto>());
    }

    public static List<RelatedResourceDto> getValueAsRelatedResourcesList(Object value) {
        return getValueAsRelatedResourcesList(value, new ArrayList<RelatedResourceDto>());
    }
}

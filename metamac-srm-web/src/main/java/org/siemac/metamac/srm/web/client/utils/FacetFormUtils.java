package org.siemac.metamac.srm.web.client.utils;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.representation.widgets.BaseFacetForm;
import org.siemac.metamac.srm.web.client.representation.widgets.StaticFacetForm;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.RepresentationConstraintSharedValidator;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.MaintainableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class FacetFormUtils {

    public static final String            TEXT_TYPE_FIELD_NAME = "repr-no-enum-text-type";

    public static Set<FacetValueTypeEnum> dsdFacetValueTypeEnums;
    public static Set<FacetValueTypeEnum> conceptFacetValueTypeEnums;

    /**
     * Shows or hides facetForm and staticFacetForm depending on whether representationType is TEXT_FORMAT or not, and on the maintainer of the resource.
     */
    public static void setFacetFormVisibility(BaseFacetForm facetForm, StaticFacetForm staticFacetForm, String representationType, MaintainableArtefactDto maintainableArtefactDto) {
        facetForm.hide();
        staticFacetForm.hide();
        if (StringUtils.equals(representationType, RepresentationTypeEnum.TEXT_FORMAT.name())) {
            if (CommonUtils.canSdmxMetadataAndStructureBeModified(maintainableArtefactDto)) {
                facetForm.show();
            } else {
                staticFacetForm.show();
            }
        }
    }

    /**
     * Returns the valid FacetValueTypeEnums for DSD
     * 
     * @return
     */
    public static Set<FacetValueTypeEnum> getDsdFacetValueTypeEnums() {
        // SimpleDataType values
        if (dsdFacetValueTypeEnums == null) {
            dsdFacetValueTypeEnums = new HashSet<FacetValueTypeEnum>();
            for (FacetValueTypeEnum f : RepresentationConstraintSharedValidator.simpleDataTypeValues) {
                dsdFacetValueTypeEnums.add(f);
            }
        }
        return dsdFacetValueTypeEnums;
    }

    /**
     * Returns the valid FacetValueTypeEnums for Concepts
     * 
     * @return
     */
    public static Set<FacetValueTypeEnum> getConceptFacetValueTypeEnums() {
        // BasicComponentDataType values
        if (conceptFacetValueTypeEnums == null) {
            conceptFacetValueTypeEnums = new HashSet<FacetValueTypeEnum>();
            for (FacetValueTypeEnum f : RepresentationConstraintSharedValidator.basicComponentDataTypeValues) {
                conceptFacetValueTypeEnums.add(f);
            }
        }
        return conceptFacetValueTypeEnums;
    }

    /**
     * True if {@link FacetValueTypeEnum} f represents time
     * 
     * @param f
     * @return
     */
    public static boolean representsTime(FacetValueTypeEnum f) {
        return RepresentationConstraintSharedValidator.isTimeDataType(f);
    }

    //
    // FormItemIfFunction for FacetTypes
    //

    /**
     * Is sequence
     * 
     * @return
     */
    public static FormItemIfFunction getIsSequenceIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Interval
     * 
     * @return
     */
    public static FormItemIfFunction getIntervalIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Start value
     * 
     * @return
     */
    public static FormItemIfFunction getStartValueIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * End value
     * 
     * @return
     */
    public static FormItemIfFunction getEndValueIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Time interval
     * 
     * @return
     */
    public static FormItemIfFunction getTimeIntervalIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Start time
     * 
     * @return
     */
    public static FormItemIfFunction getStartTimeIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
            }
        };
    }

    /**
     * End time
     * 
     * @return
     */
    public static FormItemIfFunction getEndTimeIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
            }
        };
    }

    /**
     * Min length
     * 
     * @return
     */
    public static FormItemIfFunction getMinLengthIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Max length
     * 
     * @return
     */
    public static FormItemIfFunction getMaxLengthIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Min value
     * 
     * @return
     */
    public static FormItemIfFunction getMinValueIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Max value
     * 
     * @return
     */
    public static FormItemIfFunction getMaxValueIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Decimals
     * 
     * @return
     */
    public static FormItemIfFunction getDecimalsIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }

    /**
     * Pattern
     * 
     * @return
     */
    public static FormItemIfFunction getPatternIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!StringUtils.isBlank(form.getValueAsString(TEXT_TYPE_FIELD_NAME))) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    return !representsTime(type);
                }
                return false;
            }
        };
    }
}

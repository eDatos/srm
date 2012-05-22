package org.siemac.metamac.internal.web.dsd.utils;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.domain_enum.domain.FacetValueTypeEnum;
import org.siemac.metamac.internal.web.dsd.widgets.FacetForm;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class FacetFormUtils {

    public static final String            TEXT_TYPE_FIELD_NAME = "repr-no-enum-text-type";

    public static Set<FacetValueTypeEnum> dsdFacetValueTypeEnums;
    public static Set<FacetValueTypeEnum> timeFacetValueTypeEnums;

    /**
     * Shows or hides facetForm depending on whether representationType is ENUMERATED or not.
     * 
     * @param facetForm
     * @param representationType
     */
    public static void setFacetFormVisibility(FacetForm facetForm, String representationType) {
        if (representationType == null || representationType.isEmpty()) {
            facetForm.hide();
        } else if (!CommonUtils.isRepresentationTypeEnumerated(representationType)) {
            facetForm.show();
        } else {
            facetForm.hide();
        }
    }

    /**
     * Returns the valid FacetValueTypeEnums for DSD
     * 
     * @return
     */
    public static Set<FacetValueTypeEnum> getDsdFacetValueTypeEnums() {
        // XHTML_FVT, KEY_VALUES_FVT, IDENTIFIABLE_REFERENCE_FVT, DATASET_REFERENCE_FVT
        // and ATTACHMENT_CONSTRAINT_REFERENCE_FVT are no valid FacetValutType for DSDs
        if (dsdFacetValueTypeEnums == null) {
            dsdFacetValueTypeEnums = new HashSet<FacetValueTypeEnum>();
            for (FacetValueTypeEnum f : FacetValueTypeEnum.values()) {
                if (!FacetValueTypeEnum.XHTML_FVT.equals(f) && !FacetValueTypeEnum.KEY_VALUES_FVT.equals(f) && !FacetValueTypeEnum.IDENTIFIABLE_REFERENCE_FVT.equals(f)
                        && !FacetValueTypeEnum.DATASET_REFERENCE_FVT.equals(f) && !FacetValueTypeEnum.ATTACHMENT_CONSTRAINT_REFERENCE_FVT.equals(f)) {
                    dsdFacetValueTypeEnums.add(f);
                }
            }
        }
        return dsdFacetValueTypeEnums;
    }

    /**
     * Return FacetValueTypeEnums that represents time
     * 
     * @return
     */
    public static Set<FacetValueTypeEnum> getTimeFacetValueTypeEnums() {
        if (timeFacetValueTypeEnums == null) {
            timeFacetValueTypeEnums = new HashSet<FacetValueTypeEnum>();
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.OBSERVATIONAL_TIME_PERIOD_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.STANDARD_TIME_PERIOD_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.BASIC_TIME_PERIOD_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.GREGORIAN_TIME_PERIOD_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.GREGORIAN_YEAR_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.GREGORIAN_YEAR_MONTH_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.GREGORIAN_DAY_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_TIME_PERIOD_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_YEAR_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_SEMESTER_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_TRIMESTER_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_QUARTER_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_MONTH_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_WEEK_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.REPORTING_DAY_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.DATE_TIME_FVT);
            timeFacetValueTypeEnums.add(FacetValueTypeEnum.TIMES_RANGE_FVT);
        }
        return timeFacetValueTypeEnums;
    }

    /**
     * True if {@link FacetValueTypeEnum} f represents time
     * 
     * @param f
     * @return
     */
    public static boolean representsTime(FacetValueTypeEnum f) {
        return getTimeFacetValueTypeEnums().contains(f) ? true : false;
    }

    // /////////////////////////////////////
    // //
    // FormItemIfFunction for FacetTypes //
    // //
    // /////////////////////////////////////

    /**
     * Is sequence
     * 
     * @return
     */
    public static FormItemIfFunction getIsSequenceIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.BIG_INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.LONG_FVT.equals(type) ||
                    // FacetValueTypeEnum.SHORT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type) ||
                    // FacetValueTypeEnum.DATE_TIME_FVT.equals(type) ||
                    // FacetValueTypeEnum.TIME_FVT.equals(type) ||
                    // FacetValueTypeEnum.MONTH_FVT.equals(type) ||
                    // FacetValueTypeEnum.DAY_FVT.equals(type) ||
                    // FacetValueTypeEnum.MONTH_DAY_FVT.equals(type) ||
                    // FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.BIG_INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.LONG_FVT.equals(type) ||
                    // FacetValueTypeEnum.SHORT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type) ||
                    // FacetValueTypeEnum.TIMES_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCLUSIVE_VALUE_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.EXCLUSIVE_VALUE_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.BIG_INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.LONG_FVT.equals(type) ||
                    // FacetValueTypeEnum.SHORT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type) ||
                    // FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCLUSIVE_VALUE_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.EXCLUSIVE_VALUE_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.DURATION_FVT.equals(type) ||
                    // FacetValueTypeEnum.TIMES_RANGE_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    // FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.DATE_TIME_FVT.equals(type)) {
                    // return true;
                    // }
                    return true;
                }
                return false;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    // FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.DATE_TIME_FVT.equals(type)) {
                    // return true;
                    // }
                    return true;
                }
                return false;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.STRING_FVT.equals(type) ||
                    // FacetValueTypeEnum.BIG_INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.LONG_FVT.equals(type) ||
                    // FacetValueTypeEnum.SHORT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type) ||
                    // FacetValueTypeEnum.URI_FVT.equals(type) ||
                    // FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.STRING_FVT.equals(type) ||
                    // FacetValueTypeEnum.BIG_INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.LONG_FVT.equals(type) ||
                    // FacetValueTypeEnum.SHORT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type) ||
                    // FacetValueTypeEnum.URI_FVT.equals(type) ||
                    // FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.DOUBLE_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.DOUBLE_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
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
                if (form.getValueAsString(TEXT_TYPE_FIELD_NAME) != null) {
                    FacetValueTypeEnum type = FacetValueTypeEnum.valueOf(form.getValueAsString(TEXT_TYPE_FIELD_NAME));
                    // if (FacetValueTypeEnum.STRING_FVT.equals(type) ||
                    // FacetValueTypeEnum.BIG_INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.INTEGER_FVT.equals(type) ||
                    // FacetValueTypeEnum.LONG_FVT.equals(type) ||
                    // FacetValueTypeEnum.SHORT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DECIMAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.FLOAT_FVT.equals(type) ||
                    // FacetValueTypeEnum.DOUBLE_FVT.equals(type) ||
                    // FacetValueTypeEnum.BOOLEAN_FVT.equals(type) ||
                    // FacetValueTypeEnum.DATE_TIME_FVT.equals(type) ||
                    // FacetValueTypeEnum.TIME_FVT.equals(type) ||
                    // FacetValueTypeEnum.MONTH_FVT.equals(type) ||
                    // FacetValueTypeEnum.DAY_FVT.equals(type) ||
                    // FacetValueTypeEnum.MONTH_DAY_FVT.equals(type) ||
                    // FacetValueTypeEnum.DURATION_FVT.equals(type) ||
                    // FacetValueTypeEnum.URI_FVT.equals(type) ||
                    // FacetValueTypeEnum.TIMES_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.COUNT_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCLUSIVE_VALUE_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.EXCLUSIVE_VALUE_RANGE_FVT.equals(type) ||
                    // FacetValueTypeEnum.INCREMENTAL_FVT.equals(type) ||
                    // FacetValueTypeEnum.OBSERVATIONAL_TIME_PERIOD_FVT.equals(type)) {
                    // return true;
                    // }
                    return representsTime(type) ? false : true;
                }
                return false;
            }
        };
    }

}

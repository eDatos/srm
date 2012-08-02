package org.siemac.metamac.srm.web.dsd.utils;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.srm.web.dsd.widgets.FacetForm;

import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.shared.DataStructureConstraintSharedValidator;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
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
        // SimpleDataType values
        if (dsdFacetValueTypeEnums == null) {
            dsdFacetValueTypeEnums = new HashSet<FacetValueTypeEnum>();
            for (FacetValueTypeEnum f : DataStructureConstraintSharedValidator.simpleDataTypeValues) {
                dsdFacetValueTypeEnums.add(f);
            }
        }
        return dsdFacetValueTypeEnums;
    }

    /**
     * True if {@link FacetValueTypeEnum} f represents time
     * 
     * @param f
     * @return
     */
    public static boolean representsTime(FacetValueTypeEnum f) {
        return DataStructureConstraintSharedValidator.isTimeDataType(f);
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
                    return representsTime(type) ? false : true;
                }
                return false;
            }
        };
    }

}

package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.web.common.client.utils.SDMXCommonWebValidatorsV2_1;
import org.siemac.metamac.web.common.client.utils.XsdDataTypesValidators;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

public class FacetForm extends GroupDynamicForm {

    private RequiredSelectItem textType;
    private CheckboxItem       isSequence;
    private CustomTextItem     minLength;
    private CustomTextItem     maxLength;
    private CustomTextItem     startValue;
    private CustomTextItem     endValue;
    private CustomTextItem     interval;
    private CustomTextItem     timeInterval;
    private CustomTextItem     startTime;
    private CustomTextItem     endTime;
    private CustomTextItem     minValue;
    private CustomTextItem     maxValue;
    private CustomTextItem     decimals;
    private CustomTextItem     pattern;

    public FacetForm() {
        super(MetamacSrmWeb.getConstants().dsdDimensionNonNumeratedRepresentation());
        setVisibility(Visibility.HIDDEN);

        textType = new RequiredSelectItem(FacetFormUtils.TEXT_TYPE_FIELD_NAME, MetamacSrmWeb.getConstants().dsdRepresentationTextType());
        textType.setValueMap(CommonUtils.getFacetValueTypeHashMap());
        textType.setRedrawOnChange(true);
        textType.setWidth(205);

        isSequence = new CheckboxItem("sequence-attr", MetamacSrmWeb.getConstants().dsdRepresentationIsSequence());
        isSequence.setLabelAsTitle(true);
        isSequence.setShowIfCondition(FacetFormUtils.getIsSequenceIfFunction());

        minLength = new CustomTextItem("min-length-attr", MetamacSrmWeb.getConstants().dsdRepresentationMinLength());
        minLength.setShowIfCondition(FacetFormUtils.getMinLengthIfFunction());
        minLength.setValidators(XsdDataTypesValidators.getPositiveIntegerValidator());

        maxLength = new CustomTextItem("max-length", MetamacSrmWeb.getConstants().dsdRepresentationMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());
        maxLength.setValidators(XsdDataTypesValidators.getPositiveIntegerValidator());

        startValue = new CustomTextItem("start-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());
        startValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        endValue = new CustomTextItem("end-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());
        endValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        interval = new CustomTextItem("interval-attr", MetamacSrmWeb.getConstants().dsdRepresentationInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());
        interval.setValidators(XsdDataTypesValidators.getDecimalValidator());

        timeInterval = new CustomTextItem("time-interval-attr", MetamacSrmWeb.getConstants().dsdRepresentationTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());
        timeInterval.setValidators(XsdDataTypesValidators.getDurationValidator());

        startTime = new CustomTextItem("start-time-attr", MetamacSrmWeb.getConstants().dsdRepresentationStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());
        startTime.setValidators(SDMXCommonWebValidatorsV2_1.getBasicTimePeriodValidator());

        endTime = new CustomTextItem("end-time-attr", MetamacSrmWeb.getConstants().dsdRepresentationEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());
        endTime.setValidators(SDMXCommonWebValidatorsV2_1.getBasicTimePeriodValidator());

        minValue = new CustomTextItem("min-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());
        minValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        maxValue = new CustomTextItem("max-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());
        maxValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        decimals = new CustomTextItem("decimals-attr", MetamacSrmWeb.getConstants().dsdRepresentationDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());
        decimals.setValidators(XsdDataTypesValidators.getPositiveIntegerValidator());

        pattern = new CustomTextItem("pattern-attr", MetamacSrmWeb.getConstants().dsdRepresentationPattern());
        pattern.setShowIfCondition(FacetFormUtils.getPatternIfFunction());

        setFields(textType, minLength, maxLength, isSequence, startValue, endValue, interval, timeInterval, startTime, endTime, minValue, maxValue, decimals, pattern);
    }

    public void setFacet(FacetDto facetDto) {
        // - FacetValueType
        textType.setValue(facetDto.getFacetValue() == null ? null : facetDto.getFacetValue().toString());
        // - FacetType
        isSequence.setValue(getBooleanValue(facetDto.getIsSequenceFT()));
        minLength.setValue(facetDto.getMinLengthFT());
        maxLength.setValue(facetDto.getMaxLengthFT());
        minValue.setValue(facetDto.getMinValueFT());
        maxValue.setValue(facetDto.getMaxValueFT());
        startValue.setValue(facetDto.getStartValueFT());
        endValue.setValue(facetDto.getEndValueFT());
        interval.setValue(facetDto.getInterval());
        timeInterval.setValue(facetDto.getTimeIntervalFT());
        decimals.setValue(facetDto.getDecimalsFT());
        pattern.setValue(facetDto.getPatternFT());
        startTime.setValue(facetDto.getStartTimeFT());
        endTime.setValue(facetDto.getEndTimeFT());
    }

    public FacetDto getFacet(FacetDto facetDto) {
        // - FacetValueType
        facetDto.setFacetValue(textType.getValueAsString() != null ? FacetValueTypeEnum.valueOf(textType.getValueAsString()) : null);
        // - FacetType
        facetDto.setIsSequenceFT(isSequence.getVisible() && isSequence.getValue() != null ? isSequence.getValue().toString() : null);
        facetDto.setMinLengthFT(minLength.getVisible() ? minLength.getValueAsString() : null);
        facetDto.setMaxLengthFT(maxLength.getVisible() ? maxLength.getValueAsString() : null);
        facetDto.setMinValueFT(minValue.getVisible() ? minValue.getValueAsString() : null);
        facetDto.setMaxValueFT(maxValue.getVisible() ? maxValue.getValueAsString() : null);
        facetDto.setStartValueFT(startValue.getVisible() ? startValue.getValueAsString() : null);
        facetDto.setEndValueFT(endValue.getVisible() ? endValue.getValueAsString() : null);
        facetDto.setInterval(interval.getVisible() ? interval.getValueAsString() : null);
        facetDto.setTimeIntervalFT(timeInterval.getVisible() ? timeInterval.getValueAsString() : null);
        facetDto.setDecimalsFT(decimals.getVisible() ? decimals.getValueAsString() : null);
        facetDto.setPatternFT(pattern.getVisible() ? pattern.getValueAsString() : null);
        facetDto.setStartTimeFT(startTime.getVisible() ? startTime.getValueAsString() : null);
        facetDto.setEndTimeFT(endTime.getVisible() ? endTime.getValueAsString() : null);
        return facetDto;
    }

    private boolean getBooleanValue(String value) {
        if (value != null && ("true".equals(value) || "1".equals(value))) {
            return true;
        }
        return false;
    }

    public RequiredSelectItem getTextType() {
        return textType;
    }

    public CheckboxItem getIsSequence() {
        return isSequence;
    }

    public CustomTextItem getInterval() {
        return interval;
    }

    public CustomTextItem getStartValue() {
        return startValue;
    }

    public CustomTextItem getEndValue() {
        return endValue;
    }

    public CustomTextItem getTimeInterval() {
        return timeInterval;
    }

    public CustomTextItem getStartTime() {
        return startTime;
    }

    public CustomTextItem getEndTime() {
        return endTime;
    }

    public CustomTextItem getMinLength() {
        return minLength;
    }

    public CustomTextItem getMaxLength() {
        return maxLength;
    }

    public CustomTextItem getMinValue() {
        return minValue;
    }

    public CustomTextItem getMaxValue() {
        return maxValue;
    }

    public CustomTextItem getDecimals() {
        return decimals;
    }

    public CustomTextItem getPattern() {
        return pattern;
    }

}

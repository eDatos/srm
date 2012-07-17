package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.SDMXCommonWebValidatorsV2_1;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomFloatItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

public class FacetForm extends GroupDynamicForm {

    private RequiredSelectItem textType;
    private CheckboxItem       isSequence;
    private CustomTextItem     minLength;
    private CustomTextItem     maxLength;
    private CustomFloatItem    startValue;
    private CustomFloatItem    endValue;
    private CustomFloatItem    interval;
    private CustomTextItem     timeInterval;
    private CustomTextItem     startTime;
    private CustomTextItem     endTime;
    private CustomFloatItem    minValue;
    private CustomFloatItem    maxValue;
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
        minLength.setValidators(FormItemUtils.getPositiveIntegerValidator());

        maxLength = new CustomTextItem("max-length", MetamacSrmWeb.getConstants().dsdRepresentationMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());
        maxLength.setValidators(FormItemUtils.getPositiveIntegerValidator());

        startValue = new CustomFloatItem("start-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());

        endValue = new CustomFloatItem("end-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());

        interval = new CustomFloatItem("interval-attr", MetamacSrmWeb.getConstants().dsdRepresentationInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());

        timeInterval = new CustomTextItem("time-interval-attr", MetamacSrmWeb.getConstants().dsdRepresentationTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());
        timeInterval.setValidators(SDMXCommonWebValidatorsV2_1.getDurationValidator());

        startTime = new CustomTextItem("start-time-attr", MetamacSrmWeb.getConstants().dsdRepresentationStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());
        startTime.setValidators(SDMXCommonWebValidatorsV2_1.getBasicTimePeriodValidator());

        endTime = new CustomTextItem("end-time-attr", MetamacSrmWeb.getConstants().dsdRepresentationEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());
        endTime.setValidators(SDMXCommonWebValidatorsV2_1.getBasicTimePeriodValidator());

        minValue = new CustomFloatItem("min-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());

        maxValue = new CustomFloatItem("max-value-attr", MetamacSrmWeb.getConstants().dsdRepresentationMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());

        decimals = new CustomTextItem("decimals-attr", MetamacSrmWeb.getConstants().dsdRepresentationDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());
        decimals.setValidators(FormItemUtils.getPositiveIntegerValidator());

        pattern = new CustomTextItem("pattern-attr", MetamacSrmWeb.getConstants().dsdRepresentationPattern());
        pattern.setShowIfCondition(FacetFormUtils.getPatternIfFunction());

        setFields(textType, minLength, maxLength, isSequence, startValue, endValue, interval, timeInterval, startTime, endTime, minValue, maxValue, decimals, pattern);
    }

    public void setFacet(FacetDto facetDto) {
        // - FacetValueType
        textType.setValue(facetDto.getFacetValue() == null ? null : facetDto.getFacetValue().toString());
        // - FacetType
        isSequence.setValue(facetDto.getIsSequenceFT());
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
        facetDto.setIsSequenceFT(isSequence.getVisible() ? isSequence.getValueAsBoolean() : null);
        facetDto.setMinLengthFT(minLength.getVisible() ? FormItemUtils.getValueAsInteger(minLength) : null);
        facetDto.setMaxLengthFT(maxLength.getVisible() ? FormItemUtils.getValueAsInteger(maxLength) : null);
        facetDto.setMinValueFT(minValue.getVisible() ? FormItemUtils.getValueAsDouble(minValue) : null);
        facetDto.setMaxValueFT(maxValue.getVisible() ? FormItemUtils.getValueAsDouble(maxValue) : null);
        facetDto.setStartValueFT(startValue.getVisible() ? FormItemUtils.getValueAsDouble(startValue) : null);
        facetDto.setEndValueFT(endValue.getVisible() ? FormItemUtils.getValueAsDouble(endValue) : null);
        facetDto.setInterval(interval.getVisible() ? FormItemUtils.getValueAsDouble(interval) : null);
        facetDto.setTimeIntervalFT(timeInterval.getVisible() ? timeInterval.getValueAsString() : null);
        facetDto.setDecimalsFT(decimals.getVisible() ? FormItemUtils.getValueAsInteger(decimals) : null);
        facetDto.setPatternFT(pattern.getVisible() ? pattern.getValueAsString() : null);
        facetDto.setStartTimeFT(startTime.getVisible() ? startTime.getValueAsString() : null);
        facetDto.setEndTimeFT(endTime.getVisible() ? endTime.getValueAsString() : null);
        return facetDto;
    }

    public RequiredSelectItem getTextType() {
        return textType;
    }

    public CheckboxItem getIsSequence() {
        return isSequence;
    }

    public CustomFloatItem getInterval() {
        return interval;
    }

    public CustomFloatItem getStartValue() {
        return startValue;
    }

    public CustomFloatItem getEndValue() {
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

    public CustomFloatItem getMinValue() {
        return minValue;
    }

    public CustomFloatItem getMaxValue() {
        return maxValue;
    }

    public CustomTextItem getDecimals() {
        return decimals;
    }

    public CustomTextItem getPattern() {
        return pattern;
    }

}

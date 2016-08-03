package org.siemac.metamac.srm.web.client.representation.widgets;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.ds.RepresentationDS;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.SDMXCommonWebValidatorsV2_1;
import org.siemac.metamac.web.common.client.utils.XsdDataTypesValidators;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;

public class BaseFacetForm extends GroupDynamicForm {

    protected RequiredSelectItem textType;
    protected BooleanSelectItem  isSequence;
    protected CustomTextItem     minLength;
    protected CustomTextItem     maxLength;
    protected CustomTextItem     startValue;
    protected CustomTextItem     endValue;
    protected CustomTextItem     interval;
    protected CustomTextItem     timeInterval;
    protected CustomTextItem     startTime;
    protected CustomTextItem     endTime;
    protected CustomTextItem     minValue;
    protected CustomTextItem     maxValue;
    protected CustomTextItem     decimals;
    protected CustomTextItem     pattern;

    protected FacetDto           facetDto;

    public BaseFacetForm() {
        super(MetamacSrmWeb.getConstants().representationNonNumerated());

        textType = new RequiredSelectItem(FacetFormUtils.TEXT_TYPE_FIELD_NAME, MetamacSrmWeb.getConstants().representationFacetTextType());
        textType.setValueMap(CommonUtils.getDsdFacetValueTypeHashMap());
        textType.setRedrawOnChange(true);
        textType.setWidth(205);

        isSequence = new BooleanSelectItem(RepresentationDS.FACET_IS_SEQUENCE, MetamacSrmWeb.getConstants().representationFacetIsSequence());
        isSequence.setShowIfCondition(FacetFormUtils.getIsSequenceIfFunction());

        minLength = new CustomTextItem(RepresentationDS.FACET_MIN_LENGTH, MetamacSrmWeb.getConstants().representationFacetMinLength());
        minLength.setShowIfCondition(FacetFormUtils.getMinLengthIfFunction());
        minLength.setValidators(XsdDataTypesValidators.getPositiveIntegerValidator());

        maxLength = new CustomTextItem(RepresentationDS.FACET_MAX_LENGTH, MetamacSrmWeb.getConstants().representationFacetMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());
        maxLength.setValidators(XsdDataTypesValidators.getPositiveIntegerValidator());

        startValue = new CustomTextItem(RepresentationDS.FACET_START_VALUE, MetamacSrmWeb.getConstants().representationFacetStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());
        startValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        endValue = new CustomTextItem(RepresentationDS.FACET_END_VALUE, MetamacSrmWeb.getConstants().representationFacetEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());
        endValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        interval = new CustomTextItem(RepresentationDS.FACET_INTERVAL, MetamacSrmWeb.getConstants().representationFacetInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());
        interval.setValidators(XsdDataTypesValidators.getDecimalValidator());

        timeInterval = new CustomTextItem(RepresentationDS.FACET_TIME_INTERVAL, MetamacSrmWeb.getConstants().representationFacetTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());
        timeInterval.setValidators(XsdDataTypesValidators.getDurationValidator());

        startTime = new CustomTextItem(RepresentationDS.FACET_START_TIME, MetamacSrmWeb.getConstants().representationFacetStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());
        startTime.setValidators(SDMXCommonWebValidatorsV2_1.getBasicTimePeriodValidator());

        endTime = new CustomTextItem(RepresentationDS.FACET_END_TIME, MetamacSrmWeb.getConstants().representationFacetEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());
        endTime.setValidators(SDMXCommonWebValidatorsV2_1.getBasicTimePeriodValidator());

        minValue = new CustomTextItem(RepresentationDS.FACET_MIN_VALUE, MetamacSrmWeb.getConstants().representationFacetMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());
        minValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        maxValue = new CustomTextItem(RepresentationDS.FACET_MAX_VALUE, MetamacSrmWeb.getConstants().representationFacetMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());
        maxValue.setValidators(XsdDataTypesValidators.getDecimalValidator());

        decimals = new CustomTextItem(RepresentationDS.FACET_DECIMALS, MetamacSrmWeb.getConstants().representationFacetDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());
        decimals.setValidators(XsdDataTypesValidators.getPositiveIntegerValidator());

        pattern = new CustomTextItem(RepresentationDS.FACET_PATTERN, MetamacSrmWeb.getConstants().representationFacetPattern());
        pattern.setShowIfCondition(FacetFormUtils.getPatternIfFunction());

        setFields(textType, minLength, maxLength, isSequence, startValue, endValue, interval, timeInterval, startTime, endTime, minValue, maxValue, decimals, pattern);

    }

    public void setFacet(FacetDto facetDto) {
        this.facetDto = facetDto;
        if (facetDto != null) {
            // - FacetValueType
            textType.setValue(facetDto.getFacetValue() == null ? null : facetDto.getFacetValue().toString());
            // - FacetType
            isSequence.setBooleanValue(BooleanUtils.parseBoolean(facetDto.getIsSequenceFT()));
            minLength.setValue(facetDto.getMinLengthFT());
            maxLength.setValue(facetDto.getMaxLengthFT());
            minValue.setValue(facetDto.getMinValueFT());
            maxValue.setValue(facetDto.getMaxValueFT());
            startValue.setValue(facetDto.getStartValueFT());
            endValue.setValue(facetDto.getEndValueFT());
            interval.setValue(facetDto.getIntervalFT());
            timeInterval.setValue(facetDto.getTimeIntervalFT());
            decimals.setValue(facetDto.getDecimalsFT());
            pattern.setValue(facetDto.getPatternFT());
            startTime.setValue(facetDto.getStartTimeFT());
            endTime.setValue(facetDto.getEndTimeFT());
        } else {
            clearValues();
        }
    }

    public FacetDto getFacet() {
        if (facetDto == null) {
            facetDto = new FacetDto();
        }
        // - FacetValueType
        facetDto.setFacetValue(textType.getValueAsString() != null ? FacetValueTypeEnum.valueOf(textType.getValueAsString()) : null);
        // - FacetType
        facetDto.setIsSequenceFT(isSequence.getVisible() ? (isSequence.getBooleanValue() != null ? isSequence.getBooleanValue().toString() : null) : null);
        facetDto.setMinLengthFT(minLength.getVisible() ? minLength.getValueAsString() : null);
        facetDto.setMaxLengthFT(maxLength.getVisible() ? maxLength.getValueAsString() : null);
        facetDto.setMinValueFT(minValue.getVisible() ? minValue.getValueAsString() : null);
        facetDto.setMaxValueFT(maxValue.getVisible() ? maxValue.getValueAsString() : null);
        facetDto.setStartValueFT(startValue.getVisible() ? startValue.getValueAsString() : null);
        facetDto.setEndValueFT(endValue.getVisible() ? endValue.getValueAsString() : null);
        facetDto.setIntervalFT(interval.getVisible() ? interval.getValueAsString() : null);
        facetDto.setTimeIntervalFT(timeInterval.getVisible() ? timeInterval.getValueAsString() : null);
        facetDto.setDecimalsFT(decimals.getVisible() ? decimals.getValueAsString() : null);
        facetDto.setPatternFT(pattern.getVisible() ? pattern.getValueAsString() : null);
        facetDto.setStartTimeFT(startTime.getVisible() ? startTime.getValueAsString() : null);
        facetDto.setEndTimeFT(endTime.getVisible() ? endTime.getValueAsString() : null);
        return facetDto;
    }

    public RequiredSelectItem getTextType() {
        return textType;
    }

    public BooleanSelectItem getIsSequence() {
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

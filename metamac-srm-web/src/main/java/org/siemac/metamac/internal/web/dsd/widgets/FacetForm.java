package org.siemac.metamac.internal.web.dsd.widgets;

import java.util.LinkedHashMap;

import org.siemac.metamac.domain_dto.FacetDto;
import org.siemac.metamac.domain_enum.domain.FacetValueTypeEnum;
import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class FacetForm extends GroupDynamicForm {

    private RequiredSelectItem textType;
    private CheckboxItem       isSequence;
    private FloatItem          interval;
    private FloatItem          startValue;
    private FloatItem          endValue;
    private TextItem           timeInterval;
    private TextItem           startTime;
    private TextItem           endTime;
    private IntegerItem        minLength;
    private IntegerItem        maxLength;
    private FloatItem          minValue;
    private FloatItem          maxValue;
    private IntegerItem        decimals;
    private TextItem           pattern;

    public FacetForm() {
        super(MetamacInternalWeb.getConstants().dsdDimensionNonNumeratedRepresentation());
        setVisibility(Visibility.HIDDEN);

        textType = new RequiredSelectItem(FacetFormUtils.TEXT_TYPE_FIELD_NAME, MetamacInternalWeb.getConstants().dsdRepresentationTextType());
        LinkedHashMap<String, String> valueMapFacetValueType = new LinkedHashMap<String, String>();
        for (FacetValueTypeEnum f : FacetFormUtils.getDsdFacetValueTypeEnums()) {
            String value = MetamacInternalWeb.getCoreMessages().getString(MetamacInternalWeb.getCoreMessages().facetValueTypeEnum() + f.getName());
            valueMapFacetValueType.put(f.toString(), value);
        }
        textType.setValueMap(valueMapFacetValueType);
        textType.setRedrawOnChange(true);
        textType.setWidth(205);

        isSequence = new CheckboxItem("sequence-attr", MetamacInternalWeb.getConstants().dsdRepresentationIsSequence());
        isSequence.setLabelAsTitle(true);
        isSequence.setShowIfCondition(FacetFormUtils.getIsSequenceIfFunction());

        interval = new FloatItem("interval-attr", MetamacInternalWeb.getConstants().dsdRepresentationInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());

        startValue = new FloatItem("start-value-attr", MetamacInternalWeb.getConstants().dsdRepresentationStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());

        endValue = new FloatItem("end-value-attr", MetamacInternalWeb.getConstants().dsdRepresentationEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());

        timeInterval = new TextItem("time-interval-attr", MetamacInternalWeb.getConstants().dsdRepresentationTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());

        startTime = new TextItem("start-time-attr", MetamacInternalWeb.getConstants().dsdRepresentationStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());

        endTime = new TextItem("end-time-attr", MetamacInternalWeb.getConstants().dsdRepresentationEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());

        minLength = new IntegerItem("min-length-attr", MetamacInternalWeb.getConstants().dsdRepresentationMinLength());
        minLength.setShowIfCondition(FacetFormUtils.getMinLengthIfFunction());

        maxLength = new IntegerItem("max-length", MetamacInternalWeb.getConstants().dsdRepresentationMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());

        minValue = new FloatItem("min-value-attr", MetamacInternalWeb.getConstants().dsdRepresentationMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());

        maxValue = new FloatItem("max-value-attr", MetamacInternalWeb.getConstants().dsdRepresentationMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());

        decimals = new IntegerItem("decimals-attr", MetamacInternalWeb.getConstants().dsdRepresentationDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());

        pattern = new TextItem("pattern-attr", MetamacInternalWeb.getConstants().dsdRepresentationPattern());
        pattern.setShowIfCondition(FacetFormUtils.getPatternIfFunction());

        setFields(textType, isSequence, interval, startValue, endValue, timeInterval, startTime, endTime, minLength, maxLength, minValue, maxValue, decimals, pattern);
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

    public SelectItem getTextType() {
        return textType;
    }

    public CheckboxItem getIsSequence() {
        return isSequence;
    }

    public FloatItem getInterval() {
        return interval;
    }

    public FloatItem getStartValue() {
        return startValue;
    }

    public FloatItem getEndValue() {
        return endValue;
    }

    public TextItem getTimeInterval() {
        return timeInterval;
    }

    public TextItem getStartTime() {
        return startTime;
    }

    public TextItem getEndTime() {
        return endTime;
    }

    public IntegerItem getMinLength() {
        return minLength;
    }

    public IntegerItem getMaxLength() {
        return maxLength;
    }

    public FloatItem getMinValue() {
        return minValue;
    }

    public FloatItem getMaxValue() {
        return maxValue;
    }

    public IntegerItem getDecimals() {
        return decimals;
    }

    public TextItem getPattern() {
        return pattern;
    }

}

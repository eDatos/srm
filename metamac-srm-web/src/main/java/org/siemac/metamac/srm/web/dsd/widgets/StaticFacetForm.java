package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class StaticFacetForm extends GroupDynamicForm {

    private ViewTextItem textTypeHidden;
    private ViewTextItem textType;
    private ViewTextItem isSequence;
    private ViewTextItem interval;
    private ViewTextItem startValue;
    private ViewTextItem endValue;
    private ViewTextItem timeInterval;
    private ViewTextItem startTime;
    private ViewTextItem endTime;
    private ViewTextItem minLength;
    private ViewTextItem maxLength;
    private ViewTextItem minValue;
    private ViewTextItem maxValue;
    private ViewTextItem decimals;
    private ViewTextItem pattern;

    public StaticFacetForm() {
        super(MetamacSrmWeb.getConstants().dsdDimensionNonNumeratedRepresentation());

        // This field is never shown in the form. It is only used by the IfFuctions.
        // The field textTypeHidden contains the key of the enumerated, and the field textType contains the value of this enumerated (translated name).
        textTypeHidden = new ViewTextItem(FacetFormUtils.TEXT_TYPE_FIELD_NAME, MetamacSrmWeb.getConstants().dsdRepresentationTextType());
        textTypeHidden.setVisible(false);

        textType = new ViewTextItem("repr-value-text-type-view", MetamacSrmWeb.getConstants().dsdRepresentationTextType());

        isSequence = new ViewTextItem("sequence-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationIsSequence());
        isSequence.setShowIfCondition(FacetFormUtils.getIsSequenceIfFunction());

        interval = new ViewTextItem("interval-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());

        startValue = new ViewTextItem("start-value-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());

        endValue = new ViewTextItem("end-value-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());

        timeInterval = new ViewTextItem("time-interval-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());

        startTime = new ViewTextItem("start-time-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());

        endTime = new ViewTextItem("end-time-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());

        minLength = new ViewTextItem("min-length-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationMinLength());
        minLength.setShowIfCondition(FacetFormUtils.getMinLengthIfFunction());

        maxLength = new ViewTextItem("max-length-view", MetamacSrmWeb.getConstants().dsdRepresentationMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());

        minValue = new ViewTextItem("min-value-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());

        maxValue = new ViewTextItem("max-value-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());

        decimals = new ViewTextItem("decimals-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());

        pattern = new ViewTextItem("pattern-attr-view", MetamacSrmWeb.getConstants().dsdRepresentationPattern());
        pattern.setShowIfCondition(FacetFormUtils.getPatternIfFunction());

        setFields(textTypeHidden, textType, minLength, maxLength, isSequence, startValue, endValue, interval, timeInterval, startTime, endTime, minValue, maxValue, decimals, pattern);
    }

    public void setFacet(FacetDto facetDto) {
        // - FacetValueType
        textTypeHidden.setValue(facetDto.getFacetValue() == null ? null : facetDto.getFacetValue().toString());
        String value = facetDto.getFacetValue() == null ? null : MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().facetValueTypeEnum() + facetDto.getFacetValue().getName());
        textType.setValue(value);
        // - FacetType
        isSequence.setValue(facetDto.getIsSequenceFT() == null ? null : getBooleanTextValue(facetDto.getIsSequenceFT()));
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
    }

    private String getBooleanTextValue(String value) {
        if ("true".equals(value) || "1".equals(value)) {
            return MetamacSrmWeb.getConstants().yes();
        } else {
            return MetamacSrmWeb.getConstants().no();
        }
    }

}

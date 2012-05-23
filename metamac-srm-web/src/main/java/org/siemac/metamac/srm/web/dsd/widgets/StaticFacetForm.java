package org.siemac.metamac.internal.web.dsd.widgets;

import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.dsd.utils.FacetFormUtils;
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
        super(MetamacInternalWeb.getConstants().dsdDimensionNonNumeratedRepresentation());

        // This field is never shown in the form. It is only used by the IfFuctions.
        // The field textTypeHidden contains the key of the enumerated, and the field textType contains the value of this enumerated (translated name).
        textTypeHidden = new ViewTextItem(FacetFormUtils.TEXT_TYPE_FIELD_NAME, MetamacInternalWeb.getConstants().dsdRepresentationTextType());
        textTypeHidden.setVisible(false);

        textType = new ViewTextItem("repr-value-text-type-view", MetamacInternalWeb.getConstants().dsdRepresentationTextType());

        isSequence = new ViewTextItem("sequence-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationIsSequence());
        isSequence.setShowIfCondition(FacetFormUtils.getIsSequenceIfFunction());

        interval = new ViewTextItem("interval-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());

        startValue = new ViewTextItem("start-value-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());

        endValue = new ViewTextItem("end-value-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());

        timeInterval = new ViewTextItem("time-interval-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());

        startTime = new ViewTextItem("start-time-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());

        endTime = new ViewTextItem("end-time-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());

        minLength = new ViewTextItem("min-length-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationMinLength());
        minLength.setShowIfCondition(FacetFormUtils.getMinLengthIfFunction());

        maxLength = new ViewTextItem("max-length-view", MetamacInternalWeb.getConstants().dsdRepresentationMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());

        minValue = new ViewTextItem("min-value-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());

        maxValue = new ViewTextItem("max-value-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());

        decimals = new ViewTextItem("decimals-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());

        pattern = new ViewTextItem("pattern-attr-view", MetamacInternalWeb.getConstants().dsdRepresentationPattern());
        pattern.setShowIfCondition(FacetFormUtils.getPatternIfFunction());

        setFields(textTypeHidden, textType, isSequence, interval, startValue, endValue, timeInterval, startTime, endTime, minLength, maxLength, minValue, maxValue, decimals, pattern);
    }

    public void setFacet(FacetDto facetDto) {
        // - FacetValueType
        textTypeHidden.setValue(facetDto.getFacetValue() == null ? null : facetDto.getFacetValue().toString());
        String value = facetDto.getFacetValue() == null ? null : MetamacInternalWeb.getCoreMessages().getString(
                MetamacInternalWeb.getCoreMessages().facetValueTypeEnum() + facetDto.getFacetValue().getName());
        textType.setValue(value);
        // - FacetType
        isSequence.setValue(facetDto.getIsSequenceFT() == null ? null : (facetDto.getIsSequenceFT() ? MetamacInternalWeb.getConstants().yes() : MetamacInternalWeb.getConstants().no()));
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

}

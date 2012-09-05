package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.ds.RepresentationDS;
import org.siemac.metamac.srm.web.dsd.utils.FacetFormUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.FacetDto;

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
        textTypeHidden = new ViewTextItem(FacetFormUtils.TEXT_TYPE_FIELD_NAME, MetamacSrmWeb.getConstants().representationFacetTextType());
        textTypeHidden.setVisible(false);

        textType = new ViewTextItem("repr-value-text-type-view", MetamacSrmWeb.getConstants().representationFacetTextType());

        isSequence = new ViewTextItem(RepresentationDS.FACET_IS_SEQUENCE, MetamacSrmWeb.getConstants().representationFacetIsSequence());
        isSequence.setShowIfCondition(FacetFormUtils.getIsSequenceIfFunction());

        interval = new ViewTextItem(RepresentationDS.FACET_INTERVAL, MetamacSrmWeb.getConstants().representationFacetInterval());
        interval.setShowIfCondition(FacetFormUtils.getIntervalIfFunction());

        startValue = new ViewTextItem(RepresentationDS.FACET_START_VALUE, MetamacSrmWeb.getConstants().representationFacetStartValue());
        startValue.setShowIfCondition(FacetFormUtils.getStartValueIfFunction());

        endValue = new ViewTextItem(RepresentationDS.FACET_END_VALUE, MetamacSrmWeb.getConstants().representationFacetEndValue());
        endValue.setShowIfCondition(FacetFormUtils.getEndValueIfFunction());

        timeInterval = new ViewTextItem(RepresentationDS.FACET_TIME_INTERVAL, MetamacSrmWeb.getConstants().representationFacetTimeInterval());
        timeInterval.setShowIfCondition(FacetFormUtils.getTimeIntervalIfFunction());

        startTime = new ViewTextItem(RepresentationDS.FACET_START_VALUE, MetamacSrmWeb.getConstants().representationFacetStartTime());
        startTime.setShowIfCondition(FacetFormUtils.getStartTimeIfFunction());

        endTime = new ViewTextItem(RepresentationDS.FACET_END_TIME, MetamacSrmWeb.getConstants().representationFacetEndTime());
        endTime.setShowIfCondition(FacetFormUtils.getEndTimeIfFunction());

        minLength = new ViewTextItem(RepresentationDS.FACET_MIN_LENGTH, MetamacSrmWeb.getConstants().representationFacetMinLength());
        minLength.setShowIfCondition(FacetFormUtils.getMinLengthIfFunction());

        maxLength = new ViewTextItem(RepresentationDS.FACET_MAX_LENGTH, MetamacSrmWeb.getConstants().representationFacetMaxLength());
        maxLength.setShowIfCondition(FacetFormUtils.getMaxLengthIfFunction());

        minValue = new ViewTextItem(RepresentationDS.FACET_MIN_VALUE, MetamacSrmWeb.getConstants().representationFacetMinValue());
        minValue.setShowIfCondition(FacetFormUtils.getMinValueIfFunction());

        maxValue = new ViewTextItem(RepresentationDS.FACET_MAX_VALUE, MetamacSrmWeb.getConstants().representationFacetMaxValue());
        maxValue.setShowIfCondition(FacetFormUtils.getMaxValueIfFunction());

        decimals = new ViewTextItem(RepresentationDS.FACET_DECIMALS, MetamacSrmWeb.getConstants().representationFacetDecimals());
        decimals.setShowIfCondition(FacetFormUtils.getDecimalsIfFunction());

        pattern = new ViewTextItem(RepresentationDS.FACET_PATTERN, MetamacSrmWeb.getConstants().representationFacetPattern());
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

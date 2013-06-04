package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

public class ViewQuantityForm extends BaseQuantityForm {

    public ViewQuantityForm(String groupTitle) {
        super(groupTitle);

        // Its important to set the value of the QuantityType (no translated name!) in a hidden field. It is required by the validators (see BaseQuantityForm).
        ViewTextItem type = new ViewTextItem(ConceptDS.QUANTITY_TYPE, getConstants().conceptQuantityType());
        type.setVisible(false);
        ViewTextItem typeText = new ViewTextItem(ConceptDS.QUANTITY_TYPE + "-text", getConstants().conceptQuantityType());

        ViewTextItem unitUuid = new ViewTextItem(ConceptDS.QUANTITY_UNIT_UUID, getConstants().conceptQuantityUnit());

        ViewMultiLanguageTextItem unitMultiplier = new ViewMultiLanguageTextItem(ConceptDS.QUANTITY_UNIT_MULTIPLIER, getConstants().conceptQuantityUnitMultiplier());

        ViewTextItem sigDigits = new ViewTextItem(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().conceptQuantitySignificantDigits());

        ViewTextItem decPlaces = new ViewTextItem(ConceptDS.QUANTITY_DECIMAL_PLACES, getConstants().conceptQuantityDecimalPlaces());

        ViewTextItem min = new ViewTextItem(ConceptDS.QUANTITY_MINIMUM, getConstants().conceptQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        ViewTextItem max = new ViewTextItem(ConceptDS.QUANTITY_MAXIMUM, getConstants().conceptQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        ViewTextItem denominatorUuid = new ViewTextItem(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, getConstants().conceptQuantityDenominatorConcept());
        denominatorUuid.setShowIfCondition(getDenominatorIfFunction());

        ViewTextItem numeratorUuid = new ViewTextItem(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, getConstants().conceptQuantityNumeratorConcept());
        numeratorUuid.setShowIfCondition(getNumeratorIfFunction());

        ViewTextItem isPercentange = new ViewTextItem(ConceptDS.QUANTITY_IS_PERCENTAGE, getConstants().conceptQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());

        ViewTextItem percentageOf = new ViewTextItem(ConceptDS.QUANTITY_PERCENTAGE_OF, getConstants().conceptQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        ViewTextItem indexBaseType = new ViewTextItem(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getConstants().conceptQuantityIndexMetadata());
        indexBaseType.setVisible(false);
        ViewTextItem indexBaseTypeText = new ViewTextItem(ConceptDS.QUANTITY_INDEX_BASE_TYPE + "-text", getConstants().conceptQuantityIndexMetadata());
        indexBaseTypeText.setShowIfCondition(getIndexBaseTypeIfFunction());

        ViewTextItem baseValue = new ViewTextItem(ConceptDS.QUANTITY_BASE_VALUE, getConstants().conceptQuantityBaseValue());
        baseValue.setShowIfCondition(getBaseValueIfFunction());

        ViewTextItem baseTime = new ViewTextItem(ConceptDS.QUANTITY_BASE_TIME, getConstants().conceptQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());

        ViewTextItem baseLocation = new ViewTextItem(ConceptDS.QUANTITY_BASE_LOCATION, getConstants().conceptQuantityBaseLocation());
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());

        ViewTextItem baseQuantityIndUuid = new ViewTextItem(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, getConstants().conceptQuantityBaseQuantity());
        baseQuantityIndUuid.setShowIfCondition(getBaseQuantityIfFunction());

        setFields(type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentange, percentageOf, indexBaseType, indexBaseTypeText, baseValue,
        /* baseTime, baseLocation, */baseQuantityIndUuid);
    }
    // public void setValue(QuantityDto quantityDto) {
    // clearValues();
    // if (quantityDto != null) {
    // setValue(ConceptDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : "");
    // setValue(ConceptDS.QUANTITY_TYPE + "-text", quantityDto.getType() != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityDto.getType().toString()) : "");
    // setValue(ConceptDS.QUANTITY_UNIT_UUID, getQuantityUnitTitle(quantityDto.getUnitUuid()));
    // setValue(ConceptDS.QUANTITY_UNIT_MULTIPLIER, RecordUtils.getInternationalStringRecord(quantityDto.getUnitMultiplierLabel()));
    // setValue(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits() != null ? quantityDto.getSignificantDigits().toString() : "");
    // setValue(ConceptDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces().toString() : "");
    // setValue(ConceptDS.QUANTITY_MINIMUM, quantityDto.getMinimum() != null ? quantityDto.getMinimum().toString() : "");
    // setValue(ConceptDS.QUANTITY_MAXIMUM, quantityDto.getMaximum() != null ? quantityDto.getMaximum().toString() : "");
    //
    // setValue(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, ""); // Value set in setIndicatorQuantityDenominator method
    // if (!StringUtils.isBlank(quantityDto.getDenominatorIndicatorUuid())) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).retrieveQuantityDenominatorIndicator(quantityDto.getDenominatorIndicatorUuid());
    // }
    // }
    //
    // setValue(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, ""); // Value set in setIndicatorQuantityNumerator method
    // if (!StringUtils.isBlank(quantityDto.getNumeratorIndicatorUuid())) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).retrieveQuantityNumeratorIndicator(quantityDto.getNumeratorIndicatorUuid());
    // }
    // }
    //
    // setValue(ConceptDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants()
    // .no()) : "");
    // setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null ? getIndexBaseTypeEnum(quantityDto).toString() : "");
    // setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE + "-text", getIndexBaseType(quantityDto));
    // setValue(ConceptDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue() != null ? quantityDto.getBaseValue().toString() : "");
    // setValue(ConceptDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());
    //
    // setValue(ConceptDS.QUANTITY_BASE_LOCATION, ""); // Base location set in setGeographicalValue method
    // if (!StringUtils.isBlank(quantityDto.getBaseLocationUuid())) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValue(quantityDto.getBaseLocationUuid());
    // }
    // }
    //
    // setValue(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, ""); // Value set in setIndicatorQuantityIndicatorBase method
    // if (!StringUtils.isBlank(quantityDto.getBaseQuantityIndicatorUuid())) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).retrieveQuantityIndicatorBase(quantityDto.getBaseQuantityIndicatorUuid());
    // }
    // }
    //
    // setValue(ConceptDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
    // }
    // }
    //
    // public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
    // setValue(ConceptDS.QUANTITY_BASE_LOCATION, geographicalValueDto != null
    // ? geographicalValueDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle())
    // : new String());
    // }
    //
    // public void setIndicatorQuantityDenominator(IndicatorDto indicator) {
    // setValue(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicator.getCode(), indicator.getTitle()));
    // }
    //
    // public void setIndicatorQuantityNumerator(IndicatorDto indicator) {
    // setValue(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicator.getCode(), indicator.getTitle()));
    // }
    //
    // public void setIndicatorQuantityIndicatorBase(IndicatorDto indicator) {
    // setValue(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicator.getCode(), indicator.getTitle()));
    // }
}

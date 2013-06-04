package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.concept.dto.QuantityDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.SearchWindow;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;

public class QuantityForm extends BaseQuantityForm {

    private QuantityDto  quantityDto;
    // private IndicatorDto indicatorDto;

    private SearchWindow indicatorDenominatorSearchWindow;
    private SearchWindow indicatorNumeratorSearchWindow;
    private SearchWindow indicatorBaseSearchWindow;

    public QuantityForm(String groupTitle) {
        super(groupTitle);

        CustomSelectItem type = new CustomSelectItem(ConceptDS.QUANTITY_TYPE, getConstants().conceptQuantityType());
        type.setValueMap(CommonUtils.getQuantityTypeHashMap());
        type.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                QuantityForm.this.markForRedraw();
            }
        });
        type.setValidators(getQuantityRequiredIfValidator());

        CustomSelectItem unitUuid = new CustomSelectItem(ConceptDS.QUANTITY_UNIT_UUID, getConstants().conceptQuantityUnit());
        unitUuid.setValidators(getQuantityRequiredIfValidator());

        CustomSelectItem unitMultiplier = new CustomSelectItem(ConceptDS.QUANTITY_UNIT_MULTIPLIER, getConstants().conceptQuantityUnitMultiplier());
        unitMultiplier.setValidators(getQuantityRequiredIfValidator());

        CustomIntegerItem sigDigits = new CustomIntegerItem(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().conceptQuantitySignificantDigits());

        CustomIntegerItem decPlaces = new CustomIntegerItem(ConceptDS.QUANTITY_DECIMAL_PLACES, getConstants().conceptQuantityDecimalPlaces());
        decPlaces.setRequired(true);

        CustomIntegerItem min = new CustomIntegerItem(ConceptDS.QUANTITY_MINIMUM, getConstants().conceptQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        CustomIntegerItem max = new CustomIntegerItem(ConceptDS.QUANTITY_MAXIMUM, getConstants().conceptQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        // Search denominator indicator
        CustomTextItem searchDenominatorUuid = new CustomTextItem(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, getConstants().conceptQuantityDenominatorConcept());
        searchDenominatorUuid.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        // searchDenominatorUuid.setValidators(getIndicatorSelectedValidator());
        // SearchViewTextItem searchDenominatorText = getSearchDenominatorTextItem();

        // Search numerator indicator
        CustomTextItem searchNumeratorUuid = new CustomTextItem(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_UUID, getConstants().conceptQuantityNumeratorConcept());
        searchNumeratorUuid.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        // searchNumeratorUuid.setValidators(getIndicatorSelectedValidator());
        // SearchViewTextItem searchNumeratorText = getSearchNumeratorTextItem();

        CustomCheckboxItem isPercentange = new CustomCheckboxItem(ConceptDS.QUANTITY_IS_PERCENTAGE, getConstants().conceptQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());

        MultiLanguageTextItem percentageOf = new MultiLanguageTextItem(ConceptDS.QUANTITY_PERCENTAGE_OF, getConstants().conceptQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        // CustomSelectItem indexBaseType = new CustomSelectItem(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getConstants().conceptQuantityIndexMetadata());
        // indexBaseType.setValueMap(getQuantityIndexBaseTypeValueMap());
        // indexBaseType.setShowIfCondition(getIndexBaseTypeIfFunction());
        // indexBaseType.addChangedHandler(new ChangedHandler() {
        //
        // @Override
        // public void onChanged(ChangedEvent event) {
        // QuantityForm.this.markForRedraw();
        // }
        // });

        CustomIntegerItem baseValue = new CustomIntegerItem(ConceptDS.QUANTITY_BASE_VALUE, getConstants().conceptQuantityBaseValue());
        baseValue.setRequired(true);
        baseValue.setShowIfCondition(getBaseValueIfFunction());
        //
        // RequiredTextItem baseTime = new RequiredTextItem(ConceptDS.QUANTITY_BASE_TIME, getConstants().conceptQuantityBaseTime());
        // baseTime.setShowIfCondition(getBaseTimeIfFunction());
        // baseTime.setValidators(TimeVariableWebUtils.getTimeCustomValidator());

        // final GeographicalSelectItem baseLocation = new GeographicalSelectItem(ConceptDS.QUANTITY_BASE_LOCATION, getConstants().conceptQuantityBaseLocation());
        // baseLocation.setRequired(true);
        // baseLocation.setShowIfCondition(getBaseLocationIfFunction());
        // baseLocation.getGeoGranularitySelectItem().addChangedHandler(new ChangedHandler() {
        //
        // @Override
        // public void onChanged(ChangedEvent event) {
        // // Clear geographical value
        // baseLocation.setGeoValuesValueMap(new LinkedHashMap<String, String>());
        // baseLocation.setGeoValue(new String());
        // // Set values with selected granularity
        // if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
        // if (uiHandlers instanceof IndicatorUiHandler) {
        // ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValues(event.getValue().toString());
        // }
        // }
        // }
        // });

        // Search indicator base
        TextItem searchIndicatorBaseUuid = new TextItem(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().conceptQuantityBaseQuantity());
        searchIndicatorBaseUuid.setRequired(true);
        searchIndicatorBaseUuid.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        // searchIndicatorBaseUuid.setValidators(getIndicatorSelectedValidator());
        // SearchViewTextItem searchIndicatorBaseText = getSearchIndicatorBaseTextItem();

        setFields(type, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, searchDenominatorUuid/* , searchDenominatorText */, searchNumeratorUuid/* , searchNumeratorText */, isPercentange,
                percentageOf, /* indexBaseType, */baseValue, /* baseTime, baseLocation, */searchIndicatorBaseUuid /* , searchIndicatorBaseText */);
    }

    // public void setValue(QuantityDto quantityDto) {
    // this.quantityDto = quantityDto;
    // clearValues();
    // if (quantityDto != null) {
    // setValue(ConceptDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : null);
    // setValue(ConceptDS.QUANTITY_UNIT_UUID, quantityDto.getUnitUuid());
    // setValue(ConceptDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier());
    // if (quantityDto.getSignificantDigits() != null) {
    // setValue(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits());
    // }
    // setValue(ConceptDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces() : 2);
    // if (quantityDto.getMinimum() != null) {
    // setValue(ConceptDS.QUANTITY_MINIMUM, quantityDto.getMinimum());
    // }
    // if (quantityDto.getMaximum() != null) {
    // setValue(ConceptDS.QUANTITY_MAXIMUM, quantityDto.getMaximum());
    // }
    //
    // setValue(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, quantityDto.getDenominatorIndicatorUuid());
    // setValue(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, quantityDto.getDenominatorIndicatorUuid()); // Value set in setIndicatorQuantityDenominator method
    //
    // setValue(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_UUID, quantityDto.getNumeratorIndicatorUuid());
    // setValue(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, quantityDto.getNumeratorIndicatorUuid()); // Value set in setIndicatorQuantityNumerator method
    //
    // setValue(ConceptDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage().booleanValue() : false);
    // setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null ? getIndexBaseTypeEnum(quantityDto).toString() : "");
    // if (quantityDto.getBaseValue() != null) {
    // setValue(ConceptDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue());
    // }
    // setValue(ConceptDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());
    //
    // // Base location granularity set in setGeographicalGranularity method
    // ((GeographicalSelectItem) getItem(ConceptDS.QUANTITY_BASE_LOCATION)).setGeoGranularity(new String());
    // ((GeographicalSelectItem) getItem(ConceptDS.QUANTITY_BASE_LOCATION)).setGeoValue(quantityDto.getBaseLocationUuid());
    //
    // setValue(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, quantityDto.getBaseQuantityIndicatorUuid());
    // setValue(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, quantityDto.getBaseQuantityIndicatorUuid()); // Value set in setIndicatorQuantityIndicatorBase method
    //
    // setValue(ConceptDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
    // }
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
    //
    // public QuantityDto getValue() {
    // quantityDto.setType((getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) ? QuantityTypeEnum
    // .valueOf(getValueAsString(ConceptDS.QUANTITY_TYPE)) : null);
    // quantityDto.setUnitUuid(CommonUtils.getUuidString(getValueAsString(ConceptDS.QUANTITY_UNIT_UUID)));
    // quantityDto.setUnitMultiplier(getValue(ConceptDS.QUANTITY_UNIT_MULTIPLIER) != null ? Integer.valueOf(getValueAsString(ConceptDS.QUANTITY_UNIT_MULTIPLIER)) : null);
    // quantityDto.setSignificantDigits(getValue(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS) != null ? (Integer) getValue(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS) : null);
    // quantityDto.setDecimalPlaces(getValue(ConceptDS.QUANTITY_DECIMAL_PLACES) != null ? (Integer) getValue(ConceptDS.QUANTITY_DECIMAL_PLACES) : null);
    // // Only set value if item is visible (these item are quantity type dependent)
    // quantityDto.setMinimum(getItem(ConceptDS.QUANTITY_MINIMUM).isVisible() ? (getValue(ConceptDS.QUANTITY_MINIMUM) != null ? (Integer) getValue(ConceptDS.QUANTITY_MINIMUM) : null) : null);
    // quantityDto.setMaximum(getItem(ConceptDS.QUANTITY_MAXIMUM).isVisible() ? (getValue(ConceptDS.QUANTITY_MAXIMUM) != null ? (Integer) getValue(ConceptDS.QUANTITY_MAXIMUM) : null) : null);
    // quantityDto.setDenominatorIndicatorUuid(getItem(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT).isVisible() ? CommonUtils
    // .getUuidString(getValueAsString(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_UUID)) : null);
    // quantityDto.setNumeratorIndicatorUuid(getItem(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT).isVisible() ? CommonUtils
    // .getUuidString(getValueAsString(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_UUID)) : null);
    // quantityDto.setIsPercentage(getItem(ConceptDS.QUANTITY_IS_PERCENTAGE).isVisible() ? (getValue(ConceptDS.QUANTITY_IS_PERCENTAGE) != null ? Boolean
    // .valueOf((Boolean) getValue(ConceptDS.QUANTITY_IS_PERCENTAGE)) : false) : null);
    // quantityDto.setPercentageOf(getItem(ConceptDS.QUANTITY_PERCENTAGE_OF).isVisible() ? ((MultiLanguageTextItem) getItem(ConceptDS.QUANTITY_PERCENTAGE_OF)).getValue() : null);
    // quantityDto.setBaseValue(getItem(ConceptDS.QUANTITY_BASE_VALUE).isVisible()
    // ? (getValue(ConceptDS.QUANTITY_BASE_VALUE) != null ? (Integer) getValue(ConceptDS.QUANTITY_BASE_VALUE) : null)
    // : null);
    // quantityDto.setBaseTime(getItem(ConceptDS.QUANTITY_BASE_TIME).isVisible() ? getValueAsString(ConceptDS.QUANTITY_BASE_TIME) : null);
    // quantityDto.setBaseLocationUuid(getItem(ConceptDS.QUANTITY_BASE_LOCATION).isVisible() ? CommonUtils.getUuidString(((GeographicalSelectItem) getItem(ConceptDS.QUANTITY_BASE_LOCATION))
    // .getSelectedGeoValue()) : null);
    // quantityDto.setBaseQuantityIndicatorUuid(getItem(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT).isVisible() ? CommonUtils
    // .getUuidString(getValueAsString(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID)) : null);
    // return quantityDto;
    // }
    //
    // public void setIndicatorListQuantityDenominator(List<IndicatorSummaryDto> indicators) {
    // if (indicatorDenominatorSearchWindow != null) {
    // indicatorDenominatorSearchWindow.setIndicatorList(indicators);
    // }
    // }
    //
    // public void setIndicatorListQuantityNumerator(List<IndicatorSummaryDto> indicators) {
    // if (indicatorNumeratorSearchWindow != null) {
    // indicatorNumeratorSearchWindow.setIndicatorList(indicators);
    // }
    // }
    //
    // public void setIndicatorListQuantityIndicatorBase(List<IndicatorSummaryDto> indicators) {
    // if (indicatorBaseSearchWindow != null) {
    // indicatorBaseSearchWindow.setIndicatorList(indicators);
    // }
    // }
    //
    // @Override
    // public void setQuantityUnits(List<QuantityUnitDto> units) {
    // super.setQuantityUnits(units);
    // LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
    // for (QuantityUnitDto unit : units) {
    // valueMap.put(unit.getUuid(), InternationalStringUtils.getLocalisedString(unit.getTitle()));
    // }
    // ((SelectItem) getItem(ConceptDS.QUANTITY_UNIT_UUID)).setValueMap(valueMap);
    // }
    //
    // public void setIndicator(IndicatorDto indicatorDto) {
    // this.indicatorDto = indicatorDto;
    // }
    //
    // private CustomValidator getIndicatorSelectedValidator() {
    // CustomValidator validator = new CustomValidator() {
    //
    // @Override
    // protected boolean condition(Object value) {
    // return (value != null && value.equals(indicatorDto.getUuid())) ? false : true;
    // }
    // };
    // validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
    // return validator;
    // }
    //
    // public void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos) {
    // ((CustomSelectItem) getItem(ConceptDS.QUANTITY_UNIT_MULTIPLIER)).setValueMap(CommonUtils.getUnitMultiplierValueMap(unitMultiplierDtos));
    // }
    //
    // public void setGeographicalGranularities(List<GeographicalGranularityDto> granularityDtos) {
    // ((GeographicalSelectItem) getItem(ConceptDS.QUANTITY_BASE_LOCATION)).setGeoGranularitiesValueMap(CommonUtils.getGeographicalGranularituesValueMap(granularityDtos));
    // }
    //
    // public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
    // ((GeographicalSelectItem) getItem(ConceptDS.QUANTITY_BASE_LOCATION)).setGeoValuesValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    // }
    //
    // public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
    // if (geographicalValueDto != null) {
    // ((GeographicalSelectItem) getItem(ConceptDS.QUANTITY_BASE_LOCATION)).setGeoGranularity(geographicalValueDto.getGranularityUuid());
    // // Make sure value map is set properly
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValues(geographicalValueDto.getGranularityUuid());
    // }
    // }
    // }

    public RequiredIfValidator getQuantityRequiredIfValidator() {
        return new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                // TODO return !IndicatorProcStatusEnum.DRAFT.equals(indicatorDto.getProcStatus());
                return true;
            }
        });
    }

    // ITEMS

    // private SearchViewTextItem getSearchDenominatorTextItem() {
    // SearchViewTextItem searchDenominatorText = new SearchViewTextItem(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, getConstants().conceptQuantityDenominatorIndicator());
    // searchDenominatorText.setShowIfCondition(getDenominatorIfFunction());
    // searchDenominatorText.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {
    //
    // @Override
    // public void onFormItemClick(FormItemIconClickEvent event) {
    // indicatorDenominatorSearchWindow = new IndicatorsSearchWindow(getConstants().indicatorSelection());
    // indicatorDenominatorSearchWindow.getSearchButton().addClickHandler(new ClickHandler() {
    //
    // @Override
    // public void onClick(ClickEvent event) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).searchIndicatorsQuantityDenominator(indicatorDenominatorSearchWindow.getSearchCriteria());
    // }
    // }
    // });
    //
    // indicatorDenominatorSearchWindow.getAceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
    //
    // @Override
    // public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
    // IndicatorSummaryDto indicatorSummaryDto = indicatorDenominatorSearchWindow.getSelectedIndicator();
    // indicatorDenominatorSearchWindow.destroy();
    // QuantityForm.this.setValue(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, indicatorSummaryDto != null ? indicatorSummaryDto.getUuid() : new String());
    // QuantityForm.this.setValue(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT,
    // indicatorSummaryDto != null ? CommonUtils.getIndicatorText(indicatorSummaryDto.getCode(), CommonUtils.getIndicatorTitle(indicatorSummaryDto)) : new String());
    // QuantityForm.this.getItem(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT).validate();
    // }
    // });
    // }
    // });
    // CustomValidator validator = new CustomValidator() {
    //
    // @Override
    // protected boolean condition(Object value) {
    // return QuantityForm.this.getItem(ConceptDS.QUANTITY_DENOMINATOR_INDICATOR_UUID).validate();
    // }
    // };
    // validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
    // searchDenominatorText.setValidators(validator);
    //
    // return searchDenominatorText;
    // }

    // private SearchViewTextItem getSearchNumeratorTextItem() {
    // SearchViewTextItem searchNumeratorText = new SearchViewTextItem(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, getConstants().conceptQuantityNumeratorIndicator());
    // searchNumeratorText.setShowIfCondition(getNumeratorIfFunction());
    // searchNumeratorText.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {
    //
    // @Override
    // public void onFormItemClick(FormItemIconClickEvent event) {
    // indicatorNumeratorSearchWindow = new IndicatorsSearchWindow(getConstants().indicatorSelection());
    // indicatorNumeratorSearchWindow.getSearchButton().addClickHandler(new ClickHandler() {
    //
    // @Override
    // public void onClick(ClickEvent event) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).searchIndicatorsQuantityNumerator(indicatorNumeratorSearchWindow.getSearchCriteria());
    // }
    // }
    // });
    //
    // indicatorNumeratorSearchWindow.getAceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
    //
    // @Override
    // public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
    // IndicatorSummaryDto indicatorSummaryDto = indicatorNumeratorSearchWindow.getSelectedIndicator();
    // indicatorNumeratorSearchWindow.destroy();
    // QuantityForm.this.setValue(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_UUID, indicatorSummaryDto != null ? indicatorSummaryDto.getUuid() : new String());
    // QuantityForm.this.setValue(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT,
    // indicatorSummaryDto != null ? CommonUtils.getIndicatorText(indicatorSummaryDto.getCode(), CommonUtils.getIndicatorTitle(indicatorSummaryDto)) : new String());
    // QuantityForm.this.getItem(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_TEXT).validate();
    // }
    // });
    // }
    // });
    // CustomValidator validator = new CustomValidator() {
    //
    // @Override
    // protected boolean condition(Object value) {
    // return QuantityForm.this.getItem(ConceptDS.QUANTITY_NUMERATOR_INDICATOR_UUID).validate();
    // }
    // };
    // validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
    // searchNumeratorText.setValidators(validator);
    //
    // return searchNumeratorText;
    // }

    // private SearchViewTextItem getSearchIndicatorBaseTextItem() {
    // SearchViewTextItem searchIndicatorBaseText = new SearchViewTextItem(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, getConstants().conceptQuantityBaseQuantityConcept());
    // searchIndicatorBaseText.setRequired(true);
    // searchIndicatorBaseText.setShowIfCondition(getBaseQuantityIfFunction());
    // searchIndicatorBaseText.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {
    //
    // @Override
    // public void onFormItemClick(FormItemIconClickEvent event) {
    // indicatorBaseSearchWindow = new IndicatorsSearchWindow(getConstants().indicatorSelection());
    // indicatorBaseSearchWindow.getSearchButton().addClickHandler(new ClickHandler() {
    //
    // @Override
    // public void onClick(ClickEvent event) {
    // if (uiHandlers instanceof IndicatorUiHandler) {
    // ((IndicatorUiHandler) uiHandlers).searchIndicatorsQuantityIndicatorBase(indicatorBaseSearchWindow.getSearchCriteria());
    // }
    // }
    // });
    //
    // indicatorBaseSearchWindow.getAceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
    //
    // @Override
    // public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
    // IndicatorSummaryDto indicatorDto = indicatorBaseSearchWindow.getSelectedIndicator();
    // indicatorBaseSearchWindow.destroy();
    // QuantityForm.this.setValue(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, indicatorDto != null ? indicatorDto.getUuid() : new String());
    // QuantityForm.this.setValue(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT,
    // indicatorDto != null ? CommonUtils.getIndicatorText(indicatorDto.getCode(), CommonUtils.getIndicatorTitle(indicatorDto)) : new String());
    // QuantityForm.this.getItem(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT).validate();
    // }
    // });
    // }
    // });
    // CustomValidator validator = new CustomValidator() {
    //
    // @Override
    // protected boolean condition(Object value) {
    // return QuantityForm.this.getItem(ConceptDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID).validate();
    // }
    // };
    // validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
    // searchIndicatorBaseText.setValidators(validator);
    //
    // return searchIndicatorBaseText;
    // }
}

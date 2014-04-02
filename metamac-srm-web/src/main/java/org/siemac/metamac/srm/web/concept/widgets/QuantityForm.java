package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.QuantityDto;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared.QuantityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.widgets.SearchItemItem;
import org.siemac.metamac.srm.web.concept.enums.QuantityIndexBaseTypeEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.TimeVariableWebUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;

public class QuantityForm extends BaseQuantityForm {

    // private QuantityDto quantityDto;

    private SearchConceptForQuantityDenominatorItem searchDenominatorItem;
    private SearchConceptForQuantityNumeratorItem   searchNumeratorItem;
    private SearchConceptForQuantityBaseItem        searchBaseItem;
    private SearchCodeForQuantityUnitItem           searchUnitItem;
    private SearchCodeForQuantityBaseLocationItem   searchBaseLocationItem;

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
        type.setRequired(false);

        searchUnitItem = createQuantityUnitItem(ConceptDS.QUANTITY_UNIT, getConstants().conceptQuantityUnit());
        searchUnitItem.setValidators(getQuantityRequiredIfValidator());
        searchUnitItem.setShowIfCondition(getShowIfAnyQuantityType());

        CustomSelectItem unitSymbol = new CustomSelectItem(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION, getConstants().conceptQuantityUnitSymbolPosition());
        unitSymbol.setValueMap(getQuantityUnitSymbolValueMap());
        unitSymbol.setValidators(getQuantityRequiredIfValidator());
        unitSymbol.setShowIfCondition(getShowIfAnyQuantityType());

        CustomIntegerItem unitMultiplier = new CustomIntegerItem(ConceptDS.QUANTITY_UNIT_MULTIPLIER, getConstants().conceptQuantityUnitMultiplier());
        unitMultiplier.setValidators(getQuantityRequiredIfValidator(), getUnitMultiplierValidator());
        unitMultiplier.setShowIfCondition(getShowIfAnyQuantityType());

        CustomIntegerItem sigDigits = new CustomIntegerItem(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().conceptQuantitySignificantDigits());
        sigDigits.setShowIfCondition(getShowIfAnyQuantityType());

        CustomIntegerItem decPlaces = new CustomIntegerItem(ConceptDS.QUANTITY_DECIMAL_PLACES, getConstants().conceptQuantityDecimalPlaces());
        decPlaces.setRequired(true);
        decPlaces.setShowIfCondition(getShowIfAnyQuantityType());

        CustomIntegerItem min = new CustomIntegerItem(ConceptDS.QUANTITY_MINIMUM, getConstants().conceptQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        CustomIntegerItem max = new CustomIntegerItem(ConceptDS.QUANTITY_MAXIMUM, getConstants().conceptQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        // Search denominator indicator
        searchDenominatorItem = createQuantityDenominatorItem(ConceptDS.QUANTITY_DENOMINATOR, getConstants().conceptQuantityDenominatorConcept());
        searchDenominatorItem.setShowIfCondition(getDenominatorIfFunction());

        // Search numerator indicator
        searchNumeratorItem = createQuantityNumeratorItem(ConceptDS.QUANTITY_NUMERATOR, getConstants().conceptQuantityNumeratorConcept());
        searchNumeratorItem.setShowIfCondition(getNumeratorIfFunction());

        CustomCheckboxItem isPercentange = new CustomCheckboxItem(ConceptDS.QUANTITY_IS_PERCENTAGE, getConstants().conceptQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());
        isPercentange.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                QuantityForm.this.markForRedraw();
            }
        });

        MultiLanguageTextItem percentageOf = new MultiLanguageTextItem(ConceptDS.QUANTITY_PERCENTAGE_OF, getConstants().conceptQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        CustomSelectItem indexBaseType = new CustomSelectItem(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getConstants().conceptQuantityIndexMetadata());
        indexBaseType.setValueMap(getQuantityIndexBaseTypeValueMap());
        indexBaseType.setShowIfCondition(getIndexBaseTypeIfFunction());
        indexBaseType.setValidators(getQuantityIndexBaseTypeRequiredIfValidator());
        indexBaseType.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                QuantityForm.this.markForRedraw();
            }
        });

        CustomIntegerItem baseValue = new CustomIntegerItem(ConceptDS.QUANTITY_BASE_VALUE, getConstants().conceptQuantityBaseValue());
        baseValue.setRequired(true);
        baseValue.setShowIfCondition(getBaseValueIfFunction());

        RequiredTextItem baseTime = new RequiredTextItem(ConceptDS.QUANTITY_BASE_TIME, getConstants().conceptQuantityBaseTime());
        baseTime.setRequired(true);
        baseTime.setShowIfCondition(getBaseTimeIfFunction());
        baseTime.setValidators(TimeVariableWebUtils.getTimeCustomValidator());

        // Search base location
        searchBaseLocationItem = createQuantityBaseLocationItem(ConceptDS.QUANTITY_BASE_LOCATION, getConstants().conceptQuantityBaseLocation());
        searchBaseLocationItem.setShowIfCondition(getBaseLocationIfFunction());

        // Search indicator base
        searchBaseItem = createQuantityBaseItem(ConceptDS.QUANTITY_BASE_QUANTITY, getConstants().conceptQuantityBaseQuantity());
        searchBaseItem.setShowIfCondition(getBaseQuantityIfFunction());

        setFields(type, searchUnitItem, unitSymbol, unitMultiplier, sigDigits, decPlaces, min, max, searchDenominatorItem, searchNumeratorItem, isPercentange, percentageOf, indexBaseType, baseValue,
                baseTime, searchBaseLocationItem, searchBaseItem);
    }

    public void setCodelistsForQuantityUnitFilter(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchUnitItem != null && searchUnitItem.getSearchWindow() != null) {
            searchUnitItem.getSearchWindow().setFilterRelatedResources(resources);
            searchUnitItem.getSearchWindow().refreshFilterListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setCodesForQuantityUnit(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchUnitItem != null && searchUnitItem.getSearchWindow() != null) {
            searchUnitItem.getSearchWindow().setRelatedResources(resources);
            searchUnitItem.getSearchWindow().refreshListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setConceptSchemesForQuantityBaseFilter(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchBaseItem != null && searchBaseItem.getSearchWindow() != null) {
            searchBaseItem.getSearchWindow().setFilterRelatedResources(resources);
            searchBaseItem.getSearchWindow().refreshFilterListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setCodelistsForQuantityBaseLocationFilter(List<RelatedResourceDto> codelistDtos, int firstResult, int totalResults) {
        if (searchBaseLocationItem != null && searchBaseLocationItem.getSearchWindow() != null) {
            searchBaseLocationItem.getSearchWindow().setFilterRelatedResources(codelistDtos);
            searchBaseLocationItem.getSearchWindow().refreshFilterListPaginationInfo(firstResult, codelistDtos.size(), totalResults);
        }
    }

    public void setCodeThatCanBeQuantityBaseLocation(List<RelatedResourceDto> codesDtos, int firstResult, int totalResults) {
        if (searchBaseLocationItem != null && searchBaseLocationItem.getSearchWindow() != null) {
            searchBaseLocationItem.getSearchWindow().setRelatedResources(codesDtos);
            searchBaseLocationItem.getSearchWindow().refreshListPaginationInfo(firstResult, codesDtos.size(), totalResults);
        }
    }

    public void setConceptsForQuantityBase(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchBaseItem != null && searchBaseItem.getSearchWindow() != null) {
            searchBaseItem.getSearchWindow().setRelatedResources(resources);
            searchBaseItem.getSearchWindow().refreshListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setConceptSchemesForQuantityNumeratorFilter(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchNumeratorItem != null && searchNumeratorItem.getSearchWindow() != null) {
            searchNumeratorItem.getSearchWindow().setFilterRelatedResources(resources);
            searchNumeratorItem.getSearchWindow().refreshFilterListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setConceptsForQuantityNumerator(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchNumeratorItem != null && searchNumeratorItem.getSearchWindow() != null) {
            searchNumeratorItem.getSearchWindow().setRelatedResources(resources);
            searchNumeratorItem.getSearchWindow().refreshListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setConceptSchemesForQuantityDenominatorFilter(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchDenominatorItem != null && searchDenominatorItem.getSearchWindow() != null) {
            searchDenominatorItem.getSearchWindow().setFilterRelatedResources(resources);
            searchDenominatorItem.getSearchWindow().refreshFilterListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setConceptsForQuantityDenominator(List<RelatedResourceDto> resources, int firstResult, int totalResults) {
        if (searchDenominatorItem != null && searchDenominatorItem.getSearchWindow() != null) {
            searchDenominatorItem.getSearchWindow().setRelatedResources(resources);
            searchDenominatorItem.getSearchWindow().refreshListPaginationInfo(firstResult, resources.size(), totalResults);
        }
    }

    public void setValue(QuantityDto quantityDto) {
        clearValues();
        if (quantityDto != null) {
            setValue(ConceptDS.QUANTITY_TYPE, quantityDto.getQuantityType() != null ? quantityDto.getQuantityType().toString() : "");
            if (quantityDto.getUnitCode() != null) {
                setRelatedResourceDtoValue(ConceptDS.QUANTITY_UNIT, quantityDto.getUnitCode());
            }
            setValue(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION, quantityDto.getUnitSymbolPosition() != null ? quantityDto.getUnitSymbolPosition().toString() : "");
            setValue(ConceptDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier());
            if (quantityDto.getSignificantDigits() != null) {
                setValue(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits());
            }
            setValue(ConceptDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces() : 2);
            if (quantityDto.getMinimum() != null) {
                setValue(ConceptDS.QUANTITY_MINIMUM, quantityDto.getMinimum());
            }
            if (quantityDto.getMaximum() != null) {
                setValue(ConceptDS.QUANTITY_MAXIMUM, quantityDto.getMaximum());
            }

            if (quantityDto.getDenominator() != null) {
                setRelatedResourceDtoValue(ConceptDS.QUANTITY_DENOMINATOR, quantityDto.getDenominator());
            }

            if (quantityDto.getNumerator() != null) {
                setRelatedResourceDtoValue(ConceptDS.QUANTITY_NUMERATOR, quantityDto.getNumerator());
            }

            setValue(ConceptDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage().booleanValue() : true);
            setValue(ConceptDS.QUANTITY_PERCENTAGE_OF, quantityDto.getPercentageOf());

            setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null ? getIndexBaseTypeEnum(quantityDto).toString() : "");

            if (quantityDto.getBaseValue() != null) {
                setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE, QuantityIndexBaseTypeEnum.BASE_VALUE.toString());
                setValue(ConceptDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue());
            }

            if (quantityDto.getBaseTime() != null) {
                setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE, QuantityIndexBaseTypeEnum.BASE_TIME.toString());
                setValue(ConceptDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());
            }

            setRelatedResourceDtoValue(ConceptDS.QUANTITY_BASE_LOCATION, quantityDto.getBaseLocation());

            if (quantityDto.getBaseQuantity() != null) {
                setRelatedResourceDtoValue(ConceptDS.QUANTITY_BASE_QUANTITY, quantityDto.getBaseQuantity());
            }
        } else {
            setValue(ConceptDS.QUANTITY_IS_PERCENTAGE, true);
        }
    }

    public QuantityDto getValue(QuantityDto quantityDto) {
        if (!isVisible()) {
            return null;
        }
        if (getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !StringUtils.isEmpty(getValueAsString(ConceptDS.QUANTITY_TYPE))) {
            if (quantityDto == null) {
                quantityDto = new QuantityDto();
            }
            quantityDto.setQuantityType((getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) ? QuantityTypeEnum
                    .valueOf(getValueAsString(ConceptDS.QUANTITY_TYPE)) : null);
            quantityDto.setUnitSymbolPosition((getValueAsString(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION) != null && !getValueAsString(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION).isEmpty())
                    ? QuantityUnitSymbolPositionEnum.valueOf(getValueAsString(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION))
                    : null);

            quantityDto.setUnitCode(getRelatedResourceDtoValue(ConceptDS.QUANTITY_UNIT));
            quantityDto.setUnitMultiplier(getValueAsInteger(ConceptDS.QUANTITY_UNIT_MULTIPLIER));
            quantityDto.setSignificantDigits(getValueAsInteger(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS));
            quantityDto.setDecimalPlaces(getValueAsInteger(ConceptDS.QUANTITY_DECIMAL_PLACES));
            // Only set value if item is visible (these item are quantity type dependent)
            quantityDto.setMinimum(getItem(ConceptDS.QUANTITY_MINIMUM).isVisible() ? getValueAsInteger(ConceptDS.QUANTITY_MINIMUM) : null);
            quantityDto.setMaximum(getItem(ConceptDS.QUANTITY_MAXIMUM).isVisible() ? getValueAsInteger(ConceptDS.QUANTITY_MAXIMUM) : null);

            quantityDto.setDenominator(getRelatedResourceDtoValue(ConceptDS.QUANTITY_DENOMINATOR));
            quantityDto.setNumerator(getRelatedResourceDtoValue(ConceptDS.QUANTITY_NUMERATOR));

            quantityDto.setIsPercentage(getItem(ConceptDS.QUANTITY_IS_PERCENTAGE).isVisible() ? (getValue(ConceptDS.QUANTITY_IS_PERCENTAGE) != null ? Boolean
                    .valueOf((Boolean) getValue(ConceptDS.QUANTITY_IS_PERCENTAGE)) : false) : null);
            quantityDto.setPercentageOf(getItem(ConceptDS.QUANTITY_PERCENTAGE_OF).isVisible() ? getValueAsInternationalStringDto(ConceptDS.QUANTITY_PERCENTAGE_OF) : null);
            quantityDto.setBaseValue(getItem(ConceptDS.QUANTITY_BASE_VALUE).isVisible()
                    ? (getValue(ConceptDS.QUANTITY_BASE_VALUE) != null ? (Integer) getValue(ConceptDS.QUANTITY_BASE_VALUE) : null)
                    : null);
            quantityDto.setBaseTime(getItem(ConceptDS.QUANTITY_BASE_TIME).isVisible() ? getValueAsString(ConceptDS.QUANTITY_BASE_TIME) : null);

            quantityDto.setBaseLocation(getRelatedResourceDtoValue(ConceptDS.QUANTITY_BASE_LOCATION));
            quantityDto.setBaseQuantity(getRelatedResourceDtoValue(ConceptDS.QUANTITY_BASE_QUANTITY));
            return quantityDto;
        }
        return null;
    }

    private SearchCodeForQuantityUnitItem createQuantityUnitItem(final String name, String title) {
        final SearchCodeForQuantityUnitItem item = new SearchCodeForQuantityUnitItem(name, title, getCustomLinkItemNavigationClickHandler());
        item.setSaveClickHandler(getSearchItemSaveClickHandler(item, name));
        return item;
    }

    private SearchConceptForQuantityNumeratorItem createQuantityNumeratorItem(final String name, String title) {
        final SearchConceptForQuantityNumeratorItem item = new SearchConceptForQuantityNumeratorItem(name, title, getCustomLinkItemNavigationClickHandler());
        item.setSaveClickHandler(getSearchItemSaveClickHandler(item, name));
        return item;
    }

    private SearchConceptForQuantityDenominatorItem createQuantityDenominatorItem(final String name, String title) {
        final SearchConceptForQuantityDenominatorItem item = new SearchConceptForQuantityDenominatorItem(name, title, getCustomLinkItemNavigationClickHandler());
        item.setSaveClickHandler(getSearchItemSaveClickHandler(item, name));
        return item;
    }

    private SearchConceptForQuantityBaseItem createQuantityBaseItem(final String name, String title) {
        final SearchConceptForQuantityBaseItem item = new SearchConceptForQuantityBaseItem(name, title, getCustomLinkItemNavigationClickHandler());
        item.setSaveClickHandler(getSearchItemSaveClickHandler(item, name));
        return item;
    }

    private SearchCodeForQuantityBaseLocationItem createQuantityBaseLocationItem(final String name, String title) {
        final SearchCodeForQuantityBaseLocationItem item = new SearchCodeForQuantityBaseLocationItem(name, title, getCustomLinkItemNavigationClickHandler());
        item.setSaveClickHandler(getSearchItemSaveClickHandler(item, name));
        return item;
    }

    private ClickHandler getSearchItemSaveClickHandler(final SearchItemItem searchItem, final String itemName) {
        com.smartgwt.client.widgets.form.fields.events.ClickHandler clickHandler = new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                RelatedResourceDto code = searchItem.getSelectedItem();
                searchItem.markSearchWindowForDestroy();
                searchItem.setRelatedResource(code);
                validate(false);
            }
        };
        return clickHandler;
    }

    public RequiredIfValidator getQuantityRequiredIfValidator() {
        return new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                if (QuantityForm.this.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !QuantityForm.this.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    return true;
                }
                return false;
            }
        });
    }

    public RequiredIfValidator getQuantityIndexBaseTypeRequiredIfValidator() {
        return new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                if (QuantityForm.this.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !QuantityForm.this.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(QuantityForm.this.getValueAsString(ConceptDS.QUANTITY_TYPE));
                    if (QuantityUtils.isIndexOrExtension(type)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected CustomValidator getUnitMultiplierValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (value != null) {
                    Integer intValue = null;
                    if (value instanceof Integer) {
                        intValue = (Integer) value;
                    } else if (value instanceof String) {
                        try {
                            intValue = Integer.parseInt(value.toString());
                        } catch (Exception e) {
                        }
                    }
                    if (intValue != null) {
                        if (intValue > 0) {
                            double log = Math.log(intValue);
                            if (Math.pow(10, log) == Double.valueOf(intValue)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    return false;
                }
                return true;
            }
        };
        validator.setErrorMessage(MetamacSrmWeb.getMessages().errorConceptQuantityUnitMultiplier());
        return validator;
    }

    @Override
    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchUnitItem.setUiHandlers(uiHandlers);
        searchNumeratorItem.setUiHandlers(uiHandlers);
        searchDenominatorItem.setUiHandlers(uiHandlers);
        searchBaseItem.setUiHandlers(uiHandlers);
        searchBaseLocationItem.setUiHandlers(uiHandlers);
    }

    private void setRelatedResourceDtoValue(String name, RelatedResourceDto relatedResourceDto) {
        ((SearchItemItem) getField(name)).setRelatedResource(relatedResourceDto);
    }

    private RelatedResourceDto getRelatedResourceDtoValue(String name) {
        if (getItem(name).isVisible()) {
            return ((SearchItemItem) getField(name)).getRelatedResourceDto();
        }
        return null;
    }

    private Integer getValueAsInteger(String fieldName) {
        return FormItemUtils.getValueAsInteger((CustomIntegerItem) getItem(fieldName));
    }
}

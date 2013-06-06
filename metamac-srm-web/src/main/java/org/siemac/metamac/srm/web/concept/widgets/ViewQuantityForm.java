package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;

import org.siemac.metamac.srm.core.concept.dto.QuantityDto;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceLinkItem;
import org.siemac.metamac.srm.web.concept.enums.QuantityIndexBaseTypeEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class ViewQuantityForm extends BaseQuantityForm {

    public ViewQuantityForm(String groupTitle) {
        super(groupTitle);

        // Its important to set the value of the QuantityType (no translated name!) in a hidden field. It is required by the validators (see BaseQuantityForm).
        ViewTextItem type = new ViewTextItem(ConceptDS.QUANTITY_TYPE, getConstants().conceptQuantityType());
        type.setVisible(false);
        ViewTextItem typeText = new ViewTextItem(ConceptDS.QUANTITY_TYPE + "-text", getConstants().conceptQuantityType());

        RelatedResourceLinkItem unit = new RelatedResourceLinkItem(ConceptDS.QUANTITY_UNIT, getConstants().conceptQuantityUnit(), getCustomLinkItemNavigationClickHandler());
        unit.setShowIfCondition(getShowIfAnyQuantityType());
        
        ViewTextItem unitSymbol = new ViewTextItem(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION, getConstants().conceptQuantityUnitSymbolPosition());
        unitSymbol.setShowIfCondition(getShowIfAnyQuantityType());

        ViewTextItem unitMultiplier = new ViewTextItem(ConceptDS.QUANTITY_UNIT_MULTIPLIER, getConstants().conceptQuantityUnitMultiplier());
        unitMultiplier.setShowIfCondition(getShowIfAnyQuantityType());

        ViewTextItem sigDigits = new ViewTextItem(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().conceptQuantitySignificantDigits());
        sigDigits.setShowIfCondition(getShowIfAnyQuantityType());

        ViewTextItem decPlaces = new ViewTextItem(ConceptDS.QUANTITY_DECIMAL_PLACES, getConstants().conceptQuantityDecimalPlaces());
        decPlaces.setShowIfCondition(getShowIfAnyQuantityType());

        ViewTextItem min = new ViewTextItem(ConceptDS.QUANTITY_MINIMUM, getConstants().conceptQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        ViewTextItem max = new ViewTextItem(ConceptDS.QUANTITY_MAXIMUM, getConstants().conceptQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        RelatedResourceLinkItem denominator = new RelatedResourceLinkItem(ConceptDS.QUANTITY_DENOMINATOR, getConstants().conceptQuantityDenominatorConcept(), getCustomLinkItemNavigationClickHandler());
        denominator.setShowIfCondition(getDenominatorIfFunction());

        RelatedResourceLinkItem numerator = new RelatedResourceLinkItem(ConceptDS.QUANTITY_NUMERATOR, getConstants().conceptQuantityNumeratorConcept(), getCustomLinkItemNavigationClickHandler());
        numerator.setShowIfCondition(getNumeratorIfFunction());

        ViewTextItem isPercentage = new ViewTextItem(ConceptDS.QUANTITY_IS_PERCENTAGE, getConstants().conceptQuantityIsPercentage());
        isPercentage.setVisible(false);
        
        ViewTextItem isPercentageText = new ViewTextItem(ConceptDS.QUANTITY_IS_PERCENTAGE_TEXT, getConstants().conceptQuantityIsPercentage());
        isPercentageText.setShowIfCondition(getIsPercentageIfFunction());

        ViewMultiLanguageTextItem percentageOf = new ViewMultiLanguageTextItem(ConceptDS.QUANTITY_PERCENTAGE_OF, getConstants().conceptQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        ViewTextItem indexBaseType = new ViewTextItem(ConceptDS.QUANTITY_INDEX_BASE_TYPE, getConstants().conceptQuantityIndexMetadata());
        indexBaseType.setVisible(false);
        
        ViewTextItem indexBaseTypeText = new ViewTextItem(ConceptDS.QUANTITY_INDEX_BASE_TYPE_TEXT, getConstants().conceptQuantityIndexMetadata());
        indexBaseTypeText.setShowIfCondition(getIndexBaseTypeIfFunction());

        ViewTextItem baseValue = new ViewTextItem(ConceptDS.QUANTITY_BASE_VALUE, getConstants().conceptQuantityBaseValue());
        baseValue.setShowIfCondition(getBaseValueIfFunction());

        ViewTextItem baseTime = new ViewTextItem(ConceptDS.QUANTITY_BASE_TIME, getConstants().conceptQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());

        ViewTextItem baseLocation = new ViewTextItem(ConceptDS.QUANTITY_BASE_LOCATION, getConstants().conceptQuantityBaseLocation());
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());

        RelatedResourceLinkItem baseQuantity = new RelatedResourceLinkItem(ConceptDS.QUANTITY_BASE_QUANTITY, getConstants().conceptQuantityBaseQuantity(), getCustomLinkItemNavigationClickHandler());
        baseQuantity.setShowIfCondition(getBaseQuantityIfFunction());

        setFields(type, typeText, unit, unitSymbol, unitMultiplier, sigDigits, decPlaces, min, max, denominator, numerator, isPercentage, isPercentageText, percentageOf, indexBaseType, indexBaseTypeText, baseValue,
         baseTime, /*baseLocation, */baseQuantity);
    }
    
    public void setValue(QuantityDto quantityDto) {
        clearValues();
        if (quantityDto != null) {
            setValue(ConceptDS.QUANTITY_TYPE, quantityDto.getQuantityType() != null ? quantityDto.getQuantityType().toString() : "");
            setValue(ConceptDS.QUANTITY_TYPE_TEXT, quantityDto.getQuantityType() != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityDto.getQuantityType().toString()) : "");
            setRelatedResourceDtoValue(ConceptDS.QUANTITY_UNIT, quantityDto.getUnitCode());
            setValue(ConceptDS.QUANTITY_UNIT_SYMBOL_POSITION, quantityDto.getUnitSymbolPosition() != null ? getCoreMessages().getString(getCoreMessages().quantityUnitSymbolPositionEnum() + quantityDto.getUnitSymbolPosition().toString()) : "");
            setValue(ConceptDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier() != null ? quantityDto.getUnitMultiplier().toString() : "");
            setValue(ConceptDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits() != null ? quantityDto.getSignificantDigits().toString() : "");
            setValue(ConceptDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces().toString() : "");
            setValue(ConceptDS.QUANTITY_MINIMUM, quantityDto.getMinimum() != null ? quantityDto.getMinimum().toString() : "");
            setValue(ConceptDS.QUANTITY_MAXIMUM, quantityDto.getMaximum() != null ? quantityDto.getMaximum().toString() : "");

            setRelatedResourceDtoValue(ConceptDS.QUANTITY_DENOMINATOR, quantityDto.getDenominator());
            setRelatedResourceDtoValue(ConceptDS.QUANTITY_NUMERATOR, quantityDto.getNumerator());

            setValue(ConceptDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage() : false);
            setValue(ConceptDS.QUANTITY_IS_PERCENTAGE_TEXT, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants()
                    .no()) : "");
            QuantityIndexBaseTypeEnum baseType = getIndexBaseTypeEnum(quantityDto);
            setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE, baseType != null ? baseType.toString() : null);
            setValue(ConceptDS.QUANTITY_INDEX_BASE_TYPE_TEXT, getIndexBaseType(quantityDto));
            setValue(ConceptDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue() != null ? quantityDto.getBaseValue().toString() : "");
            setValue(ConceptDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());

            //FIXME: base_location
            //setValue(ConceptDS.QUANTITY_BASE_LOCATION, ""); // 
            

            setRelatedResourceDtoValue(ConceptDS.QUANTITY_BASE_QUANTITY, quantityDto.getBaseQuantity());

            setValue(ConceptDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setRelatedResourceDtoValue(String name, RelatedResourceDto relatedResourceDto) {
        ((RelatedResourceLinkItem)getField(name)).setRelatedResource(relatedResourceDto);
    }
    
}

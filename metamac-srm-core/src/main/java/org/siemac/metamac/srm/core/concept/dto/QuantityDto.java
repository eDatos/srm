package org.siemac.metamac.srm.core.concept.dto;

import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared.QuantityUtils;

/**
 * Dto for Quatity
 */
public class QuantityDto extends QuantityDtoBase {

    private static final long serialVersionUID = 1L;

    public QuantityDto() {
        // default values
        setUnitSymbolPosition(QuantityUnitSymbolPositionEnum.END);
        setUnitMultiplier(Integer.valueOf(1));
    }

    public Boolean isQuantityOrExtension(QuantityTypeEnum type) {
        return QuantityUtils.isQuantityOrExtension(getQuantityType());
    }

    public Boolean isAmountOrExtension(QuantityTypeEnum type) {
        return QuantityUtils.isAmountOrExtension(getQuantityType());
    }

    public Boolean isMagnituteOrExtension() {
        return QuantityUtils.isMagnitudeOrExtension(getQuantityType());
    }

    public Boolean isFractionOrExtension() {
        return QuantityUtils.isFractionOrExtension(getQuantityType());
    }

    public Boolean isRatioOrExtension() {
        return QuantityUtils.isRatioOrExtension(getQuantityType());
    }

    public Boolean isRateOrExtension() {
        return QuantityUtils.isRateOrExtension(getQuantityType());
    }

    public Boolean isIndexOrExtension() {
        return QuantityUtils.isIndexOrExtension(getQuantityType());
    }

    public Boolean isChangeRateOrExtension() {
        return QuantityUtils.isChangeRateOrExtension(getQuantityType());
    }
}

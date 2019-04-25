package org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared;

import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;

/**
 * Quantity: Quantity
 * Amount: extends Quantity
 * Magnitude: extends Quantity
 * Fraction: extends Magnitude
 * Ratio: extends Fraction
 * Rate: extends Ratio
 * Index: extends Rate
 * Change Rate: extends Rate
 */
public class QuantityUtils {

    private QuantityUtils() {
    }

    public static Boolean isQuantityOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.QUANTITY.equals(type) || isAmountOrExtension(type) || isMagnitudeOrExtension(type);
    }

    public static Boolean isAmountOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.AMOUNT.equals(type);
    }

    public static Boolean isMagnitudeOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.MAGNITUDE.equals(type) || isFractionOrExtension(type);
    }

    public static Boolean isFractionOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.FRACTION.equals(type) || isRatioOrExtension(type);
    }

    public static Boolean isRatioOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.RATIO.equals(type) || isRateOrExtension(type);
    }

    public static Boolean isRateOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.RATE.equals(type) || isChangeRateOrExtension(type) || isIndexOrExtension(type);
    }

    public static Boolean isIndexOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.INDEX.equals(type);
    }

    public static Boolean isChangeRateOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.CHANGE_RATE.equals(type);
    }

    public static boolean isPowerOfTen(Object value) {
        Boolean isPowerOfTen = Boolean.FALSE;

        if (value != null) {
            Integer intValue = null;
            if (value instanceof Integer) {
                intValue = (Integer) value;
            } else if (value instanceof String) {
                try {
                    intValue = Integer.parseInt(value.toString());
                } catch (Exception e) {
                    // NOTHING TO DO HERE!
                }
            }
            if ((intValue != null) && (intValue > 0)) {
                while (intValue > 9 && intValue % 10 == 0) {
                    intValue /= 10;
                }
                isPowerOfTen = intValue == 1;
            }
        } else {
            isPowerOfTen = Boolean.TRUE;
        }
        return isPowerOfTen;
    }
}

package org.siemac.metamac.srm.core.structure.serviceimpl.utils.shared;

import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;

public class DataStructureConstraintSharedValidator {

    public static boolean isSimpleDataType(FacetValueTypeEnum facetValueTypeEnum) {
        if (facetValueTypeEnum != null) {
            switch (facetValueTypeEnum) {
                case STRING_FVT:
                    return true;
                case ALPHA_FVT:
                    return true;
                case ALPHA_NUMERIC_FVT:
                    return true;
                case NUMERIC_FVT:
                    return true;
                case BIG_INTEGER_FVT:
                    return true;
                case INTEGER_FVT:
                    return true;
                case LONG_FVT:
                    return true;
                case SHORT_FVT:
                    return true;
                case DECIMAL_FVT:
                    return true;
                case FLOAT_FVT:
                    return true;
                case DOUBLE_FVT:
                    return true;
                case BOOLEAN_FVT:
                    return true;
                case URI_FVT:
                    return true;
                case COUNT_FVT:
                    return true;
                case INCLUSIVE_VALUE_RANGE_FVT:
                    return true;
                case EXCLUSIVE_VALUE_RANGE_FVT:
                    return true;
                case INCREMENTAL_FVT:
                    return true;
                case OBSERVATIONAL_TIME_PERIOD_FVT:
                    return true;
                case STANDARD_TIME_PERIOD_FVT:
                    return true;
                case BASIC_TIME_PERIOD_FVT:
                    return true;
                case GREGORIAN_TIME_PERIOD_FVT:
                    return true;
                case GREGORIAN_YEAR_FVT:
                    return true;
                case GREGORIAN_YEAR_MONTH_FVT:
                    return true;
                case GREGORIAN_DAY_FVT:
                    return true;
                case REPORTING_TIME_PERIOD_FVT:
                    return true;
                case REPORTING_YEAR_FVT:
                    return true;
                case REPORTING_SEMESTER_FVT:
                    return true;
                case REPORTING_TRIMESTER_FVT:
                    return true;
                case REPORTING_QUARTER_FVT:
                    return true;
                case REPORTING_MONTH_FVT:
                    return true;
                case REPORTING_WEEK_FVT:
                    return true;
                case REPORTING_DAY_FVT:
                    return true;
                case DATE_TIME_FVT:
                    return true;
                case TIMES_RANGE_FVT:
                    return true;
                case MONTH_FVT:
                    return true;
                case MONTH_DAY_FVT:
                    return true;
                case DAY_FVT:
                    return true;
                case TIME_FVT:
                    return true;
                case DURATION_FVT:
                    return true;
            }
        }
        return false;
    }

    public static boolean isTimeDataType(FacetValueTypeEnum facetValueTypeEnum) {
        if (facetValueTypeEnum != null) {
            switch (facetValueTypeEnum) {
                case OBSERVATIONAL_TIME_PERIOD_FVT:
                    return true;
                case STANDARD_TIME_PERIOD_FVT:
                    return true;
                case BASIC_TIME_PERIOD_FVT:
                    return true;
                case GREGORIAN_TIME_PERIOD_FVT:
                    return true;
                case GREGORIAN_YEAR_FVT:
                    return true;
                case GREGORIAN_YEAR_MONTH_FVT:
                    return true;
                case GREGORIAN_DAY_FVT:
                    return true;
                case REPORTING_TIME_PERIOD_FVT:
                    return true;
                case REPORTING_YEAR_FVT:
                    return true;
                case REPORTING_SEMESTER_FVT:
                    return true;
                case REPORTING_TRIMESTER_FVT:
                    return true;
                case REPORTING_QUARTER_FVT:
                    return true;
                case REPORTING_MONTH_FVT:
                    return true;
                case REPORTING_WEEK_FVT:
                    return true;
                case REPORTING_DAY_FVT:
                    return true;
                case DATE_TIME_FVT:
                    return true;
                case TIMES_RANGE_FVT:
                    return true;
            }
        }
        return false;
    }

}

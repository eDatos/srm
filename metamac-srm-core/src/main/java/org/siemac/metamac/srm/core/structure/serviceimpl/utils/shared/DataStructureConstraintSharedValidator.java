package org.siemac.metamac.srm.core.structure.serviceimpl.utils.shared;

import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.ALPHA_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.ALPHA_NUMERIC_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.BASIC_TIME_PERIOD_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.BIG_INTEGER_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.BOOLEAN_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.COUNT_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.DATE_TIME_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.DAY_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.DECIMAL_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.DOUBLE_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.DURATION_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.EXCLUSIVE_VALUE_RANGE_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.FLOAT_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.GREGORIAN_DAY_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.GREGORIAN_TIME_PERIOD_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.GREGORIAN_YEAR_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.GREGORIAN_YEAR_MONTH_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.INCLUSIVE_VALUE_RANGE_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.INCREMENTAL_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.INTEGER_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.LONG_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.MONTH_DAY_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.MONTH_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.NUMERIC_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.OBSERVATIONAL_TIME_PERIOD_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_DAY_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_MONTH_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_QUARTER_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_SEMESTER_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_TIME_PERIOD_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_TRIMESTER_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_WEEK_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.REPORTING_YEAR_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.SHORT_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.STANDARD_TIME_PERIOD_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.STRING_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.TIMES_RANGE_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.TIME_FVT;
import static org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum.URI_FVT;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;

public class DataStructureConstraintSharedValidator {

    public static final Set<FacetValueTypeEnum> simpleDataTypeValues = Collections.unmodifiableSet(new HashSet<FacetValueTypeEnum>(Arrays.asList(new FacetValueTypeEnum[]{STRING_FVT, ALPHA_FVT,
            ALPHA_NUMERIC_FVT, NUMERIC_FVT, BIG_INTEGER_FVT, INTEGER_FVT, LONG_FVT, SHORT_FVT, DECIMAL_FVT, FLOAT_FVT, DOUBLE_FVT, BOOLEAN_FVT, URI_FVT, COUNT_FVT, INCLUSIVE_VALUE_RANGE_FVT,
            EXCLUSIVE_VALUE_RANGE_FVT, INCREMENTAL_FVT, OBSERVATIONAL_TIME_PERIOD_FVT, STANDARD_TIME_PERIOD_FVT, BASIC_TIME_PERIOD_FVT, GREGORIAN_TIME_PERIOD_FVT, GREGORIAN_YEAR_FVT,
            GREGORIAN_YEAR_MONTH_FVT, GREGORIAN_DAY_FVT, REPORTING_TIME_PERIOD_FVT, REPORTING_YEAR_FVT, REPORTING_SEMESTER_FVT, REPORTING_TRIMESTER_FVT, REPORTING_QUARTER_FVT, REPORTING_MONTH_FVT,
            REPORTING_WEEK_FVT, REPORTING_DAY_FVT, DATE_TIME_FVT, TIMES_RANGE_FVT, MONTH_FVT, MONTH_DAY_FVT, DAY_FVT, TIME_FVT, DURATION_FVT})));

    public static final Set<FacetValueTypeEnum> timeDataTypeValues   = Collections.unmodifiableSet(new HashSet<FacetValueTypeEnum>(Arrays.asList(new FacetValueTypeEnum[]{
            OBSERVATIONAL_TIME_PERIOD_FVT, STANDARD_TIME_PERIOD_FVT, BASIC_TIME_PERIOD_FVT, GREGORIAN_TIME_PERIOD_FVT, GREGORIAN_YEAR_FVT, GREGORIAN_YEAR_MONTH_FVT, GREGORIAN_DAY_FVT,
            REPORTING_TIME_PERIOD_FVT, REPORTING_YEAR_FVT, REPORTING_SEMESTER_FVT, REPORTING_TRIMESTER_FVT, REPORTING_QUARTER_FVT, REPORTING_MONTH_FVT, REPORTING_WEEK_FVT, REPORTING_DAY_FVT,
            DATE_TIME_FVT, TIMES_RANGE_FVT                           })));

    public static boolean isSimpleDataType(FacetValueTypeEnum facetValueTypeEnum) {
        return facetValueTypeEnum != null && simpleDataTypeValues.contains(facetValueTypeEnum);
    }

    public static boolean isTimeDataType(FacetValueTypeEnum facetValueTypeEnum) {
        return facetValueTypeEnum != null && timeDataTypeValues.contains(facetValueTypeEnum);
    }

}

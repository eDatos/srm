package org.siemac.metamac.srm.core.service.utils;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.FacetValueTypeEnum;
import org.siemac.metamac.domain.srm.enume.domain.TypeRepresentationEnum;
import org.siemac.metamac.srm.core.error.MetamacCoreExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtil {

    private static Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    public static void validateRepresentationForDataAttribute(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        validateSimpleDataStructureRepresentationType(ctx, representationDto);
    }

    public static void validateRepresentationForReportingYearStartDayType(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return;
        }

        // The ReportingYearStartDayType must be nonEnumerated
        if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationDto.getNonEnumerated())) {
            // Minimum cardinality must be one
            if (representationDto.getNonEnumerated() == null) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "NonEnumerated");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
            // fixed="MonthDay"
            else if (!FacetValueTypeEnum.MONTH_DAY_FVT.equals(representationDto.getNonEnumerated().getFacetValue())) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, FacetValueTypeEnum.MONTH_DAY_FVT);
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
        } else if (TypeRepresentationEnum.ENUMERATED.equals(representationDto.getTypeRepresentationEnum())) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "RepresentationEnum");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }
    }

    public static void validateRepresentationForDimension(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        validateSimpleDataStructureRepresentationType(ctx, representationDto);
    }

    public static void validateRepresentationForPrimaryMeasure(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        validateSimpleDataStructureRepresentationType(ctx, representationDto);
    }

    public static void validateRepresentationForMeasureDimension(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return;
        }

        // The MeasureDimension must be enumerated and use a ConceptScheme
        if (TypeRepresentationEnum.ENUMERATED.equals(representationDto.getTypeRepresentationEnum())) {
            // Minimum cardinality must be one
            if (representationDto.getEnumerated() == null) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "Enumerated");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            } else if (!TypeExternalArtefactsEnum.CONCEPT_SCHEME.equals(representationDto.getEnumerated().getType())) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "EnumeratedConceptScheme");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
        } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationDto.getNonEnumerated())) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "Enumerated");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }
    }

    public static void validateRepresentationForTimeDimension(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return;
        }

        // Must be non Enumerated
        if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationDto.getTypeRepresentationEnum())) {

            // Minimum cardinality must be one
            if (representationDto.getNonEnumerated() == null) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "NonEnumerated");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }

            // Check for restrictions
            switch (representationDto.getNonEnumerated().getFacetValue()) {
                case OBSERVATIONAL_TIME_PERIOD_FVT:
                    // Is the default value
                    break;
                case STANDARD_TIME_PERIOD_FVT:
                    break;
                case BASIC_TIME_PERIOD_FVT:
                    break;
                case GREGORIAN_TIME_PERIOD_FVT:
                    break;
                case GREGORIAN_YEAR_FVT:
                    break;
                case GREGORIAN_YEAR_MONTH_FVT:
                    break;
                case GREGORIAN_DAY_FVT:
                    break;
                case REPORTING_TIME_PERIOD_FVT:
                    break;
                case REPORTING_YEAR_FVT:
                    break;
                case REPORTING_SEMESTER_FVT:
                    break;
                case REPORTING_TRIMESTER_FVT:
                    break;
                case REPORTING_QUARTER_FVT:
                    break;
                case REPORTING_MONTH_FVT:
                    break;
                case REPORTING_WEEK_FVT:
                    break;
                case REPORTING_DAY_FVT:
                    break;
                case DATE_TIME_FVT:
                    break;
                case TIMES_RANGE_FVT:
                    break;
                default:
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "FacetValueTypeEnum");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
            }

            // FacetType Restriction : Prohibited <xs:complexType name="TimeTextFormatType">
            if (representationDto.getNonEnumerated().getIsSequenceFT() != null || representationDto.getNonEnumerated().getInterval() != null
                    || representationDto.getNonEnumerated().getStartValueFT() != null || representationDto.getNonEnumerated().getEndValueFT() != null
                    || representationDto.getNonEnumerated().getTimeIntervalFT() != null || representationDto.getNonEnumerated().getMinLengthFT() != null
                    || representationDto.getNonEnumerated().getMaxLengthFT() != null || representationDto.getNonEnumerated().getMinValueFT() != null
                    || representationDto.getNonEnumerated().getMaxValueFT() != null || representationDto.getNonEnumerated().getDecimalsFT() != null
                    || representationDto.getNonEnumerated().getPatternFT() != null) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_FACETTYPE_PROHIBITED);
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }

        } else if (TypeRepresentationEnum.ENUMERATED.equals(representationDto.getTypeRepresentationEnum())) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "NonEnumerated");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }
    }

    /**************************************************************************
     * PRIVATES
     *************************************************************************/

    private static void validateSimpleDataStructureRepresentationType(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return;
        }

        if (TypeRepresentationEnum.ENUMERATED.equals(representationDto.getTypeRepresentationEnum())) {
            // minOccurs="1"
            // Enumeration:: common:CodelistReferenceType
            if (representationDto.getEnumerated() == null) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "Enumerated");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }

            // minOccurs="0"
            // EnumerationFormat:: CodededTextFormatType
            // TODO: METAMAC NOT SUPPORT THIS --> checkCodededTextFormatType(representationDto);
        } else if (TypeRepresentationEnum.TEXT_FORMAT.equals(representationDto.getTypeRepresentationEnum())) {
            checkSimpleDataType(ctx, representationDto);

            // FacetType Restriction : Prohibited <xs:complexType name="SimpleComponentTextFormatType">
            if (representationDto.getNonEnumerated() != null) {
                if (representationDto.getNonEnumerated().getIsMultiLingual() != null) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_FACETTYPE_PROHIBITED);
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
            }

        }
    }

    private static void checkCodededTextFormatType(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return;
        }

        checkCodeDataType(ctx, representationDto);

        // FacetType Restriction : Prohibited <xs:complexType name="TimeTextFormatType">
        if (representationDto.getNonEnumerated() != null) {
            if (representationDto.getNonEnumerated().getDecimalsFT() != null) {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_FACETTYPE_PROHIBITED);
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
        }
    }

    private static void checkCodeDataType(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return;
        }

        // Check for restrictions
        if (representationDto.getNonEnumerated() != null)
            switch (representationDto.getNonEnumerated().getFacetValue()) {
                case STRING_FVT:
                    break;
                case ALPHA_FVT:
                    break;
                case ALPHA_NUMERIC_FVT:
                    break;
                case NUMERIC_FVT:
                    break;
                case BIG_INTEGER_FVT:
                    break;
                case INTEGER_FVT:
                    break;
                case LONG_FVT:
                    break;
                case SHORT_FVT:
                    break;
                case BOOLEAN_FVT:
                    break;
                case URI_FVT:
                    break;
                case COUNT_FVT:
                    break;
                case INCLUSIVE_VALUE_RANGE_FVT:
                    break;
                case EXCLUSIVE_VALUE_RANGE_FVT:
                    break;
                case INCREMENTAL_FVT:
                    break;
                case OBSERVATIONAL_TIME_PERIOD_FVT:
                    break;
                case STANDARD_TIME_PERIOD_FVT:
                    break;
                case BASIC_TIME_PERIOD_FVT:
                    break;
                case GREGORIAN_TIME_PERIOD_FVT:
                    break;
                case GREGORIAN_YEAR_FVT:
                    break;
                case GREGORIAN_YEAR_MONTH_FVT:
                    break;
                case GREGORIAN_DAY_FVT:
                    break;
                case REPORTING_TIME_PERIOD_FVT:
                    break;
                case REPORTING_YEAR_FVT:
                    break;
                case REPORTING_SEMESTER_FVT:
                    break;
                case REPORTING_TRIMESTER_FVT:
                    break;
                case REPORTING_QUARTER_FVT:
                    break;
                case REPORTING_MONTH_FVT:
                    break;
                case REPORTING_WEEK_FVT:
                    break;
                case REPORTING_DAY_FVT:
                    break;
                case MONTH_FVT:
                    break;
                case MONTH_DAY_FVT:
                    break;
                case DAY_FVT:
                    break;
                case DURATION_FVT:
                    break;
                default:
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "FacetValueTypeEnum");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
            }
    }

    private static void checkSimpleDataType(ServiceContext ctx, RepresentationDto representationDto) throws MetamacException {
        // Check for restrictions
        if (representationDto.getNonEnumerated() != null)
            // FacetValueType Restriction : <xs:simpleType name="SimpleDataType">
            switch (representationDto.getNonEnumerated().getFacetValue()) {
                case STRING_FVT:
                    break;
                case ALPHA_FVT:
                    break;
                case ALPHA_NUMERIC_FVT:
                    break;
                case NUMERIC_FVT:
                    break;
                case BIG_INTEGER_FVT:
                    break;
                case INTEGER_FVT:
                    break;
                case LONG_FVT:
                    break;
                case SHORT_FVT:
                    break;
                case DECIMAL_FVT:
                    break;
                case FLOAT_FVT:
                    break;
                case DOUBLE_FVT:
                    break;
                case BOOLEAN_FVT:
                    break;
                case URI_FVT:
                    break;
                case COUNT_FVT:
                    break;
                case INCLUSIVE_VALUE_RANGE_FVT:
                    break;
                case EXCLUSIVE_VALUE_RANGE_FVT:
                    break;
                case INCREMENTAL_FVT:
                    break;
                case OBSERVATIONAL_TIME_PERIOD_FVT:
                    break;
                case STANDARD_TIME_PERIOD_FVT:
                    break;
                case BASIC_TIME_PERIOD_FVT:
                    break;
                case GREGORIAN_TIME_PERIOD_FVT:
                    break;
                case GREGORIAN_YEAR_FVT:
                    break;
                case GREGORIAN_YEAR_MONTH_FVT:
                    break;
                case GREGORIAN_DAY_FVT:
                    break;
                case REPORTING_TIME_PERIOD_FVT:
                    break;
                case REPORTING_YEAR_FVT:
                    break;
                case REPORTING_SEMESTER_FVT:
                    break;
                case REPORTING_TRIMESTER_FVT:
                    break;
                case REPORTING_QUARTER_FVT:
                    break;
                case REPORTING_MONTH_FVT:
                    break;
                case REPORTING_WEEK_FVT:
                    break;
                case REPORTING_DAY_FVT:
                    break;
                case DATE_TIME_FVT:
                    break;
                case TIMES_RANGE_FVT:
                    break;
                case MONTH_FVT:
                    break;
                case MONTH_DAY_FVT:
                    break;
                case DAY_FVT:
                    break;
                case TIME_FVT:
                    break;
                case DURATION_FVT:
                    break;
                default:
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "FacetValueTypeEnum");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
            }
    }

}

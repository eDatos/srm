package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Representation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.Facet;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

@Component
public class CommonDo2RestMapperV10Impl implements CommonDo2RestMapperV10 {

    @Autowired
    private CodesDo2RestMapperV10    codesDo2RestMapperV10;

    @Autowired
    private ConceptsDo2RestMapperV10 conceptsDo2RestMapperV10;

    @Override
    public Representation toRepresentation(com.arte.statistic.sdmx.srm.core.base.domain.Representation source) {
        if (source == null) {
            return null;
        }
        Representation target = new Representation();

        if (RepresentationTypeEnum.TEXT_FORMAT.equals(source.getRepresentationType())) {
            target.setTextFormat(toRepresentationTextFormat(source.getTextFormat()));
        } else if (RepresentationTypeEnum.ENUMERATION.equals(source.getRepresentationType())) {
            if (source.getEnumerationCodelist() != null) {
                target.setEnumerationCodelist(codesDo2RestMapperV10.toResource(source.getEnumerationCodelist()));
            } else if (source.getEnumerationConceptScheme() != null) {
                target.setEnumerationConceptScheme(conceptsDo2RestMapperV10.toResource(source.getEnumerationConceptScheme()));
            }
        }
        return target;
    }

    private TextFormat toRepresentationTextFormat(Facet source) {
        if (source == null) {
            return null;
        }

        TextFormat target = new TextFormat();
        target.setTextType(toDataType(source.getFacetValue()));
        target.setIsSequence(CoreCommonUtil.transformBooleanLexicalRepresentationToBoolean(source.getIsSequenceFT()));
        target.setInterval(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getIntervalFT()));
        target.setStartValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getStartValueFT()));
        target.setEndValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getEndValueFT()));
        target.setTimeInterval(CoreCommonUtil.transformLexicalRepresentationToDuration(source.getTimeIntervalFT()));
        target.setStartTime(source.getStartTimeFT());
        target.setEndTime(source.getEndTimeFT());
        target.setMinLength(CoreCommonUtil.integerLexicalRepresentation2BigInteger(source.getMinLengthFT()));
        target.setMaxLength(CoreCommonUtil.integerLexicalRepresentation2BigInteger(source.getMaxLengthFT()));
        target.setMinValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getMinValueFT()));
        target.setMaxValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getMaxValueFT()));
        target.setDecimals(CoreCommonUtil.integerLexicalRepresentation2BigInteger(source.getDecimalsFT()));
        target.setPattern(source.getPatternFT());
        target.setIsMultiLingual(CoreCommonUtil.transformBooleanLexicalRepresentationToBoolean(source.getIsMultiLingual()));
        return target;
    }
    private DataType toDataType(FacetValueTypeEnum source) {
        if (source == null) {
            return null;
        }
        return DataType.fromValue(source.getValue());
    }

}
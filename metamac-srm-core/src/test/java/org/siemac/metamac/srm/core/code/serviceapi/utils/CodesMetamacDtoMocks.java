package org.siemac.metamac.srm.core.code.serviceapi.utils;

import java.util.Date;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOpennessVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class CodesMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CODELISTS
    // -----------------------------------------------------------------------------------

    public static CodelistMetamacDto mockCodelistDto(String codeMaintainer, String urnMaintainer) {
        CodelistMetamacDto codelistDto = new CodelistMetamacDto();

        codelistDto.setShortName(MetamacMocks.mockInternationalStringDto());

        CodesDtoMocks.mockCodelistDto(codelistDto);

        codelistDto.setMaintainer(new RelatedResourceDto(codeMaintainer, urnMaintainer, RelatedResourceTypeEnum.AGENCY));

        return codelistDto;
    }

    public static RelatedResourceDto mockCodelistRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CODELIST);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // CODES
    // -----------------------------------------------------------------------------------

    public static CodeMetamacDto mockCodeDto() {
        CodeMetamacDto codeMetamacDto = new CodeMetamacDto();
        codeMetamacDto.setShortName(MetamacMocks.mockInternationalStringDto());
        CodesDtoMocks.mockCodeDto(codeMetamacDto);
        return codeMetamacDto;
    }

    // -----------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // -----------------------------------------------------------------------------------
    public static CodelistFamilyDto mockCodelistFamilyDto() {
        CodelistFamilyDto codelistFamilyDto = new CodelistFamilyDto();
        codelistFamilyDto.setCode("code-" + MetamacMocks.mockString(10));
        codelistFamilyDto.setName(MetamacMocks.mockInternationalStringDto());
        return codelistFamilyDto;
    }

    // -----------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // -----------------------------------------------------------------------------------
    public static VariableFamilyDto mockVariableFamilyDto() {
        VariableFamilyDto variableFamilyDto = new VariableFamilyDto();
        variableFamilyDto.setCode("code-" + MetamacMocks.mockString(10));
        variableFamilyDto.setName(MetamacMocks.mockInternationalStringDto());
        return variableFamilyDto;
    }

    public static RelatedResourceDto mockVariableFamilyRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // VARIABLES
    // -----------------------------------------------------------------------------------
    public static VariableDto mockVariableDto() {
        VariableDto variableDto = new VariableDto();
        variableDto.setCode("code-" + MetamacMocks.mockString(10));
        variableDto.setName(MetamacMocks.mockInternationalStringDto());
        variableDto.setShortName(MetamacMocks.mockInternationalStringDto("es", "shortName" + MetamacMocks.mockString(10)));
        variableDto.setValidFrom(new Date());
        variableDto.setValidTo(new Date());
        return variableDto;
    }

    public static RelatedResourceDto mockVariableRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // -----------------------------------------------------------------------------------
    public static VariableElementDto mockVariableElementDto() {
        VariableElementDto variableElementDto = new VariableElementDto();
        variableElementDto.setCode("code-" + MetamacMocks.mockString(10));
        variableElementDto.setName(MetamacMocks.mockInternationalStringDto());
        variableElementDto.setShortName(MetamacMocks.mockInternationalStringDto("es", "shortName" + MetamacMocks.mockString(10)));
        variableElementDto.setValidFrom(new Date());
        variableElementDto.setValidTo(new Date());
        return variableElementDto;
    }

    public static RelatedResourceDto mockVariableElementRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // ORDER VISUALISATIONS
    // -----------------------------------------------------------------------------------
    public static CodelistOrderVisualisationDto mockCodelistOrderVisualisationDto() {
        CodelistOrderVisualisationDto codelistOrderVisualisationDto = new CodelistOrderVisualisationDto();
        codelistOrderVisualisationDto.setCode("code-" + MetamacMocks.mockString(10));
        codelistOrderVisualisationDto.setName(MetamacMocks.mockInternationalStringDto());
        return codelistOrderVisualisationDto;
    }

    public static RelatedResourceDto mockCodelistOrderVisualisationRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // OPENNESS VISUALISATIONS
    // -----------------------------------------------------------------------------------
    public static CodelistOpennessVisualisationDto mockCodelistOpennessVisualisationDto() {
        CodelistOpennessVisualisationDto codelistOpennessVisualisationDto = new CodelistOpennessVisualisationDto();
        codelistOpennessVisualisationDto.setCode("code-" + MetamacMocks.mockString(10));
        codelistOpennessVisualisationDto.setName(MetamacMocks.mockInternationalStringDto());
        return codelistOpennessVisualisationDto;
    }

    public static RelatedResourceDto mockCodelistOpennessVisualisationRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }

}

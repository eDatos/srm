package org.siemac.metamac.srm.core.code.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CodesMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CODELISTS
    // -----------------------------------------------------------------------------------

    public static CodelistMetamacDto mockCodelistDto(String codeMaintainer, String urnMaintainer) {
        CodelistMetamacDto codelistDto = new CodelistMetamacDto();

        codelistDto.setShortName(MetamacMocks.mockInternationalStringDto());

        CodesDtoMocks.mockCodelistDto(codelistDto);

        codelistDto.setMaintainer(new RelatedResourceDto(codeMaintainer, urnMaintainer, TypeExternalArtefactsEnum.AGENCY));

        return codelistDto;
    }

    public static RelatedResourceDto mockCodelistRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CODELIST);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // CODES
    // -----------------------------------------------------------------------------------

    public static CodeMetamacDto mockCodeDto() {
        CodeMetamacDto codeMetamacDto = new CodeMetamacDto();
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
    // VARIABLES
    // -----------------------------------------------------------------------------------
    public static RelatedResourceDto mockVariableRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }
}

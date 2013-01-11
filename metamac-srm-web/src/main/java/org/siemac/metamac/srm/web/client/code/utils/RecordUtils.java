package org.siemac.metamac.srm.web.client.code.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.web.client.code.model.record.CodeRecord;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistFamilyRecord;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.client.code.model.record.VariableFamilyRecord;
import org.siemac.metamac.srm.web.client.code.model.record.VariableRecord;

public class RecordUtils {

    // CODELISTS

    public static CodelistRecord getCodelistRecord(CodelistMetamacDto codelistDto) {
        CodelistRecord record = new CodelistRecord(codelistDto.getId(), codelistDto.getCode(), getLocalisedString(codelistDto.getName()), getLocalisedString(codelistDto.getDescription()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(codelistDto.getLifeCycle().getProcStatus()), codelistDto.getVersionLogic(), codelistDto.getUrn(), codelistDto);
        return record;
    }

    // CODES

    public static CodeRecord getCodeRecord(CodeMetamacDto codeDto) {
        CodeRecord record = new CodeRecord(codeDto.getId(), codeDto.getCode(), getLocalisedString(codeDto.getName()), codeDto.getUrn(), getLocalisedString(codeDto.getDescription()), codeDto);
        return record;
    }

    // CODELIST FAMILIES

    public static CodelistFamilyRecord getCodelistFamilyRecord(CodelistFamilyDto codelistFamilyDto) {
        CodelistFamilyRecord record = new CodelistFamilyRecord(codelistFamilyDto.getId(), codelistFamilyDto.getCode(), getLocalisedString(codelistFamilyDto.getName()), codelistFamilyDto.getUrn(),
                codelistFamilyDto);
        return record;
    }

    // VARIABLE FAMILIES

    public static VariableFamilyRecord getVariableFamilyRecord(VariableFamilyDto variableFamilyDto) {
        VariableFamilyRecord record = new VariableFamilyRecord(variableFamilyDto.getId(), variableFamilyDto.getCode(), getLocalisedString(variableFamilyDto.getName()), variableFamilyDto.getUrn(),
                variableFamilyDto);
        return record;
    }

    // VARIABLES

    public static VariableRecord getVariableRecord(VariableDto variableDto) {
        VariableRecord record = new VariableRecord(variableDto.getId(), variableDto.getCode(), getLocalisedString(variableDto.getName()), variableDto.getUrn(), variableDto);
        return record;
    }
}

package org.siemac.metamac.srm.web.client.code.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.code.model.record.CodeRecord;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistFamilyRecord;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistRecord;

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
}
